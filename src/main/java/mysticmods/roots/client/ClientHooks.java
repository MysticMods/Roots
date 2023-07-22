package mysticmods.roots.client;

import mysticmods.roots.client.gui.screen.StaffScreen;
import net.minecraft.world.InteractionHand;

public class ClientHooks {
  public static void openGui (InteractionHand hand) {
    StaffScreen.open(hand);
  }
}
