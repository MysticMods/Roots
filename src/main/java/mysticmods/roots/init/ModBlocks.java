package mysticmods.roots.init;

import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.nullness.NonNullUnaryOperator;
import mysticmods.roots.RootsTags;
import mysticmods.roots.blocks.*;
import mysticmods.roots.blocks.crops.ElementalCropBlock;
import mysticmods.roots.blocks.crops.ThreeStageCropBlock;
import mysticmods.roots.blocks.crops.WaterElementalCropBlock;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.Tags;
import noobanidus.libs.noobutil.block.BaseBlocks;
import noobanidus.libs.noobutil.data.BlockstateGenerator;
import noobanidus.libs.noobutil.data.ItemModelGenerator;

import static mysticmods.roots.Roots.REGISTRATE;
import static noobanidus.libs.noobutil.block.BaseBlocks.SeededCropsBlock;

public class ModBlocks {
  public static class Decoration {
    public static class RunedObsidian {
      public static NonNullUnaryOperator<AbstractBlock.Properties> RUNED_PROPERTIES = r -> AbstractBlock.Properties.copy(Blocks.OBSIDIAN);

      public static BlockEntry<RunedObsidianBlocks.Block> RUNED_OBSIDIAN = REGISTRATE.block("runed_obsidian", RunedObsidianBlocks.Block::new)
          .properties(RUNED_PROPERTIES)
          .item()
          .model(ItemModelGenerator::itemModel)
          .build()
          .tag(BlockTags.DRAGON_IMMUNE, BlockTags.WITHER_IMMUNE, RootsTags.Blocks.RUNED_OBSIDIAN)
          .register();

      public static BlockEntry<RunedObsidianBlocks.Button> RUNED_BUTTON = REGISTRATE.block("runed_button", RunedObsidianBlocks.Button::new)
          .properties(RUNED_PROPERTIES)
          .tag(BlockTags.DRAGON_IMMUNE, BlockTags.WITHER_IMMUNE)
          .register();

      public static BlockEntry<RunedObsidianBlocks.PressurePlate> RUNED_PRESSURE_PLATE = REGISTRATE.block("runed_pressure_plate", (p) -> new RunedObsidianBlocks.PressurePlate(PressurePlateBlock.Sensitivity.MOBS, p))
          .properties(RUNED_PROPERTIES)
          .tag(BlockTags.DRAGON_IMMUNE, BlockTags.WITHER_IMMUNE)
          .register();

      public static BlockEntry<RunedObsidianBlocks.Slab> RUNED_SLAB = REGISTRATE.block("runed_slab", RunedObsidianBlocks.Slab::new)
          .properties(RUNED_PROPERTIES)
          .tag(BlockTags.DRAGON_IMMUNE, BlockTags.WITHER_IMMUNE)
          .register();

      public static BlockEntry<RunedObsidianBlocks.Stairs> RUNED_STAIRS = REGISTRATE.block("runed_stairs", RunedObsidianBlocks.Stairs::new)
          .properties(RUNED_PROPERTIES)
          .tag(BlockTags.DRAGON_IMMUNE, BlockTags.WITHER_IMMUNE)
          .register();

      public static BlockEntry<RunedObsidianBlocks.Wall> RUNED_WALL = REGISTRATE.block("runed_wall", RunedObsidianBlocks.Wall::new)
          .properties(RUNED_PROPERTIES)
          .tag(BlockTags.DRAGON_IMMUNE, BlockTags.WITHER_IMMUNE)
          .register();

      public static BlockEntry<RunedObsidianBlocks.NarrowPost> RUNED_NARROW_POST = REGISTRATE.block("runed_narrow_post", RunedObsidianBlocks.NarrowPost::new)
          .properties(RUNED_PROPERTIES)
          .tag(BlockTags.DRAGON_IMMUNE, BlockTags.WITHER_IMMUNE)
          .register();

      public static BlockEntry<RunedObsidianBlocks.WidePost> RUNED_WIDE_POST = REGISTRATE.block("runed_wide_post", RunedObsidianBlocks.WidePost::new)
          .properties(RUNED_PROPERTIES)
          .tag(BlockTags.DRAGON_IMMUNE, BlockTags.WITHER_IMMUNE)
          .register();
    }

    public static class RunedObsidianBrick {
      public static NonNullUnaryOperator<AbstractBlock.Properties> RUNED_BRICK_PROPERTIES = r -> AbstractBlock.Properties.copy(Blocks.OBSIDIAN);

      public static BlockEntry<RunedObsidianBlocks.Block> RUNED_OBSIDIAN_BRICK = REGISTRATE.block("runed_obsidian_brick", RunedObsidianBlocks.Block::new)
          .properties(RUNED_BRICK_PROPERTIES)
          .tag(BlockTags.DRAGON_IMMUNE, BlockTags.WITHER_IMMUNE, RootsTags.Blocks.RUNED_OBSIDIAN_BRICKS)
          .register();

      public static BlockEntry<RunedObsidianBlocks.Block> CHISELED_RUNED_OBSIDIAN = REGISTRATE.block("chiseled_runed_obsidian", RunedObsidianBlocks.Block::new)
          .properties(RUNED_BRICK_PROPERTIES)
          .tag(BlockTags.DRAGON_IMMUNE, BlockTags.WITHER_IMMUNE, RootsTags.Blocks.RUNED_OBSIDIAN_BRICKS)
          .register();

      public static BlockEntry<RunedObsidianBlocks.Button> RUNED_BRICK_BUTTON = REGISTRATE.block("runed_obsidian_brick_button", RunedObsidianBlocks.Button::new)
          .properties(RUNED_BRICK_PROPERTIES)
          .tag(BlockTags.DRAGON_IMMUNE, BlockTags.WITHER_IMMUNE)
          .register();

      public static BlockEntry<RunedObsidianBlocks.PressurePlate> RUNED_BRICK_PRESSURE_PLATE = REGISTRATE.block("runed_obsidian_pressure_plate", (p) -> new RunedObsidianBlocks.PressurePlate(PressurePlateBlock.Sensitivity.MOBS, p))
          .properties(RUNED_BRICK_PROPERTIES)
          .tag(BlockTags.DRAGON_IMMUNE, BlockTags.WITHER_IMMUNE)
          .register();

      public static BlockEntry<RunedObsidianBlocks.Slab> RUNED_BRICK_SLAB = REGISTRATE.block("runed_obsidian_brick_slab", RunedObsidianBlocks.Slab::new)
          .properties(RUNED_BRICK_PROPERTIES)
          .tag(BlockTags.DRAGON_IMMUNE, BlockTags.WITHER_IMMUNE)
          .register();

      public static BlockEntry<RunedObsidianBlocks.Stairs> RUNED_BRICK_STAIRS = REGISTRATE.block("runed_obsidian_brick_stairs", RunedObsidianBlocks.Stairs::new)
          .properties(RUNED_BRICK_PROPERTIES)
          .tag(BlockTags.DRAGON_IMMUNE, BlockTags.WITHER_IMMUNE)
          .register();

      public static BlockEntry<RunedObsidianBlocks.Wall> RUNED_BRICK_WALL = REGISTRATE.block("runed_obsidian_brick_wall", RunedObsidianBlocks.Wall::new)
          .properties(RUNED_BRICK_PROPERTIES)
          .tag(BlockTags.DRAGON_IMMUNE, BlockTags.WITHER_IMMUNE)
          .register();

      public static BlockEntry<RunedObsidianBlocks.NarrowPost> RUNED_BRICK_NARROW_POST = REGISTRATE.block("runed_obsidian_brick_narrow_post", RunedObsidianBlocks.NarrowPost::new)
          .properties(RUNED_BRICK_PROPERTIES)
          .tag(BlockTags.DRAGON_IMMUNE, BlockTags.WITHER_IMMUNE)
          .register();

      public static BlockEntry<RunedObsidianBlocks.WidePost> RUNED_BRICK_WIDE_POST = REGISTRATE.block("runed_obsidian_brick_wide_post", RunedObsidianBlocks.WidePost::new)
          .properties(RUNED_BRICK_PROPERTIES)
          .tag(BlockTags.DRAGON_IMMUNE, BlockTags.WITHER_IMMUNE)
          .register();
    }

    public static class RunedObsidianBrickAlt {
      public static NonNullUnaryOperator<AbstractBlock.Properties> RUNED_BRICK_PROPERTIES = r -> AbstractBlock.Properties.copy(Blocks.OBSIDIAN);

      public static BlockEntry<RunedObsidianBlocks.Block> RUNED_OBSIDIAN_BRICK = REGISTRATE.block("runed_obsidian_brick_alt", RunedObsidianBlocks.Block::new)
          .properties(RUNED_BRICK_PROPERTIES)
          .tag(BlockTags.DRAGON_IMMUNE, BlockTags.WITHER_IMMUNE, RootsTags.Blocks.RUNED_OBSIDIAN_BRICKS)
          .register();

      public static BlockEntry<RunedObsidianBlocks.Button> RUNED_BRICK_BUTTON = REGISTRATE.block("runed_obsidian_brick_button_alt", RunedObsidianBlocks.Button::new)
          .properties(RUNED_BRICK_PROPERTIES)
          .tag(BlockTags.DRAGON_IMMUNE, BlockTags.WITHER_IMMUNE)
          .register();

      public static BlockEntry<RunedObsidianBlocks.PressurePlate> RUNED_BRICK_PRESSURE_PLATE = REGISTRATE.block("runed_obsidian_pressure_plate_alt", (p) -> new RunedObsidianBlocks.PressurePlate(PressurePlateBlock.Sensitivity.MOBS, p))
          .properties(RUNED_BRICK_PROPERTIES)
          .tag(BlockTags.DRAGON_IMMUNE, BlockTags.WITHER_IMMUNE)
          .register();

      public static BlockEntry<RunedObsidianBlocks.Slab> RUNED_BRICK_SLAB = REGISTRATE.block("runed_obsidian_brick_slab_alt", RunedObsidianBlocks.Slab::new)
          .properties(RUNED_BRICK_PROPERTIES)
          .tag(BlockTags.DRAGON_IMMUNE, BlockTags.WITHER_IMMUNE)
          .register();

      public static BlockEntry<RunedObsidianBlocks.Stairs> RUNED_BRICK_STAIRS = REGISTRATE.block("runed_obsidian_brick_stairs_alt", RunedObsidianBlocks.Stairs::new)
          .properties(RUNED_BRICK_PROPERTIES)
          .tag(BlockTags.DRAGON_IMMUNE, BlockTags.WITHER_IMMUNE)
          .register();

      public static BlockEntry<RunedObsidianBlocks.Wall> RUNED_BRICK_WALL = REGISTRATE.block("runed_obsidian_brick_wall_alt", RunedObsidianBlocks.Wall::new)
          .properties(RUNED_BRICK_PROPERTIES)
          .tag(BlockTags.DRAGON_IMMUNE, BlockTags.WITHER_IMMUNE)
          .register();

      public static BlockEntry<RunedObsidianBlocks.NarrowPost> RUNED_BRICK_NARROW_POST = REGISTRATE.block("runed_obsidian_brick_narrow_post_alt", RunedObsidianBlocks.NarrowPost::new)
          .properties(RUNED_BRICK_PROPERTIES)
          .tag(BlockTags.DRAGON_IMMUNE, BlockTags.WITHER_IMMUNE)
          .register();

      public static BlockEntry<RunedObsidianBlocks.WidePost> RUNED_BRICK_WIDE_POST = REGISTRATE.block("runed_obsidian_brick_wide_post_alt", RunedObsidianBlocks.WidePost::new)
          .properties(RUNED_BRICK_PROPERTIES)
          .tag(BlockTags.DRAGON_IMMUNE, BlockTags.WITHER_IMMUNE)
          .register();
    }

    public static class Runestone {
      public static NonNullUnaryOperator<AbstractBlock.Properties> RUNESTONE_PROPERTIES = r -> AbstractBlock.Properties.copy(Blocks.STONE);

      public static BlockEntry<Block> RUNESTONE = REGISTRATE.block("runestone", Block::new)
          .properties(RUNESTONE_PROPERTIES)
          .tag(RootsTags.Blocks.RUNESTONE)
          .register();

      public static BlockEntry<BaseBlocks.StoneButtonBlock> RUNESTONE_BUTTON = REGISTRATE.block("runestone_button", BaseBlocks.StoneButtonBlock::new)
          .properties(RUNESTONE_PROPERTIES)
          .register();

      public static BlockEntry<BaseBlocks.PressurePlateBlock> RUNESTONE_PRESSURE_PLATE = REGISTRATE.block("runestone_pressure_plate", (p) -> new BaseBlocks.PressurePlateBlock(PressurePlateBlock.Sensitivity.MOBS, p))
          .properties(RUNESTONE_PROPERTIES)
          .register();

      public static BlockEntry<SlabBlock> RUNESTONE_SLAB = REGISTRATE.block("runestone_slab", SlabBlock::new)
          .properties(RUNESTONE_PROPERTIES)
          .register();

      public static BlockEntry<StairsBlock> RUNESTONE_STAIRS = REGISTRATE.block("runestone_stairs", (p) -> new StairsBlock(() -> RUNESTONE.getDefaultState(), p))
          .properties(RUNESTONE_PROPERTIES)
          .register();

      public static BlockEntry<WallBlock> RUNESTONE_WALL = REGISTRATE.block("runestone_wall", WallBlock::new)
          .properties(RUNESTONE_PROPERTIES)
          .register();

      public static BlockEntry<BaseBlocks.NarrowPostBlock> RUNESTONE_NARROW_POST = REGISTRATE.block("runestone_narrow_post", BaseBlocks.NarrowPostBlock::new)
          .properties(RUNESTONE_PROPERTIES)
          .register();

      public static BlockEntry<BaseBlocks.WidePostBlock> RUNESTONE_WIDE_POST = REGISTRATE.block("runestone_wide_post", BaseBlocks.WidePostBlock::new)
          .properties(RUNESTONE_PROPERTIES)
          .register();
    }

    public static class RunestoneBrick {
      public static NonNullUnaryOperator<AbstractBlock.Properties> RUNESTONE_BRICK_PROPERTIES = r -> AbstractBlock.Properties.copy(Blocks.STONE_BRICKS);

      public static BlockEntry<Block> RUNESTONE_BRICK = REGISTRATE.block("runestone_brick", Block::new)
          .properties(RUNESTONE_BRICK_PROPERTIES)
          .tag(RootsTags.Blocks.RUNESTONE_BRICKS)
          .register();

      public static BlockEntry<Block> CHISELED_RUNESTONE_BRICK = REGISTRATE.block("chiseled_runestone_brick", Block::new)
          .properties(RUNESTONE_BRICK_PROPERTIES)
          .tag(RootsTags.Blocks.RUNESTONE_BRICKS)
          .register();

      public static BlockEntry<BaseBlocks.StoneButtonBlock> RUNESTONE_BRICK_BUTTON = REGISTRATE.block("runestone_brick_button", BaseBlocks.StoneButtonBlock::new)
          .properties(RUNESTONE_BRICK_PROPERTIES)
          .register();

      public static BlockEntry<BaseBlocks.PressurePlateBlock> RUNESTONE_BRICK_PRESSURE_PLATE = REGISTRATE.block("runestone_brick_pressure_plate", (p) -> new BaseBlocks.PressurePlateBlock(PressurePlateBlock.Sensitivity.MOBS, p))
          .properties(RUNESTONE_BRICK_PROPERTIES)
          .register();

      public static BlockEntry<SlabBlock> RUNESTONE_BRICK_SLAB = REGISTRATE.block("runestone_brick_slab", SlabBlock::new)
          .properties(RUNESTONE_BRICK_PROPERTIES)
          .register();

      public static BlockEntry<StairsBlock> RUNESTONE_BRICK_STAIRS = REGISTRATE.block("runestone_brick_stairs", (p) -> new StairsBlock(() -> RUNESTONE_BRICK.getDefaultState(), p))
          .properties(RUNESTONE_BRICK_PROPERTIES)
          .register();

      public static BlockEntry<WallBlock> RUNESTONE_BRICK_WALL = REGISTRATE.block("runestone_brick_wall", WallBlock::new)
          .properties(RUNESTONE_BRICK_PROPERTIES)
          .register();

      public static BlockEntry<BaseBlocks.NarrowPostBlock> RUNESTONE_BRICK_NARROW_POST = REGISTRATE.block("runestone_brick_narrow_post", BaseBlocks.NarrowPostBlock::new)
          .properties(RUNESTONE_BRICK_PROPERTIES)
          .register();

      public static BlockEntry<BaseBlocks.WidePostBlock> RUNESTONE_BRICK_WIDE_POST = REGISTRATE.block("runestone_brick_wide_post", BaseBlocks.WidePostBlock::new)
          .properties(RUNESTONE_BRICK_PROPERTIES)
          .register();
    }

    public static class RunestoneBrickAlt {
      public static NonNullUnaryOperator<AbstractBlock.Properties> RUNESTONE_BRICK_PROPERTIES = r -> AbstractBlock.Properties.copy(Blocks.STONE_BRICKS);

      public static BlockEntry<Block> RUNESTONE_BRICK_ALT = REGISTRATE.block("runestone_brick_alt", Block::new)
          .properties(RUNESTONE_BRICK_PROPERTIES)
          .tag(RootsTags.Blocks.RUNESTONE_BRICKS)
          .register();

      public static BlockEntry<BaseBlocks.StoneButtonBlock> RUNESTONE_BRICK_ALT_BUTTON = REGISTRATE.block("runestone_brick_alt_button", BaseBlocks.StoneButtonBlock::new)
          .properties(RUNESTONE_BRICK_PROPERTIES)
          .register();

      public static BlockEntry<BaseBlocks.PressurePlateBlock> RUNESTONE_BRICK_ALT_PRESSURE_PLATE = REGISTRATE.block("runestone_brick_alt_pressure_plate", (p) -> new BaseBlocks.PressurePlateBlock(PressurePlateBlock.Sensitivity.MOBS, p))
          .properties(RUNESTONE_BRICK_PROPERTIES)
          .register();

      public static BlockEntry<SlabBlock> RUNESTONE_BRICK_ALT_SLAB = REGISTRATE.block("runestone_brick_alt_slab", SlabBlock::new)
          .properties(RUNESTONE_BRICK_PROPERTIES)
          .register();

      public static BlockEntry<StairsBlock> RUNESTONE_BRICK_ALT_STAIRS = REGISTRATE.block("runestone_brick_alt_stairs", (p) -> new StairsBlock(() -> RUNESTONE_BRICK_ALT.getDefaultState(), p))
          .properties(RUNESTONE_BRICK_PROPERTIES)
          .register();

      public static BlockEntry<WallBlock> RUNESTONE_BRICK_ALT_WALL = REGISTRATE.block("runestone_brick_alt_wall", WallBlock::new)
          .properties(RUNESTONE_BRICK_PROPERTIES)
          .register();

      public static BlockEntry<BaseBlocks.NarrowPostBlock> RUNESTONE_BRICK_ALT_NARROW_POST = REGISTRATE.block("runestone_brick_alt_narrow_post", BaseBlocks.NarrowPostBlock::new)
          .properties(RUNESTONE_BRICK_PROPERTIES)
          .register();

      public static BlockEntry<BaseBlocks.WidePostBlock> RUNESTONE_BRICK_ALT_WIDE_POST = REGISTRATE.block("runestone_brick_alt_wide_post", BaseBlocks.WidePostBlock::new)
          .properties(RUNESTONE_BRICK_PROPERTIES)
          .register();
    }

    public static class RunedWood {
      public static NonNullUnaryOperator<AbstractBlock.Properties> RUNED_LOG_PROPERTIES = r -> AbstractBlock.Properties.copy(Blocks.OAK_LOG);

      public static BlockEntry<Block> RUNED_ACACIA = REGISTRATE.block("runed_acacia", Block::new)
          .properties(RUNED_LOG_PROPERTIES)
          .tag(BlockTags.LOGS_THAT_BURN, BlockTags.LOGS, BlockTags.ACACIA_LOGS, RootsTags.Blocks.RUNED_LOGS)
          .register();

      public static BlockEntry<Block> RUNED_DARK_OAK = REGISTRATE.block("runed_dark_oak", Block::new)
          .properties(RUNED_LOG_PROPERTIES)
          .tag(BlockTags.LOGS_THAT_BURN, BlockTags.LOGS, BlockTags.DARK_OAK_LOGS, RootsTags.Blocks.RUNED_LOGS)
          .register();

      public static BlockEntry<Block> RUNED_OAK = REGISTRATE.block("runed_oak", Block::new)
          .properties(RUNED_LOG_PROPERTIES)
          .tag(BlockTags.LOGS_THAT_BURN, BlockTags.LOGS, BlockTags.OAK_LOGS, RootsTags.Blocks.RUNED_LOGS)
          .register();

      public static BlockEntry<Block> RUNED_BIRCH = REGISTRATE.block("runed_birch", Block::new)
          .properties(RUNED_LOG_PROPERTIES)
          .tag(BlockTags.LOGS_THAT_BURN, BlockTags.LOGS, BlockTags.BIRCH_LOGS, RootsTags.Blocks.RUNED_LOGS)
          .register();

      public static BlockEntry<Block> RUNED_JUNGLE = REGISTRATE.block("runed_jungle", Block::new)
          .properties(RUNED_LOG_PROPERTIES)
          .tag(BlockTags.LOGS_THAT_BURN, BlockTags.LOGS, BlockTags.JUNGLE_LOGS, RootsTags.Blocks.RUNED_LOGS)
          .register();

      public static BlockEntry<Block> RUNED_SPRUCE = REGISTRATE.block("runed_spruce", Block::new)
          .properties(RUNED_LOG_PROPERTIES)
          .tag(BlockTags.LOGS_THAT_BURN, BlockTags.LOGS, BlockTags.SPRUCE_LOGS, RootsTags.Blocks.RUNED_LOGS)
          .register();

      public static BlockEntry<Block> RUNED_WILDWOOD = REGISTRATE.block("runed_wildwood", Block::new)
          .properties(RUNED_LOG_PROPERTIES)
          .tag(BlockTags.LOGS_THAT_BURN, BlockTags.LOGS, RootsTags.Blocks.WILDWOOD_LOGS, RootsTags.Blocks.RUNED_LOGS)
          .register();
    }
  }

  public static class Crops {
    public static BlockEntry<ThreeStageCropBlock> WILDROOT_CROP = REGISTRATE.block("wildroot_crop", (p) -> new ThreeStageCropBlock(p, ModItems.Herbs.WILDROOT))
        .properties(o -> AbstractBlock.Properties.copy(Blocks.WHEAT))
        .blockstate(BlockstateGenerator::cropBlockstate)
        .tag(RootsTags.Blocks.WILDROOT_CROP)
        .register();

    public static BlockEntry<ElementalCropBlock> CLOUD_BERRY_CROP = REGISTRATE.block("cloud_berry_crop", (p) -> new ElementalCropBlock(p, ModItems.Herbs.CLOUD_BERRY))
        .properties(o -> AbstractBlock.Properties.copy(Blocks.WHEAT))
        .blockstate(BlockstateGenerator::cropBlockstate)
        .tag(RootsTags.Blocks.CLOUD_BERRY_CROP)
        .register();

    public static BlockEntry<WaterElementalCropBlock> DEWGONIA_CROP = REGISTRATE.block("dewgonia_crop", (p) -> new WaterElementalCropBlock(p, ModItems.Herbs.DEWGONIA))
        .properties(o -> AbstractBlock.Properties.copy(Blocks.WHEAT))
        .blockstate(BlockstateGenerator::cropBlockstate)
        .tag(RootsTags.Blocks.DEWGONIA_CROP)
        .register();

    public static BlockEntry<ElementalCropBlock> INFERNAL_BULB_CROP = REGISTRATE.block("infernal_bulb_crop", (p) -> new ElementalCropBlock(p, ModItems.Herbs.INFERNAL_BULB))
        .properties(o -> AbstractBlock.Properties.copy(Blocks.WHEAT))
        .blockstate(BlockstateGenerator::crossBlockstate)
        .tag(RootsTags.Blocks.INFERNAL_BULB_CROP)
        .register();

    public static BlockEntry<ElementalCropBlock> STALICRIPE_CROP = REGISTRATE.block("stalicripe_crop", (p) -> new ElementalCropBlock(p, ModItems.Herbs.STALICRIPE))
        .properties(o -> AbstractBlock.Properties.copy(Blocks.WHEAT))
        .blockstate(BlockstateGenerator::cropBlockstate)
        .tag(RootsTags.Blocks.STALICRIPE_CROP)
        .register();

    public static BlockEntry<SeededCropsBlock> MOONGLOW_LEAF_CROP = REGISTRATE.block("moonglow_leaf_crop", (p) -> new SeededCropsBlock(p, ModItems.Seeds.MOONGLOW_LEAF_SEEDS))
        .properties(o -> AbstractBlock.Properties.copy(Blocks.WHEAT))
        .blockstate(BlockstateGenerator::cropBlockstate)
        .tag(RootsTags.Blocks.MOONGLOW_LEAF_CROP)
        .register();
    public static BlockEntry<SeededCropsBlock> PERESKIA_CROP = REGISTRATE.block("pereskia_crop", (p) -> new SeededCropsBlock(p, ModItems.Seeds.PERESKIA_BULB))
        .properties(o -> AbstractBlock.Properties.copy(Blocks.WHEAT))
        .blockstate(BlockstateGenerator::crossBlockstate)
        .tag(RootsTags.Blocks.PERESKIA_CROP)
        .register();
    public static BlockEntry<ThreeStageCropBlock> SPIRIT_HERB_CROP = REGISTRATE.block("spirit_herb_crop", (p) -> new ThreeStageCropBlock(p, ModItems.Seeds.SPIRIT_HERB_SEEDS))
        .properties(o -> AbstractBlock.Properties.copy(Blocks.WHEAT))
        .blockstate(BlockstateGenerator::cropBlockstate)
        .tag(RootsTags.Blocks.SPIRIT_HERB_CROP)
        .register();
    public static BlockEntry<SeededCropsBlock> WILDEWHEET_CROP = REGISTRATE.block("wildewheet_crop", (p) -> new SeededCropsBlock(p, ModItems.Seeds.WILDEWHEET_SEEDS))
        .properties(o -> AbstractBlock.Properties.copy(Blocks.WHEAT))
        .blockstate(BlockstateGenerator::cropBlockstate)
        .tag(RootsTags.Blocks.WILDEWHEET_CROP)
        .register();
  }

  public static class Soils {
    public static NonNullUnaryOperator<AbstractBlock.Properties> SOIL_PROPERTIES = r -> AbstractBlock.Properties.copy(Blocks.DIRT);

    public static BlockEntry<Block> AQUEOUS_SOIL = REGISTRATE.block("aqueous_soil", Block::new)
        .properties(SOIL_PROPERTIES)
        .tag(RootsTags.Blocks.SOILS)
        .register();

    public static BlockEntry<Block> CAELIC_SOIL = REGISTRATE.block("caelic_soil", Block::new)
        .properties(SOIL_PROPERTIES)
        .tag(RootsTags.Blocks.SOILS)
        .register();

    public static BlockEntry<Block> ELEMENTAL_SOIL = REGISTRATE.block("elemental_soil", Block::new)
        .properties(SOIL_PROPERTIES)
        .tag(RootsTags.Blocks.SOILS)
        .register();

    public static BlockEntry<Block> MAGMATIC_SOIL = REGISTRATE.block("magmatic_soil", Block::new)
        .properties(SOIL_PROPERTIES)
        .tag(RootsTags.Blocks.SOILS)
        .register();

    public static BlockEntry<Block> TERRAN_SOIL = REGISTRATE.block("terran_soil", Block::new)
        .properties(SOIL_PROPERTIES)
        .tag(RootsTags.Blocks.SOILS)
        .register();
  }

  public static class Wildwood {
    public static NonNullUnaryOperator<AbstractBlock.Properties> WILDWOOD_LOG_PROPERTIES = r -> AbstractBlock.Properties.copy(Blocks.OAK_LOG);
    public static NonNullUnaryOperator<AbstractBlock.Properties> WILDWOOD_LEAVES_PROPERTIES = r -> AbstractBlock.Properties.copy(Blocks.OAK_LEAVES);
    public static NonNullUnaryOperator<AbstractBlock.Properties> WILDWOOD_PLANKS_PROPERTIES = r -> AbstractBlock.Properties.copy(Blocks.OAK_PLANKS);
    public static NonNullUnaryOperator<AbstractBlock.Properties> WILDWOOD_DECORATION_PROPERTIES = r -> AbstractBlock.Properties.of(Material.DECORATION);

    public static BlockEntry<Block> WILDWOOD_LOG = REGISTRATE.block("wildwood_log", Block::new)
        .properties(WILDWOOD_LOG_PROPERTIES)
        .tag(BlockTags.LOGS_THAT_BURN, BlockTags.LOGS, RootsTags.Blocks.WILDWOOD_LOGS)
        .register();

    public static BlockEntry<Block> WILDWOOD_WOOD = REGISTRATE.block("wildwood_wood", Block::new)
        .properties(WILDWOOD_LOG_PROPERTIES)
        .tag(BlockTags.LOGS_THAT_BURN, BlockTags.LOGS, RootsTags.Blocks.WILDWOOD_LOGS)
        .register();

    public static BlockEntry<Block> STRIPPED_WILDWOOD_LOG = REGISTRATE.block("stripped_wildwood_log", Block::new)
        .properties(WILDWOOD_LOG_PROPERTIES)
        .tag(BlockTags.LOGS_THAT_BURN, BlockTags.LOGS, RootsTags.Blocks.WILDWOOD_LOGS)
        .register();

    public static BlockEntry<Block> STRIPPED_WILDWOOD_WOOD = REGISTRATE.block("stripped_wildwood_wood", Block::new)
        .properties(WILDWOOD_LOG_PROPERTIES)
        .tag(BlockTags.LOGS_THAT_BURN, BlockTags.LOGS, RootsTags.Blocks.WILDWOOD_LOGS)
        .register();

    public static BlockEntry<Block> WILDWOOD_LEAVES = REGISTRATE.block("wildwood_leaves", Block::new)
        .properties(WILDWOOD_LEAVES_PROPERTIES)
        .tag(BlockTags.LEAVES)
        .register();

    public static BlockEntry<Block> WILDROOT_RUNE = REGISTRATE.block("wildroot_rune", Block::new)
        .properties(WILDWOOD_LOG_PROPERTIES)
        .tag(BlockTags.LOGS_THAT_BURN, BlockTags.LOGS, RootsTags.Blocks.WILDWOOD_LOGS)
        .register();

    public static BlockEntry<Block> WILDWOOD_PLANKS = REGISTRATE.block("wildwood_planks", Block::new)
        .properties(WILDWOOD_PLANKS_PROPERTIES)
        .tag(BlockTags.PLANKS)
        .register();

    public static BlockEntry<BaseBlocks.WoodButtonBlock> WILDWOOD_BUTTON = REGISTRATE.block("wildwood_button", BaseBlocks.WoodButtonBlock::new)
        .properties(WILDWOOD_DECORATION_PROPERTIES)
        .tag(BlockTags.WOODEN_BUTTONS, BlockTags.BUTTONS)
        .register();

    public static BlockEntry<BaseBlocks.PressurePlateBlock> WILDWOOD_PRESSURE_PLATE = REGISTRATE.block("wildwood_pressure_plate", (p) -> new BaseBlocks.PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, p))
        .properties(WILDWOOD_DECORATION_PROPERTIES)
        .tag(BlockTags.WALL_POST_OVERRIDE, BlockTags.PRESSURE_PLATES, BlockTags.WOODEN_PRESSURE_PLATES)
        .register();

    public static BlockEntry<SlabBlock> WILDWOOD_SLAB = REGISTRATE.block("wildwood_slab", SlabBlock::new)
        .properties(WILDWOOD_PLANKS_PROPERTIES)
        .tag(BlockTags.WOODEN_SLABS, BlockTags.SLABS)
        .register();

    public static BlockEntry<StairsBlock> WILDWOOD_STAIRS = REGISTRATE.block("wildwood_stairs", (p) -> new StairsBlock(() -> WILDWOOD_PLANKS.getDefaultState(), p))
        .properties(WILDWOOD_PLANKS_PROPERTIES)
        .tag(BlockTags.STAIRS, BlockTags.WOODEN_STAIRS)
        .register();

    public static BlockEntry<FenceBlock> WILDWOOD_FENCE = REGISTRATE.block("wildwood_fence", FenceBlock::new)
        .properties(WILDWOOD_PLANKS_PROPERTIES)
        .tag(BlockTags.FENCES, BlockTags.WOODEN_FENCES, Tags.Blocks.FENCES, Tags.Blocks.FENCES_WOODEN)
        .register();

    public static BlockEntry<FenceGateBlock> WILDWOOD_FENCE_GATE = REGISTRATE.block("wildwood_fence_Gate", FenceGateBlock::new)
        .properties(WILDWOOD_PLANKS_PROPERTIES)
        .tag(BlockTags.FENCE_GATES, BlockTags.UNSTABLE_BOTTOM_CENTER, Tags.Blocks.FENCE_GATES, Tags.Blocks.FENCE_GATES_WOODEN)
        .register();

    public static BlockEntry<LadderBlock> WILDWOOD_LADDER = REGISTRATE.block("willdwood_ladder", LadderBlock::new)
        .properties(WILDWOOD_DECORATION_PROPERTIES)
        .tag(BlockTags.CLIMBABLE)
        .register();

    public static BlockEntry<BaseBlocks.TrapDoorBlock> WILDWOOD_TRAPDOOR = REGISTRATE.block("wildwood_trapdoor", BaseBlocks.TrapDoorBlock::new)
        .properties(WILDWOOD_PLANKS_PROPERTIES)
        .tag(BlockTags.TRAPDOORS, BlockTags.WOODEN_TRAPDOORS)
        .register();

    public static BlockEntry<BaseBlocks.DoorBlock> WILDWOOD_DOOR = REGISTRATE.block("wildwood_door", BaseBlocks.DoorBlock::new)
        .properties(WILDWOOD_PLANKS_PROPERTIES)
        .tag(BlockTags.DOORS, BlockTags.WOODEN_DOORS)
        .register();

    // todo: wildwood sign, wildwood boat, wildwood sapling?
  }

  public static BlockEntry<FeyLightBlock> FEY_LIGHT = REGISTRATE.block("fey_light", FeyLightBlock::new)
      .properties(o -> AbstractBlock.Properties.copy(Blocks.TORCH))
      .register();
  public static BlockEntry<CatalystPlateBlock> CATALYST_PLATE = REGISTRATE.block("catalyst_plate", CatalystPlateBlock::new).register();
  public static BlockEntry<CatalystPlateBlock> REINFORCED_CATALYST_PLATE = REGISTRATE.block("reinforced_catalyst_plate", CatalystPlateBlock::new).register();
  public static BlockEntry<FeyCrafterBlock> FEY_CRAFTER = REGISTRATE.block("fey_crafter", FeyCrafterBlock::new).register();
  public static BlockEntry<GroveStoneBlock> GROVE_STONE = REGISTRATE.block("grove_stone", GroveStoneBlock::new).register();
  public static BlockEntry<ImbuerBlock> IMBUER = REGISTRATE.block("imbuer", ImbuerBlock::new).register();
  public static BlockEntry<ImposerBlock> IMPOSER = REGISTRATE.block("imposer", ImposerBlock::new).register();
  public static BlockEntry<IncensePlateBlock> INCENSE_BURNER = REGISTRATE.block("incense_burner", IncensePlateBlock::new).register();
  public static BlockEntry<MortarBlock> MORTAR = REGISTRATE.block("mortar", MortarBlock::new).register();
  public static BlockEntry<PyreBlock> PYRE = REGISTRATE.block("pyre", PyreBlock::new).register();
  public static BlockEntry<PyreBlock> DECORATIVE_PYRE = REGISTRATE.block("decorative_pyre", PyreBlock::new).register();
  public static BlockEntry<PyreBlock> REINFORCED_PYRE = REGISTRATE.block("reinforced_pyre", PyreBlock::new).register();
  public static BlockEntry<UnendingBowlBlock> UNENDING_BOWL_BLOCK = REGISTRATE.block("unending_bowl", UnendingBowlBlock::new).register();

  public static void load() {
  }
}
