package epicsquid.roots.integration.jei.transmutation;

import epicsquid.roots.Roots;
import epicsquid.roots.integration.jei.JEIRootsPlugin;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

public class TransmutationCategory implements IRecipeCategory<TransmutationWrapper> {

  private final IDrawable background;

  public TransmutationCategory(IGuiHelper helper) {
    this.background = helper.createDrawable(new ResourceLocation(Roots.MODID, "textures/gui/jei/transmutation.png"), 0, 0, 166, 84);
  }

  @Override
  public String getUid() {
    return JEIRootsPlugin.TRANSMUTATION;
  }

  @Override
  public String getTitle() {
    return I18n.format("container." + JEIRootsPlugin.TRANSMUTATION + ".name");
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
  public void setRecipe(IRecipeLayout recipeLayout, TransmutationWrapper recipeWrapper, IIngredients ingredients) {
  }
}
