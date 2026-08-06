package net.rebel459.drops_backported.registry;

import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.rebel459.drops_backported.block.potent_sulfur.PotentSulfurState;
import net.rebel459.drops_backported.block.sulfur_spike.SpeleothemThickness;

public class DBBlockStateProperties {
    public static final EnumProperty<PotentSulfurState> POTENT_SULFUR_STATE = EnumProperty.create("potent_sulfur_state", PotentSulfurState.class);
    public static final EnumProperty<SpeleothemThickness> SPELEOTHEM_THICKNESS = EnumProperty.create("thickness", SpeleothemThickness.class);
}
