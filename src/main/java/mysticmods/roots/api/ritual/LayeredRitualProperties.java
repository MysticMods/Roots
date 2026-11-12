package mysticmods.roots.api.ritual;

import it.unimi.dsi.fastutil.Hash;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenCustomHashMap;
import mysticmods.roots.api.Like;
import mysticmods.roots.api.RitualLike;
import mysticmods.roots.api.modifier.RitualModifier;
import mysticmods.roots.api.modifier.RitualModifierSet;
import mysticmods.roots.api.property.LayeredProperties;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceKey;
import org.jetbrains.annotations.ApiStatus;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class LayeredRitualProperties implements LayeredProperties<LayeredRitualProperty, Ritual, IRitualInstance, RitualModifier, RitualModifierSet> {
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

  private final RitualLayerSet defaultLayer;
  private final Map<ResourceKey<RitualModifier>, RitualLayerSet> layers = new HashMap<>();
  private final Map<RitualModifierSet, LayeredRitualProperty> layerCache = new Object2ObjectOpenCustomHashMap<>(SPELL_MODIFIER_STRATEGY);

  public LayeredRitualProperties(Ritual.Properties properties, Ritual ritual) {
    this.defaultLayer = new RitualLayerSet(true, properties.color1, properties.color2, ritual.getDescriptionId(), ritual.getTooltipDescriptionId(), ritual.getTooltipExtendedDescriptionId(), properties.componentCreator, properties.textColor, false, -1);
    for (Map.Entry<ResourceKey<RitualModifier>, Ritual.Transformer> entry : properties.transformers.entrySet()) {
      if (this.layers.containsKey(entry.getKey())) {
        throw new IllegalStateException("Transformer already registered for modifier '" + entry.getKey() + "'!");
      }
      var t = entry.getValue();
      this.layers.put(entry.getKey(), new RitualLayerSet(
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

  @Override
  public LayeredRitualProperty get(IRitualInstance instance) {
    return get(instance.getEnabledModifiers());
  }

  @Override
  public LayeredRitualProperty get(RitualModifierSet modifiers) {
    return layerCache.computeIfAbsent(modifiers, mods -> {
      RitualLayerSet base = defaultLayer;
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
  public record RitualLayerSet(boolean hasColors,
                               int color1, int color2, @Nullable String descriptionId, @Nullable String descriptionTooltipId,
                               @Nullable String descriptionTooltipExtendedId,
                               @Nullable Function<Like<Ritual>, Component[]> componentCreator,
                               @Nullable TextColor color, boolean hasPredicateValue,
                               float predicateValue) {
    public RitualLayerSet compose(RitualLayerSet nextLayer) {
      return new RitualLayerSet(
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
