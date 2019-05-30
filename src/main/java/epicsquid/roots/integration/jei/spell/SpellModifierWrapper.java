package epicsquid.roots.integration.jei.spell;

import epicsquid.roots.spell.SpellBase;
import epicsquid.roots.spell.modules.SpellModule;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;

import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class SpellModifierWrapper implements IRecipeWrapper {

  public final SpellBase recipe;
  public final List<ItemStack> module_items;

  public SpellModifierWrapper(SpellBase recipe) {
    this.recipe = recipe;
    this.module_items = recipe.getModules().stream().map(SpellModule::getIngredient).collect(Collectors.toList());
  }

  @Override
  public void getIngredients(IIngredients ingredients) {
    ingredients.setOutput(VanillaTypes.ITEM, recipe.getResult());
    ingredients.setInputs(VanillaTypes.ITEM, module_items);
  }

  @Override
  public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
    String spell_name = I18n.format("roots.spell." + recipe.getName() + ".name");
    String name = recipe.getTextColor() + "" + TextFormatting.BOLD + spell_name + TextFormatting.RESET;
    int x = (75 - minecraft.fontRenderer.getStringWidth(spell_name)) / 2;
    minecraft.fontRenderer.drawString(name, x, 3, Color.BLACK.getRGB());
  }

  @Override
  public List<String> getTooltipStrings(int mouseX, int mouseY) {
    return null;
  }
}
