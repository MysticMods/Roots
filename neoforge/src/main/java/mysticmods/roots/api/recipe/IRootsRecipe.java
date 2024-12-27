package mysticmods.roots.api.recipe;

import mysticmods.roots.api.recipe.crafting.IRootsCrafting;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

import java.util.List;

public interface IRootsRecipe<H extends IItemHandler, W extends IRootsCrafting<H>> extends IBoundlessRecipe<W> {
  default NonNullList<ItemStack> process(List<ItemStack> ingredients) {
    NonNullList<ItemStack> result = NonNullList.create();
    for (ItemStack stack : ingredients) {
      if (stack.hasCraftingRemainingItem()) {
        result.add(stack.getCraftingRemainingItem());
      }
    }
    return result;
  }
}
