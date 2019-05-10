package epicsquid.roots.integration.crafttweaker;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.blamejared.mtlib.helpers.InputHelper;
import com.blamejared.mtlib.helpers.LogHelper;
import com.blamejared.mtlib.utils.BaseMapAddition;
import com.blamejared.mtlib.utils.BaseMapRemoval;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.mc1120.CraftTweaker;
import epicsquid.roots.Roots;
import epicsquid.roots.init.ModRecipes;
import epicsquid.roots.recipe.RunicShearRecipe;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenClass("mods." + Roots.MODID + ".RunicShears")
public class RunicShearsTweaker {

  @ZenMethod
  public static void addRecipe(String name, IItemStack outputDrop, IItemStack replacementBlock, IItemStack inputBlock, IItemStack jeiDisplayItem) {
    if (!InputHelper.isABlock(inputBlock) || (replacementBlock != null && !InputHelper.isABlock(replacementBlock))) {
      LogHelper.logError("Runic Shears require input and replacement to be blocks. Recipe: " + name);
    }
    CraftTweaker.LATE_ACTIONS.add(
        new Add(Collections.singletonMap(name, new RunicShearRecipe(
            CraftTweakerMC.getBlock(inputBlock.asBlock()),
            replacementBlock != null ? CraftTweakerMC.getBlock(replacementBlock.asBlock()) : Blocks.AIR,
            CraftTweakerMC.getItemStack(outputDrop),
            name + ".ct",
            jeiDisplayItem != null ? CraftTweakerMC.getItemStack(jeiDisplayItem) : ItemStack.EMPTY)))
    );
  }

  @ZenMethod
  public static void removeRecipe(IItemStack output) {
    Map<String, RunicShearRecipe> recipes = new HashMap<>();
    for(RunicShearRecipe modRecipe : ModRecipes.getRunicShearRecipes().values()) {
      if (output.matches(InputHelper.toIItemStack(modRecipe.getDrop()))) {
        recipes.put(modRecipe.getName(), modRecipe);
      }
    }
    CraftTweaker.LATE_ACTIONS.add(new Remove(recipes));
  }

  private static class Remove extends BaseMapRemoval<String, RunicShearRecipe> {

    private Remove(Map<String, RunicShearRecipe> map) {
      super("Pyre Crafting Ritual", ModRecipes.getRunicShearRecipes(), map);
    }

    @Override
    protected String getRecipeInfo(Map.Entry<String, RunicShearRecipe> recipe) {
      return LogHelper.getStackDescription(recipe.getValue().getDrop());
    }
  }

  private static class Add extends BaseMapAddition<String, RunicShearRecipe> {

    private Add(Map<String, RunicShearRecipe> map) {
      super("Runic Shears", ModRecipes.getRunicShearRecipes(), map);
    }

    @Override
    protected String getRecipeInfo(Map.Entry<String, RunicShearRecipe> recipe) {
      return LogHelper.getStackDescription(recipe.getValue().getDrop());
    }
  }
}
