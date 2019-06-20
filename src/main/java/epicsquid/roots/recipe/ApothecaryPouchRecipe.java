package epicsquid.roots.recipe;

import epicsquid.roots.Roots;
import epicsquid.roots.init.ModItems;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;

public class ApothecaryPouchRecipe extends FeyCraftingRecipe {
  public ApothecaryPouchRecipe(ItemStack result, int xp) {
    super(result, xp);
  }

  @Override
  public void postCraft(ItemStack output, IItemHandlerModifiable handler) {
    ItemStack oldPouch = null;
    for (int i = 0; i < 5; i++) {
      ItemStack slot = handler.getStackInSlot(i);
      if (slot.getItem() == ModItems.component_pouch) {
        oldPouch = slot;
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
