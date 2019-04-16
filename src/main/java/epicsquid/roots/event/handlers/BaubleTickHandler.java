package epicsquid.roots.event.handlers;

import epicsquid.mysticallib.util.Util;
import epicsquid.roots.Roots;
import epicsquid.roots.integration.baubles.quiver.BaubleQuiverInventoryUtil;
import epicsquid.roots.item.ItemQuiver;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

@Mod.EventBusSubscriber(modid= Roots.MODID)
@SuppressWarnings("unused")
public class BaubleTickHandler {
  @SubscribeEvent
  @Optional.Method(modid="baubles")
  public static void onBaubleTick (TickEvent.PlayerTickEvent event) {
    ItemStack quiver = BaubleQuiverInventoryUtil.getQuiver(event.player);
    if (quiver.isEmpty() || quiver.getItemDamage() == 0) return;

    if (Util.rand.nextInt(ItemQuiver.repairChance) == 0) {
      quiver.setItemDamage(quiver.getItemDamage()-1);
    }
  }
}
