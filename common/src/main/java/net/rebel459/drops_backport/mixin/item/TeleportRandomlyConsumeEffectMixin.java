package net.rebel459.drops_backport.mixin.item;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.consume_effects.TeleportRandomlyConsumeEffect;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.rebel459.drops_backport.network.TeleportTrailPacket;
import net.rebel459.drops_backport.tag.DBItemTags;
import net.rebel459.unified.platform.UnifiedHelpers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TeleportRandomlyConsumeEffect.class)
public abstract class TeleportRandomlyConsumeEffectMixin {

    @Inject(method = "apply", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;playSound(Lnet/minecraft/world/entity/Entity;DDDLnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;)V", shift =  At.Shift.AFTER))
    private void directionalTeleportParticles(Level level, ItemStack stack, LivingEntity user, CallbackInfoReturnable<Boolean> cir, @Local(name = "oldPos") Vec3 oldPos) {
        if (stack.is(DBItemTags.HAS_TELEPORT_TRAIL)) {
            BlockPos origin = BlockPos.containing(oldPos);
            BlockPos target = user.blockPosition();

            TeleportTrailPacket packet = new TeleportTrailPacket(origin, target);
            if (level instanceof ServerLevel serverLevel) {
                for (ServerPlayer player : serverLevel.players()) {
                    if (player.distanceToSqr(Vec3.atCenterOf(origin)) <= 64 * 64 || player.distanceToSqr(Vec3.atCenterOf(target)) <= 64 * 64) {
                        UnifiedHelpers.NETWORKING.send(packet, player);
                    }
                }
            }
        }
    }
}
