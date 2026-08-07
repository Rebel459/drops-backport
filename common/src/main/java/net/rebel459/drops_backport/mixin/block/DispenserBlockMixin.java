package net.rebel459.drops_backport.mixin.block;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.DispenserBlock;
import net.rebel459.drops_backport.block.SulfurCubeBlockDispenseItemBehavior;
import net.rebel459.drops_backport.entity.SulfurCube;
import net.rebel459.drops_backport.registry.DBAttributes;
import net.rebel459.drops_backport.tag.DBItemTags;
import net.rebel459.drops_backport.util.block.AbstractBedBlock;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(DispenserBlock.class)
public abstract class DispenserBlockMixin {

    @Inject(method = "getDefaultDispenseMethod", at = @At(value = "HEAD"), cancellable = true)
    private static void getBedOrientationStraw(ItemStack itemStack, CallbackInfoReturnable<DispenseItemBehavior> cir) {
        if (!itemStack.has(DataComponents.EQUIPPABLE) && itemStack.is(DBItemTags.SULFUR_CUBE_SWALLOWABLE)) cir.setReturnValue(SulfurCubeBlockDispenseItemBehavior.INSTANCE);
    }
}
