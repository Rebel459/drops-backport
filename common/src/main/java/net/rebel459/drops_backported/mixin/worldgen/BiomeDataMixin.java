package net.rebel459.drops_backported.mixin.worldgen;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.biome.BiomeData;
import net.minecraft.data.worldgen.biome.OverworldBiomes;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.rebel459.drops_backported.registry.DBBiomes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BiomeData.class)
public class BiomeDataMixin {

    @Inject(method = "bootstrap", at = @At("TAIL"))
    private static void addBackportedBiomes(BootstrapContext<Biome> context, CallbackInfo ci) {
        HolderGetter<PlacedFeature> placedFeatures = context.lookup(Registries.PLACED_FEATURE);
        HolderGetter<ConfiguredWorldCarver<?>> carvers = context.lookup(Registries.CONFIGURED_CARVER);

        context.register(DBBiomes.DAPPLED_FOREST, OverworldBiomes.forest(placedFeatures, carvers, false, false, true));
        context.register(DBBiomes.SULFUR_CAVES, OverworldBiomes.dripstoneCaves(placedFeatures, carvers));
    }
}
