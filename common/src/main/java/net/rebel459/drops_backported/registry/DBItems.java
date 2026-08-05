package net.rebel459.drops_backported.registry;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.MobBucketItem;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.material.Fluids;
import net.rebel459.drops_backported.DropsBackported;
import net.rebel459.unified.platform.UnifiedRegistries;
import net.rebel459.unified.util.registry.SuppliedItem;

public final class DBItems {
    private static final UnifiedRegistries.Items ITEMS = UnifiedRegistries.Items.create(DropsBackported.VANILLA_ID);

    public static final SuppliedItem SULFUR_CUBE_BUCKET = ITEMS.register(
        "sulfur_cube_bucket",
        p -> new MobBucketItem(DBEntityTypes.SULFUR_CUBE.get(), Fluids.EMPTY, DBSoundEvents.BUCKET_EMPTY_SULFUR_CUBE.get(), p),
        () -> new Item.Properties().stacksTo(1).component(DataComponents.BUCKET_ENTITY_DATA, CustomData.EMPTY)
    );
    public static final SuppliedItem SULFUR_CUBE_SPAWN_EGG = ITEMS.register(
        "sulfur_cube_spawn_egg",
        SpawnEggItem::new,
        () -> new Item.Properties().spawnEgg(DBEntityTypes.SULFUR_CUBE.get())
    );

    private DBItems() {}

    public static void init() {}
}
