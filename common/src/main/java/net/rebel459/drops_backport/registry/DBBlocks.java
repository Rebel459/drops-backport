package net.rebel459.drops_backport.registry;

import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.rebel459.drops_backport.DropsBackport;
import net.rebel459.drops_backport.sound.DBSoundEvents;
import net.rebel459.drops_backport.sound.DBSoundTypes;
import net.rebel459.drops_backport.tag.DBBlockTags;
import net.rebel459.drops_backport.util.block.AmbientLeavesBlockSoundPlayer;
import net.rebel459.unified.api.asset.BlockAssets;
import net.rebel459.unified.api.codec.ExtensibleCodecs;
import net.rebel459.unified.api.core.SuppliedBlock;
import net.rebel459.unified.api.core.UnifiedData;
import net.rebel459.unified.api.data.registry.BlockGenerator;
import net.rebel459.unified.api.data.set.*;
import net.rebel459.unified.api.registry.UnifiedDataComponents;
import net.rebel459.unified.api.registry.VanillaBlockCodecs;
import net.rebel459.unified.api.util.BlockItemTagId;

public class DBBlocks {

    private static final BlockGenerator BLOCKS = DropsBackport.DATA.registries().blocks();
    private static final UnifiedData.Sets SETS = DropsBackport.DATA.sets();

    public static final ColoredBlockSet WOOL_STAIRS = SETS.coloredBlockSet("wool_stairs", ColoredBlockPreset.WOOL)
            .type(_ -> VanillaBlockCodecs.STAIRS.create(() -> Blocks.WHITE_WOOL))
            .creativeInventoryPlacement(() -> Blocks.PINK_WOOL)
            .builder((_, builder) -> builder.data(data -> data.tag(BlockTags.WOOL)))
            .build();
    public static final ColoredBlockSet WOOL_SLABS = SETS.coloredBlockSet("wool_slab", ColoredBlockPreset.WOOL)
            .type(_ -> VanillaBlockCodecs.SLAB.create())
            .creativeInventoryPlacement(WOOL_STAIRS.getPink())
            .builder((_, builder) -> builder
                    .itemProperties(properties -> properties.component(UnifiedDataComponents.FURNACE_FUEL.get(), 50))
                    .properties(properties -> properties.flammable(30, 60))
                    .data(data -> data.tag(BlockTags.WOOL))
            )
            .build();

    public static final ColoredBlockSet CONCRETE_STAIRS = SETS.coloredBlockSet("concrete_stairs", ColoredBlockPreset.CONCRETE)
            .type(_ -> VanillaBlockCodecs.STAIRS.create(() -> Blocks.WHITE_CONCRETE))
            .creativeInventoryPlacement(() -> Blocks.PINK_CONCRETE)
            .build();
    public static final ColoredBlockSet CONCRETE_SLABS = SETS.coloredBlockSet("concrete_slab", ColoredBlockPreset.CONCRETE)
            .type(_ -> VanillaBlockCodecs.SLAB.create())
            .creativeInventoryPlacement(CONCRETE_STAIRS.getPink())
            .build();

    public static final StoneSet CINNABAR = SETS.stoneSet("cinnabar", StonePreset.DEFAULT, MapColor.COLOR_RED)
            .creativeInventoryPlacement(() -> Blocks.CUT_RED_SANDSTONE_SLAB, () -> Blocks.PRISMARINE)
            .setSoundType(DBSoundTypes.CINNABAR)
            .hasChiseled(true)
            .build();
    public static final StoneSet POLISHED_CINNABAR = SETS.stoneSet("polished_cinnabar", StonePreset.DEFAULT, MapColor.COLOR_RED)
            .creativeInventoryPlacement(CINNABAR.getChiseled())
            .setSoundType(DBSoundTypes.CINNABAR)
            .build();
    public static final StoneSet CINNABAR_BRICKS = SETS.stoneSet("cinnabar_bricks", StonePreset.DEFAULT, MapColor.COLOR_RED)
            .creativeInventoryPlacement(POLISHED_CINNABAR.getWall())
            .setSoundType(DBSoundTypes.CINNABAR)
            .build();

    public static final StoneSet SULFUR = SETS.stoneSet("sulfur", StonePreset.DEFAULT, MapColor.COLOR_YELLOW)
            .creativeInventoryPlacement(CINNABAR_BRICKS.getWall(), CINNABAR.getBase())
            .setSoundType(DBSoundTypes.SULFUR)
            .hasChiseled(true)
            .build();
    public static final StoneSet POLISHED_SULFUR = SETS.stoneSet("polished_sulfur", StonePreset.DEFAULT, MapColor.COLOR_YELLOW)
            .creativeInventoryPlacement(SULFUR.getChiseled())
            .setSoundType(DBSoundTypes.SULFUR)
            .build();
    public static final StoneSet SULFUR_BRICKS = SETS.stoneSet("sulfur_bricks", StonePreset.DEFAULT, MapColor.COLOR_YELLOW)
            .creativeInventoryPlacement(SULFUR.getWall())
            .setSoundType(DBSoundTypes.SULFUR)
            .build();

    public static final SuppliedBlock POTENT_SULFUR = BLOCKS.register(
            "potent_sulfur",
            DBBlockCodecs.POTENT_SULFUR.create(),
            builder -> builder
                    .properties(properties -> properties
                            .copyFrom(SULFUR.getBase())
                            .soundType(DBSoundTypes.POTENT_SULFUR.get())
                            .mapColor(MapColor.GOLD)
                    )
                    .blockEntity(DBBlockEntityTypes.POTENT_SULFUR)
    );

    public static final WoodSet POPLAR = SETS.woodSet("poplar", WoodPreset.DEFAULT, BlockItemTagId.create(DropsBackport.vanillaId("poplar_logs")), MapColor.COLOR_BROWN, MapColor.COLOR_LIGHT_GRAY)
            .creativeInventoryPlacement(() -> Blocks.PALE_OAK_BUTTON, () -> Blocks.PALE_OAK_LOG, () -> Blocks.PALE_OAK_SHELF, () -> Blocks.PALE_OAK_HANGING_SIGN, () -> Items.PALE_OAK_CHEST_BOAT)
            .setLeavesSoundType(DBSoundTypes.POPLAR_LEAVES)
            .setWoodSoundType(() -> SoundType.WOOD)
            .createSapling(DBBlockCodecs.POPLAR_SAPLING.create(), MapColor.PLANT, BlockAssets.PlantType.NOT_TINTED, () -> Items.PALE_OAK_SAPLING)
            .createLeaves(
                    WoodSet.Leaves.variant(
                            "red",
                            DBBlockCodecs.UNTINTED_PARTICLE_LEAVES.create(() -> new DBBlockCodecs.ParticleLeaves(
                                    0.01F,
                                    DBParticleTypes.RED_POPLAR_LEAVES.get(),
                                    AmbientLeavesBlockSoundPlayer.of(DBSoundEvents.POPLAR_LEAVES_AMBIENT, DBBlockTags.REQUIRED_FOR_POPLAR_LEAF_AMBIENCE))
                            ),
                            MapColor.COLOR_RED,
                            BlockAssets.LEAVES,
                            (block, sapling, provider) -> provider.createLeavesDrops(block, sapling, 0.05F, 0.0625F, 0.083333336F, 0.1F),
                            () -> Items.PALE_OAK_LEAVES
                    ),
                    WoodSet.Leaves.variant(
                            "orange",
                            DBBlockCodecs.UNTINTED_PARTICLE_LEAVES.create(() -> new DBBlockCodecs.ParticleLeaves(
                                    0.01F,
                                    DBParticleTypes.ORANGE_POPLAR_LEAVES.get(),
                                    AmbientLeavesBlockSoundPlayer.of(DBSoundEvents.POPLAR_LEAVES_AMBIENT, DBBlockTags.REQUIRED_FOR_POPLAR_LEAF_AMBIENCE))
                            ),
                            MapColor.COLOR_ORANGE,
                            BlockAssets.LEAVES,
                            (block, sapling, provider) -> provider.createLeavesDrops(block, sapling, 0.05F, 0.0625F, 0.083333336F, 0.1F),
                            "red"
                    ),
                    WoodSet.Leaves.variant(
                            "yellow",
                            DBBlockCodecs.UNTINTED_PARTICLE_LEAVES.create(() -> new DBBlockCodecs.ParticleLeaves(
                                    0.01F,
                                    DBParticleTypes.YELLOW_POPLAR_LEAVES.get(),
                                    AmbientLeavesBlockSoundPlayer.of(DBSoundEvents.POPLAR_LEAVES_AMBIENT, DBBlockTags.REQUIRED_FOR_POPLAR_LEAF_AMBIENCE))
                            ),
                            MapColor.COLOR_YELLOW,
                            BlockAssets.LEAVES,
                            (block, sapling, provider) -> provider.createLeavesDrops(block, sapling, 0.05F, 0.0625F, 0.083333336F, 0.1F),
                            "orange"
                    )
            )
            .build();

    public static final SuppliedBlock STRAW_BED = BLOCKS.register(
            "straw_bed",
            DBBlockCodecs.STRAW_BED.create(),
            builder -> builder
                    .properties(properties -> properties
                            .mapColor(MapColor.COLOR_YELLOW)
                            .soundType(DBSoundTypes.STRAW_BED.get())
                            .strength(0.2F)
                            .occlusion(false)
                            .flammable(30, 60)
                            .pushReaction(PushReaction.DESTROY)
                    )
                    .itemProperties(properties -> properties
                            .stacksTo(16)
                    )
    );

    public static final SuppliedBlock RED_SHRUB = BLOCKS.register(
            "red_shrub",
            VanillaBlockCodecs.BUSH.create(),
            builder -> builder
                    .properties(properties -> properties
                            .mapColor(MapColor.CRIMSON_NYLIUM)
                            .replaceable(true)
                            .collision(false)
                            .occlusion(false)
                            .instabreak()
                            .soundType(DBSoundTypes.RED_SHRUB.get())
                            .flammable(60, 100)
                            .pushReaction(PushReaction.DESTROY)
                    )
                    .itemProperties(properties -> properties
                            .component(UnifiedDataComponents.COMPOST.get(), 0.3F)
                    )
    );

    public static final SuppliedBlock SHELF_MUSHROOM = BLOCKS.register(
            "shelf_mushroom",
            DBBlockCodecs.SHELF_MUSHROOM.create(),
            builder -> builder
                    .properties(properties -> properties
                            .mapColor(MapColor.TERRACOTTA_YELLOW)
                            .soundType(DBSoundTypes.SHELF_MUSHROOM.get())
                            .occlusion(false)
                            .pushReaction(PushReaction.DESTROY)
                    )
    );

    public static final SuppliedBlock SULFUR_SPIKE = BLOCKS.register(
            "sulfur_spike",
            DBBlockCodecs.SULFUR_SPIKE.create(SULFUR.getBase()),
            builder -> builder
                    .properties(properties -> properties
                            .mapColor(MapColor.COLOR_YELLOW)
                            .solid(true)
                            .instrument(NoteBlockInstrument.BASEDRUM)
                            .occlusion(false)
                            .soundType(DBSoundTypes.SULFUR_SPIKE.get())
                            .randomTicks(true)
                            .strength(1.5F, 3.0F)
                            .dynamicShape(true)
                            .offset(BlockBehaviour.OffsetType.XZ)
                            .pushReaction(PushReaction.DESTROY)
                            .redstoneConductor(ExtensibleCodecs.STATE_PREDICATE.never().create())
                    )
    );

    public static void init() {}
}
