package net.rebel459.drops_backported.worldgen.feature;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.IntProviders;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import net.rebel459.drops_backported.worldgen.DBWorldgenCodecs;

import java.util.stream.Stream;

public class OffsetPlacement extends PlacementModifier {
    public static final MapCodec<OffsetPlacement> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            IntProviders.codec(-16, 16).fieldOf("x").forGetter(OffsetPlacement::x),
            IntProviders.codec(-16, 16).fieldOf("y").forGetter(OffsetPlacement::y),
            IntProviders.codec(-16, 16).fieldOf("z").forGetter(OffsetPlacement::z)
    ).apply(instance, OffsetPlacement::new));

    private final IntProvider x;
    private final IntProvider y;
    private final IntProvider z;

    public OffsetPlacement(IntProvider x, IntProvider y, IntProvider z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public Stream<BlockPos> getPositions(PlacementContext context, RandomSource random, BlockPos pos) {
        return Stream.of(pos.offset(x.sample(random), y.sample(random), z.sample(random)));
    }

    @Override
    public PlacementModifierType<?> type() {
        return DBWorldgenCodecs.OFFSET.get();
    }

    private IntProvider x() {
        return x;
    }

    private IntProvider y() {
        return y;
    }

    private IntProvider z() {
        return z;
    }
}
