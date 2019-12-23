package epicsquid.roots.integration.jei.summon;

import epicsquid.roots.Roots;
import epicsquid.roots.integration.jei.JEIRootsPlugin;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.List;

public class SummonCreaturesCategory implements IRecipeCategory<SummonCreaturesWrapper> {

  private final IDrawable background;

  public SummonCreaturesCategory(IGuiHelper helper) {
    this.background = helper.createDrawable(new ResourceLocation(Roots.MODID, "textures/gui/jei/summon_creatures.png"), 0, 0, 169, 84);
  }

  @Override
  public String getUid() {
    return JEIRootsPlugin.SUMMON_CREATURES;
  }

  @Override
  public String getTitle() {
    return I18n.format("container." + JEIRootsPlugin.SUMMON_CREATURES + ".name");
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
  public void setRecipe(IRecipeLayout recipeLayout, SummonCreaturesWrapper recipeWrapper, IIngredients ingredients) {
    IGuiItemStackGroup group = recipeLayout.getItemStacks();
    List<List<ItemStack>> data = ingredients.getInputs(VanillaTypes.ITEM);
    group.init(0, true, 3, 3);
    if (data.size() >= 1) {
      group.set(0, data.get(0));
    }
    group.init(1, true, 24, 3);
    if (data.size() >= 2) {
      group.set(1, data.get(1));
    }
    group.init(2, true, 45, 3);
    if (data.size() >= 3) {
      group.set(2, data.get(2));
    }
    group.init(3, true, 66, 3);
    if (data.size() >= 4) {
      group.set(3, data.get(3));
    }
    group.init(4, true, 87, 3);
    if (data.size() >= 5) {
      group.set(4, data.get(4));
    }
  }
}
