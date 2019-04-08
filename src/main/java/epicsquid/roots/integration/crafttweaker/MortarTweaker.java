package epicsquid.roots.integration.crafttweaker;

import com.blamejared.mtlib.helpers.InputHelper;
import com.blamejared.mtlib.helpers.LogHelper;
import com.blamejared.mtlib.utils.BaseAction;
import com.blamejared.mtlib.utils.BaseListAddition;
import com.blamejared.mtlib.utils.BaseListRemoval;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.mc1120.CraftTweaker;
import epicsquid.roots.Roots;
import epicsquid.roots.init.ModRecipes;
import epicsquid.roots.recipe.MortarRecipe;
import epicsquid.roots.spell.SpellBase;
import epicsquid.roots.spell.SpellRegistry;
import net.minecraft.item.crafting.Ingredient;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ZenRegister
@ZenClass("mods." + Roots.MODID + ".Mortar")
public class MortarTweaker {

  @ZenMethod
  public static void changeSpell(String spellName, IIngredient[] inputs) {
    if (inputs.length != 5) {
      CraftTweakerAPI.getLogger().logError(String.format("Invalid ingredients length to change recipe for spell %s: need 5 ingredients, got %d.", spellName, inputs.length));
    } else {
      List<Ingredient> ingredients = Stream.of(inputs).map(CraftTweakerMC::getIngredient).collect(Collectors.toList());
      CraftTweaker.LATE_ACTIONS.add(new ChangeSpell(spellName, ingredients));
    }
  }

  @ZenMethod
  public static void addRecipe(IItemStack output, IIngredient[] inputs) {
    if (inputs.length != 5) {
      if (inputs.length == 1) {
        CraftTweaker.LATE_ACTIONS.add(new AddMultiple(ModRecipes.getMortarRecipeList(CraftTweakerMC.getItemStack(output), CraftTweakerMC.getIngredient(inputs[0]))));
      } else {
        CraftTweakerAPI.getLogger().logError("Mortar recipe must have 5 items total, or 1 single item.");
      }
    } else {
      CraftTweaker.LATE_ACTIONS.add(new Add(Collections.singletonList(new MortarRecipe(
              CraftTweakerMC.getItemStack(output),
              Arrays.stream(inputs).map(CraftTweakerMC::getIngredient).toArray(Ingredient[]::new))))
      );
    }
  }

  @ZenMethod
  public static void removeRecipe(IItemStack output) {
    List<MortarRecipe> recipes = new ArrayList<>();
    for(MortarRecipe recipe : ModRecipes.getMortarRecipes()) {
      if (output.matches(InputHelper.toIItemStack(recipe.getResult()))) {
        recipes.add(recipe);
      }
    }
    CraftTweaker.LATE_ACTIONS.add(new Remove(recipes));
  }

  private static class Remove extends BaseListRemoval<MortarRecipe> {

    private Remove(List<MortarRecipe> list) {
      super("MortarRecipe", ModRecipes.getMortarRecipes(), list);
    }

    @Override
    protected String getRecipeInfo(MortarRecipe recipe) {
      return LogHelper.getStackDescription(recipe.getResult());
    }
  }

  private static class Add extends BaseListAddition<MortarRecipe> {

    private Add(List<MortarRecipe> list) {
      super("MortarRecipe", ModRecipes.getMortarRecipes(), list);
    }

    @Override
    protected String getRecipeInfo(MortarRecipe recipe) {
      return LogHelper.getStackDescription(recipe.getResult());
    }
  }

  private static class AddMultiple extends BaseAction {

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
    public String getRecipeInfo() {
      return String.format("MultiMortarRecipe for variable input of %s into variable output of %s.", multiRecipes.get(0).getIngredients().get(0).getMatchingStacks()[0].getDisplayName(), multiRecipes.get(0).getResult().getDisplayName());
    }
  }

  private static class ChangeSpell extends BaseAction {
    private String spell;
    private List<Ingredient> ingredients;

    private ChangeSpell (String spell, List<Ingredient> ingredients) {
      super("ChangeSpellRecipe");
      this.spell = spell;
      this.ingredients = ingredients;
    }

    @Override
    public void apply () {
      SpellBase spell = SpellRegistry.getSpell(this.spell);
      if (spell == null) {
        CraftTweakerAPI.logError("Invalid spell name: %s" + this.spell);
      } else {
        spell.setIngredients(this.ingredients);
      }
    }

    @Override
    public String getRecipeInfo () {
      return String.format("ChangeSpellRecipe to change spell %s", spell);
    }
  }
}
