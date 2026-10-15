package net.rebel459.drops_backport.util;

import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.rebel459.drops_backport.DropsBackport;
import net.rebel459.drops_backport.registry.DBBlocks;
import net.rebel459.drops_backport.registry.DBItems;
import net.rebel459.unified.api.data.helper.CreativeEntryGenerator;
import net.rebel459.unified.api.registry.CreativeModeTabIds;

public class DBCreativeEntries {

    public static void init() {
        CreativeEntryGenerator.Builder entries = DropsBackport.DATA.helpers().creativeEntries().create("drops_backport_additions");

        entries.insertAfter(
                CreativeModeTabIds.NATURAL_BLOCKS,
                DBBlocks.SULFUR.getBase(),
                DBBlocks.SULFUR_SPIKE,
                DBBlocks.POTENT_SULFUR
        );
        entries.insertAfter(CreativeModeTabIds.NATURAL_BLOCKS, Blocks.BUSH, DBBlocks.RED_SHRUB);

        entries.insertAfter(CreativeModeTabIds.NATURAL_BLOCKS, Blocks.RED_MUSHROOM, DBBlocks.SHELF_MUSHROOM);

        entries.insertAfter(CreativeModeTabIds.FUNCTIONAL_BLOCKS, Items.ENDER_EYE, DBBlocks.STRAW_BED);

        entries.insertAfter(CreativeModeTabIds.TOOLS_AND_UTILITIES, Items.TADPOLE_BUCKET, DBItems.SULFUR_CUBE_BUCKET);
        entries.insertAfter(CreativeModeTabIds.TOOLS_AND_UTILITIES, Items.MUSIC_DISC_LAVA_CHICKEN, DBItems.MUSIC_DISC_BOUNCE);

        entries.insertAfter(CreativeModeTabIds.SPAWN_EGGS, Items.SNIFFER_SPAWN_EGG, DBItems.SULFUR_CUBE_SPAWN_EGG);
    }
}
