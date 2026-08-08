package net.rebel459.drops_backport.tag;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class DBBlockTags {
    public static final TagKey<Block> CAUSES_PERIODIC_GEYSER_ERUPTIONS = create("causes_periodic_geyser_eruptions");
    public static final TagKey<Block> CAUSES_CONTINUOUS_GEYSER_ERUPTIONS = create("causes_continuous_geyser_eruptions");
    public static final TagKey<Block> REQUIRED_FOR_POPLAR_LEAF_AMBIENCE = create("required_for_poplar_leaf_ambience");
    public static final TagKey<Block> SPELEOTHEMS = create("speleothems");
    public static final TagKey<Block> CUSHION_USES_COLLISION_SHAPE = create("cushion_uses_collision_shape");
    public static final TagKey<Block> SUPPRESSES_BOUNCE = create("suppresses_bounce");

    private static TagKey<Block> create(final String name) {
        return TagKey.create(Registries.BLOCK, Identifier.withDefaultNamespace(name));
    }
}
