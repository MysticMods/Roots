package epicsquid.roots.integration.crafttweaker.recipes;

import crafttweaker.api.item.IIngredient;
import crafttweaker.api.minecraft.CraftTweakerMC;
import epicsquid.roots.recipe.FeyCraftingRecipe;
import epicsquid.roots.tileentity.TileEntityFeyCrafter;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

import java.util.List;
import java.util.stream.Collectors;

public class CTFeyCraftingRecipe extends FeyCraftingRecipe {
	private List<IIngredient> ingredients;
	private List<Ingredient> convertedIngredients;
	
	public CTFeyCraftingRecipe(ItemStack result, List<IIngredient> ingredients, int xp) {
		super(result, xp);
		this.ingredients = ingredients;
		this.convertedIngredients = ingredients.stream().map(CraftTweakerMC::getIngredient).collect(Collectors.toList());
	}
	
	@Override
	public List<Ingredient> getIngredients() {
		return convertedIngredients;
	}
	
	@Override
	public List<ItemStack> transformIngredients(List<ItemStack> items, TileEntityFeyCrafter pyre) {
		return CTTransformer.transformIngredients(ingredients, items, pyre);
	}
}
