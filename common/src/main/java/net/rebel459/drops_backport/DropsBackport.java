package net.rebel459.drops_backport;

import net.minecraft.resources.Identifier;
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
