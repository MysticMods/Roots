package mysticmods.roots.util;

import mysticmods.roots.init.ModAttachments;
import mysticmods.roots.init.ModAttributes;
import mysticmods.roots.init.ModEnchantment;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;

import javax.annotation.Nullable;

public class ForagingUtil {
  public static int getForagingValue(@Nullable Player player, ItemStack item) {
    int foraging = 0;

    // This currently does not check the enchantment.
    if (item.has(ModAttachments.FORAGING)) {
      //noinspection DataFlowIssue
      foraging += item.get(ModAttachments.FORAGING);
    }

    if (player == null) {
      return foraging;
    }

    foraging += (int) player.getAttributeValue(ModAttributes.FORAGING);

    var lookup = player.level().registryAccess().registryOrThrow(Registries.ENCHANTMENT);
    Holder<Enchantment> foragingEnchantment = lookup.getHolder(ModEnchantment.FORAGING).orElse(null);
    if (foragingEnchantment != null) {
      foraging += item.getEnchantmentLevel(foragingEnchantment);
    }

    return foraging;
  }
}
