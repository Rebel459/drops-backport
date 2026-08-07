package net.rebel459.drops_backported.mixin.integration.lithostitched;

import com.mojang.datafixers.util.Pair;
import dev.worldgen.lithostitched.api.event.AddBiomeInjectorsEvent;
import dev.worldgen.lithostitched.api.event.AddWorldgenModifiersEvent;
import dev.worldgen.lithostitched.api.util.InjectionType;
import dev.worldgen.lithostitched.api.worldgen.biomeinjector.BiomeInjector;
import dev.worldgen.lithostitched.api.worldgen.modifier.WorldgenModifier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.rebel459.drops_backported.DropsBackported;
import net.rebel459.drops_backported.registry.DBBiomes;
import net.rebel459.drops_backported.worldgen.DBSurfaceRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(DropsBackported.class)
public class LithostitchedMixin {

    @Inject(method = "init", at = @At("TAIL"))
    private static void useLithostitched(CallbackInfo ci) {
        AddWorldgenModifiersEvent.EVENT.register((registry, consumer) -> {
            consumer.accept(
                    DropsBackported.modId("surface_rules"),
                    WorldgenModifier.builder().addSurfaceRule(Level.OVERWORLD, InjectionType.PREPEND,
                            DBSurfaceRules.belowSurface()
                    )
            );
            consumer.accept(
                    DropsBackported.modId("preliminary_surface_rules"),
                    WorldgenModifier.builder().addSurfaceRule(Level.OVERWORLD, InjectionType.PREPEND,
                            SurfaceRules.ifTrue(
                                    SurfaceRules.abovePreliminarySurface(),
                                    DBSurfaceRules.aboveSurface()
                            )
                    )
            );
        });
        AddBiomeInjectorsEvent.EVENT.register((registry, consumer) -> {
            consumer.accept(
                    DropsBackported.modId("sulfur_caves"),
                    BiomeInjector.builder(Level.OVERWORLD).addPoints(
                            new Climate.ParameterList<>(List.of(
                                    Pair.of(
                                            new Climate.ParameterPoint(
                                                    Climate.Parameter.span(-1.0F, 1.0F),
                                                    Climate.Parameter.span(-1.0F, 1.0F),
                                                    Climate.Parameter.span(-0.19F, 0.55F),
                                                    Climate.Parameter.span(0.45F, 1.0F),
                                                    Climate.Parameter.span(0.2F, 0.9F),
                                                    Climate.Parameter.span(-1.1F, -0.85F),
                                                    0L
                                            ),
                                            registry.getOrThrow(DBBiomes.SULFUR_CAVES)
                                    )
                            ))
                    )
            );
        });
    }
}
