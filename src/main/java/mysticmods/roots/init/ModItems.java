package mysticmods.roots.init;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.datacomponent.SpellStorage;
import mysticmods.roots.api.grove.Grove;
import mysticmods.roots.api.ritual.Ritual;
import mysticmods.roots.api.spell.Spell;
import mysticmods.roots.inventory.pouch.apothecary.ApothecaryPouchMenu;
import mysticmods.roots.inventory.pouch.component.ComponentPouchMenu;
import mysticmods.roots.inventory.pouch.herb.HerbPouchMenu;
import mysticmods.roots.inventory.pouch.sylvan.SylvanPouchMenu;
import mysticmods.roots.item.*;
import mysticmods.roots.item.block.EnchantedTurfBlockItem;
import mysticmods.roots.item.living.*;
import mysticmods.roots.item.util.DyeableWithDefault;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.EnumMap;
import java.util.List;

public class ModItems {
  private static final DeferredRegister<Item> ITEMS = DeferredRegister.createItems(RootsAPI.MODID);
  private static final DeferredRegister<ArmorMaterial> ARMOR = DeferredRegister.create(Registries.ARMOR_MATERIAL, RootsAPI.MODID);

  public static final DeferredHolder<ArmorMaterial, ArmorMaterial> ANTLER_MATERIAL = ARMOR.register("antlers", () -> new ArmorMaterial(
      Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
        map.put(ArmorItem.Type.BOOTS, 0);
        map.put(ArmorItem.Type.LEGGINGS, 0);
        map.put(ArmorItem.Type.CHESTPLATE, 0);
        map.put(ArmorItem.Type.HELMET, 3);
        map.put(ArmorItem.Type.BODY, 0);
      }),
      // TODO: Sound
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
      // TODO: Sound
      SoundEvents.ARMOR_EQUIP_TURTLE,
      () -> Ingredient.of(RootsTags.Items.CARAPACE),
      List.of(new ArmorMaterial.Layer(RootsAPI.rl("carapace"))),
      0f,
      0f));
  public static final DeferredHolder<ArmorMaterial, ArmorMaterial> COPPER_MATERIAL = ARMOR.register("copper", () -> new ArmorMaterial(
      Util.make(new EnumMap<>(ArmorItem.Type.class),
          map -> {
            map.put(ArmorItem.Type.BOOTS, 2);
            map.put(ArmorItem.Type.LEGGINGS, 6);
            map.put(ArmorItem.Type.CHESTPLATE, 5);
            map.put(ArmorItem.Type.HELMET, 2);
            map.put(ArmorItem.Type.BODY, 0);
          }),
      7,
      // TODO: Sound
      SoundEvents.ARMOR_EQUIP_IRON,
      () -> Ingredient.of(Tags.Items.STORAGE_BLOCKS_COPPER),
      List.of(new ArmorMaterial.Layer(RootsAPI.rl("copper"))),
      0.0f,
      0.0f));

  // BLOCK ITEMS
  public static DeferredHolder<Item, BlockItem> WILD_ROOTS = ITEMS.register("wild_roots", () -> new BlockItem(ModBlocks.WILD_ROOTS.get(), new Item.Properties()));
  public static DeferredHolder<Item, BlockItem> HANGING_GROVE_MOSS = ITEMS.register("hanging_grove_moss", () -> new BlockItem(ModBlocks.HANGING_GROVE_MOSS.get(), new Item.Properties()));
  public static DeferredHolder<Item, BlockItem> CREEPING_GROVE_MOSS = ITEMS.register("creeping_grove_moss", () -> new BlockItem(ModBlocks.CREEPING_GROVE_MOSS.get(), new Item.Properties()));
  public static DeferredHolder<Item, BlockItem> THATCH = ITEMS.register("thatch", () -> new BlockItem(ModBlocks.THATCH.get(), new Item.Properties()));
  public static DeferredHolder<Item, BlockItem> RUNESTONE = ITEMS.register("runestone", () -> new BlockItem(ModBlocks.RUNESTONE.get(), new Item.Properties()));
  public static DeferredHolder<Item, BlockItem> MOSSY_RUNESTONE = ITEMS.register("mossy_runestone", () -> new BlockItem(ModBlocks.MOSSY_RUNESTONE.get(), new Item.Properties()));
  public static DeferredHolder<Item, BlockItem> CHISELED_RUNESTONE = ITEMS.register("chiseled_runestone", () -> new BlockItem(ModBlocks.CHISELED_RUNESTONE.get(), new Item.Properties()));
  public static DeferredHolder<Item, BlockItem> RUNESTONE_BRICK = ITEMS.register("runestone_brick", () -> new BlockItem(ModBlocks.RUNESTONE_BRICK.get(), new Item.Properties()));
  public static DeferredHolder<Item, BlockItem> RUNESTONE_TILE = ITEMS.register("runestone_tile", () -> new BlockItem(ModBlocks.RUNESTONE_TILE.get(), new Item.Properties()));
  public static DeferredHolder<Item, BlockItem> RUNED_OBSIDIAN = ITEMS.register("runed_obsidian", () -> new BlockItem(ModBlocks.RUNED_OBSIDIAN.get(), new Item.Properties().fireResistant()));
  public static DeferredHolder<Item, BlockItem> CHISELED_RUNED_OBSIDIAN = ITEMS.register("chiseled_runed_obsidian", () -> new BlockItem(ModBlocks.CHISELED_RUNED_OBSIDIAN.get(), new Item.Properties().fireResistant()));
  public static DeferredHolder<Item, BlockItem> RUNED_BRICK = ITEMS.register("runed_brick", () -> new BlockItem(ModBlocks.RUNED_BRICK.get(), new Item.Properties().fireResistant()));
  public static DeferredHolder<Item, BlockItem> RUNED_TILE = ITEMS.register("runed_tile", () -> new BlockItem(ModBlocks.RUNED_TILE.get(), new Item.Properties().fireResistant()));
  public static DeferredHolder<Item, BlockItem> SILVER_ORE = ITEMS.register("silver_ore", () -> new BlockItem(ModBlocks.SILVER_ORE.get(), new Item.Properties()));
  public static DeferredHolder<Item, BlockItem> DEEPSLATE_SILVER_ORE = ITEMS.register("deepslate_silver_ore", () -> new BlockItem(ModBlocks.DEEPSLATE_SILVER_ORE.get(), new Item.Properties()));
  public static DeferredHolder<Item, BlockItem> GRANITE_QUARTZ_ORE = ITEMS.register("granite_quartz_ore", () -> new BlockItem(ModBlocks.GRANITE_QUARTZ_ORE.get(), new Item.Properties()));
  public static DeferredHolder<Item, BlockItem> RAW_SILVER_BLOCK = ITEMS.register("raw_silver_block", () -> new BlockItem(ModBlocks.RAW_SILVER_BLOCK.get(), new Item.Properties()));
  public static DeferredHolder<Item, BlockItem> SILVER_BLOCK = ITEMS.register("silver_block", () -> new BlockItem(ModBlocks.SILVER_BLOCK.get(), new Item.Properties()));
  public static DeferredHolder<Item, BlockItem> WILDWOOD_LOG = ITEMS.register("wildwood_log", () -> new BlockItem(ModBlocks.WILDWOOD_LOG.get(), new Item.Properties()));
  public static DeferredHolder<Item, BlockItem> STRIPPED_WILDWOOD_LOG = ITEMS.register("stripped_wildwood_log", () -> new BlockItem(ModBlocks.STRIPPED_WILDWOOD_LOG.get(), new Item.Properties()));
  public static DeferredHolder<Item, BlockItem> WILDWOOD_WOOD = ITEMS.register("wildwood_wood", () -> new BlockItem(ModBlocks.WILDWOOD_WOOD.get(), new Item.Properties()));
  public static DeferredHolder<Item, BlockItem> STRIPPED_WILDWOOD_WOOD = ITEMS.register("stripped_wildwood_wood", () -> new BlockItem(ModBlocks.STRIPPED_WILDWOOD_WOOD.get(), new Item.Properties()));
  public static DeferredHolder<Item, BlockItem> WILDWOOD_PLANKS = ITEMS.register("wildwood_planks", () -> new BlockItem(ModBlocks.WILDWOOD_PLANKS.get(), new Item.Properties()));
  public static DeferredHolder<Item, BlockItem> WILDWOOD_SAPLING = ITEMS.register("wildwood_sapling", () -> new BlockItem(ModBlocks.WILDWOOD_SAPLING.get(), new Item.Properties().rarity(Rarity.EPIC)));
  public static DeferredHolder<Item, BlockItem> STONEPETAL = ITEMS.register("stonepetal", () -> new BlockItem(ModBlocks.STONEPETAL.get(), new Item.Properties()));
  public static DeferredHolder<Item, BlockItem> WILDWOOD_LEAVES = ITEMS.register("wildwood_leaves", () -> new BlockItem(ModBlocks.WILDWOOD_LEAVES.get(), new Item.Properties()));
  public static DeferredHolder<Item, BlockItem> RUNESTONE_STAIRS = ITEMS.register("runestone_stairs", () -> new BlockItem(ModBlocks.RUNESTONE_STAIRS.get(), new Item.Properties()));
  public static DeferredHolder<Item, BlockItem> MOSSY_RUNESTONE_STAIRS = ITEMS.register("mossy_runestone_stairs", () -> new BlockItem(ModBlocks.MOSSY_RUNESTONE_STAIRS.get(), new Item.Properties()));
  public static DeferredHolder<Item, BlockItem> RUNESTONE_BRICK_STAIRS = ITEMS.register("runestone_brick_stairs", () -> new BlockItem(ModBlocks.RUNESTONE_BRICK_STAIRS.get(), new Item.Properties()));
  public static DeferredHolder<Item, BlockItem> RUNESTONE_TILE_STAIRS = ITEMS.register("runestone_tile_stairs", () -> new BlockItem(ModBlocks.RUNESTONE_TILE_STAIRS.get(), new Item.Properties()));
  public static DeferredHolder<Item, BlockItem> RUNED_STAIRS = ITEMS.register("runed_stairs", () -> new BlockItem(ModBlocks.RUNED_STAIRS.get(), new Item.Properties().fireResistant()));
  public static DeferredHolder<Item, BlockItem> RUNED_BRICK_STAIRS = ITEMS.register("runed_brick_stairs", () -> new BlockItem(ModBlocks.RUNED_BRICK_STAIRS.get(), new Item.Properties().fireResistant()));
  public static DeferredHolder<Item, BlockItem> RUNED_TILE_STAIRS = ITEMS.register("runed_tile_stairs", () -> new BlockItem(ModBlocks.RUNED_TILE_STAIRS.get(), new Item.Properties().fireResistant()));
  public static DeferredHolder<Item, BlockItem> WILDWOOD_STAIRS = ITEMS.register("wildwood_stairs", () -> new BlockItem(ModBlocks.WILDWOOD_STAIRS.get(), new Item.Properties()));
  public static DeferredHolder<Item, BlockItem> RUNESTONE_SLAB = ITEMS.register("runestone_slab", () -> new BlockItem(ModBlocks.RUNESTONE_SLAB.get(), new Item.Properties()));
  public static DeferredHolder<Item, BlockItem> MOSSY_RUNESTONE_SLAB = ITEMS.register("mossy_runestone_slab", () -> new BlockItem(ModBlocks.MOSSY_RUNESTONE_SLAB.get(), new Item.Properties()));
  public static DeferredHolder<Item, BlockItem> RUNESTONE_BRICK_SLAB = ITEMS.register("runestone_brick_slab", () -> new BlockItem(ModBlocks.RUNESTONE_BRICK_SLAB.get(), new Item.Properties()));
  public static DeferredHolder<Item, BlockItem> RUNESTONE_TILE_SLAB = ITEMS.register("runestone_tile_slab", () -> new BlockItem(ModBlocks.RUNESTONE_TILE_SLAB.get(), new Item.Properties()));
  public static DeferredHolder<Item, BlockItem> RUNED_SLAB = ITEMS.register("runed_slab", () -> new BlockItem(ModBlocks.RUNED_SLAB.get(), new Item.Properties().fireResistant()));
  public static DeferredHolder<Item, BlockItem> RUNED_BRICK_SLAB = ITEMS.register("runed_brick_slab", () -> new BlockItem(ModBlocks.RUNED_BRICK_SLAB.get(), new Item.Properties().fireResistant()));
  public static DeferredHolder<Item, BlockItem> RUNED_TILE_SLAB = ITEMS.register("runed_tile_slab", () -> new BlockItem(ModBlocks.RUNED_TILE_SLAB.get(), new Item.Properties().fireResistant()));
  public static DeferredHolder<Item, BlockItem> WILDWOOD_SLAB = ITEMS.register("wildwood_slab", () -> new BlockItem(ModBlocks.WILDWOOD_SLAB.get(), new Item.Properties()));
  public static DeferredHolder<Item, BlockItem> WILDWOOD_FENCE = ITEMS.register("wildwood_fence", () -> new BlockItem(ModBlocks.WILDWOOD_FENCE.get(), new Item.Properties()));
  public static DeferredHolder<Item, BlockItem> RUNESTONE_BUTTON = ITEMS.register("runestone_button", () -> new BlockItem(ModBlocks.RUNESTONE_BUTTON.get(), new Item.Properties()));
  public static DeferredHolder<Item, BlockItem> RUNESTONE_BRICK_BUTTON = ITEMS.register("runestone_brick_button", () -> new BlockItem(ModBlocks.RUNESTONE_BRICK_BUTTON.get(), new Item.Properties()));
  public static DeferredHolder<Item, BlockItem> MOSSY_RUNESTONE_BUTTON = ITEMS.register("mossy_runestone_button", () -> new BlockItem(ModBlocks.MOSSY_RUNESTONE_BUTTON.get(), new Item.Properties()));
  public static DeferredHolder<Item, BlockItem> RUNESTONE_TILE_BUTTON = ITEMS.register("runestone_tile_button", () -> new BlockItem(ModBlocks.RUNESTONE_TILE_BUTTON.get(), new Item.Properties()));
  public static DeferredHolder<Item, BlockItem> RUNED_BUTTON = ITEMS.register("runed_button", () -> new BlockItem(ModBlocks.RUNED_BUTTON.get(), new Item.Properties().fireResistant()));
  public static DeferredHolder<Item, BlockItem> RUNED_BRICK_BUTTON = ITEMS.register("runed_brick_button", () -> new BlockItem(ModBlocks.RUNED_BRICK_BUTTON.get(), new Item.Properties().fireResistant()));
  public static DeferredHolder<Item, BlockItem> RUNED_TILE_BUTTON = ITEMS.register("runed_tile_button", () -> new BlockItem(ModBlocks.RUNED_TILE_BUTTON.get(), new Item.Properties().fireResistant()));
  public static DeferredHolder<Item, BlockItem> WILDWOOD_BUTTON = ITEMS.register("wildwood_button", () -> new BlockItem(ModBlocks.WILDWOOD_BUTTON.get(), new Item.Properties()));
  public static DeferredHolder<Item, BlockItem> RUNESTONE_PRESSURE_PLATE = ITEMS.register("runestone_pressure_plate", () -> new BlockItem(ModBlocks.RUNESTONE_PRESSURE_PLATE.get(), new Item.Properties()));
  public static DeferredHolder<Item, BlockItem> RUNESTONE_BRICK_PRESSURE_PLATE = ITEMS.register("runestone_brick_pressure_plate", () -> new BlockItem(ModBlocks.RUNESTONE_BRICK_PRESSURE_PLATE.get(), new Item.Properties()));
  public static DeferredHolder<Item, BlockItem> RUNESTONE_TILE_PRESSURE_PLATE = ITEMS.register("runestone_tile_pressure_plate", () -> new BlockItem(ModBlocks.RUNESTONE_TILE_PRESSURE_PLATE.get(), new Item.Properties()));
  public static DeferredHolder<Item, BlockItem> MOSSY_RUNESTONE_PRESSURE_PLATE = ITEMS.register("mossy_runestone_pressure_plate", () -> new BlockItem(ModBlocks.MOSSY_RUNESTONE_PRESSURE_PLATE.get(), new Item.Properties()));
  public static DeferredHolder<Item, BlockItem> RUNED_PRESSURE_PLATE = ITEMS.register("runed_pressure_plate", () -> new BlockItem(ModBlocks.RUNED_PRESSURE_PLATE.get(), new Item.Properties().fireResistant()));
  public static DeferredHolder<Item, BlockItem> RUNED_BRICK_PRESSURE_PLATE = ITEMS.register("runed_brick_pressure_plate", () -> new BlockItem(ModBlocks.RUNED_BRICK_PRESSURE_PLATE.get(), new Item.Properties().fireResistant()));
  public static DeferredHolder<Item, BlockItem> RUNED_TILE_PRESSURE_PLATE = ITEMS.register("runed_tile_pressure_plate", () -> new BlockItem(ModBlocks.RUNED_TILE_PRESSURE_PLATE.get(), new Item.Properties().fireResistant()));
  public static DeferredHolder<Item, BlockItem> WILDWOOD_PRESSURE_PLATE = ITEMS.register("wildwood_pressure_plate", () -> new BlockItem(ModBlocks.WILDWOOD_PRESSURE_PLATE.get(), new Item.Properties()));
  public static DeferredHolder<Item, BlockItem> WILDWOOD_DOOR = ITEMS.register("wildwood_door", () -> new BlockItem(ModBlocks.WILDWOOD_DOOR.get(), new Item.Properties()));
  public static DeferredHolder<Item, BlockItem> WILDWOOD_TRAPDOOR = ITEMS.register("wildwood_trapdoor", () -> new BlockItem(ModBlocks.WILDWOOD_TRAPDOOR.get(), new Item.Properties()));
  public static DeferredHolder<Item, BlockItem> WILDWOOD_LADDER = ITEMS.register("wildwood_ladder", () -> new BlockItem(ModBlocks.WILDWOOD_LADDER.get(), new Item.Properties()));
  public static DeferredHolder<Item, BlockItem> WILDWOOD_GATE = ITEMS.register("wildwood_gate", () -> new BlockItem(ModBlocks.WILDWOOD_GATE.get(), new Item.Properties()));
  public static DeferredHolder<Item, BlockItem> RUNESTONE_WALL = ITEMS.register("runestone_wall", () -> new BlockItem(ModBlocks.RUNESTONE_WALL.get(), new Item.Properties()));
  public static DeferredHolder<Item, BlockItem> MOSSY_RUNESTONE_WALL = ITEMS.register("mossy_runestone_wall", () -> new BlockItem(ModBlocks.MOSSY_RUNESTONE_WALL.get(), new Item.Properties()));
  public static DeferredHolder<Item, BlockItem> RUNESTONE_BRICK_WALL = ITEMS.register("runestone_brick_wall", () -> new BlockItem(ModBlocks.RUNESTONE_BRICK_WALL.get(), new Item.Properties()));
  public static DeferredHolder<Item, BlockItem> RUNESTONE_TILE_WALL = ITEMS.register("runestone_tile_wall", () -> new BlockItem(ModBlocks.RUNESTONE_TILE_WALL.get(), new Item.Properties()));
  public static DeferredHolder<Item, BlockItem> RUNED_WALL = ITEMS.register("runed_wall", () -> new BlockItem(ModBlocks.RUNED_WALL.get(), new Item.Properties().fireResistant()));
  public static DeferredHolder<Item, BlockItem> RUNED_BRICK_WALL = ITEMS.register("runed_brick_wall", () -> new BlockItem(ModBlocks.RUNED_BRICK_WALL.get(), new Item.Properties().fireResistant()));
  public static DeferredHolder<Item, BlockItem> RUNED_TILE_WALL = ITEMS.register("runed_tile_wall", () -> new BlockItem(ModBlocks.RUNED_TILE_WALL.get(), new Item.Properties().fireResistant()));
  public static DeferredHolder<Item, BlockItem> ELEMENTAL_SOIL = ITEMS.register("elemental_soil", () -> new BlockItem(ModBlocks.ELEMENTAL_SOIL.get(), new Item.Properties().fireResistant()));
  public static DeferredHolder<Item, BlockItem> AQUEOUS_SOIL = ITEMS.register("aqueous_soil", () -> new BlockItem(ModBlocks.AQUEOUS_SOIL.get(), new Item.Properties().fireResistant()));
  public static DeferredHolder<Item, BlockItem> CAELIC_SOIL = ITEMS.register("caelic_soil", () -> new BlockItem(ModBlocks.CAELIC_SOIL.get(), new Item.Properties().fireResistant()));
  public static DeferredHolder<Item, BlockItem> MAGMATIC_SOIL = ITEMS.register("magmatic_soil", () -> new BlockItem(ModBlocks.MAGMATIC_SOIL.get(), new Item.Properties().fireResistant()));
  public static DeferredHolder<Item, BlockItem> TERRAN_SOIL = ITEMS.register("terran_soil", () -> new BlockItem(ModBlocks.TERRAN_SOIL.get(), new Item.Properties().fireResistant()));
  public static DeferredHolder<Item, BlockItem> ENCHANTED_TURF = ITEMS.register("enchanted_turf", () -> new EnchantedTurfBlockItem(ModBlocks.ENCHANTED_TURF.get(), new Item.Properties().rarity(Rarity.RARE)));
  public static final DeferredHolder<Item, BlockItem> WILDWOOD_CHEST = ITEMS.register("wildwood_chest", () -> new BlockItem(ModBlocks.WILDWOOD_CHEST.get(), new Item.Properties().component(DataComponents.CONTAINER, ItemContainerContents.EMPTY)));
  // TODO: Contents
  public static DeferredHolder<Item, BlockItem> RITUAL_PEDESTAL = ITEMS.register("ritual_pedestal", () -> new BlockItem(ModBlocks.RITUAL_PEDESTAL.get(), new Item.Properties().component(DataComponents.CONTAINER, ItemContainerContents.EMPTY)));
  // TODO: Contens
  public static DeferredHolder<Item, BlockItem> REINFORCED_RITUAL_PEDESTAL = ITEMS.register("reinforced_ritual_pedestal", () -> new BlockItem(ModBlocks.REINFORCED_RITUAL_PEDESTAL.get(), new Item.Properties().component(DataComponents.CONTAINER, ItemContainerContents.EMPTY)
      .fireResistant()));
  public static DeferredHolder<Item, BlockItem> GROVE_CRAFTER = ITEMS.register("grove_crafter", () -> new BlockItem(ModBlocks.GROVE_CRAFTER.get(), new Item.Properties()));
  // TODO: Contents
  public static DeferredHolder<Item, BlockItem> GROVE_PEDESTAL = ITEMS.register("grove_pedestal", () -> new BlockItem(ModBlocks.GROVE_PEDESTAL.get(), new Item.Properties().component(DataComponents.CONTAINER, ItemContainerContents.EMPTY)));
  // TODO: Contents
  public static DeferredHolder<Item, BlockItem> WILDWOOD_PEDESTAL = ITEMS.register("wildwood_pedestal", () -> new BlockItem(ModBlocks.WILDWOOD_PEDESTAL.get(), new Item.Properties().component(DataComponents.CONTAINER, ItemContainerContents.EMPTY)));
  // TODO; Contents
  public static DeferredHolder<Item, BlockItem> DISPLAY_PEDESTAL = ITEMS.register("display_pedestal", () -> new BlockItem(ModBlocks.DISPLAY_PEDESTAL.get(), new Item.Properties().component(DataComponents.CONTAINER, ItemContainerContents.EMPTY)));
  /*  public static DeferredHolder<Item, BlockItem> GROWTH_AMPLIFIER = ITEMS.register("growth_amplifier", () -> new BlockItem(ModBlocks.GROWTH_AMPLIFIER.get(), new Item.Properties().component(DataComponents.CONTAINER, ItemContainerContents.EMPTY)));*/
  public static DeferredHolder<Item, BlockItem> RED_FAIRY_HUT = ITEMS.register("red_fairy_hut", () -> new BlockItem(ModBlocks.RED_FAIRY_HUT.get(), new Item.Properties()));
  public static DeferredHolder<Item, BlockItem> BROWN_FAIRY_HUT = ITEMS.register("brown_fairy_hut", () -> new BlockItem(ModBlocks.BROWN_FAIRY_HUT.get(), new Item.Properties()));
  public static DeferredHolder<Item, BlockItem> BAFFLECAP_FAIRY_HUT = ITEMS.register("bafflecap_fairy_hut", () -> new BlockItem(ModBlocks.BAFFLECAP_FAIRY_HUT.get(), new Item.Properties()));
  public static DeferredHolder<Item, BlockItem> CRIMSON_FAIRY_HUT = ITEMS.register("crimson_fairy_hut", () -> new BlockItem(ModBlocks.CRIMSON_FAIRY_HUT.get(), new Item.Properties()));
  public static DeferredHolder<Item, BlockItem> WARPED_FAIRY_HUT = ITEMS.register("warped_fairy_hut", () -> new BlockItem(ModBlocks.WARPED_FAIRY_HUT.get(), new Item.Properties()));
  public static DeferredHolder<Item, BlockItem> FUNGAL_TRANSMUTER = ITEMS.register("fungal_transmuter", () -> new BlockItem(ModBlocks.FUNGAL_TRANSMUTER.get(), new Item.Properties().component(DataComponents.CONTAINER, ItemContainerContents.EMPTY)));

  public static DeferredHolder<Item, BlockItem> BAFFLECAP_BLOCK = ITEMS.register("bafflecap_block", () -> new BlockItem(ModBlocks.BAFFLECAP_BLOCK.get(), new Item.Properties()));
  public static DeferredHolder<Item, BlockItem> PRIMAL_GROVE_STONE = ITEMS.register("primal_grove_stone", () -> new BlockItem(ModBlocks.PRIMAL_GROVE_STONE.get(), new Item.Properties()));
  public static DeferredHolder<Item, BlockItem> WILD_GROVE_STONE = ITEMS.register("wild_grove_stone", () -> new BlockItem(ModBlocks.WILD_GROVE_STONE.get(), new Item.Properties()));
  public static DeferredHolder<Item, BlockItem> SPROUTING_GROVE_STONE = ITEMS.register("sprouting_grove_stone", () -> new BlockItem(ModBlocks.SPROUTING_GROVE_STONE.get(), new Item.Properties()));
  public static DeferredHolder<Item, BlockItem> TWILIGHT_GROVE_STONE = ITEMS.register("twilight_grove_stone", () -> new BlockItem(ModBlocks.TWILIGHT_GROVE_STONE.get(), new Item.Properties()));
  public static DeferredHolder<Item, BlockItem> FUNGAL_GROVE_STONE = ITEMS.register("fungal_grove_stone", () -> new BlockItem(ModBlocks.FUNGAL_GROVE_STONE.get(), new Item.Properties()));
  public static DeferredHolder<Item, BlockItem> FAIRY_GROVE_STONE = ITEMS.register("fairy_grove_stone", () -> new BlockItem(ModBlocks.FAIRY_GROVE_STONE.get(), new Item.Properties()));
  public static DeferredHolder<Item, BlockItem> ELEMENTAL_GROVE_STONE = ITEMS.register("elemental_grove_stone", () -> new BlockItem(ModBlocks.ELEMENTAL_GROVE_STONE.get(), new Item.Properties()));
  // TODO: Info
  public static DeferredHolder<Item, BlockItem> INCENSE_BURNER = ITEMS.register("incense_burner", () -> new BlockItem(ModBlocks.INCENSE_BURNER.get(), new Item.Properties()));
  /*  public static DeferredHolder<Item, BlockItem> STONE_ALTAR = ITEMS.register("stone_altar", () -> new BlockItem(ModBlocks.STONE_ALTAR.get(), new Item.Properties()));*/
  public static DeferredHolder<Item, BlockItem> MORTAR = ITEMS.register("mortar", () -> new BlockItem(ModBlocks.MORTAR.get(), new Item.Properties().component(DataComponents.CONTAINER, ItemContainerContents.EMPTY)));
  // TODO: Contents
  public static DeferredHolder<Item, BlockItem> PYRE = ITEMS.register("pyre", () -> new BlockItem(ModBlocks.PYRE.get(), new Item.Properties().component(DataComponents.CONTAINER, ItemContainerContents.EMPTY)));
  // TODO: Contents
  public static DeferredHolder<Item, BlockItem> SOUL_PYRE = ITEMS.register("soul_pyre", () -> new BlockItem(ModBlocks.SOUL_PYRE.get(), new Item.Properties().component(DataComponents.CONTAINER, ItemContainerContents.EMPTY)));
  // TODO: Contents
  public static DeferredHolder<Item, BlockItem> REINFORCED_PYRE = ITEMS.register("reinforced_pyre", () -> new BlockItem(ModBlocks.REINFORCED_PYRE.get(), new Item.Properties().component(DataComponents.CONTAINER, ItemContainerContents.EMPTY)
      .fireResistant()));
  // TODO: Contents
  public static DeferredHolder<Item, BlockItem> REINFORCED_SOUL_PYRE = ITEMS.register("reinforced_soul_pyre", () -> new BlockItem(ModBlocks.REINFORCED_SOUL_PYRE.get(), new Item.Properties().component(DataComponents.CONTAINER, ItemContainerContents.EMPTY)
      .fireResistant()));
  public static DeferredHolder<Item, BlockItem> DECORATIVE_PYRE = ITEMS.register("decorative_pyre", () -> new BlockItem(ModBlocks.DECORATIVE_PYRE.get(), new Item.Properties()));
  public static DeferredHolder<Item, BlockItem> DECORATIVE_SOUL_PYRE = ITEMS.register("decorative_soul_pyre", () -> new BlockItem(ModBlocks.DECORATIVE_SOUL_PYRE.get(), new Item.Properties()));
  public static DeferredHolder<Item, BlockItem> UNENDING_BOWL = ITEMS.register("unending_bowl", () -> new BlockItem(ModBlocks.UNENDING_BOWL.get(), new Item.Properties()));

  // Actual items
  public static final DeferredHolder<Item, ItemNameBlockItem> WILDROOT = ITEMS.register("wildroot", () -> new ItemNameBlockItem(ModBlocks.WILDROOT_CROP.get(), new Item.Properties()));
  public static final DeferredHolder<Item, Item> GROVE_MOSS = ITEMS.register("grove_moss", () -> new Item(new Item.Properties()));
  public static final DeferredHolder<Item, ItemNameBlockItem> CLOUD_BERRY = ITEMS.register("cloud_berry", () -> new ItemNameBlockItem(ModBlocks.CLOUD_BERRY_CROP.get(), new Item.Properties().fireResistant()));
  public static final DeferredHolder<Item, ItemNameBlockItem> DEWGONIA = ITEMS.register("dewgonia", () -> new ItemNameBlockItem(ModBlocks.DEWGONIA_CROP.get(), new Item.Properties().fireResistant()));
  public static final DeferredHolder<Item, ItemNameBlockItem> INFERNO_BULB = ITEMS.register("inferno_bulb", () -> new ItemNameBlockItem(ModBlocks.INFERNO_BULB_CROP.get(), new Item.Properties().fireResistant()));
  public static final DeferredHolder<Item, ItemNameBlockItem> STALICRIPE = ITEMS.register("stalicripe", () -> new ItemNameBlockItem(ModBlocks.STALICRIPE_CROP.get(), new Item.Properties().fireResistant()));
  public static final DeferredHolder<Item, ItemNameBlockItem> BAFFLECAP = ITEMS.register("bafflecap", () -> new ItemNameBlockItem(ModBlocks.BAFFLECAP.get(), new Item.Properties()));
  public static final DeferredHolder<Item, Item> MOONGLOW = ITEMS.register("moonglow", () -> new Item(new Item.Properties()));
  public static final DeferredHolder<Item, Item> PERESKIA = ITEMS.register("pereskia", () -> new Item(new Item.Properties()));
  public static final DeferredHolder<Item, Item> SPIRITLEAF = ITEMS.register("spiritleaf", () -> new Item(new Item.Properties()));
  public static final DeferredHolder<Item, Item> WILDEWHEET = ITEMS.register("wildewheet", () -> new Item(new Item.Properties()));
  public static final DeferredHolder<Item, ItemNameBlockItem> MOONGLOW_SEEDS = ITEMS.register("moonglow_seeds", () -> new ItemNameBlockItem(ModBlocks.MOONGLOW_CROP.get(), new Item.Properties()));
  public static final DeferredHolder<Item, ItemNameBlockItem> PERESKIA_BULB = ITEMS.register("pereskia_bulb", () -> new ItemNameBlockItem(ModBlocks.PERESKIA_CROP.get(), new Item.Properties()));
  public static final DeferredHolder<Item, ItemNameBlockItem> SPIRITLEAF_SEEDS = ITEMS.register("spiritleaf_seeds", () -> new ItemNameBlockItem(ModBlocks.SPIRITLEAF_CROP.get(), new Item.Properties()));
  public static final DeferredHolder<Item, ItemNameBlockItem> WILDEWHEET_SEEDS = ITEMS.register("wildewheet_seeds", () -> new ItemNameBlockItem(ModBlocks.WILDEWHEET_CROP.get(), new Item.Properties()));
  public static final DeferredHolder<Item, GroveSporesItem> GROVE_SPORES = ITEMS.register("grove_spores", () -> new GroveSporesItem(new Item.Properties()));
  public static final DeferredHolder<Item, ItemNameBlockItem> AUBERGINE_SEEDS = ITEMS.register("aubergine_seeds", () -> new ItemNameBlockItem(ModBlocks.AUBERGINE_CROP.get(), new Item.Properties()));
  public static DeferredHolder<Item, Item> CARAPACE = ITEMS.register("carapace", () -> new Item(new Item.Properties()));
  public static DeferredHolder<Item, Item> PELT = ITEMS.register("pelt", () -> new Item(new Item.Properties()));
  public static DeferredHolder<Item, Item> ANTLERS = ITEMS.register("antlers", () -> new Item(new Item.Properties()));
  public static DeferredHolder<Item, Item> VENISON = ITEMS.register("venison", () -> new Item(new Item.Properties().food(ModFoods.VENISON)));
  public static DeferredHolder<Item, Item> COOKED_VENISON = ITEMS.register("cooked_venison", () -> new Item(new Item.Properties().food(ModFoods.COOKED_VENISON)
  ));
  public static DeferredHolder<Item, Item> RAW_SQUID = ITEMS.register("raw_squid", () -> new Item(new Item.Properties().food(ModFoods.RAW_SQUID)));
  public static DeferredHolder<Item, Item> COOKED_SQUID = ITEMS.register("cooked_squid", () -> new Item(new Item.Properties().food(ModFoods.COOKED_SQUID)
  ));
  public static DeferredHolder<Item, Item> ASSORTED_SEEDS = ITEMS.register("assorted_seeds", () -> new Item(new Item.Properties()));
  public static DeferredHolder<Item, FastFoodItem> COOKED_SEEDS = ITEMS.register("cooked_seeds", () -> new FastFoodItem(new Item.Properties().food(ModFoods.COOKED_SEEDS)
  ));
  public static DeferredHolder<Item, Item> COOKED_BEETROOT = ITEMS.register("cooked_beetroot", () -> new Item(new Item.Properties().food(ModFoods.COOKED_BEETROOT)
  ));
  public static DeferredHolder<Item, Item> COOKED_CARROT = ITEMS.register("cooked_carrot", () -> new Item(new Item.Properties().food(ModFoods.COOKED_CARROT)
  ));
  public static final DeferredHolder<Item, Item> COOKED_PERESKIA = ITEMS.register("cooked_pereskia", () -> new Item(new Item.Properties().food(ModFoods.COOKED_AUBERGINE)
  ));
  public static DeferredHolder<Item, Item> AUBERGINE = ITEMS.register("aubergine", () -> new Item(new Item.Properties().food(ModFoods.AUBERGINE)
  ));
  public static DeferredHolder<Item, Item> COOKED_AUBERGINE = ITEMS.register("cooked_aubergine", () -> new Item(new Item.Properties().food(ModFoods.COOKED_AUBERGINE)
  ));
  public static DeferredHolder<Item, Item> STUFFED_AUBERGINE = ITEMS.register("stuffed_aubergine", () -> new Item(new Item.Properties().food(ModFoods.STUFFED_AUBERGINE)));
  public static DeferredHolder<Item, Item> AUBERGINE_SALAD = ITEMS.register("aubergine_salad", () -> new Item(new Item.Properties().food(ModFoods.AUBERGINE_SALAD)
      .craftRemainder(Items.BOWL)));
  public static DeferredHolder<Item, Item> BEETROOT_SALAD = ITEMS.register("beetroot_salad", () -> new Item(new Item.Properties().food(ModFoods.BEETROOT_SALAD)
      .craftRemainder(Items.BOWL)));
  public static DeferredHolder<Item, Item> STEWED_EGGPLANT = ITEMS.register("stewed_eggplant", () -> new Item(new Item.Properties().food(ModFoods.STEWED_EGGPLANT)
      .craftRemainder(Items.BOWL)));
  public static final DeferredHolder<Item, Item> WILDROOT_STEW = ITEMS.register("wildroot_stew", () -> new Item(new Item.Properties().food(ModFoods.WILDROOT_STEW)
      .craftRemainder(Items.BOWL)));
  public static final DeferredHolder<Item, Item> FLOUR = ITEMS.register("flour", () -> new Item(new Item.Properties()));
  public static final DeferredHolder<Item, Item> WILDEWHEET_BREAD = ITEMS.register("wildewheet_bread", () -> new Item(new Item.Properties().food(ModFoods.WILDEWHEET_BREAD)));
  public static DeferredHolder<Item, TooltipDrinkItem> APPLE_CORDIAL = ITEMS.register("apple_cordial", () -> new TooltipDrinkItem("roots.drinks.slow_regen", new Item.Properties().food(ModFoods.APPLE_CORDIAL)
      .craftRemainder(Items.GLASS_BOTTLE)));
  public static DeferredHolder<Item, TooltipDrinkItem> CACTUS_SYRUP = ITEMS.register("cactus_syrup", () -> new TooltipDrinkItem("roots.drinks.slow_regen", new Item.Properties().food(ModFoods.CACTUS_SYRUP)
      .craftRemainder(Items.GLASS_BOTTLE)));
  public static DeferredHolder<Item, TooltipDrinkItem> DANDELION_CORDIAL = ITEMS.register("dandelion_cordial", () -> new TooltipDrinkItem("roots.drinks.wakefulness", new Item.Properties().food(ModFoods.DANDELION_CORDIAL)
      .craftRemainder(Items.GLASS_BOTTLE)));
  public static DeferredHolder<Item, TooltipDrinkItem> LILAC_CORDIAL = ITEMS.register("lilac_cordial", () -> new TooltipDrinkItem("roots.drinks.slow_regen", new Item.Properties().food(ModFoods.LILAC_CORDIAL)
      .craftRemainder(Items.GLASS_BOTTLE)));
  public static DeferredHolder<Item, TooltipDrinkItem> PEONY_CORDIAL = ITEMS.register("peony_cordial", () -> new TooltipDrinkItem("roots.drinks.slow_regen", new Item.Properties().food(ModFoods.PEONY_CORDIAL)
      .craftRemainder(Items.GLASS_BOTTLE)));
  public static DeferredHolder<Item, TooltipDrinkItem> ROSE_CORDIAL = ITEMS.register("rose_cordial", () -> new TooltipDrinkItem("roots.drinks.slow_regen", new Item.Properties().food(ModFoods.ROSE_CORDIAL)
      .craftRemainder(Items.GLASS_BOTTLE)));
  public static DeferredHolder<Item, TooltipDrinkItem> VINEGAR = ITEMS.register("vinegar", () -> new TooltipDrinkItem("roots.drinks.sour", new Item.Properties().food(ModFoods.VINEGAR)
      .craftRemainder(Items.GLASS_BOTTLE)));
  public static DeferredHolder<Item, TooltipDrinkItem> VEGETABLE_JUICE = ITEMS.register("vegetable_juice", () -> new TooltipDrinkItem("roots.drinks.slow_regen", new Item.Properties().food(ModFoods.VEGETABLE_JUICE)
      .craftRemainder(Items.GLASS_BOTTLE)));
  public static DeferredHolder<Item, Item> INK_BOTTLE = ITEMS.register("ink_bottle", () -> new Item(new Item.Properties().craftRemainder(Items.GLASS_BOTTLE)));

  public static final DeferredHolder<Item, PouchItem> APOTHECARY_POUCH = ITEMS.register("apothecary_pouch", () -> new PouchItem(ApothecaryPouchMenu::new, new Item.Properties().stacksTo(1)
      .component(ModAttachments.APOTHECARY_POUCH_CONTENTS, ItemContainerContents.EMPTY)
      .component(ModAttachments.DYEABLE, DyeableWithDefault.DEFAULT)));
  public static final DeferredHolder<Item, PouchItem> COMPONENT_POUCH = ITEMS.register("component_pouch", () -> new PouchItem(ComponentPouchMenu::new, new Item.Properties().stacksTo(1)
      .component(ModAttachments.COMPONENT_POUCH_CONTENTS, ItemContainerContents.EMPTY)
      .component(ModAttachments.DYEABLE, DyeableWithDefault.DEFAULT)));
  public static final DeferredHolder<Item, CreativeComponentPouch> CREATIVE_POUCH = ITEMS.register("creative_pouch", () -> new CreativeComponentPouch(new Item.Properties().stacksTo(1)
      .rarity(Rarity.EPIC)));
  public static final DeferredHolder<Item, PouchItem> SYLVAN_POUCH = ITEMS.register("sylvan_pouch", () -> new PouchItem(SylvanPouchMenu::new, new Item.Properties().stacksTo(1)
      .component(ModAttachments.SYLVAN_POUCH_CONTENTS, ItemContainerContents.EMPTY)
      .component(ModAttachments.DYEABLE, DyeableWithDefault.DEFAULT)));

  static {
    ITEMS.addAlias(RootsAPI.rl("fey_pouch"), RootsAPI.rl("sylvan_pouch"));
  }

  public static final DeferredHolder<Item, PouchItem> HERB_POUCH = ITEMS.register("herb_pouch", () -> new PouchItem(HerbPouchMenu::new, new Item.Properties().stacksTo(1)
      .component(ModAttachments.HERB_POUCH_CONTENTS, ItemContainerContents.EMPTY)
      .component(ModAttachments.DYEABLE, DyeableWithDefault.DEFAULT)));

  public static final DeferredHolder<Item, FireStarterItem> FIRE_STARTER = ITEMS.register("fire_starter", () -> new FireStarterItem(new Item.Properties()));
  // TODO: What are we doing with this
  public static final DeferredHolder<Item, GramaryItem> GRAMARY = ITEMS.register("gramary", () -> new GramaryItem(new Item.Properties().stacksTo(1)
      .component(ModAttachments.GRAMARY_MODE, GramaryItem.GramaryMode.NONE)));
  public static final DeferredHolder<Item, LivingArrowItem> LIVING_ARROW = ITEMS.register("living_arrow", () -> new LivingArrowItem(new Item.Properties()));
  public static final DeferredHolder<Item, LivingAxeItem> LIVING_AXE = ITEMS.register("living_axe", () -> new LivingAxeItem(RootsAPI.LIVING_TOOL_TIER, new Item.Properties().attributes(LivingSwordItem.createAttributes(RootsAPI.LIVING_TOOL_TIER, 6.0f, -3.2f))));
  public static final DeferredHolder<Item, LivingHoeItem> LIVING_HOE = ITEMS.register("living_hoe", () -> new LivingHoeItem(RootsAPI.LIVING_TOOL_TIER, new Item.Properties().attributes(LivingHoeItem.createAttributes(RootsAPI.LIVING_TOOL_TIER, 0f, -3.f))));
  public static final DeferredHolder<Item, LivingPickaxeItem> LIVING_PICKAXE = ITEMS.register("living_pickaxe", () -> new LivingPickaxeItem(RootsAPI.LIVING_TOOL_TIER, new Item.Properties().attributes(LivingPickaxeItem.createAttributes(RootsAPI.LIVING_TOOL_TIER, 1f, -2.8f))));
  public static final DeferredHolder<Item, LivingShovelItem> LIVING_SHOVEL = ITEMS.register("living_shovel", () -> new LivingShovelItem(RootsAPI.LIVING_TOOL_TIER, new Item.Properties().attributes(LivingShovelItem.createAttributes(RootsAPI.LIVING_TOOL_TIER, 1.5f, -3.0f))));
  public static final DeferredHolder<Item, LivingSwordItem> LIVING_SWORD = ITEMS.register("living_sword", () -> new LivingSwordItem(RootsAPI.LIVING_TOOL_TIER, new Item.Properties().attributes(LivingSwordItem.createAttributes(RootsAPI.LIVING_TOOL_TIER, 3.0f, -2.4f))));
  public static final DeferredHolder<Item, Item> PESTLE = ITEMS.register("pestle", () -> new Item(new Item.Properties().stacksTo(1)));
  public static final DeferredHolder<Item, PickaxeItem> RUNED_PICKAXE = ITEMS.register("runed_pickaxe", () -> new PickaxeItem(RootsAPI.RUNED_TIER, new Item.Properties().attributes(PickaxeItem.createAttributes(RootsAPI.RUNED_TIER, 1f, -2.8f))));
  public static final DeferredHolder<Item, AxeItem> RUNED_AXE = ITEMS.register("runed_axe", () -> new AxeItem(RootsAPI.RUNED_TIER, new Item.Properties().attributes(AxeItem.createAttributes(RootsAPI.RUNED_TIER, 6.0f, -3.2f))));
  public static final DeferredHolder<Item, SwordItem> RUNED_DAGGER = ITEMS.register("runed_dagger", () -> new SwordItem(RootsAPI.RUNED_TIER, new Item.Properties().attributes(SwordItem.createAttributes(RootsAPI.RUNED_TIER, 3.0f, -2.4f))));
  public static final DeferredHolder<Item, HoeItem> RUNED_HOE = ITEMS.register("runed_hoe", () -> new HoeItem(RootsAPI.RUNED_TIER, new Item.Properties().attributes(HoeItem.createAttributes(RootsAPI.RUNED_TIER, 0f, -3.f))));
  public static final DeferredHolder<Item, ShovelItem> RUNED_SHOVEL = ITEMS.register("runed_shovel", () -> new ShovelItem(RootsAPI.RUNED_TIER, new Item.Properties().attributes(ShovelItem.createAttributes(RootsAPI.RUNED_TIER, 1.5f, -3.0f))));
  public static final DeferredHolder<Item, SwordItem> RUNED_SWORD = ITEMS.register("runed_sword", () -> new SwordItem(RootsAPI.RUNED_TIER, new Item.Properties().attributes(SwordItem.createAttributes(RootsAPI.RUNED_TIER, 3.0f, -2.4f))));
  public static final DeferredHolder<Item, RunicShearsItem> RUNIC_SHEARS = ITEMS.register("runic_shears", () -> new RunicShearsItem(new Item.Properties().durability(313)
      .stacksTo(1).component(DataComponents.TOOL, ShearsItem.createToolProperties())));
  public static final DeferredHolder<Item, CastingItem> STAFF = ITEMS.register("staff", () -> new CastingItem(new Item.Properties().component(ModAttachments.SPELL_STORAGE, SpellStorage.EMPTY.get())
      .stacksTo(1)));
  // TODO: Durability?
  public static final DeferredHolder<Item, Item> WILDWOOD_BOW = ITEMS.register("wildwood_bow", () -> new Item(new Item.Properties().durability(384)
      .stacksTo(1)));
  public static final DeferredHolder<Item, QuiverItem> WILDWOOD_QUIVER = ITEMS.register("wildwood_quiver", () -> new QuiverItem(new Item.Properties().stacksTo(1)
      .component(ModAttachments.QUIVER_CONTENTS, ItemContainerContents.EMPTY)));
  public static final DeferredHolder<Item, WoodenShearsItem> WOODEN_SHEARS = ITEMS.register("wooden_shears", () -> new WoodenShearsItem(new Item.Properties().durability(120)
      .stacksTo(1).component(DataComponents.TOOL, ShearsItem.createToolProperties())));
  public static DeferredHolder<Item, KnifeItem> WOODEN_KNIFE = ITEMS.register("wooden_knife", () -> new KnifeItem(Tiers.WOOD, new Item.Properties().component(ModAttachments.FORAGING, 1)
      .stacksTo(1).attributes(SwordItem.createAttributes(Tiers.WOOD, 1f, -2.4f))));
  public static DeferredHolder<Item, KnifeItem> STONE_KNIFE = ITEMS.register("stone_knife", () -> new KnifeItem(Tiers.STONE, new Item.Properties().component(ModAttachments.FORAGING, 2)
      .stacksTo(1).attributes(SwordItem.createAttributes(Tiers.STONE, 1f, -2.4f))));
  public static DeferredHolder<Item, KnifeItem> COPPER_KNIFE = ITEMS.register("copper_knife", () -> new KnifeItem(RootsAPI.COPPER_TIER, new Item.Properties().component(ModAttachments.FORAGING, 3)
      .stacksTo(1).attributes(SwordItem.createAttributes(RootsAPI.COPPER_TIER, 1f, -2.4f))));
  public static DeferredHolder<Item, KnifeItem> IRON_KNIFE = ITEMS.register("iron_knife", () -> new KnifeItem(Tiers.IRON, new Item.Properties().component(ModAttachments.FORAGING, 3)
      .stacksTo(1).attributes(SwordItem.createAttributes(Tiers.IRON, 1f, -2.4f))));
  public static DeferredHolder<Item, KnifeItem> GOLDEN_KNIFE = ITEMS.register("golden_knife", () -> new KnifeItem(Tiers.GOLD, new Item.Properties().component(ModAttachments.FORAGING, 4)
      .stacksTo(1).attributes(SwordItem.createAttributes(Tiers.GOLD, 1f, -2.4f))));
  public static DeferredHolder<Item, KnifeItem> SILVER_KNIFE = ITEMS.register("silver_knife", () -> new KnifeItem(RootsAPI.SILVER_TIER, new Item.Properties().component(ModAttachments.FORAGING, 4)
      .stacksTo(1).attributes(SwordItem.createAttributes(Tiers.GOLD, 1f, -2.4f))));
  public static DeferredHolder<Item, KnifeItem> DIAMOND_KNIFE = ITEMS.register("diamond_knife", () -> new KnifeItem(Tiers.DIAMOND, new Item.Properties().component(ModAttachments.FORAGING, 5)
      .stacksTo(1).attributes(SwordItem.createAttributes(Tiers.DIAMOND, 1f, -2.4f))));
  public static DeferredHolder<Item, KnifeItem> NETHERITE_KNIFE = ITEMS.register("netherite_knife", () -> new KnifeItem(Tiers.NETHERITE, new Item.Properties().component(ModAttachments.FORAGING, 6)
      .stacksTo(1).attributes(SwordItem.createAttributes(Tiers.NETHERITE, 1f, -2.4f)).fireResistant()));

  public static final DeferredHolder<Item, Item> RELIQUARY = ITEMS.register("reliquary", () -> new Item(new Item.Properties().rarity(Rarity.EPIC)));
  public static final DeferredHolder<Item, Item> SPIRIT_BAG = ITEMS.register("spirit_bag", () -> new Item(new Item.Properties().rarity(Rarity.RARE)));
  public static final DeferredHolder<Item, Item> SYLVAN_LEATHER = ITEMS.register("sylvan_leather", () -> new Item(new Item.Properties().rarity(Rarity.RARE)));

  static {
    ITEMS.addAlias(RootsAPI.rl("fey_leather"), RootsAPI.rl("sylvan_leather"));
  }

  public static final DeferredHolder<Item, EffectUseItem> GLASS_EYE = ITEMS.register("glass_eye", () -> new EffectUseItem(MobEffects.NIGHT_VISION, 0, 20 * 30, new Item.Properties()));
  public static final DeferredHolder<Item, Item> LIFE_ESSENCE = ITEMS.register("life_essence", () -> new Item(new Item.Properties()));
  public static final DeferredHolder<Item, Item> MYSTIC_FEATHER = ITEMS.register("mystic_feather", () -> new Item(new Item.Properties()));
  public static final DeferredHolder<Item, Item> RUNIC_DUST = ITEMS.register("runic_dust", () -> new Item(new Item.Properties()));
  public static final DeferredHolder<Item, Item> STRANGE_OOZE = ITEMS.register("strange_ooze", () -> new Item(new Item.Properties()));
  public static final DeferredHolder<Item, ArmorItem> ANTLER_HAT = ITEMS.register("antler_hat", () -> new ArmorItem(ANTLER_MATERIAL, ArmorItem.Type.HELMET, new Item.Properties().durability(399)));
  public static DeferredHolder<Item, ArmorItem> BEETLE_HELMET = ITEMS.register("beetle_helmet", () -> new ArmorItem(CARAPACE_MATERIAL, ArmorItem.Type.HELMET, new Item.Properties().durability(ArmorItem.Type.HELMET.getDurability(25))));
  public static DeferredHolder<Item, ArmorItem> BEETLE_CHESTPLATE = ITEMS.register("beetle_chestplate", () -> new ArmorItem(CARAPACE_MATERIAL, ArmorItem.Type.CHESTPLATE, new Item.Properties().durability(ArmorItem.Type.CHESTPLATE.getDurability(25))));
  public static DeferredHolder<Item, ArmorItem> BEETLE_LEGGINGS = ITEMS.register("beetle_leggings", () -> new ArmorItem(CARAPACE_MATERIAL, ArmorItem.Type.LEGGINGS, new Item.Properties().durability(ArmorItem.Type.LEGGINGS.getDurability(25))));
  public static DeferredHolder<Item, ArmorItem> BEETLE_BOOTS = ITEMS.register("beetle_boots", () -> new ArmorItem(CARAPACE_MATERIAL, ArmorItem.Type.BOOTS, new Item.Properties().durability(ArmorItem.Type.BOOTS.getDurability(25))));
  public static DeferredHolder<Item, Item> RAW_SILVER = ITEMS.register("raw_silver", () -> new Item(new Item.Properties()));
  public static DeferredHolder<Item, Item> SILVER_INGOT = ITEMS.register("silver_ingot", () -> new Item(new Item.Properties()));
  public static DeferredHolder<Item, Item> SILVER_NUGGET = ITEMS.register("silver_nugget", () -> new Item(new Item.Properties()));
  public static DeferredHolder<Item, Item> SILVER_STATER = ITEMS.register("silver_stater", () -> new Item(new Item.Properties()));
  public static DeferredHolder<Item, Item> COPPER_NUGGET = ITEMS.register("copper_nugget", () -> new Item(new Item.Properties()));
  // TODO: Check damage values
  public static DeferredHolder<Item, AxeItem> COPPER_AXE = ITEMS.register("copper_axe", () -> new AxeItem(RootsAPI.COPPER_TIER, new Item.Properties().attributes(AxeItem.createAttributes(RootsAPI.COPPER_TIER, 6.0f, -3.2f))));
  public static DeferredHolder<Item, HoeItem> COPPER_HOE = ITEMS.register("copper_hoe", () -> new HoeItem(RootsAPI.COPPER_TIER, new Item.Properties().attributes(HoeItem.createAttributes(RootsAPI.COPPER_TIER, 0f, -3.f))));
  public static DeferredHolder<Item, PickaxeItem> COPPER_PICKAXE = ITEMS.register("copper_pickaxe", () -> new PickaxeItem(RootsAPI.COPPER_TIER, new Item.Properties().attributes(PickaxeItem.createAttributes(RootsAPI.COPPER_TIER, 1f, -2.8f))));
  public static DeferredHolder<Item, ShovelItem> COPPER_SHOVEL = ITEMS.register("copper_shovel", () -> new ShovelItem(RootsAPI.COPPER_TIER, new Item.Properties().attributes(ShovelItem.createAttributes(RootsAPI.COPPER_TIER, 1.5f, -3.0f))));
  public static DeferredHolder<Item, SwordItem> COPPER_SWORD = ITEMS.register("copper_sword", () -> new SwordItem(RootsAPI.COPPER_TIER, new Item.Properties().attributes(SwordItem.createAttributes(RootsAPI.COPPER_TIER, 3.0f, -2.4f))));
  public static DeferredHolder<Item, ArmorItem> COPPER_HELMET = ITEMS.register("copper_helmet", () -> new ArmorItem(COPPER_MATERIAL, ArmorItem.Type.HELMET, new Item.Properties().durability(ArmorItem.Type.HELMET.getDurability(15))));
  public static DeferredHolder<Item, ArmorItem> COPPER_CHESTPLATE = ITEMS.register("copper_chestplate", () -> new ArmorItem(COPPER_MATERIAL, ArmorItem.Type.CHESTPLATE, new Item.Properties().durability(ArmorItem.Type.CHESTPLATE.getDurability(15))));
  public static DeferredHolder<Item, ArmorItem> COPPER_LEGGINGS = ITEMS.register("copper_leggings", () -> new ArmorItem(COPPER_MATERIAL, ArmorItem.Type.LEGGINGS, new Item.Properties().durability(ArmorItem.Type.LEGGINGS.getDurability(15))));
  public static DeferredHolder<Item, ArmorItem> COPPER_BOOTS = ITEMS.register("copper_boots", () -> new ArmorItem(COPPER_MATERIAL, ArmorItem.Type.BOOTS, new Item.Properties().durability(ArmorItem.Type.BOOTS.getDurability(15))));
  public static DeferredHolder<Item, Item> ALERTNESS_CHARM = ITEMS.register("charm_of_alertness", () -> new Item(new Item.Properties().stacksTo(1)));
  public static DeferredHolder<Item, HomesicknessCharm> HOMESICKNSES_CHARM = ITEMS.register("charm_of_homesickness", () -> new HomesicknessCharm(new Item.Properties().stacksTo(1)));
  public static DeferredHolder<Item, DeferredSpawnEggItem> BEETLE_SPAWN_EGG = ITEMS.register("beetle_spawn_egg", () -> new DeferredSpawnEggItem(ModEntities.BEETLE, 0x418594, 0x211D15, new Item.Properties()));
  public static final DeferredHolder<Item, DeferredSpawnEggItem> JERBOA_SPAWN_EGG = ITEMS.register("jerboa_spawn_egg", () -> new DeferredSpawnEggItem(ModEntities.JERBOA, 0xdbc6a4, 0xeaaea1, new Item.Properties()));
  public static DeferredHolder<Item, DeferredSpawnEggItem> DEER_SPAWN_EGG = ITEMS.register("deer_spawn_egg", () -> new DeferredSpawnEggItem(ModEntities.DEER, 0xa18458, 0x5e4d33, new Item.Properties()));
  public static DeferredHolder<Item, DeferredSpawnEggItem> FENNEC_SPAWN_EGG = ITEMS.register("fennec_spawn_egg", () -> new DeferredSpawnEggItem(ModEntities.FENNEC, 0xe9dcc2, 0xb1855c, new Item.Properties()));
  public static DeferredHolder<Item, DeferredSpawnEggItem> GREEN_SPROUT_SPAWN_EGG = ITEMS.register("green_sprout_spawn_egg", () -> new DeferredSpawnEggItem(ModEntities.GREEN_SPROUT, 0x9adb58, 0x2c9425, new Item.Properties()));
  public static DeferredHolder<Item, DeferredSpawnEggItem> TAN_SPROUT_SPAWN_EGG = ITEMS.register("tan_sprout_spawn_egg", () -> new DeferredSpawnEggItem(ModEntities.TAN_SPROUT, 0xeeca5f, 0xbb6c20, new Item.Properties()));
  public static DeferredHolder<Item, DeferredSpawnEggItem> RED_SPROUT_SPAWN_EGG = ITEMS.register("red_sprout_spawn_egg", () -> new DeferredSpawnEggItem(ModEntities.RED_SPROUT, 0xe6754c, 0xbd2637, new Item.Properties()));
  public static DeferredHolder<Item, DeferredSpawnEggItem> PURPLE_SPROUT_SPAWN_EGG = ITEMS.register("purple_sprout_spawn_egg", () -> new DeferredSpawnEggItem(ModEntities.PURPLE_SPROUT, 0xdd45e6, 0x6825ba, new Item.Properties()));
  public static DeferredHolder<Item, DeferredSpawnEggItem> SNOW_SPROUT_SPAWN_EGG = ITEMS.register("snow_sprout_spawn_egg", () -> new DeferredSpawnEggItem(ModEntities.SNOW_SPROUT, 0xfffffe, 0xcbe7e8, new Item.Properties()));
  public static DeferredHolder<Item, DeferredSpawnEggItem> MELODY_SPROUT_SPAWN_EGG = ITEMS.register("melody_sprout_spawn_egg", () -> new DeferredSpawnEggItem(ModEntities.MELODY_SPROUT, 0xecfbaf, 0xa472a3, new Item.Properties()));
  public static DeferredHolder<Item, DeferredSpawnEggItem> OWL_SPAWN_EGG = ITEMS.register("owl_spawn_egg", () -> new DeferredSpawnEggItem(ModEntities.OWL, 0x8c654a, 0xdec9ba, new Item.Properties()));
  public static DeferredHolder<Item, DeferredSpawnEggItem> DUCK_SPAWN_EGG = ITEMS.register("duck_spawn_egg", () -> new DeferredSpawnEggItem(ModEntities.DUCK, 0xe4d6a5, 0xe9ad36, new Item.Properties()));

  public static DeferredHolder<Item, TokenItem.SpellTokenItem> SPELL_ACID_CLOUD = ITEMS.register("acid_cloud", () -> spell(ModSpells.ACID_CLOUD));
  public static DeferredHolder<Item, TokenItem.SpellTokenItem> SPELL_AQUA_BUBBLE = ITEMS.register("aqua_bubble", () -> spell(ModSpells.AQUA_BUBBLE));
  public static DeferredHolder<Item, TokenItem.SpellTokenItem> SPELL_SUMMON_UNDEAD = ITEMS.register("summon_undead", () -> spell(ModSpells.SUMMON_UNDEAD));
  public static DeferredHolder<Item, TokenItem.SpellTokenItem> SPELL_DANDELION_WINDS = ITEMS.register("dandelion_winds", () -> spell(ModSpells.DANDELION_WINDS));
  public static DeferredHolder<Item, TokenItem.SpellTokenItem> SPELL_DECAY = ITEMS.register("decay", () -> spell(ModSpells.DECAY));
  public static DeferredHolder<Item, TokenItem.SpellTokenItem> SPELL_DESATURATE = ITEMS.register("desaturate", () -> spell(ModSpells.DESATURATE));
  public static DeferredHolder<Item, TokenItem.SpellTokenItem> SPELL_DISARM = ITEMS.register("disarm", () -> spell(ModSpells.DISARM));
  public static DeferredHolder<Item, TokenItem.SpellTokenItem> SPELL_EXTENSION = ITEMS.register("extension", () -> spell(ModSpells.EXTENSION));
  public static DeferredHolder<Item, TokenItem.SpellTokenItem> SPELL_SYLVAN_LIGHT = ITEMS.register("sylvan_light", () -> spell(ModSpells.SYLVAN_LIGHT));
  public static DeferredHolder<Item, TokenItem.SpellTokenItem> SPELL_GEAS = ITEMS.register("geas", () -> spell(ModSpells.GEAS));
  public static DeferredHolder<Item, TokenItem.SpellTokenItem> SPELL_GROWTH_INFUSION = ITEMS.register("growth_infusion", () -> spell(ModSpells.GROWTH_INFUSION));
  public static DeferredHolder<Item, TokenItem.SpellTokenItem> SPELL_HARVEST = ITEMS.register("harvest", () -> spell(ModSpells.HARVEST));
  public static DeferredHolder<Item, TokenItem.SpellTokenItem> SPELL_JAUNT = ITEMS.register("jaunt", () -> spell(ModSpells.JAUNT));
  public static DeferredHolder<Item, TokenItem.SpellTokenItem> SPELL_LIFE_DRAIN = ITEMS.register("life_drain", () -> spell(ModSpells.LIFE_DRAIN));
  // Light drifter
  public static DeferredHolder<Item, TokenItem.SpellTokenItem> SPELL_LIGHT_DRIFTER = ITEMS.register("light_drifter", () -> spell(ModSpells.LIGHT_DRIFTER));
  public static DeferredHolder<Item, TokenItem.SpellTokenItem> SPELL_MAGNETISM = ITEMS.register("magnetism", () -> spell(ModSpells.MAGNETISM));
  public static DeferredHolder<Item, TokenItem.SpellTokenItem> SPELL_NONDETECTION = ITEMS.register("nondetection", () -> spell(ModSpells.NONDETECTION));
  public static DeferredHolder<Item, TokenItem.SpellTokenItem> SPELL_PETAL_SHELL = ITEMS.register("petal_shell", () -> spell(ModSpells.PETAL_SHELL));
  public static DeferredHolder<Item, TokenItem.SpellTokenItem> SPELL_RADIANCE = ITEMS.register("radiance", () -> spell(ModSpells.RADIANCE));
  public static DeferredHolder<Item, TokenItem.SpellTokenItem> SPELL_RAMPANT_GROWTH = ITEMS.register("rampant_growth", () -> spell(ModSpells.RAMPANT_GROWTH));
  public static DeferredHolder<Item, TokenItem.SpellTokenItem> SPELL_ROSE_THORNS = ITEMS.register("rose_thorns", () -> spell(ModSpells.ROSE_THORNS));
  public static DeferredHolder<Item, TokenItem.SpellTokenItem> SPELL_SANCTUARY = ITEMS.register("sanctuary", () -> spell(ModSpells.SANCTUARY));
  public static DeferredHolder<Item, TokenItem.SpellTokenItem> SPELL_SATURATE = ITEMS.register("saturate", () -> spell(ModSpells.SATURATE));
  public static DeferredHolder<Item, TokenItem.SpellTokenItem> SPELL_SHATTER = ITEMS.register("shatter", () -> spell(ModSpells.SHATTER));
  public static DeferredHolder<Item, TokenItem.SpellTokenItem> SPELL_SKY_SOARER = ITEMS.register("sky_soarer", () -> spell(ModSpells.SKY_SOARER));
  public static DeferredHolder<Item, TokenItem.SpellTokenItem> SPELL_STORM_CLOUD = ITEMS.register("storm_cloud", () -> spell(ModSpells.STORM_CLOUD));
  public static DeferredHolder<Item, TokenItem.SpellTokenItem> SPELL_TEMPORAL_MORASS = ITEMS.register("temporal_morass", () -> spell(ModSpells.TEMPORAL_MORASS));
  public static DeferredHolder<Item, TokenItem.SpellTokenItem> SPELL_WILDFIRE = ITEMS.register("wildfire", () -> spell(ModSpells.WILDFIRE));

  static {
    ITEMS.addAlias(RootsAPI.rl("spell_acid_cloud"), RootsAPI.rl("acid_cloud"));
    ITEMS.addAlias(RootsAPI.rl("spell_aqua_bubble"), RootsAPI.rl("aqua_bubble"));
    ITEMS.addAlias(RootsAPI.rl("spell_control_undead"), RootsAPI.rl("control_undead"));
    ITEMS.addAlias(RootsAPI.rl("spell_dandelion_winds"), RootsAPI.rl("dandelion_winds"));
    ITEMS.addAlias(RootsAPI.rl("spell_decay"), RootsAPI.rl("decay"));
    ITEMS.addAlias(RootsAPI.rl("spell_desaturate"), RootsAPI.rl("desaturate"));
    ITEMS.addAlias(RootsAPI.rl("spell_disarm"), RootsAPI.rl("disarm"));
    ITEMS.addAlias(RootsAPI.rl("spell_extension"), RootsAPI.rl("extension"));
    ITEMS.addAlias(RootsAPI.rl("spell_fey_light"), RootsAPI.rl("sylvan_light"));
    ITEMS.addAlias(RootsAPI.rl("spell_geas"), RootsAPI.rl("geas"));
    ITEMS.addAlias(RootsAPI.rl("spell_growth_infusion"), RootsAPI.rl("growth_infusion"));
    ITEMS.addAlias(RootsAPI.rl("spell_harvest"), RootsAPI.rl("harvest"));
    ITEMS.addAlias(RootsAPI.rl("spell_jaunt"), RootsAPI.rl("jaunt"));
    ITEMS.addAlias(RootsAPI.rl("spell_life_drain"), RootsAPI.rl("life_drain"));
    ITEMS.addAlias(RootsAPI.rl("spell_light_drifter"), RootsAPI.rl("light_drifter"));
    ITEMS.addAlias(RootsAPI.rl("spell_magnetism"), RootsAPI.rl("magnetism"));
    ITEMS.addAlias(RootsAPI.rl("spell_nondetection"), RootsAPI.rl("nondetection"));
    ITEMS.addAlias(RootsAPI.rl("spell_petal_shell"), RootsAPI.rl("petal_shell"));
    ITEMS.addAlias(RootsAPI.rl("spell_radiance"), RootsAPI.rl("radiance"));
    ITEMS.addAlias(RootsAPI.rl("spell_rampant_growth"), RootsAPI.rl("rampant_growth"));
    ITEMS.addAlias(RootsAPI.rl("spell_rose_thorns"), RootsAPI.rl("rose_thorns"));
    ITEMS.addAlias(RootsAPI.rl("spell_sanctuary"), RootsAPI.rl("sanctuary"));
    ITEMS.addAlias(RootsAPI.rl("spell_saturate"), RootsAPI.rl("saturate"));
    ITEMS.addAlias(RootsAPI.rl("spell_shatter"), RootsAPI.rl("shatter"));
    ITEMS.addAlias(RootsAPI.rl("spell_sky_soarer"), RootsAPI.rl("sky_soarer"));
    ITEMS.addAlias(RootsAPI.rl("spell_storm_cloud"), RootsAPI.rl("storm_cloud"));
    ITEMS.addAlias(RootsAPI.rl("spell_temporal_morass"), RootsAPI.rl("temporal_morass"));
    ITEMS.addAlias(RootsAPI.rl("spell_wildfire"), RootsAPI.rl("wildfire"));
    ITEMS.addAlias(RootsAPI.rl("time_stop"), RootsAPI.rl("temporal_morass"));
    ITEMS.addAlias(RootsAPI.rl("fey_light"), RootsAPI.rl("sylvan_light"));
    ITEMS.addAlias(RootsAPI.rl("spell_time_stop"), RootsAPI.rl("temporal_morass"));
    ITEMS.addAlias(RootsAPI.rl("spell_fey_light"), RootsAPI.rl("sylvan_light"));
    ITEMS.addAlias(RootsAPI.rl("control_undead"), RootsAPI.rl("summon_undead"));
    ITEMS.addAlias(RootsAPI.rl("spell_control_undead"), RootsAPI.rl("summon_undead"));
  }

  public static DeferredHolder<Item, TokenItem.RitualTokenItem> RITUAL_ANIMAL_HARVEST = ITEMS.register("animal_harvest", () -> ritual(ModRituals.ANIMAL_HARVEST));
  public static DeferredHolder<Item, TokenItem.RitualTokenItem> RITUAL_AUGMENTATION = ITEMS.register("augmentation", () -> ritual(ModRituals.AUGMENTATION));
  public static DeferredHolder<Item, TokenItem.RitualTokenItem> RITUAL_BLOOMING = ITEMS.register("blooming", () -> ritual(ModRituals.BLOOMING));
  public static DeferredHolder<Item, TokenItem.RitualTokenItem> RITUAL_CRAFTING = ITEMS.register("crafting", () -> ritual(ModRituals.CRAFTING));
  public static DeferredHolder<Item, TokenItem.RitualTokenItem> RITUAL_FIRE_STORM = ITEMS.register("fire_storm", () -> ritual(ModRituals.FIRE_STORM));
  public static DeferredHolder<Item, TokenItem.RitualTokenItem> RITUAL_FROST_LANDS = ITEMS.register("frost_lands", () -> ritual(ModRituals.FROST_LANDS));
  public static DeferredHolder<Item, TokenItem.RitualTokenItem> RITUAL_GATHERING = ITEMS.register("gathering", () -> ritual(ModRituals.GATHERING));
  public static DeferredHolder<Item, TokenItem.RitualTokenItem> RITUAL_GERMINATION = ITEMS.register("germination", () -> ritual(ModRituals.GERMINATION));
  public static DeferredHolder<Item, TokenItem.RitualTokenItem> RITUAL_GROVE_SUPPLICATION = ITEMS.register("grove_supplication", () -> ritual(ModRituals.GROVE_SUPPLICATION));
  public static DeferredHolder<Item, TokenItem.RitualTokenItem> RITUAL_HEALING_AURA = ITEMS.register("healing_aura", () -> ritual(ModRituals.HEALING_AURA));
  public static DeferredHolder<Item, TokenItem.RitualTokenItem> RITUAL_HEAVY_STORMS = ITEMS.register("heavy_storms", () -> ritual(ModRituals.HEAVY_STORMS));
  public static DeferredHolder<Item, TokenItem.RitualTokenItem> RITUAL_OVERGROWTH = ITEMS.register("overgrowth", () -> ritual(ModRituals.OVERGROWTH));
  public static DeferredHolder<Item, TokenItem.RitualTokenItem> RITUAL_PROTECTION = ITEMS.register("protection", () -> ritual(ModRituals.PROTECTION));
  public static DeferredHolder<Item, TokenItem.RitualTokenItem> RITUAL_PURITY = ITEMS.register("purity", () -> ritual(ModRituals.PURITY));
  public static DeferredHolder<Item, TokenItem.RitualTokenItem> RITUAL_SPREADING_FOREST = ITEMS.register("spreading_forest", () -> ritual(ModRituals.SPREADING_FOREST));
  public static DeferredHolder<Item, TokenItem.RitualTokenItem> RITUAL_SUMMON_CREATURES = ITEMS.register("summon_creatures", () -> ritual(ModRituals.SUMMON_CREATURES));
  public static DeferredHolder<Item, TokenItem.RitualTokenItem> RITUAL_WARDING = ITEMS.register("warding", () -> ritual(ModRituals.WARDING));
  public static DeferredHolder<Item, TokenItem.RitualTokenItem> RITUAL_WILDROOT_GROWTH = ITEMS.register("wildroot_growth", () -> ritual(ModRituals.WILDROOT_GROWTH));
  public static DeferredHolder<Item, TokenItem.RitualTokenItem> RITUAL_WINDWALL = ITEMS.register("windwall", () -> ritual(ModRituals.WINDWALL));

  static {
    ITEMS.addAlias(RootsAPI.rl("ritual_animal_harvest"), RootsAPI.rl("animal_harvest"));
    ITEMS.addAlias(RootsAPI.rl("ritual_augmentation"), RootsAPI.rl("augmentation"));
    ITEMS.addAlias(RootsAPI.rl("ritual_blooming"), RootsAPI.rl("blooming"));
    ITEMS.addAlias(RootsAPI.rl("ritual_crafting"), RootsAPI.rl("crafting"));
    ITEMS.addAlias(RootsAPI.rl("ritual_fire_storm"), RootsAPI.rl("fire_storm"));
    ITEMS.addAlias(RootsAPI.rl("ritual_frost_lands"), RootsAPI.rl("frost_lands"));
    ITEMS.addAlias(RootsAPI.rl("ritual_gathering"), RootsAPI.rl("gathering"));
    ITEMS.addAlias(RootsAPI.rl("ritual_germination"), RootsAPI.rl("germination"));
    ITEMS.addAlias(RootsAPI.rl("ritual_grove_supplication"), RootsAPI.rl("grove_supplication"));
    ITEMS.addAlias(RootsAPI.rl("ritual_healing_aura"), RootsAPI.rl("healing_aura"));
    ITEMS.addAlias(RootsAPI.rl("ritual_heavy_storms"), RootsAPI.rl("heavy_storms"));
    ITEMS.addAlias(RootsAPI.rl("ritual_overgrowth"), RootsAPI.rl("overgrowth"));
    ITEMS.addAlias(RootsAPI.rl("ritual_protection"), RootsAPI.rl("protection"));
    ITEMS.addAlias(RootsAPI.rl("ritual_purity"), RootsAPI.rl("purity"));
    ITEMS.addAlias(RootsAPI.rl("ritual_spreading_forest"), RootsAPI.rl("spreading_forest"));
    ITEMS.addAlias(RootsAPI.rl("ritual_summon_creatures"), RootsAPI.rl("summon_creatures"));
    ITEMS.addAlias(RootsAPI.rl("ritual_warding"), RootsAPI.rl("warding"));
    ITEMS.addAlias(RootsAPI.rl("ritual_wildroot_growth"), RootsAPI.rl("wildroot_growth"));
    ITEMS.addAlias(RootsAPI.rl("ritual_windwall"), RootsAPI.rl("windwall"));
  }

  public static DeferredHolder<Item, TokenItem.GroveTokenItem> GROVE_ELEMENTAL = ITEMS.register("elemental", () -> grove(ModGroves.ELEMENTAL));
  public static DeferredHolder<Item, TokenItem.GroveTokenItem> GROVE_FAIRY = ITEMS.register("fairy", () -> grove(ModGroves.FAIRY));
  public static DeferredHolder<Item, TokenItem.GroveTokenItem> GROVE_FUNGAL = ITEMS.register("fungal", () -> grove(ModGroves.FUNGAL));
  public static DeferredHolder<Item, TokenItem.GroveTokenItem> GROVE_PRIMAL = ITEMS.register("primal", () -> grove(ModGroves.PRIMAL));
  public static DeferredHolder<Item, TokenItem.GroveTokenItem> GROVE_SPROUTING = ITEMS.register("sprouting", () -> grove(ModGroves.SPROUTING));
  public static DeferredHolder<Item, TokenItem.GroveTokenItem> GROVE_TWILIGHT = ITEMS.register("twilight", () -> grove(ModGroves.TWILIGHT));
  public static DeferredHolder<Item, TokenItem.GroveTokenItem> GROVE_WILD = ITEMS.register("wild", () -> grove(ModGroves.WILD));


  private static TokenItem.SpellTokenItem spell(Holder<Spell> spell) {
    return new TokenItem.SpellTokenItem(spell.getKey(), new Item.Properties().stacksTo(1));
  }

  private static TokenItem.RitualTokenItem ritual(Holder<Ritual> ritual) {
    return new TokenItem.RitualTokenItem(ritual.getKey(), new Item.Properties().stacksTo(1));
  }

  private static TokenItem.GroveTokenItem grove(Holder<Grove> grove) {
    return new TokenItem.GroveTokenItem(grove.getKey(), new Item.Properties().stacksTo(1));
  }

  public static void register(IEventBus bus) {
    ARMOR.register(bus);
    ITEMS.register(bus);
  }
}
