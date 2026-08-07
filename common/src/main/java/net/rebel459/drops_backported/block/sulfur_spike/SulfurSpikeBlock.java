package net.rebel459.drops_backported.block.sulfur_spike;

import net.minecraft.world.level.block.state.BlockState;

public class SulfurSpikeBlock extends SpeleothemBlock {
    private static final int MAX_GROWING_LENGTH = 2;

    public SulfurSpikeBlock(final BlockState blockToGrowOn, final Properties properties) {
        super(blockToGrowOn, properties);
    }

    @Override
    protected int getStalactiteLandingSound() {
        return 1052;
    }

    @Override
    protected int getMaxGrowthLength() {
        return 2;
    }
}
