package net.rebel459.drops_backported;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod(value = DropsBackported.MOD_ID, dist = Dist.CLIENT)
public class DropsBackportedNeoForgeClient {

    public DropsBackportedNeoForgeClient(IEventBus modEventBus) {
        DropsBackportedClient.initRegistries();
        modEventBus.addListener(DropsBackportedNeoForgeClient::commonSetup);
    }

    private static void commonSetup(final FMLCommonSetupEvent event) {
        DropsBackportedClient.init();
    }
}