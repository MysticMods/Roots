package mysticmods.roots.api.datamap;

import com.mojang.serialization.Codec;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.action.GroveAction;
import mysticmods.roots.api.action.GroveReputationEntry;
import mysticmods.roots.api.grove.Grove;
import mysticmods.roots.api.grove.GrovePowerGenerator;
import mysticmods.roots.api.grove.ReputationRanks;
import mysticmods.roots.api.herb.Cost;
import mysticmods.roots.api.herb.CostInstance;
import mysticmods.roots.api.herb.Herb;
import mysticmods.roots.api.registry.RootsRegistries;
import mysticmods.roots.api.ritual.Ritual;
import mysticmods.roots.api.ritual.RitualModifier;
import mysticmods.roots.api.spell.Spell;
import mysticmods.roots.api.spell.SpellModifier;
import mysticmods.roots.growth.GrowthRecord;
import mysticmods.roots.growth.HarvestRecord;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.neoforged.neoforge.registries.datamaps.AdvancedDataMapType;
import net.neoforged.neoforge.registries.datamaps.DataMapType;
import net.neoforged.neoforge.registries.datamaps.DataMapValueMerger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataMaps {
  public static final Map<ResourceKey<Level>,ItemStack> DIMENSION_LOOKUP = new HashMap<>();

  public static final DataMapType<Block, Block> STEM_BLOCKS = DataMapType.builder(RootsAPI.rl("stem_blocks"), Registries.BLOCK, BuiltInRegistries.BLOCK.byNameCodec())
      .synced(BuiltInRegistries.BLOCK.byNameCodec(), true)
      .build();
  public static final DataMapType<SpellModifier, SpellModifier> SPELL_MODIFIER_PARENT = DataMapType.builder(RootsAPI.rl("spell_modifier_parent"), RootsRegistries.Keys.SPELL_MODIFIERS, RootsRegistries.SPELL_MODIFIERS.byNameCodec())
      .synced(RootsRegistries.SPELL_MODIFIERS.byNameCodec(), true)
      .build();
  public static final DataMapType<SpellModifier, Spell> SPELL_MODIFIER_SPELL = DataMapType.builder(RootsAPI.rl("spell_modifier_spell"), RootsRegistries.Keys.SPELL_MODIFIERS, RootsRegistries.SPELLS.byNameCodec())
      .synced(RootsRegistries.SPELLS.byNameCodec(), true)
      .build();
  public static final DataMapType<RitualModifier, Ritual> RITUAL_MODIFIER_RITUAL = DataMapType.builder(RootsAPI.rl("ritual_modifier_ritual"), RootsRegistries.Keys.RITUAL_MODIFIERS, RootsRegistries.RITUALS.byNameCodec())
      .synced(RootsRegistries.RITUALS.byNameCodec(), true).build();
  public static final AdvancedDataMapType<Spell, CostInstance, CostRemover<Spell>> SPELL_COST_DATA = AdvancedDataMapType.builder(RootsAPI.rl("spell_cost_data"), RootsRegistries.Keys.SPELLS, CostRemover.CODEC)
      .synced(CostRemover.CODEC, true)
      .merger(costMerger())
      .remover(CostRemover.codec())
      .build();
  public static final AdvancedDataMapType<SpellModifier, CostInstance, CostRemover<SpellModifier>> SPELL_MODIFIER_COST_DATA = AdvancedDataMapType.builder(RootsAPI.rl("spell_modifier_cost_data"), RootsRegistries.Keys.SPELL_MODIFIERS, CostRemover.CODEC)
      .synced(CostRemover.CODEC, true)
      .merger(costMerger())
      .remover(CostRemover.codec())
      .build();
  public static final AdvancedDataMapType<RitualModifier, CostInstance, CostRemover<RitualModifier>> RITUAL_MODIFIER_COST_DATA = AdvancedDataMapType.builder(RootsAPI.rl("ritual_modifier_cost_data"), RootsRegistries.Keys.RITUAL_MODIFIERS, CostRemover.CODEC)
      .synced(CostRemover.CODEC, true)
      .merger(costMerger())
      .remover(CostRemover.codec())
      .build();
  public static final DataMapType<Item, Herb> HERB_ITEM_DATA = DataMapType.builder(RootsAPI.rl("herb_item_data"), Registries.ITEM, RootsRegistries.HERBS.byNameCodec())
      .synced(RootsRegistries.HERBS.byNameCodec(), true)
      .build();
  public static final DataMapType<Spell, PropertyDataMap> SPELL_PROPERTY_DATA = DataMapType.builder(RootsAPI.rl("spell_property_data"), RootsRegistries.Keys.SPELLS, PropertyDataMap.CODEC)
      .synced(PropertyDataMap.CODEC, true)
      .build();
  public static final DataMapType<Ritual, PropertyDataMap> RITUAL_PROPERTY_DATA = DataMapType.builder(RootsAPI.rl("ritual_property_data"), RootsRegistries.Keys.RITUALS, PropertyDataMap.CODEC)
      .synced(PropertyDataMap.CODEC, true)
      .build();
  public static final DataMapType<Item, List<SproutGift>> SPROUT_BREEDING_ITEM_CHANCE = DataMapType.builder(RootsAPI.rl("sprout_breeding_item_chance"), Registries.ITEM, SproutGift.LIST_CODEC)
      .synced(SproutGift.LIST_CODEC, true)
      .build();
  public static final DataMapType<Block, GrowthRecord> GROWTH_RECORDS = DataMapType.builder(RootsAPI.rl("growth_records"), Registries.BLOCK, GrowthRecord.CODEC)
      .synced(GrowthRecord.CODEC, true)
      .build();
  public static final DataMapType<Block, HarvestRecord> HARVEST_RECORDS = DataMapType.builder(RootsAPI.rl("harvest_records"), Registries.BLOCK, HarvestRecord.CODEC)
      .synced(HarvestRecord.CODEC, true)
      .build();
  public static final DataMapType<GroveAction, List<GroveReputationEntry>> GROVE_ACTION_REPUTATIONS = DataMapType.builder(RootsAPI.rl("grove_action_reputations"), RootsRegistries.Keys.GROVE_ACTIONS, GroveReputationEntry.LIST_CODEC)
      .synced(GroveReputationEntry.LIST_CODEC, true)
      .build();
  public static final DataMapType<Grove, ReputationRanks> GROVE_RANKS = DataMapType.builder(RootsAPI.rl("grove_ranks"), RootsRegistries.Keys.GROVES, ReputationRanks.CODEC)
      .synced(ReputationRanks.CODEC, true)
      .build();
  public static final DataMapType<Block, Integer> OPERATION_COST = DataMapType.builder(RootsAPI.rl("shatter_operation_cost"), Registries.BLOCK, Codec.INT)
      .synced(Codec.INT, true)
      .build();
  public static final DataMapType<Item, Block> HARVEST_SEED_TO_CROP = DataMapType.builder(RootsAPI.rl("harvest_seed_to_crop"), Registries.ITEM, BuiltInRegistries.BLOCK.byNameCodec())
      .synced(BuiltInRegistries.BLOCK.byNameCodec(), true)
      .build();
  public static final DataMapType<Item, Block> GROWTH_SEED_TO_CROP = DataMapType.builder(RootsAPI.rl("growth_seed_to_crop"), Registries.ITEM, BuiltInRegistries.BLOCK.byNameCodec())
      .synced(BuiltInRegistries.BLOCK.byNameCodec(), true)
      .build();
  public static final DataMapType<Block, List<GrovePowerGenerator.Generator>> GROVE_POWER_GENERATORS = DataMapType.builder(RootsAPI.rl("grove_power_generator"), Registries.BLOCK, GrovePowerGenerator.Generator.LIST_CODEC)
      .synced(GrovePowerGenerator.Generator.LIST_CODEC, true).build();
  public static final DataMapType<Grove, List<GrovePowerGenerator.GenerationEntry>> GROVE_GENERATION_ENTRIES = DataMapType.builder(RootsAPI.rl("grove_generation_entries"), RootsRegistries.Keys.GROVES, GrovePowerGenerator.GenerationEntry.LIST_CODEC)
      .synced(GrovePowerGenerator.GenerationEntry.LIST_CODEC, true).build();
  public static final DataMapType<EntityType<?>, List<ResourceKey<LootTable>>> ADDITIONAL_ANIMAL_HARVEST_LOOT_TABLES = DataMapType.builder(RootsAPI.rl("additional_animal_harvest_loot_tables"), Registries.ENTITY_TYPE, ResourceKey.codec(Registries.LOOT_TABLE)
          .listOf())
      .synced(ResourceKey.codec(Registries.LOOT_TABLE).listOf(), true)
      .build();
  public static final DataMapType<GroveAction, Item> GROVE_ACTION_ICONS = DataMapType.builder(RootsAPI.rl("grove_action_icons"), RootsRegistries.Keys.GROVE_ACTIONS, BuiltInRegistries.ITEM.byNameCodec())
      .synced(BuiltInRegistries.ITEM.byNameCodec(), true)
      .build();
  public static final DataMapType<Attribute, AugmentationData> AUGMENTATION_DATA = DataMapType.builder(RootsAPI.rl("augmentation_data"), Registries.ATTRIBUTE, AugmentationData.CODEC)
      .synced(AugmentationData.CODEC, true)
      .build();
  public static final DataMapType<Block, Item> EXTRA_CROP_DATA = DataMapType.builder(RootsAPI.rl("extra_crop_data"), Registries.BLOCK, BuiltInRegistries.ITEM.byNameCodec())
      .synced(BuiltInRegistries.ITEM.byNameCodec(), true)
      .build();
  public static final DataMapType<Block, Float> EXTRA_CROP_CHANCE = DataMapType.builder(RootsAPI.rl("extra_crop_chance"), Registries.BLOCK, Codec.FLOAT)
      .synced(Codec.FLOAT, true)
      .build();
  public static final DataMapType<Item, List<ResourceKey<Level>>> DIMENSION_ITEM = DataMapType.builder(RootsAPI.rl("dimension_item"), Registries.ITEM, ResourceKey.codec(Registries.DIMENSION)
          .listOf())
      .synced(ResourceKey.codec(Registries.DIMENSION).listOf(), true)
      .build();

  public static ItemStack getDimensionItem (ResourceKey<Level> dimension) {
    if (DIMENSION_LOOKUP.isEmpty()) {
      BuiltInRegistries.ITEM.holders().forEach(o -> {
        List<ResourceKey<Level>> keys = o.getData(DataMaps.DIMENSION_ITEM);
        if (keys != null) {
          for (ResourceKey<Level> key : keys) {
            if (DIMENSION_LOOKUP.containsKey(key)) {
              RootsAPI.LOG.warn("Multiple items registered for dimension {}: {} and {}", key, DIMENSION_LOOKUP.get(key), o.value());
            } else {
              DIMENSION_LOOKUP.put(key, new ItemStack(o.value()));
            }
          }
        }
      });
    }

    return DIMENSION_LOOKUP.getOrDefault(dimension, ItemStack.EMPTY);
  }

  // Additional data maps need to be added to the register event in `DataEventHandler`

  static <R> DataMapValueMerger<R, CostInstance> costMerger() {
    return (registry, first, firstValue, second, secondValue) -> {
      final List<Cost> list = new ArrayList<>(firstValue.costs());
      list.addAll(secondValue.costs());
      return CostInstance.of(firstValue.chargeType(), list);
    };
  }
}
