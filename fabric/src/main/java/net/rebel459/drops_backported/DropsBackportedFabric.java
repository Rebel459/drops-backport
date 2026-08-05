package net.rebel459.drops_backported;

import net.fabricmc.api.ModInitializer;

public class DropsBackportedFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        DropsBackported.initRegistries();
        DropsBackported.init();
    }
}
