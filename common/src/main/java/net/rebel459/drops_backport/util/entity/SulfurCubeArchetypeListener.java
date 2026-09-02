package net.rebel459.drops_backport.util.entity;

import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.rebel459.drops_backport.DropsBackport;
import org.jspecify.annotations.NonNull;

import java.util.Map;

public class SulfurCubeArchetypeListener extends SimpleJsonResourceReloadListener<SulfurCubeArchetype> {

    public static final Identifier ID = DropsBackport.vanillaId("sulfur_cube_archetype");

    public SulfurCubeArchetypeListener() {
        super(RegistryAccess.fromRegistryOfRegistries(BuiltInRegistries.REGISTRY), SulfurCubeArchetype.CODEC, SulfurCubeArchetype.REGISTRY_KEY);
    }

    @Override
    protected void apply(Map<Identifier, SulfurCubeArchetype> identifierRecordMap, @NonNull ResourceManager resourceManager, @NonNull ProfilerFiller profilerFiller) {
        SulfurCubeArchetype.REGISTERED.clear();
        for (Map.Entry<Identifier, SulfurCubeArchetype> entry : identifierRecordMap.entrySet()) {
            SulfurCubeArchetype.REGISTERED.add(entry.getValue());
        }
    }
}
