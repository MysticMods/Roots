package mysticmods.roots.util;

import mysticmods.roots.init.ModAttachments;
import net.minecraft.world.item.ItemStack;

public class ForagingUtil {
  public static int getForagingValue(ItemStack item) {
    // This currently does not check the enchantment.
    if (item.has(ModAttachments.FORAGING)) {
      //noinspection DataFlowIssue
      return item.get(ModAttachments.FORAGING);
    }

    return 0;
  }
}
