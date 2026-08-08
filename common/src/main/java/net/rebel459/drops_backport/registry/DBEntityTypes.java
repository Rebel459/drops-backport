package net.rebel459.drops_backport.registry;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.rebel459.drops_backport.DropsBackport;
import net.rebel459.drops_backport.entity.Cushion;
import net.rebel459.drops_backport.entity.cube.SulfurCube;
import net.rebel459.unified.platform.UnifiedRegistries;
import net.rebel459.unified.util.registry.Supplied;

public final class DBEntityTypes {
    private static final UnifiedRegistries.EntityTypes ENTITIES = UnifiedRegistries.EntityTypes.create(DropsBackport.VANILLA_ID);

    public static final Supplied<EntityType<SulfurCube>> SULFUR_CUBE = ENTITIES.register(
            "sulfur_cube",
            EntityType.Builder.of(SulfurCube::new, MobCategory.MONSTER)
                    .sized(0.49F, 0.49F)
                    .eyeHeight(0.175F)
                    .spawnDimensionsScale(2.0F)
                    .clientTrackingRange(10),
            SulfurCube.createSulfurCubeAttributes()
    );

    public static final Supplied<EntityType<Cushion>> CUSHION = ENTITIES.register(
            "cushion",
            EntityType.Builder.of(Cushion::new, MobCategory.MISC)
                    .noLootTable()
                    .sized(1.0F, 0.25F)
                    .clientTrackingRange(10)
                    .updateInterval(Integer.MAX_VALUE)
    );

    private DBEntityTypes() {
    }

    public static void init() {
    }
}
