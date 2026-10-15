package net.rebel459.drops_backport.registry;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.rebel459.drops_backport.DropsBackport;
import net.rebel459.drops_backport.entity.Cushion;
import net.rebel459.drops_backport.entity.cube.SulfurCube;
import net.rebel459.unified.api.core.Supplied;
import net.rebel459.unified.api.data.registry.EntityGenerator;

public final class DBEntities {
    private static final EntityGenerator ENTITIES = DropsBackport.DATA.registries().entities();

    public static final Supplied<EntityType<SulfurCube>> SULFUR_CUBE = ENTITIES.register(
            "sulfur_cube",
            EntityType.Builder.of(SulfurCube::new, MobCategory.MONSTER)
                    .sized(0.49F, 0.49F)
                    .eyeHeight(0.175F)
                    .spawnDimensionsScale(2.0F)
                    .clientTrackingRange(10),
            builder -> builder.properties(properties -> properties.attributes(SulfurCube.createSulfurCubeAttributes()))
    );

    public static final Supplied<EntityType<Cushion>> CUSHION = ENTITIES.register(
            "cushion",
            EntityType.Builder.of(Cushion::new, MobCategory.MISC)
                    .noLootTable()
                    .sized(1.0F, 0.25F)
                    .clientTrackingRange(10)
                    .updateInterval(Integer.MAX_VALUE),
            _ -> {}
    );

    public static void init() {}
}
