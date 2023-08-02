package mysticmods.roots.client;

import com.mojang.blaze3d.platform.InputConstants;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.RootsTags;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = RootsAPI.MODID, value = Dist.CLIENT)
public class KeyHandler {
  @SubscribeEvent
  public static void keyEvent(InputEvent.Key event) {
    Minecraft mc = Minecraft.getInstance();
    if (mc.player == null || event.getAction() == InputConstants.PRESS || event.getKey() == -1) {
      return;
    }
    if (KeyBindings.OPEN_SPELL_LIBRARY.matches(event.getKey(), event.getScanCode())) {
      InteractionHand hand = InteractionHand.MAIN_HAND;
      ItemStack stack = mc.player.getItemInHand(hand);
      if (!stack.is(RootsTags.Items.CASTING_TOOLS)) {
        hand = InteractionHand.OFF_HAND;
        stack = mc.player.getItemInHand(hand);
        if (!stack.is(RootsTags.Items.CASTING_TOOLS)) {
          return;
        }
      }

      ClientHooks.openGui(hand);
    }
  }
}
