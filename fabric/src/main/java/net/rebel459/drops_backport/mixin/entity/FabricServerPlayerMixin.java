package net.rebel459.drops_backport.mixin.entity;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.rebel459.drops_backport.util.block.AbstractBedBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ServerPlayer.class)
public abstract class FabricServerPlayerMixin {

    @Shadow
    public abstract ServerLevel level();

    @WrapOperation(method = "startSleepInBed", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;setRespawnPosition(Lnet/minecraft/server/level/ServerPlayer$RespawnConfig;Z)V"))
    private void startSleepInBedStraw(ServerPlayer player, ServerPlayer.RespawnConfig respawnConfig, boolean showMessage, Operation<Void> original, @Local(argsOnly = true) BlockPos pos) {
        if (this.level().getBlockState(pos).getBlock() instanceof AbstractBedBlock bed) {
            if (!bed.canSetSpawn()) return;
        }
        original.call(player, respawnConfig, showMessage);
    }
}
