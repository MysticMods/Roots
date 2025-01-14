package mysticmods.roots.gen;

import mysticmods.roots.api.data.DataMaps;
import mysticmods.roots.api.data.PropertyDataMap;
import mysticmods.roots.api.herb.Cost;
import mysticmods.roots.api.herb.Herb;
import mysticmods.roots.api.spell.SpellModifier;
import mysticmods.roots.api.registry.RootsRegistries;
import mysticmods.roots.api.ritual.Ritual;
import mysticmods.roots.api.spell.Spell;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.data.DataMapProvider;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class RootsDataMapProvider extends DataMapProvider {
  public RootsDataMapProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
    super(packOutput, lookupProvider);
  }

  @Override
  protected void gather() {
    Builder<List<Cost>, Spell> builder = builder(DataMaps.SPELL_COST_DATA)
        .replace(false);

    RootsRegistries.SPELLS.stream().forEach(spell -> {
      builder.add(spell.builtInRegistryHolder(), spell.getDefaultCosts(), false);
    });

    Builder<Herb, Item> builder2 = builder(DataMaps.HERB_ITEM_DATA)
        .replace(false);

    RootsRegistries.HERBS.stream().forEach(herb -> {
      builder2.add(herb.getItem(), herb, false);
    });

    Builder<List<Cost>, SpellModifier> builder3 = builder(DataMaps.SPELL_MODIFIER_COST_DATA)
        .replace(false);

    RootsRegistries.SPELL_MODIFIERS.stream().forEach(modifier -> {
      builder3.add(modifier.builtInRegistryHolder(), modifier.getDefaultCosts(), false);
    });

    Builder<PropertyDataMap, Spell> builder4 = builder(DataMaps.SPELL_PROPERTY_DATA)
        .replace(false);
    RootsRegistries.SPELLS.stream().forEach(spell -> {
      builder4.add(spell.builtInRegistryHolder(), new PropertyDataMap(spell.getProperties()), false);
    });

    Builder<PropertyDataMap, Ritual> builder5 = builder(DataMaps.RITUAL_PROPERTY_DATA)
        .replace(false);

    RootsRegistries.RITUALS.stream().forEach(ritual -> {
      builder5.add(ritual.builtInRegistryHolder(), new PropertyDataMap(ritual.getProperties()), false);
    });
  }
}
