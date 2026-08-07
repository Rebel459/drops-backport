package net.rebel459.drops_backported.worldgen.feature.speleothem;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Plane;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

import java.util.Optional;

public class SpeleothemFeature extends Feature<SpeleothemFeature.Configuration> {
    public SpeleothemFeature(Codec<Configuration> codec) {
        super(codec);
    }

    @Override
    public boolean place(final FeaturePlaceContext<Configuration> context) {
        Configuration config = context.config();
        WorldGenLevel level = context.level();
        ChunkGenerator chunkGenerator = context.chunkGenerator();
        RandomSource random = context.random();
        BlockPos origin = context.origin();
        Optional<Direction> tipDirection = getTipDirection(level, origin, random, config);
        if (tipDirection.isEmpty()) {
            return false;
        } else {
            BlockPos rootPos = origin.relative(tipDirection.get().getOpposite());
            createPatchOfBaseBlocks(level, random, rootPos, config);
            int height = random.nextFloat() < config.chanceOfTallerGeneration
                    && SpeleothemUtils.isEmptyOrWater(level.getBlockState(origin.relative(tipDirection.get())))
                    ? 2
                    : 1;
            SpeleothemUtils.growSpeleothem(
                    level,
                    origin,
                    tipDirection.get(),
                    height,
                    false,
                    config.baseBlock.getBlock(),
                    config.pointedBlock.getBlock(),
                    config.replaceableBlocks
            );
            return true;
        }
    }

    private static Optional<Direction> getTipDirection(final LevelAccessor level, final BlockPos pos, final RandomSource random, final Configuration config) {
        boolean canPlaceAbove = SpeleothemUtils.isBase(level.getBlockState(pos.above()), config.baseBlock.getBlock(), config.replaceableBlocks);
        boolean canPlaceBelow = SpeleothemUtils.isBase(level.getBlockState(pos.below()), config.baseBlock.getBlock(), config.replaceableBlocks);
        if (canPlaceAbove && canPlaceBelow) {
            return Optional.of(random.nextBoolean() ? Direction.DOWN : Direction.UP);
        } else if (canPlaceAbove) {
            return Optional.of(Direction.DOWN);
        } else {
            return canPlaceBelow ? Optional.of(Direction.UP) : Optional.empty();
        }
    }

    private static void createPatchOfBaseBlocks(final LevelAccessor level, final RandomSource random, final BlockPos pos, final Configuration config) {
        SpeleothemUtils.placeBaseBlockIfPossible(level, pos, config.baseBlock.getBlock(), config.replaceableBlocks);

        for (Direction direction : Plane.HORIZONTAL) {
            if (!(random.nextFloat() > config.chanceOfDirectionalSpread)) {
                BlockPos pos1 = pos.relative(direction);
                SpeleothemUtils.placeBaseBlockIfPossible(level, pos1, config.baseBlock.getBlock(), config.replaceableBlocks);
                if (!(random.nextFloat() > config.chanceOfSpreadRadius2)) {
                    BlockPos pos2 = pos1.relative(Direction.getRandom(random));
                    SpeleothemUtils.placeBaseBlockIfPossible(level, pos2, config.baseBlock.getBlock(), config.replaceableBlocks);
                    if (!(random.nextFloat() > config.chanceOfSpreadRadius3)) {
                        BlockPos pos3 = pos2.relative(Direction.getRandom(random));
                        SpeleothemUtils.placeBaseBlockIfPossible(level, pos3, config.baseBlock.getBlock(), config.replaceableBlocks);
                    }
                }
            }
        }
    }

    public record Configuration(
            BlockState baseBlock,
            BlockState pointedBlock,
            HolderSet<Block> replaceableBlocks,
            float chanceOfTallerGeneration,
            float chanceOfDirectionalSpread,
            float chanceOfSpreadRadius2,
            float chanceOfSpreadRadius3
    ) implements FeatureConfiguration {
        public static final Codec<Configuration> CODEC = RecordCodecBuilder.create(
                i -> i.group(
                                BlockState.CODEC.fieldOf("base_block").forGetter(Configuration::baseBlock),
                                BlockState.CODEC.fieldOf("pointed_block").forGetter(Configuration::pointedBlock),
                                RegistryCodecs.homogeneousList(Registries.BLOCK).fieldOf("replaceable_blocks").forGetter(Configuration::replaceableBlocks),
                                Codec.floatRange(0.0F, 1.0F).optionalFieldOf("chance_of_taller_generation", 0.2F).forGetter(Configuration::chanceOfTallerGeneration),
                                Codec.floatRange(0.0F, 1.0F).optionalFieldOf("chance_of_directional_spread", 0.7F).forGetter(Configuration::chanceOfDirectionalSpread),
                                Codec.floatRange(0.0F, 1.0F).optionalFieldOf("chance_of_spread_radius2", 0.5F).forGetter(Configuration::chanceOfSpreadRadius2),
                                Codec.floatRange(0.0F, 1.0F).optionalFieldOf("chance_of_spread_radius3", 0.5F).forGetter(Configuration::chanceOfSpreadRadius3)
                        )
                        .apply(i, Configuration::new)
        );
    }
}
