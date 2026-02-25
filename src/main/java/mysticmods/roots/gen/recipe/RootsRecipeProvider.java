package mysticmods.roots.gen.recipe;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.attachment.Unlock;
import mysticmods.roots.api.grove.GroveNumber;
import mysticmods.roots.api.recipe.BaseRecipeData;
import mysticmods.roots.api.test.world.PartialBlockState;
import mysticmods.roots.api.test.world.PartialBlockStateMatchWorldTest;
import mysticmods.roots.init.*;
import mysticmods.roots.recipe.PouchDyeRecipe;
import mysticmods.roots.recipe.grove.GrovePouchRecipe;
import mysticmods.roots.recipe.grove.GroveRecipe;
import mysticmods.roots.recipe.mortar.MortarRecipe;
import mysticmods.roots.recipe.pyre.PyreRecipe;
import mysticmods.roots.recipe.pyre.SummonCreaturesRecipe;
import mysticmods.roots.recipe.runic.RunicBlockRecipe;
import mysticmods.roots.recipe.runic.RunicEntityRecipe;
import mysticmods.roots.recipe.transmutation.TransmutationRecipe;
import mysticmods.roots.test.entity.EntityTagTest;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.block.BeetrootBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.crafting.DifferenceIngredient;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

public final class RootsRecipeProvider extends RecipeProvider {
  public static AtomicBoolean GENERATING_RECIPES = new AtomicBoolean();

  public RootsRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
    super(output, registries);
  }

  @SuppressWarnings("deprecation")
  @Override
  protected void buildRecipes(RecipeOutput c, HolderLookup.Provider p) {
    GENERATING_RECIPES.set(true);
    ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.ELEMENTAL_SOIL.get(), 1)
        .requires(RootsTags.Items.ELEMENTAL_SOIL)
        .unlockedBy("has_elemental_soil", has(RootsTags.Items.ELEMENTAL_SOIL))
        .save(c, RootsAPI.rl("base_elemental_soil_from_elemental_soil"));
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

    ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, ModBlocks.MOSSY_RUNESTONE.get())
        .requires(RootsTags.Items.RUNESTONE).requires(RootsTags.Items.GROVE_MOSS_CROP)
        .unlockedBy("has_grove_moss", has(RootsTags.Items.GROVE_MOSS_CROP))
        .save(c, RootsAPI.rl("mossy_runestone_from_runestone_grove_moss"));

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
        .define('W', ModItems.WILDWOOD_PLANKS.get())
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

    stonecutterResultFromBase(c, RecipeCategory.BUILDING_BLOCKS, ModBlocks.RUNESTONE_BRICK.get(), ModBlocks.RUNESTONE.get());
    stonecutterResultFromBase(c, RecipeCategory.BUILDING_BLOCKS, ModBlocks.RUNESTONE_TILE.get(), ModBlocks.RUNESTONE.get());
    stonecutterResultFromBase(c, RecipeCategory.BUILDING_BLOCKS, ModBlocks.CHISELED_RUNESTONE.get(), ModBlocks.RUNESTONE.get());

    stonecutterResultFromBase(c, RecipeCategory.BUILDING_BLOCKS, ModBlocks.RUNESTONE.get(), ModBlocks.RUNESTONE_BRICK.get());
    stonecutterResultFromBase(c, RecipeCategory.BUILDING_BLOCKS, ModBlocks.RUNESTONE_TILE.get(), ModBlocks.RUNESTONE_BRICK.get());
    stonecutterResultFromBase(c, RecipeCategory.BUILDING_BLOCKS, ModBlocks.CHISELED_RUNESTONE.get(), ModBlocks.RUNESTONE_BRICK.get());

    stonecutterResultFromBase(c, RecipeCategory.BUILDING_BLOCKS, ModBlocks.RUNESTONE.get(), ModBlocks.RUNESTONE_TILE.get());
    stonecutterResultFromBase(c, RecipeCategory.BUILDING_BLOCKS, ModBlocks.RUNESTONE_BRICK.get(), ModBlocks.RUNESTONE_TILE.get());
    stonecutterResultFromBase(c, RecipeCategory.BUILDING_BLOCKS, ModBlocks.CHISELED_RUNESTONE.get(), ModBlocks.RUNESTONE_TILE.get());

    stonecutterResultFromBase(c, RecipeCategory.BUILDING_BLOCKS, ModBlocks.RUNESTONE.get(), ModBlocks.CHISELED_RUNESTONE.get());
    stonecutterResultFromBase(c, RecipeCategory.BUILDING_BLOCKS, ModBlocks.RUNESTONE_BRICK.get(), ModBlocks.CHISELED_RUNESTONE.get());
    stonecutterResultFromBase(c, RecipeCategory.BUILDING_BLOCKS, ModBlocks.RUNESTONE_TILE.get(), ModBlocks.CHISELED_RUNESTONE.get());

    // Runed obsidian
    stonecutterResultFromBase(c, RecipeCategory.BUILDING_BLOCKS, ModBlocks.RUNED_BRICK.get(), ModBlocks.RUNED_OBSIDIAN.get());
    stonecutterResultFromBase(c, RecipeCategory.BUILDING_BLOCKS, ModBlocks.RUNED_TILE.get(), ModBlocks.RUNED_OBSIDIAN.get());
    stonecutterResultFromBase(c, RecipeCategory.BUILDING_BLOCKS, ModBlocks.CHISELED_RUNED_OBSIDIAN.get(), ModBlocks.RUNED_OBSIDIAN.get());

    stonecutterResultFromBase(c, RecipeCategory.BUILDING_BLOCKS, ModBlocks.RUNED_OBSIDIAN.get(), ModBlocks.RUNED_BRICK.get());
    stonecutterResultFromBase(c, RecipeCategory.BUILDING_BLOCKS, ModBlocks.RUNED_TILE.get(), ModBlocks.RUNED_BRICK.get());
    stonecutterResultFromBase(c, RecipeCategory.BUILDING_BLOCKS, ModBlocks.CHISELED_RUNED_OBSIDIAN.get(), ModBlocks.RUNED_BRICK.get());

    stonecutterResultFromBase(c, RecipeCategory.BUILDING_BLOCKS, ModBlocks.RUNED_OBSIDIAN.get(), ModBlocks.RUNED_TILE.get());
    stonecutterResultFromBase(c, RecipeCategory.BUILDING_BLOCKS, ModBlocks.RUNED_BRICK.get(), ModBlocks.RUNED_TILE.get());
    stonecutterResultFromBase(c, RecipeCategory.BUILDING_BLOCKS, ModBlocks.CHISELED_RUNED_OBSIDIAN.get(), ModBlocks.RUNED_TILE.get());

    stonecutterResultFromBase(c, RecipeCategory.BUILDING_BLOCKS, ModBlocks.RUNED_OBSIDIAN.get(), ModBlocks.CHISELED_RUNED_OBSIDIAN.get());
    stonecutterResultFromBase(c, RecipeCategory.BUILDING_BLOCKS, ModBlocks.RUNED_BRICK.get(), ModBlocks.CHISELED_RUNED_OBSIDIAN.get());
    stonecutterResultFromBase(c, RecipeCategory.BUILDING_BLOCKS, ModBlocks.RUNED_TILE.get(), ModBlocks.CHISELED_RUNED_OBSIDIAN.get());

    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.WILDWOOD_CHEST.get())
        .pattern("WPW")
        .pattern("P P")
        .pattern("WPW")
        .define('W', RootsTags.Items.WILDWOOD_LOGS)
        .define('P', RootsTags.Items.WILDWOOD_PLANKS)
        .unlockedBy("has_wildwood_log", has(RootsTags.Items.WILDWOOD_LOGS))
        .save(c, RootsAPI.rl("wildwood_chest_from_logs_planks"));

    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.WILDWOOD_CHEST.get())
        .pattern("WPW")
        .pattern("P P")
        .pattern("WPW")
        .define('P', RootsTags.Items.WILDWOOD_LOGS)
        .define('W', RootsTags.Items.WILDWOOD_PLANKS)
        .unlockedBy("has_wildwood_log", has(RootsTags.Items.WILDWOOD_LOGS))
        .save(c, RootsAPI.rl("wildwood_chest_from_planks_logs"));

    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.RITUAL_PEDESTAL.get())
        .pattern("RRR")
        .pattern(" R ")
        .pattern("RRR")
        .define('R', RootsTags.Items.RUNESTONE)
        .unlockedBy("has_runestone", has(RootsTags.Items.RUNESTONE))
        .save(c);

/*    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.GROWTH_AMPLIFIER.get())
        .pattern("WRW")
        .pattern(" W ")
        .pattern("WWW")
        .define('R', RootsTags.Items.RUNESTONE)
        .define('W', ItemTags.LOGS)
        .unlockedBy("has_runestone", has(RootsTags.Items.RUNESTONE))
        .unlockedBy("has_logs", has(ItemTags.LOGS))
        .save(c);*/

    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.REINFORCED_RITUAL_PEDESTAL.get())
        .pattern("RRR")
        .pattern(" R ")
        .pattern("RRR")
        .define('R', RootsTags.Items.RUNED_OBSIDIAN)
        .unlockedBy("has_runed_obsidian", has(RootsTags.Items.RUNED_OBSIDIAN))
        .save(c);

    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.GROVE_CRAFTER.get())
        .pattern("RGR")
        .pattern("RRR")
        .define('G', RootsTags.Items.RUNESTONE_HERBS)
        .define('R', RootsTags.Items.RUNESTONE)
        .unlockedBy("has_runestone", has(RootsTags.Items.RUNESTONE))
        .save(c);

    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.GROVE_PEDESTAL.get(), 5)
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

    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.WILD_GROVE_STONE.get())
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

    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.SOUL_PYRE.get())
        .pattern("LCL")
        .pattern("RRR")
        .define('L', ItemTags.LOGS)
        .define('C', ItemTags.SOUL_FIRE_BASE_BLOCKS)
        .define('R', RootsTags.Items.RUNESTONE)
        .unlockedBy("has_runestone", has(RootsTags.Items.RUNESTONE))
        .unlockedBy("has_soul_fire_base_blocks", has(ItemTags.SOUL_FIRE_BASE_BLOCKS))
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

    ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModBlocks.SOUL_PYRE.get())
        .requires(ModBlocks.PYRE.get())
        .requires(ItemTags.SOUL_FIRE_BASE_BLOCKS)
        .unlockedBy("has_pyre", has(ModBlocks.PYRE.get()))
        .unlockedBy("has_soul_fire_base_blocks", has(ItemTags.SOUL_FIRE_BASE_BLOCKS))
        .save(c, RootsAPI.rl("soul_pyre_from_pyre"));

    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.REINFORCED_SOUL_PYRE.get())
        .pattern("XXX")
        .pattern("XCX")
        .pattern("XXX")
        .define('X', RootsTags.Items.RUNED_OBSIDIAN)
        .define('C', ModBlocks.SOUL_PYRE.get())
        .unlockedBy("has_runed_obsidian", has(RootsTags.Items.RUNED_OBSIDIAN))
        .save(c, RootsAPI.rl("reinforced_soul_pyre_from_soul_pyre"));

    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.REINFORCED_SOUL_PYRE.get())
        .pattern("LCL")
        .pattern("RRR")
        .define('L', ItemTags.LOGS)
        .define('C', ItemTags.SOUL_FIRE_BASE_BLOCKS)
        .define('R', RootsTags.Items.RUNED_OBSIDIAN)
        .unlockedBy("has_runestone", has(RootsTags.Items.RUNED_OBSIDIAN))
        .unlockedBy("has_soul_fire_base_blocks", has(ItemTags.SOUL_FIRE_BASE_BLOCKS))
        .save(c);

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

    ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, ModItems.WILDEWHEET_BREAD.get())
        .pattern("XXX")
        .define('X', RootsTags.Items.WILDEWHEET_CROP)
        .unlockedBy("has_wildewheet", has(RootsTags.Items.WILDEWHEET_CROP))
        .save(c);

    ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, ModItems.WILDROOT_STEW.get(), 2)
        .pattern("W ")
        .pattern("BB")
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

    SimpleCookingRecipeBuilder.smelting(Ingredient.of(RootsTags.Items.SILVER_ORE), RecipeCategory.MISC, ModItems.SILVER_INGOT.get(), 0.7f, 200)
        .unlockedBy("has_silver_ore", has(RootsTags.Items.SILVER_ORE))
        .save(c, RootsAPI.rl("silver_ingot_from_smelting_silver_ore"));
    SimpleCookingRecipeBuilder.blasting(Ingredient.of(RootsTags.Items.SILVER_ORE), RecipeCategory.MISC, ModItems.SILVER_INGOT.get(), 0.7f, 100)
        .unlockedBy("has_silver_ore", has(RootsTags.Items.SILVER_ORE))
        .save(c, RootsAPI.rl("silver_ingot_from_blasting_silver_ore"));

    SimpleCookingRecipeBuilder.smelting(Ingredient.of(RootsTags.Items.QUARTZ_ORE), RecipeCategory.MISC, Items.QUARTZ, 0.2f, 200)
        .unlockedBy("has_quartz_ore", has(RootsTags.Items.QUARTZ_ORE))
        .save(c, RootsAPI.rl("quartz_from_smelting_quartz_ore"));
    SimpleCookingRecipeBuilder.blasting(Ingredient.of(RootsTags.Items.QUARTZ_ORE), RecipeCategory.MISC, Items.QUARTZ, 0.2f, 100)
        .unlockedBy("has_quartz_ore", has(RootsTags.Items.QUARTZ_ORE))
        .save(c, RootsAPI.rl("quartz_from_blasting_quartz_ore"));

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

    // TODO: recycling recipes ^^^

    // JEI TEST RECIPES
/*    RecipeSaver.saver().unlockedBy("has_something", has(Items.STICK)).save(KnifeRecipe.Builder.create()
        .test(new BlockMatchWorldTest(Blocks.AMETHYST_BLOCK))
        .outputState(new PartialBlockState(Blocks.BUDDING_AMETHYST))
        .build(BaseRecipeData.Builder.create()
            *//*.result(Items.STICK, 2)*//*), c, RootsAPI.rl("knife/budding_amethyst_from_amethyst_block"));

    RecipeSaver.saver().unlockedBy("has_knife", has(RootsTags.Items.KNIVES)).save(KnifeRecipe.Builder.create()
        .test(new PartialBlockStateMatchWorldTest(new PartialBlockState(Blocks.SEA_PICKLE.defaultBlockState().setValue(SeaPickleBlock.PICKLES, 4), List.of("pickles"))))
        .outputState(new PartialBlockState(Blocks.SEA_PICKLE.defaultBlockState().setValue(SeaPickleBlock.PICKLES, 1), List.of("pickles")))
        .build(BaseRecipeData.Builder.create()
            .priority(100)
            .result(Items.SEA_PICKLE, 3)), c, RootsAPI.rl("knife/sea_pickle_shenanigans"));*/

    BaseRecipeData.Builder groveRunestoneBuilder = BaseRecipeData.Builder.create()
        .requires(RootsTags.Items.STONELIKE)
        .condition(ModConditions.ANY_GROVE_STONE_ACTIVE.get())
        .result(ModBlocks.RUNESTONE, 1);

    // TODO: Simplify this
    RecipeSaver saver = RecipeSaver.saver().unlockedBy("has_stone", has(RootsTags.Items.STONELIKE));
    saver.save(GroveRecipe.Builder.create().build(groveRunestoneBuilder), c, RootsAPI.rl("grove/runestone_1"));
    saver.save(GroveRecipe.Builder.create()
        .build(groveRunestoneBuilder.multiplty(2)), c, RootsAPI.rl("grove/runestone_2"));
    saver.save(GroveRecipe.Builder.create()
        .build(groveRunestoneBuilder.multiplty(3)), c, RootsAPI.rl("grove/runestone_3"));
    saver.save(GroveRecipe.Builder.create()
        .build(groveRunestoneBuilder.multiplty(4)), c, RootsAPI.rl("grove/runestone_4"));
    saver.save(GroveRecipe.Builder.create()
        .build(groveRunestoneBuilder.multiplty(5)), c, RootsAPI.rl("grove/runestone_5"));
    saver.save(GroveRecipe.Builder.create()
        .build(groveRunestoneBuilder.multiplty(6)), c, RootsAPI.rl("grove/runestone_6"));
    saver.save(GroveRecipe.Builder.create()
        .build(groveRunestoneBuilder.multiplty(7)), c, RootsAPI.rl("grove/runestone_7"));
    saver.save(GroveRecipe.Builder.create()
        .build(groveRunestoneBuilder.multiplty(8)), c, RootsAPI.rl("grove/runestone_8"));
    saver.save(GroveRecipe.Builder.create()
        .build(groveRunestoneBuilder.multiplty(9)), c, RootsAPI.rl("grove/runestone_9"));
    saver.save(GroveRecipe.Builder.create()
        .build(groveRunestoneBuilder.multiplty(10)), c, RootsAPI.rl("grove/runestone_10"));

    BaseRecipeData.Builder runedObsidianBuilder = BaseRecipeData.Builder.create()
        .requires(RootsTags.Items.RUNESTONE)
        .requires(RootsTags.Items.RUNESTONE)
        .requires(RootsTags.Items.RUNESTONE)
        .requires(Tags.Items.OBSIDIANS)
        .condition(ModConditions.ANY_GROVE_STONE_ACTIVE.get())
        .result(ModBlocks.RUNED_OBSIDIAN, 4);

    saver = RecipeSaver.saver().unlockedBy("has_runestone", has(RootsTags.Items.RUNESTONE))
        .unlockedBy("has_obsidian", has(Tags.Items.OBSIDIANS));
    saver.save(GroveRecipe.Builder.create().build(runedObsidianBuilder), c, RootsAPI.rl("grove/runed_obsidian_4"));
    saver.save(GroveRecipe.Builder.create()
        .build(runedObsidianBuilder.multiplty(2)), c, RootsAPI.rl("grove/runed_obsidian_8"));

    saver = RecipeSaver.saver().unlockedBy("has_sugar_cane", has(Tags.Items.CROPS_SUGAR_CANE));
    MortarRecipe.Builder mortarbuilder = MortarRecipe.Builder.create().times(5);
    BaseRecipeData.Builder sugarbuilder = BaseRecipeData.Builder.create().requires(Tags.Items.CROPS_SUGAR_CANE)
        .result(Items.SUGAR, 1).chanceOutput(Items.SUGAR, 0.5f);

    saver.save(mortarbuilder.build(sugarbuilder), c, RootsAPI.rl("mortar/sugar_from_sugar_cane_1"));
    saver.save(mortarbuilder.times(5)
        .build(sugarbuilder.multiplty(2)), c, RootsAPI.rl("mortar/sugar_from_sugar_cane_2"));
    saver.save(mortarbuilder.times(5)
        .build(sugarbuilder.multiplty(3)), c, RootsAPI.rl("mortar/sugar_from_sugar_cane_3"));
    saver.save(mortarbuilder.times(5)
        .build(sugarbuilder.multiplty(4)), c, RootsAPI.rl("mortar/sugar_from_sugar_cane_4"));
    saver.save(mortarbuilder.times(5)
        .build(sugarbuilder.multiplty(5)), c, RootsAPI.rl("mortar/sugar_from_sugar_cane_5"));

    RecipeSaver.saver().unlockedBy("has_runic_dust", has(RootsTags.Items.RUNIC_DUST)).save(GroveRecipe.Builder.create()
        .build(BaseRecipeData.Builder.create().requires(RootsTags.Items.RUNIC_DUST).requires(Tags.Items.GRAVELS)
            .requires(ItemTags.DIRT).requires(ItemTags.DIRT).requires(ItemTags.DIRT).result(ModItems.ELEMENTAL_SOIL, 4)
            .condition(ModConditions.ANY_GROVE_STONE_ACTIVE.get())), c, RootsAPI.rl("grove/elemental_soil"));

    ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.AQUEOUS_SOIL.get(), 1)
        .requires(ModItems.ELEMENTAL_SOIL.get()).requires(RootsTags.Items.DEWGONIA_HERB)
        .unlockedBy("has_elemental_soil", has(ModItems.ELEMENTAL_SOIL.get()))
        .unlockedBy("has_dewgonia", has(RootsTags.Items.DEWGONIA_HERB))
        .save(c, RootsAPI.rl("aqueous_soil_from_elemental_soil_and_dewgonia"));

    ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.MAGMATIC_SOIL.get(), 1)
        .requires(ModItems.ELEMENTAL_SOIL.get()).requires(RootsTags.Items.INFERNO_BULB_HERB)
        .unlockedBy("has_elemental_soil", has(ModItems.ELEMENTAL_SOIL.get()))
        .unlockedBy("has_inferno_bulb", has(RootsTags.Items.INFERNO_BULB_HERB))
        .save(c, RootsAPI.rl("magmatic_soil_from_elemental_soil_and_inferno_bulb"));

    RecipeSaver.saver().unlockedBy("has_elemental_soil", has(ModItems.ELEMENTAL_SOIL.get()))
        .save(GroveRecipe.Builder.create().build(BaseRecipeData.Builder.create()
            .requires(ModItems.ELEMENTAL_SOIL.get())
            .requires(ModItems.ELEMENTAL_SOIL.get())
            .requires(ModItems.ELEMENTAL_SOIL.get())
            .requires(ModItems.ELEMENTAL_SOIL.get())
            .requires(RootsTags.Items.GROVE_MOSS_HERB)
            .result(ModItems.ENCHANTED_TURF.get(), 1)
            .condition(ModConditions.ANY_GROVE_STONE_ACTIVE.get())), c, RootsAPI.rl("wildwood_soil_from_elemental_soil_and_wildwood_herb"));

    ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.TERRAN_SOIL.get(), 1)
        .requires(ModItems.ELEMENTAL_SOIL.get()).requires(RootsTags.Items.STALICRIPE_HERB)
        .unlockedBy("has_elemental_soil", has(ModItems.ELEMENTAL_SOIL.get()))
        .unlockedBy("has_stalicripe", has(RootsTags.Items.STALICRIPE_HERB))
        .save(c, RootsAPI.rl("terran_soil_from_elemental_soil_and_stalicripe"));

    ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.CAELIC_SOIL.get(), 1)
        .requires(ModItems.ELEMENTAL_SOIL.get()).requires(RootsTags.Items.CLOUD_BERRY_HERB)
        .unlockedBy("has_elemental_soil", has(ModItems.ELEMENTAL_SOIL.get()))
        .unlockedBy("has_cloud_berry", has(RootsTags.Items.CLOUD_BERRY_HERB))
        .save(c, RootsAPI.rl("caelic_soil_from_elemental_soil_and_cloud_berry"));

    // TODO: Ritual pedestal
    // TODO: Reinforced ritual pedestal
    RecipeSaver.saver().unlockedBy("has_wildwood", has(RootsTags.Items.WILDWOOD_LOGS)).save(GroveRecipe.Builder.create()
        .build(BaseRecipeData.Builder.create().requires(RootsTags.Items.WILDWOOD_LOGS)
            .requires(RootsTags.Items.WILDWOOD_LOGS).requires(RootsTags.Items.WILDWOOD_LOGS)
            .requires(RootsTags.Items.WILDWOOD_LOGS)
            .condition(ModConditions.ANY_GROVE_STONE_ACTIVE.get())
            .result(ModBlocks.WILDWOOD_PEDESTAL, 5)), c, RootsAPI.rl("grove/wildwood_pedestal"));

    ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.DECORATIVE_PYRE.get(), 1)
        .requires(ModItems.PYRE.get()).requires(RootsTags.Items.LEVERS)
        .unlockedBy("has_decorative_pyre", has(ModItems.PYRE.get()))
        .unlockedBy("has_lever", has(RootsTags.Items.LEVERS))
        .save(c, RootsAPI.rl("decorative_pyre_from_pyre_and_lever"));

    ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.DECORATIVE_SOUL_PYRE.get(), 1)
        .requires(ModItems.SOUL_PYRE.get()).requires(RootsTags.Items.LEVERS)
        .unlockedBy("has_decorative_soul_pyre", has(ModItems.SOUL_PYRE.get()))
        .unlockedBy("has_lever", has(RootsTags.Items.LEVERS))
        .save(c, RootsAPI.rl("decorative_soul_pyre_from_soul_pyre_and_lever"));

    RecipeSaver.saver().unlockedBy("has_runestone", has(RootsTags.Items.RUNESTONE)).save(GroveRecipe.Builder.create()
        .build(BaseRecipeData.Builder.create().requires(RootsTags.Items.RUNESTONE).requires(RootsTags.Items.RUNESTONE)
            .requires(ItemTags.SMALL_FLOWERS).requires(RootsTags.Items.GROVE_MOSS_HERB)
            .condition(ModConditions.ANY_GROVE_STONE_ACTIVE.get())
            .requires(Ingredient.of(ModItems.WOODEN_SHEARS.get(), Items.SHEARS))
            .result(ModItems.RUNIC_SHEARS)), c, RootsAPI.rl("grove/runic_shears"));

    RecipeSaver.saver().unlockedBy("has_grove_moss", has(RootsTags.Items.GROVE_MOSS_HERB))
        .save(GroveRecipe.Builder.create()
            .build(BaseRecipeData.Builder.create().requires(RootsTags.Items.GROVE_MOSS_HERB)
                .requires(Tags.Items.INGOTS_GOLD)
                .requires(RootsTags.Items.WILDROOT_HERB).requires(RootsTags.Items.WILDROOT_HERB)
                .requires(Items.WOODEN_PICKAXE)
                .condition(ModConditions.ANY_GROVE_STONE_ACTIVE.get())
                .result(ModItems.LIVING_PICKAXE)), c, RootsAPI.rl("grove/living_pickaxe"));

    // Living sword
    RecipeSaver.saver().unlockedBy("has_grove_moss", has(RootsTags.Items.GROVE_MOSS_HERB))
        .save(GroveRecipe.Builder.create()
            .build(BaseRecipeData.Builder.create().requires(RootsTags.Items.GROVE_MOSS_HERB)
                .requires(Tags.Items.INGOTS_GOLD)
                .requires(RootsTags.Items.WILDROOT_HERB).requires(RootsTags.Items.WILDROOT_HERB)
                .requires(Items.WOODEN_SWORD)
                .condition(ModConditions.ANY_GROVE_STONE_ACTIVE.get())
                .result(ModItems.LIVING_SWORD)), c, RootsAPI.rl("grove/living_sword"));

    // Living axe
    RecipeSaver.saver().unlockedBy("has_grove_moss", has(RootsTags.Items.GROVE_MOSS_HERB))
        .save(GroveRecipe.Builder.create()
            .build(BaseRecipeData.Builder.create().requires(RootsTags.Items.GROVE_MOSS_HERB)
                .requires(Tags.Items.INGOTS_GOLD)
                .requires(RootsTags.Items.WILDROOT_HERB).requires(RootsTags.Items.WILDROOT_HERB)
                .requires(Items.WOODEN_AXE)
                .condition(ModConditions.ANY_GROVE_STONE_ACTIVE.get())
                .result(ModItems.LIVING_AXE)), c, RootsAPI.rl("grove/living_axe"));

    // Living hoe
    RecipeSaver.saver().unlockedBy("has_grove_moss", has(RootsTags.Items.GROVE_MOSS_HERB))
        .save(GroveRecipe.Builder.create()
            .build(BaseRecipeData.Builder.create().requires(RootsTags.Items.GROVE_MOSS_HERB)
                .requires(Tags.Items.INGOTS_GOLD)
                .requires(RootsTags.Items.WILDROOT_HERB).requires(RootsTags.Items.WILDROOT_HERB)
                .requires(Items.WOODEN_HOE)
                .condition(ModConditions.ANY_GROVE_STONE_ACTIVE.get())
                .result(ModItems.LIVING_HOE)), c, RootsAPI.rl("grove/living_hoe"));

    // Living shovel
    RecipeSaver.saver().unlockedBy("has_grove_moss", has(RootsTags.Items.GROVE_MOSS_HERB))
        .save(GroveRecipe.Builder.create()
            .build(BaseRecipeData.Builder.create().requires(RootsTags.Items.GROVE_MOSS_HERB)
                .requires(Tags.Items.INGOTS_GOLD)
                .requires(RootsTags.Items.WILDROOT_HERB).requires(RootsTags.Items.WILDROOT_HERB)
                .requires(Items.WOODEN_SHOVEL)
                .condition(ModConditions.ANY_GROVE_STONE_ACTIVE.get())
                .result(ModItems.LIVING_SHOVEL)), c, RootsAPI.rl("grove/living_shovel"));


    RecipeSaver.saver().unlockedBy("has_shears", has(RootsTags.Items.RUNIC_SHEARS))
        .save(RunicEntityRecipe.Builder.create().durabilityCost(10)
            .test(new EntityTagTest(RootsTags.Entities.SYLVAN_LEATHER)).cooldown(2 * 60 * 20)
            .build(BaseRecipeData.Builder.create()
                .result(ModItems.SYLVAN_LEATHER, 1)), c, RootsAPI.rl("runic_entity/sylvan_leather"));

    RecipeSaver.saver().unlockedBy("has_shears", has(RootsTags.Items.RUNIC_SHEARS))
        .save(RunicBlockRecipe.Builder.create().durabilityCost(15)
            .test(new PartialBlockStateMatchWorldTest(new PartialBlockState(Blocks.WHEAT.defaultBlockState()
                .setValue(CropBlock.AGE, CropBlock.MAX_AGE))))
            .outputState(new PartialBlockState(Blocks.WHEAT.defaultBlockState().setValue(CropBlock.AGE, 0)))
            .skipProperty(CropBlock.AGE)
            .build(BaseRecipeData.Builder.create()
                .result(ModItems.WILDEWHEET_SEEDS, 1)
                .chanceOutput(ModItems.WILDEWHEET, 0.6f)
                .build()
            ), c, RootsAPI.rl("runic_block/wildewheet_seeds_and_wheat_chance"));

    // Spiritleaf from beetroot
    RecipeSaver.saver().unlockedBy("has_shears", has(RootsTags.Items.RUNIC_SHEARS))
        .save(RunicBlockRecipe.Builder.create().durabilityCost(15)
            .test(new PartialBlockStateMatchWorldTest(new PartialBlockState(Blocks.BEETROOTS.defaultBlockState()
                .setValue(BeetrootBlock.AGE, BeetrootBlock.MAX_AGE))))
            .outputState(new PartialBlockState(Blocks.BEETROOTS.defaultBlockState().setValue(BeetrootBlock.AGE, 0)))
            .skipProperties(BeetrootBlock.AGE)
            .build(BaseRecipeData.Builder.create()
                .result(ModItems.SPIRITLEAF_SEEDS, 1)
                .chanceOutput(ModItems.SPIRITLEAF, 0.6f)
                .build()
            ), c, RootsAPI.rl("runic_block/spiritleaf_from_beetroot"));

    MortarRecipe.Builder flourBuilder = MortarRecipe.Builder.create().times(5);
    BaseRecipeData.Builder flourData = BaseRecipeData.Builder.create().requires(Tags.Items.CROPS_WHEAT)
        .result(ModItems.FLOUR, 1);
    saver = RecipeSaver.saver().unlockedBy("has_wheat", has(Tags.Items.CROPS_WHEAT));

    saver.save(flourBuilder.build(flourData), c, RootsAPI.rl("mortar/flour"));
    saver.save(flourBuilder.times(5).build(flourData.multiplty(2)), c, RootsAPI.rl("mortar/flour_2"));
    saver.save(flourBuilder.times(5).build(flourData.multiplty(3)), c, RootsAPI.rl("mortar/flour_3"));
    saver.save(flourBuilder.times(5).build(flourData.multiplty(4)), c, RootsAPI.rl("mortar/flour_4"));
    saver.save(flourBuilder.times(5).build(flourData.multiplty(5)), c, RootsAPI.rl("mortar/flour_5"));

    MortarRecipe.Builder runicDustBuilder = MortarRecipe.Builder.create().times(10);
    BaseRecipeData.Builder runicDustData = BaseRecipeData.Builder.create().requires(RootsTags.Items.RUNESTONE)
        .result(ModItems.RUNIC_DUST, 1);
    saver = RecipeSaver.saver().unlockedBy("has_runestone", has(RootsTags.Items.RUNESTONE));

    saver.save(runicDustBuilder.build(runicDustData), c, RootsAPI.rl("mortar/runic_dust"));
    saver.save(runicDustBuilder.times(10).build(runicDustData.multiplty(2)), c, RootsAPI.rl("mortar/runic_dust_2"));
    saver.save(runicDustBuilder.times(10).build(runicDustData.multiplty(3)), c, RootsAPI.rl("mortar/runic_dust_3"));
    saver.save(runicDustBuilder.times(10).build(runicDustData.multiplty(4)), c, RootsAPI.rl("mortar/runic_dust_4"));
    saver.save(runicDustBuilder.times(10).build(runicDustData.multiplty(5)), c, RootsAPI.rl("mortar/runic_dust_5"));

    MortarRecipe.Builder stringBuilder = MortarRecipe.Builder.create().times(7);
    BaseRecipeData.Builder stringData = BaseRecipeData.Builder.create().requires(ItemTags.WOOL)
        .result(Items.STRING.builtInRegistryHolder(), 2)
        .chanceOutput(Items.STRING, 0.5f)
        .chanceOutput(Items.STRING, 0.25f);
    saver = RecipeSaver.saver().unlockedBy("has_wool", has(ItemTags.WOOL));

    saver.save(stringBuilder.build(stringData), c, RootsAPI.rl("mortar/string_from_wool"));
    saver.save(stringBuilder.times(7).build(stringData.multiplty(2)), c, RootsAPI.rl("mortar/string_from_wool_2"));
    saver.save(stringBuilder.times(7).build(stringData.multiplty(3)), c, RootsAPI.rl("mortar/string_from_wool_3"));
    saver.save(stringBuilder.times(7).build(stringData.multiplty(4)), c, RootsAPI.rl("mortar/string_from_wool_4"));
    saver.save(stringBuilder.times(7).build(stringData.multiplty(5)), c, RootsAPI.rl("mortar/string_from_wool_5"));

    MortarRecipe.Builder bonemealBuilder = MortarRecipe.Builder.create().times(10);
    BaseRecipeData.Builder bonemealData = BaseRecipeData.Builder.create().requires(Tags.Items.BONES)
        .result(Items.BONE_MEAL.builtInRegistryHolder(), 3).chanceOutput(Items.BONE_MEAL, 0.5f);
    saver = RecipeSaver.saver().unlockedBy("has_bones", has(Tags.Items.BONES));

    saver.save(bonemealBuilder.build(bonemealData), c, RootsAPI.rl("mortar/bonemeal_from_bones"));
    saver.save(bonemealBuilder.times(10)
        .build(bonemealData.multiplty(2)), c, RootsAPI.rl("mortar/bonemeal_from_bones_2"));
    saver.save(bonemealBuilder.times(10)
        .build(bonemealData.multiplty(3)), c, RootsAPI.rl("mortar/bonemeal_from_bones_3"));
    saver.save(bonemealBuilder.times(10)
        .build(bonemealData.multiplty(4)), c, RootsAPI.rl("mortar/bonemeal_from_bones_4"));
    saver.save(bonemealBuilder.times(10)
        .build(bonemealData.multiplty(5)), c, RootsAPI.rl("mortar/bonemeal_from_bones_5"));

    MortarRecipe.Builder flintBuilder = MortarRecipe.Builder.create().times(10);
    BaseRecipeData.Builder flintData = BaseRecipeData.Builder.create().requires(Tags.Items.GRAVELS)
        .result(Items.FLINT.builtInRegistryHolder(), 1);
    saver = RecipeSaver.saver().unlockedBy("has_flint", has(RootsTags.Items.FLINT));

    saver.save(flintBuilder.build(flintData), c, RootsAPI.rl("mortar/flint_from_gravel"));
    saver.save(flintBuilder.times(10).build(flintData.multiplty(2)), c, RootsAPI.rl("mortar/flint_from_gravel_2"));
    saver.save(flintBuilder.times(10).build(flintData.multiplty(3)), c, RootsAPI.rl("mortar/flint_from_gravel_3"));
    saver.save(flintBuilder.times(10).build(flintData.multiplty(4)), c, RootsAPI.rl("mortar/flint_from_gravel_4"));
    saver.save(flintBuilder.times(10).build(flintData.multiplty(5)), c, RootsAPI.rl("mortar/flint_from_gravel_5"));

    MortarRecipe.Builder magmaCreamFromMagmaBlockBuilder = MortarRecipe.Builder.create().times(10);
    BaseRecipeData.Builder magmaCreamFromMagmaBlockData = BaseRecipeData.Builder.create().requires(Items.MAGMA_BLOCK)
        .result(Items.MAGMA_CREAM.builtInRegistryHolder(), 4);
    saver = RecipeSaver.saver().unlockedBy("has_magma_block", has(Items.MAGMA_BLOCK));

    saver.save(magmaCreamFromMagmaBlockBuilder.build(magmaCreamFromMagmaBlockData), c, RootsAPI.rl("mortar/magma_cream_from_magma_block"));
    saver.save(magmaCreamFromMagmaBlockBuilder.times(10)
        .build(magmaCreamFromMagmaBlockData.multiplty(2)), c, RootsAPI.rl("mortar/magma_cream_from_magma_block_2"));
    saver.save(magmaCreamFromMagmaBlockBuilder.times(10)
        .build(magmaCreamFromMagmaBlockData.multiplty(3)), c, RootsAPI.rl("mortar/magma_cream_from_magma_block_3"));
    saver.save(magmaCreamFromMagmaBlockBuilder.times(10)
        .build(magmaCreamFromMagmaBlockData.multiplty(4)), c, RootsAPI.rl("mortar/magma_cream_from_magma_block_4"));
    saver.save(magmaCreamFromMagmaBlockBuilder.times(10)
        .build(magmaCreamFromMagmaBlockData.multiplty(5)), c, RootsAPI.rl("mortar/magma_cream_from_magma_block_5"));

    RecipeSaver.saver().unlockedBy("has_torch", has(Items.TORCH)).save(GroveRecipe.Builder.create()
        .build(BaseRecipeData.Builder.create().requires(Tags.Items.GLASS_BLOCKS).requires(Tags.Items.GLASS_BLOCKS)
            .requires(Tags.Items.GLASS_BLOCKS).requires(Tags.Items.GLASS_BLOCKS).requires(Items.TORCH)
            .condition(ModConditions.ANY_GROVE_STONE_ACTIVE.get())
            .result(ModItems.GLASS_EYE, 2)), c, RootsAPI.rl("grove/glass_eye"));

    RecipeSaver.saver().unlockedBy("has_honeycomb", has(Items.HONEYCOMB))
        .save(PyreRecipe.Builder.create().ritual(ModRituals.HEALING_AURA)
            .build(BaseRecipeData.Builder.create()
                .requires(Items.GLISTERING_MELON_SLICE)
                .requires(Tags.Items.CROPS_CARROT)
                .requires(Items.HONEYCOMB)
                .requires(Items.APPLE)
                .requires(RootsTags.Items.SILVER_INGOT)), c, RootsAPI.rl("pyre/healing_aura")
        );

    RecipeSaver.saver().unlockedBy("has_dewgonia", has(RootsTags.Items.DEWGONIA_HERB))
        .save(PyreRecipe.Builder.create().ritual(ModRituals.FROST_LANDS)
            .build(BaseRecipeData.Builder.create()
                .requires(Tags.Items.CROPS_SUGAR_CANE)
                .requires(RootsTags.Items.DEWGONIA_HERB)
                .requires(ItemTags.SPRUCE_LOGS)
                .requires(ItemTags.SPRUCE_LOGS)
                .requires(Items.VINE)), c, RootsAPI.rl("pyre/frost_lands"));

    RecipeSaver.saver().unlockedBy("has_wildroot", has(RootsTags.Items.WILDROOT_HERB))
        .save(PyreRecipe.Builder.create().ritual(ModRituals.ANIMAL_HARVEST)
            .build(BaseRecipeData.Builder.create().requires(RootsTags.Items.WILDEWHEET_HERB).requires(ItemTags.WOOL)
                .requires(Tags.Items.CROPS_CARROT).requires(Tags.Items.CROPS_POTATO)
                .requires(RootsTags.Items.WILDROOT_HERB)
                .condition(ModConditions.RUNESTONE_PILLAR_3_HIGH.get())
                .condition(ModConditions.RUNESTONE_PILLAR_4_HIGH.get())
                .condition(ModConditions.RUNESTONE_PILLAR_3_HIGH.get())), c, RootsAPI.rl("pyre/animal_harvest"));

    RecipeSaver.saver().unlockedBy("has_wildwood", has(RootsTags.Items.WILDWOOD_LOGS))
        .save(PyreRecipe.Builder.create().ritual(ModRituals.AUGMENTATION)
            .build(BaseRecipeData.Builder.create()
                .requires(Items.GOLDEN_CARROT)
                .requires(RootsTags.Items.SPIRITLEAF_CROP)
                .requires(RootsTags.Items.SILVER_INGOT)
                .requires(ItemTags.BIRCH_LOGS)
                .requires(RootsTags.Items.WILDWOOD_LOGS)
                .condition(ModConditions.WILD_RANK_1.get())
                .condition(ModConditions.RUNESTONE_PILLAR_3_HIGH.get())
                .condition(ModConditions.RUNESTONE_PILLAR_4_HIGH.get())
                .condition(ModConditions.RUNESTONE_PILLAR_3_HIGH.get())), c, RootsAPI.rl("pyre/augmentation"));

    RecipeSaver.saver().unlockedBy("has_stalicripe", has(RootsTags.Items.STALICRIPE_HERB))
        .save(PyreRecipe.Builder.create().ritual(ModRituals.GATHERING).build(
            BaseRecipeData.Builder.create()
                .requires(RootsTags.Items.STALICRIPE_HERB)
                .requires(RootsTags.Items.WILDROOT_HERB)
                .requires(ItemTags.BUTTONS)
                .requires(RootsTags.Items.LEVERS)
                .requires(Tags.Items.DUSTS_REDSTONE)), c, RootsAPI.rl("pyre/gathering"));

    RecipeSaver.saver().unlockedBy("has_stalicripe", has(RootsTags.Items.STALICRIPE_HERB))
        .save(PyreRecipe.Builder.create().ritual(ModRituals.WARDING).build(
            BaseRecipeData.Builder.create()
                .requires(Tags.Items.INGOTS)
                .requires(RootsTags.Items.STALICRIPE_HERB)
                .requires(Tags.Items.OBSIDIANS)
                .requires(Tags.Items.LEATHERS)
                .requires(Tags.Items.BRICKS)), c, RootsAPI.rl("pyre/warding"));

    RecipeSaver.saver().unlockedBy("has_pereskia", has(RootsTags.Items.PERESKIA_HERB))
        .save(PyreRecipe.Builder.create().ritual(ModRituals.GERMINATION).build(
            BaseRecipeData.Builder.create()
                .requires(Tags.Items.FERTILIZERS)
                .requires(ItemTags.OAK_LOGS)
                .requires(Tags.Items.SEEDS)
                .requires(ItemTags.DIRT)
                .requires(RootsTags.Items.PERESKIA_HERB)), c, RootsAPI.rl("pyre/germination"));

    RecipeSaver.saver().unlockedBy("has_cloud_berry", has(RootsTags.Items.CLOUD_BERRY_HERB))
        .save(PyreRecipe.Builder.create().ritual(ModRituals.WINDWALL).build(
            BaseRecipeData.Builder.create()
                .requires(ItemTags.SMALL_FLOWERS)
                .requires(RootsTags.Items.CLOUD_BERRY_HERB)
                .requires(RootsTags.Items.SHORT_GRASS)
                .requires(Items.HONEYCOMB) // TODO: Tag?
                .requires(Tags.Items.FEATHERS)), c, RootsAPI.rl("pyre/windwall"));

    RecipeSaver.saver().unlockedBy("has_moonglow", has(RootsTags.Items.MOONGLOW_HERB))
        .save(PyreRecipe.Builder.create().ritual(ModRituals.PROTECTION).build(
            BaseRecipeData.Builder.create()
                .requires(RootsTags.Items.MOONGLOW_HERB)
                .requires(Items.SUNFLOWER) // TODO: Tag?
                .requires(Tags.Items.RAW_MATERIALS_IRON)
                .requires(ItemTags.STONE_CRAFTING_MATERIALS)
                .requires(Items.HONEYCOMB)
        ), c, RootsAPI.rl("pyre/protection"));

    RecipeSaver.saver().unlockedBy("has_cloud_berry", has(RootsTags.Items.CLOUD_BERRY_HERB))
        .save(PyreRecipe.Builder.create().ritual(ModRituals.HEAVY_STORMS).build(
            BaseRecipeData.Builder.create()
                .requires(RootsTags.Items.DEWGONIA_HERB)
                .requires(RootsTags.Items.CLOUD_BERRY_HERB)
                .requires(Tags.Items.GEMS_LAPIS)
                .requires(RootsTags.Items.COPPER_NUGGET)
                .requires(RootsTags.Items.GROVE_MOSS_HERB)), c, RootsAPI.rl("pyre/heavy_storms"));

    RecipeSaver.saver().unlockedBy("has_inferno_bulb", has(RootsTags.Items.INFERNO_BULB_HERB))
        .save(PyreRecipe.Builder.create().ritual(ModRituals.FIRE_STORM).build(
            BaseRecipeData.Builder.create()
                .requires(RootsTags.Items.INFERNO_BULB_HERB)
                .requires(ItemTags.COALS)
                .requires(Tags.Items.DUSTS_REDSTONE)
                .requires(ItemTags.LOGS_THAT_BURN)
                .requires(Blocks.MAGMA_BLOCK.asItem()) // TODO: Tag
        ), c, RootsAPI.rl("pyre/fire_storm"));

    RecipeSaver.saver().unlockedBy("has_pereskia", has(RootsTags.Items.PERESKIA_HERB))
        .save(PyreRecipe.Builder.create().ritual(ModRituals.BLOOMING).build(
            BaseRecipeData.Builder.create()
                .requires(RootsTags.Items.PERESKIA_HERB)
                .requires(ItemTags.SMALL_FLOWERS)
                .requires(ItemTags.SMALL_FLOWERS)
                .requires(Tags.Items.NUGGETS_GOLD)
                .requires(Tags.Items.FERTILIZERS)
        ), c, RootsAPI.rl("pyre/blooming"));

    RecipeSaver.saver().unlockedBy("has_spiritleaf", has(RootsTags.Items.SPIRITLEAF_HERB))
        .save(PyreRecipe.Builder.create().ritual(ModRituals.SPREADING_FOREST).build(
            BaseRecipeData.Builder.create()
                .requires(RootsTags.Items.SPIRITLEAF_HERB)
                .requires(Tags.Items.FERTILIZERS)
                .requires(ItemTags.SAPLINGS)
                .requires(ItemTags.SAPLINGS)
                .requires(RootsTags.Items.WILDWOOD_LOGS)), c, RootsAPI.rl("pyre/spreading_forest"));

    RecipeSaver.saver().unlockedBy("has_bafflecap", has(RootsTags.Items.BAFFLECAP_HERB))
        .save(PyreRecipe.Builder.create().ritual(ModRituals.PURITY).build(
            BaseRecipeData.Builder.create()
                .requires(RootsTags.Items.BOTTLES)
                .requires(RootsTags.Items.WILDROOT_HERB)
                .requires(RootsTags.Items.BAFFLECAP_HERB)
                .requires(Tags.Items.GEMS_QUARTZ)
                .requires(Tags.Items.GLASS_BLOCKS)), c, RootsAPI.rl("pyre/purity"));

    RecipeSaver.saver().unlockedBy("has_spiritleaf", has(RootsTags.Items.SPIRITLEAF_HERB))
        .save(PyreRecipe.Builder.create().ritual(ModRituals.SUMMON_CREATURES).build(
            BaseRecipeData.Builder.create()
                .requires(Tags.Items.BONES)
                .requires(RootsTags.Items.SPIRITLEAF_HERB)
                .requires(Items.APPLE) // TODO: Tag
                .requires(Tags.Items.SEEDS)
                .requires(Tags.Items.MUSHROOMS)), c, RootsAPI.rl("pyre/summon_creatures"));

    RecipeSaver.saver().unlockedBy("has_sugar_cane", has(Tags.Items.CROPS_SUGAR_CANE))
        .save(PyreRecipe.Builder.create().ritual(ModRituals.OVERGROWTH)
            .build(BaseRecipeData.Builder.create().requires(ItemTags.LOGS)
                .requires(ItemTags.LOGS)
                .requires(RootsTags.Items.GROVE_MOSS_HERB).requires(Tags.Items.CROPS_SUGAR_CANE)
                .requires(RootsTags.Items.SHORT_GRASS)
                .condition(ModConditions.OVERGROWTH.get())), c, RootsAPI.rl("pyre/overgrowth"));

    RecipeSaver.saver().unlockedBy("has_door", has(ItemTags.DOORS))
        .save(PyreRecipe.Builder.create().ritual(ModRituals.GROVE_SUPPLICATION)
            .build(BaseRecipeData.Builder.create().requires(ItemTags.DOORS).requires(Items.BOWL)
                .requires(ItemTags.SAPLINGS).requires(ItemTags.SMALL_FLOWERS).requires(Tags.Items.FOODS_BREAD)
                .condition(ModConditions.ANY_GROVE_STONE.get())), c, RootsAPI.rl("pyre/grove_supplication"));

    RecipeSaver.saver().unlockedBy("has_spiritleaf", has(RootsTags.Items.SPIRITLEAF_HERB))
        .save(PyreRecipe.Builder.create().ritual(ModRituals.WILDROOT_GROWTH)
            .build(BaseRecipeData.Builder.create().requires(RootsTags.Items.WILDROOT_HERB)
                .requires(ItemTags.LOGS).requires(ItemTags.LOGS)
                .requires(RootsTags.Items.SPIRITLEAF_HERB).requires(ItemTags.SAPLINGS)
                .condition(ModConditions.MATURE_WILDROOT_CROP.get())), c, RootsAPI.rl("pyre/wildroot_growth"));

    RecipeSaver.saver().unlockedBy("has_lightning_rod", has(Items.LIGHTNING_ROD)).save(PyreRecipe.Builder.create()
        .build(BaseRecipeData.Builder.create().requires(Items.LIGHTNING_ROD).requires(Items.SUGAR)
            .requires(ItemTags.LEAVES).requires(ItemTags.WOOL).requires(ItemTags.LOGS)
            .result(ModItems.CLOUD_BERRY, 2)), c, RootsAPI.rl("pyre/cloud_berry"));

    RecipeSaver.saver().unlockedBy("has_kelp", has(Items.KELP)).save(PyreRecipe.Builder.create()
        .build(BaseRecipeData.Builder.create().requires(Items.WATER_BUCKET).requires(Items.CLAY_BALL)
            .requires(Items.SEAGRASS).requires(Tags.Items.CROPS_SUGAR_CANE).requires(Items.KELP)
            .result(ModItems.DEWGONIA, 2)), c, RootsAPI.rl("pyre/dewgonia"));

    RecipeSaver.saver().unlockedBy("has_netherrack", has(Tags.Items.NETHERRACKS)).save(PyreRecipe.Builder.create()
        .build(BaseRecipeData.Builder.create().requires(Items.MAGMA_CREAM).requires(Tags.Items.NETHERRACKS)
            .requires(ItemTags.COALS).requires(Items.STICK).requires(Items.BRICK)
            .result(ModItems.INFERNO_BULB, 2)), c, RootsAPI.rl("pyre/inferno_bulb"));

    RecipeSaver.saver().unlockedBy("has_pink_tulip", has(Items.PINK_TULIP))
        .save(PyreRecipe.Builder.create()
            .build(BaseRecipeData.Builder.create().requires(Items.POPPY).requires(Items.SUGAR)
                .requires(Items.APPLE).requires(Tags.Items.DUSTS_REDSTONE).requires(RootsTags.Items.AUBERGINE_CROP)
                .result(ModItems.PERESKIA, 1)
                .chanceOutput(ModItems.PERESKIA_BULB, 2, 1f)), c, RootsAPI.rl("pyre/pereskia"));

    RecipeSaver.saver().unlockedBy("has_blue_orchid", has(Items.BLUE_ORCHID))
        .save(PyreRecipe.Builder.create()
            .build(BaseRecipeData.Builder.create().requires(Items.BLUE_ORCHID).requires(Tags.Items.GLASS_BLOCKS)
                .requires(RootsTags.Items.SILVER_NUGGET).requires(Tags.Items.NUGGETS_IRON)
                .requires(RootsTags.Items.GROVE_MOSS_HERB).result(ModItems.MOONGLOW, 1)
                .chanceOutput(ModItems.MOONGLOW_SEEDS, 2, 1f)), c, RootsAPI.rl("pyre/moonglow"));

    RecipeSaver.saver().unlockedBy("has_brown_mushroom", has(Items.BROWN_MUSHROOM))
        .save(PyreRecipe.Builder.create()
            .build(BaseRecipeData.Builder.create().requires(Items.BROWN_MUSHROOM).requires(Items.RED_MUSHROOM)
                .requires(Items.ALLIUM).requires(Tags.Items.FERTILIZERS)
                .requires(RootsTags.Items.WILDROOT_HERB)
                .result(ModItems.BAFFLECAP, 2)), c, RootsAPI.rl("pyre/bafflecap"));

    RecipeSaver.saver().unlockedBy("has_glow_lichen", has(Items.GLOW_LICHEN)).save(PyreRecipe.Builder.create()
        .build(BaseRecipeData.Builder.create().requires(Items.GLOW_LICHEN).requires(Items.TUFF)
            .requires(Tags.Items.COBBLESTONES_DEEPSLATE).requires(RootsTags.Items.FLINT)
            .requires(Tags.Items.RAW_MATERIALS_IRON)
            .result(ModItems.STALICRIPE, 2)), c, RootsAPI.rl("pyre/stalicripe"));

    RecipeSaver.saver().unlockedBy("has_spider_eye", has(Items.SPIDER_EYE)).save(MortarRecipe.Builder.create().times(5)
        .build(BaseRecipeData.Builder.create().requires(Items.ROTTEN_FLESH).requires(RootsTags.Items.BAFFLECAP_HERB)
            .requires(RootsTags.Items.RUNIC_DUST).requires(RootsTags.Items.EYES).requires(ItemTags.WOOL)
            .unlocks(Unlock.spell(ModSpells.ACID_CLOUD))), c, RootsAPI.rl("spell/acid_cloud"));

    RecipeSaver.saver().unlockedBy("has_glow_berry", has(Items.GLOW_BERRIES))
        .save(MortarRecipe.Builder.create().times(5)
            .build(BaseRecipeData.Builder.create().requires(Items.GLOW_BERRIES).requires(Tags.Items.FEATHERS)
                .requires(Items.SUNFLOWER).requires(ItemTags.CANDLES).requires(RootsTags.Items.MOONGLOW_HERB)
                .unlocks(Unlock.spell(ModSpells.LIGHT_DRIFTER))), c, RootsAPI.rl("spell/light_drifter"));

    RecipeSaver.saver().unlockedBy("has_dewgonia", has(RootsTags.Items.DEWGONIA_HERB)).save(
        MortarRecipe.Builder.create().times(5)
            .build(BaseRecipeData.Builder.create().requires(RootsTags.Items.DEWGONIA_HERB)
                .requires(Tags.Items.GEMS_LAPIS).requires(Tags.Items.GLASS_BLOCKS).requires(Items.CLAY_BALL)
                .requires(Tags.Items.CROPS_SUGAR_CANE)
                .unlocks(Unlock.spell(ModSpells.AQUA_BUBBLE))), c, RootsAPI.rl("spell/aqua_bubble"));

    RecipeSaver.saver().unlockedBy("has_wildewheet", has(RootsTags.Items.WILDEWHEET_HERB)).save(
        MortarRecipe.Builder.create().times(5)
            .build(BaseRecipeData.Builder.create().requires(Items.BOWL).requires(RootsTags.Items.BOTTLES)
                .requires(Items.ROTTEN_FLESH).requires(RootsTags.Items.WILDEWHEET_HERB).requires(Tags.Items.BONES)
                .unlocks(Unlock.spell(ModSpells.DESATURATE))), c, RootsAPI.rl("spell/desaturate"));

    RecipeSaver.saver().unlockedBy("has_spiritleaf", has(RootsTags.Items.SPIRITLEAF_HERB)).save(
        MortarRecipe.Builder.create().times(5)
            .build(
                BaseRecipeData.Builder.create().requires(RootsTags.Items.SPIRITLEAF_HERB).requires(Items.IRON_BARS)
                    .requires(Items.FISHING_ROD).requires(RootsTags.Items.SPIRITLEAF_HERB)
                    .requires(RootsTags.Items.LEVERS)
                    .unlocks(Unlock.spell(ModSpells.DISARM))), c, RootsAPI.rl("spell/disarm"));

    RecipeSaver.saver().unlockedBy("has_dandelion", has(Items.DANDELION)).save(MortarRecipe.Builder.create().times(5)
        .build(BaseRecipeData.Builder.create().requires(Items.DANDELION).requires(Tags.Items.CROPS_WHEAT)
            .requires(ItemTags.SMALL_FLOWERS).requires(Tags.Items.DYES_YELLOW).requires(Tags.Items.SEEDS)
            .unlocks(Unlock.spell(ModSpells.DANDELION_WINDS))), c, RootsAPI.rl("spell/dandelion_winds"));

    RecipeSaver.saver().unlockedBy("has_torch", has(Items.TORCH)).save(MortarRecipe.Builder.create().times(5)
        .build(BaseRecipeData.Builder.create().requires(ItemTags.WOOL).requires(Items.TORCH)
            .requires(Items.JACK_O_LANTERN).requires(RootsTags.Items.COPPER_NUGGET).requires(RootsTags.Items.RUNIC_DUST)
            .unlocks(Unlock.spell(ModSpells.SYLVAN_LIGHT))), c, RootsAPI.rl("spell/sylvan_light"));
    RecipeSaver.saver().unlockedBy("has_shield", has(Items.SHIELD)).save(MortarRecipe.Builder.create().times(5)
        .build(BaseRecipeData.Builder.create().requires(ItemTags.SMALL_FLOWERS).requires(Items.SHIELD)
            .requires(Tags.Items.INGOTS_IRON).requires(Items.EGG).requires(Tags.Items.GLASS_BLOCKS)
            .unlocks(Unlock.spell(ModSpells.PETAL_SHELL))), c, RootsAPI.rl("spell/petal_shell"));
    RecipeSaver.saver().unlockedBy("has_birch_bark", has(ItemTags.BIRCH_LOGS))
        .save(MortarRecipe.Builder.create().times(5)
            .build(BaseRecipeData.Builder.create().requires(ItemTags.BIRCH_LOGS).requires(Items.REDSTONE_TORCH)
                .requires(ItemTags.BOATS).requires(Tags.Items.TOOLS_BOW).requires(Tags.Items.GUNPOWDERS)
                .unlocks(Unlock.spell(ModSpells.JAUNT))), c, RootsAPI.rl("spell/jaunt"));
    RecipeSaver.saver().unlockedBy("has_hoe", has(ItemTags.HOES)).save(MortarRecipe.Builder.create().times(5)
        .build(BaseRecipeData.Builder.create().requires(Tags.Items.SEEDS).requires(Items.COMPOSTER)
            .requires(ItemTags.HOES).requires(Items.BONE_MEAL).requires(ItemTags.SMALL_FLOWERS)
            .unlocks(Unlock.spell(ModSpells.GROWTH_INFUSION))), c, RootsAPI.rl("spell/growth_infusion"));
    RecipeSaver.saver().unlockedBy("has_milk_bucket", has(Items.MILK_BUCKET))
        .save(MortarRecipe.Builder.create().times(5)
            .build(BaseRecipeData.Builder.create().requires(RootsTags.Items.WILDEWHEET_CROP)
                .requires(RootsTags.Items.WILDEWHEET_SEEDS).requires(Tags.Items.CROPS_SUGAR_CANE)
                .requires(Tags.Items.CROPS_CARROT)
                .requires(Items.MILK_BUCKET)
                .unlocks(Unlock.spell(ModSpells.RAMPANT_GROWTH))), c, RootsAPI.rl("spell/rampant_growth"));
    RecipeSaver.saver().unlockedBy("has_bow", has(Tags.Items.TOOLS_BOW)).save(MortarRecipe.Builder.create().times(5)
        .build(BaseRecipeData.Builder.create().requires(Tags.Items.TOOLS_BOW).requires(Items.PAPER)
            .requires(Items.LADDER).requires(RootsTags.Items.CLOUD_BERRY_CROP).requires(Items.SHORT_GRASS)
            .unlocks(Unlock.spell(ModSpells.SKY_SOARER))), c, RootsAPI.rl("spell/sky_soarer"));
    RecipeSaver.saver().unlockedBy("has_redstone", has(Tags.Items.DUSTS_REDSTONE))
        .save(MortarRecipe.Builder.create().times(5)
            .build(BaseRecipeData.Builder.create().requires(Tags.Items.NUGGETS_IRON).requires(Tags.Items.DUSTS_REDSTONE)
                .requires(RootsTags.Items.WILDROOT_CROP).requires(Items.FISHING_ROD)
                .requires(RootsTags.Items.AUBERGINE_CROP)
                .unlocks(Unlock.spell(ModSpells.MAGNETISM))), c, RootsAPI.rl("spell/magnetism"));
    RecipeSaver.saver().unlockedBy("has_carrots", has(Tags.Items.CROPS_CARROT))
        .save(MortarRecipe.Builder.create().times(5)
            .build(BaseRecipeData.Builder.create().requires(Tags.Items.CROPS_CARROT).requires(Tags.Items.NUGGETS_GOLD)
                .requires(RootsTags.Items.GROVE_MOSS_CROP).requires(ItemTags.LOGS).requires(Items.TORCH)
                .unlocks(Unlock.spell(ModSpells.EXTENSION))), c, RootsAPI.rl("spell/extension"));

    RecipeSaver.saver().unlockedBy("has_bafflecap", has(RootsTags.Items.BAFFLECAP_HERB))
        .save(MortarRecipe.Builder.create().times(5)
            .build(
                BaseRecipeData.Builder.create().requires(Items.CHAIN).requires(RootsTags.Items.BAFFLECAP_HERB)
                    .requires(Items.TRIPWIRE_HOOK).requires(ItemTags.WOODEN_PRESSURE_PLATES)
                    .requires(Tags.Items.GUNPOWDERS)
                    .unlocks(Unlock.spell(ModSpells.GEAS))), c, RootsAPI.rl("spell/geas"));

    RecipeSaver.saver().unlockedBy("has_bafflecap", has(RootsTags.Items.BAFFLECAP_HERB))
        .save(MortarRecipe.Builder.create().times(5)
            .build(
                BaseRecipeData.Builder.create().requires(Items.IRON_SWORD).requires(Tags.Items.RAW_MATERIALS_COPPER)
                    .requires(RootsTags.Items.BAFFLECAP_HERB).requires(Tags.Items.GUNPOWDERS)
                    .requires(ItemTags.SMALL_FLOWERS)
                    .unlocks(Unlock.spell(ModSpells.LIFE_DRAIN))), c, RootsAPI.rl("spell/life_drain"));

    RecipeSaver.saver().unlockedBy("has_wildewheet", has(RootsTags.Items.WILDEWHEET_HERB))
        .save(MortarRecipe.Builder.create().times(5)
            .build(
                BaseRecipeData.Builder.create().requires(Items.WOODEN_HOE).requires(Items.SWEET_BERRIES)
                    .requires(ModItems.WOODEN_SHEARS.get()).requires(Tags.Items.GEMS_EMERALD)
                    .requires(RootsTags.Items.WILDEWHEET_HERB)
                    .unlocks(Unlock.spell(ModSpells.HARVEST))), c, RootsAPI.rl("spell/harvest"));

    // Nondetection
    RecipeSaver.saver().unlockedBy("has_spiritleaf", has(RootsTags.Items.SPIRITLEAF_HERB))
        .save(MortarRecipe.Builder.create().times(5)
            .build(
                BaseRecipeData.Builder.create().requires(RootsTags.Items.SPIRITLEAF_HERB).requires(Items.TORCH)
                    .requires(ItemTags.CANDLES).requires(Items.GLOW_LICHEN)
                    .requires(ModItems.GLASS_EYE.get())
                    .unlocks(Unlock.spell(ModSpells.NONDETECTION))), c, RootsAPI.rl("spell/nondetection"));

    // Time stop
    RecipeSaver.saver().unlockedBy("has_stalicripe", has(RootsTags.Items.STALICRIPE_HERB))
        .save(MortarRecipe.Builder.create().times(5)
            .build(
                BaseRecipeData.Builder.create().requires(RootsTags.Items.STALICRIPE_HERB).requires(Items.CLOCK)
                    .requires(Tags.Items.SANDS).requires(Tags.Items.GLASS_BLOCKS)
                    .requires(Items.TRIPWIRE_HOOK)
                    .unlocks(Unlock.spell(ModSpells.TEMPORAL_MORASS))), c, RootsAPI.rl("spell/temporal_morass"));

    // Shatter
    RecipeSaver.saver().unlockedBy("has_stalicripe", has(RootsTags.Items.STALICRIPE_HERB))
        .save(MortarRecipe.Builder.create().times(5)
            .build(
                BaseRecipeData.Builder.create().requires(RootsTags.Items.STALICRIPE_HERB)
                    .requires(Tags.Items.RAW_MATERIALS_IRON)
                    .requires(Items.TNT).requires(Items.PISTON)
                    .requires(Items.STONE_PICKAXE)
                    .unlocks(Unlock.spell(ModSpells.SHATTER))), c, RootsAPI.rl("spell/shatter"));

    // Saturate
    RecipeSaver.saver().unlockedBy("has_wildewheet", has(RootsTags.Items.WILDEWHEET_HERB))
        .save(MortarRecipe.Builder.create().times(5)
            .build(
                BaseRecipeData.Builder.create().requires(RootsTags.Items.WILDROOT_HERB)
                    .requires(RootsTags.Items.WILDEWHEET_HERB)
                    .requires(Items.COOKED_BEEF).requires(Items.COOKED_CHICKEN)
                    .requires(Items.BAKED_POTATO)
                    .unlocks(Unlock.spell(ModSpells.SATURATE))), c, RootsAPI.rl("spell/saturate"));

    // Sanctuary
/*    RecipeSaver.saver().unlockedBy("has_pereskia", has(RootsTags.Items.PERESKIA_HERB))
        .save(MortarRecipe.Builder.create().times(5)
            .build(
                BaseRecipeData.Builder.create().requires(RootsTags.Items.PERESKIA_HERB)
                    .requires(ItemTags.SMALL_FLOWERS).requires(Items.SHIELD)
                    .requires(Items.GLOW_BERRIES).requires(Tags.Items.GEMS_AMETHYST)
                    .unlocks(Unlock.spell(ModSpells.SANCTUARY))), c, RootsAPI.rl("spell/sanctuary"));*/

    // Rose thorns
    RecipeSaver.saver().unlockedBy("has_rose_bush", has(Items.ROSE_BUSH))
        .save(MortarRecipe.Builder.create().times(5)
            .build(
                BaseRecipeData.Builder.create().requires(Items.ROSE_BUSH)
                    .requires(Items.TRIPWIRE_HOOK).requires(Items.CHAIN)
                    .requires(RootsTags.Items.WILDROOT_HERB).requires(ItemTags.SAPLINGS)
                    .unlocks(Unlock.spell(ModSpells.ROSE_THORNS))), c, RootsAPI.rl("spell/rose_thorns"));

    // Wildfire
    RecipeSaver.saver().unlockedBy("has_inferno_bulb", has(RootsTags.Items.INFERNO_BULB_HERB))
        .save(MortarRecipe.Builder.create().times(5)
            .build(
                BaseRecipeData.Builder.create().requires(RootsTags.Items.INFERNO_BULB_HERB)
                    .requires(Items.GUNPOWDER).requires(Items.CAMPFIRE)
                    .requires(Items.MAGMA_CREAM).requires(Items.LAVA_BUCKET)
                    .unlocks(Unlock.spell(ModSpells.WILDFIRE))), c, RootsAPI.rl("spell/wildfire"));

    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Blocks.MOSS_BLOCK)
        .pattern("XX")
        .pattern("XX")
        .define('X', RootsTags.Items.GROVE_MOSS_CROP)
        .unlockedBy("has_grove_moss", has(RootsTags.Items.GROVE_MOSS_HERB))
        .save(c, RootsAPI.rl("moss_block_from_grove_moss"));

    ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.WOODEN_KNIFE.get())
        .pattern("  X")
        .pattern(" W ")
        .pattern("S  ")
        .define('S', Tags.Items.RODS_WOODEN)
        .define('W', RootsTags.Items.WILDROOT_HERB)
        .define('X', ItemTags.PLANKS)
        .unlockedBy("has_planks", has(ItemTags.PLANKS))
        .save(c, RootsAPI.rl("wooden_knife"));

    ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.IRON_KNIFE.get())
        .pattern("  X")
        .pattern(" W ")
        .pattern("S  ")
        .define('S', Tags.Items.RODS_WOODEN)
        .define('W', RootsTags.Items.WILDROOT_HERB)
        .define('X', Tags.Items.INGOTS_IRON)
        .unlockedBy("has_iron_ingot", has(Tags.Items.INGOTS_IRON))
        .save(c, RootsAPI.rl("iron_knife"));

    ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.GOLDEN_KNIFE.get())
        .pattern("  X")
        .pattern(" W ")
        .pattern("S  ")
        .define('W', RootsTags.Items.WILDROOT_HERB)
        .define('S', Tags.Items.RODS_WOODEN)
        .define('X', Tags.Items.INGOTS_GOLD)
        .unlockedBy("has_gold_ingot", has(Tags.Items.INGOTS_GOLD))
        .save(c, RootsAPI.rl("golden_knife"));

    ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.DIAMOND_KNIFE.get())
        .pattern("  X")
        .pattern(" W ")
        .pattern("S  ")
        .define('W', RootsTags.Items.WILDROOT_HERB)
        .define('S', Tags.Items.RODS_WOODEN)
        .define('X', Tags.Items.GEMS_DIAMOND)
        .unlockedBy("has_diamond", has(Tags.Items.GEMS_DIAMOND))
        .save(c, RootsAPI.rl("diamond_knife"));

    // TODO: This is in the meincraft namespace
    netheriteSmithing(c, ModItems.DIAMOND_KNIFE.get(), RecipeCategory.COMBAT, ModItems.NETHERITE_KNIFE.get());

    // Copper
    ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.COPPER_KNIFE.get())
        .pattern("  X")
        .pattern(" W ")
        .pattern("S  ")
        .define('W', RootsTags.Items.WILDROOT_HERB)
        .define('S', Tags.Items.RODS_WOODEN)
        .define('X', Tags.Items.INGOTS_COPPER)
        .unlockedBy("has_copper_ingot", has(Tags.Items.INGOTS_COPPER))
        .save(c, RootsAPI.rl("copper_knife"));

    // Silver
    ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.SILVER_KNIFE.get())
        .pattern("  X")
        .pattern(" W ")
        .pattern("S  ")
        .define('W', RootsTags.Items.WILDROOT_HERB)
        .define('S', Tags.Items.RODS_WOODEN)
        .define('X', RootsTags.Items.SILVER_INGOT)
        .unlockedBy("has_silver_ingot", has(RootsTags.Items.SILVER_INGOT))
        .save(c, RootsAPI.rl("silver_knife"));

    // Stone
    ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.STONE_KNIFE.get())
        .pattern("  X")
        .pattern(" W ")
        .pattern("S  ")
        .define('W', RootsTags.Items.WILDROOT_HERB)
        .define('S', Tags.Items.RODS_WOODEN)
        .define('X', ItemTags.STONE_TOOL_MATERIALS)
        .unlockedBy("has_stone", has(ItemTags.STONE_TOOL_MATERIALS))
        .save(c, RootsAPI.rl("stone_knife"));

    ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.BEETLE_HELMET.get())
        .pattern("CCC")
        .pattern("C C")
        .define('C', RootsTags.Items.CARAPACE)
        .unlockedBy("has_beetle_carapace", has(RootsTags.Items.CARAPACE))
        .save(c, RootsAPI.rl("beetle_helmet"));

    ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.BEETLE_CHESTPLATE.get())
        .pattern("C C")
        .pattern("CCC")
        .pattern("CCC")
        .define('C', RootsTags.Items.CARAPACE)
        .unlockedBy("has_beetle_carapace", has(RootsTags.Items.CARAPACE))
        .save(c, RootsAPI.rl("beetle_chestplate"));

    ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.BEETLE_LEGGINGS.get())
        .pattern("CCC")
        .pattern("C C")
        .pattern("C C")
        .define('C', RootsTags.Items.CARAPACE)
        .unlockedBy("has_beetle_carapace", has(RootsTags.Items.CARAPACE))
        .save(c, RootsAPI.rl("beetle_leggings"));

    ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.BEETLE_BOOTS.get())
        .pattern("C C")
        .pattern("C C")
        .define('C', RootsTags.Items.CARAPACE)
        .unlockedBy("has_beetle_carapace", has(RootsTags.Items.CARAPACE))
        .save(c, RootsAPI.rl("beetle_boots"));

    // Copper armor
    ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.COPPER_HELMET.get())
        .pattern("CCC")
        .pattern("C C")
        .define('C', Tags.Items.STORAGE_BLOCKS_COPPER)
        .unlockedBy("has_copper_ingot", has(Tags.Items.STORAGE_BLOCKS_COPPER))
        .save(c, RootsAPI.rl("copper_helmet"));

    ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.COPPER_CHESTPLATE.get())
        .pattern("C C")
        .pattern("CCC")
        .pattern("CCC")
        .define('C', Tags.Items.STORAGE_BLOCKS_COPPER)
        .unlockedBy("has_copper_ingot", has(Tags.Items.STORAGE_BLOCKS_COPPER))
        .save(c, RootsAPI.rl("copper_chestplate"));

    ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.COPPER_LEGGINGS.get())
        .pattern("CCC")
        .pattern("C C")
        .pattern("C C")
        .define('C', Tags.Items.STORAGE_BLOCKS_COPPER)
        .unlockedBy("has_copper_ingot", has(Tags.Items.STORAGE_BLOCKS_COPPER))
        .save(c, RootsAPI.rl("copper_leggings"));

    ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.COPPER_BOOTS.get())
        .pattern("C C")
        .pattern("C C")
        .define('C', Tags.Items.STORAGE_BLOCKS_COPPER)
        .unlockedBy("has_copper_ingot", has(Tags.Items.STORAGE_BLOCKS_COPPER))
        .save(c, RootsAPI.rl("copper_boots"));

    // Copper axe
    ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.COPPER_AXE.get())
        .pattern("CC")
        .pattern("CS")
        .pattern(" S")
        .define('C', Tags.Items.STORAGE_BLOCKS_COPPER)
        .define('S', Tags.Items.RODS_WOODEN)
        .unlockedBy("has_copper_ingot", has(Tags.Items.STORAGE_BLOCKS_COPPER))
        .save(c, RootsAPI.rl("copper_axe"));

    // Copper hoe
    ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.COPPER_HOE.get())
        .pattern("CC")
        .pattern(" S")
        .pattern(" S")
        .define('C', Tags.Items.STORAGE_BLOCKS_COPPER)
        .define('S', Tags.Items.RODS_WOODEN)
        .unlockedBy("has_copper_ingot", has(Tags.Items.STORAGE_BLOCKS_COPPER))
        .save(c, RootsAPI.rl("copper_hoe"));

    // Copper pickaxe
    ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.COPPER_PICKAXE.get())
        .pattern("CCC")
        .pattern(" S ")
        .pattern(" S ")
        .define('C', Tags.Items.STORAGE_BLOCKS_COPPER)
        .define('S', Tags.Items.RODS_WOODEN)
        .unlockedBy("has_copper_ingot", has(Tags.Items.STORAGE_BLOCKS_COPPER))
        .save(c, RootsAPI.rl("copper_pickaxe"));

    // Copper shovel
    ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.COPPER_SHOVEL.get())
        .pattern("C")
        .pattern("S")
        .pattern("S")
        .define('C', Tags.Items.STORAGE_BLOCKS_COPPER)
        .define('S', Tags.Items.RODS_WOODEN)
        .unlockedBy("has_copper_ingot", has(Tags.Items.STORAGE_BLOCKS_COPPER))
        .save(c, RootsAPI.rl("copper_shovel"));

    // Copper sword
    ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.COPPER_SWORD.get())
        .pattern("C")
        .pattern("C")
        .pattern("S")
        .define('C', Tags.Items.STORAGE_BLOCKS_COPPER)
        .define('S', Tags.Items.RODS_WOODEN)
        .unlockedBy("has_copper_ingot", has(Tags.Items.STORAGE_BLOCKS_COPPER))
        .save(c, RootsAPI.rl("copper_sword"));

    ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.ANTLER_HAT.get())
        .pattern("AAA")
        .pattern("A A")
        .define('A', RootsTags.Items.ANTLERS)
        .unlockedBy("has_antlers", has(RootsTags.Items.ANTLERS))
        .save(c, RootsAPI.rl("antler_helmet"));

    ShapedRecipeBuilder.shaped(RecipeCategory.TRANSPORTATION, Items.SADDLE)
        .pattern("FFF")
        .pattern("FFF")
        .pattern("F F")
        .define('F', RootsTags.Items.SYLVAN_LEATHERS)
        .unlockedBy("has_sylvan_leather", has(RootsTags.Items.SYLVAN_LEATHERS))
        .save(c, RootsAPI.rl("sylvan_leather_saddle"));

    // Herb Pouch
    ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.HERB_POUCH.get())
        .pattern("SWS")
        .pattern("WHW")
        .pattern(" W ")
        .define('W', ItemTags.WOOL)
        .define('S', Tags.Items.STRINGS)
        .define('H', RootsTags.Items.RUNESTONE_HERBS)
        .unlockedBy("has_wool", has(ItemTags.WOOL))
        .save(c, RootsAPI.rl("herb_pouch"));

    RecipeSaver.saver().unlockedBy("has_iron", has(Tags.Items.INGOTS_IRON)).save(GrovePouchRecipe.Builder.create()
        .build(BaseRecipeData.Builder.create().result(ModItems.COMPONENT_POUCH, 1).requires(Tags.Items.STRINGS)
            .requires(ModItems.HERB_POUCH.get())
            .requires(Tags.Items.STRINGS).requires(Tags.Items.INGOTS_IRON).requires(Tags.Items.INGOTS_IRON)
            .requires(ItemTags.WOOL).requires(ItemTags.WOOL).requires(Tags.Items.CHESTS).requires(Tags.Items.CHESTS)
            .condition(ModConditions.ANY_GROVE_STONE_ACTIVE.get())), c, RootsAPI.rl("grove/component_pouch"));

    RecipeSaver.saver().unlockedBy("has_gold", has(Tags.Items.INGOTS_GOLD)).save(GrovePouchRecipe.Builder.create()
        .build(BaseRecipeData.Builder.create().result(ModItems.APOTHECARY_POUCH, 1).requires(Tags.Items.STRINGS)
            .requires(ModItems.COMPONENT_POUCH.get())
            .requires(Tags.Items.STRINGS).requires(Tags.Items.INGOTS_GOLD).requires(Tags.Items.INGOTS_GOLD)
            .requires(ItemTags.WOOL).requires(ItemTags.WOOL).requires(Tags.Items.CHESTS).requires(Tags.Items.CHESTS)
            .condition(ModConditions.ANY_GROVE_STONE_ACTIVE.get())), c, RootsAPI.rl("grove/apothecary_pouch"));

    RecipeSaver.saver().unlockedBy("has_sylvan_leather", has(RootsTags.Items.SYLVAN_LEATHERS))
        .save(GrovePouchRecipe.Builder.create().build(BaseRecipeData.Builder.create().result(ModItems.SYLVAN_POUCH, 1)
            .requires(ModItems.APOTHECARY_POUCH.get())
            .requires(RootsTags.Items.SYLVAN_LEATHERS).requires(RootsTags.Items.SYLVAN_LEATHERS).
            requires(RootsTags.Items.SYLVAN_LEATHERS).requires(RootsTags.Items.SYLVAN_LEATHERS).
            requires(RootsTags.Items.SYLVAN_LEATHERS)
            .requires(RootsTags.Items.PERESKIA_HERB).requires(RootsTags.Items.PERESKIA_HERB)
            .requires(Tags.Items.INGOTS_GOLD).requires(Tags.Items.INGOTS_GOLD)
            .requires(RootsTags.Items.WILDWOOD_CHESTS).requires(RootsTags.Items.WILDWOOD_CHESTS)
            .condition(ModConditions.ANY_GROVE_STONE_ACTIVE.get())), c, RootsAPI.rl("grove/dye_pouch"));

    SpecialRecipeBuilder.special(PouchDyeRecipe::new).save(c, RootsAPI.rl("dye_pouch"));

    // Living arrows
    RecipeSaver.saver().unlockedBy("has_leaves", has(ItemTags.LEAVES)).save(GroveRecipe.Builder.create()
        .build(BaseRecipeData.Builder.create().result(ModItems.LIVING_ARROW, 2).requires(ItemTags.LEAVES)
            .requires(ItemTags.LEAVES).requires(RootsTags.Items.FLINT).requires(Tags.Items.RODS_WOODEN)
            .condition(ModConditions.ANY_GROVE_STONE_ACTIVE.get())), c, RootsAPI.rl("grove/living_arrow"));

    ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.ALERTNESS_CHARM.get(), 1)
        .pattern("HNH")
        .pattern("N N")
        .pattern(" N ")
        .define('H', RootsTags.Items.HERBS)
        .define('N', Tags.Items.NUGGETS)
        .unlockedBy("has_nugget", has(Tags.Items.NUGGETS))
        .save(c);

    ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ModItems.HOMESICKNSES_CHARM.get(), 1)
        .pattern(" B ")
        .pattern("BEB")
        .pattern(" B ")
        .define('B', Tags.Items.BRICKS)
        .define('E', Tags.Items.GEMS_EMERALD)
        .unlockedBy("has_emerald", has(Tags.Items.GEMS_EMERALD))
        .save(c);

    ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.WRITABLE_BOOK)
        .requires(Items.BOOK)
        .requires(Items.FEATHER)
        .requires(ModItems.INK_BOTTLE.get())
        .unlockedBy("has_ink_bottle", has(ModItems.INK_BOTTLE.get()))
        .save(c, RootsAPI.rl("writable_book_ink_bottle"));

    saver = RecipeSaver.saver().unlockedBy("has_fire_soil", has(RootsTags.Items.FIRE_SOIL));
    MortarRecipe.Builder fireSoilRecipe = MortarRecipe.Builder.create().times(5);
    BaseRecipeData.Builder fireSoilData = BaseRecipeData.Builder.create().requires(RootsTags.Items.FIRE_SOIL)
        .result(ModItems.INFERNO_BULB)
        .chanceOutput(ModItems.ELEMENTAL_SOIL.get(), 1);

    saver.save(fireSoilRecipe.build(fireSoilData), c, RootsAPI.rl("mortar/inferno_bulb_from_fire_soil"));
    saver.save(fireSoilRecipe.times(10)
        .build(fireSoilData.multiplty(2)), c, RootsAPI.rl("mortar/inferno_bulb_from_fire_soil_2"));
    saver.save(fireSoilRecipe.times(10)
        .build(fireSoilData.multiplty(3)), c, RootsAPI.rl("mortar/inferno_bulb_from_fire_soil_3"));
    saver.save(fireSoilRecipe.times(10)
        .build(fireSoilData.multiplty(4)), c, RootsAPI.rl("mortar/inferno_bulb_from_fire_soil_4"));
    saver.save(fireSoilRecipe.times(10)
        .build(fireSoilData.multiplty(5)), c, RootsAPI.rl("mortar/inferno_bulb_from_fire_soil_5"));

    saver = RecipeSaver.saver().unlockedBy("has_water_soil", has(RootsTags.Items.WATER_SOIL));
    MortarRecipe.Builder waterSoilRecipe = MortarRecipe.Builder.create().times(5);
    BaseRecipeData.Builder waterSoilData = BaseRecipeData.Builder.create().requires(RootsTags.Items.WATER_SOIL)
        .result(ModItems.DEWGONIA)
        .chanceOutput(ModItems.ELEMENTAL_SOIL, 1);

    saver.save(waterSoilRecipe.build(waterSoilData), c, RootsAPI.rl("mortar/dewgonia_from_water_soil"));
    saver.save(waterSoilRecipe.times(10)
        .build(waterSoilData.multiplty(2)), c, RootsAPI.rl("mortar/dewgonia_from_water_soil_2"));
    saver.save(waterSoilRecipe.times(10)
        .build(waterSoilData.multiplty(3)), c, RootsAPI.rl("mortar/dewgonia_from_water_soil_3"));
    saver.save(waterSoilRecipe.times(10)
        .build(waterSoilData.multiplty(4)), c, RootsAPI.rl("mortar/dewgonia_from_water_soil_4"));
    saver.save(waterSoilRecipe.times(10)
        .build(waterSoilData.multiplty(5)), c, RootsAPI.rl("mortar/dewgonia_from_water_soil_5"));

    saver = RecipeSaver.saver().unlockedBy("has_earth_soil", has(RootsTags.Items.EARTH_SOIL));
    MortarRecipe.Builder earthSoilRecipe = MortarRecipe.Builder.create().times(5);
    BaseRecipeData.Builder earthSoilData = BaseRecipeData.Builder.create().requires(RootsTags.Items.EARTH_SOIL)
        .result(ModItems.STALICRIPE)
        .chanceOutput(ModItems.ELEMENTAL_SOIL, 1);

    saver.save(earthSoilRecipe.build(earthSoilData), c, RootsAPI.rl("mortar/stalicripe_from_earth_soil"));
    saver.save(earthSoilRecipe.times(10)
        .build(earthSoilData.multiplty(2)), c, RootsAPI.rl("mortar/stalicripe_from_earth_soil_2"));
    saver.save(earthSoilRecipe.times(10)
        .build(earthSoilData.multiplty(3)), c, RootsAPI.rl("mortar/stalicripe_from_earth_soil_3"));
    saver.save(earthSoilRecipe.times(10)
        .build(earthSoilData.multiplty(4)), c, RootsAPI.rl("mortar/stalicripe_from_earth_soil_4"));
    saver.save(earthSoilRecipe.times(10)
        .build(earthSoilData.multiplty(5)), c, RootsAPI.rl("mortar/stalicripe_from_earth_soil_5"));

    saver = RecipeSaver.saver().unlockedBy("has_air_soil", has(RootsTags.Items.AIR_SOIL));
    MortarRecipe.Builder airSoilRecipe = MortarRecipe.Builder.create().times(5);
    BaseRecipeData.Builder airSoilData = BaseRecipeData.Builder.create().requires(RootsTags.Items.AIR_SOIL)
        .result(ModItems.CLOUD_BERRY)
        .chanceOutput(ModItems.ELEMENTAL_SOIL, 1);

    saver.save(airSoilRecipe.build(airSoilData), c, RootsAPI.rl("mortar/cloud_berry_from_air_soil"));
    saver.save(airSoilRecipe.times(10)
        .build(airSoilData.multiplty(2)), c, RootsAPI.rl("mortar/cloud_berry_from_air_soil_2"));
    saver.save(airSoilRecipe.times(10)
        .build(airSoilData.multiplty(3)), c, RootsAPI.rl("mortar/cloud_berry_from_air_soil_3"));
    saver.save(airSoilRecipe.times(10)
        .build(airSoilData.multiplty(4)), c, RootsAPI.rl("mortar/cloud_berry_from_air_soil_4"));
    saver.save(airSoilRecipe.times(10)
        .build(airSoilData.multiplty(5)), c, RootsAPI.rl("mortar/cloud_berry_from_air_soil_5"));

    MortarRecipe.Builder blazePowder1 = MortarRecipe.Builder.create().times(10);
    BaseRecipeData.Builder blazePowderData = BaseRecipeData.Builder.create().requires(Tags.Items.RODS_BLAZE)
        .result(Items.BLAZE_POWDER.builtInRegistryHolder(), 2).chanceOutput(Items.BLAZE_POWDER, 0.5f);
    saver = RecipeSaver.saver().unlockedBy("has_blaze_rod", has(Tags.Items.RODS_BLAZE));

    saver.save(blazePowder1.build(blazePowderData), c, RootsAPI.rl("mortar/blaze_powder_from_blaze_rod"));
    saver.save(blazePowder1.times(10)
        .build(blazePowderData.multiplty(2)), c, RootsAPI.rl("mortar/blaze_powder_from_blaze_rod_2"));
    saver.save(blazePowder1.times(10)
        .build(blazePowderData.multiplty(3)), c, RootsAPI.rl("mortar/blaze_powder_from_blaze_rod_3"));
    saver.save(blazePowder1.times(10)
        .build(blazePowderData.multiplty(4)), c, RootsAPI.rl("mortar/blaze_powder_from_blaze_rod_4"));
    saver.save(blazePowder1.times(10)
        .build(blazePowderData.multiplty(5)), c, RootsAPI.rl("mortar/blaze_powder_from_blaze_rod_5"));

    blazePowderData = BaseRecipeData.Builder.create().requires(Items.MAGMA_CREAM).result(Items.SLIME_BALL, 1)
        .chanceOutput(Items.BLAZE_POWDER, 1f);
    saver = RecipeSaver.saver().unlockedBy("has_magma_cream", has(Items.MAGMA_CREAM));
    saver.save(MortarRecipe.Builder.create().times(10)
        .build(blazePowderData), c, RootsAPI.rl("mortar/blaze_powder_from_magma_cream"));
    saver.save(MortarRecipe.Builder.create().times(10)
        .build(blazePowderData.multiplty(2)), c, RootsAPI.rl("mortar/blaze_powder_from_magma_cream_2"));
    saver.save(MortarRecipe.Builder.create().times(10)
        .build(blazePowderData.multiplty(3)), c, RootsAPI.rl("mortar/blaze_powder_from_magma_cream_3"));
    saver.save(MortarRecipe.Builder.create().times(10)
        .build(blazePowderData.multiplty(4)), c, RootsAPI.rl("mortar/blaze_powder_from_magma_cream_4"));
    saver.save(MortarRecipe.Builder.create().times(10)
        .build(blazePowderData.multiplty(5)), c, RootsAPI.rl("mortar/blaze_powder_from_magma_cream_5"));

    // Grove Stones
    RecipeSaver.saver().unlockedBy("has_grove_stone", has(RootsTags.Items.GROVE_STONE_WILD))
        .save(GroveRecipe.Builder.create()
            .build(BaseRecipeData.Builder.create()
                .result(ModItems.SPROUTING_GROVE_STONE, 1)
                .requires(RootsTags.Items.GROVE_STONE_WILD)
                .requires(RootsTags.Items.WILDEWHEET_HERB)
                .requires(RootsTags.Items.WILDEWHEET_HERB)
                .requires(RootsTags.Items.WILDEWHEET_HERB)
                .requires(RootsTags.Items.WILDEWHEET_HERB)
                .requires(Tags.Items.SEEDS)
                .requires(Tags.Items.SEEDS)
                .requires(Tags.Items.SEEDS)
                .requires(Tags.Items.BUCKETS)
                .requires(Items.GOLDEN_HOE)
                .condition(ModConditions.ANY_GROVE_STONE_ACTIVE.get())
                .condition(ModConditions.SPROUTING_RANK_1.get())), c, RootsAPI.rl("grove/grove_stone_sprouting"));

    // Fungal
    RecipeSaver.saver().unlockedBy("has_grove_stone", has(RootsTags.Items.GROVE_STONE_WILD))
        .save(GroveRecipe.Builder.create()
            .build(BaseRecipeData.Builder.create()
                .result(ModItems.FUNGAL_GROVE_STONE, 1)
                .requires(RootsTags.Items.GROVE_STONE_WILD)
                .requires(RootsTags.Items.BAFFLECAP_HERB)
                .requires(RootsTags.Items.BAFFLECAP_HERB)
                .requires(RootsTags.Items.BAFFLECAP_HERB)
                .requires(RootsTags.Items.BAFFLECAP_HERB)
                .requires(Items.BROWN_MUSHROOM)
                .requires(Items.RED_MUSHROOM)
                .requires(Items.CRIMSON_FUNGUS)
                .requires(Items.WARPED_FUNGUS)
                .requires(Items.GOLDEN_SHOVEL)
                .condition(ModConditions.ANY_GROVE_STONE_ACTIVE.get())
                .condition(ModConditions.FUNGAL_RANK_1.get())
            ), c, RootsAPI.rl("grove/grove_stone_fungal"));

    RecipeSaver.saver().unlockedBy("has_grove_stone", has(RootsTags.Items.GROVE_STONE_WILD))
        .save(GroveRecipe.Builder.create()
            .build(BaseRecipeData.Builder.create()
                .result(ModItems.TWILIGHT_GROVE_STONE, 1)
                .requires(RootsTags.Items.GROVE_STONE_WILD)
                .requires(RootsTags.Items.MOONGLOW_HERB)
                .requires(RootsTags.Items.MOONGLOW_HERB)
                .requires(RootsTags.Items.MOONGLOW_HERB)
                .requires(RootsTags.Items.MOONGLOW_HERB)
                .requires(RootsTags.Items.MOONGLOW_HERB)
                .requires(Tags.Items.GEMS_QUARTZ)
                .requires(Tags.Items.FEATHERS)
                .requires(RootsTags.Items.SILVER_INGOT)
                .requires(Items.GOLDEN_SWORD)
                .condition(ModConditions.ANY_GROVE_STONE_ACTIVE.get())
                .condition(ModConditions.TWILIGHT_RANK_1.get())
            ), c, RootsAPI.rl("grove/grove_stone_twilight"));


    RecipeSaver.saver().unlockedBy("has_grove_stone", has(RootsTags.Items.GROVE_STONE_WILD))
        .save(GroveRecipe.Builder.create()
            .build(BaseRecipeData.Builder.create()
                .result(ModItems.ELEMENTAL_GROVE_STONE, 1)
                .requires(RootsTags.Items.GROVE_STONE_WILD)
                .requires(RootsTags.Items.CLOUD_BERRY_HERB)
                .requires(RootsTags.Items.CLOUD_BERRY_HERB)
                .requires(RootsTags.Items.INFERNO_BULB_HERB)
                .requires(RootsTags.Items.INFERNO_BULB_HERB)
                .requires(RootsTags.Items.DEWGONIA_HERB)
                .requires(RootsTags.Items.DEWGONIA_HERB)
                .requires(RootsTags.Items.STALICRIPE_HERB)
                .requires(RootsTags.Items.STALICRIPE_HERB)
                .requires(Items.GOLDEN_PICKAXE)
                .condition(ModConditions.ANY_GROVE_STONE_ACTIVE.get())
                .condition(ModConditions.ELEMENTAL_RANK_1.get())
            ), c, RootsAPI.rl("grove/grove_stone_elemental"));

    RecipeSaver.saver().unlockedBy("has_grove_stone", has(RootsTags.Items.GROVE_STONE_WILD))
        .save(GroveRecipe.Builder.create()
            .build(BaseRecipeData.Builder.create()
                .result(ModItems.FAIRY_GROVE_STONE, 1)
                .requires(RootsTags.Items.GROVE_STONE_WILD)
                .requires(RootsTags.Items.PERESKIA_HERB)
                .requires(RootsTags.Items.PERESKIA_HERB)
                .requires(RootsTags.Items.PERESKIA_HERB)
                .requires(RootsTags.Items.PERESKIA_HERB)
                .requires(ItemTags.BOOKSHELF_BOOKS)
                .requires(Items.COMPASS)
                .requires(Tags.Items.GEMS_EMERALD)
                .requires(Tags.Items.GEMS_EMERALD)
                .requires(ModItems.GOLDEN_KNIFE.get())
                .condition(ModConditions.ANY_GROVE_STONE_ACTIVE.get())
                .condition(ModConditions.FAIRY_RANK_1.get())
            ), c, RootsAPI.rl("grove/grove_stone_fairy"));

    RecipeSaver.saver().unlockedBy("has_grove_stone", has(RootsTags.Items.GROVE_STONE_WILD))
        .save(GroveRecipe.Builder.create()
            .build(BaseRecipeData.Builder.create()
                .result(ModItems.PRIMAL_GROVE_STONE, 1)
                .requires(RootsTags.Items.GROVE_STONE_WILD)
                .requires(RootsTags.Items.SPIRITLEAF_HERB)
                .requires(RootsTags.Items.SPIRITLEAF_HERB)
                .requires(RootsTags.Items.SPIRITLEAF_HERB)
                .requires(RootsTags.Items.SPIRITLEAF_HERB)
                .requires(Tags.Items.CROPS_WHEAT)
                .requires(Tags.Items.CROPS)
                .requires(Tags.Items.CROPS)
                .requires(Tags.Items.CROPS)
                .requires(Items.GOLDEN_AXE)
                .condition(ModConditions.ANY_GROVE_STONE_ACTIVE.get())
                .condition(ModConditions.WILD_RANK_1.get())
            ), c, RootsAPI.rl("grove/grove_stone_wild"));

    RecipeSaver.saver().unlockedBy("has_red_mushroom", has(Items.RED_MUSHROOM))
        .save(GroveRecipe.Builder.create()
            .build(BaseRecipeData.Builder.create()
                .result(ModItems.RED_FAIRY_HUT.get(), 1)
                .condition(ModConditions.FAIRY_GROVE_STONE_ACTIVE.get())
                .condition(ModConditions.FAIRY_RANK_1.get())
                .requires(Items.RED_MUSHROOM)
                .requires(Items.RED_MUSHROOM)
                .requires(Items.RED_MUSHROOM)
                .requires(Items.RED_MUSHROOM)
                .requires(Items.EXPERIENCE_BOTTLE)
                .requires(Tags.Items.GEMS_EMERALD)
                .requires(ItemTags.WOODEN_DOORS)
                .requires(Tags.Items.CHESTS_WOODEN)
                .requires(Tags.Items.COBBLESTONES_MOSSY)
                .requires(Tags.Items.FERTILIZERS)
            ), c, RootsAPI.rl("grove/red_fairy_hut"));

    RecipeSaver.saver().unlockedBy("has_brown_mushroom", has(Items.BROWN_MUSHROOM))
        .save(GroveRecipe.Builder.create()
            .build(BaseRecipeData.Builder.create()
                .result(ModItems.BROWN_FAIRY_HUT.get(), 1)
                .condition(ModConditions.FAIRY_GROVE_STONE_ACTIVE.get())
                .condition(ModConditions.FAIRY_RANK_1.get())
                .requires(Items.BROWN_MUSHROOM)
                .requires(Items.BROWN_MUSHROOM)
                .requires(Items.BROWN_MUSHROOM)
                .requires(Items.BROWN_MUSHROOM)
                .requires(Items.EXPERIENCE_BOTTLE)
                .requires(Tags.Items.GEMS_EMERALD)
                .requires(ItemTags.WOODEN_DOORS)
                .requires(Tags.Items.CHESTS_WOODEN)
                .requires(Tags.Items.COBBLESTONES_MOSSY)
                .requires(Tags.Items.FERTILIZERS)
            ), c, RootsAPI.rl("grove/brown_fairy_hut"));

    RecipeSaver.saver().unlockedBy("has_bafflecap", has(RootsTags.Items.BAFFLECAP_HERB))
        .save(GroveRecipe.Builder.create()
            .build(BaseRecipeData.Builder.create()
                .result(ModItems.BAFFLECAP_FAIRY_HUT.get(), 1)
                .condition(ModConditions.FAIRY_GROVE_STONE_ACTIVE.get())
                .condition(ModConditions.FAIRY_RANK_1.get())
                .requires(RootsTags.Items.BAFFLECAP_HERB)
                .requires(RootsTags.Items.BAFFLECAP_HERB)
                .requires(RootsTags.Items.BAFFLECAP_HERB)
                .requires(RootsTags.Items.BAFFLECAP_HERB)
                .requires(Items.EXPERIENCE_BOTTLE)
                .requires(Tags.Items.GEMS_EMERALD)
                .requires(ItemTags.WOODEN_DOORS)
                .requires(Tags.Items.CHESTS_WOODEN)
                .requires(Tags.Items.COBBLESTONES_MOSSY)
                .requires(Tags.Items.FERTILIZERS)
            ), c, RootsAPI.rl("grove/bafflecap_fairy_hut"));

    RecipeSaver.saver().unlockedBy("has_crimson", has(Items.CRIMSON_FUNGUS))
        .save(GroveRecipe.Builder.create()
            .build(BaseRecipeData.Builder.create()
                .result(ModItems.CRIMSON_FAIRY_HUT.get(), 1)
                .condition(ModConditions.FAIRY_GROVE_STONE_ACTIVE.get())
                .condition(ModConditions.FAIRY_RANK_1.get())
                .requires(Items.CRIMSON_FUNGUS)
                .requires(Items.CRIMSON_FUNGUS)
                .requires(Items.CRIMSON_FUNGUS)
                .requires(Items.CRIMSON_FUNGUS)
                .requires(Items.EXPERIENCE_BOTTLE)
                .requires(Tags.Items.INGOTS_GOLD)
                .requires(RootsTags.Items.NETHER_DOORS)
                .requires(Tags.Items.CHESTS_WOODEN)
                .requires(Tags.Items.NETHERRACKS)
                .requires(Tags.Items.FERTILIZERS)
            ), c, RootsAPI.rl("grove/crimson_fairy_hut"));

    RecipeSaver.saver().unlockedBy("has_warped", has(Items.WARPED_FUNGUS))
        .save(GroveRecipe.Builder.create()
            .build(BaseRecipeData.Builder.create()
                .result(ModItems.WARPED_FAIRY_HUT.get(), 1)
                .condition(ModConditions.FAIRY_GROVE_STONE_ACTIVE.get())
                .condition(ModConditions.FAIRY_RANK_1.get())
                .requires(Items.WARPED_FUNGUS)
                .requires(Items.WARPED_FUNGUS)
                .requires(Items.WARPED_FUNGUS)
                .requires(Items.WARPED_FUNGUS)
                .requires(Items.EXPERIENCE_BOTTLE)
                .requires(Tags.Items.INGOTS_GOLD)
                .requires(RootsTags.Items.NETHER_DOORS)
                .requires(Tags.Items.CHESTS_WOODEN)
                .requires(Tags.Items.NETHERRACKS)
                .requires(Tags.Items.FERTILIZERS)
            ), c, RootsAPI.rl("grove/warped_fairy_hut"));

    // TODO: Fungal transmuter recipe

    RecipeSaver.saver().unlockedBy("has_dewgonia", has(RootsTags.Items.DEWGONIA_HERB))
        .save(SummonCreaturesRecipe.Builder.create().entity(ModEntities.SNOW_SPROUT.get())
            .build(BaseRecipeData.Builder.create().requires(RootsTags.Items.WILDROOT_SEEDS)
                .requires(RootsTags.Items.DEWGONIA_HERB)
                .requires(Items.SNOWBALL)), c, RootsAPI.rl("summon/snow_sprout"));

    RecipeSaver.saver().unlockedBy("has_moonglow", has(RootsTags.Items.MOONGLOW_HERB))
        .save(SummonCreaturesRecipe.Builder.create().entity(ModEntities.MELODY_SPROUT.get())
            .build(BaseRecipeData.Builder.create().requires(RootsTags.Items.AUBERGINE_SEEDS)
                .requires(RootsTags.Items.MOONGLOW_HERB)
                .requires(Tags.Items.END_STONES)), c, RootsAPI.rl("summon/melody_sprout"));

    RecipeSaver.saver().unlockedBy("has_aubergine", has(RootsTags.Items.AUBERGINE_CROP))
        .save(SummonCreaturesRecipe.Builder.create().entity(ModEntities.PURPLE_SPROUT.get())
            .build(BaseRecipeData.Builder.create().requires(RootsTags.Items.WILDROOT_SEEDS)
                .requires(RootsTags.Items.AUBERGINE_CROP)
                .requires(Tags.Items.DYES_PURPLE)), c, RootsAPI.rl("summon/purple_sprout"));

    RecipeSaver.saver().unlockedBy("has_aubergine", has(RootsTags.Items.AUBERGINE_CROP))
        .save(SummonCreaturesRecipe.Builder.create().entity(ModEntities.TAN_SPROUT.get())
            .build(BaseRecipeData.Builder.create().requires(RootsTags.Items.WILDROOT_SEEDS)
                .requires(RootsTags.Items.AUBERGINE_CROP)
                .requires(Tags.Items.DYES_YELLOW)), c, RootsAPI.rl("summon/tan_sprout"));

    RecipeSaver.saver().unlockedBy("has_aubergine", has(RootsTags.Items.AUBERGINE_CROP))
        .save(SummonCreaturesRecipe.Builder.create().entity(ModEntities.GREEN_SPROUT.get())
            .build(BaseRecipeData.Builder.create().requires(RootsTags.Items.WILDROOT_SEEDS)
                .requires(RootsTags.Items.AUBERGINE_CROP)
                .requires(Tags.Items.DYES_GREEN)), c, RootsAPI.rl("summon/green_sprout"));

    RecipeSaver.saver().unlockedBy("has_aubergine", has(RootsTags.Items.AUBERGINE_CROP))
        .save(SummonCreaturesRecipe.Builder.create().entity(ModEntities.RED_SPROUT.get())
            .build(BaseRecipeData.Builder.create().requires(RootsTags.Items.WILDROOT_SEEDS)
                .requires(RootsTags.Items.AUBERGINE_CROP)
                .requires(Tags.Items.DYES_RED)), c, RootsAPI.rl("summon/red_sprout"));


    RecipeSaver.saver().unlockedBy("has_cloud_berry", has(RootsTags.Items.CLOUD_BERRY_HERB))
        .save(SummonCreaturesRecipe.Builder.create().entity(ModEntities.OWL.get())
            .build(BaseRecipeData.Builder.create().requires(ItemTags.LEAVES)
                .requires(RootsTags.Items.CLOUD_BERRY_HERB)
                .requires(Items.CHICKEN)), c, RootsAPI.rl("summon/owl"));

    RecipeSaver.saver().unlockedBy("has_stalicripe", has(RootsTags.Items.STALICRIPE_HERB))
        .save(SummonCreaturesRecipe.Builder.create().entity(ModEntities.FENNEC.get())
            .build(BaseRecipeData.Builder.create().requires(ItemTags.SAND)
                .requires(RootsTags.Items.STALICRIPE_HERB)
                .requires(Tags.Items.FOODS_BERRY)), c, RootsAPI.rl("summon/fennec"));

    RecipeSaver.saver().unlockedBy("has_dewgonia", has(RootsTags.Items.DEWGONIA_HERB))
        .save(SummonCreaturesRecipe.Builder.create().entity(ModEntities.DUCK.get())
            .build(BaseRecipeData.Builder.create().requires(ItemTags.SAND)
                .requires(RootsTags.Items.DEWGONIA_HERB)
                .requires(Tags.Items.SEEDS_WHEAT)), c, RootsAPI.rl("summon/duck"));

    RecipeSaver.saver().unlockedBy("has_leaves", has(ItemTags.LEAVES))
        .save(SummonCreaturesRecipe.Builder.create().entity(ModEntities.DEER.get())
            .build(BaseRecipeData.Builder.create().requires(ItemTags.LEAVES)
                .requires(ItemTags.SAPLINGS)
                .requires(Items.SPRUCE_SAPLING)), c, RootsAPI.rl("summon/deer"));

    RecipeSaver.saver().unlockedBy("has_cactus", has(Tags.Items.CROPS_CACTUS))
        .save(SummonCreaturesRecipe.Builder.create().entity(ModEntities.JERBOA.get())
            .build(BaseRecipeData.Builder.create().requires(Tags.Items.CROPS_CACTUS)
                .requires(Items.DEAD_BUSH)
                .requires(Tags.Items.CROPS_CACTUS)), c, RootsAPI.rl("summon/cactus_bunny"));

    RecipeSaver.saver().unlockedBy("has_melon_seeds", has(Tags.Items.SEEDS_MELON))
        .save(SummonCreaturesRecipe.Builder.create().entity(ModEntities.BEETLE.get())
            .build(BaseRecipeData.Builder.create().requires(Tags.Items.SEEDS_MELON)
                .requires(RootsTags.Items.SHORT_GRASS)
                .requires(Tags.Items.DYES_BLUE)), c, RootsAPI.rl("summon/beetle"));

    RecipeSaver.saver().unlockedBy("has_tropical_fish", has(Items.TROPICAL_FISH))
        .save(SummonCreaturesRecipe.Builder.create().entity(EntityType.AXOLOTL)
            .build(BaseRecipeData.Builder.create().requires(Items.TROPICAL_FISH)
                .requires(Items.TROPICAL_FISH)
                .requires(Items.SEAGRASS)), c, RootsAPI.rl("summon/axolotl"));

    RecipeSaver.saver().unlockedBy("has_black_dye", has(Tags.Items.DYES_BLACK))
        .save(SummonCreaturesRecipe.Builder.create().entity(EntityType.BAT)
            .build(BaseRecipeData.Builder.create().requires(Tags.Items.DYES_BLACK)
                .requires(Items.TORCH)
                .requires(Tags.Items.STONES)), c, RootsAPI.rl("summon/bat"));

    RecipeSaver.saver().unlockedBy("has_yellow_dye", has(Tags.Items.DYES_YELLOW))
        .save(SummonCreaturesRecipe.Builder.create().entity(EntityType.BEE)
            .build(BaseRecipeData.Builder.create().requires(Tags.Items.DYES_YELLOW)
                .requires(Items.DANDELION)
                .requires(Items.CORNFLOWER)), c, RootsAPI.rl("summon/bee"));

    RecipeSaver.saver().unlockedBy("has_cactus", has(Tags.Items.CROPS_CACTUS))
        .save(SummonCreaturesRecipe.Builder.create().entity(EntityType.CAMEL)
            .build(BaseRecipeData.Builder.create().requires(Tags.Items.CROPS_CACTUS)
                .requires(Tags.Items.CROPS_CACTUS)
                .requires(Items.SAND)), c, RootsAPI.rl("summon/camel"));

    RecipeSaver.saver().unlockedBy("has_string", has(Tags.Items.STRINGS))
        .save(SummonCreaturesRecipe.Builder.create().entity(EntityType.CAT)
            .build(BaseRecipeData.Builder.create().requires(Tags.Items.STRINGS)
                .requires(Items.COD)
                .requires(Items.SALMON)), c, RootsAPI.rl("summon/cat"));

    RecipeSaver.saver().unlockedBy("has_seeds", has(Tags.Items.SEEDS))
        .save(SummonCreaturesRecipe.Builder.create().entity(EntityType.CHICKEN)
            .build(BaseRecipeData.Builder.create().requires(Tags.Items.SEEDS)
                .requires(RootsTags.Items.SHORT_GRASS)
                .requires(RootsTags.Items.SHORT_GRASS)), c, RootsAPI.rl("summon/chicken"));

    RecipeSaver.saver().unlockedBy("has_carpet", has(ItemTags.WOOL_CARPETS))
        .save(SummonCreaturesRecipe.Builder.create().entity(EntityType.LLAMA)
            .build(BaseRecipeData.Builder.create().requires(ItemTags.WOOL_CARPETS)
                .requires(Items.HAY_BLOCK)
                .requires(RootsTags.Items.SHORT_GRASS)), c, RootsAPI.rl("summon/llama"));

    RecipeSaver.saver().unlockedBy("has_mushroom", has(Tags.Items.MUSHROOMS))
        .save(SummonCreaturesRecipe.Builder.create().entity(EntityType.MOOSHROOM)
            .build(BaseRecipeData.Builder.create().requires(Tags.Items.MUSHROOMS)
                .requires(Tags.Items.CROPS_WHEAT)
                .requires(Items.BOWL)), c, RootsAPI.rl("summon/mooshroom"));

    RecipeSaver.saver().unlockedBy("has_iron_nugget", has(Tags.Items.NUGGETS_IRON))
        .save(SummonCreaturesRecipe.Builder.create().entity(EntityType.HORSE)
            .build(BaseRecipeData.Builder.create().requires(Tags.Items.NUGGETS_IRON)
                .requires(Tags.Items.CROPS_CARROT)
                .requires(Tags.Items.CROPS_CARROT)), c, RootsAPI.rl("summon/horse"));

    RecipeSaver.saver().unlockedBy("has_spider_eye", has(Items.SPIDER_EYE))
        .save(SummonCreaturesRecipe.Builder.create().entity(EntityType.ARMADILLO)
            .build(BaseRecipeData.Builder.create().requires(Items.SPIDER_EYE)
                .requires(Tags.Items.FEATHERS)
                .requires(RootsTags.Items.SHORT_GRASS)), c, RootsAPI.rl("summon/armadillo"));

    RecipeSaver.saver().unlockedBy("has_wheat", has(Tags.Items.CROPS_WHEAT))
        .save(SummonCreaturesRecipe.Builder.create().entity(EntityType.GOAT)
            .build(BaseRecipeData.Builder.create().requires(Tags.Items.CROPS_WHEAT)
                .requires(Tags.Items.STONES)
                .requires(Tags.Items.STONES)), c, RootsAPI.rl("summon/goat"));

    RecipeSaver.saver().unlockedBy("has_glow_lichen", has(Items.GLOW_LICHEN))
        .save(SummonCreaturesRecipe.Builder.create().entity(EntityType.GLOW_SQUID)
            .build(BaseRecipeData.Builder.create().requires(Items.GLOW_LICHEN)
                .requires(Items.GLOW_LICHEN)
                .requires(Tags.Items.DYES_BLACK)), c, RootsAPI.rl("summon/glow_squid"));

    RecipeSaver.saver().unlockedBy("has_stalicripe", has(RootsTags.Items.STALICRIPE_HERB))
        .save(SummonCreaturesRecipe.Builder.create().entity(EntityType.FROG)
            .build(BaseRecipeData.Builder.create().requires(RootsTags.Items.STALICRIPE_HERB)
                .requires(Items.LILY_PAD)
                .requires(RootsTags.Items.GROVE_MOSS_HERB)), c, RootsAPI.rl("summon/frog"));

    RecipeSaver.saver().unlockedBy("has_silver_nugget", has(RootsTags.Items.SILVER_NUGGET))
        .save(SummonCreaturesRecipe.Builder.create().entity(EntityType.DONKEY)
            .build(BaseRecipeData.Builder.create().requires(RootsTags.Items.SILVER_NUGGET)
                .requires(RootsTags.Items.SHORT_GRASS)
                .requires(Tags.Items.CROPS_CARROT)), c, RootsAPI.rl("summon/donkey"));

    RecipeSaver.saver().unlockedBy("has_iron_bars", has(Items.IRON_BARS))
        .save(SummonCreaturesRecipe.Builder.create().entity(EntityType.POLAR_BEAR)
            .build(BaseRecipeData.Builder.create().requires(Items.IRON_BARS)
                .requires(Items.SNOWBALL)
                .requires(Items.SNOWBALL)), c, RootsAPI.rl("summon/polar_bear"));

    RecipeSaver.saver().unlockedBy("has_beetroot", has(Tags.Items.CROPS_BEETROOT))
        .save(SummonCreaturesRecipe.Builder.create().entity(EntityType.PIG)
            .build(BaseRecipeData.Builder.create().requires(Tags.Items.CROPS_BEETROOT)
                .requires(Tags.Items.CROPS_POTATO)
                .requires(Tags.Items.CROPS_CARROT)), c, RootsAPI.rl("summon/pig"));

    RecipeSaver.saver().unlockedBy("has_beetroot_seeds", has(Tags.Items.SEEDS_BEETROOT))
        .save(SummonCreaturesRecipe.Builder.create().entity(EntityType.PARROT)
            .build(BaseRecipeData.Builder.create().requires(Tags.Items.SEEDS_BEETROOT)
                .requires(Items.VINE)
                .requires(Items.JUNGLE_SAPLING)), c, RootsAPI.rl("summon/parrot"));

    RecipeSaver.saver().unlockedBy("has_bamboo", has(Items.BAMBOO))
        .save(SummonCreaturesRecipe.Builder.create().entity(EntityType.PANDA)
            .build(BaseRecipeData.Builder.create().requires(Items.BAMBOO)
                .requires(Items.JUNGLE_SAPLING)
                .requires(ItemTags.DIRT)), c, RootsAPI.rl("summon/panda"));

    RecipeSaver.saver().unlockedBy("has_raw_salmon", has(Items.SALMON))
        .save(SummonCreaturesRecipe.Builder.create().entity(EntityType.OCELOT)
            .build(BaseRecipeData.Builder.create().requires(Items.SALMON)
                .requires(Items.COD)
                .requires(Tags.Items.SANDS)), c, RootsAPI.rl("summon/ocelot"));

    RecipeSaver.saver().unlockedBy("has_sea_pickle", has(Items.SEA_PICKLE))
        .save(SummonCreaturesRecipe.Builder.create().entity(EntityType.DOLPHIN)
            .build(BaseRecipeData.Builder.create().requires(Items.SEA_PICKLE)
                .requires(Items.KELP)
                .requires(Items.SEAGRASS)), c, RootsAPI.rl("summon/dolphin"));

    RecipeSaver.saver().unlockedBy("has_copper_nugget", has(RootsTags.Items.COPPER_NUGGET))
        .save(SummonCreaturesRecipe.Builder.create().entity(EntityType.MULE)
            .build(BaseRecipeData.Builder.create().requires(RootsTags.Items.COPPER_NUGGET)
                .requires(Tags.Items.CROPS_CARROT)
                .requires(RootsTags.Items.SHORT_GRASS)), c, RootsAPI.rl("summon/mule"));

    RecipeSaver.saver().unlockedBy("has_dandelion", has(Items.DANDELION))
        .save(SummonCreaturesRecipe.Builder.create().entity(EntityType.RABBIT)
            .build(BaseRecipeData.Builder.create().requires(Items.DANDELION)
                .requires(Tags.Items.CROPS_CARROT)
                .requires(Tags.Items.SANDS)), c, RootsAPI.rl("summon/rabbit"));

    RecipeSaver.saver().unlockedBy("has_netherrack", has(Tags.Items.NETHERRACKS))
        .save(SummonCreaturesRecipe.Builder.create().entity(EntityType.STRIDER)
            .build(BaseRecipeData.Builder.create().requires(Tags.Items.NETHERRACKS)
                .requires(Items.MAGMA_CREAM)
                .requires(Tags.Items.NETHERRACKS)), c, RootsAPI.rl("summon/strider"));

    RecipeSaver.saver().unlockedBy("has_wheat", has(Tags.Items.CROPS_WHEAT))
        .save(SummonCreaturesRecipe.Builder.create().entity(EntityType.COW)
            .build(BaseRecipeData.Builder.create().requires(Tags.Items.CROPS_WHEAT)
                .requires(Tags.Items.CROPS_WHEAT)
                .requires(Tags.Items.CROPS_WHEAT)), c, RootsAPI.rl("summon/cow"));

    RecipeSaver.saver().unlockedBy("has_wool", has(ItemTags.WOOL))
        .save(SummonCreaturesRecipe.Builder.create().entity(EntityType.SHEEP)
            .build(BaseRecipeData.Builder.create().requires(ItemTags.WOOL)
                .requires(ItemTags.WOOL)
                .requires(Tags.Items.CROPS_WHEAT)), c, RootsAPI.rl("summon/sheep"));

    RecipeSaver.saver().unlockedBy("has_seagrass", has(Items.SEAGRASS))
        .save(SummonCreaturesRecipe.Builder.create().entity(EntityType.TURTLE)
            .build(BaseRecipeData.Builder.create().requires(Items.SEAGRASS)
                .requires(Tags.Items.SANDS)
                .requires(Tags.Items.SANDS)), c, RootsAPI.rl("summon/turtle"));

    RecipeSaver.saver().unlockedBy("has_bone", has(Tags.Items.BONES))
        .save(SummonCreaturesRecipe.Builder.create().entity(EntityType.WOLF)
            .build(BaseRecipeData.Builder.create().requires(Tags.Items.BONES)
                .requires(Tags.Items.BONES)
                .requires(Items.MUTTON)), c, RootsAPI.rl("summon/wolf"));

    RecipeSaver.saver().unlockedBy("has_lilypad", has(Items.LILY_PAD))
        .save(SummonCreaturesRecipe.Builder.create().entity(EntityType.TADPOLE)
            .build(BaseRecipeData.Builder.create().requires(Items.LILY_PAD)
                .requires(RootsTags.Items.GROVE_MOSS_HERB)
                .requires(RootsTags.Items.DEWGONIA_HERB)), c, RootsAPI.rl("summon/tadpole"));

    RecipeSaver.saver().unlockedBy("has_vines", has(Items.VINE))
        .save(SummonCreaturesRecipe.Builder.create().entity(EntityType.SQUID)
            .build(BaseRecipeData.Builder.create().requires(Items.VINE)
                .requires(Items.VINE)
                .requires(Tags.Items.DYES_BLACK)), c, RootsAPI.rl("summon/squid"));

    RecipeSaver.saver().unlockedBy("has_lily_pad", has(Items.LILY_PAD))
        .save(SummonCreaturesRecipe.Builder.create().entity(EntityType.PUFFERFISH)
            .build(BaseRecipeData.Builder.create().requires(Items.LILY_PAD)
                .requires(Items.SEAGRASS)
                .requires(Items.KELP)), c, RootsAPI.rl("summon/pufferfish"));

    RecipeSaver.saver().unlockedBy("has_seagrass", has(Items.SEAGRASS))
        .save(SummonCreaturesRecipe.Builder.create().entity(EntityType.SALMON)
            .build(BaseRecipeData.Builder.create().requires(Items.SEAGRASS)
                .requires(Items.SEAGRASS)
                .requires(Items.KELP)), c, RootsAPI.rl("summon/salmon"));

    RecipeSaver.saver().unlockedBy("has_kelp", has(Items.KELP))
        .save(SummonCreaturesRecipe.Builder.create().entity(EntityType.TROPICAL_FISH)
            .build(BaseRecipeData.Builder.create().requires(Items.KELP)
                .requires(Items.KELP)
                .requires(Items.KELP)), c, RootsAPI.rl("summon/tropical_fish"));

    RecipeSaver.saver().unlockedBy("has_seagrass", has(Items.SEAGRASS))
        .save(SummonCreaturesRecipe.Builder.create().entity(EntityType.COD)
            .build(BaseRecipeData.Builder.create().requires(Items.SEAGRASS)
                .requires(Items.KELP)
                .requires(Items.KELP)), c, RootsAPI.rl("summon/cod"));

    cordial(c, ModItems.APPLE_CORDIAL, RootsTags.Items.APPLES, 4);
    cordial(c, ModItems.DANDELION_CORDIAL, RootsTags.Items.DANDELIONS, 4);
    cordial(c, ModItems.LILAC_CORDIAL, RootsTags.Items.LILACS, 4);
    cordial(c, ModItems.PEONY_CORDIAL, RootsTags.Items.PEONIES, 4);
    cordial(c, ModItems.ROSE_CORDIAL, RootsTags.Items.ROSES, 4);

    cordial(c, ModItems.CACTUS_SYRUP, Tags.Items.CROPS_CACTUS, 4);

    // Former Chrysopoeia recipes

    RecipeSaver.saver().unlockedBy("has_gold", has(Tags.Items.INGOTS_GOLD))
        .save(TransmutationRecipe.create().build(BaseRecipeData.Builder.create()
            .result(new ItemStack(ModItems.SILVER_INGOT.get()))
            .requires(new GroveNumber(ModGroves.FUNGAL.get(), 1500))
            .requires(Tags.Items.INGOTS_GOLD)
            .requires(Tags.Items.INGOTS_GOLD)), c, RootsAPI.rl("transmute/gold_to_silver"));

    RecipeSaver.saver().unlockedBy("has_silver", has(RootsTags.Items.SILVER_INGOT))
        .save(TransmutationRecipe.create().build(BaseRecipeData.Builder.create()
            .result(new ItemStack(Items.GOLD_INGOT))
            .requires(new GroveNumber(ModGroves.FUNGAL.get(), 1500))
            .requires(RootsTags.Items.SILVER_INGOT)
            .requires(RootsTags.Items.SILVER_INGOT)), c, RootsAPI.rl("transmute/silver_to_gold"));

    RecipeSaver.saver().unlockedBy("has_gold", has(Tags.Items.NUGGETS_GOLD))
        .save(TransmutationRecipe.create().build(BaseRecipeData.Builder.create()
            .result(new ItemStack(ModItems.SILVER_NUGGET.get()))
            .requires(new GroveNumber(ModGroves.FUNGAL.get(), 1500 / 9))
            .requires(Tags.Items.NUGGETS_GOLD)
            .requires(Tags.Items.NUGGETS_GOLD)), c, RootsAPI.rl("transmute/gold_to_silver_nugget"));

    RecipeSaver.saver().unlockedBy("has_silver", has(RootsTags.Items.SILVER_INGOT))
        .save(TransmutationRecipe.create().build(BaseRecipeData.Builder.create()
            .result(new ItemStack(Items.GOLD_NUGGET))
            .requires(new GroveNumber(ModGroves.FUNGAL.get(), 1500 / 9))
            .requires(RootsTags.Items.SILVER_NUGGET)
            .requires(RootsTags.Items.SILVER_NUGGET)), c, RootsAPI.rl("transmute/silver_to_gold_nugget"));

    RecipeSaver.saver().unlockedBy("has_rotten_flesh", has(RootsTags.Items.ROTTEN_FLESH))
        .save(TransmutationRecipe.create().build(BaseRecipeData.Builder.create()
            .result(new ItemStack(Items.LEATHER))
            .requires(new GroveNumber(ModGroves.FUNGAL.get(), 900))
            .requires(RootsTags.Items.ROTTEN_FLESH)
            .requires(RootsTags.Items.ROTTEN_FLESH)
            .requires(RootsTags.Items.ROTTEN_FLESH)), c, RootsAPI.rl("transmute/rotten_flesh_to_leather"));

    // 1.12 transmutation recipes

    // TODO: Multi-recipes for singles
    RecipeSaver.saver().unlockedBy("has_pumpkin", has(Tags.Items.CROPS_PUMPKIN))
        .save(TransmutationRecipe.create().build(BaseRecipeData.Builder.create()
            .result(new ItemStack(Items.MELON))
            .requires(new GroveNumber(ModGroves.FUNGAL.get(), 600))
            .requires(Tags.Items.CROPS_PUMPKIN)), c, RootsAPI.rl("transmute/pumpkin_to_melon"));

    RecipeSaver.saver().unlockedBy("has_dead_bush", has(Items.DEAD_BUSH))
        .save(TransmutationRecipe.create().build(BaseRecipeData.Builder.create()
            .result(new ItemStack(Items.COCOA_BEANS, 3))
            .requires(new GroveNumber(ModGroves.FUNGAL.get(), 600))
            .requires(Items.DEAD_BUSH)), c, RootsAPI.rl("transmute/dead_bush_to_cocoa"));

    RecipeSaver.saver().unlockedBy("has_birch_leaves", has(Items.BIRCH_LEAVES))
        .save(TransmutationRecipe.create().build(BaseRecipeData.Builder.create()
            .result(new ItemStack(Items.JUNGLE_LEAVES))
            .requires(new GroveNumber(ModGroves.FUNGAL.get(), 600))
            .requires(Items.BIRCH_LEAVES)), c, RootsAPI.rl("transmute/birch_leaves_to_jungle_leaves"));

    RecipeSaver.saver().unlockedBy("has_birch_logs", has(ItemTags.BIRCH_LOGS))
        .save(TransmutationRecipe.create().build(BaseRecipeData.Builder.create()
            .result(new ItemStack(Items.JUNGLE_LOG))
            .requires(new GroveNumber(ModGroves.FUNGAL.get(), 600))
            .requires(ItemTags.BIRCH_LOGS)), c, RootsAPI.rl("transmute/birch_logs_to_jungle_logs"));

    RecipeSaver.saver().unlockedBy("has_melon", has(Tags.Items.CROPS_MELON))
        .save(TransmutationRecipe.create().build(BaseRecipeData.Builder.create()
            .result(new ItemStack(Items.CACTUS))
            .requires(new GroveNumber(ModGroves.FUNGAL.get(), 600))
            .requires(Tags.Items.CROPS_MELON)), c, RootsAPI.rl("transmute/melon_to_cactus"));

    RecipeSaver.saver().unlockedBy("has_cocoa", has(Tags.Items.CROPS_COCOA_BEAN))
        .save(TransmutationRecipe.create().build(BaseRecipeData.Builder.create()
            .result(new ItemStack(Items.CARROT))
            .requires(new GroveNumber(ModGroves.FUNGAL.get(), 600))
            .requires(Tags.Items.CROPS_COCOA_BEAN)), c, RootsAPI.rl("transmute/cocoa_to_carrot"));

    RecipeSaver.saver().unlockedBy("has_carrot", has(Tags.Items.CROPS_CARROT))
        .save(TransmutationRecipe.create().build(BaseRecipeData.Builder.create()
            .result(new ItemStack(Items.BEETROOT_SEEDS))
            .requires(new GroveNumber(ModGroves.FUNGAL.get(), 600))
            .requires(Tags.Items.CROPS_CARROT)), c, RootsAPI.rl("transmute/carrot_to_beetroot_seeds"));

    RecipeSaver.saver().unlockedBy("has_beetroot", has(Tags.Items.CROPS_BEETROOT))
        .save(TransmutationRecipe.create().build(BaseRecipeData.Builder.create()
            .result(new ItemStack(Items.POTATO))
            .requires(new GroveNumber(ModGroves.FUNGAL.get(), 600))
            .requires(Tags.Items.CROPS_BEETROOT)), c, RootsAPI.rl("transmute/beetroot_to_potato"));

    RecipeSaver.saver().unlockedBy("has_trapdoor", has(ItemTags.TRAPDOORS))
        .save(TransmutationRecipe.create().build(BaseRecipeData.Builder.create()
            .result(new ItemStack(Items.COBWEB))
            .requires(new GroveNumber(ModGroves.FUNGAL.get(), 600))
            .requires(ItemTags.TRAPDOORS)), c, RootsAPI.rl("transmute/trapdoor_to_cobweb"));

    RecipeSaver.saver().unlockedBy("has_carpet", has(ItemTags.WOOL_CARPETS))
        .save(TransmutationRecipe.create().build(BaseRecipeData.Builder.create()
            .result(new ItemStack(Items.LILY_PAD))
            .requires(new GroveNumber(ModGroves.FUNGAL.get(), 600))
            .requires(ItemTags.WOOL_CARPETS)), c, RootsAPI.rl("transmute/carpet_to_lilypad"));

    RecipeSaver.saver().unlockedBy("has_cactus", has(Tags.Items.CROPS_CACTUS))
        .save(TransmutationRecipe.create().build(BaseRecipeData.Builder.create()
            .result(new ItemStack(Items.PUMPKIN))
            .requires(new GroveNumber(ModGroves.FUNGAL.get(), 600))
            .requires(Tags.Items.CROPS_CACTUS)), c, RootsAPI.rl("transmute/cactus_to_pumpkin"));

    RecipeSaver.saver().unlockedBy("has_redstone", has(Tags.Items.DUSTS_REDSTONE))
        .save(TransmutationRecipe.create().build(BaseRecipeData.Builder.create()
            .result(new ItemStack(Items.VINE))
            .requires(new GroveNumber(ModGroves.FUNGAL.get(), 600))
            .requires(Tags.Items.DUSTS_REDSTONE)), c, RootsAPI.rl("transmute/redstone_to_vine"));

    RecipeSaver.saver().unlockedBy("has_redstone_block", has(Tags.Items.STORAGE_BLOCKS_REDSTONE))
        .save(TransmutationRecipe.create().build(BaseRecipeData.Builder.create()
            .result(new ItemStack(Items.GLOWSTONE))
            .requires(new GroveNumber(ModGroves.FUNGAL.get(), 2000))
            .requires(Tags.Items.STORAGE_BLOCKS_REDSTONE)), c, RootsAPI.rl("transmute/redstone_block_to_glowstone"));

    RecipeSaver.saver().unlockedBy("has_lily_pad", has(Items.LILY_PAD))
        .save(TransmutationRecipe.create().build(BaseRecipeData.Builder.create()
            .result(new ItemStack(Items.SNOW))
            .requires(new GroveNumber(ModGroves.FUNGAL.get(), 600))
            .requires(Items.LILY_PAD)), c, RootsAPI.rl("transmute/lily_pad_to_snow"));

    RecipeSaver.saver().unlockedBy("has_lever", has(RootsTags.Items.LEVERS))
        .save(TransmutationRecipe.create().build(BaseRecipeData.Builder.create()
            .result(new ItemStack(Items.BROWN_MUSHROOM))
            .requires(new GroveNumber(ModGroves.FUNGAL.get(), 600))
            .requires(RootsTags.Items.LEVERS)), c, RootsAPI.rl("transmute/lever_to_brown_mushroom"));

    RecipeSaver.saver().unlockedBy("has_redstone_torch", has(Items.REDSTONE_TORCH))
        .save(TransmutationRecipe.create().build(BaseRecipeData.Builder.create()
            .result(new ItemStack(Items.RED_MUSHROOM))
            .requires(new GroveNumber(ModGroves.FUNGAL.get(), 600))
            .requires(Items.REDSTONE_TORCH)), c, RootsAPI.rl("transmute/redstone_torch_to_red_mushroom"));

    RecipeSaver.saver().unlockedBy("has_bone_meal", has(Tags.Items.FERTILIZERS))
        .save(GroveRecipe.Builder.create()
            .build(BaseRecipeData.Builder.create()
                .result(ModItems.FUNGAL_TRANSMUTER.get(), 1)
                .condition(ModConditions.FUNGAL_GROVE_STONE_ACTIVE.get())
                .condition(ModConditions.FUNGAL_RANK_1.get())
                .requires(Tags.Items.FERTILIZERS)
                .requires(ItemTags.LOGS)
                .requires(Tags.Items.INGOTS_IRON)
                .requires(RootsTags.Items.WILDWOOD_LOGS)
                .requires(RootsTags.Items.WILDWOOD_LOGS)
                .requires(RootsTags.Items.WILDWOOD_LOGS)
            ), c, RootsAPI.rl("grove/fungal_transmuter"));

    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.GRAMARY.get())
        .pattern("LLL")
        .pattern("LHL")
        .pattern("LLL")
        .define('L', ItemTags.LOGS)
        .define('H', RootsTags.Items.WILDROOT_CROP)
        .unlockedBy("has_wildroot", has(RootsTags.Items.WILDROOT_CROP))
        .save(c, RootsAPI.rl("gramary"));

    ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ModBlocks.RUNESTONE_TICKER.get())
        .pattern("RRR")
        .pattern("RDR")
        .pattern("RRR")
        .define('R', RootsTags.Items.RUNESTONE)
        .define('D', Tags.Items.DUSTS_REDSTONE)
        .unlockedBy("has_redstone", has(Tags.Items.DUSTS_REDSTONE))
        .save(c, RootsAPI.rl("runestone_ticker"));

    RecipeSaver.saver().unlockedBy("has_wildwood", has(RootsTags.Items.WILDWOOD_LOGS))
        .save(GroveRecipe.Builder.create()
            .build(BaseRecipeData.Builder.create()
                .result(ModItems.WILDWOOD_QUIVER.get(), 1)
                .requires(RootsTags.Items.WILDWOOD_LOGS)
                .requires(RootsTags.Items.WILDWOOD_LOGS)
                .requires(RootsTags.Items.WILDWOOD_LOGS)
                .requires(Tags.Items.STRINGS)
                .requires(ItemTags.ARROWS)
                .requires(RootsTags.Items.SPIRITLEAF_CROP)
                .requires(RootsTags.Items.WILDEWHEET_CROP)
                .requires(RootsTags.Items.PERESKIA_CROP)
            ), c, RootsAPI.rl("grove/wildwood_quiver"));
    GENERATING_RECIPES.set(false);
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

  @SuppressWarnings("NullableProblems")
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

  protected static void cordial(RecipeOutput c, Holder<Item> result, TagKey<Item> ingredient, int amount) {
    ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, result.value(), amount)
        .pattern("1S1")
        .pattern("BWB")
        .pattern("BSB")
        .define('1', ingredient)
        .define('S', Items.SUGAR)
        .define('B', RootsTags.Items.BOTTLES)
        .define('W', Items.WATER_BUCKET)
        .unlockedBy("has_sugar", has(Items.SUGAR))
        .unlockedBy("has_ingredient", has(ingredient))
        .save(c, RootsAPI.rl("cordial/" + result.getKey().location().getPath()));
  }
}
