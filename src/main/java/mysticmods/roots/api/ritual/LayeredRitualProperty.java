package mysticmods.roots.api.ritual;

import mysticmods.roots.api.registry.Like;
import mysticmods.roots.api.property.LayeredProperty;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;

import java.util.function.Function;

public record LayeredRitualProperty(
    int color1, int color2, String descriptionId, String descriptionTooltipId,
    String descriptionTooltipExtendedId,
    Function<Like<Ritual>, Component[]> componentCreator,
    TextColor color,
    float predicateValue) implements LayeredProperty<Ritual> {
  public LayeredRitualProperty(LayeredRitualProperties.RitualLayerSet base) {
    this(base.color1(), base.color2(), base.descriptionId(), base.descriptionTooltipId(), base.descriptionTooltipExtendedId(), base.componentCreator(), base.color(), base.predicateValue());
  }
}
