package net.rebel459.drops_backport.item;

import net.minecraft.ChatFormatting;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.RegistryOps;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.rebel459.drops_backport.entity.cube.SulfurCube;
import net.rebel459.drops_backport.registry.DBEntityTypes;
import org.jspecify.annotations.Nullable;

import java.util.Optional;
import java.util.function.Consumer;

public class SulfurCubeBucketItem extends Item {
    private final SoundEvent emptySound;

    public SulfurCubeBucketItem(SoundEvent emptySound, Properties properties) {
        super(properties);
        this.emptySound = emptySound;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay tooltipDisplay, Consumer<Component> tooltip, TooltipFlag tooltipFlag) {
        if (hasBucketedCustomName(stack)) {
            return;
        }

        getBucketedBodyItem(stack, context.registries())
                .map(ItemStack::getHoverName)
                .map(component -> component.copy().withStyle(ChatFormatting.GRAY))
                .ifPresent(tooltip);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        BlockHitResult hitResult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.NONE);
        if (hitResult.getType() == HitResult.Type.MISS) {
            return InteractionResult.PASS;
        }

        if (hitResult.getType() != HitResult.Type.BLOCK) {
            return InteractionResult.PASS;
        }

        BlockPos clickedPos = hitResult.getBlockPos();
        BlockPos spawnPos = clickedPos.relative(hitResult.getDirection());
        if (!level.mayInteract(player, clickedPos) || !player.mayUseItemAt(spawnPos, hitResult.getDirection(), stack)) {
            return InteractionResult.FAIL;
        }

        if (level instanceof ServerLevel serverLevel && !this.placeSulfurCube(player, serverLevel, stack, spawnPos)) {
            return InteractionResult.FAIL;
        }
        if (player instanceof ServerPlayer serverPlayer) {
            CriteriaTriggers.PLACED_BLOCK.trigger(serverPlayer, spawnPos, stack);
        }

        player.awardStat(Stats.ITEM_USED.get(this));
        ItemStack result = ItemUtils.createFilledResult(stack, player, BucketItem.getEmptySuccessItem(stack, player));
        return InteractionResult.SUCCESS.heldItemTransformedTo(result);
    }

    public boolean placeSulfurCube(@Nullable LivingEntity user, ServerLevel level, ItemStack stack, BlockPos pos) {
        if (!this.spawn(level, stack, pos)) {
            return false;
        }

        level.playSound(user, pos, this.emptySound, SoundSource.NEUTRAL, 1.0F, 1.0F);
        level.gameEvent(user, GameEvent.ENTITY_PLACE, pos);
        return true;
    }

    private boolean spawn(ServerLevel level, ItemStack stack, BlockPos pos) {
        Entity entity = DBEntityTypes.SULFUR_CUBE.get().create(
                level,
                EntityType.createDefaultStackConfig(level, stack, null),
                pos,
                EntitySpawnReason.BUCKET,
                true,
                false
        );
        if (!(entity instanceof SulfurCube sulfurCube)) {
            return false;
        }

        CustomData bucketData = stack.getOrDefault(DataComponents.BUCKET_ENTITY_DATA, CustomData.EMPTY);
        sulfurCube.loadFromBucketTag(bucketData.copyTag());
        sulfurCube.setFromBucket(true);
        level.addFreshEntityWithPassengers(sulfurCube);
        sulfurCube.playAmbientSound();
        return true;
    }

    public static Optional<InteractionResult> bucketMobPickup(Player player, InteractionHand hand, SulfurCube sulfurCube) {
        ItemStack heldItem = player.getItemInHand(hand);
        if (!heldItem.is(Items.BUCKET) || !sulfurCube.isAlive()) {
            return Optional.empty();
        }

        sulfurCube.playSound(sulfurCube.getPickupSound(), 1.0F, 1.0F);
        ItemStack bucket = sulfurCube.getBucketItemStack();
        sulfurCube.saveToBucketTag(bucket);
        ItemStack result = ItemUtils.createFilledResult(heldItem, player, bucket, false);
        player.setItemInHand(hand, result);
        if (!sulfurCube.level().isClientSide()) {
            CriteriaTriggers.FILLED_BUCKET.trigger((ServerPlayer) player, bucket);
        }

        sulfurCube.discard();
        return Optional.of(InteractionResult.SUCCESS);
    }

    private static boolean hasBucketedCustomName(ItemStack stack) {
        return stack.getOrDefault(DataComponents.BUCKET_ENTITY_DATA, CustomData.EMPTY)
                .copyTag()
                .contains(SulfurCube.BUCKET_CUSTOM_NAME_TAG);
    }

    private static Optional<ItemStack> getBucketedBodyItem(ItemStack stack, HolderLookup.Provider registries) {
        Tag bodyItem = stack.getOrDefault(DataComponents.BUCKET_ENTITY_DATA, CustomData.EMPTY)
                .copyTag()
                .get(SulfurCube.BUCKET_BODY_ITEM_TAG);
        if (bodyItem == null) {
            return Optional.empty();
        }

        return ItemStack.CODEC.parse(RegistryOps.create(NbtOps.INSTANCE, registries), bodyItem)
                .result()
                .filter(bodyStack -> !bodyStack.isEmpty());
    }

}
