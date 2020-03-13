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
}
