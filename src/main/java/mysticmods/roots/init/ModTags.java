package mysticmods.roots.init;

import com.tterrag.registrate.providers.ProviderType;
import mysticmods.roots.api.RootsTags;
import mysticmods.roots.gen.RootsDataProviderTypes;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;

import static mysticmods.roots.Roots.REGISTRATE;

@SuppressWarnings("unchecked")
public class ModTags {
  static {
    REGISTRATE.addDataGenerator(RootsDataProviderTypes.RITUAL_TAGS, b -> {
      b.tag(RootsTags.Rituals.NYI).add(ModRituals.BLOOMING.get(), ModRituals.FIRE_STORM.get(), ModRituals.FROST_LANDS.get(), ModRituals.GATHERING.get(), ModRituals.GERMINATION.get(), ModRituals.HEALING_AURA.get(), ModRituals.HEAVY_STORMS.get(), ModRituals.OVERGROWTH.get(), ModRituals.PROTECTION.get(), ModRituals.PURITY.get(), ModRituals.SPREADING_FOREST.get(), ModRituals.SUMMON_CREATURES.get(), ModRituals.TRANSMUTATION.get(), ModRituals.WARDING.get(), ModRituals.WILDROOT_GROWTH.get(), ModRituals.WINDWALL.get());
    });

    REGISTRATE.addDataGenerator(RootsDataProviderTypes.SPELL_TAGS, b -> {
      b.tag(RootsTags.Spells.NYI).add(ModSpells.ACID_CLOUD.get(), ModSpells.AQUA_BUBBLE.get(), ModSpells.AUGMENT.get(), ModSpells.LIGHT_DRIFTER.get(), ModSpells.MAGNETISM.get(), ModSpells.DANDELION_WINDS.get(), ModSpells.DESATURATE.get(), ModSpells.DISARM.get(), ModSpells.EXTENSION.get(), ModSpells.NONDETECTION.get(), ModSpells.GEAS.get(), ModSpells.CONTROL_UNDEAD.get(), ModSpells.RAMPANT_GROWTH.get(), ModSpells.HARVEST.get(), ModSpells.LIFE_DRAIN.get(), ModSpells.RADIANCE.get(), ModSpells.ROSE_THORNS.get(), ModSpells.SANCTUARY.get(), ModSpells.SHATTER.get(), ModSpells.JAUNT.get(), ModSpells.STORM_CLOUD.get(), ModSpells.TIME_STOP.get(), ModSpells.WILDFIRE.get());
    });

    REGISTRATE.addDataGenerator(RootsDataProviderTypes.HERB_TAGS, b -> {
      b.tag(RootsTags.Herbs.FIRE).add(ModHerbs.INFERNO_BULB.get());
      b.tag(RootsTags.Herbs.AIR).add(ModHerbs.CLOUD_BERRY.get());
      b.tag(RootsTags.Herbs.WATER).add(ModHerbs.DEWGONIA.get());
      b.tag(RootsTags.Herbs.EARTH).add(ModHerbs.STALICRIPE.get());
      b.tag(RootsTags.Herbs.ELEMENTAL).addTags(RootsTags.Herbs.AIR, RootsTags.Herbs.EARTH, RootsTags.Herbs.FIRE, RootsTags.Herbs.WATER);
    });

    REGISTRATE.addDataGenerator(RootsDataProviderTypes.MODIFIER_TAGS, b -> {
    });

    REGISTRATE.addDataGenerator(ProviderType.ENTITY_TAGS, b -> {
      b.tag(RootsTags.Entities.SQUID).add(EntityType.SQUID, EntityType.GLOW_SQUID);
      b.tag(RootsTags.Entities.PACIFIST)
        .add(
          EntityType.ALLAY,
          EntityType.AXOLOTL,
          EntityType.BEE,
          EntityType.CAT,
          EntityType.CHICKEN,
          EntityType.COD,
          EntityType.COW,
          EntityType.DOLPHIN,
          EntityType.DONKEY,
          EntityType.FOX,
          EntityType.FROG,
          EntityType.GLOW_SQUID,
          EntityType.GOAT,
          EntityType.HORSE,
          EntityType.IRON_GOLEM,
          EntityType.LLAMA,
          EntityType.MULE,
          EntityType.MOOSHROOM,
          EntityType.OCELOT,
          EntityType.PANDA,
          EntityType.PARROT,
          EntityType.PIG,
          EntityType.POLAR_BEAR,
          EntityType.PUFFERFISH,
          EntityType.RABBIT, // Specific exclusion for killer bunnies
          EntityType.SALMON,
          EntityType.SHEEP,
          EntityType.SNOW_GOLEM,
          EntityType.SQUID,
          EntityType.STRIDER,
          EntityType.TADPOLE,
          EntityType.TRADER_LLAMA,
          EntityType.TROPICAL_FISH,
          EntityType.TURTLE,
          EntityType.VILLAGER,
          EntityType.WANDERING_TRADER,
          EntityType.WOLF);
      b.tag(RootsTags.Entities.ANIMAL_HARVEST).addTag(RootsTags.Entities.PACIFIST);
      b.tag(RootsTags.Entities.BOATS).add(EntityType.BOAT, EntityType.CHEST_BOAT);
    });

    REGISTRATE.addDataGenerator(ProviderType.BLOCK_TAGS, b -> {
      // Internal mod tags
      b.tag(RootsTags.Blocks.GRASS).add(Blocks.GRASS, Blocks.TALL_GRASS, Blocks.FERN, Blocks.LARGE_FERN);
      // TODO: CHECK
      b.tag(RootsTags.Blocks.SUPPORTS_STONEPETAL).addTag(BlockTags.STONE_ORE_REPLACEABLES);
      b.tag(RootsTags.Blocks.SUPPORTS_WILD_AUBERGINE).addTag(BlockTags.DIRT);
      b.tag(RootsTags.Blocks.SUPPORTS_WILD_ROOTS).addTags(BlockTags.BASE_STONE_OVERWORLD, BlockTags.DIRT, BlockTags.MOSS_REPLACEABLE).add(Blocks.MOSS_BLOCK, Blocks.SNOW_BLOCK, Blocks.GRAVEL, Blocks.CLAY);
      b.tag(RootsTags.Blocks.PEDESTALS).addTags(RootsTags.Blocks.RITUAL_PEDESTALS, RootsTags.Blocks.GROVE_PEDESTALS);
      b.tag(RootsTags.Blocks.SOILS).addTag(RootsTags.Blocks.ELEMENTAL_SOIL);
      b.tag(RootsTags.Blocks.ELEMENTAL_SOIL).addTags(RootsTags.Blocks.EARTH_SOIL, RootsTags.Blocks.AIR_SOIL, RootsTags.Blocks.FIRE_SOIL, RootsTags.Blocks.WATER_SOIL);
      b.tag(RootsTags.Blocks.RUNED_LOGS).addTags(RootsTags.Blocks.RUNED_ACACIA_LOG, RootsTags.Blocks.RUNED_BIRCH_LOG, RootsTags.Blocks.RUNED_OAK_LOG, RootsTags.Blocks.RUNED_DARK_OAK_LOG, RootsTags.Blocks.RUNED_CRIMSON_STEM, RootsTags.Blocks.RUNED_JUNGLE_LOG, RootsTags.Blocks.RUNED_SPRUCE_LOG, RootsTags.Blocks.RUNED_WARPED_STEM, RootsTags.Blocks.RUNED_WILDWOOD_LOG);
      b.tag(RootsTags.Blocks.AIR_CROPS).addTags(RootsTags.Blocks.CLOUD_BERRY_CROP);
      b.tag(RootsTags.Blocks.FIRE_CROPS).addTags(RootsTags.Blocks.INFERNO_BULB_CROP);
      b.tag(RootsTags.Blocks.WATER_CROPS).addTags(RootsTags.Blocks.DEWGONIA_CROP);
      b.tag(RootsTags.Blocks.EARTH_CROPS).addTags(RootsTags.Blocks.STALICRIPE_CROP);
      b.tag(RootsTags.Blocks.ELEMENTAL_CROPS).addTags(RootsTags.Blocks.WATER_CROPS, RootsTags.Blocks.AIR_CROPS, RootsTags.Blocks.EARTH_CROPS, RootsTags.Blocks.FIRE_CROPS);
      b.tag(RootsTags.Blocks.CROPS).addTags(RootsTags.Blocks.WILDEWHEET_CROP, RootsTags.Blocks.WILDROOT_CROP, RootsTags.Blocks.SPIRITLEAF_CROP, RootsTags.Blocks.PERESKIA_CROP, RootsTags.Blocks.MOONGLOW_CROP, RootsTags.Blocks.ELEMENTAL_CROPS);

      // Forge compat tags
      b.tag(RootsTags.Blocks.FORGE_CROPS).addTag(RootsTags.Blocks.CROPS);

      // Logs that burn
      b.tag(RootsTags.Blocks.MINECRAFT_LOGS_THAT_BURN).addTags(RootsTags.Blocks.WILDWOOD_LOGS);
      b.tag(RootsTags.Blocks.MINECRAFT_LOGS).addTags(RootsTags.Blocks.WILDWOOD_LOGS);

      b.tag(RootsTags.Blocks.ACACIA_CAPSTONES).addTags(RootsTags.Blocks.RUNED_ACACIA_LOG);
      b.tag(RootsTags.Blocks.ACACIA_PILLARS).add(Blocks.ACACIA_LOG, Blocks.STRIPPED_ACACIA_LOG, Blocks.ACACIA_WOOD, Blocks.STRIPPED_ACACIA_WOOD);
      b.tag(RootsTags.Blocks.BIRCH_CAPSTONES).addTags(RootsTags.Blocks.RUNED_BIRCH_LOG);
      b.tag(RootsTags.Blocks.BIRCH_PILLARS).add(Blocks.BIRCH_LOG, Blocks.STRIPPED_BIRCH_LOG, Blocks.BIRCH_WOOD, Blocks.STRIPPED_BIRCH_WOOD);
      b.tag(RootsTags.Blocks.DARK_OAK_CAPSTONES).addTags(RootsTags.Blocks.RUNED_DARK_OAK_LOG);
      b.tag(RootsTags.Blocks.DARK_OAK_PILLARS).add(Blocks.DARK_OAK_LOG, Blocks.STRIPPED_DARK_OAK_LOG, Blocks.DARK_OAK_WOOD, Blocks.STRIPPED_DARK_OAK_WOOD);
      b.tag(RootsTags.Blocks.JUNGLE_CAPSTONES).addTags(RootsTags.Blocks.RUNED_JUNGLE_LOG);
      b.tag(RootsTags.Blocks.JUNGLE_PILLARS).add(Blocks.JUNGLE_LOG, Blocks.STRIPPED_JUNGLE_LOG, Blocks.JUNGLE_WOOD, Blocks.STRIPPED_JUNGLE_WOOD);
      b.tag(RootsTags.Blocks.OAK_CAPSTONES).addTags(RootsTags.Blocks.RUNED_OAK_LOG);
      b.tag(RootsTags.Blocks.OAK_PILLARS).add(Blocks.OAK_LOG, Blocks.STRIPPED_OAK_LOG, Blocks.OAK_WOOD, Blocks.STRIPPED_OAK_WOOD);
      b.tag(RootsTags.Blocks.SPRUCE_CAPSTONES).addTags(RootsTags.Blocks.RUNED_SPRUCE_LOG);
      b.tag(RootsTags.Blocks.SPRUCE_PILLARS).add(Blocks.SPRUCE_LOG, Blocks.STRIPPED_SPRUCE_LOG, Blocks.SPRUCE_WOOD, Blocks.STRIPPED_SPRUCE_WOOD);
      b.tag(RootsTags.Blocks.WARPED_CAPSTONES).addTags(RootsTags.Blocks.RUNED_WARPED_STEM);
      b.tag(RootsTags.Blocks.WARPED_PILLARS).add(Blocks.WARPED_STEM, Blocks.STRIPPED_WARPED_STEM, Blocks.WARPED_HYPHAE, Blocks.STRIPPED_WARPED_HYPHAE);
      b.tag(RootsTags.Blocks.CRIMSON_CAPSTONES).addTags(RootsTags.Blocks.RUNED_CRIMSON_STEM);
      b.tag(RootsTags.Blocks.CRIMSON_PILLARS).add(Blocks.CRIMSON_STEM, Blocks.STRIPPED_CRIMSON_STEM, Blocks.CRIMSON_HYPHAE, Blocks.STRIPPED_CRIMSON_HYPHAE);
      b.tag(RootsTags.Blocks.WILDWOOD_CAPSTONES).addTags(RootsTags.Blocks.RUNED_WILDWOOD_LOG);
      b.tag(RootsTags.Blocks.WILDWOOD_PILLARS).add(ModBlocks.WILDWOOD_LOG.get(), ModBlocks.STRIPPED_WILDWOOD_LOG.get(), ModBlocks.WILDWOOD_WOOD.get(), ModBlocks.STRIPPED_WILDWOOD_WOOD.get());

      b.tag(RootsTags.Blocks.GROWTH_FORCE);
      b.tag(RootsTags.Blocks.GROWTH_BLACKLIST);
      b.tag(RootsTags.Blocks.GROWTH_REDUCE);
    });

    REGISTRATE.addDataGenerator(ProviderType.ITEM_TAGS, b -> {
      // Block copies

      b.copy(RootsTags.Blocks.SOILS, RootsTags.Items.SOILS);
      b.copy(RootsTags.Blocks.WATER_SOIL, RootsTags.Items.WATER_SOIL);
      b.copy(RootsTags.Blocks.AIR_SOIL, RootsTags.Items.AIR_SOIL);
      b.copy(RootsTags.Blocks.EARTH_SOIL, RootsTags.Items.EARTH_SOIL);
      b.copy(RootsTags.Blocks.FIRE_SOIL, RootsTags.Items.FIRE_SOIL);
      b.copy(RootsTags.Blocks.ELEMENTAL_SOIL, RootsTags.Items.ELEMENTAL_SOIL);
      b.copy(RootsTags.Blocks.RUNED_OBSIDIAN, RootsTags.Items.RUNED_OBSIDIAN);
      b.copy(RootsTags.Blocks.RUNESTONE, RootsTags.Items.RUNESTONE);
      b.copy(RootsTags.Blocks.WILDWOOD_LOGS, RootsTags.Items.WILDWOOD_LOGS);
      b.copy(RootsTags.Blocks.RUNED_LOGS, RootsTags.Items.RUNED_LOGS);
      b.copy(RootsTags.Blocks.RUNED_ACACIA_LOG, RootsTags.Items.RUNED_ACACIA_LOG);
      b.copy(RootsTags.Blocks.RUNED_DARK_OAK_LOG, RootsTags.Items.RUNED_DARK_OAK_LOG);
      b.copy(RootsTags.Blocks.RUNED_OAK_LOG, RootsTags.Items.RUNED_OAK_LOG);
      b.copy(RootsTags.Blocks.RUNED_BIRCH_LOG, RootsTags.Items.RUNED_BIRCH_LOG);
      b.copy(RootsTags.Blocks.RUNED_JUNGLE_LOG, RootsTags.Items.RUNED_JUNGLE_LOG);
      b.copy(RootsTags.Blocks.RUNED_SPRUCE_LOG, RootsTags.Items.RUNED_SPRUCE_LOG);
      b.copy(RootsTags.Blocks.RUNED_WILDWOOD_LOG, RootsTags.Items.RUNED_WILDWOOD_LOG);
      b.copy(RootsTags.Blocks.RUNED_CRIMSON_STEM, RootsTags.Items.RUNED_CRIMSON_STEM);
      b.copy(RootsTags.Blocks.RUNED_WARPED_STEM, RootsTags.Items.RUNED_WARPED_STEM);
      b.copy(RootsTags.Blocks.GROVE_STONES, RootsTags.Items.GROVE_STONES);
      b.copy(RootsTags.Blocks.PYRES, RootsTags.Items.PYRES);
      b.copy(RootsTags.Blocks.RITUAL_PEDESTALS, RootsTags.Items.RITUAL_PEDESTALS);
      b.copy(RootsTags.Blocks.GROVE_PEDESTALS, RootsTags.Items.GROVE_PEDESTALS);
      b.copy(RootsTags.Blocks.GROVE_STONE_PRIMAL, RootsTags.Items.GROVE_STONE_PRIMAL);
      b.copy(RootsTags.Blocks.MORTARS, RootsTags.Items.MORTARS);
      b.copy(RootsTags.Blocks.CRAFTERS, RootsTags.Items.CRAFTERS);
      b.copy(RootsTags.Blocks.NYI, RootsTags.Items.NYI);

      b.tag(RootsTags.Items.FLINT).add(Items.FLINT);
      b.tag(RootsTags.Items.STONELIKE).addTags(Tags.Items.SANDSTONE, Tags.Items.STONE, ItemTags.STONE_BRICKS, ItemTags.STONE_CRAFTING_MATERIALS, ItemTags.STONE_TOOL_MATERIALS).add(Items.DIORITE, Items.GRANITE, Items.CALCITE, Items.TUFF, Items.POLISHED_DIORITE, Items.POLISHED_GRANITE, Items.POLISHED_ANDESITE, Items.ANDESITE, Items.POLISHED_DEEPSLATE, Items.POLISHED_BLACKSTONE);

      b.tag(RootsTags.Items.RUNESTONE_HERBS).addTags(RootsTags.Items.WILDROOT_CROP, RootsTags.Items.GROVE_MOSS_CROP);

      b.tag(RootsTags.Items.AIR_CROPS).addTags(RootsTags.Items.CLOUD_BERRY_CROP);
      b.tag(RootsTags.Items.WATER_CROPS).addTags(RootsTags.Items.DEWGONIA_CROP);
      b.tag(RootsTags.Items.EARTH_CROPS).addTags(RootsTags.Items.STALICRIPE_CROP);
      b.tag(RootsTags.Items.FIRE_CROPS).addTags(RootsTags.Items.INFERNO_BULB_CROP);

      b.tag(RootsTags.Items.CROPS).addTags(RootsTags.Items.AIR_CROPS, RootsTags.Items.FIRE_CROPS, RootsTags.Items.EARTH_CROPS, RootsTags.Items.WATER_CROPS, RootsTags.Items.WILDEWHEET_CROP, RootsTags.Items.SPIRITLEAF_CROP, RootsTags.Items.PERESKIA_CROP, RootsTags.Items.WILDROOT_CROP, RootsTags.Items.MOONGLOW_CROP, RootsTags.Items.AUBERGINE_CROP);

      b.tag(RootsTags.Items.BARKS).addTags(RootsTags.Items.BIRCH_BARK, RootsTags.Items.ACACIA_BARK, RootsTags.Items.DARK_OAK_BARK, RootsTags.Items.OAK_BARK, RootsTags.Items.JUNGLE_BARK, RootsTags.Items.SPRUCE_BARK, RootsTags.Items.WILDWOOD_BARK, RootsTags.Items.CRIMSON_BARK, RootsTags.Items.WARPED_BARK, RootsTags.Items.MIXED_BARK);

      b.tag(RootsTags.Items.GROVE_CRAFTER_ACTIVATION).add(Items.STICK);
      b.tag(RootsTags.Items.PYRE_ACTIVATION).add(Items.FLINT_AND_STEEL);

      b.tag(RootsTags.Items.Herbs.MOONGLOW).addTag(RootsTags.Items.MOONGLOW_CROP);
      b.tag(RootsTags.Items.Herbs.SPIRITLEAF).addTag(RootsTags.Items.SPIRITLEAF_CROP);
      b.tag(RootsTags.Items.Herbs.CLOUD_BERRY).addTag(RootsTags.Items.CLOUD_BERRY_CROP);
      b.tag(RootsTags.Items.Herbs.DEWGONIA).addTag(RootsTags.Items.DEWGONIA_CROP);
      b.tag(RootsTags.Items.Herbs.PERESKIA).addTag(RootsTags.Items.PERESKIA_CROP);
      b.tag(RootsTags.Items.Herbs.STALICRIPE).addTag(RootsTags.Items.STALICRIPE_CROP);
      b.tag(RootsTags.Items.Herbs.INFERNO_BULB).addTag(RootsTags.Items.INFERNO_BULB_CROP);
      b.tag(RootsTags.Items.Herbs.WILDROOT).addTag(RootsTags.Items.WILDROOT_CROP);
      b.tag(RootsTags.Items.Herbs.WILDEWHEET).addTag(RootsTags.Items.WILDEWHEET_CROP);
      b.tag(RootsTags.Items.Herbs.SPIRITLEAF).addTag(RootsTags.Items.SPIRITLEAF_CROP);
      b.tag(RootsTags.Items.Herbs.GROVE_MOSS).addTag(RootsTags.Items.GROVE_MOSS_CROP);

      b.tag(RootsTags.Items.Herbs.HERBS).addTags(RootsTags.Items.Herbs.MOONGLOW, RootsTags.Items.Herbs.SPIRITLEAF, RootsTags.Items.Herbs.CLOUD_BERRY, RootsTags.Items.Herbs.DEWGONIA, RootsTags.Items.Herbs.PERESKIA, RootsTags.Items.Herbs.STALICRIPE, RootsTags.Items.Herbs.INFERNO_BULB, RootsTags.Items.Herbs.WILDROOT, RootsTags.Items.Herbs.WILDEWHEET, RootsTags.Items.Herbs.SPIRITLEAF, RootsTags.Items.Herbs.GROVE_MOSS);

      b.tag(RootsTags.Items.BOTTLES).add(Items.GLASS_BOTTLE);

      // TODO: Copying crops to the actual item tag?
    });
  }

  public static void load() {
  }
}
