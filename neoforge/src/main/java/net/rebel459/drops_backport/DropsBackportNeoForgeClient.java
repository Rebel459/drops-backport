package net.rebel459.drops_backport;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod(value = DropsBackport.MOD_ID, dist = Dist.CLIENT)
public class DropsBackportNeoForgeClient {

    public DropsBackportNeoForgeClient(IEventBus modEventBus) {
        DropsBackportClient.initRegistries();
        modEventBus.addListener(DropsBackportNeoForgeClient::commonSetup);
    }

    private static void commonSetup(final FMLCommonSetupEvent event) {
        DropsBackportClient.init();
    }
}