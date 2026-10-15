package net.rebel459.drops_backport.registry;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.item.DyeColor;
import net.rebel459.drops_backport.DropsBackport;
import net.rebel459.unified.api.core.Supplied;
import net.rebel459.unified.api.core.UnifiedRegistries;

public class DBDataComponents {
    private static final UnifiedRegistries.DataComponentTypes COMPONENTS = UnifiedRegistries.DataComponentTypes.create(DropsBackport.VANILLA_ID);

    public static final Supplied<DataComponentType<DyeColor>> CUSHION_COLOR = COMPONENTS.register(
            "cushion/color", b -> b.persistent(DyeColor.CODEC).networkSynchronized(DyeColor.STREAM_CODEC)
    );

    public static void init() {}
}
