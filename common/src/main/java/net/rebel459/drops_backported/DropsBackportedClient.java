package net.rebel459.drops_backported;

import net.rebel459.drops_backported.client.entity.sulfur_cube.SulfurCubeRenderer;
import net.rebel459.drops_backported.client.entity.sulfur_cube.SmallSulfurCubeModel;
import net.rebel459.drops_backported.client.entity.sulfur_cube.SulfurCubeModel;
import net.rebel459.drops_backported.client.particle.SulfurBubbleParticle;
import net.rebel459.drops_backported.entity.sulfur_cube.SulfurCube;
import net.rebel459.drops_backported.registry.DBEntityTypes;
import net.rebel459.drops_backported.registry.DBParticleTypes;
import net.rebel459.unified.platform.client.UnifiedClientHelpers;

import java.util.function.Supplier;
import net.rebel459.drops_backported.client.particle.SulfurCubeGooParticleProvider;
import net.minecraft.world.entity.EntityType;

public class DropsBackportedClient {

    public static void initRegistries() {}

    public static void init() {
        UnifiedClientHelpers.ENTITY_RENDERERS.addLayerDefinition(SulfurCubeRenderer.SULFUR_CUBE, SulfurCubeModel::createOuterBodyLayer);
        UnifiedClientHelpers.ENTITY_RENDERERS.addLayerDefinition(SulfurCubeRenderer.SULFUR_CUBE_INNER, SulfurCubeModel::createInnerBodyLayer);
        UnifiedClientHelpers.ENTITY_RENDERERS.addLayerDefinition(SulfurCubeRenderer.SULFUR_CUBE_SMALL, SmallSulfurCubeModel::createOuterBodyLayer);
        UnifiedClientHelpers.ENTITY_RENDERERS.addLayerDefinition(SulfurCubeRenderer.SULFUR_CUBE_SMALL_INNER, SmallSulfurCubeModel::createInnerBodyLayer);
        UnifiedClientHelpers.PARTICLE_PROVIDERS.add(DBParticleTypes.SULFUR_BUBBLES, SulfurBubbleParticle.Provider::new);
        UnifiedClientHelpers.PARTICLE_PROVIDERS.add(DBParticleTypes.SULFUR_CUBE_GOO, SulfurCubeGooParticleProvider::new);
        UnifiedClientHelpers.ENTITY_RENDERERS.addEntityRenderer(DBEntityTypes.SULFUR_CUBE::get, SulfurCubeRenderer::new);
    }
}
