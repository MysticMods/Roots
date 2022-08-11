package epicsquid.roots.integration.jei.carving;

import epicsquid.roots.Roots;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.integration.jei.JEIRootsPlugin;
import epicsquid.roots.util.RitualUtil;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class RunedWoodCategory implements IRecipeCategory<RunedWoodWrapper> {
	
	private final IDrawable background;
	
	public RunedWoodCategory(IGuiHelper helper) {
		this.background = helper.createDrawable(new ResourceLocation(Roots.MODID, "textures/gui/jei/runic_carving.png"), 0, 0, 78, 39);
	}
	
	@Override
	public String getUid() {
		return JEIRootsPlugin.RUNED_WOOD;
	}
	
	@Override
	public String getTitle() {
		return I18n.format("container." + JEIRootsPlugin.RUNED_WOOD + ".name");
	}
	
	@Override
	public String getModName() {
		return Roots.NAME;
	}
	
	@Override
	public IDrawable getBackground() {
		return background;
	}
	
	@Override
	public void setRecipe(IRecipeLayout recipeLayout, RunedWoodWrapper recipeWrapper, IIngredients ingredients) {
		IGuiItemStackGroup group = recipeLayout.getItemStacks();
		RitualUtil.RunedWoodType recipe = recipeWrapper.type;
		group.init(0, true, 0, 19);
		group.set(0, recipe.getVisual());
		group.init(1, false, 60, 19);
		group.set(1, new ItemStack(recipe.getTopper()));
		group.init(2, true, 28, 2);
		group.set(2, new ItemStack(ModItems.wildroot));
	}
}
