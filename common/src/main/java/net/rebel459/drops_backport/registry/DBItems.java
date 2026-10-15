package net.rebel459.drops_backport.registry;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.block.Blocks;
import net.rebel459.drops_backport.DropsBackport;
import net.rebel459.drops_backport.sound.DBJukeboxSongs;
import net.rebel459.drops_backport.sound.DBSoundEvents;
import net.rebel459.unified.api.core.SuppliedItem;
import net.rebel459.unified.api.core.UnifiedData;
import net.rebel459.unified.api.data.registry.ItemGenerator;
import net.rebel459.unified.api.data.set.ColoredItemPreset;
import net.rebel459.unified.api.data.set.ColoredItemSet;
import net.rebel459.unified.api.registry.CreativeModeTabIds;
import net.rebel459.unified.api.registry.VanillaItemCodecs;

public final class DBItems {
    private static final ItemGenerator ITEMS = DropsBackport.DATA.registries().items();
    private static final UnifiedData.Sets SETS = DropsBackport.DATA.sets();

    public static final SuppliedItem SULFUR_CUBE_BUCKET = ITEMS.register(
            "sulfur_cube_bucket",
            DBItemCodecs.SULFUR_CUBE_BUCKET.create(DBSoundEvents.BUCKET_EMPTY_SULFUR_CUBE),
            builder -> builder
                    .properties(properties -> properties
                            .stacksTo(1)
                            .component(DataComponents.BUCKET_ENTITY_DATA, CustomData.EMPTY)
                    )
    );
    public static final SuppliedItem SULFUR_CUBE_SPAWN_EGG = ITEMS.register(
            "sulfur_cube_spawn_egg",
            VanillaItemCodecs.SPAWN_EGG.create(),
            builder -> builder
                    .properties(properties -> properties
                            .spawnEgg(DBEntities.SULFUR_CUBE.get())
                    )
    );
    public static final SuppliedItem MUSIC_DISC_BOUNCE = ITEMS.register(
            "music_disc_bounce",
            VanillaItemCodecs.ITEM.create(),
            builder -> builder
                    .properties(properties -> properties
                            .stacksTo(1)
                            .rarity(Rarity.UNCOMMON)
                            .jukeboxPlayable(DBJukeboxSongs.BOUNCE)
                    )
                    .assets(assets -> assets
                            .name("Music Disc")
                    )
    );

    public static final ColoredItemSet CUSHION = SETS.coloredItemSet("cushion", ColoredItemPreset.DEFAULT)
            .creativeInventoryPlacement(CreativeModeTabIds.FUNCTIONAL_BLOCKS, () -> Blocks.PINK_BED)
            .type(_ -> DBItemCodecs.CUSHION.create())
            .builder((color, builder) -> builder
                    .properties(properties -> properties
                            .stacksTo(16)
                            .component(DBDataComponents.CUSHION_COLOR.get(), color)
                    )
            )
            .build();

    private DBItems() {}

    public static void init() {}
}
