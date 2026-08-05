package net.rebel459.drops_backported.mixin.entity;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RangedAttribute.class)
public abstract class RangedAttributeMixin {
    @Inject(method = "sanitizeValue", at = @At("HEAD"), cancellable = true)
    private void allowNegativeKnockbackResistance(double value, CallbackInfoReturnable<Double> cir) {
        if (RangedAttribute.class.cast(this) == Attributes.KNOCKBACK_RESISTANCE.value()) {
            cir.setReturnValue(Double.isNaN(value) ? -2.0 : Mth.clamp(value, -2.0, 1.0));
        }
    }
}
