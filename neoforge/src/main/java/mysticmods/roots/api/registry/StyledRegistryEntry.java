package mysticmods.roots.api.registry;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Style;
import org.jetbrains.annotations.Nullable;

public abstract class StyledRegistryEntry<T> extends DescribedRegistryEntry<T> implements IStyledRegistryEntry {
  protected Style style;
  protected ChatFormatting color;

  @Override
  @Nullable
  public ChatFormatting getColor() {
    return color;
  }

  @Override
  public void setColor(ChatFormatting color) {
    this.color = color;
  }

  @Override
  public Style getOrCreateStyle() {
    if (style == null) {
      ChatFormatting color = getColor();
      if (color != null) {
        style = Style.EMPTY.withColor(color).withBold(isBold());
      } else {
        style = Style.EMPTY.withBold(isBold());
      }
    }
    return style;
  }
}
