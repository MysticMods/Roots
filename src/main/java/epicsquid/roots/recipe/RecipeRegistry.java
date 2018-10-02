package epicsquid.roots.recipe;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;

public class RecipeRegistry {

  public static ArrayList<MortarRecipe> mortarRecipes = new ArrayList<MortarRecipe>();

  public static MortarRecipe getMortarRecipe(List<ItemStack> items){
    System.out.println(mortarRecipes.size());
    for (int i = 0; i < mortarRecipes.size(); i ++){
      if (mortarRecipes.get(i).matches(items)){
        return mortarRecipes.get(i);
      }
    }
    return null;
  }

  public static void addMortarRecipe(MortarRecipe recipe){
    for (int i = 0; i < mortarRecipes.size(); i ++){
      if (mortarRecipes.get(i).matches(recipe.ingredients)){
        System.out.println("Recipe is already registered with output - " + recipe.getResult().getItem().getUnlocalizedName());
        return;
      }
    }

    mortarRecipes.add(recipe);
  }

}
