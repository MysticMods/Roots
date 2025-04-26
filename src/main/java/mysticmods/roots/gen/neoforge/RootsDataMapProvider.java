package mysticmods.roots.gen.neoforge;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.action.GroveReputation;
import mysticmods.roots.api.action.GroveReputationEntry;
import mysticmods.roots.api.condition.CanonicalRepresentation;
import mysticmods.roots.condition.GroveStoneCondition;
import mysticmods.roots.api.condition.ILevelCondition;
import mysticmods.roots.condition.PillarCondition;
import mysticmods.roots.api.datamap.DataMaps;
import mysticmods.roots.api.datamap.PropertyDataMap;
import mysticmods.roots.api.datamap.SproutGift;
import mysticmods.roots.api.grove.GrovePower;
import mysticmods.roots.api.herb.CostInstance;
import mysticmods.roots.api.herb.Herb;
import mysticmods.roots.api.registry.RootsRegistries;
import mysticmods.roots.api.ritual.Ritual;
import mysticmods.roots.api.ritual.RitualModifier;
import mysticmods.roots.api.spell.Spell;
import mysticmods.roots.api.spell.SpellModifier;
import mysticmods.roots.api.test.world.PartialBlockState;
import mysticmods.roots.block.crop.FourStageCropBlock;
import mysticmods.roots.block.crop.ThreeStageCropBlock;
import mysticmods.roots.growth.GrowthRecord;
import mysticmods.roots.growth.HarvestRecord;
import mysticmods.roots.init.*;
import mysticmods.roots.item.TokenItem;
import mysticmods.roots.mixin.accessor.AccessorMixinCropBlock;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.DataMapProvider;
import net.neoforged.neoforge.registries.datamaps.builtin.Compostable;
import net.neoforged.neoforge.registries.datamaps.builtin.FurnaceFuel;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@SuppressWarnings("deprecation")
public class RootsDataMapProvider extends DataMapProvider {
  public RootsDataMapProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
    super(packOutput, lookupProvider);
  }

  @Override
  protected void gather() {
    Builder<CostInstance, Spell> builder = builder(DataMaps.SPELL_COST_DATA)
        .replace(false);

    RootsRegistries.SPELLS.stream().forEach(spell -> {
      builder.add(spell.builtInRegistryHolder(), spell.getDefaultCosts(), false);
    });

    Builder<Herb, Item> builder2 = builder(DataMaps.HERB_ITEM_DATA)
        .replace(false);

    RootsRegistries.HERBS.stream().forEach(herb -> {
      builder2.add(herb.getItem(), herb, false);
    });

    Builder<CostInstance, SpellModifier> builder3 = builder(DataMaps.SPELL_MODIFIER_COST_DATA)
        .replace(false);

    RootsRegistries.SPELL_MODIFIERS.stream().forEach(modifier -> {
      builder3.add(modifier.builtInRegistryHolder(), modifier.getDefaultCosts(), false);
    });

    Builder<CostInstance, RitualModifier> builder18 = builder(DataMaps.RITUAL_MODIFIER_COST_DATA)
        .replace(false);

    RootsRegistries.RITUAL_MODIFIERS.stream().forEach(modifier -> {
      builder18.add(modifier.builtInRegistryHolder(), modifier.getDefaultCosts(), false);
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

    Builder<Spell, SpellModifier> builder7 = builder(DataMaps.SPELL_MODIFIER_SPELL).replace(false);
    RootsRegistries.SPELL_MODIFIERS.stream().forEach(modifier -> {
      builder7.add(modifier.builtInRegistryHolder(), modifier.getSpell().value(), false);
    });

    Builder<Ritual, RitualModifier> builder19 = builder(DataMaps.RITUAL_MODIFIER_RITUAL).replace(false);
    RootsRegistries.RITUAL_MODIFIERS.stream().forEach(modifier -> {
      builder19.add(modifier.builtInRegistryHolder(), modifier.getRitual().value(), false);
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
    builder9.add(ModItems.BAFFLECAP_BLOCK, new Compostable(0.5f, true), false);
    builder9.add(ModItems.SPIRITLEAF, new Compostable(0.9f, true), false);
    builder9.add(ModItems.MOONGLOW, new Compostable(0.9f, true), false);
    builder9.add(ModItems.WILDEWHEET, new Compostable(0.9f, true), false);
    builder9.add(ModItems.PERESKIA, new Compostable(0.9f, true), false);

    builder9.add(RootsTags.Items.BARKS, new Compostable(0.3f, false), false);

    builder9.add(ModItems.STONEPETAL, new Compostable(0.65f, false), false);

    // TODO: Recalculate all of these
    Builder<FurnaceFuel, Item> builder10 = builder(NeoForgeDataMaps.FURNACE_FUELS).replace(false);
    builder10.add(ModItems.THATCH, new FurnaceFuel(300), false);
    builder10.add(ModItems.WILDWOOD_PEDESTAL, new FurnaceFuel(300), false);
    builder10.add(ModItems.GROVE_PEDESTAL, new FurnaceFuel(300), false);
    builder10.add(ModItems.DISPLAY_PEDESTAL, new FurnaceFuel(300), false);
    builder10.add(ModItems.INFERNO_BULB, new FurnaceFuel(600), false);
    builder10.add(ModItems.MAGMATIC_SOIL, new FurnaceFuel(900), false);
    builder10.add(ModItems.WILDWOOD_PLANKS, new FurnaceFuel(300), false);
    builder10.add(ModItems.WILDWOOD_LOG, new FurnaceFuel(300), false);
    builder10.add(ModItems.WILDWOOD_WOOD, new FurnaceFuel(300), false);
    builder10.add(ModItems.STRIPPED_WILDWOOD_LOG, new FurnaceFuel(300), false);
    builder10.add(ModItems.STRIPPED_WILDWOOD_WOOD, new FurnaceFuel(300), false);
    builder10.add(ModItems.WILDWOOD_FENCE, new FurnaceFuel(300), false);
    builder10.add(ModItems.WILDWOOD_GATE, new FurnaceFuel(300), false);
    builder10.add(ModItems.WILDWOOD_PEDESTAL, new FurnaceFuel(100), false);
    builder10.add(ModItems.WILDWOOD_DOOR, new FurnaceFuel(200), false);
    builder10.add(ModItems.WILDWOOD_TRAPDOOR, new FurnaceFuel(300), false);
    builder10.add(ModItems.WILDWOOD_BUTTON, new FurnaceFuel(100), false);
    builder10.add(ModItems.WILDWOOD_STAIRS, new FurnaceFuel(300), false);
    builder10.add(ModItems.WILDWOOD_SLAB, new FurnaceFuel(150), false);
    builder10.add(ModItems.GROVE_PEDESTAL, new FurnaceFuel(100), false);
    builder10.add(ModItems.THATCH, new FurnaceFuel(100), false);
    builder10.add(ModItems.STAFF, new FurnaceFuel(900), false);
    builder10.add(ModItems.WOODEN_SHEARS, new FurnaceFuel(1500), false);
    builder10.add(ModItems.WILDWOOD_LADDER, new FurnaceFuel(300), false);
    builder10.add(ModItems.FIRE_STARTER, new FurnaceFuel(90), false);
    builder10.add(RootsTags.Items.RUNED_LOGS, new FurnaceFuel(300), false);
    builder10.add(RootsTags.Items.BARKS_THAT_BURN, new FurnaceFuel(100), false);

    Builder<ItemStack, Ritual> builder11 = builder(DataMaps.RITUAL_DISPLAY_ITEM).replace(false);
    Builder<ItemStack, Spell> builder12 = builder(DataMaps.SPELL_DISPLAY_ITEM).replace(false);

    BuiltInRegistries.ITEM.entrySet().forEach(o -> {
      if (o.getKey().location().getNamespace().equals(RootsAPI.MODID)) {
        if (o.getKey().location().getPath()
            .startsWith("ritual_") && o.getValue() instanceof TokenItem.RitualTokenItem ritual) {
          builder11.add(ritual.getRitual().builtInRegistryHolder(), new ItemStack(o.getValue()), false);
        } else if (o.getKey().location().getPath()
            .startsWith("spell_") && o.getValue() instanceof TokenItem.SpellTokenItem spell) {
          builder12.add(spell.getSpell().builtInRegistryHolder(), new ItemStack(o.getValue()), false);
        }
      }
    });

/*    Builder<CanonicalRepresentation, ILevelCondition> builder13 = builder(DataMaps.LEVEL_CONDITION_CANONS).replace(false);
    builder13.add(ModConditions.GROVE_STONE_ANY.getDelegate(), GroveStoneCondition.fromBlockState(ModBlocks.PRIMAL_GROVE_STONE.get()
        .defaultBlockState(), false, false), false);
    builder13.add(ModConditions.GROVE_STONE_ACTIVE.getDelegate(), GroveStoneCondition.fromBlockState(ModBlocks.PRIMAL_GROVE_STONE.get()
        .defaultBlockState(), true, false), false);
    builder13.add(ModConditions.PRIMAL_GROVE_STONE_ACTIVE.getDelegate(), GroveStoneCondition.fromBlockState(ModBlocks.PRIMAL_GROVE_STONE.get()
        .defaultBlockState(), true, false), false);
    builder13.add(ModConditions.PRIMAL_GROVE_STONE_INACTIVE.getDelegate(), GroveStoneCondition.fromBlockState(ModBlocks.PRIMAL_GROVE_STONE.get()
        .defaultBlockState(), false, true), false);
    builder13.add(ModConditions.PRIMAL_GROVE_STONE_ANY.getDelegate(), GroveStoneCondition.fromBlockState(ModBlocks.PRIMAL_GROVE_STONE.get()
        .defaultBlockState(), false, false), false);
    builder13.add(ModConditions.ACACIA_PILLAR_4_HIGH.getDelegate(), PillarCondition.fromStates(ModBlocks.RUNED_ACACIA_LOG.get()
        .defaultBlockState(), Blocks.ACACIA_LOG.defaultBlockState(), 4), false);
    builder13.add(ModConditions.ACACIA_PILLAR_3_HIGH.getDelegate(), PillarCondition.fromStates(ModBlocks.RUNED_ACACIA_LOG.get()
        .defaultBlockState(), Blocks.ACACIA_LOG.defaultBlockState(), 3), false);
    builder13.add(ModConditions.LOG_PILLAR_3_HIGH.getDelegate(), PillarCondition.fromStates(ModBlocks.RUNED_OAK_LOG.get()
        .defaultBlockState(), Blocks.OAK_LOG.defaultBlockState(), 3), false);
    builder13.add(ModConditions.LOG_PILLAR_4_HIGH.getDelegate(), PillarCondition.fromStates(ModBlocks.RUNED_OAK_LOG.get()
        .defaultBlockState(), Blocks.OAK_LOG.defaultBlockState(), 4), false);
    builder13.add(ModConditions.RUNE_PILLAR_3_HIGH.getDelegate(), PillarCondition.fromStates(ModBlocks.CHISELED_RUNESTONE.get()
        .defaultBlockState(), ModBlocks.RUNESTONE.get().defaultBlockState(), 3), false);
    builder13.add(ModConditions.RUNE_PILLAR_4_HIGH.getDelegate(), PillarCondition.fromStates(ModBlocks.CHISELED_RUNESTONE.get()
        .defaultBlockState(), ModBlocks.RUNESTONE.get().defaultBlockState(), 4), false);
    builder13.add(ModConditions.WILDWOOD_PILLAR_3_HIGH.getDelegate(), PillarCondition.fromStates(ModBlocks.RUNED_WILDWOOD_LOG.get()
        .defaultBlockState(), ModBlocks.WILDWOOD_LOG.get().defaultBlockState(), 3), false);
    builder13.add(ModConditions.WILDWOOD_PILLAR_4_HIGH.getDelegate(), PillarCondition.fromStates(ModBlocks.RUNED_WILDWOOD_LOG.get()
        .defaultBlockState(), ModBlocks.WILDWOOD_LOG.get().defaultBlockState(), 4), false);
    builder13.add(ModConditions.BIRCH_PILLAR_3_HIGH.getDelegate(), PillarCondition.fromStates(ModBlocks.RUNED_BIRCH_LOG.get()
        .defaultBlockState(), Blocks.BIRCH_LOG.defaultBlockState(), 3), false);
    builder13.add(ModConditions.BIRCH_PILLAR_4_HIGH.getDelegate(), PillarCondition.fromStates(ModBlocks.RUNED_BIRCH_LOG.get()
        .defaultBlockState(), Blocks.BIRCH_LOG.defaultBlockState(), 4), false);
    builder13.add(ModConditions.CRIMSON_PILLAR_3_HIGH.getDelegate(), PillarCondition.fromStates(ModBlocks.RUNED_CRIMSON_STEM.get()
        .defaultBlockState(), Blocks.CRIMSON_STEM.defaultBlockState(), 3), false);
    builder13.add(ModConditions.CRIMSON_PILLAR_4_HIGH.getDelegate(), PillarCondition.fromStates(ModBlocks.RUNED_CRIMSON_STEM.get()
        .defaultBlockState(), Blocks.CRIMSON_STEM.defaultBlockState(), 4), false);
    builder13.add(ModConditions.DARK_OAK_PILLAR_3_HIGH.getDelegate(), PillarCondition.fromStates(ModBlocks.RUNED_DARK_OAK_LOG.get()
        .defaultBlockState(), Blocks.DARK_OAK_LOG.defaultBlockState(), 3), false);
    builder13.add(ModConditions.DARK_OAK_PILLAR_4_HIGH.getDelegate(), PillarCondition.fromStates(ModBlocks.RUNED_DARK_OAK_LOG.get()
        .defaultBlockState(), Blocks.DARK_OAK_LOG.defaultBlockState(), 4), false);
    builder13.add(ModConditions.JUNGLE_PILLAR_3_HIGH.getDelegate(), PillarCondition.fromStates(ModBlocks.RUNED_JUNGLE_LOG.get()
        .defaultBlockState(), Blocks.JUNGLE_LOG.defaultBlockState(), 3), false);
    builder13.add(ModConditions.JUNGLE_PILLAR_4_HIGH.getDelegate(), PillarCondition.fromStates(ModBlocks.RUNED_JUNGLE_LOG.get()
        .defaultBlockState(), Blocks.JUNGLE_LOG.defaultBlockState(), 4), false);
    builder13.add(ModConditions.OAK_PILLAR_3_HIGH.getDelegate(), PillarCondition.fromStates(ModBlocks.RUNED_OAK_LOG.get()
        .defaultBlockState(), Blocks.OAK_LOG.defaultBlockState(), 3), false);
    builder13.add(ModConditions.OAK_PILLAR_4_HIGH.getDelegate(), PillarCondition.fromStates(ModBlocks.RUNED_OAK_LOG.get()
        .defaultBlockState(), Blocks.OAK_LOG.defaultBlockState(), 4), false);
    builder13.add(ModConditions.SPRUCE_PILLAR_3_HIGH.getDelegate(), PillarCondition.fromStates(ModBlocks.RUNED_SPRUCE_LOG.get()
        .defaultBlockState(), Blocks.SPRUCE_LOG.defaultBlockState(), 3), false);
    builder13.add(ModConditions.SPRUCE_PILLAR_4_HIGH.getDelegate(), PillarCondition.fromStates(ModBlocks.RUNED_SPRUCE_LOG.get()
        .defaultBlockState(), Blocks.SPRUCE_LOG.defaultBlockState(), 4), false);
    builder13.add(ModConditions.WARPED_PILLAR_3_HIGH.getDelegate(), PillarCondition.fromStates(ModBlocks.RUNED_WARPED_STEM.get()
        .defaultBlockState(), Blocks.WARPED_STEM.defaultBlockState(), 3), false);
    builder13.add(ModConditions.WARPED_PILLAR_4_HIGH.getDelegate(), PillarCondition.fromStates(ModBlocks.RUNED_WARPED_STEM.get()
        .defaultBlockState(), Blocks.WARPED_STEM.defaultBlockState(), 4), false);
    builder13.add(ModConditions.MATURE_WILDROOT_CROP.getDelegate(), new CanonicalRepresentation(new PartialBlockState(Blocks.FARMLAND), new PartialBlockState(ModBlocks.WILDROOT_CROP.get()
        .defaultBlockState().setValue(ThreeStageCropBlock.AGE, 3), ThreeStageCropBlock.AGE)), false);*/

    var builder14 = builder(DataMaps.SPROUT_BREEDING_ITEM_CHANCE);
    // Requires finding a world-generated crop
    builder14.add(Items.BEETROOT_SEEDS.builtInRegistryHolder(), List.of(
        new SproutGift(RootsTags.Entities.NORMAL_SPROUTS, 40)
    ), false);
    // Super-easy to find
    builder14.add(Items.WHEAT_SEEDS.builtInRegistryHolder(), List.of(
        new SproutGift(RootsTags.Entities.NORMAL_SPROUTS, 5)
    ), false);
    // Craftable from melons, requires finding a jungle
    builder14.add(Items.MELON_SEEDS.builtInRegistryHolder(), List.of(
        new SproutGift(RootsTags.Entities.NORMAL_SPROUTS, 20)
    ), false);
    // Requires finding a jungle
    builder14.add(Items.COCOA_BEANS.builtInRegistryHolder(), List.of(
        new SproutGift(RootsTags.Entities.NORMAL_SPROUTS, 40)
    ), false);
    // Craftable from pumpkins, easily found
    builder14.add(Items.PUMPKIN_SEEDS.builtInRegistryHolder(), List.of(
        new SproutGift(RootsTags.Entities.NORMAL_SPROUTS, 10)
    ), false);
    // Requires finding a potato crop or potato in a whatever
    builder14.add(Items.POTATO.builtInRegistryHolder(), List.of(
        new SproutGift(RootsTags.Entities.NORMAL_SPROUTS, 50)
    ), false);
    // Requires finding a carrot crop or a carrot in a whatever
    builder14.add(Items.CARROT.builtInRegistryHolder(), List.of(
        new SproutGift(RootsTags.Entities.NORMAL_SPROUTS, 40)
    ), false);

    // Requires specific crafting recipes
    SproutGift rootsHerbs = new SproutGift(RootsTags.Entities.SPROUTS, 40);

    builder14.add(ModItems.MOONGLOW_SEEDS, List.of(rootsHerbs), false);
    builder14.add(ModItems.SPIRITLEAF_SEEDS, List.of(rootsHerbs), false);
    builder14.add(ModItems.WILDEWHEET_SEEDS, List.of(rootsHerbs), false);
    builder14.add(ModItems.BAFFLECAP, List.of(rootsHerbs), false);

    // These should be exceptionally rare
    builder14.add(Items.TORCHFLOWER_SEEDS.builtInRegistryHolder(), List.of(
        new SproutGift(RootsTags.Entities.SPROUTS, 8)
    ), false);
    builder14.add(Items.PITCHER_POD.builtInRegistryHolder(), List.of(
        new SproutGift(RootsTags.Entities.SPROUTS, 8)
    ), false);

    // Nether-based plants
    List<SproutGift> nether = List.of(
        new SproutGift(RootsTags.Entities.NORMAL_SPROUTS, 10),
        new SproutGift(RootsTags.Entities.SPECIAL_SPROUTS, 30)
    );
    builder14.add(Items.CRIMSON_FUNGUS.builtInRegistryHolder(), nether, false);
    builder14.add(Items.WARPED_FUNGUS.builtInRegistryHolder(), nether, false);
    builder14.add(Items.NETHER_WART.builtInRegistryHolder(), nether, false);

    // Rarer plants
    builder14.add(Items.KELP.builtInRegistryHolder(), List.of(
        new SproutGift(RootsTags.Entities.NORMAL_SPROUTS, 20)
    ), false);

    // Super rare plants
    builder14.add(Items.CHORUS_FLOWER.builtInRegistryHolder(), List.of(
        new SproutGift(RootsTags.Entities.NORMAL_SPROUTS, 5),
        new SproutGift(RootsTags.Entities.SPECIAL_SPROUTS, 15)
    ), false);

    // Growth records!
    var builder15 = builder(DataMaps.GROWTH_RECORDS);
    var builder16 = builder(DataMaps.HARVEST_RECORDS);
    var builder17 = builder(DataMaps.STEM_BLOCKS);

    int AGE_SEVEN_TICKS = 2;
    int AGE_THREE_TICKS = 4;
    int SAPLING_TICKS = 4;

    // TODO: It does actually need to care about height
    builder15.add(Blocks.BAMBOO_SAPLING.builtInRegistryHolder(), new GrowthRecord(Blocks.BAMBOO_SAPLING, Optional.empty(), -1, SAPLING_TICKS, ModTests.ALWAYS_CAN_GROW_UP.get(), ModTests.LIGHT_ABOVE_EIGHT.get()), false);
    builder15.add(Blocks.BAMBOO.builtInRegistryHolder(), new GrowthRecord(Blocks.BAMBOO, Optional.empty(), 1, AGE_THREE_TICKS, ModTests.ALWAYS_CAN_GROW_UP.get(), ModTests.LIGHT_ABOVE_EIGHT.get()), false);
    builder16.add(Blocks.BAMBOO.builtInRegistryHolder(), new HarvestRecord(Blocks.BAMBOO, Optional.empty(), Optional.empty(), -1, ModTests.CAN_HARVEST_LOWEST.get(), ModTests.HARVEST_ALL_ABOVE_SAME_BLOCK.get()), false);

    builder15.add(Blocks.BEETROOTS.builtInRegistryHolder(), new GrowthRecord(Blocks.BEETROOTS, Optional.of(BeetrootBlock.AGE), BeetrootBlock.MAX_AGE, AGE_THREE_TICKS, ModTests.AGE_CAN_GROW.get(), ModTests.LIGHT_ABOVE_EIGHT.get()), false);
    builder16.add(Blocks.BEETROOTS.builtInRegistryHolder(), HarvestRecord.of((CropBlock) Blocks.BEETROOTS, ModTests.HARVEST_SINGLE_CROP_BLOCK.get()), false);

    builder15.add(Blocks.CARROTS.builtInRegistryHolder(), new GrowthRecord(Blocks.CARROTS, Optional.of(CarrotBlock.AGE), CarrotBlock.MAX_AGE, AGE_SEVEN_TICKS, ModTests.AGE_CAN_GROW.get(), ModTests.LIGHT_ABOVE_EIGHT.get()), false);
    builder16.add(Blocks.CARROTS.builtInRegistryHolder(), HarvestRecord.of((CropBlock) Blocks.CARROTS, ModTests.HARVEST_SINGLE_CROP_BLOCK.get()), false);

    builder15.add(Blocks.COCOA.builtInRegistryHolder(), new GrowthRecord(Blocks.COCOA, Optional.of(CocoaBlock.AGE), CocoaBlock.MAX_AGE, AGE_THREE_TICKS, ModTests.AGE_CAN_GROW.get(), ModTests.ANY_LIGHT.get()), false);
    builder16.add(Blocks.COCOA.builtInRegistryHolder(), new HarvestRecord(Blocks.COCOA, Optional.of(Items.COCOA_BEANS), Optional.of(CocoaBlock.AGE), CocoaBlock.MAX_AGE, ModTests.SINGLE_CROP_AGE.get(), ModTests.HARVEST_SINGLE_CROP_BLOCK.get()), false);

    // TODO: Only if there's spreadable locations
    builder15.add(RootsTags.Blocks.SPREADING_MUSHROOMS, new GrowthRecord(null, Optional.empty(), -1, 2, ModTests.ALWAYS_CAN_GROW.get(), ModTests.ANY_LIGHT.get()), false);

    builder15.add(Blocks.KELP.builtInRegistryHolder(), new GrowthRecord(Blocks.KELP, Optional.of(KelpBlock.AGE), KelpBlock.MAX_AGE, 1, ModTests.KELP_CAN_GROW.get(), ModTests.ANY_LIGHT.get()), false);
    builder16.add(Blocks.KELP_PLANT.builtInRegistryHolder(), new HarvestRecord(Blocks.KELP_PLANT, Optional.empty(), Optional.of(KelpBlock.AGE), KelpBlock.MAX_AGE, ModTests.CAN_HARVEST_GROWING_PLANT_BLOCK.get(), ModTests.HARVEST_GROWING_PLANT_BLOCK.get()), false);

    builder15.add(Blocks.PITCHER_CROP.builtInRegistryHolder(), new GrowthRecord(Blocks.PITCHER_CROP, Optional.of(PitcherCropBlock.AGE), PitcherCropBlock.MAX_AGE, AGE_THREE_TICKS, ModTests.AGE_CAN_GROW.get(), ModTests.LIGHT_ABOVE_EIGHT.get()), false);
    builder16.add(Blocks.PITCHER_CROP.builtInRegistryHolder(), new HarvestRecord(Blocks.PITCHER_CROP, Optional.empty(), Optional.of(PitcherCropBlock.AGE), PitcherCropBlock.MAX_AGE, ModTests.CAN_HARVEST_TWO_BLOCK_PLANT_AGE.get(), ModTests.HARVEST_CROP_AND_ABOVE.get()), false);

    builder15.add(Blocks.TORCHFLOWER_CROP.builtInRegistryHolder(), new GrowthRecord(Blocks.TORCHFLOWER_CROP, Optional.of(TorchflowerCropBlock.AGE), TorchflowerCropBlock.MAX_AGE, AGE_THREE_TICKS, ModTests.AGE_CAN_GROW.get(), ModTests.LIGHT_ABOVE_EIGHT.get()), false);
    builder16.add(Blocks.TORCHFLOWER.builtInRegistryHolder(), new HarvestRecord(Blocks.TORCHFLOWER, Optional.empty(), Optional.of(TorchflowerCropBlock.AGE), TorchflowerCropBlock.MAX_AGE, ModTests.CAN_SAFE_HARVEST_FARMLAND.get(), ModTests.HARVEST_SINGLE_CROP_BLOCK.get()), false);

    builder15.add(Blocks.POTATOES.builtInRegistryHolder(), new GrowthRecord(Blocks.POTATOES, Optional.of(PotatoBlock.AGE), PotatoBlock.MAX_AGE, AGE_SEVEN_TICKS, ModTests.AGE_CAN_GROW.get(), ModTests.LIGHT_ABOVE_EIGHT.get()), false);

    builder16.add(Blocks.POTATOES.builtInRegistryHolder(), HarvestRecord.of((CropBlock) Blocks.POTATOES, ModTests.HARVEST_SINGLE_CROP_BLOCK.get()), false);

    // TODO: Mangrove stuff
    builder15.add(BlockTags.SAPLINGS, new GrowthRecord(null, Optional.empty(), -1, SAPLING_TICKS, ModTests.ALWAYS_CAN_GROW.get(), ModTests.LIGHT_ABOVE_EIGHT.get()), false);

    builder17.add(Blocks.MELON.builtInRegistryHolder(), Blocks.ATTACHED_MELON_STEM, false);
    builder15.add(Blocks.MELON_STEM.builtInRegistryHolder(), new GrowthRecord(Blocks.MELON_STEM, Optional.of(StemBlock.AGE), StemBlock.MAX_AGE, AGE_SEVEN_TICKS, ModTests.STEM_BLOCK_CAN_GROW.get(), ModTests.LIGHT_ABOVE_EIGHT.get()), false);
    builder16.add(Blocks.MELON.builtInRegistryHolder(), new HarvestRecord(Blocks.MELON, Optional.empty(), Optional.empty(), -1, ModTests.CAN_HARVEST_STEM_BLOCK.get(), ModTests.HARVEST_BREAK_SINGLE_BLOCK.get()), false);

    builder15.add(Blocks.NETHER_WART.builtInRegistryHolder(), new GrowthRecord(Blocks.NETHER_WART, Optional.of(NetherWartBlock.AGE), NetherWartBlock.MAX_AGE, AGE_THREE_TICKS, ModTests.AGE_CAN_GROW.get(), ModTests.ANY_LIGHT.get()), false);
    builder16.add(Blocks.NETHER_WART.builtInRegistryHolder(), new HarvestRecord(Blocks.NETHER_WART, Optional.of(Items.NETHER_WART), Optional.of(NetherWartBlock.AGE), NetherWartBlock.MAX_AGE, ModTests.SINGLE_CROP_AGE.get(), ModTests.HARVEST_SINGLE_CROP_BLOCK.get()), false);

    builder17.add(Blocks.PUMPKIN.builtInRegistryHolder(), Blocks.ATTACHED_PUMPKIN_STEM, false);
    builder16.add(Blocks.PUMPKIN.builtInRegistryHolder(), new HarvestRecord(Blocks.PUMPKIN, Optional.empty(), Optional.empty(), -1, ModTests.CAN_HARVEST_STEM_BLOCK.get(), ModTests.HARVEST_BREAK_SINGLE_BLOCK.get()), false);
    builder15.add(Blocks.PUMPKIN_STEM.builtInRegistryHolder(), new GrowthRecord(Blocks.PUMPKIN_STEM, Optional.of(StemBlock.AGE), StemBlock.MAX_AGE, AGE_SEVEN_TICKS, ModTests.STEM_BLOCK_CAN_GROW.get(), ModTests.LIGHT_ABOVE_EIGHT.get()), false);

    builder15.add(Blocks.SWEET_BERRY_BUSH.builtInRegistryHolder(), new GrowthRecord(Blocks.SWEET_BERRY_BUSH, Optional.of(SweetBerryBushBlock.AGE), SweetBerryBushBlock.MAX_AGE, AGE_THREE_TICKS, ModTests.ALWAYS_CAN_GROW.get(), ModTests.LIGHT_ABOVE_EIGHT.get()), false);
    builder16.add(Blocks.SWEET_BERRY_BUSH.builtInRegistryHolder(), new HarvestRecord(Blocks.SWEET_BERRY_BUSH, Optional.of(Items.SWEET_BERRIES), Optional.of(SweetBerryBushBlock.AGE), SweetBerryBushBlock.MAX_AGE, ModTests.SINGLE_CROP_AGE.get(), ModTests.HARVEST_SWEET_BERRIES.get()), false);

    builder15.add(Blocks.TWISTING_VINES.builtInRegistryHolder(), new GrowthRecord(Blocks.TWISTING_VINES, Optional.of(TwistingVinesBlock.AGE), TwistingVinesBlock.MAX_AGE, 2, ModTests.AGE_CAN_GROW_UP.get(), ModTests.LIGHT_ABOVE_EIGHT.get()), false); // Harvest: all above but base
    builder16.add(Blocks.TWISTING_VINES_PLANT.builtInRegistryHolder(), new HarvestRecord(Blocks.TWISTING_VINES_PLANT, Optional.empty(), Optional.of(TwistingVinesBlock.AGE), TwistingVinesBlock.MAX_AGE, ModTests.CAN_HARVEST_GROWING_PLANT_BLOCK.get(), ModTests.HARVEST_GROWING_PLANT_BLOCK.get()), false);

    builder15.add(Blocks.WEEPING_VINES.builtInRegistryHolder(), new GrowthRecord(Blocks.WEEPING_VINES, Optional.of(WeepingVinesBlock.AGE), WeepingVinesBlock.MAX_AGE, 2, ModTests.AGE_CAN_GROW_DOWN.get(), ModTests.LIGHT_ABOVE_EIGHT.get()), false);
    builder16.add(Blocks.WEEPING_VINES_PLANT.builtInRegistryHolder(), new HarvestRecord(Blocks.WEEPING_VINES_PLANT, Optional.empty(), Optional.of(WeepingVinesBlock.AGE), WeepingVinesBlock.MAX_AGE, ModTests.CAN_HARVEST_GROWING_PLANT_BLOCK.get(), ModTests.HARVEST_GROWING_PLANT_BLOCK.get()), false);

    // TODO: Improve this spread check
    builder15.add(Blocks.VINE.builtInRegistryHolder(), new GrowthRecord(Blocks.VINE, Optional.empty(), -1, 2, ModTests.VINES_CAN_SPREAD.get(), ModTests.ANY_LIGHT.get()), false);

    builder15.add(Blocks.WHEAT.builtInRegistryHolder(), new GrowthRecord(Blocks.WHEAT, Optional.of(CropBlock.AGE), CropBlock.MAX_AGE, AGE_SEVEN_TICKS, ModTests.AGE_CAN_GROW.get(), ModTests.LIGHT_ABOVE_EIGHT.get()), false);
    builder16.add(Blocks.WHEAT.builtInRegistryHolder(), HarvestRecord.of((CropBlock) Blocks.WHEAT, ModTests.HARVEST_SINGLE_CROP_BLOCK.get()), false);

    builder15.add(Blocks.CACTUS.builtInRegistryHolder(), new GrowthRecord(Blocks.CACTUS, Optional.of(CactusBlock.AGE), CactusBlock.MAX_AGE, 2, ModTests.CACTUS_CANE_CAN_GROW.get(), ModTests.ANY_LIGHT.get()), false);
    builder16.add(Blocks.CACTUS.builtInRegistryHolder(), new HarvestRecord(Blocks.CACTUS, Optional.empty(), Optional.empty(), -1, ModTests.CAN_HARVEST_LOWEST.get(), ModTests.HARVEST_ALL_ABOVE_SAME_BLOCK.get()), false);

    builder15.add(Blocks.SUGAR_CANE.builtInRegistryHolder(), new GrowthRecord(Blocks.SUGAR_CANE, Optional.of(SugarCaneBlock.AGE), 15, 2, ModTests.CACTUS_CANE_CAN_GROW.get(), ModTests.ANY_LIGHT.get()), false);
    builder16.add(Blocks.SUGAR_CANE.builtInRegistryHolder(), new HarvestRecord(Blocks.SUGAR_CANE, Optional.empty(), Optional.empty(), -1, ModTests.CAN_HARVEST_LOWEST.get(), ModTests.HARVEST_ALL_ABOVE_SAME_BLOCK.get()), false);

    builder15.add(Blocks.CHORUS_FLOWER.builtInRegistryHolder(), new GrowthRecord(Blocks.CHORUS_FLOWER, Optional.of(ChorusFlowerBlock.AGE), ChorusFlowerBlock.DEAD_AGE, 2, ModTests.ALWAYS_CAN_GROW_UP.get(), ModTests.ANY_LIGHT.get()), false);

    builder16.add(Blocks.CAVE_VINES_PLANT.builtInRegistryHolder(), new HarvestRecord(Blocks.CAVE_VINES_PLANT, Optional.empty(), Optional.empty(), -1, ModTests.CAN_HARVEST_GLOW_BERRIES.get(), ModTests.HARVEST_GLOW_BERRIES.get()), false);

    builder15.add(ModBlocks.WILDROOT_CROP, new GrowthRecord(ModBlocks.WILDROOT_CROP.get(), Optional.of(ThreeStageCropBlock.AGE), ThreeStageCropBlock.MAX_AGE, AGE_THREE_TICKS, ModTests.AGE_CAN_GROW.get(), ModTests.LIGHT_ABOVE_EIGHT.get()), false);
    builder16.add(ModBlocks.WILDROOT_CROP, HarvestRecord.of(ModBlocks.WILDROOT_CROP.get(), ModTests.HARVEST_SINGLE_CROP_BLOCK.get()), false);

    builder15.add(ModBlocks.CLOUD_BERRY_CROP, new GrowthRecord(ModBlocks.CLOUD_BERRY_CROP.get(), Optional.of(ThreeStageCropBlock.AGE), ThreeStageCropBlock.MAX_AGE, AGE_THREE_TICKS, ModTests.AGE_CAN_GROW.get(), ModTests.ANY_LIGHT.get()), false);
    builder16.add(ModBlocks.CLOUD_BERRY_CROP, HarvestRecord.of(ModBlocks.CLOUD_BERRY_CROP.get(), ModTests.HARVEST_SINGLE_CROP_BLOCK.get()), false);

    builder15.add(ModBlocks.INFERNO_BULB_CROP, new GrowthRecord(ModBlocks.INFERNO_BULB_CROP.get(), Optional.of(ThreeStageCropBlock.AGE), ThreeStageCropBlock.MAX_AGE, AGE_THREE_TICKS, ModTests.AGE_CAN_GROW.get(), ModTests.ANY_LIGHT.get()), false);
    builder16.add(ModBlocks.INFERNO_BULB_CROP, HarvestRecord.of(ModBlocks.INFERNO_BULB_CROP.get(), ModTests.HARVEST_SINGLE_CROP_BLOCK.get()), false);

    builder15.add(ModBlocks.STALICRIPE_CROP, new GrowthRecord(ModBlocks.STALICRIPE_CROP.get(), Optional.of(ThreeStageCropBlock.AGE), ThreeStageCropBlock.MAX_AGE, AGE_THREE_TICKS, ModTests.AGE_CAN_GROW.get(), ModTests.ANY_LIGHT.get()), false);
    builder16.add(ModBlocks.STALICRIPE_CROP, HarvestRecord.of(ModBlocks.STALICRIPE_CROP.get(), ModTests.HARVEST_SINGLE_CROP_BLOCK.get()), false);

    builder15.add(ModBlocks.DEWGONIA_CROP, new GrowthRecord(ModBlocks.DEWGONIA_CROP.get(), Optional.of(ThreeStageCropBlock.AGE), ThreeStageCropBlock.MAX_AGE, AGE_THREE_TICKS, ModTests.AGE_CAN_GROW.get(), ModTests.ANY_LIGHT.get()), false);
    builder16.add(ModBlocks.DEWGONIA_CROP, HarvestRecord.of(ModBlocks.DEWGONIA_CROP.get(), ModTests.HARVEST_SINGLE_CROP_BLOCK.get()), false);

    builder15.add(ModBlocks.MOONGLOW_CROP, new GrowthRecord(ModBlocks.MOONGLOW_CROP.get(), Optional.of(CropBlock.AGE), CropBlock.MAX_AGE, AGE_SEVEN_TICKS, ModTests.AGE_CAN_GROW.get(), ModTests.LIGHT_ABOVE_EIGHT.get()), false);
    builder16.add(ModBlocks.MOONGLOW_CROP, HarvestRecord.of(ModBlocks.MOONGLOW_CROP.get(), ModTests.HARVEST_SINGLE_CROP_BLOCK.get()), false);

    builder15.add(ModBlocks.PERESKIA_CROP, new GrowthRecord(ModBlocks.PERESKIA_CROP.get(), Optional.of(CropBlock.AGE), CropBlock.MAX_AGE, AGE_SEVEN_TICKS, ModTests.AGE_CAN_GROW.get(), ModTests.LIGHT_ABOVE_EIGHT.get()), false);
    builder16.add(ModBlocks.PERESKIA_CROP, HarvestRecord.of(ModBlocks.PERESKIA_CROP.get(), ModTests.HARVEST_SINGLE_CROP_BLOCK.get()), false);

    builder15.add(ModBlocks.SPIRITLEAF_CROP, new GrowthRecord(ModBlocks.SPIRITLEAF_CROP.get(), Optional.of(FourStageCropBlock.AGE), FourStageCropBlock.MAX_AGE, AGE_SEVEN_TICKS, ModTests.AGE_CAN_GROW.get(), ModTests.LIGHT_ABOVE_EIGHT.get()), false);
    builder16.add(ModBlocks.SPIRITLEAF_CROP, HarvestRecord.of(ModBlocks.SPIRITLEAF_CROP.get(), ModTests.HARVEST_SINGLE_CROP_BLOCK.get()), false);

    builder15.add(ModBlocks.WILDEWHEET_CROP, new GrowthRecord(ModBlocks.WILDEWHEET_CROP.get(), Optional.of(CropBlock.AGE), CropBlock.MAX_AGE, AGE_SEVEN_TICKS, ModTests.AGE_CAN_GROW.get(), ModTests.LIGHT_ABOVE_EIGHT.get()), false);
    builder16.add(ModBlocks.WILDEWHEET_CROP, HarvestRecord.of(ModBlocks.WILDEWHEET_CROP.get(), ModTests.HARVEST_SINGLE_CROP_BLOCK.get()), false);

    builder15.add(ModBlocks.AUBERGINE_CROP, new GrowthRecord(ModBlocks.AUBERGINE_CROP.get(), Optional.of(CropBlock.AGE), CropBlock.MAX_AGE, AGE_SEVEN_TICKS, ModTests.AGE_CAN_GROW.get(), ModTests.LIGHT_ABOVE_EIGHT.get()), false);
    builder16.add(ModBlocks.AUBERGINE_CROP, HarvestRecord.of(ModBlocks.AUBERGINE_CROP.get(), ModTests.HARVEST_SINGLE_CROP_BLOCK.get()), false);

    var builder20 = builder(DataMaps.GROVE_ACTION_REPUTATIONS);
    builder20.add(ModActions.CROP_GROWTH, List.of(
        new GroveReputationEntry(ModGroves.SPROUTING.value(), RootsAPI.rl("sprout_crop_growth"), new GroveReputation(100, 10, 1, 0), GroveReputationEntry.SubEntryType.BLOCK, RootsTags.Blocks.SPROUT_REPUTATION_CROPS),
        new GroveReputationEntry(ModGroves.ELEMENTAL.value(), RootsAPI.rl("elemental_crop_growth"), new GroveReputation(100, 10, 1, 0), GroveReputationEntry.SubEntryType.BLOCK, RootsTags.Blocks.ELEMENTAL_REPUTATION_CROPS)
    ), false);

    int first_spell_reward = 250;

    builder20.add(ModActions.LEARN_SPELL, List.of(
        new GroveReputationEntry(ModGroves.FAIRY.value(), RootsAPI.rl("learn_spell/sylvan_light"), new GroveReputation(first_spell_reward), true, List.of(new GroveReputationEntry.SubEntry(GroveReputationEntry.SubEntryType.EXACT_SPELL, ModSpells.SYLVAN_LIGHT.getId()))),
        new GroveReputationEntry(ModGroves.FAIRY.value(), RootsAPI.rl("learn_spell/petal_shell"), new GroveReputation(first_spell_reward), true, List.of(new GroveReputationEntry.SubEntry(GroveReputationEntry.SubEntryType.EXACT_SPELL, ModSpells.PETAL_SHELL.getId()))),
        new GroveReputationEntry(ModGroves.FAIRY.value(), RootsAPI.rl("learn_spell/rose_thorns"), new GroveReputation(first_spell_reward), true, List.of(new GroveReputationEntry.SubEntry(GroveReputationEntry.SubEntryType.EXACT_SPELL, ModSpells.ROSE_THORNS.getId()))),
        new GroveReputationEntry(ModGroves.FAIRY.value(), RootsAPI.rl("learn_spell/sanctuary"), new GroveReputation(first_spell_reward), true, List.of(new GroveReputationEntry.SubEntry(GroveReputationEntry.SubEntryType.EXACT_SPELL, ModSpells.SANCTUARY.getId()))),
        new GroveReputationEntry(ModGroves.FUNGAL.value(), RootsAPI.rl("learn_spell/acid_cloud"), new GroveReputation(first_spell_reward), true, List.of(new GroveReputationEntry.SubEntry(GroveReputationEntry.SubEntryType.EXACT_SPELL, ModSpells.ACID_CLOUD.getId()))),
        new GroveReputationEntry(ModGroves.FUNGAL.value(), RootsAPI.rl("learn_spell/disarm"), new GroveReputation(first_spell_reward), true, List.of(new GroveReputationEntry.SubEntry(GroveReputationEntry.SubEntryType.EXACT_SPELL, ModSpells.DISARM.getId()))),
        new GroveReputationEntry(ModGroves.FUNGAL.value(), RootsAPI.rl("learn_spell/geas"), new GroveReputation(first_spell_reward), true, List.of(new GroveReputationEntry.SubEntry(GroveReputationEntry.SubEntryType.EXACT_SPELL, ModSpells.GEAS.getId()))),
        new GroveReputationEntry(ModGroves.FUNGAL.value(), RootsAPI.rl("learn_spell/geas"), new GroveReputation(first_spell_reward), true, List.of(new GroveReputationEntry.SubEntry(GroveReputationEntry.SubEntryType.EXACT_SPELL, ModSpells.GEAS.getId()))),
        new GroveReputationEntry(ModGroves.FUNGAL.value(), RootsAPI.rl("learn_spell/summon_undead"), new GroveReputation(first_spell_reward), true, List.of(new GroveReputationEntry.SubEntry(GroveReputationEntry.SubEntryType.EXACT_SPELL, ModSpells.SUMMON_UNDEAD.getId()))),
        new GroveReputationEntry(ModGroves.ELEMENTAL.value(), RootsAPI.rl("learn_spell/aqua_bubble"), new GroveReputation(first_spell_reward), true, List.of(new GroveReputationEntry.SubEntry(GroveReputationEntry.SubEntryType.EXACT_SPELL, ModSpells.AQUA_BUBBLE.getId()))),
        new GroveReputationEntry(ModGroves.ELEMENTAL.value(), RootsAPI.rl("learn_spell/dandelion_winds"), new GroveReputation(first_spell_reward), true, List.of(new GroveReputationEntry.SubEntry(GroveReputationEntry.SubEntryType.EXACT_SPELL, ModSpells.DANDELION_WINDS.getId()))),
        new GroveReputationEntry(ModGroves.ELEMENTAL.value(), RootsAPI.rl("learn_spell/radiance"), new GroveReputation(first_spell_reward), true, List.of(new GroveReputationEntry.SubEntry(GroveReputationEntry.SubEntryType.EXACT_SPELL, ModSpells.RADIANCE.getId()))),
        new GroveReputationEntry(ModGroves.ELEMENTAL.value(), RootsAPI.rl("learn_spell/shatter"), new GroveReputation(first_spell_reward), true, List.of(new GroveReputationEntry.SubEntry(GroveReputationEntry.SubEntryType.EXACT_SPELL, ModSpells.SHATTER.getId()))),
        new GroveReputationEntry(ModGroves.ELEMENTAL.value(), RootsAPI.rl("learn_spell/storm_cloud"), new GroveReputation(first_spell_reward), true, List.of(new GroveReputationEntry.SubEntry(GroveReputationEntry.SubEntryType.EXACT_SPELL, ModSpells.STORM_CLOUD.getId()))),
        new GroveReputationEntry(ModGroves.ELEMENTAL.value(), RootsAPI.rl("learn_spell/sky_soarer"), new GroveReputation(first_spell_reward), true, List.of(new GroveReputationEntry.SubEntry(GroveReputationEntry.SubEntryType.EXACT_SPELL, ModSpells.SKY_SOARER.getId()))),
        new GroveReputationEntry(ModGroves.ELEMENTAL.value(), RootsAPI.rl("learn_spell/wildfire"), new GroveReputation(first_spell_reward), true, List.of(new GroveReputationEntry.SubEntry(GroveReputationEntry.SubEntryType.EXACT_SPELL, ModSpells.WILDFIRE.getId()))),
        new GroveReputationEntry(ModGroves.SPROUTING.value(), RootsAPI.rl("learn_spell/desaturate"), new GroveReputation(first_spell_reward), true, List.of(new GroveReputationEntry.SubEntry(GroveReputationEntry.SubEntryType.EXACT_SPELL, ModSpells.DESATURATE.getId()))),
        new GroveReputationEntry(ModGroves.SPROUTING.value(), RootsAPI.rl("learn_spell/saturate"), new GroveReputation(first_spell_reward), true, List.of(new GroveReputationEntry.SubEntry(GroveReputationEntry.SubEntryType.EXACT_SPELL, ModSpells.SATURATE.getId()))),
        new GroveReputationEntry(ModGroves.SPROUTING.value(), RootsAPI.rl("learn_spell/growth_infusion"), new GroveReputation(first_spell_reward), true, List.of(new GroveReputationEntry.SubEntry(GroveReputationEntry.SubEntryType.EXACT_SPELL, ModSpells.GROWTH_INFUSION.getId()))),
        new GroveReputationEntry(ModGroves.SPROUTING.value(), RootsAPI.rl("learn_spell/rampant_growth"), new GroveReputation(first_spell_reward), true, List.of(new GroveReputationEntry.SubEntry(GroveReputationEntry.SubEntryType.EXACT_SPELL, ModSpells.RAMPANT_GROWTH.getId()))),
        new GroveReputationEntry(ModGroves.SPROUTING.value(), RootsAPI.rl("learn_spell/harvest"), new GroveReputation(first_spell_reward), true, List.of(new GroveReputationEntry.SubEntry(GroveReputationEntry.SubEntryType.EXACT_SPELL, ModSpells.HARVEST.getId()))),
        // Primal grove doesn't have reputation
        new GroveReputationEntry(ModGroves.TWILIGHT.value(), RootsAPI.rl("learn_spell/light_drifter"), new GroveReputation(first_spell_reward), true, List.of(new GroveReputationEntry.SubEntry(GroveReputationEntry.SubEntryType.EXACT_SPELL, ModSpells.LIGHT_DRIFTER.getId()))),
        new GroveReputationEntry(ModGroves.TWILIGHT.value(), RootsAPI.rl("learn_spell/life_drain"), new GroveReputation(first_spell_reward), true, List.of(new GroveReputationEntry.SubEntry(GroveReputationEntry.SubEntryType.EXACT_SPELL, ModSpells.LIFE_DRAIN.getId()))),
        new GroveReputationEntry(ModGroves.TWILIGHT.value(), RootsAPI.rl("learn_spell/jaunt"), new GroveReputation(first_spell_reward), true, List.of(new GroveReputationEntry.SubEntry(GroveReputationEntry.SubEntryType.EXACT_SPELL, ModSpells.JAUNT.getId()))),
        new GroveReputationEntry(ModGroves.TWILIGHT.value(), RootsAPI.rl("learn_spell/temporal_morass"), new GroveReputation(first_spell_reward), true, List.of(new GroveReputationEntry.SubEntry(GroveReputationEntry.SubEntryType.EXACT_SPELL, ModSpells.TEMPORAL_MORASS.getId()))),
        new GroveReputationEntry(ModGroves.WILD.value(), RootsAPI.rl("learn_spell/nondetection"), new GroveReputation(first_spell_reward), true, List.of(new GroveReputationEntry.SubEntry(GroveReputationEntry.SubEntryType.EXACT_SPELL, ModSpells.NONDETECTION.getId())))
    ), false);

    int first_ritual_reward = 100;

    builder20.add(ModActions.START_RITUAL, List.of(
        new GroveReputationEntry(ModGroves.FUNGAL.value(), RootsAPI.rl("first_ritual/purity"), new GroveReputation(first_ritual_reward), true, List.of(new GroveReputationEntry.SubEntry(GroveReputationEntry.SubEntryType.EXACT_RITUAL, ModRituals.PURITY.getId()))),
        new GroveReputationEntry(ModGroves.ELEMENTAL.value(), RootsAPI.rl("first_ritual/fire_storm"), new GroveReputation(first_ritual_reward), true, List.of(new GroveReputationEntry.SubEntry(GroveReputationEntry.SubEntryType.EXACT_RITUAL, ModRituals.FIRE_STORM.getId()))),
        new GroveReputationEntry(ModGroves.ELEMENTAL.value(), RootsAPI.rl("first_ritual/frost_lands"), new GroveReputation(first_ritual_reward), true, List.of(new GroveReputationEntry.SubEntry(GroveReputationEntry.SubEntryType.EXACT_RITUAL, ModRituals.FROST_LANDS.getId()))),
        new GroveReputationEntry(ModGroves.ELEMENTAL.value(), RootsAPI.rl("first_ritual/heavy_storms"), new GroveReputation(first_ritual_reward), true, List.of(new GroveReputationEntry.SubEntry(GroveReputationEntry.SubEntryType.EXACT_RITUAL, ModRituals.HEAVY_STORMS.getId()))),
        new GroveReputationEntry(ModGroves.ELEMENTAL.value(), RootsAPI.rl("first_ritual/windwall"), new GroveReputation(first_ritual_reward), true, List.of(new GroveReputationEntry.SubEntry(GroveReputationEntry.SubEntryType.EXACT_RITUAL, ModRituals.WINDWALL.getId()))),
        new GroveReputationEntry(ModGroves.SPROUTING.value(), RootsAPI.rl("first_ritual/germination"), new GroveReputation(first_ritual_reward), true, List.of(new GroveReputationEntry.SubEntry(GroveReputationEntry.SubEntryType.EXACT_RITUAL, ModRituals.GERMINATION.getId()))),
        new GroveReputationEntry(ModGroves.SPROUTING.value(), RootsAPI.rl("first_ritual/spreading_forest"), new GroveReputation(first_ritual_reward), true, List.of(new GroveReputationEntry.SubEntry(GroveReputationEntry.SubEntryType.EXACT_RITUAL, ModRituals.SPREADING_FOREST.getId()))),
        new GroveReputationEntry(ModGroves.SPROUTING.value(), RootsAPI.rl("first_ritual/wildroot_growth"), new GroveReputation(first_ritual_reward), true, List.of(new GroveReputationEntry.SubEntry(GroveReputationEntry.SubEntryType.EXACT_RITUAL, ModRituals.WILDROOT_GROWTH.getId()))),
        new GroveReputationEntry(ModGroves.TWILIGHT.value(), RootsAPI.rl("first_ritual/healing_aura"), new GroveReputation(first_ritual_reward), true, List.of(new GroveReputationEntry.SubEntry(GroveReputationEntry.SubEntryType.EXACT_RITUAL, ModRituals.HEALING_AURA.getId()))),
        new GroveReputationEntry(ModGroves.TWILIGHT.value(), RootsAPI.rl("first_ritual/transmutation"), new GroveReputation(first_ritual_reward), true, List.of(new GroveReputationEntry.SubEntry(GroveReputationEntry.SubEntryType.EXACT_RITUAL, ModRituals.TRANSMUTATION.getId()))),
        new GroveReputationEntry(ModGroves.WILD.value(), RootsAPI.rl("first_ritual/animal_harvest"), new GroveReputation(first_ritual_reward), true, List.of(new GroveReputationEntry.SubEntry(GroveReputationEntry.SubEntryType.EXACT_RITUAL, ModRituals.ANIMAL_HARVEST.getId()))),
        new GroveReputationEntry(ModGroves.WILD.value(), RootsAPI.rl("first_ritual/gathering"), new GroveReputation(first_ritual_reward), true, List.of(new GroveReputationEntry.SubEntry(GroveReputationEntry.SubEntryType.EXACT_RITUAL, ModRituals.GATHERING.getId()))),
        new GroveReputationEntry(ModGroves.FAIRY.value(), RootsAPI.rl("first_ritual/blooming"), new GroveReputation(first_ritual_reward), true, List.of(new GroveReputationEntry.SubEntry(GroveReputationEntry.SubEntryType.EXACT_RITUAL, ModRituals.BLOOMING.getId()))),
        new GroveReputationEntry(ModGroves.FAIRY.value(), RootsAPI.rl("first_ritual/protection"), new GroveReputation(first_ritual_reward), true, List.of(new GroveReputationEntry.SubEntry(GroveReputationEntry.SubEntryType.EXACT_RITUAL, ModRituals.PROTECTION.getId()))),
        new GroveReputationEntry(ModGroves.FAIRY.value(), RootsAPI.rl("first_ritual/warding"), new GroveReputation(first_ritual_reward), true, List.of(new GroveReputationEntry.SubEntry(GroveReputationEntry.SubEntryType.EXACT_RITUAL, ModRituals.WARDING.getId())))
    ), false);
    builder20.add(ModActions.GEAS, List.of(
        new GroveReputationEntry(ModGroves.FUNGAL.value(), RootsAPI.rl("inflict_geas"), new GroveReputation(50, 10, 5, 1))
    ), false);
    builder20.add(ModActions.CRAFT_ITEM, List.of(
        new GroveReputationEntry(ModGroves.WILD.value(), RootsAPI.rl("create_runic_shears"), new GroveReputation(50), true, List.of(new GroveReputationEntry.SubEntry(GroveReputationEntry.SubEntryType.EXACT_ITEM, ModItems.RUNIC_SHEARS.getId())))
    ), false);
    builder20.add(ModActions.BRED_ANIMAL, List.of(
        new GroveReputationEntry(ModGroves.WILD.value(), RootsAPI.rl("bred_animal"), new GroveReputation(10, 8, 4, 1)),
        new GroveReputationEntry(ModGroves.SPROUTING.value(), RootsAPI.rl("bred_sprouts"), new GroveReputation(20, 10, 5, 1), GroveReputationEntry.SubEntryType.TARGET_ENTITY, RootsTags.Entities.SPROUTS)
    ), false);
    builder20.add(ModActions.SHATTER_BLOCK, List.of(
        new GroveReputationEntry(ModGroves.ELEMENTAL.value(), RootsAPI.rl("shattered_ore_block"), new GroveReputation(2, 1, 0, 0), GroveReputationEntry.SubEntryType.BLOCK, Tags.Blocks.ORES)
    ), false);
    builder20.add(ModActions.KILL_ENTITY, List.of(
        new GroveReputationEntry(ModGroves.WILD.value(), RootsAPI.rl("kill_pacifist_animals"), new GroveReputation(-2, -8, -10, -20), GroveReputationEntry.SubEntryType.TARGET_ENTITY, RootsTags.Entities.PACIFIST),
        new GroveReputationEntry(ModGroves.TWILIGHT.value(), RootsAPI.rl("kill_pacifist_animals"), new GroveReputation(10, 5, 2, 0), GroveReputationEntry.SubEntryType.TARGET_ENTITY, RootsTags.Entities.PACIFIST),
        new GroveReputationEntry(ModGroves.ELEMENTAL.value(), RootsAPI.rl("kill_withers"), new GroveReputation(50, 40, 30, 20), GroveReputationEntry.SubEntryType.TARGET_ENTITY, RootsTags.Entities.WITHERS),
        new GroveReputationEntry(ModGroves.HOLLOW.value(), RootsAPI.rl("kill_dragons"), new GroveReputation(50, 40, 30, 20), GroveReputationEntry.SubEntryType.TARGET_ENTITY, RootsTags.Entities.DRAGONS),
        new GroveReputationEntry(ModGroves.FAIRY.value(), RootsAPI.rl("kill_traders"), new GroveReputation(-5, -8, -10, -20), GroveReputationEntry.SubEntryType.TARGET_ENTITY, RootsTags.Entities.TRADERS),
        new GroveReputationEntry(ModGroves.FUNGAL.value(), RootsAPI.rl("kill_undead"), new GroveReputation(10, 8, 6, 2), GroveReputationEntry.SubEntryType.TARGET_ENTITY, RootsTags.Entities.UNDEAD),
        new GroveReputationEntry(ModGroves.SPROUTING.value(), RootsAPI.rl("kill_sprouts"), new GroveReputation(0, -2, -5, -10), GroveReputationEntry.SubEntryType.TARGET_ENTITY, RootsTags.Entities.SPROUTS)
    ), false);
    builder20.add(ModActions.TAME_ANIMAL, List.of(
        new GroveReputationEntry(ModGroves.WILD.value(), RootsAPI.rl("tame_animals"), new GroveReputation(10, 8, 5, 2))
    ), false);
    builder20.add(ModActions.TRADE_VILLAGER, List.of(
        new GroveReputationEntry(ModGroves.FAIRY.value(), RootsAPI.rl("trade_with_villager"), new GroveReputation(20, 15, 8, 2))
    ), false);
    builder20.add(ModActions.TRADE_PIGLIN, List.of(
        new GroveReputationEntry(ModGroves.FAIRY.value(), RootsAPI.rl("trade_with_piglin"), new GroveReputation(4, 2, 1, 0))
    ), false);
    builder20.add(ModActions.CURE_VILLAGER, List.of(
        new GroveReputationEntry(ModGroves.FAIRY.value(), RootsAPI.rl("cure_villager"), new GroveReputation(100, 20, 10, 10))
    ), false);
    // TODO: What items?
    builder20.add(ModActions.EAT_ITEM, List.of(
        new GroveReputationEntry(ModGroves.FUNGAL.value(), RootsAPI.rl("eat_rotten_flesh"), new GroveReputation(20, 0, 0, 0), GroveReputationEntry.SubEntryType.EXACT_ITEM, RootsTags.Items.ROTTEN_FLESH)
    ), false);
    builder20.add(ModActions.HARVEST_BEE_HIVE, List.of(
        new GroveReputationEntry(ModGroves.WILD.value(), RootsAPI.rl("harvest_bee_hive"), new GroveReputation(10, 8, 4, 2))
    ), false);
    builder20.add(ModActions.FILL_COMPOST, List.of(
        new GroveReputationEntry(ModGroves.FUNGAL.value(), RootsAPI.rl("fill_compost_fungal"), new GroveReputation(10, 8, 4, 2)),
        new GroveReputationEntry(ModGroves.SPROUTING.value(), RootsAPI.rl("fill_compost_sprout"), new GroveReputation(10, 5, 0, 0))
    ), false);
    builder20.add(ModActions.GROW_HUGE_MUSHROOM, List.of(
        new GroveReputationEntry(ModGroves.SPROUTING.value(), RootsAPI.rl("grove_huge_mushroom_sprout"), new GroveReputation(5, 2, 1, 0)),
        new GroveReputationEntry(ModGroves.FUNGAL.value(), RootsAPI.rl("grove_huge_mushroom_fungal"), new GroveReputation(15, 8, 2, 1))
    ), false);
    builder20.add(ModActions.MILK_COW, List.of(
        new GroveReputationEntry(ModGroves.WILD.value(), RootsAPI.rl("milk_cow"), new GroveReputation(2, 1, 0, 0))
    ), false);
    builder20.add(ModActions.ARRIVE_DIMENSION, List.of(
        new GroveReputationEntry(ModGroves.HOLLOW.value(), RootsAPI.rl("arrive_in_the_end"), new GroveReputation(500), true, List.of(new GroveReputationEntry.SubEntry(GroveReputationEntry.SubEntryType.DIMENSION, Level.END.location()))),
        new GroveReputationEntry(ModGroves.ELEMENTAL.value(), RootsAPI.rl("arrive_in_the_nether"), new GroveReputation(500), true, List.of(new GroveReputationEntry.SubEntry(GroveReputationEntry.SubEntryType.DIMENSION, Level.NETHER.location())))
    ), false);

    var builder21 = builder(DataMaps.GROVE_RANKS);
    RootsRegistries.GROVES.forEach(grove -> {
      builder21.add(grove.builtInRegistryHolder(), grove.getDefaultRanks(), false);
    });

    var builder22 = builder(DataMaps.OPERATION_COST);
    BuiltInRegistries.BLOCK.forEach(block -> {
      builder22.add(block.builtInRegistryHolder(), 1, false);
    });

    var builder23 = builder(DataMaps.SEED_TO_CROP);
    BuiltInRegistries.BLOCK.forEach(block -> {
      if (block instanceof CropBlock crop) {
        Item seed = ((AccessorMixinCropBlock) crop).rootsCallGetBaseSeedId().asItem();
        builder23.add(seed.builtInRegistryHolder(), block, false);
      }
    });

    builder23.add(ModItems.PERESKIA, ModBlocks.PERESKIA_CROP.get(), false);
    builder23.add(ModItems.MOONGLOW, ModBlocks.MOONGLOW_CROP.get(), false);
    builder23.add(ModItems.SPIRITLEAF, ModBlocks.SPIRITLEAF_CROP.get(), false);
    builder23.add(ModItems.WILDEWHEET, ModBlocks.WILDEWHEET_CROP.get(), false);
    builder23.add(Items.PITCHER_PLANT.builtInRegistryHolder(), Blocks.PITCHER_CROP, false);
    builder23.add(Items.MELON.builtInRegistryHolder(), Blocks.MELON_STEM, false);
    builder23.add(Items.MELON_SLICE.builtInRegistryHolder(), Blocks.MELON_STEM, false);
    builder23.add(Items.MELON_SEEDS.builtInRegistryHolder(), Blocks.MELON_STEM, false);
    builder23.add(Items.PUMPKIN.builtInRegistryHolder(), Blocks.PUMPKIN_STEM, false);
    builder23.add(Items.PUMPKIN_SEEDS.builtInRegistryHolder(), Blocks.PUMPKIN_STEM, false);

    var builder24 = builder(DataMaps.GROVE_POWER_GENERATORS);
    builder24.add(RootsTags.Blocks.FAIRY_GROVE_GENERATORS, List.of(
        new GrovePower.Generator(RootsTags.Groves.FAIRY, 5)), false);
    builder24.add(RootsTags.Blocks.FAIRY_GROVE_PATHS, List.of(
        new GrovePower.Generator(RootsTags.Groves.FAIRY, 1)), false);
    builder24.add(RootsTags.Blocks.ELEMENTAL_GROVE_GENERATORS, List.of(
        new GrovePower.Generator(RootsTags.Groves.ELEMENTAL, 5)), false);
    builder24.add(RootsTags.Blocks.SPROUTING_GROVE_GENERATORS, List.of(
        new GrovePower.Generator(RootsTags.Groves.SPROUTING, 5)), false);
    builder24.add(RootsTags.Blocks.FUNGAL_GROVE_GENERATORS, List.of(
        new GrovePower.Generator(RootsTags.Groves.FUNGAL, 5)), false);
    builder24.add(RootsTags.Blocks.WILD_GROVE_GENERATORS, List.of(
        new GrovePower.Generator(RootsTags.Groves.WILD, 5)), false);
    builder24.add(RootsTags.Blocks.TWILIGHT_GROVE_GENERATORS, List.of(
        new GrovePower.Generator(RootsTags.Groves.TWILIGHT, 5)), false);

    var builder25 = builder(DataMaps.GROVE_GENERATION_ENTRIES);
    builder25.add(ModGroves.FAIRY, List.of(
        new GrovePower.GenerationEntry(RootsTags.Blocks.FAIRY_GROVE_GENERATORS, 2, GrovePower.Symmetry.RADIAL_SAME_BLOCK),
        new GrovePower.GenerationEntry(RootsTags.Blocks.FAIRY_GROVE_PATHS, 30, GrovePower.Symmetry.RADIAL_SAME_BLOCK)
    ), false);
    builder25.add(ModGroves.ELEMENTAL, List.of(
        new GrovePower.GenerationEntry(RootsTags.Blocks.ELEMENTAL_GROVE_GENERATORS, 2, GrovePower.Symmetry.RADIAL_DIFFERENT_SAME_TAG)
    ), false);
  }
}
