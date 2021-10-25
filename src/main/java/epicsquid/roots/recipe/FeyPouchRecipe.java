package epicsquid.roots.recipe;

import epicsquid.roots.Roots;
import epicsquid.roots.init.ModItems;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

import java.util.List;

public class FeyPouchRecipe extends FeyCraftingRecipe {
  public FeyPouchRecipe(ItemStack result, int xp) {
    super(result, xp);
  }

  @Override
  public void postCraft(ItemStack output, List<ItemStack> inputs, PlayerEntity player) {
    ItemStack oldPouch = null;
    for (ItemStack stack : inputs) {
      if (stack.getItem() == ModItems.herb_pouch) {
        oldPouch = stack;
        break;
      }
    }

    if (oldPouch == null) {
      Roots.logger.error("Couldn't find original!");
    } else {
      // Copy nbt!
      output.setTagCompound(oldPouch.getTagCompound());
    }
  }
}
