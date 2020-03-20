package epicsquid.roots.integration.crafttweaker.recipes;

import crafttweaker.api.item.IIngredient;
import crafttweaker.api.minecraft.CraftTweakerMC;
import epicsquid.roots.spell.SpellBase;
import epicsquid.roots.tileentity.TileEntityMortar;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

import java.util.List;
import java.util.stream.Collectors;

public class CTSpellRecipe extends SpellBase.SpellRecipe {
  private List<IIngredient> ingredients;
  private List<Ingredient> convertedIngredients;

  public CTSpellRecipe(SpellBase result, List<IIngredient> ingredients) {
    super();
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
