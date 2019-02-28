package epicsquid.roots.api;

import epicsquid.roots.init.ModRecipes;
import epicsquid.roots.recipe.RunicCarvingRecipe;
import epicsquid.roots.recipe.RunicShearRecipe;
import net.minecraft.item.ItemStack;
import vazkii.patchouli.api.IComponentProcessor;
import vazkii.patchouli.api.IVariableProvider;
import vazkii.patchouli.common.util.ItemStackUtil;

public class RunicCarvingRecipeProcessor implements IComponentProcessor {

  private RunicCarvingRecipe recipe;

  @Override
  public void setup(IVariableProvider<String> iVariableProvider) {
    String recipeName = iVariableProvider.get("recipe");
    recipe = ModRecipes.getRunicCarvingRecipe(recipeName);
  }

  @Override
  public String process(String s) {
    if (s.equals("block")) {
      return ItemStackUtil.serializeStack(new ItemStack(recipe.getCarvingBlock().getBlock()));
    } else if (s.equals("rune")) {
      return ItemStackUtil.serializeStack(new ItemStack(recipe.getRuneBlock().getBlock()));
    } else if (s.equals("herb")) {
      return ItemStackUtil.serializeStack(new ItemStack(recipe.getHerb().getItem()));
    }
    return null;
  }

}
