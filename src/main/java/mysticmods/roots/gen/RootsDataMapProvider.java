package mysticmods.roots.gen;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.datamap.DataMaps;
import mysticmods.roots.api.datamap.GroveData;
import mysticmods.roots.api.datamap.PropertyDataMap;
import mysticmods.roots.api.grove.Grove;
import mysticmods.roots.api.herb.Cost;
import mysticmods.roots.api.herb.Herb;
import mysticmods.roots.api.registry.RootsRegistries;
import mysticmods.roots.api.ritual.Ritual;
import mysticmods.roots.api.spell.Spell;
import mysticmods.roots.api.spell.SpellModifier;
import mysticmods.roots.init.ModGroves;
import mysticmods.roots.init.ModItems;
import mysticmods.roots.item.TokenItem;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.data.DataMapProvider;
import net.neoforged.neoforge.registries.datamaps.builtin.Compostable;
import net.neoforged.neoforge.registries.datamaps.builtin.FurnaceFuel;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;

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

    Builder<GroveData, Grove> builder6 = builder(DataMaps.GROVE_DATA).replace(false);
    ModGroves.RECORDS.forEach(record -> {
      builder6.add(RootsRegistries.GROVES.getHolderOrThrow(record.groveKey()), new GroveData(record), false);
    });

    Builder<Spell, SpellModifier> builder7 = builder(DataMaps.SPELL_MODIFIER_SPELL).replace(false);
    RootsRegistries.SPELL_MODIFIERS.stream().forEach(modifier -> {
      builder7.add(modifier.builtInRegistryHolder(), modifier.getSpell().value(), false);
    });

    Builder<SpellModifier, SpellModifier> builder8 = builder(DataMaps.SPELL_MODIFIER_PARENT).replace(false);
    RootsRegistries.SPELL_MODIFIERS.stream().forEach(modifier -> {
      if (modifier.getParent() != null) {
        builder8.add(modifier.builtInRegistryHolder(), modifier.getParent(), false);
      }
    });

    Builder<Compostable, Item> builder9 = builder(NeoForgeDataMaps.COMPOSTABLES).replace(false);
    builder9.add(ModItems.WILDWOOD_LEAVES, new Compostable(0.3f), false);
    builder9.add(ModItems.WILDWOOD_SAPLING, new Compostable(0.6f), false);
    builder9.add(ModItems.AUBERGINE_SEEDS, new Compostable(0.3f, true), false);
    builder9.add(ModItems.PERESKIA_BULB, new Compostable(0.3f, true), false);
    builder9.add(ModItems.SPIRITLEAF_SEEDS, new Compostable(0.3f, true), false);
    builder9.add(ModItems.MOONGLOW_SEEDS, new Compostable(0.3f, true), false);
    builder9.add(ModItems.WILDEWHEET_SEEDS, new Compostable(0.3f, true), false);
    builder9.add(ModItems.GROVE_SPORES, new Compostable(0.3f, true), false);

    builder9.add(ModItems.COOKED_PERESKIA, new Compostable(0.3f, false), false);
    builder9.add(ModItems.ASSORTED_SEEDS, new Compostable(0.3f, false), false);
    builder9.add(ModItems.COOKED_SEEDS, new Compostable(0.3f, false), false);
    builder9.add(ModItems.AUBERGINE, new Compostable(0.65f, true), false);
    builder9.add(ModItems.STUFFED_AUBERGINE, new Compostable(1f, false), false);
    builder9.add(ModItems.COOKED_AUBERGINE, new Compostable(0.95f, true), false);
    builder9.add(ModItems.COOKED_BEETROOT, new Compostable(0.65f, true), false);
    builder9.add(ModItems.COOKED_CARROT, new Compostable(0.65f, true), false);
    builder9.add(ModItems.FLOUR, new Compostable(0.65f, true), false);
    builder9.add(ModItems.WILDEWHEET_BREAD, new Compostable(1f, true), false);

    builder9.add(ModItems.WILDROOT, new Compostable(0.4f, true), false);
    builder9.add(ModItems.CLOUD_BERRY, new Compostable(0.9f, true), false);
    builder9.add(ModItems.INFERNO_BULB, new Compostable(0.9f, true), false);
    builder9.add(ModItems.STALICRIPE, new Compostable(0.9f, true), false);
    builder9.add(ModItems.DEWGONIA, new Compostable(0.9f, true), false);
    builder9.add(ModItems.GROVE_MOSS, new Compostable(0.4f, true), false);
    builder9.add(ModItems.BAFFLECAP, new Compostable(0.9f, true), false);
    builder9.add(ModItems.SPIRITLEAF, new Compostable(0.9f, true), false);
    builder9.add(ModItems.MOONGLOW, new Compostable(0.9f, true), false);
    builder9.add(ModItems.WILDEWHEET, new Compostable(0.9f, true), false);
    builder9.add(ModItems.PERESKIA, new Compostable(0.9f, true), false);

    // TODO: Burnable barks, should exclude crimson/warped
    builder9.add(RootsTags.Items.BARKS, new Compostable(0.3f, false), false);

    builder9.add(ModItems.PETALS, new Compostable(0.65f, true), false);
    builder9.add(ModItems.STONEPETAL, new Compostable(0.65f, false), false);

    Builder<FurnaceFuel, Item> builder10 = builder(NeoForgeDataMaps.FURNACE_FUELS).replace(false);
    builder10.add(ModItems.THATCH, new FurnaceFuel(300), false);
    builder10.add(ModItems.WILDWOOD_PEDESTAL, new FurnaceFuel(300), false);
    builder10.add(ModItems.GROVE_PEDESTAL, new FurnaceFuel(300), false);
    builder10.add(ModItems.DISPLAY_PEDESTAL, new FurnaceFuel(300), false);
    builder10.add(ModItems.INFERNO_BULB, new FurnaceFuel(600), false);
    builder10.add(ModItems.MAGMATIC_SOIL, new FurnaceFuel(900), false);

    Builder<Item, Ritual> builder11 = builder(DataMaps.RITUAL_DISPLAY_ITEM).replace(false);
    Builder<Item, Spell> builder12 = builder(DataMaps.SPELL_DISPLAY_ITEM).replace(false);

    BuiltInRegistries.ITEM.entrySet().forEach(o -> {
      if (o.getKey().location().getNamespace().equals(RootsAPI.MODID)) {
        if (o.getKey().location().getPath().startsWith("ritual_") && o.getValue() instanceof TokenItem.RitualTokenItem ritual) {
          builder11.add(ritual.getRitual().builtInRegistryHolder(), o.getValue(), false);
        } else if (o.getKey().location().getPath().startsWith("spell_") && o.getValue() instanceof TokenItem.SpellTokenItem spell) {
          builder12.add(spell.getSpell().builtInRegistryHolder(), o.getValue(), false);
        }
      }
    });

  }
}
