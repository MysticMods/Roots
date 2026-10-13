package mysticmods.roots.api.ritual;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;

import java.util.function.Supplier;

public record LayeredRitualProperty(
    int color1, int color2, String descriptionId, String descriptionTooltipId,
    String descriptionTooltipExtendedId,
    Supplier<Component[]> componentGetter,
    TextColor color,
    float predicateValue) {
  public LayeredRitualProperty(LayeredRitualProperties.LayerSet base) {
    this(base.color1(), base.color2(), base.descriptionId(), base.descriptionTooltipId(), base.descriptionTooltipExtendedId(), base.componentGetter(), base.color(), base.predicateValue());
  }
}
