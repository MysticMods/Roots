package epicsquid.roots.recipe;

import epicsquid.roots.Roots;
import epicsquid.roots.init.ModItems;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

import java.util.List;

public class ApothecaryPouchRecipe extends FeyCraftingRecipe {
  public ApothecaryPouchRecipe(ItemStack result, int xp) {
    super(result, xp);
  }

  @Override
  public void postCraft(ItemStack output, List<ItemStack> inputs, PlayerEntity player) {
    ItemStack oldPouch = null;
    for (ItemStack stack : inputs) {
      if (stack.getItem() == ModItems.component_pouch) {
        oldPouch = stack;
        break;
      }
    }

    if (oldPouch == null) {
      Roots.logger.error("Couldn't find original Component Pouch!");
    } else {
      // Copy nbt!
      output.setTagCompound(oldPouch.getTagCompound());
    }
  }
}
