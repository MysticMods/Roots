package epicsquid.roots.recipe;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;

public class RecipeRegistry {

  private static ArrayList<MortarRecipe> mortarRecipes = new ArrayList<>();
  private static ArrayList<SpellRecipe> spellRecipes = new ArrayList<>();

  public static MortarRecipe getMortarRecipe(List<ItemStack> items) {
    for (int i = 0; i < mortarRecipes.size(); i++) {
      if (mortarRecipes.get(i).matches(items)) {
        return mortarRecipes.get(i);
      }
    }
    return null;
  }

  public static void addMortarRecipe(MortarRecipe recipe) {
    for (int i = 0; i < mortarRecipes.size(); i++) {
      if (mortarRecipes.get(i).matches(recipe.ingredients)) {
        System.out.println("Recipe is already registered with output - " + recipe.getResult().getItem().getUnlocalizedName());
        return;
      }
    }

    mortarRecipes.add(recipe);
  }

  public static SpellRecipe getSpellRecipe(List<ItemStack> items){
    for (int i = 0; i < spellRecipes.size(); i ++){
      if (spellRecipes.get(i).matches(items)){
        return spellRecipes.get(i);
      }
    }
    return null;
  }

  public static SpellRecipe getSpellRecipe(String spell){
    for (int i = 0; i < spellRecipes.size(); i ++){
      if (spellRecipes.get(i).result.compareTo(spell) == 0){
        return spellRecipes.get(i);
      }
    }
    return null;
  }

  public static void addSpellRecipe(SpellRecipe recipe) {
    for (int i = 0; i < spellRecipes.size(); i++) {
      if (spellRecipes.get(i).matches(recipe.ingredients)) {
        System.out.println("A Spell Recipe is already registered");
        return;
      }
    }

    spellRecipes.add(recipe);
  }

}
