package net.rebel459.drops_backported;

import net.minecraft.resources.Identifier;
import net.rebel459.drops_backported.registry.*;
import net.rebel459.drops_backported.sound.DBSoundEvents;
import net.rebel459.drops_backported.worldgen.DBWorldgenCodecs;

public class DropsBackported {

    public static void initRegistries() {
        DBSoundEvents.init();
        DBAttributes.init();
        DBSulfurCubeArchetypes.init();
        DBParticleTypes.init();
        DBEntityTypes.init();
        DBBlocks.init();
        DBBlockEntityTypes.init();
        DBParticleTypes.init();
        DBItems.init();
        DBWorldgenCodecs.init();
        DBMapDecorationTypes.init();
    }

    public static void init() {}

    public static Identifier modId(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }

    public static Identifier vanillaId(String path) {
        return Identifier.fromNamespaceAndPath(VANILLA_ID, path);
    }

    public static final String MOD_ID = "drops_backported";
    public static final String VANILLA_ID = "minecraft";
}
