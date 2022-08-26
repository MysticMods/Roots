package epicsquid.roots.client;

import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.roots.Roots;
import epicsquid.roots.network.MessageServerOpenLibrary;
import epicsquid.roots.network.MessageServerOpenPouch;
import epicsquid.roots.network.MessageServerOpenQuiver;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber(modid = Roots.MODID)
public class Keybinds {
  private static final String ROOTS_BASE = "roots.keybinds.";
  private static final String ROOTS_GROUP = ROOTS_BASE + "group";

  public static KeyBinding POUCH_KEYBIND = null;
  public static KeyBinding QUIVER_KEYBIND = null;

  public static KeyBinding OPEN_SPELL_LIBRARY = null;
  public static KeyBinding CYCLE_PROFILE = null;

  public static void init() {
    POUCH_KEYBIND = new KeyBinding(ROOTS_BASE + "pouch", 0, ROOTS_GROUP);
    QUIVER_KEYBIND = new KeyBinding(ROOTS_BASE + "quiver", 0, ROOTS_GROUP);
    OPEN_SPELL_LIBRARY = new KeyBinding(ROOTS_BASE + "spell_library", 0, ROOTS_GROUP);
    CYCLE_PROFILE = new KeyBinding(ROOTS_BASE + "cycle", 0, ROOTS_GROUP);

    ClientRegistry.registerKeyBinding(POUCH_KEYBIND);
    ClientRegistry.registerKeyBinding(QUIVER_KEYBIND);
    ClientRegistry.registerKeyBinding(OPEN_SPELL_LIBRARY);
    ClientRegistry.registerKeyBinding(CYCLE_PROFILE);
  }

  @SubscribeEvent
  @SideOnly(Side.CLIENT)
  public static void onInput(InputEvent event) {
    Minecraft mc = Minecraft.getMinecraft();
    if (POUCH_KEYBIND.isKeyDown() && mc.inGameHasFocus) {
      MessageServerOpenPouch packet = new MessageServerOpenPouch();
      PacketHandler.INSTANCE.sendToServer(packet);
    } else if (QUIVER_KEYBIND.isKeyDown() && mc.inGameHasFocus) {
      MessageServerOpenQuiver packet = new MessageServerOpenQuiver();
      PacketHandler.INSTANCE.sendToServer(packet);
    } else if (OPEN_SPELL_LIBRARY.isKeyDown() && mc.inGameHasFocus) {
      MessageServerOpenLibrary packet = new MessageServerOpenLibrary();
      PacketHandler.INSTANCE.sendToServer(packet);
    } else if (CYCLE_PROFILE.isKeyDown() && mc.inGameHasFocus) {

    }
  }

}
