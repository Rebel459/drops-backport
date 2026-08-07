package net.rebel459.drops_backport;

import net.fabricmc.api.ClientModInitializer;

public class DropsBackportFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        DropsBackportClient.initRegistries();
        DropsBackportClient.init();
    }
}
