package net.rebel459.drops_backported.mixin.entity;

import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Attributes.class)
public class AttributesMixin {
    @ModifyVariable(
            method = "register",
            at = @At("HEAD"),
            argsOnly = true
    )
    private static Attribute modifyRegisteredAttribute(Attribute attribute, String name) {
        if (name.equals("knockback_resistance")) {
            return new RangedAttribute(
                    "attribute.name.knockback_resistance",
                    0.0,
                    -2.0,
                    2.0
            );
        }
        return attribute;
    }
}
