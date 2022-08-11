package epicsquid.roots.integration.crafttweaker.recipes;

import crafttweaker.api.item.IIngredient;
import crafttweaker.api.minecraft.CraftTweakerMC;
import epicsquid.roots.recipe.SummonCreatureRecipe;
import epicsquid.roots.tileentity.TileEntityPyre;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;

import java.util.List;
import java.util.stream.Collectors;

public class CTSummonCreaturesRecipe extends SummonCreatureRecipe {
	private List<IIngredient> ingredients;
	private List<Ingredient> convertedIngredients;
	
	public CTSummonCreaturesRecipe(ResourceLocation resource, Class<? extends EntityLivingBase> clazz, List<IIngredient> ingredients) {
		super(resource, clazz);
		this.ingredients = ingredients;
		this.convertedIngredients = ingredients.stream().map(CraftTweakerMC::getIngredient).collect(Collectors.toList());
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
