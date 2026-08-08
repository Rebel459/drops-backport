package net.rebel459.drops_backport.mixin.level;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.vibrations.VibrationSystem;
import net.rebel459.drops_backport.registry.DBGameEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(VibrationSystem.class)
public interface VibrationSystemMixin {

    @Inject(
            method = "getGameEventFrequency(Lnet/minecraft/resources/ResourceKey;)I",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void addBounceVibrationFrequency(ResourceKey<GameEvent> event, CallbackInfoReturnable<Integer> cir) {
        if (event.equals(DBGameEvents.BOUNCE.unwrapKey().get())) cir.setReturnValue(2);
    }
}