package mysticmods.roots.integration.curios;

import mysticmods.roots.util.ItemUtil;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;

import java.util.Collections;
import java.util.List;

public class CuriosIntegrationInternal {
  public static List<ItemStack> getTagged(Player player, TagKey<Item> itemTagKey) {
    return
        CuriosApi.getCuriosInventory(player)
            .map(o -> o.findCurios(ItemUtil.tag(itemTagKey)).stream().map(SlotResult::stack).toList())
            .orElse(Collections.emptyList());
  }
}
