package epicsquid.roots.util;

import epicsquid.roots.api.Herb;
import epicsquid.roots.item.ItemPouch;
import net.minecraft.entity.player.EntityPlayer;

public class PowderInventoryUtil {

  public static double getPowderCapacityTotal(EntityPlayer player, Herb herb) {
    double amount = 0;
    for (int i = 0; i < 36; i++) {
      if (player.inventory.getStackInSlot(i).getItem() instanceof ItemPouch) {
        if (player.inventory.getStackInSlot(i).hasTagCompound()) {
          if (player.inventory.getStackInSlot(i).getTagCompound().hasKey("plant")) {
            if (player.inventory.getStackInSlot(i).getTagCompound().getString("plant").compareTo(herb.getName()) == 0) {
//              amount += ItemPouch.capacity;
            }
          }
        }
      }
    }
    return amount;
  }

  public static double getPowderTotal(EntityPlayer player, Herb herb) {
    double amount = 0;
    for (int i = 0; i < 36; i++) {
      if (player.inventory.getStackInSlot(i).getItem() instanceof ItemPouch) {
        if (player.inventory.getStackInSlot(i).hasTagCompound()) {
          if (player.inventory.getStackInSlot(i).getTagCompound().hasKey("plant")) {
            if (player.inventory.getStackInSlot(i).getTagCompound().getString("plant").compareTo(herb.getName()) == 0) {
//              amount += ItemPouch.getQuantity(player.inventory.getStackInSlot(i), herb.getName());
            }
          }
        }
      }
    }
    return amount;
  }

  public static void removePowder(EntityPlayer player, Herb herb, double amount) {
    double temp = amount;
    for (int i = 0; i < 36; i++) {
      if (player.inventory.getStackInSlot(i).getItem() instanceof ItemPouch) {
        if (player.inventory.getStackInSlot(i).hasTagCompound()) {
          if (player.inventory.getStackInSlot(i).getTagCompound().hasKey("plant")) {
            if (player.inventory.getStackInSlot(i).getTagCompound().getString("plant").compareTo(herb.getName()) == 0) {
//              double removeAmount = Math.min(128.0, Math.min(ItemPouch.getQuantity(player.inventory.getStackInSlot(i), herb.getName()), temp));
//              ItemPouch.setQuantity(player.inventory.getStackInSlot(i), herb.getName(),
//                  ItemPouch.getQuantity(player.inventory.getStackInSlot(i), herb.getName()) - removeAmount);
//              temp -= removeAmount;
            }
          }
        }
      }
    }
  }
}