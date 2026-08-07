package net.rebel459.drops_backported.registry;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.saveddata.maps.MapDecorationType;
import net.minecraft.world.level.saveddata.maps.MapDecorationTypes;
import net.rebel459.drops_backported.DropsBackported;
import net.rebel459.unified.platform.UnifiedRegistries;

public class DBMapDecorationTypes {

    public static UnifiedRegistries.DeferredRegistry<MapDecorationType> DECORATIONS = UnifiedRegistries.DeferredRegistry.create(DropsBackported.VANILLA_ID, BuiltInRegistries.MAP_DECORATION_TYPE);

    public static final Holder<MapDecorationType> ANCIENT_CITY = register("ancient_city");
    public static final Holder<MapDecorationType> ABANDONED_CAMPSITE = register("abandoned_campsite");
    public static final Holder<MapDecorationType> DESERT_PYRAMID = register("desert_pyramid");
    public static final Holder<MapDecorationType> MINESHAFT = register("mineshaft");
    public static final Holder<MapDecorationType> WARM_OCEAN_RUINS = register("warm_ocean_ruins");

    public static void init() {
    }

    private static Holder<MapDecorationType> register(String string) {
        return DECORATIONS.registerForHolder(string, () -> new MapDecorationType(DropsBackported.vanillaId(string), true, 0, true, false));
    }
}