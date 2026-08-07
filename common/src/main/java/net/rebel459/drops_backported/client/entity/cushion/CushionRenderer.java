package net.rebel459.drops_backported.client.entity.cushion;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Util;
import net.minecraft.world.item.DyeColor;
import net.rebel459.drops_backported.entity.Cushion;

import java.util.EnumMap;

public class CushionRenderer extends EntityRenderer<Cushion, CushionRenderState> {
   private static final EnumMap<DyeColor, Identifier> TEXTURES_BY_COLOR = Util.make(new EnumMap(DyeColor.class), textures -> {
      for (DyeColor color : DyeColor.values()) {
         textures.put(color, Identifier.withDefaultNamespace("textures/entity/cushion/" + color.getName() + "_cushion.png"));
      }
   });
   private final CushionModel model;

   public CushionRenderer(final EntityRendererProvider.Context context) {
      super(context);
      this.model = new CushionModel(context.bakeLayer(DBModelLayers.CUSHION));
   }

   public void extractRenderState(final Cushion cushion, final CushionRenderState state, final float partialTicks) {
      super.extractRenderState(cushion, state, partialTicks);
      state.direction = Direction.fromYRot(cushion.getYRot());
      state.texture = (Identifier)TEXTURES_BY_COLOR.get(cushion.getColor());
   }

   public void submit(final CushionRenderState state, final PoseStack poseStack, final SubmitNodeCollector submitNodeCollector, final CameraRenderState camera) {
      poseStack.pushPose();
      poseStack.mulPose(Axis.YP.rotationDegrees(180.0F - state.direction.toYRot()));
      poseStack.mulPose(Axis.XP.rotationDegrees(180.0F));
      poseStack.translate(0.0, -0.25, 0.0);
      submitNodeCollector.submitModel(
         this.model, state, poseStack, this.model.renderType(state.texture), state.lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor
      );
      poseStack.popPose();
      super.submit(state, poseStack, submitNodeCollector, camera);
   }

   public CushionRenderState createRenderState() {
      return new CushionRenderState();
   }
}
