package mysticmods.roots.api.datamap;

import com.mojang.serialization.Codec;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.condition.CanonicalRepresentation;
import mysticmods.roots.api.condition.LevelCondition;
import mysticmods.roots.api.condition.PlayerCondition;
import mysticmods.roots.api.grove.Grove;
import mysticmods.roots.api.herb.Cost;
import mysticmods.roots.api.herb.CostInstance;
import mysticmods.roots.api.herb.Herb;
import mysticmods.roots.api.registry.RootsRegistries;
import mysticmods.roots.api.ritual.Ritual;
import mysticmods.roots.api.spell.Spell;
import mysticmods.roots.api.spell.SpellModifier;
import mysticmods.roots.growth.GrowthRecord;
import mysticmods.roots.growth.HarvestRecord;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.datamaps.AdvancedDataMapType;
import net.neoforged.neoforge.registries.datamaps.DataMapType;
import net.neoforged.neoforge.registries.datamaps.DataMapValueMerger;
import net.neoforged.neoforge.registries.datamaps.RegisterDataMapTypesEvent;

import java.util.ArrayList;
import java.util.List;

@EventBusSubscriber(modid = RootsAPI.MODID, bus = EventBusSubscriber.Bus.MOD)
public class DataMaps {
  public static final DataMapType<Block, Block> STEM_BLOCKS = DataMapType.builder(RootsAPI.rl("stem_blocks"), Registries.BLOCK, BuiltInRegistries.BLOCK.byNameCodec())
      .synced(BuiltInRegistries.BLOCK.byNameCodec(), false)
      .build();
  public static final DataMapType<Ritual, ItemStack> RITUAL_DISPLAY_ITEM = DataMapType.builder(RootsAPI.rl("ritual_item_data"), RootsRegistries.Keys.RITUALS, ItemStack.CODEC)
      .synced(ItemStack.CODEC, false)
      .build();
  public static final DataMapType<Spell, ItemStack> SPELL_DISPLAY_ITEM = DataMapType.builder(RootsAPI.rl("spell_item_data"), RootsRegistries.Keys.SPELLS, ItemStack.CODEC)
      .synced(ItemStack.CODEC, false)
      .build();
  public static final DataMapType<SpellModifier, SpellModifier> SPELL_MODIFIER_PARENT = DataMapType.builder(RootsAPI.rl("spell_modifier_parent"), RootsRegistries.Keys.SPELL_MODIFIERS, RootsRegistries.SPELL_MODIFIERS.byNameCodec())
      .synced(RootsRegistries.SPELL_MODIFIERS.byNameCodec(), false)
      .build();
  public static final DataMapType<SpellModifier, Spell> SPELL_MODIFIER_SPELL = DataMapType.builder(RootsAPI.rl("spell_modifier_spell"), RootsRegistries.Keys.SPELL_MODIFIERS, RootsRegistries.SPELLS.byNameCodec())
      .synced(RootsRegistries.SPELLS.byNameCodec(), false)
      .build();
  public static final DataMapType<Grove, GroveData> GROVE_DATA = DataMapType.builder(RootsAPI.rl("grove_data"), RootsRegistries.Keys.GROVES, GroveData.CODEC)
      .synced(GroveData.CODEC, false)
      .build();
  public static final AdvancedDataMapType<Spell, CostInstance, CostRemover<Spell>> SPELL_COST_DATA = AdvancedDataMapType.builder(RootsAPI.rl("spell_cost_data"), RootsRegistries.Keys.SPELLS, CostRemover.CODEC)
      .synced(CostRemover.CODEC, false)
      .merger(costMerger())
      .remover(CostRemover.codec())
      .build();
  public static final AdvancedDataMapType<SpellModifier, CostInstance, CostRemover<SpellModifier>> SPELL_MODIFIER_COST_DATA = AdvancedDataMapType.builder(RootsAPI.rl("spell_modifier_cost_data"), RootsRegistries.Keys.SPELL_MODIFIERS, CostRemover.CODEC)
      .synced(CostRemover.CODEC, false)
      .merger(costMerger())
      .remover(CostRemover.codec())
      .build();
  public static final DataMapType<Item, Herb> HERB_ITEM_DATA = DataMapType.builder(RootsAPI.rl("herb_item_data"), Registries.ITEM, RootsRegistries.HERBS.byNameCodec())
      .synced(RootsRegistries.HERBS.byNameCodec(), false)
      .build();
  public static final DataMapType<Spell, PropertyDataMap> SPELL_PROPERTY_DATA = DataMapType.builder(RootsAPI.rl("spell_property_data"), RootsRegistries.Keys.SPELLS, PropertyDataMap.CODEC)
      .synced(PropertyDataMap.CODEC, false)
      .build();
  public static final DataMapType<Ritual, PropertyDataMap> RITUAL_PROPERTY_DATA = DataMapType.builder(RootsAPI.rl("ritual_property_data"), RootsRegistries.Keys.RITUALS, PropertyDataMap.CODEC)
      .synced(PropertyDataMap.CODEC, false)
      .build();
  public static final DataMapType<LevelCondition, CanonicalRepresentation> LEVEL_CONDITION_CANONS = DataMapType.builder(RootsAPI.rl("level_condition_canons"), RootsRegistries.Keys.LEVEL_CONDITIONS, CanonicalRepresentation.CODEC)
      .synced(CanonicalRepresentation.CODEC, false)
      .build();
  public static final DataMapType<PlayerCondition, CanonicalRepresentation> PLAYER_CONDITION_CANONS = DataMapType.builder(RootsAPI.rl("player_condition_canons"), RootsRegistries.Keys.PLAYER_CONDITIONS, CanonicalRepresentation.CODEC)
      .synced(CanonicalRepresentation.CODEC, false)
      .build();
  public static final DataMapType<Item, Integer> SPROUT_BREEDING_ITEM_CHANCE = DataMapType.builder(RootsAPI.rl("sprout_breeding_item_chance"), Registries.ITEM, Codec.INT)
      .synced(Codec.INT, false)
      .build();
  public static final DataMapType<Block, GrowthRecord> GROWTH_RECORDS = DataMapType.builder(RootsAPI.rl("growth_records"), Registries.BLOCK, GrowthRecord.CODEC)
      .synced(GrowthRecord.CODEC, false)
      .build();
  public static final DataMapType<Block, HarvestRecord> HARVEST_RECORDS = DataMapType.builder(RootsAPI.rl("harvest_records"), Registries.BLOCK, HarvestRecord.CODEC)
      .synced(HarvestRecord.CODEC, false)
      .build();

  @SubscribeEvent
  public static void registerDataMaps(RegisterDataMapTypesEvent event) {
    event.register(SPELL_COST_DATA);
    event.register(SPELL_MODIFIER_COST_DATA);
    event.register(HERB_ITEM_DATA);
    event.register(SPELL_PROPERTY_DATA);
    event.register(RITUAL_PROPERTY_DATA);
    event.register(RITUAL_DISPLAY_ITEM);
    event.register(SPELL_DISPLAY_ITEM);
    event.register(SPELL_MODIFIER_PARENT);
    event.register(SPELL_MODIFIER_SPELL);
    event.register(GROVE_DATA);
    event.register(LEVEL_CONDITION_CANONS);
    event.register(PLAYER_CONDITION_CANONS);
    event.register(SPROUT_BREEDING_ITEM_CHANCE);
    event.register(GROWTH_RECORDS);
    event.register(HARVEST_RECORDS);
  }

  static <R> DataMapValueMerger<R, CostInstance> costMerger() {
    return (registry, first, firstValue, second, secondValue) -> {
      final List<Cost> list = new ArrayList<>(firstValue.costs());
      list.addAll(secondValue.costs());
      return CostInstance.of(firstValue.chargeType(), list);
    };
  }
}
