package epicsquid.roots.integration.jei.interact;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

public class BlockBreakRecipe {
  private Ingredient breaks;
  private ItemStack output;

  public BlockBreakRecipe(Ingredient breaks, ItemStack output) {
    this.breaks = breaks;
    this.output = output;
  }

  public Ingredient getBreaks() {
    return breaks;
  }

  public ItemStack getOutput() {
    return output;
  }
}
