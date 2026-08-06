package net.rebel459.drops_backported.registry;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.rebel459.drops_backported.DropsBackported;
import net.rebel459.drops_backported.block.potent_sulfur.PotentSulfurBlock;
import net.rebel459.drops_backported.sound.DBSoundTypes;
import net.rebel459.unified.platform.UnifiedRegistries;
import net.rebel459.unified.util.builder.BlockPreset;
import net.rebel459.unified.util.builder.BlockSet;
import net.rebel459.unified.util.builder.ColoredBlockPreset;
import net.rebel459.unified.util.builder.ColoredBlockSet;
import net.rebel459.unified.util.registry.SuppliedBlock;

public class DBBlocks {

    public static UnifiedRegistries.Blocks BLOCKS = UnifiedRegistries.Blocks.create(DropsBackported.VANILLA_ID);
    public static UnifiedRegistries.Blocks.Builders BLOCK_BUILDERS = BLOCKS.builders();

    public static final ColoredBlockSet WOOL_STAIRS = BLOCK_BUILDERS.coloredBlockSet("wool_stairs", ColoredBlockPreset.WOOL)
            .function(properties -> new StairBlock(Blocks.WHITE_WOOL.defaultBlockState(), properties))
            .creativeInventoryPlacement(() -> Blocks.PINK_WOOL)
            .build();
    public static final ColoredBlockSet WOOL_SLABS = BLOCK_BUILDERS.coloredBlockSet("wool_slab", ColoredBlockPreset.WOOL)
            .function(SlabBlock::new)
            .creativeInventoryPlacement(WOOL_STAIRS.getPink())
            .build();

    public static final ColoredBlockSet CONCRETE_STAIRS = BLOCK_BUILDERS.coloredBlockSet("concrete_stairs", ColoredBlockPreset.CONCRETE)
            .function(properties -> new StairBlock(Blocks.WHITE_CONCRETE.defaultBlockState(), properties))
            .creativeInventoryPlacement(() -> Blocks.PINK_CONCRETE)
            .build();
    public static final ColoredBlockSet CONCRETE_SLABS = BLOCK_BUILDERS.coloredBlockSet("concrete_slab", ColoredBlockPreset.CONCRETE)
            .function(SlabBlock::new)
            .creativeInventoryPlacement(CONCRETE_STAIRS.getPink())
            .build();

    public static final BlockSet CINNABAR = BLOCK_BUILDERS.blockSet("cinnabar", BlockPreset.DEFAULT, MapColor.COLOR_RED)
            .creativeInventoryPlacement(() -> Blocks.CUT_RED_SANDSTONE_SLAB)
            .setSoundType(DBSoundTypes.CINNABAR)
            .hasChiseled(true)
            .build();
    public static final BlockSet POLISHED_CINNABAR = BLOCK_BUILDERS.blockSet("polished_cinnabar", BlockPreset.DEFAULT, MapColor.COLOR_RED)
            .creativeInventoryPlacement(CINNABAR.getChiseled())
            .setSoundType(DBSoundTypes.CINNABAR)
            .build();
    public static final BlockSet CINNABAR_BRICKS = BLOCK_BUILDERS.blockSet("cinnabar_bricks", BlockPreset.DEFAULT, MapColor.COLOR_RED)
            .creativeInventoryPlacement(POLISHED_CINNABAR.getWall())
            .setSoundType(DBSoundTypes.CINNABAR)
            .build();

    public static final BlockSet SULFUR = BLOCK_BUILDERS.blockSet("sulfur", BlockPreset.DEFAULT, MapColor.COLOR_YELLOW)
            .creativeInventoryPlacement(CINNABAR_BRICKS.getWall())
            .setSoundType(DBSoundTypes.SULFUR)
            .hasChiseled(true)
            .build();
    public static final BlockSet POLISHED_SULFUR = BLOCK_BUILDERS.blockSet("polished_sulfur", BlockPreset.DEFAULT, MapColor.COLOR_YELLOW)
            .creativeInventoryPlacement(SULFUR.getChiseled())
            .setSoundType(DBSoundTypes.SULFUR)
            .build();
    public static final BlockSet SULFUR_BRICKS = BLOCK_BUILDERS.blockSet("sulfur_bricks", BlockPreset.DEFAULT, MapColor.COLOR_YELLOW)
            .creativeInventoryPlacement(SULFUR.getWall())
            .setSoundType(DBSoundTypes.SULFUR)
            .build();

    public static final SuppliedBlock POTENT_SULFUR = BLOCKS.register("potent_sulfur",
            PotentSulfurBlock::new,
            () -> BlockBehaviour.Properties.ofFullCopy(SULFUR.getBase().get()).sound(DBSoundTypes.POTENT_SULFUR.get())
                    .mapColor(MapColor.GOLD)
    );

    public static void init() {}
}
