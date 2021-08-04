package epicsquid.roots.integration.jei.interact;

import epicsquid.roots.init.ModBlocks;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.spell.SpellBase;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ImbuingWrapper implements IRecipeWrapper {
  public static List<List<ItemStack>> output = null;

  public final SpellBase recipe;

  public ImbuingWrapper(SpellBase recipe) {
    this.recipe = recipe;
    if (output == null) {
      ItemStack imbuer = new ItemStack(ModBlocks.imbuer);
      ItemStack gramary = new ItemStack(ModItems.gramary);
      output = Arrays.asList(Collections.singletonList(gramary), Collections.singletonList(imbuer));
    }
  }

  @Override
  public void getIngredients(IIngredients ingredients) {
    ingredients.setInput(VanillaTypes.ITEM, recipe.getResult());
    ingredients.setOutputLists(VanillaTypes.ITEM, output);
  }
}
