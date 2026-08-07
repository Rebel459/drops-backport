package net.rebel459.drops_backport;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.rebel459.unified.platform.NeoForgeUnifiedRegistries;

@Mod(DropsBackport.MOD_ID)
public class DropsBackportNeoForge {

    public DropsBackportNeoForge(IEventBus modEventBus) {
        NeoForgeUnifiedRegistries.registerBus(DropsBackport.VANILLA_ID, modEventBus);
        DropsBackport.initRegistries();
        modEventBus.addListener(DropsBackportNeoForge::commonSetup);
    }

    private static void commonSetup(final FMLCommonSetupEvent event) {
        DropsBackport.init();
    }
}