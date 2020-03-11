package epicsquid.roots.util;

import epicsquid.roots.api.Herb;
import epicsquid.roots.integration.baubles.pouch.BaublePowderInventoryUtil;
import epicsquid.roots.item.ItemPouch;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;

import java.util.ArrayList;
import java.util.List;

public class ClientHerbUtil {
  public static double herbAmount(Herb herb) {
    return HerbHud.herbAmount(herb);
  }

  public static ItemStack getFirstPouch(EntityPlayer player) {
    List<ItemStack> pouches = getPouches(player);
    if (pouches.isEmpty()) {
      return ItemStack.EMPTY;
    }

    return pouches.get(0);
  }

  public static List<ItemStack> getPouches(EntityPlayer player) {
    List<ItemStack> result = new ArrayList<>();
    if (Loader.isModLoaded("baubles")) {
      ItemStack stack = BaublePowderInventoryUtil.getPouch(player);
      if (!stack.isEmpty()) {
        result.add(stack);
      }
    }

    for (int i = 0; i < 36; i++) {
      if (player.inventory.getStackInSlot(i).getItem() instanceof ItemPouch) {
        result.add(player.inventory.getStackInSlot(i));
      }
    }

    return result;
  }
}
