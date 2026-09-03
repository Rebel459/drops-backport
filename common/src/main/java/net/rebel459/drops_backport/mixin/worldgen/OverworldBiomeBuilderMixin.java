package net.rebel459.drops_backport.mixin.worldgen;

import com.mojang.datafixers.util.Pair;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.biome.OverworldBiomeBuilder;
import net.rebel459.drops_backport.registry.DBBiomes;
import net.rebel459.unified.platform.UnifiedPlatform;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(OverworldBiomeBuilder.class)
public abstract class OverworldBiomeBuilderMixin {

    @Shadow
    protected abstract void addUndergroundBiome(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> biomes, Climate.Parameter temperature, Climate.Parameter humidity, Climate.Parameter continentalness, Climate.Parameter erosion, Climate.Parameter weirdness, float offset, ResourceKey<Biome> biome);

    @Shadow
    @Final
    private Climate.Parameter FULL_RANGE;

    @Shadow
    @Final
    private Climate.Parameter coastContinentalness;

    @Shadow
    @Final
    private Climate.Parameter inlandContinentalness;

    @Shadow
    @Final
    private Climate.Parameter[] erosions;

    @Inject(
            method = "addUndergroundBiomes",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/biome/OverworldBiomeBuilder;addUndergroundBiome(Ljava/util/function/Consumer;Lnet/minecraft/world/level/biome/Climate$Parameter;Lnet/minecraft/world/level/biome/Climate$Parameter;Lnet/minecraft/world/level/biome/Climate$Parameter;Lnet/minecraft/world/level/biome/Climate$Parameter;Lnet/minecraft/world/level/biome/Climate$Parameter;FLnet/minecraft/resources/ResourceKey;)V",
                    ordinal = 1,
                    shift = At.Shift.AFTER
            )
    )
    private void addSulfurCaves(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> biomes, CallbackInfo ci) {
        if (UnifiedPlatform.isModLoaded("lithostitched")) return;
        this.addUndergroundBiome(
                biomes,
                this.FULL_RANGE,
                this.FULL_RANGE,
                Climate.Parameter.span(this.coastContinentalness, this.inlandContinentalness),
                Climate.Parameter.span(this.erosions[5], this.erosions[6]),
                Climate.Parameter.span(-1.1F, -0.85F),
                0.0F,
                DBBiomes.SULFUR_CAVES
        );
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void addDappledForest(CallbackInfo ci) {
        if (UnifiedPlatform.isModLoaded("lithostitched")) return;
        OverworldBiomeBuilder.class.cast(this).MIDDLE_BIOMES_VARIANT[1][0] = DBBiomes.DAPPLED_FOREST;
    }
}
