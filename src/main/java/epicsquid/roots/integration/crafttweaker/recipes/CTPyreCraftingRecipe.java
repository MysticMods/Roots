package epicsquid.roots.integration.crafttweaker.recipes;

import crafttweaker.api.item.IIngredient;
import crafttweaker.api.minecraft.CraftTweakerMC;
import epicsquid.roots.recipe.PyreCraftingRecipe;
import epicsquid.roots.tileentity.TileEntityPyre;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

import java.util.List;
import java.util.stream.Collectors;

public class CTPyreCraftingRecipe extends PyreCraftingRecipe {
  private List<IIngredient> ingredients;
  private List<Ingredient> convertedIngredients;

  public CTPyreCraftingRecipe(ItemStack result, List<IIngredient> ingredients, int xp) {
    super(result, xp);
    this.ingredients = ingredients;
    this.convertedIngredients = this.ingredients.stream().map(CraftTweakerMC::getIngredient).collect(Collectors.toList());
  }

  @Override
  public List<Ingredient> getIngredients() {
    return convertedIngredients;
  }

  @Override
  public List<ItemStack> transformIngredients(List<ItemStack> items, TileEntityPyre pyre) {
    return CTTransformer.transformIngredients(ingredients, items, pyre);
  }

}
