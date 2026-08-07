package net.rebel459.drops_backported.util.entity;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.util.valueproviders.FloatProvider;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.rebel459.drops_backported.sound.DBSoundEvents;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public record SulfurCubeArchetype(
        Identifier id,
        TagKey<Item> items,
        float speed,
        float bounce,
        float friction,
        float airDrag,
        boolean buoyant,
        Optional<ExplosionData> explosion,
        Optional<ContactDamage> contactDamage,
        KnockbackModifiers knockbackModifiers,
        SoundSettings soundSettings
) {
    public static final KnockbackModifiers DEFAULT_KNOCKBACK_MODIFIERS = new KnockbackModifiers(0.33F, 0.06F);
    public static final SoundSettings DEFAULT_SOUND_SETTINGS = new SoundSettings(DBSoundEvents.SULFUR_CUBE_REGULAR_HIT, DBSoundEvents.SULFUR_CUBE_REGULAR_PUSH, 0.2F, 0.5F);
    static final List<SulfurCubeArchetype> REGISTERED = new ArrayList<>();

    static List<SulfurCubeArchetype> matching(ItemStack stack) {
        return REGISTERED.stream().filter(archetype -> stack.is(archetype.items())).toList();
    }

    public static SulfurCubeArchetype register(Identifier id, float speed, float bounce, float friction, float airDrag, boolean buoyant, Optional<ExplosionData> explosion, Optional<ContactDamage> contactDamage, KnockbackModifiers knockbackModifiers, SoundSettings soundSettings) {
        SulfurCubeArchetype archetype = new SulfurCubeArchetype(id, TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(id.getNamespace(), "sulfur_cube_archetype/" + id.getPath())), speed, bounce, friction, airDrag, buoyant, explosion, contactDamage, knockbackModifiers, soundSettings);
        ;
        REGISTERED.add(archetype);
        return archetype;
    }

    public record ExplosionData(int power, boolean causesFire, int fuse) {
    }

    public record ContactDamage(ResourceKey<DamageType> damageType, FloatProvider amount, boolean attributeToSource) {
    }

    public record KnockbackModifiers(float horizontalPower, float verticalPower) {
    }

    public record SoundSettings(Holder<SoundEvent> hitSound, Holder<SoundEvent> pushSound,
                                float pushSoundImpulseThreshold, float pushSoundCooldown) {
    }
}
