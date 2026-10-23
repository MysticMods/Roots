package mysticmods.roots.api.ritual;

import it.unimi.dsi.fastutil.Hash;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenCustomHashMap;
import mysticmods.roots.api.modifier.RitualModifier;
import mysticmods.roots.api.modifier.RitualModifierSet;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceKey;
import org.jetbrains.annotations.ApiStatus;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class LayeredRitualProperties {
  private static final Hash.Strategy<RitualModifierSet> SPELL_MODIFIER_STRATEGY = new Hash.Strategy<>() {
    @Override
    public int hashCode(RitualModifierSet o) {
      return o.transformingHash();
    }

    @Override
    public boolean equals(RitualModifierSet a, RitualModifierSet b) {
      if (a == null || b == null) {
        return false;
      }
      return a.transformingHash() == b.transformingHash();
    }
  };

  private final LayerSet defaultLayer;
  private final Map<ResourceKey<RitualModifier>, LayerSet> layers = new HashMap<>();
  private final Map<RitualModifierSet, LayeredRitualProperty> layerCache = new Object2ObjectOpenCustomHashMap<>(SPELL_MODIFIER_STRATEGY);

  public LayeredRitualProperties(Ritual.Properties properties, Ritual spell) {
    this.defaultLayer = new LayerSet(true, properties.color1, properties.color2, spell.getDescriptionId(), spell.getTooltipDescriptionId(), spell.getTooltipExtendedDescriptionId(), properties.componentCreator, properties.textColor, false, -1);
    for (Map.Entry<ResourceKey<RitualModifier>, Ritual.Transformer> entry : properties.transformers.entrySet()) {
      if (this.layers.containsKey(entry.getKey())) {
        throw new IllegalStateException("Transformer already registered for modifier '" + entry.getKey() + "'!");
      }
      var t = entry.getValue();
      this.layers.put(entry.getKey(), new LayerSet(
          t.hasColors,
          t.color1,
          t.color2,
          t.descriptionId,
          t.descriptionTooltipId,
          t.descriptionExtendedTooltipId,
          t.componentCreator,
          t.textColor,
          t.hasPredicateValue,
          t.predicateValue
      ));
    }
  }

  public LayeredRitualProperty get(IRitualInstance instance) {
    return get(instance.getEnabledModifiers());
  }

  public LayeredRitualProperty get(RitualModifierSet modifiers) {
    return layerCache.computeIfAbsent(modifiers, mods -> {
      LayerSet base = defaultLayer;
      for (ResourceKey<RitualModifier> transformer : mods.getTransformingKeys()) {
        var nextLayer = layers.get(transformer);
        if (nextLayer == null) {
          throw new NullPointerException("Layer not defined for '" + transformer + "'!");
        }
        base = base.compose(nextLayer);
      }
      return new LayeredRitualProperty(base);
    });
  }

  @ApiStatus.Internal
  public record LayerSet(boolean hasColors,
                         int color1, int color2, @Nullable String descriptionId, @Nullable String descriptionTooltipId,
                         @Nullable String descriptionTooltipExtendedId,
                         @Nullable Function<Ritual, Component[]> componentCreator,
                         @Nullable TextColor color, boolean hasPredicateValue,
                         float predicateValue) {
    public LayerSet compose(LayerSet nextLayer) {
      return new LayerSet(
          true,
          nextLayer.hasColors ? nextLayer.color1 : this.color1,
          nextLayer.hasColors ? nextLayer.color2 : this.color2,
          nextLayer.descriptionId != null ? nextLayer.descriptionId : this.descriptionId,
          nextLayer.descriptionTooltipId != null ? nextLayer.descriptionTooltipId : this.descriptionTooltipId,
          nextLayer.descriptionTooltipExtendedId != null ? nextLayer.descriptionTooltipExtendedId : this.descriptionTooltipExtendedId,
          nextLayer.componentCreator != null ? nextLayer.componentCreator : this.componentCreator,
          nextLayer.color != null ? nextLayer.color : this.color,
          nextLayer.hasPredicateValue || this.hasPredicateValue,
          nextLayer.hasPredicateValue ? nextLayer.predicateValue : this.predicateValue
      );
    }
  }
}
