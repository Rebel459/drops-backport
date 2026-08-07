package net.rebel459.drops_backported.util;

import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.rebel459.drops_backported.registry.DBBlocks;
import net.rebel459.drops_backported.registry.DBItems;
import net.rebel459.unified.platform.UnifiedHelpers;
import net.rebel459.unified.util.CreativeModeTabs;

public class DBCreativeEntries {

    public static void init() {
        UnifiedHelpers.CREATIVE_ENTRIES.insertAfter(
                CreativeModeTabs.NATURAL_BLOCKS,
                DBBlocks.SULFUR.getBase(),
                DBBlocks.SULFUR_SPIKE,
                DBBlocks.POTENT_SULFUR
        );
        UnifiedHelpers.CREATIVE_ENTRIES.insertAfter(CreativeModeTabs.NATURAL_BLOCKS, Blocks.BUSH, DBBlocks.RED_SHRUB);
        UnifiedHelpers.CREATIVE_ENTRIES.insertAfter(
                CreativeModeTabs.NATURAL_BLOCKS,
                Blocks.PALE_OAK_LEAVES,
                DBBlocks.RED_POPLAR_LEAVES,
                DBBlocks.ORANGE_POPLAR_LEAVES,
                DBBlocks.YELLOW_POPLAR_LEAVES
        );
        UnifiedHelpers.CREATIVE_ENTRIES.insertAfter(CreativeModeTabs.NATURAL_BLOCKS, Blocks.RED_MUSHROOM, DBBlocks.SHELF_MUSHROOM);

        UnifiedHelpers.CREATIVE_ENTRIES.insertAfter(CreativeModeTabs.FUNCTIONAL_BLOCKS, Items.ENDER_EYE, DBBlocks.STRAW_BED);

        UnifiedHelpers.CREATIVE_ENTRIES.insertAfter(CreativeModeTabs.TOOLS_AND_UTILITIES, Items.TADPOLE_BUCKET, DBItems.SULFUR_CUBE_BUCKET);
        UnifiedHelpers.CREATIVE_ENTRIES.insertAfter(CreativeModeTabs.TOOLS_AND_UTILITIES, Items.MUSIC_DISC_LAVA_CHICKEN, DBItems.MUSIC_DISC_BOUNCE);

        UnifiedHelpers.CREATIVE_ENTRIES.insertAfter(CreativeModeTabs.SPAWN_EGGS, Items.SNIFFER_SPAWN_EGG, DBItems.SULFUR_CUBE_SPAWN_EGG);
    }
}
