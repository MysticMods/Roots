package epicsquid.roots.integration.patchouli;

import epicsquid.roots.init.ModRecipes;
import epicsquid.roots.recipe.PyreCraftingRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import vazkii.patchouli.api.IComponentProcessor;
import vazkii.patchouli.api.IVariableProvider;
import vazkii.patchouli.common.util.ItemStackUtil;

public class RitualCraftingRecipeProcessor implements IComponentProcessor {

  private PyreCraftingRecipe pyreCraftingRecipe = null;

  @Override
  public void setup(IVariableProvider<String> iVariableProvider) {
    String ritualName = iVariableProvider.get("recipe");
    pyreCraftingRecipe = ModRecipes.getCraftingRecipe(ritualName);
  }

  @Override
  public String process(String s) {
    if ((s.startsWith("item") || s.equalsIgnoreCase("result")) && pyreCraftingRecipe == null) {
      return ItemStackUtil.serializeStack(ItemStack.EMPTY);
    }
    if (s.startsWith("item")) {
      int index = Integer.parseInt(s.substring(4)) - 1;

      if (index >= pyreCraftingRecipe.getIngredients().size()) {
        return ItemStackUtil.serializeStack(ItemStack.EMPTY);
      }

      Ingredient ingredient = pyreCraftingRecipe.getIngredients().get(index);

      return ItemStackUtil.serializeIngredient(ingredient);
    }

    if (s.equalsIgnoreCase("result")) {
      return ItemStackUtil.serializeStack(pyreCraftingRecipe.getResult());
    }
    return null;
  }

}
