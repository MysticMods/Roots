package epicsquid.roots.integration.jei.spell;

import epicsquid.roots.Roots;
import epicsquid.roots.integration.jei.JEIRootsPlugin;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class SpellModifierCategory implements IRecipeCategory<SpellModifierWrapper> {

  private final IDrawable background;

  public SpellModifierCategory(IGuiHelper helper) {
    this.background = helper.createDrawable(new ResourceLocation(Roots.MODID, "textures/gui/jei/spell_modifiers.png"), 0, 0, 90, 91);
  }

  @Override
  public String getUid() {
    return JEIRootsPlugin.SPELL_MODIFIERS;
  }

  @Override
  public String getTitle() {
    return I18n.format("container." + JEIRootsPlugin.SPELL_MODIFIERS + ".name");
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
  public void setRecipe(IRecipeLayout recipeLayout, SpellModifierWrapper recipeWrapper, IIngredients ingredients) {
    IGuiItemStackGroup group = recipeLayout.getItemStacks();
    group.init(0, true, 36, 17);
    group.init(1, true, 10, 40);
    group.init(2, true, 62, 40);
    group.init(3, true, 17, 71);
    group.init(4, true, 57, 71);
    group.init(5, true, 109, 40);
    group.init(6, false, 36, 45);
    group.set(6, recipeWrapper.recipe.getResult());

    int count = 0;
    for (ItemStack stack : recipeWrapper.module_items) {
      group.set(count, stack);
      count++;
    }

    for (int i = count; i < 5; i++) {
      group.set(i, ItemStack.EMPTY);
    }
  }
}
