package epicsquid.roots.integration.patchouli;

import epicsquid.roots.init.ModRecipes;
import epicsquid.roots.recipe.RunicShearRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import vazkii.patchouli.api.IComponentProcessor;
import vazkii.patchouli.api.IVariableProvider;
import vazkii.patchouli.common.util.ItemStackUtil;

public class RunicShearsRecipeProcessor implements IComponentProcessor {

  private Ingredient output = Ingredient.EMPTY;
  private ItemStack input = ItemStack.EMPTY;

  @Override
  public void setup(IVariableProvider<String> iVariableProvider) {
    String recipeName = iVariableProvider.get("recipe");
    RunicShearRecipe recipe = ModRecipes.getRunicShearRecipe(recipeName);
    if (recipe != null) {
      output = recipe.getDropMatch();

      if (!recipe.getOptionalDisplayItem().isEmpty()) {
        input = recipe.getOptionalDisplayItem();
      }
    }
  }

  @Override
  public String process(String s) {
    if ((output == Ingredient.EMPTY || input.isEmpty())) {
      return ItemStackUtil.serializeStack(ItemStack.EMPTY);
    }
    if (s.equals("itemIn")) {
      return ItemStackUtil.serializeStack(input);
    } else if (s.equals("itemOut")) {
      return ItemStackUtil.serializeIngredient(output);
    }
    return null;
  }

}
