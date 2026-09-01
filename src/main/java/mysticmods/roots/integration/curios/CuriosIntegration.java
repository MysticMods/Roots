package mysticmods.roots.integration.curios;

import mysticmods.roots.api.RootsTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;

import java.util.ArrayList;
import java.util.List;

public class CuriosIntegration {
  public static List<ItemStack> getCharms(Player player) {
    return getTagged(player, RootsTags.Items.CHARMS);
  }

  public static List<ItemStack> getPouches(Player player) {
    return getTagged(player, RootsTags.Items.ALL_POUCHES);
  }

  public static List<ItemStack> getTagged(Player player, TagKey<Item> allPouches) {
    if (ModList.get().isLoaded("curios")) {
      return CuriosIntegrationInternal.getTagged(player, allPouches);
    } else {
      return new ArrayList<>();
    }
  }

  public static void init (IEventBus bus) {
    if (ModList.get().isLoaded("curios")) {
      CuriosIntegrationInternal.init(bus);
    }
  }
}
