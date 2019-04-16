package epicsquid.roots.integration.baubles.quiver;

import baubles.api.BaubleType;
import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import epicsquid.roots.item.ItemPouch;
import epicsquid.roots.item.ItemQuiver;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class BaubleQuiverInventoryUtil {
  public static ItemStack getQuiver (EntityPlayer player) {
    IBaublesItemHandler handler = BaublesApi.getBaublesHandler(player);
    for (int i : BaubleType.BODY.getValidSlots()) {
      ItemStack stack = handler.getStackInSlot(i);
      if (stack.getItem() instanceof ItemQuiver) {
        return stack;
      }
    }
    return ItemStack.EMPTY;
  }
}
