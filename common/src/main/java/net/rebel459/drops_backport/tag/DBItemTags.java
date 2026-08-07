package net.rebel459.drops_backport.tag;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class DBItemTags {
    public static final TagKey<Item> SULFUR_CUBE_SWALLOWABLE = create("sulfur_cube_swallowable");

    private static TagKey<Item> create(final String name) {
        return TagKey.create(Registries.ITEM, Identifier.withDefaultNamespace(name));
    }
}
