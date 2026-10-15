package net.rebel459.drops_backport.registry;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.Item;
import net.rebel459.drops_backport.DropsBackport;
import net.rebel459.drops_backport.item.CushionItem;
import net.rebel459.drops_backport.item.SulfurCubeBucketItem;
import net.rebel459.unified.api.codec.ExtensibleCodecs;
import net.rebel459.unified.api.codec.ExtensibleItemCodec;

import java.util.function.BiFunction;
import java.util.function.Function;

public class DBItemCodecs {

	private static ExtensibleItemCodec.Simple simple(String id, Function<Item.Properties, Item> factory) {
		return ExtensibleCodecs.ITEM.register(DropsBackport.modId(id), () -> factory);
	}

	private static <T> ExtensibleItemCodec.Complex<T> complex(String id, MapCodec<T> codec, BiFunction<T, Item.Properties, Item> factory) {
		return ExtensibleCodecs.ITEM.register(DropsBackport.modId(id), codec, (definition) -> (properties) -> factory.apply(definition, properties));
	}

	public static final ExtensibleItemCodec.Simple CUSHION = simple("cushion", CushionItem::new);

	public static final ExtensibleItemCodec.Complex<SoundEvent> SULFUR_CUBE_BUCKET = complex("sulfur_cube_bucket", BuiltInRegistries.SOUND_EVENT.byNameCodec().fieldOf("empty_sound"), SulfurCubeBucketItem::new);

	public static void init() {}
}