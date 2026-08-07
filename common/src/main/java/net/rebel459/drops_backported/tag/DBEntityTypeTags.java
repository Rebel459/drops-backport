package net.rebel459.drops_backported.tag;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

public interface DBEntityTypeTags {

    TagKey<EntityType<?>> NOT_AFFECTED_BY_GEYSERS = create("not_affected_by_geysers");

    private static TagKey<EntityType<?>> create(final String name) {
        return TagKey.create(Registries.ENTITY_TYPE, Identifier.withDefaultNamespace(name));
    }
}
