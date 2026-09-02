package net.rebel459.drops_backport.mixin.client;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.MapDecorations;
import net.minecraft.world.level.saveddata.maps.MapDecorationTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(targets = "net.minecraft.client.gui.screens.debug.GameModeSwitcherScreen$GameModeIcon")
public class GameModeIconMixin {

    @ModifyArg(
            method = "<clinit>",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screens/debug/GameModeSwitcherScreen$GameModeIcon;<init>(Ljava/lang/String;ILnet/minecraft/network/chat/Component;Lnet/minecraft/world/level/GameType;Lnet/minecraft/world/item/ItemStack;)V",
                    ordinal = 2
            ),
            index = 4
    )
    private static ItemStack updatedAdventureMap(ItemStack original) {
        ItemStack stack = new ItemStack(Items.FILLED_MAP);
        stack.set(DataComponents.MAP_DECORATIONS, MapDecorations.EMPTY.withDecoration("treasure", new MapDecorations.Entry(MapDecorationTypes.RED_X, 0.0, 0.0, 0.0F)));
        return stack;
    }
}
