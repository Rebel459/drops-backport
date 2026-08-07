package net.rebel459.drops_backported.mixin.entity;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.rebel459.drops_backported.registry.DBAttributes;
import net.rebel459.drops_backported.util.block.AbstractBedBlock;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class NeoForgeLivingEntityMixin {

    @Shadow
    public abstract @Nullable AttributeInstance getAttribute(Holder<Attribute> attribute);

    @Redirect(
            method = "travelInAir",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;getFriction(Lnet/minecraft/world/level/LevelReader;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/entity/Entity;)F")
    )
    private float applyFrictionModifier(BlockState state, LevelReader levelReader, BlockPos blockPos, Entity entity) {
        return computeModifiedFriction(state.getFriction(levelReader, blockPos, entity), getAttributeValue(DBAttributes.FRICTION_MODIFIER, 1.0F));
    }

    @Unique
    private float getAttributeValue(Holder<Attribute> attribute, float fallback) {
        AttributeInstance instance = this.getAttribute(attribute);
        return instance == null ? fallback : (float) instance.getValue();
    }

    @Unique
    private static float computeModifiedFriction(float friction, float modifier) {
        return Mth.clamp(1.0F - (1.0F - friction) * modifier, 0.0F, 1.0F);
    }

    @Inject(method = "startSleeping", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;isBed(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/entity/LivingEntity;)Z"))
    private void startSleepingStraw(BlockPos bedPosition, CallbackInfo ci, @Local(name = "blockState") BlockState blockState) {
        if (blockState.getBlock() instanceof AbstractBedBlock) {
            LivingEntity.class.cast(this).level().setBlock(bedPosition, blockState.setValue(AbstractBedBlock.OCCUPIED, true), 3);
        }
    }

    @Inject(method = "lambda$stopSleeping$0", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;isBed(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/entity/LivingEntity;)Z"), cancellable = true)
    private void stopSleepingStraw(CallbackInfo ci, @Local(name = "state") BlockState state, @Local(argsOnly = true, name = "bedPosition") BlockPos bedPosition) {
        if (state.getBlock() instanceof AbstractBedBlock bed) {
            LivingEntity entity = LivingEntity.class.cast(this);
            Direction facing = state.getValue(AbstractBedBlock.FACING);
            entity.level().setBlock(bedPosition, state.setValue(AbstractBedBlock.OCCUPIED, false), 3);
            Vec3 standUp = AbstractBedBlock.findStandUpPosition(entity.getType(), entity.level(), bedPosition, facing, entity.getYRot()).orElseGet(() -> {
                BlockPos above = bedPosition.above();
                return new Vec3(above.getX() + 0.5, above.getY() + 0.1, above.getZ() + 0.5);
            });
            Vec3 lookDirection = Vec3.atBottomCenterOf(bedPosition).subtract(standUp).normalize();
            float yaw = (float) Mth.wrapDegrees(Mth.atan2(lookDirection.z, lookDirection.x) * 180.0F / (float) Math.PI - 90.0);
            entity.setPos(standUp.x, standUp.y, standUp.z);
            entity.setYRot(yaw);
            entity.setXRot(0.0F);
            bed.onStopSleeping(entity.level(), bedPosition);
        }
    }
}
