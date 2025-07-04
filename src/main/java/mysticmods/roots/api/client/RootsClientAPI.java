package mysticmods.roots.api.client;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.item.GramaryItem;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;

public class RootsClientAPI {
  public static ItemStack getGramary () {
    Minecraft mc = Minecraft.getInstance();
    if (mc.player == null) {
      return ItemStack.EMPTY;
    }

    return RootsAPI.getInstance().getTome(mc.player);
  }

  public static GramaryItem.GramaryMode getGramaryMode () {
    Minecraft mc = Minecraft.getInstance();
    if (mc.player == null) {
      return GramaryItem.GramaryMode.NONE;
    }

    return RootsAPI.getInstance().getTomeMode(mc.player);
  }

  public static boolean isGramaryMode (GramaryItem.GramaryMode mode) {
    return getGramaryMode() == mode;
  }
}
