package net.rebel459.drops_backported.mixin.entity;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.core.Holder;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.level.block.Block;
import net.rebel459.drops_backported.entity.sulfur_cube.SulfurCube;
import net.rebel459.drops_backported.registry.DBAttributes;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @Shadow
    public abstract @Nullable AttributeInstance getAttribute(Holder<Attribute> attribute);

    @Redirect(
            method = "travelInAir",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Block;getFriction()F")
    )
    private float applyFrictionModifier(Block block) {
        return computeModifiedFriction(block.getFriction(), getAttributeValue(DBAttributes.FRICTION_MODIFIER, 1.0F));
    }

    @ModifyConstant(method = "travelInAir", constant = @Constant(floatValue = 0.91F))
    private float applyHorizontalAirDragModifier(float friction) {
        return computeModifiedFriction(friction, getAttributeValue(DBAttributes.AIR_DRAG_MODIFIER, 1.0F));
    }

    @ModifyConstant(method = "travelInAir", constant = @Constant(floatValue = 0.98F))
    private float applyVerticalAirDragModifier(float friction) {
        if (LivingEntity.class.cast(this) instanceof SulfurCube cube && cube.hasBodyItem()) {
            friction = 0.91F;
        }

        return computeModifiedFriction(friction, getAttributeValue(DBAttributes.AIR_DRAG_MODIFIER, 1.0F));
    }

    @ModifyArg(
            method = "travelInAir",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/LivingEntity;handleRelativeFrictionAndCalculateMovement(Lnet/minecraft/world/phys/Vec3;F)Lnet/minecraft/world/phys/Vec3;"
            ),
            index = 1
    )
    private float applyLowFrictionInputRule(float blockFriction) {
        return blockFriction > 0.6F ? blockFriction : 0.6F;
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
}
