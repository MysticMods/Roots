package epicsquid.roots.integration.jei.spell;

import epicsquid.roots.spell.SpellBase;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;

import java.awt.*;
import java.util.List;

public class SpellCostWrapper implements IRecipeWrapper {

  public final SpellBase recipe;

  public SpellCostWrapper(SpellBase recipe) {
    this.recipe = recipe;
  }

  @Override
  public void getIngredients(IIngredients ingredients) {
    List<ItemStack> costs = this.recipe.getCostItems();
    costs.add(this.recipe.getResult());
    costs.add(this.recipe.getIcon());
    ingredients.setInputs(VanillaTypes.ITEM, costs);
  }

  @Override
  public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
    int row = 0;
    String spell_name = I18n.format("roots.spell." + recipe.getName() + ".name");
    String name = recipe.getTextColor() + "" + TextFormatting.BOLD + spell_name + TextFormatting.RESET;
    int x = (110 - minecraft.fontRenderer.getStringWidth(spell_name)) / 2;
    minecraft.fontRenderer.drawString(name, x, 3, Color.BLACK.getRGB());

    for (double cost : recipe.getCosts().values()) {
      String c = String.format("x %.4f", cost);
      minecraft.fontRenderer.drawString(c, 82, (row + 1) * 20, Color.BLACK.getRGB());
      row++;
    }
  }
}
