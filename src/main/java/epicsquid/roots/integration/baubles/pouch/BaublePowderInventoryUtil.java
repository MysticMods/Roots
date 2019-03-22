package epicsquid.roots.integration.baubles.pouch;

import baubles.api.BaubleType;
import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import epicsquid.roots.api.Herb;
import epicsquid.roots.item.ItemPouch;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class BaublePowderInventoryUtil {
  public static double getPowderTotal (EntityPlayer player, Herb herb) {
    IBaublesItemHandler handler = BaublesApi.getBaublesHandler(player);
    for (int i : BaubleType.BELT.getValidSlots()) {
      ItemStack stack = handler.getStackInSlot(i);
      if (stack.getItem() instanceof ItemPouch) {
        return ItemPouch.getHerbQuantity(stack, herb);
      }
    }
    return 0.0;
  }

  public static void removePowder(EntityPlayer player, Herb herb, double amount) {
    IBaublesItemHandler handler = BaublesApi.getBaublesHandler(player);
    for (int i : BaubleType.BELT.getValidSlots()) {
      ItemStack stack = handler.getStackInSlot(i);
      if (stack.getItem() instanceof ItemPouch) {
        ItemPouch.useQuantity(stack, herb, amount);
      }
    }
  }
}
