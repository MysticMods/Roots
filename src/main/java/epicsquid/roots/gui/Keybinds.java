package epicsquid.roots.gui;

import epicsquid.roots.Roots;
import epicsquid.roots.util.PowderInventoryUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber(modid= Roots.MODID)
public class Keybinds {
  public static final String ROOTS_BASE = "roots.keybinds.";
  public static final String ROOTS_GROUP = ROOTS_BASE + "group";

  public static KeyBinding POUCH_KEYBIND = null;
  public static KeyBinding QUIVER_KEYBIND = null;

  public static KeyBinding SPELL_SLOT_1 = null;
  public static KeyBinding SPELL_SLOT_2 = null;
  public static KeyBinding SPELL_SLOT_3 = null;
  public static KeyBinding SPELL_SLOT_4 = null;
  public static KeyBinding SPELL_SLOT_5 = null;
  public static KeyBinding SPELL_SLOT_NEXT = null;
  public static KeyBinding SPELL_SLOT_PREVIOUS = null;

  public static void init () {
    POUCH_KEYBIND = new KeyBinding(ROOTS_BASE + "pouch", 0, ROOTS_GROUP);
    QUIVER_KEYBIND = new KeyBinding(ROOTS_BASE + "quiver", 0, ROOTS_GROUP);

    ClientRegistry.registerKeyBinding(POUCH_KEYBIND);
    ClientRegistry.registerKeyBinding(QUIVER_KEYBIND);
  }

  @SubscribeEvent
  @SideOnly(Side.CLIENT)
  public static void onKeyInput (InputEvent.KeyInputEvent event) {
    Minecraft mc = Minecraft.getMinecraft();
    if (POUCH_KEYBIND.isKeyDown() && mc.inGameHasFocus) {
      ItemStack pouch = PowderInventoryUtil.getPouch(mc.player);
      if (!pouch.isEmpty()) {
        pouch.getItem().onItemRightClick(mc.world, mc.player, EnumHand.MAIN_HAND);
      }
    } else if (QUIVER_KEYBIND.isKeyDown() && mc.inGameHasFocus) {
      // Handle the quiver here when implemented
    }
  }

}
