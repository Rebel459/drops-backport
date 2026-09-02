package net.rebel459.drops_backport.mixin.entity;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.monster.Shulker;
import net.minecraft.world.level.block.Blocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Shulker.class)
public abstract class ShulkerMixin {

    @WrapOperation(
            method = "findAttachableSurface",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/monster/Shulker;canStayAt(Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Direction;)Z"
            )
    )
    private boolean noBedrock(Shulker instance, BlockPos target, Direction face, Operation<Boolean> original) {
        return original.call(instance, target, face) && !instance.level().getBlockState(target.relative(face)).is(Blocks.BEDROCK);
    }
}
