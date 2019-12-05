package epicsquid.roots.event.handlers;

import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.roots.Roots;
import epicsquid.roots.handler.QuiverHandler;
import epicsquid.roots.item.ItemQuiver;
import epicsquid.roots.network.MessageServerTryPickupArrows;
import epicsquid.roots.util.QuiverInventoryUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

@Mod.EventBusSubscriber(modid = Roots.MODID)
@SuppressWarnings("unused")
public class SneakHandler {
  private static boolean lastSneak = false;

  @SubscribeEvent
  @SideOnly(Side.CLIENT)
  public static void onPlayerSneak(TickEvent.ClientTickEvent event) {
    Minecraft mc = Minecraft.getMinecraft();
    if (mc.player == null) return;

    if (lastSneak != mc.player.isSneaking() && !lastSneak) {
      lastSneak = mc.player.isSneaking();

      List<AbstractArrowEntity> arrows = mc.world.getEntitiesWithinAABB(AbstractArrowEntity.class, ItemQuiver.bounding.offset(mc.player.getPosition()));
      if (arrows.isEmpty()) return;

      ItemStack quiver = QuiverInventoryUtil.getQuiver(mc.player);
      if (quiver.isEmpty()) return;

      QuiverHandler handler = QuiverHandler.getHandler(quiver);

      MessageServerTryPickupArrows packet = new MessageServerTryPickupArrows();
      PacketHandler.INSTANCE.sendToServer(packet);
    }

    lastSneak = mc.player.isSneaking();
  }
}
