package net.rebel459.drops_backported.worldgen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import net.rebel459.drops_backported.registry.DBBiomes;
import net.rebel459.drops_backported.registry.DBBlocks;

public final class DBSurfaceRules {

	public static SurfaceRules.RuleSource sulfurCaves() {
		ResourceKey<NormalNoise.NoiseParameters> noise = ResourceKey.create(Registries.NOISE, Identifier.withDefaultNamespace("sulfur_cave_gradient"));
		return SurfaceRules.ifTrue(
				SurfaceRules.isBiome(DBBiomes.SULFUR_CAVES),
				SurfaceRules.sequence(
						SurfaceRules.ifTrue(
								threeDimensionalNoise(noise, -0.4000000059604645, -0.10000000149011612),
								makeStateRule(DBBlocks.CINNABAR.getBase().get())
						),
						SurfaceRules.ifTrue(
								threeDimensionalNoise(noise, 0.0, 0.4000000059604645),
								makeStateRule(DBBlocks.SULFUR.getBase().get())
						),
						SurfaceRules.ifTrue(
								threeDimensionalNoise(noise, 0.4000000059604645, 1.7976931348623157E308),
								makeStateRule(DBBlocks.CINNABAR.getBase().get())
						),
						makeStateRule(Blocks.STONE)
				)
		);
	}

	public static SurfaceRules.RuleSource dappledForest() {
		ResourceKey<NormalNoise.NoiseParameters> noise = ResourceKey.create(Registries.NOISE, Identifier.withDefaultNamespace("small_patch"));
		return SurfaceRules.ifTrue(
				SurfaceRules.isBiome(DBBiomes.DAPPLED_FOREST),
				SurfaceRules.ifTrue(
						SurfaceRules.noiseCondition(noise, 1.2000000476837158, 1.7976931348623157E308),
						makeStateRule(Blocks.COARSE_DIRT)
				)
		);
	}

	public static SurfaceRules.ConditionSource threeDimensionalNoise(ResourceKey<NormalNoise.NoiseParameters> noise, double minRange, double maxRange) {
		return new ThreeDimensionalNoise(noise, minRange, maxRange);
	}

	public static SurfaceRules.RuleSource makeStateRule(Block block) {
		return SurfaceRules.state(block.defaultBlockState());
	}

	public static SurfaceRules.RuleSource belowSurface() {
		return SurfaceRules.sequence(
				sulfurCaves()
		);
	}

    public static SurfaceRules.RuleSource aboveSurface() {
		return SurfaceRules.sequence(
				dappledForest()
		);
    }

	private record ThreeDimensionalNoise(ResourceKey<NormalNoise.NoiseParameters> noise, double minThreshold, double maxThreshold)
			implements SurfaceRules.ConditionSource {
		private static final KeyDispatchDataCodec<ThreeDimensionalNoise> CODEC = KeyDispatchDataCodec.of(
				RecordCodecBuilder.mapCodec(
						i -> i.group(
										ResourceKey.codec(Registries.NOISE).fieldOf("noise").forGetter(ThreeDimensionalNoise::noise),
										Codec.DOUBLE.fieldOf("min_threshold").forGetter(ThreeDimensionalNoise::minThreshold),
										Codec.DOUBLE.fieldOf("max_threshold").forGetter(ThreeDimensionalNoise::maxThreshold)
								)
								.apply(i, ThreeDimensionalNoise::new)
				)
		);

		@Override
		public KeyDispatchDataCodec<? extends SurfaceRules.ConditionSource> codec() {
			return CODEC;
		}

		public SurfaceRules.Condition apply(SurfaceRules.Context ruleContext) {
			final NormalNoise noise = ruleContext.randomState.getOrCreateNoise(this.noise);

			class Condition extends SurfaceRules.LazyXZCondition {
				private Condition() {
                    super(ruleContext);
				}

				@Override
				protected boolean compute() {
					double value = noise.getValue(this.context.blockX, this.context.blockY, this.context.blockZ);
					return value >= ThreeDimensionalNoise.this.minThreshold && value <= ThreeDimensionalNoise.this.maxThreshold;
				}
			}

			return new Condition();
		}
	}
}
