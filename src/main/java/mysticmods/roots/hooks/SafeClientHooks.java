package mysticmods.roots.hooks;

import mysticmods.roots.api.RootsTags;
import mysticmods.roots.client.gui.screen.StaffScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;

public class SafeClientHooks {
  public static void openGui (InteractionHand hand) {
    StaffScreen.open(hand);
  }

  public static void updateGui (ItemStack stack) {
    Minecraft instance = Minecraft.getInstance();
    if (instance.screen instanceof StaffScreen staffScreen && stack.is(RootsTags.Items.CASTING_TOOLS)) {
      staffScreen.setStack(stack);
    }
  }
}
