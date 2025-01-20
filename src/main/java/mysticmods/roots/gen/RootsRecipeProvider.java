package mysticmods.roots.gen;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.attachment.Unlock;
import mysticmods.roots.api.recipe.BaseRecipeData;
import mysticmods.roots.api.recipe.WorldCondition;
import mysticmods.roots.api.test.world.TagMatchWorldTest;
import mysticmods.roots.init.*;
import mysticmods.roots.recipe.bark.BarkRecipe;
import mysticmods.roots.recipe.bark.DynamicBarkRecipe;
import mysticmods.roots.recipe.grove.GroveRecipe;
import mysticmods.roots.recipe.mortar.MortarRecipe;
import mysticmods.roots.recipe.pyre.PyreRecipe;
import mysticmods.roots.recipe.runic.RunicEntityRecipe;
import mysticmods.roots.test.entity.EntityTagTest;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.crafting.DifferenceIngredient;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class RootsRecipeProvider extends RecipeProvider {
  public RootsRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
    super(output, registries);
  }

  @Override
  protected void buildRecipes(RecipeOutput c, HolderLookup.Provider p) {
    ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.THATCH.get(), 32)
        .pattern("XY")
        .pattern("YX")
        .define('X', Blocks.HAY_BLOCK)
        .define('Y', Tags.Items.CROPS_WHEAT)
        .unlockedBy("has_hay", has(Blocks.HAY_BLOCK))
        .unlockedBy("has_wheat", has(Items.WHEAT))
        .save(c);

    ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.RUNESTONE.get(), 3)
        .pattern("SSS")
        .pattern("SHS")
        .pattern("SSS")
        .define('S', RootsTags.Items.STONELIKE)
        .define('H', RootsTags.Items.RUNESTONE_HERBS)
        .unlockedBy("has_herb", has(RootsTags.Items.RUNESTONE_HERBS))
        .save(c, RootsAPI.rl("runestone_simple_crafting"));

    // Grove Recipe builder

    ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, ModBlocks.MOSSY_RUNESTONE.get())
        .requires(RootsTags.Items.RUNESTONE).requires(RootsTags.Items.GROVE_MOSS_CROP)
        .unlockedBy("has_grove_moss", has(RootsTags.Items.GROVE_MOSS_CROP))
        .save(c, RootsAPI.rl("mossy_runestone_from_runestone_grove_moss"));

/*    ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.MOSSY_RUNESTONE.get())
        .pattern("VVV")
        .pattern("VSV")
        .pattern("VVV")
        .define('S', RootsTags.Items.RUNESTONE)
        // TODO: Tag-ify
        .define('V', Ingredient.of(Items.VINE))
        .unlockedBy("has_runestone", has(RootsTags.Items.RUNESTONE))
        .save(c, RootsAPI.rl("mossy_runestone_from_runestone_vine"));*/

    ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.CHISELED_RUNESTONE.get(), 4)
        .pattern("XX")
        .pattern("XX")
        .define('X', ModBlocks.RUNESTONE_TILE.get())
        .unlockedBy("has_runestone_tile", has(ModBlocks.RUNESTONE_TILE.get()))
        .save(c, RootsAPI.rl("chiseled_runestone_from_runestone_tile"));

    ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.RUNESTONE_TILE.get(), 4)
        .pattern("XX")
        .pattern("XX")
        .define('X', ModBlocks.RUNESTONE_BRICK.get())
        .unlockedBy("has_runestone", has(ModBlocks.RUNESTONE_BRICK.get()))
        .save(c, RootsAPI.rl("runestone_tile_from_runestone_brick"));

    ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.RUNESTONE_BRICK.get(), 4)
        .pattern("XX")
        .pattern("XX")
        .define('X', ModBlocks.RUNESTONE.get())
        .unlockedBy("has_runestone", has(ModBlocks.RUNESTONE.get()))
        .save(c, RootsAPI.rl("runestone_brick_from_runestone"));

    // Grove recipe for runed obsidian

    ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.CHISELED_RUNED_OBSIDIAN.get(), 4)
        .pattern("XX")
        .pattern("XX")
        .define('X', ModBlocks.RUNED_TILE.get())
        .unlockedBy("has_runed_obsidian_tile", has(ModBlocks.RUNED_TILE.get()))
        .save(c, RootsAPI.rl("chiseled_runed_obsidian_from_runed_tile"));

    ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.RUNED_TILE.get(), 4)
        .pattern("XX")
        .pattern("XX")
        .define('X', ModBlocks.RUNED_BRICK.get())
        .unlockedBy("has_runed_obsidian_brick", has(ModBlocks.RUNED_BRICK.get()))
        .save(c, RootsAPI.rl("runed_tile_from_runed_brick"));

    ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.RUNED_BRICK.get(), 4)
        .pattern("XX")
        .pattern("XX")
        .define('X', ModBlocks.RUNED_OBSIDIAN.get())
        .unlockedBy("has_runed_obsidian", has(ModBlocks.RUNED_OBSIDIAN.get()))
        .save(c, RootsAPI.rl("runed_brick_from_runed_obsidian"));

    ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.RAW_SILVER_BLOCK.get(), 1)
        .pattern("XXX")
        .pattern("XIX")
        .pattern("XXX")
        .define('X', RootsTags.Items.RAW_SILVER)
        .define('I', ModItems.RAW_SILVER.get())
        .unlockedBy("has_raw_silver", has(RootsTags.Items.RAW_SILVER))
        .save(c);

    ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.SILVER_BLOCK.get(), 1)
        .pattern("XXX")
        .pattern("XIX")
        .pattern("XXX")
        .define('X', RootsTags.Items.SILVER_INGOT)
        .define('I', ModItems.SILVER_INGOT.get())
        .unlockedBy("has_silver_ingot", has(RootsTags.Items.SILVER_INGOT))
        .save(c);

    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.SILVER_INGOT.get(), 1)
        .pattern("XXX")
        .pattern("XIX")
        .pattern("XXX")
        .define('X', RootsTags.Items.SILVER_NUGGET)
        .define('I', ModItems.SILVER_NUGGET.get())
        .unlockedBy("has_silver_nugget", has(RootsTags.Items.SILVER_NUGGET))
        .save(c);

    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.COPPER_INGOT, 1)
        .pattern("XXX")
        .pattern("XIX")
        .pattern("XXX")
        .define('X', RootsTags.Items.COPPER_NUGGET)
        .define('I', ModItems.COPPER_NUGGET.get())
        .unlockedBy("has_copper_nugget", has(RootsTags.Items.COPPER_NUGGET))
        .save(c, RootsAPI.rl("copper_ingot_from_nuggets"));

    ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.WILDWOOD_WOOD.get(), 3)
        .pattern("XX")
        .pattern("XX")
        .define('X', ModBlocks.WILDWOOD_LOG.get())
        .unlockedBy("has_wildwood_log", has(ModBlocks.WILDWOOD_LOG.get()))
        .save(c, RootsAPI.rl("wildwood_wood_from_logs"));

    ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.STRIPPED_WILDWOOD_WOOD.get(), 3)
        .pattern("XX")
        .pattern("XX")
        .define('X', ModBlocks.STRIPPED_WILDWOOD_LOG.get())
        .unlockedBy("has_stripped_wildwood_log", has(ModBlocks.STRIPPED_WILDWOOD_LOG.get()))
        .save(c, RootsAPI.rl("stripped_wildwood_wood_from_logs"));

    ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, ModBlocks.WILDWOOD_PLANKS.get(), 4)
        .requires(RootsTags.Items.WILDWOOD_LOGS)
        .unlockedBy("has_wildwood_logs", has(RootsTags.Items.WILDWOOD_LOGS))
        .save(c, RootsAPI.rl("wildwood_planks_from_logs"));

    ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.GRAY_DYE, 4)
        .requires(RootsTags.Items.STONEPETAL)
        .unlockedBy("has_stonepetal", has(RootsTags.Items.STONEPETAL))
        .save(c, RootsAPI.rl("gray_dye_from_stonepetal"));

    stairBuilder(ModBlocks.RUNESTONE_STAIRS.get(), Ingredient.of(ModBlocks.RUNESTONE.get()))
        .unlockedBy("has_runestone", has(ModBlocks.RUNESTONE.get()))
        .save(c);

    stonecutterResultFromBase(c, RecipeCategory.BUILDING_BLOCKS, ModBlocks.RUNESTONE_STAIRS.get(), ModBlocks.RUNESTONE.get());

    stairBuilder(ModBlocks.MOSSY_RUNESTONE_STAIRS.get(), Ingredient.of(ModBlocks.MOSSY_RUNESTONE.get()))
        .unlockedBy("has_mossy_runestone", has(ModBlocks.MOSSY_RUNESTONE.get()))
        .save(c);

    stonecutterResultFromBase(c, RecipeCategory.BUILDING_BLOCKS, ModBlocks.MOSSY_RUNESTONE_STAIRS.get(), ModBlocks.MOSSY_RUNESTONE.get());

    stairBuilder(ModBlocks.RUNESTONE_BRICK_STAIRS.get(), Ingredient.of(ModBlocks.RUNESTONE_BRICK.get()))
        .unlockedBy("has_runestone_brick", has(ModBlocks.RUNESTONE_BRICK.get()))
        .save(c);

    stonecutterResultFromBase(c, RecipeCategory.BUILDING_BLOCKS, ModBlocks.RUNESTONE_BRICK_STAIRS.get(), ModBlocks.RUNESTONE_BRICK.get());

    stairBuilder(ModBlocks.RUNESTONE_TILE_STAIRS.get(), Ingredient.of(ModBlocks.RUNESTONE_TILE.get()))
        .unlockedBy("has_runestone_tile", has(ModBlocks.RUNESTONE_TILE.get()))
        .save(c);

    stonecutterResultFromBase(c, RecipeCategory.BUILDING_BLOCKS, ModBlocks.RUNESTONE_TILE_STAIRS.get(), ModBlocks.RUNESTONE_TILE.get());

    stairBuilder(ModBlocks.RUNED_STAIRS.get(), Ingredient.of(ModBlocks.RUNED_OBSIDIAN.get()))
        .unlockedBy("has_runed_obsidian", has(ModBlocks.RUNED_OBSIDIAN.get()))
        .save(c);

    stonecutterResultFromBase(c, RecipeCategory.BUILDING_BLOCKS, ModBlocks.RUNED_STAIRS.get(), ModBlocks.RUNED_OBSIDIAN.get());

    stairBuilder(ModBlocks.RUNED_BRICK_STAIRS.get(), Ingredient.of(ModBlocks.RUNED_BRICK.get()))
        .unlockedBy("has_runed_brick", has(ModBlocks.RUNED_BRICK.get()))
        .save(c);

    stonecutterResultFromBase(c, RecipeCategory.BUILDING_BLOCKS, ModBlocks.RUNED_BRICK_STAIRS.get(), ModBlocks.RUNED_BRICK.get());

    stairBuilder(ModBlocks.RUNED_TILE_STAIRS.get(), Ingredient.of(ModBlocks.RUNED_TILE.get()))
        .unlockedBy("has_runed_tile", has(ModBlocks.RUNED_TILE.get()))
        .save(c);

    stonecutterResultFromBase(c, RecipeCategory.BUILDING_BLOCKS, ModBlocks.RUNED_TILE_STAIRS.get(), ModBlocks.RUNED_TILE.get());

    stairBuilder(ModBlocks.WILDWOOD_STAIRS.get(), Ingredient.of(ModBlocks.WILDWOOD_PLANKS.get()))
        .unlockedBy("has_wildwood_planks", has(ModBlocks.WILDWOOD_PLANKS.get()))
        .save(c);

    slabBuilder(RecipeCategory.BUILDING_BLOCKS, ModBlocks.RUNESTONE_SLAB.get(), Ingredient.of(ModBlocks.RUNESTONE.get()))
        .unlockedBy("has_runestone", has(ModBlocks.RUNESTONE.get()))
        .save(c);

    stonecutterResultFromBase(c, RecipeCategory.BUILDING_BLOCKS, ModBlocks.RUNESTONE_SLAB.get(), ModBlocks.RUNESTONE.get());

    slabBuilder(RecipeCategory.BUILDING_BLOCKS, ModBlocks.MOSSY_RUNESTONE_SLAB.get(), Ingredient.of(ModBlocks.MOSSY_RUNESTONE.get()))
        .unlockedBy("has_mossy_runestone", has(ModBlocks.MOSSY_RUNESTONE.get()))
        .save(c);

    stonecutterResultFromBase(c, RecipeCategory.BUILDING_BLOCKS, ModBlocks.MOSSY_RUNESTONE_SLAB.get(), ModBlocks.MOSSY_RUNESTONE.get());

    slabBuilder(RecipeCategory.BUILDING_BLOCKS, ModBlocks.RUNESTONE_BRICK_SLAB.get(), Ingredient.of(ModBlocks.RUNESTONE_BRICK.get()))
        .unlockedBy("has_runestone_brick", has(ModBlocks.RUNESTONE_BRICK.get()))
        .save(c);

    stonecutterResultFromBase(c, RecipeCategory.BUILDING_BLOCKS, ModBlocks.RUNESTONE_BRICK_SLAB.get(), ModBlocks.RUNESTONE_BRICK.get());

    slabBuilder(RecipeCategory.BUILDING_BLOCKS, ModBlocks.RUNESTONE_TILE_SLAB.get(), Ingredient.of(ModBlocks.RUNESTONE_TILE.get()))
        .unlockedBy("has_runestone_tile", has(ModBlocks.RUNESTONE_TILE.get()))
        .save(c);

    stonecutterResultFromBase(c, RecipeCategory.BUILDING_BLOCKS, ModBlocks.RUNESTONE_TILE_SLAB.get(), ModBlocks.RUNESTONE_TILE.get());

    slabBuilder(RecipeCategory.BUILDING_BLOCKS, ModBlocks.RUNED_SLAB.get(), Ingredient.of(ModBlocks.RUNED_OBSIDIAN.get()))
        .unlockedBy("has_runed_obsidian", has(ModBlocks.RUNED_OBSIDIAN.get()))
        .save(c);

    stonecutterResultFromBase(c, RecipeCategory.BUILDING_BLOCKS, ModBlocks.RUNED_SLAB.get(), ModBlocks.RUNED_OBSIDIAN.get());

    slabBuilder(RecipeCategory.BUILDING_BLOCKS, ModBlocks.RUNED_BRICK_SLAB.get(), Ingredient.of(ModBlocks.RUNED_BRICK.get()))
        .unlockedBy("has_runed_brick", has(ModBlocks.RUNED_BRICK.get()))
        .save(c);

    stonecutterResultFromBase(c, RecipeCategory.BUILDING_BLOCKS, ModBlocks.RUNED_BRICK_SLAB.get(), ModBlocks.RUNED_BRICK.get());

    slabBuilder(RecipeCategory.BUILDING_BLOCKS, ModBlocks.RUNED_TILE_SLAB.get(), Ingredient.of(ModBlocks.RUNED_TILE.get()))
        .unlockedBy("has_runed_tile", has(ModBlocks.RUNED_TILE.get()))
        .save(c);

    stonecutterResultFromBase(c, RecipeCategory.BUILDING_BLOCKS, ModBlocks.RUNED_TILE_SLAB.get(), ModBlocks.RUNED_TILE.get());

    slabBuilder(RecipeCategory.BUILDING_BLOCKS, ModBlocks.WILDWOOD_SLAB.get(), Ingredient.of(ModBlocks.WILDWOOD_PLANKS.get()))
        .unlockedBy("has_wildwood_planks", has(ModBlocks.WILDWOOD_PLANKS.get()))
        .save(c);

    fenceBuilder(ModBlocks.WILDWOOD_FENCE.get(), Ingredient.of(ModBlocks.WILDWOOD_PLANKS.get()))
        .unlockedBy("has_wildwood_planks", has(ModBlocks.WILDWOOD_PLANKS.get()))
        .save(c);

    fenceGateBuilder(ModBlocks.WILDWOOD_GATE.get(), Ingredient.of(ModBlocks.WILDWOOD_PLANKS.get()))
        .unlockedBy("has_wildwood_planks", has(ModBlocks.WILDWOOD_PLANKS.get()))
        .save(c);

    buttonBuilder(ModBlocks.RUNESTONE_BUTTON.get(), Ingredient.of(ModBlocks.RUNESTONE.get()))
        .unlockedBy("has_runestone", has(ModBlocks.RUNESTONE.get()))
        .save(c);

    stonecutterResultFromBase(c, RecipeCategory.BUILDING_BLOCKS, ModBlocks.RUNESTONE_BUTTON.get(), ModBlocks.RUNESTONE.get());

    buttonBuilder(ModBlocks.RUNESTONE_BRICK_BUTTON.get(), Ingredient.of(ModBlocks.RUNESTONE_BRICK.get()))
        .unlockedBy("has_runestone_brick", has(ModBlocks.RUNESTONE_BRICK.get()))
        .save(c);

    stonecutterResultFromBase(c, RecipeCategory.BUILDING_BLOCKS, ModBlocks.RUNESTONE_BRICK_BUTTON.get(), ModBlocks.RUNESTONE_BRICK.get());

    buttonBuilder(ModBlocks.RUNESTONE_TILE_BUTTON.get(), Ingredient.of(ModBlocks.RUNESTONE_TILE.get()))
        .unlockedBy("has_runestone_tile", has(ModBlocks.RUNESTONE_TILE.get()))
        .save(c);

    stonecutterResultFromBase(c, RecipeCategory.BUILDING_BLOCKS, ModBlocks.RUNESTONE_TILE_BUTTON.get(), ModBlocks.RUNESTONE_TILE.get());

    buttonBuilder(ModBlocks.MOSSY_RUNESTONE_BUTTON.get(), Ingredient.of(ModBlocks.MOSSY_RUNESTONE.get()))
        .unlockedBy("has_mossy_runestone", has(ModBlocks.MOSSY_RUNESTONE.get()))
        .save(c);

    stonecutterResultFromBase(c, RecipeCategory.BUILDING_BLOCKS, ModBlocks.MOSSY_RUNESTONE_BUTTON.get(), ModBlocks.MOSSY_RUNESTONE.get());

    buttonBuilder(ModBlocks.RUNED_BUTTON.get(), Ingredient.of(ModBlocks.RUNED_OBSIDIAN.get()))
        .unlockedBy("has_runed_obsidian", has(ModBlocks.RUNED_OBSIDIAN.get()))
        .save(c);

    stonecutterResultFromBase(c, RecipeCategory.BUILDING_BLOCKS, ModBlocks.RUNED_BUTTON.get(), ModBlocks.RUNED_OBSIDIAN.get());

    buttonBuilder(ModBlocks.RUNED_BRICK_BUTTON.get(), Ingredient.of(ModBlocks.RUNED_BRICK.get()))
        .unlockedBy("has_runed_brick", has(ModBlocks.RUNED_BRICK.get()))
        .save(c);

    stonecutterResultFromBase(c, RecipeCategory.BUILDING_BLOCKS, ModBlocks.RUNED_BRICK_BUTTON.get(), ModBlocks.RUNED_BRICK.get());

    buttonBuilder(ModBlocks.RUNED_TILE_BUTTON.get(), Ingredient.of(ModBlocks.RUNED_TILE.get()))
        .unlockedBy("has_runed_tile", has(ModBlocks.RUNED_TILE.get()))
        .save(c);

    stonecutterResultFromBase(c, RecipeCategory.BUILDING_BLOCKS, ModBlocks.RUNED_TILE_BUTTON.get(), ModBlocks.RUNED_TILE.get());

    buttonBuilder(ModBlocks.WILDWOOD_BUTTON.get(), Ingredient.of(ModBlocks.WILDWOOD_PLANKS.get()))
        .unlockedBy("has_wildwood_planks", has(ModBlocks.WILDWOOD_PLANKS.get()))
        .save(c);

    pressurePlate(c, ModBlocks.RUNESTONE_PRESSURE_PLATE.get(), ModBlocks.RUNESTONE.get());
    pressurePlate(c, ModBlocks.RUNESTONE_BRICK_PRESSURE_PLATE.get(), ModBlocks.RUNESTONE_BRICK.get());
    pressurePlate(c, ModBlocks.RUNESTONE_TILE_PRESSURE_PLATE.get(), ModBlocks.RUNESTONE_TILE.get());
    pressurePlate(c, ModBlocks.MOSSY_RUNESTONE_PRESSURE_PLATE.get(), ModBlocks.MOSSY_RUNESTONE.get());
    pressurePlate(c, ModBlocks.RUNED_PRESSURE_PLATE.get(), ModBlocks.RUNED_OBSIDIAN.get());
    pressurePlate(c, ModBlocks.RUNED_BRICK_PRESSURE_PLATE.get(), ModBlocks.RUNED_BRICK.get());
    pressurePlate(c, ModBlocks.RUNED_TILE_PRESSURE_PLATE.get(), ModBlocks.RUNED_TILE.get());
    pressurePlate(c, ModBlocks.WILDWOOD_PRESSURE_PLATE.get(), ModBlocks.WILDWOOD_PLANKS.get());

    doorBuilder(ModBlocks.WILDWOOD_DOOR.get(), Ingredient.of(ModBlocks.WILDWOOD_PLANKS.get()))
        .unlockedBy("has_wildwood_planks", has(ModBlocks.WILDWOOD_PLANKS.get()))
        .save(c);

    trapdoorBuilder(ModBlocks.WILDWOOD_TRAPDOOR.get(), Ingredient.of(ModBlocks.WILDWOOD_PLANKS.get()))
        .unlockedBy("has_wildwood_planks", has(ModBlocks.WILDWOOD_PLANKS.get()))
        .save(c);

    ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.WILDWOOD_LADDER.get(), 3)
        .pattern("X X")
        .pattern("XWX")
        .pattern("X X")
        .define('X', Tags.Items.RODS_WOODEN)
        .define('W', RootsTags.Items.WILDWOOD_PLANKS)
        .unlockedBy("has_wildwood_planks", has(ModBlocks.WILDWOOD_PLANKS.get()))
        .save(c);

    wall(c, RecipeCategory.BUILDING_BLOCKS, ModBlocks.RUNESTONE_WALL.get(), ModBlocks.RUNESTONE.get());

    stonecutterResultFromBase(c, RecipeCategory.BUILDING_BLOCKS, ModBlocks.RUNESTONE_WALL.get(), ModBlocks.RUNESTONE.get());

    wall(c, RecipeCategory.BUILDING_BLOCKS, ModBlocks.RUNESTONE_BRICK_WALL.get(), ModBlocks.RUNESTONE_BRICK.get());

    stonecutterResultFromBase(c, RecipeCategory.BUILDING_BLOCKS, ModBlocks.RUNESTONE_BRICK_WALL.get(), ModBlocks.RUNESTONE_BRICK.get());

    wall(c, RecipeCategory.BUILDING_BLOCKS, ModBlocks.RUNESTONE_TILE_WALL.get(), ModBlocks.RUNESTONE_TILE.get());

    stonecutterResultFromBase(c, RecipeCategory.BUILDING_BLOCKS, ModBlocks.RUNESTONE_TILE_WALL.get(), ModBlocks.RUNESTONE_TILE.get());

    wall(c, RecipeCategory.BUILDING_BLOCKS, ModBlocks.MOSSY_RUNESTONE_WALL.get(), ModBlocks.MOSSY_RUNESTONE.get());

    stonecutterResultFromBase(c, RecipeCategory.BUILDING_BLOCKS, ModBlocks.MOSSY_RUNESTONE_WALL.get(), ModBlocks.MOSSY_RUNESTONE.get());

    wall(c, RecipeCategory.BUILDING_BLOCKS, ModBlocks.RUNED_WALL.get(), ModBlocks.RUNED_OBSIDIAN.get());

    stonecutterResultFromBase(c, RecipeCategory.BUILDING_BLOCKS, ModBlocks.RUNED_WALL.get(), ModBlocks.RUNED_OBSIDIAN.get());

    wall(c, RecipeCategory.BUILDING_BLOCKS, ModBlocks.RUNED_BRICK_WALL.get(), ModBlocks.RUNED_BRICK.get());

    stonecutterResultFromBase(c, RecipeCategory.BUILDING_BLOCKS, ModBlocks.RUNED_BRICK_WALL.get(), ModBlocks.RUNED_BRICK.get());

    wall(c, RecipeCategory.BUILDING_BLOCKS, ModBlocks.RUNED_TILE_WALL.get(), ModBlocks.RUNED_TILE.get());

    stonecutterResultFromBase(c, RecipeCategory.BUILDING_BLOCKS, ModBlocks.RUNED_TILE_WALL.get(), ModBlocks.RUNED_TILE.get());


    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.GROVE_CRAFTER.get())
        .pattern("LLL")
        .pattern(" L ")
        .pattern("RRR")
        .define('L', ItemTags.LOGS)
        .define('R', RootsTags.Items.RUNESTONE)
        .unlockedBy("has_runestone", has(RootsTags.Items.RUNESTONE))
        .save(c);

    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.GROVE_PEDESTAL.get(), 4)
        .pattern("LLL")
        .pattern(" L ")
        .pattern("LLL")
        .define('L', ItemTags.LOGS)
        .unlockedBy("has_logs", has(ItemTags.LOGS))
        .save(c);

    ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.DISPLAY_PEDESTAL.get())
        .requires(ModBlocks.GROVE_PEDESTAL.get())
        .requires(RootsTags.Items.LEVERS)
        .unlockedBy("has_grove_pedestal", has(ModBlocks.GROVE_PEDESTAL.get()))
        .unlockedBy("has_lever", has(RootsTags.Items.LEVERS))
        .save(c);

    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.PRIMAL_GROVE_STONE.get())
        .pattern("RR")
        .pattern("RR")
        .pattern("RR")
        .define('R', RootsTags.Items.RUNESTONE)
        .unlockedBy("has_runestone", has(RootsTags.Items.RUNESTONE))
        .save(c, RootsAPI.rl("primal_grove_stone"));

    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.MORTAR.get())
        .pattern("R R")
        .pattern("R R")
        .pattern("RRR")
        .define('R', RootsTags.Items.RUNESTONE)
        .unlockedBy("has_runestone", has(RootsTags.Items.RUNESTONE))
        .save(c);

    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.PYRE.get())
        .pattern("LCL")
        .pattern("RRR")
        .define('L', ItemTags.LOGS)
        .define('C', ItemTags.COALS)
        .define('R', RootsTags.Items.RUNESTONE)
        .unlockedBy("has_runestone", has(RootsTags.Items.RUNESTONE))
        .save(c);

    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.REINFORCED_PYRE.get())
        .pattern("LCL")
        .pattern("RRR")
        .define('L', ItemTags.LOGS)
        .define('C', ItemTags.COALS)
        .define('R', RootsTags.Items.RUNED_OBSIDIAN)
        .unlockedBy("has_runestone", has(RootsTags.Items.RUNED_OBSIDIAN))
        .save(c);

    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.REINFORCED_PYRE.get())
        .pattern("XXX")
        .pattern("XCX")
        .pattern("XXX")
        .define('X', RootsTags.Items.RUNED_OBSIDIAN)
        .define('C', ModBlocks.PYRE.get())
        .unlockedBy("has_runed_obsidian", has(RootsTags.Items.RUNED_OBSIDIAN))
        .save(c, RootsAPI.rl("reinforced_pyre_from_pyre"));

    // Grove spores


    ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.BLUE_DYE, 2)
        .requires(RootsTags.Items.CARAPACE)
        .unlockedBy("has_carapace", has(RootsTags.Items.CARAPACE))
        .save(c, RootsAPI.rl("blue_dye_from_carapace"));

    ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.LEATHER, 1)
        .requires(RootsTags.Items.PELT)
        .unlockedBy("has_pelt", has(RootsTags.Items.PELT))
        .save(c, RootsAPI.rl("leather_from_pelt"));

    ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.BONE_MEAL, 9)
        .requires(RootsTags.Items.ANTLERS)
        .unlockedBy("has_antlers", has(RootsTags.Items.ANTLERS))
        .save(c, RootsAPI.rl("bone_meal_from_antlers"));

    cookRecipes(c, "smoking", RecipeSerializer.SMOKING_RECIPE, SmokingRecipe::new, 100);
    cookRecipes(c, "campfire_cooking", RecipeSerializer.CAMPFIRE_COOKING_RECIPE, CampfireCookingRecipe::new, 600);

    SimpleCookingRecipeBuilder.smelting(Ingredient.of(ModItems.VENISON.get()), RecipeCategory.FOOD, ModItems.COOKED_VENISON.get(), 0.35F, 200)
        .unlockedBy("has_venison", has(ModItems.VENISON.get()))
        .save(c);

    SimpleCookingRecipeBuilder.smelting(Ingredient.of(Items.CARROT), RecipeCategory.FOOD, ModItems.COOKED_CARROT.get(), 0.35F, 200)
        .unlockedBy("has_carrot", has(Items.CARROT))
        .save(c);

    SimpleCookingRecipeBuilder.smelting(Ingredient.of(Items.BEETROOT), RecipeCategory.FOOD, ModItems.COOKED_BEETROOT.get(), 0.35F, 200)
        .unlockedBy("has_beetroot", has(Items.BEETROOT))
        .save(c);

    SimpleCookingRecipeBuilder.smelting(Ingredient.of(ModItems.ASSORTED_SEEDS.get()), RecipeCategory.FOOD, ModItems.COOKED_SEEDS.get(), 0.35F, 200)
        .unlockedBy("has_seeds", has(ModItems.ASSORTED_SEEDS.get()))
        .save(c);

    SimpleCookingRecipeBuilder.smelting(Ingredient.of(ModItems.AUBERGINE.get()), RecipeCategory.FOOD, ModItems.COOKED_AUBERGINE.get(), 0.35F, 200)
        .unlockedBy("has_aubergine", has(ModItems.AUBERGINE.get()))
        .save(c);

    SimpleCookingRecipeBuilder.smelting(Ingredient.of(ModItems.RAW_SQUID.get()), RecipeCategory.FOOD, ModItems.COOKED_SQUID.get(), 0.35F, 200)
        .unlockedBy("has_raw_squid", has(ModItems.RAW_SQUID.get()))
        .save(c);

    SimpleCookingRecipeBuilder.smelting(Ingredient.of(ModItems.FLOUR.get()), RecipeCategory.FOOD, Items.BREAD, 0.35F, 200)
        .unlockedBy("has_flour", has(ModItems.FLOUR.get()))
        .save(c, RootsAPI.rl("bread_from_flour"));

    SimpleCookingRecipeBuilder.smelting(Ingredient.of(ModItems.PERESKIA_BULB.get()), RecipeCategory.FOOD, ModItems.COOKED_PERESKIA.get(), 0.35F, 200)
        .unlockedBy("has_pereskia_bulb", has(ModItems.PERESKIA_BULB.get()))
        .save(c, RootsAPI.rl("cooked_pereskia_from_pereskia_bulb"));

    ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ModItems.ASSORTED_SEEDS.get(), 4)
        .requires(Tags.Items.SEEDS)
        .requires(Tags.Items.SEEDS)
        .requires(Tags.Items.SEEDS)
        .requires(Tags.Items.SEEDS)
        .unlockedBy("has_seeds", has(Tags.Items.SEEDS))
        .save(c, RootsAPI.rl("assorted_seeds_from_seeds"));


    ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ModItems.STUFFED_AUBERGINE.get())
        .requires(ModItems.COOKED_AUBERGINE.get())
        .requires(DifferenceIngredient.of(Ingredient.of(RootsTags.Items.VEGETABLES), Ingredient.of(ModItems.AUBERGINE.get())))
        .requires(DifferenceIngredient.of(Ingredient.of(RootsTags.Items.VEGETABLES), Ingredient.of(ModItems.AUBERGINE.get())))
        .requires(DifferenceIngredient.of(Ingredient.of(RootsTags.Items.COOKED_VEGETABLES), Ingredient.of(ModItems.COOKED_AUBERGINE.get())))
        .unlockedBy("has_cooked_aubergine", has(ModItems.COOKED_AUBERGINE.get()))
        .save(c);

    ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, ModItems.AUBERGINE_SALAD.get(), 3)
        .pattern("AAA")
        .pattern("KKK")
        .pattern("BBB")
        .define('A', RootsTags.Items.AUBERGINE_CROP)
        .define('B', Items.BOWL)
        .define('K', Items.KELP)
        .unlockedBy("has_aubergine", has(RootsTags.Items.AUBERGINE_CROP))
        .unlockedBy("has_kelp", has(Items.KELP))
        .save(c);


    ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, ModItems.BEETROOT_SALAD.get(), 3)
        .pattern("AAA")
        .pattern("KKK")
        .pattern("BBB")
        .define('A', Items.BEETROOT)
        .define('B', Items.BOWL)
        .define('K', Items.KELP)
        .unlockedBy("has_beetroot", has(Items.BEETROOT))
        .unlockedBy("has_kelp", has(Items.KELP))
        .save(c);

    ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, ModItems.STEWED_EGGPLANT.get(), 3)
        .pattern("AAA")
        .pattern("MLM")
        .pattern("BBB")
        .define('A', ModItems.COOKED_AUBERGINE.get())
        .define('B', Items.BOWL)
        .define('L', Items.ALLIUM)
        .define('M', Ingredient.of(Items.RED_MUSHROOM, Items.BROWN_MUSHROOM))
        .unlockedBy("has_cooked_aubergine", has(ModItems.COOKED_AUBERGINE.get()))
        .save(c);
    ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, ModItems.VINEGAR.get(), 6)
        .pattern("BBB")
        .pattern("PPP")
        .pattern("BBB")
        .define('P', Items.SEA_PICKLE)
        .define('B', Items.GLASS_BOTTLE)
        .unlockedBy("has_sea_pickle", has(Items.SEA_PICKLE))
        .save(c);

    ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, ModItems.VEGETABLE_JUICE.get(), 4)
        .pattern("ARC")
        .pattern("BPB")
        .pattern("BWB")
        .define('A', RootsTags.Items.AUBERGINE_CROP)
        .define('R', Items.BEETROOT)
        .define('C', Items.CARROT)
        .define('P', Items.APPLE)
        .define('B', Items.GLASS_BOTTLE)
        .define('W', Items.WATER_BUCKET)
        .unlockedBy("has_aubergine", has(RootsTags.Items.AUBERGINE_CROP))
        .unlockedBy("has_beetroot", has(Items.BEETROOT))
        .unlockedBy("has_carrot", has(Items.CARROT))
        .unlockedBy("has_apple", has(Items.APPLE))
        .save(c);

    ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.BLACK_DYE, 2)
        .requires(ModItems.INK_BOTTLE.get())
        .unlockedBy("has_ink_bottle", has(ModItems.INK_BOTTLE.get()))
        .save(c, RootsAPI.rl("black_dye_from_ink_bottle"));

    // TODO?
    c.accept(DynamicBarkRecipe.IDENTIFIER, DynamicBarkRecipe.INSTANCE, null);

    ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, ModItems.WILDEWHEET_BREAD.get())
        .pattern("XXX")
        .define('X', RootsTags.Items.WILDEWHEET_CROP)
        .unlockedBy("has_wildewheet", has(RootsTags.Items.WILDEWHEET_CROP))
        .save(c);

    ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, ModItems.WILDROOT_STEW.get(), 3)
        .pattern(" W ")
        .pattern("BBB")
        .define('W', RootsTags.Items.WILDROOT_CROP)
        .define('B', Ingredient.of(Items.BOWL))
        .unlockedBy("has_wildroot", has(RootsTags.Items.WILDROOT_CROP))
        .save(c, RootsAPI.rl("wildroot_stew"));

    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.FIRE_STARTER.get(), 4)
        .pattern("SFS")
        .pattern(" L ")
        .pattern("S S")
        .define('S', Tags.Items.RODS_WOODEN)
        .define('F', RootsTags.Items.FLINT)
        .define('L', ItemTags.LOGS)
        .unlockedBy("has_stick", has(Tags.Items.RODS_WOODEN))
        .unlockedBy("has_flint", has(RootsTags.Items.FLINT))
        .save(c, RootsAPI.rl("fire_starter"));

    ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.PESTLE.get())
        .pattern("  S")
        .pattern("SS ")
        .pattern("SS ")
        .define('S', RootsTags.Items.STONELIKE)
        .unlockedBy("has_stone", has(RootsTags.Items.STONELIKE))
        .save(c);

    ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.STAFF.get())
        .pattern(" WX")
        .pattern(" XW")
        .pattern("X  ")
        .define('X', ItemTags.LOGS)
        .define('W', RootsTags.Items.RUNESTONE_HERBS)
        .unlockedBy("has_runestone_herbs", has(RootsTags.Items.RUNESTONE_HERBS))
        .save(c);

    ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.WOODEN_SHEARS.get())
        .pattern(" LL")
        .pattern("L  ")
        .pattern(" LL")
        .define('L', ItemTags.LOGS)
        .unlockedBy("has_logs", has(ItemTags.LOGS))
        .save(c);

    // KNIFE RECIPES
    SimpleCookingRecipeBuilder.smelting(Ingredient.of(RootsTags.Items.RAW_SILVER), RecipeCategory.MISC, ModItems.SILVER_INGOT.get(), 0.7f, 200)
        .unlockedBy("has_raw_silver", has(RootsTags.Items.RAW_SILVER))
        .save(c, RootsAPI.rl("silver_ingot_from_smelting_raw_silver"));
    SimpleCookingRecipeBuilder.blasting(Ingredient.of(RootsTags.Items.RAW_SILVER), RecipeCategory.MISC, ModItems.SILVER_INGOT.get(), 0.7f, 100)
        .unlockedBy("has_raw_silver", has(RootsTags.Items.RAW_SILVER))
        .save(c, RootsAPI.rl("silver_ingot_from_blasting_raw_silver"));
    ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.RAW_SILVER.get(), 9)
        .requires(RootsTags.Items.RAW_SILVER_STORAGE)
        .unlockedBy("has_raw_silver_storage", has(RootsTags.Items.RAW_SILVER_STORAGE))
        .save(c, RootsAPI.rl("raw_silver_from_raw_silver_storage"));
    ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.SILVER_INGOT.get(), 9)
        .requires(RootsTags.Items.SILVER_STORAGE)
        .unlockedBy("has_silver_storage", has(RootsTags.Items.SILVER_STORAGE))
        .save(c, RootsAPI.rl("silver_ingot_from_silver_storage"));
    ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.SILVER_NUGGET.get(), 9)
        .requires(RootsTags.Items.SILVER_INGOT)
        .unlockedBy("has_silver_ingot", has(RootsTags.Items.SILVER_INGOT))
        .save(c, RootsAPI.rl("silver_nugget_from_silver_ingot"));
    ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.COPPER_NUGGET.get(), 9)
        .requires(Tags.Items.INGOTS_COPPER)
        .unlockedBy("has_copper_ingot", has(Tags.Items.INGOTS_COPPER))
        .save(c, RootsAPI.rl("copper_nugget_from_copper_ingot"));

    // TODO: recycling recipes
    RecipeSaver.saver().unlockedBy("has_knife", has(RootsTags.Items.KNIVES)).save(BarkRecipe.Builder.create().condition(new WorldCondition(new TagMatchWorldTest(RootsTags.Blocks.OAK_LOGS_TO_STRIP))).stateMapper(Blocks.OAK_LOG, Blocks.STRIPPED_OAK_LOG, Blocks.OAK_WOOD, Blocks.STRIPPED_OAK_WOOD).build(BaseRecipeData.Builder.create().result(ModItems.OAK_BARK, 3)), c, RootsAPI.rl("bark/oak_bark_from_oak_log"));

    RecipeSaver.saver().unlockedBy("has_knife", has(RootsTags.Items.KNIVES)).save(BarkRecipe.Builder.create().condition(new WorldCondition(new TagMatchWorldTest(RootsTags.Blocks.BIRCH_LOGS_TO_STRIP))).stateMapper(Blocks.BIRCH_LOG, Blocks.STRIPPED_BIRCH_LOG, Blocks.BIRCH_WOOD, Blocks.STRIPPED_BIRCH_WOOD).build(BaseRecipeData.Builder.create().result(ModItems.BIRCH_BARK, 3)), c, RootsAPI.rl("bark/birch_bark_from_birch_log"));

    RecipeSaver.saver().unlockedBy("has_knife", has(RootsTags.Items.KNIVES)).save(BarkRecipe.Builder.create().condition(new WorldCondition(new TagMatchWorldTest(RootsTags.Blocks.SPRUCE_LOGS_TO_STRIP))).stateMapper(Blocks.SPRUCE_LOG, Blocks.STRIPPED_SPRUCE_LOG, Blocks.SPRUCE_WOOD, Blocks.STRIPPED_SPRUCE_WOOD).build(BaseRecipeData.Builder.create().result(ModItems.SPRUCE_BARK, 3)), c, RootsAPI.rl("bark/spruce_bark_from_spruce_log"));

    RecipeSaver.saver().unlockedBy("has_knife", has(RootsTags.Items.KNIVES)).save(BarkRecipe.Builder.create().condition(new WorldCondition(new TagMatchWorldTest(RootsTags.Blocks.JUNGLE_LOGS_TO_STRIP))).stateMapper(Blocks.JUNGLE_LOG, Blocks.STRIPPED_JUNGLE_LOG, Blocks.JUNGLE_WOOD, Blocks.STRIPPED_JUNGLE_WOOD).build(BaseRecipeData.Builder.create().result(ModItems.JUNGLE_BARK, 3)), c, RootsAPI.rl("bark/jungle_bark_from_jungle_log"));

    RecipeSaver.saver().unlockedBy("has_knife", has(RootsTags.Items.KNIVES)).save(BarkRecipe.Builder.create().condition(new WorldCondition(new TagMatchWorldTest(RootsTags.Blocks.ACACIA_LOGS_TO_STRIP))).stateMapper(Blocks.ACACIA_LOG, Blocks.STRIPPED_ACACIA_LOG, Blocks.ACACIA_WOOD, Blocks.STRIPPED_ACACIA_WOOD).build(BaseRecipeData.Builder.create().result(ModItems.ACACIA_BARK, 3)), c, RootsAPI.rl("bark/acacia_bark_from_acacia_log"));

    RecipeSaver.saver().unlockedBy("has_knife", has(RootsTags.Items.KNIVES)).save(BarkRecipe.Builder.create().condition(new WorldCondition(new TagMatchWorldTest(RootsTags.Blocks.DARK_OAK_LOGS_TO_STRIP))).stateMapper(Blocks.DARK_OAK_LOG, Blocks.STRIPPED_DARK_OAK_LOG, Blocks.DARK_OAK_WOOD, Blocks.STRIPPED_DARK_OAK_WOOD).build(BaseRecipeData.Builder.create().result(ModItems.DARK_OAK_BARK, 3)), c, RootsAPI.rl("bark/dark_oak_bark_from_dark_oak_log"));

    RecipeSaver.saver().unlockedBy("has_knife", has(RootsTags.Items.KNIVES)).save(BarkRecipe.Builder.create().condition(new WorldCondition(new TagMatchWorldTest(RootsTags.Blocks.CRIMSON_STEMS_TO_STRIP))).stateMapper(Blocks.CRIMSON_STEM, Blocks.STRIPPED_CRIMSON_STEM, Blocks.CRIMSON_HYPHAE, Blocks.STRIPPED_CRIMSON_HYPHAE).build(BaseRecipeData.Builder.create().result(ModItems.CRIMSON_BARK, 3)), c, RootsAPI.rl("bark/crimson_bark_from_crimson_stem"));

    RecipeSaver.saver().unlockedBy("has_knife", has(RootsTags.Items.KNIVES)).save(BarkRecipe.Builder.create().condition(new WorldCondition(new TagMatchWorldTest(RootsTags.Blocks.WARPED_STEMS_TO_STRIP))).stateMapper(Blocks.WARPED_STEM, Blocks.STRIPPED_WARPED_STEM, Blocks.WARPED_HYPHAE, Blocks.STRIPPED_WARPED_HYPHAE).build(BaseRecipeData.Builder.create().result(ModItems.WARPED_BARK, 3)), c, RootsAPI.rl("bark/warped_bark_from_warped_stem"));

    RecipeSaver.saver().unlockedBy("has_knife", has(RootsTags.Items.KNIVES)).save(BarkRecipe.Builder.create().condition(new WorldCondition(new TagMatchWorldTest(RootsTags.Blocks.MANGROVE_LOGS_TO_STRIP))).stateMapper(Blocks.MANGROVE_LOG, Blocks.STRIPPED_MANGROVE_LOG, Blocks.MANGROVE_WOOD, Blocks.STRIPPED_MANGROVE_WOOD).build(BaseRecipeData.Builder.create().result(ModItems.MANGROVE_BARK, 3)), c, RootsAPI.rl("bark/mangrove_bark_from_mangrove_log"));

    RecipeSaver.saver().unlockedBy("has_knife", has(RootsTags.Items.KNIVES)).save(BarkRecipe.Builder.create().condition(new WorldCondition(new TagMatchWorldTest(RootsTags.Blocks.WILDWOOD_LOGS_TO_STRIP))).stateMapper(ModBlocks.WILDWOOD_LOG.get(), ModBlocks.STRIPPED_WILDWOOD_LOG.get(), ModBlocks.WILDWOOD_WOOD.get(), ModBlocks.STRIPPED_WILDWOOD_WOOD.get()).build(BaseRecipeData.Builder.create().result(ModItems.WILDWOOD_BARK, 3)), c, RootsAPI.rl("bark/wildwood_bark_from_wildwood_log"));

    BaseRecipeData.Builder groveRunestoneBuilder = BaseRecipeData.Builder.create()
        .requires(RootsTags.Items.STONELIKE)
        .condition(ModConditions.GROVE_STONE_VALID.get())
        .result(ModBlocks.RUNESTONE, 1);

    // TODO: Simplify this
    RecipeSaver saver = RecipeSaver.saver().unlockedBy("has_stone", has(RootsTags.Items.STONELIKE));
    saver.save(GroveRecipe.Builder.create().build(groveRunestoneBuilder), c, RootsAPI.rl("grove/runestone_1"));
    saver.save(GroveRecipe.Builder.create().build(groveRunestoneBuilder.multiplty(2)), c, RootsAPI.rl("grove/runestone_2"));
    saver.save(GroveRecipe.Builder.create().build(groveRunestoneBuilder.multiplty(3)), c, RootsAPI.rl("grove/runestone_3"));
    saver.save(GroveRecipe.Builder.create().build(groveRunestoneBuilder.multiplty(4)), c, RootsAPI.rl("grove/runestone_4"));
    saver.save(GroveRecipe.Builder.create().build(groveRunestoneBuilder.multiplty(5)), c, RootsAPI.rl("grove/runestone_5"));
    saver.save(GroveRecipe.Builder.create().build(groveRunestoneBuilder.multiplty(6)), c, RootsAPI.rl("grove/runestone_6"));
    saver.save(GroveRecipe.Builder.create().build(groveRunestoneBuilder.multiplty(7)), c, RootsAPI.rl("grove/runestone_7"));
    saver.save(GroveRecipe.Builder.create().build(groveRunestoneBuilder.multiplty(8)), c, RootsAPI.rl("grove/runestone_8"));
    saver.save(GroveRecipe.Builder.create().build(groveRunestoneBuilder.multiplty(9)), c, RootsAPI.rl("grove/runestone_9"));
    saver.save(GroveRecipe.Builder.create().build(groveRunestoneBuilder.multiplty(10)), c, RootsAPI.rl("grove/runestone_10"));

    BaseRecipeData.Builder runedObsidianBuilder = BaseRecipeData.Builder.create()
        .requires(RootsTags.Items.RUNESTONE)
        .requires(RootsTags.Items.RUNESTONE)
        .requires(RootsTags.Items.RUNESTONE)
        .requires(Tags.Items.OBSIDIANS)
        .condition(ModConditions.GROVE_STONE_VALID.get())
        .result(ModBlocks.RUNED_OBSIDIAN, 4);

    saver = RecipeSaver.saver().unlockedBy("has_runestone", has(RootsTags.Items.RUNESTONE)).unlockedBy("has_obsidian", has(Tags.Items.OBSIDIANS));
    saver.save(GroveRecipe.Builder.create().build(runedObsidianBuilder), c, RootsAPI.rl("grove/runed_obsidian_4"));
    saver.save(GroveRecipe.Builder.create().build(runedObsidianBuilder.multiplty(2)), c, RootsAPI.rl("grove/runed_obsidian_8"));

    RecipeSaver.saver().unlockedBy("has_runic_dust", has(RootsTags.Items.RUNIC_DUST)).save(GroveRecipe.Builder.create().build(BaseRecipeData.Builder.create().requires(RootsTags.Items.RUNIC_DUST).requires(Tags.Items.GRAVELS).requires(ItemTags.DIRT).requires(ItemTags.DIRT).requires(ItemTags.DIRT).result(ModItems.ELEMENTAL_SOIL, 4).condition(ModConditions.GROVE_STONE_VALID.get())), c, RootsAPI.rl("grove/elemental_soil"));

    ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.AQUEOUS_SOIL.get(), 1).requires(ModItems.ELEMENTAL_SOIL.get()).requires(RootsTags.Items.DEWGONIA_HERB).unlockedBy("has_elemental_soil", has(ModItems.ELEMENTAL_SOIL.get())).unlockedBy("has_dewgonia", has(RootsTags.Items.DEWGONIA_HERB)).save(c, RootsAPI.rl("aqueous_soil_from_elemental_soil_and_dewgonia"));

    ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.MAGMATIC_SOIL.get(), 1).requires(ModItems.ELEMENTAL_SOIL.get()).requires(RootsTags.Items.INFERNO_BULB_HERB).unlockedBy("has_elemental_soil", has(ModItems.ELEMENTAL_SOIL.get())).unlockedBy("has_inferno_bulb", has(RootsTags.Items.INFERNO_BULB_HERB)).save(c, RootsAPI.rl("magmatic_soil_from_elemental_soil_and_inferno_bulb"));

    ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.TERRAN_SOIL.get(), 1).requires(ModItems.ELEMENTAL_SOIL.get()).requires(RootsTags.Items.STALICRIPE_HERB).unlockedBy("has_elemental_soil", has(ModItems.ELEMENTAL_SOIL.get())).unlockedBy("has_stalicripe", has(RootsTags.Items.STALICRIPE_HERB)).save(c, RootsAPI.rl("terran_soil_from_elemental_soil_and_stalicripe"));

    ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.CAELIC_SOIL.get(), 1).requires(ModItems.ELEMENTAL_SOIL.get()).requires(RootsTags.Items.CLOUD_BERRY_HERB).unlockedBy("has_elemental_soil", has(ModItems.ELEMENTAL_SOIL.get())).unlockedBy("has_cloud_berry", has(RootsTags.Items.CLOUD_BERRY_HERB)).save(c, RootsAPI.rl("caelic_soil_from_elemental_soil_and_cloud_berry"));

    // TODO: Ritual pedestal
    // TODO: Reinforced ritual pedestal
    RecipeSaver.saver().unlockedBy("has_wildwood", has(RootsTags.Items.WILDWOOD_LOGS)).save(GroveRecipe.Builder.create().build(BaseRecipeData.Builder.create().requires(RootsTags.Items.WILDWOOD_LOGS).requires(RootsTags.Items.WILDWOOD_LOGS).requires(RootsTags.Items.WILDWOOD_LOGS).requires(RootsTags.Items.WILDWOOD_LOGS).requires(RootsTags.Items.WILDWOOD_LOGS).condition(ModConditions.GROVE_STONE_VALID.get()).result(ModBlocks.WILDWOOD_PEDESTAL, 5)), c, RootsAPI.rl("grove/wildwood_pedestal"));

    ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.DECORATIVE_PYRE.get(), 1).requires(ModItems.PYRE.get()).requires(RootsTags.Items.LEVERS).unlockedBy("has_decorative_pyre", has(ModItems.DECORATIVE_PYRE.get())).unlockedBy("has_lever", has(RootsTags.Items.LEVERS)).save(c, RootsAPI.rl("decorative_pyre_from_decorative_pyre_and_lever"));

    RecipeSaver.saver().unlockedBy("has_runestone", has(RootsTags.Items.RUNESTONE)).save(GroveRecipe.Builder.create().build(BaseRecipeData.Builder.create().requires(RootsTags.Items.RUNESTONE).requires(RootsTags.Items.RUNESTONE).requires(RootsTags.Items.PETALS).requires(RootsTags.Items.GROVE_MOSS_HERB).condition(ModConditions.GROVE_STONE_VALID.get()).requires(Ingredient.of(ModItems.WOODEN_SHEARS.get(), Items.SHEARS))), c, RootsAPI.rl("grove/runic_shears"));

    RecipeSaver.saver().unlockedBy("has_shears", has(RootsTags.Items.RUNIC_SHEARS)).save(RunicEntityRecipe.Builder.create().durabilityCost(10).test(new EntityTagTest(RootsTags.Entities.FEY_LEATHER)).cooldown(2 * 60 * 20).build(BaseRecipeData.Builder.create().result(ModItems.FEY_LEATHER, 1)), c, RootsAPI.rl("runic_entity/fey_leather"));

    BaseRecipeData.Builder basePetalsBuilder = BaseRecipeData.Builder.create().requires(ItemTags.SMALL_FLOWERS).result(ModItems.PETALS, 1);
    MortarRecipe.Builder petalsBuilder = MortarRecipe.Builder.create().times(1);

    saver = RecipeSaver.saver().unlockedBy("has_small_flowers", has(ItemTags.SMALL_FLOWERS));
    saver.save(petalsBuilder.build(basePetalsBuilder), c, RootsAPI.rl("mortar/petals"));
    saver.save(petalsBuilder.times(2).build(basePetalsBuilder.multiplty(2)), c, RootsAPI.rl("mortar/petals_2"));
    saver.save(petalsBuilder.times(3).build(basePetalsBuilder.multiplty(3)), c, RootsAPI.rl("mortar/petals_3"));
    saver.save(petalsBuilder.times(4).build(basePetalsBuilder.multiplty(4)), c, RootsAPI.rl("mortar/petals_4"));
    saver.save(petalsBuilder.times(5).build(basePetalsBuilder.multiplty(5)), c, RootsAPI.rl("mortar/petals_5"));

    basePetalsBuilder = BaseRecipeData.Builder.create().requires(ItemTags.TALL_FLOWERS).result(ModItems.PETALS, 2);
    petalsBuilder = MortarRecipe.Builder.create().times(1);

    saver = RecipeSaver.saver().unlockedBy("has_tall_flowers", has(ItemTags.TALL_FLOWERS));

    saver.save(petalsBuilder.build(basePetalsBuilder), c, RootsAPI.rl("mortar/petals_tall"));
    saver.save(petalsBuilder.times(2).build(basePetalsBuilder.multiplty(2)), c, RootsAPI.rl("mortar/petals_tall_2"));
    saver.save(petalsBuilder.times(3).build(basePetalsBuilder.multiplty(3)), c, RootsAPI.rl("mortar/petals_tall_3"));
    saver.save(petalsBuilder.times(4).build(basePetalsBuilder.multiplty(4)), c, RootsAPI.rl("mortar/petals_tall_4"));
    saver.save(petalsBuilder.times(5).build(basePetalsBuilder.multiplty(5)), c, RootsAPI.rl("mortar/petals_tall_5"));

    MortarRecipe.Builder runicDustBuilder = MortarRecipe.Builder.create().times(5);
    BaseRecipeData.Builder runicDustData = BaseRecipeData.Builder.create().requires(RootsTags.Items.RUNESTONE).result(ModItems.RUNIC_DUST, 1);
    saver = RecipeSaver.saver().unlockedBy("has_runestone", has(RootsTags.Items.RUNESTONE));

    saver.save(runicDustBuilder.build(runicDustData), c, RootsAPI.rl("mortar/runic_dust"));
    saver.save(runicDustBuilder.times(2).build(runicDustData.multiplty(2)), c, RootsAPI.rl("mortar/runic_dust_2"));
    saver.save(runicDustBuilder.times(3).build(runicDustData.multiplty(3)), c, RootsAPI.rl("mortar/runic_dust_3"));
    saver.save(runicDustBuilder.times(4).build(runicDustData.multiplty(4)), c, RootsAPI.rl("mortar/runic_dust_4"));
    saver.save(runicDustBuilder.times(5).build(runicDustData.multiplty(5)), c, RootsAPI.rl("mortar/runic_dust_5"));

    MortarRecipe.Builder stringBuilder = MortarRecipe.Builder.create().times(1);
    BaseRecipeData.Builder stringData = BaseRecipeData.Builder.create().requires(ItemTags.WOOL).result(Items.STRING.builtInRegistryHolder(), 1);
    saver = RecipeSaver.saver().unlockedBy("has_wool", has(ItemTags.WOOL));

    saver.save(stringBuilder.build(stringData), c, RootsAPI.rl("mortar/string_from_wool"));
    saver.save(stringBuilder.times(2).build(stringData.multiplty(2)), c, RootsAPI.rl("mortar/string_from_wool_2"));
    saver.save(stringBuilder.times(3).build(stringData.multiplty(3)), c, RootsAPI.rl("mortar/string_from_wool_3"));
    saver.save(stringBuilder.times(4).build(stringData.multiplty(4)), c, RootsAPI.rl("mortar/string_from_wool_4"));
    saver.save(stringBuilder.times(5).build(stringData.multiplty(5)), c, RootsAPI.rl("mortar/string_from_wool_5"));

    MortarRecipe.Builder flintBuilder = MortarRecipe.Builder.create().times(1);
    BaseRecipeData.Builder flintData = BaseRecipeData.Builder.create().requires(RootsTags.Items.FLINT).result(Items.FLINT.builtInRegistryHolder(), 1);
    saver = RecipeSaver.saver().unlockedBy("has_flint", has(RootsTags.Items.FLINT));

    saver.save(flintBuilder.build(flintData), c, RootsAPI.rl("mortar/flint_from_gravel"));
    saver.save(flintBuilder.times(2).build(flintData.multiplty(2)), c, RootsAPI.rl("mortar/flint_from_gravel_2"));
    saver.save(flintBuilder.times(3).build(flintData.multiplty(3)), c, RootsAPI.rl("mortar/flint_from_gravel_3"));
    saver.save(flintBuilder.times(4).build(flintData.multiplty(4)), c, RootsAPI.rl("mortar/flint_from_gravel_4"));
    saver.save(flintBuilder.times(5).build(flintData.multiplty(5)), c, RootsAPI.rl("mortar/flint_from_gravel_5"));

    MortarRecipe.Builder magmaCreamFromMagmaBlockBuilder = MortarRecipe.Builder.create().times(1);
    BaseRecipeData.Builder magmaCreamFromMagmaBlockData = BaseRecipeData.Builder.create().requires(Items.MAGMA_BLOCK).result(Items.MAGMA_CREAM.builtInRegistryHolder(), 4);
    saver = RecipeSaver.saver().unlockedBy("has_magma_block", has(Items.MAGMA_BLOCK));

    saver.save(magmaCreamFromMagmaBlockBuilder.build(magmaCreamFromMagmaBlockData), c, RootsAPI.rl("mortar/magma_cream_from_magma_block"));
    saver.save(magmaCreamFromMagmaBlockBuilder.times(2).build(magmaCreamFromMagmaBlockData.multiplty(2)), c, RootsAPI.rl("mortar/magma_cream_from_magma_block_2"));
    saver.save(magmaCreamFromMagmaBlockBuilder.times(3).build(magmaCreamFromMagmaBlockData.multiplty(3)), c, RootsAPI.rl("mortar/magma_cream_from_magma_block_3"));

    RecipeSaver.saver().unlockedBy("has_glowstone", has(Tags.Items.DUSTS_GLOWSTONE)).save(GroveRecipe.Builder.create().build(BaseRecipeData.Builder.create().requires(Tags.Items.GLASS_BLOCKS).requires(Tags.Items.GLASS_BLOCKS).requires(Tags.Items.GLASS_BLOCKS).requires(Tags.Items.DUSTS_GLOWSTONE).result(ModItems.GLASS_EYE, 1)), c, RootsAPI.rl("grove/glass_eye"));

    RecipeSaver.saver().unlockedBy("has_wildroot", has(RootsTags.Items.WILDROOT_HERB)).save(PyreRecipe.Builder.create().ritual(ModRituals.ANIMAL_HARVEST).build(BaseRecipeData.Builder.create().requires(RootsTags.Items.WILDEWHEET_HERB).requires(ItemTags.WOOL).requires(Tags.Items.CROPS_CARROT).requires(Tags.Items.CROPS_POTATO).requires(RootsTags.Items.WILDROOT_HERB)), c, RootsAPI.rl("pyre/animal_harvest"));

    RecipeSaver.saver().unlockedBy("has_sugar_cane", has(Tags.Items.CROPS_SUGAR_CANE)).save(PyreRecipe.Builder.create().ritual(ModRituals.OVERGROWTH).build(BaseRecipeData.Builder.create().requires(RootsTags.Items.BARKS).requires(RootsTags.Items.BARKS).requires(RootsTags.Items.GROVE_MOSS_HERB).requires(Tags.Items.CROPS_SUGAR_CANE).requires(Items.SHORT_GRASS)), c, RootsAPI.rl("pyre/overgrowth"));

    RecipeSaver.saver().unlockedBy("has_door", has(ItemTags.DOORS)).save(PyreRecipe.Builder.create().ritual(ModRituals.GROVE_SUPPLICATION).build(BaseRecipeData.Builder.create().requires(ItemTags.DOORS).requires(Items.BOWL).requires(ItemTags.SAPLINGS).requires(RootsTags.Items.PETALS).requires(Tags.Items.FOODS_BREAD)), c, RootsAPI.rl("pyre/grove_supplication"));

    RecipeSaver.saver().unlockedBy("has_spiritleaf", has(RootsTags.Items.SPIRITLEAF_HERB)).save(PyreRecipe.Builder.create().ritual(ModRituals.WILDROOT_GROWTH).build(BaseRecipeData.Builder.create().requires(RootsTags.Items.WILDROOT_HERB).requires(RootsTags.Items.BARKS).requires(RootsTags.Items.BARKS).requires(RootsTags.Items.SPIRITLEAF_HERB).requires(ItemTags.SAPLINGS).condition(ModConditions.MATURE_WILDROOT_CROP.get())), c, RootsAPI.rl("pyre/wildroot_growth"));

    RecipeSaver.saver().unlockedBy("has_lightning_rod", has(Items.LIGHTNING_ROD)).save(PyreRecipe.Builder.create().build(BaseRecipeData.Builder.create().requires(Items.LIGHTNING_ROD).requires(Items.SUGAR).requires(ItemTags.LEAVES).requires(ItemTags.WOOL).requires(RootsTags.Items.ACACIA_BARK).result(ModItems.CLOUD_BERRY, 2)), c, RootsAPI.rl("pyre/cloud_berry"));

    RecipeSaver.saver().unlockedBy("has_kelp", has(Items.KELP)).save(PyreRecipe.Builder.create().build(BaseRecipeData.Builder.create().requires(Items.WATER_BUCKET).requires(Items.CLAY_BALL).requires(Items.PUMPKIN).requires(Tags.Items.CROPS_SUGAR_CANE).requires(Items.KELP).result(ModItems.DEWGONIA, 2)), c, RootsAPI.rl("pyre/dewgonia"));

    RecipeSaver.saver().unlockedBy("has_netherrack", has(Tags.Items.NETHERRACKS)).save(PyreRecipe.Builder.create().build(BaseRecipeData.Builder.create().requires(Items.MAGMA_CREAM).requires(Tags.Items.NETHERRACKS).requires(ItemTags.COALS).requires(Items.STICK).requires(Items.BRICK).result(ModItems.INFERNO_BULB, 2)), c, RootsAPI.rl("pyre/inferno_bulb"));

    RecipeSaver.saver().unlockedBy("has_glow_lichen", has(Items.GLOW_LICHEN)).save(PyreRecipe.Builder.create().build(BaseRecipeData.Builder.create().requires(Items.GLOW_LICHEN).requires(Items.TUFF).requires(Tags.Items.COBBLESTONES_DEEPSLATE).requires(RootsTags.Items.FLINT).requires(Tags.Items.RAW_MATERIALS_IRON).result(ModItems.STALICRIPE, 2)), c, RootsAPI.rl("pyre/stalicripe"));

    RecipeSaver.saver().unlockedBy("has_spider_eye", has(Items.SPIDER_EYE)).save(MortarRecipe.Builder.create().times(5).build(BaseRecipeData.Builder.create().requires(Items.ROTTEN_FLESH).requires(RootsTags.Items.BAFFLECAP_HERB).requires(RootsTags.Items.RUNIC_DUST).requires(Items.SPIDER_EYE).requires(ItemTags.WOOL).unlocks(Unlock.spell(ModSpells.ACID_CLOUD))), c, RootsAPI.rl("spell/acid_cloud"));

    RecipeSaver.saver().unlockedBy("has_dandelion", has(Items.DANDELION)).save(MortarRecipe.Builder.create().times(5).build(BaseRecipeData.Builder.create().requires(Items.DANDELION).requires(Tags.Items.CROPS_WHEAT).requires(RootsTags.Items.PETALS).requires(Tags.Items.DYES_YELLOW).requires(Tags.Items.SEEDS).unlocks(Unlock.spell(ModSpells.DANDELION_WINDS))), c, RootsAPI.rl("spell/dandelion_winds"));

    RecipeSaver.saver().unlockedBy("has_torch", has(Items.TORCH)).save(MortarRecipe.Builder.create().times(5).build(BaseRecipeData.Builder.create().requires(ItemTags.WOOL).requires(Items.TORCH).requires(Items.JACK_O_LANTERN).requires(RootsTags.Items.COPPER_NUGGET).requires(RootsTags.Items.RUNIC_DUST).unlocks(Unlock.spell(ModSpells.FEY_LIGHT))), c, RootsAPI.rl("spell/fey_light"));
    RecipeSaver.saver().unlockedBy("has_shield", has(Items.SHIELD)).save(MortarRecipe.Builder.create().times(5).build(BaseRecipeData.Builder.create().requires(RootsTags.Items.PETALS).requires(Items.SHIELD).requires(Tags.Items.INGOTS_IRON).requires(Items.EGG).requires(Tags.Items.GLASS_BLOCKS).unlocks(Unlock.spell(ModSpells.PETAL_SHELL))), c, RootsAPI.rl("spell/petal_shell"));
    RecipeSaver.saver().unlockedBy("has_birch_bark", has(RootsTags.Items.BIRCH_BARK)).save(MortarRecipe.Builder.create().times(5).build(BaseRecipeData.Builder.create().requires(RootsTags.Items.BIRCH_BARK).requires(Items.REDSTONE_TORCH).requires(ItemTags.BOATS).requires(Tags.Items.TOOLS_BOW).requires(Tags.Items.GUNPOWDERS).unlocks(Unlock.spell(ModSpells.JAUNT))), c, RootsAPI.rl("spell/jaunt"));
    RecipeSaver.saver().unlockedBy("has_hoe", has(ItemTags.HOES)).save(MortarRecipe.Builder.create().times(5).build(BaseRecipeData.Builder.create().requires(Tags.Items.SEEDS).requires(Items.COMPOSTER).requires(ItemTags.HOES).requires(Items.BONE_MEAL).requires(ItemTags.SMALL_FLOWERS).unlocks(Unlock.spell(ModSpells.GROWTH_INFUSION))), c, RootsAPI.rl("spell/growth_infusion"));
    RecipeSaver.saver().unlockedBy("has_bow", has(Tags.Items.TOOLS_BOW)).save(MortarRecipe.Builder.create().times(5).build(BaseRecipeData.Builder.create().requires(Tags.Items.TOOLS_BOW).requires(Items.PAPER).requires(Items.LADDER).requires(RootsTags.Items.CLOUD_BERRY_CROP).requires(Items.SHORT_GRASS).unlocks(Unlock.spell(ModSpells.SKY_SOARER))), c, RootsAPI.rl("spell/sky_soarer"));
    RecipeSaver.saver().unlockedBy("has_redstone", has(Tags.Items.DUSTS_REDSTONE)).save(MortarRecipe.Builder.create().times(5).build(BaseRecipeData.Builder.create().requires(Tags.Items.NUGGETS_IRON).requires(Tags.Items.DUSTS_REDSTONE).requires(RootsTags.Items.WILDROOT_CROP).requires(Items.FISHING_ROD).requires(RootsTags.Items.AUBERGINE_CROP).unlocks(Unlock.spell(ModSpells.MAGNETISM))), c, RootsAPI.rl("spell/magnetism"));
    RecipeSaver.saver().unlockedBy("has_carrots", has(Tags.Items.CROPS_CARROT)).save(MortarRecipe.Builder.create().times(5).build(BaseRecipeData.Builder.create().requires(Tags.Items.CROPS_CARROT).requires(Tags.Items.NUGGETS_GOLD).requires(RootsTags.Items.GROVE_MOSS_CROP).requires(RootsTags.Items.BARKS).requires(Items.TORCH).unlocks(Unlock.spell(ModSpells.EXTENSION))), c, RootsAPI.rl("spell/extension"));

    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Blocks.MOSS_BLOCK)
        .pattern("XX")
        .pattern("XX")
        .define('X', RootsTags.Items.GROVE_MOSS_CROP)
        .unlockedBy("has_grove_moss", has(RootsTags.Items.GROVE_MOSS_HERB))
        .save(c, RootsAPI.rl("moss_block_from_grove_moss"));
  }

  public static class RecipeSaver {
    private final Map<String, Criterion<?>> criteria = new HashMap<>();

    public RecipeSaver unlockedBy(String name, Criterion<?> criterion) {
      this.criteria.put(name, criterion);
      return this;
    }

    public static RecipeSaver saver() {
      return new RecipeSaver();
    }

    public void save(Recipe<?> recipe, RecipeOutput recipeOutput, ResourceLocation id) {
      Advancement.Builder advancements = recipeOutput.advancement()
          .addCriterion("has the recipe", RecipeUnlockedTrigger.unlocked(id))
          .rewards(AdvancementRewards.Builder.recipe(id))
          .requirements(AdvancementRequirements.Strategy.OR);
      this.criteria.forEach(advancements::addCriterion);
      recipeOutput.accept(id, recipe, advancements.build(id.withPrefix("recipes/" + RecipeCategory.MISC.getFolderName() + "/")));
    }
  }

  protected static <T extends AbstractCookingRecipe> void cookRecipes(RecipeOutput recipeOutput, String cookingMethod, RecipeSerializer<T> cookingSerializer, AbstractCookingRecipe.Factory<T> recipeFactory, int cookingTime) {
    simpleCookingRecipe(recipeOutput, cookingMethod, cookingSerializer, recipeFactory, cookingTime, ModItems.VENISON.get(), ModItems.COOKED_VENISON.get(), 0.35F);
    simpleCookingRecipe(recipeOutput, cookingMethod, cookingSerializer, recipeFactory, cookingTime, Items.CARROT, ModItems.COOKED_CARROT.get(), 0.35F);
    simpleCookingRecipe(recipeOutput, cookingMethod, cookingSerializer, recipeFactory, cookingTime, Items.BEETROOT, ModItems.COOKED_BEETROOT.get(), 0.35F);
    simpleCookingRecipe(recipeOutput, cookingMethod, cookingSerializer, recipeFactory, cookingTime, ModItems.ASSORTED_SEEDS.get(), ModItems.COOKED_SEEDS.get(), 0.35F);
    simpleCookingRecipe(recipeOutput, cookingMethod, cookingSerializer, recipeFactory, cookingTime, ModItems.AUBERGINE.get(), ModItems.COOKED_AUBERGINE.get(), 0.35F);
    simpleCookingRecipe(recipeOutput, cookingMethod, cookingSerializer, recipeFactory, cookingTime, ModItems.RAW_SQUID.get(), ModItems.COOKED_SQUID.get(), 0.35F);
    simpleCookingRecipe(recipeOutput, cookingMethod, cookingSerializer, recipeFactory, cookingTime, ModItems.FLOUR.get(), Items.BREAD, 0.35F);
    simpleCookingRecipe(recipeOutput, cookingMethod, cookingSerializer, recipeFactory, cookingTime, ModItems.PERESKIA_BULB.get(), ModItems.COOKED_PERESKIA.get(), 0.35F);
  }
}
