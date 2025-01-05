package mysticmods.roots.api.data;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.herb.Herb;
import mysticmods.roots.api.registry.RootsRegistries;
import mysticmods.roots.api.spell.Spell;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.data.DataMapProvider;
import net.neoforged.neoforge.registries.datamaps.AdvancedDataMapType;

import java.util.concurrent.CompletableFuture;


public class RootsDataMapProvider extends DataMapProvider {
  public RootsDataMapProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
    super(packOutput, lookupProvider);
  }

  @Override
  protected void gather() {
    Builder<SpellCostDataMap.SpellCostData, Spell> builder = builder(DataMaps.SPELL_COST_DATA)
        .replace(false);

    RootsRegistries.SPELLS.stream().forEach(spell -> {
      builder.add(spell.builtInRegistryHolder(), new SpellCostDataMap.SpellCostData(spell.getCosts()), false);
    });

    Builder<Herb, Item> builder2 = builder(DataMaps.HERB_ITEM_DATA)
        .replace(false);

    RootsRegistries.HERBS.stream().forEach(herb -> {
      builder2.add(herb.getItem(), herb, false);
    });
  }
}
