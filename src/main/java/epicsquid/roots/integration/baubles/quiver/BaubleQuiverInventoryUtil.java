package epicsquid.roots.integration.baubles.quiver;

import baubles.api.BaubleType;
import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import epicsquid.roots.item.ItemPouch;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class BaubleQuiverInventoryUtil {
  public static ItemStack getQuiver(EntityPlayer player) {
    IBaublesItemHandler handler = BaublesApi.getBaublesHandler(player);
    for (int i : BaubleType.BELT.getValidSlots()) {
      ItemStack stack = handler.getStackInSlot(i);
      if (stack.getItem() instanceof ItemPouch) {
        return stack;
      }
    }
    return ItemStack.EMPTY;
  }
}
