package mysticmods.roots.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class TooltipItem extends Item {
  private final String translationKey;


  public TooltipItem(Properties properties, String translationKey) {
    super(properties);
    this.translationKey = translationKey;
  }

  @Override
  public void appendHoverText(ItemStack arg, TooltipContext arg2, List<Component> tooltip, TooltipFlag arg3) {
    super.appendHoverText(arg, arg2, tooltip, arg3);

    tooltip.add(Component.literal(""));
    tooltip.add(Component.translatable(translationKey)
        .setStyle(Style.EMPTY.withColor(TextColor.fromLegacyFormat(ChatFormatting.YELLOW))));
  }
}
