package epicsquid.roots.integration.jei.spell;

import epicsquid.roots.Roots;
import epicsquid.roots.integration.jei.JEIRootsPlugin;
import epicsquid.roots.spell.SpellBase;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.List;

public class SpellCostCategory implements IRecipeCategory<SpellCostWrapper> {
	
	private final IDrawable background;
	
	public SpellCostCategory(IGuiHelper helper) {
		this.background = helper.createDrawable(new ResourceLocation(Roots.MODID, "textures/gui/jei/spell_costs.png"), 0, 0, 125, 73);
	}
	
	@Override
	public String getUid() {
		return JEIRootsPlugin.SPELL_COSTS;
	}
	
	@Override
	public String getTitle() {
		return I18n.format("container." + JEIRootsPlugin.SPELL_COSTS + ".name");
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
	public void setRecipe(IRecipeLayout recipeLayout, SpellCostWrapper recipeWrapper, IIngredients ingredients) {
		IGuiItemStackGroup group = recipeLayout.getItemStacks();
		SpellBase recipe = recipeWrapper.recipe;
		List<ItemStack> costs = recipe.getCostItems();
		group.init(0, true, 61, 15);
		group.set(0, costs.get(0));
		if (costs.size() >= 2) {
			group.init(1, true, 61, 35);
			group.set(1, costs.get(1));
		}
		if (costs.size() == 3) {
			group.init(2, true, 61, 55);
			group.set(2, costs.get(2));
		}
		group.init(3, false, 0, 35);
		group.set(3, recipe.getIcon());
	}
}
