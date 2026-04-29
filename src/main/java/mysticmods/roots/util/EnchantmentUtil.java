package mysticmods.roots.util;

import mysticmods.roots.init.ModAttachments;
import mysticmods.roots.init.ModAttributes;
import mysticmods.roots.init.ModEnchantment;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.apache.commons.lang3.mutable.MutableFloat;

import javax.annotation.Nullable;

public class EnchantmentUtil {
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

  public static float getCollectingIncrease(ServerLevel level, ItemStack stack) {
    MutableFloat mutablefloat = new MutableFloat(0.0F);
    EnchantmentHelper.runIterationOnItem(
        stack, (ench, enchLevel) -> ench.value()
            .modifyUnfilteredValue(ModEnchantment.COLLECTING_EFFECT.get(), level.random, enchLevel, mutablefloat)
    );
    return Math.max(0.0F, mutablefloat.floatValue());
  }
}
