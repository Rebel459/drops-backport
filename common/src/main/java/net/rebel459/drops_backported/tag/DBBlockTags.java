package net.rebel459.drops_backported.tag;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class DBBlockTags {
    public static final TagKey<Block> CAUSES_PERIODIC_GEYSER_ERUPTIONS = create("causes_periodic_geyser_eruptions");
    public static final TagKey<Block> CAUSES_CONTINUOUS_GEYSER_ERUPTIONS = create("causes_continuous_geyser_eruptions");

    private static TagKey<Block> create(final String name) {
        return TagKey.create(Registries.BLOCK, Identifier.withDefaultNamespace(name));
    }
}
