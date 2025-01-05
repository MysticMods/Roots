package mysticmods.roots.api.data;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.herb.Herb;
import mysticmods.roots.api.modifier.SpellModifier;
import mysticmods.roots.api.registry.RootsRegistries;
import mysticmods.roots.api.spell.Spell;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.datamaps.AdvancedDataMapType;
import net.neoforged.neoforge.registries.datamaps.DataMapType;
import net.neoforged.neoforge.registries.datamaps.RegisterDataMapTypesEvent;

@EventBusSubscriber(modid = RootsAPI.MODID)
public class DataMaps {
  public static final AdvancedDataMapType<Spell, CostDataMap.CostData, CostDataMap.SpellCostRemover> SPELL_COST_DATA = AdvancedDataMapType.builder(RootsAPI.rl("spell_cost_data"), RootsRegistries.Keys.SPELLS, CostDataMap.CostData.CODEC)
      .merger(CostDataMap.listMerger())
      .remover(CostDataMap.SpellCostRemover.CODEC)
      .build();
  public static final AdvancedDataMapType<SpellModifier, CostDataMap.CostData, CostDataMap.SpellModifierCostRemover> SPELL_MODIFIER_COST_DATA = AdvancedDataMapType.builder(RootsAPI.rl("spell_modifier_cost_data"), RootsRegistries.Keys.SPELL_MODIFIERS, CostDataMap.CostData.CODEC)
      .merger(CostDataMap.listMerger())
      .remover(CostDataMap.SpellModifierCostRemover.CODEC)
      .build();
  public static final DataMapType<Item, Herb> HERB_ITEM_DATA = DataMapType.builder(RootsAPI.rl("herb_item_data"), Registries.ITEM, RootsRegistries.HERBS.byNameCodec())
      .build();

  @SubscribeEvent
  public static void registerDataMaps (RegisterDataMapTypesEvent event) {
    event.register(SPELL_COST_DATA);
    event.register(SPELL_MODIFIER_COST_DATA);
    event.register(HERB_ITEM_DATA);
  }
}
