package net.rebel459.drops_backport;

import net.rebel459.drops_backport.client.entity.cushion.CushionModel;
import net.rebel459.drops_backport.client.entity.cushion.CushionRenderer;
import net.rebel459.drops_backport.client.entity.sulfur_cube.SmallSulfurCubeModel;
import net.rebel459.drops_backport.client.entity.sulfur_cube.SulfurCubeModel;
import net.rebel459.drops_backport.client.entity.sulfur_cube.SulfurCubeRenderer;
import net.rebel459.drops_backport.client.item.ExplorerMapItemModel;
import net.rebel459.drops_backport.client.particle.*;
import net.rebel459.drops_backport.client.particle.geyser.GeyserBaseParticle;
import net.rebel459.drops_backport.client.particle.geyser.GeyserEruptionParticle;
import net.rebel459.drops_backport.client.particle.geyser.GeyserPlumeParticle;
import net.rebel459.drops_backport.registry.DBEntityTypes;
import net.rebel459.drops_backport.registry.DBParticleTypes;
import net.rebel459.unified.platform.client.UnifiedClientHelpers;

public class DropsBackportClient {

    public static void initRegistries() {
        ExplorerMapItemModel.init();
        UnifiedClientHelpers.ENTITY_RENDERERS.addLayerDefinition(SulfurCubeRenderer.SULFUR_CUBE, SulfurCubeModel::createOuterBodyLayer);
        UnifiedClientHelpers.ENTITY_RENDERERS.addLayerDefinition(SulfurCubeRenderer.SULFUR_CUBE_INNER, SulfurCubeModel::createInnerBodyLayer);
        UnifiedClientHelpers.ENTITY_RENDERERS.addLayerDefinition(SulfurCubeRenderer.SULFUR_CUBE_SMALL, SmallSulfurCubeModel::createOuterBodyLayer);
        UnifiedClientHelpers.ENTITY_RENDERERS.addLayerDefinition(SulfurCubeRenderer.SULFUR_CUBE_SMALL_INNER, SmallSulfurCubeModel::createInnerBodyLayer);
        UnifiedClientHelpers.ENTITY_RENDERERS.addLayerDefinition(CushionRenderer.CUSHION, CushionModel::createBodyLayer);
        UnifiedClientHelpers.PARTICLE_PROVIDERS.add(DBParticleTypes.ORANGE_POPLAR_LEAVES, PoplarProvider::new);
        UnifiedClientHelpers.PARTICLE_PROVIDERS.add(DBParticleTypes.YELLOW_POPLAR_LEAVES, PoplarProvider::new);
        UnifiedClientHelpers.PARTICLE_PROVIDERS.add(DBParticleTypes.RED_POPLAR_LEAVES, PoplarProvider::new);
        UnifiedClientHelpers.PARTICLE_PROVIDERS.add(DBParticleTypes.SULFUR_BUBBLES, SulfurBubbleParticle.Provider::new);
        UnifiedClientHelpers.PARTICLE_PROVIDERS.add(DBParticleTypes.SULFUR_CUBE_GOO, SulfurCubeGooParticleProvider::new);
        UnifiedClientHelpers.PARTICLE_PROVIDERS.add(DBParticleTypes.GEYSER, _ -> new GeyserEruptionParticle.Provider());
        UnifiedClientHelpers.PARTICLE_PROVIDERS.add(DBParticleTypes.GEYSER_BASE, GeyserBaseParticle.Provider::new);
        UnifiedClientHelpers.PARTICLE_PROVIDERS.add(DBParticleTypes.GEYSER_POOF, GeyserBaseParticle.Provider::new);
        UnifiedClientHelpers.PARTICLE_PROVIDERS.add(DBParticleTypes.GEYSER_PLUME, GeyserPlumeParticle.Provider::new);
        UnifiedClientHelpers.PARTICLE_PROVIDERS.add(DBParticleTypes.NOXIOUS_GAS, NoxiousGasParticle.Provider::new);
        UnifiedClientHelpers.PARTICLE_PROVIDERS.add(DBParticleTypes.NOXIOUS_GAS_CLOUD, _ -> new NoxiousGasCloudParticle.Provider());
        UnifiedClientHelpers.ENTITY_RENDERERS.addEntityRenderer(DBEntityTypes.SULFUR_CUBE::get, SulfurCubeRenderer::new);
        UnifiedClientHelpers.ENTITY_RENDERERS.addEntityRenderer(DBEntityTypes.CUSHION::get, CushionRenderer::new);
    }

    public static void init() {}
}
