package epicsquid.roots.recipe.crafting;

import epicsquid.roots.init.ModItems;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;

import javax.annotation.Nonnull;

public class ApothecaryGroveCraftingRecipe extends ShapedGroveCraftingRecipe {
  public ApothecaryGroveCraftingRecipe(ResourceLocation group, @Nonnull ItemStack result, CraftingHelper.ShapedPrimer primer) {
    super(group, result, primer);
  }

  @Nonnull
  @Override
  public ItemStack getCraftingResult(@Nonnull InventoryCrafting inv) {
    ItemStack inputStack = ItemStack.EMPTY;
    ItemStack outputStack = output.copy();

    for (int i = 0; i < inv.getSizeInventory(); i++) {
      ItemStack stack = inv.getStackInSlot(i);
      if (stack.getItem() == ModItems.component_pouch) {
        inputStack = stack;
        break;
      }
    }

    if (inputStack.isEmpty()) return ItemStack.EMPTY;

    outputStack.setTagCompound(inputStack.getTagCompound());

    return outputStack;
  }

  public static class Factory extends ShapedFactory<ApothecaryGroveCraftingRecipe> {
    @Override
    public ApothecaryGroveCraftingRecipe newRecipeFromPrimer(PrimerResult result) {
      return new ApothecaryGroveCraftingRecipe(result.group, result.result, result.primer);
    }
  }
}
