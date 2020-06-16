package epicsquid.roots.util;

import epicsquid.roots.init.ModItems;
import net.minecraft.item.ItemStack;

public class SpellUtil {
  public static boolean isValidStaff (ItemStack stack) {
    return stack.getItem() == ModItems.staff || stack.getItem() == ModItems.spell_dust;
  }

  public static boolean isValidDust (ItemStack stack) {
    return stack.getItem() == ModItems.spell_dust;
  }
}
