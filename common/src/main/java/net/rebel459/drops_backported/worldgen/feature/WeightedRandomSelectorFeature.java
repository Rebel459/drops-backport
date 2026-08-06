package net.rebel459.drops_backported.worldgen.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import java.util.stream.Stream;

public class WeightedRandomSelectorFeature extends Feature<WeightedRandomSelectorFeature.Configuration> {
    public WeightedRandomSelectorFeature(Codec<Configuration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<Configuration> context) {
        return context.config().features().getRandom(context.random())
                .map(feature -> feature.place(context.level(), context.chunkGenerator(), context.random(), context.origin()))
                .orElse(false);
    }

    public record Configuration(WeightedList<PlacedFeature> features) implements FeatureConfiguration {
        public static final Codec<Configuration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                WeightedList.nonEmptyCodec(PlacedFeature.DIRECT_CODEC).fieldOf("features").forGetter(Configuration::features)
        ).apply(instance, Configuration::new));

        @Override
        public Stream<Holder<ConfiguredFeature<?, ?>>> getSubFeatures() {
            return features.unwrap().stream().flatMap(entry -> entry.value().getFeatures());
        }
    }
}
