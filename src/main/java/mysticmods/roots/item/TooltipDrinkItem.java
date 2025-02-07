package mysticmods.roots.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class TooltipDrinkItem extends Item {
  private final String translationKey;

  public TooltipDrinkItem(String translationKey, Properties properties) {
    super(properties);
    this.translationKey = translationKey;
  }

  @Override
  public void appendHoverText(ItemStack pStack, TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
    super.appendHoverText(pStack, pContext, pTooltipComponents, pTooltipFlag);
    pTooltipComponents.add(Component.literal(""));
    pTooltipComponents.add(Component.translatable(translationKey)
        .setStyle(Style.EMPTY.withColor(TextColor.fromLegacyFormat(ChatFormatting.YELLOW))));
  }
}
