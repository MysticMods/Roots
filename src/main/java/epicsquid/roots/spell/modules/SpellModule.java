package epicsquid.roots.spell.modules;

import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;

public class SpellModule {

  private ItemStack ingredient;
  private String name;
  private TextFormatting colour;

  public SpellModule(String name, ItemStack ingredient, TextFormatting colour) {
    this.name = name;
    this.ingredient = ingredient;
    this.colour = colour;
  }

  public String getName() {
    return name;
  }

  public ItemStack getIngredient() {
    return ingredient;
  }

  public TextFormatting getFormat() {
    return colour;
  }
}
