package mysticmods.roots.api.recipe;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.crafting.IRecipe;

// TODO: Move to NoobUtil
public interface IBoundlessRecipe<C extends IInventory> extends IRecipe<C> {
  @Override
  default boolean canCraftInDimensions(int pWidth, int pHeight) {
    return true;
  }
}
