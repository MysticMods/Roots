package epicsquid.roots.integration.crafttweaker.recipes;

import crafttweaker.api.item.IIngredient;
import crafttweaker.api.minecraft.CraftTweakerMC;
import epicsquid.roots.Roots;
import epicsquid.roots.recipe.MortarRecipe;
import epicsquid.roots.tileentity.TileEntityMortar;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;

import java.util.List;
import java.util.stream.Collectors;

public class CTMortarRecipe extends MortarRecipe {
  private List<IIngredient> ingredients;
  private List<Ingredient> convertedIngredients;

  public CTMortarRecipe(String name, ItemStack result, List<IIngredient> ingredients) {
    super(result, new Ingredient[]{});
    this.setRegistryName(new ResourceLocation(Roots.MODID, name));
    this.ingredients = ingredients;
    this.convertedIngredients = ingredients.stream().map(CraftTweakerMC::getIngredient).collect(Collectors.toList());
  }

  public CTMortarRecipe(String name, ItemStack result, List<IIngredient> ingredients, float red1, float green1, float blue1, float red2, float green2, float blue2) {
    super(result, new Ingredient[]{}, red1, green1, blue1, red2, green2, blue2);
    this.setRegistryName(new ResourceLocation(Roots.MODID, name));
    this.ingredients = ingredients;
    this.convertedIngredients = ingredients.stream().map(CraftTweakerMC::getIngredient).collect(Collectors.toList());
  }

  @Override
  public List<Ingredient> getIngredients() {
    return convertedIngredients;
  }

  @Override
  public List<ItemStack> transformIngredients(List<ItemStack> items, TileEntityMortar pyre) {
    return CTTransformer.transformIngredients(ingredients, items, pyre);
  }
}
