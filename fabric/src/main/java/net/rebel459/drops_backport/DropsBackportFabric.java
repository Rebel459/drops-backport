package net.rebel459.drops_backport;

import net.fabricmc.api.ModInitializer;
import net.rebel459.unified.fabric.FabricUnifiedInitializer;

public class DropsBackportFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        DropsBackport.initRegistries();
        FabricUnifiedInitializer.register(DropsBackport::init);
    }
}
