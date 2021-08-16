package epicsquid.roots.integration.crafttweaker.tweaks;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.mc1120.CraftTweaker;
import epicsquid.roots.Roots;
import epicsquid.roots.init.ModRecipes;
import epicsquid.roots.integration.crafttweaker.Action;
import epicsquid.roots.integration.crafttweaker.CTUtil;
import epicsquid.roots.util.IngredientWithStack;
import epicsquid.roots.util.zen.ZenDocAppend;
import epicsquid.roots.util.zen.ZenDocArg;
import epicsquid.roots.util.zen.ZenDocClass;
import epicsquid.roots.util.zen.ZenDocMethod;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenDocClass("mods.roots.Chrysopoeia")
@ZenDocAppend({"docs/include/chrysopoeia.example.md"})
@ZenRegister
@ZenClass("mods." + Roots.MODID + ".Chrysopoeia")
public class ChrysopoeiaTweaker {

  @ZenDocMethod(
      order = 1,
      args = {
          @ZenDocArg(arg = "name", info = "the name of the recipe being added"),
          @ZenDocArg(arg = "ingredient", info = "a single ingredient (may have variable stack size)"),
          @ZenDocArg(arg = "output", info = "the output produce by Transubstantiation")
      },
      description = "Adds a transmutative recipe that converts an input (in the form of an ingredient, possibly with a variable stack size, transforms are supported), into an output (as an itemstack). Requires a name."
  )
  @ZenMethod
  public static void addRecipe(String name, IIngredient ingredient, IItemStack output) {
    CraftTweaker.LATE_ACTIONS.add(new Add(name, CTUtil.ingredientWithStack(ingredient), CraftTweakerMC.getItemStack(output)));
  }

  @ZenDocMethod(
      order = 2,
      args = {
          @ZenDocArg(arg = "output", info = "the output of the recipe you wish to remove")
      },
      description = "Removes a transmutative recipe based on the output of the recipe, matches regardless of stack sizes."
  )
  @ZenMethod
  public static void removeRecipeByOutput(IItemStack output) {
    CraftTweaker.LATE_ACTIONS.add(new Remove(CraftTweakerMC.getItemStack(output), true));
  }

  @ZenDocMethod(
      order = 3,
      args = {
          @ZenDocArg(arg = "input", info = "the exact input of the recipe you wish to remove")
      },
      description = "Removes a transmutative recipe based on the exact input (including size, tag, etc)"
  )
  @ZenMethod
  public static void removeRecipe(IItemStack input) {
    CraftTweaker.LATE_ACTIONS.add(new Remove(CraftTweakerMC.getItemStack(input), false));
  }


  private static class Remove extends Action {
    private final ItemStack output;
    private final boolean isOutput;

    public Remove(ItemStack output, boolean isOutput) {
      super("remove transubstantiation recipe");
      this.output = output;
      this.isOutput = isOutput;
    }

    @Override
    public void apply() {
      if (isOutput) {
        if (!ModRecipes.removeChrysopoeiaRecipeByOutput(output)) {
          CraftTweakerAPI.logError("Unable to remove Chrysopoeia recipe for output item " + output + ": it does not exist as a recipe.");
        }
      } else {
        if (!ModRecipes.removeChrysopoeiaRecipe(output)) {
          CraftTweakerAPI.logError("Unable to remove Chrysopoeia recipe for input item " + output + ": it does not exist as a recipe.");
        }
      }
    }

    @Override
    public String describe() {
      return String.format("Recipe to remove %s from Transubstantiation Recipes", output.toString());
    }
  }

  private static class Add extends Action {
    private final IngredientWithStack inputs;
    private final ItemStack output;
    private final String name;

    public Add(String name, IngredientWithStack inputs, ItemStack output) {
      super("Add transubstantiation recipe");
      this.name = name;
      this.inputs = inputs;
      this.output = output;
    }

    @Override
    public void apply() {
      ModRecipes.addChrysopoeiaRecipe(name, inputs, output);
    }

    @Override
    public String describe() {
      return String.format("Recipe (%s) to add %s to Transubstantion Recipes", name, output.toString());
    }
  }
}
