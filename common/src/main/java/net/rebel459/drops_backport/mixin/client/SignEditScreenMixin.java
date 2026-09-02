package net.rebel459.drops_backport.mixin.client;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractSignEditScreen;
import net.minecraft.client.gui.screens.inventory.SignEditScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.PlainSignBlock;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.rebel459.drops_backport.registry.DBBlocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SignEditScreen.class)
public abstract class SignEditScreenMixin extends AbstractSignEditScreen {

    @Unique
    private static final Identifier POPLAR_SIGN_TEXTURE = Identifier.withDefaultNamespace("textures/gui/signs/poplar.png");

    protected SignEditScreenMixin(SignBlockEntity sign, boolean isFrontText, boolean isFiltered) {
        super(sign, isFrontText, isFiltered);
    }

    @Inject(
            method = "init",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screens/inventory/AbstractSignEditScreen;init()V",
                    shift = At.Shift.AFTER
            ),
            cancellable = true
    )
    private void skipEntityModel(CallbackInfo ci) {
        if (this.woodType.name().equals(DBBlocks.POPLAR.getId().toString())) {
            ci.cancel();
        }
    }

    @Inject(method = "extractSignBackground", at = @At("HEAD"), cancellable = true)
    private void drawTexture(GuiGraphicsExtractor graphics, CallbackInfo ci) {
        if (!this.woodType.name().equals(DBBlocks.POPLAR.getId().toString())) {
            return;
        }

        boolean wallSign = PlainSignBlock.getAttachmentPoint(this.sign.getBlockState()) == PlainSignBlock.Attachment.WALL;
        int displayedHeight = wallSign ? 12 : 26;

        graphics.pose().translate(0.0F, 27.0F);
        graphics.pose().scale(3.9F, 3.9F);
        graphics.blit(RenderPipelines.GUI_TEXTURED, POPLAR_SIGN_TEXTURE, -12, -13, 0.0F, 0.0F, 24, displayedHeight, 24, 26);
        ci.cancel();
    }
}
