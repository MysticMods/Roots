package epicsquid.mysticallib.recipe.factories;

import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.CompoundIngredient;

import java.util.Arrays;

public class AccessibleCompoundIngredient extends CompoundIngredient {
  public AccessibleCompoundIngredient(Ingredient... children) {
    super(Arrays.asList(children));
  }
}
