package net.rebel459.drops_backported.block.potent_sulfur;

import net.minecraft.util.StringRepresentable;

public enum PotentSulfurState implements StringRepresentable {
    DRY("dry"),
    WET("wet"),
    DORMANT("dormant"),
    ERUPTING("erupting"),
    CONTINUOUS("continuous");

    private final String name;

    private PotentSulfurState(final String name) {
        this.name = name;
    }

    public String toString() {
        return this.name;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }
}
