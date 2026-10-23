package mysticmods.roots.api.ritual;

import mysticmods.roots.api.spell.Spell;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;

import java.util.function.Function;
import java.util.function.Supplier;

public record LayeredRitualProperty(
    int color1, int color2, String descriptionId, String descriptionTooltipId,
    String descriptionTooltipExtendedId,
    Function<Ritual, Component[]> componentCreator,
    TextColor color,
    float predicateValue) {
  public LayeredRitualProperty(LayeredRitualProperties.LayerSet base) {
    this(base.color1(), base.color2(), base.descriptionId(), base.descriptionTooltipId(), base.descriptionTooltipExtendedId(), base.componentCreator(), base.color(), base.predicateValue());
  }
}
