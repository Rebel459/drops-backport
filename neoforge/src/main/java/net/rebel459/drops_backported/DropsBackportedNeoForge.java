package net.rebel459.drops_backported;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.rebel459.unified.platform.NeoForgeUnifiedRegistries;

@Mod(DropsBackported.MOD_ID)
public class DropsBackportedNeoForge {

    public DropsBackportedNeoForge(IEventBus modEventBus) {
        NeoForgeUnifiedRegistries.registerBus(DropsBackported.VANILLA_ID, modEventBus);
        DropsBackported.initRegistries();
        modEventBus.addListener(DropsBackportedNeoForge::commonSetup);
    }

    private static void commonSetup(final FMLCommonSetupEvent event) {
        DropsBackported.init();
    }
}