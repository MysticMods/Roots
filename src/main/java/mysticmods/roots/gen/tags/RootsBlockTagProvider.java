package mysticmods.roots.gen.tags;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.RootsTags;
import mysticmods.roots.init.ModBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

import static mysticmods.roots.init.ModBlocks.*;

@SuppressWarnings("unchecked")
public final class RootsBlockTagProvider extends BlockTagsProvider {
  public RootsBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
    super(output, lookupProvider, RootsAPI.MODID, existingFileHelper);
  }

  @Override
  protected void addTags(HolderLookup.Provider provider) {
    this.tag(BlockTags.MINEABLE_WITH_SHOVEL).addTag(RootsTags.Blocks.ALL_SOIL).add(ENCHANTED_TURF.get());
    this.tag(BlockTags.MINEABLE_WITH_HOE)
        .add(THATCH.get(), STONEPETAL.get(), WILDWOOD_LEAVES.get(), WILD_ROOTS.get(), CREEPING_GROVE_MOSS.get(), HANGING_GROVE_MOSS.get(), BAFFLECAP.get());
    this.tag(BlockTags.MINEABLE_WITH_PICKAXE)
        .add(RUNESTONE.get(), RUNESTONE_BRICK.get(), CHISELED_RUNESTONE.get(), MOSSY_RUNESTONE.get(), RUNESTONE_TILE.get(), CHISELED_RUNED_OBSIDIAN.get(), RUNED_BRICK.get(), RUNED_TILE.get(), SILVER_ORE.get(), DEEPSLATE_SILVER_ORE.get(), GRANITE_QUARTZ_ORE.get(), RAW_SILVER_BLOCK.get(), SILVER_BLOCK.get(), RUNESTONE_STAIRS.get(), RUNESTONE_BRICK_STAIRS.get(), RUNESTONE_TILE_STAIRS.get(), MOSSY_RUNESTONE_STAIRS.get(), RUNED_STAIRS.get(), RUNED_TILE_STAIRS.get(), RUNED_BRICK_STAIRS.get(), RUNED_SLAB.get(), RUNED_BRICK_SLAB.get(), RUNED_TILE_SLAB.get(), RUNESTONE_SLAB.get(), RUNESTONE_BRICK_SLAB.get(), RUNESTONE_TILE_SLAB.get(), MOSSY_RUNESTONE_SLAB.get(), RUNED_BUTTON.get(), RUNED_TILE_BUTTON.get(), RUNED_BRICK_BUTTON.get(), MOSSY_RUNESTONE_BUTTON.get(), RUNESTONE_BUTTON.get(), RUNESTONE_BRICK_BUTTON.get(), RUNESTONE_TILE_BUTTON.get(), RUNED_PRESSURE_PLATE.get(), RUNED_TILE_PRESSURE_PLATE.get(), RUNED_BRICK_PRESSURE_PLATE.get(), MOSSY_RUNESTONE_PRESSURE_PLATE.get(), RUNESTONE_PRESSURE_PLATE.get(), RUNESTONE_BRICK_PRESSURE_PLATE.get(), RUNESTONE_TILE_PRESSURE_PLATE.get(), RUNED_WALL.get(), RUNED_BRICK_WALL.get(), RUNED_TILE_WALL.get(), RUNED_OBSIDIAN.get(), CHISELED_RUNED_OBSIDIAN.get(), RUNESTONE_WALL.get(), RUNESTONE_BRICK_WALL.get(), RUNESTONE_TILE_WALL.get(), MOSSY_RUNESTONE_WALL.get(), INCENSE_BURNER.get(), MORTAR.get(), UNENDING_BOWL.get(), GROVE_CRAFTER.get())
        .addTag(RootsTags.Blocks.GROVE_STONES);
    this.tag(BlockTags.MINEABLE_WITH_AXE)
        .add(WILDWOOD_LOG.get(), STRIPPED_WILDWOOD_LOG.get(), STRIPPED_WILDWOOD_WOOD.get(), WILDWOOD_WOOD.get(), WILDWOOD_STAIRS.get(), WILDWOOD_SLAB.get(), WILDWOOD_FENCE.get(), WILDWOOD_GATE.get(), WILDWOOD_BUTTON.get(), WILDWOOD_PRESSURE_PLATE.get(), WILDWOOD_DOOR.get(), WILDWOOD_TRAPDOOR.get(), PYRE.get(), REINFORCED_SOUL_PYRE.get(), SOUL_PYRE.get(), REINFORCED_PYRE.get(), DECORATIVE_PYRE.get(), DECORATIVE_SOUL_PYRE.get(), BAFFLECAP_BLOCK.get())
        .addTag(RootsTags.Blocks.HUTS).add(FUNGAL_TRANSMUTER.get());
    this.tag(BlockTags.SLABS)
        .add(RUNED_SLAB.get(), RUNED_BRICK_SLAB.get(), RUNED_TILE_SLAB.get(), RUNESTONE_SLAB.get(), RUNESTONE_BRICK_SLAB.get(), RUNESTONE_TILE_SLAB.get(), MOSSY_RUNESTONE_SLAB.get(), WILDWOOD_BUTTON.get());
    this.tag(BlockTags.WOODEN_SLABS).add(WILDWOOD_SLAB.get());
    this.tag(BlockTags.WOODEN_STAIRS).add(WILDWOOD_STAIRS.get());
    this.tag(BlockTags.STAIRS)
        .add(RUNESTONE_STAIRS.get(), RUNESTONE_BRICK_STAIRS.get(), RUNESTONE_TILE_STAIRS.get(), MOSSY_RUNESTONE_STAIRS.get(), RUNED_STAIRS.get(), RUNED_TILE_STAIRS.get(), RUNED_BRICK_STAIRS.get());

    this.tag(BlockTags.UNSTABLE_BOTTOM_CENTER).add(WILDWOOD_GATE.get());

    this.tag(BlockTags.WALLS)
        .add(RUNED_WALL.get(), RUNED_BRICK_WALL.get(), RUNED_TILE_WALL.get(), RUNESTONE_WALL.get(), RUNESTONE_BRICK_WALL.get(), RUNESTONE_TILE_WALL.get(), MOSSY_RUNESTONE_WALL.get());

    this.tag(BlockTags.CLIMBABLE).add(WILDWOOD_LADDER.get());
    this.tag(BlockTags.WOODEN_FENCES).add(WILDWOOD_FENCE.get());
    this.tag(Tags.Blocks.FENCES_WOODEN).add(WILDWOOD_FENCE.get());

    this.tag(BlockTags.FENCE_GATES).add(WILDWOOD_GATE.get());
    this.tag(Tags.Blocks.FENCE_GATES_WOODEN).add(WILDWOOD_GATE.get());

    this.tag(Tags.Blocks.ORES).addTags(RootsTags.Blocks.SILVER_ORE, RootsTags.Blocks.QUARTZ_ORE);

    this.tag(BlockTags.BUTTONS)
        .add(RUNED_BUTTON.get(), RUNED_TILE_BUTTON.get(), RUNED_BRICK_BUTTON.get(), MOSSY_RUNESTONE_BUTTON.get(), RUNESTONE_BUTTON.get(), RUNESTONE_BRICK_BUTTON.get(), RUNESTONE_TILE_BUTTON.get());
    this.tag(BlockTags.WOODEN_BUTTONS).add(WILDWOOD_BUTTON.get());
    this.tag(BlockTags.STONE_PRESSURE_PLATES)
        .add(RUNED_PRESSURE_PLATE.get(), RUNED_TILE_PRESSURE_PLATE.get(), RUNED_BRICK_PRESSURE_PLATE.get(), MOSSY_RUNESTONE_PRESSURE_PLATE.get(), RUNESTONE_PRESSURE_PLATE.get(), RUNESTONE_BRICK_PRESSURE_PLATE.get(), RUNESTONE_TILE_PRESSURE_PLATE.get());
    this.tag(BlockTags.WOODEN_PRESSURE_PLATES).add(WILDWOOD_PRESSURE_PLATE.get());

    this.tag(BlockTags.WOODEN_DOORS).add(WILDWOOD_DOOR.get());
    this.tag(BlockTags.WOODEN_TRAPDOORS).add(WILDWOOD_TRAPDOOR.get());
    this.tag(BlockTags.BEACON_BASE_BLOCKS).add(SILVER_BLOCK.get());
    this.tag(BlockTags.NEEDS_IRON_TOOL)
        .add(RUNED_OBSIDIAN.get(), CHISELED_RUNED_OBSIDIAN.get(), RUNED_BRICK.get(), RUNED_TILE.get(), SILVER_ORE.get(), DEEPSLATE_SILVER_ORE.get(), SILVER_BLOCK.get());
    this.tag(BlockTags.DRAGON_IMMUNE)
        .add(RUNED_OBSIDIAN.get(), CHISELED_RUNED_OBSIDIAN.get(), RUNED_BRICK.get(), RUNED_TILE.get(), RUNED_STAIRS.get(), RUNED_TILE_STAIRS.get(), RUNED_BRICK_STAIRS.get(), RUNED_SLAB.get(), RUNED_BRICK_SLAB.get(), RUNED_TILE_SLAB.get(), RUNED_BUTTON.get(), RUNED_TILE_BUTTON.get(), RUNED_BRICK_BUTTON.get(), RUNED_PRESSURE_PLATE.get(), RUNED_TILE_PRESSURE_PLATE.get(), RUNED_BRICK_PRESSURE_PLATE.get(), RUNED_WALL.get(), RUNED_BRICK_WALL.get(), RUNED_TILE_WALL.get(), REINFORCED_RITUAL_PEDESTAL.get(), REINFORCED_PYRE.get(), REINFORCED_SOUL_PYRE.get());
    this.tag(BlockTags.WITHER_IMMUNE)
        .add(RUNED_OBSIDIAN.get(), CHISELED_RUNED_OBSIDIAN.get(), RUNED_BRICK.get(), RUNED_TILE.get(), RUNED_STAIRS.get(), RUNED_TILE_STAIRS.get(), RUNED_BRICK_STAIRS.get(), RUNED_SLAB.get(), RUNED_BRICK_SLAB.get(), RUNED_TILE_SLAB.get(), RUNED_BUTTON.get(), RUNED_TILE_BUTTON.get(), RUNED_BRICK_BUTTON.get(), RUNED_PRESSURE_PLATE.get(), RUNED_TILE_PRESSURE_PLATE.get(), RUNED_BRICK_PRESSURE_PLATE.get(), RUNED_WALL.get(), RUNED_BRICK_WALL.get(), RUNED_TILE_WALL.get(), REINFORCED_RITUAL_PEDESTAL.get(), REINFORCED_PYRE.get(), REINFORCED_SOUL_PYRE.get());
    this.tag(BlockTags.PLANKS).add(WILDWOOD_PLANKS.get());
    this.tag(BlockTags.SAPLINGS).add(WILDWOOD_SAPLING.get());
    this.tag(BlockTags.LEAVES).add(WILDWOOD_LEAVES.get());

    this.tag(BlockTags.MINEABLE_WITH_PICKAXE).addTag(RootsTags.Blocks.RITUAL_PEDESTALS);
    this.tag(BlockTags.MINEABLE_WITH_AXE).addTag(RootsTags.Blocks.GROVE_PEDESTALS);

    this.tag(RootsTags.Blocks.STONEPETAL).add(STONEPETAL.get());
    this.tag(RootsTags.Blocks.RAW_SILVER_STORAGE).add(RAW_SILVER_BLOCK.get());
    this.tag(RootsTags.Blocks.SILVER_STORAGE).add(SILVER_BLOCK.get());
    this.tag(RootsTags.Blocks.SILVER_ORE).add(SILVER_ORE.get(), DEEPSLATE_SILVER_ORE.get());
    this.tag(RootsTags.Blocks.WILDWOOD_LOGS)
        .add(WILDWOOD_LOG.get(), STRIPPED_WILDWOOD_LOG.get(), STRIPPED_WILDWOOD_WOOD.get(), WILDWOOD_WOOD.get());
    this.tag(RootsTags.Blocks.WILDWOOD_PLANKS)
        .add(WILDWOOD_PLANKS.get());
    this.tag(RootsTags.Blocks.WILDWOOD_CHESTS)
        .add(WILDWOOD_CHEST.get());
    this.tag(Tags.Blocks.CHESTS_WOODEN).addTag(RootsTags.Blocks.WILDWOOD_CHESTS);
    this.tag(RootsTags.Blocks.QUARTZ_ORE).add(GRANITE_QUARTZ_ORE.get());
    this.tag(RootsTags.Blocks.RUNED_OBSIDIAN)
        .add(RUNED_OBSIDIAN.get(), CHISELED_RUNED_OBSIDIAN.get(), RUNED_BRICK.get(), RUNED_TILE.get());
    this.tag(RootsTags.Blocks.RUNED_CAPSTONES).add(CHISELED_RUNED_OBSIDIAN.get());
    this.tag(RootsTags.Blocks.RUNED_PILLARS).add(RUNED_OBSIDIAN.get(), RUNED_BRICK.get(), RUNED_TILE.get());
    this.tag(RootsTags.Blocks.RUNESTONE)
        .add(RUNESTONE.get(), RUNESTONE_BRICK.get(), CHISELED_RUNESTONE.get(), MOSSY_RUNESTONE.get(), RUNESTONE_TILE.get());
    this.tag(RootsTags.Blocks.RUNE_CAPSTONES).add(CHISELED_RUNESTONE.get());
    this.tag(RootsTags.Blocks.RUNE_PILLARS)
        .add(RUNESTONE.get(), RUNESTONE_BRICK.get(), MOSSY_RUNESTONE.get(), RUNESTONE_TILE.get());
    this.tag(RootsTags.Blocks.BASE_ELEMENTAL_SOIL).add(ELEMENTAL_SOIL.get());
    this.tag(RootsTags.Blocks.AIR_SOIL).add(CAELIC_SOIL.get());
    this.tag(RootsTags.Blocks.FIRE_SOIL).add(MAGMATIC_SOIL.get());
    this.tag(RootsTags.Blocks.WATER_SOIL).add(AQUEOUS_SOIL.get());
    this.tag(RootsTags.Blocks.EARTH_SOIL).add(TERRAN_SOIL.get());
    this.tag(BlockTags.DIRT).add(ENCHANTED_TURF.get());
    //noinspection unchecked
    this.tag(RootsTags.Blocks.ELEMENTAL_SOIL)
        .addTags(RootsTags.Blocks.AIR_SOIL, RootsTags.Blocks.FIRE_SOIL, RootsTags.Blocks.WATER_SOIL, RootsTags.Blocks.EARTH_SOIL);
    this.tag(RootsTags.Blocks.ALL_SOIL).addTags(RootsTags.Blocks.ELEMENTAL_SOIL, RootsTags.Blocks.BASE_ELEMENTAL_SOIL);
    this.tag(RootsTags.Blocks.SOILS).addTag(RootsTags.Blocks.ELEMENTAL_SOIL);
    this.tag(RootsTags.Blocks.NYI).add(INCENSE_BURNER.get(), UNENDING_BOWL.get());
    this.tag(RootsTags.Blocks.WIP).addTag(RootsTags.Blocks.SOILS);
    this.tag(RootsTags.Blocks.WIP).add(RUNESTONE_TICKER.get());
    this.tag(RootsTags.Blocks.AMPLIFIERS);
    this.tag(RootsTags.Blocks.DISPLAY_PEDESTALS).add(DISPLAY_PEDESTAL.get());
    this.tag(Tags.Blocks.CHESTS_WOODEN).add(WILDWOOD_CHEST.get());
    this.tag(RootsTags.Blocks.RITUAL_PEDESTALS).add(RITUAL_PEDESTAL.get(), REINFORCED_RITUAL_PEDESTAL.get());
    this.tag(RootsTags.Blocks.GROVE_PEDESTALS).add(GROVE_PEDESTAL.get(), WILDWOOD_PEDESTAL.get());
    //noinspection unchecked
    this.tag(RootsTags.Blocks.PEDESTALS)
        .addTags(RootsTags.Blocks.RITUAL_PEDESTALS, RootsTags.Blocks.GROVE_PEDESTALS, RootsTags.Blocks.DISPLAY_PEDESTALS, RootsTags.Blocks.LIMITED_PEDESTALS)
        .add(INCENSE_BURNER.get());

    this.tag(RootsTags.Blocks.LIMITED_PEDESTALS).add(GROVE_PEDESTAL.get(), DISPLAY_PEDESTAL.get());

    this.tag(RootsTags.Blocks.GROVE_CRAFTERS).add(GROVE_CRAFTER.get());

    this.tag(RootsTags.Blocks.GROVE_MOSS).add(CREEPING_GROVE_MOSS.get(), HANGING_GROVE_MOSS.get());

    this.tag(RootsTags.Blocks.GROVE_STONE_WILD).add(WILD_GROVE_STONE.get());
    this.tag(RootsTags.Blocks.GROVE_STONE_PRIMAL).add(PRIMAL_GROVE_STONE.get());
    this.tag(RootsTags.Blocks.GROVE_STONE_FAIRY).add(FAIRY_GROVE_STONE.get());
    this.tag(RootsTags.Blocks.GROVE_STONE_SPROUTING).add(SPROUTING_GROVE_STONE.get());
    this.tag(RootsTags.Blocks.GROVE_STONE_TWILIGHT).add(TWILIGHT_GROVE_STONE.get());
    this.tag(RootsTags.Blocks.GROVE_STONE_FUNGAL).add(FUNGAL_GROVE_STONE.get());
    this.tag(RootsTags.Blocks.GROVE_STONE_ELEMENTAL).add(ELEMENTAL_GROVE_STONE.get());
    this.tag(RootsTags.Blocks.GROVE_STONES)
        .addTags(RootsTags.Blocks.GROVE_STONE_PRIMAL, RootsTags.Blocks.GROVE_STONE_WILD, RootsTags.Blocks.GROVE_STONE_FAIRY, RootsTags.Blocks.GROVE_STONE_SPROUTING, RootsTags.Blocks.GROVE_STONE_TWILIGHT, RootsTags.Blocks.GROVE_STONE_FUNGAL, RootsTags.Blocks.GROVE_STONE_ELEMENTAL);
    this.tag(RootsTags.Blocks.RED_HUTS).add(RED_FAIRY_HUT.get());
    this.tag(RootsTags.Blocks.BROWN_HUTS).add(BROWN_FAIRY_HUT.get());
    this.tag(RootsTags.Blocks.BAFFLECAP_HUTS).add(BAFFLECAP_FAIRY_HUT.get());
    this.tag(RootsTags.Blocks.CRIMSON_HUTS).add(CRIMSON_FAIRY_HUT.get());
    this.tag(RootsTags.Blocks.WARPED_HUTS).add(WARPED_FAIRY_HUT.get());
    this.tag(RootsTags.Blocks.HUTS)
        .addTags(RootsTags.Blocks.RED_HUTS, RootsTags.Blocks.BROWN_HUTS, RootsTags.Blocks.BAFFLECAP_HUTS, RootsTags.Blocks.CRIMSON_HUTS, RootsTags.Blocks.WARPED_HUTS);

    this.tag(RootsTags.Blocks.MORTARS).add(MORTAR.get());

    this.tag(RootsTags.Blocks.FUNCTIONAL_PYRES)
        .add(PYRE.get(), REINFORCED_PYRE.get(), SOUL_PYRE.get(), REINFORCED_SOUL_PYRE.get());
    this.tag(RootsTags.Blocks.DECORATIVE_PYRES).add(DECORATIVE_PYRE.get(), DECORATIVE_SOUL_PYRE.get());

    this.tag(RootsTags.Blocks.WILDROOT_CROP).add(WILDROOT_CROP.get());
    this.tag(RootsTags.Blocks.CLOUD_BERRY_CROP).add(CLOUD_BERRY_CROP.get());
    this.tag(RootsTags.Blocks.DEWGONIA_CROP).add(DEWGONIA_CROP.get());
    this.tag(RootsTags.Blocks.INFERNO_BULB_CROP).add(INFERNO_BULB_CROP.get());
    this.tag(RootsTags.Blocks.SPIRITLEAF_CROP).add(SPIRITLEAF_CROP.get());
    this.tag(RootsTags.Blocks.MOONGLOW_CROP).add(MOONGLOW_CROP.get());
    this.tag(RootsTags.Blocks.PERESKIA_CROP).add(PERESKIA_CROP.get());
    this.tag(RootsTags.Blocks.STALICRIPE_CROP).add(STALICRIPE_CROP.get());
    this.tag(RootsTags.Blocks.WILDEWHEET_CROP).add(WILDEWHEET_CROP.get());

    this.tag(RootsTags.Blocks.AIR_CROPS).addTag(RootsTags.Blocks.CLOUD_BERRY_CROP);
    this.tag(RootsTags.Blocks.EARTH_CROPS).addTag(RootsTags.Blocks.STALICRIPE_CROP);
    this.tag(RootsTags.Blocks.FIRE_CROPS).addTag(RootsTags.Blocks.INFERNO_BULB_CROP);
    this.tag(RootsTags.Blocks.WATER_CROPS).addTag(RootsTags.Blocks.DEWGONIA_CROP);

    //noinspection unchecked
    this.tag(RootsTags.Blocks.ELEMENTAL_CROPS)
        .addTags(RootsTags.Blocks.AIR_CROPS, RootsTags.Blocks.EARTH_CROPS, RootsTags.Blocks.FIRE_CROPS, RootsTags.Blocks.WATER_CROPS);

    //noinspection unchecked
    this.tag(RootsTags.Blocks.CROPS)
        .addTags(RootsTags.Blocks.ELEMENTAL_CROPS, RootsTags.Blocks.WILDEWHEET_CROP, RootsTags.Blocks.WILDROOT_CROP, RootsTags.Blocks.MOONGLOW_CROP, RootsTags.Blocks.PERESKIA_CROP, RootsTags.Blocks.SPIRITLEAF_CROP);

    // From the old ModTags
    this.tag(RootsTags.Blocks.STANDING_STONE_CROPS).add(Blocks.BEETROOTS, Blocks.POTATOES, Blocks.CARROTS);
    this.tag(RootsTags.Blocks.BAFFLECAP_CONVERSION)
        .add(Blocks.BROWN_MUSHROOM, Blocks.RED_MUSHROOM, Blocks.WARPED_FUNGUS, Blocks.CRIMSON_FUNGUS);
    // This is for loot modifiers
    this.tag(RootsTags.Blocks.SHORT_GRASS).add(Blocks.SHORT_GRASS, Blocks.FERN);
    this.tag(RootsTags.Blocks.TALL_GRASS).add(Blocks.TALL_GRASS, Blocks.LARGE_FERN);
    this.tag(RootsTags.Blocks.LEVERS).add(Blocks.LEVER);
    this.tag(RootsTags.Blocks.GROWTH_FORCE);
    this.tag(RootsTags.Blocks.GROWTH_BLACKLIST);
    this.tag(RootsTags.Blocks.GROWTH_REDUCE);

    this.tag(RootsTags.Blocks.SUPPORTS_HELL_SPROUT_SPAWN)
        .add(Blocks.NETHERRACK, Blocks.NETHER_BRICKS, Blocks.RED_NETHER_BRICKS, Blocks.MAGMA_BLOCK, Blocks.OBSIDIAN, Blocks.CRIMSON_NYLIUM, Blocks.WARPED_NYLIUM);
    this.tag(RootsTags.Blocks.SUPPORTS_MELODY_SPROUT_SPAWN).add(Blocks.END_STONE, Blocks.PURPUR_BLOCK);
    this.tag(RootsTags.Blocks.SUPPORTS_SNOW_SPROUT_SPAWN)
        .add(Blocks.SNOW, Blocks.SNOW_BLOCK, Blocks.ICE, Blocks.PACKED_ICE, Blocks.BLUE_ICE, Blocks.FROSTED_ICE);

    // TODO: CHECK
    this.tag(RootsTags.Blocks.SUPPORTS_STONEPETAL).addTag(BlockTags.STONE_ORE_REPLACEABLES).addTag(Tags.Blocks.GRAVELS);
    this.tag(RootsTags.Blocks.SUPPORTS_WILD_AUBERGINE).addTag(BlockTags.DIRT);
    //noinspection unchecked
    this.tag(RootsTags.Blocks.SUPPORTS_WILD_ROOTS)
        .addTags(BlockTags.BASE_STONE_OVERWORLD, BlockTags.DIRT, BlockTags.MOSS_REPLACEABLE)
        .add(Blocks.MOSS_BLOCK, Blocks.SNOW_BLOCK, Blocks.GRAVEL, Blocks.CLAY);
    //noinspection unchecked
    this.tag(RootsTags.Blocks.SUPPORTS_HANGING_MOSS).addTags(BlockTags.LOGS);
    //noinspection unchecked
    this.tag(RootsTags.Blocks.PEDESTALS).addTags(RootsTags.Blocks.RITUAL_PEDESTALS, RootsTags.Blocks.GROVE_PEDESTALS);

    this.tag(BlockTags.CROPS).addTag(RootsTags.Blocks.CROPS);

    this.tag(RootsTags.Blocks.PYRES).addTags(RootsTags.Blocks.FUNCTIONAL_PYRES, RootsTags.Blocks.DECORATIVE_PYRES);

    // Logs that burn
    //noinspection unchecked
    this.tag(BlockTags.LOGS_THAT_BURN).addTags(RootsTags.Blocks.WILDWOOD_LOGS);
    //noinspection unchecked
    this.tag(BlockTags.LOGS).addTags(RootsTags.Blocks.WILDWOOD_LOGS);

    //noinspection unchecked
    this.tag(RootsTags.Blocks.RUNES_CAPSTONES)
        .addTags(RootsTags.Blocks.RUNE_CAPSTONES, RootsTags.Blocks.RUNED_CAPSTONES);
    //noinspection unchecked
    this.tag(RootsTags.Blocks.CAPSTONES).addTags(RootsTags.Blocks.RUNES_CAPSTONES);
    //noinspection unchecked
    this.tag(RootsTags.Blocks.RUNES_PILLARS).addTags(RootsTags.Blocks.RUNE_PILLARS, RootsTags.Blocks.RUNED_PILLARS);
    //noinspection unchecked
    this.tag(RootsTags.Blocks.PILLARS).addTags(RootsTags.Blocks.RUNES_PILLARS);

    //noinspection unchecked
    this.tag(BlockTags.MINEABLE_WITH_AXE).addTags(RootsTags.Blocks.GROVE_PEDESTALS, RootsTags.Blocks.DISPLAY_PEDESTALS)
        .add(GROVE_CRAFTER.get());

    this.tag(RootsTags.Blocks.GRANITE_ORE_REPLACEABLES).add(Blocks.GRANITE);
    this.tag(RootsTags.Blocks.BLOOMING_ELIGIBLE_FLOWERS).add(
        Blocks.DANDELION,
        Blocks.POPPY,
        Blocks.BLUE_ORCHID,
        Blocks.ALLIUM,
        Blocks.AZURE_BLUET,
        Blocks.RED_TULIP,
        Blocks.ORANGE_TULIP,
        Blocks.WHITE_TULIP,
        Blocks.PINK_TULIP,
        Blocks.OXEYE_DAISY,
        Blocks.CORNFLOWER,
        Blocks.LILY_OF_THE_VALLEY,
        STONEPETAL.get()
    );
    this.tag(RootsTags.Blocks.BLOOMING_ELIGIBLE_PEDESTAL_FLOWERS).add(
        Blocks.DANDELION,
        Blocks.POPPY,
        Blocks.BLUE_ORCHID,
        Blocks.ALLIUM,
        Blocks.AZURE_BLUET,
        Blocks.RED_TULIP,
        Blocks.ORANGE_TULIP,
        Blocks.WHITE_TULIP,
        Blocks.PINK_TULIP,
        Blocks.OXEYE_DAISY,
        Blocks.CORNFLOWER,
        Blocks.LILY_OF_THE_VALLEY,
        STONEPETAL.get(),
        Blocks.TORCHFLOWER,
        Blocks.WITHER_ROSE,
        Blocks.SUNFLOWER, Blocks.LILAC, Blocks.PEONY, Blocks.ROSE_BUSH, Blocks.PITCHER_PLANT // Tall flowers
    );
    this.tag(RootsTags.Blocks.BLOOMING_INELIGIBLE_BLOCKS).addTag(RootsTags.Blocks.ELEMENTAL_SOIL);

    this.tag(BlockTags.MAINTAINS_FARMLAND).addTag(RootsTags.Blocks.CROPS);

    this.tag(BlockTags.BIG_DRIPLEAF_PLACEABLE).addTag(RootsTags.Blocks.SOILS);

    this.tag(BlockTags.MUSHROOM_GROW_BLOCK).addTag(RootsTags.Blocks.SOILS);

    this.tag(RootsTags.Blocks.SOIL_ELIGIBLE_CROPS).add(
        // Classic crops
        Blocks.WHEAT,
        Blocks.BEETROOTS,
        Blocks.CARROTS,
        Blocks.POTATOES,
        Blocks.MELON_STEM,
        Blocks.MELON,
        Blocks.ATTACHED_MELON_STEM,
        Blocks.ATTACHED_PUMPKIN_STEM,
        Blocks.PUMPKIN_STEM,
        Blocks.PUMPKIN,
        Blocks.TORCHFLOWER_CROP,
        Blocks.PITCHER_CROP,
        // Non-traditional crops
        Blocks.NETHER_WART,
        // Mushrooms
        Blocks.RED_MUSHROOM,
        Blocks.BROWN_MUSHROOM,
        Blocks.CRIMSON_FUNGUS,
        Blocks.WARPED_FUNGUS,
        BAFFLECAP.get(),
        // Berry bushes
        Blocks.SWEET_BERRY_BUSH
    ).addTag(
        RootsTags.Blocks.CROPS
    );

    tag(RootsTags.Blocks.SPREADING_MUSHROOMS).add(Blocks.RED_MUSHROOM, Blocks.BROWN_MUSHROOM, BAFFLECAP.get());

    tag(RootsTags.Blocks.FORAGEABLE_SINGLE_BLOCKS).addTags(BlockTags.LEAVES, RootsTags.Blocks.SHORT_GRASS)
        .add(Blocks.SEAGRASS, Blocks.VINE, Blocks.CAVE_VINES, Blocks.DEAD_BUSH, Blocks.HANGING_ROOTS);

    tag(RootsTags.Blocks.FORAGEABLE_DOUBLE_BLOCKS).addTags(RootsTags.Blocks.TALL_GRASS).add(Blocks.TALL_SEAGRASS);
    tag(RootsTags.Blocks.FORAGEABLES).addTags(RootsTags.Blocks.FORAGEABLE_DOUBLE_BLOCKS, RootsTags.Blocks.FORAGEABLE_SINGLE_BLOCKS);
    tag(RootsTags.Blocks.SHATTER_EXCLUDE);
    tag(RootsTags.Blocks.SHATTER_INCLUDE);
    tag(RootsTags.Blocks.RAMPANT_GROWTH_EXCLUDE_MODE).add(Blocks.VINE);
    tag(RootsTags.Blocks.ALLOW_CASTING_TOOL_RIGHT_CLICK);

    tag(RootsTags.Blocks.ELEMENTAL_REPUTATION_CROPS).addTag(RootsTags.Blocks.ELEMENTAL_CROPS);
    // All crops for sprouting
    tag(RootsTags.Blocks.SPROUT_REPUTATION_CROPS).addTags(BlockTags.CROPS);
    tag(RootsTags.Blocks.FAIRY_REPUTATION_CROPS).addTags(RootsTags.Blocks.PERESKIA_CROP);
    tag(RootsTags.Blocks.WILD_REPUTATION_CROPS).addTags(RootsTags.Blocks.SPIRITLEAF_CROP);
    tag(RootsTags.Blocks.TWILIGHT_REPUTATION_CROPS).addTags(RootsTags.Blocks.MOONGLOW_CROP);
    tag(RootsTags.Blocks.UNDERWATER_FARMLAND).add(Blocks.DIRT, Blocks.DIRT_PATH, Blocks.COARSE_DIRT, Blocks.PODZOL, Blocks.MYCELIUM, Blocks.GRASS_BLOCK);
    tag(RootsTags.Blocks.FARMLANDS).add(Blocks.FARMLAND).addTag(RootsTags.Blocks.ALL_SOIL);
    tag(BlockTags.MUSHROOM_GROW_BLOCK).addTag(RootsTags.Blocks.ALL_SOIL);

    tag(BlockTags.SMALL_FLOWERS).addTag(RootsTags.Blocks.STONEPETAL);

    tag(RootsTags.Blocks.ELEMENTAL_GROVE_GENERATORS).add(Blocks.SNOW_BLOCK, Blocks.MAGMA_BLOCK, Blocks.MUD, Blocks.OBSIDIAN, Blocks.ICE, Blocks.PACKED_ICE, Blocks.PACKED_MUD, Blocks.BLUE_ICE, Blocks.END_STONE)
        .addTags(RootsTags.Blocks.ALL_SOIL);
    tag(RootsTags.Blocks.ELEMENTAL_GROVE_LIQUID_GENERATORS).add(Blocks.LAVA, Blocks.WATER);
    tag(RootsTags.Blocks.FAIRY_GROVE_GENERATORS).addTags(BlockTags.SMALL_FLOWERS, BlockTags.TALL_FLOWERS);
    tag(RootsTags.Blocks.FAIRY_GROVE_PATHS).add(Blocks.DIRT_PATH);
    tag(RootsTags.Blocks.SPROUTING_GROVE_GENERATORS).addTags(BlockTags.CROPS);
    tag(RootsTags.Blocks.WILD_GROVE_GENERATORS).add(Blocks.DRAGON_EGG);
    // TODO: Separate into two seaparte tags
    tag(RootsTags.Blocks.FUNGAL_GROVE_DIRT_GENERATORS).add(Blocks.MYCELIUM, Blocks.PODZOL);
    tag(RootsTags.Blocks.FUNGAL_GROVE_OTHER_GENERATORS).add(Blocks.MUSHROOM_STEM, Blocks.BROWN_MUSHROOM_BLOCK, Blocks.RED_MUSHROOM_BLOCK, BAFFLECAP_BLOCK.get());
    tag(RootsTags.Blocks.FUNGAL_GROVE_MUSHROOM_GENERATORS).add(Blocks.BROWN_MUSHROOM, Blocks.RED_MUSHROOM, BAFFLECAP.get(), Blocks.WARPED_FUNGUS, Blocks.CRIMSON_FUNGUS);
    tag(RootsTags.Blocks.TWILIGHT_GROVE_GENERATORS).add(Blocks.CREEPER_HEAD, Blocks.CREEPER_WALL_HEAD, Blocks.DRAGON_HEAD, Blocks.DRAGON_WALL_HEAD, Blocks.PIGLIN_WALL_HEAD, Blocks.PIGLIN_HEAD, Blocks.PLAYER_HEAD, Blocks.PLAYER_WALL_HEAD, Blocks.ZOMBIE_HEAD, Blocks.ZOMBIE_WALL_HEAD);
    tag(RootsTags.Blocks.CREATIVE_GROVE_GENERATORS).add(Blocks.BEDROCK);

    tag(RootsTags.Blocks.PYRE_HUD_RENDERER).addTag(RootsTags.Blocks.FUNCTIONAL_PYRES);
    tag(RootsTags.Blocks.GROVE_CRAFTER_HUD_RENDERER).addTag(RootsTags.Blocks.GROVE_CRAFTERS);
    tag(RootsTags.Blocks.MORTAR_HUD_RENDERER).addTag(RootsTags.Blocks.MORTARS);
    tag(RootsTags.Blocks.GROVE_STONE_HUD_RENDERER).addTag(RootsTags.Blocks.GROVE_STONES);
    tag(RootsTags.Blocks.TRANSMUTER_HUD_RENDERER).add(FUNGAL_TRANSMUTER.get());

    tag(RootsTags.Blocks.GROWTH_AMPLIFIER_GRASSES).add(Blocks.TALL_GRASS, Blocks.SHORT_GRASS, Blocks.FERN, Blocks.LARGE_FERN);

    tag(RootsTags.Blocks.GROVE_CONSUMERS).addTag(RootsTags.Blocks.HUTS)
        .add(ENCHANTED_TURF.get(), FUNGAL_TRANSMUTER.get());

    tag(RootsTags.Blocks.NETHER_DOORS).add(Blocks.CRIMSON_DOOR, Blocks.WARPED_DOOR);

    this.tag(RootsTags.Blocks.TIER_00_SHATTER).add(Blocks.COBBLESTONE, Blocks.NETHERRACK).addTag(BlockTags.LEAVES);
    this.tag(RootsTags.Blocks.TIER_05_SHATTER).addTag(BlockTags.BASE_STONE_OVERWORLD).addTag(BlockTags.BASE_STONE_NETHER).addTag(BlockTags.SAND).addTag(Tags.Blocks.GRAVELS);
  }

  @Override
  public String getName() {
    return "Roots Block Tags";
  }
}
