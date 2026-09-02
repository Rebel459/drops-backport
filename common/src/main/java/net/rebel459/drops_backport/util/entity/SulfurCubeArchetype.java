package net.rebel459.drops_backport.util.entity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.valueproviders.FloatProvider;
import net.minecraft.util.valueproviders.FloatProviders;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.rebel459.drops_backport.sound.DBSoundEvents;
import net.rebel459.drops_backport.DropsBackport;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public record SulfurCubeArchetype(
        HolderSet<Item> items,
        List<SulfurCubeArchetype.AttributeEntry> attributeModifiers,
        boolean buoyant,
        Optional<SulfurCubeArchetype.ExplosionData> explosion,
        Optional<SulfurCubeArchetype.ContactDamage> contactDamage,
        SulfurCubeArchetype.KnockbackModifiers knockbackModifiers,
        SulfurCubeArchetype.SoundSettings soundSettings
) {
    public static final ResourceKey<Registry<SulfurCubeArchetype>> REGISTRY_KEY = ResourceKey.createRegistryKey(DropsBackport.vanillaId("sulfur_cube_archetype"));

    private static final Codec<HolderSet<Item>> ITEM_SET_CODEC = Codec.either(
            TagKey.hashedCodec(Registries.ITEM),
            ExtraCodecs.compactListCodec(BuiltInRegistries.ITEM.holderByNameCodec())).xmap(either -> either.map(
                    tag -> HolderSet.emptyNamed(BuiltInRegistries.ITEM, tag),
                    HolderSet::direct
            ), HolderSet::unwrap
    );

    public static final Codec<SulfurCubeArchetype> CODEC = RecordCodecBuilder.create(
            i -> i.group(
                            ITEM_SET_CODEC.fieldOf("items").forGetter(SulfurCubeArchetype::items),
                            SulfurCubeArchetype.AttributeEntry.CODEC.listOf().fieldOf("attribute_modifiers").forGetter(SulfurCubeArchetype::attributeModifiers),
                            Codec.BOOL.optionalFieldOf("buoyant", false).forGetter(SulfurCubeArchetype::buoyant),
                            SulfurCubeArchetype.ExplosionData.CODEC.optionalFieldOf("explosion").forGetter(SulfurCubeArchetype::explosion),
                            SulfurCubeArchetype.ContactDamage.CODEC.optionalFieldOf("contact_damage").forGetter(SulfurCubeArchetype::contactDamage),
                            SulfurCubeArchetype.KnockbackModifiers.CODEC.fieldOf("knockback_modifiers").forGetter(SulfurCubeArchetype::knockbackModifiers),
                            SulfurCubeArchetype.SoundSettings.CODEC.fieldOf("sound_settings").forGetter(SulfurCubeArchetype::soundSettings)
                    )
                    .apply(i, SulfurCubeArchetype::new)
    );

    public static final KnockbackModifiers DEFAULT_KNOCKBACK_MODIFIERS = new KnockbackModifiers(0.33F, 0.06F);

    public static final SoundSettings DEFAULT_SOUND_SETTINGS = new SoundSettings(DBSoundEvents.SULFUR_CUBE_REGULAR_HIT, DBSoundEvents.SULFUR_CUBE_REGULAR_PUSH, 0.2F, 0.5F);

    public static final List<SulfurCubeArchetype> REGISTERED = new ArrayList<>();

    public static List<SulfurCubeArchetype> matching(ItemStack stack) {
        return REGISTERED.stream()
                .filter(archetype -> archetype.items().unwrapKey()
                        .map(stack::is)
                        .orElseGet(() -> stack.is(archetype.items())))
                .toList();
    }

    public record AttributeEntry(Holder<Attribute> attribute, AttributeModifier modifier) {
        public static final Codec<AttributeEntry> CODEC = RecordCodecBuilder.create(
                i -> i.group(
                                Attribute.CODEC.fieldOf("attribute").forGetter(SulfurCubeArchetype.AttributeEntry::attribute),
                                AttributeModifier.MAP_CODEC.forGetter(SulfurCubeArchetype.AttributeEntry::modifier)
                        )
                        .apply(i, SulfurCubeArchetype.AttributeEntry::new)
        );

        public static SulfurCubeArchetype.AttributeEntry add(final Holder<Attribute> attribute, final double amount, final ResourceKey<SulfurCubeArchetype> archetype) {
            return new SulfurCubeArchetype.AttributeEntry(
                    attribute,
                    new AttributeModifier(
                            Identifier.withDefaultNamespace(archetype.identifier().getPath() + "_add_" + attribute.unwrapKey().get().identifier().getPath()),
                            amount,
                            AttributeModifier.Operation.ADD_VALUE
                    )
            );
        }

        public static SulfurCubeArchetype.AttributeEntry multiply(final Holder<Attribute> attribute, final double amount, final ResourceKey<SulfurCubeArchetype> archetype) {
            return new SulfurCubeArchetype.AttributeEntry(
                    attribute,
                    new AttributeModifier(
                            Identifier.withDefaultNamespace(archetype.identifier().getPath() + "_mul_" + attribute.unwrapKey().get().identifier().getPath()),
                            amount - 1.0,
                            AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
                    )
            );
        }
    }

    public record ExplosionData(int power, boolean causesFire, int fuse) {
        public static final Codec<SulfurCubeArchetype.ExplosionData> CODEC = RecordCodecBuilder.create(
                i -> i.group(
                                ExtraCodecs.NON_NEGATIVE_INT.fieldOf("power").forGetter(SulfurCubeArchetype.ExplosionData::power),
                                Codec.BOOL.fieldOf("causes_fire").forGetter(SulfurCubeArchetype.ExplosionData::causesFire),
                                ExtraCodecs.POSITIVE_INT.fieldOf("fuse").forGetter(SulfurCubeArchetype.ExplosionData::fuse)
                        )
                        .apply(i, SulfurCubeArchetype.ExplosionData::new)
        );
    }

    public record ContactDamage(ResourceKey<DamageType> damageType, FloatProvider amount, boolean attributeToSource) {
        public static final Codec<SulfurCubeArchetype.ContactDamage> CODEC = RecordCodecBuilder.create(
                i -> i.group(
                                ResourceKey.codec(Registries.DAMAGE_TYPE).fieldOf("damage_type").forGetter(SulfurCubeArchetype.ContactDamage::damageType),
                                FloatProviders.codec(0.0F, Float.MAX_VALUE).fieldOf("amount").forGetter(SulfurCubeArchetype.ContactDamage::amount),
                                Codec.BOOL.fieldOf("attribute_to_source").forGetter(SulfurCubeArchetype.ContactDamage::attributeToSource)
                        )
                        .apply(i, SulfurCubeArchetype.ContactDamage::new)
        );
    }

    public record KnockbackModifiers(float horizontalPower, float verticalPower) {
        public static final Codec<SulfurCubeArchetype.KnockbackModifiers> CODEC = RecordCodecBuilder.create(
                i -> i.group(
                                Codec.FLOAT.fieldOf("horizontal_power").forGetter(SulfurCubeArchetype.KnockbackModifiers::horizontalPower),
                                Codec.FLOAT.fieldOf("vertical_power").forGetter(SulfurCubeArchetype.KnockbackModifiers::verticalPower)
                        )
                        .apply(i, SulfurCubeArchetype.KnockbackModifiers::new)
        );
    }

    public record SoundSettings(Holder<SoundEvent> hitSound, Holder<SoundEvent> pushSound, float pushSoundImpulseThreshold, float pushSoundCooldown) {
        public static final Codec<SulfurCubeArchetype.SoundSettings> CODEC = RecordCodecBuilder.create(
                i -> i.group(
                                BuiltInRegistries.SOUND_EVENT.holderByNameCodec().fieldOf("hit_sound").forGetter(SulfurCubeArchetype.SoundSettings::hitSound),
                                BuiltInRegistries.SOUND_EVENT.holderByNameCodec().fieldOf("push_sound").forGetter(SulfurCubeArchetype.SoundSettings::pushSound),
                                Codec.FLOAT.fieldOf("push_sound_impulse_threshold").forGetter(SulfurCubeArchetype.SoundSettings::pushSoundImpulseThreshold),
                                Codec.FLOAT.fieldOf("push_sound_cooldown").forGetter(SulfurCubeArchetype.SoundSettings::pushSoundCooldown)
                        )
                        .apply(i, SulfurCubeArchetype.SoundSettings::new)
        );
    }
}
