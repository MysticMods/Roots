package mysticmods.roots.api.recipe.inventory;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import net.neoforged.neoforge.items.IItemHandler;

public record RecipeInputWrapper (IItemHandler handler) implements RecipeInput {
  @Override
  public ItemStack getItem(int index) {
    return handler.getStackInSlot(index);
  }

  @Override
  public int size() {
    return handler.getSlots();
  }
}
