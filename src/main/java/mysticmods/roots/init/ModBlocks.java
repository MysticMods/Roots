package mysticmods.roots.init;

import com.tterrag.registrate.providers.loot.RegistrateBlockLootTables;
import com.tterrag.registrate.util.DataIngredient;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.nullness.NonNullUnaryOperator;
import mysticmods.roots.Roots;
import mysticmods.roots.RootsTags;
import mysticmods.roots.blocks.RunedObsidianBlocks;
import mysticmods.roots.blocks.crops.ElementalCropBlock;
import mysticmods.roots.blocks.crops.ThreeStageCropBlock;
import mysticmods.roots.blocks.crops.WaterElementalCropBlock;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
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

      public static BlockEntry<Block> RUNED_OBSIDIAN = REGISTRATE.block("runed_obsidian", Block::new)
          .properties(RUNED_PROPERTIES)
          .item()
          .model(ItemModelGenerator::itemModel)
          .build()
          .tag(BlockTags.DRAGON_IMMUNE, BlockTags.WITHER_IMMUNE, RootsTags.Blocks.RUNED_OBSIDIAN)
          .register();

      public static BlockEntry<Block> CHISELED_RUNED_OBSIDIAN = REGISTRATE.block("chiseled_runed_obsidian", Block::new)
          .properties(RUNED_PROPERTIES)
          .item()
          .model(ItemModelGenerator::itemModel)
          .build()
          .tag(BlockTags.DRAGON_IMMUNE, BlockTags.WITHER_IMMUNE, RootsTags.Blocks.RUNED_OBSIDIAN)
          .register();

      public static BlockEntry<RunedObsidianBlocks.Button> RUNED_BUTTON = REGISTRATE.block("runed_button", RunedObsidianBlocks.Button::new)
          .properties(o -> AbstractBlock.Properties.copy(Blocks.STONE_BUTTON))
          .blockstate(BlockstateGenerator.button(RUNED_OBSIDIAN))
          .recipe((ctx, p) -> {
            p.singleItem(DataIngredient.items(RunedObsidian.RUNED_OBSIDIAN), RunedObsidian.RUNED_BUTTON, 1, 1);
            p.stonecutting(DataIngredient.items(RunedObsidian.RUNED_OBSIDIAN), RunedObsidian.RUNED_BUTTON);
          })
          .item()
          .model(ItemModelGenerator::inventoryModel)
          .tag(ItemTags.BUTTONS)
          .build()
          .tag(BlockTags.DRAGON_IMMUNE, BlockTags.WITHER_IMMUNE, BlockTags.BUTTONS)
          .register();

      public static BlockEntry<RunedObsidianBlocks.PressurePlate> RUNED_PRESSURE_PLATE = REGISTRATE.block("runed_pressure_plate", (p) -> new RunedObsidianBlocks.PressurePlate(PressurePlateBlock.Sensitivity.MOBS, p))
          .properties(o -> AbstractBlock.Properties.of(Material.STONE, MaterialColor.COLOR_BLUE).noCollission().strength(0.5f).sound(SoundType.WOOD))
          .blockstate(BlockstateGenerator.pressurePlate(RUNED_OBSIDIAN))
          .recipe((ctx, p) -> {
            ShapedRecipeBuilder.shaped(ctx.getEntry(), 1)
                .pattern("XX")
                .define('X', DataIngredient.items(RunedObsidian.RUNED_OBSIDIAN))
                .unlockedBy("has_runed_obsidian", DataIngredient.items(RunedObsidian.RUNED_OBSIDIAN).getCritereon(p))
                .save(p, p.safeId(ctx.getEntry()));
            p.stonecutting(DataIngredient.items(RunedObsidian.RUNED_OBSIDIAN), RunedObsidian.RUNED_PRESSURE_PLATE);
          })
          .item()
          .model(ItemModelGenerator::itemModel)
          .build()
          .tag(BlockTags.DRAGON_IMMUNE, BlockTags.WITHER_IMMUNE, BlockTags.STONE_PRESSURE_PLATES)
          .register();

      public static BlockEntry<RunedObsidianBlocks.Slab> RUNED_SLAB = REGISTRATE.block("runed_slab", RunedObsidianBlocks.Slab::new)
          .properties(o -> AbstractBlock.Properties.copy(Blocks.OBSIDIAN).requiresCorrectToolForDrops())
          .blockstate(BlockstateGenerator.slab(RUNED_OBSIDIAN))
          .recipe((ctx, p) -> p.slab(DataIngredient.items(RunedObsidian.RUNED_OBSIDIAN), RunedObsidian.RUNED_SLAB, null, true))
          .item()
          .model(ItemModelGenerator::itemModel)
          .tag(ItemTags.SLABS)
          .build()
          .tag(BlockTags.DRAGON_IMMUNE, BlockTags.WITHER_IMMUNE, BlockTags.SLABS)
          .loot((p, t) -> p.add(t, RegistrateBlockLootTables.droppingSlab(t)))
          .register();

      public static BlockEntry<RunedObsidianBlocks.Stairs> RUNED_STAIRS = REGISTRATE.block("runed_stairs", RunedObsidianBlocks.Stairs::new)
          .properties(RUNED_PROPERTIES)
          .blockstate(BlockstateGenerator.stairs(RUNED_OBSIDIAN))
          .recipe((ctx, p) -> p.stairs(DataIngredient.items(RunedObsidian.RUNED_OBSIDIAN), RunedObsidian.RUNED_STAIRS, null, true))
          .item()
          .model(ItemModelGenerator::itemModel)
          .tag(ItemTags.STAIRS)
          .build()
          .tag(BlockTags.DRAGON_IMMUNE, BlockTags.WITHER_IMMUNE, BlockTags.STAIRS)
          .register();

      public static BlockEntry<RunedObsidianBlocks.Wall> RUNED_WALL = REGISTRATE.block("runed_wall", RunedObsidianBlocks.Wall::new)
          .properties(RUNED_PROPERTIES)
          .blockstate(BlockstateGenerator.wall(RUNED_OBSIDIAN))
          .recipe((ctx, p) -> p.wall(DataIngredient.items(RunedObsidian.RUNED_OBSIDIAN), RunedObsidian.RUNED_WALL))
          .item()
          .model(ItemModelGenerator::inventoryModel)
          .tag(ItemTags.WALLS)
          .build()
          .tag(BlockTags.DRAGON_IMMUNE, BlockTags.WITHER_IMMUNE, BlockTags.WALLS)
          .register();

      public static BlockEntry<RunedObsidianBlocks.NarrowPost> RUNED_NARROW_POST = REGISTRATE.block("runed_narrow_post", RunedObsidianBlocks.NarrowPost::new)
          .properties(RUNED_PROPERTIES)
          .recipe((ctx, p) -> Roots.RECIPES.narrowPost(RunedObsidian.RUNED_OBSIDIAN, RunedObsidian.RUNED_NARROW_POST, null, true, p))
          .blockstate(BlockstateGenerator.narrowPost(RUNED_OBSIDIAN))
          .item()
          .model(ItemModelGenerator::itemModel)
          .build()
          .tag(BlockTags.DRAGON_IMMUNE, BlockTags.WITHER_IMMUNE)
          .register();

      public static BlockEntry<RunedObsidianBlocks.WidePost> RUNED_WIDE_POST = REGISTRATE.block("runed_wide_post", RunedObsidianBlocks.WidePost::new)
          .properties(RUNED_PROPERTIES)
          .recipe((ctx, p) -> Roots.RECIPES.widePost(RunedObsidian.RUNED_OBSIDIAN, RunedObsidian.RUNED_WIDE_POST, null, true, p))
          .blockstate(BlockstateGenerator.widePost(RUNED_OBSIDIAN))
          .item()
          .model(ItemModelGenerator::itemModel)
          .build()
          .tag(BlockTags.DRAGON_IMMUNE, BlockTags.WITHER_IMMUNE)
          .register();

      public static BlockEntry<RunedObsidianBlocks.Fence> RUNED_FENCE = REGISTRATE.block("runed_fence", RunedObsidianBlocks.Fence::new)
          .properties(RUNED_PROPERTIES)
          .recipe((ctx, p) -> {
                p.fence(DataIngredient.items(RunedObsidian.RUNED_OBSIDIAN), RunedObsidian.RUNED_FENCE, null);
                p.stonecutting(DataIngredient.items(RunedObsidian.RUNED_OBSIDIAN), RunedObsidian.RUNED_FENCE, 2);
              }
          )
          .blockstate(BlockstateGenerator.fence(RUNED_OBSIDIAN))
          .item()
          .model(ItemModelGenerator::inventoryModel)
          .build()
          .tag(BlockTags.DRAGON_IMMUNE, BlockTags.WITHER_IMMUNE, BlockTags.FENCES, Tags.Blocks.FENCES)
          .register();

      public static BlockEntry<RunedObsidianBlocks.Gate> RUNED_GATE = REGISTRATE.block("runed_gate", RunedObsidianBlocks.Gate::new)
          .properties(RUNED_PROPERTIES)
          .recipe((ctx, p) -> {
            p.fenceGate(DataIngredient.items(RunedObsidian.RUNED_OBSIDIAN), RunedObsidian.RUNED_GATE, null);
            p.stonecutting(DataIngredient.items(RunedObsidian.RUNED_OBSIDIAN), RunedObsidian.RUNED_GATE, 2);
          })
          .blockstate(BlockstateGenerator.gate(RUNED_OBSIDIAN))
          .item()
          .model(ItemModelGenerator::itemModel)
          .build()
          .tag(BlockTags.DRAGON_IMMUNE, BlockTags.WITHER_IMMUNE, BlockTags.FENCE_GATES, Tags.Blocks.FENCE_GATES, BlockTags.UNSTABLE_BOTTOM_CENTER)
          .register();

      public static void load() {
      }
    }

    public static class RunedObsidianBrick {
      public static NonNullUnaryOperator<AbstractBlock.Properties> RUNED_PROPERTIES = r -> AbstractBlock.Properties.copy(Blocks.OBSIDIAN);

      public static BlockEntry<Block> RUNED_OBSIDIAN_BRICK = REGISTRATE.block("runed_obsidian_brick", Block::new)
          .properties(RUNED_PROPERTIES)
          .item()
          .model(ItemModelGenerator::itemModel)
          .build()
          .tag(BlockTags.DRAGON_IMMUNE, BlockTags.WITHER_IMMUNE, RootsTags.Blocks.RUNED_OBSIDIAN)
          .register();

      public static BlockEntry<RunedObsidianBlocks.Button> RUNED_BRICK_BUTTON = REGISTRATE.block("runed_brick_button", RunedObsidianBlocks.Button::new)
          .properties(o -> AbstractBlock.Properties.copy(Blocks.STONE_BUTTON))
          .blockstate(BlockstateGenerator.button(RUNED_OBSIDIAN_BRICK))
          .recipe((ctx, p) -> {
            p.singleItem(DataIngredient.items(RunedObsidianBrick.RUNED_OBSIDIAN_BRICK), RunedObsidianBrick.RUNED_BRICK_BUTTON, 1, 1);
            p.stonecutting(DataIngredient.items(RunedObsidianBrick.RUNED_OBSIDIAN_BRICK), RunedObsidianBrick.RUNED_BRICK_BUTTON);
          })
          .item()
          .model(ItemModelGenerator::inventoryModel)
          .tag(ItemTags.BUTTONS)
          .build()
          .tag(BlockTags.DRAGON_IMMUNE, BlockTags.WITHER_IMMUNE, BlockTags.BUTTONS)
          .register();

      public static BlockEntry<RunedObsidianBlocks.PressurePlate> RUNED_BRICK_PRESSURE_PLATE = REGISTRATE.block("runed_brick_pressure_plate", (p) -> new RunedObsidianBlocks.PressurePlate(PressurePlateBlock.Sensitivity.MOBS, p))
          .properties(o -> AbstractBlock.Properties.of(Material.STONE, MaterialColor.COLOR_BLUE).noCollission().strength(0.5f).sound(SoundType.WOOD))
          .blockstate(BlockstateGenerator.pressurePlate(RUNED_OBSIDIAN_BRICK))
          .recipe((ctx, p) -> {
            ShapedRecipeBuilder.shaped(ctx.getEntry(), 1)
                .pattern("XX")
                .define('X', DataIngredient.items(RunedObsidianBrick.RUNED_OBSIDIAN_BRICK))
                .unlockedBy("has_runed_obsidian_brick", DataIngredient.items(RunedObsidianBrick.RUNED_OBSIDIAN_BRICK).getCritereon(p))
                .save(p, p.safeId(ctx.getEntry()));
            p.stonecutting(DataIngredient.items(RunedObsidianBrick.RUNED_OBSIDIAN_BRICK), RunedObsidianBrick.RUNED_BRICK_PRESSURE_PLATE);
          })
          .item()
          .model(ItemModelGenerator::itemModel)
          .build()
          .tag(BlockTags.DRAGON_IMMUNE, BlockTags.WITHER_IMMUNE, BlockTags.STONE_PRESSURE_PLATES)
          .register();

      public static BlockEntry<RunedObsidianBlocks.Slab> RUNED_BRICK_SLAB = REGISTRATE.block("runed_brick_slab", RunedObsidianBlocks.Slab::new)
          .properties(o -> AbstractBlock.Properties.copy(Blocks.OBSIDIAN).requiresCorrectToolForDrops())
          .blockstate(BlockstateGenerator.slab(RUNED_OBSIDIAN_BRICK))
          .recipe((ctx, p) -> p.slab(DataIngredient.items(RunedObsidianBrick.RUNED_OBSIDIAN_BRICK), RunedObsidianBrick.RUNED_BRICK_SLAB, null, true))
          .item()
          .model(ItemModelGenerator::itemModel)
          .tag(ItemTags.SLABS)
          .build()
          .tag(BlockTags.DRAGON_IMMUNE, BlockTags.WITHER_IMMUNE, BlockTags.SLABS)
          .loot((p, t) -> p.add(t, RegistrateBlockLootTables.droppingSlab(t)))
          .register();

      public static BlockEntry<RunedObsidianBlocks.Stairs> RUNED_BRICK_STAIRS = REGISTRATE.block("runed_brick_stairs", RunedObsidianBlocks.Stairs::new)
          .properties(RUNED_PROPERTIES)
          .blockstate(BlockstateGenerator.stairs(RUNED_OBSIDIAN_BRICK))
          .recipe((ctx, p) -> p.stairs(DataIngredient.items(RunedObsidianBrick.RUNED_OBSIDIAN_BRICK), RunedObsidianBrick.RUNED_BRICK_STAIRS, null, true))
          .item()
          .model(ItemModelGenerator::itemModel)
          .tag(ItemTags.STAIRS)
          .build()
          .tag(BlockTags.DRAGON_IMMUNE, BlockTags.WITHER_IMMUNE, BlockTags.STAIRS)
          .register();

      public static BlockEntry<RunedObsidianBlocks.Wall> RUNED_BRICK_WALL = REGISTRATE.block("runed_brick_wall", RunedObsidianBlocks.Wall::new)
          .properties(RUNED_PROPERTIES)
          .blockstate(BlockstateGenerator.wall(RUNED_OBSIDIAN_BRICK))
          .recipe((ctx, p) -> p.wall(DataIngredient.items(RunedObsidianBrick.RUNED_OBSIDIAN_BRICK), RunedObsidianBrick.RUNED_BRICK_WALL))
          .item()
          .model(ItemModelGenerator::inventoryModel)
          .tag(ItemTags.WALLS)
          .build()
          .tag(BlockTags.DRAGON_IMMUNE, BlockTags.WITHER_IMMUNE, BlockTags.WALLS)
          .register();

      public static BlockEntry<RunedObsidianBlocks.NarrowPost> RUNED_BRICK_NARROW_POST = REGISTRATE.block("runed_brick_narrow_post", RunedObsidianBlocks.NarrowPost::new)
          .properties(RUNED_PROPERTIES)
          .recipe((ctx, p) -> Roots.RECIPES.narrowPost(RunedObsidianBrick.RUNED_OBSIDIAN_BRICK, RunedObsidianBrick.RUNED_BRICK_NARROW_POST, null, true, p))
          .blockstate(BlockstateGenerator.narrowPost(RUNED_OBSIDIAN_BRICK))
          .item()
          .model(ItemModelGenerator::itemModel)
          .build()
          .tag(BlockTags.DRAGON_IMMUNE, BlockTags.WITHER_IMMUNE)
          .register();

      public static BlockEntry<RunedObsidianBlocks.WidePost> RUNED_BRICK_WIDE_POST = REGISTRATE.block("runed_brick_wide_post", RunedObsidianBlocks.WidePost::new)
          .properties(RUNED_PROPERTIES)
          .recipe((ctx, p) -> Roots.RECIPES.widePost(RunedObsidianBrick.RUNED_OBSIDIAN_BRICK, RunedObsidianBrick.RUNED_BRICK_WIDE_POST, null, true, p))
          .blockstate(BlockstateGenerator.widePost(RUNED_OBSIDIAN_BRICK))
          .item()
          .model(ItemModelGenerator::itemModel)
          .build()
          .tag(BlockTags.DRAGON_IMMUNE, BlockTags.WITHER_IMMUNE)
          .register();

      public static BlockEntry<RunedObsidianBlocks.Fence> RUNED_BRICK_FENCE = REGISTRATE.block("runed_brick_fence", RunedObsidianBlocks.Fence::new)
          .properties(RUNED_PROPERTIES)
          .recipe((ctx, p) -> {
                p.fence(DataIngredient.items(RunedObsidianBrick.RUNED_OBSIDIAN_BRICK), RunedObsidianBrick.RUNED_BRICK_FENCE, null);
                p.stonecutting(DataIngredient.items(RunedObsidianBrick.RUNED_OBSIDIAN_BRICK), RunedObsidianBrick.RUNED_BRICK_FENCE, 2);
              }
          )
          .blockstate(BlockstateGenerator.fence(RUNED_OBSIDIAN_BRICK))
          .item()
          .model(ItemModelGenerator::inventoryModel)
          .build()
          .tag(BlockTags.DRAGON_IMMUNE, BlockTags.WITHER_IMMUNE, BlockTags.FENCES, Tags.Blocks.FENCES)
          .register();

      public static BlockEntry<RunedObsidianBlocks.Gate> RUNED_BRICK_GATE = REGISTRATE.block("runed_brick_gate", RunedObsidianBlocks.Gate::new)
          .properties(RUNED_PROPERTIES)
          .recipe((ctx, p) -> {
            p.fenceGate(DataIngredient.items(RunedObsidianBrick.RUNED_OBSIDIAN_BRICK), RunedObsidianBrick.RUNED_BRICK_GATE, null);
            p.stonecutting(DataIngredient.items(RunedObsidianBrick.RUNED_OBSIDIAN_BRICK), RunedObsidianBrick.RUNED_BRICK_GATE, 2);
          })
          .blockstate(BlockstateGenerator.gate(RUNED_OBSIDIAN_BRICK))
          .item()
          .model(ItemModelGenerator::itemModel)
          .build()
          .tag(BlockTags.DRAGON_IMMUNE, BlockTags.WITHER_IMMUNE, BlockTags.FENCE_GATES, Tags.Blocks.FENCE_GATES, BlockTags.UNSTABLE_BOTTOM_CENTER)
          .register();

      public static void load() {
      }
    }

    public static class RunedObsidianBrickAlt {
      public static NonNullUnaryOperator<AbstractBlock.Properties> RUNED_PROPERTIES = r -> AbstractBlock.Properties.copy(Blocks.OBSIDIAN);

      public static BlockEntry<Block> RUNED_OBSIDIAN_BRICK_ALT = REGISTRATE.block("runed_obsidian_brick_alt", Block::new)
          .properties(RUNED_PROPERTIES)
          .item()
          .model(ItemModelGenerator::itemModel)
          .build()
          .tag(BlockTags.DRAGON_IMMUNE, BlockTags.WITHER_IMMUNE, RootsTags.Blocks.RUNED_OBSIDIAN)
          .register();

      public static BlockEntry<RunedObsidianBlocks.Button> RUNED_BRICK_ALT_BUTTON = REGISTRATE.block("runed_brick_alt_button", RunedObsidianBlocks.Button::new)
          .properties(o -> AbstractBlock.Properties.copy(Blocks.STONE_BUTTON))
          .blockstate(BlockstateGenerator.button(RUNED_OBSIDIAN_BRICK_ALT))
          .recipe((ctx, p) -> {
            p.singleItem(DataIngredient.items(RunedObsidianBrickAlt.RUNED_OBSIDIAN_BRICK_ALT), RunedObsidianBrickAlt.RUNED_BRICK_ALT_BUTTON, 1, 1);
            p.stonecutting(DataIngredient.items(RunedObsidianBrickAlt.RUNED_OBSIDIAN_BRICK_ALT), RunedObsidianBrickAlt.RUNED_BRICK_ALT_BUTTON);
          })
          .item()
          .model(ItemModelGenerator::inventoryModel)
          .tag(ItemTags.BUTTONS)
          .build()
          .tag(BlockTags.DRAGON_IMMUNE, BlockTags.WITHER_IMMUNE, BlockTags.BUTTONS)
          .register();

      public static BlockEntry<RunedObsidianBlocks.PressurePlate> RUNED_BRICK_ALT_PRESSURE_PLATE = REGISTRATE.block("runed_brick_alt_pressure_plate", (p) -> new RunedObsidianBlocks.PressurePlate(PressurePlateBlock.Sensitivity.MOBS, p))
          .properties(o -> AbstractBlock.Properties.of(Material.STONE, MaterialColor.COLOR_BLUE).noCollission().strength(0.5f).sound(SoundType.WOOD))
          .blockstate(BlockstateGenerator.pressurePlate(RUNED_OBSIDIAN_BRICK_ALT))
          .recipe((ctx, p) -> {
            ShapedRecipeBuilder.shaped(ctx.getEntry(), 1)
                .pattern("XX")
                .define('X', DataIngredient.items(RunedObsidianBrickAlt.RUNED_OBSIDIAN_BRICK_ALT))
                .unlockedBy("has_runed_obsidian_brick_alt", DataIngredient.items(RunedObsidianBrickAlt.RUNED_OBSIDIAN_BRICK_ALT).getCritereon(p))
                .save(p, p.safeId(ctx.getEntry()));
            p.stonecutting(DataIngredient.items(RunedObsidianBrickAlt.RUNED_OBSIDIAN_BRICK_ALT), RunedObsidianBrickAlt.RUNED_BRICK_ALT_PRESSURE_PLATE);
          })
          .item()
          .model(ItemModelGenerator::itemModel)
          .build()
          .tag(BlockTags.DRAGON_IMMUNE, BlockTags.WITHER_IMMUNE, BlockTags.STONE_PRESSURE_PLATES)
          .register();

      public static BlockEntry<RunedObsidianBlocks.Slab> RUNED_BRICK_ALT_SLAB = REGISTRATE.block("runed_brick_alt_slab", RunedObsidianBlocks.Slab::new)
          .properties(o -> AbstractBlock.Properties.copy(Blocks.OBSIDIAN).requiresCorrectToolForDrops())
          .blockstate(BlockstateGenerator.slab(RUNED_OBSIDIAN_BRICK_ALT))
          .recipe((ctx, p) -> p.slab(DataIngredient.items(RunedObsidianBrickAlt.RUNED_OBSIDIAN_BRICK_ALT), RunedObsidianBrickAlt.RUNED_BRICK_ALT_SLAB, null, true))
          .item()
          .model(ItemModelGenerator::itemModel)
          .tag(ItemTags.SLABS)
          .build()
          .tag(BlockTags.DRAGON_IMMUNE, BlockTags.WITHER_IMMUNE, BlockTags.SLABS)
          .loot((p, t) -> p.add(t, RegistrateBlockLootTables.droppingSlab(t)))
          .register();

      public static BlockEntry<RunedObsidianBlocks.Stairs> RUNED_BRICK_ALT_STAIRS = REGISTRATE.block("runed_brick_alt_stairs", RunedObsidianBlocks.Stairs::new)
          .properties(RUNED_PROPERTIES)
          .blockstate(BlockstateGenerator.stairs(RUNED_OBSIDIAN_BRICK_ALT))
          .recipe((ctx, p) -> p.stairs(DataIngredient.items(RunedObsidianBrickAlt.RUNED_OBSIDIAN_BRICK_ALT), RunedObsidianBrickAlt.RUNED_BRICK_ALT_STAIRS, null, true))
          .item()
          .model(ItemModelGenerator::itemModel)
          .tag(ItemTags.STAIRS)
          .build()
          .tag(BlockTags.DRAGON_IMMUNE, BlockTags.WITHER_IMMUNE, BlockTags.STAIRS)
          .register();

      public static BlockEntry<RunedObsidianBlocks.Wall> RUNED_BRICK_ALT_WALL = REGISTRATE.block("runed_brick_alt_wall", RunedObsidianBlocks.Wall::new)
          .properties(RUNED_PROPERTIES)
          .blockstate(BlockstateGenerator.wall(RUNED_OBSIDIAN_BRICK_ALT))
          .recipe((ctx, p) -> p.wall(DataIngredient.items(RunedObsidianBrickAlt.RUNED_OBSIDIAN_BRICK_ALT), RunedObsidianBrickAlt.RUNED_BRICK_ALT_WALL))
          .item()
          .model(ItemModelGenerator::inventoryModel)
          .tag(ItemTags.WALLS)
          .build()
          .tag(BlockTags.DRAGON_IMMUNE, BlockTags.WITHER_IMMUNE, BlockTags.WALLS)
          .register();

      public static BlockEntry<RunedObsidianBlocks.NarrowPost> RUNED_BRICK_ALT_NARROW_POST = REGISTRATE.block("runed_brick_alt_narrow_post", RunedObsidianBlocks.NarrowPost::new)
          .properties(RUNED_PROPERTIES)
          .recipe((ctx, p) -> Roots.RECIPES.narrowPost(RunedObsidianBrickAlt.RUNED_OBSIDIAN_BRICK_ALT, RunedObsidianBrickAlt.RUNED_BRICK_ALT_NARROW_POST, null, true, p))
          .blockstate(BlockstateGenerator.narrowPost(RUNED_OBSIDIAN_BRICK_ALT))
          .item()
          .model(ItemModelGenerator::itemModel)
          .build()
          .tag(BlockTags.DRAGON_IMMUNE, BlockTags.WITHER_IMMUNE)
          .register();

      public static BlockEntry<RunedObsidianBlocks.WidePost> RUNED_BRICK_ALT_WIDE_POST = REGISTRATE.block("runed_brick_alt_wide_post", RunedObsidianBlocks.WidePost::new)
          .properties(RUNED_PROPERTIES)
          .recipe((ctx, p) -> Roots.RECIPES.widePost(RunedObsidianBrickAlt.RUNED_OBSIDIAN_BRICK_ALT, RunedObsidianBrickAlt.RUNED_BRICK_ALT_WIDE_POST, null, true, p))
          .blockstate(BlockstateGenerator.widePost(RUNED_OBSIDIAN_BRICK_ALT))
          .item()
          .model(ItemModelGenerator::itemModel)
          .build()
          .tag(BlockTags.DRAGON_IMMUNE, BlockTags.WITHER_IMMUNE)
          .register();

      public static BlockEntry<RunedObsidianBlocks.Fence> RUNED_BRICK_ALT_FENCE = REGISTRATE.block("runed_brick_alt_fence", RunedObsidianBlocks.Fence::new)
          .properties(RUNED_PROPERTIES)
          .recipe((ctx, p) -> {
                p.fence(DataIngredient.items(RunedObsidianBrickAlt.RUNED_OBSIDIAN_BRICK_ALT), RunedObsidianBrickAlt.RUNED_BRICK_ALT_FENCE, null);
                p.stonecutting(DataIngredient.items(RunedObsidianBrickAlt.RUNED_OBSIDIAN_BRICK_ALT), RunedObsidianBrickAlt.RUNED_BRICK_ALT_FENCE, 2);
              }
          )
          .blockstate(BlockstateGenerator.fence(RUNED_OBSIDIAN_BRICK_ALT))
          .item()
          .model(ItemModelGenerator::inventoryModel)
          .build()
          .tag(BlockTags.DRAGON_IMMUNE, BlockTags.WITHER_IMMUNE, BlockTags.FENCES, Tags.Blocks.FENCES)
          .register();

      public static BlockEntry<RunedObsidianBlocks.Gate> RUNED_BRICK_ALT_GATE = REGISTRATE.block("runed_brick_alt_gate", RunedObsidianBlocks.Gate::new)
          .properties(RUNED_PROPERTIES)
          .recipe((ctx, p) -> {
            p.fenceGate(DataIngredient.items(RunedObsidianBrickAlt.RUNED_OBSIDIAN_BRICK_ALT), RunedObsidianBrickAlt.RUNED_BRICK_ALT_GATE, null);
            p.stonecutting(DataIngredient.items(RunedObsidianBrickAlt.RUNED_OBSIDIAN_BRICK_ALT), RunedObsidianBrickAlt.RUNED_BRICK_ALT_GATE, 2);
          })
          .blockstate(BlockstateGenerator.gate(RUNED_OBSIDIAN_BRICK_ALT))
          .item()
          .model(ItemModelGenerator::itemModel)
          .build()
          .tag(BlockTags.DRAGON_IMMUNE, BlockTags.WITHER_IMMUNE, BlockTags.FENCE_GATES, Tags.Blocks.FENCE_GATES, BlockTags.UNSTABLE_BOTTOM_CENTER)
          .register();

      public static void load() {
      }
    }

    public static class Runestone {
      public static NonNullUnaryOperator<AbstractBlock.Properties> RUNESTONE_PROPERTIES = r -> AbstractBlock.Properties.copy(Blocks.STONE);

      public static BlockEntry<Block> RUNESTONE = REGISTRATE.block("runestone", Block::new)
          .properties(RUNESTONE_PROPERTIES)
          .item()
          .model(ItemModelGenerator::itemModel)
          .build()
          .tag(RootsTags.Blocks.RUNED_OBSIDIAN)
          .register();

      public static BlockEntry<Block> CHISELED_RUNESTONE = REGISTRATE.block("chiseled_runestone", Block::new)
          .properties(RUNESTONE_PROPERTIES)
          .item()
          .model(ItemModelGenerator::itemModel)
          .build()
          .tag(RootsTags.Blocks.RUNED_OBSIDIAN)
          .register();

      public static BlockEntry<BaseBlocks.StoneButtonBlock> RUNESTONE_BUTTON = REGISTRATE.block("runestone_button", BaseBlocks.StoneButtonBlock::new)
          .properties(o -> AbstractBlock.Properties.copy(Blocks.STONE_BUTTON))
          .blockstate(BlockstateGenerator.button(RUNESTONE))
          .recipe((ctx, p) -> {
            p.singleItem(DataIngredient.items(Runestone.RUNESTONE), Runestone.RUNESTONE_BUTTON, 1, 1);
            p.stonecutting(DataIngredient.items(Runestone.RUNESTONE), Runestone.RUNESTONE_BUTTON);
          })
          .item()
          .model(ItemModelGenerator::inventoryModel)
          .tag(ItemTags.BUTTONS)
          .build()
          .tag(BlockTags.BUTTONS)
          .register();

      public static BlockEntry<BaseBlocks.PressurePlateBlock> RUNESTONE_PRESSURE_PLATE = REGISTRATE.block("runestone_pressure_plate", (p) -> new BaseBlocks.PressurePlateBlock(PressurePlateBlock.Sensitivity.MOBS, p))
          .properties(o -> AbstractBlock.Properties.of(Material.STONE, MaterialColor.COLOR_BLUE).noCollission().strength(0.5f).sound(SoundType.WOOD))
          .blockstate(BlockstateGenerator.pressurePlate(RUNESTONE))
          .recipe((ctx, p) -> {
            ShapedRecipeBuilder.shaped(ctx.getEntry(), 1)
                .pattern("XX")
                .define('X', DataIngredient.items(Runestone.RUNESTONE))
                .unlockedBy("has_runestone", DataIngredient.items(Runestone.RUNESTONE).getCritereon(p))
                .save(p, p.safeId(ctx.getEntry()));
            p.stonecutting(DataIngredient.items(Runestone.RUNESTONE), Runestone.RUNESTONE_PRESSURE_PLATE);
          })
          .item()
          .model(ItemModelGenerator::itemModel)
          .build()
          .tag(BlockTags.STONE_PRESSURE_PLATES)
          .register();

      public static BlockEntry<SlabBlock> RUNESTONE_SLAB = REGISTRATE.block("runestone_slab", SlabBlock::new)
          .properties(o -> AbstractBlock.Properties.of(Material.STONE, MaterialColor.TERRACOTTA_BLUE).requiresCorrectToolForDrops().strength(1.5f, 6.0f))
          .blockstate(BlockstateGenerator.slab(RUNESTONE))
          .recipe((ctx, p) -> p.slab(DataIngredient.items(Runestone.RUNESTONE), Runestone.RUNESTONE_SLAB, null, true))
          .item()
          .model(ItemModelGenerator::itemModel)
          .tag(ItemTags.SLABS)
          .build()
          .tag(BlockTags.SLABS)
          .loot((p, t) -> p.add(t, RegistrateBlockLootTables.droppingSlab(t)))
          .register();

      public static BlockEntry<StairsBlock> RUNESTONE_STAIRS = REGISTRATE.block("runestone_stairs", (p) -> new StairsBlock(Runestone.RUNESTONE::getDefaultState, p))
          .properties(RUNESTONE_PROPERTIES)
          .blockstate(BlockstateGenerator.stairs(RUNESTONE))
          .recipe((ctx, p) -> p.stairs(DataIngredient.items(Runestone.RUNESTONE), Runestone.RUNESTONE_STAIRS, null, true))
          .item()
          .model(ItemModelGenerator::itemModel)
          .tag(ItemTags.STAIRS)
          .build()
          .tag(BlockTags.STAIRS)
          .register();

      public static BlockEntry<WallBlock> RUNESTONE_WALL = REGISTRATE.block("runestone_wall", WallBlock::new)
          .properties(RUNESTONE_PROPERTIES)
          .blockstate(BlockstateGenerator.wall(RUNESTONE))
          .recipe((ctx, p) -> p.wall(DataIngredient.items(Runestone.RUNESTONE), Runestone.RUNESTONE_WALL))
          .item()
          .model(ItemModelGenerator::inventoryModel)
          .tag(ItemTags.WALLS)
          .build()
          .tag(BlockTags.WALLS)
          .register();

      public static BlockEntry<BaseBlocks.NarrowPostBlock> RUNESTONE_NARROW_POST = REGISTRATE.block("runestone_narrow_post", BaseBlocks.NarrowPostBlock::new)
          .properties(RUNESTONE_PROPERTIES)
          .recipe((ctx, p) -> Roots.RECIPES.narrowPost(Runestone.RUNESTONE, Runestone.RUNESTONE_NARROW_POST, null, true, p))
          .blockstate(BlockstateGenerator.narrowPost(RUNESTONE))
          .item()
          .model(ItemModelGenerator::itemModel)
          .build()
          .tag()
          .register();

      public static BlockEntry<BaseBlocks.WidePostBlock> RUNESTONE_WIDE_POST = REGISTRATE.block("runestone_wide_post", BaseBlocks.WidePostBlock::new)
          .properties(RUNESTONE_PROPERTIES)
          .recipe((ctx, p) -> Roots.RECIPES.widePost(Runestone.RUNESTONE, Runestone.RUNESTONE_WIDE_POST, null, true, p))
          .blockstate(BlockstateGenerator.widePost(RUNESTONE))
          .item()
          .model(ItemModelGenerator::itemModel)
          .build()
          .tag()
          .register();

      public static BlockEntry<FenceBlock> RUNESTONE_FENCE = REGISTRATE.block("runestone_fence", FenceBlock::new)
          .properties(RUNESTONE_PROPERTIES)
          .recipe((ctx, p) -> {
                p.fence(DataIngredient.items(Runestone.RUNESTONE), Runestone.RUNESTONE_FENCE, null);
                p.stonecutting(DataIngredient.items(Runestone.RUNESTONE), Runestone.RUNESTONE_FENCE, 2);
              }
          )
          .blockstate(BlockstateGenerator.fence(RUNESTONE))
          .item()
          .model(ItemModelGenerator::inventoryModel)
          .build()
          .tag(BlockTags.FENCES, Tags.Blocks.FENCES)
          .register();

      public static BlockEntry<FenceGateBlock> RUNESTONE_GATE = REGISTRATE.block("runestone_gate", FenceGateBlock::new)
          .properties(RUNESTONE_PROPERTIES)
          .recipe((ctx, p) -> {
            p.fenceGate(DataIngredient.items(Runestone.RUNESTONE), Runestone.RUNESTONE_GATE, null);
            p.stonecutting(DataIngredient.items(Runestone.RUNESTONE), Runestone.RUNESTONE_GATE, 2);
          })
          .blockstate(BlockstateGenerator.gate(RUNESTONE))
          .item()
          .model(ItemModelGenerator::itemModel)
          .build()
          .tag(BlockTags.FENCE_GATES, Tags.Blocks.FENCE_GATES, BlockTags.UNSTABLE_BOTTOM_CENTER)
          .register();

      public static void load() {
      }
    }

    public static class RunestoneBrick {
      public static NonNullUnaryOperator<AbstractBlock.Properties> RUNESTONE_PROPERTIES = r -> AbstractBlock.Properties.copy(Blocks.STONE);

      public static BlockEntry<Block> RUNESTONE_BRICK = REGISTRATE.block("runestone_brick", Block::new)
          .properties(RUNESTONE_PROPERTIES)
          .item()
          .model(ItemModelGenerator::itemModel)
          .build()
          .tag(RootsTags.Blocks.RUNED_OBSIDIAN)
          .register();

      public static BlockEntry<BaseBlocks.StoneButtonBlock> RUNESTONE_BRICK_BUTTON = REGISTRATE.block("runestone_brick_button", BaseBlocks.StoneButtonBlock::new)
          .properties(o -> AbstractBlock.Properties.copy(Blocks.STONE_BUTTON))
          .blockstate(BlockstateGenerator.button(RUNESTONE_BRICK))
          .recipe((ctx, p) -> {
            p.singleItem(DataIngredient.items(RunestoneBrick.RUNESTONE_BRICK), RunestoneBrick.RUNESTONE_BRICK_BUTTON, 1, 1);
            p.stonecutting(DataIngredient.items(RunestoneBrick.RUNESTONE_BRICK), RunestoneBrick.RUNESTONE_BRICK_BUTTON);
          })
          .item()
          .model(ItemModelGenerator::inventoryModel)
          .tag(ItemTags.BUTTONS)
          .build()
          .tag(BlockTags.BUTTONS)
          .register();

      public static BlockEntry<BaseBlocks.PressurePlateBlock> RUNESTONE_BRICK_PRESSURE_PLATE = REGISTRATE.block("runestone_brick_pressure_plate", (p) -> new BaseBlocks.PressurePlateBlock(PressurePlateBlock.Sensitivity.MOBS, p))
          .properties(o -> AbstractBlock.Properties.of(Material.STONE, MaterialColor.COLOR_BLUE).noCollission().strength(0.5f).sound(SoundType.WOOD))
          .blockstate(BlockstateGenerator.pressurePlate(RUNESTONE_BRICK))
          .recipe((ctx, p) -> {
            ShapedRecipeBuilder.shaped(ctx.getEntry(), 1)
                .pattern("XX")
                .define('X', DataIngredient.items(RunestoneBrick.RUNESTONE_BRICK))
                .unlockedBy("has_runestone_brick", DataIngredient.items(RunestoneBrick.RUNESTONE_BRICK).getCritereon(p))
                .save(p, p.safeId(ctx.getEntry()));
            p.stonecutting(DataIngredient.items(RunestoneBrick.RUNESTONE_BRICK), RunestoneBrick.RUNESTONE_BRICK_PRESSURE_PLATE);
          })
          .item()
          .model(ItemModelGenerator::itemModel)
          .build()
          .tag(BlockTags.STONE_PRESSURE_PLATES)
          .register();

      public static BlockEntry<SlabBlock> RUNESTONE_BRICK_SLAB = REGISTRATE.block("runestone_brick_slab", SlabBlock::new)
          .properties(o -> AbstractBlock.Properties.of(Material.STONE, MaterialColor.TERRACOTTA_BLUE).requiresCorrectToolForDrops().strength(1.5f, 6.0f))
          .blockstate(BlockstateGenerator.slab(RUNESTONE_BRICK))
          .recipe((ctx, p) -> p.slab(DataIngredient.items(RunestoneBrick.RUNESTONE_BRICK), RunestoneBrick.RUNESTONE_BRICK_SLAB, null, true))
          .item()
          .model(ItemModelGenerator::itemModel)
          .tag(ItemTags.SLABS)
          .build()
          .tag(BlockTags.SLABS)
          .loot((p, t) -> p.add(t, RegistrateBlockLootTables.droppingSlab(t)))
          .register();

      public static BlockEntry<StairsBlock> RUNESTONE_BRICK_STAIRS = REGISTRATE.block("runestone_brick_stairs", (p) -> new StairsBlock(RunestoneBrick.RUNESTONE_BRICK::getDefaultState, p))
          .properties(RUNESTONE_PROPERTIES)
          .blockstate(BlockstateGenerator.stairs(RUNESTONE_BRICK))
          .recipe((ctx, p) -> p.stairs(DataIngredient.items(RunestoneBrick.RUNESTONE_BRICK), RunestoneBrick.RUNESTONE_BRICK_STAIRS, null, true))
          .item()
          .model(ItemModelGenerator::itemModel)
          .tag(ItemTags.STAIRS)
          .build()
          .tag(BlockTags.STAIRS)
          .register();

      public static BlockEntry<WallBlock> RUNESTONE_BRICK_WALL = REGISTRATE.block("runestone_brick_wall", WallBlock::new)
          .properties(RUNESTONE_PROPERTIES)
          .blockstate(BlockstateGenerator.wall(RUNESTONE_BRICK))
          .recipe((ctx, p) -> p.wall(DataIngredient.items(RunestoneBrick.RUNESTONE_BRICK), RunestoneBrick.RUNESTONE_BRICK_WALL))
          .item()
          .model(ItemModelGenerator::inventoryModel)
          .tag(ItemTags.WALLS)
          .build()
          .tag(BlockTags.WALLS)
          .register();

      public static BlockEntry<BaseBlocks.NarrowPostBlock> RUNESTONE_BRICK_NARROW_POST = REGISTRATE.block("runestone_brick_narrow_post", BaseBlocks.NarrowPostBlock::new)
          .properties(RUNESTONE_PROPERTIES)
          .recipe((ctx, p) -> Roots.RECIPES.narrowPost(RunestoneBrick.RUNESTONE_BRICK, RunestoneBrick.RUNESTONE_BRICK_NARROW_POST, null, true, p))
          .blockstate(BlockstateGenerator.narrowPost(RUNESTONE_BRICK))
          .item()
          .model(ItemModelGenerator::itemModel)
          .build()
          .tag()
          .register();

      public static BlockEntry<BaseBlocks.WidePostBlock> RUNESTONE_BRICK_WIDE_POST = REGISTRATE.block("runestone_brick_wide_post", BaseBlocks.WidePostBlock::new)
          .properties(RUNESTONE_PROPERTIES)
          .recipe((ctx, p) -> Roots.RECIPES.widePost(RunestoneBrick.RUNESTONE_BRICK, RunestoneBrick.RUNESTONE_BRICK_WIDE_POST, null, true, p))
          .blockstate(BlockstateGenerator.widePost(RUNESTONE_BRICK))
          .item()
          .model(ItemModelGenerator::itemModel)
          .build()
          .tag()
          .register();

      public static BlockEntry<FenceBlock> RUNESTONE_BRICK_FENCE = REGISTRATE.block("runestone_brick_fence", FenceBlock::new)
          .properties(RUNESTONE_PROPERTIES)
          .recipe((ctx, p) -> {
                p.fence(DataIngredient.items(RunestoneBrick.RUNESTONE_BRICK), RunestoneBrick.RUNESTONE_BRICK_FENCE, null);
                p.stonecutting(DataIngredient.items(RunestoneBrick.RUNESTONE_BRICK), RunestoneBrick.RUNESTONE_BRICK_FENCE, 2);
              }
          )
          .blockstate(BlockstateGenerator.fence(RUNESTONE_BRICK))
          .item()
          .model(ItemModelGenerator::inventoryModel)
          .build()
          .tag(BlockTags.FENCES, Tags.Blocks.FENCES)
          .register();

      public static BlockEntry<FenceGateBlock> RUNESTONE_BRICK_GATE = REGISTRATE.block("runestone_brick_gate", FenceGateBlock::new)
          .properties(RUNESTONE_PROPERTIES)
          .recipe((ctx, p) -> {
            p.fenceGate(DataIngredient.items(RunestoneBrick.RUNESTONE_BRICK), RunestoneBrick.RUNESTONE_BRICK_GATE, null);
            p.stonecutting(DataIngredient.items(RunestoneBrick.RUNESTONE_BRICK), RunestoneBrick.RUNESTONE_BRICK_GATE, 2);
          })
          .blockstate(BlockstateGenerator.gate(RUNESTONE_BRICK))
          .item()
          .model(ItemModelGenerator::itemModel)
          .build()
          .tag(BlockTags.FENCE_GATES, Tags.Blocks.FENCE_GATES, BlockTags.UNSTABLE_BOTTOM_CENTER)
          .register();

      public static void load() {
      }
    }

    public static class RunestoneBrickAlt {
      public static NonNullUnaryOperator<AbstractBlock.Properties> RUNESTONE_PROPERTIES = r -> AbstractBlock.Properties.copy(Blocks.STONE);

      public static BlockEntry<Block> RUNESTONE_BRICK_ALT = REGISTRATE.block("runestone_brick_alt", Block::new)
          .properties(RUNESTONE_PROPERTIES)
          .item()
          .model(ItemModelGenerator::itemModel)
          .build()
          .tag(RootsTags.Blocks.RUNED_OBSIDIAN)
          .register();

      public static BlockEntry<BaseBlocks.StoneButtonBlock> RUNESTONE_BRICK_ALT_BUTTON = REGISTRATE.block("runestone_brick_alt_button", BaseBlocks.StoneButtonBlock::new)
          .properties(o -> AbstractBlock.Properties.copy(Blocks.STONE_BUTTON))
          .blockstate(BlockstateGenerator.button(RUNESTONE_BRICK_ALT))
          .recipe((ctx, p) -> {
            p.singleItem(DataIngredient.items(RunestoneBrickAlt.RUNESTONE_BRICK_ALT), RunestoneBrickAlt.RUNESTONE_BRICK_ALT_BUTTON, 1, 1);
            p.stonecutting(DataIngredient.items(RunestoneBrickAlt.RUNESTONE_BRICK_ALT), RunestoneBrickAlt.RUNESTONE_BRICK_ALT_BUTTON);
          })
          .item()
          .model(ItemModelGenerator::inventoryModel)
          .tag(ItemTags.BUTTONS)
          .build()
          .tag(BlockTags.BUTTONS)
          .register();

      public static BlockEntry<BaseBlocks.PressurePlateBlock> RUNESTONE_BRICK_ALT_PRESSURE_PLATE = REGISTRATE.block("runestone_brick_alt_pressure_plate", (p) -> new BaseBlocks.PressurePlateBlock(PressurePlateBlock.Sensitivity.MOBS, p))
          .properties(o -> AbstractBlock.Properties.of(Material.STONE, MaterialColor.COLOR_BLUE).noCollission().strength(0.5f).sound(SoundType.WOOD))
          .blockstate(BlockstateGenerator.pressurePlate(RUNESTONE_BRICK_ALT))
          .recipe((ctx, p) -> {
            ShapedRecipeBuilder.shaped(ctx.getEntry(), 1)
                .pattern("XX")
                .define('X', DataIngredient.items(RunestoneBrickAlt.RUNESTONE_BRICK_ALT))
                .unlockedBy("has_runestone_brick_alt", DataIngredient.items(RunestoneBrickAlt.RUNESTONE_BRICK_ALT).getCritereon(p))
                .save(p, p.safeId(ctx.getEntry()));
            p.stonecutting(DataIngredient.items(RunestoneBrickAlt.RUNESTONE_BRICK_ALT), RunestoneBrickAlt.RUNESTONE_BRICK_ALT_PRESSURE_PLATE);
          })
          .item()
          .model(ItemModelGenerator::itemModel)
          .build()
          .tag(BlockTags.STONE_PRESSURE_PLATES)
          .register();

      public static BlockEntry<SlabBlock> RUNESTONE_BRICK_ALT_SLAB = REGISTRATE.block("runestone_brick_alt_slab", SlabBlock::new)
          .properties(o -> AbstractBlock.Properties.of(Material.STONE, MaterialColor.TERRACOTTA_BLUE).requiresCorrectToolForDrops().strength(1.5f, 6.0f))
          .blockstate(BlockstateGenerator.slab(RUNESTONE_BRICK_ALT))
          .recipe((ctx, p) -> p.slab(DataIngredient.items(RunestoneBrickAlt.RUNESTONE_BRICK_ALT), RunestoneBrickAlt.RUNESTONE_BRICK_ALT_SLAB, null, true))
          .item()
          .model(ItemModelGenerator::itemModel)
          .tag(ItemTags.SLABS)
          .build()
          .tag(BlockTags.SLABS)
          .loot((p, t) -> p.add(t, RegistrateBlockLootTables.droppingSlab(t)))
          .register();

      public static BlockEntry<StairsBlock> RUNESTONE_BRICK_ALT_STAIRS = REGISTRATE.block("runestone_brick_alt_stairs", (p) -> new StairsBlock(RunestoneBrickAlt.RUNESTONE_BRICK_ALT::getDefaultState, p))
          .properties(RUNESTONE_PROPERTIES)
          .blockstate(BlockstateGenerator.stairs(RUNESTONE_BRICK_ALT))
          .recipe((ctx, p) -> p.stairs(DataIngredient.items(RunestoneBrickAlt.RUNESTONE_BRICK_ALT), RunestoneBrickAlt.RUNESTONE_BRICK_ALT_STAIRS, null, true))
          .item()
          .model(ItemModelGenerator::itemModel)
          .tag(ItemTags.STAIRS)
          .build()
          .tag(BlockTags.STAIRS)
          .register();

      public static BlockEntry<WallBlock> RUNESTONE_BRICK_ALT_WALL = REGISTRATE.block("runestone_brick_alt_wall", WallBlock::new)
          .properties(RUNESTONE_PROPERTIES)
          .blockstate(BlockstateGenerator.wall(RUNESTONE_BRICK_ALT))
          .recipe((ctx, p) -> p.wall(DataIngredient.items(RunestoneBrickAlt.RUNESTONE_BRICK_ALT), RunestoneBrickAlt.RUNESTONE_BRICK_ALT_WALL))
          .item()
          .model(ItemModelGenerator::inventoryModel)
          .tag(ItemTags.WALLS)
          .build()
          .tag(BlockTags.WALLS)
          .register();

      public static BlockEntry<BaseBlocks.NarrowPostBlock> RUNESTONE_BRICK_ALT_NARROW_POST = REGISTRATE.block("runestone_brick_alt_narrow_post", BaseBlocks.NarrowPostBlock::new)
          .properties(RUNESTONE_PROPERTIES)
          .recipe((ctx, p) -> Roots.RECIPES.narrowPost(RunestoneBrickAlt.RUNESTONE_BRICK_ALT, RunestoneBrickAlt.RUNESTONE_BRICK_ALT_NARROW_POST, null, true, p))
          .blockstate(BlockstateGenerator.narrowPost(RUNESTONE_BRICK_ALT))
          .item()
          .model(ItemModelGenerator::itemModel)
          .build()
          .tag()
          .register();

      public static BlockEntry<BaseBlocks.WidePostBlock> RUNESTONE_BRICK_ALT_WIDE_POST = REGISTRATE.block("runestone_brick_alt_wide_post", BaseBlocks.WidePostBlock::new)
          .properties(RUNESTONE_PROPERTIES)
          .recipe((ctx, p) -> Roots.RECIPES.widePost(RunestoneBrickAlt.RUNESTONE_BRICK_ALT, RunestoneBrickAlt.RUNESTONE_BRICK_ALT_WIDE_POST, null, true, p))
          .blockstate(BlockstateGenerator.widePost(RUNESTONE_BRICK_ALT))
          .item()
          .model(ItemModelGenerator::itemModel)
          .build()
          .tag()
          .register();

      public static BlockEntry<FenceBlock> RUNESTONE_BRICK_ALT_FENCE = REGISTRATE.block("runestone_brick_alt_fence", FenceBlock::new)
          .properties(RUNESTONE_PROPERTIES)
          .recipe((ctx, p) -> {
                p.fence(DataIngredient.items(RunestoneBrickAlt.RUNESTONE_BRICK_ALT), RunestoneBrickAlt.RUNESTONE_BRICK_ALT_FENCE, null);
                p.stonecutting(DataIngredient.items(RunestoneBrickAlt.RUNESTONE_BRICK_ALT), RunestoneBrickAlt.RUNESTONE_BRICK_ALT_FENCE, 2);
              }
          )
          .blockstate(BlockstateGenerator.fence(RUNESTONE_BRICK_ALT))
          .item()
          .model(ItemModelGenerator::inventoryModel)
          .build()
          .tag(BlockTags.FENCES, Tags.Blocks.FENCES)
          .register();

      public static BlockEntry<FenceGateBlock> RUNESTONE_BRICK_ALT_GATE = REGISTRATE.block("runestone_brick_alt_gate", FenceGateBlock::new)
          .properties(RUNESTONE_PROPERTIES)
          .recipe((ctx, p) -> {
            p.fenceGate(DataIngredient.items(RunestoneBrickAlt.RUNESTONE_BRICK_ALT), RunestoneBrickAlt.RUNESTONE_BRICK_ALT_GATE, null);
            p.stonecutting(DataIngredient.items(RunestoneBrickAlt.RUNESTONE_BRICK_ALT), RunestoneBrickAlt.RUNESTONE_BRICK_ALT_GATE, 2);
          })
          .blockstate(BlockstateGenerator.gate(RUNESTONE_BRICK_ALT))
          .item()
          .model(ItemModelGenerator::itemModel)
          .build()
          .tag(BlockTags.FENCE_GATES, Tags.Blocks.FENCE_GATES, BlockTags.UNSTABLE_BOTTOM_CENTER)
          .register();

      public static void load() {
      }
    }

    public static class RunedWood {
      public static NonNullUnaryOperator<AbstractBlock.Properties> RUNED_LOG_PROPERTIES = r -> AbstractBlock.Properties.copy(Blocks.OAK_LOG);
      public static NonNullUnaryOperator<AbstractBlock.Properties> RUNED_STEM_PROPERTIES = r -> AbstractBlock.Properties.copy(Blocks.CRIMSON_STEM);

      public static BlockEntry<RotatedPillarBlock> RUNED_ACACIA_LOG = REGISTRATE.block("runed_acacia_log", RotatedPillarBlock::new)
          .properties(RUNED_LOG_PROPERTIES)
          .blockstate((ctx, p) -> p.axisBlock(ctx.getEntry(), new ResourceLocation(Roots.MODID, "block/runed_acacia"), new ResourceLocation("minecraft", "block/acacia_log_top")))
          .tag(BlockTags.ACACIA_LOGS, RootsTags.Blocks.RUNED_ACACIA_LOG)
          .item()
          .model(ItemModelGenerator::itemModel)
          .build()
          .register();


      public static BlockEntry<RotatedPillarBlock> RUNED_DARK_OAK_LOG = REGISTRATE.block("runed_dark_oak_log", RotatedPillarBlock::new)
          .properties(RUNED_LOG_PROPERTIES)
          .blockstate((ctx, p) -> p.axisBlock(ctx.getEntry(), new ResourceLocation(Roots.MODID, "block/runed_dark_oak"), new ResourceLocation("minecraft", "block/dark_oak_log_top")))
          .tag(BlockTags.DARK_OAK_LOGS, RootsTags.Blocks.RUNED_DARK_OAK_LOG)
          .item()
          .model(ItemModelGenerator::itemModel)
          .build()
          .register();


      public static BlockEntry<RotatedPillarBlock> RUNED_OAK_LOG = REGISTRATE.block("runed_oak_log", RotatedPillarBlock::new)
          .properties(RUNED_LOG_PROPERTIES)
          .blockstate((ctx, p) -> p.axisBlock(ctx.getEntry(), new ResourceLocation(Roots.MODID, "block/runed_oak"), new ResourceLocation("minecraft", "block/oak_log_top")))
          .tag(BlockTags.OAK_LOGS, RootsTags.Blocks.RUNED_OAK_LOG)
          .item()
          .model(ItemModelGenerator::itemModel)
          .build()
          .register();


      public static BlockEntry<RotatedPillarBlock> RUNED_BIRCH_LOG = REGISTRATE.block("runed_birch_log", RotatedPillarBlock::new)
          .properties(RUNED_LOG_PROPERTIES)
          .blockstate((ctx, p) -> p.axisBlock(ctx.getEntry(), new ResourceLocation(Roots.MODID, "block/runed_birch"), new ResourceLocation("minecraft", "block/birch_log_top")))
          .tag(BlockTags.BIRCH_LOGS, RootsTags.Blocks.RUNED_BIRCH_LOG)
          .item()
          .model(ItemModelGenerator::itemModel)
          .build()
          .register();


      public static BlockEntry<RotatedPillarBlock> RUNED_JUNGLE_LOG = REGISTRATE.block("runed_jungle_log", RotatedPillarBlock::new)
          .properties(RUNED_LOG_PROPERTIES)
          .blockstate((ctx, p) -> p.axisBlock(ctx.getEntry(), new ResourceLocation(Roots.MODID, "block/runed_jungle"), new ResourceLocation("minecraft", "block/jungle_log_top")))
          .tag(BlockTags.JUNGLE_LOGS, RootsTags.Blocks.RUNED_JUNGLE_LOG)
          .item()
          .model(ItemModelGenerator::itemModel)
          .build()
          .register();


      public static BlockEntry<RotatedPillarBlock> RUNED_SPRUCE_LOG = REGISTRATE.block("runed_spruce_log", RotatedPillarBlock::new)
          .properties(RUNED_LOG_PROPERTIES)
          .blockstate((ctx, p) -> p.axisBlock(ctx.getEntry(), new ResourceLocation(Roots.MODID, "block/runed_spruce"), new ResourceLocation("minecraft", "block/spruce_log_top")))
          .tag(BlockTags.SPRUCE_LOGS, RootsTags.Blocks.RUNED_SPRUCE_LOG)
          .item()
          .model(ItemModelGenerator::itemModel)
          .build()
          .register();


      public static BlockEntry<RotatedPillarBlock> RUNED_WILDWOOD_LOG = REGISTRATE.block("runed_wildwood_log", RotatedPillarBlock::new)
          .properties(RUNED_LOG_PROPERTIES)
          .blockstate((ctx, p) -> p.axisBlock(ctx.getEntry(), new ResourceLocation(Roots.MODID, "block/runed_wildwood"), new ResourceLocation(Roots.MODID, "block/wildwood_log_top")))
          .tag(RootsTags.Blocks.WILDWOOD_LOGS, RootsTags.Blocks.RUNED_WILDWOOD_LOG)
          .item()
          .model(ItemModelGenerator::itemModel)
          .build()
          .register();


      public static BlockEntry<RotatedPillarBlock> RUNED_CRIMSON_STEM = REGISTRATE.block("runed_crimson_stem", RotatedPillarBlock::new)
          .properties(RUNED_LOG_PROPERTIES)
          .blockstate((ctx, p) -> p.axisBlock(ctx.getEntry(), new ResourceLocation(Roots.MODID, "block/runed_crimson"), new ResourceLocation("minecraft", "block/crimson_stem_top")))
          .tag(BlockTags.CRIMSON_STEMS, RootsTags.Blocks.RUNED_CRIMSON_STEM)
          .item()
          .model(ItemModelGenerator::itemModel)
          .build()
          .register();


      public static BlockEntry<RotatedPillarBlock> RUNED_WARPED_STEM = REGISTRATE.block("runed_warped_stem", RotatedPillarBlock::new)
          .properties(RUNED_LOG_PROPERTIES)
          .blockstate((ctx, p) -> p.axisBlock(ctx.getEntry(), new ResourceLocation(Roots.MODID, "block/runed_warped"), new ResourceLocation("minecraft", "block/warped_stem_top")))
          .tag(BlockTags.WARPED_STEMS, RootsTags.Blocks.RUNED_WARPED_STEM)
          .item()
          .model(ItemModelGenerator::itemModel)
          .build()
          .register();

      public static void load () {}
    }

    public static void load() {
      RunedObsidian.load();
      RunedObsidianBrick.load();
      RunedObsidianBrickAlt.load();
      Runestone.load();
      RunestoneBrick.load();
      RunestoneBrickAlt.load();
      RunedWood.load();
    }
  }

  public static class Crops {
    public static BlockEntry<ThreeStageCropBlock> WILDROOT_CROP = REGISTRATE.block("wildroot_crop", (p) -> new ThreeStageCropBlock(p, () -> ModItems.Herbs.WILDROOT))
        .properties(o -> AbstractBlock.Properties.copy(Blocks.WHEAT))
        .blockstate(BlockstateGenerator::cropBlockstate)
        .tag(RootsTags.Blocks.WILDROOT_CROP)
        .register();

    public static BlockEntry<ElementalCropBlock> CLOUD_BERRY_CROP = REGISTRATE.block("cloud_berry_crop", (p) -> new ElementalCropBlock(p, () -> ModItems.Herbs.CLOUD_BERRY))
        .properties(o -> AbstractBlock.Properties.copy(Blocks.WHEAT))
        .blockstate(BlockstateGenerator::cropBlockstate)
        .tag(RootsTags.Blocks.CLOUD_BERRY_CROP)
        .register();

    public static BlockEntry<WaterElementalCropBlock> DEWGONIA_CROP = REGISTRATE.block("dewgonia_crop", (p) -> new WaterElementalCropBlock(p, () -> ModItems.Herbs.DEWGONIA))
        .properties(o -> AbstractBlock.Properties.copy(Blocks.WHEAT))
        .blockstate(BlockstateGenerator::cropBlockstate)
        .tag(RootsTags.Blocks.DEWGONIA_CROP)
        .register();

    public static BlockEntry<ElementalCropBlock> INFERNAL_BULB_CROP = REGISTRATE.block("infernal_bulb_crop", (p) -> new ElementalCropBlock(p, () -> ModItems.Herbs.INFERNAL_BULB))
        .properties(o -> AbstractBlock.Properties.copy(Blocks.WHEAT))
        .blockstate(BlockstateGenerator::crossBlockstate)
        .tag(RootsTags.Blocks.INFERNAL_BULB_CROP)
        .register();

    public static BlockEntry<ElementalCropBlock> STALICRIPE_CROP = REGISTRATE.block("stalicripe_crop", (p) -> new ElementalCropBlock(p, () -> ModItems.Herbs.STALICRIPE))
        .properties(o -> AbstractBlock.Properties.copy(Blocks.WHEAT))
        .blockstate(BlockstateGenerator::cropBlockstate)
        .tag(RootsTags.Blocks.STALICRIPE_CROP)
        .register();

    public static BlockEntry<SeededCropsBlock> MOONGLOW_LEAF_CROP = REGISTRATE.block("moonglow_leaf_crop", (p) -> new SeededCropsBlock(p, () -> ModItems.Seeds.MOONGLOW_LEAF_SEEDS))
        .properties(o -> AbstractBlock.Properties.copy(Blocks.WHEAT))
        .blockstate(BlockstateGenerator::cropBlockstate)
        .tag(RootsTags.Blocks.MOONGLOW_LEAF_CROP)
        .register();
    public static BlockEntry<SeededCropsBlock> PERESKIA_CROP = REGISTRATE.block("pereskia_crop", (p) -> new SeededCropsBlock(p, () -> ModItems.Seeds.PERESKIA_BULB))
        .properties(o -> AbstractBlock.Properties.copy(Blocks.WHEAT))
        .blockstate(BlockstateGenerator::crossBlockstate)
        .tag(RootsTags.Blocks.PERESKIA_CROP)
        .register();
    public static BlockEntry<ThreeStageCropBlock> SPIRIT_HERB_CROP = REGISTRATE.block("spirit_herb_crop", (p) -> new ThreeStageCropBlock(p, () -> ModItems.Seeds.SPIRIT_HERB_SEEDS))
        .properties(o -> AbstractBlock.Properties.copy(Blocks.WHEAT))
        .blockstate(BlockstateGenerator::cropBlockstate)
        .tag(RootsTags.Blocks.SPIRIT_HERB_CROP)
        .register();
    public static BlockEntry<SeededCropsBlock> WILDEWHEET_CROP = REGISTRATE.block("wildewheet_crop", (p) -> new SeededCropsBlock(p, () -> ModItems.Seeds.WILDEWHEET_SEEDS))
        .properties(o -> AbstractBlock.Properties.copy(Blocks.WHEAT))
        .blockstate(BlockstateGenerator::cropBlockstate)
        .tag(RootsTags.Blocks.WILDEWHEET_CROP)
        .register();

    public static void load() {
    }
  }

  public static class Soils {
    public static NonNullUnaryOperator<AbstractBlock.Properties> SOIL_PROPERTIES = r -> AbstractBlock.Properties.copy(Blocks.DIRT);

    public static BlockEntry<Block> AQUEOUS_SOIL = REGISTRATE.block("aqueous_soil", Block::new)
        .properties(SOIL_PROPERTIES)
        .blockstate(BlockstateGenerator.pillar("block/water_soil_side", "block/water_soil_top"))
        .item()
        .model(ItemModelGenerator::itemModel)
        .build()
        .tag(RootsTags.Blocks.WATER_SOIL)
        .register();

    public static BlockEntry<Block> CAELIC_SOIL = REGISTRATE.block("caelic_soil", Block::new)
        .properties(SOIL_PROPERTIES)
        .blockstate(BlockstateGenerator.pillar("block/air_soil_side", "block/air_soil_top"))
        .item()
        .model(ItemModelGenerator::itemModel)
        .build()
        .tag(RootsTags.Blocks.AIR_SOIL)
        .register();

    public static BlockEntry<Block> ELEMENTAL_SOIL = REGISTRATE.block("elemental_soil", Block::new)
        .properties(SOIL_PROPERTIES)
        .item()
        .model(ItemModelGenerator::itemModel)
        .build()
        .tag(RootsTags.Blocks.ELEMENTAL_SOIL)
        .register();

    public static BlockEntry<Block> MAGMATIC_SOIL = REGISTRATE.block("magmatic_soil", Block::new)
        .properties(SOIL_PROPERTIES)
        .blockstate(BlockstateGenerator.pillar("block/fire_soil_side", "block/fire_soil_top"))
        .item()
        .model(ItemModelGenerator::itemModel)
        .build()
        .tag(RootsTags.Blocks.FIRE_SOIL)
        .register();

    public static BlockEntry<Block> TERRAN_SOIL = REGISTRATE.block("terran_soil", Block::new)
        .properties(SOIL_PROPERTIES)
        .blockstate(BlockstateGenerator.pillar("block/earth_soil_side", "block/earth_soil_top"))
        .item()
        .model(ItemModelGenerator::itemModel)
        .build()
        .tag(RootsTags.Blocks.EARTH_SOIL)
        .register();

    public static void load() {
    }
  }

/*  public static class Wildwood {
    public static NonNullUnaryOperator<AbstractBlock.Properties> WILDWOOD_LOG_PROPERTIES = r -> AbstractBlock.Properties.copy(Blocks.OAK_LOG);
    public static NonNullUnaryOperator<AbstractBlock.Properties> WILDWOOD_LEAVES_PROPERTIES = r -> AbstractBlock.Properties.copy(Blocks.OAK_LEAVES);
    public static NonNullUnaryOperator<AbstractBlock.Properties> WILDWOOD_PLANKS_PROPERTIES = r -> AbstractBlock.Properties.copy(Blocks.OAK_PLANKS);
    public static NonNullUnaryOperator<AbstractBlock.Properties> WILDWOOD_DECORATION_PROPERTIES = r -> AbstractBlock.Properties.of(Material.DECORATION);

    public static BlockEntry<Block> WILDWOOD_LOG = REGISTRATE.block("wildwood_log", Material.WOOD, Block::new)
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
        .recipe((ctx, p) ->
            ShapedRecipeBuilder.shaped(ctx.getEntry(), 3)
                .pattern("X X")
                .pattern("XWX")
                .pattern("X X")
                .define('X', Tags.Items.RODS_WOODEN)
                .define('W', DataIngredient.items(Wildwood.WILDWOOD_PLANKS))
                .unlockedBy("has_wildwood_planks", DataIngredient.items(Wildwood.WILDWOOD_PLANKS).getCritereon(p))
                .save(p, p.safeId(ctx.getEntry())))
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
  }*/

/*  public static BlockEntry<FeyLightBlock> FEY_LIGHT = REGISTRATE.block("fey_light", FeyLightBlock::new)
      .properties(o -> AbstractBlock.Properties.copy(Blocks.TORCH))
      .blockstate(NonNullBiConsumer.noop())
      .register();

  public static BlockEntry<CatalystPlateBlock> CATALYST_PLATE = REGISTRATE.block("catalyst_plate", Material.STONE, CatalystPlateBlock::new)
      .properties(o -> o)

      .register();


  public static BlockEntry<CatalystPlateBlock> REINFORCED_CATALYST_PLATE = REGISTRATE.block("reinforced_catalyst_plate", CatalystPlateBlock::new).register();

  public static BlockEntry<FeyCrafterBlock> FEY_CRAFTER = REGISTRATE.block("fey_crafter", FeyCrafterBlock::new).register();

  public static BlockEntry<GroveStoneBlock> GROVE_STONE = REGISTRATE.block("grove_stone", GroveStoneBlock::new).register();

  public static BlockEntry<ImbuerBlock> IMBUER = REGISTRATE.block("imbuer", ImbuerBlock::new).register();

  public static BlockEntry<ImposerBlock> IMPOSER = REGISTRATE.block("imposer", ImposerBlock::new).register();

  public static BlockEntry<IncensePlateBlock> INCENSE_PLATE = REGISTRATE.block("incense_plate", IncensePlateBlock::new).register();

  public static BlockEntry<MortarBlock> MORTAR = REGISTRATE.block("mortar", MortarBlock::new).register();

  public static BlockEntry<PyreBlock> PYRE = REGISTRATE.block("pyre", PyreBlock::new).register();

  // TODO: Decorative Pyre Block
  public static BlockEntry<PyreBlock> DECORATIVE_PYRE = REGISTRATE.block("decorative_pyre", PyreBlock::new).register();

  public static BlockEntry<PyreBlock> REINFORCED_PYRE = REGISTRATE.block("reinforced_pyre", PyreBlock::new).register();

  public static BlockEntry<UnendingBowlBlock> UNENDING_BOWL = REGISTRATE.block("unending_bowl", UnendingBowlBlock::new).register();*/

  public static void load() {
    Decoration.load();
    Soils.load();
    Crops.load();
  }
}
