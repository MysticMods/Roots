package epicsquid.roots.util;

import epicsquid.roots.api.Herb;
import epicsquid.roots.integration.baubles.pouch.BaublePowderInventoryUtil;
import epicsquid.roots.item.ItemPouch;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;

public class PowderInventoryUtil {

  public static double getPowderTotal(EntityPlayer player, Herb herb) {
    for (int i = 0; i < 36; i++) {
      if (player.inventory.getStackInSlot(i).getItem() instanceof ItemPouch) {
        return ItemPouch.getHerbQuantity(player.inventory.getStackInSlot(i), herb);
      }
    }
    if (Loader.isModLoaded("baubles")) {
      return BaublePowderInventoryUtil.getPowderTotal(player, herb);
    }
    return 0.0;
  }

  public static void removePowder(EntityPlayer player, Herb herb, double amount) {
    for (int i = 0; i < 36; i++) {
      if (player.inventory.getStackInSlot(i).getItem() instanceof ItemPouch) {
        ItemPouch.useQuantity(player.inventory.getStackInSlot(i), herb, amount);
      }
    }
    if (Loader.isModLoaded("baubles")) {
      BaublePowderInventoryUtil.removePowder(player, herb, amount);
    }
  }
}