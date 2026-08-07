package net.rebel459.drops_backport.mixin.entity;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.animal.wolf.Wolf;
import net.rebel459.drops_backport.DropsBackport;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(OwnerHurtByTargetGoal.class)
public abstract class OwnerHurtByTargetGoalMixin {

    @Shadow
    @Final
    private TamableAnimal tameAnimal;

    @Inject(
            method = "canUse",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/LivingEntity;getLastHurtByMob()Lnet/minecraft/world/entity/LivingEntity;",
                    shift = At.Shift.BEFORE
            ),
            cancellable = true
    )
    private void noWolfRetaliation(CallbackInfoReturnable<Boolean> cir) {
        DamageSource source = this.tameAnimal.getOwner().getLastDamageSource();
        if (this.tameAnimal instanceof Wolf && source != null && source.is(TagKey.create(Registries.DAMAGE_TYPE, DropsBackport.vanillaId("no_wolf_retaliation"))))
            cir.setReturnValue(false);
    }
}
