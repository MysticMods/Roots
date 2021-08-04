package epicsquid.roots.integration.jei.interact;

import epicsquid.roots.init.ModBlocks;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.modifiers.Modifier;
import epicsquid.roots.spell.SpellBase;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ImposingWrapper implements IRecipeWrapper {
  public static ItemStack imposer = ItemStack.EMPTY;
  public static List<List<ItemStack>> knives = null;
  public final List<ItemStack> modifiers;

  public final SpellBase recipe;

  public ImposingWrapper(SpellBase recipe) {
    this.recipe = recipe;
    if (imposer.isEmpty()) {
      imposer = new ItemStack(ModBlocks.imposer);
    }
    if (knives == null) {
      knives = Collections.singletonList(ModItems.knives.stream().map(ItemStack::new).collect(Collectors.toList()));
    }
    modifiers = recipe.getModifiers().stream().map(Modifier::getModifierStack).collect(Collectors.toList());
  }

  @Override
  public void getIngredients(IIngredients ingredients) {
    ingredients.setInputLists(VanillaTypes.ITEM, knives);
    ingredients.setOutputLists(VanillaTypes.ITEM, Arrays.asList(Collections.singletonList(recipe.getStaff()), Collections.singletonList(imposer), modifiers));
  }

  @Override
  public List<String> getTooltipStrings(int mouseX, int mouseY) {
    if (mouseX >= 21 && mouseX <= 34 && mouseY >= 29 && mouseY <= 42) {
      return Collections.singletonList(I18n.format("jei.roots.right_click"));
    }
    return Collections.emptyList();
  }
}
