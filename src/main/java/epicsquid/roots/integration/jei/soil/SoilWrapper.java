package epicsquid.roots.integration.jei.soil;

import epicsquid.roots.config.ElementalSoilConfig;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import java.util.Arrays;
import java.util.List;

public class SoilWrapper implements IRecipeWrapper {
	
	public SoilRecipe recipe;
	
	public SoilWrapper(SoilRecipe recipe) {
		this.recipe = recipe;
	}
	
	@Override
	public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
		FontRenderer fr = minecraft.fontRenderer;
		switch (recipe.getType()) {
			case WATER:
			case FIRE:
				SoilCategory.liquid.draw(minecraft, 15, 23);
				break;
			case AIR:
				fr.drawStringWithShadow(I18n.format("jei.roots.soil.above"), 79, 0, 16777215);
				fr.drawStringWithShadow("Y" + ElementalSoilConfig.AirSoilMinY, 79, 12, 16777215);
				fr.drawStringWithShadow(I18n.format("jei.roots.soil.delay"), 79, 24, 16777215);
				fr.drawStringWithShadow(String.format("%.01f", ((double) ElementalSoilConfig.AirSoilDelay / 20.0f)) + "s", 79, 36, 16777215);
				SoilCategory.air.draw(minecraft, 15, 23);
				break;
			case EARTH:
				fr.drawStringWithShadow(I18n.format("jei.roots.soil.below"), 79, 0, 16777215);
				fr.drawStringWithShadow("Y" + ElementalSoilConfig.EarthSoilMaxY, 79, 12, 16777215);
				fr.drawStringWithShadow(I18n.format("jei.roots.soil.delay"), 79, 24, 16777215);
				fr.drawStringWithShadow(String.format("%.01f", ((double) ElementalSoilConfig.EarthSoilDelay / 20.0f)) + "s", 79, 36, 16777215);
				SoilCategory.earth.draw(minecraft, 15, 23);
				break;
		}
	}
	
	public static List<FluidStack> FLUIDS = Arrays.asList(new FluidStack(FluidRegistry.LAVA, 1000), new FluidStack(FluidRegistry.WATER, 1000));
	
	@Override
	public void getIngredients(IIngredients ingredients) {
		if (recipe != null) {
			ingredients.setInput(VanillaTypes.ITEM, this.recipe.getSoil());
			ingredients.setOutput(VanillaTypes.ITEM, this.recipe.getOutput());
			ingredients.setInputs(VanillaTypes.FLUID, FLUIDS);
		}
	}
}
