package net.rebel459.drops_backport.mixin.entity;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnderMan.class)
public abstract class EnderManMixin extends Monster {

    protected EnderManMixin(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "hurtServer", at = @At("HEAD"), cancellable = true)
    private void projectilesHitPassengerEndermen(ServerLevel level, DamageSource source, float damage, CallbackInfoReturnable<Boolean> cir) {
        if (!this.isInvulnerableTo(level, source) && this.isPassenger()) {
            cir.setReturnValue(super.hurtServer(level, source, damage));
        }
    }

    @WrapOperation(method = "teleport(DDD)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;blocksMotion()Z", ordinal = 1))
    private boolean noBedrock(BlockState state, Operation<Boolean> original) {
        return original.call(state) && state.getBlock() != Blocks.BEDROCK;
    }
}
