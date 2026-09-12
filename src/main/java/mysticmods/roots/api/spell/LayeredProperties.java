package mysticmods.roots.api.spell;

import com.google.common.base.Suppliers;
import mysticmods.roots.api.Cycling;
import mysticmods.roots.api.SpellType;
import mysticmods.roots.api.modifier.SpellModifier;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceKey;
import org.jetbrains.annotations.Nls;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class LayeredProperties {
  private final LayerSet defaultLayer;
  private final Map<ResourceKey<SpellModifier>, LayerSet> layers = new HashMap<>();
  private final Map<List<ResourceKey<SpellModifier>>, LayerSet> layerCache = new HashMap<>();

  public LayeredProperties (Spell.Properties properties, Spell spell) {
    this.defaultLayer = new LayerSet(properties.castType, properties.chargeType, true, properties.color1, properties.color2, spell.getDescriptionId(), spell.getTooltipDescriptionId(), spell.getTooltipExtendedDescriptionId(), Suppliers.memoize(spell::getOrCreateDescriptionComponents), properties.textColor, properties.cycleComponent != null, properties.cycleComponent, false, -1);
  }

/*  public LayerSet get (ISpellInstance instance) {

  }*/

  public record LayerSet (@Nullable SpellType.Cast cast, @Nullable SpellType.Charge charge, boolean hasColors, int color1, int color2, @Nullable String descriptionId, @Nullable String descriptionTooltipId, @Nullable String descriptionTooltipExtendedId, @Nullable Supplier<Component[]> componentGetter, @Nullable TextColor color, boolean hasComponent, @Nullable DataComponentType<? extends Cycling<?>> component, boolean hasPredicateValue, float predicateValue) {
  }

  public static class LayerBuilder {

  }
}
