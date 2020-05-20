package epicsquid.roots.util;

import epicsquid.roots.api.Herb;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.integration.baubles.pouch.BaublePowderInventoryUtil;
import epicsquid.roots.item.ItemPouch;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

public class ClientHerbUtil {
  @SuppressWarnings("ConstantConditions")
  @SideOnly(Side.CLIENT)
  public static boolean isCreativePouch () {
    Minecraft mc = Minecraft.getMinecraft();
    if (mc == null || mc.player == null) {
      return false;
    }
    for (ItemStack stack : CommonHerbUtil.getPouches(mc.player)) {
      if (stack.getItem() == ModItems.creative_pouch) {
        return true;
      }
    }
    return false;
  }

  public static double getHerbAmount(Herb herb) {
    if (isCreativePouch()) {
      return 999;
    }

    return HerbHud.herbAmount(herb);
  }
}
