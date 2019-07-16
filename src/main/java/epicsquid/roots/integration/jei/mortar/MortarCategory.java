package epicsquid.roots.integration.jei.mortar;

import epicsquid.roots.Roots;
import epicsquid.roots.integration.jei.JEIRootsPlugin;
import epicsquid.roots.recipe.MortarRecipe;
import epicsquid.roots.spell.SpellBase;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;

import java.util.Arrays;

public class MortarCategory implements IRecipeCategory<MortarWrapper> {

  private final IDrawable background;

  public MortarCategory(IGuiHelper helper) {
    this.background = helper.createDrawable(new ResourceLocation(Roots.MODID, "textures/gui/jei/mortar_and_pestle.png"), 0, 0, 90, 53);
  }

  @Override
  public String getUid() {
    return JEIRootsPlugin.MORTAR_AND_PESTLE;
  }

  @Override
  public String getTitle() {
    return I18n.format("container." + JEIRootsPlugin.MORTAR_AND_PESTLE + ".name");
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
  public void setRecipe(IRecipeLayout recipeLayout, MortarWrapper recipeWrapper, IIngredients ingredients) {
    IGuiItemStackGroup group = recipeLayout.getItemStacks();
    if (recipeWrapper.recipe != null) {
      MortarRecipe recipe = recipeWrapper.recipe;
      for (int i = 0; i < recipe.getIngredients().size(); i++) {
        group.init(i, true, i * 18, 2);
        Ingredient ing = recipe.getIngredients().get(i);
        if (ing != null) {
          group.set(i, Arrays.asList(ing.getMatchingStacks()));
        }
      }
      group.init(5, false, 72, 27);
      group.set(5, recipe.getResult());
    } else if (recipeWrapper.spellBase != null) {
      SpellBase spell = recipeWrapper.spellBase;
      for (int i = 0; i < spell.getIngredients().size(); i++) {
        group.init(i, true, i * 18, 2);
        group.set(i, Arrays.asList(spell.getIngredients().get(i).getMatchingStacks()));
      }
      group.init(5, false, 72, 27);
      group.set(5, spell.getResult());
    }
  }
}
