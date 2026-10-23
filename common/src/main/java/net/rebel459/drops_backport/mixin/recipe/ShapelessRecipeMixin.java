package net.rebel459.drops_backport.mixin.recipe;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.ShapelessCraftingRecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import net.minecraft.world.level.Level;
import net.rebel459.drops_backport.tag.DBItemTags;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(ShapelessRecipe.class)
public abstract class ShapelessRecipeMixin {

    @Shadow
    @Final
    private ItemStackTemplate result;

    @Shadow
    @Final
    private List<Ingredient> ingredients;

    @Inject(
            method = "matches(Lnet/minecraft/world/item/crafting/CraftingInput;Lnet/minecraft/world/level/Level;)Z",
            at = @At("RETURN"),
            cancellable = true
    )
    private void updatedSuspiciousStewRecipes(CraftingInput input, Level level, CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValueZ() || !this.result.is(Items.SUSPICIOUS_STEW) || input.ingredientCount() != this.ingredients.size()) return;

        boolean[] mushroomSlot = new boolean[this.ingredients.size()];
        boolean any = false;
        for (int i = 0; i < mushroomSlot.length; i++) {
            Ingredient ingredient = this.ingredients.get(i);
            mushroomSlot[i] = ingredient.test(Items.BROWN_MUSHROOM.getDefaultInstance()) != ingredient.test(Items.RED_MUSHROOM.getDefaultInstance());
            any |= mushroomSlot[i];
        }
        if (!any) return;

        cir.setReturnValue(match(this.ingredients, mushroomSlot, input.items(), new boolean[input.size()], 0));
    }

    @ModifyReturnValue(method = "display", at = @At("RETURN"))
    private List<RecipeDisplay> updatedSuspiciousStewRecipesInRecipeBook(List<RecipeDisplay> original) {
        if (!this.result.is(Items.SUSPICIOUS_STEW)) return original;

        return original.stream().map(display -> {
            if (!(display instanceof ShapelessCraftingRecipeDisplay shapeless)) return display;

            List<SlotDisplay> ingredientDisplays = new ArrayList<>(shapeless.ingredients());
            for (int i = 0; i < this.ingredients.size(); i++) {
                Ingredient ingredient = this.ingredients.get(i);
                if (ingredient.test(Items.BROWN_MUSHROOM.getDefaultInstance()) != ingredient.test(Items.RED_MUSHROOM.getDefaultInstance())) ingredientDisplays.set(i, new SlotDisplay.TagSlotDisplay(DBItemTags.MUSHROOMS));
            }

            return new ShapelessCraftingRecipeDisplay(ingredientDisplays, shapeless.result(), shapeless.craftingStation());
        }).toList();
    }

    @Unique
    private static boolean match(List<Ingredient> ingredients, boolean[] mushroomSlot, List<ItemStack> items, boolean[] used, int slot) {
        if (slot == ingredients.size()) return true;

        Ingredient ingredient = ingredients.get(slot);
        boolean mushroom = mushroomSlot[slot];

        for (int i = 0; i < items.size(); i++) {
            ItemStack stack = items.get(i);
            if (used[i] || stack.isEmpty()) continue;

            if (!ingredient.test(stack) && !(mushroom && stack.is(DBItemTags.MUSHROOMS))) continue;

            used[i] = true;
            if (match(ingredients, mushroomSlot, items, used, slot + 1)) return true;
            used[i] = false;
        }
        return false;
    }
}
