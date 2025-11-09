package mysticmods.roots.api.modifier;

import mysticmods.roots.api.datamap.DataMaps;
import mysticmods.roots.api.grove.Grove;
import mysticmods.roots.api.herb.CostInstance;
import mysticmods.roots.api.registry.RootsRegistries;
import mysticmods.roots.api.spell.Spell;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.registries.datamaps.DataMapType;
import org.jetbrains.annotations.NotNull;

public class SpellModifier extends Modifier<Spell, SpellModifier> {
  public SpellModifier(ResourceKey<Grove> grove, CostInstance defaultCosts, @NotNull ResourceKey<SpellModifier> parent, ResourceKey<Spell> applicable) {
    super(grove, defaultCosts, parent, applicable);
  }

  public SpellModifier(ResourceKey<Grove> grove, CostInstance defaultCosts, ResourceKey<Spell> applicable) {
    super(grove, defaultCosts, applicable);
  }

  @Override
  protected DataMapType<SpellModifier, CostInstance> getDataMapType() {
    return DataMaps.SPELL_MODIFIER_COST_DATA;
  }

  @Override
  public Holder<mysticmods.roots.api.modifier.SpellModifier> builtInRegistryHolder() {
    return RootsRegistries.SPELL_MODIFIERS.wrapAsHolder(this);
  }

  @Override
  protected String getSignifier() {
    return "spell_modifier";
  }
}
