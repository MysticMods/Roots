package epicsquid.roots.integration.patchouli;

import epicsquid.roots.init.ModRecipes;
import epicsquid.roots.recipe.BarkRecipe;
import net.minecraft.util.ResourceLocation;
import vazkii.patchouli.api.IComponentProcessor;
import vazkii.patchouli.api.IVariableProvider;
import vazkii.patchouli.common.util.ItemStackUtil;

public class BarkRecipeProcessor implements IComponentProcessor {
  private BarkRecipe recipe1 = null;
  private BarkRecipe recipe2 = null;

  @Override
  public void setup(IVariableProvider<String> iVariableProvider) {
    String recipe1 = null;
    String recipe2 = null;
    if (iVariableProvider.has("recipe")) {
      recipe1 = iVariableProvider.get("recipe");
    } else if (iVariableProvider.has("recipe1")) {
      recipe1 = iVariableProvider.get("recipe1");
    }
    if (iVariableProvider.has("recipe2")) {
      recipe2 = iVariableProvider.get("recipe2");
    }
    if (recipe1 == null) {
      throw new IllegalStateException("must have at least one recipe");
    }
    this.recipe1 = ModRecipes.getBarkRecipeByName(new ResourceLocation(recipe1));
    if (recipe2 != null) {
      this.recipe2 = ModRecipes.getBarkRecipeByName(new ResourceLocation(recipe2));
      if (this.recipe2 == null) {
        throw new IllegalArgumentException("invalid argument provided for recipe2: " + recipe2);
      }
    }
    if (this.recipe1 == null) {
      throw new IllegalArgumentException("invalid argumnet provided for recipe1: " + recipe1);
    }
  }

  @Override
  public String process(String s) {
    if (s.equals("input") || s.equals("input1")) {
      return ItemStackUtil.serializeStack(recipe1.getBlockStack());
    }
    if (s.equals("input2")) {
      if (recipe1 == null) {
        throw new IllegalStateException("this is not a dual-recipe template");
      }
      return ItemStackUtil.serializeStack(recipe2.getBlockStack());
    }

    if (s.equals("output") || s.equals("output1")) {
      return ItemStackUtil.serializeStack(recipe1.getItem());
    }
    if (s.equals("output2")) {
      if (recipe1 == null) {
        throw new IllegalStateException("this is not a dual-recipe template");
      }
      return ItemStackUtil.serializeStack(recipe2.getItem());
    }

    return null;
  }
}
