package mysticmods.roots.api.spell;

import com.google.common.base.Suppliers;
import it.unimi.dsi.fastutil.Hash;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenCustomHashMap;
import mysticmods.roots.api.Cycling;
import mysticmods.roots.api.SpellType;
import mysticmods.roots.api.modifier.SpellModifier;
import mysticmods.roots.api.modifier.SpellModifierSet;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceKey;
import org.jetbrains.annotations.ApiStatus;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class LayeredProperties {
  private static final Hash.Strategy<SpellModifierSet> SPELL_MODIFIER_STRATEGY = new Hash.Strategy<>() {
    @Override
    public int hashCode(SpellModifierSet o) {
      return o.transformingHash();
    }

    @Override
    public boolean equals(SpellModifierSet a, SpellModifierSet b) {
      if (a == null || b == null) {
        return false;
      }
      return a.transformingHash() == b.transformingHash();
    }
  };

  private final LayerSet defaultLayer;
  private final Map<ResourceKey<SpellModifier>, LayerSet> layers = new HashMap<>();
  private final Map<SpellModifierSet, LayeredProperty> layerCache = new Object2ObjectOpenCustomHashMap<>(SPELL_MODIFIER_STRATEGY);

  public LayeredProperties(Spell.Properties properties, Spell spell) {
    this.defaultLayer = new LayerSet(properties.castType, properties.chargeType, true, properties.color1, properties.color2, spell.getDescriptionId(), spell.getTooltipDescriptionId(), spell.getTooltipExtendedDescriptionId(), Suppliers.memoize(spell::getOrCreateDescriptionComponents), properties.textColor, properties.cycleComponent != null, properties.cycleComponent, false, -1);
    for (Map.Entry<ResourceKey<SpellModifier>, Spell.Transformer> entry : properties.transformers.entrySet()) {
      if (this.layers.containsKey(entry.getKey())) {
        throw new IllegalStateException("Transformer already registered for modifier '" + entry.getKey() + "'!");
      }
      var t = entry.getValue();
      this.layers.put(entry.getKey(), new LayerSet(
          t.castType,
          t.unitType,
          t.hasColors,
          t.color1,
          t.color2,
          t.descriptionId,
          t.descriptionTooltipId,
          t.descriptionExtendedTooltipId,
          t.extendedComponents != null ? Suppliers.memoize(t.extendedComponents::get) : null,
          t.textColor,
          t.hasComponent,
          t.cyclingComponent,
          t.hasPredicateValue,
          t.predicateValue
      ));
    }
  }

  public LayeredProperty get(ISpellInstance instance) {
    return get(instance.getEnabledModifiers());
  }

  public LayeredProperty get(SpellModifierSet modifiers) {
    return layerCache.computeIfAbsent(modifiers, mods -> {
      LayerSet base = defaultLayer;
      for (ResourceKey<SpellModifier> transformer : mods.getTransformingKeys()) {
        var nextLayer = layers.get(transformer);
        if (nextLayer == null) {
          throw new NullPointerException("Layer not defined for '" + transformer + "'!");
        }
        base = base.compose(nextLayer);
      }
      return new LayeredProperty(base);
    });
  }

  @ApiStatus.Internal
  public record LayerSet(@Nullable SpellType.Cast cast, @Nullable SpellType.Charge charge, boolean hasColors,
                         int color1, int color2, @Nullable String descriptionId, @Nullable String descriptionTooltipId,
                         @Nullable String descriptionTooltipExtendedId, @Nullable Supplier<Component[]> componentGetter,
                         @Nullable TextColor color, boolean hasComponent,
                         @Nullable DataComponentType<? extends Cycling<?>> component, boolean hasPredicateValue,
                         float predicateValue) {
    public LayerSet compose(LayerSet nextLayer) {
      return new LayerSet(
          nextLayer.cast != null ? nextLayer.cast : this.cast,
          nextLayer.charge != null ? nextLayer.charge : this.charge,
          true,
          nextLayer.hasColors ? nextLayer.color1 : this.color1,
          nextLayer.hasColors ? nextLayer.color2 : this.color2,
          nextLayer.descriptionId != null ? nextLayer.descriptionId : this.descriptionId,
          nextLayer.descriptionTooltipId != null ? nextLayer.descriptionTooltipId : this.descriptionTooltipId,
          nextLayer.descriptionTooltipExtendedId != null ? nextLayer.descriptionTooltipExtendedId : this.descriptionTooltipExtendedId,
          nextLayer.componentGetter != null ? nextLayer.componentGetter : this.componentGetter,
          nextLayer.color != null ? nextLayer.color : this.color,
          nextLayer.hasComponent || this.hasComponent,
          nextLayer.hasComponent ? nextLayer.component : this.component,
          nextLayer.hasPredicateValue || this.hasPredicateValue,
          nextLayer.hasPredicateValue ? nextLayer.predicateValue : this.predicateValue
      );
    }
  }
}
