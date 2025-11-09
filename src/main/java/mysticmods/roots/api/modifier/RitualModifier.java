package mysticmods.roots.api.modifier;

import mysticmods.roots.api.datamap.DataMaps;
import mysticmods.roots.api.grove.Grove;
import mysticmods.roots.api.herb.CostInstance;
import mysticmods.roots.api.registry.RootsRegistries;
import mysticmods.roots.api.ritual.Ritual;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.registries.datamaps.DataMapType;
import org.jetbrains.annotations.NotNull;

public class RitualModifier extends Modifier<Ritual, RitualModifier> {
  public RitualModifier(ResourceKey<Grove> grove, CostInstance defaultCosts, @NotNull ResourceKey<RitualModifier> parent, ResourceKey<Ritual> applicable) {
    super(grove, defaultCosts, parent, applicable);
  }

  public RitualModifier(ResourceKey<Grove> grove, CostInstance defaultCosts, ResourceKey<Ritual> applicable) {
    super(grove, defaultCosts, applicable);
  }

  @Override
  protected DataMapType<RitualModifier, CostInstance> getDataMapType() {
    return DataMaps.RITUAL_MODIFIER_COST_DATA;
  }

  @Override
  public Holder<RitualModifier> builtInRegistryHolder() {
    return RootsRegistries.RITUAL_MODIFIERS.wrapAsHolder(this);
  }

  @Override
  protected String getSignifier() {
    return "ritual_modifier";
  }
}
