package net.rebel459.drops_backported.client.entity.sulfur_cube;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;

public class SulfurCubeInnerLayer extends RenderLayer<SulfurCubeRenderState, SulfurCubeModel> {
    private static final Identifier INNER = Identifier.withDefaultNamespace("textures/entity/sulfur_cube/sulfur_cube_inner.png");
    private static final Identifier SMALL_INNER = Identifier.withDefaultNamespace("textures/entity/sulfur_cube/sulfur_cube_inner_small.png");

    private final SulfurCubeModel normalModel;
    private final SulfurCubeModel smallModel;

    public SulfurCubeInnerLayer(RenderLayerParent<SulfurCubeRenderState, SulfurCubeModel> renderer, EntityModelSet modelSet) {
        super(renderer);
        this.normalModel = new SulfurCubeModel(modelSet.bakeLayer(SulfurCubeRenderer.SULFUR_CUBE_INNER));
        this.smallModel = new SulfurCubeModel(modelSet.bakeLayer(SulfurCubeRenderer.SULFUR_CUBE_SMALL_INNER));
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, SulfurCubeRenderState state, float yRot, float xRot) {
        int overlayCoords = state.fuseRemainingTicks > 0.0F && isLit(state.fuseRemainingTicks)
            ? OverlayTexture.pack(OverlayTexture.u(1.0F), 10)
            : LivingEntityRenderer.getOverlayCoords(state, 0.0F);
        if (!state.containedBlock.isEmpty()) {
            poseStack.pushPose();
            poseStack.mulPose(Axis.XP.rotationDegrees(180.0F));
            if (state.isBaby) {
                poseStack.scale(0.5F, 0.5F, 0.5F);
            }

            poseStack.translate(-0.5F, -0.518F, -0.5F);
            state.containedBlock.submit(poseStack, submitNodeCollector, lightCoords, overlayCoords, state.outlineColor);
            poseStack.popPose();
        } else if (!state.isInvisible) {
            Identifier texture = state.isBaby ? SMALL_INNER : INNER;
            SulfurCubeModel model = state.isBaby ? this.smallModel : this.normalModel;
            submitNodeCollector.order(-1).submitModel(model, state, poseStack, RenderTypes.entityTranslucent(texture), lightCoords, overlayCoords, -1, null, state.outlineColor, null);
        }
    }

    private static boolean isLit(float fuse) {
        return (int)(fuse / 5.0F) % 2 == 0;
    }
}
