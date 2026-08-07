package net.rebel459.drops_backport.registry;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.block.Blocks;
import net.rebel459.drops_backport.DropsBackport;
import net.rebel459.drops_backport.item.CushionItem;
import net.rebel459.drops_backport.item.SulfurCubeBucketItem;
import net.rebel459.drops_backport.sound.DBJukeboxSongs;
import net.rebel459.drops_backport.sound.DBSoundEvents;
import net.rebel459.unified.platform.UnifiedRegistries;
import net.rebel459.unified.util.CreativeModeTabs;
import net.rebel459.unified.util.builder.ColoredItemPreset;
import net.rebel459.unified.util.builder.ColoredItemSet;
import net.rebel459.unified.util.registry.SuppliedItem;

public final class DBItems {
    private static final UnifiedRegistries.Items ITEMS = UnifiedRegistries.Items.create(DropsBackport.VANILLA_ID);
    private static final UnifiedRegistries.Items.Builders ITEM_BUILDERS = ITEMS.builders();

    public static final SuppliedItem SULFUR_CUBE_BUCKET = ITEMS.register(
            "sulfur_cube_bucket",
            p -> new SulfurCubeBucketItem(DBSoundEvents.BUCKET_EMPTY_SULFUR_CUBE.get(), p),
            () -> new Item.Properties()
                    .stacksTo(1)
                    .component(DataComponents.BUCKET_ENTITY_DATA, CustomData.EMPTY)
    );
    public static final SuppliedItem SULFUR_CUBE_SPAWN_EGG = ITEMS.register(
            "sulfur_cube_spawn_egg",
            SpawnEggItem::new,
            () -> new Item.Properties()
                    .spawnEgg(DBEntityTypes.SULFUR_CUBE.get())
    );
    public static final SuppliedItem MUSIC_DISC_BOUNCE = ITEMS.register(
            "music_disc_bounce",
            Item::new,
            () -> new Item.Properties()
                    .stacksTo(1)
                    .rarity(Rarity.UNCOMMON)
                    .jukeboxPlayable(DBJukeboxSongs.BOUNCE)
    );

    public static final ColoredItemSet CUSHION = ITEM_BUILDERS.coloredItemSet("cushion", ColoredItemPreset.DEFAULT)
            .creativeInventoryPlacement(CreativeModeTabs.FUNCTIONAL_BLOCKS, () -> Blocks.PINK_BED)
            .function(CushionItem::new)
            .build();

    private DBItems() {
    }

    public static void init() {
    }
}
