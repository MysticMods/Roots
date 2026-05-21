package mysticmods.roots.network.server.debug;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

public class ServerDebugHooks {
  public static void tryDroppingStaff(Player player, @Nullable InteractionHand hand, int inventorySlot) {
    if (hand != null) {
      player.drop(player.getItemInHand(hand), false);
      player.setItemInHand(hand, ItemStack.EMPTY);
    } else {
      var item = player.getInventory().getItem(inventorySlot);
      player.drop(item, false);
      player.getInventory().setItem(inventorySlot, ItemStack.EMPTY);
    }
  }
}
