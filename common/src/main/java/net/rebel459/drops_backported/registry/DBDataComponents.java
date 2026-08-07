package net.rebel459.drops_backported.registry;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.item.DyeColor;
import net.rebel459.drops_backported.DropsBackported;
import net.rebel459.unified.platform.UnifiedRegistries;
import net.rebel459.unified.util.registry.Supplied;

public class DBDataComponents {

    public static UnifiedRegistries.DataComponentTypes DATA_COMPONENTS = UnifiedRegistries.DataComponentTypes.create(DropsBackported.VANILLA_ID);

    public static final Supplied<DataComponentType<DyeColor>> CUSHION_COLOR = DATA_COMPONENTS.register(
            "cushion/color", b -> b.persistent(DyeColor.CODEC).networkSynchronized(DyeColor.STREAM_CODEC)
    );
}
