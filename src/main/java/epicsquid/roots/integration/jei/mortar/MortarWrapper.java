package epicsquid.roots.integration.jei.mortar;

import java.util.Arrays;

import epicsquid.roots.init.ModItems;
import epicsquid.roots.item.ItemPetalDust;
import epicsquid.roots.recipe.MortarRecipe;
import epicsquid.roots.recipe.RunicShearRecipe;
import epicsquid.roots.recipe.SpellRecipe;
import epicsquid.roots.spell.SpellBase;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

public class MortarWrapper implements IRecipeWrapper {

  public MortarRecipe recipe;
  public SpellBase spellBase;

  public MortarWrapper(MortarRecipe recipe) {
    this.recipe = recipe;
  }

  public MortarWrapper(SpellBase spellBase) {
    this.spellBase = spellBase;
  }

  @Override
  public void getIngredients(IIngredients ingredients) {
    if (recipe != null) {
      for (Ingredient ingredient : recipe.getIngredients()) {
        ingredients.setInputs(VanillaTypes.ITEM, Arrays.asList(ingredient.getMatchingStacks()));
      }
      ingredients.setOutput(VanillaTypes.ITEM, this.recipe.getResult());
    } else if (spellBase != null) {
      for (Ingredient ingredient : spellBase.getIngredients()) {
        ingredients.setInputs(VanillaTypes.ITEM, Arrays.asList(ingredient.getMatchingStacks()));
      }
      ItemStack spellDust = new ItemStack(ModItems.petal_dust);
      ItemPetalDust.createData(spellDust, spellBase);
      ingredients.setOutput(VanillaTypes.ITEM, spellDust);
    }
  }
}
