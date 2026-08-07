package net.rebel459.drops_backport;

import net.fabricmc.api.ModInitializer;

public class DropsBackportFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        DropsBackport.initRegistries();
        DropsBackport.init();
    }
}
