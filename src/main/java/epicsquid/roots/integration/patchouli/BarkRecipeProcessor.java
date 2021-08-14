package epicsquid.roots.integration.patchouli;

import epicsquid.roots.init.ModRecipes;
import epicsquid.roots.recipe.BarkRecipe;
import net.minecraft.item.ItemStack;
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

    if (recipe1 != null) {
      this.recipe1 = ModRecipes.getBarkRecipeByName(new ResourceLocation(recipe1));
    }
    if (recipe2 != null) {
      this.recipe2 = ModRecipes.getBarkRecipeByName(new ResourceLocation(recipe2));
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

    if (quint) {
      if (recipe3 != null) {
        this.recipe3 = ModRecipes.getBarkRecipeByName(new ResourceLocation(recipe3));
      }
      if (recipe4 != null) {
        this.recipe4 = ModRecipes.getBarkRecipeByName(new ResourceLocation(recipe4));
      }
      if (recipe5 != null) {
        this.recipe5 = ModRecipes.getBarkRecipeByName(new ResourceLocation(recipe5));
      }
    }
  }

  private final String EMPTY = ItemStackUtil.serializeStack(ItemStack.EMPTY);

  @Override
  public String process(String s) {
    if (s.equals("input1")) {
      if (recipe1 == null) {
        return EMPTY;
      }
      return ItemStackUtil.serializeStack(recipe1.getBlockStack());
    }
    if (s.equals("input2")) {
      if (recipe2 == null) {
        return EMPTY;
      }
      return ItemStackUtil.serializeStack(recipe2.getBlockStack());
    }
    if (s.equals("input3")) {
      if (recipe3 == null) {
        return EMPTY;
      }
      return ItemStackUtil.serializeStack(recipe3.getBlockStack());
    }
    if (s.equals("input4")) {
      if (recipe4 == null) {
        return EMPTY;
      }
      return ItemStackUtil.serializeStack(recipe4.getBlockStack());
    }
    if (s.equals("input5")) {
      if (recipe5 == null) {
        return EMPTY;
      }
      return ItemStackUtil.serializeStack(recipe5.getBlockStack());
    }

    if (s.equals("output1")) {
      if (recipe1 == null) {
        return EMPTY;
      }
      return ItemStackUtil.serializeStack(recipe1.getItem());
    }
    if (s.equals("output2")) {
      if (recipe2 == null) {
        return EMPTY;
      }
      return ItemStackUtil.serializeStack(recipe2.getItem());
    }

    if (s.equals("output3")) {
      if (recipe3 == null) {
        return EMPTY;
      }
      return ItemStackUtil.serializeStack(recipe3.getItem());
    }
    if (s.equals("output4")) {
      if (recipe4 == null) {
        return EMPTY;
      }
      return ItemStackUtil.serializeStack(recipe4.getItem());
    }
    if (s.equals("output5")) {
      if (recipe5 == null) {
        return EMPTY;
      }
      return ItemStackUtil.serializeStack(recipe5.getItem());
    }

    return null;
  }
}
