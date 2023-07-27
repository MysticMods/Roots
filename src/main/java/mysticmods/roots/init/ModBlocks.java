package mysticmods.roots.init;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.providers.loot.RegistrateBlockLootTables;
import com.tterrag.registrate.util.DataIngredient;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import com.tterrag.registrate.util.nullness.NonNullUnaryOperator;
import mysticmods.roots.Roots;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.StateProperties;
import mysticmods.roots.block.*;
import mysticmods.roots.block.crop.ElementalCropBlock;
import mysticmods.roots.block.crop.ThreeStageCropBlock;
import mysticmods.roots.block.crop.WaterElementalCropBlock;
import mysticmods.roots.recipe.grove.GroveRecipe;
import mysticmods.roots.worldgen.trees.WildwoodTreeGrower;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.Direction;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.BinomialDistributionGenerator;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.Tags;
import noobanidus.libs.noobutil.block.BaseBlocks;
import noobanidus.libs.noobutil.block.BaseBlocks.SeededCropsBlock;
import noobanidus.libs.noobutil.block.WaterloggedBlock;
import noobanidus.libs.noobutil.data.generator.BlockstateGenerator;
import noobanidus.libs.noobutil.data.generator.ItemModelGenerator;

import java.util.Collections;
import java.util.function.Supplier;

import static mysticmods.roots.Roots.REGISTRATE;

// STAIRS
// SLABS
// FENCES
// BUTTONS
// PRESSURE PLATES
// DOORS
// TRAPDOORS
// FENCE GATES

public class ModBlocks {
  public static NonNullUnaryOperator<BlockBehaviour.Properties> RUNED_PROPERTIES = r -> BlockBehaviour.Properties.copy(net.minecraft.world.level.block.Blocks.OBSIDIAN);
  public static NonNullUnaryOperator<BlockBehaviour.Properties> RUNESTONE_PROPERTIES = r -> BlockBehaviour.Properties.copy(net.minecraft.world.level.block.Blocks.STONE);
  public static NonNullUnaryOperator<BlockBehaviour.Properties> RUNED_LOG_PROPERTIES = r -> BlockBehaviour.Properties.copy(net.minecraft.world.level.block.Blocks.OAK_LOG);
  public static NonNullUnaryOperator<BlockBehaviour.Properties> RUNED_STEM_PROPERTIES = r -> BlockBehaviour.Properties.copy(net.minecraft.world.level.block.Blocks.CRIMSON_STEM);
  public static NonNullUnaryOperator<BlockBehaviour.Properties> WILDWOOD_PLANKS_PROPERTIES = r -> BlockBehaviour.Properties.copy(net.minecraft.world.level.block.Blocks.OAK_PLANKS);
  public static NonNullUnaryOperator<BlockBehaviour.Properties> WILDWOOD_LOG_PROPERTIES = r -> BlockBehaviour.Properties.copy(net.minecraft.world.level.block.Blocks.OAK_LOG);
  public static NonNullUnaryOperator<BlockBehaviour.Properties> WILDWOOD_LEAVES_PROPERTIES = r -> BlockBehaviour.Properties.copy(net.minecraft.world.level.block.Blocks.OAK_LEAVES);
  public static NonNullUnaryOperator<BlockBehaviour.Properties> CROP_PROPERTIES = r -> BlockBehaviour.Properties.copy(Blocks.WHEAT);
  private static final NonNullUnaryOperator<Block.Properties> ORE_PROPERTIES = o -> BlockBehaviour.Properties.copy(Blocks.IRON_ORE);
  private static final NonNullUnaryOperator<Block.Properties> DEEPSLATE_ORE_PROPERTIES = o -> BlockBehaviour.Properties.copy(Blocks.DEEPSLATE_IRON_ORE);

  private static <T extends Block> NonNullBiConsumer<RegistrateBlockLootTables, T> oreLoot(Supplier<Item> drops) {
    return (ctx, p) -> ctx.add(p, RegistrateBlockLootTables.createOreDrop(p, drops.get()));
  }

  private static <T extends Block> NonNullBiConsumer<RegistrateBlockLootTables, T> cropLoot(IntegerProperty property, Supplier<? extends Item> seedSupplier, Supplier<? extends Item> productSupplier) {
    return (p, t) -> {
      int maxValue = Collections.max(property.getPossibleValues());
      LootItemBlockStatePropertyCondition.Builder condition = new LootItemBlockStatePropertyCondition.Builder(t).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(property, maxValue));
      p.add(t,
        RegistrateBlockLootTables.applyExplosionDecay(t, LootTable.lootTable().withPool(LootPool.lootPool().add(LootItem.lootTableItem(productSupplier.get()).when(condition).otherwise(LootItem.lootTableItem(seedSupplier.get())))).withPool(LootPool.lootPool().when(condition).add(LootItem.lootTableItem(seedSupplier.get()).apply(ApplyBonusCount.addBonusBinomialDistributionCount(Enchantments.BLOCK_FORTUNE, 0.2714286F, 3))))));
    };
  }

  private static <T extends Block> NonNullBiConsumer<RegistrateBlockLootTables, T> seedlessCropLoot(IntegerProperty property, Supplier<? extends Item> seedSupplier) {
    return (p, t) -> {
      LootItemCondition.Builder grown = new LootItemBlockStatePropertyCondition.Builder(t).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(property, Collections.max(property.getPossibleValues())));
      p.add(t, RegistrateBlockLootTables.applyExplosionDecay(t, LootTable.lootTable().withPool(LootPool.lootPool().add(LootItem.lootTableItem(seedSupplier.get()).when(grown).otherwise(LootItem.lootTableItem(seedSupplier.get())))).withPool(LootPool.lootPool().when(grown).add(LootItem.lootTableItem(seedSupplier.get())))));
    };
  }

  // TODO: AT this?
  private static final float[] NORMAL_LEAVES_SAPLING_CHANCES = new float[]{0.05F, 0.0625F, 0.083333336F, 0.1F};


  public static BlockEntry<WaterloggedBlock> THATCH = REGISTRATE.block("thatch", Material.WOOD, WaterloggedBlock::new)
    .item()
    .model((ctx, p) -> p.blockItem(ModBlocks.THATCH))
    .build()
    .recipe((ctx, p) -> ShapedRecipeBuilder.shaped(ModBlocks.THATCH.get(), 32)
      .pattern("XY")
      .pattern("YX")
      .define('X', Blocks.HAY_BLOCK)
      .define('Y', Tags.Items.CROPS_WHEAT)
      .unlockedBy("has_hay", RegistrateRecipeProvider.has(Blocks.HAY_BLOCK))
      .unlockedBy("has_wheat", RegistrateRecipeProvider.has(Items.WHEAT))
      .save(p)
    )
    .tag(BlockTags.MINEABLE_WITH_HOE)
    .register();
  public static BlockEntry<Block> RUNESTONE = REGISTRATE.block("runestone", Block::new)
    .properties(RUNESTONE_PROPERTIES)
    .recipe((ctx, p) -> Roots.RECIPES.twoByTwo(ModBlocks.RUNESTONE_BRICK_ALT, ModBlocks.RUNESTONE, null, 4, p))
    .item()
    .model(ItemModelGenerator::itemModel)
    .build()
    .tag(RootsTags.Blocks.RUNESTONE, RootsTags.Blocks.RUNE_PILLARS, BlockTags.MINEABLE_WITH_PICKAXE)
    .recipe((ctx, p) -> {
      ShapedRecipeBuilder.shaped(ctx.getEntry(), 3)
        .pattern("SSS")
        .pattern("SHS")
        .pattern("SSS")
        .define('S', Ingredient.of(RootsTags.Items.STONELIKE))
        .define('H', Ingredient.of(RootsTags.Items.RUNESTONE_HERBS))
        .unlockedBy("has_item", RegistrateRecipeProvider.has(RootsTags.Items.RUNESTONE_HERBS))
        .save(p, new ResourceLocation(RootsAPI.MODID, "runestone_simple_crafting"));
      // TODO: Grove Stone condition
      GroveRecipe.multiBuilder(ctx.getEntry(), 20)
        .addIngredient(Ingredient.of(RootsTags.Items.STONELIKE))
        .addLevelCondition(ModConditions.PRIMAL_GROVE_STONE_ANY.get())
        .unlockedBy("has_item", RegistrateRecipeProvider.has(RootsTags.Items.STONELIKE))
        .save(p, new ResourceLocation(RootsAPI.MODID, "grove/runestone_grove_crafting"));
    })
    .register();
  public static BlockEntry<Block> CHISELED_RUNESTONE = REGISTRATE.block("chiseled_runestone", Block::new)
    .properties(RUNESTONE_PROPERTIES)
    .recipe((ctx, p) -> Roots.RECIPES.twoByTwo(ModBlocks.RUNESTONE_BRICK, ModBlocks.CHISELED_RUNESTONE, null, 4, p))
    .item()
    .model(ItemModelGenerator::itemModel)
    .build()
    .tag(RootsTags.Blocks.RUNESTONE, RootsTags.Blocks.RUNE_CAPSTONES, BlockTags.MINEABLE_WITH_PICKAXE)
    .register();
  public static BlockEntry<Block> RUNESTONE_BRICK = REGISTRATE.block("runestone_brick", Block::new)
    .properties(RUNESTONE_PROPERTIES)
    .recipe((ctx, p) -> Roots.RECIPES.twoByTwo(ModBlocks.RUNESTONE, ModBlocks.RUNESTONE_BRICK, null, 4, p))
    .item()
    .model(ItemModelGenerator::itemModel)
    .build()
    .tag(RootsTags.Blocks.RUNESTONE, BlockTags.MINEABLE_WITH_PICKAXE)
    .register();


  public static BlockEntry<Block> RUNESTONE_BRICK_ALT = REGISTRATE.block("runestone_brick_alt", Block::new)
    .properties(RUNESTONE_PROPERTIES)
    .recipe((ctx, p) -> Roots.RECIPES.twoByTwo(ModBlocks.CHISELED_RUNESTONE, ModBlocks.RUNESTONE_BRICK_ALT, null, 4, p))
    .item()
    .model(ItemModelGenerator::itemModel)
    .build()
    .tag(RootsTags.Blocks.RUNESTONE, RootsTags.Blocks.RUNE_PILLARS)
    .register();

  public static BlockEntry<Block> RUNED_OBSIDIAN = REGISTRATE.block("runed_obsidian", Block::new)
    .properties(RUNED_PROPERTIES)
    .recipe((ctx, p) -> Roots.RECIPES.twoByTwo(ModBlocks.RUNED_OBSIDIAN_BRICK_ALT, ModBlocks.RUNED_OBSIDIAN, null, 4, p))
    .item()
    .model(ItemModelGenerator::itemModel)
    .build()
    .recipe((ctx, p) -> {
      Ingredient RUNESTONE = Ingredient.of(RootsTags.Items.RUNESTONE);
      GroveRecipe.builder(ctx.getEntry(), 4)
        .addIngredient(RUNESTONE)
        .addIngredient(RUNESTONE)
        .addIngredient(RUNESTONE)
        .addIngredient(RUNESTONE)
        .addIngredient(Tags.Items.OBSIDIAN)
        .addLevelCondition(ModConditions.GROVE_STONE_VALID.get())
        .unlockedBy("has_runestone", p.has(RootsTags.Items.RUNESTONE))
        .unlockedBy("has_obsidian", p.has(Tags.Items.OBSIDIAN))
        .save(p, new ResourceLocation(RootsAPI.MODID, "grove/runed_obsidian_4"));
      GroveRecipe.builder(ctx.getEntry(), 8)
        .addIngredient(RUNESTONE)
        .addIngredient(RUNESTONE)
        .addIngredient(RUNESTONE)
        .addIngredient(RUNESTONE)
        .addIngredient(RUNESTONE)
        .addIngredient(RUNESTONE)
        .addIngredient(RUNESTONE)
        .addIngredient(RUNESTONE)
        .addIngredient(Tags.Items.OBSIDIAN)
        .addLevelCondition(ModConditions.GROVE_STONE_VALID.get())
        .unlockedBy("has_runestone", p.has(RootsTags.Items.RUNESTONE))
        .unlockedBy("has_obsidian", p.has(Tags.Items.OBSIDIAN))
        .save(p, new ResourceLocation(RootsAPI.MODID, "grove/runed_obsidian_8"));
    })
    .tag(BlockTags.DRAGON_IMMUNE, BlockTags.WITHER_IMMUNE, RootsTags.Blocks.RUNED_OBSIDIAN, RootsTags.Blocks.RUNE_PILLARS, BlockTags.MINEABLE_WITH_PICKAXE, BlockTags.NEEDS_IRON_TOOL)
    .register();
  public static BlockEntry<Block> CHISELED_RUNED_OBSIDIAN = REGISTRATE.block("chiseled_runed_obsidian", Block::new)
    .properties(RUNED_PROPERTIES)
    .recipe((ctx, p) -> Roots.RECIPES.twoByTwo(ModBlocks.RUNED_OBSIDIAN_BRICK, ModBlocks.CHISELED_RUNED_OBSIDIAN, null, 4, p))
    .item()
    .model(ItemModelGenerator::itemModel)
    .build()
    .tag(BlockTags.DRAGON_IMMUNE, BlockTags.WITHER_IMMUNE, RootsTags.Blocks.RUNED_OBSIDIAN, RootsTags.Blocks.RUNE_CAPSTONES, BlockTags.MINEABLE_WITH_PICKAXE, BlockTags.NEEDS_IRON_TOOL)
    .register();

  public static BlockEntry<Block> RUNED_OBSIDIAN_BRICK = REGISTRATE.block("runed_obsidian_brick", Block::new)
    .properties(RUNED_PROPERTIES)
    .recipe((ctx, p) -> Roots.RECIPES.twoByTwo(RUNED_OBSIDIAN, ModBlocks.RUNED_OBSIDIAN_BRICK, null, 4, p))
    .item()
    .model(ItemModelGenerator::itemModel)
    .build()
    .tag(BlockTags.DRAGON_IMMUNE, BlockTags.WITHER_IMMUNE, RootsTags.Blocks.RUNED_OBSIDIAN, RootsTags.Blocks.RUNE_PILLARS, BlockTags.MINEABLE_WITH_PICKAXE, BlockTags.NEEDS_IRON_TOOL)
    .register();
  public static BlockEntry<Block> RUNED_OBSIDIAN_BRICK_ALT = REGISTRATE.block("runed_obsidian_brick_alt", Block::new)
    .properties(RUNED_PROPERTIES)
    .recipe((ctx, p) -> Roots.RECIPES.twoByTwo(CHISELED_RUNED_OBSIDIAN, ModBlocks.RUNED_OBSIDIAN_BRICK_ALT, null, 4, p))
    .item()
    .model(ItemModelGenerator::itemModel)
    .build()
    .tag(BlockTags.DRAGON_IMMUNE, BlockTags.WITHER_IMMUNE, RootsTags.Blocks.RUNED_OBSIDIAN, RootsTags.Blocks.RUNE_PILLARS, BlockTags.MINEABLE_WITH_PICKAXE, BlockTags.NEEDS_IRON_TOOL)
    .register();

  public static BlockEntry<DropExperienceBlock> SILVER_ORE = REGISTRATE.block("silver_ore", DropExperienceBlock::new)
    .properties(ORE_PROPERTIES)
    .item()
    .model(ItemModelGenerator::itemModel)
    .build()
    .tag(RootsTags.Blocks.SILVER_ORE, BlockTags.MINEABLE_WITH_PICKAXE, BlockTags.NEEDS_IRON_TOOL)
    .blockstate(BlockstateGenerator::simpleBlockstate)
    .loot(oreLoot(() -> ModItems.RAW_SILVER.get()))
    .register();

  public static BlockEntry<DropExperienceBlock> DEEPSLATE_SILVER_ORE = REGISTRATE.block("deepslate_silver_ore", DropExperienceBlock::new)
    .properties(DEEPSLATE_ORE_PROPERTIES)
    .item()
    .model(ItemModelGenerator::itemModel)
    .build()
    .tag(RootsTags.Blocks.SILVER_ORE, BlockTags.MINEABLE_WITH_PICKAXE, BlockTags.NEEDS_IRON_TOOL)
    .blockstate(BlockstateGenerator::simpleBlockstate)
    .loot(oreLoot(() -> ModItems.RAW_SILVER.get()))
    .register();

  public static BlockEntry<DropExperienceBlock> GRANITE_QUARTZ_ORE = REGISTRATE.block("granite_quartz_ore", (p) -> new DropExperienceBlock(p, UniformInt.of(2, 5)))
    .properties(ORE_PROPERTIES)
    .item()
    .model(ItemModelGenerator::itemModel)
    .build()
    .tag(RootsTags.Blocks.QUARTZ_ORE, BlockTags.MINEABLE_WITH_PICKAXE)
    .blockstate(BlockstateGenerator::simpleBlockstate)
    .loot(oreLoot(() -> Items.QUARTZ))
    .register();

  public static BlockEntry<Block> RAW_SILVER_BLOCK = REGISTRATE.block("raw_silver_block", Material.METAL, Block::new)
    .item()
    .model(ItemModelGenerator::itemModel)
    .build()
    .tag(RootsTags.Blocks.RAW_SILVER_STORAGE, BlockTags.MINEABLE_WITH_PICKAXE)
    .blockstate(BlockstateGenerator::simpleBlockstate)
    .recipe((ctx, p) ->
      ShapedRecipeBuilder.shaped(ctx.getEntry())
        .pattern("###")
        .pattern("#I#")
        .pattern("###")
        .define('#', RootsTags.Items.RAW_SILVER)
        .define('I', ModItems.RAW_SILVER.get())
        .unlockedBy("has_raw_silver", RegistrateRecipeProvider.has(RootsTags.Items.RAW_SILVER))
        .save(p)
    )
    .register();

  public static BlockEntry<Block> SILVER_BLOCK = REGISTRATE.block("silver_block", Material.METAL, Block::new)
    .properties(o -> o.strength(5.0f, 6.0f).sound(SoundType.METAL))
    .item()
    .model(ItemModelGenerator::itemModel)
    .build()
    .tag(RootsTags.Blocks.SILVER_STORAGE, BlockTags.BEACON_BASE_BLOCKS, BlockTags.MINEABLE_WITH_PICKAXE, BlockTags.NEEDS_IRON_TOOL)
    .blockstate(BlockstateGenerator::simpleBlockstate)
    .register();

  public static BlockEntry<RotatedPillarBlock> WILDWOOD_LOG = REGISTRATE.block("wildwood_log", Material.WOOD, RotatedPillarBlock::new)
    .properties(WILDWOOD_LOG_PROPERTIES)
    .blockstate((ctx, p) -> p.logBlock(ctx.getEntry()))
    .item()
    .model(ItemModelGenerator::itemModel)
    .build()
    .tag(RootsTags.Blocks.WILDWOOD_LOGS, BlockTags.MINEABLE_WITH_AXE)
    .register();
  public static BlockEntry<RotatedPillarBlock> STRIPPED_WILDWOOD_LOG = REGISTRATE.block("stripped_wildwood_log", Material.WOOD, RotatedPillarBlock::new)
    .properties(WILDWOOD_LOG_PROPERTIES)
    .blockstate((ctx, p) -> p.logBlock(ctx.getEntry()))
    .item()
    .model(ItemModelGenerator::itemModel)
    .build()
    .tag(RootsTags.Blocks.WILDWOOD_LOGS, BlockTags.MINEABLE_WITH_AXE)
    .register();
  public static BlockEntry<RotatedPillarBlock> WILDWOOD_WOOD = REGISTRATE.block("wildwood_wood", Material.WOOD, RotatedPillarBlock::new)
    .properties(WILDWOOD_LOG_PROPERTIES)
    .blockstate((ctx, p) -> p.axisBlock(ctx.getEntry(), new ResourceLocation(RootsAPI.MODID, "block/wildwood_log"), new ResourceLocation(RootsAPI.MODID, "block/wildwood_log")))
    .item()
    .model(ItemModelGenerator::itemModel)
    .build()
    .tag(RootsTags.Blocks.WILDWOOD_LOGS, BlockTags.MINEABLE_WITH_AXE)
    .register();
  public static BlockEntry<RotatedPillarBlock> STRIPPED_WILDWOOD_WOOD = REGISTRATE.block("stripped_wildwood_wood", Material.WOOD, RotatedPillarBlock::new)
    .properties(WILDWOOD_LOG_PROPERTIES)
    .blockstate((ctx, p) -> p.axisBlock(ctx.getEntry(), new ResourceLocation(RootsAPI.MODID, "block/stripped_wildwood_log"), new ResourceLocation(RootsAPI.MODID, "block/stripped_wildwood_log")))
    .item()
    .model(ItemModelGenerator::itemModel)
    .build()
    .tag(RootsTags.Blocks.WILDWOOD_LOGS, BlockTags.MINEABLE_WITH_AXE)
    .register();

  public static BlockEntry<Block> WILDWOOD_PLANKS = REGISTRATE.block("wildwood_planks", Block::new)
    .properties(WILDWOOD_PLANKS_PROPERTIES)
    .item()
    .model(ItemModelGenerator::itemModel)
    .build()
    .tag(BlockTags.PLANKS, BlockTags.MINEABLE_WITH_AXE)
    .register();

  public static BlockEntry<SaplingBlock> WILDWOOD_SAPLING = REGISTRATE.block("wildwood_sapling", (p) -> new SaplingBlock(new WildwoodTreeGrower(), p))
    .properties(o -> BlockBehaviour.Properties.copy(Blocks.OAK_SAPLING))
    .blockstate((ctx, p) -> {
      ModelFile crop = p.models().getExistingFile(new ResourceLocation("minecraft", "block/cross"));
      p.getVariantBuilder(ctx.getEntry())
        .forAllStates(state -> {
          ModelFile stage = p.models().getBuilder("block/wildwood_sapling")
            .parent(crop)
            .texture("cross", p.modLoc("block/wildwood_sapling"));
          return ConfiguredModel.builder().modelFile(stage).build();
        });
    })
    .item()
    .tag(ItemTags.SAPLINGS)
    .model(ItemModelGenerator::generated)
    .build()
    .tag(BlockTags.SAPLINGS)
    .register();

  public static BlockEntry<PetrifiedFlowerBlock> STONEPETAL = REGISTRATE.block("stonepetal", Material.PLANT, PetrifiedFlowerBlock::new)
    .properties(o -> o.noCollission().instabreak().sound(SoundType.GRASS))
    .blockstate((ctx, p) -> p.getVariantBuilder(ctx.getEntry()).partialState().setModels(new ConfiguredModel(p.models().cross(ctx.getName(), p.blockTexture(ctx.getEntry())))))
    .item()
    .model(ItemModelGenerator::generated)
    .build()
    .tag(RootsTags.Blocks.STONEPETAL, BlockTags.MINEABLE_WITH_HOE)
    .recipe((ctx, p) -> {
      DataIngredient a = DataIngredient.items(ModBlocks.STONEPETAL.get());
      ShapelessRecipeBuilder.shapeless(Items.GRAY_DYE, 4).requires(ctx.getEntry()).unlockedBy("has_stonepetal", a.getCritereon(p)).save(p, new ResourceLocation(RootsAPI.MODID, "gray_dye_from_stonepetal"));
    })
    .register();

  // TODO: Leaves additionally drop wildroot
  public static BlockEntry<LeavesBlock> WILDWOOD_LEAVES = REGISTRATE.block("wildwood_leaves", LeavesBlock::new)
    .properties(WILDWOOD_LEAVES_PROPERTIES)
    .loot((p, ctx) -> p.add(ctx, p.createLeavesDrops(ctx, ModBlocks.WILDWOOD_SAPLING.get(), NORMAL_LEAVES_SAPLING_CHANCES)))
    .item()
    .model(ItemModelGenerator::itemModel)
    .build()
    .tag(BlockTags.LEAVES, BlockTags.MINEABLE_WITH_HOE)
    .register();

  public static BlockEntry<RotatedPillarBlock> RUNED_WILDWOOD_LOG = REGISTRATE.block("runed_wildwood_log", RotatedPillarBlock::new)
    .properties(RUNED_LOG_PROPERTIES)
    .blockstate((ctx, p) -> p.axisBlock(ctx.getEntry(), new ResourceLocation(RootsAPI.MODID, "block/runed_wildwood"), new ResourceLocation(RootsAPI.MODID, "block/wildwood_log_top")))
    .tag(RootsTags.Blocks.WILDWOOD_LOGS, RootsTags.Blocks.RUNED_WILDWOOD_LOG, BlockTags.MINEABLE_WITH_AXE)
    .item()
    .model(ItemModelGenerator::itemModel)
    .build()
    .register();
  public static BlockEntry<RotatedPillarBlock> RUNED_SPRUCE_LOG = REGISTRATE.block("runed_spruce_log", RotatedPillarBlock::new)
    .properties(RUNED_LOG_PROPERTIES)
    .blockstate((ctx, p) -> p.axisBlock(ctx.getEntry(), new ResourceLocation(RootsAPI.MODID, "block/runed_spruce"), new ResourceLocation("minecraft", "block/spruce_log_top")))
    .tag(BlockTags.SPRUCE_LOGS, RootsTags.Blocks.RUNED_SPRUCE_LOG, BlockTags.MINEABLE_WITH_AXE)
    .item()
    .model(ItemModelGenerator::itemModel)
    .build()
    .register();
  public static BlockEntry<RotatedPillarBlock> RUNED_JUNGLE_LOG = REGISTRATE.block("runed_jungle_log", RotatedPillarBlock::new)
    .properties(RUNED_LOG_PROPERTIES)
    .blockstate((ctx, p) -> p.axisBlock(ctx.getEntry(), new ResourceLocation(RootsAPI.MODID, "block/runed_jungle"), new ResourceLocation("minecraft", "block/jungle_log_top")))
    .tag(BlockTags.JUNGLE_LOGS, RootsTags.Blocks.RUNED_JUNGLE_LOG, BlockTags.MINEABLE_WITH_AXE)
    .item()
    .model(ItemModelGenerator::itemModel)
    .build()
    .register();
  public static BlockEntry<RotatedPillarBlock> RUNED_BIRCH_LOG = REGISTRATE.block("runed_birch_log", RotatedPillarBlock::new)
    .properties(RUNED_LOG_PROPERTIES)
    .blockstate((ctx, p) -> p.axisBlock(ctx.getEntry(), new ResourceLocation(RootsAPI.MODID, "block/runed_birch"), new ResourceLocation("minecraft", "block/birch_log_top")))
    .tag(BlockTags.BIRCH_LOGS, RootsTags.Blocks.RUNED_BIRCH_LOG, BlockTags.MINEABLE_WITH_AXE)
    .item()
    .model(ItemModelGenerator::itemModel)
    .build()
    .register();
  public static BlockEntry<RotatedPillarBlock> RUNED_OAK_LOG = REGISTRATE.block("runed_oak_log", RotatedPillarBlock::new)
    .properties(RUNED_LOG_PROPERTIES)
    .blockstate((ctx, p) -> p.axisBlock(ctx.getEntry(), new ResourceLocation(RootsAPI.MODID, "block/runed_oak"), new ResourceLocation("minecraft", "block/oak_log_top")))
    .tag(BlockTags.OAK_LOGS, RootsTags.Blocks.RUNED_OAK_LOG, BlockTags.MINEABLE_WITH_AXE)
    .item()
    .model(ItemModelGenerator::itemModel)
    .build()
    .register();
  public static BlockEntry<RotatedPillarBlock> RUNED_DARK_OAK_LOG = REGISTRATE.block("runed_dark_oak_log", RotatedPillarBlock::new)
    .properties(RUNED_LOG_PROPERTIES)
    .blockstate((ctx, p) -> p.axisBlock(ctx.getEntry(), new ResourceLocation(RootsAPI.MODID, "block/runed_dark_oak"), new ResourceLocation("minecraft", "block/dark_oak_log_top")))
    .tag(BlockTags.DARK_OAK_LOGS, RootsTags.Blocks.RUNED_DARK_OAK_LOG, BlockTags.MINEABLE_WITH_AXE)
    .item()
    .model(ItemModelGenerator::itemModel)
    .build()
    .register();
  public static BlockEntry<RotatedPillarBlock> RUNED_ACACIA_LOG = REGISTRATE.block("runed_acacia_log", RotatedPillarBlock::new)
    .properties(RUNED_LOG_PROPERTIES)
    .blockstate((ctx, p) -> p.axisBlock(ctx.getEntry(), new ResourceLocation(RootsAPI.MODID, "block/runed_acacia"), new ResourceLocation("minecraft", "block/acacia_log_top")))
    .tag(BlockTags.ACACIA_LOGS, RootsTags.Blocks.RUNED_ACACIA_LOG, BlockTags.MINEABLE_WITH_AXE)
    .item()
    .model(ItemModelGenerator::itemModel)
    .build()
    .register();
  public static BlockEntry<RotatedPillarBlock> RUNED_MANGROVE_LOG = REGISTRATE.block("runed_mangro_log", RotatedPillarBlock::new)
    .properties(RUNED_LOG_PROPERTIES)
    .blockstate((ctx, p) -> p.axisBlock(ctx.getEntry(), new ResourceLocation(RootsAPI.MODID, "block/runed_mangrove"), new ResourceLocation("minecraft", "block/mangrove_log_top")))
    .tag(BlockTags.MANGROVE_LOGS, RootsTags.Blocks.RUNED_MANGROVE_LOG, BlockTags.MINEABLE_WITH_AXE)
    .item()
    .model(ItemModelGenerator::itemModel)
    .build()
    .register();

  public static BlockEntry<RotatedPillarBlock> RUNED_WARPED_STEM = REGISTRATE.block("runed_warped_stem", RotatedPillarBlock::new)
    .properties(RUNED_STEM_PROPERTIES)
    .blockstate((ctx, p) -> p.axisBlock(ctx.getEntry(), new ResourceLocation(RootsAPI.MODID, "block/runed_warped"), new ResourceLocation("minecraft", "block/warped_stem_top")))
    .tag(BlockTags.WARPED_STEMS, RootsTags.Blocks.RUNED_WARPED_STEM, BlockTags.MINEABLE_WITH_AXE)
    .item()
    .model(ItemModelGenerator::itemModel)
    .build()
    .register();
  public static BlockEntry<RotatedPillarBlock> RUNED_CRIMSON_STEM = REGISTRATE.block("runed_crimson_stem", RotatedPillarBlock::new)
    .properties(RUNED_STEM_PROPERTIES)
    .blockstate((ctx, p) -> p.axisBlock(ctx.getEntry(), new ResourceLocation(RootsAPI.MODID, "block/runed_crimson"), new ResourceLocation("minecraft", "block/crimson_stem_top")))
    .tag(BlockTags.CRIMSON_STEMS, RootsTags.Blocks.RUNED_CRIMSON_STEM, BlockTags.MINEABLE_WITH_AXE)
    .item()
    .model(ItemModelGenerator::itemModel)
    .build()
    .register();


  // 1st STAIRS

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

  // SLaBS
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

  // FENCES

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


  // BUTTONS
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

  // PRESSURE PLATES

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

  // DOORS

  public static BlockEntry<BaseBlocks.DoorBlock> WILDWOOD_DOOR = REGISTRATE.block("wildwood_door", BaseBlocks.DoorBlock::new)
    .properties(WILDWOOD_PLANKS_PROPERTIES)
    .recipe((ctx, p) -> p.door(DataIngredient.items(ModBlocks.WILDWOOD_PLANKS), ModBlocks.WILDWOOD_DOOR, null))
    .loot((p, t) -> p.add(t, RegistrateBlockLootTables.createDoorTable(t)))
    .blockstate((ctx, p) -> p.doorBlock(ctx.getEntry(), "wildwood", p.modLoc("block/wildwood_door_bottom"), p.modLoc("block/wildwood_door_top")))
    .item()
    .model((ctx, p) -> p.generated(ctx::getEntry, p.modLoc("item/" + p.name(ctx::getEntry))))
    .build()
    .tag(BlockTags.DOORS, BlockTags.WOODEN_DOORS, BlockTags.MINEABLE_WITH_AXE)
    .register();

  // TRAPDOORS

  public static BlockEntry<BaseBlocks.TrapDoorBlock> WILDWOOD_TRAPDOOR = REGISTRATE.block("wildwood_trapdoor", BaseBlocks.TrapDoorBlock::new)
    .properties(WILDWOOD_PLANKS_PROPERTIES.andThen(o -> o.noOcclusion()))
    .recipe((ctx, p) -> p.trapDoor(DataIngredient.items(ModBlocks.WILDWOOD_PLANKS), ModBlocks.WILDWOOD_TRAPDOOR, null))
    .blockstate((ctx, p) -> p.trapdoorBlock(ctx.getEntry(), p.modLoc("block/wildwood_trapdoor"), true))
    .item()
    .model(ItemModelGenerator::generated)
    .build()
    .tag(BlockTags.TRAPDOORS, BlockTags.WOODEN_TRAPDOORS, BlockTags.MINEABLE_WITH_AXE)
    .register();

  // GATES

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
  public static BlockEntry<FenceGateBlock> WILDWOOD_GATE = REGISTRATE.block("wildwood_gate", FenceGateBlock::new)
    .properties(WILDWOOD_PLANKS_PROPERTIES)
    .recipe((ctx, p) -> p.fenceGate(DataIngredient.items(ModBlocks.WILDWOOD_PLANKS), ModBlocks.WILDWOOD_GATE, null))
    .blockstate(BlockstateGenerator.gate(WILDWOOD_PLANKS))
    .item()
    .model(ItemModelGenerator::itemModel)
    .build()
    .tag(BlockTags.FENCE_GATES, net.minecraftforge.common.Tags.Blocks.FENCE_GATES, BlockTags.UNSTABLE_BOTTOM_CENTER, net.minecraftforge.common.Tags.Blocks.FENCE_GATES_WOODEN, BlockTags.MINEABLE_WITH_AXE)
    .register();

  // WALLS

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

  // FUNCTIONAL BLOCKS BEGIN HERE




  public static NonNullUnaryOperator<BlockBehaviour.Properties> SOIL_PROPERTIES = r -> BlockBehaviour.Properties.copy(net.minecraft.world.level.block.Blocks.DIRT);

  public static BlockEntry<ElementalSoilBlock> AQUEOUS_SOIL = REGISTRATE.block("aqueous_soil", ElementalSoilBlock::new)
    .properties(SOIL_PROPERTIES)
    .blockstate(BlockstateGenerator.pillar("block/water_soil_side", "block/water_soil_top"))
    .item()
    .model(ItemModelGenerator::itemModel)
    .build()
    .tag(RootsTags.Blocks.WATER_SOIL, BlockTags.MINEABLE_WITH_SHOVEL, RootsTags.Blocks.NYI)
    .register();

  public static BlockEntry<ElementalSoilBlock> CAELIC_SOIL = REGISTRATE.block("caelic_soil", ElementalSoilBlock::new)
    .properties(SOIL_PROPERTIES)
    .blockstate(BlockstateGenerator.pillar("block/air_soil_side", "block/air_soil_top"))
    .item()
    .model(ItemModelGenerator::itemModel)
    .build()
    .tag(RootsTags.Blocks.AIR_SOIL, BlockTags.MINEABLE_WITH_SHOVEL, RootsTags.Blocks.NYI)
    .register();

  public static BlockEntry<ElementalSoilBlock> ELEMENTAL_SOIL = REGISTRATE.block("elemental_soil", ElementalSoilBlock::new)
    .properties(SOIL_PROPERTIES)
    .item()
    .model(ItemModelGenerator::itemModel)
    .build()
    .tag(RootsTags.Blocks.ELEMENTAL_SOIL, BlockTags.MINEABLE_WITH_SHOVEL)
    .register();

  public static BlockEntry<ElementalSoilBlock> MAGMATIC_SOIL = REGISTRATE.block("magmatic_soil", ElementalSoilBlock::new)
    .properties(SOIL_PROPERTIES)
    .blockstate(BlockstateGenerator.pillar("block/fire_soil_side", "block/fire_soil_top"))
    .item()
    .model(ItemModelGenerator::itemModel)
    .build()
    .tag(RootsTags.Blocks.FIRE_SOIL, BlockTags.MINEABLE_WITH_SHOVEL, RootsTags.Blocks.NYI)
    .register();

  public static BlockEntry<ElementalSoilBlock> TERRAN_SOIL = REGISTRATE.block("terran_soil", ElementalSoilBlock::new)
    .properties(SOIL_PROPERTIES)
    .blockstate(BlockstateGenerator.pillar("block/earth_soil_side", "block/earth_soil_top"))
    .item()
    .model(ItemModelGenerator::itemModel)
    .build()
    .tag(RootsTags.Blocks.EARTH_SOIL, BlockTags.MINEABLE_WITH_SHOVEL, RootsTags.Blocks.NYI)
    .register();

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
    .tag(RootsTags.Blocks.PEDESTALS, RootsTags.Blocks.RITUAL_PEDESTALS, BlockTags.MINEABLE_WITH_PICKAXE)
    .item()
    .model(ItemModelGenerator::complexItemModel)
    .build()
    .register();

  public static BlockEntry<RitualPedestalBlock> REINFORCED_RITUAL_PEDESTAL = REGISTRATE.block("reinforced_ritual_pedestal", Material.STONE, RitualPedestalBlock::new)
    .properties(BASE_REINFORCED_PROPERTIES)
    .blockstate(BlockstateGenerator.existingNoRotation("block/complex/reinforced_ritual_pedestal"))
    .tag(BlockTags.WITHER_IMMUNE, BlockTags.DRAGON_IMMUNE, RootsTags.Blocks.PEDESTALS, RootsTags.Blocks.RITUAL_PEDESTALS, BlockTags.MINEABLE_WITH_PICKAXE)
    .item()
    .model(ItemModelGenerator::complexItemModel)
    .build()
    .register();

  public static BlockEntry<GroveCrafterBlock> GROVE_CRAFTER = REGISTRATE.block("grove_crafter", Material.WOOD, GroveCrafterBlock::new)
    .properties(BASE_WOODEN_PROPERTIES)
    .blockstate(BlockstateGenerator.existingNoRotation("block/complex/grove_crafter"))
    .tag(RootsTags.Blocks.CRAFTERS, BlockTags.MINEABLE_WITH_AXE)
    .item()
    .model(ItemModelGenerator::complexItemModel)
    .build()
    .recipe((ctx, p) -> {
      ShapedRecipeBuilder.shaped(ctx.getEntry())
        .pattern("LLL")
        .pattern(" L ")
        .pattern("RRR")
        .define('L', Ingredient.of(ItemTags.LOGS))
        .define('R', Ingredient.of(RootsTags.Items.RUNESTONE))
        .unlockedBy("has_runestone", RegistrateRecipeProvider.has(RootsTags.Items.RUNESTONE))
        .save(p, new ResourceLocation(RootsAPI.MODID, "grove_crafter"));
    })
    .register();

  public static BlockEntry<GrovePedestalBlock> GROVE_PEDESTAL = REGISTRATE.block("grove_pedestal", Material.WOOD, GrovePedestalBlock::new)
    .properties(BASE_WOODEN_PROPERTIES)
    .blockstate(BlockstateGenerator.existingNoRotation("block/complex/grove_pedestal"))
    .tag(RootsTags.Blocks.PEDESTALS, RootsTags.Blocks.GROVE_PEDESTALS, RootsTags.Blocks.LIMITED_PEDESTALS, BlockTags.MINEABLE_WITH_AXE)
    .item()
    .model(ItemModelGenerator::complexItemModel)
    .build()
    .recipe((ctx, p) -> {
      ShapedRecipeBuilder.shaped(ctx.getEntry(), 4)
        .pattern("LLL")
        .pattern(" L ")
        .pattern("LLL")
        .define('L', Ingredient.of(ItemTags.LOGS))
        .unlockedBy("has_log", RegistrateRecipeProvider.has(ItemTags.LOGS))
        .save(p, new ResourceLocation(RootsAPI.MODID, "grove_pedestal"));
    })
    .register();

  public static BlockEntry<GrovePedestalBlock> WILDWOOD_PEDESTAL = REGISTRATE.block("wildwood_pedestal", Material.WOOD, GrovePedestalBlock::new)
    .properties(BASE_WOODEN_PROPERTIES)
    .blockstate(BlockstateGenerator.existingNoRotation("block/complex/wildwood_pedestal"))
    .tag(RootsTags.Blocks.PEDESTALS, RootsTags.Blocks.GROVE_PEDESTALS, BlockTags.MINEABLE_WITH_AXE)
    .item()
    .model(ItemModelGenerator::complexItemModel)
    .build()
    .recipe((ctx, p) -> {
      Ingredient WILDWOOD_LOG = Ingredient.of(RootsTags.Items.WILDWOOD_LOGS);
      GroveRecipe.builder(ctx.getEntry(), 5)
        .addIngredient(WILDWOOD_LOG)
        .addIngredient(WILDWOOD_LOG)
        .addIngredient(WILDWOOD_LOG)
        .addIngredient(WILDWOOD_LOG)
        .addIngredient(WILDWOOD_LOG)
        .addLevelCondition(ModConditions.GROVE_STONE_VALID.get())
        .unlockedBy("has_wildwood", p.has(RootsTags.Items.WILDWOOD_LOGS))
        .save(p, new ResourceLocation(RootsAPI.MODID, "grove/wildwood_pedestal"));
    })
    .register();

  public static BlockEntry<GrovePedestalBlock> DISPLAY_PEDESTAL = REGISTRATE.block("display_pedestal", Material.WOOD, GrovePedestalBlock::new)
    .properties(BASE_WOODEN_PROPERTIES)
    .blockstate(BlockstateGenerator.existingNoRotation("block/complex/grove_pedestal"))
    .tag(RootsTags.Blocks.PEDESTALS, RootsTags.Blocks.GROVE_PEDESTALS, RootsTags.Blocks.LIMITED_PEDESTALS, RootsTags.Blocks.DISPLAY_PEDESTALS, BlockTags.MINEABLE_WITH_AXE)
    .item()
    .model((ctx, p) -> p.withExistingParent(p.name(ctx::getEntry), new ResourceLocation(p.modid(ctx::getEntry), "block/complex/grove_pedestal")))
    .build()
    .recipe((ctx, p) -> {
      ShapelessRecipeBuilder.shapeless(ctx.getEntry())
        .requires(Ingredient.of(ModBlocks.GROVE_PEDESTAL.get()))
        .requires(Ingredient.of(RootsTags.Items.LEVERS))
        .unlockedBy("has_pedestal", RegistrateRecipeProvider.has(ModBlocks.GROVE_PEDESTAL.get()))
        .save(p, new ResourceLocation(RootsAPI.MODID, "display_pedestal"));
    })
    .register();

  public static BlockEntry<WildRootsBlock> WILD_ROOTS = REGISTRATE.block("wild_roots", Material.GRASS, WildRootsBlock::new)
    .properties(o -> BASE_WOODEN_PROPERTIES.apply(o).strength(0.2f))
    .blockstate(NonNullBiConsumer.noop())
    .loot((ctx, p) -> {
      ctx.add(p, LootTable.lootTable()
        .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1f)).add(RegistrateBlockLootTables.applyExplosionDecay(p, LootItem.lootTableItem(ModItems.WILDROOT.get()).apply(SetItemCountFunction.setCount(UniformGenerator.between(1f, 3f))))))
        .withPool(LootPool.lootPool().add(RegistrateBlockLootTables.applyExplosionDecay(p, LootItem.lootTableItem(ModItems.GROVE_SPORES.get()).apply(SetItemCountFunction.setCount(BinomialDistributionGenerator.binomial(2, 0.8f))).when(new LootItemBlockStatePropertyCondition.Builder(p).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(WildRootsBlock.MOSSY, true))))))
      );
    })
    .tag(BlockTags.MINEABLE_WITH_HOE)
    .register();

  public static BlockEntry<CreepingGroveMossBlock> CREEPING_GROVE_MOSS = REGISTRATE.block("creeping_grove_moss", Material.GRASS, CreepingGroveMossBlock::new)
    .properties(o -> BlockBehaviour.Properties.copy(Blocks.MOSS_CARPET))
    .loot((p, t) -> {
      p.add(t, RegistrateBlockLootTables.applyExplosionDecay(t, LootTable.lootTable().withPool(LootPool.lootPool().add(LootItem.lootTableItem(ModItems.GROVE_MOSS.get()).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1f))))).withPool(LootPool.lootPool().add(LootItem.lootTableItem(ModItems.GROVE_MOSS.get()).apply(SetItemCountFunction.setCount(BinomialDistributionGenerator.binomial(1, 0.2f))))).withPool(LootPool.lootPool().add(LootItem.lootTableItem(ModItems.GROVE_SPORES.get()).apply(SetItemCountFunction.setCount(BinomialDistributionGenerator.binomial(1, 0.05f)))))));
    })
    .blockstate((ctx, p) -> {
      p.simpleBlock(ctx.getEntry(), p.models().singleTexture(ctx.getName(), new ResourceLocation("minecraft", "block/carpet"), "wool", p.modLoc("block/creeping_grove_moss")));
    })
    .tag(BlockTags.MINEABLE_WITH_HOE)
    .register();

  public static BlockEntry<HangingRootsBlock> HANGING_GROVE_MOSS = REGISTRATE.block("hanging_grove_moss", Material.REPLACEABLE_PLANT, HangingRootsBlock::new)
    .properties(o -> BlockBehaviour.Properties.copy(Blocks.HANGING_ROOTS))
    .loot((p, t) -> {
      p.add(t, RegistrateBlockLootTables.applyExplosionDecay(t, LootTable.lootTable().withPool(LootPool.lootPool().add(LootItem.lootTableItem(ModItems.GROVE_MOSS.get()).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1f))))).withPool(LootPool.lootPool().add(LootItem.lootTableItem(ModItems.GROVE_SPORES.get()).apply(SetItemCountFunction.setCount(BinomialDistributionGenerator.binomial(1, 0.2f))))).withPool(LootPool.lootPool().add(LootItem.lootTableItem(ModItems.GROVE_SPORES.get()).apply(SetItemCountFunction.setCount(BinomialDistributionGenerator.binomial(1, 0.05f)))))));
    })
    .blockstate((ctx, p) -> {
      p.simpleBlock(ctx.getEntry(), p.models().cross(ctx.getName(), p.modLoc("block/hanging_grove_moss")));
    })
    .tag(BlockTags.MINEABLE_WITH_AXE)
    .register();
  public static BlockEntry<HugeMushroomBlock> BAFFLECAP_BLOCK = REGISTRATE.block("bafflecap_block", Material.WOOD, HugeMushroomBlock::new)
    .properties(o -> BlockBehaviour.Properties.copy(Blocks.BROWN_MUSHROOM_BLOCK))
    .blockstate((ctx, p) -> {
      ModelFile modelInside = p.models().withExistingParent("bafflecap_block_inside", new ResourceLocation("block/template_single_face")).texture("texture", p.modLoc("block/bafflecap_block_inside"));
      ModelFile modelOutside = p.models().withExistingParent("bafflecap_block_outside", new ResourceLocation("block/template_single_face")).texture("texture", p.modLoc("block/bafflecap_block_outside"));
      p.getMultipartBuilder(ctx.getEntry()).part().modelFile(modelInside).addModel().condition(HugeMushroomBlock.NORTH, false).end();
      p.getMultipartBuilder(ctx.getEntry()).part().modelFile(modelOutside).addModel().condition(HugeMushroomBlock.NORTH, true).end();
      p.getMultipartBuilder(ctx.getEntry()).part().modelFile(modelInside).uvLock(true).rotationY(180).addModel().condition(HugeMushroomBlock.SOUTH, false).end();
      p.getMultipartBuilder(ctx.getEntry()).part().modelFile(modelOutside).uvLock(true).rotationY(180).addModel().condition(HugeMushroomBlock.SOUTH, true).end();
      p.getMultipartBuilder(ctx.getEntry()).part().modelFile(modelInside).uvLock(true).rotationY(270).addModel().condition(HugeMushroomBlock.WEST, false).end();
      p.getMultipartBuilder(ctx.getEntry()).part().modelFile(modelOutside).uvLock(true).rotationY(270).addModel().condition(HugeMushroomBlock.WEST, true).end();
      p.getMultipartBuilder(ctx.getEntry()).part().modelFile(modelInside).uvLock(true).rotationY(90).addModel().condition(HugeMushroomBlock.EAST, false).end();
      p.getMultipartBuilder(ctx.getEntry()).part().modelFile(modelOutside).uvLock(true).rotationY(90).addModel().condition(HugeMushroomBlock.EAST, true).end();
      p.getMultipartBuilder(ctx.getEntry()).part().modelFile(modelInside).uvLock(true).rotationX(270).addModel().condition(HugeMushroomBlock.UP, false).end();
      p.getMultipartBuilder(ctx.getEntry()).part().modelFile(modelOutside).uvLock(true).rotationX(270).addModel().condition(HugeMushroomBlock.UP, true).end();
      p.getMultipartBuilder(ctx.getEntry()).part().modelFile(modelInside).uvLock(true).rotationX(90).addModel().condition(HugeMushroomBlock.DOWN, false).end();
      p.getMultipartBuilder(ctx.getEntry()).part().modelFile(modelOutside).uvLock(true).rotationX(90).addModel().condition(HugeMushroomBlock.DOWN, true).end();
    })
    .tag(BlockTags.MINEABLE_WITH_HOE)
    .loot((p, t) -> p.add(t, RegistrateBlockLootTables.createSilkTouchDispatchTable(t, RegistrateBlockLootTables.applyExplosionDecay(t, LootItem.lootTableItem(ModItems.BAFFLECAP.get()).apply(SetItemCountFunction.setCount(BinomialDistributionGenerator.binomial(3, 0.05f)))))))
    .register();

  public static <T extends Block> NonNullBiConsumer<DataGenContext<Block, T>, RegistrateBlockstateProvider> groveStone(String type) {
    return (ctx, p) -> p.getVariantBuilder(ctx.getEntry()).forAllStates(state -> {
      BlockModelBuilder model;
      boolean valid = state.getValue(StateProperties.GroveStone.VALID);

      model = switch (state.getValue(GroveStoneBlock.PART)) {
        case MIDDLE ->
          p.models().withExistingParent(type + "_grove_stone_middle" + (valid ? "_valid" : ""), new ResourceLocation(RootsAPI.MODID, "block/complex/grove_stone_middle"));
        case BOTTOM ->
          p.models().withExistingParent(type + "_grove_stone_bottom" + (valid ? "_valid" : ""), new ResourceLocation(RootsAPI.MODID, "block/complex/grove_stone_bottom"));
        default ->
          p.models().withExistingParent(type + "_grove_stone_top" + (valid ? "_valid" : ""), new ResourceLocation(RootsAPI.MODID, "block/complex/grove_stone_top"));
      };

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
    .tag(RootsTags.Blocks.GROVE_STONE_PRIMAL, BlockTags.MINEABLE_WITH_PICKAXE)
    .item()
    .model((ctx, p) -> p.blockWithInventoryModel(ctx::getEntry))
    .build()
    .recipe((ctx, p) -> {
      ShapedRecipeBuilder.shaped(ctx.getEntry())
        .pattern("RR")
        .pattern("RR")
        .pattern("RR")
        .define('R', Ingredient.of(RootsTags.Items.RUNESTONE))
        .unlockedBy("has_runestone", RegistrateRecipeProvider.has(RootsTags.Items.RUNESTONE))
        .save(p, new ResourceLocation(RootsAPI.MODID, "primal_grove_stone"));
    })
    .register();

  public static BlockEntry<IncenseBurnerBlock> INCENSE_BURNER = REGISTRATE.block("incense_burner", Material.STONE, IncenseBurnerBlock::new)
    .blockstate(BlockstateGenerator.existingNoRotation("block/complex/incense_burner"))
    .properties(BASE_PROPERTIES)
    .tag(RootsTags.Blocks.PEDESTALS, BlockTags.MINEABLE_WITH_PICKAXE, RootsTags.Blocks.NYI)
    .item()
    .model(ItemModelGenerator::complexItemModel)
    .build()
    .register();

  public static BlockEntry<MortarBlock> MORTAR = REGISTRATE.block("mortar", Material.STONE, MortarBlock::new)
    .properties(BASE_PROPERTIES)
    .blockstate(BlockstateGenerator.existingNoRotation("block/complex/mortar"))
    .tag(RootsTags.Blocks.MORTARS, BlockTags.MINEABLE_WITH_PICKAXE)
    .item()
    .model(ItemModelGenerator::complexItemModel)
    .build()
    .recipe((ctx, p) -> {
      ShapedRecipeBuilder.shaped(ctx.getEntry())
        .pattern("R R")
        .pattern("R R")
        .pattern("RRR")
        .define('R', Ingredient.of(RootsTags.Items.RUNESTONE))
        .unlockedBy("has_item", RegistrateRecipeProvider.has(RootsTags.Items.RUNESTONE))
        .save(p, new ResourceLocation(RootsAPI.MODID, "mortar"));
    })
    .register();

  public static BlockEntry<PyreBlock> PYRE = REGISTRATE.block("pyre", Material.WOOD, PyreBlock::new)
    .properties(BASE_WOODEN_PROPERTIES)
    .blockstate(BlockstateGenerator.existingNoRotation("block/complex/pyre"))
    .tag(RootsTags.Blocks.PYRES, BlockTags.MINEABLE_WITH_AXE)
    .item()
    .model(ItemModelGenerator::complexItemModel)
    .build()
    .recipe((ctx, p) -> {
      ShapedRecipeBuilder.shaped(ctx.getEntry())
        .pattern("LCL")
        .pattern("RRR")
        .define('L', Ingredient.of(ItemTags.LOGS))
        .define('C', Ingredient.of(ItemTags.COALS))
        .define('R', Ingredient.of(RootsTags.Items.RUNESTONE))
        .unlockedBy("has_item", RegistrateRecipeProvider.has(RootsTags.Items.RUNESTONE))
        .save(p, new ResourceLocation(RootsAPI.MODID, "pyre"));
    })
    .register();

  public static BlockEntry<PyreBlock> REINFORCED_PYRE = REGISTRATE.block("reinforced_pyre", Material.STONE, PyreBlock::new)
    .properties(BASE_REINFORCED_PROPERTIES)
    .blockstate(BlockstateGenerator.existingNoRotation("block/complex/reinforced_pyre"))
    .tag(BlockTags.WITHER_IMMUNE, BlockTags.DRAGON_IMMUNE, RootsTags.Blocks.PYRES, BlockTags.MINEABLE_WITH_PICKAXE, RootsTags.Blocks.NYI)
    .item()
    .model(ItemModelGenerator::complexItemModel)
    .build()
    .recipe((ctx, p) -> {
      ShapedRecipeBuilder.shaped(ctx.getEntry())
        .pattern("LCL")
        .pattern("RRR")
        .define('L', Ingredient.of(ItemTags.LOGS))
        .define('C', Ingredient.of(ItemTags.COALS))
        .define('R', Ingredient.of(RootsTags.Items.RUNED_OBSIDIAN))
        .unlockedBy("has_item", RegistrateRecipeProvider.has(RootsTags.Items.RUNESTONE))
        .save(p, new ResourceLocation(RootsAPI.MODID, "reinforced_pyre"));
      ShapedRecipeBuilder.shaped(ctx.getEntry())
        .pattern("RRR")
        .pattern("RPR")
        .pattern("RRR")
        .define('R', Ingredient.of(RootsTags.Items.RUNED_OBSIDIAN))
        .define('P', Ingredient.of(ModBlocks.PYRE.get()))
        .unlockedBy("has_item", RegistrateRecipeProvider.has(RootsTags.Items.RUNED_OBSIDIAN))
        .unlockedBy("has_item2", RegistrateRecipeProvider.has(ModBlocks.PYRE.get()))
        .save(p, new ResourceLocation(RootsAPI.MODID, "reinforced_pyre_from_pyre"));
    })
    .register();

  public static BlockEntry<DecorativePyreBlock> DECORATIVE_PYRE = REGISTRATE.block("decorative_pyre", Material.WOOD, DecorativePyreBlock::new)
    .properties(BASE_WOODEN_PROPERTIES.andThen(o -> o.lightLevel((state) -> 15)))
    .blockstate(BlockstateGenerator.existingNoRotation("block/complex/pyre"))
    .tag(RootsTags.Blocks.PYRES, BlockTags.MINEABLE_WITH_AXE, RootsTags.Blocks.NYI)
    .item()
    .model((ctx, p) -> p.withExistingParent(p.name(ctx::getEntry), new ResourceLocation(RootsAPI.MODID, "block/complex/pyre")))
    .build()
    .register();

  public static BlockEntry<UnendingBowlBlock> UNENDING_BOWL = REGISTRATE.block("unending_bowl", Material.STONE, UnendingBowlBlock::new)
    .properties(BASE_PROPERTIES)
    .blockstate(BlockstateGenerator.existingNoRotation("block/complex/unending_bowl"))
    .tag(BlockTags.MINEABLE_WITH_PICKAXE, RootsTags.Blocks.NYI)
    .item()
    .model(ItemModelGenerator::complexItemModel)
    .build()
    .register();

  // CROPS
  public static BlockEntry<MushroomBlock> BAFFLECAP = REGISTRATE.block("bafflecap", Material.WOOD, (o) -> new MushroomBlock(o, ModFeatures.HUGE_BAFFLECAP.getHolder()::get))
    .properties(o -> BlockBehaviour.Properties.copy(Blocks.BROWN_MUSHROOM))
    .lang("Bafflecap")
    .blockstate((ctx, p) -> {
      // TODO: this really shouldn't be like this
      ModelFile crop = p.models().getExistingFile(new ResourceLocation("minecraft", "block/cross"));
      p.getVariantBuilder(ctx.getEntry())
        .forAllStates(state -> {
          ModelFile stage = p.models().getBuilder("block/bafflecap")
            .parent(crop)
            .texture("cross", p.modLoc("block/bafflecap"));
          return ConfiguredModel.builder().modelFile(stage).build();
        });
    })
    .tag(BlockTags.MINEABLE_WITH_HOE)
    .register();
  public static BlockEntry<ThreeStageCropBlock> WILDROOT_CROP = REGISTRATE.block("wildroot_crop", (p) -> new ThreeStageCropBlock(p, () -> ModItems.WILDROOT))
    .properties(CROP_PROPERTIES)
    .blockstate(BlockstateGenerator::cropBlockstate)
    .tag(RootsTags.Blocks.WILDROOT_CROP, BlockTags.MINEABLE_WITH_HOE)
    .loot(seedlessCropLoot(ThreeStageCropBlock.AGE, () -> ModItems.WILDROOT.get()))
    .register();

  public static BlockEntry<ElementalCropBlock> CLOUD_BERRY_CROP = REGISTRATE.block("cloud_berry_crop", (p) -> new ElementalCropBlock(p, () -> ModItems.CLOUD_BERRY))
    .properties(CROP_PROPERTIES)
    .blockstate(BlockstateGenerator::cropBlockstate)
    .tag(RootsTags.Blocks.CLOUD_BERRY_CROP, BlockTags.MINEABLE_WITH_HOE)
    .loot(seedlessCropLoot(ThreeStageCropBlock.AGE, () -> ModItems.CLOUD_BERRY.get()))
    .register();

  public static BlockEntry<WaterElementalCropBlock> DEWGONIA_CROP = REGISTRATE.block("dewgonia_crop", (p) -> new WaterElementalCropBlock(p, () -> ModItems.DEWGONIA))
    .properties(CROP_PROPERTIES)
    .blockstate(BlockstateGenerator::cropBlockstate)
    .tag(RootsTags.Blocks.DEWGONIA_CROP, BlockTags.MINEABLE_WITH_HOE)
    .loot(seedlessCropLoot(ThreeStageCropBlock.AGE, () -> ModItems.DEWGONIA.get()))
    .register();

  public static BlockEntry<ElementalCropBlock> INFERNO_BULB_CROP = REGISTRATE.block("inferno_bulb_crop", (p) -> new ElementalCropBlock(p, () -> ModItems.INFERNO_BULB))
    .properties(CROP_PROPERTIES)
    .blockstate(BlockstateGenerator::crossBlockstate)
    .tag(RootsTags.Blocks.INFERNO_BULB_CROP, BlockTags.MINEABLE_WITH_HOE)
    .loot(seedlessCropLoot(ElementalCropBlock.AGE, () -> ModItems.INFERNO_BULB.get()))
    .register();

  public static BlockEntry<ElementalCropBlock> STALICRIPE_CROP = REGISTRATE.block("stalicripe_crop", (p) -> new ElementalCropBlock(p, () -> ModItems.STALICRIPE))
    .properties(CROP_PROPERTIES)
    .blockstate(BlockstateGenerator::cropBlockstate)
    .tag(RootsTags.Blocks.STALICRIPE_CROP, BlockTags.MINEABLE_WITH_HOE)
    .loot(seedlessCropLoot(ElementalCropBlock.AGE, () -> ModItems.STALICRIPE.get()))
    .register();

  public static BlockEntry<SeededCropsBlock> MOONGLOW_CROP = REGISTRATE.block("moonglow_crop", (p) -> new SeededCropsBlock(p, () -> ModItems.MOONGLOW_SEEDS))
    .properties(CROP_PROPERTIES)
    .blockstate(BlockstateGenerator::cropBlockstate)
    .tag(RootsTags.Blocks.MOONGLOW_CROP, BlockTags.MINEABLE_WITH_HOE)
    .loot(cropLoot(SeededCropsBlock.AGE, () -> ModItems.MOONGLOW_SEEDS.get(), () -> ModItems.MOONGLOW.get()))
    .register();
  public static BlockEntry<SeededCropsBlock> PERESKIA_CROP = REGISTRATE.block("pereskia_crop", (p) -> new SeededCropsBlock(p, () -> ModItems.PERESKIA_BULB))
    .properties(CROP_PROPERTIES)
    .blockstate(BlockstateGenerator::crossBlockstate)
    .tag(RootsTags.Blocks.PERESKIA_CROP, BlockTags.MINEABLE_WITH_HOE)
    .loot(cropLoot(SeededCropsBlock.AGE, () -> ModItems.PERESKIA_BULB.get(), () -> ModItems.PERESKIA.get()))
    .register();
  // TODO: Pottable pereskia?
  public static BlockEntry<ThreeStageCropBlock> SPIRITLEAF_CROP = REGISTRATE.block("spiritleaf_crop", (p) -> new ThreeStageCropBlock(p, () -> ModItems.SPIRITLEAF_SEEDS))
    .properties(CROP_PROPERTIES)
    .blockstate(BlockstateGenerator::cropBlockstate)
    .tag(RootsTags.Blocks.SPIRITLEAF_CROP, BlockTags.MINEABLE_WITH_HOE)
    .loot(cropLoot(ThreeStageCropBlock.AGE, () -> ModItems.SPIRITLEAF_SEEDS.get(), () -> ModItems.SPIRITLEAF.get()))
    .register();
  public static BlockEntry<SeededCropsBlock> WILDEWHEET_CROP = REGISTRATE.block("wildewheet_crop", (p) -> new SeededCropsBlock(p, () -> ModItems.WILDEWHEET_SEEDS))
    .properties(CROP_PROPERTIES)
    .blockstate(BlockstateGenerator::cropBlockstate)
    .tag(RootsTags.Blocks.WILDEWHEET_CROP, BlockTags.MINEABLE_WITH_HOE)
    .loot(cropLoot(SeededCropsBlock.AGE, () -> ModItems.WILDEWHEET_SEEDS.get(), () -> ModItems.WILDEWHEET.get()))
    .register();

  public static BlockEntry<SeededCropsBlock> AUBERGINE_CROP = REGISTRATE.block("aubergine_crop", (b) -> new SeededCropsBlock(b, () -> ModItems.AUBERGINE_SEEDS.get()::asItem))
    .properties(CROP_PROPERTIES)
    .loot(cropLoot(SeededCropsBlock.AGE, () -> ModItems.AUBERGINE_SEEDS.get(),() ->  ModItems.AUBERGINE.get()))
    .blockstate(BlockstateGenerator::cropBlockstate)
    .tag(BlockTags.CROPS, BlockTags.MINEABLE_WITH_HOE)
    .register();

  public static BlockEntry<BaseBlocks.WildCropBlock> WILD_AUBERGINE = REGISTRATE.block("wild_aubergine", (b) -> new BaseBlocks.WildCropBlock(b, RootsTags.Blocks.SUPPORTS_WILD_AUBERGINE))
    .properties(o -> Block.Properties.of(Material.PLANT).noCollission().strength(0f).sound(SoundType.CROP).randomTicks())
    .loot((p, t) -> p.add(t, RegistrateBlockLootTables.applyExplosionDecay(t, LootTable.lootTable().withPool(LootPool.lootPool().add(LootItem.lootTableItem(ModItems.AUBERGINE_SEEDS.get()).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1f)))))
      .withPool(LootPool.lootPool().add(LootItem.lootTableItem(ModItems.AUBERGINE.get()).apply(SetItemCountFunction.setCount(ConstantValue.exactly(1f)))))
      .withPool(LootPool.lootPool().add(LootItem.lootTableItem(ModItems.AUBERGINE.get()).apply(SetItemCountFunction.setCount(BinomialDistributionGenerator.binomial(2, 0.3f))))))))
    .blockstate((ctx, p) ->
      p.getVariantBuilder(ctx.getEntry())
        .partialState()
        .addModels(new ConfiguredModel(p.models().crop(ctx.getName(), p.blockTexture(ctx.getEntry()))))
    )
    .tag(BlockTags.CROPS, BlockTags.MINEABLE_WITH_HOE)
    .register();

  // POTS

  public static BlockEntry<FlowerPotBlock> POTTED_BAFFLECAP = REGISTRATE.block("potted_bafflecap", Material.DECORATION, (p) -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, ModBlocks.BAFFLECAP, BlockBehaviour.Properties.copy(Blocks.OAK_SAPLING)))
    .blockstate((ctx, p) -> p.simpleBlock(ctx.getEntry(), p.models().withExistingParent(ctx.getName(), "minecraft:block/flower_pot_cross").texture("plant", new ResourceLocation(RootsAPI.MODID, "block/bafflecap"))))
    .loot((ctx, p) -> ctx.add(p, RegistrateBlockLootTables.createPotFlowerItemTable(ModBlocks.BAFFLECAP.get())))
    .tag(BlockTags.FLOWER_POTS)
    .register();
  public static BlockEntry<FlowerPotBlock> POTTED_STONEPETAL = REGISTRATE.block("potted_stonepetal", Material.DECORATION, (p) -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, ModBlocks.STONEPETAL, BlockBehaviour.Properties.copy(Blocks.OAK_SAPLING)))
    .blockstate((ctx, p) -> p.simpleBlock(ctx.getEntry(), p.models().withExistingParent(ctx.getName(), "minecraft:block/flower_pot_cross").texture("plant", new ResourceLocation(RootsAPI.MODID, "block/stonepetal"))))
    .loot((ctx, p) -> ctx.add(p, RegistrateBlockLootTables.createPotFlowerItemTable(ModBlocks.STONEPETAL.get())))
    .tag(BlockTags.FLOWER_POTS)
    .register();

  public static BlockEntry<FlowerPotBlock> POTTED_WILDWOOD_SAPLING = REGISTRATE.block("potted_wildwood_spaling", Material.DECORATION, (p) -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, ModBlocks.WILDWOOD_SAPLING, BlockBehaviour.Properties.copy(Blocks.OAK_SAPLING)))
    .blockstate((ctx, p) -> p.simpleBlock(ctx.getEntry(), p.models().withExistingParent(ctx.getName(), "minecraft:block/flower_pot_cross").texture("plant", new ResourceLocation(RootsAPI.MODID, "block/wildwood_sapling"))))
    .loot((ctx, p) -> ctx.add(p, RegistrateBlockLootTables.createPotFlowerItemTable(ModBlocks.WILDWOOD_SAPLING.get())))
    .tag(BlockTags.FLOWER_POTS)
    .register();


  public static void load() {
  }
}
