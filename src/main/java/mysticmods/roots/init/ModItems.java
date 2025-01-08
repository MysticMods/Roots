package mysticmods.roots.init;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.item.*;
import mysticmods.roots.item.living.*;
import net.minecraft.world.item.*;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModItems {
  private static final DeferredRegister<Item> ITEMS = DeferredRegister.createItems(RootsAPI.MODID);
/*  private static <T extends Item> NonNullBiConsumer<DataGenContext<Item, T>, RegistrateItemModelProvider> subfolder(String subfolder) {
    return (ctx, p) -> p.generated(ctx::getEntry, RootsAPI.rl("item/" + subfolder + "/" + ctx.getName()));
  }*/

  private static final Item.Properties DEFAULT = new Item.Properties();


  // Block items
  public static DeferredHolder<Item, BlockItem> THATCH = ITEMS.register("thatch", () -> new BlockItem(ModBlocks.THATCH.get(), DEFAULT));
  public static DeferredHolder<Item, BlockItem> RUNESTONE = ITEMS.register("runestone", () -> new BlockItem(ModBlocks.RUNESTONE.get(), DEFAULT));
  public static DeferredHolder<Item, BlockItem> MOSSY_RUNESTONE = ITEMS.register("mossy_runestone", () -> new BlockItem(ModBlocks.MOSSY_RUNESTONE.get(), DEFAULT));
  public static DeferredHolder<Item, BlockItem> CHISELED_RUNESTONE = ITEMS.register("chiseled_runestone", () -> new BlockItem(ModBlocks.CHISELED_RUNESTONE.get(), DEFAULT));
  public static DeferredHolder<Item, BlockItem> RUNESTONE_BRICK = ITEMS.register("runestone_brick", () -> new BlockItem(ModBlocks.RUNESTONE_BRICK.get(), DEFAULT));
  public static DeferredHolder<Item, BlockItem> RUNESTONE_TILE = ITEMS.register("runestone_tile", () -> new BlockItem(ModBlocks.RUNESTONE_TILE.get(), DEFAULT));
  public static DeferredHolder<Item, BlockItem> RUNED_OBSIDIAN = ITEMS.register("runed_obsidian", () -> new BlockItem(ModBlocks.RUNED_OBSIDIAN.get(), DEFAULT));
  public static DeferredHolder<Item, BlockItem> CHISELED_RUNED_OBSIDIAN = ITEMS.register("chiseled_runed_obsidian", () -> new BlockItem(ModBlocks.CHISELED_RUNED_OBSIDIAN.get(), DEFAULT));
  public static DeferredHolder<Item, BlockItem> RUNED_BRICK = ITEMS.register("runed_brick", () -> new BlockItem(ModBlocks.RUNED_BRICK.get(), DEFAULT));
  public static DeferredHolder<Item, BlockItem> RUNED_TILE = ITEMS.register("runed_tile", () -> new BlockItem(ModBlocks.RUNED_TILE.get(), DEFAULT));
  public static DeferredHolder<Item, BlockItem> SILVER_ORE = ITEMS.register("silver_ore", () -> new BlockItem(ModBlocks.SILVER_ORE.get(), DEFAULT));
  public static DeferredHolder<Item, BlockItem> DEEPSLATE_SILVER_ORE = ITEMS.register("deepslate_silver_ore", () -> new BlockItem(ModBlocks.DEEPSLATE_SILVER_ORE.get(), DEFAULT));
  public static DeferredHolder<Item, BlockItem> GRANITE_QUARTZ_ORE = ITEMS.register("granite_quartz_ore", () -> new BlockItem(ModBlocks.GRANITE_QUARTZ_ORE.get(), DEFAULT));
  public static DeferredHolder<Item, BlockItem> RAW_SILVER_BLOCK = ITEMS.register("raw_silver_block", () -> new BlockItem(ModBlocks.RAW_SILVER_BLOCK.get(), DEFAULT));
  public static DeferredHolder<Item, BlockItem> SILVER_BLOCK = ITEMS.register("silver_block", () -> new BlockItem(ModBlocks.SILVER_BLOCK.get(), DEFAULT));
  public static DeferredHolder<Item, BlockItem> WILDWOOD_LOG = ITEMS.register("wildwood_log", () -> new BlockItem(ModBlocks.WILDWOOD_LOG.get(), DEFAULT));
  public static DeferredHolder<Item, BlockItem> STRIPPED_WILDWOOD_LOG = ITEMS.register("stripped_wildwood_log", () -> new BlockItem(ModBlocks.STRIPPED_WILDWOOD_LOG.get(), DEFAULT));
  public static DeferredHolder<Item, BlockItem> WILDWOOD_WOOD = ITEMS.register("wildwood_wood", () -> new BlockItem(ModBlocks.WILDWOOD_WOOD.get(), DEFAULT));
  public static DeferredHolder<Item, BlockItem> STRIPPED_WILDWOOD_WOOD = ITEMS.register("stripped_wildwood_wood", () -> new BlockItem(ModBlocks.STRIPPED_WILDWOOD_WOOD.get(), DEFAULT));
  public static DeferredHolder<Item, BlockItem> WILDWOOD_PLANKS = ITEMS.register("wildwood_planks", () -> new BlockItem(ModBlocks.WILDWOOD_PLANKS.get(), DEFAULT));
  public static DeferredHolder<Item, BlockItem> WILDWOOD_SAPLING = ITEMS.register("wildwood_sapling", () -> new BlockItem(ModBlocks.WILDWOOD_SAPLING.get(), DEFAULT));
  public static DeferredHolder<Item, BlockItem> STONEPETAL = ITEMS.register("stonepetal", () -> new BlockItem(ModBlocks.STONEPETAL.get(), DEFAULT));
  public static DeferredHolder<Item, BlockItem> WILDWOOD_LEAVES = ITEMS.register("wildwood_leaves", () -> new BlockItem(ModBlocks.WILDWOOD_LEAVES.get(), DEFAULT));
  // Runed wildwood log
  public static DeferredHolder<Item, BlockItem> RUNED_WILDWOOD_LOG = ITEMS.register("runed_wildwood_log", () -> new BlockItem(ModBlocks.RUNED_WILDWOOD_LOG.get(), DEFAULT));
  // Runed spruce log
  public static DeferredHolder<Item, BlockItem> RUNED_SPRUCE_LOG = ITEMS.register("runed_spruce_log", () -> new BlockItem(ModBlocks.RUNED_SPRUCE_LOG.get(), DEFAULT));
  // Runed jungle log
  public static DeferredHolder<Item, BlockItem> RUNED_JUNGLE_LOG = ITEMS.register("runed_jungle_log", () -> new BlockItem(ModBlocks.RUNED_JUNGLE_LOG.get(), DEFAULT));
  // Runed birch log
  public static DeferredHolder<Item, BlockItem> RUNED_BIRCH_LOG = ITEMS.register("runed_birch_log", () -> new BlockItem(ModBlocks.RUNED_BIRCH_LOG.get(), DEFAULT));
  // Runed oak log
  public static DeferredHolder<Item, BlockItem> RUNED_OAK_LOG = ITEMS.register("runed_oak_log", () -> new BlockItem(ModBlocks.RUNED_OAK_LOG.get(), DEFAULT));
  // Runed dark oak log
  public static DeferredHolder<Item, BlockItem> RUNED_DARK_OAK_LOG = ITEMS.register("runed_dark_oak_log", () -> new BlockItem(ModBlocks.RUNED_DARK_OAK_LOG.get(), DEFAULT));
  // Runed acacia log
  public static DeferredHolder<Item, BlockItem> RUNED_ACACIA_LOG = ITEMS.register("runed_acacia_log", () -> new BlockItem(ModBlocks.RUNED_ACACIA_LOG.get(), DEFAULT));
  // Runed mangrove log
  public static DeferredHolder<Item, BlockItem> RUNED_MANGROVE_LOG = ITEMS.register("runed_mangrove_log", () -> new BlockItem(ModBlocks.RUNED_MANGROVE_LOG.get(), DEFAULT));
  // Runed warped stem
  public static DeferredHolder<Item, BlockItem> RUNED_WARPED_STEM = ITEMS.register("runed_warped_stem", () -> new BlockItem(ModBlocks.RUNED_WARPED_STEM.get(), DEFAULT));
  // Runed crimson stem
  public static DeferredHolder<Item, BlockItem> RUNED_CRIMSON_STEM = ITEMS.register("runed_crimson_stem", () -> new BlockItem(ModBlocks.RUNED_CRIMSON_STEM.get(), DEFAULT));
  // Runestone stairs
  public static DeferredHolder<Item, BlockItem> RUNESTONE_STAIRS = ITEMS.register("runestone_stairs", () -> new BlockItem(ModBlocks.RUNESTONE_STAIRS.get(), DEFAULT));
  // Mossy runestone stairs
  public static DeferredHolder<Item, BlockItem> MOSSY_RUNESTONE_STAIRS = ITEMS.register("mossy_runestone_stairs", () -> new BlockItem(ModBlocks.MOSSY_RUNESTONE_STAIRS.get(), DEFAULT));
  // Runestone brick stairs
  public static DeferredHolder<Item, BlockItem> RUNESTONE_BRICK_STAIRS = ITEMS.register("runestone_brick_stairs", () -> new BlockItem(ModBlocks.RUNESTONE_BRICK_STAIRS.get(), DEFAULT));
  // Runestone tile stairs
  public static DeferredHolder<Item, BlockItem> RUNESTONE_TILE_STAIRS = ITEMS.register("runestone_tile_stairs", () -> new BlockItem(ModBlocks.RUNESTONE_TILE_STAIRS.get(), DEFAULT));
  // Runed stairs
  public static DeferredHolder<Item, BlockItem> RUNED_STAIRS = ITEMS.register("runed_stairs", () -> new BlockItem(ModBlocks.RUNED_STAIRS.get(), DEFAULT));
  // Runed brick stairs
  public static DeferredHolder<Item, BlockItem> RUNED_BRICK_STAIRS = ITEMS.register("runed_brick_stairs", () -> new BlockItem(ModBlocks.RUNED_BRICK_STAIRS.get(), DEFAULT));
  // Runed tile stairs
  public static DeferredHolder<Item, BlockItem> RUNED_TILE_STAIRS = ITEMS.register("runed_tile_stairs", () -> new BlockItem(ModBlocks.RUNED_TILE_STAIRS.get(), DEFAULT));
  // Wildwood stairs
  public static DeferredHolder<Item, BlockItem> WILDWOOD_STAIRS = ITEMS.register("wildwood_stairs", () -> new BlockItem(ModBlocks.WILDWOOD_STAIRS.get(), DEFAULT));
  // Runestone slab
  public static DeferredHolder<Item, BlockItem> RUNESTONE_SLAB = ITEMS.register("runestone_slab", () -> new BlockItem(ModBlocks.RUNESTONE_SLAB.get(), DEFAULT));
  // Mossy runestone slab
  public static DeferredHolder<Item, BlockItem> MOSSY_RUNESTONE_SLAB = ITEMS.register("mossy_runestone_slab", () -> new BlockItem(ModBlocks.MOSSY_RUNESTONE_SLAB.get(), DEFAULT));
  // Runestone brick slab
  public static DeferredHolder<Item, BlockItem> RUNESTONE_BRICK_SLAB = ITEMS.register("runestone_brick_slab", () -> new BlockItem(ModBlocks.RUNESTONE_BRICK_SLAB.get(), DEFAULT));
  // Runestone tile slab
  public static DeferredHolder<Item, BlockItem> RUNESTONE_TILE_SLAB = ITEMS.register("runestone_tile_slab", () -> new BlockItem(ModBlocks.RUNESTONE_TILE_SLAB.get(), DEFAULT));
  // Runed slab
  public static DeferredHolder<Item, BlockItem> RUNED_SLAB = ITEMS.register("runed_slab", () -> new BlockItem(ModBlocks.RUNED_SLAB.get(), DEFAULT));
  // Runed brick slab
  public static DeferredHolder<Item, BlockItem> RUNED_BRICK_SLAB = ITEMS.register("runed_brick_slab", () -> new BlockItem(ModBlocks.RUNED_BRICK_SLAB.get(), DEFAULT));
  // Runed tile slab
  public static DeferredHolder<Item, BlockItem> RUNED_TILE_SLAB = ITEMS.register("runed_tile_slab", () -> new BlockItem(ModBlocks.RUNED_TILE_SLAB.get(), DEFAULT));
  // Wildwood slab
  public static DeferredHolder<Item, BlockItem> WILDWOOD_SLAB = ITEMS.register("wildwood_slab", () -> new BlockItem(ModBlocks.WILDWOOD_SLAB.get(), DEFAULT));
  // Wildwood fence
  public static DeferredHolder<Item, BlockItem> WILDWOOD_FENCE = ITEMS.register("wildwood_fence", () -> new BlockItem(ModBlocks.WILDWOOD_FENCE.get(), DEFAULT));
  // Runestone button
  public static DeferredHolder<Item, BlockItem> RUNESTONE_BUTTON = ITEMS.register("runestone_button", () -> new BlockItem(ModBlocks.RUNESTONE_BUTTON.get(), DEFAULT));
  // Runestone brick button
  public static DeferredHolder<Item, BlockItem> RUNESTONE_BRICK_BUTTON = ITEMS.register("runestone_brick_button", () -> new BlockItem(ModBlocks.RUNESTONE_BRICK_BUTTON.get(), DEFAULT));
  // Runestone tile button
  public static DeferredHolder<Item, BlockItem> RUNESTONE_TILE_BUTTON = ITEMS.register("runestone_tile_button", () -> new BlockItem(ModBlocks.RUNESTONE_TILE_BUTTON.get(), DEFAULT));
  // Runed button
  public static DeferredHolder<Item, BlockItem> RUNED_BUTTON = ITEMS.register("runed_button", () -> new BlockItem(ModBlocks.RUNED_BUTTON.get(), DEFAULT));
  // Runed brick button
  public static DeferredHolder<Item, BlockItem> RUNED_BRICK_BUTTON = ITEMS.register("runed_brick_button", () -> new BlockItem(ModBlocks.RUNED_BRICK_BUTTON.get(), DEFAULT));
  // Runed tile button
  public static DeferredHolder<Item, BlockItem> RUNED_TILE_BUTTON = ITEMS.register("runed_tile_button", () -> new BlockItem(ModBlocks.RUNED_TILE_BUTTON.get(), DEFAULT));
  // Wildwood button
  public static DeferredHolder<Item, BlockItem> WILDWOOD_BUTTON = ITEMS.register("wildwood_button", () -> new BlockItem(ModBlocks.WILDWOOD_BUTTON.get(), DEFAULT));
  // Runestone pressure plate
  public static DeferredHolder<Item, BlockItem> RUNESTONE_PRESSURE_PLATE = ITEMS.register("runestone_pressure_plate", () -> new BlockItem(ModBlocks.RUNESTONE_PRESSURE_PLATE.get(), DEFAULT));
  // Runestone brick pressure plate
  public static DeferredHolder<Item, BlockItem> RUNESTONE_BRICK_PRESSURE_PLATE = ITEMS.register("runestone_brick_pressure_plate", () -> new BlockItem(ModBlocks.RUNESTONE_BRICK_PRESSURE_PLATE.get(), DEFAULT));
  // Runestone tile pressure plate
  public static DeferredHolder<Item, BlockItem> RUNESTONE_TILE_PRESSURE_PLATE = ITEMS.register("runestone_tile_pressure_plate", () -> new BlockItem(ModBlocks.RUNESTONE_TILE_PRESSURE_PLATE.get(), DEFAULT));
  // Runed pressure plate
  public static DeferredHolder<Item, BlockItem> RUNED_PRESSURE_PLATE = ITEMS.register("runed_pressure_plate", () -> new BlockItem(ModBlocks.RUNED_PRESSURE_PLATE.get(), DEFAULT));
  // Runed brick pressure plate
  public static DeferredHolder<Item, BlockItem> RUNED_BRICK_PRESSURE_PLATE = ITEMS.register("runed_brick_pressure_plate", () -> new BlockItem(ModBlocks.RUNED_BRICK_PRESSURE_PLATE.get(), DEFAULT));
  // Runed tile pressure plate
  public static DeferredHolder<Item, BlockItem> RUNED_TILE_PRESSURE_PLATE = ITEMS.register("runed_tile_pressure_plate", () -> new BlockItem(ModBlocks.RUNED_TILE_PRESSURE_PLATE.get(), DEFAULT));
  // Wildwood pressure plate
  public static DeferredHolder<Item, BlockItem> WILDWOOD_PRESSURE_PLATE = ITEMS.register("wildwood_pressure_plate", () -> new BlockItem(ModBlocks.WILDWOOD_PRESSURE_PLATE.get(), DEFAULT));
  // Wildwood door
  public static DeferredHolder<Item, BlockItem> WILDWOOD_DOOR = ITEMS.register("wildwood_door", () -> new BlockItem(ModBlocks.WILDWOOD_DOOR.get(), DEFAULT));
  // Wildwood trapdoor
  public static DeferredHolder<Item, BlockItem> WILDWOOD_TRAPDOOR = ITEMS.register("wildwood_trapdoor", () -> new BlockItem(ModBlocks.WILDWOOD_TRAPDOOR.get(), DEFAULT));
  // Wildwood ladder
  public static DeferredHolder<Item, BlockItem> WILDWOOD_LADDER = ITEMS.register("wildwood_ladder", () -> new BlockItem(ModBlocks.WILDWOOD_LADDER.get(), DEFAULT));
  // Wildwood gate
  public static DeferredHolder<Item, BlockItem> WILDWOOD_GATE = ITEMS.register("wildwood_gate", () -> new BlockItem(ModBlocks.WILDWOOD_GATE.get(), DEFAULT));
  // Runestone wall
  public static DeferredHolder<Item, BlockItem> RUNESTONE_WALL = ITEMS.register("runestone_wall", () -> new BlockItem(ModBlocks.RUNESTONE_WALL.get(), DEFAULT));
  // Mossy runestone wall
  public static DeferredHolder<Item, BlockItem> MOSSY_RUNESTONE_WALL = ITEMS.register("mossy_runestone_wall", () -> new BlockItem(ModBlocks.MOSSY_RUNESTONE_WALL.get(), DEFAULT));
  // Runestone brick wall
  public static DeferredHolder<Item, BlockItem> RUNESTONE_BRICK_WALL = ITEMS.register("runestone_brick_wall", () -> new BlockItem(ModBlocks.RUNESTONE_BRICK_WALL.get(), DEFAULT));
  // Runestone tile wall
  public static DeferredHolder<Item, BlockItem> RUNESTONE_TILE_WALL = ITEMS.register("runestone_tile_wall", () -> new BlockItem(ModBlocks.RUNESTONE_TILE_WALL.get(), DEFAULT));
  // Runed wall
  public static DeferredHolder<Item, BlockItem> RUNED_WALL = ITEMS.register("runed_wall", () -> new BlockItem(ModBlocks.RUNED_WALL.get(), DEFAULT));
  // Runed brick wall
  public static DeferredHolder<Item, BlockItem> RUNED_BRICK_WALL = ITEMS.register("runed_brick_wall", () -> new BlockItem(ModBlocks.RUNED_BRICK_WALL.get(), DEFAULT));
  // Runed tile wall
  public static DeferredHolder<Item, BlockItem> RUNED_TILE_WALL = ITEMS.register("runed_tile_wall", () -> new BlockItem(ModBlocks.RUNED_TILE_WALL.get(), DEFAULT));
  // Elemental soil
  public static DeferredHolder<Item, BlockItem> ELEMENTAL_SOIL = ITEMS.register("elemental_soil", () -> new BlockItem(ModBlocks.ELEMENTAL_SOIL.get(), DEFAULT));
  // Aqueous soil
  public static DeferredHolder<Item, BlockItem> AQUEOUS_SOIL = ITEMS.register("aqueous_soil", () -> new BlockItem(ModBlocks.AQUEOUS_SOIL.get(), DEFAULT));
  // Caelic soil
  public static DeferredHolder<Item, BlockItem> CAELIC_SOIL = ITEMS.register("caelic_soil", () -> new BlockItem(ModBlocks.CAELIC_SOIL.get(), DEFAULT));
  // Magmatic soil
  public static DeferredHolder<Item, BlockItem> MAGMATIC_SOIL = ITEMS.register("magmatic_soil", () -> new BlockItem(ModBlocks.MAGMATIC_SOIL.get(), DEFAULT));
  // Terran soil
  public static DeferredHolder<Item, BlockItem> TERRAN_SOIL = ITEMS.register("terran_soil", () -> new BlockItem(ModBlocks.TERRAN_SOIL.get(), DEFAULT));
  // Ritual pedestal
  public static DeferredHolder<Item, BlockItem> RITUAL_PEDESTAL = ITEMS.register("ritual_pedestal", () -> new BlockItem(ModBlocks.RITUAL_PEDESTAL.get(), DEFAULT));
  // Reinforced ritual pedestal
  public static DeferredHolder<Item, BlockItem> REINFORCED_RITUAL_PEDESTAL = ITEMS.register("reinforced_ritual_pedestal", () -> new BlockItem(ModBlocks.REINFORCED_RITUAL_PEDESTAL.get(), DEFAULT));
  // Grove crafter
  public static DeferredHolder<Item, BlockItem> GROVE_CRAFTER = ITEMS.register("grove_crafter", () -> new BlockItem(ModBlocks.GROVE_CRAFTER.get(), DEFAULT));
  // Grove pedestal
  public static DeferredHolder<Item, BlockItem> GROVE_PEDESTAL = ITEMS.register("grove_pedestal", () -> new BlockItem(ModBlocks.GROVE_PEDESTAL.get(), DEFAULT));
  // Wildwood pedestal
  public static DeferredHolder<Item, BlockItem> WILDWOOD_PEDESTAL = ITEMS.register("wildwood_pedestal", () -> new BlockItem(ModBlocks.WILDWOOD_PEDESTAL.get(), DEFAULT));
  // Display pedestal
  public static DeferredHolder<Item, BlockItem> DISPLAY_PEDESTAL = ITEMS.register("display_pedestal", () -> new BlockItem(ModBlocks.DISPLAY_PEDESTAL.get(), DEFAULT));
  // Bafflecap block
  public static DeferredHolder<Item, BlockItem> BAFFLECAP_BLOCK = ITEMS.register("bafflecap_block", () -> new BlockItem(ModBlocks.BAFFLECAP_BLOCK.get(), DEFAULT));
  // Primal grove stone
  public static DeferredHolder<Item, BlockItem> PRIMAL_GROVE_STONE = ITEMS.register("primal_grove_stone", () -> new BlockItem(ModBlocks.PRIMAL_GROVE_STONE.get(), DEFAULT));
  // Incense burner
  public static DeferredHolder<Item, BlockItem> INCENSE_BURNER = ITEMS.register("incense_burner", () -> new BlockItem(ModBlocks.INCENSE_BURNER.get(), DEFAULT));
  // Mortar
  public static DeferredHolder<Item, BlockItem> MORTAR = ITEMS.register("mortar", () -> new BlockItem(ModBlocks.MORTAR.get(), DEFAULT));
  // Pyre
  public static DeferredHolder<Item, BlockItem> PYRE = ITEMS.register("pyre", () -> new BlockItem(ModBlocks.PYRE.get(), DEFAULT));
  // Reinforced pyre
  public static DeferredHolder<Item, BlockItem> REINFORCED_PYRE = ITEMS.register("reinforced_pyre", () -> new BlockItem(ModBlocks.REINFORCED_PYRE.get(), DEFAULT));
  // Decorative pyre
  public static DeferredHolder<Item, BlockItem> DECORATIVE_PYRE = ITEMS.register("decorative_pyre", () -> new BlockItem(ModBlocks.DECORATIVE_PYRE.get(), DEFAULT));
  // Unending bowl
  public static DeferredHolder<Item, BlockItem> UNENDING_BOWL = ITEMS.register("unending_bowl", () -> new BlockItem(ModBlocks.UNENDING_BOWL.get(), DEFAULT));
  // Potted bafflecap
  public static DeferredHolder<Item, BlockItem> POTTED_BAFFLECAP = ITEMS.register("potted_bafflecap", () -> new BlockItem(ModBlocks.POTTED_BAFFLECAP.get(), DEFAULT));
  // Potted stonepetal
  public static DeferredHolder<Item, BlockItem> POTTED_STONEPETAL = ITEMS.register("potted_stonepetal", () -> new BlockItem(ModBlocks.POTTED_STONEPETAL.get(), DEFAULT));
  // Potted wildwood sapling
  public static DeferredHolder<Item, BlockItem> POTTED_WILDWOOD_SAPLING = ITEMS.register("potted_wildwood_sapling", () -> new BlockItem(ModBlocks.POTTED_WILDWOOD_SAPLING.get(), DEFAULT));

  // GATHERED CROPS
  public static final DeferredHolder<Item, ItemNameBlockItem> WILDROOT = ITEMS.register("wildroot", () -> new ItemNameBlockItem(ModBlocks.WILDROOT_CROP.get(), DEFAULT));
  /*      REGISTRATE.item("wildroot", (p) -> new ItemNameBlockItem(ModBlocks.WILDROOT_CROP.get(), p))
      .model(subfolder("herbs"))
      .tag(RootsTags.Items.WILDROOT_SEEDS, RootsTags.Items.WILDROOT_CROP)
      .defaultLang()
      .register();*/
  public static final DeferredHolder<Item, Item> GROVE_MOSS = ITEMS.register("grove_moss", () -> new Item(DEFAULT));
/*    REGISTRATE.item("grove_moss", Item::new)
    .model(subfolder("herbs"))
    .tag(RootsTags.Items.GROVE_MOSS_CROP)
    .register();*/

  // PYRE-CRAFTED CROPS
  public static final DeferredHolder<Item, ItemNameBlockItem> CLOUD_BERRY = ITEMS.register("cloud_berry", () -> new ItemNameBlockItem(ModBlocks.CLOUD_BERRY_CROP.get(), DEFAULT));
  /*      REGISTRATE.item("cloud_berry", (p) -> new ItemNameBlockItem(ModBlocks.CLOUD_BERRY_CROP.get(), p))
      .model(subfolder("herbs"))
      .tag(RootsTags.Items.CLOUD_BERRY_SEEDS, RootsTags.Items.CLOUD_BERRY_CROP)
      .register();*/
  public static final DeferredHolder<Item, ItemNameBlockItem> DEWGONIA = ITEMS.register("dewgonia", () -> new ItemNameBlockItem(ModBlocks.DEWGONIA_CROP.get(), DEFAULT));
  /*    REGISTRATE.item("dewgonia", (p) -> new ItemNameBlockItem(ModBlocks.DEWGONIA_CROP.get(), p))
      .model(subfolder("herbs"))
      .tag(RootsTags.Items.DEWGONIA_SEEDS, RootsTags.Items.DEWGONIA_CROP)
      .register();*/
  public static final DeferredHolder<Item, ItemNameBlockItem> INFERNO_BULB = ITEMS.register("inferno_bulb", () -> new ItemNameBlockItem(ModBlocks.INFERNO_BULB_CROP.get(), DEFAULT));
  /*    REGISTRATE.item("inferno_bulb", (p) -> new ItemNameBlockItem(ModBlocks.INFERNO_BULB_CROP.get(), p))
      .model(subfolder("herbs"))
      .tag(RootsTags.Items.INFERNO_BULB_SEEDS, RootsTags.Items.INFERNO_BULB_CROP)
      .register();*/
  public static final DeferredHolder<Item, ItemNameBlockItem> STALICRIPE = ITEMS.register("stalicripe", () -> new ItemNameBlockItem(ModBlocks.STALICRIPE_CROP.get(), DEFAULT));
/*    REGISTRATE.item("stalicripe", (p) -> new ItemNameBlockItem(ModBlocks.STALICRIPE_CROP.get(), p))
    .model(subfolder("herbs"))
    .tag(RootsTags.Items.STALICRIPE_SEEDS, RootsTags.Items.STALICRIPE_CROP)
    .register();*/
  // OTHER SOURCE CROPS

  // RUNIC SHEARS -> MUSHROOM
  public static final DeferredHolder<Item, ItemNameBlockItem> BAFFLECAP = ITEMS.register("bafflecap", () -> new ItemNameBlockItem(ModBlocks.BAFFLECAP.get(), DEFAULT));
  /*      REGISTRATE.item("bafflecap", (p) -> new ItemNameBlockItem(ModBlocks.BAFFLECAP.get(), p))
      .model(subfolder("herbs"))
      .tag(RootsTags.Items.BAFFLECAP_CROP)
      .recipe((ctx, p) -> {
        RunicBlockRecipe.builder(ctx.getEntry())
          .durabilityCost(15)
          .setCondition(new WorldRecipe.Condition(new TagMatchTest(RootsTags.Blocks.BAFFLECAP_CONVERSION)))
          .setOutputState(Blocks.AIR.defaultBlockState())
          .unlockedBy("has_runic_shears", p.has(RootsTags.Items.RUNIC_SHEARS))
          .save(p, RootsAPI.rl("runic/block/bafflecap_from_mushroom"));
      })
      .register();*/
  // TODO: Determine which tags have been referenced but are empty
  public static final DeferredHolder<Item, Item> MOONGLOW = ITEMS.register("moonglow", () -> new Item(DEFAULT));
/*    REGISTRATE.item("moonglow", Item::new)
    .model(subfolder("herbs"))
    .tag(RootsTags.Items.MOONGLOW_CROP)
    .register();*/

  // RUNIC SHEARS -> FLOWER -> BULB
  public static final DeferredHolder<Item, Item> PERESKIA = ITEMS.register("pereskia", () -> new Item(DEFAULT));
/*      REGISTRATE.item("pereskia", Item::new)
    .model(subfolder("herbs"))
    .tag(RootsTags.Items.PERESKIA_CROP)
    .register();*/

  // RUNIC SHEARS -> BEETROOT -> SPIRITLEAF SEEDS
  public static final DeferredHolder<Item, Item> SPIRITLEAF = ITEMS.register("spiritleaf", () -> new Item(DEFAULT));
/*      REGISTRATE.item("spiritleaf", Item::new)
    .model(subfolder("herbs"))
    .tag(RootsTags.Items.SPIRITLEAF_CROP)
    .register();*/

  // RUNIC SHEARS -> WHEAT -> WILDEWHEET SEEDS
  public static final DeferredHolder<Item, Item> WILDEWHEET = ITEMS.register("wildewheet", () -> new Item(DEFAULT));
/*      REGISTRATE.item("wildewheet", Item::new)
    .model(subfolder("herbs"))
    .tag(RootsTags.Items.WILDEWHEET_CROP)
    .register();*/

  public static final DeferredHolder<Item, ItemNameBlockItem> MOONGLOW_SEEDS = ITEMS.register("moonglow_seeds", () -> new ItemNameBlockItem(ModBlocks.MOONGLOW_CROP.get(), DEFAULT));
  /*      REGISTRATE.item("moonglow_seeds", (p) -> new ItemNameBlockItem(ModBlocks.MOONGLOW_CROP.get(), p))
      .model(subfolder("herbs"))
      .tag(RootsTags.Items.MOONGLOW_SEEDS)
      .register();*/
  public static final DeferredHolder<Item, ItemNameBlockItem> PERESKIA_BULB = ITEMS.register("pereskia_bulb", () -> new ItemNameBlockItem(ModBlocks.PERESKIA_CROP.get(), DEFAULT));
  /*    REGISTRATE.item("pereskia_bulb", (p) -> new ItemNameBlockItem(ModBlocks.PERESKIA_CROP.get(), p))
      .model(subfolder("herbs"))
      .tag(RootsTags.Items.PERESKIA_SEEDS)
      .recipe((ctx, p) -> {
        RunicBlockRecipe.builder(ctx.getEntry())
          .durabilityCost(15)
          .setCondition(new WorldRecipe.Condition(new TagMatchTest(BlockTags.SMALL_FLOWERS)))
          .setOutputState(Blocks.AIR.defaultBlockState())
          .unlockedBy("has_runic_shears", p.has(RootsTags.Items.RUNIC_SHEARS))
          .save(p, RootsAPI.rl("runic/block/pereskia_from_mushroom"));
      })
      .register();*/
  public static final DeferredHolder<Item, ItemNameBlockItem> SPIRITLEAF_SEEDS = ITEMS.register("spiritleaf_seeds", () -> new ItemNameBlockItem(ModBlocks.SPIRITLEAF_CROP.get(), DEFAULT));
  /*    REGISTRATE.item("spiritleaf_seeds", (p) -> new ItemNameBlockItem(ModBlocks.SPIRITLEAF_CROP.get(), p))
      .model(subfolder("herbs"))
      .tag(RootsTags.Items.SPIRITLEAF_SEEDS)
      .recipe((ctx, p) -> {
        RunicBlockRecipe.builder(ctx.getEntry())
          .durabilityCost(15)
          .skipProperty(BeetrootBlock.AGE)
          .setCondition(new WorldRecipe.Condition(new BlockPropertyMatchTest(Blocks.BEETROOTS.defaultBlockState().setValue(BeetrootBlock.AGE, BeetrootBlock.MAX_AGE), BeetrootBlock.AGE)))
          .setOutputState(Blocks.BEETROOTS.defaultBlockState().setValue(BeetrootBlock.AGE, 0))
          .unlockedBy("has_shears", p.has(RootsTags.Items.RUNIC_SHEARS))
          .save(p, RootsAPI.rl("runic/block/spiritleaf_seeds"));
      })
      .register();*/
  public static final DeferredHolder<Item, ItemNameBlockItem> WILDEWHEET_SEEDS = ITEMS.register("wildewheet_seeds", () -> new ItemNameBlockItem(ModBlocks.WILDEWHEET_CROP.get(), DEFAULT));
/*    REGISTRATE.item("wildewheet_seeds", (p) -> new ItemNameBlockItem(ModBlocks.WILDEWHEET_CROP.get(), p))
    .model(subfolder("herbs"))
    .tag(RootsTags.Items.WILDEWHEET_SEEDS)
    .recipe((ctx, p) -> {
      RunicBlockRecipe.builder(ctx.getEntry())
        .durabilityCost(15)
        .skipProperty(CropBlock.AGE)
        .setCondition(new WorldRecipe.Condition(new BlockPropertyMatchTest(Blocks.WHEAT.defaultBlockState().setValue(CropBlock.AGE, CropBlock.MAX_AGE), CropBlock.AGE)))
        .setOutputState(Blocks.WHEAT.defaultBlockState().setValue(CropBlock.AGE, 0))
        .unlockedBy("has_shears", p.has(RootsTags.Items.RUNIC_SHEARS))
        .save(p, RootsAPI.rl("runic/block/wildewheet_seeds"));
    })
    .register();*/

  public static final DeferredHolder<Item, GroveSporesItem> GROVE_SPORES = ITEMS.register("grove_spores", () -> new GroveSporesItem(DEFAULT));
/*      REGISTRATE.item("grove_spores", GroveSporesItem::new)
    .model(subfolder("herbs"))
    .tag(RootsTags.Items.SEEDS)
    .recipe((ctx, p) -> {
      MortarRecipe.multiBuilder(10)
        .addIngredient(ItemTags.DIRT)
        .addChanceOutput(new ItemStack(ctx.getEntry(), 1), 0.1f)
        .unlockedBy("has_item", p.has(ItemTags.DIRT))
        .save(p, RootsAPI.rl("mortar/grove_spores_from_dirt"));
    })
    .register();*/

  public static final DeferredHolder<Item, ItemNameBlockItem> AUBERGINE_SEEDS = ITEMS.register("aubergine_seeds", () -> new ItemNameBlockItem(ModBlocks.AUBERGINE_CROP.get(), DEFAULT));
/*      REGISTRATE.item("aubergine_seeds", blockNamedItem(() -> ModBlocks.AUBERGINE_CROP))
    .tag(RootsTags.Items.SEEDS)
    .register();*/

  public static DeferredHolder<Item, Item> CARAPACE = ITEMS.register("carapace", () -> new Item(DEFAULT));
/*
      REGISTRATE.item("carapace", Item::new)
    .recipe((ctx, p) -> RECIPES.dye(ModItems.CARAPACE, () -> Items.BLUE_DYE, 1, 2, p))
    .tag(RootsTags.Items.CARAPACE)
    .register();
*/

  public static DeferredHolder<Item, Item> PELT = ITEMS.register("pelt", () -> new Item(DEFAULT));
/*      REGISTRATE.item("pelt", Item::new)
    .recipe((ctx, p) -> RECIPES.singleItemUnfinished(ModItems.PELT, () -> Items.LEATHER, 1, 1).save(p, RootsAPI.rl("pelt_to_leather")))
    .register();*/

  public static DeferredHolder<Item, Item> ANTLERS = ITEMS.register("antlers", () -> new Item(DEFAULT));
/*      REGISTRATE.item("antlers", Item::new)
    .recipe((ctx, p) -> RECIPES.singleItemUnfinished(ModItems.ANTLERS, () -> Items.BONE_MEAL, 1, 9).save(p, RootsAPI.rl("antlers_to_bonemeal")))
    .register();*/

  public static DeferredHolder<Item, Item> VENISON = ITEMS.register("venison", () -> new Item(new Item.Properties().food(ModFoods.VENISON)));
/*      REGISTRATE.item("venison", Item::new)
    .properties(o -> o.food(ModFoods.VENISON))
    .tag(RootsTags.Items.PROTEINS)
    .recipe((ctx, p) -> {
      RECIPES.food(ModItems.VENISON, ModItems.COOKED_VENISON, 0.15f, p);
      RECIPES.food(Tags.Items.CROPS_CARROT, ModItems.COOKED_CARROT, 0.15f, p);
      RECIPES.food(Tags.Items.CROPS_BEETROOT, ModItems.COOKED_BEETROOT, 0.15f, p);
      RECIPES.food(ModItems.ASSORTED_SEEDS, ModItems.COOKED_SEEDS, 0.05f, p);
      RECIPES.food(RootsTags.Items.AUBERGINE_CROP, ModItems.COOKED_AUBERGINE, 0.15f, p);
      RECIPES.food(ModItems.RAW_SQUID, ModItems.COOKED_SQUID, 0.15f, p);
      RECIPES.food(ModItems.FLOUR, () -> Items.BREAD, 0.15f, p);
      RECIPES.food(ModItems.PERESKIA_BULB, ModItems.COOKED_PERESKIA, 0.14f, p);
    })
    .register();*/


  public static DeferredHolder<Item, Item> COOKED_VENISON = ITEMS.register("cooked_venison", () -> new Item(new Item.Properties().food(ModFoods.COOKED_VENISON)));
/*      REGISTRATE.item("cooked_venison", Item::new)
    .tag(RootsTags.Items.PROTEINS)
    .properties(o -> o.food(ModFoods.COOKED_VENISON))
    .register();*/

  public static DeferredHolder<Item, Item> RAW_SQUID = ITEMS.register("raw_squid", () -> new Item(new Item.Properties().food(ModFoods.RAW_SQUID)));
/*      REGISTRATE.item("raw_squid", Item::new)
    .tag(RootsTags.Items.PROTEINS)
    .properties(o -> o.food(ModFoods.RAW_SQUID))
    .register();*/

  public static DeferredHolder<Item, Item> COOKED_SQUID = ITEMS.register("cooked_squid", () -> new Item(new Item.Properties().food(ModFoods.COOKED_SQUID)));
/*      REGISTRATE.item("cooked_squid", Item::new)
    .tag(RootsTags.Items.PROTEINS, RootsTags.Items.COOKED_SEAFOOD)
    .properties(o -> o.food(ModFoods.COOKED_SQUID))
    .register();*/

  public static DeferredHolder<Item, Item> ASSORTED_SEEDS = ITEMS.register("assorted_seeds", () -> new Item(DEFAULT));
/*      REGISTRATE.item("assorted_seeds", Item::new)
    .recipe((ctx, p) -> {
      ShapelessRecipeBuilder.shapeless(ctx.getEntry(), 4)
        .requires(Tags.Items.SEEDS)
        .requires(Tags.Items.SEEDS)
        .requires(Tags.Items.SEEDS)
        .requires(Tags.Items.SEEDS)
        .unlockedBy("has_seeds", p.has(Tags.Items.SEEDS))
        .save(p, RootsAPI.rl("assorted_seeds_from_seeds"));
    })
    .register();*/

  public static DeferredHolder<Item, BaseItems.FastFoodItem> COOKED_SEEDS = ITEMS.register("cooked_seeds", () -> new BaseItems.FastFoodItem(new Item.Properties().food(ModFoods.COOKED_SEEDS)));
/*      REGISTRATE.item("cooked_seeds", BaseItems.FastFoodItem::new)
    .properties(o -> o.food(ModFoods.COOKED_SEEDS))
    .register();*/

  public static DeferredHolder<Item, Item> COOKED_BEETROOT = ITEMS.register("cooked_beetroot", () -> new Item(new Item.Properties().food(ModFoods.COOKED_BEETROOT)));
/*      REGISTRATE.item("cooked_beetroot", Item::new)
    .tag(RootsTags.Items.COOKED_VEGETABLES)
    .properties(o -> o.food(ModFoods.COOKED_BEETROOT))
    .register();*/

  public static DeferredHolder<Item, Item> COOKED_CARROT = ITEMS.register("cooked_carrot", () -> new Item(new Item.Properties().food(ModFoods.COOKED_CARROT)));
/*      REGISTRATE.item("cooked_carrot", Item::new)
    .tag(RootsTags.Items.COOKED_VEGETABLES)
    .properties(o -> o.food(ModFoods.COOKED_CARROT))
    .register();*/

  public static DeferredHolder<Item, Item> AUBERGINE = ITEMS.register("aubergine", () -> new Item(new Item.Properties().food(ModFoods.AUBERGINE)));
/*      REGISTRATE.item("aubergine", Item::new)
    .properties(o -> o.food(ModFoods.AUBERGINE))
    .tag(RootsTags.Items.AUBERGINE_CROP, RootsTags.Items.VEGETABLES)
    .register();*/

  public static DeferredHolder<Item, Item> COOKED_AUBERGINE = ITEMS.register("cooked_aubergine", () -> new Item(new Item.Properties().food(ModFoods.COOKED_AUBERGINE)));
/*      REGISTRATE.item("cooked_aubergine", Item::new)
    .tag(RootsTags.Items.COOKED_VEGETABLES)
    .properties(o -> o.food(ModFoods.COOKED_AUBERGINE))
    .register();*/

  public static DeferredHolder<Item, Item> STUFFED_AUBERGINE = ITEMS.register("stuffed_aubergine", () -> new Item(new Item.Properties().food(ModFoods.STUFFED_AUBERGINE)));
/*      REGISTRATE.item("stuffed_aubergine", Item::new)
    .properties(o -> o.food(ModFoods.STUFFED_AUBERGINE))
    .recipe((ctx, p) -> ShapelessRecipeBuilder.shapeless(ModItems.STUFFED_AUBERGINE.get(), 1).requires(ModItems.COOKED_AUBERGINE.get()).requires(ExcludingIngredient.create(RootsTags.Items.VEGETABLES, ModItems.AUBERGINE.get())).requires(ExcludingIngredient.create(RootsTags.Items.VEGETABLES, ModItems.AUBERGINE.get())).requires(ExcludingIngredient.create(RootsTags.Items.COOKED_VEGETABLES, ModItems.COOKED_AUBERGINE.get())).unlockedBy("has_cooked_aubergine", RegistrateRecipeProvider.has(ModItems.COOKED_AUBERGINE.get())).save(p))
    .register();*/

  // Salads
  public static DeferredHolder<Item, BaseItems.BowlItem> AUBERGINE_SALAD = ITEMS.register("aubergine_salad", () -> new BaseItems.BowlItem(new Item.Properties().food(ModFoods.AUBERGINE_SALAD).craftRemainder(Items.BOWL)));
/*      REGISTRATE.item("aubergine_salad", BaseItems.BowlItem::new)
    .properties(o -> o.food(ModFoods.AUBERGINE_SALAD).craftRemainder(Items.BOWL))
    .recipe((ctx, p) -> ShapedRecipeBuilder.shaped(ModItems.AUBERGINE_SALAD.get(), 3)
      .pattern("AAA")
      .pattern("KKK")
      .pattern("BBB")
      .define('A', RootsTags.Items.AUBERGINE_CROP)
      .define('B', Items.BOWL)
      .define('K', Items.KELP)
      .unlockedBy("has_aubergine", RegistrateRecipeProvider.has(RootsTags.Items.AUBERGINE_CROP))
      .unlockedBy("has_kelp", RegistrateRecipeProvider.has(Items.KELP))
      .save(p))
    .register();*/

  public static DeferredHolder<Item, BaseItems.BowlItem> BEETROOT_SALAD = ITEMS.register("beetroot_salad", () -> new BaseItems.BowlItem(new Item.Properties().food(ModFoods.BEETROOT_SALAD).craftRemainder(Items.BOWL)));
/*      REGISTRATE.item("beetroot_salad", BaseItems.BowlItem::new)
    .properties(o -> o.food(ModFoods.BEETROOT_SALAD).craftRemainder(Items.BOWL))
    .recipe((ctx, p) -> ShapedRecipeBuilder.shaped(ModItems.BEETROOT_SALAD.get(), 3)
      .pattern("AAA")
      .pattern("KKK")
      .pattern("BBB")
      .define('A', Items.BEETROOT)
      .define('B', Items.BOWL)
      .define('K', Items.KELP)
      .unlockedBy("has_beetroot", RegistrateRecipeProvider.has(Items.BEETROOT))
      .unlockedBy("has_kelp", RegistrateRecipeProvider.has(Items.KELP))
      .save(p))
    .register();*/

/*  public static DeferredHolder<Item, BaseItems.BowlItem> CACTUS_DANDELION_SALAD =
      REGISTRATE.item("cactus_dandelion_salad", BaseItems.BowlItem::new)
    .properties(o -> o.food(ModFoods.CACTUS_DANDELION_SALAD).craftRemainder(Items.BOWL))
    .recipe((ctx, p) -> ShapedRecipeBuilder.shaped(ModItems.CACTUS_DANDELION_SALAD.get(), 3)
      .pattern("DCD")
      .pattern("CDC")
      .pattern("BBB")
      .define('D', Items.DANDELION)
      .define('C', Items.CACTUS)
      .define('B', Items.BOWL)
      .unlockedBy("has_dandelion", RegistrateRecipeProvider.has(Items.DANDELION))
      .unlockedBy("has_cactus", RegistrateRecipeProvider.has(Items.CACTUS))
      .save(p))
    .register();*/

/*  public static DeferredHolder<Item, BaseItems.BowlItem> DANDELION_CORNFLOWER_SALAD = REGISTRATE.item("dandelion_cornflower_salad", BaseItems.BowlItem::new)
    .properties(o -> o.food(ModFoods.DANDELION_CORNFLOWER_SALAD).craftRemainder(Items.BOWL))
    .recipe((ctx, p) -> ShapedRecipeBuilder.shaped(ModItems.DANDELION_CORNFLOWER_SALAD.get(), 3)
      .pattern("CDC")
      .pattern("DCD")
      .pattern("BBB")
      .define('D', Items.DANDELION)
      .define('C', Items.CORNFLOWER)
      .define('B', Items.BOWL)
      .unlockedBy("has_dandelion", RegistrateRecipeProvider.has(Items.DANDELION))
      .unlockedBy("has_cornflower", RegistrateRecipeProvider.has(Items.CORNFLOWER))
      .save(p))
    .register();*/

  public static DeferredHolder<Item, BaseItems.BowlItem> STEWED_EGGPLANT = ITEMS.register("stewed_eggplant", () -> new BaseItems.BowlItem(new Item.Properties().food(ModFoods.STEWED_EGGPLANT).craftRemainder(Items.BOWL)));
/*      REGISTRATE.item("stewed_eggplant", BaseItems.BowlItem::new)
    .properties(o -> o.food(ModFoods.STEWED_EGGPLANT).craftRemainder(Items.BOWL))
    .recipe((ctx, p) -> ShapedRecipeBuilder.shaped(ModItems.STEWED_EGGPLANT.get(), 3)
      .pattern("AAA")
      .pattern("MLM")
      .pattern("BBB")
      .define('A', ModItems.COOKED_AUBERGINE.get())
      .define('B', Items.BOWL)
      .define('L', Items.ALLIUM)
      .define('M', Ingredient.of(Items.RED_MUSHROOM, Items.BROWN_MUSHROOM))
      .unlockedBy("has_cooked_aubergine", RegistrateRecipeProvider.has(ModItems.COOKED_AUBERGINE.get()))
      .save(p))
    .register();*/

  public static Supplier<Item> tooltipDrink(Item.Properties properties, String translationKey) {
    return () -> new TooltipDrinkItem(translationKey, properties);
  }

  // Drinkies
  public static DeferredHolder<Item, TooltipDrinkItem> APPLE_CORDIAL = ITEMS.register("apple_cordial", () -> new TooltipDrinkItem("roots.drinks.slow_regen", new Item.Properties().food(ModFoods.APPLE_CORDIAL).craftRemainder(Items.GLASS_BOTTLE)));
/*      REGISTRATE.item("apple_cordial", tooltipDrink("roots.drinks.slow_regen"))
    .properties(o -> o.food(ModFoods.APPLE_CORDIAL).craftRemainder(Items.GLASS_BOTTLE))
    .recipe(RECIPES.cordial(() -> ModItems.APPLE_CORDIAL, Items.APPLE))
    .register();*/

  public static DeferredHolder<Item, TooltipDrinkItem> CACTUS_SYRUP = ITEMS.register("cactus_syrup", () -> new TooltipDrinkItem("roots.drinks.slow_regen", new Item.Properties().food(ModFoods.CACTUS_SYRUP).craftRemainder(Items.GLASS_BOTTLE)));
/*      REGISTRATE.item("cactus_syrup", tooltipDrink("roots.drinks.slow_regen"))
    .properties(o -> o.food(ModFoods.CACTUS_SYRUP).craftRemainder(Items.GLASS_BOTTLE))
    .recipe(RECIPES.cordial(() -> ModItems.CACTUS_SYRUP, Items.CACTUS))
    .register();*/

  public static DeferredHolder<Item, TooltipDrinkItem> DANDELION_CORDIAL = ITEMS.register("dandelion_cordial", () -> new TooltipDrinkItem("roots.drinks.wakefulness", new Item.Properties().food(ModFoods.DANDELION_CORDIAL).craftRemainder(Items.GLASS_BOTTLE)));
/*      REGISTRATE.item("dandelion_cordial", tooltipDrink("roots.drinks.wakefulness"))
    .properties(o -> o.food(ModFoods.DANDELION_CORDIAL).craftRemainder(Items.GLASS_BOTTLE))
    .recipe(RECIPES.cordial(() -> ModItems.DANDELION_CORDIAL, Items.DANDELION))
    .register();*/

  public static DeferredHolder<Item, TooltipDrinkItem> LILAC_CORDIAL = ITEMS.register("lilac_cordial", () -> new TooltipDrinkItem("roots.drinks.slow_regen", new Item.Properties().food(ModFoods.LILAC_CORDIAL).craftRemainder(Items.GLASS_BOTTLE)));
/*      REGISTRATE.item("lilac_cordial", tooltipDrink("roots.drinks.slow_regen"))
    .properties(o -> o.food(ModFoods.LILAC_CORDIAL).craftRemainder(Items.GLASS_BOTTLE))
    .recipe(RECIPES.cordial(() -> ModItems.LILAC_CORDIAL, Items.LILAC))
    .register();*/

  public static DeferredHolder<Item, TooltipDrinkItem> PEONY_CORDIAL = ITEMS.register("peony_cordial", () -> new TooltipDrinkItem("roots.drinks.slow_regen", new Item.Properties().food(ModFoods.PEONY_CORDIAL).craftRemainder(Items.GLASS_BOTTLE)));
/*      REGISTRATE.item("peony_cordial", tooltipDrink("roots.drinks.slow_regen"))
    .properties(o -> o.food(ModFoods.PEONY_CORDIAL).craftRemainder(Items.GLASS_BOTTLE))
    .recipe(RECIPES.cordial(() -> ModItems.PEONY_CORDIAL, Items.PEONY))
    .register();*/

  public static DeferredHolder<Item, TooltipDrinkItem> ROSE_CORDIAL = ITEMS.register("rose_cordial", () -> new TooltipDrinkItem("roots.drinks.slow_regen", new Item.Properties().food(ModFoods.ROSE_CORDIAL).craftRemainder(Items.GLASS_BOTTLE)));
/*      REGISTRATE.item("rose_cordial", tooltipDrink("roots.drinks.slow_regen"))
    .properties(o -> o.food(ModFoods.ROSE_CORDIAL).craftRemainder(Items.GLASS_BOTTLE))
    .recipe(RECIPES.cordial(() -> ModItems.ROSE_CORDIAL, Items.ROSE_BUSH))
    .register();*/

  public static DeferredHolder<Item, TooltipDrinkItem> VINEGAR = ITEMS.register("vinegar", () -> new TooltipDrinkItem("roots.drinks.sour", new Item.Properties().food(ModFoods.VINEGAR).craftRemainder(Items.GLASS_BOTTLE)));
/*      REGISTRATE.item("vinegar", tooltipDrink("roots.drinks.sour"))
    .properties(o -> o.food(ModFoods.VINEGAR).craftRemainder(Items.GLASS_BOTTLE))
    .recipe((ctx, p) -> ShapedRecipeBuilder.shaped(ModItems.VINEGAR.get(), 6)
      .pattern("BBB")
      .pattern("PPP")
      .pattern("BBB")
      .define('P', Items.SEA_PICKLE)
      .define('B', Items.GLASS_BOTTLE)
      .unlockedBy("has_sea_pickle", RegistrateRecipeProvider.has(Items.SEA_PICKLE))
      .save(p))
    .register();*/

  public static DeferredHolder<Item, TooltipDrinkItem> VEGETABLE_JUICE = ITEMS.register("vegetable_juice", () -> new TooltipDrinkItem("roots.drinks.slow_regen", new Item.Properties().food(ModFoods.VEGETABLE_JUICE).craftRemainder(Items.GLASS_BOTTLE)));
/*      REGISTRATE.item("vegetable_juice", tooltipDrink("roots.drinks.slow_regen"))
    .properties(o -> o.food(ModFoods.VEGETABLE_JUICE).craftRemainder(Items.GLASS_BOTTLE))
    .recipe((ctx, p) -> ShapedRecipeBuilder.shaped(ModItems.VEGETABLE_JUICE.get(), 4)
      .pattern("ARC")
      .pattern("BPB")
      .pattern("BWB")
      .define('A', RootsTags.Items.AUBERGINE_CROP)
      .define('R', Items.BEETROOT)
      .define('C', Items.CARROT)
      .define('P', Items.APPLE)
      .define('B', Items.GLASS_BOTTLE)
      .define('W', Items.WATER_BUCKET)
      .unlockedBy("has_aubergine", RegistrateRecipeProvider.has(RootsTags.Items.AUBERGINE_CROP))
      .unlockedBy("has_beetroot", RegistrateRecipeProvider.has(Items.BEETROOT))
      .unlockedBy("has_carrot", RegistrateRecipeProvider.has(Items.CARROT))
      .unlockedBy("has_apple", RegistrateRecipeProvider.has(Items.APPLE))
      .save(p))
    .register();*/

  public static DeferredHolder<Item, Item> INK_BOTTLE = ITEMS.register("ink_bottle", () -> new Item(DEFAULT));
/*      REGISTRATE.item("ink_bottle", Item::new)
    .properties(o -> o.craftRemainder(Items.GLASS_BOTTLE))
    .recipe((ctx, p) -> RECIPES.dye(ModItems.INK_BOTTLE, () -> Items.BLACK_DYE, 1, 2, p))
    .register();*/

  public static final DeferredHolder<Item, Item> ACACIA_BARK = ITEMS.register("acacia_bark", () -> new Item(DEFAULT));
/*      REGISTRATE.item("acacia_bark", Item::new)
    .tag(RootsTags.Items.ACACIA_BARK)
    .model(subfolder("bark"))
    .recipe((ctx, p) -> {
      BarkRecipe.builder(ctx.getEntry(), 2)
        .setOutputState(Blocks.STRIPPED_ACACIA_LOG.defaultBlockState())
        .setCondition(new WorldRecipe.Condition(new BlockMatchTest(Blocks.ACACIA_LOG)))
        .unlockedBy("has_knife", p.has(RootsTags.Items.KNIVES))
        .save(p, RootsAPI.rl("bark/acacia_log_stripping"));
      BarkRecipe.builder(ctx.getEntry(), 3)
        .setOutputState(Blocks.STRIPPED_ACACIA_WOOD.defaultBlockState())
        .setCondition(new WorldRecipe.Condition(new BlockMatchTest(Blocks.ACACIA_WOOD)))
        .unlockedBy("has_knife", p.has(RootsTags.Items.KNIVES))
        .save(p, RootsAPI.rl("bark/acacia_wood_stripping"));
    })
    .register();*/

  public static final DeferredHolder<Item, Item> BIRCH_BARK = ITEMS.register("birch_bark", () -> new Item(DEFAULT));
/*      REGISTRATE.item("birch_bark", Item::new)
    .tag(RootsTags.Items.BIRCH_BARK)
    .model(subfolder("bark"))
    .recipe((ctx, p) -> {
      BarkRecipe.builder(ctx.getEntry(), 2)
        .setOutputState(Blocks.STRIPPED_BIRCH_LOG.defaultBlockState())
        .setCondition(new WorldRecipe.Condition(new BlockMatchTest(Blocks.BIRCH_LOG)))
        .unlockedBy("has_knife", p.has(RootsTags.Items.KNIVES))
        .save(p, RootsAPI.rl("bark/birch_log_stripping"));
      BarkRecipe.builder(ctx.getEntry(), 3)
        .setOutputState(Blocks.STRIPPED_BIRCH_WOOD.defaultBlockState())
        .setCondition(new WorldRecipe.Condition(new BlockMatchTest(Blocks.BIRCH_WOOD)))
        .unlockedBy("has_knife", p.has(RootsTags.Items.KNIVES))
        .save(p, RootsAPI.rl("bark/birch_wood_stripping"));
    })
    .register();*/

  public static final DeferredHolder<Item, Item> DARK_OAK_BARK = ITEMS.register("dark_oak_bark", () -> new Item(DEFAULT));
/*      REGISTRATE.item("dark_oak_bark", Item::new)
    .tag(RootsTags.Items.DARK_OAK_BARK)
    .model(subfolder("bark"))
    .recipe((ctx, p) -> {
      BarkRecipe.builder(ctx.getEntry(), 2)
        .setOutputState(Blocks.STRIPPED_DARK_OAK_LOG.defaultBlockState())
        .setCondition(new WorldRecipe.Condition(new BlockMatchTest(Blocks.DARK_OAK_LOG)))
        .unlockedBy("has_knife", p.has(RootsTags.Items.KNIVES))
        .save(p, RootsAPI.rl("bark/dark_oak_log_stripping"));
      BarkRecipe.builder(ctx.getEntry(), 3)
        .setOutputState(Blocks.STRIPPED_DARK_OAK_WOOD.defaultBlockState())
        .setCondition(new WorldRecipe.Condition(new BlockMatchTest(Blocks.DARK_OAK_WOOD)))
        .unlockedBy("has_knife", p.has(RootsTags.Items.KNIVES))
        .save(p, RootsAPI.rl("bark/dark_oak_wood_stripping"));
    })
    .register();*/

  public static final DeferredHolder<Item, Item> JUNGLE_BARK = ITEMS.register("jungle_bark", () -> new Item(DEFAULT));
/*      REGISTRATE.item("jungle_bark", Item::new)
    .tag(RootsTags.Items.JUNGLE_BARK)
    .model(subfolder("bark"))
    .recipe((ctx, p) -> {
      BarkRecipe.builder(ctx.getEntry(), 2)
        .setOutputState(Blocks.STRIPPED_JUNGLE_LOG.defaultBlockState())
        .setCondition(new WorldRecipe.Condition(new BlockMatchTest(Blocks.JUNGLE_LOG)))
        .unlockedBy("has_knife", p.has(RootsTags.Items.KNIVES))
        .save(p, RootsAPI.rl("bark/jungle_log_stripping"));
      BarkRecipe.builder(ctx.getEntry(), 3)
        .setOutputState(Blocks.STRIPPED_JUNGLE_WOOD.defaultBlockState())
        .setCondition(new WorldRecipe.Condition(new BlockMatchTest(Blocks.JUNGLE_WOOD)))
        .unlockedBy("has_knife", p.has(RootsTags.Items.KNIVES))
        .save(p, RootsAPI.rl("bark/jungle_wood_stripping"));
    })
    .register();*/

  public static final DeferredHolder<Item, Item> OAK_BARK = ITEMS.register("oak_bark", () -> new Item(DEFAULT));
/*      REGISTRATE.item("oak_bark", Item::new)
    .tag(RootsTags.Items.OAK_BARK)
    .model(subfolder("bark"))
    .recipe((ctx, p) -> {
      BarkRecipe.builder(ctx.getEntry(), 2)
        .setOutputState(Blocks.STRIPPED_OAK_LOG.defaultBlockState())
        .setCondition(new WorldRecipe.Condition(new BlockMatchTest(Blocks.OAK_LOG)))
        .unlockedBy("has_knife", p.has(RootsTags.Items.KNIVES))
        .save(p, RootsAPI.rl("bark/oak_log_stripping"));
      BarkRecipe.builder(ctx.getEntry(), 3)
        .setOutputState(Blocks.STRIPPED_OAK_WOOD.defaultBlockState())
        .setCondition(new WorldRecipe.Condition(new BlockMatchTest(Blocks.OAK_WOOD)))
        .unlockedBy("has_knife", p.has(RootsTags.Items.KNIVES))
        .save(p, RootsAPI.rl("bark/oak_wood_stripping"));
    })
    .register();*/

  public static final DeferredHolder<Item, Item> SPRUCE_BARK = ITEMS.register("spruce_bark", () -> new Item(DEFAULT));
/*      REGISTRATE.item("spruce_bark", Item::new)
    .tag(RootsTags.Items.SPRUCE_BARK)
    .model(subfolder("bark"))
    .recipe((ctx, p) -> {
      BarkRecipe.builder(ctx.getEntry(), 2)
        .setOutputState(Blocks.STRIPPED_SPRUCE_LOG.defaultBlockState())
        .setCondition(new WorldRecipe.Condition(new BlockMatchTest(Blocks.SPRUCE_LOG)))
        .unlockedBy("has_knife", p.has(RootsTags.Items.KNIVES))
        .save(p, RootsAPI.rl("bark/spruce_log_stripping"));
      BarkRecipe.builder(ctx.getEntry(), 3)
        .setOutputState(Blocks.STRIPPED_SPRUCE_WOOD.defaultBlockState())
        .setCondition(new WorldRecipe.Condition(new BlockMatchTest(Blocks.SPRUCE_WOOD)))
        .unlockedBy("has_knife", p.has(RootsTags.Items.KNIVES))
        .save(p, RootsAPI.rl("bark/spruce_wood_stripping"));
    })
    .register();*/

  public static final DeferredHolder<Item, Item> WILDWOOD_BARK = ITEMS.register("wildwood_bark", () -> new Item(DEFAULT));
/*      REGISTRATE.item("wildwood_bark", Item::new)
    .tag(RootsTags.Items.WILDWOOD_BARK)
    .model(subfolder("bark"))
    .recipe((ctx, p) -> {
      BarkRecipe.builder(ctx.getEntry(), 2)
        .setOutputState(ModBlocks.STRIPPED_WILDWOOD_LOG.getDefaultState())
        .setCondition(new WorldRecipe.Condition(new BlockMatchTest(ModBlocks.WILDWOOD_LOG.get())))
        .unlockedBy("has_knife", p.has(RootsTags.Items.KNIVES))
        .save(p, RootsAPI.rl("bark/wildwood_log_stripping"));
      BarkRecipe.builder(ctx.getEntry(), 3)
        .setOutputState(ModBlocks.STRIPPED_WILDWOOD_WOOD.getDefaultState())
        .setCondition(new WorldRecipe.Condition(new BlockMatchTest(ModBlocks.WILDWOOD_WOOD.get())))
        .unlockedBy("has_knife", p.has(RootsTags.Items.KNIVES))
        .save(p, RootsAPI.rl("bark/wildwood_wood_stripping"));
    })
    .register();*/

  public static final DeferredHolder<Item, Item> CRIMSON_BARK = ITEMS.register("crimson_bark", () -> new Item(DEFAULT));
/*      REGISTRATE.item("crimson_bark", Item::new)
    .tag(RootsTags.Items.CRIMSON_BARK)
    .model(subfolder("bark"))
    .recipe((ctx, p) -> {
      BarkRecipe.builder(ctx.getEntry(), 2)
        .setOutputState(Blocks.STRIPPED_CRIMSON_STEM.defaultBlockState())
        .setCondition(new WorldRecipe.Condition(new BlockMatchTest(Blocks.CRIMSON_STEM)))
        .unlockedBy("has_knife", p.has(RootsTags.Items.KNIVES))
        .save(p, RootsAPI.rl("bark/crimson_stem_stripping"));
      BarkRecipe.builder(ctx.getEntry(), 3)
        .setOutputState(Blocks.STRIPPED_CRIMSON_HYPHAE.defaultBlockState())
        .setCondition(new WorldRecipe.Condition(new BlockMatchTest(Blocks.CRIMSON_HYPHAE)))
        .unlockedBy("has_knife", p.has(RootsTags.Items.KNIVES))
        .save(p, RootsAPI.rl("bark/crimson_hyphae_stripping"));
    })
    .register();*/

  public static final DeferredHolder<Item, Item> WARPED_BARK = ITEMS.register("warped_bark", () -> new Item(DEFAULT));
/*      REGISTRATE.item("warped_bark", Item::new)
    .tag(RootsTags.Items.WARPED_BARK)
    .model(subfolder("bark"))
    .recipe((ctx, p) -> {
      BarkRecipe.builder(ctx.getEntry(), 2)
        .setOutputState(Blocks.STRIPPED_WARPED_STEM.defaultBlockState())
        .setCondition(new WorldRecipe.Condition(new BlockMatchTest(Blocks.WARPED_STEM)))
        .unlockedBy("has_knife", p.has(RootsTags.Items.KNIVES))
        .save(p, RootsAPI.rl("bark/warped_stem_stripping"));
      BarkRecipe.builder(ctx.getEntry(), 3)
        .setOutputState(Blocks.STRIPPED_WARPED_HYPHAE.defaultBlockState())
        .setCondition(new WorldRecipe.Condition(new BlockMatchTest(Blocks.WARPED_HYPHAE)))
        .unlockedBy("has_knife", p.has(RootsTags.Items.KNIVES))
        .save(p, RootsAPI.rl("bark/warped_hyphae_stripping"));
    })
    .register();*/

  public static final DeferredHolder<Item, Item> MANGROVE_BARK = ITEMS.register("mangrove_bark", () -> new Item(DEFAULT));
/*      REGISTRATE.item("mangrove_bark", Item::new)
    .tag(RootsTags.Items.MANGROVE_BARK)
    .model(subfolder("bark"))
    .recipe((ctx, p) -> {
      BarkRecipe.builder(ctx.getEntry(), 2)
        .setOutputState(Blocks.STRIPPED_MANGROVE_LOG.defaultBlockState())
        .setCondition(new WorldRecipe.Condition(new BlockMatchTest(Blocks.MANGROVE_LOG)))
        .unlockedBy("has_knife", p.has(RootsTags.Items.KNIVES))
        .save(p, RootsAPI.rl("bark/mangrove_log_stripping"));
      BarkRecipe.builder(ctx.getEntry(), 3)
        .setOutputState(Blocks.STRIPPED_MANGROVE_WOOD.defaultBlockState())
        .setCondition(new WorldRecipe.Condition(new BlockMatchTest(Blocks.MANGROVE_WOOD)))
        .unlockedBy("has_knife", p.has(RootsTags.Items.KNIVES))
        .save(p, RootsAPI.rl("bark/mangrove_wood_stripping"));
    })
    .register();*/

  public static final DeferredHolder<Item, Item> MIXED_BARK = ITEMS.register("mixed_bark", () -> new Item(DEFAULT));
/*      REGISTRATE.item("mixed_bark", Item::new)
    .tag(RootsTags.Items.MIXED_BARK)
    .model(subfolder("bark"))
    .recipe((ctx, p) -> {
      p.accept(new DynamicBarkRecipe.Result());
    })
    .register();*/

  public static final DeferredHolder<Item, Item> APOTHECARY_POUCH = ITEMS.register("apothecary_pouch", () -> new Item(DEFAULT));
/*      REGISTRATE.item("apothecary_pouch", Item::new)
    .model(subfolder("pouches"))
    .register();*/

  public static final DeferredHolder<Item, Item> COMPONENT_POUCH = ITEMS.register("component_pouch", () -> new Item(DEFAULT));
/*      REGISTRATE.item("component_pouch", Item::new)
    .model(subfolder("pouches"))
    .register();*/

  public static final DeferredHolder<Item, Item> CREATIVE_POUCH = ITEMS.register("creative_pouch", () -> new Item(DEFAULT));
/*      REGISTRATE.item("creative_pouch", Item::new)
    .model(subfolder("pouches"))
    .register();*/

  public static final DeferredHolder<Item, Item> FEY_POUCH = ITEMS.register("fey_pouch", () -> new Item(DEFAULT));
/*      REGISTRATE.item("fey_pouch", Item::new)
    .model(subfolder("pouches"))
    .register();*/

  public static final DeferredHolder<Item, Item> HERB_POUCH = ITEMS.register("herb_pouch", () -> new Item(DEFAULT));
/*      REGISTRATE.item("herb_pouch", Item::new)
    .model(subfolder("pouches"))
    .register();*/

  public static final DeferredHolder<Item, Item> COOKED_PERESKIA = ITEMS.register("cooked_pereskia", () -> new Item(new Item.Properties().food(ModFoods.COOKED_AUBERGINE)));
/*      REGISTRATE.item("cooked_pereskia", Item::new)
    .properties(o -> o.food(ModFoods.COOKED_AUBERGINE))
    .model(subfolder("food"))
    .register();*/

  public static final DeferredHolder<Item, Item> FLOUR = ITEMS.register("flour", () -> new Item(DEFAULT));
/*
      REGISTRATE.item("flour", Item::new)
    .model(subfolder("food"))
    .register();
*/

  public static final DeferredHolder<Item, Item> WILDEWHEET_BREAD = ITEMS.register("wildewheet_bread", () -> new Item(DEFAULT));
/*      REGISTRATE.item("wildewheet_bread", Item::new)
    .model(subfolder("food"))
    .recipe((ctx, p) -> {
      ShapedRecipeBuilder.shaped(ctx.getEntry())
        .pattern("XXX")
        .define('X', RootsTags.Items.WILDEWHEET_CROP)
        .unlockedBy("has_wildewheet", p.has(RootsTags.Items.WILDEWHEET_CROP))
        .save(p, RootsAPI.rl("wildewheet_bread"));
    })
    .register();*/

  public static final DeferredHolder<Item, Item> WILDROOT_STEW = ITEMS.register("wildroot_stew", () -> new Item(new Item.Properties().food(ModFoods.WILDROOT_STEW)));
/*      REGISTRATE.item("wildroot_stew", Item::new)
    .properties(o -> o.food(ModFoods.WILDROOT_STEW))
    .model(subfolder("food"))
    .recipe((ctx, p) -> {
      ShapedRecipeBuilder.shaped(ctx.getEntry(), 3)
        .pattern(" W ")
        .pattern("BBB")
        .define('W', Ingredient.of(RootsTags.Items.WILDROOT_CROP))
        .define('B', Ingredient.of(Items.BOWL))
        .unlockedBy("has_wildroot", p.has(RootsTags.Items.WILDROOT_CROP))
        .save(p, RootsAPI.rl("wildroot_stew"));
    })
    .register();*/

  public static final DeferredHolder<Item, FireStarterItem> FIRE_STARTER = ITEMS.register("fire_starter", () -> new FireStarterItem(DEFAULT));
/*      REGISTRATE.item("fire_starter", FireStarterItem::new)
    .properties(o -> o)
    .model(subfolder("tools"))
    .recipe((ctx, p) -> {
      ShapedRecipeBuilder.shaped(ctx.getEntry(), 4)
        .pattern("SFS")
        .pattern(" L ")
        .pattern("S S")
        .define('S', Ingredient.of(Tags.Items.RODS_WOODEN))
        .define('F', Ingredient.of(RootsTags.Items.FLINT))
        .define('L', Ingredient.of(ItemTags.LOGS))
        .unlockedBy("has_stick", p.has(Tags.Items.RODS_WOODEN))
        .unlockedBy("has_flint", p.has(RootsTags.Items.FLINT))
        .save(p, RootsAPI.rl("fire_starter"));
    })
    .register();*/

  public static final DeferredHolder<Item, Item> GRAMARY = ITEMS.register("gramary", () -> new Item(DEFAULT));
/*      REGISTRATE.item("gramary", Item::new)
    .model(subfolder("tools"))
    .register();*/

  public static final DeferredHolder<Item, Item> LIVING_ARROW = ITEMS.register("living_arrow", () -> new Item(DEFAULT));
/*      REGISTRATE.item("living_arrow", Item::new)
    .model(subfolder("tools"))
    .register();*/

  public static final DeferredHolder<Item, LivingAxeItem> LIVING_AXE = ITEMS.register("living_axe", () -> new LivingAxeItem(RootsAPI.LIVING_TOOL_TIER, DEFAULT));
  /*  7.0F, -3.2F));*/
/*      REGISTRATE.item("living_axe", (p) -> new LivingAxeItem(RootsAPI.LIVING_TOOL_TIER, 7.0F, -3.2F, p))
    .model(subfolder("tools"))
    .recipe((ctx, p) -> GroveRecipe.builder(ctx.getEntry())
      .addIngredient(Items.WOODEN_AXE)
      .addIngredient(net.minecraftforge.common.Tags.Items.INGOTS_GOLD)
      .addIngredient(RootsTags.Items.WILDROOT_CROP)
      .addIngredient(RootsTags.Items.BARKS)
      .addIngredient(RootsTags.Items.BARKS)
      .addLevelCondition(ModConditions.GROVE_STONE_VALID.get())
      .unlockedBy("has_gold", p.has(Tags.Items.INGOTS_GOLD))
      .unlockedBy("has_wildroot", p.has(RootsTags.Items.WILDROOT_CROP))
      .save(p, RootsAPI.rl("grove/living_axe")))
    .register();*/

  public static final DeferredHolder<Item, LivingHoeItem> LIVING_HOE = ITEMS.register("living_hoe", () -> new LivingHoeItem(RootsAPI.LIVING_TOOL_TIER, DEFAULT));
/*      REGISTRATE.item("living_hoe", (p) -> new LivingHoeItem(RootsAPI.LIVING_TOOL_TIER, -1, -2.0f, p))
    .model(subfolder("tools"))
    .recipe((ctx, p) -> GroveRecipe.builder(ctx.getEntry())
      .addIngredient(Items.WOODEN_HOE)
      .addIngredient(net.minecraftforge.common.Tags.Items.INGOTS_GOLD)
      .addIngredient(RootsTags.Items.WILDROOT_CROP)
      .addIngredient(RootsTags.Items.BARKS)
      .addIngredient(RootsTags.Items.BARKS)
      .addLevelCondition(ModConditions.GROVE_STONE_VALID.get())
      .unlockedBy("has_gold", p.has(Tags.Items.INGOTS_GOLD))
      .unlockedBy("has_wildroot", p.has(RootsTags.Items.WILDROOT_CROP))
      .save(p, RootsAPI.rl("grove/living_hoe")))
    .register();*/

  public static final DeferredHolder<Item, LivingPickaxeItem> LIVING_PICKAXE = ITEMS.register("living_pickaxe", () -> new LivingPickaxeItem(RootsAPI.LIVING_TOOL_TIER, DEFAULT));
/*      REGISTRATE.item("living_pickaxe", (p) -> new LivingPickaxeItem(RootsAPI.LIVING_TOOL_TIER, 1, -2.8f, p))
    .model(subfolder("tools"))
    .recipe((ctx, p) -> GroveRecipe.builder(ctx.getEntry())
      .addIngredient(Items.WOODEN_PICKAXE)
      .addIngredient(net.minecraftforge.common.Tags.Items.INGOTS_GOLD)
      .addIngredient(RootsTags.Items.WILDROOT_CROP)
      .addIngredient(RootsTags.Items.BARKS)
      .addIngredient(RootsTags.Items.BARKS)
      .addLevelCondition(ModConditions.GROVE_STONE_VALID.get())
      .unlockedBy("has_gold", p.has(Tags.Items.INGOTS_GOLD))
      .unlockedBy("has_wildroot", p.has(RootsTags.Items.WILDROOT_CROP))
      .save(p, RootsAPI.rl("grove/living_pickaxe")))
    .register();*/

  public static final DeferredHolder<Item, LivingShovelItem> LIVING_SHOVEL = ITEMS.register("living_shovel", () -> new LivingShovelItem(RootsAPI.LIVING_TOOL_TIER, DEFAULT));
/*      REGISTRATE.item("living_shovel", (p) -> new LivingShovelItem(RootsAPI.LIVING_TOOL_TIER, 1.5F, -3.0F, p))
    .model(subfolder("tools"))
    .recipe((ctx, p) -> GroveRecipe.builder(ctx.getEntry())
      .addIngredient(Items.WOODEN_SHOVEL)
      .addIngredient(net.minecraftforge.common.Tags.Items.INGOTS_GOLD)
      .addIngredient(RootsTags.Items.WILDROOT_CROP)
      .addIngredient(RootsTags.Items.BARKS)
      .addIngredient(RootsTags.Items.BARKS)
      .addLevelCondition(ModConditions.GROVE_STONE_VALID.get())
      .unlockedBy("has_gold", p.has(Tags.Items.INGOTS_GOLD))
      .unlockedBy("has_wildroot", p.has(RootsTags.Items.WILDROOT_CROP))
      .save(p, RootsAPI.rl("grove/living_shovel")))
    .register();*/

  public static final DeferredHolder<Item, LivingSwordItem> LIVING_SWORD = ITEMS.register("living_sword", () -> new LivingSwordItem(RootsAPI.LIVING_TOOL_TIER, DEFAULT));

 /*     REGISTRATE.item("living_sword", (p) -> new LivingSwordItem(RootsAPI.LIVING_TOOL_TIER, 3, -2.4f, p))
    .model(subfolder("tools"))
    .recipe((ctx, p) -> GroveRecipe.builder(ctx.getEntry())
      .addIngredient(Items.WOODEN_SWORD)
      .addIngredient(net.minecraftforge.common.Tags.Items.INGOTS_GOLD)
      .addIngredient(RootsTags.Items.WILDROOT_CROP)
      .addIngredient(RootsTags.Items.BARKS)
      .addIngredient(RootsTags.Items.BARKS)
      .addLevelCondition(ModConditions.GROVE_STONE_VALID.get())
      .unlockedBy("has_gold", p.has(Tags.Items.INGOTS_GOLD))
      .unlockedBy("has_wildroot", p.has(RootsTags.Items.WILDROOT_CROP))
      .save(p, RootsAPI.rl("grove/living_sword")))
    .register();*/

  public static final DeferredHolder<Item, Item> PESTLE = ITEMS.register("pestle", () -> new Item(DEFAULT));
/*      REGISTRATE.item("pestle", Item::new)
    .model(subfolder("tools"))
    .tag(RootsTags.Items.MORTAR_ACTIVATION)
    .recipe((ctx, p) -> {
      ShapedRecipeBuilder.shaped(ctx.getEntry())
        .pattern("  S")
        .pattern("SS ")
        .pattern("SS ")
        .define('S', Ingredient.of(RootsTags.Items.STONELIKE))
        .unlockedBy("has_stone", p.has(RootsTags.Items.STONELIKE))
        .save(p, RootsAPI.rl("pestle"));
    })
    .register();*/

  public static final DeferredHolder<Item, AxeItem> RUNED_AXE = ITEMS.register("runed_axe", () -> new AxeItem(RootsAPI.RUNED_TIER, DEFAULT));
/*  REGISTRATE.item("runed_axe", Item::new)
    .model(subfolder("tools"))
    .register();*/

/*  public static final DeferredHolder<Item, Item> RUNED_DAGGER =
      REGISTRATE.item("runed_dagger", Item::new)
    .model(subfolder("tools"))
    .register();*/

  public static final DeferredHolder<Item, HoeItem> RUNED_HOE = ITEMS.register("runed_hoe", () -> new HoeItem(RootsAPI.RUNED_TIER, DEFAULT));

/*      REGISTRATE.item("runed_hoe", Item::new)
    .model(subfolder("tools"))
    .register();*/

  public static final DeferredHolder<Item, ShovelItem> RUNED_SHOVEL = ITEMS.register("runed_shovel", () -> new ShovelItem(RootsAPI.RUNED_TIER, DEFAULT));
/*      REGISTRATE.item("runed_shovel", Item::new)
    .model(subfolder("tools"))
    .register();*/

  public static final DeferredHolder<Item, SwordItem> RUNED_SWORD = ITEMS.register("runed_sword", () -> new SwordItem(RootsAPI.RUNED_TIER, DEFAULT));
/*      REGISTRATE.item("runed_sword", Item::new)
    .model(subfolder("tools"))
    .register();*/

  public static final DeferredHolder<Item, RunicShearsItem> RUNIC_SHEARS = ITEMS.register("runic_shears", () -> new RunicShearsItem(new Item.Properties().durability(313)));
/*      REGISTRATE.item("runic_shears", RunicShearsItem::new)
    .properties(o -> o.durability(313))
    .model(subfolder("tools"))
    .recipe((ctx, p) -> {
      GroveRecipe.builder(ctx.getEntry())
        .addIngredient(Ingredient.of(RootsTags.Items.RUNESTONE))
        .addIngredient(Ingredient.of(RootsTags.Items.RUNESTONE))
        .addIngredient(Ingredient.of(RootsTags.Items.PETALS))
        .addIngredient(Ingredient.of(RootsTags.Items.GROVE_MOSS_CROP))
        .addIngredient(Ingredient.of(ModItems.WOODEN_SHEARS.get()))
        .addLevelCondition(ModConditions.GROVE_STONE_VALID.get())
        .unlockedBy("has_runestone", p.has(RootsTags.Items.RUNESTONE))
        .unlockedBy("has_grove_moss", p.has(RootsTags.Items.GROVE_MOSS_CROP))
        .unlockedBy("has_shears", p.has(ModItems.WOODEN_SHEARS.get()))
        .save(p, RootsAPI.rl("grove/runic_shears"));
    })
    .tag(RootsTags.Items.RUNIC_SHEARS)
    .register();*/

  public static final DeferredHolder<Item, CastingItem> STAFF = ITEMS.register("staff", () -> new CastingItem(DEFAULT));
/*      REGISTRATE.item("staff", CastingItem::new)
    // TODO: CUSTOM MODEL
    .model((ctx, p) -> {
      ModelFile generated = new ModelFile.UncheckedModelFile("item/generated");
      p.getBuilder(ctx.getName()).parent(generated).texture("layer0", p.modLoc("item/tools/staff")).texture("layer1", p.modLoc("item/tools/staff_petal_1")).texture("layer2", p.modLoc("item/tools/staff_petal_2"));
    })
    .tag(RootsTags.Items.CASTING_TOOLS)
    .recipe((ctx, p) -> {
      ShapedRecipeBuilder.shaped(ctx.getEntry())
        .pattern(" WX")
        .pattern(" XW")
        .pattern("X  ")
        .define('X', ItemTags.LOGS)
        .define('W', RootsTags.Items.WILDROOT_CROP)
        .unlockedBy("has_wildroot", p.has(RootsTags.Items.WILDROOT_CROP))
        .save(p, RootsAPI.rl("staff"));
    })
    .register();*/

  public static final DeferredHolder<Item, Item> WILDWOOD_BOW = ITEMS.register("wildwood_bow", () -> new Item(DEFAULT));
/*  public static final DeferredHolder<Item, Item> WILDWOOD_BOW = REGISTRATE.item("wildwood_bow", Item::new)
    // TODO: MODEL, ETC
    .model(subfolder("tools"))
    .register();*/

  public static final DeferredHolder<Item, Item> WILDWOOD_QUIVER = ITEMS.register("wildwood_quiver", () -> new Item(DEFAULT));
/*  public static final DeferredHolder<Item, Item> WILDWOOD_QUIVER = REGISTRATE.item("wildwood_quiver", Item::new)
    .model(subfolder("tools"))
    .register();*/

  public static final DeferredHolder<Item, ShearsItem> WOODEN_SHEARS = ITEMS.register("wooden_shears", () -> new ShearsItem(new Item.Properties().durability(120)));
/*  REGISTRATE.item("wooden_shears", ShearsItem::new)
    .properties(o -> o.durability(120))
    .model(subfolder("tools"))
    .recipe((ctx, p) -> {
      ShapedRecipeBuilder.shaped(ctx.getEntry())
        .pattern(" LL")
        .pattern("L  ")
        .pattern(" LL")
        .define('L', Ingredient.of(ItemTags.LOGS))
        .unlockedBy("has_log", p.has(ItemTags.LOGS))
        .save(p, RootsAPI.rl("wooden_shears"));
    })
    .register();*/

  public static DeferredHolder<Item, KnifeItem> WOODEN_KNIFE = ITEMS.register("wooden_knife", () -> new KnifeItem(Tiers.WOOD, DEFAULT));
  /*      REGISTRATE.item("wood_knife", (p) -> new KnifeItem(Tiers.WOOD, 0f, -1.5f, p))
      .tag(RootsTags.Items.KNIVES)
      .model((ctx, p) -> p.handheld(ModItems.WOODEN_KNIFE))
      .recipe((ctx, p) -> RECIPES.knife(ItemTags.PLANKS, ModItems.WOODEN_KNIFE, null, p)).register();*/
  public static DeferredHolder<Item, KnifeItem> STONE_KNIFE = ITEMS.register("stone_knife", () -> new KnifeItem(Tiers.STONE, DEFAULT));
  /*REGISTRATE.item("stone_knife", (p) -> new KnifeItem(Tiers.STONE, 0f, -1.0f, p))
  .tag(RootsTags.Items.KNIVES)
  .model((ctx, p) -> p.handheld(ModItems.STONE_KNIFE))
  .recipe((ctx, p) -> {
    RECIPES.knife(Tags.Items.STONE, ModItems.STONE_KNIFE, null, p);
    RECIPES.knife(Tags.Items.COBBLESTONE, ModItems.STONE_KNIFE, null, p);
  }).register();*/
  public static DeferredHolder<Item, KnifeItem> COPPER_KNIFE = ITEMS.register("copper_knife", () -> new KnifeItem(RootsAPI.COPPER_TIER, DEFAULT));
  /*  REGISTRATE.item("copper_knife", (p) -> new KnifeItem(RootsAPI.COPPER_TIER, 0f, -1.5f, p))
      .tag(RootsTags.Items.COPPER_ITEMS, RootsTags.Items.KNIVES)
      .model((ctx, p) -> p.handheld(ModItems.COPPER_KNIFE))
      .recipe((ctx, p) -> RECIPES.knife(Tags.Items.STORAGE_BLOCKS_COPPER, ModItems.COPPER_KNIFE, null, p)).register();*/
  public static DeferredHolder<Item, KnifeItem> IRON_KNIFE = ITEMS.register("iron_knife", () -> new KnifeItem(Tiers.IRON, DEFAULT));
  /*    REGISTRATE.item("iron_knife", (p) -> new KnifeItem(Tiers.IRON, 0, -1.5f, p))
      .tag(RootsTags.Items.KNIVES)
      .model((ctx, p) -> p.handheld(ModItems.IRON_KNIFE))
      .recipe((ctx, p) -> RECIPES.knife(Tags.Items.INGOTS_IRON, ModItems.IRON_KNIFE, null, p)).register();*/
  public static DeferredHolder<Item, KnifeItem> GOLD_KNIFE = ITEMS.register("gold_knife", () -> new KnifeItem(Tiers.GOLD, DEFAULT));
  /*    REGISTRATE.item("gold_knife", (p) -> new KnifeItem(Tiers.GOLD, 0f, -1.0f, p))
      .tag(RootsTags.Items.KNIVES)
      .model((ctx, p) -> p.handheld(ModItems.GOLD_KNIFE))
      .recipe((ctx, p) -> RECIPES.knife(Tags.Items.INGOTS_GOLD, ModItems.GOLD_KNIFE, null, p))
      .tag(ItemTags.PIGLIN_LOVED)
      .register();*/
  public static DeferredHolder<Item, KnifeItem> SILVER_KNIFE = ITEMS.register("silver_knife", () -> new KnifeItem(Tiers.GOLD, DEFAULT));
  /*    REGISTRATE.item("silver_knife", (p) -> new KnifeItem(Tiers.GOLD, 0f, -1.0f, p))
      .tag(RootsTags.Items.KNIVES, RootsTags.Items.SILVER_ITEMS)
      .model((ctx, p) -> p.handheld(ctx::getEntry))
      .recipe((ctx, p) -> RECIPES.knife(RootsTags.Items.SILVER_INGOT, ctx::getEntry, null, p))
      .register();*/
  public static DeferredHolder<Item, KnifeItem> DIAMOND_KNIFE = ITEMS.register("diamond_knife", () -> new KnifeItem(Tiers.DIAMOND, DEFAULT));
  /*    REGISTRATE.item("diamond_knife", (p) -> new KnifeItem(Tiers.DIAMOND, 0.5f, -1.2f, p))
      .tag(RootsTags.Items.KNIVES)
      .model((ctx, p) -> p.handheld(ModItems.DIAMOND_KNIFE))
      .recipe((ctx, p) -> RECIPES.knife(Tags.Items.GEMS_DIAMOND, ModItems.DIAMOND_KNIFE, null, p)).register();*/
  public static DeferredHolder<Item, KnifeItem> NETHERITE_KNIFE = ITEMS.register("netherite_knife", () -> new KnifeItem(Tiers.NETHERITE, DEFAULT));
/*
    REGISTRATE.item("netherite_knife", (p) -> new KnifeItem(Tiers.NETHERITE, 0.5f, -1.2f, p))
    .tag(RootsTags.Items.KNIVES)
    .model((ctx, p) -> p.handheld(ModItems.NETHERITE_KNIFE))
    .recipe((ctx, p) -> RECIPES.knife(Tags.Items.INGOTS_NETHERITE, ModItems.NETHERITE_KNIFE, null, p)).register();
*/

  public static final DeferredHolder<Item, Item> RELIQUARY = ITEMS.register("reliquary", () -> new Item(DEFAULT));
/*      REGISTRATE.item("reliquary", Item::new)
    .model(subfolder("containers"))
    .register();*/

  public static final DeferredHolder<Item, Item> SPIRIT_BAG = ITEMS.register("spirit_bag", () -> new Item(DEFAULT));
/*      REGISTRATE.item("spirit_bag", Item::new)
    .model(subfolder("containers"))
    .register();*/

  public static final DeferredHolder<Item, Item> FEY_LEATHER = ITEMS.register("fey_leather", () -> new Item(DEFAULT));
/*      REGISTRATE.item("fey_leather", Item::new)
    .model(subfolder("resources"))
    .recipe((ctx, p) -> {
      RunicEntityRecipe.builder(ctx.getEntry())
        .setCooldown(2 * 60 * 20)
        .setTest(new EntityTagTest(RootsTags.Entities.FEY_LEATHER))
        .unlockedBy("has_shears", p.has(RootsTags.Items.RUNIC_SHEARS))
        .save(p, RootsAPI.rl("runic/entity/fey_leather"));
    })
    .register();*/

  public static final DeferredHolder<Item, Item> GLASS_EYE = ITEMS.register("glass_eye", () -> new Item(DEFAULT));
/*      REGISTRATE.item("glass_eye", Item::new)
    .model(subfolder("resources"))
    .register();*/

  public static final DeferredHolder<Item, Item> LIFE_ESSENCE = ITEMS.register("life_essence", () -> new Item(DEFAULT));
/*
      "REGISTRATE.item("life_essence", Item::new)
    .model(subfolder("resources"))
    .register();
*/

  public static final DeferredHolder<Item, Item> MYSTIC_FEATHER = ITEMS.register("mystic_feather", () -> new Item(DEFAULT));/* +
      "REGISTRATE.item("mystic_feather", Item::new)
    .model(subfolder("resources"))
    .register();*/

  public static final DeferredHolder<Item, Item> PETALS = ITEMS.register("petals", () -> new Item(DEFAULT));
/*      REGISTRATE.item("petals", Item::new)
    .model(subfolder("resources"))
    .tag(RootsTags.Items.PETALS)
    .recipe((ctx, p) -> {
      MortarRecipe.multiBuilder(ctx.getEntry(), 1)
        .addIngredient(ItemTags.SMALL_FLOWERS)
        .unlockedBy("has_flower", p.has(ItemTags.SMALL_FLOWERS))
        .save(p, RootsAPI.rl("petals_from_small_flowers"));
      MortarRecipe.multiBuilder(new ItemStack(ctx.getEntry(), 2), 2)
        .addIngredient(ItemTags.TALL_FLOWERS)
        .unlockedBy("has_flower", p.has(ItemTags.TALL_FLOWERS))
        .save(p, RootsAPI.rl("petals_from_tall_flowers"));
    })
    .register();*/

  public static final DeferredHolder<Item, Item> RUNIC_DUST = ITEMS.register("runic_dust", () -> new Item(DEFAULT));

/*      REGISTRATE.item("runic_dust", Item::new)
    .model(subfolder("resources"))
    .recipe((ctx, p) -> MortarRecipe.multiBuilder(ctx.getEntry(), 5)
      .addIngredient(RootsTags.Items.RUNESTONE)
      .unlockedBy("has_runestone", p.has(RootsTags.Items.RUNESTONE))
      .save(p, RootsAPI.rl("runic_dust")))
    .tag(RootsTags.Items.RUNIC_DUST)
    .register();*/

  public static final DeferredHolder<Item, Item> STRANGE_OOZE = ITEMS.register("strange_ooze", () -> new Item(DEFAULT));/*REGISTRATE.item("strange_ooze", Item::new)
    .model(subfolder("resources"))
    .register();*/

/*  public static DeferredHolder<Item, AntlerHatItem> ANTLER_HAT = ITEMS.register("antler_hat", () -> new AntlerHatItem(DEFAULT));*/
    /* REGISTRATE.item("antler_hat", AntlerHatItem::new)
    .properties(o -> o.durability(399).rarity(Rarity.RARE))
    .recipe((o, p) -> ShapedRecipeBuilder.shaped(o.getEntry(), 1)
      .pattern("AWA")
      .pattern("WWW")
      .pattern("S S")
      .define('A', ModItems.ANTLERS.get())
      .define('W', ItemTags.WOOL)
      .define('S', Tags.Items.STRING)
      .unlockedBy("has_antlers", RegistrateRecipeProvider.has(ModItems.ANTLERS.get()))
      .save(p))
    .register();*/

  public static DeferredHolder<Item, ArmorItem> BEETLE_HELMET = ITEMS.register("beetle_helmet", () -> new ArmorItem(ArmorMaterials.TURTLE, ArmorItem.Type.HELMET, DEFAULT));
    /*REGISTRATE.item("beetle_helmet", (b) -> new BeetleArmorItem(b, EquipmentSlot.HEAD))
    .properties(o -> o.rarity(Rarity.RARE))
    .recipe((o, p) -> ShapedRecipeBuilder.shaped(o.getEntry(), 1)
      .pattern("CCC")
      .pattern("C C")
      .define('C', RootsTags.Items.CARAPACE)
      .unlockedBy("has_carapace", RegistrateRecipeProvider.has(RootsTags.Items.CARAPACE))
      .save(p))
    .register();*/

  public static DeferredHolder<Item, ArmorItem> BEETLE_CHESTPLATE = ITEMS.register("beetle_chestplate", () -> new ArmorItem(ArmorMaterials.TURTLE, ArmorItem.Type.CHESTPLATE, DEFAULT));
/*      REGISTRATE.item("beetle_chestplate", (b) -> new BeetleArmorItem(b, EquipmentSlot.CHEST))
    .properties(o -> o.rarity(Rarity.RARE))
    .recipe((ctx, p) -> ShapedRecipeBuilder.shaped(ctx.getEntry(), 1)
      .pattern("C C")
      .pattern("CCC")
      .pattern("CCC")
      .define('C', RootsTags.Items.CARAPACE)
      .unlockedBy("has_carapace", RegistrateRecipeProvider.has(RootsTags.Items.CARAPACE))
      .save(p))
    .register();*/

  public static DeferredHolder<Item, ArmorItem> BEETLE_LEGGINGS = ITEMS.register("beetle_leggings", () -> new ArmorItem(ArmorMaterials.TURTLE, ArmorItem.Type.LEGGINGS, DEFAULT));
/*      REGISTRATE.item("beetle_leggings", (b) -> new BeetleArmorItem(b, EquipmentSlot.LEGS))
    .recipe((ctx, p) -> ShapedRecipeBuilder.shaped(ctx.getEntry(), 1)
      .pattern("CCC")
      .pattern("C C")
      .pattern("C C")
      .define('C', RootsTags.Items.CARAPACE)
      .unlockedBy("has_carapace", RegistrateRecipeProvider.has(RootsTags.Items.CARAPACE))
      .save(p))
    .register();*/

  public static DeferredHolder<Item, ArmorItem> BEETLE_BOOTS = ITEMS.register("beetle_boots", () -> new ArmorItem(ArmorMaterials.TURTLE, ArmorItem.Type.BOOTS, DEFAULT));
/*      REGISTRATE.item("beetle_boots", (b) -> new BeetleArmorItem(b, EquipmentSlot.FEET))
    .recipe((ctx, p) -> ShapedRecipeBuilder.shaped(ctx.getEntry(), 1)
      .pattern("C C")
      .pattern("C C")
      .define('C', RootsTags.Items.CARAPACE)
      .unlockedBy("has_carapace", RegistrateRecipeProvider.has(RootsTags.Items.CARAPACE))
      .save(p))
    .register();*/

  public static DeferredHolder<Item, Item> RAW_SILVER = ITEMS.register("raw_silver", () -> new Item(DEFAULT));
  /*      REGISTRATE.item("raw_silver", Item::new)
      .tag(RootsTags.Items.RAW_SILVER)
      .recipe((ctx, p) -> {
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(RootsTags.Items.RAW_SILVER), ModItems.SILVER_INGOT.get(), 0.7f, 200)
          .unlockedBy("has_raw_silver", RegistrateRecipeProvider.has(RootsTags.Items.RAW_SILVER))
          .save(p, RootsAPI.rl("silver_ingot_from_raw_silver_smelting"));
        SimpleCookingRecipeBuilder.blasting(Ingredient.of(RootsTags.Items.RAW_SILVER), ModItems.SILVER_INGOT.get(), 0.7f, 100)
          .unlockedBy("has_raw_silver", RegistrateRecipeProvider.has(RootsTags.Items.RAW_SILVER))
          .save(p, RootsAPI.rl("silver_ingot_from_raw_silver_blasting"));
        ShapelessRecipeBuilder.shapeless(ctx.getEntry(), 9)
          .requires(RootsTags.Items.RAW_SILVER_STORAGE)
          .unlockedBy("has_raw_silver_storage", RegistrateRecipeProvider.has(RootsTags.Items.RAW_SILVER_STORAGE))
          .save(p);
      })
      .register();*/
  public static DeferredHolder<Item, Item> SILVER_INGOT = ITEMS.register("silver_ingot", () -> new Item(DEFAULT));
/*    REGISTRATE.item("silver_ingot", Item::new)
    .tag(RootsTags.Items.SILVER_INGOT, ItemTags.BEACON_PAYMENT_ITEMS)
    .recipe(RECIPES.storage(() -> ModBlocks.SILVER_BLOCK, () -> ModItems.SILVER_INGOT, RootsTags.Items.SILVER_STORAGE, RootsTags.Items.SILVER_INGOT, RootsTags.Items.SILVER_ORE, () -> ModItems.SILVER_NUGGET, RootsTags.Items.SILVER_NUGGET, null))
    .register();*/

  public static DeferredHolder<Item, Item> SILVER_NUGGET = ITEMS.register("silver_nugget", () -> new Item(DEFAULT));
/*

      REGISTRATE.item("silver_nugget", Item::new)
    .tag(RootsTags.Items.SILVER_NUGGET)
    .recipe((ctx, p) -> {
      RECIPES.recycle(RootsTags.Items.SILVER_ITEMS, ModItems.SILVER_NUGGET, 0.15f, p);
    })
    .register();
*/

  public static DeferredHolder<Item, Item> COPPER_NUGGET = ITEMS.register("copper_nugget", () -> new Item(DEFAULT));

/*
      REGISTRATE.item("copper_nugget", Item::new)
    .tag(RootsTags.Items.COPPER_NUGGET)
    .recipe((ctx, p) -> {
      RECIPES.recycle(RootsTags.Items.COPPER_ITEMS, () -> Items.COPPER_INGOT, 0.15f, p);
      RECIPES.recycle(ModItems.GOLD_KNIFE, () -> Items.GOLD_NUGGET, 0.15f, RootsAPI.MODID, p);
      RECIPES.recycle(ModItems.IRON_KNIFE, () -> Items.IRON_NUGGET, 0.15f, RootsAPI.MODID, p);
      // TODO: Nugget change?
      ShapelessRecipeBuilder.shapeless(ctx.getEntry(), 9)
        .requires(Tags.Items.INGOTS_COPPER)
        .unlockedBy("has_copper_ingot", RegistrateRecipeProvider.has(Tags.Items.INGOTS_COPPER))
        .save(p, RootsAPI.rl("copper_nugget_from_ingot"));
      ShapedRecipeBuilder.shaped(Items.COPPER_INGOT)
        .pattern("***")
        .pattern("*X*")
        .pattern("***")
        .define('*', RootsTags.Items.COPPER_NUGGET)
        .define('X', ctx.getEntry())
        .unlockedBy("has_copper_nugget", p.has(RootsTags.Items.COPPER_NUGGET))
        .save(p, RootsAPI.rl("copper_ingot_from_nuggets"));
    })
    .register();

*/

  // TODO: Check damage values
  public static DeferredHolder<Item, AxeItem> COPPER_AXE = ITEMS.register("copper_axe", () -> new AxeItem(RootsAPI.COPPER_TIER, DEFAULT));

  /*      REGISTRATE.item("copper_axe", (p) -> new AxeItem(RootsAPI.COPPER_TIER, 5.0f, -3.1f, p))
      .tag(RootsTags.Items.COPPER_ITEMS)
      .model((ctx, p) -> p.handheld(ModItems.COPPER_AXE))
      .recipe((ctx, p) -> RECIPES.axe(Tags.Items.STORAGE_BLOCKS_COPPER, ModItems.COPPER_AXE, null, p)).register();*/
  public static DeferredHolder<Item, HoeItem> COPPER_HOE = ITEMS.register("copper_hoe", () -> new HoeItem(RootsAPI.COPPER_TIER, DEFAULT));
  /*REGISTRATE.item("copper_hoe", (p) -> new HoeItem(RootsAPI.COPPER_TIER, 1, -1f, p))
    .tag(RootsTags.Items.COPPER_ITEMS)
    .model((ctx, p) -> p.handheld(ModItems.COPPER_HOE))
    .recipe((ctx, p) -> RECIPES.hoe(Tags.Items.STORAGE_BLOCKS_COPPER, ModItems.COPPER_HOE, null, p)).register();*/
  public static DeferredHolder<Item, PickaxeItem> COPPER_PICKAXE = ITEMS.register("copper_pickaxe", () -> new PickaxeItem(RootsAPI.COPPER_TIER, DEFAULT));

  /*REGISTRATE.item("copper_pickaxe", (p) -> new PickaxeItem(RootsAPI.COPPER_TIER, 1, -1f, p))
    .tag(RootsTags.Items.COPPER_ITEMS)
    .model((ctx, p) -> p.handheld(ModItems.COPPER_PICKAXE))
    .recipe((ctx, p) -> RECIPES.pickaxe(Tags.Items.STORAGE_BLOCKS_COPPER, ModItems.COPPER_PICKAXE, null, p)).register();*/
  public static DeferredHolder<Item, ShovelItem> COPPER_SHOVEL = ITEMS.register("copper_shovel", () -> new ShovelItem(RootsAPI.COPPER_TIER, DEFAULT));

  /*REGISTRATE.item("copper_shovel", (p) -> new ShovelItem(RootsAPI.COPPER_TIER, 1, -1f, p))
    .tag(RootsTags.Items.COPPER_ITEMS)
    .model((ctx, p) -> p.handheld(ModItems.COPPER_SHOVEL))
    .recipe((ctx, p) -> RECIPES.shovel(Tags.Items.STORAGE_BLOCKS_COPPER, ModItems.COPPER_SHOVEL, null, p)).register();*/
  public static DeferredHolder<Item, SwordItem> COPPER_SWORD = ITEMS.register("copper_sword", () -> new SwordItem(RootsAPI.COPPER_TIER, DEFAULT));

    /*REGISTRATE.item("copper_sword", (p) -> new SwordItem(RootsAPI.COPPER_TIER, 1, -1f, p))
    .tag(RootsTags.Items.COPPER_ITEMS)
    .model((ctx, p) -> p.handheld(ModItems.COPPER_SWORD))
    .recipe((ctx, p) -> RECIPES.sword(Tags.Items.STORAGE_BLOCKS_COPPER, ModItems.COPPER_SWORD, null, p)).register();*/

  public static DeferredHolder<Item, ArmorItem> COPPER_HELMET = ITEMS.register("copper_helmet", () -> new ArmorItem(ArmorMaterials.GOLD, ArmorItem.Type.HELMET, DEFAULT));
  /*      REGISTRATE.item("copper_helmet", (p) -> new ArmorItem(Roots.COPPER_MATERIAL, EquipmentSlot.HEAD, p))
      .recipe((ctx, p) -> RECIPES.helmet(Tags.Items.STORAGE_BLOCKS_COPPER, ModItems.COPPER_HELMET, null, p))
      .tag(RootsTags.Items.COPPER_ITEMS)
      .register();*/
  public static DeferredHolder<Item, ArmorItem> COPPER_CHESTPLATE = ITEMS.register("copper_chestplate", () -> new ArmorItem(ArmorMaterials.GOLD, ArmorItem.Type.CHESTPLATE, DEFAULT));

  /*REGISTRATE.item("copper_chestplate", (p) -> new ArmorItem(Roots.COPPER_MATERIAL, EquipmentSlot.CHEST, p))
      .recipe((ctx, p) -> RECIPES.chest(Tags.Items.STORAGE_BLOCKS_COPPER, ModItems.COPPER_CHESTPLATE, null, p))
      .tag(RootsTags.Items.COPPER_ITEMS)
      .register();*/
  public static DeferredHolder<Item, ArmorItem> COPPER_LEGGINGS = ITEMS.register("copper_leggings", () -> new ArmorItem(ArmorMaterials.GOLD, ArmorItem.Type.LEGGINGS, DEFAULT));
  /*      REGISTRATE.item("copper_leggings", (p) -> new ArmorItem(Roots.COPPER_MATERIAL, EquipmentSlot.LEGS, p))
        .recipe((ctx, p) -> RECIPES.legs(Tags.Items.STORAGE_BLOCKS_COPPER, ModItems.COPPER_LEGGINGS, null, p))
        .tag(RootsTags.Items.COPPER_ITEMS)
        .register();*/
  public static DeferredHolder<Item, ArmorItem> COPPER_BOOTS = ITEMS.register("copper_boots", () -> new ArmorItem(ArmorMaterials.GOLD, ArmorItem.Type.BOOTS, DEFAULT));
    /*REGISTRATE.item("copper_boots", (p) -> new ArmorItem(Roots.COPPER_MATERIAL, EquipmentSlot.FEET, p))
      .recipe((ctx, p) -> RECIPES.boots(Tags.Items.STORAGE_BLOCKS_COPPER, ModItems.COPPER_BOOTS, null, p))
      .tag(RootsTags.Items.COPPER_ITEMS)
      .register();*/

  public static DeferredHolder<Item, DeferredSpawnEggItem> BEETLE_SPAWN_EGG = ITEMS.register("beetle_spawn_egg", () -> new DeferredSpawnEggItem(ModEntities.BEETLE, 0x418594, 0x211D15, DEFAULT));

  public static DeferredHolder<Item, DeferredSpawnEggItem> DEER_SPAWN_EGG = ITEMS.register("deer_spawn_egg", () -> new DeferredSpawnEggItem(ModEntities.DEER, 0xa18458, 0x5e4d33, DEFAULT));

  public static DeferredHolder<Item, DeferredSpawnEggItem> FENNEC_SPAWN_EGG = ITEMS.register("fennec_spawn_egg", () -> new DeferredSpawnEggItem(ModEntities.FENNEC, 0xe9dcc2, 0xb1855c, DEFAULT));

  public static DeferredHolder<Item, DeferredSpawnEggItem> GREEN_SPROUT_SPAWN_EGG = ITEMS.register("green_sprout_spawn_egg", () -> new DeferredSpawnEggItem(ModEntities.GREEN_SPROUT, 0x9adb58, 0x2c9425, DEFAULT));

  public static DeferredHolder<Item, DeferredSpawnEggItem> TAN_SPROUT_SPAWN_EGG = ITEMS.register("tan_sprout_spawn_egg", () -> new DeferredSpawnEggItem(ModEntities.TAN_SPROUT, 0xeeca5f, 0xbb6c20, DEFAULT));

  public static DeferredHolder<Item, DeferredSpawnEggItem> RED_SPROUT_SPAWN_EGG = ITEMS.register("red_sprout_spawn_egg", () -> new DeferredSpawnEggItem(ModEntities.RED_SPROUT, 0xe6754c, 0xbd2637, DEFAULT));

  public static DeferredHolder<Item, DeferredSpawnEggItem> PURPLE_SPROUT_SPAWN_EGG = ITEMS.register("purple_sprout_spawn_egg", () -> new DeferredSpawnEggItem(ModEntities.PURPLE_SPROUT, 0xdd45e6, 0x6825ba, DEFAULT));

  public static DeferredHolder<Item, DeferredSpawnEggItem> OWL_SPAWN_EGG = ITEMS.register("owl_spawn_egg", () -> new DeferredSpawnEggItem(ModEntities.OWL, 0x8c654a, 0xdec9ba, DEFAULT));

  public static DeferredHolder<Item, DeferredSpawnEggItem> DUCK_SPAWN_EGG = ITEMS.register("duck_spawn_egg", () -> new DeferredSpawnEggItem(ModEntities.DUCK, 0xe4d6a5, 0xe9ad36, DEFAULT));

  public static DeferredHolder<Item, Item> TOKEN = ITEMS.register("token", () -> new Item(DEFAULT));

/*
  public static DeferredHolder<Item, SpawnEggItem> BEETLE_SPAWN_EGG =
      REGISTRATE.item("beetle_spawn_egg", (p) -> new ForgeSpawnEggItem(ModEntities.BEETLE, 0x418594, 0x211D15, p))
      .properties(o -> o.tab(CreativeModeTab.TAB_MISC))
      .model(ModItems::spawnEggModel)
      .register();

  public static DeferredHolder<Item, ForgeSpawnEggItem> DEER_SPAWN_EGG = REGISTRATE.item("deer_spawn_egg", (p) -> new ForgeSpawnEggItem(ModEntities.DEER, 0xa18458, 0x5e4d33, p))
      .properties(o -> o.tab(CreativeModeTab.TAB_MISC))
      .model(ModItems::spawnEggModel)
      .register();

  public static DeferredHolder<Item, ForgeSpawnEggItem> FENNEC_SPAWN_EGG = REGISTRATE.item("fennec_spawn_egg", (p) -> new ForgeSpawnEggItem(ModEntities.FENNEC, 0xe9dcc2, 0xb1855c, p))
      .properties(o -> o.tab(CreativeModeTab.TAB_MISC))
      .model(ModItems::spawnEggModel)
      .register();

  public static DeferredHolder<Item, ForgeSpawnEggItem> GREEN_SPROUT_SPAWN_EGG = REGISTRATE.item("green_sprout_spawn_egg", (p) -> new ForgeSpawnEggItem(ModEntities.GREEN_SPROUT, 0x9adb58, 0x2c9425, p))
      .properties(o -> o.tab(CreativeModeTab.TAB_MISC))
      .model(ModItems::spawnEggModel)
      .register();

  public static DeferredHolder<Item, ForgeSpawnEggItem> TAN_SPROUT_SPAWN_EGG = REGISTRATE.item("tan_sprout_spawn_egg", (p) -> new ForgeSpawnEggItem(ModEntities.TAN_SPROUT, 0xeeca5f, 0xbb6c20, p))
      .properties(o -> o.tab(CreativeModeTab.TAB_MISC))
      .model(ModItems::spawnEggModel)
      .register();

  public static DeferredHolder<Item, ForgeSpawnEggItem> RED_SPROUT_SPAWN_EGG = REGISTRATE.item("red_sprout_spawn_egg", (p) -> new ForgeSpawnEggItem(ModEntities.RED_SPROUT, 0xe6754c, 0xbd2637, p))
      .properties(o -> o.tab(CreativeModeTab.TAB_MISC))
      .model(ModItems::spawnEggModel)
      .register();

  public static DeferredHolder<Item, ForgeSpawnEggItem> PURPLE_SPROUT_SPAWN_EGG = REGISTRATE.item("purple_sprout_spawn_egg", (p) -> new ForgeSpawnEggItem(ModEntities.PURPLE_SPROUT, 0xdd45e6, 0x6825ba, p))
      .properties(o -> o.tab(CreativeModeTab.TAB_MISC))
      .model(ModItems::spawnEggModel)
      .register();

  public static DeferredHolder<Item, ForgeSpawnEggItem> OWL_SPAWN_EGG = REGISTRATE.item("owl_spawn_egg", (p) -> new ForgeSpawnEggItem(ModEntities.OWL, 0x8c654a, 0xdec9ba, p))
      .properties(o -> o.tab(CreativeModeTab.TAB_MISC))
      .model(ModItems::spawnEggModel)
      .register();

  public static DeferredHolder<Item, ForgeSpawnEggItem> DUCK_SPAWN_EGG = REGISTRATE.item("duck_spawn_egg", (p) -> new ForgeSpawnEggItem(ModEntities.DUCK, 0xe4d6a5, 0xe9ad36, p))
      .properties(o -> o.tab(CreativeModeTab.TAB_MISC))
      .model(ModItems::spawnEggModel)
      .register();

  public static final DeferredHolder<Item, TokenItem> TOKEN = REGISTRATE.item("token", TokenItem::new)
      .model((ctx, p) -> {
        ModelFile generated = new ModelFile.UncheckedModelFile("item/generated");
        for (ResourceLocation ritual : RootsRegistries.RITUAL_REGISTRY.get().getKeys()) {
          p.getBuilder("ritual_" + ritual.getPath()).parent(generated).texture("layer0", p.modLoc("item/rituals/" + ritual.getPath()));
        }
        for (ResourceLocation spell : RootsRegistries.SPELL_REGISTRY.get().getKeys()) {
          p.getBuilder("spell_" + spell.getPath()).parent(generated).texture("layer0", p.modLoc("item/spells/" + spell.getPath()));
        }
      })
      .register();
*/

  public static void register (IEventBus bus) {
    ITEMS.register(bus);
  }
}
