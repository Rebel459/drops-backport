package net.rebel459.drops_backport.mixin.entity;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.rebel459.drops_backport.entity.SulfurCube;
import net.rebel459.drops_backport.registry.DBAttributes;
import net.rebel459.drops_backport.util.block.AbstractBedBlock;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @Shadow
    public abstract @Nullable AttributeInstance getAttribute(Holder<Attribute> attribute);

    @Shadow
    public abstract Optional<BlockPos> getSleepingPos();

    @ModifyConstant(
            method = "travelInAir",
            constant = @Constant(floatValue = 0.91F)
    )
    private float applyHorizontalAirDragModifier(float friction) {
        return computeModifiedFriction(friction, getAttributeValue(DBAttributes.AIR_DRAG_MODIFIER, 1.0F));
    }

    @ModifyConstant(method = "travelInAir", constant = @Constant(floatValue = 0.98F))
    private float applyVerticalAirDragModifier(float friction) {
        if (LivingEntity.class.cast(this) instanceof SulfurCube cube && cube.hasBodyItem()) friction = 0.91F;
        return computeModifiedFriction(friction, getAttributeValue(DBAttributes.AIR_DRAG_MODIFIER, 1.0F));
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

    @ModifyReturnValue(method = "createLivingAttributes", at = @At("RETURN"))
    private static AttributeSupplier.Builder addDBAttributes(AttributeSupplier.Builder builder) {
        return builder
                .add(DBAttributes.AIR_DRAG_MODIFIER)
                .add(DBAttributes.FRICTION_MODIFIER)
                .add(DBAttributes.BOUNCINESS);
    }

    @Inject(method = "checkBedExists", at = @At(value = "RETURN"), cancellable = true)
    private void checkBedExistsStraw(CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValue()) {
            boolean hasStrawBed = this.getSleepingPos().map(bedPosition -> LivingEntity.class.cast(this).level().getBlockState(bedPosition).getBlock() instanceof AbstractBedBlock).orElse(false);
            if (hasStrawBed) cir.setReturnValue(true);
        }
    }

    @Inject(method = "getBedOrientation", at = @At(value = "TAIL"), cancellable = true)
    private void getBedOrientationStraw(CallbackInfoReturnable<Direction> cir, @Local(name = "bedPos") BlockPos bedPos) {
        if (cir.getReturnValue() == null) {
            Direction facing = bedPos != null ? AbstractBedBlock.getBedOrientation(LivingEntity.class.cast(this).level(), bedPos) : null;
            if (facing != null) cir.setReturnValue(facing);
        }
    }
}
