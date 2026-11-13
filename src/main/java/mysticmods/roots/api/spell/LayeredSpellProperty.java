package mysticmods.roots.api.spell;

import mysticmods.roots.api.description.Cycling;
import mysticmods.roots.api.registry.Like;
import mysticmods.roots.api.property.LayeredProperty;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;

import javax.annotation.Nullable;
import java.util.function.Function;

public record LayeredSpellProperty(
    SpellType.Cast cast, SpellType.Charge charge,
    int color1, int color2, String descriptionId, String descriptionTooltipId,
    String descriptionTooltipExtendedId,
    Function<Like<Spell>, Component[]> componentCreator,
    TextColor color,
    @Nullable DataComponentType<? extends Cycling<?>> cycleComponent,
    float predicateValue) implements LayeredProperty<Spell> {
  public LayeredSpellProperty(LayeredSpellProperties.LayerSet base) {
    this(base.cast(), base.charge(), base.color1(), base.color2(), base.descriptionId(), base.descriptionTooltipId(), base.descriptionTooltipExtendedId(), base.componentCreator(), base.color(), base.component(), base.predicateValue());
  }
}
