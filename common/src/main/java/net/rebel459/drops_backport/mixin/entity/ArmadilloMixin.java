package net.rebel459.drops_backport.mixin.entity;

import net.minecraft.world.entity.animal.armadillo.Armadillo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Armadillo.class)
public abstract class ArmadilloMixin {

    @Inject(method = "rollUp", at = @At("HEAD"), cancellable = true)
    private void preventLiquidRollingUp(CallbackInfo ci) {
        Armadillo armadillo = Armadillo.class.cast(this);
        if (armadillo.isInLiquid()) ci.cancel();
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void unrollInLiquid(CallbackInfo ci) {
        Armadillo armadillo = Armadillo.class.cast(this);
        if (armadillo.isInLiquid() && armadillo.isScared()) armadillo.rollOut();
    }
}