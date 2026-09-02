package net.rebel459.drops_backport.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.AbstractSignRenderer;
import net.minecraft.client.renderer.blockentity.state.SignRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.rebel459.drops_backport.registry.DBBlocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractSignRenderer.class)
public abstract class AbstractSignRendererMixin {

    @WrapOperation(
            method = "submitSignWithText",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/blockentity/AbstractSignRenderer;getSignModel(Lnet/minecraft/client/renderer/blockentity/state/SignRenderState;)Lnet/minecraft/client/model/Model$Simple;"
            )
    )
    private Model.Simple skipModelLookup(AbstractSignRenderer<?> instance, SignRenderState state, Operation<Model.Simple> original) {
        return isPoplar(state.woodType) ? null : original.call(instance, state);
    }

    @Inject(method = "submitSign", at = @At("HEAD"), cancellable = true)
    private void useBlockModel(PoseStack poseStack, int lightCoords, WoodType type, Model.Simple signModel, ModelFeatureRenderer.CrumblingOverlay breakProgress, SubmitNodeCollector submitNodeCollector, CallbackInfo ci) {
        if (isPoplar(type)) {
            ci.cancel();
        }
    }

    @Unique
    private static boolean isPoplar(WoodType woodType) {
        return woodType.name().equals(DBBlocks.POPLAR.getId().toString());
    }
}
