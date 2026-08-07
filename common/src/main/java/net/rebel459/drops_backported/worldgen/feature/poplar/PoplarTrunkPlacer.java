package net.rebel459.drops_backported.worldgen.feature.poplar;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.IntProviders;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;
import net.rebel459.drops_backported.worldgen.DBWorldgenCodecs;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PoplarTrunkPlacer extends TrunkPlacer {
    public static final MapCodec<PoplarTrunkPlacer> CODEC = RecordCodecBuilder.mapCodec(instance ->
            trunkPlacerParts(instance).and(instance.group(
                    IntProviders.codec(0, 8).fieldOf("branch_amount").forGetter(PoplarTrunkPlacer::branchAmount),
                    IntProviders.codec(0, 16).fieldOf("trunk_height_above_branches").forGetter(PoplarTrunkPlacer::trunkHeightAboveBranches)
            )).apply(instance, PoplarTrunkPlacer::new));

    private final IntProvider branchAmount;
    private final IntProvider trunkHeightAboveBranches;

    public PoplarTrunkPlacer(int baseHeight, int heightRandA, int heightRandB, IntProvider branchAmount, IntProvider trunkHeightAboveBranches) {
        super(baseHeight, heightRandA, heightRandB);
        this.branchAmount = branchAmount;
        this.trunkHeightAboveBranches = trunkHeightAboveBranches;
    }

    @Override
    protected TrunkPlacerType<?> type() {
        return DBWorldgenCodecs.POPLAR_TRUNK_PLACER.get();
    }

    @Override
    public List<FoliagePlacer.FoliageAttachment> placeTrunk(WorldGenLevel level, BiConsumer<BlockPos, BlockState> setter, RandomSource random, int height, BlockPos pos, TreeConfiguration config) {
        placeBelowTrunkBlock(level, setter, random, pos.below(), config);
        int trunkHeightUpToFoliageBranches = height - this.trunkHeightAboveBranches.sample(random);

        for (int y = 0; y < height; y++) {
            this.placeLog(level, setter, random, pos.above(y), config);
            List<Direction> directions = getShuffledBranchDirections(random);
            if (trunkHeightUpToFoliageBranches - 1 == y) {
                int branches = this.branchAmount.sample(random);

                for (int x = 0; x < branches; x++) {
                    Direction branchDirection = directions.get(x);
                    this.placeLog(level, setter, random, pos.above(y).relative(branchDirection, 1), config, getSidewaysStateModifier(branchDirection));
                }
            }
        }

        return List.of(new FoliagePlacer.FoliageAttachment(pos.above(trunkHeightUpToFoliageBranches), 0, false));
    }

    private static Function<BlockState, BlockState> getSidewaysStateModifier(final Direction branchDirection) {
        return state -> (BlockState) state.trySetValue(RotatedPillarBlock.AXIS, branchDirection.getAxis());
    }

    private static List<Direction> getShuffledBranchDirections(final RandomSource random) {
        return Direction.allShuffled(random).stream().filter(direction -> !direction.getAxis().isVertical()).collect(Collectors.toList());
    }

    private static Function<BlockState, BlockState> axis(Direction direction) {
        return state -> state.hasProperty(RotatedPillarBlock.AXIS) ? state.setValue(RotatedPillarBlock.AXIS, direction.getAxis()) : state;
    }

    private IntProvider branchAmount() {
        return branchAmount;
    }

    private IntProvider trunkHeightAboveBranches() {
        return trunkHeightAboveBranches;
    }
}
