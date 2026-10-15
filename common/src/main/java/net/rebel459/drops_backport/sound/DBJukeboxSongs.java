package net.rebel459.drops_backport.sound;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.JukeboxSong;
import net.rebel459.drops_backport.DropsBackport;

import java.util.HashSet;
import java.util.Set;

public class DBJukeboxSongs {

    private static final Set<Pair<ResourceKey<JukeboxSong>, JukeboxSong>> KEYS = new HashSet<>();

    public static ResourceKey<JukeboxSong> BOUNCE = register("bounce", new JukeboxSong(DBSoundEvents.MUSIC_DISC_BOUNCE.holder(), Component.translatable("jukebox_song.minecraft.bounce"), 234, 8));

    private static ResourceKey<JukeboxSong> register(String path, JukeboxSong jukeboxSong) {
        ResourceKey<JukeboxSong> key = ResourceKey.create(Registries.JUKEBOX_SONG, DropsBackport.vanillaId(path));
        KEYS.add(Pair.of(key, jukeboxSong));
        return key;
    }

    public static void bootstrap(BootstrapContext<JukeboxSong> context) {
        KEYS.forEach((pair) -> {
            context.register(pair.getFirst(), pair.getSecond());
        });
    }
}
