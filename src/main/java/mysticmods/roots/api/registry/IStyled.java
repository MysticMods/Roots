package mysticmods.roots.api.registry;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;

import javax.annotation.Nullable;

public interface IStyled extends IDescribed {
  @Nullable
  TextColor getTextColor();

  Style getOrCreateStyle();

  default boolean isBold() {
    return false;
  }

  default MutableComponent getStyledName() {
    return getStyledName(isBold());
  }

  default MutableComponent getStyledName(boolean bold) {
    return getName(getStyle().withBold(bold));
  }

  default Style getStyle() {
    return getOrCreateStyle();
  }
}
