package mysticmods.roots.integration.curios;

import mysticmods.roots.api.RootsTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CuriosIntegration {
  public static List<ItemStack> getCharms(Player player) {
    return getTagged(player, RootsTags.Items.CHARMS);
  }

  public static List<ItemStack> getPouches(Player player) {
    return getTagged(player, RootsTags.Items.ALL_POUCHES);
  }

  private static List<ItemStack> getTagged(Player player, TagKey<Item> allPouches) {
    if (ModList.get().isLoaded("curios")) {
      return CuriosIntegrationInternal.getTagged(player, allPouches);
    } else {
      return new ArrayList<>();
    }
  }
}
