package net.rebel459.drops_backport;

import net.minecraft.core.BlockPos;
import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.DispenserBlock;
import net.rebel459.drops_backport.item.SulfurCubeBucketItem;
import net.rebel459.drops_backport.registry.*;
import net.rebel459.drops_backport.sound.DBSoundEvents;
import net.rebel459.drops_backport.util.DBCreativeEntries;
import net.rebel459.drops_backport.worldgen.DBWorldgenCodecs;

public class DropsBackport {

    public static void initRegistries() {
        DBSoundEvents.init();
        DBAttributes.init();
        DBSulfurCubeArchetypes.init();
        DBParticleTypes.init();
        DBEntityTypes.init();
        DBBlockEntityTypes.init();
        DBBlocks.init();
        DBParticleTypes.init();
        DBItems.init();
        DBWorldgenCodecs.init();
        DBMapDecorationTypes.init();
    }

    public static void init() {
        DBBlocks.createProperties();
        DBCreativeEntries.init();
        DispenserBlock.registerBehavior(DBItems.SULFUR_CUBE_BUCKET.get(), new DefaultDispenseItemBehavior() {
            private final DefaultDispenseItemBehavior defaultDispenseItemBehavior = new DefaultDispenseItemBehavior();

            @Override
            public ItemStack execute(final BlockSource source, final ItemStack dispensed) {
                BlockPos target = source.pos().relative(source.state().getValue(DispenserBlock.FACING));
                if (dispensed.getItem() instanceof SulfurCubeBucketItem bucket && bucket.placeSulfurCube(null, source.level(), dispensed, target)) {
                    return this.consumeWithRemainder(source, dispensed, new ItemStack(Items.BUCKET));
                } else {
                    return this.defaultDispenseItemBehavior.dispense(source, dispensed);
                }
            }
        });
    }

    public static Identifier modId(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }

    public static Identifier vanillaId(String path) {
        return Identifier.fromNamespaceAndPath(VANILLA_ID, path);
    }

    public static final String MOD_ID = "drops_backport";
    public static final String VANILLA_ID = "minecraft";
}
