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
  private BarkRecipe recipe3 = null;
  private BarkRecipe recipe4 = null;
  private BarkRecipe recipe5 = null;

  @Override
  public void setup(IVariableProvider<String> iVariableProvider) {
    String recipe1 = iVariableProvider.get("recipe1");
    String recipe2 = iVariableProvider.get("recipe2");
    if (recipe1 == null || recipe2 == null) {
      throw new IllegalStateException("must have at least two recipes");
    }
    this.recipe1 = ModRecipes.getBarkRecipeByName(new ResourceLocation(recipe1));
    this.recipe2 = ModRecipes.getBarkRecipeByName(new ResourceLocation(recipe2));
    if (this.recipe1 == null || this.recipe2 == null) {
      throw new IllegalArgumentException("invalid argumnet provided for recipes: " + recipe1 + ", " + recipe2);
    }

    String recipe3 = null;
    String recipe4 = null;
    String recipe5 = null;
    boolean quint = false;
    if (iVariableProvider.has("recipe3")) {
      recipe3 = iVariableProvider.get("recipe3");
      quint = true;
    }
    if (iVariableProvider.has("recipe4")) {
      recipe4 = iVariableProvider.get("recipe4");
      quint = true;
    }
    if (iVariableProvider.has("recipe5")) {
      recipe5 = iVariableProvider.get("recipe5");
      quint = true;
    }
    if (quint && (recipe3 == null || recipe4 == null || recipe5 == null)) {
      throw new IllegalStateException("must have five total recipes recipes");
    }
    if (quint) {
      this.recipe3 = ModRecipes.getBarkRecipeByName(new ResourceLocation(recipe3));
      this.recipe4 = ModRecipes.getBarkRecipeByName(new ResourceLocation(recipe4));
      this.recipe5 = ModRecipes.getBarkRecipeByName(new ResourceLocation(recipe5));
      if (this.recipe3 == null || this.recipe4 == null || this.recipe5 == null) {
        throw new IllegalArgumentException("invalid argument provided for recipes: " + recipe3 + ", " + recipe4 + ", " + recipe5);
      }
    }
  }

  @Override
  public String process(String s) {
    if (s.equals("input1")) {
      return ItemStackUtil.serializeStack(recipe1.getBlockStack());
    }
    if (s.equals("input2")) {
      return ItemStackUtil.serializeStack(recipe2.getBlockStack());
    }
    if (s.equals("input3")) {
      return ItemStackUtil.serializeStack(recipe3.getBlockStack());
    }
    if (s.equals("input4")) {
      return ItemStackUtil.serializeStack(recipe4.getBlockStack());
    }
    if (s.equals("input5")) {
      return ItemStackUtil.serializeStack(recipe5.getBlockStack());
    }

    if (s.equals("output1")) {
      return ItemStackUtil.serializeStack(recipe1.getItem());
    }
    if (s.equals("output2")) {
      return ItemStackUtil.serializeStack(recipe2.getItem());
    }

    if (s.equals("output3")) {
      return ItemStackUtil.serializeStack(recipe3.getItem());
    }
    if (s.equals("output4")) {
      return ItemStackUtil.serializeStack(recipe4.getItem());
    }
    if (s.equals("output5")) {
      return ItemStackUtil.serializeStack(recipe5.getItem());
    }

    return null;
  }
}
