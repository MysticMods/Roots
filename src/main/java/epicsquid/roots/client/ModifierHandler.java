package epicsquid.roots.client;

import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.roots.Roots;
import epicsquid.roots.container.IModifierContainer;
import epicsquid.roots.network.MessageServerModifier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

@Mod.EventBusSubscriber(modid = Roots.MODID)
public class ModifierHandler {
  private static boolean ctrlDown = false;
  private static boolean shiftDown = false;
  private static boolean altDown = false;
  private static boolean update = false;

  @SuppressWarnings("ConstantConditions")
  @SubscribeEvent
  public static void onClientTick(TickEvent.ClientTickEvent event) {
    if (event.phase == TickEvent.Phase.END) {
      Minecraft mc = Minecraft.getMinecraft();
      if (mc == null || mc.player == null) {
        return;
      }

      if (!(mc.player.openContainer instanceof IModifierContainer)) {
        ctrlDown = false;
        shiftDown = false;
        altDown = false;
        update = false;
      } else {
        if (ctrlDown && !Screen.isCtrlKeyDown() || !ctrlDown && Screen.isCtrlKeyDown()) {
          ctrlDown = Screen.isCtrlKeyDown();
          update = true;
        }
        if (shiftDown && !Screen.isShiftKeyDown() || !shiftDown && Screen.isShiftKeyDown()) {
          shiftDown = Screen.isShiftKeyDown();
          update = true;
        }
        if (altDown && !Screen.isAltKeyDown() || !altDown && Screen.isAltKeyDown()) {
          altDown = Screen.isAltKeyDown();
          update = true;
        }
        if (update/* || mc.world.getWorldTime() % 20 == 0*/) { // TODO: Is this bit needed?
          update = false;
          MessageServerModifier message = new MessageServerModifier(shiftDown, ctrlDown, altDown);
          PacketHandler.INSTANCE.sendToServer(message);
          ((IModifierContainer) mc.player.openContainer).setModifierStatus(0, shiftDown);
          ((IModifierContainer) mc.player.openContainer).setModifierStatus(1, ctrlDown);
          ((IModifierContainer) mc.player.openContainer).setModifierStatus(2, altDown);
        }
      }
    }
  }
}
