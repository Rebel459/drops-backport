package net.rebel459.drops_backport.registry;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.rebel459.drops_backport.DropsBackport;
import net.rebel459.drops_backport.block.PoplarSaplingBlock;
import net.rebel459.drops_backport.block.PotentSulfurBlock;
import net.rebel459.drops_backport.block.ShelfMushroomBlock;
import net.rebel459.drops_backport.block.bed.StrawBedBlock;
import net.rebel459.drops_backport.block.leaves.UntintedParticleLeavesBlock;
import net.rebel459.drops_backport.block.speleothem.SulfurSpikeBlock;
import net.rebel459.drops_backport.util.block.AmbientLeavesBlockSoundPlayer;
import net.rebel459.unified.api.codec.ExtensibleBlockCodec;
import net.rebel459.unified.api.codec.ExtensibleCodecs;
import net.rebel459.unified.api.registry.VanillaBlockCodecs;

import java.util.function.BiFunction;
import java.util.function.Function;

public class DBBlockCodecs {

	private static ExtensibleBlockCodec.Simple simple(String id, Function<BlockBehaviour.Properties, ? extends Block> factory) {
		return ExtensibleCodecs.BLOCK.register(DropsBackport.modId(id), () -> factory);
	}

	private static <T> ExtensibleBlockCodec.Complex<T> complex(String id, MapCodec<T> codec, BiFunction<T, BlockBehaviour.Properties, ? extends Block> factory) {
		return ExtensibleCodecs.BLOCK.register(DropsBackport.modId(id), codec, (definition) -> (properties) -> (Block)factory.apply(definition, properties));
	}

	public static final ExtensibleBlockCodec.Simple POPLAR_SAPLING = simple("poplar_sapling", PoplarSaplingBlock::new);

	public static final ExtensibleBlockCodec.Simple POTENT_SULFUR = simple("potent_sulfur", PotentSulfurBlock::new);

	public static final ExtensibleBlockCodec.Simple STRAW_BED = simple("straw_bed", StrawBedBlock::new);

	public static final ExtensibleBlockCodec.Simple SHELF_MUSHROOM = simple("shelf_mushroom", ShelfMushroomBlock::new);

	public static final ExtensibleBlockCodec.Complex<Block> SULFUR_SPIKE = complex("sulfur_spike", BuiltInRegistries.BLOCK.byNameCodec().fieldOf("block_to_grow_on"),
			(definition, properties) -> new SulfurSpikeBlock(definition.defaultBlockState(), properties));

	public static final ExtensibleBlockCodec.Complex<ParticleLeaves> UNTINTED_PARTICLE_LEAVES = complex("untinted_particle_leaves", ParticleLeaves.CODEC,
			(definition, properties) -> new UntintedParticleLeavesBlock(definition.chance, definition.particle, definition.player, properties));

	public record ParticleLeaves(float chance, ParticleOptions particle, AmbientLeavesBlockSoundPlayer player) {
		public static final MapCodec<ParticleLeaves> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
				ExtraCodecs.floatRange(0.0F, 1.0F).fieldOf("leaf_particle_chance").forGetter(ParticleLeaves::chance),
				ParticleTypes.CODEC.fieldOf("leaf_particle").forGetter(ParticleLeaves::particle),
				AmbientLeavesBlockSoundPlayer.CODEC.fieldOf("sound_player").forGetter(ParticleLeaves::player)
		).apply(instance, ParticleLeaves::new));
	}

	public static void init() {}
}