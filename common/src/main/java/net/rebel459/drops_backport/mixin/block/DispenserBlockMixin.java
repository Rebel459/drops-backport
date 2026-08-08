package net.rebel459.drops_backport.mixin.block;

import net.minecraft.core.component.DataComponents;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.DispenserBlock;
import net.rebel459.drops_backport.util.block.SulfurCubeBlockDispenseItemBehavior;
import net.rebel459.drops_backport.tag.DBItemTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DispenserBlock.class)
public abstract class DispenserBlockMixin {

    @Inject(method = "getDefaultDispenseMethod", at = @At(value = "HEAD"), cancellable = true)
    private static void getBedOrientationStraw(ItemStack itemStack, CallbackInfoReturnable<DispenseItemBehavior> cir) {
        if (!itemStack.has(DataComponents.EQUIPPABLE) && itemStack.is(DBItemTags.SULFUR_CUBE_SWALLOWABLE)) cir.setReturnValue(SulfurCubeBlockDispenseItemBehavior.INSTANCE);
    }
}
