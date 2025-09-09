package mysticmods.roots.integration.curios;

import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CuriosIntegrationInternal {
  public static List<ItemStack> getTagged(Player player, TagKey<Item> itemTagKey) {
    List<ItemStack> result = new ArrayList<>();
    Optional<ICuriosItemHandler> optHandler = CuriosApi.getCuriosInventory(player);
    if (optHandler.isPresent()) {
      ICuriosItemHandler handler = optHandler.get();
      // This doesn't seem to be the correct way
      IItemHandlerModifiable curios = handler.getEquippedCurios();
      for (int i = 0; i < curios.getSlots(); i++) {
        ItemStack stack = curios.getStackInSlot(i);
        if (stack.is(itemTagKey)) {
          result.add(stack);
        }
      }
    }
    return result;
  }
}
