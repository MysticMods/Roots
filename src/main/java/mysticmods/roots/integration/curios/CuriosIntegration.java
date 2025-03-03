package mysticmods.roots.integration.curios;

import mysticmods.roots.api.RootsTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class CuriosIntegration {
  public static List<ItemStack> getCharms(Player player) {
    List<ItemStack> result = new ArrayList<>();
    Optional<ICuriosItemHandler> optHandler = CuriosApi.getCuriosInventory(player);
    if (optHandler.isPresent()) {
      ICuriosItemHandler handler = optHandler.get();
      // This doesn't seem to be the correct way
      IItemHandlerModifiable curios = handler.getEquippedCurios();
      for (int i = 0; i < curios.getSlots(); i++) {
        ItemStack stack = curios.getStackInSlot(i);
        if (stack.is(RootsTags.Items.CHARMS)) {
          result.add(stack);
        }
      }

      return result;
    } else {
      return Collections.emptyList();
    }
  }
}
