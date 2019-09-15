package epicsquid.roots.spell.modules;

import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;

import java.util.function.Supplier;

public class SpellModule {

  private Supplier<ItemStack> ingredient;
  private String name;
  private TextFormatting colour;

  public SpellModule(String name, Supplier<ItemStack> ingredient, TextFormatting colour) {
    this.name = name;
    this.ingredient = ingredient;
    this.colour = colour;
  }

  public String getName() {
    return name;
  }

  public ItemStack getIngredient() {
    return ingredient.get();
  }

  public TextFormatting getFormat() {
    return colour;
  }
}
