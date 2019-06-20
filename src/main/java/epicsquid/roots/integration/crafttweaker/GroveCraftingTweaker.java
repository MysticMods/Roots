package epicsquid.roots.integration.crafttweaker;

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
import epicsquid.roots.recipe.FeyCraftingRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

@ZenRegister
@ZenClass("mods." + Roots.MODID + ".Grove")
public class GroveCraftingTweaker {

  @ZenMethod
  public static void addRecipe(String name, IItemStack output, IIngredient[] inputs) {
    if (inputs.length != 5) {
      CraftTweakerAPI.logError("Grove Crafting Ritual must have 5 items: " + name);
    }
    CraftTweaker.LATE_ACTIONS.add(new Add(Collections.singletonMap(new ResourceLocation(Roots.MODID, name + ".ct"), new FeyCraftingRecipe(InputHelper.toStack(output)).addIngredients((Object) Stream.of(inputs).map(CraftTweakerMC::getIngredient).toArray(Ingredient[]::new)).setName(name + ".ct"))));
  }

  @ZenMethod
  public static void removeRecipe(IItemStack output) {
    Map<ResourceLocation, FeyCraftingRecipe> recipes = new HashMap<>();
    for (Map.Entry<ResourceLocation, FeyCraftingRecipe> modRecipe : ModRecipes.getFeyCraftingRecipes().entrySet()) {
      if (output.matches(InputHelper.toIItemStack(modRecipe.getValue().getResult()))) {
        recipes.put(modRecipe.getKey(), modRecipe.getValue());
      }
    }
    CraftTweaker.LATE_ACTIONS.add(new Remove(recipes));
  }

  private static class Remove extends BaseMapRemoval<ResourceLocation, FeyCraftingRecipe> {

    private Remove(Map<ResourceLocation, FeyCraftingRecipe> map) {
      super("Grove Crafting Ritual", ModRecipes.getFeyCraftingRecipes(), map);
    }

    @Override
    protected String getRecipeInfo(Map.Entry<ResourceLocation, FeyCraftingRecipe> recipe) {
      return LogHelper.getStackDescription(recipe.getValue().getResult());
    }
  }

  private static class Add extends BaseMapAddition<ResourceLocation, FeyCraftingRecipe> {

    private Add(Map<ResourceLocation, FeyCraftingRecipe> map) {
      super("Grove Crafting Ritual", ModRecipes.getFeyCraftingRecipes(), map);
    }

    @Override
    protected String getRecipeInfo(Map.Entry<ResourceLocation, FeyCraftingRecipe> recipe) {
      return LogHelper.getStackDescription(recipe.getValue().getResult());
    }
  }
}
