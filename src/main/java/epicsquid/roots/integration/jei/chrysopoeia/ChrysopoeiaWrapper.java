package epicsquid.roots.integration.jei.chrysopoeia;

import epicsquid.roots.recipe.ChrysopoeiaRecipe;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.ItemStack;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ChrysopoeiaWrapper implements IRecipeWrapper {
	
	public ChrysopoeiaRecipe recipe;
	
	public ChrysopoeiaWrapper(ChrysopoeiaRecipe recipe) {
		this.recipe = recipe;
	}
	
	@Override
	public void getIngredients(IIngredients ingredients) {
		List<ItemStack> inputs = Stream.of(recipe.getIngredient().getIngredient().getMatchingStacks()).map(ItemStack::copy).peek(o -> o.setCount(recipe.getIngredient().getCount())).collect(Collectors.toList());
		ingredients.setInputs(VanillaTypes.ITEM, inputs);
		ingredients.setOutput(VanillaTypes.ITEM, recipe.getOutput());
	}
}
