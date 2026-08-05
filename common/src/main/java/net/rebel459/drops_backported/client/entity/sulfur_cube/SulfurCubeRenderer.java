package net.rebel459.drops_backported.client.entity.sulfur_cube;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.BlockModelResolver;
import net.minecraft.client.renderer.block.model.BlockDisplayContext;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.BlockItemStateProperties;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.rebel459.drops_backported.entity.sulfur_cube.SulfurCube;

public class SulfurCubeRenderer extends MobRenderer<SulfurCube, SulfurCubeRenderState, SulfurCubeModel> {
    public static final ModelLayerLocation SULFUR_CUBE = layer("sulfur_cube", "main");
    public static final ModelLayerLocation SULFUR_CUBE_INNER = layer("sulfur_cube", "inner");
    public static final ModelLayerLocation SULFUR_CUBE_SMALL = layer("sulfur_cube", "small");
    public static final ModelLayerLocation SULFUR_CUBE_SMALL_INNER = layer("sulfur_cube", "small_inner");

    private static final Identifier NORMAL = Identifier.withDefaultNamespace("textures/entity/sulfur_cube/sulfur_cube_outer.png");
    private static final Identifier SMALL = Identifier.withDefaultNamespace("textures/entity/sulfur_cube/sulfur_cube_outer_small.png");
    private static final BlockDisplayContext BLOCK_DISPLAY_CONTEXT = BlockDisplayContext.create();

    private final SulfurCubeModel normalModel;
    private final SmallSulfurCubeModel smallModel;
    private final BlockModelResolver blockModelResolver;

    public SulfurCubeRenderer(EntityRendererProvider.Context context) {
        super(context, new SulfurCubeModel(context.bakeLayer(SULFUR_CUBE)), 0.25F);
        this.normalModel = this.model;
        this.smallModel = new SmallSulfurCubeModel(context.bakeLayer(SULFUR_CUBE_SMALL));
        this.blockModelResolver = context.getBlockModelResolver();
        this.addLayer(new SulfurCubeInnerLayer(this, context.getModelSet()));
    }

    @Override
    public void submit(SulfurCubeRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        this.model = state.isBaby ? this.smallModel : this.normalModel;
        super.submit(state, poseStack, submitNodeCollector, camera);
    }

    @Override
    protected float getShadowRadius(SulfurCubeRenderState state) {
        return state.size * 0.25F;
    }

    @Override
    protected void scale(SulfurCubeRenderState state, PoseStack poseStack) {
        poseStack.scale(0.999F, 0.999F, 0.999F);
        poseStack.translate(0.0F, 0.001F, 0.0F);
        this.applySizeAndSquish(state, poseStack);
        if (state.fuseRemainingTicks < 10.0F && state.fuseRemainingTicks > 0.0F) {
            float s = 1.0F + getSwellAmount(state.fuseRemainingTicks);
            poseStack.scale(s, s, s);
        }

        float vOffset = state.isBaby ? 1.24F : 0.98F;
        float extraDownscale = state.isBaby ? 1.0F : 0.5F;
        float onePixelUpIfVisible = (state.isInvisible ? 0.0F : 1.0F) / 16.0F;
        poseStack.scale(extraDownscale, extraDownscale, extraDownscale);
        poseStack.translate(0.0F, vOffset - onePixelUpIfVisible, 0.0F);
    }

    @Override
    public Identifier getTextureLocation(SulfurCubeRenderState state) {
        return state.isBaby ? SMALL : NORMAL;
    }

    @Override
    public SulfurCubeRenderState createRenderState() {
        return new SulfurCubeRenderState();
    }

    @Override
    public void extractRenderState(SulfurCube entity, SulfurCubeRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.squish = Mth.lerp(partialTicks, entity.oSquish, entity.squish);
        state.size = entity.getSize();
        state.isBaby = entity.isBaby();
        state.fuseRemainingTicks = entity.isPrimed() ? entity.getFuse() - partialTicks + 1.0F : 0.0F;
        state.containedBlock.clear();
        ItemStack containedBlock = entity.getBodyArmorItem();
        if (!containedBlock.isEmpty()) {
            BlockItemStateProperties blockItemState = containedBlock.getOrDefault(DataComponents.BLOCK_STATE, BlockItemStateProperties.EMPTY);
            BlockState blockState = blockItemState.apply(Block.byItem(containedBlock.getItem()).defaultBlockState());
            this.blockModelResolver.update(state.containedBlock, blockState, BLOCK_DISPLAY_CONTEXT);
        }
    }

    private void applySizeAndSquish(SulfurCubeRenderState state, PoseStack poseStack) {
        float size = state.size;
        float ss = state.containedBlock.isEmpty() ? state.squish / (size * 0.5F + 1.0F) : 0.0F;
        float w = 1.0F / (ss + 1.0F);
        poseStack.scale(w * size, 1.0F / w * size, w * size);
    }

    private static float getSwellAmount(float fuse) {
        return 1.0F - Mth.clamp(fuse / 10.0F, 0.0F, 1.0F);
    }

    private static ModelLayerLocation layer(String path, String part) {
        return new ModelLayerLocation(Identifier.withDefaultNamespace(path), part);
    }
}
