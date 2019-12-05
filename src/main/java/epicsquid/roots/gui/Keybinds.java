package epicsquid.roots.gui;

import epicsquid.roots.Roots;
import epicsquid.roots.init.ModItems;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;

import java.util.Map;

@Mod.EventBusSubscriber(modid = Roots.MODID)
public class Keybinds {
  private static final String ROOTS_BASE = "roots.keybinds.";
  private static final String ROOTS_GROUP = ROOTS_BASE + "group";

  public static KeyBinding POUCH_KEYBIND = null;
  public static KeyBinding QUIVER_KEYBIND = null;

  private static KeyBinding SPELL_SLOT_1 = null;
  private static KeyBinding SPELL_SLOT_2 = null;
  private static KeyBinding SPELL_SLOT_3 = null;
  private static KeyBinding SPELL_SLOT_4 = null;
  private static KeyBinding SPELL_SLOT_5 = null;
  private static KeyBinding SPELL_SLOT_NEXT = null;
  private static KeyBinding SPELL_SLOT_PREVIOUS = null;

  private static Object2IntOpenHashMap<KeyBinding> SLOT_MAP = new Object2IntOpenHashMap<>();

  public static void init() {
    POUCH_KEYBIND = new KeyBinding(ROOTS_BASE + "pouch", 0, ROOTS_GROUP);
    QUIVER_KEYBIND = new KeyBinding(ROOTS_BASE + "quiver", 0, ROOTS_GROUP);

    SLOT_MAP.put(SPELL_SLOT_1 = new KeyBinding(ROOTS_BASE + "spell_slot_1", 0, ROOTS_GROUP), 1);
    SLOT_MAP.put(SPELL_SLOT_2 = new KeyBinding(ROOTS_BASE + "spell_slot_2", 0, ROOTS_GROUP), 2);
    SLOT_MAP.put(SPELL_SLOT_3 = new KeyBinding(ROOTS_BASE + "spell_slot_3", 0, ROOTS_GROUP), 3);
    SLOT_MAP.put(SPELL_SLOT_4 = new KeyBinding(ROOTS_BASE + "spell_slot_4", 0, ROOTS_GROUP), 4);
    SLOT_MAP.put(SPELL_SLOT_5 = new KeyBinding(ROOTS_BASE + "spell_slot_5", 0, ROOTS_GROUP), 5);
    SLOT_MAP.put(SPELL_SLOT_NEXT = new KeyBinding(ROOTS_BASE + "spell_slot_next", 0, ROOTS_GROUP), 50);
    SLOT_MAP.put(SPELL_SLOT_PREVIOUS = new KeyBinding(ROOTS_BASE + "spell_slot_previous", 0, ROOTS_GROUP), 60);

    ClientRegistry.registerKeyBinding(POUCH_KEYBIND);
    ClientRegistry.registerKeyBinding(QUIVER_KEYBIND);
    for (KeyBinding spellKb : SLOT_MAP.keySet()) {
      ClientRegistry.registerKeyBinding(spellKb);
    }

  }

  @SubscribeEvent
  @OnlyIn(Dist.CLIENT)
  public static void onKeyInput(InputEvent.KeyInputEvent event) {
    Minecraft mc = Minecraft.getInstance();
    if (POUCH_KEYBIND.isKeyDown() && mc.isGameFocused()) {
      // TODO: Packets
      /*MessageServerOpenPouch packet = new MessageServerOpenPouch();
      PacketHandler.INSTANCE.sendToServer(packet);*/
    } else if (QUIVER_KEYBIND.isKeyDown() && mc.isGameFocused()) {
      // TODO: Packets
 /*     MessageServerOpenQuiver packet = new MessageServerOpenQuiver();
      PacketHandler.INSTANCE.sendToServer(packet);*/
    } else if (mc.isGameFocused()) {
      if (mc.player.getHeldItemOffhand().getItem() != ModItems.staff && mc.player.getHeldItemMainhand().getItem() != ModItems.staff) {
        return;
      }

      for (Map.Entry<KeyBinding, Integer> spell : SLOT_MAP.object2IntEntrySet()) {
        if (spell.getKey().isKeyDown()) {
          //  TODO: Packets
/*          MessageServerUpdateStaff packet = new MessageServerUpdateStaff(spell.getValue());
          PacketHandler.INSTANCE.sendToServer(packet);*/
          break;
        }
      }
    }
  }

}
