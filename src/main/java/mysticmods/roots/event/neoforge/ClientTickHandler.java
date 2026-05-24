package mysticmods.roots.event.neoforge;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.client.gui.screen.fake.SpellModifierScreen;
import mysticmods.roots.client.gui.screen.fake.StaffScreen;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;

@EventBusSubscriber(modid = RootsAPI.MODID, value = Dist.CLIENT)
public class ClientTickHandler {
  private static int lastTimesChanged = -1;

  @SubscribeEvent
  public static void onClientTick(ClientTickEvent.Post event) {
    Minecraft mc = Minecraft.getInstance();
    if (mc == null || mc.player == null) {
      return;
    }

    var newTimesChanged = mc.player.getInventory().getTimesChanged();
    if (lastTimesChanged != -1 && newTimesChanged != lastTimesChanged) {
      playerInventoryChanged(mc, event);
    }

    lastTimesChanged = newTimesChanged;
  }

  public static void playerInventoryChanged(Minecraft mc, ClientTickEvent.Post event) {
    // Ordering is important: test all sub screens before the top screen
    if (mc.screen instanceof SpellModifierScreen spellModifierScreen) {
      spellModifierScreen.validate();
    }
    if (mc.screen instanceof StaffScreen staffScreen) {
      staffScreen.validate();
    }
  }
}
