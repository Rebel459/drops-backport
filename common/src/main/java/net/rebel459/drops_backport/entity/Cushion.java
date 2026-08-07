package net.rebel459.drops_backport.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.SynchedEntityData.Builder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.decoration.BlockAttachedEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.rebel459.drops_backport.registry.DBItems;
import net.rebel459.drops_backport.sound.DBSoundEvents;
import org.jspecify.annotations.Nullable;

public class Cushion extends BlockAttachedEntity {
    private static final DyeColor DEFAULT_COLOR = DyeColor.WHITE;
    private static final int LIGHTNING_DROP_INVULNERABLE_TICKS = 20;
    private static final EntityDataAccessor<Integer> DATA_COLOR = SynchedEntityData.defineId(Cushion.class, EntityDataSerializers.INT);

    public Cushion(final EntityType<Cushion> type, final Level level) {
        super(type, level);
    }

    public DyeColor getColor() {
        return DyeColor.byId(this.entityData.get(DATA_COLOR));
    }

    public void setColor(final DyeColor color) {
        this.entityData.set(DATA_COLOR, color.getId());
    }

    int invulnerableTicks = 0;

    @Override
    public void dropItem(final ServerLevel level, final @Nullable Entity causedBy) {
        this.playSound(DBSoundEvents.CUSHION_BREAK.get(), 1.0F, 1.0F);
        this.showBreakingParticles();
        if (level.getGameRules().get(GameRules.ENTITY_DROPS)) {
            if (!(causedBy instanceof Player player && player.hasInfiniteMaterials())) {
                ItemEntity itemEntity = this.spawnAtLocation(level, this.getCushionItemStackWithData());
                if (itemEntity != null && causedBy instanceof LightningBolt) {
                    itemEntity.setInvulnerable(true);
                    this.invulnerableTicks = 20;
                }
            }
        }
    }

    @Override
    public InteractionResult interact(final Player player, final InteractionHand hand, final Vec3 location) {
        if (player.isSecondaryUseActive() || this.isVehicle()) {
            return InteractionResult.PASS;
        } else if (!this.level().isClientSide() && player.startRiding(this)) {
            this.playSound(DBSoundEvents.CUSHION_SIT.get(), 1.0F, 1.0F);
            return InteractionResult.SUCCESS_SERVER;
        } else {
            return InteractionResult.CONSUME;
        }
    }

    @Override
    protected void removePassenger(final Entity passenger) {
        super.removePassenger(passenger);
        if (!this.level().isClientSide() && this.getRemovalReason() == null) {
            this.playSound(DBSoundEvents.CUSHION_GET_UP.get(), 1.0F, 1.0F);
        }
    }

    @Override
    public ItemStack getPickResult() {
        return new ItemStack(DBItems.CUSHION.getItemFromDye(this.getColor()).get());
    }

    @Override
    public void tick() {
        if (this.level() instanceof ServerLevel level) {
            BlockPos blockPos = this.blockPosition();
            FluidState fluidState = level.getBlockState(blockPos).getFluidState();
            if (this.collidedWithFluid(fluidState, blockPos, this.position(), this.position())) {
                fluidState.entityInside(level, blockPos, this, this.insideEffectCollector);
                this.insideEffectCollector.applyAndClear(this);
            }

            this.destroyIfInFire(level);

            if (this.isInvulnerable()) {
                invulnerableTicks--;
                if (invulnerableTicks <= 0) this.setInvulnerable(false);
            }
        }
    }

    public void destroyIfInFire(final ServerLevel level) {
        if (!this.isRemoved()) {
            for (BlockPos blockPos : BlockPos.betweenClosed(nextDeflated(this.getBoundingBox()))) {
                if (level.getBlockState(blockPos).is(BlockTags.FIRE)) {
                    this.hurtServer(level, this.damageSources().inFire(), 1.0F);
                    break;
                }
            }
        }
    }

    @Override
    public void thunderHit(final ServerLevel level, final LightningBolt lightningBolt) {
        if (!this.isRemoved()) {
            this.kill(level);
            this.dropItem(level, lightningBolt);
        }
    }

    @Override
    public void setPos(final double x, final double y, final double z) {
        this.setPosRaw(x, y, z);
        super.setPos(x, y, z);
    }

    private void showBreakingParticles() {
        if (this.level() instanceof ServerLevel level) {
            level.sendParticles(
                    new BlockParticleOption(ParticleTypes.BLOCK, getWoolBlock(this.getColor()).defaultBlockState()),
                    this.getX(),
                    this.getY(0.6666666666666666),
                    this.getZ(),
                    10,
                    this.getBbWidth() / 4.0F,
                    this.getBbHeight() / 4.0F,
                    this.getBbWidth() / 4.0F,
                    0.05
            );
        }
    }

    public static boolean canBePlacedAt(final Level level, final AABB boundingBox) {
        return wouldSurviveAt(level, boundingBox) && !isAnchorBuried(level, boundingBox);
    }

    public static boolean wouldSurviveAt(final Level level, final AABB boundingBox) {
        return hasAnchorBelow(level, boundingBox) && !isCoveredByFullBlocks(level, boundingBox);
    }

    private static boolean hasAnchorBelow(final Level level, final AABB boundingBox) {
        AABB anchorBox = new AABB(
                boundingBox.minX, boundingBox.minY - 0.015625, boundingBox.minZ, Math.nextDown(boundingBox.maxX), boundingBox.minY, Math.nextDown(boundingBox.maxZ)
        );
        for (BlockPos blockPos : BlockPos.betweenClosed(anchorBox.expandTowards(0.0, -0.125, 0.0))) {
            BlockState blockState = level.getBlockState(blockPos);
            VoxelShape shape = blockState.getShape(level, blockPos);
            if (!shape.isEmpty() && shape.bounds().move(blockPos).intersects(anchorBox)) {
                return true;
            }
        }

        return false;
    }

    private static boolean isAnchorBuried(final Level level, final AABB boundingBox) {
        AABB restingSlice = new AABB(boundingBox.minX, boundingBox.minY, boundingBox.minZ, boundingBox.maxX, boundingBox.minY + 0.015625, boundingBox.maxZ)
                .deflate(1.0E-7);
        VoxelShape exposedSurface = Shapes.create(restingSlice);

        for (VoxelShape collider : level.getBlockCollisions(null, restingSlice)) {
            exposedSurface = Shapes.join(exposedSurface, collider, BooleanOp.ONLY_FIRST);
            if (exposedSurface.isEmpty()) {
                return true;
            }
        }

        return false;
    }

    private static boolean isCoveredByFullBlocks(final Level level, final AABB boundingBox) {
        for (BlockPos blockPos : BlockPos.betweenClosed(nextDeflated(boundingBox))) {
            if (!level.getBlockState(blockPos).isCollisionShapeFullBlock(level, blockPos)) {
                return false;
            }
        }

        return true;
    }

    private static AABB nextDeflated(final AABB boundingBox) {
        return boundingBox.deflate(1.0E-7);
    }

    private static Block getWoolBlock(final DyeColor color) {
        return switch (color) {
            case WHITE -> Blocks.WHITE_WOOL;
            case ORANGE -> Blocks.ORANGE_WOOL;
            case MAGENTA -> Blocks.MAGENTA_WOOL;
            case LIGHT_BLUE -> Blocks.LIGHT_BLUE_WOOL;
            case YELLOW -> Blocks.YELLOW_WOOL;
            case LIME -> Blocks.LIME_WOOL;
            case PINK -> Blocks.PINK_WOOL;
            case GRAY -> Blocks.GRAY_WOOL;
            case LIGHT_GRAY -> Blocks.LIGHT_GRAY_WOOL;
            case CYAN -> Blocks.CYAN_WOOL;
            case PURPLE -> Blocks.PURPLE_WOOL;
            case BLUE -> Blocks.BLUE_WOOL;
            case BROWN -> Blocks.BROWN_WOOL;
            case GREEN -> Blocks.GREEN_WOOL;
            case RED -> Blocks.RED_WOOL;
            case BLACK -> Blocks.BLACK_WOOL;
        };
    }

    @Override
    public boolean survives() {
        return wouldSurviveAt(this.level(), this.getBoundingBox());
    }

    @Override
    protected void recalculateBoundingBox() {
        this.setBoundingBox(this.makeBoundingBox());
    }

    @Override
    protected void defineSynchedData(final Builder entityData) {
        entityData.define(DATA_COLOR, DEFAULT_COLOR.getId());
    }

    @Override
    protected void addAdditionalSaveData(final ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.store("color", DyeColor.CODEC, this.getColor());
    }

    @Override
    protected void readAdditionalSaveData(final ValueInput input) {
        super.readAdditionalSaveData(input);
        this.setColor(input.read("color", DyeColor.CODEC).orElse(DEFAULT_COLOR));
    }

    private ItemStack getCushionItemStackWithData() {
        ItemStack itemStack = new ItemStack(DBItems.CUSHION.getItemFromDye(this.getColor()).get());
        if (this.hasCustomName()) {
            itemStack.set(DataComponents.CUSTOM_NAME, this.getCustomName());
        }
        return itemStack;
    }
}
