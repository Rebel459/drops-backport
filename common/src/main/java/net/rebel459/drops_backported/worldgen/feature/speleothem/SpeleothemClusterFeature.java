package net.rebel459.drops_backported.worldgen.feature.speleothem;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import java.util.OptionalInt;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Plane;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.ClampedNormalFloat;
import net.minecraft.util.valueproviders.FloatProvider;
import net.minecraft.util.valueproviders.FloatProviders;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.IntProviders;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Column;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public class SpeleothemClusterFeature extends Feature<SpeleothemClusterFeature.Configuration> {
    public SpeleothemClusterFeature(Codec<Configuration> codec) {
        super(codec);
    }

    @Override
    public boolean place(final FeaturePlaceContext<Configuration> context) {
        Configuration config = context.config();
        WorldGenLevel level = context.level();
        RandomSource random = context.random();
        BlockPos origin = context.origin();
        if (!SpeleothemUtils.isEmptyOrWater(level, origin)) {
            return false;
        } else {
            int height = config.height.sample(random);
            float wetness = config.wetness.sample(random);
            float density = config.density.sample(random);
            int xRadius = config.radius.sample(random);
            int zRadius = config.radius.sample(random);

            for (int dx = -xRadius; dx <= xRadius; dx++) {
                for (int dz = -zRadius; dz <= zRadius; dz++) {
                    double chanceOfStalagmiteOrStalactite = getChanceOfStalagmiteOrStalactite(xRadius, zRadius, dx, dz, config);
                    BlockPos pos = origin.offset(dx, 0, dz);
                    placeColumn(level, random, pos, dx, dz, wetness, chanceOfStalagmiteOrStalactite, height, density, config);
                }
            }

            return true;
        }
    }

    private static void placeColumn(
            final WorldGenLevel level,
            final RandomSource random,
            final BlockPos pos,
            final int dx,
            final int dz,
            final float chanceOfWater,
            final double chanceOfStalagmiteOrStalactite,
            final int clusterHeight,
            final float density,
            final Configuration config
    ) {
        Optional<Column> baseColumn = Column.scan(
                level, pos, config.floorToCeilingSearchRange, SpeleothemUtils::isEmptyOrWater, SpeleothemUtils::isNeitherEmptyNorWater
        );
        if (!baseColumn.isEmpty()) {
            OptionalInt ceiling = baseColumn.get().getCeiling();
            OptionalInt baseFloor = baseColumn.get().getFloor();
            if (!ceiling.isEmpty() || !baseFloor.isEmpty()) {
                boolean wantPool = random.nextFloat() < chanceOfWater;
                Column column;
                if (wantPool && baseFloor.isPresent() && canPlacePool(level, pos.atY(baseFloor.getAsInt()), config)) {
                    int baseFloorY = baseFloor.getAsInt();
                    column = baseColumn.get().withFloor(OptionalInt.of(baseFloorY - 1));
                    level.setBlock(pos.atY(baseFloorY), Blocks.WATER.defaultBlockState(), 2);
                } else {
                    column = baseColumn.get();
                }

                OptionalInt floor = column.getFloor();
                boolean wantStalactite = random.nextDouble() < chanceOfStalagmiteOrStalactite;
                int stalactiteHeight;
                if (ceiling.isPresent() && wantStalactite && !isLava(level, pos.atY(ceiling.getAsInt()))) {
                    int ceilingThickness = config.speleothemBlockLayerThickness.sample(random);
                    replaceBlocksWithBaseBlocks(level, pos.atY(ceiling.getAsInt()), ceilingThickness, Direction.UP, config);
                    int maxHeightForThisColumn;
                    if (floor.isPresent()) {
                        maxHeightForThisColumn = Math.min(clusterHeight, ceiling.getAsInt() - floor.getAsInt());
                    } else {
                        maxHeightForThisColumn = clusterHeight;
                    }

                    stalactiteHeight = getSpeleothemHeight(random, dx, dz, density, maxHeightForThisColumn, config);
                } else {
                    stalactiteHeight = 0;
                }

                boolean wantStalagmite = random.nextDouble() < chanceOfStalagmiteOrStalactite;
                int stalagmiteHeight;
                if (floor.isPresent() && wantStalagmite && !isLava(level, pos.atY(floor.getAsInt()))) {
                    int floorThickness = config.speleothemBlockLayerThickness.sample(random);
                    replaceBlocksWithBaseBlocks(level, pos.atY(floor.getAsInt()), floorThickness, Direction.DOWN, config);
                    if (ceiling.isPresent()) {
                        stalagmiteHeight = Math.max(
                                0,
                                stalactiteHeight + Mth.randomBetweenInclusive(
                                        random,
                                        -config.maxStalagmiteStalactiteHeightDiff,
                                        config.maxStalagmiteStalactiteHeightDiff
                                )
                        );
                    } else {
                        stalagmiteHeight = getSpeleothemHeight(random, dx, dz, density, clusterHeight, config);
                    }
                } else {
                    stalagmiteHeight = 0;
                }

                int actualStalagmiteHeight;
                int actualStalactiteHeight;
                if (ceiling.isPresent() && floor.isPresent() && ceiling.getAsInt() - stalactiteHeight <= floor.getAsInt() + stalagmiteHeight) {
                    int floorY = floor.getAsInt();
                    int ceilingY = ceiling.getAsInt();
                    int lowestStalactiteBottom = Math.max(ceilingY - stalactiteHeight, floorY + 1);
                    int highestStalagmiteTop = Math.min(floorY + stalagmiteHeight, ceilingY - 1);
                    int actualStalactiteBottom = Mth.randomBetweenInclusive(random, lowestStalactiteBottom, highestStalagmiteTop + 1);
                    int actualStalagmiteTop = actualStalactiteBottom - 1;
                    actualStalactiteHeight = ceilingY - actualStalactiteBottom;
                    actualStalagmiteHeight = actualStalagmiteTop - floorY;
                } else {
                    actualStalactiteHeight = stalactiteHeight;
                    actualStalagmiteHeight = stalagmiteHeight;
                }

                boolean mergeTips = random.nextBoolean()
                        && actualStalactiteHeight > 0
                        && actualStalagmiteHeight > 0
                        && column.getHeight().isPresent()
                        && actualStalactiteHeight + actualStalagmiteHeight == column.getHeight().getAsInt();
                if (ceiling.isPresent()) {
                    SpeleothemUtils.growSpeleothem(
                            level,
                            pos.atY(ceiling.getAsInt() - 1),
                            Direction.DOWN,
                            actualStalactiteHeight,
                            mergeTips,
                            config.baseBlock.getBlock(),
                            config.pointedBlock.getBlock(),
                            config.replaceableBlocks
                    );
                }

                if (floor.isPresent()) {
                    SpeleothemUtils.growSpeleothem(
                            level,
                            pos.atY(floor.getAsInt() + 1),
                            Direction.UP,
                            actualStalagmiteHeight,
                            mergeTips,
                            config.baseBlock.getBlock(),
                            config.pointedBlock.getBlock(),
                            config.replaceableBlocks
                    );
                }
            }
        }
    }

    private static boolean isLava(final LevelReader level, final BlockPos pos) {
        return level.getBlockState(pos).is(Blocks.LAVA);
    }

    private static int getSpeleothemHeight(final RandomSource random, final int dx, final int dz, final float density, final int maxHeight, final Configuration config) {
        if (random.nextFloat() > density) {
            return 0;
        } else {
            int distanceFromCenter = Math.abs(dx) + Math.abs(dz);
            float heightMean = (float)Mth.clampedMap((double)distanceFromCenter, 0.0, (double)config.maxDistanceFromCenterAffectingHeightBias, maxHeight / 2.0, 0.0);
            return (int)randomBetweenBiased(random, 0.0F, maxHeight, heightMean, config.heightDeviation);
        }
    }

    private static boolean canPlacePool(final WorldGenLevel level, final BlockPos pos, final Configuration config) {
        BlockState state = level.getBlockState(pos);
        if (!state.is(Blocks.WATER) && !state.is(config.baseBlock.getBlock()) && !state.is(config.pointedBlock.getBlock())) {
            if (level.getBlockState(pos.above()).getFluidState().is(FluidTags.WATER)) {
                return false;
            } else {
                for (Direction direction : Plane.HORIZONTAL) {
                    if (!canBeAdjacentToWater(level, pos.relative(direction))) {
                        return false;
                    }
                }

                return canBeAdjacentToWater(level, pos.below());
            }
        } else {
            return false;
        }
    }

    private static boolean canBeAdjacentToWater(final LevelAccessor level, final BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        return state.is(BlockTags.BASE_STONE_OVERWORLD) || state.getFluidState().is(FluidTags.WATER);
    }

    private static void replaceBlocksWithBaseBlocks(final WorldGenLevel level, final BlockPos firstPos, final int maxCount, final Direction direction, final Configuration config) {
        MutableBlockPos pos = firstPos.mutable();

        for (int i = 0; i < maxCount; i++) {
            if (!SpeleothemUtils.placeBaseBlockIfPossible(level, pos, config.baseBlock.getBlock(), config.replaceableBlocks)) {
                return;
            }

            pos.move(direction);
        }
    }

    private static double getChanceOfStalagmiteOrStalactite(final int xRadius, final int zRadius, final int dx, final int dz, final Configuration config) {
        int xDistanceFromEdge = xRadius - Math.abs(dx);
        int zDistanceFromEdge = zRadius - Math.abs(dz);
        int distanceFromEdge = Math.min(xDistanceFromEdge, zDistanceFromEdge);
        return Mth.clampedMap(
                (float)distanceFromEdge,
                0.0F,
                (float)config.maxDistanceFromEdgeAffectingChanceOfSpeleothem,
                config.chanceOfSpeleothemAtMaxDistanceFromCenter,
                1.0F
        );
    }

    private static float randomBetweenBiased(final RandomSource random, final float min, final float maxExclusive, final float mean, final float deviation) {
        return ClampedNormalFloat.sample(random, mean, deviation, min, maxExclusive);
    }

    public record Configuration(
            BlockState baseBlock,
            BlockState pointedBlock,
            HolderSet<Block> replaceableBlocks,
            int floorToCeilingSearchRange,
            IntProvider height,
            IntProvider radius,
            int maxStalagmiteStalactiteHeightDiff,
            int heightDeviation,
            IntProvider speleothemBlockLayerThickness,
            FloatProvider density,
            FloatProvider wetness,
            float chanceOfSpeleothemAtMaxDistanceFromCenter,
            int maxDistanceFromEdgeAffectingChanceOfSpeleothem,
            int maxDistanceFromCenterAffectingHeightBias
    ) implements FeatureConfiguration {
        public static final Codec<Configuration> CODEC = RecordCodecBuilder.create(
                i -> i.group(
                                BlockState.CODEC.fieldOf("base_block").forGetter(Configuration::baseBlock),
                                BlockState.CODEC.fieldOf("pointed_block").forGetter(Configuration::pointedBlock),
                                RegistryCodecs.homogeneousList(Registries.BLOCK).fieldOf("replaceable_blocks").forGetter(Configuration::replaceableBlocks),
                                Codec.intRange(1, 512).fieldOf("floor_to_ceiling_search_range").forGetter(Configuration::floorToCeilingSearchRange),
                                IntProviders.codec(1, 128).fieldOf("height").forGetter(Configuration::height),
                                IntProviders.codec(1, 128).fieldOf("radius").forGetter(Configuration::radius),
                                Codec.intRange(0, 64).fieldOf("max_stalagmite_stalactite_height_diff").forGetter(Configuration::maxStalagmiteStalactiteHeightDiff),
                                Codec.intRange(1, 64).fieldOf("height_deviation").forGetter(Configuration::heightDeviation),
                                IntProviders.codec(0, 128).fieldOf("speleothem_block_layer_thickness").forGetter(Configuration::speleothemBlockLayerThickness),
                                FloatProviders.codec(0.0F, 2.0F).fieldOf("density").forGetter(Configuration::density),
                                FloatProviders.codec(0.0F, 2.0F).fieldOf("wetness").forGetter(Configuration::wetness),
                                Codec.floatRange(0.0F, 1.0F)
                                        .fieldOf("chance_of_speleothem_at_max_distance_from_center")
                                        .forGetter(Configuration::chanceOfSpeleothemAtMaxDistanceFromCenter),
                                Codec.intRange(1, 64)
                                        .fieldOf("max_distance_from_edge_affecting_chance_of_speleothem")
                                        .forGetter(Configuration::maxDistanceFromEdgeAffectingChanceOfSpeleothem),
                                Codec.intRange(1, 64)
                                        .fieldOf("max_distance_from_center_affecting_height_bias")
                                        .forGetter(Configuration::maxDistanceFromCenterAffectingHeightBias)
                        )
                        .apply(i, Configuration::new)
        );
    }
}
