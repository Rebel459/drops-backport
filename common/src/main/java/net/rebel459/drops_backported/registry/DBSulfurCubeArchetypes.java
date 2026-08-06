package net.rebel459.drops_backported.registry;

import java.util.Optional;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.valueproviders.ConstantFloat;
import net.rebel459.drops_backported.DropsBackported;
import net.rebel459.drops_backported.entity.sulfur_cube.SulfurCubeArchetype;
import net.rebel459.drops_backported.sound.DBSoundEvents;

public final class DBSulfurCubeArchetypes {
    public static final SulfurCubeArchetype REGULAR = SulfurCubeArchetype.register(
        DropsBackported.vanillaId("regular"),
        1.0F,
        0.5F,
        0.3F,
        0.1F,
        true,
        Optional.empty(),
        Optional.empty(),
        new SulfurCubeArchetype.KnockbackModifiers(0.4125F, 0.09F),
        SulfurCubeArchetype.DEFAULT_SOUND_SETTINGS
    );
    public static final SulfurCubeArchetype BOUNCY = SulfurCubeArchetype.register(
        DropsBackported.vanillaId("bouncy"),
        2.0F,
        0.9F,
        0.3F,
        0.01F,
        true,
        Optional.empty(),
        Optional.empty(),
        new SulfurCubeArchetype.KnockbackModifiers(0.4125F, 0.105F),
        new SulfurCubeArchetype.SoundSettings(DBSoundEvents.SULFUR_CUBE_BOUNCY_HIT, DBSoundEvents.SULFUR_CUBE_BOUNCY_PUSH, 0.3F, 0.7F)
    );
    public static final SulfurCubeArchetype SLOW_BOUNCY = SulfurCubeArchetype.register(
        DropsBackported.vanillaId("slow_bouncy"),
        -0.4F,
        0.6F,
        0.3F,
        0.05F,
        false,
        Optional.empty(),
        Optional.empty(),
        new SulfurCubeArchetype.KnockbackModifiers(0.4125F, 0.24F),
        new SulfurCubeArchetype.SoundSettings(DBSoundEvents.SULFUR_CUBE_SLOW_BOUNCY_HIT, DBSoundEvents.SULFUR_CUBE_SLOW_BOUNCY_PUSH, 0.05F, 0.5F)
    );
    public static final SulfurCubeArchetype SLOW_FLAT = SulfurCubeArchetype.register(
        DropsBackported.vanillaId("slow_flat"),
        -0.5F,
        0.4F,
        0.4F,
        0.1F,
        false,
        Optional.empty(),
        Optional.empty(),
        new SulfurCubeArchetype.KnockbackModifiers(0.4125F, 0.105F),
        new SulfurCubeArchetype.SoundSettings(DBSoundEvents.SULFUR_CUBE_SLOW_FLAT_HIT, DBSoundEvents.SULFUR_CUBE_SLOW_FLAT_PUSH, 0.03F, 0.9F)
    );
    public static final SulfurCubeArchetype FAST_FLAT = SulfurCubeArchetype.register(
        DropsBackported.vanillaId("fast_flat"),
        1.0F,
        0.5F,
        0.2F,
        0.01F,
        false,
        Optional.empty(),
        Optional.empty(),
        new SulfurCubeArchetype.KnockbackModifiers(0.9125F, 0.09F),
        new SulfurCubeArchetype.SoundSettings(DBSoundEvents.SULFUR_CUBE_FAST_FLAT_HIT, DBSoundEvents.SULFUR_CUBE_FAST_FLAT_PUSH, 0.03F, 0.9F)
    );
    public static final SulfurCubeArchetype LIGHT = SulfurCubeArchetype.register(
        DropsBackported.vanillaId("light"),
        1.0F,
        1.0F,
        0.3F,
        1.8F,
        true,
        Optional.empty(),
        Optional.empty(),
        new SulfurCubeArchetype.KnockbackModifiers(0.4125F, 0.18F),
        new SulfurCubeArchetype.SoundSettings(DBSoundEvents.SULFUR_CUBE_LIGHT_HIT, DBSoundEvents.SULFUR_CUBE_LIGHT_PUSH, 0.2F, 0.7F)
    );
    public static final SulfurCubeArchetype FAST_SLIDING = SulfurCubeArchetype.register(
        DropsBackported.vanillaId("fast_sliding"),
        -0.5F,
        0.1F,
        0.05F,
        0.01F,
        false,
        Optional.empty(),
        Optional.empty(),
        new SulfurCubeArchetype.KnockbackModifiers(0.6625F, 0.09F),
        new SulfurCubeArchetype.SoundSettings(DBSoundEvents.SULFUR_CUBE_FAST_SLIDING_HIT, DBSoundEvents.SULFUR_CUBE_FAST_SLIDING_PUSH, 0.05F, 1.0F)
    );
    public static final SulfurCubeArchetype SLOW_SLIDING = SulfurCubeArchetype.register(
        DropsBackported.vanillaId("slow_sliding"),
        -0.8F,
        0.1F,
        0.05F,
        0.01F,
        false,
        Optional.empty(),
        Optional.empty(),
        new SulfurCubeArchetype.KnockbackModifiers(0.4125F, 0.09F),
        new SulfurCubeArchetype.SoundSettings(DBSoundEvents.SULFUR_CUBE_SLOW_SLIDING_HIT, DBSoundEvents.SULFUR_CUBE_SLOW_SLIDING_PUSH, 0.02F, 1.0F)
    );
    public static final SulfurCubeArchetype STICKY = SulfurCubeArchetype.register(
        DropsBackported.vanillaId("sticky"),
        2.0F,
        0.0F,
        2.0F,
        0.01F,
        false,
        Optional.empty(),
        Optional.empty(),
        new SulfurCubeArchetype.KnockbackModifiers(0.4125F, 0.09F),
        new SulfurCubeArchetype.SoundSettings(DBSoundEvents.SULFUR_CUBE_STICKY_HIT, DBSoundEvents.SULFUR_CUBE_STICKY_PUSH, 0.05F, 0.5F)
    );
    public static final SulfurCubeArchetype HIGH_RESISTANCE = SulfurCubeArchetype.register(
        DropsBackported.vanillaId("high_resistance"),
        -0.7F,
        0.2F,
        1.0F,
        0.01F,
        false,
        Optional.empty(),
        Optional.empty(),
        new SulfurCubeArchetype.KnockbackModifiers(0.4125F, 0.09F),
        new SulfurCubeArchetype.SoundSettings(DBSoundEvents.SULFUR_CUBE_HIGH_RESISTANCE_HIT, DBSoundEvents.SULFUR_CUBE_HIGH_RESISTANCE_PUSH, 0.03F, 0.7F)
    );
    public static final SulfurCubeArchetype EXPLOSIVE = SulfurCubeArchetype.register(
        DropsBackported.vanillaId("explosive"),
        1.0F,
        0.5F,
        0.3F,
        0.3F,
        true,
        Optional.of(new SulfurCubeArchetype.ExplosionData(3, false, 120)),
        Optional.empty(),
        new SulfurCubeArchetype.KnockbackModifiers(0.4125F, 0.09F),
        new SulfurCubeArchetype.SoundSettings(DBSoundEvents.SULFUR_CUBE_EXPLOSIVE_HIT, DBSoundEvents.SULFUR_CUBE_EXPLOSIVE_PUSH, 0.1F, 0.7F)
    );
    public static final SulfurCubeArchetype HOT = SulfurCubeArchetype.register(
        DropsBackported.vanillaId("hot"),
        1.0F,
        0.5F,
        0.3F,
        0.1F,
        true,
        Optional.empty(),
        Optional.of(new SulfurCubeArchetype.ContactDamage(ResourceKey.create(Registries.DAMAGE_TYPE, Identifier.withDefaultNamespace("sulfur_cube_hot")), ConstantFloat.of(1.0F), false)),
        new SulfurCubeArchetype.KnockbackModifiers(0.4125F, 0.09F),
        new SulfurCubeArchetype.SoundSettings(DBSoundEvents.SULFUR_CUBE_HOT_HIT, DBSoundEvents.SULFUR_CUBE_HOT_PUSH, 0.2F, 0.7F)
    );

    private DBSulfurCubeArchetypes() {}

    public static void init() {}
}
