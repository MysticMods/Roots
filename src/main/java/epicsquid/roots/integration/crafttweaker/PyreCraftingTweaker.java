package epicsquid.roots.integration.crafttweaker;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.blamejared.mtlib.helpers.InputHelper;
import com.blamejared.mtlib.helpers.LogHelper;
import com.blamejared.mtlib.utils.BaseMapAddition;
import com.blamejared.mtlib.utils.BaseMapRemoval;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.mc1120.CraftTweaker;
import epicsquid.roots.Roots;
import epicsquid.roots.init.ModRecipes;
import epicsquid.roots.recipe.PyreCraftingRecipe;
import net.minecraft.item.crafting.Ingredient;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenClass("mods." + Roots.MODID + ".Pyre")
public class PyreCraftingTweaker {

  @ZenMethod
  public static void addRecipe(String name, IItemStack output, IIngredient[] inputs) {
    if (inputs.length != 5) {
      CraftTweakerAPI.getLogger().logError("Pyre Crafting Ritual must have 5 items: " + name);
    }
    CraftTweaker.LATE_ACTIONS.add(new Add(Collections.singletonMap(name + ".ct", new PyreCraftingRecipe(InputHelper.toStack(output)).addIngredients(
        Arrays.stream(inputs).map(CraftTweakerMC::getIngredient).toArray(Ingredient[]::new)).setName(name + ".ct"))));
  }

  @ZenMethod
  public static void removeRecipe(IItemStack output) {
    Map<String, PyreCraftingRecipe> recipes = new HashMap<>();
    for(PyreCraftingRecipe modRecipe : ModRecipes.getPyreCraftingRecipes().values()) {
      if (output.matches(InputHelper.toIItemStack(modRecipe.getResult()))) {
        recipes.put(modRecipe.getName(), modRecipe);
      }
    }
    CraftTweaker.LATE_ACTIONS.add(new Remove(recipes));
  }

  private static class Remove extends BaseMapRemoval<String, PyreCraftingRecipe> {

    private Remove(Map<String, PyreCraftingRecipe> map) {
      super("Pyre Crafting Ritual", ModRecipes.getPyreCraftingRecipes(), map);
    }

    @Override
    protected String getRecipeInfo(Map.Entry<String, PyreCraftingRecipe> recipe) {
      return LogHelper.getStackDescription(recipe.getValue().getResult());
    }
  }

  private static class Add extends BaseMapAddition<String, PyreCraftingRecipe> {

    private Add(Map<String, PyreCraftingRecipe> map) {
      super("Pyre Crafting Ritual", ModRecipes.getPyreCraftingRecipes(), map);
    }

    @Override
    protected String getRecipeInfo(Map.Entry<String, PyreCraftingRecipe> recipe) {
      return LogHelper.getStackDescription(recipe.getValue().getResult());
    }
  }
}
