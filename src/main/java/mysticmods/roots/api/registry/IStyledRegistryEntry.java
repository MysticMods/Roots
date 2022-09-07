package mysticmods.roots.api.registry;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;

import javax.annotation.Nullable;

public interface IStyledRegistryEntry extends IDescribedRegistryEntry {
  @Nullable
  ChatFormatting getColor();

  void setColor(ChatFormatting color);

  Style getOrCreateStyle();

  default boolean isBold() {
    return false;
  }

  default MutableComponent getStyledName() {
    return getStyledName(false);
  }

  default MutableComponent getStyledName(boolean bold) {
    return getName(getStyle().withBold(bold));
  }

  default Style getStyle() {
    return getOrCreateStyle();
  }
}
