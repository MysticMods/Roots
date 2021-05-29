package epicsquid.roots.integration.crafttweaker.tweaks;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenDoc;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.mc1120.CraftTweaker;
import epicsquid.roots.Roots;
import epicsquid.roots.init.ModRecipes;
import epicsquid.roots.integration.crafttweaker.Action;
import epicsquid.roots.integration.crafttweaker.recipes.CTMortarRecipe;
import epicsquid.roots.integration.crafttweaker.recipes.CTSpellRecipe;
import epicsquid.roots.spell.SpellBase;
import epicsquid.roots.spell.SpellRegistry;
import epicsquid.roots.util.zen.ZenDocAppend;
import epicsquid.roots.util.zen.ZenDocArg;
import epicsquid.roots.util.zen.ZenDocClass;
import epicsquid.roots.util.zen.ZenDocMethod;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@ZenDocClass("mods.roots.Mortar")
@ZenDocAppend({"docs/include/mortar.example.md"})
@ZenRegister
@ZenClass("mods." + Roots.MODID + ".Mortar")
public class MortarTweaker {

  @ZenDocMethod(
      order = 1,
      args = {
          @ZenDocArg(arg = "name", info = "the name of this recipe, should match a recipe being replaced"),
          @ZenDocArg(arg = "output", info = "the item output of this recipe"),
          @ZenDocArg(arg = "inputs", info = "an array of ingredients that is either 5 long or 1 long")
      },
      description = "Creates a recipe to create output from an array of ingredients (allows transformations). If the array is 5 long, a single recipe will be produced. If the array consists of only one ingredient, 5 separate recipes will be produced, with the output adjusted every time to compensate."
  )
  @ZenMethod
  public static void addRecipe(String name, IItemStack output, IIngredient[] inputs) {
    if (inputs.length == 1) {
      CraftTweaker.LATE_ACTIONS.add(new AddMultiple(name, CraftTweakerMC.getItemStack(output), inputs[0]));
    } else if (inputs.length == 5) {
      CraftTweaker.LATE_ACTIONS.add(new Add(name, CraftTweakerMC.getItemStack(output), Arrays.asList(inputs)));
    } else {
      CraftTweakerAPI.getLogger().logError("Mortar recipe must have 5 thaumcraft.items total, or 1 single item.");
    }
  }

  @ZenDocMethod(
      order = 2,
      args = {
          @ZenDocArg(arg = "spellName", info = "the name of the spell as in the spell registry"),
          @ZenDocArg(arg = "inputs", info = "an array of 5 thaumcraft.items that are the new ingredients for the recipe")
      },
      description = "Allows the modification of the recipe for a Spell using the specified array of 5 ingredients (allows for transformations)."
  )
  @ZenMethod
  public static void changeSpell(String spellName, IIngredient[] inputs) {
    if (inputs.length != 5) {
      CraftTweakerAPI.getLogger().logError(String.format("Invalid ingredients length to change recipe for spell %s: need 5 ingredients, got %d.", spellName, inputs.length));
    } else {
      CraftTweaker.LATE_ACTIONS.add(new ChangeSpell(spellName, Arrays.asList(inputs)));
    }
  }

  @ZenDocMethod(
      order = 3,
      args = {
          @ZenDocArg(arg = "output", info = "the item stack produced by the recipe")
      },
      description = "Removes a Mortar Recipe based on output. Compares output to existing recipes without regard for size, meaning that matching recipes with 1-5 inputs and 1-5x outputs will all be removed."
  )
  @ZenMethod
  public static void removeRecipe(IItemStack output) {
    CraftTweaker.LATE_ACTIONS.add(new Remove(CraftTweakerMC.getItemStack(output)));
  }

  private static class Remove extends Action {
    private ItemStack output;

    private Remove(ItemStack output) {
      super("MortarRecipe");
      this.output = output;
    }

    @Override
    public String describe() {
      return "Removing Mortar Recipe for item output: " + output;
    }

    @Override
    public void apply() {
      ModRecipes.removeMortarRecipes(output);
    }
  }

  private static class Add extends Action {
    private ItemStack output;
    private List<IIngredient> inputs;
    private String name;

    private Add(String name, ItemStack output, List<IIngredient> inputs) {
      super("MortarRecipe");
      this.output = output;
      this.inputs = inputs;
      this.name = name;
    }

    @Override
    public void apply() {
      CTMortarRecipe recipe = new CTMortarRecipe(name, output, inputs);
      ModRecipes.addMortarRecipe(recipe);
    }

    @Override
    public String describe() {
      return "Adding MortarRecipe to make " + output;
    }
  }

  private static class AddMultiple extends Action {
    private ItemStack output;
    private IIngredient input;
    private String name;

    private AddMultiple(String name, ItemStack output, IIngredient input) {
      super("MultiMortarRecipe");
      this.output = output;
      this.input = input;
      this.name = name;
    }

    @Override
    public void apply() {
      ItemStack output2 = output.copy();
      output2.setCount(output.getCount() * 2);
      ItemStack output3 = output.copy();
      output3.setCount(output.getCount() * 3);
      ItemStack output4 = output.copy();
      output4.setCount(output.getCount() * 4);
      ItemStack output5 = output.copy();
      output5.setCount(output.getCount() * 5);

      List<CTMortarRecipe> recipes = Arrays.asList(
          new CTMortarRecipe(name + "_1", output.copy(), Collections.singletonList(input)),
          new CTMortarRecipe(name + "_2", output2, Arrays.asList(input, input)),
          new CTMortarRecipe(name + "_3", output3, Arrays.asList(input, input, input)),
          new CTMortarRecipe(name + "_4", output4, Arrays.asList(input, input, input, input)),
          new CTMortarRecipe(name + "_5", output5, Arrays.asList(input, input, input, input, input))
      );
      recipes.forEach(ModRecipes::addMortarRecipe);
    }

    @Override
    public String describe() {
      return String.format("MultiMortarRecipe for variable input of %s into variable output of %s.", input.toCommandString(), output.getDisplayName());
    }
  }

  private static class ChangeSpell extends Action {
    private String spell;
    private List<IIngredient> ingredients;

    private ChangeSpell(String spell, List<IIngredient> ingredients) {
      super("ChangeSpellRecipe");
      this.spell = spell;
      this.ingredients = ingredients;
    }

    @Override
    public void apply() {
      SpellBase spell = SpellRegistry.getSpell(this.spell);
      if (spell == null) {
        CraftTweakerAPI.logError("Invalid spell name: " + this.spell);
      } else {
        CTSpellRecipe recipe = new CTSpellRecipe(spell, ingredients);
        spell.setRecipe(recipe);
      }
    }

    @Override
    public String describe() {
      return String.format("ChangeSpellRecipe to change spell %s", spell);
    }
  }
}
