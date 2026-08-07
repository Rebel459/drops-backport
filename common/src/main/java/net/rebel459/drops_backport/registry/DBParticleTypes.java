package net.rebel459.drops_backport.registry;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.rebel459.drops_backport.DropsBackport;
import net.rebel459.drops_backport.particle.GeyserBaseParticleOptions;
import net.rebel459.drops_backport.particle.GeyserParticleOptions;
import net.rebel459.unified.platform.UnifiedRegistries;
import net.rebel459.unified.util.registry.Supplied;

import java.util.function.Function;

public final class DBParticleTypes {
    private static final UnifiedRegistries.DeferredRegistry<ParticleType<?>> PARTICLES = UnifiedRegistries.DeferredRegistry.create(DropsBackport.VANILLA_ID, BuiltInRegistries.PARTICLE_TYPE);

    public static final Supplied<SimpleParticleType> SULFUR_BUBBLES = register("sulfur_bubbles", false);
    public static final Supplied<SimpleParticleType> SULFUR_CUBE_GOO = register("sulfur_cube_goo", false);

    public static final Supplied<SimpleParticleType> NOXIOUS_GAS = register("noxious_gas", false);
    public static final Supplied<SimpleParticleType> NOXIOUS_GAS_CLOUD = register("noxious_gas_cloud", false);
    public static final Supplied<ParticleType<GeyserParticleOptions>> GEYSER = register("geyser", true, GeyserParticleOptions::codec, GeyserParticleOptions::streamCodec);
    public static final Supplied<ParticleType<GeyserBaseParticleOptions>> GEYSER_BASE = register(
            "geyser_base", true, GeyserBaseParticleOptions::codec, GeyserBaseParticleOptions::streamCodec
    );
    public static final Supplied<ParticleType<GeyserBaseParticleOptions>> GEYSER_POOF = register(
            "geyser_poof", true, GeyserBaseParticleOptions::codec, GeyserBaseParticleOptions::streamCodec
    );
    public static final Supplied<ParticleType<GeyserParticleOptions>> GEYSER_PLUME = register(
            "geyser_plume", true, GeyserParticleOptions::codec, GeyserParticleOptions::streamCodec
    );
    public static final Supplied<SimpleParticleType> RED_POPLAR_LEAVES = register("red_poplar_leaves", false);
    public static final Supplied<SimpleParticleType> ORANGE_POPLAR_LEAVES = register("orange_poplar_leaves", false);
    public static final Supplied<SimpleParticleType> YELLOW_POPLAR_LEAVES = register("yellow_poplar_leaves", false);

    private static Supplied<SimpleParticleType> register(final String name, final boolean overrideLimiter) {
        return PARTICLES.register(name, () -> new SimpleParticleType(overrideLimiter));
    }

    private static <T extends ParticleOptions> Supplied<ParticleType<T>> register(
            final String name,
            final boolean overrideLimiter,
            final Function<ParticleType<T>, MapCodec<T>> codec,
            final Function<ParticleType<T>, StreamCodec<? super RegistryFriendlyByteBuf, T>> streamCodec
    ) {
        return PARTICLES.register(name, () -> new ParticleType<T>(overrideLimiter) {
            @Override
            public MapCodec<T> codec() {
                return codec.apply(this);
            }

            @Override
            public StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec() {
                return streamCodec.apply(this);
            }
        });
    }

    private DBParticleTypes() {
    }

    public static void init() {
    }
}
