package epicsquid.roots.integration.crafttweaker;

import crafttweaker.api.item.IIngredient;
import crafttweaker.api.minecraft.CraftTweakerMC;
import epicsquid.roots.util.IngredientWithStack;
import net.minecraft.item.crafting.Ingredient;

public class CTUtil {
  public static IngredientWithStack ingredientWithStack(IIngredient input) {
    Ingredient ingredient = CraftTweakerMC.getIngredient(input);
    return new IngredientWithStack(ingredient, input.getAmount());
  }
}
