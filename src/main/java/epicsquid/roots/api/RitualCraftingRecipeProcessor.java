package epicsquid.roots.api;

import java.util.ArrayList;
import java.util.List;

import epicsquid.roots.init.ModRecipes;
import epicsquid.roots.recipe.PyreCraftingRecipe;
import epicsquid.roots.ritual.RitualBase;
import epicsquid.roots.ritual.RitualRegistry;
import net.minecraft.item.ItemStack;
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
    if(s.startsWith("item")) {
      int index = Integer.parseInt(s.substring(4)) - 1;
      ItemStack ingredient = pyreCraftingRecipe.getIngredients().get(index);

      return ItemStackUtil.serializeStack(ingredient);
    }

    if(s.equalsIgnoreCase("result")){
      return ItemStackUtil.serializeStack(pyreCraftingRecipe.getResult());
    }
    return null;
  }

}
