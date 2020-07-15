package epicsquid.roots.recipe;

import epicsquid.roots.config.MossConfig;
import epicsquid.roots.init.ModItems;
import net.minecraft.item.ItemStack;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MossRecipe {
  private static ItemStack TERRA_MOSS = ItemStack.EMPTY;
  private static List<MossRecipe> RECIPE_LIST = null;

  private ItemStack input;
  private ItemStack output;

  public MossRecipe(ItemStack input, ItemStack output) {
    this.input = input;
    this.output = output;
  }

  public MossRecipe(Map.Entry<ItemStack, ItemStack> entry) {
    this.input = entry.getKey();
    this.output = entry.getValue();
  }

  public ItemStack getInput() {
    return input;
  }

  public ItemStack getOutput() {
    return output;
  }

  public static List<MossRecipe> getRecipeList(Map<ItemStack, ItemStack> recipes) {
    if (RECIPE_LIST == null) {
      RECIPE_LIST = recipes.entrySet().stream().map(MossRecipe::new).collect(Collectors.toList());
    }
    return RECIPE_LIST;
  }

  public static List<MossRecipe> getRecipeList() {
    if (RECIPE_LIST == null) {
      return getRecipeList(MossConfig.getMossyCobblestones());
    }

    return RECIPE_LIST;
  }

  public static ItemStack getTerraMoss() {
    if (TERRA_MOSS.isEmpty()) {
      TERRA_MOSS = new ItemStack(ModItems.terra_moss);
    }
    return TERRA_MOSS;
  }
}
