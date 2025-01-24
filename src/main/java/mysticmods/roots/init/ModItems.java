package mysticmods.roots.init;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.datacomponent.SpellStorage;
import mysticmods.roots.api.ritual.Ritual;
import mysticmods.roots.api.spell.Spell;
import mysticmods.roots.item.*;
import mysticmods.roots.item.living.*;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.EnumMap;
import java.util.List;
import java.util.function.Supplier;

public class ModItems {
  private static final DeferredRegister<Item> ITEMS = DeferredRegister.createItems(RootsAPI.MODID);
  private static final DeferredRegister<ArmorMaterial> ARMOR = DeferredRegister.create(Registries.ARMOR_MATERIAL, RootsAPI.MODID);

  private static final Supplier<Item.Properties> DEFAULT_64 = () -> new Item.Properties().stacksTo(64);
  private static final Supplier<Item.Properties> DEFAULT_SINGLE = () -> new Item.Properties().stacksTo(1);

  private static final DeferredHolder<ArmorMaterial, ArmorMaterial> ANTLER_MATERIAL = ARMOR.register("antlers", () -> new ArmorMaterial(
      Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
        map.put(ArmorItem.Type.BOOTS, 0);
        map.put(ArmorItem.Type.LEGGINGS, 0);
        map.put(ArmorItem.Type.CHESTPLATE, 0);
        map.put(ArmorItem.Type.HELMET, 3);
        map.put(ArmorItem.Type.BODY, 0);
      }),
      18, SoundEvents.ARMOR_EQUIP_TURTLE, () -> Ingredient.of(RootsTags.Items.ANTLERS), List.of(new ArmorMaterial.Layer(RootsAPI.rl("antlers"))), 1f, 0f));
  public static final DeferredHolder<ArmorMaterial, ArmorMaterial> CARAPACE_MATERIAL = ARMOR.register("carapace", () -> new ArmorMaterial(
      Util.make(new EnumMap<>(ArmorItem.Type.class),
          map -> {
            map.put(ArmorItem.Type.BOOTS, 2);
            map.put(ArmorItem.Type.LEGGINGS, 6);
            map.put(ArmorItem.Type.CHESTPLATE, 5);
            map.put(ArmorItem.Type.HELMET, 2);
            map.put(ArmorItem.Type.BODY, 0);
          }),
      18,
      SoundEvents.ARMOR_EQUIP_TURTLE,
      () -> Ingredient.of(RootsTags.Items.CARAPACE),
      List.of(new ArmorMaterial.Layer(RootsAPI.rl("carapace"))),
      0f,
      0f));
  private static final DeferredHolder<ArmorMaterial, ArmorMaterial> COPPER_MATERIAL = ARMOR.register("copper", () -> new ArmorMaterial(
      Util.make(new EnumMap<>(ArmorItem.Type.class),
          map -> {
            map.put(ArmorItem.Type.BOOTS, 2);
            map.put(ArmorItem.Type.LEGGINGS, 6);
            map.put(ArmorItem.Type.CHESTPLATE, 5);
            map.put(ArmorItem.Type.HELMET, 2);
            map.put(ArmorItem.Type.BODY, 0);
          }),
      7,
      SoundEvents.ARMOR_EQUIP_IRON,
      () -> Ingredient.of(Tags.Items.INGOTS_COPPER),
      List.of(new ArmorMaterial.Layer(RootsAPI.rl("copper"))),
      0.0f,
      0.0f));

  // BLOCK ITEMS
  public static DeferredHolder<Item, BlockItem> THATCH = ITEMS.register("thatch", () -> new BlockItem(ModBlocks.THATCH.get(), DEFAULT_64.get()));
  public static DeferredHolder<Item, BlockItem> RUNESTONE = ITEMS.register("runestone", () -> new BlockItem(ModBlocks.RUNESTONE.get(), DEFAULT_64.get()));
  public static DeferredHolder<Item, BlockItem> MOSSY_RUNESTONE = ITEMS.register("mossy_runestone", () -> new BlockItem(ModBlocks.MOSSY_RUNESTONE.get(), DEFAULT_64.get()));
  public static DeferredHolder<Item, BlockItem> CHISELED_RUNESTONE = ITEMS.register("chiseled_runestone", () -> new BlockItem(ModBlocks.CHISELED_RUNESTONE.get(), DEFAULT_64.get()));
  public static DeferredHolder<Item, BlockItem> RUNESTONE_BRICK = ITEMS.register("runestone_brick", () -> new BlockItem(ModBlocks.RUNESTONE_BRICK.get(), DEFAULT_64.get()));
  public static DeferredHolder<Item, BlockItem> RUNESTONE_TILE = ITEMS.register("runestone_tile", () -> new BlockItem(ModBlocks.RUNESTONE_TILE.get(), DEFAULT_64.get()));
  public static DeferredHolder<Item, BlockItem> RUNED_OBSIDIAN = ITEMS.register("runed_obsidian", () -> new BlockItem(ModBlocks.RUNED_OBSIDIAN.get(), DEFAULT_64.get()));
  public static DeferredHolder<Item, BlockItem> CHISELED_RUNED_OBSIDIAN = ITEMS.register("chiseled_runed_obsidian", () -> new BlockItem(ModBlocks.CHISELED_RUNED_OBSIDIAN.get(), DEFAULT_64.get()));
  public static DeferredHolder<Item, BlockItem> RUNED_BRICK = ITEMS.register("runed_brick", () -> new BlockItem(ModBlocks.RUNED_BRICK.get(), DEFAULT_64.get()));
  public static DeferredHolder<Item, BlockItem> RUNED_TILE = ITEMS.register("runed_tile", () -> new BlockItem(ModBlocks.RUNED_TILE.get(), DEFAULT_64.get()));
  public static DeferredHolder<Item, BlockItem> SILVER_ORE = ITEMS.register("silver_ore", () -> new BlockItem(ModBlocks.SILVER_ORE.get(), DEFAULT_64.get()));
  public static DeferredHolder<Item, BlockItem> DEEPSLATE_SILVER_ORE = ITEMS.register("deepslate_silver_ore", () -> new BlockItem(ModBlocks.DEEPSLATE_SILVER_ORE.get(), DEFAULT_64.get()));
  public static DeferredHolder<Item, BlockItem> GRANITE_QUARTZ_ORE = ITEMS.register("granite_quartz_ore", () -> new BlockItem(ModBlocks.GRANITE_QUARTZ_ORE.get(), DEFAULT_64.get()));
  public static DeferredHolder<Item, BlockItem> RAW_SILVER_BLOCK = ITEMS.register("raw_silver_block", () -> new BlockItem(ModBlocks.RAW_SILVER_BLOCK.get(), DEFAULT_64.get()));
  public static DeferredHolder<Item, BlockItem> SILVER_BLOCK = ITEMS.register("silver_block", () -> new BlockItem(ModBlocks.SILVER_BLOCK.get(), DEFAULT_64.get()));
  public static DeferredHolder<Item, BlockItem> WILDWOOD_LOG = ITEMS.register("wildwood_log", () -> new BlockItem(ModBlocks.WILDWOOD_LOG.get(), DEFAULT_64.get()));
  public static DeferredHolder<Item, BlockItem> STRIPPED_WILDWOOD_LOG = ITEMS.register("stripped_wildwood_log", () -> new BlockItem(ModBlocks.STRIPPED_WILDWOOD_LOG.get(), DEFAULT_64.get()));
  public static DeferredHolder<Item, BlockItem> WILDWOOD_WOOD = ITEMS.register("wildwood_wood", () -> new BlockItem(ModBlocks.WILDWOOD_WOOD.get(), DEFAULT_64.get()));
  public static DeferredHolder<Item, BlockItem> STRIPPED_WILDWOOD_WOOD = ITEMS.register("stripped_wildwood_wood", () -> new BlockItem(ModBlocks.STRIPPED_WILDWOOD_WOOD.get(), DEFAULT_64.get()));
  public static DeferredHolder<Item, BlockItem> WILDWOOD_PLANKS = ITEMS.register("wildwood_planks", () -> new BlockItem(ModBlocks.WILDWOOD_PLANKS.get(), DEFAULT_64.get()));
  public static DeferredHolder<Item, BlockItem> WILDWOOD_SAPLING = ITEMS.register("wildwood_sapling", () -> new BlockItem(ModBlocks.WILDWOOD_SAPLING.get(), DEFAULT_64.get()));
  public static DeferredHolder<Item, BlockItem> STONEPETAL = ITEMS.register("stonepetal", () -> new BlockItem(ModBlocks.STONEPETAL.get(), DEFAULT_64.get()));
  public static DeferredHolder<Item, BlockItem> WILDWOOD_LEAVES = ITEMS.register("wildwood_leaves", () -> new BlockItem(ModBlocks.WILDWOOD_LEAVES.get(), DEFAULT_64.get()));
  public static DeferredHolder<Item, BlockItem> RUNED_WILDWOOD_LOG = ITEMS.register("runed_wildwood_log", () -> new BlockItem(ModBlocks.RUNED_WILDWOOD_LOG.get(), DEFAULT_64.get()));
  public static DeferredHolder<Item, BlockItem> RUNED_SPRUCE_LOG = ITEMS.register("runed_spruce_log", () -> new BlockItem(ModBlocks.RUNED_SPRUCE_LOG.get(), DEFAULT_64.get()));
  public static DeferredHolder<Item, BlockItem> RUNED_JUNGLE_LOG = ITEMS.register("runed_jungle_log", () -> new BlockItem(ModBlocks.RUNED_JUNGLE_LOG.get(), DEFAULT_64.get()));
  public static DeferredHolder<Item, BlockItem> RUNED_BIRCH_LOG = ITEMS.register("runed_birch_log", () -> new BlockItem(ModBlocks.RUNED_BIRCH_LOG.get(), DEFAULT_64.get()));
  public static DeferredHolder<Item, BlockItem> RUNED_OAK_LOG = ITEMS.register("runed_oak_log", () -> new BlockItem(ModBlocks.RUNED_OAK_LOG.get(), DEFAULT_64.get()));
  public static DeferredHolder<Item, BlockItem> RUNED_DARK_OAK_LOG = ITEMS.register("runed_dark_oak_log", () -> new BlockItem(ModBlocks.RUNED_DARK_OAK_LOG.get(), DEFAULT_64.get()));
  public static DeferredHolder<Item, BlockItem> RUNED_ACACIA_LOG = ITEMS.register("runed_acacia_log", () -> new BlockItem(ModBlocks.RUNED_ACACIA_LOG.get(), DEFAULT_64.get()));
  public static DeferredHolder<Item, BlockItem> RUNED_MANGROVE_LOG = ITEMS.register("runed_mangrove_log", () -> new BlockItem(ModBlocks.RUNED_MANGROVE_LOG.get(), DEFAULT_64.get()));
  public static DeferredHolder<Item, BlockItem> RUNED_WARPED_STEM = ITEMS.register("runed_warped_stem", () -> new BlockItem(ModBlocks.RUNED_WARPED_STEM.get(), DEFAULT_64.get()));
  public static DeferredHolder<Item, BlockItem> RUNED_CRIMSON_STEM = ITEMS.register("runed_crimson_stem", () -> new BlockItem(ModBlocks.RUNED_CRIMSON_STEM.get(), DEFAULT_64.get()));
  public static DeferredHolder<Item, BlockItem> RUNESTONE_STAIRS = ITEMS.register("runestone_stairs", () -> new BlockItem(ModBlocks.RUNESTONE_STAIRS.get(), DEFAULT_64.get()));
  public static DeferredHolder<Item, BlockItem> MOSSY_RUNESTONE_STAIRS = ITEMS.register("mossy_runestone_stairs", () -> new BlockItem(ModBlocks.MOSSY_RUNESTONE_STAIRS.get(), DEFAULT_64.get()));
  public static DeferredHolder<Item, BlockItem> RUNESTONE_BRICK_STAIRS = ITEMS.register("runestone_brick_stairs", () -> new BlockItem(ModBlocks.RUNESTONE_BRICK_STAIRS.get(), DEFAULT_64.get()));
  public static DeferredHolder<Item, BlockItem> RUNESTONE_TILE_STAIRS = ITEMS.register("runestone_tile_stairs", () -> new BlockItem(ModBlocks.RUNESTONE_TILE_STAIRS.get(), DEFAULT_64.get()));
  public static DeferredHolder<Item, BlockItem> RUNED_STAIRS = ITEMS.register("runed_stairs", () -> new BlockItem(ModBlocks.RUNED_STAIRS.get(), DEFAULT_64.get()));
  public static DeferredHolder<Item, BlockItem> RUNED_BRICK_STAIRS = ITEMS.register("runed_brick_stairs", () -> new BlockItem(ModBlocks.RUNED_BRICK_STAIRS.get(), DEFAULT_64.get()));
  public static DeferredHolder<Item, BlockItem> RUNED_TILE_STAIRS = ITEMS.register("runed_tile_stairs", () -> new BlockItem(ModBlocks.RUNED_TILE_STAIRS.get(), DEFAULT_64.get()));
  public static DeferredHolder<Item, BlockItem> WILDWOOD_STAIRS = ITEMS.register("wildwood_stairs", () -> new BlockItem(ModBlocks.WILDWOOD_STAIRS.get(), DEFAULT_64.get()));
  public static DeferredHolder<Item, BlockItem> RUNESTONE_SLAB = ITEMS.register("runestone_slab", () -> new BlockItem(ModBlocks.RUNESTONE_SLAB.get(), DEFAULT_64.get()));
  public static DeferredHolder<Item, BlockItem> MOSSY_RUNESTONE_SLAB = ITEMS.register("mossy_runestone_slab", () -> new BlockItem(ModBlocks.MOSSY_RUNESTONE_SLAB.get(), DEFAULT_64.get()));
  public static DeferredHolder<Item, BlockItem> RUNESTONE_BRICK_SLAB = ITEMS.register("runestone_brick_slab", () -> new BlockItem(ModBlocks.RUNESTONE_BRICK_SLAB.get(), DEFAULT_64.get()));
  public static DeferredHolder<Item, BlockItem> RUNESTONE_TILE_SLAB = ITEMS.register("runestone_tile_slab", () -> new BlockItem(ModBlocks.RUNESTONE_TILE_SLAB.get(), DEFAULT_64.get()));
  public static DeferredHolder<Item, BlockItem> RUNED_SLAB = ITEMS.register("runed_slab", () -> new BlockItem(ModBlocks.RUNED_SLAB.get(), DEFAULT_64.get()));
  public static DeferredHolder<Item, BlockItem> RUNED_BRICK_SLAB = ITEMS.register("runed_brick_slab", () -> new BlockItem(ModBlocks.RUNED_BRICK_SLAB.get(), DEFAULT_64.get()));
  public static DeferredHolder<Item, BlockItem> RUNED_TILE_SLAB = ITEMS.register("runed_tile_slab", () -> new BlockItem(ModBlocks.RUNED_TILE_SLAB.get(), DEFAULT_64.get()));
  public static DeferredHolder<Item, BlockItem> WILDWOOD_SLAB = ITEMS.register("wildwood_slab", () -> new BlockItem(ModBlocks.WILDWOOD_SLAB.get(), DEFAULT_64.get()));
  public static DeferredHolder<Item, BlockItem> WILDWOOD_FENCE = ITEMS.register("wildwood_fence", () -> new BlockItem(ModBlocks.WILDWOOD_FENCE.get(), DEFAULT_64.get()));
  public static DeferredHolder<Item, BlockItem> RUNESTONE_BUTTON = ITEMS.register("runestone_button", () -> new BlockItem(ModBlocks.RUNESTONE_BUTTON.get(), DEFAULT_64.get()));
  public static DeferredHolder<Item, BlockItem> RUNESTONE_BRICK_BUTTON = ITEMS.register("runestone_brick_button", () -> new BlockItem(ModBlocks.RUNESTONE_BRICK_BUTTON.get(), DEFAULT_64.get()));
  public static DeferredHolder<Item, BlockItem> MOSSY_RUNESTONE_BUTTON = ITEMS.register("mossy_runestone_button", () -> new BlockItem(ModBlocks.MOSSY_RUNESTONE_BUTTON.get(), DEFAULT_64.get()));
  public static DeferredHolder<Item, BlockItem> RUNESTONE_TILE_BUTTON = ITEMS.register("runestone_tile_button", () -> new BlockItem(ModBlocks.RUNESTONE_TILE_BUTTON.get(), DEFAULT_64.get()));
  public static DeferredHolder<Item, BlockItem> RUNED_BUTTON = ITEMS.register("runed_button", () -> new BlockItem(ModBlocks.RUNED_BUTTON.get(), DEFAULT_64.get()));
  public static DeferredHolder<Item, BlockItem> RUNED_BRICK_BUTTON = ITEMS.register("runed_brick_button", () -> new BlockItem(ModBlocks.RUNED_BRICK_BUTTON.get(), DEFAULT_64.get()));
  public static DeferredHolder<Item, BlockItem> RUNED_TILE_BUTTON = ITEMS.register("runed_tile_button", () -> new BlockItem(ModBlocks.RUNED_TILE_BUTTON.get(), DEFAULT_64.get()));
  public static DeferredHolder<Item, BlockItem> WILDWOOD_BUTTON = ITEMS.register("wildwood_button", () -> new BlockItem(ModBlocks.WILDWOOD_BUTTON.get(), DEFAULT_64.get()));
  public static DeferredHolder<Item, BlockItem> RUNESTONE_PRESSURE_PLATE = ITEMS.register("runestone_pressure_plate", () -> new BlockItem(ModBlocks.RUNESTONE_PRESSURE_PLATE.get(), DEFAULT_64.get()));
  public static DeferredHolder<Item, BlockItem> RUNESTONE_BRICK_PRESSURE_PLATE = ITEMS.register("runestone_brick_pressure_plate", () -> new BlockItem(ModBlocks.RUNESTONE_BRICK_PRESSURE_PLATE.get(), DEFAULT_64.get()));
  public static DeferredHolder<Item, BlockItem> RUNESTONE_TILE_PRESSURE_PLATE = ITEMS.register("runestone_tile_pressure_plate", () -> new BlockItem(ModBlocks.RUNESTONE_TILE_PRESSURE_PLATE.get(), DEFAULT_64.get()));
  public static DeferredHolder<Item, BlockItem> MOSSY_RUNESTONE_PRESSURE_PLATE = ITEMS.register("mossy_runestone_pressure_plate", () -> new BlockItem(ModBlocks.MOSSY_RUNESTONE_PRESSURE_PLATE.get(), DEFAULT_64.get()));
  public static DeferredHolder<Item, BlockItem> RUNED_PRESSURE_PLATE = ITEMS.register("runed_pressure_plate", () -> new BlockItem(ModBlocks.RUNED_PRESSURE_PLATE.get(), DEFAULT_64.get()));
  public static DeferredHolder<Item, BlockItem> RUNED_BRICK_PRESSURE_PLATE = ITEMS.register("runed_brick_pressure_plate", () -> new BlockItem(ModBlocks.RUNED_BRICK_PRESSURE_PLATE.get(), DEFAULT_64.get()));
  public static DeferredHolder<Item, BlockItem> RUNED_TILE_PRESSURE_PLATE = ITEMS.register("runed_tile_pressure_plate", () -> new BlockItem(ModBlocks.RUNED_TILE_PRESSURE_PLATE.get(), DEFAULT_64.get()));
  public static DeferredHolder<Item, BlockItem> WILDWOOD_PRESSURE_PLATE = ITEMS.register("wildwood_pressure_plate", () -> new BlockItem(ModBlocks.WILDWOOD_PRESSURE_PLATE.get(), DEFAULT_64.get()));
  public static DeferredHolder<Item, BlockItem> WILDWOOD_DOOR = ITEMS.register("wildwood_door", () -> new BlockItem(ModBlocks.WILDWOOD_DOOR.get(), DEFAULT_64.get()));
  public static DeferredHolder<Item, BlockItem> WILDWOOD_TRAPDOOR = ITEMS.register("wildwood_trapdoor", () -> new BlockItem(ModBlocks.WILDWOOD_TRAPDOOR.get(), DEFAULT_64.get()));
  public static DeferredHolder<Item, BlockItem> WILDWOOD_LADDER = ITEMS.register("wildwood_ladder", () -> new BlockItem(ModBlocks.WILDWOOD_LADDER.get(), DEFAULT_64.get()));
  public static DeferredHolder<Item, BlockItem> WILDWOOD_GATE = ITEMS.register("wildwood_gate", () -> new BlockItem(ModBlocks.WILDWOOD_GATE.get(), DEFAULT_64.get()));
  public static DeferredHolder<Item, BlockItem> RUNESTONE_WALL = ITEMS.register("runestone_wall", () -> new BlockItem(ModBlocks.RUNESTONE_WALL.get(), DEFAULT_64.get()));
  public static DeferredHolder<Item, BlockItem> MOSSY_RUNESTONE_WALL = ITEMS.register("mossy_runestone_wall", () -> new BlockItem(ModBlocks.MOSSY_RUNESTONE_WALL.get(), DEFAULT_64.get()));
  public static DeferredHolder<Item, BlockItem> RUNESTONE_BRICK_WALL = ITEMS.register("runestone_brick_wall", () -> new BlockItem(ModBlocks.RUNESTONE_BRICK_WALL.get(), DEFAULT_64.get()));
  public static DeferredHolder<Item, BlockItem> RUNESTONE_TILE_WALL = ITEMS.register("runestone_tile_wall", () -> new BlockItem(ModBlocks.RUNESTONE_TILE_WALL.get(), DEFAULT_64.get()));
  public static DeferredHolder<Item, BlockItem> RUNED_WALL = ITEMS.register("runed_wall", () -> new BlockItem(ModBlocks.RUNED_WALL.get(), DEFAULT_64.get()));
  public static DeferredHolder<Item, BlockItem> RUNED_BRICK_WALL = ITEMS.register("runed_brick_wall", () -> new BlockItem(ModBlocks.RUNED_BRICK_WALL.get(), DEFAULT_64.get()));
  public static DeferredHolder<Item, BlockItem> RUNED_TILE_WALL = ITEMS.register("runed_tile_wall", () -> new BlockItem(ModBlocks.RUNED_TILE_WALL.get(), DEFAULT_64.get()));
  public static DeferredHolder<Item, BlockItem> ELEMENTAL_SOIL = ITEMS.register("elemental_soil", () -> new BlockItem(ModBlocks.ELEMENTAL_SOIL.get(), DEFAULT_64.get()));
  public static DeferredHolder<Item, BlockItem> AQUEOUS_SOIL = ITEMS.register("aqueous_soil", () -> new BlockItem(ModBlocks.AQUEOUS_SOIL.get(), DEFAULT_64.get()));
  public static DeferredHolder<Item, BlockItem> CAELIC_SOIL = ITEMS.register("caelic_soil", () -> new BlockItem(ModBlocks.CAELIC_SOIL.get(), DEFAULT_64.get()));
  public static DeferredHolder<Item, BlockItem> MAGMATIC_SOIL = ITEMS.register("magmatic_soil", () -> new BlockItem(ModBlocks.MAGMATIC_SOIL.get(), DEFAULT_64.get()));
  public static DeferredHolder<Item, BlockItem> TERRAN_SOIL = ITEMS.register("terran_soil", () -> new BlockItem(ModBlocks.TERRAN_SOIL.get(), DEFAULT_64.get()));
  public static DeferredHolder<Item, BlockItem> RITUAL_PEDESTAL = ITEMS.register("ritual_pedestal", () -> new BlockItem(ModBlocks.RITUAL_PEDESTAL.get(), DEFAULT_64.get()));
  public static DeferredHolder<Item, BlockItem> REINFORCED_RITUAL_PEDESTAL = ITEMS.register("reinforced_ritual_pedestal", () -> new BlockItem(ModBlocks.REINFORCED_RITUAL_PEDESTAL.get(), DEFAULT_64.get()));
  public static DeferredHolder<Item, BlockItem> GROVE_CRAFTER = ITEMS.register("grove_crafter", () -> new BlockItem(ModBlocks.GROVE_CRAFTER.get(), DEFAULT_64.get()));
  public static DeferredHolder<Item, BlockItem> GROVE_PEDESTAL = ITEMS.register("grove_pedestal", () -> new BlockItem(ModBlocks.GROVE_PEDESTAL.get(), DEFAULT_64.get()));
  public static DeferredHolder<Item, BlockItem> WILDWOOD_PEDESTAL = ITEMS.register("wildwood_pedestal", () -> new BlockItem(ModBlocks.WILDWOOD_PEDESTAL.get(), DEFAULT_64.get()));
  public static DeferredHolder<Item, BlockItem> DISPLAY_PEDESTAL = ITEMS.register("display_pedestal", () -> new BlockItem(ModBlocks.DISPLAY_PEDESTAL.get(), DEFAULT_64.get()));
  public static DeferredHolder<Item, BlockItem> BAFFLECAP_BLOCK = ITEMS.register("bafflecap_block", () -> new BlockItem(ModBlocks.BAFFLECAP_BLOCK.get(), DEFAULT_64.get()));
  public static DeferredHolder<Item, BlockItem> PRIMAL_GROVE_STONE = ITEMS.register("primal_grove_stone", () -> new BlockItem(ModBlocks.PRIMAL_GROVE_STONE.get(), DEFAULT_64.get()));
  public static DeferredHolder<Item, BlockItem> INCENSE_BURNER = ITEMS.register("incense_burner", () -> new BlockItem(ModBlocks.INCENSE_BURNER.get(), DEFAULT_64.get()));
  public static DeferredHolder<Item, BlockItem> MORTAR = ITEMS.register("mortar", () -> new BlockItem(ModBlocks.MORTAR.get(), DEFAULT_64.get()));
  public static DeferredHolder<Item, BlockItem> PYRE = ITEMS.register("pyre", () -> new BlockItem(ModBlocks.PYRE.get(), DEFAULT_64.get()));
  public static DeferredHolder<Item, BlockItem> REINFORCED_PYRE = ITEMS.register("reinforced_pyre", () -> new BlockItem(ModBlocks.REINFORCED_PYRE.get(), DEFAULT_64.get()));
  public static DeferredHolder<Item, BlockItem> DECORATIVE_PYRE = ITEMS.register("decorative_pyre", () -> new BlockItem(ModBlocks.DECORATIVE_PYRE.get(), DEFAULT_64.get()));
  public static DeferredHolder<Item, BlockItem> UNENDING_BOWL = ITEMS.register("unending_bowl", () -> new BlockItem(ModBlocks.UNENDING_BOWL.get(), DEFAULT_64.get()));

  // Actual items
  public static final DeferredHolder<Item, ItemNameBlockItem> WILDROOT = ITEMS.register("wildroot", () -> new ItemNameBlockItem(ModBlocks.WILDROOT_CROP.get(), DEFAULT_64.get()));
  public static final DeferredHolder<Item, Item> GROVE_MOSS = ITEMS.register("grove_moss", () -> new Item(DEFAULT_64.get()));
  public static final DeferredHolder<Item, ItemNameBlockItem> CLOUD_BERRY = ITEMS.register("cloud_berry", () -> new ItemNameBlockItem(ModBlocks.CLOUD_BERRY_CROP.get(), DEFAULT_64.get()));
  public static final DeferredHolder<Item, ItemNameBlockItem> DEWGONIA = ITEMS.register("dewgonia", () -> new ItemNameBlockItem(ModBlocks.DEWGONIA_CROP.get(), DEFAULT_64.get()));
  public static final DeferredHolder<Item, ItemNameBlockItem> INFERNO_BULB = ITEMS.register("inferno_bulb", () -> new ItemNameBlockItem(ModBlocks.INFERNO_BULB_CROP.get(), DEFAULT_64.get()));
  public static final DeferredHolder<Item, ItemNameBlockItem> STALICRIPE = ITEMS.register("stalicripe", () -> new ItemNameBlockItem(ModBlocks.STALICRIPE_CROP.get(), DEFAULT_64.get()));
  public static final DeferredHolder<Item, ItemNameBlockItem> BAFFLECAP = ITEMS.register("bafflecap", () -> new ItemNameBlockItem(ModBlocks.BAFFLECAP.get(), DEFAULT_64.get()));
  public static final DeferredHolder<Item, Item> MOONGLOW = ITEMS.register("moonglow", () -> new Item(DEFAULT_64.get()));
  public static final DeferredHolder<Item, Item> PERESKIA = ITEMS.register("pereskia", () -> new Item(DEFAULT_64.get()));
  public static final DeferredHolder<Item, Item> SPIRITLEAF = ITEMS.register("spiritleaf", () -> new Item(DEFAULT_64.get()));
  public static final DeferredHolder<Item, Item> WILDEWHEET = ITEMS.register("wildewheet", () -> new Item(DEFAULT_64.get()));
  public static final DeferredHolder<Item, ItemNameBlockItem> MOONGLOW_SEEDS = ITEMS.register("moonglow_seeds", () -> new ItemNameBlockItem(ModBlocks.MOONGLOW_CROP.get(), DEFAULT_64.get()));
  public static final DeferredHolder<Item, ItemNameBlockItem> PERESKIA_BULB = ITEMS.register("pereskia_bulb", () -> new ItemNameBlockItem(ModBlocks.PERESKIA_CROP.get(), DEFAULT_64.get()));
  public static final DeferredHolder<Item, ItemNameBlockItem> SPIRITLEAF_SEEDS = ITEMS.register("spiritleaf_seeds", () -> new ItemNameBlockItem(ModBlocks.SPIRITLEAF_CROP.get(), DEFAULT_64.get()));
  public static final DeferredHolder<Item, ItemNameBlockItem> WILDEWHEET_SEEDS = ITEMS.register("wildewheet_seeds", () -> new ItemNameBlockItem(ModBlocks.WILDEWHEET_CROP.get(), DEFAULT_64.get()));
  public static final DeferredHolder<Item, GroveSporesItem> GROVE_SPORES = ITEMS.register("grove_spores", () -> new GroveSporesItem(DEFAULT_64.get()));
  public static final DeferredHolder<Item, ItemNameBlockItem> AUBERGINE_SEEDS = ITEMS.register("aubergine_seeds", () -> new ItemNameBlockItem(ModBlocks.AUBERGINE_CROP.get(), DEFAULT_64.get()));
  public static DeferredHolder<Item, Item> CARAPACE = ITEMS.register("carapace", () -> new Item(DEFAULT_64.get()));
  public static DeferredHolder<Item, Item> PELT = ITEMS.register("pelt", () -> new Item(DEFAULT_64.get()));
  public static DeferredHolder<Item, Item> ANTLERS = ITEMS.register("antlers", () -> new Item(DEFAULT_64.get()));
  public static DeferredHolder<Item, Item> VENISON = ITEMS.register("venison", () -> new Item(new Item.Properties().food(ModFoods.VENISON)));
  public static DeferredHolder<Item, Item> COOKED_VENISON = ITEMS.register("cooked_venison", () -> new Item(new Item.Properties().food(ModFoods.COOKED_VENISON)
      .stacksTo(64)));
  public static DeferredHolder<Item, Item> RAW_SQUID = ITEMS.register("raw_squid", () -> new Item(new Item.Properties().food(ModFoods.RAW_SQUID)));
  public static DeferredHolder<Item, Item> COOKED_SQUID = ITEMS.register("cooked_squid", () -> new Item(new Item.Properties().food(ModFoods.COOKED_SQUID)
      .stacksTo(64)));
  public static DeferredHolder<Item, Item> ASSORTED_SEEDS = ITEMS.register("assorted_seeds", () -> new Item(DEFAULT_64.get()));
  public static DeferredHolder<Item, BaseItems.FastFoodItem> COOKED_SEEDS = ITEMS.register("cooked_seeds", () -> new BaseItems.FastFoodItem(new Item.Properties().food(ModFoods.COOKED_SEEDS)
      .stacksTo(64)));
  public static DeferredHolder<Item, Item> COOKED_BEETROOT = ITEMS.register("cooked_beetroot", () -> new Item(new Item.Properties().food(ModFoods.COOKED_BEETROOT)
      .stacksTo(64)));
  public static DeferredHolder<Item, Item> COOKED_CARROT = ITEMS.register("cooked_carrot", () -> new Item(new Item.Properties().food(ModFoods.COOKED_CARROT)
      .stacksTo(64)));
  public static DeferredHolder<Item, Item> AUBERGINE = ITEMS.register("aubergine", () -> new Item(new Item.Properties().food(ModFoods.AUBERGINE)
      .stacksTo(64)));
  public static DeferredHolder<Item, Item> COOKED_AUBERGINE = ITEMS.register("cooked_aubergine", () -> new Item(new Item.Properties().food(ModFoods.COOKED_AUBERGINE)
      .stacksTo(64)));
  public static DeferredHolder<Item, Item> STUFFED_AUBERGINE = ITEMS.register("stuffed_aubergine", () -> new Item(new Item.Properties().food(ModFoods.STUFFED_AUBERGINE)));
  public static DeferredHolder<Item, Item> AUBERGINE_SALAD = ITEMS.register("aubergine_salad", () -> new Item(new Item.Properties().food(ModFoods.AUBERGINE_SALAD)
      .craftRemainder(Items.BOWL).stacksTo(64)));
  public static DeferredHolder<Item, Item> BEETROOT_SALAD = ITEMS.register("beetroot_salad", () -> new Item(new Item.Properties().food(ModFoods.BEETROOT_SALAD)
      .craftRemainder(Items.BOWL).stacksTo(64)));
  public static DeferredHolder<Item, Item> STEWED_EGGPLANT = ITEMS.register("stewed_eggplant", () -> new Item(new Item.Properties().food(ModFoods.STEWED_EGGPLANT)
      .craftRemainder(Items.BOWL).stacksTo(64)));
  public static DeferredHolder<Item, TooltipDrinkItem> APPLE_CORDIAL = ITEMS.register("apple_cordial", () -> new TooltipDrinkItem("roots.drinks.slow_regen", new Item.Properties().food(ModFoods.APPLE_CORDIAL)
      .craftRemainder(Items.GLASS_BOTTLE).stacksTo(64)));
  public static DeferredHolder<Item, TooltipDrinkItem> CACTUS_SYRUP = ITEMS.register("cactus_syrup", () -> new TooltipDrinkItem("roots.drinks.slow_regen", new Item.Properties().food(ModFoods.CACTUS_SYRUP)
      .craftRemainder(Items.GLASS_BOTTLE).stacksTo(64)));
  public static DeferredHolder<Item, TooltipDrinkItem> DANDELION_CORDIAL = ITEMS.register("dandelion_cordial", () -> new TooltipDrinkItem("roots.drinks.wakefulness", new Item.Properties().food(ModFoods.DANDELION_CORDIAL)
      .craftRemainder(Items.GLASS_BOTTLE).stacksTo(64)));
  public static DeferredHolder<Item, TooltipDrinkItem> LILAC_CORDIAL = ITEMS.register("lilac_cordial", () -> new TooltipDrinkItem("roots.drinks.slow_regen", new Item.Properties().food(ModFoods.LILAC_CORDIAL)
      .craftRemainder(Items.GLASS_BOTTLE).stacksTo(64)));
  public static DeferredHolder<Item, TooltipDrinkItem> PEONY_CORDIAL = ITEMS.register("peony_cordial", () -> new TooltipDrinkItem("roots.drinks.slow_regen", new Item.Properties().food(ModFoods.PEONY_CORDIAL)
      .craftRemainder(Items.GLASS_BOTTLE).stacksTo(64)));
  public static DeferredHolder<Item, TooltipDrinkItem> ROSE_CORDIAL = ITEMS.register("rose_cordial", () -> new TooltipDrinkItem("roots.drinks.slow_regen", new Item.Properties().food(ModFoods.ROSE_CORDIAL)
      .craftRemainder(Items.GLASS_BOTTLE).stacksTo(64)));
  public static DeferredHolder<Item, TooltipDrinkItem> VINEGAR = ITEMS.register("vinegar", () -> new TooltipDrinkItem("roots.drinks.sour", new Item.Properties().food(ModFoods.VINEGAR)
      .craftRemainder(Items.GLASS_BOTTLE).stacksTo(64)));
  public static DeferredHolder<Item, TooltipDrinkItem> VEGETABLE_JUICE = ITEMS.register("vegetable_juice", () -> new TooltipDrinkItem("roots.drinks.slow_regen", new Item.Properties().food(ModFoods.VEGETABLE_JUICE)
      .craftRemainder(Items.GLASS_BOTTLE).stacksTo(64)));
  public static DeferredHolder<Item, Item> INK_BOTTLE = ITEMS.register("ink_bottle", () -> new Item(DEFAULT_64.get()));
  public static final DeferredHolder<Item, Item> ACACIA_BARK = ITEMS.register("acacia_bark", () -> new Item(DEFAULT_64.get()));
  public static final DeferredHolder<Item, Item> BIRCH_BARK = ITEMS.register("birch_bark", () -> new Item(DEFAULT_64.get()));
  public static final DeferredHolder<Item, Item> DARK_OAK_BARK = ITEMS.register("dark_oak_bark", () -> new Item(DEFAULT_64.get()));
  public static final DeferredHolder<Item, Item> JUNGLE_BARK = ITEMS.register("jungle_bark", () -> new Item(DEFAULT_64.get()));
  public static final DeferredHolder<Item, Item> OAK_BARK = ITEMS.register("oak_bark", () -> new Item(DEFAULT_64.get()));
  public static final DeferredHolder<Item, Item> SPRUCE_BARK = ITEMS.register("spruce_bark", () -> new Item(DEFAULT_64.get()));
  public static final DeferredHolder<Item, Item> WILDWOOD_BARK = ITEMS.register("wildwood_bark", () -> new Item(DEFAULT_64.get()));
  public static final DeferredHolder<Item, Item> CRIMSON_BARK = ITEMS.register("crimson_bark", () -> new Item(DEFAULT_64.get()));
  public static final DeferredHolder<Item, Item> WARPED_BARK = ITEMS.register("warped_bark", () -> new Item(DEFAULT_64.get()));
  public static final DeferredHolder<Item, Item> MANGROVE_BARK = ITEMS.register("mangrove_bark", () -> new Item(DEFAULT_64.get()));
  public static final DeferredHolder<Item, Item> MIXED_BARK = ITEMS.register("mixed_bark", () -> new Item(DEFAULT_64.get()));

  // TODO (POUCHES):
  public static final DeferredHolder<Item, Item> APOTHECARY_POUCH = ITEMS.register("apothecary_pouch", () -> new Item(DEFAULT_SINGLE.get()));
  public static final DeferredHolder<Item, Item> COMPONENT_POUCH = ITEMS.register("component_pouch", () -> new Item(DEFAULT_SINGLE.get()));
  public static final DeferredHolder<Item, Item> CREATIVE_POUCH = ITEMS.register("creative_pouch", () -> new Item(DEFAULT_SINGLE.get()));
  public static final DeferredHolder<Item, Item> FEY_POUCH = ITEMS.register("fey_pouch", () -> new Item(DEFAULT_SINGLE.get()));
  public static final DeferredHolder<Item, Item> HERB_POUCH = ITEMS.register("herb_pouch", () -> new Item(DEFAULT_SINGLE.get()));

  public static final DeferredHolder<Item, Item> COOKED_PERESKIA = ITEMS.register("cooked_pereskia", () -> new Item(new Item.Properties().food(ModFoods.COOKED_AUBERGINE)
      .stacksTo(64)));
  public static final DeferredHolder<Item, Item> FLOUR = ITEMS.register("flour", () -> new Item(DEFAULT_64.get()));
  public static final DeferredHolder<Item, Item> WILDEWHEET_BREAD = ITEMS.register("wildewheet_bread", () -> new Item(DEFAULT_64.get()));
  public static final DeferredHolder<Item, Item> WILDROOT_STEW = ITEMS.register("wildroot_stew", () -> new Item(new Item.Properties().food(ModFoods.WILDROOT_STEW)
      .stacksTo(64).craftRemainder(Items.BOWL)));
  public static final DeferredHolder<Item, FireStarterItem> FIRE_STARTER = ITEMS.register("fire_starter", () -> new FireStarterItem(DEFAULT_64.get()));
  // TODO: What are we doing with this
  public static final DeferredHolder<Item, Item> GRAMARY = ITEMS.register("gramary", () -> new Item(DEFAULT_SINGLE.get()));
  public static final DeferredHolder<Item, Item> LIVING_ARROW = ITEMS.register("living_arrow", () -> new Item(DEFAULT_64.get()));
  public static final DeferredHolder<Item, LivingAxeItem> LIVING_AXE = ITEMS.register("living_axe", () -> new LivingAxeItem(RootsAPI.LIVING_TOOL_TIER, new Item.Properties()));
  public static final DeferredHolder<Item, LivingHoeItem> LIVING_HOE = ITEMS.register("living_hoe", () -> new LivingHoeItem(RootsAPI.LIVING_TOOL_TIER, new Item.Properties()));
  public static final DeferredHolder<Item, LivingPickaxeItem> LIVING_PICKAXE = ITEMS.register("living_pickaxe", () -> new LivingPickaxeItem(RootsAPI.LIVING_TOOL_TIER, DEFAULT_64.get()));
  public static final DeferredHolder<Item, LivingShovelItem> LIVING_SHOVEL = ITEMS.register("living_shovel", () -> new LivingShovelItem(RootsAPI.LIVING_TOOL_TIER, new Item.Properties()));
  public static final DeferredHolder<Item, LivingSwordItem> LIVING_SWORD = ITEMS.register("living_sword", () -> new LivingSwordItem(RootsAPI.LIVING_TOOL_TIER, new Item.Properties()));
  public static final DeferredHolder<Item, Item> PESTLE = ITEMS.register("pestle", () -> new Item(DEFAULT_SINGLE.get()));
  public static final DeferredHolder<Item, AxeItem> RUNED_AXE = ITEMS.register("runed_axe", () -> new AxeItem(RootsAPI.RUNED_TIER, new Item.Properties()));
  public static final DeferredHolder<Item, SwordItem> RUNED_DAGGER = ITEMS.register("runed_dagger", () -> new SwordItem(RootsAPI.RUNED_TIER, new Item.Properties()));
  public static final DeferredHolder<Item, PickaxeItem> RUNED_PICKAXE = ITEMS.register("runed_pickaxe", () -> new PickaxeItem(RootsAPI.RUNED_TIER, new Item.Properties()));
  public static final DeferredHolder<Item, HoeItem> RUNED_HOE = ITEMS.register("runed_hoe", () -> new HoeItem(RootsAPI.RUNED_TIER, new Item.Properties()));
  public static final DeferredHolder<Item, ShovelItem> RUNED_SHOVEL = ITEMS.register("runed_shovel", () -> new ShovelItem(RootsAPI.RUNED_TIER, new Item.Properties()));
  public static final DeferredHolder<Item, SwordItem> RUNED_SWORD = ITEMS.register("runed_sword", () -> new SwordItem(RootsAPI.RUNED_TIER, new Item.Properties()));
  public static final DeferredHolder<Item, RunicShearsItem> RUNIC_SHEARS = ITEMS.register("runic_shears", () -> new RunicShearsItem(new Item.Properties().durability(313)));
  public static final DeferredHolder<Item, CastingItem> STAFF = ITEMS.register("staff", () -> new CastingItem(new Item.Properties().component(ModAttachments.SPELL_STORAGE, SpellStorage.EMPTY.get())
      .stacksTo(1)));
  // TODO: Durability?
  public static final DeferredHolder<Item, Item> WILDWOOD_BOW = ITEMS.register("wildwood_bow", () -> new Item(new Item.Properties().durability(384)));
  public static final DeferredHolder<Item, Item> WILDWOOD_QUIVER = ITEMS.register("wildwood_quiver", () -> new Item(DEFAULT_SINGLE.get()));
  public static final DeferredHolder<Item, ShearsItem> WOODEN_SHEARS = ITEMS.register("wooden_shears", () -> new ShearsItem(new Item.Properties().durability(120)));
  public static DeferredHolder<Item, KnifeItem> WOODEN_KNIFE = ITEMS.register("wooden_knife", () -> new KnifeItem(Tiers.WOOD, new Item.Properties()));
  public static DeferredHolder<Item, KnifeItem> STONE_KNIFE = ITEMS.register("stone_knife", () -> new KnifeItem(Tiers.STONE, new Item.Properties()));
  public static DeferredHolder<Item, KnifeItem> COPPER_KNIFE = ITEMS.register("copper_knife", () -> new KnifeItem(RootsAPI.COPPER_TIER, new Item.Properties()));
  public static DeferredHolder<Item, KnifeItem> IRON_KNIFE = ITEMS.register("iron_knife", () -> new KnifeItem(Tiers.IRON, new Item.Properties()));
  public static DeferredHolder<Item, KnifeItem> GOLD_KNIFE = ITEMS.register("gold_knife", () -> new KnifeItem(Tiers.GOLD, new Item.Properties()));
  public static DeferredHolder<Item, KnifeItem> SILVER_KNIFE = ITEMS.register("silver_knife", () -> new KnifeItem(Tiers.GOLD, new Item.Properties()));
  public static DeferredHolder<Item, KnifeItem> DIAMOND_KNIFE = ITEMS.register("diamond_knife", () -> new KnifeItem(Tiers.DIAMOND, new Item.Properties()));
  public static DeferredHolder<Item, KnifeItem> NETHERITE_KNIFE = ITEMS.register("netherite_knife", () -> new KnifeItem(Tiers.NETHERITE, new Item.Properties()));

  public static final DeferredHolder<Item, Item> RELIQUARY = ITEMS.register("reliquary", () -> new Item(DEFAULT_64.get()));
  public static final DeferredHolder<Item, Item> SPIRIT_BAG = ITEMS.register("spirit_bag", () -> new Item(DEFAULT_64.get()));
  public static final DeferredHolder<Item, Item> FEY_LEATHER = ITEMS.register("fey_leather", () -> new Item(DEFAULT_64.get()));
  public static final DeferredHolder<Item, Item> GLASS_EYE = ITEMS.register("glass_eye", () -> new Item(DEFAULT_64.get()));
  public static final DeferredHolder<Item, Item> LIFE_ESSENCE = ITEMS.register("life_essence", () -> new Item(DEFAULT_64.get()));
  public static final DeferredHolder<Item, Item> MYSTIC_FEATHER = ITEMS.register("mystic_feather", () -> new Item(DEFAULT_64.get()));
  public static final DeferredHolder<Item, Item> PETALS = ITEMS.register("petals", () -> new Item(DEFAULT_64.get()));
  public static final DeferredHolder<Item, Item> RUNIC_DUST = ITEMS.register("runic_dust", () -> new Item(DEFAULT_64.get()));
  public static final DeferredHolder<Item, Item> STRANGE_OOZE = ITEMS.register("strange_ooze", () -> new Item(DEFAULT_64.get()));
  public static final DeferredHolder<Item, ArmorItem> ANTLER_HAT = ITEMS.register("antler_hat", () -> new ArmorItem(ANTLER_MATERIAL, ArmorItem.Type.HELMET, new Item.Properties().durability(399)));
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
  public static DeferredHolder<Item, ArmorItem> BEETLE_HELMET = ITEMS.register("beetle_helmet", () -> new ArmorItem(CARAPACE_MATERIAL, ArmorItem.Type.HELMET, new Item.Properties().durability(ArmorItem.Type.HELMET.getDurability(25))));
  public static DeferredHolder<Item, ArmorItem> BEETLE_CHESTPLATE = ITEMS.register("beetle_chestplate", () -> new ArmorItem(CARAPACE_MATERIAL, ArmorItem.Type.CHESTPLATE, new Item.Properties().durability(ArmorItem.Type.CHESTPLATE.getDurability(25))));
  public static DeferredHolder<Item, ArmorItem> BEETLE_LEGGINGS = ITEMS.register("beetle_leggings", () -> new ArmorItem(CARAPACE_MATERIAL, ArmorItem.Type.LEGGINGS, new Item.Properties().durability(ArmorItem.Type.LEGGINGS.getDurability(25))));
  public static DeferredHolder<Item, ArmorItem> BEETLE_BOOTS = ITEMS.register("beetle_boots", () -> new ArmorItem(CARAPACE_MATERIAL, ArmorItem.Type.BOOTS, new Item.Properties().durability(ArmorItem.Type.BOOTS.getDurability(25))));
  public static DeferredHolder<Item, Item> RAW_SILVER = ITEMS.register("raw_silver", () -> new Item(DEFAULT_64.get()));
  public static DeferredHolder<Item, Item> SILVER_INGOT = ITEMS.register("silver_ingot", () -> new Item(DEFAULT_64.get()));
  public static DeferredHolder<Item, Item> SILVER_NUGGET = ITEMS.register("silver_nugget", () -> new Item(DEFAULT_64.get()));
  public static DeferredHolder<Item, Item> COPPER_NUGGET = ITEMS.register("copper_nugget", () -> new Item(DEFAULT_64.get()));
  // TODO: Check damage values
  public static DeferredHolder<Item, AxeItem> COPPER_AXE = ITEMS.register("copper_axe", () -> new AxeItem(RootsAPI.COPPER_TIER, new Item.Properties()));
  public static DeferredHolder<Item, HoeItem> COPPER_HOE = ITEMS.register("copper_hoe", () -> new HoeItem(RootsAPI.COPPER_TIER, new Item.Properties()));
  public static DeferredHolder<Item, PickaxeItem> COPPER_PICKAXE = ITEMS.register("copper_pickaxe", () -> new PickaxeItem(RootsAPI.COPPER_TIER, new Item.Properties()));
  public static DeferredHolder<Item, ShovelItem> COPPER_SHOVEL = ITEMS.register("copper_shovel", () -> new ShovelItem(RootsAPI.COPPER_TIER, new Item.Properties()));
  public static DeferredHolder<Item, SwordItem> COPPER_SWORD = ITEMS.register("copper_sword", () -> new SwordItem(RootsAPI.COPPER_TIER, new Item.Properties()));
  // TODO: Gold -> COPPER
  public static DeferredHolder<Item, ArmorItem> COPPER_HELMET = ITEMS.register("copper_helmet", () -> new ArmorItem(COPPER_MATERIAL, ArmorItem.Type.HELMET, new Item.Properties().durability(ArmorItem.Type.HELMET.getDurability(15))));
  public static DeferredHolder<Item, ArmorItem> COPPER_CHESTPLATE = ITEMS.register("copper_chestplate", () -> new ArmorItem(COPPER_MATERIAL, ArmorItem.Type.CHESTPLATE, new Item.Properties().durability(ArmorItem.Type.CHESTPLATE.getDurability(15))));
  public static DeferredHolder<Item, ArmorItem> COPPER_LEGGINGS = ITEMS.register("copper_leggings", () -> new ArmorItem(COPPER_MATERIAL, ArmorItem.Type.LEGGINGS, new Item.Properties().durability(ArmorItem.Type.LEGGINGS.getDurability(15))));
  public static DeferredHolder<Item, ArmorItem> COPPER_BOOTS = ITEMS.register("copper_boots", () -> new ArmorItem(COPPER_MATERIAL, ArmorItem.Type.BOOTS, new Item.Properties().durability(ArmorItem.Type.BOOTS.getDurability(15))));
  public static DeferredHolder<Item, DeferredSpawnEggItem> BEETLE_SPAWN_EGG = ITEMS.register("beetle_spawn_egg", () -> new DeferredSpawnEggItem(ModEntities.BEETLE, 0x418594, 0x211D15, DEFAULT_64.get()));
  public static DeferredHolder<Item, DeferredSpawnEggItem> DEER_SPAWN_EGG = ITEMS.register("deer_spawn_egg", () -> new DeferredSpawnEggItem(ModEntities.DEER, 0xa18458, 0x5e4d33, DEFAULT_64.get()));
  public static DeferredHolder<Item, DeferredSpawnEggItem> FENNEC_SPAWN_EGG = ITEMS.register("fennec_spawn_egg", () -> new DeferredSpawnEggItem(ModEntities.FENNEC, 0xe9dcc2, 0xb1855c, DEFAULT_64.get()));
  public static DeferredHolder<Item, DeferredSpawnEggItem> GREEN_SPROUT_SPAWN_EGG = ITEMS.register("green_sprout_spawn_egg", () -> new DeferredSpawnEggItem(ModEntities.GREEN_SPROUT, 0x9adb58, 0x2c9425, DEFAULT_64.get()));
  public static DeferredHolder<Item, DeferredSpawnEggItem> TAN_SPROUT_SPAWN_EGG = ITEMS.register("tan_sprout_spawn_egg", () -> new DeferredSpawnEggItem(ModEntities.TAN_SPROUT, 0xeeca5f, 0xbb6c20, DEFAULT_64.get()));
  public static DeferredHolder<Item, DeferredSpawnEggItem> RED_SPROUT_SPAWN_EGG = ITEMS.register("red_sprout_spawn_egg", () -> new DeferredSpawnEggItem(ModEntities.RED_SPROUT, 0xe6754c, 0xbd2637, DEFAULT_64.get()));
  public static DeferredHolder<Item, DeferredSpawnEggItem> PURPLE_SPROUT_SPAWN_EGG = ITEMS.register("purple_sprout_spawn_egg", () -> new DeferredSpawnEggItem(ModEntities.PURPLE_SPROUT, 0xdd45e6, 0x6825ba, DEFAULT_64.get()));
  public static DeferredHolder<Item, DeferredSpawnEggItem> OWL_SPAWN_EGG = ITEMS.register("owl_spawn_egg", () -> new DeferredSpawnEggItem(ModEntities.OWL, 0x8c654a, 0xdec9ba, DEFAULT_64.get()));
  public static DeferredHolder<Item, DeferredSpawnEggItem> DUCK_SPAWN_EGG = ITEMS.register("duck_spawn_egg", () -> new DeferredSpawnEggItem(ModEntities.DUCK, 0xe4d6a5, 0xe9ad36, DEFAULT_64.get()));

  public static DeferredHolder<Item, TokenItem.SpellTokenItem> SPELL_ACID_CLOUD = ITEMS.register("spell_acid_cloud", () -> spell(ModSpells.ACID_CLOUD));
  public static DeferredHolder<Item, TokenItem.SpellTokenItem> SPELL_AQUA_BUBBLE = ITEMS.register("spell_aqua_bubble", () -> spell(ModSpells.AQUA_BUBBLE));
  public static DeferredHolder<Item, TokenItem.SpellTokenItem> SPELL_AUGMENT = ITEMS.register("spell_augment", () -> spell(ModSpells.AUGMENT));
  public static DeferredHolder<Item, TokenItem.SpellTokenItem> SPELL_CONTROL_UNDEAD = ITEMS.register("spell_control_undead", () -> spell(ModSpells.CONTROL_UNDEAD));
  public static DeferredHolder<Item, TokenItem.SpellTokenItem> SPELL_DANDELION_WINDS = ITEMS.register("spell_dandelion_winds", () -> spell(ModSpells.DANDELION_WINDS));
  public static DeferredHolder<Item, TokenItem.SpellTokenItem> SPELL_DESATURATE = ITEMS.register("spell_desaturate", () -> spell(ModSpells.DESATURATE));
  public static DeferredHolder<Item, TokenItem.SpellTokenItem> SPELL_DISARM = ITEMS.register("spell_disarm", () -> spell(ModSpells.DISARM));
  public static DeferredHolder<Item, TokenItem.SpellTokenItem> SPELL_EXTENSION = ITEMS.register("spell_extension", () -> spell(ModSpells.EXTENSION));
  public static DeferredHolder<Item, TokenItem.SpellTokenItem> SPELL_FEY_LIGHT = ITEMS.register("spell_fey_light", () -> spell(ModSpells.FEY_LIGHT));
  public static DeferredHolder<Item, TokenItem.SpellTokenItem> SPELL_GEAS = ITEMS.register("spell_geas", () -> spell(ModSpells.GEAS));
  public static DeferredHolder<Item, TokenItem.SpellTokenItem> SPELL_GROWTH_INFUSION = ITEMS.register("spell_growth_infusion", () -> spell(ModSpells.GROWTH_INFUSION));
  public static DeferredHolder<Item, TokenItem.SpellTokenItem> SPELL_HARVEST = ITEMS.register("spell_harvest", () -> spell(ModSpells.HARVEST));
  public static DeferredHolder<Item, TokenItem.SpellTokenItem> SPELL_JAUNT = ITEMS.register("spell_jaunt", () -> spell(ModSpells.JAUNT));
  public static DeferredHolder<Item, TokenItem.SpellTokenItem> SPELL_LIFE_DRAIN = ITEMS.register("spell_life_drain", () -> spell(ModSpells.LIFE_DRAIN));
  // Light drifter
  public static DeferredHolder<Item, TokenItem.SpellTokenItem> SPELL_LIGHT_DRIFTER = ITEMS.register("spell_light_drifter", () -> spell(ModSpells.LIGHT_DRIFTER));
  public static DeferredHolder<Item, TokenItem.SpellTokenItem> SPELL_MAGNETISM = ITEMS.register("spell_magnetism", () -> spell(ModSpells.MAGNETISM));
  public static DeferredHolder<Item, TokenItem.SpellTokenItem> SPELL_NONDETECTION = ITEMS.register("spell_nondetection", () -> spell(ModSpells.NONDETECTION));
  public static DeferredHolder<Item, TokenItem.SpellTokenItem> SPELL_PETAL_SHELL = ITEMS.register("spell_petal_shell", () -> spell(ModSpells.PETAL_SHELL));
  public static DeferredHolder<Item, TokenItem.SpellTokenItem> SPELL_RADIANCE = ITEMS.register("spell_radiance", () -> spell(ModSpells.RADIANCE));
  public static DeferredHolder<Item, TokenItem.SpellTokenItem> SPELL_RAMPANT_GROWTH = ITEMS.register("spell_rampant_growth", () -> spell(ModSpells.RAMPANT_GROWTH));
  public static DeferredHolder<Item, TokenItem.SpellTokenItem> SPELL_ROSE_THORNS = ITEMS.register("spell_rose_thorns", () -> spell(ModSpells.ROSE_THORNS));
  public static DeferredHolder<Item, TokenItem.SpellTokenItem> SPELL_SANCTUARY = ITEMS.register("spell_sanctuary", () -> spell(ModSpells.SANCTUARY));
  public static DeferredHolder<Item, TokenItem.SpellTokenItem> SPELL_SATURATE = ITEMS.register("spell_saturate", () -> spell(ModSpells.SATURATE));
  public static DeferredHolder<Item, TokenItem.SpellTokenItem> SPELL_SHATTER = ITEMS.register("spell_shatter", () -> spell(ModSpells.SHATTER));
  public static DeferredHolder<Item, TokenItem.SpellTokenItem> SPELL_SKY_SOARER = ITEMS.register("spell_sky_soarer", () -> spell(ModSpells.SKY_SOARER));
  public static DeferredHolder<Item, TokenItem.SpellTokenItem> SPELL_STORM_CLOUD = ITEMS.register("spell_storm_cloud", () -> spell(ModSpells.STORM_CLOUD));
  public static DeferredHolder<Item, TokenItem.SpellTokenItem> SPELL_TIME_STOP = ITEMS.register("spell_time_stop", () -> spell(ModSpells.TIME_STOP));
  public static DeferredHolder<Item, TokenItem.SpellTokenItem> SPELL_WILDFIRE = ITEMS.register("spell_wildfire", () -> spell(ModSpells.WILDFIRE));

  public static DeferredHolder<Item, TokenItem.RitualTokenItem> RITUAL_ANIMAL_HARVEST = ITEMS.register("ritual_animal_harvest", () -> ritual(ModRituals.ANIMAL_HARVEST));
  public static DeferredHolder<Item, TokenItem.RitualTokenItem> RITUAL_BLOOMING = ITEMS.register("ritual_blooming", () -> ritual(ModRituals.BLOOMING));
  public static DeferredHolder<Item, TokenItem.RitualTokenItem> RITUAL_CRAFTING = ITEMS.register("ritual_crafting", () -> ritual(ModRituals.CRAFTING));
  public static DeferredHolder<Item, TokenItem.RitualTokenItem> RITUAL_FIRE_STORM = ITEMS.register("ritual_fire_storm", () -> ritual(ModRituals.FIRE_STORM));
  public static DeferredHolder<Item, TokenItem.RitualTokenItem> RITUAL_FROST_LANDS = ITEMS.register("ritual_frost_lands", () -> ritual(ModRituals.FROST_LANDS));
  public static DeferredHolder<Item, TokenItem.RitualTokenItem> RITUAL_GATHERING = ITEMS.register("ritual_gathering", () -> ritual(ModRituals.GATHERING));
  public static DeferredHolder<Item, TokenItem.RitualTokenItem> RITUAL_GERMINATION = ITEMS.register("ritual_germination", () -> ritual(ModRituals.GERMINATION));
  public static DeferredHolder<Item, TokenItem.RitualTokenItem> RITUAL_GROVE_SUPPLICATION = ITEMS.register("ritual_grove_supplication", () -> ritual(ModRituals.GROVE_SUPPLICATION));
  public static DeferredHolder<Item, TokenItem.RitualTokenItem> RITUAL_HEALING_AURA = ITEMS.register("ritual_healing_aura", () -> ritual(ModRituals.HEALING_AURA));
  public static DeferredHolder<Item, TokenItem.RitualTokenItem> RITUAL_HEAVY_STORMS = ITEMS.register("ritual_heavy_storms", () -> ritual(ModRituals.HEAVY_STORMS));
  public static DeferredHolder<Item, TokenItem.RitualTokenItem> RITUAL_OVERGROWTH = ITEMS.register("ritual_overgrowth", () -> ritual(ModRituals.OVERGROWTH));
  public static DeferredHolder<Item, TokenItem.RitualTokenItem> RITUAL_PROTECTION = ITEMS.register("ritual_protection", () -> ritual(ModRituals.PROTECTION));
  public static DeferredHolder<Item, TokenItem.RitualTokenItem> RITUAL_PURITY = ITEMS.register("ritual_purity", () -> ritual(ModRituals.PURITY));
  public static DeferredHolder<Item, TokenItem.RitualTokenItem> RITUAL_SPREADING_FOREST = ITEMS.register("ritual_spreading_forest", () -> ritual(ModRituals.SPREADING_FOREST));
  public static DeferredHolder<Item, TokenItem.RitualTokenItem> RITUAL_SUMMON_CREATURES = ITEMS.register("ritual_summon_creatures", () -> ritual(ModRituals.SUMMON_CREATURES));
  public static DeferredHolder<Item, TokenItem.RitualTokenItem> RITUAL_TRANSMUTATION = ITEMS.register("ritual_transmutation", () -> ritual(ModRituals.TRANSMUTATION));
  public static DeferredHolder<Item, TokenItem.RitualTokenItem> RITUAL_WARDING = ITEMS.register("ritual_warding", () -> ritual(ModRituals.WARDING));
  public static DeferredHolder<Item, TokenItem.RitualTokenItem> RITUAL_WILDROOT_GROWTH = ITEMS.register("ritual_wildroot_growth", () -> ritual(ModRituals.WILDROOT_GROWTH));
  public static DeferredHolder<Item, TokenItem.RitualTokenItem> RITUAL_WINDWALL = ITEMS.register("ritual_windwall", () -> ritual(ModRituals.WINDWALL));

  private static TokenItem.SpellTokenItem spell(Holder<Spell> spell) {
    return new TokenItem.SpellTokenItem(spell.getKey(), new Item.Properties().stacksTo(1));
  }

  private static TokenItem.RitualTokenItem ritual(Holder<Ritual> ritual) {
    return new TokenItem.RitualTokenItem(ritual.getKey(), new Item.Properties().stacksTo(1));
  }

  public static void register(IEventBus bus) {
    ARMOR.register(bus);
    ITEMS.register(bus);
  }
}
