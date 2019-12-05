/*package epicsquid.roots.integration.crafttweaker.tweaks;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.mc1120.CraftTweaker;
import epicsquid.roots.Roots;
import epicsquid.roots.init.ModRecipes;
import epicsquid.roots.integration.crafttweaker.Action;
import epicsquid.roots.recipe.MortarRecipe;
import epicsquid.roots.spell.SpellBase;
import epicsquid.roots.spell.SpellRegistry;
import epicsquid.roots.util.zen.ZenDocAppend;
import epicsquid.roots.util.zen.ZenDocArg;
import epicsquid.roots.util.zen.ZenDocClass;
import epicsquid.roots.util.zen.ZenDocMethod;
import jeresources.util.LogHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ZenDocClass("mods.roots.Mortar")
@ZenDocAppend({"docs/include/mortar.example.md"})
@ZenRegister
@ZenClass("mods." + Roots.MODID + ".Mortar")
public class MortarTweaker {

  @ZenDocMethod(
      order = 1,
      args = {
          @ZenDocArg(arg = "output", info = "the item output of this recipe"),
          @ZenDocArg(arg = "inputs", info = "an array of ingredients that is either 5 long or 1 long")
      }
  )
  @ZenMethod
  public static void addRecipe(IItemStack output, IIngredient[] inputs) {
    if (inputs.length != 5) {
      if (inputs.length == 1) {
        CraftTweaker.LATE_ACTIONS.add(new AddMultiple(ModRecipes.getMortarRecipeList(CraftTweakerMC.getItemStack(output), CraftTweakerMC.getIngredient(inputs[0]))));
      } else {
        CraftTweakerAPI.getLogger().logError("Mortar recipe must have 5 items total, or 1 single item.");
      }
    } else {
      CraftTweaker.LATE_ACTIONS.add(new Add(CraftTweakerMC.getItemStack(output), Stream.of(inputs).map(CraftTweakerMC::getIngredient).toArray(Ingredient[]::new)));
    }
  }

  @ZenDocMethod(
      order = 2,
      args = {
          @ZenDocArg(arg = "spellName", info = "the name of the spell as in the spell registry"),
          @ZenDocArg(arg = "inputs", info = "an array of 5 items that are the new ingredients for the recipe")
      }
  )
  @ZenMethod
  public static void changeSpell(String spellName, IIngredient[] inputs) {
    if (inputs.length != 5) {
      CraftTweakerAPI.getLogger().logError(String.format("Invalid ingredients length to change recipe for spell %s: need 5 ingredients, got %d.", spellName, inputs.length));
    } else {
      List<Ingredient> ingredients = Stream.of(inputs).map(CraftTweakerMC::getIngredient).collect(Collectors.toList());
      CraftTweaker.LATE_ACTIONS.add(new ChangeSpell(spellName, ingredients));
    }
  }

  @ZenDocMethod(
      order = 3,
      args = {
          @ZenDocArg(arg = "output", info = "the item stack produced by the recipe")
      }
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
    private Ingredient[] inputs;

    private Add(ItemStack output, Ingredient[] inputs) {
      super("MortarRecipe");
      this.output = output;
      this.inputs = inputs;
    }

    @Override
    public void apply() {
      MortarRecipe recipe = new MortarRecipe(output, inputs);
      ModRecipes.addMortarRecipe(recipe);
    }

    @Override
    public String describe() {
      return "Adding MortarRecipe to make " + output;
    }
  }

  private static class AddMultiple extends Action {

    private List<MortarRecipe> multiRecipes;

    private AddMultiple(List<MortarRecipe> recipes) {
      super("MultiMortarRecipe");
      multiRecipes = recipes;
    }

    @Override
    public void apply() {
      ModRecipes.getMortarRecipes().addAll(multiRecipes);
    }

    @Override
    public String describe() {
      return String.format("MultiMortarRecipe for variable input of %s into variable output of %s.", multiRecipes.get(0).getIngredients().get(0).getMatchingStacks()[0].getDisplayName(), multiRecipes.get(0).getResult().getDisplayName());
    }
  }

  private static class ChangeSpell extends Action {
    private String spell;
    private List<Ingredient> ingredients;

    private ChangeSpell(String spell, List<Ingredient> ingredients) {
      super("ChangeSpellRecipe");
      this.spell = spell;
      this.ingredients = ingredients;
    }

    @Override
    public void apply() {
      SpellBase spell = SpellRegistry.getSpell(this.spell);
      if (spell == null) {
        CraftTweakerAPI.logError("Invalid spell name: %s" + this.spell);
      } else {
        spell.setIngredients(this.ingredients);
      }
    }

    @Override
    public String describe() {
      return String.format("ChangeSpellRecipe to change spell %s", spell);
    }
  }
}*/
