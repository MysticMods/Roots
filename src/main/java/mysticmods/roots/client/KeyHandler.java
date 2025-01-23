package mysticmods.roots.client;

import com.mojang.blaze3d.platform.InputConstants;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.RootsTags;
import mysticmods.roots.init.ModAttachments;
import mysticmods.roots.network.client.ClientNetworkHandlers;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;


@EventBusSubscriber(modid = RootsAPI.MODID, value = Dist.CLIENT)
public class KeyHandler {
  @SubscribeEvent
  public static void keyEvent(InputEvent.Key event) {
    Minecraft mc = Minecraft.getInstance();
    if (mc.player == null || event.getAction() == InputConstants.PRESS || event.getKey() == -1) {
      return;
    }
    if (KeyBindings.OPEN_SPELL_LIBRARY.matches(event.getKey(), event.getScanCode())) {
      int inventorySlot = -1;
      InteractionHand hand = InteractionHand.MAIN_HAND;
      ItemStack stack = mc.player.getItemInHand(hand);
      if (!stack.is(RootsTags.Items.CASTING_TOOLS) || !stack.has(ModAttachments.SPELL_STORAGE)) {
        hand = InteractionHand.OFF_HAND;
        stack = mc.player.getItemInHand(hand);
      }
      if (!stack.is(RootsTags.Items.CASTING_TOOLS) || !stack.has(ModAttachments.SPELL_STORAGE)) {
        Inventory inv = mc.player.getInventory();
        for (int i = 0; i < inv.getContainerSize(); i++) {
          stack = inv.getItem(i);
          if (stack.is(RootsTags.Items.CASTING_TOOLS) && stack.has(ModAttachments.SPELL_STORAGE)) {
            hand = null;
            inventorySlot = i;
            break;
          }
        }
      }
      if (!stack.is(RootsTags.Items.CASTING_TOOLS) || !stack.has(ModAttachments.SPELL_STORAGE)) {
        return;
      }

      ClientNetworkHandlers.openLibrary(hand, inventorySlot);
    }
  }
}
