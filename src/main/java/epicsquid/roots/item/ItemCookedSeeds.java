package epicsquid.roots.item;

import epicsquid.mysticallib.item.ItemFoodBase;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class ItemCookedSeeds extends ItemFoodBase {
  public ItemCookedSeeds(@Nonnull String name, int amount, float saturation, boolean isWolfFood) {
    super(name, amount, saturation, isWolfFood);
  }

  public ItemCookedSeeds(@Nonnull String name, int amount, boolean isWolfFood) {
    super(name, amount, isWolfFood);
  }

  @Override
  public int getMaxItemUseDuration(ItemStack stack) {
    return 5;
  }
}
