package net.rebel459.drops_backported.sound;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.JukeboxSong;

public interface DBJukeboxSongs {
    ResourceKey<JukeboxSong> BOUNCE = create("bounce");

    private static ResourceKey<JukeboxSong> create(String id) {
        return ResourceKey.create(Registries.JUKEBOX_SONG, Identifier.withDefaultNamespace(id));
    }
}