package epicsquid.roots.recipe;

import epicsquid.roots.config.GeneralConfig;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MossRecipe {
  private ItemStack input;
  private ItemStack output;

  public MossRecipe(ItemStack input, ItemStack output) {
    this.input = input;
    this.output = output;
  }

  public MossRecipe (Map.Entry<ItemStack, ItemStack> entry) {
    this.input = entry.getKey();
    this.output = entry.getValue();
  }

  public ItemStack getInput() {
    return input;
  }

  public ItemStack getOutput() {
    return output;
  }

  public static List<MossRecipe> getRecipeList (Map<ItemStack, ItemStack> recipes) {
    return recipes.entrySet().stream().map(MossRecipe::new).collect(Collectors.toList());
  }

  public static List<MossRecipe> getRecipeList () {
    return getRecipeList(GeneralConfig.getMossyCobblestones());
  }
}
