package mysticmods.roots.init;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.providers.loot.RegistrateBlockLootTables;
import com.tterrag.registrate.util.DataIngredient;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import com.tterrag.registrate.util.nullness.NonNullUnaryOperator;
import mysticmods.roots.Roots;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.block.*;
import mysticmods.roots.block.crop.ElementalCropBlock;
import mysticmods.roots.block.crop.ThreeStageCropBlock;
import mysticmods.roots.block.crop.WaterElementalCropBlock;
import mysticmods.roots.blockentity.RunicCrafterBlock;
import net.minecraft.core.Direction;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import noobanidus.libs.noobutil.block.BaseBlocks;
import noobanidus.libs.noobutil.block.BaseBlocks.SeededCropsBlock;
import noobanidus.libs.noobutil.data.generator.BlockstateGenerator;
import noobanidus.libs.noobutil.data.generator.ItemModelGenerator;

import static mysticmods.roots.Roots.REGISTRATE;

public class ModBlocks {
  public static NonNullUnaryOperator<BlockBehaviour.Properties> RUNED_PROPERTIES = r -> BlockBehaviour.Properties.copy(net.minecraft.world.level.block.Blocks.OBSIDIAN);
  public static BlockEntry<Block> RUNED_OBSIDIAN = REGISTRATE.block("runed_obsidian", Block::new)
      .properties(RUNED_PROPERTIES)
      .recipe((ctx, p) -> Roots.RECIPES.twoByTwo(ModBlocks.RUNED_OBSIDIAN_BRICK_ALT, ModBlocks.RUNED_OBSIDIAN, null, 4, p))
      .item()
      .model(ItemModelGenerator::itemModel)
      .build()
      .tag(BlockTags.DRAGON_IMMUNE, BlockTags.WITHER_IMMUNE, RootsAPI.Tags.Blocks.RUNED_OBSIDIAN, RootsAPI.Tags.Blocks.RUNE_PILLARS, BlockTags.MINEABLE_WITH_PICKAXE)
      .register();
  public static BlockEntry<Block> CHISELED_RUNED_OBSIDIAN = REGISTRATE.block("chiseled_runed_obsidian", Block::new)
      .properties(RUNED_PROPERTIES)
      .recipe((ctx, p) -> Roots.RECIPES.twoByTwo(ModBlocks.RUNED_OBSIDIAN_BRICK, ModBlocks.CHISELED_RUNED_OBSIDIAN, null, 4, p))
      .item()
      .model(ItemModelGenerator::itemModel)
      .build()
      .tag(BlockTags.DRAGON_IMMUNE, BlockTags.WITHER_IMMUNE, RootsAPI.Tags.Blocks.RUNED_OBSIDIAN, RootsAPI.Tags.Blocks.RUNE_CAPSTONES, BlockTags.MINEABLE_WITH_PICKAXE)
      .register();
  public static BlockEntry<RunedObsidianBlocks.Button> RUNED_BUTTON = REGISTRATE.block("runed_button", RunedObsidianBlocks.Button::new)
      .properties(o -> BlockBehaviour.Properties.copy(net.minecraft.world.level.block.Blocks.STONE_BUTTON))
      .blockstate(BlockstateGenerator.button(RUNED_OBSIDIAN))
      .recipe((ctx, p) -> {
        p.singleItem(DataIngredient.items(ModBlocks.RUNED_OBSIDIAN), ModBlocks.RUNED_BUTTON, 1, 1);
        p.stonecutting(DataIngredient.items(ModBlocks.RUNED_OBSIDIAN), ModBlocks.RUNED_BUTTON);
      })
      .item()
      .model(ItemModelGenerator::inventoryModel)
      .tag(ItemTags.BUTTONS)
      .build()
      .tag(BlockTags.DRAGON_IMMUNE, BlockTags.WITHER_IMMUNE, BlockTags.BUTTONS, BlockTags.MINEABLE_WITH_PICKAXE)
      .register();
  public static BlockEntry<RunedObsidianBlocks.PressurePlate> RUNED_PRESSURE_PLATE = REGISTRATE.block("runed_pressure_plate", (p) -> new RunedObsidianBlocks.PressurePlate(PressurePlateBlock.Sensitivity.MOBS, p))
      .properties(o -> BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_BLUE).noCollission().strength(0.5f).sound(SoundType.WOOD))
      .blockstate(BlockstateGenerator.pressurePlate(RUNED_OBSIDIAN))
      .recipe((ctx, p) -> {
        ShapedRecipeBuilder.shaped(ctx.getEntry(), 1)
            .pattern("XX")
            .define('X', DataIngredient.items(ModBlocks.RUNED_OBSIDIAN))
            .unlockedBy("has_runed_obsidian", DataIngredient.items(ModBlocks.RUNED_OBSIDIAN).getCritereon(p))
            .save(p, p.safeId(ctx.getEntry()));
        p.stonecutting(DataIngredient.items(ModBlocks.RUNED_OBSIDIAN), ModBlocks.RUNED_PRESSURE_PLATE);
      })
      .item()
      .model(ItemModelGenerator::itemModel)
      .build()
      .tag(BlockTags.DRAGON_IMMUNE, BlockTags.WITHER_IMMUNE, BlockTags.STONE_PRESSURE_PLATES, BlockTags.MINEABLE_WITH_PICKAXE)
      .register();
  public static BlockEntry<RunedObsidianBlocks.Slab> RUNED_SLAB = REGISTRATE.block("runed_slab", RunedObsidianBlocks.Slab::new)
      .properties(o -> BlockBehaviour.Properties.copy(net.minecraft.world.level.block.Blocks.OBSIDIAN).requiresCorrectToolForDrops())
      .blockstate(BlockstateGenerator.slab(RUNED_OBSIDIAN))
      .recipe((ctx, p) -> p.slab(DataIngredient.items(ModBlocks.RUNED_OBSIDIAN), ModBlocks.RUNED_SLAB, null, true))
      .item()
      .model(ItemModelGenerator::itemModel)
      .tag(ItemTags.SLABS)
      .build()
      .tag(BlockTags.DRAGON_IMMUNE, BlockTags.WITHER_IMMUNE, BlockTags.SLABS, BlockTags.MINEABLE_WITH_PICKAXE)
      .loot((p, t) -> p.add(t, RegistrateBlockLootTables.createSlabItemTable(t)))
      .register();
  public static BlockEntry<RunedObsidianBlocks.Stairs> RUNED_STAIRS = REGISTRATE.block("runed_stairs", RunedObsidianBlocks.Stairs::new)
      .properties(RUNED_PROPERTIES)
      .blockstate(BlockstateGenerator.stairs(RUNED_OBSIDIAN))
      .recipe((ctx, p) -> p.stairs(DataIngredient.items(ModBlocks.RUNED_OBSIDIAN), ModBlocks.RUNED_STAIRS, null, true))
      .item()
      .model(ItemModelGenerator::itemModel)
      .tag(ItemTags.STAIRS)
      .build()
      .tag(BlockTags.DRAGON_IMMUNE, BlockTags.WITHER_IMMUNE, BlockTags.STAIRS, BlockTags.MINEABLE_WITH_PICKAXE)
      .register();
  public static BlockEntry<RunedObsidianBlocks.Wall> RUNED_WALL = REGISTRATE.block("runed_wall", RunedObsidianBlocks.Wall::new)
      .properties(RUNED_PROPERTIES)
      .blockstate(BlockstateGenerator.wall(RUNED_OBSIDIAN))
      .recipe((ctx, p) -> p.wall(DataIngredient.items(ModBlocks.RUNED_OBSIDIAN), ModBlocks.RUNED_WALL))
      .item()
      .model(ItemModelGenerator::inventoryModel)
      .tag(ItemTags.WALLS)
      .build()
      .tag(BlockTags.DRAGON_IMMUNE, BlockTags.WITHER_IMMUNE, BlockTags.WALLS, BlockTags.MINEABLE_WITH_PICKAXE)
      .register();
  public static BlockEntry<RunedObsidianBlocks.NarrowPost> RUNED_NARROW_POST = REGISTRATE.block("runed_narrow_post", RunedObsidianBlocks.NarrowPost::new)
      .properties(RUNED_PROPERTIES)
      .recipe((ctx, p) -> Roots.RECIPES.narrowPost(ModBlocks.RUNED_OBSIDIAN, ModBlocks.RUNED_NARROW_POST, null, true, p))
      .blockstate(BlockstateGenerator.narrowPost(RUNED_OBSIDIAN))
      .item()
      .model(ItemModelGenerator::itemModel)
      .build()
      .tag(BlockTags.DRAGON_IMMUNE, BlockTags.WITHER_IMMUNE, BlockTags.MINEABLE_WITH_PICKAXE)
      .register();
  public static BlockEntry<RunedObsidianBlocks.WidePost> RUNED_WIDE_POST = REGISTRATE.block("runed_wide_post", RunedObsidianBlocks.WidePost::new)
      .properties(RUNED_PROPERTIES)
      .recipe((ctx, p) -> Roots.RECIPES.widePost(ModBlocks.RUNED_OBSIDIAN, ModBlocks.RUNED_WIDE_POST, null, true, p))
      .blockstate(BlockstateGenerator.widePost(RUNED_OBSIDIAN))
      .item()
      .model(ItemModelGenerator::itemModel)
      .build()
      .tag(BlockTags.DRAGON_IMMUNE, BlockTags.WITHER_IMMUNE, BlockTags.MINEABLE_WITH_PICKAXE)
      .register();
  public static BlockEntry<RunedObsidianBlocks.Fence> RUNED_FENCE = REGISTRATE.block("runed_fence", RunedObsidianBlocks.Fence::new)
      .properties(RUNED_PROPERTIES)
      .recipe((ctx, p) -> {
            p.fence(DataIngredient.items(ModBlocks.RUNED_OBSIDIAN), ModBlocks.RUNED_FENCE, null);
            p.stonecutting(DataIngredient.items(ModBlocks.RUNED_OBSIDIAN), ModBlocks.RUNED_FENCE, 2);
          }
      )
      .blockstate(BlockstateGenerator.fence(RUNED_OBSIDIAN))
      .item()
      .model(ItemModelGenerator::inventoryModel)
      .build()
      .tag(BlockTags.DRAGON_IMMUNE, BlockTags.WITHER_IMMUNE, BlockTags.FENCES, net.minecraftforge.common.Tags.Blocks.FENCES, BlockTags.MINEABLE_WITH_PICKAXE)
      .register();
  public static BlockEntry<RunedObsidianBlocks.Gate> RUNED_GATE = REGISTRATE.block("runed_gate", RunedObsidianBlocks.Gate::new)
      .properties(RUNED_PROPERTIES)
      .recipe((ctx, p) -> {
        p.fenceGate(DataIngredient.items(ModBlocks.RUNED_OBSIDIAN), ModBlocks.RUNED_GATE, null);
        p.stonecutting(DataIngredient.items(ModBlocks.RUNED_OBSIDIAN), ModBlocks.RUNED_GATE, 2);
      })
      .blockstate(BlockstateGenerator.gate(RUNED_OBSIDIAN))
      .item()
      .model(ItemModelGenerator::itemModel)
      .build()
      .tag(BlockTags.DRAGON_IMMUNE, BlockTags.WITHER_IMMUNE, BlockTags.FENCE_GATES, net.minecraftforge.common.Tags.Blocks.FENCE_GATES, BlockTags.UNSTABLE_BOTTOM_CENTER, BlockTags.MINEABLE_WITH_PICKAXE)
      .register();
  public static BlockEntry<Block> RUNED_OBSIDIAN_BRICK = REGISTRATE.block("runed_obsidian_brick", Block::new)
      .properties(RUNED_PROPERTIES)
      .recipe((ctx, p) -> Roots.RECIPES.twoByTwo(RUNED_OBSIDIAN, ModBlocks.RUNED_OBSIDIAN_BRICK, null, 4, p))
      .item()
      .model(ItemModelGenerator::itemModel)
      .build()
      .tag(BlockTags.DRAGON_IMMUNE, BlockTags.WITHER_IMMUNE, RootsAPI.Tags.Blocks.RUNED_OBSIDIAN, RootsAPI.Tags.Blocks.RUNE_PILLARS, BlockTags.MINEABLE_WITH_PICKAXE)
      .register();
  public static BlockEntry<RunedObsidianBlocks.Button> RUNED_BRICK_BUTTON = REGISTRATE.block("runed_brick_button", RunedObsidianBlocks.Button::new)
      .properties(o -> BlockBehaviour.Properties.copy(net.minecraft.world.level.block.Blocks.STONE_BUTTON))
      .blockstate(BlockstateGenerator.button(RUNED_OBSIDIAN_BRICK))
      .recipe((ctx, p) -> {
        p.singleItem(DataIngredient.items(ModBlocks.RUNED_OBSIDIAN_BRICK), ModBlocks.RUNED_BRICK_BUTTON, 1, 1);
        p.stonecutting(DataIngredient.items(ModBlocks.RUNED_OBSIDIAN_BRICK), ModBlocks.RUNED_BRICK_BUTTON);
      })
      .item()

      .model(ItemModelGenerator::inventoryModel)
      .tag(ItemTags.BUTTONS)
      .build()
      .tag(BlockTags.DRAGON_IMMUNE, BlockTags.WITHER_IMMUNE, BlockTags.BUTTONS, BlockTags.MINEABLE_WITH_PICKAXE)
      .register();
  public static BlockEntry<RunedObsidianBlocks.PressurePlate> RUNED_BRICK_PRESSURE_PLATE = REGISTRATE.block("runed_brick_pressure_plate", (p) -> new RunedObsidianBlocks.PressurePlate(PressurePlateBlock.Sensitivity.MOBS, p))
      .properties(o -> BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_BLUE).noCollission().strength(0.5f).sound(SoundType.WOOD))
      .blockstate(BlockstateGenerator.pressurePlate(RUNED_OBSIDIAN_BRICK))
      .recipe((ctx, p) -> {
        ShapedRecipeBuilder.shaped(ctx.getEntry(), 1)
            .pattern("XX")
            .define('X', DataIngredient.items(ModBlocks.RUNED_OBSIDIAN_BRICK))
            .unlockedBy("has_runed_obsidian_brick", DataIngredient.items(ModBlocks.RUNED_OBSIDIAN_BRICK).getCritereon(p))
            .save(p, p.safeId(ctx.getEntry()));
        p.stonecutting(DataIngredient.items(ModBlocks.RUNED_OBSIDIAN_BRICK), ModBlocks.RUNED_BRICK_PRESSURE_PLATE);
      })
      .item()
      .model(ItemModelGenerator::itemModel)
      .build()
      .tag(BlockTags.DRAGON_IMMUNE, BlockTags.WITHER_IMMUNE, BlockTags.STONE_PRESSURE_PLATES, BlockTags.MINEABLE_WITH_PICKAXE)
      .register();
  public static BlockEntry<RunedObsidianBlocks.Slab> RUNED_BRICK_SLAB = REGISTRATE.block("runed_brick_slab", RunedObsidianBlocks.Slab::new)
      .properties(o -> BlockBehaviour.Properties.copy(net.minecraft.world.level.block.Blocks.OBSIDIAN).requiresCorrectToolForDrops())
      .blockstate(BlockstateGenerator.slab(RUNED_OBSIDIAN_BRICK))
      .recipe((ctx, p) -> p.slab(DataIngredient.items(ModBlocks.RUNED_OBSIDIAN_BRICK), ModBlocks.RUNED_BRICK_SLAB, null, true))
      .item()
      .model(ItemModelGenerator::itemModel)
      .tag(ItemTags.SLABS)
      .build()
      .tag(BlockTags.DRAGON_IMMUNE, BlockTags.WITHER_IMMUNE, BlockTags.SLABS, BlockTags.MINEABLE_WITH_PICKAXE)
      .loot((p, t) -> p.add(t, RegistrateBlockLootTables.createSlabItemTable(t)))
      .register();
  public static BlockEntry<RunedObsidianBlocks.Stairs> RUNED_BRICK_STAIRS = REGISTRATE.block("runed_brick_stairs", RunedObsidianBlocks.Stairs::new)
      .properties(RUNED_PROPERTIES)
      .blockstate(BlockstateGenerator.stairs(RUNED_OBSIDIAN_BRICK))
      .recipe((ctx, p) -> p.stairs(DataIngredient.items(ModBlocks.RUNED_OBSIDIAN_BRICK), ModBlocks.RUNED_BRICK_STAIRS, null, true))
      .item()
      .model(ItemModelGenerator::itemModel)
      .tag(ItemTags.STAIRS)
      .build()
      .tag(BlockTags.DRAGON_IMMUNE, BlockTags.WITHER_IMMUNE, BlockTags.STAIRS, BlockTags.MINEABLE_WITH_PICKAXE)
      .register();
  public static BlockEntry<RunedObsidianBlocks.Wall> RUNED_BRICK_WALL = REGISTRATE.block("runed_brick_wall", RunedObsidianBlocks.Wall::new)
      .properties(RUNED_PROPERTIES)
      .blockstate(BlockstateGenerator.wall(RUNED_OBSIDIAN_BRICK))
      .recipe((ctx, p) -> p.wall(DataIngredient.items(ModBlocks.RUNED_OBSIDIAN_BRICK), ModBlocks.RUNED_BRICK_WALL))
      .item()
      .model(ItemModelGenerator::inventoryModel)
      .tag(ItemTags.WALLS)
      .build()
      .tag(BlockTags.DRAGON_IMMUNE, BlockTags.WITHER_IMMUNE, BlockTags.WALLS, BlockTags.MINEABLE_WITH_PICKAXE)
      .register();
  public static BlockEntry<RunedObsidianBlocks.NarrowPost> RUNED_BRICK_NARROW_POST = REGISTRATE.block("runed_brick_narrow_post", RunedObsidianBlocks.NarrowPost::new)
      .properties(RUNED_PROPERTIES)
      .recipe((ctx, p) -> Roots.RECIPES.narrowPost(ModBlocks.RUNED_OBSIDIAN_BRICK, ModBlocks.RUNED_BRICK_NARROW_POST, null, true, p))
      .blockstate(BlockstateGenerator.narrowPost(RUNED_OBSIDIAN_BRICK))
      .item()
      .model(ItemModelGenerator::itemModel)
      .build()
      .tag(BlockTags.DRAGON_IMMUNE, BlockTags.WITHER_IMMUNE, BlockTags.MINEABLE_WITH_PICKAXE)
      .register();
  public static BlockEntry<RunedObsidianBlocks.WidePost> RUNED_BRICK_WIDE_POST = REGISTRATE.block("runed_brick_wide_post", RunedObsidianBlocks.WidePost::new)
      .properties(RUNED_PROPERTIES)
      .recipe((ctx, p) -> Roots.RECIPES.widePost(ModBlocks.RUNED_OBSIDIAN_BRICK, ModBlocks.RUNED_BRICK_WIDE_POST, null, true, p))
      .blockstate(BlockstateGenerator.widePost(RUNED_OBSIDIAN_BRICK))
      .item()
      .model(ItemModelGenerator::itemModel)
      .build()
      .tag(BlockTags.DRAGON_IMMUNE, BlockTags.WITHER_IMMUNE, BlockTags.MINEABLE_WITH_PICKAXE)
      .register();
  public static BlockEntry<RunedObsidianBlocks.Fence> RUNED_BRICK_FENCE = REGISTRATE.block("runed_brick_fence", RunedObsidianBlocks.Fence::new)
      .properties(RUNED_PROPERTIES)
      .recipe((ctx, p) -> {
            p.fence(DataIngredient.items(ModBlocks.RUNED_OBSIDIAN_BRICK), ModBlocks.RUNED_BRICK_FENCE, null);
            p.stonecutting(DataIngredient.items(ModBlocks.RUNED_OBSIDIAN_BRICK), ModBlocks.RUNED_BRICK_FENCE, 2);
          }
      )
      .blockstate(BlockstateGenerator.fence(RUNED_OBSIDIAN_BRICK))
      .item()
      .model(ItemModelGenerator::inventoryModel)
      .build()
      .tag(BlockTags.DRAGON_IMMUNE, BlockTags.WITHER_IMMUNE, BlockTags.FENCES, net.minecraftforge.common.Tags.Blocks.FENCES, BlockTags.MINEABLE_WITH_PICKAXE)
      .register();
  public static BlockEntry<RunedObsidianBlocks.Gate> RUNED_BRICK_GATE = REGISTRATE.block("runed_brick_gate", RunedObsidianBlocks.Gate::new)
      .properties(RUNED_PROPERTIES)
      .recipe((ctx, p) -> {
        p.fenceGate(DataIngredient.items(ModBlocks.RUNED_OBSIDIAN_BRICK), ModBlocks.RUNED_BRICK_GATE, null);
        p.stonecutting(DataIngredient.items(ModBlocks.RUNED_OBSIDIAN_BRICK), ModBlocks.RUNED_BRICK_GATE, 2);
      })
      .blockstate(BlockstateGenerator.gate(RUNED_OBSIDIAN_BRICK))
      .item()
      .model(ItemModelGenerator::itemModel)
      .build()
      .tag(BlockTags.DRAGON_IMMUNE, BlockTags.WITHER_IMMUNE, BlockTags.FENCE_GATES, net.minecraftforge.common.Tags.Blocks.FENCE_GATES, BlockTags.UNSTABLE_BOTTOM_CENTER, BlockTags.MINEABLE_WITH_PICKAXE)
      .register();
  public static BlockEntry<Block> RUNED_OBSIDIAN_BRICK_ALT = REGISTRATE.block("runed_obsidian_brick_alt", Block::new)
      .properties(RUNED_PROPERTIES)
      .recipe((ctx, p) -> Roots.RECIPES.twoByTwo(CHISELED_RUNED_OBSIDIAN, ModBlocks.RUNED_OBSIDIAN_BRICK_ALT, null, 4, p))
      .item()
      .model(ItemModelGenerator::itemModel)
      .build()
      .tag(BlockTags.DRAGON_IMMUNE, BlockTags.WITHER_IMMUNE, RootsAPI.Tags.Blocks.RUNED_OBSIDIAN, RootsAPI.Tags.Blocks.RUNE_PILLARS, BlockTags.MINEABLE_WITH_PICKAXE)
      .register();
  public static BlockEntry<RunedObsidianBlocks.Button> RUNED_BRICK_ALT_BUTTON = REGISTRATE.block("runed_brick_alt_button", RunedObsidianBlocks.Button::new)
      .properties(o -> BlockBehaviour.Properties.copy(net.minecraft.world.level.block.Blocks.STONE_BUTTON))
      .blockstate(BlockstateGenerator.button(RUNED_OBSIDIAN_BRICK_ALT))
      .recipe((ctx, p) -> {
        p.singleItem(DataIngredient.items(ModBlocks.RUNED_OBSIDIAN_BRICK_ALT), ModBlocks.RUNED_BRICK_ALT_BUTTON, 1, 1);
        p.stonecutting(DataIngredient.items(ModBlocks.RUNED_OBSIDIAN_BRICK_ALT), ModBlocks.RUNED_BRICK_ALT_BUTTON);
      })
      .item()
      .model(ItemModelGenerator::inventoryModel)
      .tag(ItemTags.BUTTONS)
      .build()
      .tag(BlockTags.DRAGON_IMMUNE, BlockTags.WITHER_IMMUNE, BlockTags.BUTTONS, BlockTags.MINEABLE_WITH_PICKAXE)
      .register();
  public static BlockEntry<RunedObsidianBlocks.PressurePlate> RUNED_BRICK_ALT_PRESSURE_PLATE = REGISTRATE.block("runed_brick_alt_pressure_plate", (p) -> new RunedObsidianBlocks.PressurePlate(PressurePlateBlock.Sensitivity.MOBS, p))
      .properties(o -> BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_BLUE).noCollission().strength(0.5f).sound(SoundType.WOOD))
      .blockstate(BlockstateGenerator.pressurePlate(RUNED_OBSIDIAN_BRICK_ALT))
      .recipe((ctx, p) -> {
        ShapedRecipeBuilder.shaped(ctx.getEntry(), 1)
            .pattern("XX")
            .define('X', DataIngredient.items(ModBlocks.RUNED_OBSIDIAN_BRICK_ALT))
            .unlockedBy("has_runed_obsidian_brick_alt", DataIngredient.items(ModBlocks.RUNED_OBSIDIAN_BRICK_ALT).getCritereon(p))
            .save(p, p.safeId(ctx.getEntry()));
        p.stonecutting(DataIngredient.items(ModBlocks.RUNED_OBSIDIAN_BRICK_ALT), ModBlocks.RUNED_BRICK_ALT_PRESSURE_PLATE);
      })
      .item()
      .model(ItemModelGenerator::itemModel)
      .build()
      .tag(BlockTags.DRAGON_IMMUNE, BlockTags.WITHER_IMMUNE, BlockTags.STONE_PRESSURE_PLATES, BlockTags.MINEABLE_WITH_PICKAXE)
      .register();
  public static BlockEntry<RunedObsidianBlocks.Slab> RUNED_BRICK_ALT_SLAB = REGISTRATE.block("runed_brick_alt_slab", RunedObsidianBlocks.Slab::new)
      .properties(o -> BlockBehaviour.Properties.copy(net.minecraft.world.level.block.Blocks.OBSIDIAN).requiresCorrectToolForDrops())
      .blockstate(BlockstateGenerator.slab(RUNED_OBSIDIAN_BRICK_ALT))
      .recipe((ctx, p) -> p.slab(DataIngredient.items(ModBlocks.RUNED_OBSIDIAN_BRICK_ALT), ModBlocks.RUNED_BRICK_ALT_SLAB, null, true))
      .item()
      .model(ItemModelGenerator::itemModel)
      .tag(ItemTags.SLABS)
      .build()
      .tag(BlockTags.DRAGON_IMMUNE, BlockTags.WITHER_IMMUNE, BlockTags.SLABS, BlockTags.MINEABLE_WITH_PICKAXE)
      .loot((p, t) -> p.add(t, RegistrateBlockLootTables.createSlabItemTable(t)))
      .register();
  public static BlockEntry<RunedObsidianBlocks.Stairs> RUNED_BRICK_ALT_STAIRS = REGISTRATE.block("runed_brick_alt_stairs", RunedObsidianBlocks.Stairs::new)
      .properties(RUNED_PROPERTIES)
      .blockstate(BlockstateGenerator.stairs(RUNED_OBSIDIAN_BRICK_ALT))
      .recipe((ctx, p) -> p.stairs(DataIngredient.items(ModBlocks.RUNED_OBSIDIAN_BRICK_ALT), ModBlocks.RUNED_BRICK_ALT_STAIRS, null, true))
      .item()
      .model(ItemModelGenerator::itemModel)
      .tag(ItemTags.STAIRS)
      .build()
      .tag(BlockTags.DRAGON_IMMUNE, BlockTags.WITHER_IMMUNE, BlockTags.STAIRS, BlockTags.MINEABLE_WITH_PICKAXE)
      .register();
  public static BlockEntry<RunedObsidianBlocks.Wall> RUNED_BRICK_ALT_WALL = REGISTRATE.block("runed_brick_alt_wall", RunedObsidianBlocks.Wall::new)
      .properties(RUNED_PROPERTIES)
      .blockstate(BlockstateGenerator.wall(RUNED_OBSIDIAN_BRICK_ALT))
      .recipe((ctx, p) -> p.wall(DataIngredient.items(ModBlocks.RUNED_OBSIDIAN_BRICK_ALT), ModBlocks.RUNED_BRICK_ALT_WALL))
      .item()
      .model(ItemModelGenerator::inventoryModel)
      .tag(ItemTags.WALLS)
      .build()
      .tag(BlockTags.DRAGON_IMMUNE, BlockTags.WITHER_IMMUNE, BlockTags.WALLS, BlockTags.MINEABLE_WITH_PICKAXE)
      .register();
  public static BlockEntry<RunedObsidianBlocks.NarrowPost> RUNED_BRICK_ALT_NARROW_POST = REGISTRATE.block("runed_brick_alt_narrow_post", RunedObsidianBlocks.NarrowPost::new)
      .properties(RUNED_PROPERTIES)
      .recipe((ctx, p) -> Roots.RECIPES.narrowPost(ModBlocks.RUNED_OBSIDIAN_BRICK_ALT, ModBlocks.RUNED_BRICK_ALT_NARROW_POST, null, true, p))
      .blockstate(BlockstateGenerator.narrowPost(RUNED_OBSIDIAN_BRICK_ALT))
      .item()
      .model(ItemModelGenerator::itemModel)
      .build()
      .tag(BlockTags.DRAGON_IMMUNE, BlockTags.WITHER_IMMUNE, BlockTags.MINEABLE_WITH_PICKAXE)
      .register();
  public static BlockEntry<RunedObsidianBlocks.WidePost> RUNED_BRICK_ALT_WIDE_POST = REGISTRATE.block("runed_brick_alt_wide_post", RunedObsidianBlocks.WidePost::new)
      .properties(RUNED_PROPERTIES)
      .recipe((ctx, p) -> Roots.RECIPES.widePost(ModBlocks.RUNED_OBSIDIAN_BRICK_ALT, ModBlocks.RUNED_BRICK_ALT_WIDE_POST, null, true, p))
      .blockstate(BlockstateGenerator.widePost(RUNED_OBSIDIAN_BRICK_ALT))
      .item()
      .model(ItemModelGenerator::itemModel)
      .build()
      .tag(BlockTags.DRAGON_IMMUNE, BlockTags.WITHER_IMMUNE, BlockTags.MINEABLE_WITH_PICKAXE)
      .register();
  public static BlockEntry<RunedObsidianBlocks.Fence> RUNED_BRICK_ALT_FENCE = REGISTRATE.block("runed_brick_alt_fence", RunedObsidianBlocks.Fence::new)
      .properties(RUNED_PROPERTIES)
      .recipe((ctx, p) -> {
            p.fence(DataIngredient.items(ModBlocks.RUNED_OBSIDIAN_BRICK_ALT), ModBlocks.RUNED_BRICK_ALT_FENCE, null);
            p.stonecutting(DataIngredient.items(ModBlocks.RUNED_OBSIDIAN_BRICK_ALT), ModBlocks.RUNED_BRICK_ALT_FENCE, 2);
          }
      )
      .blockstate(BlockstateGenerator.fence(RUNED_OBSIDIAN_BRICK_ALT))
      .item()
      .model(ItemModelGenerator::inventoryModel)
      .build()
      .tag(BlockTags.DRAGON_IMMUNE, BlockTags.WITHER_IMMUNE, BlockTags.FENCES, net.minecraftforge.common.Tags.Blocks.FENCES, BlockTags.MINEABLE_WITH_PICKAXE)
      .register();
  public static BlockEntry<RunedObsidianBlocks.Gate> RUNED_BRICK_ALT_GATE = REGISTRATE.block("runed_brick_alt_gate", RunedObsidianBlocks.Gate::new)
      .properties(RUNED_PROPERTIES)
      .recipe((ctx, p) -> {
        p.fenceGate(DataIngredient.items(ModBlocks.RUNED_OBSIDIAN_BRICK_ALT), ModBlocks.RUNED_BRICK_ALT_GATE, null);
        p.stonecutting(DataIngredient.items(ModBlocks.RUNED_OBSIDIAN_BRICK_ALT), ModBlocks.RUNED_BRICK_ALT_GATE, 2);
      })
      .blockstate(BlockstateGenerator.gate(RUNED_OBSIDIAN_BRICK_ALT))
      .item()
      .model(ItemModelGenerator::itemModel)
      .build()
      .tag(BlockTags.DRAGON_IMMUNE, BlockTags.WITHER_IMMUNE, BlockTags.FENCE_GATES, net.minecraftforge.common.Tags.Blocks.FENCE_GATES, BlockTags.UNSTABLE_BOTTOM_CENTER, BlockTags.MINEABLE_WITH_PICKAXE)
      .register();
  public static NonNullUnaryOperator<BlockBehaviour.Properties> RUNESTONE_PROPERTIES = r -> BlockBehaviour.Properties.copy(net.minecraft.world.level.block.Blocks.STONE);
  public static BlockEntry<Block> RUNESTONE = REGISTRATE.block("runestone", Block::new)
      .properties(RUNESTONE_PROPERTIES)
      .recipe((ctx, p) -> Roots.RECIPES.twoByTwo(ModBlocks.RUNESTONE_BRICK_ALT, ModBlocks.RUNESTONE, null, 4, p))
      .item()
      .model(ItemModelGenerator::itemModel)
      .build()
      .tag(RootsAPI.Tags.Blocks.RUNED_OBSIDIAN, RootsAPI.Tags.Blocks.RUNE_PILLARS, BlockTags.MINEABLE_WITH_PICKAXE)
      .register();
  public static BlockEntry<Block> CHISELED_RUNESTONE = REGISTRATE.block("chiseled_runestone", Block::new)
      .properties(RUNESTONE_PROPERTIES)
      .recipe((ctx, p) -> Roots.RECIPES.twoByTwo(ModBlocks.RUNESTONE_BRICK, ModBlocks.CHISELED_RUNESTONE, null, 4, p))
      .item()
      .model(ItemModelGenerator::itemModel)
      .build()
      .tag(RootsAPI.Tags.Blocks.RUNED_OBSIDIAN, RootsAPI.Tags.Blocks.RUNE_CAPSTONES, BlockTags.MINEABLE_WITH_PICKAXE)
      .register();
  public static BlockEntry<BaseBlocks.StoneButtonBlock> RUNESTONE_BUTTON = REGISTRATE.block("runestone_button", BaseBlocks.StoneButtonBlock::new)
      .properties(o -> BlockBehaviour.Properties.copy(net.minecraft.world.level.block.Blocks.STONE_BUTTON))
      .blockstate(BlockstateGenerator.button(RUNESTONE))
      .recipe((ctx, p) -> {
        p.singleItem(DataIngredient.items(ModBlocks.RUNESTONE), ModBlocks.RUNESTONE_BUTTON, 1, 1);
        p.stonecutting(DataIngredient.items(ModBlocks.RUNESTONE), ModBlocks.RUNESTONE_BUTTON);
      })
      .item()
      .model(ItemModelGenerator::inventoryModel)
      .tag(ItemTags.BUTTONS)
      .build()
      .tag(BlockTags.BUTTONS, BlockTags.MINEABLE_WITH_PICKAXE)
      .register();
  public static BlockEntry<BaseBlocks.PressurePlateBlock> RUNESTONE_PRESSURE_PLATE = REGISTRATE.block("runestone_pressure_plate", (p) -> new BaseBlocks.PressurePlateBlock(PressurePlateBlock.Sensitivity.MOBS, p))
      .properties(o -> BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_BLUE).noCollission().strength(0.5f).sound(SoundType.WOOD))
      .blockstate(BlockstateGenerator.pressurePlate(RUNESTONE))
      .recipe((ctx, p) -> {
        ShapedRecipeBuilder.shaped(ctx.getEntry(), 1)
            .pattern("XX")
            .define('X', DataIngredient.items(ModBlocks.RUNESTONE))
            .unlockedBy("has_runestone", DataIngredient.items(ModBlocks.RUNESTONE).getCritereon(p))
            .save(p, p.safeId(ctx.getEntry()));
        p.stonecutting(DataIngredient.items(ModBlocks.RUNESTONE), ModBlocks.RUNESTONE_PRESSURE_PLATE);
      })
      .item()
      .model(ItemModelGenerator::itemModel)
      .build()
      .tag(BlockTags.STONE_PRESSURE_PLATES, BlockTags.MINEABLE_WITH_PICKAXE)
      .register();
  public static BlockEntry<SlabBlock> RUNESTONE_SLAB = REGISTRATE.block("runestone_slab", SlabBlock::new)
      .properties(o -> BlockBehaviour.Properties.of(Material.STONE, MaterialColor.TERRACOTTA_BLUE).requiresCorrectToolForDrops().strength(1.5f, 6.0f))
      .blockstate(BlockstateGenerator.slab(RUNESTONE))
      .recipe((ctx, p) -> p.slab(DataIngredient.items(ModBlocks.RUNESTONE), ModBlocks.RUNESTONE_SLAB, null, true))
      .item()
      .model(ItemModelGenerator::itemModel)
      .tag(ItemTags.SLABS)
      .build()
      .tag(BlockTags.SLABS, BlockTags.MINEABLE_WITH_PICKAXE)
      .loot((p, t) -> p.add(t, RegistrateBlockLootTables.createSlabItemTable(t)))
      .register();
  public static BlockEntry<StairBlock> RUNESTONE_STAIRS = REGISTRATE.block("runestone_stairs", (p) -> new StairBlock(ModBlocks.RUNESTONE::getDefaultState, p))
      .properties(RUNESTONE_PROPERTIES)
      .blockstate(BlockstateGenerator.stairs(RUNESTONE))
      .recipe((ctx, p) -> p.stairs(DataIngredient.items(ModBlocks.RUNESTONE), ModBlocks.RUNESTONE_STAIRS, null, true))
      .item()
      .model(ItemModelGenerator::itemModel)
      .tag(ItemTags.STAIRS)
      .build()
      .tag(BlockTags.STAIRS, BlockTags.MINEABLE_WITH_PICKAXE)
      .register();
  public static BlockEntry<WallBlock> RUNESTONE_WALL = REGISTRATE.block("runestone_wall", WallBlock::new)
      .properties(RUNESTONE_PROPERTIES)
      .blockstate(BlockstateGenerator.wall(RUNESTONE))
      .recipe((ctx, p) -> p.wall(DataIngredient.items(ModBlocks.RUNESTONE), ModBlocks.RUNESTONE_WALL))
      .item()
      .model(ItemModelGenerator::inventoryModel)
      .tag(ItemTags.WALLS)
      .build()
      .tag(BlockTags.WALLS, BlockTags.MINEABLE_WITH_PICKAXE)
      .register();
  public static BlockEntry<BaseBlocks.NarrowPostBlock> RUNESTONE_NARROW_POST = REGISTRATE.block("runestone_narrow_post", BaseBlocks.NarrowPostBlock::new)
      .properties(RUNESTONE_PROPERTIES)
      .recipe((ctx, p) -> Roots.RECIPES.narrowPost(ModBlocks.RUNESTONE, ModBlocks.RUNESTONE_NARROW_POST, null, true, p))
      .blockstate(BlockstateGenerator.narrowPost(RUNESTONE))
      .item()
      .model(ItemModelGenerator::itemModel)
      .build()
      .tag(BlockTags.MINEABLE_WITH_PICKAXE)
      .register();
  public static BlockEntry<BaseBlocks.WidePostBlock> RUNESTONE_WIDE_POST = REGISTRATE.block("runestone_wide_post", BaseBlocks.WidePostBlock::new)
      .properties(RUNESTONE_PROPERTIES)
      .recipe((ctx, p) -> Roots.RECIPES.widePost(ModBlocks.RUNESTONE, ModBlocks.RUNESTONE_WIDE_POST, null, true, p))
      .blockstate(BlockstateGenerator.widePost(RUNESTONE))
      .item()
      .model(ItemModelGenerator::itemModel)
      .build()
      .tag(BlockTags.MINEABLE_WITH_PICKAXE)
      .register();
  public static BlockEntry<FenceBlock> RUNESTONE_FENCE = REGISTRATE.block("runestone_fence", FenceBlock::new)
      .properties(RUNESTONE_PROPERTIES)
      .recipe((ctx, p) -> {
            p.fence(DataIngredient.items(ModBlocks.RUNESTONE), ModBlocks.RUNESTONE_FENCE, null);
            p.stonecutting(DataIngredient.items(ModBlocks.RUNESTONE), ModBlocks.RUNESTONE_FENCE, 2);
          }
      )
      .blockstate(BlockstateGenerator.fence(RUNESTONE))
      .item()
      .model(ItemModelGenerator::inventoryModel)
      .build()
      .tag(BlockTags.FENCES, net.minecraftforge.common.Tags.Blocks.FENCES, BlockTags.MINEABLE_WITH_PICKAXE)
      .register();
  public static BlockEntry<FenceGateBlock> RUNESTONE_GATE = REGISTRATE.block("runestone_gate", FenceGateBlock::new)
      .properties(RUNESTONE_PROPERTIES)
      .recipe((ctx, p) -> {
        p.fenceGate(DataIngredient.items(ModBlocks.RUNESTONE), ModBlocks.RUNESTONE_GATE, null);
        p.stonecutting(DataIngredient.items(ModBlocks.RUNESTONE), ModBlocks.RUNESTONE_GATE, 2);
      })
      .blockstate(BlockstateGenerator.gate(RUNESTONE))
      .item()
      .model(ItemModelGenerator::itemModel)
      .build()
      .tag(BlockTags.FENCE_GATES, net.minecraftforge.common.Tags.Blocks.FENCE_GATES, BlockTags.UNSTABLE_BOTTOM_CENTER, BlockTags.MINEABLE_WITH_PICKAXE)
      .register();
  public static BlockEntry<Block> RUNESTONE_BRICK = REGISTRATE.block("runestone_brick", Block::new)
      .properties(RUNESTONE_PROPERTIES)
      .recipe((ctx, p) -> Roots.RECIPES.twoByTwo(ModBlocks.RUNESTONE, ModBlocks.RUNESTONE_BRICK, null, 4, p))
      .item()
      .model(ItemModelGenerator::itemModel)
      .build()
      .tag(RootsAPI.Tags.Blocks.RUNED_OBSIDIAN, RootsAPI.Tags.Blocks.RUNE_CAPSTONES, BlockTags.MINEABLE_WITH_PICKAXE)
      .register();
  public static BlockEntry<BaseBlocks.StoneButtonBlock> RUNESTONE_BRICK_BUTTON = REGISTRATE.block("runestone_brick_button", BaseBlocks.StoneButtonBlock::new)
      .properties(o -> BlockBehaviour.Properties.copy(net.minecraft.world.level.block.Blocks.STONE_BUTTON))
      .blockstate(BlockstateGenerator.button(RUNESTONE_BRICK))
      .recipe((ctx, p) -> {
        p.singleItem(DataIngredient.items(ModBlocks.RUNESTONE_BRICK), ModBlocks.RUNESTONE_BRICK_BUTTON, 1, 1);
        p.stonecutting(DataIngredient.items(ModBlocks.RUNESTONE_BRICK), ModBlocks.RUNESTONE_BRICK_BUTTON);
      })
      .item()
      .model(ItemModelGenerator::inventoryModel)
      .tag(ItemTags.BUTTONS)
      .build()
      .tag(BlockTags.BUTTONS, BlockTags.MINEABLE_WITH_PICKAXE)
      .register();
  public static BlockEntry<BaseBlocks.PressurePlateBlock> RUNESTONE_BRICK_PRESSURE_PLATE = REGISTRATE.block("runestone_brick_pressure_plate", (p) -> new BaseBlocks.PressurePlateBlock(PressurePlateBlock.Sensitivity.MOBS, p))
      .properties(o -> BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_BLUE).noCollission().strength(0.5f).sound(SoundType.WOOD))
      .blockstate(BlockstateGenerator.pressurePlate(RUNESTONE_BRICK))
      .recipe((ctx, p) -> {
        ShapedRecipeBuilder.shaped(ctx.getEntry(), 1)
            .pattern("XX")
            .define('X', DataIngredient.items(ModBlocks.RUNESTONE_BRICK))
            .unlockedBy("has_runestone_brick", DataIngredient.items(ModBlocks.RUNESTONE_BRICK).getCritereon(p))
            .save(p, p.safeId(ctx.getEntry()));
        p.stonecutting(DataIngredient.items(ModBlocks.RUNESTONE_BRICK), ModBlocks.RUNESTONE_BRICK_PRESSURE_PLATE);
      })
      .item()
      .model(ItemModelGenerator::itemModel)
      .build()
      .tag(BlockTags.STONE_PRESSURE_PLATES, BlockTags.MINEABLE_WITH_PICKAXE)
      .register();
  public static BlockEntry<SlabBlock> RUNESTONE_BRICK_SLAB = REGISTRATE.block("runestone_brick_slab", SlabBlock::new)
      .properties(o -> BlockBehaviour.Properties.of(Material.STONE, MaterialColor.TERRACOTTA_BLUE).requiresCorrectToolForDrops().strength(1.5f, 6.0f))
      .blockstate(BlockstateGenerator.slab(RUNESTONE_BRICK))
      .recipe((ctx, p) -> p.slab(DataIngredient.items(ModBlocks.RUNESTONE_BRICK), ModBlocks.RUNESTONE_BRICK_SLAB, null, true))
      .item()
      .model(ItemModelGenerator::itemModel)
      .tag(ItemTags.SLABS)
      .build()
      .tag(BlockTags.SLABS, BlockTags.MINEABLE_WITH_PICKAXE)
      .loot((p, t) -> p.add(t, RegistrateBlockLootTables.createSlabItemTable(t)))
      .register();
  public static BlockEntry<StairBlock> RUNESTONE_BRICK_STAIRS = REGISTRATE.block("runestone_brick_stairs", (p) -> new StairBlock(ModBlocks.RUNESTONE_BRICK::getDefaultState, p))
      .properties(RUNESTONE_PROPERTIES)
      .blockstate(BlockstateGenerator.stairs(RUNESTONE_BRICK))
      .recipe((ctx, p) -> p.stairs(DataIngredient.items(ModBlocks.RUNESTONE_BRICK), ModBlocks.RUNESTONE_BRICK_STAIRS, null, true))
      .item()
      .model(ItemModelGenerator::itemModel)
      .tag(ItemTags.STAIRS)
      .build()
      .tag(BlockTags.STAIRS, BlockTags.MINEABLE_WITH_PICKAXE)
      .register();
  public static BlockEntry<WallBlock> RUNESTONE_BRICK_WALL = REGISTRATE.block("runestone_brick_wall", WallBlock::new)
      .properties(RUNESTONE_PROPERTIES)
      .blockstate(BlockstateGenerator.wall(RUNESTONE_BRICK))
      .recipe((ctx, p) -> p.wall(DataIngredient.items(ModBlocks.RUNESTONE_BRICK), ModBlocks.RUNESTONE_BRICK_WALL))
      .item()
      .model(ItemModelGenerator::inventoryModel)
      .tag(ItemTags.WALLS)
      .build()
      .tag(BlockTags.WALLS, BlockTags.MINEABLE_WITH_PICKAXE)
      .register();
  public static BlockEntry<BaseBlocks.NarrowPostBlock> RUNESTONE_BRICK_NARROW_POST = REGISTRATE.block("runestone_brick_narrow_post", BaseBlocks.NarrowPostBlock::new)
      .properties(RUNESTONE_PROPERTIES)
      .recipe((ctx, p) -> Roots.RECIPES.narrowPost(ModBlocks.RUNESTONE_BRICK, ModBlocks.RUNESTONE_BRICK_NARROW_POST, null, true, p))
      .blockstate(BlockstateGenerator.narrowPost(RUNESTONE_BRICK))
      .item()
      .model(ItemModelGenerator::itemModel)
      .build()
      .tag(BlockTags.MINEABLE_WITH_PICKAXE)
      .register();
  public static BlockEntry<BaseBlocks.WidePostBlock> RUNESTONE_BRICK_WIDE_POST = REGISTRATE.block("runestone_brick_wide_post", BaseBlocks.WidePostBlock::new)
      .properties(RUNESTONE_PROPERTIES)
      .recipe((ctx, p) -> Roots.RECIPES.widePost(ModBlocks.RUNESTONE_BRICK, ModBlocks.RUNESTONE_BRICK_WIDE_POST, null, true, p))
      .blockstate(BlockstateGenerator.widePost(RUNESTONE_BRICK))
      .item()
      .model(ItemModelGenerator::itemModel)
      .build()
      .tag(BlockTags.MINEABLE_WITH_PICKAXE)
      .register();
  public static BlockEntry<FenceBlock> RUNESTONE_BRICK_FENCE = REGISTRATE.block("runestone_brick_fence", FenceBlock::new)
      .properties(RUNESTONE_PROPERTIES)
      .recipe((ctx, p) -> {
            p.fence(DataIngredient.items(ModBlocks.RUNESTONE_BRICK), ModBlocks.RUNESTONE_BRICK_FENCE, null);
            p.stonecutting(DataIngredient.items(ModBlocks.RUNESTONE_BRICK), ModBlocks.RUNESTONE_BRICK_FENCE, 2);
          }
      )
      .blockstate(BlockstateGenerator.fence(RUNESTONE_BRICK))
      .item()
      .model(ItemModelGenerator::inventoryModel)
      .build()
      .tag(BlockTags.FENCES, net.minecraftforge.common.Tags.Blocks.FENCES, BlockTags.MINEABLE_WITH_PICKAXE)
      .register();
  public static BlockEntry<FenceGateBlock> RUNESTONE_BRICK_GATE = REGISTRATE.block("runestone_brick_gate", FenceGateBlock::new)
      .properties(RUNESTONE_PROPERTIES)
      .recipe((ctx, p) -> {
        p.fenceGate(DataIngredient.items(ModBlocks.RUNESTONE_BRICK), ModBlocks.RUNESTONE_BRICK_GATE, null);
        p.stonecutting(DataIngredient.items(ModBlocks.RUNESTONE_BRICK), ModBlocks.RUNESTONE_BRICK_GATE, 2);
      })
      .blockstate(BlockstateGenerator.gate(RUNESTONE_BRICK))
      .item()
      .model(ItemModelGenerator::itemModel)
      .build()
      .tag(BlockTags.FENCE_GATES, net.minecraftforge.common.Tags.Blocks.FENCE_GATES, BlockTags.UNSTABLE_BOTTOM_CENTER, BlockTags.MINEABLE_WITH_PICKAXE)
      .register();
  public static BlockEntry<Block> RUNESTONE_BRICK_ALT = REGISTRATE.block("runestone_brick_alt", Block::new)
      .properties(RUNESTONE_PROPERTIES)
      .recipe((ctx, p) -> Roots.RECIPES.twoByTwo(ModBlocks.CHISELED_RUNESTONE, ModBlocks.RUNESTONE_BRICK_ALT, null, 4, p))
      .item()
      .model(ItemModelGenerator::itemModel)
      .build()
      .tag(RootsAPI.Tags.Blocks.RUNED_OBSIDIAN, RootsAPI.Tags.Blocks.RUNE_PILLARS)
      .register();
  public static BlockEntry<BaseBlocks.StoneButtonBlock> RUNESTONE_BRICK_ALT_BUTTON = REGISTRATE.block("runestone_brick_alt_button", BaseBlocks.StoneButtonBlock::new)
      .properties(o -> BlockBehaviour.Properties.copy(net.minecraft.world.level.block.Blocks.STONE_BUTTON))
      .blockstate(BlockstateGenerator.button(RUNESTONE_BRICK_ALT))
      .recipe((ctx, p) -> {
        p.singleItem(DataIngredient.items(ModBlocks.RUNESTONE_BRICK_ALT), ModBlocks.RUNESTONE_BRICK_ALT_BUTTON, 1, 1);
        p.stonecutting(DataIngredient.items(ModBlocks.RUNESTONE_BRICK_ALT), ModBlocks.RUNESTONE_BRICK_ALT_BUTTON);
      })
      .item()
      .model(ItemModelGenerator::inventoryModel)
      .tag(ItemTags.BUTTONS)
      .build()
      .tag(BlockTags.BUTTONS, BlockTags.MINEABLE_WITH_PICKAXE)
      .register();
  public static BlockEntry<BaseBlocks.PressurePlateBlock> RUNESTONE_BRICK_ALT_PRESSURE_PLATE = REGISTRATE.block("runestone_brick_alt_pressure_plate", (p) -> new BaseBlocks.PressurePlateBlock(PressurePlateBlock.Sensitivity.MOBS, p))
      .properties(o -> BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_BLUE).noCollission().strength(0.5f).sound(SoundType.WOOD))
      .blockstate(BlockstateGenerator.pressurePlate(RUNESTONE_BRICK_ALT))
      .recipe((ctx, p) -> {
        ShapedRecipeBuilder.shaped(ctx.getEntry(), 1)
            .pattern("XX")
            .define('X', DataIngredient.items(ModBlocks.RUNESTONE_BRICK_ALT))
            .unlockedBy("has_runestone_brick_alt", DataIngredient.items(ModBlocks.RUNESTONE_BRICK_ALT).getCritereon(p))
            .save(p, p.safeId(ctx.getEntry()));
        p.stonecutting(DataIngredient.items(ModBlocks.RUNESTONE_BRICK_ALT), ModBlocks.RUNESTONE_BRICK_ALT_PRESSURE_PLATE);
      })
      .item()
      .model(ItemModelGenerator::itemModel)
      .build()
      .tag(BlockTags.STONE_PRESSURE_PLATES, BlockTags.MINEABLE_WITH_PICKAXE)
      .register();
  public static BlockEntry<SlabBlock> RUNESTONE_BRICK_ALT_SLAB = REGISTRATE.block("runestone_brick_alt_slab", SlabBlock::new)
      .properties(o -> BlockBehaviour.Properties.of(Material.STONE, MaterialColor.TERRACOTTA_BLUE).requiresCorrectToolForDrops().strength(1.5f, 6.0f))
      .blockstate(BlockstateGenerator.slab(RUNESTONE_BRICK_ALT))
      .recipe((ctx, p) -> p.slab(DataIngredient.items(ModBlocks.RUNESTONE_BRICK_ALT), ModBlocks.RUNESTONE_BRICK_ALT_SLAB, null, true))
      .item()
      .model(ItemModelGenerator::itemModel)
      .tag(ItemTags.SLABS)
      .build()
      .tag(BlockTags.SLABS, BlockTags.MINEABLE_WITH_PICKAXE)
      .loot((p, t) -> p.add(t, RegistrateBlockLootTables.createSlabItemTable(t)))
      .register();
  public static BlockEntry<StairBlock> RUNESTONE_BRICK_ALT_STAIRS = REGISTRATE.block("runestone_brick_alt_stairs", (p) -> new StairBlock(ModBlocks.RUNESTONE_BRICK_ALT::getDefaultState, p))
      .properties(RUNESTONE_PROPERTIES)
      .blockstate(BlockstateGenerator.stairs(RUNESTONE_BRICK_ALT))
      .recipe((ctx, p) -> p.stairs(DataIngredient.items(ModBlocks.RUNESTONE_BRICK_ALT), ModBlocks.RUNESTONE_BRICK_ALT_STAIRS, null, true))
      .item()
      .model(ItemModelGenerator::itemModel)
      .tag(ItemTags.STAIRS)
      .build()
      .tag(BlockTags.STAIRS, BlockTags.MINEABLE_WITH_PICKAXE)
      .register();
  public static BlockEntry<WallBlock> RUNESTONE_BRICK_ALT_WALL = REGISTRATE.block("runestone_brick_alt_wall", WallBlock::new)
      .properties(RUNESTONE_PROPERTIES)
      .blockstate(BlockstateGenerator.wall(RUNESTONE_BRICK_ALT))
      .recipe((ctx, p) -> p.wall(DataIngredient.items(ModBlocks.RUNESTONE_BRICK_ALT), ModBlocks.RUNESTONE_BRICK_ALT_WALL))
      .item()
      .model(ItemModelGenerator::inventoryModel)
      .tag(ItemTags.WALLS)
      .build()
      .tag(BlockTags.WALLS, BlockTags.MINEABLE_WITH_PICKAXE)
      .register();
  public static BlockEntry<BaseBlocks.NarrowPostBlock> RUNESTONE_BRICK_ALT_NARROW_POST = REGISTRATE.block("runestone_brick_alt_narrow_post", BaseBlocks.NarrowPostBlock::new)
      .properties(RUNESTONE_PROPERTIES)
      .recipe((ctx, p) -> Roots.RECIPES.narrowPost(ModBlocks.RUNESTONE_BRICK_ALT, ModBlocks.RUNESTONE_BRICK_ALT_NARROW_POST, null, true, p))
      .blockstate(BlockstateGenerator.narrowPost(RUNESTONE_BRICK_ALT))
      .item()
      .model(ItemModelGenerator::itemModel)
      .build()
      .tag(BlockTags.MINEABLE_WITH_PICKAXE)
      .register();
  public static BlockEntry<BaseBlocks.WidePostBlock> RUNESTONE_BRICK_ALT_WIDE_POST = REGISTRATE.block("runestone_brick_alt_wide_post", BaseBlocks.WidePostBlock::new)
      .properties(RUNESTONE_PROPERTIES)
      .recipe((ctx, p) -> Roots.RECIPES.widePost(ModBlocks.RUNESTONE_BRICK_ALT, ModBlocks.RUNESTONE_BRICK_ALT_WIDE_POST, null, true, p))
      .blockstate(BlockstateGenerator.widePost(RUNESTONE_BRICK_ALT))
      .item()
      .model(ItemModelGenerator::itemModel)
      .build()
      .tag(BlockTags.MINEABLE_WITH_PICKAXE)
      .register();
  public static BlockEntry<FenceBlock> RUNESTONE_BRICK_ALT_FENCE = REGISTRATE.block("runestone_brick_alt_fence", FenceBlock::new)
      .properties(RUNESTONE_PROPERTIES)
      .recipe((ctx, p) -> {
            p.fence(DataIngredient.items(ModBlocks.RUNESTONE_BRICK_ALT), ModBlocks.RUNESTONE_BRICK_ALT_FENCE, null);
            p.stonecutting(DataIngredient.items(ModBlocks.RUNESTONE_BRICK_ALT), ModBlocks.RUNESTONE_BRICK_ALT_FENCE, 2);
          }
      )
      .blockstate(BlockstateGenerator.fence(RUNESTONE_BRICK_ALT))
      .item()
      .model(ItemModelGenerator::inventoryModel)
      .build()
      .tag(BlockTags.FENCES, net.minecraftforge.common.Tags.Blocks.FENCES, BlockTags.MINEABLE_WITH_PICKAXE)
      .register();
  public static BlockEntry<FenceGateBlock> RUNESTONE_BRICK_ALT_GATE = REGISTRATE.block("runestone_brick_alt_gate", FenceGateBlock::new)
      .properties(RUNESTONE_PROPERTIES)
      .recipe((ctx, p) -> {
        p.fenceGate(DataIngredient.items(ModBlocks.RUNESTONE_BRICK_ALT), ModBlocks.RUNESTONE_BRICK_ALT_GATE, null);
        p.stonecutting(DataIngredient.items(ModBlocks.RUNESTONE_BRICK_ALT), ModBlocks.RUNESTONE_BRICK_ALT_GATE, 2);
      })
      .blockstate(BlockstateGenerator.gate(RUNESTONE_BRICK_ALT))
      .item()
      .model(ItemModelGenerator::itemModel)
      .build()
      .tag(BlockTags.FENCE_GATES, net.minecraftforge.common.Tags.Blocks.FENCE_GATES, BlockTags.UNSTABLE_BOTTOM_CENTER, BlockTags.MINEABLE_WITH_PICKAXE)
      .register();
  public static NonNullUnaryOperator<BlockBehaviour.Properties> RUNED_LOG_PROPERTIES = r -> BlockBehaviour.Properties.copy(net.minecraft.world.level.block.Blocks.OAK_LOG);
  public static BlockEntry<RotatedPillarBlock> RUNED_WILDWOOD_LOG = REGISTRATE.block("runed_wildwood_log", RotatedPillarBlock::new)
      .properties(RUNED_LOG_PROPERTIES)
      .blockstate((ctx, p) -> p.axisBlock(ctx.getEntry(), new ResourceLocation(RootsAPI.MODID, "block/runed_wildwood"), new ResourceLocation(RootsAPI.MODID, "block/wildwood_log_top")))
      .tag(RootsAPI.Tags.Blocks.WILDWOOD_LOGS, RootsAPI.Tags.Blocks.RUNED_WILDWOOD_LOG, BlockTags.MINEABLE_WITH_AXE)
      .item()
      .model(ItemModelGenerator::itemModel)
      .build()
      .register();
  public static BlockEntry<RotatedPillarBlock> RUNED_SPRUCE_LOG = REGISTRATE.block("runed_spruce_log", RotatedPillarBlock::new)
      .properties(RUNED_LOG_PROPERTIES)
      .blockstate((ctx, p) -> p.axisBlock(ctx.getEntry(), new ResourceLocation(RootsAPI.MODID, "block/runed_spruce"), new ResourceLocation("minecraft", "block/spruce_log_top")))
      .tag(BlockTags.SPRUCE_LOGS, RootsAPI.Tags.Blocks.RUNED_SPRUCE_LOG, BlockTags.MINEABLE_WITH_AXE)
      .item()
      .model(ItemModelGenerator::itemModel)
      .build()
      .register();
  public static BlockEntry<RotatedPillarBlock> RUNED_JUNGLE_LOG = REGISTRATE.block("runed_jungle_log", RotatedPillarBlock::new)
      .properties(RUNED_LOG_PROPERTIES)
      .blockstate((ctx, p) -> p.axisBlock(ctx.getEntry(), new ResourceLocation(RootsAPI.MODID, "block/runed_jungle"), new ResourceLocation("minecraft", "block/jungle_log_top")))
      .tag(BlockTags.JUNGLE_LOGS, RootsAPI.Tags.Blocks.RUNED_JUNGLE_LOG, BlockTags.MINEABLE_WITH_AXE)
      .item()
      .model(ItemModelGenerator::itemModel)
      .build()
      .register();
  public static BlockEntry<RotatedPillarBlock> RUNED_BIRCH_LOG = REGISTRATE.block("runed_birch_log", RotatedPillarBlock::new)
      .properties(RUNED_LOG_PROPERTIES)
      .blockstate((ctx, p) -> p.axisBlock(ctx.getEntry(), new ResourceLocation(RootsAPI.MODID, "block/runed_birch"), new ResourceLocation("minecraft", "block/birch_log_top")))
      .tag(BlockTags.BIRCH_LOGS, RootsAPI.Tags.Blocks.RUNED_BIRCH_LOG, BlockTags.MINEABLE_WITH_AXE)
      .item()
      .model(ItemModelGenerator::itemModel)
      .build()
      .register();
  public static BlockEntry<RotatedPillarBlock> RUNED_OAK_LOG = REGISTRATE.block("runed_oak_log", RotatedPillarBlock::new)
      .properties(RUNED_LOG_PROPERTIES)
      .blockstate((ctx, p) -> p.axisBlock(ctx.getEntry(), new ResourceLocation(RootsAPI.MODID, "block/runed_oak"), new ResourceLocation("minecraft", "block/oak_log_top")))
      .tag(BlockTags.OAK_LOGS, RootsAPI.Tags.Blocks.RUNED_OAK_LOG, BlockTags.MINEABLE_WITH_AXE)
      .item()
      .model(ItemModelGenerator::itemModel)
      .build()
      .register();
  public static BlockEntry<RotatedPillarBlock> RUNED_DARK_OAK_LOG = REGISTRATE.block("runed_dark_oak_log", RotatedPillarBlock::new)
      .properties(RUNED_LOG_PROPERTIES)
      .blockstate((ctx, p) -> p.axisBlock(ctx.getEntry(), new ResourceLocation(RootsAPI.MODID, "block/runed_dark_oak"), new ResourceLocation("minecraft", "block/dark_oak_log_top")))
      .tag(BlockTags.DARK_OAK_LOGS, RootsAPI.Tags.Blocks.RUNED_DARK_OAK_LOG, BlockTags.MINEABLE_WITH_AXE)
      .item()
      .model(ItemModelGenerator::itemModel)
      .build()
      .register();
  public static BlockEntry<RotatedPillarBlock> RUNED_ACACIA_LOG = REGISTRATE.block("runed_acacia_log", RotatedPillarBlock::new)
      .properties(RUNED_LOG_PROPERTIES)
      .blockstate((ctx, p) -> p.axisBlock(ctx.getEntry(), new ResourceLocation(RootsAPI.MODID, "block/runed_acacia"), new ResourceLocation("minecraft", "block/acacia_log_top")))
      .tag(BlockTags.ACACIA_LOGS, RootsAPI.Tags.Blocks.RUNED_ACACIA_LOG, BlockTags.MINEABLE_WITH_AXE)
      .item()
      .model(ItemModelGenerator::itemModel)
      .build()
      .register();
  public static NonNullUnaryOperator<BlockBehaviour.Properties> RUNED_STEM_PROPERTIES = r -> BlockBehaviour.Properties.copy(net.minecraft.world.level.block.Blocks.CRIMSON_STEM);
  public static BlockEntry<RotatedPillarBlock> RUNED_WARPED_STEM = REGISTRATE.block("runed_warped_stem", RotatedPillarBlock::new)
      .properties(RUNED_STEM_PROPERTIES)
      .blockstate((ctx, p) -> p.axisBlock(ctx.getEntry(), new ResourceLocation(RootsAPI.MODID, "block/runed_warped"), new ResourceLocation("minecraft", "block/warped_stem_top")))
      .tag(BlockTags.WARPED_STEMS, RootsAPI.Tags.Blocks.RUNED_WARPED_STEM, BlockTags.MINEABLE_WITH_AXE)
      .item()
      .model(ItemModelGenerator::itemModel)
      .build()
      .register();
  public static BlockEntry<RotatedPillarBlock> RUNED_CRIMSON_STEM = REGISTRATE.block("runed_crimson_stem", RotatedPillarBlock::new)
      .properties(RUNED_STEM_PROPERTIES)
      .blockstate((ctx, p) -> p.axisBlock(ctx.getEntry(), new ResourceLocation(RootsAPI.MODID, "block/runed_crimson"), new ResourceLocation("minecraft", "block/crimson_stem_top")))
      .tag(BlockTags.CRIMSON_STEMS, RootsAPI.Tags.Blocks.RUNED_CRIMSON_STEM, BlockTags.MINEABLE_WITH_AXE)
      .item()
      .model(ItemModelGenerator::itemModel)
      .build()
      .register();
  public static NonNullUnaryOperator<BlockBehaviour.Properties> WILDWOOD_PLANKS_PROPERTIES = r -> BlockBehaviour.Properties.copy(net.minecraft.world.level.block.Blocks.OAK_PLANKS);
  public static BlockEntry<Block> WILDWOOD_PLANKS = REGISTRATE.block("wildwood_planks", Block::new)
      .properties(WILDWOOD_PLANKS_PROPERTIES)
      .item()
      .model(ItemModelGenerator::itemModel)
      .build()
      .tag(BlockTags.PLANKS, BlockTags.MINEABLE_WITH_AXE)
      .register();
  public static BlockEntry<BaseBlocks.PressurePlateBlock> WILDWOOD_PRESSURE_PLATE = REGISTRATE.block("wildwood_pressure_plate", (p) -> new BaseBlocks.PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING, p))
      .properties(o -> BlockBehaviour.Properties.of(Material.WOOD, MaterialColor.COLOR_BLUE).noCollission().strength(0.5f).sound(SoundType.WOOD))
      .blockstate(BlockstateGenerator.pressurePlate(WILDWOOD_PLANKS))
      .recipe((ctx, p) -> ShapedRecipeBuilder.shaped(ctx.getEntry(), 1)
          .pattern("XX")
          .define('X', DataIngredient.items(ModBlocks.WILDWOOD_PLANKS))
          .unlockedBy("has_wildwood", DataIngredient.items(ModBlocks.WILDWOOD_PLANKS).getCritereon(p))
          .save(p, p.safeId(ctx.getEntry())))
      .item()
      .model(ItemModelGenerator::itemModel)
      .build()
      .tag(BlockTags.WOODEN_PRESSURE_PLATES, BlockTags.MINEABLE_WITH_AXE)
      .register();
  public static NonNullUnaryOperator<BlockBehaviour.Properties> WILDWOOD_LOG_PROPERTIES = r -> BlockBehaviour.Properties.copy(net.minecraft.world.level.block.Blocks.OAK_LOG);
  public static BlockEntry<RotatedPillarBlock> STRIPPED_WILDWOOD_WOOD = REGISTRATE.block("stripped_wildwood_wood", Material.WOOD, RotatedPillarBlock::new)
      .properties(WILDWOOD_LOG_PROPERTIES)
      .blockstate((ctx, p) -> p.axisBlock(ctx.getEntry(), new ResourceLocation(RootsAPI.MODID, "block/stripped_wildwood_log"), new ResourceLocation(RootsAPI.MODID, "block/stripped_wildwood_log")))
      .item()
      .model(ItemModelGenerator::itemModel)
      .build()
      .tag(RootsAPI.Tags.Blocks.WILDWOOD_LOGS, BlockTags.MINEABLE_WITH_AXE)
      .register();
  public static BlockEntry<RotatedPillarBlock> WILDWOOD_WOOD = REGISTRATE.block("wildwood_wood", Material.WOOD, RotatedPillarBlock::new)
      .properties(WILDWOOD_LOG_PROPERTIES)
      .blockstate((ctx, p) -> p.axisBlock(ctx.getEntry(), new ResourceLocation(RootsAPI.MODID, "block/wildwood_log"), new ResourceLocation(RootsAPI.MODID, "block/wildwood_log")))
      .item()
      .model(ItemModelGenerator::itemModel)
      .build()
      .tag(RootsAPI.Tags.Blocks.WILDWOOD_LOGS, BlockTags.MINEABLE_WITH_AXE)
      .register();
  public static BlockEntry<RotatedPillarBlock> STRIPPED_WILDWOOD_LOG = REGISTRATE.block("stripped_wildwood_log", Material.WOOD, RotatedPillarBlock::new)
      .properties(WILDWOOD_LOG_PROPERTIES)
      .blockstate((ctx, p) -> p.logBlock(ctx.getEntry()))
      .item()
      .model(ItemModelGenerator::itemModel)
      .build()
      .tag(RootsAPI.Tags.Blocks.WILDWOOD_LOGS, BlockTags.MINEABLE_WITH_AXE)
      .register();
  public static BlockEntry<RotatedPillarBlock> WILDWOOD_LOG = REGISTRATE.block("wildwood_log", Material.WOOD, RotatedPillarBlock::new)
      .properties(WILDWOOD_LOG_PROPERTIES)
      .blockstate((ctx, p) -> p.logBlock(ctx.getEntry()))
      .item()
      .model(ItemModelGenerator::itemModel)
      .build()
      .tag(RootsAPI.Tags.Blocks.WILDWOOD_LOGS, BlockTags.MINEABLE_WITH_AXE)
      .register();
  public static NonNullUnaryOperator<BlockBehaviour.Properties> WILDWOOD_LEAVES_PROPERTIES = r -> BlockBehaviour.Properties.copy(net.minecraft.world.level.block.Blocks.OAK_LEAVES);
  public static BlockEntry<Block> WILDWOOD_LEAVES = REGISTRATE.block("wildwood_leaves", Block::new)
      .properties(WILDWOOD_LEAVES_PROPERTIES)
      .item()
      .model(ItemModelGenerator::itemModel)
      .build()
      .tag(BlockTags.LEAVES, BlockTags.MINEABLE_WITH_HOE)
      .register();
  public static BlockEntry<BaseBlocks.WoodButtonBlock> WILDWOOD_BUTTON = REGISTRATE.block("wildwood_button", BaseBlocks.WoodButtonBlock::new)
      .properties(o -> BlockBehaviour.Properties.copy(net.minecraft.world.level.block.Blocks.OAK_BUTTON))
      .blockstate(BlockstateGenerator.button(WILDWOOD_PLANKS))
      .recipe((ctx, p) -> p.singleItem(DataIngredient.items(ModBlocks.WILDWOOD_PLANKS), ModBlocks.WILDWOOD_BUTTON, 1, 1))
      .item()
      .model(ItemModelGenerator::inventoryModel)
      .tag(ItemTags.BUTTONS)
      .build()
      .tag(BlockTags.WOODEN_BUTTONS, BlockTags.MINEABLE_WITH_AXE)
      .register();
  public static BlockEntry<SlabBlock> WILDWOOD_SLAB = REGISTRATE.block("wildwood_slab", SlabBlock::new)
      .properties(o -> BlockBehaviour.Properties.copy(net.minecraft.world.level.block.Blocks.OAK_PLANKS).requiresCorrectToolForDrops())
      .blockstate(BlockstateGenerator.slab(WILDWOOD_PLANKS))
      .recipe((ctx, p) -> p.slab(DataIngredient.items(ModBlocks.WILDWOOD_PLANKS), ModBlocks.WILDWOOD_SLAB, null, false))
      .item()
      .model(ItemModelGenerator::itemModel)
      .tag(ItemTags.SLABS)
      .build()
      .tag(BlockTags.WOODEN_SLABS, BlockTags.MINEABLE_WITH_AXE)
      .loot((p, t) -> p.add(t, RegistrateBlockLootTables.createSlabItemTable(t)))
      .register();
  public static BlockEntry<StairBlock> WILDWOOD_STAIRS = REGISTRATE.block("wildwood_stairs", (p) -> new StairBlock(ModBlocks.WILDWOOD_PLANKS::getDefaultState, p))
      .properties(WILDWOOD_PLANKS_PROPERTIES)
      .blockstate(BlockstateGenerator.stairs(WILDWOOD_PLANKS))
      .recipe((ctx, p) -> p.stairs(DataIngredient.items(ModBlocks.WILDWOOD_PLANKS), ModBlocks.WILDWOOD_STAIRS, null, false))
      .item()
      .model(ItemModelGenerator::itemModel)
      .tag(ItemTags.STAIRS)
      .build()
      .tag(BlockTags.WOODEN_STAIRS, BlockTags.MINEABLE_WITH_AXE)
      .register();
  public static BlockEntry<WallBlock> WILDWOOD_WALL = REGISTRATE.block("wildwood_wall", WallBlock::new)
      .properties(WILDWOOD_PLANKS_PROPERTIES)
      .blockstate(BlockstateGenerator.wall(WILDWOOD_PLANKS))
      .recipe((ctx, p) -> p.wall(DataIngredient.items(ModBlocks.WILDWOOD_PLANKS), ModBlocks.WILDWOOD_WALL))
      .item()
      .model(ItemModelGenerator::inventoryModel)
      .tag(ItemTags.WALLS)
      .build()
      .tag(BlockTags.WALLS, BlockTags.MINEABLE_WITH_AXE)
      .register();
  public static BlockEntry<BaseBlocks.NarrowPostBlock> WILDWOOD_NARROW_POST = REGISTRATE.block("wildwood_narrow_post", BaseBlocks.NarrowPostBlock::new)
      .properties(WILDWOOD_PLANKS_PROPERTIES)
      .recipe((ctx, p) -> Roots.RECIPES.narrowPost(ModBlocks.WILDWOOD_PLANKS, ModBlocks.WILDWOOD_NARROW_POST, null, false, p))
      .blockstate(BlockstateGenerator.narrowPost(WILDWOOD_PLANKS))
      .item()
      .model(ItemModelGenerator::itemModel)
      .build()
      .tag(BlockTags.MINEABLE_WITH_AXE)
      .register();
  public static BlockEntry<BaseBlocks.WidePostBlock> WILDWOOD_WIDE_POST = REGISTRATE.block("wildwood_wide_post", BaseBlocks.WidePostBlock::new)
      .properties(WILDWOOD_PLANKS_PROPERTIES)
      .recipe((ctx, p) -> Roots.RECIPES.widePost(ModBlocks.WILDWOOD_PLANKS, ModBlocks.WILDWOOD_WIDE_POST, null, false, p))
      .blockstate(BlockstateGenerator.widePost(WILDWOOD_PLANKS))
      .item()
      .model(ItemModelGenerator::itemModel)
      .build()
      .tag(BlockTags.MINEABLE_WITH_AXE)
      .register();
  public static BlockEntry<FenceBlock> WILDWOOD_FENCE = REGISTRATE.block("wildwood_fence", FenceBlock::new)
      .properties(WILDWOOD_PLANKS_PROPERTIES)
      .recipe((ctx, p) -> p.fence(DataIngredient.items(ModBlocks.WILDWOOD_PLANKS), ModBlocks.WILDWOOD_FENCE, null)
      )
      .blockstate(BlockstateGenerator.fence(WILDWOOD_PLANKS))
      .item()
      .model(ItemModelGenerator::inventoryModel)
      .build()
      .tag(BlockTags.WOODEN_FENCES, net.minecraftforge.common.Tags.Blocks.FENCES_WOODEN, BlockTags.MINEABLE_WITH_AXE)
      .register();
  public static BlockEntry<FenceGateBlock> WILDWOOD_GATE = REGISTRATE.block("wildwood_gate", FenceGateBlock::new)
      .properties(WILDWOOD_PLANKS_PROPERTIES)
      .recipe((ctx, p) -> p.fenceGate(DataIngredient.items(ModBlocks.WILDWOOD_PLANKS), ModBlocks.WILDWOOD_GATE, null))
      .blockstate(BlockstateGenerator.gate(WILDWOOD_PLANKS))
      .item()
      .model(ItemModelGenerator::itemModel)
      .build()
      .tag(BlockTags.FENCE_GATES, net.minecraftforge.common.Tags.Blocks.FENCE_GATES, BlockTags.UNSTABLE_BOTTOM_CENTER, net.minecraftforge.common.Tags.Blocks.FENCE_GATES_WOODEN, BlockTags.MINEABLE_WITH_AXE)
      .register();

  public static class Crops {
    public static BlockEntry<ThreeStageCropBlock> WILDROOT_CROP = REGISTRATE.block("wildroot_crop", (p) -> new ThreeStageCropBlock(p, () -> ModItems.Herbs.WILDROOT))
        .properties(o -> BlockBehaviour.Properties.copy(net.minecraft.world.level.block.Blocks.WHEAT))
        .blockstate(BlockstateGenerator::cropBlockstate)
        .tag(RootsAPI.Tags.Blocks.WILDROOT_CROP, BlockTags.MINEABLE_WITH_HOE)
        .register();

    public static BlockEntry<ElementalCropBlock> CLOUD_BERRY_CROP = REGISTRATE.block("cloud_berry_crop", (p) -> new ElementalCropBlock(p, () -> ModItems.Herbs.CLOUD_BERRY))
        .properties(o -> BlockBehaviour.Properties.copy(net.minecraft.world.level.block.Blocks.WHEAT))
        .blockstate(BlockstateGenerator::cropBlockstate)
        .tag(RootsAPI.Tags.Blocks.CLOUD_BERRY_CROP, BlockTags.MINEABLE_WITH_HOE)
        .register();

    public static BlockEntry<WaterElementalCropBlock> DEWGONIA_CROP = REGISTRATE.block("dewgonia_crop", (p) -> new WaterElementalCropBlock(p, () -> ModItems.Herbs.DEWGONIA))
        .properties(o -> BlockBehaviour.Properties.copy(net.minecraft.world.level.block.Blocks.WHEAT))
        .blockstate(BlockstateGenerator::cropBlockstate)
        .tag(RootsAPI.Tags.Blocks.DEWGONIA_CROP, BlockTags.MINEABLE_WITH_HOE)
        .register();

    public static BlockEntry<ElementalCropBlock> INFERNO_BULB_CROP = REGISTRATE.block("inferno_bulb_crop", (p) -> new ElementalCropBlock(p, () -> ModItems.Herbs.INFERNO_BULB))
        .properties(o -> BlockBehaviour.Properties.copy(net.minecraft.world.level.block.Blocks.WHEAT))
        .blockstate(BlockstateGenerator::crossBlockstate)
        .tag(RootsAPI.Tags.Blocks.INFERNO_BULB_CROP, BlockTags.MINEABLE_WITH_HOE)
        .register();

    public static BlockEntry<ElementalCropBlock> STALICRIPE_CROP = REGISTRATE.block("stalicripe_crop", (p) -> new ElementalCropBlock(p, () -> ModItems.Herbs.STALICRIPE))
        .properties(o -> BlockBehaviour.Properties.copy(net.minecraft.world.level.block.Blocks.WHEAT))
        .blockstate(BlockstateGenerator::cropBlockstate)
        .tag(RootsAPI.Tags.Blocks.STALICRIPE_CROP, BlockTags.MINEABLE_WITH_HOE)
        .register();

    public static BlockEntry<SeededCropsBlock> MOONGLOW_CROP = REGISTRATE.block("moonglow_crop", (p) -> new SeededCropsBlock(p, () -> ModItems.Seeds.MOONGLOW_SEEDS))
        .properties(o -> BlockBehaviour.Properties.copy(net.minecraft.world.level.block.Blocks.WHEAT))
        .blockstate(BlockstateGenerator::cropBlockstate)
        .tag(RootsAPI.Tags.Blocks.MOONGLOW_CROP, BlockTags.MINEABLE_WITH_HOE)
        .register();
    public static BlockEntry<SeededCropsBlock> PERESKIA_CROP = REGISTRATE.block("pereskia_crop", (p) -> new SeededCropsBlock(p, () -> ModItems.Seeds.PERESKIA_BULB))
        .properties(o -> BlockBehaviour.Properties.copy(net.minecraft.world.level.block.Blocks.WHEAT))
        .blockstate(BlockstateGenerator::crossBlockstate)
        .tag(RootsAPI.Tags.Blocks.PERESKIA_CROP, BlockTags.MINEABLE_WITH_HOE)
        .register();
    public static BlockEntry<ThreeStageCropBlock> SPIRITLEAF_CROP = REGISTRATE.block("spiritleaf_crop", (p) -> new ThreeStageCropBlock(p, () -> ModItems.Seeds.SPIRITLEAF_SEEDS))
        .properties(o -> BlockBehaviour.Properties.copy(net.minecraft.world.level.block.Blocks.WHEAT))
        .blockstate(BlockstateGenerator::cropBlockstate)
        .tag(RootsAPI.Tags.Blocks.SPIRITLEAF_CROP, BlockTags.MINEABLE_WITH_HOE)
        .register();
    public static BlockEntry<SeededCropsBlock> WILDEWHEET_CROP = REGISTRATE.block("wildewheet_crop", (p) -> new SeededCropsBlock(p, () -> ModItems.Seeds.WILDEWHEET_SEEDS))
        .properties(o -> BlockBehaviour.Properties.copy(net.minecraft.world.level.block.Blocks.WHEAT))
        .blockstate(BlockstateGenerator::cropBlockstate)
        .tag(RootsAPI.Tags.Blocks.WILDEWHEET_CROP, BlockTags.MINEABLE_WITH_HOE)
        .register();

    public static void load() {
    }
  }

  public static class Soils {
    public static NonNullUnaryOperator<BlockBehaviour.Properties> SOIL_PROPERTIES = r -> BlockBehaviour.Properties.copy(net.minecraft.world.level.block.Blocks.DIRT);

    public static BlockEntry<Block> AQUEOUS_SOIL = REGISTRATE.block("aqueous_soil", Block::new)
        .properties(SOIL_PROPERTIES)
        .blockstate(BlockstateGenerator.pillar("block/water_soil_side", "block/water_soil_top"))
        .item()
        .model(ItemModelGenerator::itemModel)
        .build()
        .tag(RootsAPI.Tags.Blocks.WATER_SOIL, BlockTags.MINEABLE_WITH_SHOVEL)
        .register();

    public static BlockEntry<Block> CAELIC_SOIL = REGISTRATE.block("caelic_soil", Block::new)
        .properties(SOIL_PROPERTIES)
        .blockstate(BlockstateGenerator.pillar("block/air_soil_side", "block/air_soil_top"))
        .item()
        .model(ItemModelGenerator::itemModel)
        .build()
        .tag(RootsAPI.Tags.Blocks.AIR_SOIL, BlockTags.MINEABLE_WITH_SHOVEL)
        .register();

    public static BlockEntry<Block> ELEMENTAL_SOIL = REGISTRATE.block("elemental_soil", Block::new)
        .properties(SOIL_PROPERTIES)
        .item()
        .model(ItemModelGenerator::itemModel)
        .build()
        .tag(RootsAPI.Tags.Blocks.ELEMENTAL_SOIL, BlockTags.MINEABLE_WITH_SHOVEL)
        .register();

    public static BlockEntry<Block> MAGMATIC_SOIL = REGISTRATE.block("magmatic_soil", Block::new)
        .properties(SOIL_PROPERTIES)
        .blockstate(BlockstateGenerator.pillar("block/fire_soil_side", "block/fire_soil_top"))
        .item()
        .model(ItemModelGenerator::itemModel)
        .build()
        .tag(RootsAPI.Tags.Blocks.FIRE_SOIL, BlockTags.MINEABLE_WITH_SHOVEL)
        .register();

    public static BlockEntry<Block> TERRAN_SOIL = REGISTRATE.block("terran_soil", Block::new)
        .properties(SOIL_PROPERTIES)
        .blockstate(BlockstateGenerator.pillar("block/earth_soil_side", "block/earth_soil_top"))
        .item()
        .model(ItemModelGenerator::itemModel)
        .build()
        .tag(RootsAPI.Tags.Blocks.EARTH_SOIL, BlockTags.MINEABLE_WITH_SHOVEL)
        .register();

    public static void load() {
    }
  }

  public static final NonNullUnaryOperator<BlockBehaviour.Properties> BASE_PROPERTIES = r -> r.dynamicShape().noOcclusion().strength(1.5f).sound(SoundType.STONE);
  public static final NonNullUnaryOperator<BlockBehaviour.Properties> BASE_WOODEN_PROPERTIES = r -> BASE_PROPERTIES.apply(r).sound(SoundType.WOOD);
  public static final NonNullUnaryOperator<BlockBehaviour.Properties> BASE_REINFORCED_PROPERTIES = r -> BASE_PROPERTIES.apply(r).requiresCorrectToolForDrops().strength(50.0F, 1200.0F);

  // TODO all: voxel shapes & bounding boxes

  // TODO: Blockstate
  public static BlockEntry<FeyLightBlock> FEY_LIGHT = REGISTRATE.block("fey_light", FeyLightBlock::new)
      .properties(o -> BlockBehaviour.Properties.copy(net.minecraft.world.level.block.Blocks.TORCH).lightLevel(l -> 15).sound(SoundType.WOOL))
      .blockstate((ctx, p) -> {
        ModelFile model = p.models().cubeAll(ctx.getName(), new ResourceLocation(RootsAPI.MODID, "block/grove_padding"));
        p.getVariantBuilder(ctx.getEntry()).forAllStates(state -> ConfiguredModel.builder().modelFile(model).build());
      })
      .tag(BlockTags.MINEABLE_WITH_AXE)
      .register();

  public static BlockEntry<RitualPedestalBlock> RITUAL_PEDESTAL = REGISTRATE.block("ritual_pedestal", Material.STONE, RitualPedestalBlock::new)
      .properties(BASE_PROPERTIES)
      .blockstate(BlockstateGenerator.existingNoRotation("block/complex/ritual_pedestal"))
      .tag(RootsAPI.Tags.Blocks.PEDESTALS, RootsAPI.Tags.Blocks.RITUAL_PEDESTALS, BlockTags.MINEABLE_WITH_PICKAXE)
      .item()
      .model(ItemModelGenerator::complexItemModel)
      .tag(RootsAPI.Tags.Items.Blocks.PEDESTALS, RootsAPI.Tags.Items.Blocks.RITUAL_PEDESTALS)
      .build()
      .register();

  public static BlockEntry<RitualPedestalBlock> REINFORCED_RITUAL_PEDESTAL = REGISTRATE.block("reinforced_ritual_pedestal", Material.STONE, RitualPedestalBlock::new)
      .properties(BASE_REINFORCED_PROPERTIES)
      .blockstate(BlockstateGenerator.existingNoRotation("block/complex/reinforced_ritual_pedestal"))
      .tag(BlockTags.WITHER_IMMUNE, BlockTags.DRAGON_IMMUNE, RootsAPI.Tags.Blocks.PEDESTALS, RootsAPI.Tags.Blocks.RITUAL_PEDESTALS, BlockTags.MINEABLE_WITH_PICKAXE)
      .item()
      .model(ItemModelGenerator::complexItemModel)
      .tag(RootsAPI.Tags.Items.Blocks.PEDESTALS, RootsAPI.Tags.Items.Blocks.RITUAL_PEDESTALS)
      .build()
      .register();

  public static BlockEntry<GroveCrafterBlock> GROVE_CRAFTER = REGISTRATE.block("grove_crafter", Material.WOOD, GroveCrafterBlock::new)
      .properties(BASE_WOODEN_PROPERTIES)
      .blockstate(BlockstateGenerator.existingNoRotation("block/complex/grove_crafter"))
      .tag(RootsAPI.Tags.Blocks.CRAFTERS, BlockTags.MINEABLE_WITH_AXE)
      .item()
      .model(ItemModelGenerator::complexItemModel)
      .tag(RootsAPI.Tags.Items.Blocks.CRAFTERS)
      .build()
      .register();

  public static BlockEntry<GrovePedestalBlock> GROVE_PEDESTAL = REGISTRATE.block("grove_pedestal", Material.WOOD, GrovePedestalBlock::new)
      .properties(BASE_WOODEN_PROPERTIES)
      .blockstate(BlockstateGenerator.existingNoRotation("block/complex/grove_pedestal"))
      .tag(RootsAPI.Tags.Blocks.PEDESTALS, RootsAPI.Tags.Blocks.GROVE_PEDESTALS, BlockTags.MINEABLE_WITH_PICKAXE)
      .item()
      .model(ItemModelGenerator::complexItemModel)
      .tag(RootsAPI.Tags.Items.Blocks.PEDESTALS, RootsAPI.Tags.Items.Blocks.GROVE_PEDESTALS)
      .build()
      .register();

  public static BlockEntry<Block> WILD_ROOTS = REGISTRATE.block("wild_roots", Material.GRASS, Block::new)
      .properties(BASE_WOODEN_PROPERTIES)
      .blockstate((ctx, p) -> {
      })
      .tag(BlockTags.MINEABLE_WITH_HOE)
      .register();

  public static BlockEntry<Block> GROVE_MOSS = REGISTRATE.block("creeping_grove_moss", Material.GRASS, Block::new)
      .properties(BASE_WOODEN_PROPERTIES)
      .blockstate(BlockstateGenerator.existingNoRotation("block/moss"))
      .tag(BlockTags.MINEABLE_WITH_HOE)
      .register();

  public static BlockEntry<Block> BAFFLECAP_BLOCK = REGISTRATE.block("bafflecap_block", Material.WOOD, Block::new)
      .properties(o -> BlockBehaviour.Properties.copy(Blocks.BROWN_MUSHROOM_BLOCK))
      .lang("Bafflecap")
      .blockstate((ctx, p) -> {
        // TODO: this really shouldn't be like this
        ModelFile crop = p.models().getExistingFile(new ResourceLocation("minecraft", "block/cross"));
        p.getVariantBuilder(ctx.getEntry())
            .forAllStates(state -> {
              ModelFile stage = p.models().getBuilder("block/bafflecap_block")
                  .parent(crop)
                  .texture("cross", p.modLoc("block/bafflecap_block"));
              return ConfiguredModel.builder().modelFile(stage).build();
            });
      })
      .tag(BlockTags.MINEABLE_WITH_HOE)
      .register();

  public static BlockEntry<RunicCrafterBlock> RUNIC_CRAFTER = REGISTRATE.block("runic_crafter", Material.STONE, RunicCrafterBlock::new)
      .properties(BASE_PROPERTIES)
      .blockstate(BlockstateGenerator.existingNoRotation("block/complex/runic_crafter"))
      .tag(RootsAPI.Tags.Blocks.CRAFTERS, BlockTags.MINEABLE_WITH_PICKAXE)
      .item()
      .model(ItemModelGenerator::complexItemModel)
      .tag(RootsAPI.Tags.Items.Blocks.CRAFTERS)
      .build()
      .register();

  public static <T extends Block> NonNullBiConsumer<DataGenContext<Block, T>, RegistrateBlockstateProvider> groveStone(String type) {
    return (ctx, p) -> p.getVariantBuilder(ctx.getEntry()).forAllStates(state -> {
      BlockModelBuilder model;
      boolean valid = state.getValue(GroveStoneBlock.VALID);

      switch (state.getValue(GroveStoneBlock.PART)) {
        case MIDDLE:
          model = p.models().withExistingParent(type + "_grove_stone_middle" + (valid ? "_valid" : ""), new ResourceLocation(RootsAPI.MODID, "block/complex/grove_stone_middle"));
          break;
        case BOTTOM:
          model = p.models().withExistingParent(type + "_grove_stone_bottom" + (valid ? "_valid" : ""), new ResourceLocation(RootsAPI.MODID, "block/complex/grove_stone_bottom"));
          break;
        default:
        case TOP:
          model = p.models().withExistingParent(type + "_grove_stone_top" + (valid ? "_valid" : ""), new ResourceLocation(RootsAPI.MODID, "block/complex/grove_stone_top"));
          break;
      }

      ResourceLocation active = new ResourceLocation(RootsAPI.MODID, type.equals("primal") ? "block/ob_stone_active" : "block/ob_stone_active_" + type);

      if (valid) {
        model.texture("monolith", active);
        model.texture("particle", active);
      }

      if (type.equals("primal")) {
        p.models().withExistingParent(type + "_grove_stone_inventory", new ResourceLocation(RootsAPI.MODID, "block/complex/grove_stone_full"));
      } else {
        p.models().withExistingParent(type + "_grove_stone_inventory", new ResourceLocation(RootsAPI.MODID, "block/complex/grove_stone_full")).texture("monolith", active).texture("particle", active);
      }

      Direction dir = state.getValue(GroveStoneBlock.FACING);

      return ConfiguredModel.builder()
          .modelFile(model)
          .rotationX(0)
          .rotationY(dir.getAxis().isVertical() ? 0 : (int) (dir.toYRot() % 360))
          .build();

    });
  }

  public static BlockEntry<GroveStoneBlock> PRIMAL_GROVE_STONE = REGISTRATE.block("primal_grove_stone", Material.STONE, GroveStoneBlock::new)
      .properties(BASE_PROPERTIES)
      .blockstate(groveStone("primal"))
      .tag(RootsAPI.Tags.Blocks.GROVE_STONE_PRIMAL, BlockTags.MINEABLE_WITH_PICKAXE)
      .item()
      .model((ctx, p) -> p.blockWithInventoryModel(ctx::getEntry))
      .tag(RootsAPI.Tags.Items.Blocks.GROVE_STONE_PRIMAL)
      .build()
      .register();

  public static BlockEntry<IncenseBurnerBlock> INCENSE_BURNER = REGISTRATE.block("incense_burner", Material.STONE, IncenseBurnerBlock::new)
      .blockstate(BlockstateGenerator.existingNoRotation("block/complex/incense_burner"))
      .properties(BASE_PROPERTIES)
      .tag(RootsAPI.Tags.Blocks.PEDESTALS, BlockTags.MINEABLE_WITH_PICKAXE)
      .item()
      .model(ItemModelGenerator::complexItemModel)
      .tag(RootsAPI.Tags.Items.Blocks.PEDESTALS)
      .build()
      .register();

  public static BlockEntry<MortarBlock> MORTAR = REGISTRATE.block("mortar", Material.STONE, MortarBlock::new)
      .properties(BASE_PROPERTIES)
      .blockstate(BlockstateGenerator.existingNoRotation("block/complex/mortar"))
      .tag(RootsAPI.Tags.Blocks.MORTARS, BlockTags.MINEABLE_WITH_PICKAXE)
      .item()
      .model(ItemModelGenerator::complexItemModel)
      .tag(RootsAPI.Tags.Items.Blocks.MORTARS)
      .build()
      .register();

  public static BlockEntry<PyreBlock> PYRE = REGISTRATE.block("pyre", Material.WOOD, PyreBlock::new)
      .properties(BASE_WOODEN_PROPERTIES)
      .blockstate(BlockstateGenerator.existingNoRotation("block/complex/pyre"))
      .tag(RootsAPI.Tags.Blocks.PYRES, BlockTags.MINEABLE_WITH_AXE)
      .item()
      .model(ItemModelGenerator::complexItemModel)
      .tag(RootsAPI.Tags.Items.Blocks.PYRES)
      .build()
      .register();

  public static BlockEntry<PyreBlock> REINFORCED_PYRE = REGISTRATE.block("reinforced_pyre", Material.STONE, PyreBlock::new)
      .properties(BASE_REINFORCED_PROPERTIES)
      .blockstate(BlockstateGenerator.existingNoRotation("block/complex/reinforced_pyre"))
      .tag(BlockTags.WITHER_IMMUNE, BlockTags.DRAGON_IMMUNE, RootsAPI.Tags.Blocks.PYRES, BlockTags.MINEABLE_WITH_PICKAXE)
      .item()
      .model(ItemModelGenerator::complexItemModel)
      .tag(RootsAPI.Tags.Items.Blocks.PYRES)
      .build()
      .register();

  public static BlockEntry<DecorativePyreBlock> DECORATIVE_PYRE = REGISTRATE.block("decorative_pyre", Material.WOOD, DecorativePyreBlock::new)
      .properties(BASE_WOODEN_PROPERTIES.andThen(o -> o.lightLevel((state) -> 15)))
      .blockstate(BlockstateGenerator.existingNoRotation("block/complex/pyre"))
      .tag(RootsAPI.Tags.Blocks.PYRES, BlockTags.MINEABLE_WITH_AXE)
      .item()
      .model((ctx, p) -> p.withExistingParent(p.name(ctx::getEntry), new ResourceLocation(RootsAPI.MODID, "block/complex/pyre")))
      .tag(RootsAPI.Tags.Items.Blocks.PYRES)
      .build()
      .register();

  public static BlockEntry<UnendingBowlBlock> UNENDING_BOWL = REGISTRATE.block("unending_bowl", Material.STONE, UnendingBowlBlock::new)
      .properties(BASE_PROPERTIES)
      .blockstate(BlockstateGenerator.existingNoRotation("block/complex/unending_bowl"))
      .tag(BlockTags.MINEABLE_WITH_PICKAXE)
      .item()
      .model(ItemModelGenerator::complexItemModel)
      .build()
      .register();

  public static void load() {
    Soils.load();
    Crops.load();
  }
}
