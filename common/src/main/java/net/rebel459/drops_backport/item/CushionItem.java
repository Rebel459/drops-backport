package net.rebel459.drops_backport.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.rebel459.drops_backport.entity.Cushion;
import net.rebel459.drops_backport.registry.DBEntityTypes;
import net.rebel459.drops_backport.registry.DBItems;
import net.rebel459.drops_backport.sound.DBSoundEvents;
import net.rebel459.drops_backport.tag.DBBlockTags;

import java.util.function.Consumer;

public class CushionItem extends Item {
    private static final double COLLISION_SHAPE_RAYCAST_EPSILON = 0.001;

    public CushionItem(final Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(final UseOnContext context) {
        UseOnContext recalculatedContext = recalculateContextForSpecialCollisionShapes(context);
        Level level = context.getLevel();
        Direction clickedFace = recalculatedContext.getClickedFace();
        if (clickedFace != Direction.UP) {
            return InteractionResult.FAIL;
        } else {
            BlockPlaceContext placeContext = new BlockPlaceContext(recalculatedContext);
            BlockPos blockPos = placeContext.getClickedPos();
            Vec3 entityPos = new Vec3(blockPos.getX() + 0.5, recalculatedContext.getClickLocation().y, blockPos.getZ() + 0.5);
            AABB spawnAABB = DBEntityTypes.CUSHION.get().getSpawnAABB(entityPos.x, entityPos.y, entityPos.z);
            if (!Cushion.canBePlacedAt(level, spawnAABB)) {
                return InteractionResult.FAIL;
            } else {
                ItemStack itemStack = context.getItemInHand();
                if (level instanceof ServerLevel serverLevel) {
                    if (!serverLevel.getEntitiesOfClass(Cushion.class, spawnAABB).isEmpty()) {
                        return InteractionResult.FAIL;
                    }

                    Consumer<Cushion> entityConfig = EntityType.createDefaultStackConfig(serverLevel, itemStack, context.getPlayer());
                    Cushion cushion = DBEntityTypes.CUSHION.get().create(serverLevel, entityConfig, blockPos, EntitySpawnReason.SPAWN_ITEM_USE, true, true);
                    if (cushion == null) {
                        return InteractionResult.FAIL;
                    }

                    cushion.setColor(getCushionColor(itemStack));
                    cushion.snapTo(entityPos, Direction.fromYRot(placeContext.getRotation()).toYRot(), 0.0F);
                    serverLevel.addFreshEntity(cushion);
                    cushion.destroyIfInFire(serverLevel);
                    level.playSound(null, cushion.getX(), cushion.getY(), cushion.getZ(), DBSoundEvents.CUSHION_PLACE.get(), SoundSource.BLOCKS, 0.75F, 0.8F);
                    cushion.gameEvent(GameEvent.ENTITY_PLACE);
                    itemStack.consume(1, placeContext.getPlayer());
                }

                return InteractionResult.SUCCESS;
            }
        }
    }

    private static DyeColor getCushionColor(final ItemStack itemStack) {

        for (var suppliedItem : DBItems.CUSHION.getRegisteredItems()) {
            if (itemStack.is(suppliedItem.get())) {
                return DBItems.CUSHION.getDyeFromItem(suppliedItem);
            }
        }

        return DyeColor.WHITE;
    }

    private static UseOnContext recalculateContextForSpecialCollisionShapes(final UseOnContext context) {
        Player player = context.getPlayer();
        if (player == null) {
            return context;
        } else {
            Level level = context.getLevel();
            BlockPos clickedPos = context.getClickedPos();
            BlockState clickedState = level.getBlockState(clickedPos);
            if (!clickedState.is(DBBlockTags.CUSHION_USES_COLLISION_SHAPE)) {
                return context;
            } else {
                Vec3 rayFrom = player.getEyePosition();
                Vec3 ray = context.getClickLocation().subtract(rayFrom);
                Vec3 rayTo = context.getClickLocation().add(ray.normalize().scale(0.001));
                BlockHitResult collisionHitResult = clickedState.getCollisionShape(level, clickedPos).clip(rayFrom, rayTo, clickedPos);
                return collisionHitResult == null ? context : new UseOnContext(player, context.getHand(), collisionHitResult);
            }
        }
    }
}
