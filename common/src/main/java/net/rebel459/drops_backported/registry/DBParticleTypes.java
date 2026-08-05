package net.rebel459.drops_backported.registry;

import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.rebel459.drops_backported.DropsBackported;
import net.rebel459.unified.platform.UnifiedRegistries;
import net.rebel459.unified.util.registry.Supplied;

public final class DBParticleTypes {
    private static final UnifiedRegistries.DeferredRegistry<ParticleType<?>> PARTICLES = UnifiedRegistries.DeferredRegistry.create(DropsBackported.VANILLA_ID, BuiltInRegistries.PARTICLE_TYPE);

    public static final Supplied<SimpleParticleType> SULFUR_BUBBLES = PARTICLES.register("sulfur_bubbles", () -> new SimpleParticleType(false));
    public static final Supplied<SimpleParticleType> SULFUR_CUBE_GOO = PARTICLES.register("sulfur_cube_goo", () -> new SimpleParticleType(false));

    private DBParticleTypes() {}

    public static void init() {}
}
