package net.rebel459.drops_backport.mixin.worldgen;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.rebel459.drops_backport.worldgen.DBSurfaceRules;
import net.rebel459.unified.platform.UnifiedPlatform;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(NoiseGeneratorSettings.class)
public class NoiseGeneratorSettingsMixin {

    @ModifyReturnValue(method = "surfaceRule", at = @At("RETURN"))
    private SurfaceRules.RuleSource addSurfaceRules(SurfaceRules.RuleSource original) {
        if (!UnifiedPlatform.isModLoaded("lithostitched")) return
                SurfaceRules.sequence(
                        DBSurfaceRules.belowSurface(),
                        SurfaceRules.ifTrue(
                                SurfaceRules.abovePreliminarySurface(),
                                DBSurfaceRules.aboveSurface()
                        ),
                        original
                );
        else return original;
    }
}
