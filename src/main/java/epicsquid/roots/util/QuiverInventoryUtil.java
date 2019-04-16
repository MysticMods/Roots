package epicsquid.roots.util;

import epicsquid.roots.Roots;
import epicsquid.roots.api.Herb;
import epicsquid.roots.integration.baubles.pouch.BaublePowderInventoryUtil;
import epicsquid.roots.integration.baubles.quiver.BaubleQuiverInventoryUtil;
import epicsquid.roots.item.ItemPouch;
import epicsquid.roots.item.ItemQuiver;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashMap;
import java.util.Map;

@Mod.EventBusSubscriber(modid= Roots.MODID)
public class QuiverInventoryUtil {
  public static ItemStack getQuiver (EntityPlayer player) {
    for (int i = 0; i < 36; i++) {
      if (player.inventory.getStackInSlot(i).getItem() instanceof ItemQuiver) {
        return player.inventory.getStackInSlot(i);
      }
    }
    if (Loader.isModLoaded("baubles")) {
      return BaubleQuiverInventoryUtil.getQuiver(player);
    }

    return ItemStack.EMPTY;
  }
}