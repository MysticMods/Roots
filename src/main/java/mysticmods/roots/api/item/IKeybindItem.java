package mysticmods.roots.api.item;

import mysticmods.roots.client.RootsClientHooks;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.ArrayList;
import java.util.List;

public interface IKeybindItem {
  String getBoundKey();

  default String[] getBoundKeys() {
    return null;
  }

  String getTranslationKey();

  default void includeKeybind(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
    if (context.level() != null && context.level().isClientSide()) {
      tooltipComponents.add(CommonComponents.EMPTY);
      String[] keys = getBoundKeys();
      if (keys == null) {
        tooltipComponents.add(Component.translatable(getTranslationKey(), RootsClientHooks.getKey(tooltipFlag, getBoundKey())));
      } else {
        List<Component> components = new ArrayList<>();
        for (String key : keys) {
          components.add(RootsClientHooks.getKey(tooltipFlag, key));
        }
        tooltipComponents.add(Component.translatable(getTranslationKey(), components.toArray()));
      }
    }
  }

  default Component summary (ItemStack stack, Item.TooltipContext context, TooltipFlag tooltipFlag) {
    return Component.translatable("roots.tooltip.crafted_key_binding", RootsClientHooks.getExtendedKey(getBoundKey()));
  }
}
