package mysticmods.roots.api.modifier;

import mysticmods.roots.api.datamap.DataMaps;
import mysticmods.roots.api.herb.ChargeType;
import mysticmods.roots.api.herb.CostInstance;
import mysticmods.roots.api.registry.RootsRegistries;
import mysticmods.roots.api.ritual.Ritual;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.datamaps.DataMapType;
import org.jetbrains.annotations.NotNull;

// TODO: Costs??? Do these really need costs?
public class RitualModifier extends Modifier<Ritual, RitualModifier> {
  public RitualModifier(CostInstance defaultCosts, @NotNull ResourceKey<RitualModifier> parent, ResourceKey<Ritual> applicable) {
    super(defaultCosts, parent, applicable);
  }

  public RitualModifier(CostInstance defaultCosts, ResourceKey<Ritual> applicable) {
    super(defaultCosts, applicable);
  }

  @Override
  protected DataMapType<RitualModifier, CostInstance> getDataMapType() {
    return DataMaps.RITUAL_MODIFIER_COST_DATA;
  }

  @Override
  protected DataMapType<RitualModifier, Item> getIconDataMapType() {
    return DataMaps.RITUAL_MODIFIER_ICONS;
  }

  @Override
  public Holder<RitualModifier> builtInRegistryHolder() {
    return RootsRegistries.RITUAL_MODIFIERS.wrapAsHolder(this);
  }

  @Override
  protected String getSignifier() {
    return "ritual_modifier";
  }

  @Override
  public ChargeType getChargeType() {
    return ChargeType.INSTANCE;
  }
}
