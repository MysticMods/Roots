package epicsquid.roots.integration.jei.loot;

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

import java.util.ArrayList;
import java.util.List;

public class LootCategory implements IRecipeCategory<LootWrapper> {

  private final IDrawable background;

  public LootCategory(IGuiHelper helper) {
    this.background = helper.createDrawable(new ResourceLocation(Roots.MODID, "textures/gui/jei/loot.png"), 0, 0, 128, 132);
  }

  @Override
  public String getUid() {
    return JEIRootsPlugin.LOOT;
  }

  @Override
  public String getTitle() {
    return I18n.format("container." + JEIRootsPlugin.LOOT + ".name");
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
  public void setRecipe(IRecipeLayout recipeLayout, LootWrapper wrapper, IIngredients ingredients) {
    recipeLayout.getItemStacks().addTooltipCallback(wrapper);
    IGuiItemStackGroup group = recipeLayout.getItemStacks();
    group.init(0, true, 55, 4);
    group.set(0, wrapper.getType());

    List<List<ItemStack>> drops = new ArrayList<>();
    for (int i = 0; i < 35; i++) {
      drops.add(new ArrayList<>());
    }
    List<ItemStack> totalDrops = wrapper.getStacks();
    for (int i = 0; i < totalDrops.size(); i++) {
      drops.get(i % 35).add(totalDrops.get(i));
    }
    drops.removeIf(List::isEmpty);

    for (int i = 0; i < drops.size(); i++) {
      int x = 1 + 18 * (i % 7);
      int y = 40 + 18 * (i / 7);
      group.init(i + 1, false, x, y);
      group.set(i + 1, drops.get(i));
    }
  }
}
