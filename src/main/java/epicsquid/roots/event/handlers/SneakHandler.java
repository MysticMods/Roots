package epicsquid.roots.event.handlers;

import epicsquid.roots.Roots;
import epicsquid.roots.handler.QuiverHandler;
import epicsquid.roots.item.QuiverItem;
import epicsquid.roots.util.QuiverInventoryUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(modid = Roots.MODID)
@SuppressWarnings("unused")
public class SneakHandler {
  private static boolean lastSneak = false;

  @SubscribeEvent
  @OnlyIn(Dist.CLIENT)
  public static void onPlayerSneak(TickEvent.ClientTickEvent event) {
    Minecraft mc = Minecraft.getInstance();
    if (mc.player == null) return;

    if (lastSneak != mc.player.isSneaking() && !lastSneak) {
      lastSneak = mc.player.isSneaking();

      List<AbstractArrowEntity> arrows = mc.world.getEntitiesWithinAABB(AbstractArrowEntity.class, QuiverItem.bounding.offset(mc.player.getPosition()));
      if (arrows.isEmpty()) return;

      ItemStack quiver = QuiverInventoryUtil.getQuiver(mc.player);
      if (quiver.isEmpty()) return;

      QuiverHandler handler = QuiverHandler.getHandler(quiver);

      // TODO: When packets are handled

      //MessageServerTryPickupArrows packet = new MessageServerTryPickupArrows();
      //PacketHandler.INSTANCE.sendToServer(packet);
    }

    lastSneak = mc.player.isSneaking();
  }
}
