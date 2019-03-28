package epicsquid.roots.api;

import epicsquid.roots.init.ModRecipes;
import epicsquid.roots.recipe.RunicShearRecipe;
import net.minecraft.item.ItemStack;
import vazkii.patchouli.api.IComponentProcessor;
import vazkii.patchouli.api.IVariableProvider;
import vazkii.patchouli.common.util.ItemStackUtil;

public class RunicShearsRecipeProcessor implements IComponentProcessor {

  private ItemStack output = ItemStack.EMPTY;
  private ItemStack input = ItemStack.EMPTY;

  @Override
  public void setup(IVariableProvider<String> iVariableProvider) {
    String recipeName = iVariableProvider.get("recipe");
    RunicShearRecipe recipe = ModRecipes.getRunicShearRecipe(recipeName);
    output = recipe.getDrop();

    if (recipe.isBlockRecipe()) {
      if (!recipe.getOptionalDisplayItem().isEmpty()) {
        input = recipe.getOptionalDisplayItem();
      }
    }
  }

  @Override
  public String process(String s) {
    if (s.equals("itemIn")) {
      return ItemStackUtil.serializeStack(input);
    } else if (s.equals("itemOut")) {
      return ItemStackUtil.serializeStack(output);
    }
    return null;
  }

}
