package net.rebel459.drops_backport.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.rebel459.drops_backport.worldgen.DBFeatures;
import net.rebel459.drops_backport.worldgen.DBTreeGrowers;

import java.util.List;

public class PoplarSaplingBlock extends SaplingBlock {
    private static final List<ResourceKey<ConfiguredFeature<?, ?>>> POPLAR_FEATURES = List.of(
            DBFeatures.RED_POPLAR,
            DBFeatures.ORANGE_POPLAR,
            DBFeatures.YELLOW_POPLAR
    );

    public PoplarSaplingBlock(final BlockBehaviour.Properties properties) {
        super(DBTreeGrowers.POPLAR, properties);
    }

    @Override
    public void advanceTree(final ServerLevel level, final BlockPos pos, final BlockState state, final RandomSource random) {
        if (state.getValue(STAGE) == 0) {
            level.setBlock(pos, state.cycle(STAGE), 260);
        } else {
            this.growPoplarTree(level, level.getChunkSource().getGenerator(), pos, state, random);
        }
    }

    private void growPoplarTree(
            final ServerLevel level,
            final ChunkGenerator generator,
            final BlockPos pos,
            final BlockState state,
            final RandomSource random
    ) {
        ResourceKey<ConfiguredFeature<?, ?>> featureKey = POPLAR_FEATURES.get(random.nextInt(POPLAR_FEATURES.size()));
        Holder<ConfiguredFeature<?, ?>> featureHolder = level.registryAccess()
                .lookupOrThrow(Registries.CONFIGURED_FEATURE)
                .get(featureKey)
                .orElse(null);
        if (featureHolder == null) {
            return;
        }

        ConfiguredFeature<?, ?> feature = featureHolder.value();
        BlockState emptyBlock = level.getFluidState(pos).createLegacyBlock();
        level.setBlock(pos, emptyBlock, 260);
        if (feature.place(level, generator, random, pos)) {
            if (level.getBlockState(pos) == emptyBlock) {
                level.sendBlockUpdated(pos, state, emptyBlock, 2);
            }
        } else {
            level.setBlock(pos, state, 260);
        }
    }
}
