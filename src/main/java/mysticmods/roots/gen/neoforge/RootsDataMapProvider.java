package mysticmods.roots.gen.neoforge;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.condition.CanonicalRepresentation;
import mysticmods.roots.api.condition.LevelCondition;
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
import mysticmods.roots.api.test.world.PartialBlockState;
import mysticmods.roots.block.crop.ThreeStageCropBlock;
import mysticmods.roots.growth.GrowthRecord;
import mysticmods.roots.growth.HarvestRecord;
import mysticmods.roots.init.*;
import mysticmods.roots.item.TokenItem;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.*;
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

    Builder<CanonicalRepresentation, LevelCondition> builder13 = builder(DataMaps.LEVEL_CONDITION_CANONS).replace(false);
    builder13.add(ModConditions.GROVE_STONE_ANY.getDelegate(), LevelCondition.GroveStoneCondition.fromBlockState(ModBlocks.PRIMAL_GROVE_STONE.get()
        .defaultBlockState(), false, false), false);
    builder13.add(ModConditions.GROVE_STONE_ACTIVE.getDelegate(), LevelCondition.GroveStoneCondition.fromBlockState(ModBlocks.PRIMAL_GROVE_STONE.get()
        .defaultBlockState(), true, false), false);
    builder13.add(ModConditions.PRIMAL_GROVE_STONE_ACTIVE.getDelegate(), LevelCondition.GroveStoneCondition.fromBlockState(ModBlocks.PRIMAL_GROVE_STONE.get()
        .defaultBlockState(), true, false), false);
    builder13.add(ModConditions.PRIMAL_GROVE_STONE_INACTIVE.getDelegate(), LevelCondition.GroveStoneCondition.fromBlockState(ModBlocks.PRIMAL_GROVE_STONE.get()
        .defaultBlockState(), false, true), false);
    builder13.add(ModConditions.PRIMAL_GROVE_STONE_ANY.getDelegate(), LevelCondition.GroveStoneCondition.fromBlockState(ModBlocks.PRIMAL_GROVE_STONE.get()
        .defaultBlockState(), false, false), false);
    builder13.add(ModConditions.ACACIA_PILLAR_4_HIGH.getDelegate(), LevelCondition.PillarCondition.fromStates(ModBlocks.RUNED_ACACIA_LOG.get()
        .defaultBlockState(), Blocks.ACACIA_LOG.defaultBlockState(), 4), false);
    builder13.add(ModConditions.ACACIA_PILLAR_3_HIGH.getDelegate(), LevelCondition.PillarCondition.fromStates(ModBlocks.RUNED_ACACIA_LOG.get()
        .defaultBlockState(), Blocks.ACACIA_LOG.defaultBlockState(), 3), false);
    builder13.add(ModConditions.LOG_PILLAR_3_HIGH.getDelegate(), LevelCondition.PillarCondition.fromStates(ModBlocks.RUNED_OAK_LOG.get()
        .defaultBlockState(), Blocks.OAK_LOG.defaultBlockState(), 3), false);
    builder13.add(ModConditions.LOG_PILLAR_4_HIGH.getDelegate(), LevelCondition.PillarCondition.fromStates(ModBlocks.RUNED_OAK_LOG.get()
        .defaultBlockState(), Blocks.OAK_LOG.defaultBlockState(), 4), false);
    builder13.add(ModConditions.RUNE_PILLAR_3_HIGH.getDelegate(), LevelCondition.PillarCondition.fromStates(ModBlocks.CHISELED_RUNESTONE.get()
        .defaultBlockState(), ModBlocks.RUNESTONE.get().defaultBlockState(), 3), false);
    builder13.add(ModConditions.RUNE_PILLAR_4_HIGH.getDelegate(), LevelCondition.PillarCondition.fromStates(ModBlocks.CHISELED_RUNESTONE.get()
        .defaultBlockState(), ModBlocks.RUNESTONE.get().defaultBlockState(), 4), false);
    builder13.add(ModConditions.WILDWOOD_PILLAR_3_HIGH.getDelegate(), LevelCondition.PillarCondition.fromStates(ModBlocks.RUNED_WILDWOOD_LOG.get()
        .defaultBlockState(), ModBlocks.WILDWOOD_LOG.get().defaultBlockState(), 3), false);
    builder13.add(ModConditions.WILDWOOD_PILLAR_4_HIGH.getDelegate(), LevelCondition.PillarCondition.fromStates(ModBlocks.RUNED_WILDWOOD_LOG.get()
        .defaultBlockState(), ModBlocks.WILDWOOD_LOG.get().defaultBlockState(), 4), false);
    builder13.add(ModConditions.BIRCH_PILLAR_3_HIGH.getDelegate(), LevelCondition.PillarCondition.fromStates(ModBlocks.RUNED_BIRCH_LOG.get()
        .defaultBlockState(), Blocks.BIRCH_LOG.defaultBlockState(), 3), false);
    builder13.add(ModConditions.BIRCH_PILLAR_4_HIGH.getDelegate(), LevelCondition.PillarCondition.fromStates(ModBlocks.RUNED_BIRCH_LOG.get()
        .defaultBlockState(), Blocks.BIRCH_LOG.defaultBlockState(), 4), false);
    builder13.add(ModConditions.CRIMSON_PILLAR_3_HIGH.getDelegate(), LevelCondition.PillarCondition.fromStates(ModBlocks.RUNED_CRIMSON_STEM.get()
        .defaultBlockState(), Blocks.CRIMSON_STEM.defaultBlockState(), 3), false);
    builder13.add(ModConditions.CRIMSON_PILLAR_4_HIGH.getDelegate(), LevelCondition.PillarCondition.fromStates(ModBlocks.RUNED_CRIMSON_STEM.get()
        .defaultBlockState(), Blocks.CRIMSON_STEM.defaultBlockState(), 4), false);
    builder13.add(ModConditions.DARK_OAK_PILLAR_3_HIGH.getDelegate(), LevelCondition.PillarCondition.fromStates(ModBlocks.RUNED_DARK_OAK_LOG.get()
        .defaultBlockState(), Blocks.DARK_OAK_LOG.defaultBlockState(), 3), false);
    builder13.add(ModConditions.DARK_OAK_PILLAR_4_HIGH.getDelegate(), LevelCondition.PillarCondition.fromStates(ModBlocks.RUNED_DARK_OAK_LOG.get()
        .defaultBlockState(), Blocks.DARK_OAK_LOG.defaultBlockState(), 4), false);
    builder13.add(ModConditions.JUNGLE_PILLAR_3_HIGH.getDelegate(), LevelCondition.PillarCondition.fromStates(ModBlocks.RUNED_JUNGLE_LOG.get()
        .defaultBlockState(), Blocks.JUNGLE_LOG.defaultBlockState(), 3), false);
    builder13.add(ModConditions.JUNGLE_PILLAR_4_HIGH.getDelegate(), LevelCondition.PillarCondition.fromStates(ModBlocks.RUNED_JUNGLE_LOG.get()
        .defaultBlockState(), Blocks.JUNGLE_LOG.defaultBlockState(), 4), false);
    builder13.add(ModConditions.OAK_PILLAR_3_HIGH.getDelegate(), LevelCondition.PillarCondition.fromStates(ModBlocks.RUNED_OAK_LOG.get()
        .defaultBlockState(), Blocks.OAK_LOG.defaultBlockState(), 3), false);
    builder13.add(ModConditions.OAK_PILLAR_4_HIGH.getDelegate(), LevelCondition.PillarCondition.fromStates(ModBlocks.RUNED_OAK_LOG.get()
        .defaultBlockState(), Blocks.OAK_LOG.defaultBlockState(), 4), false);
    builder13.add(ModConditions.SPRUCE_PILLAR_3_HIGH.getDelegate(), LevelCondition.PillarCondition.fromStates(ModBlocks.RUNED_SPRUCE_LOG.get()
        .defaultBlockState(), Blocks.SPRUCE_LOG.defaultBlockState(), 3), false);
    builder13.add(ModConditions.SPRUCE_PILLAR_4_HIGH.getDelegate(), LevelCondition.PillarCondition.fromStates(ModBlocks.RUNED_SPRUCE_LOG.get()
        .defaultBlockState(), Blocks.SPRUCE_LOG.defaultBlockState(), 4), false);
    builder13.add(ModConditions.WARPED_PILLAR_3_HIGH.getDelegate(), LevelCondition.PillarCondition.fromStates(ModBlocks.RUNED_WARPED_STEM.get()
        .defaultBlockState(), Blocks.WARPED_STEM.defaultBlockState(), 3), false);
    builder13.add(ModConditions.WARPED_PILLAR_4_HIGH.getDelegate(), LevelCondition.PillarCondition.fromStates(ModBlocks.RUNED_WARPED_STEM.get()
        .defaultBlockState(), Blocks.WARPED_STEM.defaultBlockState(), 4), false);
    builder13.add(ModConditions.MATURE_WILDROOT_CROP.getDelegate(), new CanonicalRepresentation(new PartialBlockState(Blocks.FARMLAND), new PartialBlockState(ModBlocks.WILDROOT_CROP.get()
        .defaultBlockState().setValue(ThreeStageCropBlock.AGE, 3), ThreeStageCropBlock.AGE)), false);

    var builder14 = builder(DataMaps.SPROUT_BREEDING_ITEM_CHANCE);
    // Requires finding a world-generated crop
    builder14.add(Items.BEETROOT_SEEDS.builtInRegistryHolder(), 40, false);
    // Super-easy to find
    builder14.add(Items.WHEAT_SEEDS.builtInRegistryHolder(), 5, false);
    // Craftable from melons, requires finding a jungle
    builder14.add(Items.MELON_SEEDS.builtInRegistryHolder(), 40, false);
    // Requires finding a jungle
    builder14.add(Items.COCOA_BEANS.builtInRegistryHolder(), 40, false);
    // Craftable from pumpkins, easily found
    builder14.add(Items.PUMPKIN_SEEDS.builtInRegistryHolder(), 15, false);
    // Requires finding a potato crop or potato in a whatever
    builder14.add(Items.POTATO.builtInRegistryHolder(), 30, false);
    // Requires finding a carrot crop or a carrot in a whatever
    builder14.add(Items.CARROT.builtInRegistryHolder(), 30, false);

    // Relatively easy to find eventually
    builder14.add(ModItems.WILDROOT, 20, false);

    // Easy to find and replicate
    builder14.add(ModItems.GROVE_SPORES, 10, false);

    // Requires finding an aubergine world spawn
    builder14.add(ModItems.AUBERGINE_SEEDS, 20, false);

    // Requires specific crafting recipes
    builder14.add(ModItems.MOONGLOW_SEEDS, 40, false);
    builder14.add(ModItems.SPIRITLEAF_SEEDS, 40, false);
    builder14.add(ModItems.WILDEWHEET_SEEDS, 40, false);
    builder14.add(ModItems.BAFFLECAP, 40, false);

    // These should be exceptionally rare
    builder14.add(Items.TORCHFLOWER_SEEDS.builtInRegistryHolder(), 5, false);
    builder14.add(Items.PITCHER_POD.builtInRegistryHolder(), 5, false);

    // Nether-based plants
    builder14.add(Items.CRIMSON_FUNGUS.builtInRegistryHolder(), 10, false);
    builder14.add(Items.WARPED_FUNGUS.builtInRegistryHolder(), 10, false);
    builder14.add(Items.NETHER_WART.builtInRegistryHolder(), 10, false);

    // Rarer plants
    builder14.add(Items.KELP.builtInRegistryHolder(), 50, false);
    builder14.add(Items.SEAGRASS.builtInRegistryHolder(), 50, false);

    // Super rare plants
    builder14.add(Items.CHORUS_PLANT.builtInRegistryHolder(), 1, false);

    // Growth records!
    var builder15 = builder(DataMaps.GROWTH_RECORDS);
    var builder16 = builder(DataMaps.HARVEST_RECORDS);

    int AGE_SEVEN_TICKS = 2;
    int AGE_THREE_TICKS = 4;
    int SAPLING_TICKS = 4;

    // Explore what bamboo saplings look like
    builder15.add(Blocks.BAMBOO_SAPLING.builtInRegistryHolder(), new GrowthRecord(Blocks.BAMBOO_SAPLING, Optional.empty(), -1, SAPLING_TICKS, ModTests.BAMBOO_CAN_GROW.get(), ModTests.LIGHT_ABOVE_EIGHT.get()), false);
    builder15.add(Blocks.BAMBOO.builtInRegistryHolder(), new GrowthRecord(Blocks.BAMBOO, Optional.empty(), 1, AGE_THREE_TICKS, ModTests.BAMBOO_CAN_GROW.get(), ModTests.LIGHT_ABOVE_EIGHT.get()), false);

    // TOdO: Bamboo harvest


    builder15.add(Blocks.BEETROOTS.builtInRegistryHolder(), new GrowthRecord(Blocks.BEETROOTS, Optional.of(BeetrootBlock.AGE), BeetrootBlock.MAX_AGE, AGE_THREE_TICKS, ModTests.AGE_CAN_GROW.get(), ModTests.LIGHT_ABOVE_EIGHT.get()), false);
    builder16.add(Blocks.BEETROOTS.builtInRegistryHolder(), HarvestRecord.of((CropBlock) Blocks.BEETROOTS, ModTests.AGE_REPLANT.get()), false);

    builder15.add(Blocks.CARROTS.builtInRegistryHolder(), new GrowthRecord(Blocks.CARROTS, Optional.of(CarrotBlock.AGE), CarrotBlock.MAX_AGE, AGE_SEVEN_TICKS, ModTests.AGE_CAN_GROW.get(), ModTests.LIGHT_ABOVE_EIGHT.get()), false);
    builder16.add(Blocks.CARROTS.builtInRegistryHolder(), HarvestRecord.of((CropBlock) Blocks.CARROTS, ModTests.AGE_REPLANT.get()), false);

    builder15.add(Blocks.COCOA.builtInRegistryHolder(), new GrowthRecord(Blocks.COCOA, Optional.of(CocoaBlock.AGE), CocoaBlock.MAX_AGE, AGE_THREE_TICKS, ModTests.AGE_CAN_GROW.get(), ModTests.ANY_LIGHT.get()), false);
    builder16.add(Blocks.COCOA.builtInRegistryHolder(), new HarvestRecord(Blocks.COCOA, Items.COCOA_BEANS, Optional.of(CocoaBlock.AGE), CocoaBlock.MAX_AGE, ModTests.AGE_REPLANT.get()), false);

    builder15.add(RootsTags.Blocks.SPREADING_MUSHROOMS, new GrowthRecord(null, Optional.empty(), -1, 2, ModTests.ALWAYS_CAN_GROW.get(), ModTests.LIGHT_BELOW_THIRTEEN.get()), false);

    // TODO: Custom harvest

    // Kelp block
    builder15.add(Blocks.KELP.builtInRegistryHolder(), new GrowthRecord(Blocks.KELP, Optional.of(KelpBlock.AGE), KelpBlock.MAX_AGE, 1, ModTests.KELP_CAN_GROW.get(), ModTests.ANY_LIGHT.get()), false);

    // TODO: Custom harvest

    // Pitcher crop
    builder15.add(Blocks.PITCHER_CROP.builtInRegistryHolder(), new GrowthRecord(Blocks.PITCHER_CROP, Optional.of(PitcherCropBlock.AGE), PitcherCropBlock.MAX_AGE, AGE_THREE_TICKS, ModTests.AGE_CAN_GROW.get(), ModTests.LIGHT_ABOVE_EIGHT.get()), false);

    builder15.add(Blocks.POTATOES.builtInRegistryHolder(), new GrowthRecord(Blocks.POTATOES, Optional.of(PotatoBlock.AGE), PotatoBlock.MAX_AGE, AGE_SEVEN_TICKS, ModTests.AGE_CAN_GROW.get(), ModTests.LIGHT_ABOVE_EIGHT.get()), false);

    builder16.add(Blocks.POTATOES.builtInRegistryHolder(), HarvestRecord.of((CropBlock) Blocks.POTATOES, ModTests.AGE_REPLANT.get()), false);

    builder15.add(BlockTags.SAPLINGS, new GrowthRecord(null, Optional.empty(), -1, SAPLING_TICKS, ModTests.ALWAYS_CAN_GROW.get(), ModTests.LIGHT_ABOVE_EIGHT.get()), false);

    builder15.add(Blocks.MELON_STEM.builtInRegistryHolder(), new GrowthRecord(Blocks.MELON_STEM, Optional.of(StemBlock.AGE), StemBlock.MAX_AGE, AGE_SEVEN_TICKS, ModTests.ALWAYS_CAN_GROW.get(), ModTests.LIGHT_ABOVE_EIGHT.get()), false);
    builder16.add(Blocks.MELON.builtInRegistryHolder(), new HarvestRecord(Blocks.MELON, Items.MELON_SEEDS, Optional.empty(), -1, ModTests.REPLACE_WITH_AIR.get()), false);

    builder15.add(Blocks.NETHER_WART.builtInRegistryHolder(), new GrowthRecord(Blocks.NETHER_WART, Optional.of(NetherWartBlock.AGE), NetherWartBlock.MAX_AGE, AGE_THREE_TICKS, ModTests.AGE_CAN_GROW.get(), ModTests.ANY_LIGHT.get()), false);
    builder16.add(Blocks.NETHER_WART.builtInRegistryHolder(), new HarvestRecord(Blocks.NETHER_WART, Items.NETHER_WART, Optional.of(NetherWartBlock.AGE), NetherWartBlock.MAX_AGE, ModTests.AGE_REPLANT.get()), false);

    // TOOD: Stemmy?
    builder16.add(Blocks.PUMPKIN.builtInRegistryHolder(), new HarvestRecord(Blocks.PUMPKIN, Items.PUMPKIN_SEEDS, Optional.empty(), -1, ModTests.REPLACE_WITH_AIR.get()), false);
    builder15.add(Blocks.PUMPKIN_STEM.builtInRegistryHolder(), new GrowthRecord(Blocks.PUMPKIN_STEM, Optional.of(StemBlock.AGE), StemBlock.MAX_AGE, AGE_SEVEN_TICKS, ModTests.ALWAYS_CAN_GROW.get(), ModTests.LIGHT_ABOVE_EIGHT.get()), false);

    builder15.add(Blocks.SWEET_BERRY_BUSH.builtInRegistryHolder(), new GrowthRecord(Blocks.SWEET_BERRY_BUSH, Optional.of(SweetBerryBushBlock.AGE), SweetBerryBushBlock.MAX_AGE, AGE_THREE_TICKS, ModTests.ALWAYS_CAN_GROW.get(), ModTests.LIGHT_ABOVE_EIGHT.get()), false);
    builder16.add(Blocks.SWEET_BERRY_BUSH.builtInRegistryHolder(), new HarvestRecord(Blocks.SWEET_BERRY_BUSH, Items.SWEET_BERRIES, Optional.of(SweetBerryBushBlock.AGE), SweetBerryBushBlock.MAX_AGE, ModTests.AGE_REPLANT.get()), false);

    builder15.add(Blocks.TORCHFLOWER_CROP.builtInRegistryHolder(), new GrowthRecord(Blocks.TORCHFLOWER_CROP, Optional.of(TorchflowerCropBlock.AGE), TorchflowerCropBlock.MAX_AGE, AGE_THREE_TICKS, ModTests.AGE_CAN_GROW.get(), ModTests.LIGHT_ABOVE_EIGHT.get()), false);

    // TODO: Check for farmland below

    builder15.add(Blocks.TWISTING_VINES.builtInRegistryHolder(), new GrowthRecord(Blocks.TWISTING_VINES, Optional.of(TwistingVinesBlock.AGE), TwistingVinesBlock.MAX_AGE, 2, ModTests.AGE_CAN_GROW.get(), ModTests.LIGHT_ABOVE_EIGHT.get()), false);

    // TODO: Replace all but one, transform the lowest/highest block into the original plant

    builder15.add(Blocks.VINE.builtInRegistryHolder(), new GrowthRecord(Blocks.VINE, Optional.empty(), -1, 2, ModTests.ALWAYS_CAN_GROW.get(), ModTests.ANY_LIGHT.get()), false);

    builder15.add(Blocks.WEEPING_VINES.builtInRegistryHolder(), new GrowthRecord(Blocks.WEEPING_VINES, Optional.of(WeepingVinesBlock.AGE), WeepingVinesBlock.MAX_AGE, 2, ModTests.AGE_CAN_GROW.get(), ModTests.LIGHT_ABOVE_EIGHT.get()), false);

    builder15.add(Blocks.WHEAT.builtInRegistryHolder(), new GrowthRecord(Blocks.WHEAT, Optional.of(CropBlock.AGE), CropBlock.MAX_AGE, AGE_SEVEN_TICKS, ModTests.AGE_CAN_GROW.get(), ModTests.LIGHT_ABOVE_EIGHT.get()), false);
    builder16.add(Blocks.WHEAT.builtInRegistryHolder(), HarvestRecord.of((CropBlock) Blocks.WHEAT, ModTests.AGE_REPLANT.get()), false);

    // TODO: Replants similar to vines
    builder15.add(Blocks.CACTUS.builtInRegistryHolder(), new GrowthRecord(Blocks.CACTUS, Optional.of(CactusBlock.AGE), CactusBlock.MAX_AGE, 2, ModTests.CACTUS_CANE_CAN_GROW.get(), ModTests.ANY_LIGHT.get()), false);
    builder15.add(Blocks.SUGAR_CANE.builtInRegistryHolder(), new GrowthRecord(Blocks.SUGAR_CANE, Optional.of(SugarCaneBlock.AGE), 15, 2, ModTests.CACTUS_CANE_CAN_GROW.get(), ModTests.ANY_LIGHT.get()), false);

    builder15.add(ModBlocks.WILDROOT_CROP, new GrowthRecord(ModBlocks.WILDROOT_CROP.get(), Optional.of(ThreeStageCropBlock.AGE), ThreeStageCropBlock.MAX_AGE, AGE_THREE_TICKS, ModTests.AGE_CAN_GROW.get(), ModTests.LIGHT_ABOVE_EIGHT.get()), false);
    builder16.add(ModBlocks.WILDROOT_CROP, HarvestRecord.of(ModBlocks.WILDROOT_CROP.get(), ModTests.AGE_REPLANT.get()), false);

    // Elemental crops can grow in any light
    builder15.add(ModBlocks.CLOUD_BERRY_CROP, new GrowthRecord(ModBlocks.CLOUD_BERRY_CROP.get(), Optional.of(ThreeStageCropBlock.AGE), ThreeStageCropBlock.MAX_AGE, AGE_THREE_TICKS, ModTests.AGE_CAN_GROW.get(), ModTests.ANY_LIGHT.get()), false);
    builder16.add(ModBlocks.CLOUD_BERRY_CROP, HarvestRecord.of(ModBlocks.CLOUD_BERRY_CROP.get(), ModTests.AGE_REPLANT.get()), false);
    builder15.add(ModBlocks.INFERNO_BULB_CROP, new GrowthRecord(ModBlocks.INFERNO_BULB_CROP.get(), Optional.of(ThreeStageCropBlock.AGE), ThreeStageCropBlock.MAX_AGE, AGE_THREE_TICKS, ModTests.AGE_CAN_GROW.get(), ModTests.ANY_LIGHT.get()), false);
    builder16.add(ModBlocks.INFERNO_BULB_CROP, HarvestRecord.of(ModBlocks.INFERNO_BULB_CROP.get(), ModTests.AGE_REPLANT.get()), false);
    builder15.add(ModBlocks.STALICRIPE_CROP, new GrowthRecord(ModBlocks.STALICRIPE_CROP.get(), Optional.of(ThreeStageCropBlock.AGE), ThreeStageCropBlock.MAX_AGE, AGE_THREE_TICKS, ModTests.AGE_CAN_GROW.get(), ModTests.ANY_LIGHT.get()), false);
    builder16.add(ModBlocks.STALICRIPE_CROP, HarvestRecord.of(ModBlocks.STALICRIPE_CROP.get(), ModTests.AGE_REPLANT.get()), false);
    builder15.add(ModBlocks.DEWGONIA_CROP, new GrowthRecord(ModBlocks.DEWGONIA_CROP.get(), Optional.of(ThreeStageCropBlock.AGE), ThreeStageCropBlock.MAX_AGE, AGE_THREE_TICKS, ModTests.AGE_CAN_GROW.get(), ModTests.ANY_LIGHT.get()), false);
    builder16.add(ModBlocks.DEWGONIA_CROP, HarvestRecord.of(ModBlocks.DEWGONIA_CROP.get(), ModTests.AGE_REPLANT.get()), false);

    // Moonglow
    builder15.add(ModBlocks.MOONGLOW_CROP, new GrowthRecord(ModBlocks.MOONGLOW_CROP.get(), Optional.of(CropBlock.AGE), CropBlock.MAX_AGE, AGE_SEVEN_TICKS, ModTests.AGE_CAN_GROW.get(), ModTests.LIGHT_ABOVE_EIGHT.get()), false);
    builder16.add(ModBlocks.MOONGLOW_CROP, HarvestRecord.of(ModBlocks.MOONGLOW_CROP.get(), ModTests.AGE_REPLANT.get()), false);
    builder15.add(ModBlocks.PERESKIA_CROP, new GrowthRecord(ModBlocks.PERESKIA_CROP.get(), Optional.of(CropBlock.AGE), CropBlock.MAX_AGE, AGE_SEVEN_TICKS, ModTests.AGE_CAN_GROW.get(), ModTests.LIGHT_ABOVE_EIGHT.get()), false);
    builder16.add(ModBlocks.PERESKIA_CROP, HarvestRecord.of(ModBlocks.PERESKIA_CROP.get(), ModTests.AGE_REPLANT.get()), false);
    builder15.add(ModBlocks.SPIRITLEAF_CROP, new GrowthRecord(ModBlocks.SPIRITLEAF_CROP.get(), Optional.of(CropBlock.AGE), CropBlock.MAX_AGE, AGE_SEVEN_TICKS, ModTests.AGE_CAN_GROW.get(), ModTests.LIGHT_ABOVE_EIGHT.get()), false);
    builder16.add(ModBlocks.SPIRITLEAF_CROP, HarvestRecord.of(ModBlocks.SPIRITLEAF_CROP.get(), ModTests.AGE_REPLANT.get()), false);
    builder15.add(ModBlocks.WILDEWHEET_CROP, new GrowthRecord(ModBlocks.WILDEWHEET_CROP.get(), Optional.of(CropBlock.AGE), CropBlock.MAX_AGE, AGE_SEVEN_TICKS, ModTests.AGE_CAN_GROW.get(), ModTests.LIGHT_ABOVE_EIGHT.get()), false);
    builder16.add(ModBlocks.WILDEWHEET_CROP, HarvestRecord.of(ModBlocks.WILDEWHEET_CROP.get(), ModTests.AGE_REPLANT.get()), false);
    builder15.add(ModBlocks.AUBERGINE_CROP, new GrowthRecord(ModBlocks.AUBERGINE_CROP.get(), Optional.of(CropBlock.AGE), CropBlock.MAX_AGE, AGE_SEVEN_TICKS, ModTests.AGE_CAN_GROW.get(), ModTests.LIGHT_ABOVE_EIGHT.get()), false);
    builder16.add(ModBlocks.AUBERGINE_CROP, HarvestRecord.of(ModBlocks.AUBERGINE_CROP.get(), ModTests.AGE_REPLANT.get()), false);
  }
}
