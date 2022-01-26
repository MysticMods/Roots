package mysticmods.roots.api.recipe;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;

import java.util.List;

public interface IBoundlessRecipe<C extends IInventory> extends IRecipe<C> {
  @Override
  default boolean canCraftInDimensions(int pWidth, int pHeight) {
    return true;
  }
}
