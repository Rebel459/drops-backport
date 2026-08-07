package net.rebel459.drops_backport.client.entity.sulfur_cube;

import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.client.renderer.entity.state.SlimeRenderState;

public class SulfurCubeRenderState extends SlimeRenderState {
    public final BlockModelRenderState containedBlock = new BlockModelRenderState();
    public float fuseRemainingTicks;
    public boolean isBaby;
}
