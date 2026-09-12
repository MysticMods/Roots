package mysticmods.roots.api.spell;

import mysticmods.roots.api.Cycling;
import mysticmods.roots.api.SpellType;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public record LayeredProperty(
    SpellType.Cast cast, SpellType.Charge charge,
    int color1, int color2, String descriptionId, String descriptionTooltipId,
    String descriptionTooltipExtendedId,
    Supplier<Component[]> componentGetter,
    TextColor color,
    @Nullable DataComponentType<? extends Cycling<?>> component,
    float predicateValue) {
  public LayeredProperty(LayeredProperties.LayerSet base) {
    this(base.cast(), base.charge(), base.color1(), base.color2(), base.descriptionId(), base.descriptionTooltipId(), base.descriptionTooltipExtendedId(), base.componentGetter(), base.color(), base.component(), base.predicateValue());
  }
}
