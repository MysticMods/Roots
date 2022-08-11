package epicsquid.roots.integration.jei.ritual;

import epicsquid.roots.recipe.PyreCraftingRecipe;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RitualCraftingWrapper implements IRecipeWrapper {
	
	public final PyreCraftingRecipe recipe;
	
	public RitualCraftingWrapper(PyreCraftingRecipe recipe) {
		this.recipe = recipe;
	}
	
	@Override
	public void getIngredients(IIngredients ingredients) {
		List<Ingredient> ingreds = recipe.getIngredients();
		List<List<ItemStack>> inputs = new ArrayList<>();
		for (Ingredient ingredient : ingreds) {
			inputs.add(Arrays.asList(ingredient.getMatchingStacks()));
		}
		ingredients.setInputLists(VanillaTypes.ITEM, inputs);
		ingredients.setOutput(VanillaTypes.ITEM, recipe.getResult());
	}
}
