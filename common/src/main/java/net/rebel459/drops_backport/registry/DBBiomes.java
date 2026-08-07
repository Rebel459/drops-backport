package net.rebel459.drops_backport.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;

public class DBBiomes {
    public static ResourceKey<Biome> SULFUR_CAVES = create("sulfur_caves");
    public static ResourceKey<Biome> DAPPLED_FOREST = create("dappled_forest");

    public static ResourceKey<Biome> create(String path) {
        return ResourceKey.create(Registries.BIOME, Identifier.withDefaultNamespace(path));
    }
}
