package mysticmods.roots.init;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.RootsTags;
import mysticmods.roots.block.*;
import mysticmods.roots.block.crop.*;
import mysticmods.roots.worldgen.trees.RootsTreeGrowers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBlocks {
  private static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(RootsAPI.MODID);

  public static final DeferredHolder<Block, WaterloggedBlock> THATCH = BLOCKS.register("thatch", () -> new WaterloggedBlock(BlockBehaviour.Properties.of()
      .mapColor(MapColor.COLOR_YELLOW).instrument(NoteBlockInstrument.BANJO).strength(0.5f).sound(SoundType.GRASS)));

  static {
    BLOCKS.addAlias(RootsAPI.rl("sheared_thatch"), RootsAPI.rl("thatch"));
  }

  private static final BlockBehaviour.Properties RUNESTONE_PROPERTIES = BlockBehaviour.Properties.of()
      .strength(2.0f, 6.0f);

  public static final DeferredHolder<Block, Block> RUNESTONE = BLOCKS.register("runestone", () -> new Block(RUNESTONE_PROPERTIES));
  public static final DeferredHolder<Block, Block> MOSSY_RUNESTONE = BLOCKS.register("mossy_runestone", () -> new Block(RUNESTONE_PROPERTIES));
  public static final DeferredHolder<Block, Block> CHISELED_RUNESTONE = BLOCKS.register("chiseled_runestone", () -> new Block(RUNESTONE_PROPERTIES));
  public static final DeferredHolder<Block, Block> RUNESTONE_BRICK = BLOCKS.register("runestone_brick", () -> new Block(RUNESTONE_PROPERTIES));
  public static final DeferredHolder<Block, Block> RUNESTONE_TILE = BLOCKS.register("runestone_tile", () -> new Block(RUNESTONE_PROPERTIES));

  private static final BlockBehaviour.Properties RUNED_OBSIDIAN_PROPERTIES = BlockBehaviour.Properties.of()
      .mapColor(DyeColor.BLACK).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops()
      .strength(50.0f, 1200.0f).pushReaction(PushReaction.BLOCK);
  public static final DeferredHolder<Block, Block> RUNED_OBSIDIAN = BLOCKS.register("runed_obsidian", () -> new Block(RUNED_OBSIDIAN_PROPERTIES));
  public static final DeferredHolder<Block, Block> CHISELED_RUNED_OBSIDIAN = BLOCKS.register("chiseled_runed_obsidian", () -> new Block(RUNED_OBSIDIAN_PROPERTIES));
  public static final DeferredHolder<Block, Block> RUNED_BRICK = BLOCKS.register("runed_brick", () -> new Block(RUNED_OBSIDIAN_PROPERTIES));
  public static final DeferredHolder<Block, Block> RUNED_TILE = BLOCKS.register("runed_tile", () -> new Block(RUNED_OBSIDIAN_PROPERTIES));
  // TODO: Fix the experience
  public static final DeferredHolder<Block, DropExperienceBlock> SILVER_ORE = BLOCKS.register("silver_ore", () -> new DropExperienceBlock(ConstantInt.of(0), BlockBehaviour.Properties.of()
      .mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops()
      .strength(3.0f, 3.0f)));
  public static final DeferredHolder<Block, DropExperienceBlock> DEEPSLATE_SILVER_ORE = BLOCKS.register("deepslate_silver_ore", () -> new DropExperienceBlock(ConstantInt.of(0), BlockBehaviour.Properties.of()
      .mapColor(MapColor.DEEPSLATE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops()
      .strength(4.5f, 3.0f).sound(SoundType.DEEPSLATE)));
  public static final DeferredHolder<Block, DropExperienceBlock> GRANITE_QUARTZ_ORE = BLOCKS.register("granite_quartz_ore", () -> new DropExperienceBlock(UniformInt.of(2, 5), BlockBehaviour.Properties.of()
      .strength(3.0f, 3.0f)));
  public static final DeferredHolder<Block, Block> RAW_SILVER_BLOCK = BLOCKS.register("raw_silver_block", () -> new Block(BlockBehaviour.Properties.of()
      .strength(5.0f, 6.0f)));
  public static final DeferredHolder<Block, Block> SILVER_BLOCK = BLOCKS.register("silver_block", () -> new Block(BlockBehaviour.Properties.of()
      .strength(3.0f, 6.0f).sound(SoundType.METAL)));
  public static final DeferredHolder<Block, RotatedPillarBlock> WILDWOOD_LOG = BLOCKS.register("wildwood_log", () -> new RotatedPillarBlock(BlockBehaviour.Properties.of()
      .mapColor((state -> state.getValue(RotatedPillarBlock.AXIS) == Direction.Axis.Y ? MapColor.WOOD : MapColor.COLOR_BROWN))
      .instrument(NoteBlockInstrument.BASS).strength(2.0f).sound(SoundType.WOOD).ignitedByLava()));
  public static final DeferredHolder<Block, RotatedPillarBlock> STRIPPED_WILDWOOD_LOG = BLOCKS.register("stripped_wildwood_log", () -> new RotatedPillarBlock(BlockBehaviour.Properties.of()
      .mapColor(MapColor.WOOD).instrument(NoteBlockInstrument.BASS).strength(2.0f).sound(SoundType.WOOD)
      .ignitedByLava()));
  public static final DeferredHolder<Block, RotatedPillarBlock> WILDWOOD_WOOD = BLOCKS.register("wildwood_wood", () -> new RotatedPillarBlock(BlockBehaviour.Properties.of()
      .mapColor(MapColor.PODZOL).instrument(NoteBlockInstrument.BASS).strength(2.0f).sound(SoundType.WOOD)
      .ignitedByLava()));
  public static final DeferredHolder<Block, RotatedPillarBlock> STRIPPED_WILDWOOD_WOOD = BLOCKS.register("stripped_wildwood_wood", () -> new RotatedPillarBlock(BlockBehaviour.Properties.of()
      .mapColor(MapColor.WOOD).instrument(NoteBlockInstrument.BASS).strength(2.0f).sound(SoundType.WOOD)
      .ignitedByLava()));
  private static final BlockBehaviour.Properties WILDWOOD_PLANKS_PROPERTIES = BlockBehaviour.Properties.of()
      .mapColor(MapColor.WOOD).instrument(NoteBlockInstrument.BASS).strength(2.0f, 3.0f).sound(SoundType.WOOD)
      .ignitedByLava();

  public static final DeferredHolder<Block, Block> WILDWOOD_PLANKS = BLOCKS.register("wildwood_planks", () -> new Block(WILDWOOD_PLANKS_PROPERTIES));
  public static final DeferredHolder<Block, SaplingBlock> WILDWOOD_SAPLING = BLOCKS.register("wildwood_sapling", () -> new SaplingBlock(RootsTreeGrowers.WILDWOOD_TREE, BlockBehaviour.Properties.of()
      .mapColor(MapColor.PLANT).noCollission().randomTicks().instabreak().sound(SoundType.GRASS)
      .pushReaction(PushReaction.DESTROY)));

  // TODO: Whatever happened to plant types?
  public static final DeferredHolder<Block, PetrifiedFlowerBlock> STONEPETAL = BLOCKS.register("stonepetal", () -> new PetrifiedFlowerBlock(BlockBehaviour.Properties.of()
      .mapColor(MapColor.COLOR_GRAY).noCollission().instabreak().sound(SoundType.GRASS)
      .offsetType(BlockBehaviour.OffsetType.XZ).pushReaction(PushReaction.DESTROY)));
  public static final DeferredHolder<Block, LeavesBlock> WILDWOOD_LEAVES = BLOCKS.register("wildwood_leaves", () -> new LeavesBlock(BlockBehaviour.Properties.of()
      .mapColor(MapColor.PLANT).strength(0.2f).randomTicks().sound(SoundType.GRASS).noOcclusion()
      .isSuffocating(ModBlocks::never).isViewBlocking(ModBlocks::never).ignitedByLava()
      .pushReaction(PushReaction.DESTROY).isRedstoneConductor(ModBlocks::never)));
  public static final DeferredHolder<Block, RotatedPillarBlock> RUNED_WILDWOOD_LOG = BLOCKS.register("runed_wildwood_log", () -> new RotatedPillarBlock(BlockBehaviour.Properties.of()
      .mapColor((state -> state.getValue(RotatedPillarBlock.AXIS) == Direction.Axis.Y ? MapColor.WOOD : MapColor.COLOR_BROWN))
      .instrument(NoteBlockInstrument.BASS).strength(2.0f).sound(SoundType.WOOD).ignitedByLava()));
  public static final DeferredHolder<Block, RotatedPillarBlock> RUNED_SPRUCE_LOG = BLOCKS.register("runed_spruce_log", () -> new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SPRUCE_LOG)));
  public static final DeferredHolder<Block, RotatedPillarBlock> RUNED_JUNGLE_LOG = BLOCKS.register("runed_jungle_log", () -> new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.JUNGLE_LOG)));
  public static final DeferredHolder<Block, RotatedPillarBlock> RUNED_BIRCH_LOG = BLOCKS.register("runed_birch_log", () -> new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BIRCH_LOG)));
  public static final DeferredHolder<Block, RotatedPillarBlock> RUNED_OAK_LOG = BLOCKS.register("runed_oak_log", () -> new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LOG)));
  public static final DeferredHolder<Block, RotatedPillarBlock> RUNED_DARK_OAK_LOG = BLOCKS.register("runed_dark_oak_log", () -> new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.DARK_OAK_LOG)));
  public static final DeferredHolder<Block, RotatedPillarBlock> RUNED_ACACIA_LOG = BLOCKS.register("runed_acacia_log", () -> new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.ACACIA_LOG)));
  public static final DeferredHolder<Block, RotatedPillarBlock> RUNED_MANGROVE_LOG = BLOCKS.register("runed_mangrove_log", () -> new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.MANGROVE_LOG)));
  public static final DeferredHolder<Block, RotatedPillarBlock> RUNED_WARPED_STEM = BLOCKS.register("runed_warped_stem", () -> new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.WARPED_STEM)));
  public static final DeferredHolder<Block, RotatedPillarBlock> RUNED_CRIMSON_STEM = BLOCKS.register("runed_crimson_stem", () -> new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.CRIMSON_STEM)));
  public static final DeferredHolder<Block, StairBlock> RUNESTONE_STAIRS = BLOCKS.register("runestone_stairs", () -> new StairBlock(ModBlocks.RUNESTONE.get()
      .defaultBlockState(), RUNESTONE_PROPERTIES));
  public static final DeferredHolder<Block, StairBlock> MOSSY_RUNESTONE_STAIRS = BLOCKS.register("mossy_runestone_stairs", () -> new StairBlock(ModBlocks.MOSSY_RUNESTONE.get()
      .defaultBlockState(), RUNESTONE_PROPERTIES));
  public static final DeferredHolder<Block, StairBlock> RUNESTONE_BRICK_STAIRS = BLOCKS.register("runestone_brick_stairs", () -> new StairBlock(ModBlocks.RUNESTONE_BRICK.get()
      .defaultBlockState(), RUNESTONE_PROPERTIES));
  public static final DeferredHolder<Block, StairBlock> RUNESTONE_TILE_STAIRS = BLOCKS.register("runestone_tile_stairs", () -> new StairBlock(ModBlocks.RUNESTONE_TILE.get()
      .defaultBlockState(), RUNESTONE_PROPERTIES));
  public static final DeferredHolder<Block, StairBlock> RUNED_STAIRS = BLOCKS.register("runed_stairs", () -> new StairBlock(ModBlocks.RUNED_OBSIDIAN.get()
      .defaultBlockState(), RUNED_OBSIDIAN_PROPERTIES));
  public static final DeferredHolder<Block, StairBlock> RUNED_BRICK_STAIRS = BLOCKS.register("runed_brick_stairs", () -> new StairBlock(ModBlocks.RUNED_BRICK.get()
      .defaultBlockState(), RUNED_OBSIDIAN_PROPERTIES));
  public static final DeferredHolder<Block, StairBlock> RUNED_TILE_STAIRS = BLOCKS.register("runed_tile_stairs", () -> new StairBlock(ModBlocks.RUNED_TILE.get()
      .defaultBlockState(), RUNED_OBSIDIAN_PROPERTIES));
  public static final DeferredHolder<Block, StairBlock> WILDWOOD_STAIRS = BLOCKS.register("wildwood_stairs", () -> new StairBlock(ModBlocks.WILDWOOD_PLANKS.get()
      .defaultBlockState(), WILDWOOD_PLANKS_PROPERTIES));
  public static final DeferredHolder<Block, SlabBlock> RUNESTONE_SLAB = BLOCKS.register("runestone_slab", () -> new SlabBlock(RUNESTONE_PROPERTIES));
  public static final DeferredHolder<Block, SlabBlock> MOSSY_RUNESTONE_SLAB = BLOCKS.register("mossy_runestone_slab", () -> new SlabBlock(RUNESTONE_PROPERTIES));
  public static final DeferredHolder<Block, SlabBlock> RUNESTONE_BRICK_SLAB = BLOCKS.register("runestone_brick_slab", () -> new SlabBlock(RUNESTONE_PROPERTIES));
  public static final DeferredHolder<Block, SlabBlock> RUNESTONE_TILE_SLAB = BLOCKS.register("runestone_tile_slab", () -> new SlabBlock(RUNESTONE_PROPERTIES));
  public static final DeferredHolder<Block, SlabBlock> RUNED_SLAB = BLOCKS.register("runed_slab", () -> new SlabBlock(RUNED_OBSIDIAN_PROPERTIES));
  public static final DeferredHolder<Block, SlabBlock> RUNED_BRICK_SLAB = BLOCKS.register("runed_brick_slab", () -> new SlabBlock(RUNED_OBSIDIAN_PROPERTIES));
  public static final DeferredHolder<Block, SlabBlock> RUNED_TILE_SLAB = BLOCKS.register("runed_tile_slab", () -> new SlabBlock(RUNED_OBSIDIAN_PROPERTIES));
  public static final DeferredHolder<Block, SlabBlock> WILDWOOD_SLAB = BLOCKS.register("wildwood_slab", () -> new SlabBlock(WILDWOOD_PLANKS_PROPERTIES));
  public static final DeferredHolder<Block, FenceBlock> WILDWOOD_FENCE = BLOCKS.register("wildwood_fence", () -> new FenceBlock(WILDWOOD_PLANKS_PROPERTIES));

  private static final BlockBehaviour.Properties RUNESTONE_BUTTON_PROPERTIES = BlockBehaviour.Properties.of()
      .noCollission().strength(0.5F).sound(SoundType.STONE).pushReaction(PushReaction.DESTROY);

  public static final DeferredHolder<Block, ButtonBlock> RUNESTONE_BUTTON = BLOCKS.register("runestone_button", () -> new ButtonBlock(ModTypes.RUNESTONE_SET, 20, RUNESTONE_BUTTON_PROPERTIES));
  public static final DeferredHolder<Block, ButtonBlock> RUNESTONE_BRICK_BUTTON = BLOCKS.register("runestone_brick_button", () -> new ButtonBlock(ModTypes.RUNESTONE_SET, 20, RUNESTONE_BUTTON_PROPERTIES));
  public static final DeferredHolder<Block, ButtonBlock> RUNESTONE_TILE_BUTTON = BLOCKS.register("runestone_tile_button", () -> new ButtonBlock(ModTypes.RUNESTONE_SET, 20, RUNESTONE_BUTTON_PROPERTIES));
  public static final DeferredHolder<Block, ButtonBlock> MOSSY_RUNESTONE_BUTTON = BLOCKS.register("mossy_runestone_button", () -> new ButtonBlock(ModTypes.RUNESTONE_SET, 20, RUNESTONE_BUTTON_PROPERTIES));
  public static final DeferredHolder<Block, ButtonBlock> RUNED_BUTTON = BLOCKS.register("runed_button", () -> new ButtonBlock(ModTypes.RUNED_OBSIDIAN_SET, 20, RUNESTONE_BUTTON_PROPERTIES));
  public static final DeferredHolder<Block, ButtonBlock> RUNED_BRICK_BUTTON = BLOCKS.register("runed_brick_button", () -> new ButtonBlock(ModTypes.RUNED_OBSIDIAN_SET, 20, RUNESTONE_BUTTON_PROPERTIES));
  public static final DeferredHolder<Block, ButtonBlock> RUNED_TILE_BUTTON = BLOCKS.register("runed_tile_button", () -> new ButtonBlock(ModTypes.RUNED_OBSIDIAN_SET, 20, RUNESTONE_BUTTON_PROPERTIES));
  public static final DeferredHolder<Block, ButtonBlock> WILDWOOD_BUTTON = BLOCKS.register("wildwood_button", () -> new ButtonBlock(ModTypes.WILDWOOD_SET, 20, WILDWOOD_PLANKS_PROPERTIES));
  public static final DeferredHolder<Block, PressurePlateBlock> RUNESTONE_PRESSURE_PLATE = BLOCKS.register("runestone_pressure_plate", () -> new PressurePlateBlock(ModTypes.RUNESTONE_SET, RUNESTONE_PROPERTIES));
  public static final DeferredHolder<Block, PressurePlateBlock> RUNESTONE_BRICK_PRESSURE_PLATE = BLOCKS.register("runestone_brick_pressure_plate", () -> new PressurePlateBlock(ModTypes.RUNESTONE_SET, RUNESTONE_PROPERTIES));
  public static final DeferredHolder<Block, PressurePlateBlock> RUNESTONE_TILE_PRESSURE_PLATE = BLOCKS.register("runestone_tile_pressure_plate", () -> new PressurePlateBlock(ModTypes.RUNESTONE_SET, RUNESTONE_PROPERTIES));
  public static final DeferredHolder<Block, PressurePlateBlock> MOSSY_RUNESTONE_PRESSURE_PLATE = BLOCKS.register("mossy_runestone_pressure_plate", () -> new PressurePlateBlock(ModTypes.RUNESTONE_SET, RUNESTONE_PROPERTIES));
  public static final DeferredHolder<Block, PressurePlateBlock> RUNED_PRESSURE_PLATE = BLOCKS.register("runed_pressure_plate", () -> new PressurePlateBlock(ModTypes.RUNED_OBSIDIAN_SET, RUNED_OBSIDIAN_PROPERTIES));
  public static final DeferredHolder<Block, PressurePlateBlock> RUNED_BRICK_PRESSURE_PLATE = BLOCKS.register("runed_brick_pressure_plate", () -> new PressurePlateBlock(ModTypes.RUNED_OBSIDIAN_SET, RUNED_OBSIDIAN_PROPERTIES));
  public static final DeferredHolder<Block, PressurePlateBlock> RUNED_TILE_PRESSURE_PLATE = BLOCKS.register("runed_tile_pressure_plate", () -> new PressurePlateBlock(ModTypes.RUNED_OBSIDIAN_SET, RUNED_OBSIDIAN_PROPERTIES));
  public static final DeferredHolder<Block, PressurePlateBlock> WILDWOOD_PRESSURE_PLATE = BLOCKS.register("wildwood_pressure_plate", () -> new PressurePlateBlock(ModTypes.WILDWOOD_SET, BlockBehaviour.Properties.of()
      .mapColor(MapColor.WOOD).forceSolidOn().instrument(NoteBlockInstrument.BASS).noCollission().strength(0.5f)
      .ignitedByLava().pushReaction(PushReaction.DESTROY)));
  public static final DeferredHolder<Block, DoorBlock> WILDWOOD_DOOR = BLOCKS.register("wildwood_door", () -> new DoorBlock(ModTypes.WILDWOOD_SET, BlockBehaviour.Properties.of()
      .mapColor(MapColor.WOOD).instrument(NoteBlockInstrument.BASS).strength(3.0f).noOcclusion().ignitedByLava()
      .pushReaction(PushReaction.DESTROY)));
  public static final DeferredHolder<Block, TrapDoorBlock> WILDWOOD_TRAPDOOR = BLOCKS.register("wildwood_trapdoor", () -> new TrapDoorBlock(ModTypes.WILDWOOD_SET, BlockBehaviour.Properties.of()
      .mapColor(MapColor.WOOD).instrument(NoteBlockInstrument.BASS).strength(3.0f).noOcclusion()
      .isValidSpawn(ModBlocks::never).ignitedByLava()));

  public static final DeferredHolder<Block, LadderBlock> WILDWOOD_LADDER = BLOCKS.register("wildwood_ladder", () -> new LadderBlock(BlockBehaviour.Properties.of()
      .forceSolidOff().strength(0.4F).sound(SoundType.LADDER).noOcclusion().pushReaction(PushReaction.DESTROY)));
  public static final DeferredHolder<Block, FenceGateBlock> WILDWOOD_GATE = BLOCKS.register("wildwood_gate", () -> new FenceGateBlock(ModTypes.WILDWOOD_WOOD_TYPE, BlockBehaviour.Properties.of()
      .forceSolidOn().instrument(NoteBlockInstrument.BASS).strength(2.0f, 3.0f).ignitedByLava()));
  public static final DeferredHolder<Block, WallBlock> RUNESTONE_WALL = BLOCKS.register("runestone_wall", () -> new WallBlock(RUNESTONE_PROPERTIES));
  public static final DeferredHolder<Block, WallBlock> MOSSY_RUNESTONE_WALL = BLOCKS.register("mossy_runestone_wall", () -> new WallBlock(RUNESTONE_PROPERTIES));
  public static final DeferredHolder<Block, WallBlock> RUNESTONE_BRICK_WALL = BLOCKS.register("runestone_brick_wall", () -> new WallBlock(RUNESTONE_PROPERTIES));
  public static final DeferredHolder<Block, WallBlock> RUNESTONE_TILE_WALL = BLOCKS.register("runestone_tile_wall", () -> new WallBlock(RUNESTONE_PROPERTIES));
  public static final DeferredHolder<Block, WallBlock> RUNED_WALL = BLOCKS.register("runed_wall", () -> new WallBlock(RUNED_OBSIDIAN_PROPERTIES));
  public static final DeferredHolder<Block, WallBlock> RUNED_BRICK_WALL = BLOCKS.register("runed_brick_wall", () -> new WallBlock(RUNED_OBSIDIAN_PROPERTIES));
  public static final DeferredHolder<Block, WallBlock> RUNED_TILE_WALL = BLOCKS.register("runed_tile_wall", () -> new WallBlock(RUNED_OBSIDIAN_PROPERTIES));
  public static final DeferredHolder<Block, ElementalSoilBlock> ELEMENTAL_SOIL = BLOCKS.register("elemental_soil", () -> new ElementalSoilBlock(ElementalType.DEFAULT, BlockBehaviour.Properties.of()
      .mapColor(MapColor.PODZOL).instrument(NoteBlockInstrument.BASEDRUM).strength(0.5f).sound(SoundType.GRAVEL)));
  public static final DeferredHolder<Block, ElementalSoilBlock> AQUEOUS_SOIL = BLOCKS.register("aqueous_soil", () -> new ElementalSoilBlock(ElementalType.WATER, BlockBehaviour.Properties.of()
      .mapColor(MapColor.WATER).instrument(NoteBlockInstrument.BASEDRUM).strength(0.5f).sound(SoundType.LILY_PAD)));
  public static final DeferredHolder<Block, ElementalSoilBlock> CAELIC_SOIL = BLOCKS.register("caelic_soil", () -> new ElementalSoilBlock(ElementalType.AIR, BlockBehaviour.Properties.of()
      .mapColor(MapColor.COLOR_LIGHT_GREEN).instrument(NoteBlockInstrument.BASEDRUM).strength(0.5f)
      .sound(SoundType.WOOL)));
  public static final DeferredHolder<Block, ElementalSoilBlock> MAGMATIC_SOIL = BLOCKS.register("magmatic_soil", () -> new ElementalSoilBlock(ElementalType.FIRE, BlockBehaviour.Properties.of()
      .mapColor(MapColor.COLOR_RED).instrument(NoteBlockInstrument.BASEDRUM).strength(0.5f).sound(SoundType.CANDLE)));
  public static final DeferredHolder<Block, ElementalSoilBlock> TERRAN_SOIL = BLOCKS.register("terran_soil", () -> new ElementalSoilBlock(ElementalType.EARTH, BlockBehaviour.Properties.of()
      .mapColor(MapColor.PODZOL).instrument(NoteBlockInstrument.BASEDRUM).strength(0.5f)
      .sound(SoundType.HANGING_ROOTS)));
  public static final DeferredHolder<Block, SylvanLightBlock> SYLVAN_LIGHT = BLOCKS.register("sylvan_light", () -> new SylvanLightBlock(BlockBehaviour.Properties.of()
      .instabreak().lightLevel((o) -> 15).noCollission().noLootTable().noTerrainParticles().sound(SoundType.WOOL)
      .noOcclusion()));

  static {
    BLOCKS.addAlias(RootsAPI.rl("fey_light"), RootsAPI.rl("sylvan_light"));
  }

  public static final DeferredHolder<Block, RitualPedestalBlock> RITUAL_PEDESTAL = BLOCKS.register("ritual_pedestal", () -> new RitualPedestalBlock(BlockBehaviour.Properties.of()
      .mapColor(DyeColor.BLUE).instrument(NoteBlockInstrument.BASS).strength(2.5f).sound(SoundType.STONE)
      .requiresCorrectToolForDrops()));
  public static final DeferredHolder<Block, RitualPedestalBlock> REINFORCED_RITUAL_PEDESTAL = BLOCKS.register("reinforced_ritual_pedestal", () -> new RitualPedestalBlock(BlockBehaviour.Properties.of()
      .mapColor(MapColor.COLOR_BLACK).instrument(NoteBlockInstrument.BASS).strength(2.5f, 1200f)
      .requiresCorrectToolForDrops()));
  public static final DeferredHolder<Block, GroveCrafterBlock> GROVE_CRAFTER = BLOCKS.register("grove_crafter", () -> new GroveCrafterBlock(BlockBehaviour.Properties.of()
      .mapColor(MapColor.WOOD).instrument(NoteBlockInstrument.BASEDRUM).strength(2.5f).requiresCorrectToolForDrops()));

  public static final DeferredHolder<Block, PedestalBlock.GrovePedestalBlock> GROVE_PEDESTAL = BLOCKS.register("grove_pedestal", () -> new PedestalBlock.GrovePedestalBlock(BlockBehaviour.Properties.of()
      .mapColor(MapColor.WOOD).instrument(NoteBlockInstrument.BASS).strength(1.5f).sound(SoundType.WOOD)
  ));
  public static final DeferredHolder<Block, PedestalBlock.WildwoodPedestalBlock> WILDWOOD_PEDESTAL = BLOCKS.register("wildwood_pedestal", () -> new PedestalBlock.WildwoodPedestalBlock(BlockBehaviour.Properties.of()
      .mapColor(MapColor.WOOD).instrument(NoteBlockInstrument.BASS).strength(1.5f).sound(SoundType.WOOD)
  ));
  public static final DeferredHolder<Block, PedestalBlock.GrovePedestalBlock> DISPLAY_PEDESTAL = BLOCKS.register("display_pedestal", () -> new PedestalBlock.GrovePedestalBlock(BlockBehaviour.Properties.of()
      .mapColor(MapColor.WOOD).instrument(NoteBlockInstrument.BASS).strength(1.5f).sound(SoundType.WOOD)
  ));

  public static final DeferredHolder<Block, AmplifierBlock> GROWTH_AMPLIFIER = BLOCKS.register("growth_amplifier", () -> new AmplifierBlock(BlockBehaviour.Properties.of()
      .mapColor(MapColor.WOOD).instrument(NoteBlockInstrument.BASS).strength(1.5f).sound(SoundType.WOOD)
      .requiresCorrectToolForDrops()));

  public static final DeferredHolder<Block, FairyHutBlock> RED_FAIRY_HUT = BLOCKS.register("red_fairy_hut", () -> new FairyHutBlock(BlockBehaviour.Properties.of()
      .mapColor(DyeColor.RED).instrument(NoteBlockInstrument.BASS).strength(1.5f).sound(SoundType.WOOD).lightLevel(FairyHutBlock::lightLevel)));
  public static final DeferredHolder<Block, FairyHutBlock> BROWN_FAIRY_HUT = BLOCKS.register("brown_fairy_hut", () -> new FairyHutBlock(BlockBehaviour.Properties.of()
      .mapColor(DyeColor.BROWN).instrument(NoteBlockInstrument.BASS).strength(1.5f).sound(SoundType.WOOD).lightLevel(FairyHutBlock::lightLevel)));
  public static final DeferredHolder<Block, FairyHutBlock> BAFFLECAP_FAIRY_HUT = BLOCKS.register("bafflecap_fairy_hut", () -> new FairyHutBlock(BlockBehaviour.Properties.of()
      .mapColor(DyeColor.YELLOW).instrument(NoteBlockInstrument.BASS).strength(1.5f).sound(SoundType.WOOD).lightLevel(FairyHutBlock::lightLevel)));
  public static final DeferredHolder<Block, FairyHutBlock> CRIMSON_FAIRY_HUT = BLOCKS.register("crimson_fairy_hut", () -> new FairyHutBlock(BlockBehaviour.Properties.of()
      .mapColor(DyeColor.RED).instrument(NoteBlockInstrument.BASS).strength(1.5f).sound(SoundType.WOOD).lightLevel(FairyHutBlock::lightLevel)));
  public static final DeferredHolder<Block, FairyHutBlock> WARPED_FAIRY_HUT = BLOCKS.register("warped_fairy_hut", () -> new FairyHutBlock(BlockBehaviour.Properties.of()
      .mapColor(DyeColor.BLUE).instrument(NoteBlockInstrument.BASS).strength(1.5f).sound(SoundType.WOOD).lightLevel(FairyHutBlock::lightLevel)));

  public static final DeferredHolder<Block, WildRootsBlock> WILD_ROOTS = BLOCKS.register("wild_roots", () -> new WildRootsBlock(BlockBehaviour.Properties.of()
      .mapColor(MapColor.WOOD).instrument(NoteBlockInstrument.BASS).strength(0.2f).sound(SoundType.WOOD)));
  public static final DeferredHolder<Block, CreepingGroveMossBlock> CREEPING_GROVE_MOSS = BLOCKS.register("creeping_grove_moss", () -> new CreepingGroveMossBlock(BlockBehaviour.Properties.of()
      .mapColor(MapColor.COLOR_LIGHT_GREEN).strength(0.1f).sound(SoundType.MOSS_CARPET)
      .pushReaction(PushReaction.DESTROY)));
  public static final DeferredHolder<Block, HangingGroveMossBlock> HANGING_GROVE_MOSS = BLOCKS.register("hanging_grove_moss", () -> new HangingGroveMossBlock(BlockBehaviour.Properties.of()
      .mapColor(MapColor.GRASS)
      .replaceable()
      .noCollission()
      .instabreak()
      .sound(SoundType.HANGING_ROOTS)
      .ignitedByLava()
      .pushReaction(PushReaction.DESTROY)));
  public static final DeferredHolder<Block, HugeMushroomBlock> BAFFLECAP_BLOCK = BLOCKS.register("bafflecap_block", () -> new HugeMushroomBlock(BlockBehaviour.Properties.of()
      .mapColor(MapColor.DIRT).instrument(NoteBlockInstrument.BASS).strength(0.2f).sound(SoundType.WOOD)
      .ignitedByLava()));
  public static final DeferredHolder<Block, GroveStoneBlock> PRIMAL_GROVE_STONE = BLOCKS.register("primal_grove_stone", () -> new GroveStoneBlock(ModGroves.PRIMAL, BlockBehaviour.Properties.of()
      .mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops()
      .strength(2.0f, 6.0f).forceSolidOn()));
  public static final DeferredHolder<Block, GroveStoneBlock> FAIRY_GROVE_STONE = BLOCKS.register("fairy_grove_stone", () -> new GroveStoneBlock(ModGroves.FAIRY, BlockBehaviour.Properties.of()
      .mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops()
      .strength(2.0f, 6.0f).forceSolidOn()));
  public static final DeferredHolder<Block, GroveStoneBlock> WILD_GROVE_STONE = BLOCKS.register("wild_grove_stone", () -> new GroveStoneBlock(ModGroves.WILD, BlockBehaviour.Properties.of()
      .mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops()
      .strength(2.0f, 6.0f).forceSolidOn()));
  public static final DeferredHolder<Block, GroveStoneBlock> FUNGAL_GROVE_STONE = BLOCKS.register("fungal_grove_stone", () -> new GroveStoneBlock(ModGroves.FUNGAL, BlockBehaviour.Properties.of()
      .mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops()
      .strength(2.0f, 6.0f).forceSolidOn()));
  public static final DeferredHolder<Block, GroveStoneBlock> ELEMENTAL_GROVE_STONE = BLOCKS.register("elemental_grove_stone", () -> new GroveStoneBlock(ModGroves.ELEMENTAL, BlockBehaviour.Properties.of()
      .mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops()
      .strength(2.0f, 6.0f).forceSolidOn()));
  public static final DeferredHolder<Block, GroveStoneBlock> SPROUTING_GROVE_STONE = BLOCKS.register("sprouting_grove_stone", () -> new GroveStoneBlock(ModGroves.SPROUTING, BlockBehaviour.Properties.of()
      .mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops()
      .strength(2.0f, 6.0f).forceSolidOn()));
  public static final DeferredHolder<Block, GroveStoneBlock> TWILIGHT_GROVE_STONE = BLOCKS.register("twilight_grove_stone", () -> new GroveStoneBlock(ModGroves.TWILIGHT, BlockBehaviour.Properties.of()
      .mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops()
      .strength(2.0f, 6.0f).forceSolidOn()));
  public static final DeferredHolder<Block, IncenseBurnerBlock> INCENSE_BURNER = BLOCKS.register("incense_burner", () -> new IncenseBurnerBlock(BlockBehaviour.Properties.of()
      .mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops()
      .strength(2.0f, 6.0f).forceSolidOn()));
  public static final DeferredHolder<Block, AltarBlock> STONE_ALTAR = BLOCKS.register("stone_altar", () -> new AltarBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(2.0f, 6.0f).forceSolidOn()));
  public static final DeferredHolder<Block, MortarBlock> MORTAR = BLOCKS.register("mortar", () -> new MortarBlock(BlockBehaviour.Properties.of()
      .strength(1.0f, 6.0f).forceSolidOn().dynamicShape().noOcclusion()));
  public static final DeferredHolder<Block, PyreBlock> PYRE = BLOCKS.register("pyre", () -> new PyreBlock(BlockBehaviour.Properties.of()
      .dynamicShape().forceSolidOn().noOcclusion().strength(1.5f).sound(SoundType.WOOD)
      .lightLevel(o -> o.getValue(PyreBlock.LIT) ? 15 : 0)));
  public static final DeferredHolder<Block, PyreBlock> SOUL_PYRE = BLOCKS.register("soul_pyre", () -> new PyreBlock(BlockBehaviour.Properties.of()
      .dynamicShape().forceSolidOn().noOcclusion().strength(1.5f).sound(SoundType.WOOD)
      .lightLevel(o -> o.getValue(PyreBlock.LIT) ? 2 : 0)));
  public static final DeferredHolder<Block, PyreBlock> REINFORCED_PYRE = BLOCKS.register("reinforced_pyre", () -> new PyreBlock(BlockBehaviour.Properties.of()
      .dynamicShape().forceSolidOn().noOcclusion().strength(1.5f, 1200f).sound(SoundType.STONE)
      .lightLevel(o -> o.getValue(PyreBlock.LIT) ? 15 : 0)));
  public static final DeferredHolder<Block, PyreBlock> REINFORCED_SOUL_PYRE = BLOCKS.register("reinforced_soul_pyre", () -> new PyreBlock(BlockBehaviour.Properties.of()
      .dynamicShape().forceSolidOn().noOcclusion().strength(1.5f, 1200f).sound(SoundType.STONE)
      .lightLevel(o -> o.getValue(PyreBlock.LIT) ? 2 : 0)));
  public static final DeferredHolder<Block, DecorativePyreBlock> DECORATIVE_PYRE = BLOCKS.register("decorative_pyre", () -> new DecorativePyreBlock(BlockBehaviour.Properties.of()
      .dynamicShape().forceSolidOn().noOcclusion().strength(1.5f).sound(SoundType.WOOD)
      .lightLevel(o -> 15)));
  public static final DeferredHolder<Block, DecorativePyreBlock> DECORATIVE_SOUL_PYRE = BLOCKS.register("decorative_soul_pyre", () -> new DecorativePyreBlock(BlockBehaviour.Properties.of()
      .dynamicShape().forceSolidOn().noOcclusion().strength(1.5f).sound(SoundType.WOOD)
      .lightLevel(o -> 2)));
  public static final DeferredHolder<Block, UnendingBowlBlock> UNENDING_BOWL = BLOCKS.register("unending_bowl", () -> new UnendingBowlBlock(BlockBehaviour.Properties.of()
      .strength(1.0f, 6.0f).forceSolidOn().dynamicShape().noOcclusion()));
  public static final DeferredHolder<Block, MushroomBlock> BAFFLECAP = BLOCKS.register("bafflecap", () -> new MushroomBlock(ModFeatures.CONFIGURED_HUGE_BAFFLECAP_KEY, BlockBehaviour.Properties.ofFullCopy(Blocks.BROWN_MUSHROOM)));

  private static final BlockBehaviour.Properties CROP_PROPERTIES = BlockBehaviour.Properties.of()
      .mapColor(MapColor.PLANT).noCollission().randomTicks().instabreak().sound(SoundType.CROP)
      .pushReaction(PushReaction.DESTROY);

  public static final DeferredHolder<Block, ThreeStageCropBlock> WILDROOT_CROP = BLOCKS.register("wildroot_crop", () -> new ThreeStageCropBlock(ModItems.WILDROOT, CROP_PROPERTIES));
  public static final DeferredHolder<Block, ElementalCropBlock> CLOUD_BERRY_CROP = BLOCKS.register("cloud_berry_crop", () -> new ElementalCropBlock(ModItems.CLOUD_BERRY, ElementalType.AIR, CROP_PROPERTIES));
  public static final DeferredHolder<Block, WaterElementalCropBlock> DEWGONIA_CROP = BLOCKS.register("dewgonia_crop", () -> new WaterElementalCropBlock(ModItems.DEWGONIA, ElementalType.WATER, CROP_PROPERTIES));
  public static final DeferredHolder<Block, ElementalCropBlock> INFERNO_BULB_CROP = BLOCKS.register("inferno_bulb_crop", () -> new ElementalCropBlock(ModItems.INFERNO_BULB, ElementalType.FIRE, CROP_PROPERTIES));
  public static final DeferredHolder<Block, ElementalCropBlock> STALICRIPE_CROP = BLOCKS.register("stalicripe_crop", () -> new ElementalCropBlock(ModItems.STALICRIPE, ElementalType.EARTH, CROP_PROPERTIES));
  public static final DeferredHolder<Block, BaseBlocks.SeededCropsBlock> MOONGLOW_CROP = BLOCKS.register("moonglow_crop", () -> new BaseBlocks.SeededCropsBlock(ModItems.MOONGLOW_SEEDS, CROP_PROPERTIES));
  public static final DeferredHolder<Block, BaseBlocks.SeededCropsBlock> PERESKIA_CROP = BLOCKS.register("pereskia_crop", () -> new BaseBlocks.SeededCropsBlock(ModItems.PERESKIA_BULB, CROP_PROPERTIES));
  // TODO: Pottable pereskia?
  public static final DeferredHolder<Block, FourStageCropBlock> SPIRITLEAF_CROP = BLOCKS.register("spiritleaf_crop", () -> new FourStageCropBlock(ModItems.SPIRITLEAF_SEEDS, CROP_PROPERTIES));
  public static final DeferredHolder<Block, BaseBlocks.SeededCropsBlock> WILDEWHEET_CROP = BLOCKS.register("wildewheet_crop", () -> new BaseBlocks.SeededCropsBlock(ModItems.WILDEWHEET_SEEDS, CROP_PROPERTIES));
  public static final DeferredHolder<Block, BaseBlocks.SeededCropsBlock> AUBERGINE_CROP = BLOCKS.register("aubergine_crop", () -> new BaseBlocks.SeededCropsBlock(ModItems.AUBERGINE_SEEDS, CROP_PROPERTIES));
  public static final DeferredHolder<Block, BaseBlocks.WildCropBlock> WILD_AUBERGINE = BLOCKS.register("wild_aubergine", () -> new BaseBlocks.WildCropBlock(RootsTags.Blocks.SUPPORTS_WILD_AUBERGINE, CROP_PROPERTIES));
  public static final DeferredHolder<Block, FlowerPotBlock> POTTED_BAFFLECAP = BLOCKS.register("potted_bafflecap", () -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, ModBlocks.BAFFLECAP, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SAPLING)));
  public static final DeferredHolder<Block, FlowerPotBlock> POTTED_STONEPETAL = BLOCKS.register("potted_stonepetal", () -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, ModBlocks.STONEPETAL, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SAPLING)));
  public static final DeferredHolder<Block, FlowerPotBlock> POTTED_WILDWOOD_SAPLING = BLOCKS.register("potted_wildwood_sapling", () -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, ModBlocks.WILDWOOD_SAPLING, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SAPLING)));

  private static boolean never(BlockState state, BlockGetter blockGetter, BlockPos pos) {
    return false;
  }

  private static boolean never(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, EntityType<?> entityType) {
    return false;
  }

  public static void register(IEventBus modEventBus) {
    BLOCKS.register(modEventBus);
  }
}
