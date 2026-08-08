package net.rebel459.drops_backport.registry;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.rebel459.drops_backport.DropsBackport;
import net.rebel459.drops_backport.block.PoplarSaplingBlock;
import net.rebel459.drops_backport.block.ShelfMushroomBlock;
import net.rebel459.drops_backport.util.block.AmbientLeavesBlockSoundPlayer;
import net.rebel459.drops_backport.block.PotentSulfurBlock;
import net.rebel459.drops_backport.block.bed.StrawBedBlock;
import net.rebel459.drops_backport.block.speleothem.SulfurSpikeBlock;
import net.rebel459.drops_backport.sound.DBSoundEvents;
import net.rebel459.drops_backport.sound.DBSoundTypes;
import net.rebel459.drops_backport.tag.DBBlockTags;
import net.rebel459.drops_backport.block.leaves.UntintedParticleLeavesBlock;
import net.rebel459.unified.platform.UnifiedHelpers;
import net.rebel459.unified.platform.UnifiedRegistries;
import net.rebel459.unified.util.builder.*;
import net.rebel459.unified.util.registry.SuppliedBlock;

import java.util.function.Supplier;

public class DBBlocks {

    public static UnifiedRegistries.Blocks BLOCKS = UnifiedRegistries.Blocks.create(DropsBackport.VANILLA_ID);
    public static UnifiedRegistries.Blocks.Builders BLOCK_BUILDERS = BLOCKS.builders();

    public static final ColoredBlockSet WOOL_STAIRS = BLOCK_BUILDERS.coloredBlockSet("wool_stairs", ColoredBlockPreset.WOOL)
            .function(properties -> new StairBlock(Blocks.WHITE_WOOL.defaultBlockState(), properties))
            .creativeInventoryPlacement(() -> Blocks.PINK_WOOL)
            .build();
    public static final ColoredBlockSet WOOL_SLABS = BLOCK_BUILDERS.coloredBlockSet("wool_slab", ColoredBlockPreset.WOOL)
            .function(SlabBlock::new)
            .creativeInventoryPlacement(WOOL_STAIRS.getPink())
            .setFlammability(30, 60, 50)
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
            .creativeInventoryPlacement(() -> Blocks.CUT_RED_SANDSTONE_SLAB, () -> Blocks.PRISMARINE)
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
            .creativeInventoryPlacement(CINNABAR_BRICKS.getWall(), CINNABAR.getBase())
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
                    .mapColor(MapColor.GOLD),
            DBBlockEntityTypes.POTENT_SULFUR
    );

    public static final WoodSet POPLAR = BLOCK_BUILDERS.woodSet("poplar", WoodPreset.DEFAULT, MapColor.COLOR_BROWN, MapColor.COLOR_LIGHT_GRAY)
            .creativeInventoryPlacement(() -> Blocks.PALE_OAK_BUTTON, () -> Blocks.PALE_OAK_LOG, () -> Blocks.PALE_OAK_SHELF, () -> Blocks.PALE_OAK_HANGING_SIGN, () -> Items.PALE_OAK_CHEST_BOAT)
            .setLeafSoundType(DBSoundTypes.POPLAR_LEAVES)
            .setWoodSoundType(() -> SoundType.WOOD)
            .createSapling(PoplarSaplingBlock::new, MapColor.PLANT, () -> Items.PALE_OAK_SAPLING)
            .build();

    public static final SuppliedBlock STRAW_BED = BLOCKS.register("straw_bed",
            StrawBedBlock::new,
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_YELLOW)
                    .sound(DBSoundTypes.STRAW_BED.get())
                    .strength(0.2F)
                    .noOcclusion()
                    .ignitedByLava()
                    .pushReaction(PushReaction.DESTROY)
    );

    public static final SuppliedBlock RED_POPLAR_LEAVES = BLOCKS.register(
            "red_poplar_leaves",
            p -> new UntintedParticleLeavesBlock(
                    0.01F,
                    DBParticleTypes.RED_POPLAR_LEAVES.get(),
                    AmbientLeavesBlockSoundPlayer.of(DBSoundEvents.POPLAR_LEAVES_AMBIENT, DBBlockTags.REQUIRED_FOR_POPLAR_LEAF_AMBIENCE),
                    p
            ),
            leavesProperties(DBSoundTypes.POPLAR_LEAVES, MapColor.COLOR_RED)
    );
    public static final SuppliedBlock ORANGE_POPLAR_LEAVES = BLOCKS.register(
            "orange_poplar_leaves",
            p -> new UntintedParticleLeavesBlock(
                    0.01F,
                    DBParticleTypes.ORANGE_POPLAR_LEAVES.get(),
                    AmbientLeavesBlockSoundPlayer.of(DBSoundEvents.POPLAR_LEAVES_AMBIENT, DBBlockTags.REQUIRED_FOR_POPLAR_LEAF_AMBIENCE),
                    p
            ),
            leavesProperties(DBSoundTypes.POPLAR_LEAVES, MapColor.COLOR_ORANGE)
    );
    public static final SuppliedBlock YELLOW_POPLAR_LEAVES = BLOCKS.register(
            "yellow_poplar_leaves",
            p -> new UntintedParticleLeavesBlock(
                    0.01F,
                    DBParticleTypes.YELLOW_POPLAR_LEAVES.get(),
                    AmbientLeavesBlockSoundPlayer.of(DBSoundEvents.POPLAR_LEAVES_AMBIENT, DBBlockTags.REQUIRED_FOR_POPLAR_LEAF_AMBIENCE),
                    p
            ),
            leavesProperties(DBSoundTypes.POPLAR_LEAVES, MapColor.COLOR_YELLOW)
    );

    public static final SuppliedBlock RED_SHRUB = BLOCKS.register(
            "red_shrub",
            BushBlock::new,
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.CRIMSON_NYLIUM)
                    .replaceable()
                    .noCollision()
                    .instabreak()
                    .sound(DBSoundTypes.RED_SHRUB.get())
                    .ignitedByLava()
                    .pushReaction(PushReaction.DESTROY)
    );

    public static final SuppliedBlock SHELF_MUSHROOM = BLOCKS.register(
            "shelf_mushroom",
            ShelfMushroomBlock::new,
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.TERRACOTTA_YELLOW)
                    .sound(DBSoundTypes.SHELF_MUSHROOM.get())
                    .noOcclusion()
                    .pushReaction(PushReaction.DESTROY)
    );

    public static final SuppliedBlock SULFUR_SPIKE = BLOCKS.register(
            "sulfur_spike",
            p -> new SulfurSpikeBlock(SULFUR.getBase().defaultBlockState(), p),
            () -> BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_YELLOW)
                    .forceSolidOn()
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .noOcclusion()
                    .sound(DBSoundTypes.SULFUR_SPIKE.get())
                    .randomTicks()
                    .strength(1.5F, 3.0F)
                    .dynamicShape()
                    .offsetType(BlockBehaviour.OffsetType.XZ)
                    .pushReaction(PushReaction.DESTROY)
                    .isRedstoneConductor(Blocks::never)
                    .noOcclusion()
    );

    private static Supplier<BlockBehaviour.Properties> leavesProperties(final Supplier<SoundType> soundType, MapColor color) {
        return () -> BlockBehaviour.Properties.of()
                .mapColor(MapColor.PLANT)
                .strength(0.2F)
                .randomTicks()
                .sound(soundType.get())
                .noOcclusion()
                .isValidSpawn(Blocks::ocelotOrParrot)
                .isSuffocating(Blocks::never)
                .ignitedByLava()
                .pushReaction(PushReaction.DESTROY)
                .isRedstoneConductor(Blocks::never)
                .mapColor(color);
    }

    public static void init() {
    }

    public static void createProperties() {
        UnifiedHelpers.DATA_COMPONENTS.addCompost(RED_POPLAR_LEAVES, 0.3F);
        UnifiedHelpers.DATA_COMPONENTS.addCompost(ORANGE_POPLAR_LEAVES, 0.3F);
        UnifiedHelpers.DATA_COMPONENTS.addCompost(YELLOW_POPLAR_LEAVES, 0.3F);
        UnifiedHelpers.DATA_COMPONENTS.addCompost(RED_SHRUB, 0.3F);

        final FireBlock fire = (FireBlock) Blocks.FIRE;
        fire.setFlammable(RED_POPLAR_LEAVES.get(), 30, 60);
        fire.setFlammable(ORANGE_POPLAR_LEAVES.get(), 30, 60);
        fire.setFlammable(YELLOW_POPLAR_LEAVES.get(), 30, 60);
        fire.setFlammable(RED_SHRUB.get(), 60, 100);

        UnifiedHelpers.DATA_COMPONENTS.add(STRAW_BED.get(), DataComponents.MAX_STACK_SIZE, 16);
    }
}
