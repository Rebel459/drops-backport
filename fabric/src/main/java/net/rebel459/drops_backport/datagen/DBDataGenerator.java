package net.rebel459.drops_backport.datagen;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.rebel459.drops_backport.sound.DBJukeboxSongs;
import net.rebel459.unified.fabric.FabricUnifiedDatagen;
import org.jetbrains.annotations.NotNull;

public final class DBDataGenerator implements DataGeneratorEntrypoint {

	@Override
	public void onInitializeDataGenerator(@NotNull FabricDataGenerator dataGenerator) {
		final FabricUnifiedDatagen.Pack pack = FabricUnifiedDatagen.register(dataGenerator);
	}

	@Override
	public void buildRegistry(RegistrySetBuilder registryBuilder) {
		registryBuilder.add(Registries.JUKEBOX_SONG, DBJukeboxSongs::bootstrap);
	}
}