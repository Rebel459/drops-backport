package net.rebel459.drops_backported.registry;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.rebel459.drops_backported.DropsBackported;
import net.rebel459.unified.platform.UnifiedRegistries;

public final class DBAttributes {
    private static final UnifiedRegistries.DeferredRegistry<Attribute> ATTRIBUTES = UnifiedRegistries.DeferredRegistry.create(DropsBackported.VANILLA_ID, BuiltInRegistries.ATTRIBUTE);

    public static final Holder<Attribute> AIR_DRAG_MODIFIER = ATTRIBUTES.registerForHolder(
            "air_drag_modifier",
            () -> new RangedAttribute("attribute.minecraft.air_drag_modifier", 1.0, 0.0, 2048.0).setSyncable(true)
    );
    public static final Holder<Attribute> BOUNCINESS = ATTRIBUTES.registerForHolder(
            "bounciness",
            () -> new RangedAttribute("attribute.minecraft.bounciness", 0.0, 0.0, 1.0).setSyncable(true)
    );
    public static final Holder<Attribute> FRICTION_MODIFIER = ATTRIBUTES.registerForHolder(
            "friction_modifier",
            () -> new RangedAttribute("attribute.minecraft.friction_modifier", 1.0, 0.0, 2048.0).setSyncable(true)
    );

    private DBAttributes() {
    }

    public static void init() {
    }
}
