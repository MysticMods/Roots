package epicsquid.roots.integration.jei.summon;

import epicsquid.roots.integration.jei.shears.RunicShearsEntityWrapper;
import epicsquid.roots.recipe.SummonCreatureIntermediate;
import epicsquid.roots.recipe.SummonCreatureRecipe;
import epicsquid.roots.util.RenderUtil;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SummonCreaturesWrapper implements IRecipeWrapper {
	public EntityLivingBase entity = null;
	public final SummonCreatureRecipe recipe;
	
	public SummonCreaturesWrapper(SummonCreatureRecipe recipe) {
		this.recipe = recipe;
	}
	
	public SummonCreaturesWrapper(SummonCreatureIntermediate recipe) {
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
	}
	
	@Override
	public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
		if (entity == null) {
			entity = recipe.getEntity(minecraft.world);
		}
		float scale = RunicShearsEntityWrapper.getScale(entity);
		RenderUtil.drawEntityOnScreen(137, 70, scale, 38 - mouseX, 70 - mouseY, entity);
	}
}
