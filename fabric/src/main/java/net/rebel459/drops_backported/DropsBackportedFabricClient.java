package net.rebel459.drops_backported;

import net.fabricmc.api.ClientModInitializer;

public class DropsBackportedFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        DropsBackportedClient.initRegistries();
        DropsBackportedClient.init();
    }
}
