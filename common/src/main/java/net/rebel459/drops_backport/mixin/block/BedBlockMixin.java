package net.rebel459.drops_backport.mixin.block;

import net.minecraft.world.level.block.BedBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(BedBlock.class)
public abstract class BedBlockMixin {

    @ModifyConstant(
            method = "bounceUp",
            constant = @Constant(doubleValue = 0.6600000262260437D)
    )
    private double higherBounce(double constant) {
        return 0.75D;
    }
}