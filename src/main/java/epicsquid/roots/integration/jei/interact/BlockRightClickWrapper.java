package epicsquid.roots.integration.jei.interact;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.resources.I18n;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class BlockRightClickWrapper implements IRecipeWrapper {
	
	public final BlockRightClickRecipe recipe;
	
	public BlockRightClickWrapper(BlockRightClickRecipe recipe) {
		this.recipe = recipe;
	}
	
	@Override
	public void getIngredients(IIngredients ingredients) {
		ingredients.setInputLists(VanillaTypes.ITEM, Arrays.asList(Collections.singletonList(recipe.getInput()), recipe.getBlocks()));
		ingredients.setOutputLists(VanillaTypes.ITEM, Collections.singletonList(recipe.getOutputs()));
	}
	
	@Override
	public List<String> getTooltipStrings(int mouseX, int mouseY) {
		// 23,17 - 31, 27
		if (mouseX >= 23 && mouseX <= 31 && mouseY >= 17 && mouseY <= 27) {
			return Collections.singletonList(I18n.format("jei.roots.right_click"));
		}
		if (mouseX >= 58 && mouseX <= 66 && mouseY >= 17 && mouseY <= 27) {
			return Collections.singletonList(I18n.format("jei.roots.water_info"));
		}
		return Collections.emptyList();
	}
}
