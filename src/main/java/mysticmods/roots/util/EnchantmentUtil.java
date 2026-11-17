package mysticmods.roots.util;

import mysticmods.roots.init.ModAttributes;
import mysticmods.roots.init.ModEnchantment;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.apache.commons.lang3.mutable.MutableFloat;

public class EnchantmentUtil {
  public static double getForagingValue(ItemStack item) {
    AttributeInstance instance = new AttributeInstance(ModAttributes.FORAGING, (c) -> {
    });
    item.getAttributeModifiers().forEach(EquipmentSlotGroup.HAND, (attr, mod) -> {
      if (attr.is(ModAttributes.FORAGING)) {
        instance.addPermanentModifier(mod);
      }
    });
    return instance.getValue();
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
