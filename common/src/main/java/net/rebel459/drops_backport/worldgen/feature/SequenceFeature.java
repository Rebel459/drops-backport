package net.rebel459.drops_backport.worldgen.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import java.util.List;
import java.util.stream.Stream;

public class SequenceFeature extends Feature<SequenceFeature.Configuration> {
    public SequenceFeature(Codec<Configuration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<Configuration> context) {
        boolean placedAny = false;
        WorldGenLevel level = context.level();
        ChunkGenerator generator = context.chunkGenerator();
        RandomSource random = context.random();
        BlockPos origin = context.origin();

        for (PlacedFeature feature : context.config().features()) {
            placedAny |= feature.place(level, generator, random, origin);
        }

        return placedAny;
    }

    public record Configuration(List<PlacedFeature> features) implements FeatureConfiguration {
        public static final Codec<Configuration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                PlacedFeature.DIRECT_CODEC.listOf().fieldOf("features").forGetter(Configuration::features)
        ).apply(instance, Configuration::new));

        @Override
        public Stream<Holder<ConfiguredFeature<?, ?>>> getSubFeatures() {
            return features.stream().flatMap(PlacedFeature::getFeatures);
        }
    }
}
