package mysticmods.roots.init;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateItemModelProvider;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import mysticmods.roots.Roots;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.recipe.WorldRecipe;
import mysticmods.roots.api.registry.Registries;
import mysticmods.roots.item.*;
import mysticmods.roots.item.copper.CopperArmorItem;
import mysticmods.roots.item.living.*;
import mysticmods.roots.recipe.bark.BarkRecipe;
import mysticmods.roots.recipe.bark.DynamicBarkRecipe;
import mysticmods.roots.recipe.grove.GroveRecipe;
import mysticmods.roots.recipe.mortar.MortarRecipe;
import mysticmods.roots.recipe.runic.RunicBlockRecipe;
import mysticmods.roots.recipe.runic.RunicEntityRecipe;
import mysticmods.roots.test.block.BlockPropertyMatchTest;
import mysticmods.roots.test.entity.EntityTagTest;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.BeetrootBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.common.Tags;
import noobanidus.libs.noobutil.data.generator.ItemGenerator;
import noobanidus.libs.noobutil.ingredient.ExcludingIngredient;
import noobanidus.libs.noobutil.item.BaseItems;

import java.util.function.Supplier;

import static mysticmods.roots.Roots.RECIPES;
import static mysticmods.roots.Roots.REGISTRATE;

public class ModItems {
  private static <T extends Item> NonNullBiConsumer<DataGenContext<Item, T>, RegistrateItemModelProvider> subfolder(String subfolder) {
    return (ctx, p) -> p.generated(ctx::getEntry, new ResourceLocation(RootsAPI.MODID, "item/" + subfolder + "/" + ctx.getName()));
  }

  // GATHERED CROPS
  public static final ItemEntry<ItemNameBlockItem> WILDROOT = REGISTRATE.item("wildroot", (p) -> new ItemNameBlockItem(ModBlocks.WILDROOT_CROP.get(), p))
    .model(subfolder("herbs"))
    .tag(RootsTags.Items.WILDROOT_SEEDS, RootsTags.Items.WILDROOT_CROP)
    .defaultLang()
    .register();
  public static final ItemEntry<Item> GROVE_MOSS = REGISTRATE.item("grove_moss", Item::new)
    .model(subfolder("herbs"))
    .tag(RootsTags.Items.GROVE_MOSS_CROP)
    .register();

  // PYRE-CRAFTED CROPS
  public static final ItemEntry<ItemNameBlockItem> CLOUD_BERRY = REGISTRATE.item("cloud_berry", (p) -> new ItemNameBlockItem(ModBlocks.CLOUD_BERRY_CROP.get(), p))
    .model(subfolder("herbs"))
    .tag(RootsTags.Items.CLOUD_BERRY_SEEDS, RootsTags.Items.CLOUD_BERRY_CROP)
    .register();
  public static final ItemEntry<ItemNameBlockItem> DEWGONIA = REGISTRATE.item("dewgonia", (p) -> new ItemNameBlockItem(ModBlocks.DEWGONIA_CROP.get(), p))
    .model(subfolder("herbs"))
    .tag(RootsTags.Items.DEWGONIA_SEEDS, RootsTags.Items.DEWGONIA_CROP)
    .register();
  public static final ItemEntry<ItemNameBlockItem> INFERNO_BULB = REGISTRATE.item("inferno_bulb", (p) -> new ItemNameBlockItem(ModBlocks.INFERNO_BULB_CROP.get(), p))
    .model(subfolder("herbs"))
    .tag(RootsTags.Items.INFERNO_BULB_SEEDS, RootsTags.Items.INFERNO_BULB_CROP)
    .register();
  public static final ItemEntry<ItemNameBlockItem> STALICRIPE = REGISTRATE.item("stalicripe", (p) -> new ItemNameBlockItem(ModBlocks.STALICRIPE_CROP.get(), p))
    .model(subfolder("herbs"))
    .tag(RootsTags.Items.STALICRIPE_SEEDS, RootsTags.Items.STALICRIPE_CROP)
    .register();
  // OTHER SOURCE CROPS

  // RUNIC SHEARS -> MUSHROOM
  public static final ItemEntry<ItemNameBlockItem> BAFFLECAP = REGISTRATE.item("bafflecap", (p) -> new ItemNameBlockItem(ModBlocks.BAFFLECAP.get(), p))
    .model(subfolder("herbs"))
    .tag(RootsTags.Items.BAFFLECAP_CROP)
    .recipe((ctx, p) -> {
      RunicBlockRecipe.builder(ctx.getEntry())
        .durabilityCost(15)
        .setCondition(new WorldRecipe.Condition(new TagMatchTest(RootsTags.Blocks.BAFFLECAP_CONVERSION)))
        .setOutputState(Blocks.AIR.defaultBlockState())
        .unlockedBy("has_runic_shears", p.has(RootsTags.Items.RUNIC_SHEARS))
        .save(p, new ResourceLocation(RootsAPI.MODID, "runic/block/bafflecap_from_mushroom"));
    })
    .register();
  public static final ItemEntry<Item> MOONGLOW = REGISTRATE.item("moonglow", Item::new)
    .model(subfolder("herbs"))
    .tag(RootsTags.Items.MOONGLOW_CROP)
    .register();

  // RUNIC SHEARS -> FLOWER -> BULB
  public static final ItemEntry<Item> PERESKIA = REGISTRATE.item("pereskia", Item::new)
    .model(subfolder("herbs"))
    .tag(RootsTags.Items.PERESKIA_CROP)
    .register();

  // RUNIC SHEARS -> BEETROOT -> SPIRITLEAF SEEDS
  public static final ItemEntry<Item> SPIRITLEAF = REGISTRATE.item("spiritleaf", Item::new)
    .model(subfolder("herbs"))
    .tag(RootsTags.Items.SPIRITLEAF_CROP)
    .register();

  // RUNIC SHEARS -> WHEAT -> WILDEWHEET SEEDS
  public static final ItemEntry<Item> WILDEWHEET = REGISTRATE.item("wildewheet", Item::new)
    .model(subfolder("herbs"))
    .tag(RootsTags.Items.WILDEWHEET_CROP)
    .register();

  public static final ItemEntry<ItemNameBlockItem> MOONGLOW_SEEDS = REGISTRATE.item("moonglow_seeds", (p) -> new ItemNameBlockItem(ModBlocks.MOONGLOW_CROP.get(), p))
    .model(subfolder("herbs"))
    .tag(RootsTags.Items.MOONGLOW_SEEDS)
    .register();
  public static final ItemEntry<ItemNameBlockItem> PERESKIA_BULB = REGISTRATE.item("pereskia_bulb", (p) -> new ItemNameBlockItem(ModBlocks.PERESKIA_CROP.get(), p))
    .model(subfolder("herbs"))
    .tag(RootsTags.Items.PERESKIA_SEEDS)
    .register();
  public static final ItemEntry<ItemNameBlockItem> SPIRITLEAF_SEEDS = REGISTRATE.item("spiritleaf_seeds", (p) -> new ItemNameBlockItem(ModBlocks.SPIRITLEAF_CROP.get(), p))
    .model(subfolder("herbs"))
    .tag(RootsTags.Items.SPIRITLEAF_SEEDS)
    .recipe((ctx, p) -> {
      RunicBlockRecipe.builder(ctx.getEntry())
        .durabilityCost(15)
        .skipProperty(BeetrootBlock.AGE)
        .setCondition(new WorldRecipe.Condition(new BlockPropertyMatchTest(Blocks.BEETROOTS.defaultBlockState().setValue(BeetrootBlock.AGE, BeetrootBlock.MAX_AGE), BeetrootBlock.AGE)))
        .setOutputState(Blocks.BEETROOTS.defaultBlockState().setValue(BeetrootBlock.AGE, 0))
        .unlockedBy("has_shears", p.has(RootsTags.Items.RUNIC_SHEARS))
        .save(p, new ResourceLocation(RootsAPI.MODID, "runic/block/spiritleaf_seeds"));
    })
    .register();
  public static final ItemEntry<ItemNameBlockItem> WILDEWHEET_SEEDS = REGISTRATE.item("wildewheet_seeds", (p) -> new ItemNameBlockItem(ModBlocks.WILDEWHEET_CROP.get(), p))
    .model(subfolder("herbs"))
    .tag(RootsTags.Items.WILDEWHEET_SEEDS)
    .register();

  public static final ItemEntry<GroveSporesItem> GROVE_SPORES = REGISTRATE.item("grove_spores", GroveSporesItem::new)
    .model(subfolder("herbs"))
    .tag(RootsTags.Items.SEEDS)
    .recipe((ctx, p) -> {
      MortarRecipe.multiBuilder(10)
        .addIngredient(ItemTags.DIRT)
        .addChanceOutput(new ItemStack(ctx.getEntry(), 1), 0.1f)
        .unlockedBy("has_item", p.has(ItemTags.DIRT))
        .save(p, new ResourceLocation(RootsAPI.MODID, "mortar/grove_spores_from_dirt"));
    })
    .register();

  public static RegistryEntry<Item> CARAPACE = REGISTRATE.item("carapace", Item::new)
    .recipe((ctx, p) -> RECIPES.dye(ModItems.CARAPACE, () -> Items.BLUE_DYE, 1, 2, p))
    .tag(RootsTags.Items.CARAPACE)
    .register();

  public static RegistryEntry<Item> PELT = REGISTRATE.item("pelt", Item::new)
    .recipe((ctx, p) -> RECIPES.singleItemUnfinished(ModItems.PELT, () -> Items.LEATHER, 1, 1).save(p, new ResourceLocation(RootsAPI.MODID, "pelt_to_leather")))
    .register();

  public static RegistryEntry<Item> ANTLERS = REGISTRATE.item("antlers", Item::new)
    .recipe((ctx, p) -> RECIPES.singleItemUnfinished(ModItems.ANTLERS, () -> Items.BONE_MEAL, 1, 9).save(p, new ResourceLocation(RootsAPI.MODID, "antlers_to_bonemeal")))
    .register();

  public static ItemEntry<Item> VENISON = REGISTRATE.item("venison", Item::new)
    .properties(o -> o.food(ModFoods.VENISON))
    .tag(RootsTags.Items.PROTEINS)
    .recipe((ctx, p) -> {
      RECIPES.food(ModItems.VENISON, ModItems.COOKED_VENISON, 0.15f, p);
      RECIPES.food(Tags.Items.CROPS_CARROT, ModItems.COOKED_CARROT, 0.15f, p);
      RECIPES.food(Tags.Items.CROPS_BEETROOT, ModItems.COOKED_BEETROOT, 0.15f, p);
      RECIPES.food(ModItems.ASSORTED_SEEDS, ModItems.COOKED_SEEDS, 0.05f, p);
      RECIPES.food(RootsTags.Items.AUBERGINE_CROP, ModItems.COOKED_AUBERGINE, 0.15f, p);
      RECIPES.food(ModItems.RAW_SQUID, ModItems.COOKED_SQUID, 0.15f, p);
    })
    .register();


  public static ItemEntry<Item> COOKED_VENISON = REGISTRATE.item("cooked_venison", Item::new)
    .tag(RootsTags.Items.PROTEINS)
    .properties(o -> o.food(ModFoods.COOKED_VENISON))
    .register();

  public static ItemEntry<Item> RAW_SQUID = REGISTRATE.item("raw_squid", Item::new)
    .tag(RootsTags.Items.PROTEINS)
    .properties(o -> o.food(ModFoods.RAW_SQUID))
    .register();

  public static ItemEntry<Item> COOKED_SQUID = REGISTRATE.item("cooked_squid", Item::new)
    .tag(RootsTags.Items.PROTEINS, RootsTags.Items.COOKED_SEAFOOD)
    .properties(o -> o.food(ModFoods.COOKED_SQUID))
    .register();

  public static ItemEntry<Item> ASSORTED_SEEDS = REGISTRATE.item("assorted_seeds", Item::new)
    .register();

  public static ItemEntry<BaseItems.FastFoodItem> COOKED_SEEDS = REGISTRATE.item("cooked_seeds", BaseItems.FastFoodItem::new)
    .properties(o -> o.food(ModFoods.COOKED_SEEDS))
    .register();

  public static ItemEntry<Item> COOKED_BEETROOT = REGISTRATE.item("cooked_beetroot", Item::new)
    .tag(RootsTags.Items.COOKED_VEGETABLES)
    .properties(o -> o.food(ModFoods.COOKED_BEETROOT))
    .register();

  public static ItemEntry<Item> COOKED_CARROT = REGISTRATE.item("cooked_carrot", Item::new)
    .tag(RootsTags.Items.COOKED_VEGETABLES)
    .properties(o -> o.food(ModFoods.COOKED_CARROT))
    .register();

  public static ItemEntry<Item> AUBERGINE = REGISTRATE.item("aubergine", Item::new)
    .properties(o -> o.food(ModFoods.AUBERGINE))
    .tag(RootsTags.Items.AUBERGINE_CROP, RootsTags.Items.VEGETABLES)
    .register();

  public static ItemEntry<Item> COOKED_AUBERGINE = REGISTRATE.item("cooked_aubergine", Item::new)
    .tag(RootsTags.Items.COOKED_VEGETABLES)
    .properties(o -> o.food(ModFoods.COOKED_AUBERGINE))
    .register();

  public static ItemEntry<Item> STUFFED_AUBERGINE = REGISTRATE.item("stuffed_aubergine", Item::new)
    .properties(o -> o.food(ModFoods.STUFFED_AUBERGINE))
    .recipe((ctx, p) -> ShapelessRecipeBuilder.shapeless(ModItems.STUFFED_AUBERGINE.get(), 1).requires(ModItems.COOKED_AUBERGINE.get()).requires(ExcludingIngredient.create(RootsTags.Items.VEGETABLES, ModItems.AUBERGINE.get())).requires(ExcludingIngredient.create(RootsTags.Items.VEGETABLES, ModItems.AUBERGINE.get())).requires(ExcludingIngredient.create(RootsTags.Items.COOKED_VEGETABLES, ModItems.COOKED_AUBERGINE.get())).unlockedBy("has_cooked_aubergine", RegistrateRecipeProvider.has(ModItems.COOKED_AUBERGINE.get())).save(p))
    .register();

  // Salads
  public static ItemEntry<BaseItems.BowlItem> AUBERGINE_SALAD = REGISTRATE.item("aubergine_salad", BaseItems.BowlItem::new)
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
    .register();

  public static ItemEntry<BaseItems.BowlItem> BEETROOT_SALAD = REGISTRATE.item("beetroot_salad", BaseItems.BowlItem::new)
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
    .register();

  public static ItemEntry<BaseItems.BowlItem> CACTUS_DANDELION_SALAD = REGISTRATE.item("cactus_dandelion_salad", BaseItems.BowlItem::new)
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
    .register();

  public static ItemEntry<BaseItems.BowlItem> DANDELION_CORNFLOWER_SALAD = REGISTRATE.item("dandelion_cornflower_salad", BaseItems.BowlItem::new)
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
    .register();

  public static ItemEntry<BaseItems.BowlItem> STEWED_EGGPLANT = REGISTRATE.item("stewed_eggplant", BaseItems.BowlItem::new)
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
    .register();

  public static NonNullFunction<Item.Properties, TooltipDrinkItem> tooltipDrink(String translationKey) {
    return (b) -> new TooltipDrinkItem(b, translationKey);
  }

  // Drinkies
  public static ItemEntry<TooltipDrinkItem> APPLE_CORDIAL = REGISTRATE.item("apple_cordial", tooltipDrink("roots.drinks.slow_regen"))
    .properties(o -> o.food(ModFoods.APPLE_CORDIAL).craftRemainder(Items.GLASS_BOTTLE))
    .recipe(RECIPES.cordial(() -> ModItems.APPLE_CORDIAL, Items.APPLE))
    .register();

  public static ItemEntry<TooltipDrinkItem> CACTUS_SYRUP = REGISTRATE.item("cactus_syrup", tooltipDrink("roots.drinks.slow_regen"))
    .properties(o -> o.food(ModFoods.CACTUS_SYRUP).craftRemainder(Items.GLASS_BOTTLE))
    .recipe(RECIPES.cordial(() -> ModItems.CACTUS_SYRUP, Items.CACTUS))
    .register();

  public static ItemEntry<TooltipDrinkItem> DANDELION_CORDIAL = REGISTRATE.item("dandelion_cordial", tooltipDrink("roots.drinks.wakefulness"))
    .properties(o -> o.food(ModFoods.DANDELION_CORDIAL).craftRemainder(Items.GLASS_BOTTLE))
    .recipe(RECIPES.cordial(() -> ModItems.DANDELION_CORDIAL, Items.DANDELION))
    .register();

  public static ItemEntry<TooltipDrinkItem> LILAC_CORDIAL = REGISTRATE.item("lilac_cordial", tooltipDrink("roots.drinks.slow_regen"))
    .properties(o -> o.food(ModFoods.LILAC_CORDIAL).craftRemainder(Items.GLASS_BOTTLE))
    .recipe(RECIPES.cordial(() -> ModItems.LILAC_CORDIAL, Items.LILAC))
    .register();

  public static ItemEntry<TooltipDrinkItem> PEONY_CORDIAL = REGISTRATE.item("peony_cordial", tooltipDrink("roots.drinks.slow_regen"))
    .properties(o -> o.food(ModFoods.PEONY_CORDIAL).craftRemainder(Items.GLASS_BOTTLE))
    .recipe(RECIPES.cordial(() -> ModItems.PEONY_CORDIAL, Items.PEONY))
    .register();

  public static ItemEntry<TooltipDrinkItem> ROSE_CORDIAL = REGISTRATE.item("rose_cordial", tooltipDrink("roots.drinks.slow_regen"))
    .properties(o -> o.food(ModFoods.ROSE_CORDIAL).craftRemainder(Items.GLASS_BOTTLE))
    .recipe(RECIPES.cordial(() -> ModItems.ROSE_CORDIAL, Items.ROSE_BUSH))
    .register();

  public static ItemEntry<TooltipDrinkItem> VINEGAR = REGISTRATE.item("vinegar", tooltipDrink("roots.drinks.sour"))
    .properties(o -> o.food(ModFoods.VINEGAR).craftRemainder(Items.GLASS_BOTTLE))
    .recipe((ctx, p) -> ShapedRecipeBuilder.shaped(ModItems.VINEGAR.get(), 6)
      .pattern("BBB")
      .pattern("PPP")
      .pattern("BBB")
      .define('P', Items.SEA_PICKLE)
      .define('B', Items.GLASS_BOTTLE)
      .unlockedBy("has_sea_pickle", RegistrateRecipeProvider.has(Items.SEA_PICKLE))
      .save(p))
    .register();

  public static ItemEntry<TooltipDrinkItem> VEGETABLE_JUICE = REGISTRATE.item("vegetable_juice", tooltipDrink("roots.drinks.slow_regen"))
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
    .register();

  public static ItemEntry<Item> INK_BOTTLE = REGISTRATE.item("ink_bottle", Item::new)
    .properties(o -> o.craftRemainder(Items.GLASS_BOTTLE))
    .recipe((ctx, p) -> RECIPES.dye(ModItems.INK_BOTTLE, () -> Items.BLACK_DYE, 1, 2, p))
    .register();

  public static <T extends Block> NonNullFunction<Item.Properties, ItemNameBlockItem> blockNamedItem(Supplier<Supplier<T>> block) {
    return (b) -> new ItemNameBlockItem(block.get().get(), b);
  }

  public static final ItemEntry<ItemNameBlockItem> AUBERGINE_SEEDS = REGISTRATE.item("aubergine_seeds", blockNamedItem(() -> ModBlocks.AUBERGINE_CROP))
    .tag(RootsTags.Items.SEEDS)
    .register();

  public static final ItemEntry<Item> ACACIA_BARK = REGISTRATE.item("acacia_bark", Item::new)
    .tag(RootsTags.Items.ACACIA_BARK)
    .model(subfolder("bark"))
    .recipe((ctx, p) -> {
      BarkRecipe.builder(ctx.getEntry(), 2)
        .setOutputState(Blocks.STRIPPED_ACACIA_LOG.defaultBlockState())
        .setCondition(new WorldRecipe.Condition(new BlockMatchTest(Blocks.ACACIA_LOG)))
        .unlockedBy("has_knife", p.has(RootsTags.Items.KNIVES))
        .save(p, new ResourceLocation(RootsAPI.MODID, "bark/acacia_log_stripping"));
      BarkRecipe.builder(ctx.getEntry(), 3)
        .setOutputState(Blocks.STRIPPED_ACACIA_WOOD.defaultBlockState())
        .setCondition(new WorldRecipe.Condition(new BlockMatchTest(Blocks.ACACIA_WOOD)))
        .unlockedBy("has_knife", p.has(RootsTags.Items.KNIVES))
        .save(p, new ResourceLocation(RootsAPI.MODID, "bark/acacia_wood_stripping"));
    })
    .register();

  public static final ItemEntry<Item> BIRCH_BARK = REGISTRATE.item("birch_bark", Item::new)
    .tag(RootsTags.Items.BIRCH_BARK)
    .model(subfolder("bark"))
    .recipe((ctx, p) -> {
      BarkRecipe.builder(ctx.getEntry(), 2)
        .setOutputState(Blocks.STRIPPED_BIRCH_LOG.defaultBlockState())
        .setCondition(new WorldRecipe.Condition(new BlockMatchTest(Blocks.BIRCH_LOG)))
        .unlockedBy("has_knife", p.has(RootsTags.Items.KNIVES))
        .save(p, new ResourceLocation(RootsAPI.MODID, "bark/birch_log_stripping"));
      BarkRecipe.builder(ctx.getEntry(), 3)
        .setOutputState(Blocks.STRIPPED_BIRCH_WOOD.defaultBlockState())
        .setCondition(new WorldRecipe.Condition(new BlockMatchTest(Blocks.BIRCH_WOOD)))
        .unlockedBy("has_knife", p.has(RootsTags.Items.KNIVES))
        .save(p, new ResourceLocation(RootsAPI.MODID, "bark/birch_wood_stripping"));
    })
    .register();

  public static final ItemEntry<Item> DARK_OAK_BARK = REGISTRATE.item("dark_oak_bark", Item::new)
    .tag(RootsTags.Items.DARK_OAK_BARK)
    .model(subfolder("bark"))
    .recipe((ctx, p) -> {
      BarkRecipe.builder(ctx.getEntry(), 2)
        .setOutputState(Blocks.STRIPPED_DARK_OAK_LOG.defaultBlockState())
        .setCondition(new WorldRecipe.Condition(new BlockMatchTest(Blocks.DARK_OAK_LOG)))
        .unlockedBy("has_knife", p.has(RootsTags.Items.KNIVES))
        .save(p, new ResourceLocation(RootsAPI.MODID, "bark/dark_oak_log_stripping"));
      BarkRecipe.builder(ctx.getEntry(), 3)
        .setOutputState(Blocks.STRIPPED_DARK_OAK_WOOD.defaultBlockState())
        .setCondition(new WorldRecipe.Condition(new BlockMatchTest(Blocks.DARK_OAK_WOOD)))
        .unlockedBy("has_knife", p.has(RootsTags.Items.KNIVES))
        .save(p, new ResourceLocation(RootsAPI.MODID, "bark/dark_oak_wood_stripping"));
    })
    .register();

  public static final ItemEntry<Item> JUNGLE_BARK = REGISTRATE.item("jungle_bark", Item::new)
    .tag(RootsTags.Items.JUNGLE_BARK)
    .model(subfolder("bark"))
    .recipe((ctx, p) -> {
      BarkRecipe.builder(ctx.getEntry(), 2)
        .setOutputState(Blocks.STRIPPED_JUNGLE_LOG.defaultBlockState())
        .setCondition(new WorldRecipe.Condition(new BlockMatchTest(Blocks.JUNGLE_LOG)))
        .unlockedBy("has_knife", p.has(RootsTags.Items.KNIVES))
        .save(p, new ResourceLocation(RootsAPI.MODID, "bark/jungle_log_stripping"));
      BarkRecipe.builder(ctx.getEntry(), 3)
        .setOutputState(Blocks.STRIPPED_JUNGLE_WOOD.defaultBlockState())
        .setCondition(new WorldRecipe.Condition(new BlockMatchTest(Blocks.JUNGLE_WOOD)))
        .unlockedBy("has_knife", p.has(RootsTags.Items.KNIVES))
        .save(p, new ResourceLocation(RootsAPI.MODID, "bark/jungle_wood_stripping"));
    })
    .register();

  public static final ItemEntry<Item> OAK_BARK = REGISTRATE.item("oak_bark", Item::new)
    .tag(RootsTags.Items.OAK_BARK)
    .model(subfolder("bark"))
    .recipe((ctx, p) -> {
      BarkRecipe.builder(ctx.getEntry(), 2)
        .setOutputState(Blocks.STRIPPED_OAK_LOG.defaultBlockState())
        .setCondition(new WorldRecipe.Condition(new BlockMatchTest(Blocks.OAK_LOG)))
        .unlockedBy("has_knife", p.has(RootsTags.Items.KNIVES))
        .save(p, new ResourceLocation(RootsAPI.MODID, "bark/oak_log_stripping"));
      BarkRecipe.builder(ctx.getEntry(), 3)
        .setOutputState(Blocks.STRIPPED_OAK_WOOD.defaultBlockState())
        .setCondition(new WorldRecipe.Condition(new BlockMatchTest(Blocks.OAK_WOOD)))
        .unlockedBy("has_knife", p.has(RootsTags.Items.KNIVES))
        .save(p, new ResourceLocation(RootsAPI.MODID, "bark/oak_wood_stripping"));
    })
    .register();

  public static final ItemEntry<Item> SPRUCE_BARK = REGISTRATE.item("spruce_bark", Item::new)
    .tag(RootsTags.Items.SPRUCE_BARK)
    .model(subfolder("bark"))
    .recipe((ctx, p) -> {
      BarkRecipe.builder(ctx.getEntry(), 2)
        .setOutputState(Blocks.STRIPPED_SPRUCE_LOG.defaultBlockState())
        .setCondition(new WorldRecipe.Condition(new BlockMatchTest(Blocks.SPRUCE_LOG)))
        .unlockedBy("has_knife", p.has(RootsTags.Items.KNIVES))
        .save(p, new ResourceLocation(RootsAPI.MODID, "bark/spruce_log_stripping"));
      BarkRecipe.builder(ctx.getEntry(), 3)
        .setOutputState(Blocks.STRIPPED_SPRUCE_WOOD.defaultBlockState())
        .setCondition(new WorldRecipe.Condition(new BlockMatchTest(Blocks.SPRUCE_WOOD)))
        .unlockedBy("has_knife", p.has(RootsTags.Items.KNIVES))
        .save(p, new ResourceLocation(RootsAPI.MODID, "bark/spruce_wood_stripping"));
    })
    .register();

  public static final ItemEntry<Item> WILDWOOD_BARK = REGISTRATE.item("wildwood_bark", Item::new)
    .tag(RootsTags.Items.WILDWOOD_BARK)
    .model(subfolder("bark"))
    .recipe((ctx, p) -> {
      BarkRecipe.builder(ctx.getEntry(), 2)
        .setOutputState(ModBlocks.STRIPPED_WILDWOOD_LOG.getDefaultState())
        .setCondition(new WorldRecipe.Condition(new BlockMatchTest(ModBlocks.WILDWOOD_LOG.get())))
        .unlockedBy("has_knife", p.has(RootsTags.Items.KNIVES))
        .save(p, new ResourceLocation(RootsAPI.MODID, "bark/wildwood_log_stripping"));
      BarkRecipe.builder(ctx.getEntry(), 3)
        .setOutputState(ModBlocks.STRIPPED_WILDWOOD_WOOD.getDefaultState())
        .setCondition(new WorldRecipe.Condition(new BlockMatchTest(ModBlocks.WILDWOOD_WOOD.get())))
        .unlockedBy("has_knife", p.has(RootsTags.Items.KNIVES))
        .save(p, new ResourceLocation(RootsAPI.MODID, "bark/wildwood_wood_stripping"));
    })
    .register();

  public static final ItemEntry<Item> CRIMSON_BARK = REGISTRATE.item("crimson_bark", Item::new)
    .tag(RootsTags.Items.CRIMSON_BARK)
    .model(subfolder("bark"))
    .recipe((ctx, p) -> {
      BarkRecipe.builder(ctx.getEntry(), 2)
        .setOutputState(Blocks.STRIPPED_CRIMSON_STEM.defaultBlockState())
        .setCondition(new WorldRecipe.Condition(new BlockMatchTest(Blocks.CRIMSON_STEM)))
        .unlockedBy("has_knife", p.has(RootsTags.Items.KNIVES))
        .save(p, new ResourceLocation(RootsAPI.MODID, "bark/crimson_stem_stripping"));
      BarkRecipe.builder(ctx.getEntry(), 3)
        .setOutputState(Blocks.STRIPPED_CRIMSON_HYPHAE.defaultBlockState())
        .setCondition(new WorldRecipe.Condition(new BlockMatchTest(Blocks.CRIMSON_HYPHAE)))
        .unlockedBy("has_knife", p.has(RootsTags.Items.KNIVES))
        .save(p, new ResourceLocation(RootsAPI.MODID, "bark/crimson_hyphae_stripping"));
    })
    .register();

  public static final ItemEntry<Item> WARPED_BARK = REGISTRATE.item("warped_bark", Item::new)
    .tag(RootsTags.Items.WARPED_BARK)
    .model(subfolder("bark"))
    .recipe((ctx, p) -> {
      BarkRecipe.builder(ctx.getEntry(), 2)
        .setOutputState(Blocks.STRIPPED_WARPED_STEM.defaultBlockState())
        .setCondition(new WorldRecipe.Condition(new BlockMatchTest(Blocks.WARPED_STEM)))
        .unlockedBy("has_knife", p.has(RootsTags.Items.KNIVES))
        .save(p, new ResourceLocation(RootsAPI.MODID, "bark/warped_stem_stripping"));
      BarkRecipe.builder(ctx.getEntry(), 3)
        .setOutputState(Blocks.STRIPPED_WARPED_HYPHAE.defaultBlockState())
        .setCondition(new WorldRecipe.Condition(new BlockMatchTest(Blocks.WARPED_HYPHAE)))
        .unlockedBy("has_knife", p.has(RootsTags.Items.KNIVES))
        .save(p, new ResourceLocation(RootsAPI.MODID, "bark/warped_hyphae_stripping"));
    })
    .register();

  public static final ItemEntry<Item> MANGROVE_BARK = REGISTRATE.item("mangrove_bark", Item::new)
    .tag(RootsTags.Items.MANGROVE_BARK)
    .model(subfolder("bark"))
    .recipe((ctx, p) -> {
      BarkRecipe.builder(ctx.getEntry(), 2)
        .setOutputState(Blocks.STRIPPED_MANGROVE_LOG.defaultBlockState())
        .setCondition(new WorldRecipe.Condition(new BlockMatchTest(Blocks.MANGROVE_LOG)))
        .unlockedBy("has_knife", p.has(RootsTags.Items.KNIVES))
        .save(p, new ResourceLocation(RootsAPI.MODID, "bark/mangrove_log_stripping"));
      BarkRecipe.builder(ctx.getEntry(), 3)
        .setOutputState(Blocks.STRIPPED_MANGROVE_WOOD.defaultBlockState())
        .setCondition(new WorldRecipe.Condition(new BlockMatchTest(Blocks.MANGROVE_WOOD)))
        .unlockedBy("has_knife", p.has(RootsTags.Items.KNIVES))
        .save(p, new ResourceLocation(RootsAPI.MODID, "bark/mangrove_wood_stripping"));
    })
    .register();

  public static final ItemEntry<Item> MIXED_BARK = REGISTRATE.item("mixed_bark", Item::new)
    .tag(RootsTags.Items.MIXED_BARK)
    .model(subfolder("bark"))
    .recipe((ctx, p) -> {
      p.accept(new DynamicBarkRecipe.Result());
    })
    .register();

  public static final ItemEntry<Item> APOTHECARY_POUCH = REGISTRATE.item("apothecary_pouch", Item::new)
    .model(subfolder("pouches"))
    .register();

  public static final ItemEntry<Item> COMPONENT_POUCH = REGISTRATE.item("component_pouch", Item::new)
    .model(subfolder("pouches"))
    .register();

  public static final ItemEntry<Item> CREATIVE_POUCH = REGISTRATE.item("creative_pouch", Item::new)
    .model(subfolder("pouches"))
    .register();

  public static final ItemEntry<Item> FEY_POUCH = REGISTRATE.item("fey_pouch", Item::new)
    .model(subfolder("pouches"))
    .register();

  public static final ItemEntry<Item> HERB_POUCH = REGISTRATE.item("herb_pouch", Item::new)
    .model(subfolder("pouches"))
    .register();

  public static final ItemEntry<Item> COOKED_PERESKIA = REGISTRATE.item("cooked_pereskia", Item::new)
    .model(subfolder("food"))
    .register();

  public static final ItemEntry<Item> FLOUR = REGISTRATE.item("flour", Item::new)
    .model(subfolder("food"))
    .register();

  public static final ItemEntry<Item> WILDEWHEET_BREAD = REGISTRATE.item("wildewheet_bread", Item::new)
    .model(subfolder("food"))
    .register();

  public static final ItemEntry<Item> WILDROOT_STEW = REGISTRATE.item("wildroot_stew", Item::new)
    .model(subfolder("food"))
    .recipe((ctx, p) -> {
      ShapedRecipeBuilder.shaped(ctx.getEntry(), 3)
        .pattern("WW ")
        .pattern("BBB")
        .define('W', Ingredient.of(RootsTags.Items.WILDROOT_CROP))
        .define('B', Ingredient.of(Items.BOWL))
        .unlockedBy("has_wildroot", p.has(RootsTags.Items.WILDROOT_CROP))
        .save(p, new ResourceLocation(RootsAPI.MODID, "wildroot_stew"));
    })
    .register();

  public static final ItemEntry<FireStarterItem> FIRE_STARTER = REGISTRATE.item("fire_starter", FireStarterItem::new)
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
        .save(p, new ResourceLocation(RootsAPI.MODID, "fire_starter"));
    })
    .register();

  public static final ItemEntry<Item> GRAMARY = REGISTRATE.item("gramary", Item::new)
    .model(subfolder("tools"))
    .register();

  public static final ItemEntry<Item> LIVING_ARROW = REGISTRATE.item("living_arrow", Item::new)
    .model(subfolder("tools"))
    .register();

  public static final ItemEntry<LivingAxeItem> LIVING_AXE = REGISTRATE.item("living_axe", (p) -> new LivingAxeItem(RootsAPI.LIVING_TOOL_TIER, 7.0F, -3.2F, p))
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
      .save(p, new ResourceLocation(RootsAPI.MODID, "grove/living_axe")))
    .register();

  public static final ItemEntry<LivingHoeItem> LIVING_HOE = REGISTRATE.item("living_hoe", (p) -> new LivingHoeItem(RootsAPI.LIVING_TOOL_TIER, -1, -2.0f, p))
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
      .save(p, new ResourceLocation(RootsAPI.MODID, "grove/living_hoe")))
    .register();

  public static final ItemEntry<LivingPickaxeItem> LIVING_PICKAXE = REGISTRATE.item("living_pickaxe", (p) -> new LivingPickaxeItem(RootsAPI.LIVING_TOOL_TIER, 1, -2.8f, p))
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
      .save(p, new ResourceLocation(RootsAPI.MODID, "grove/living_pickaxe")))
    .register();

  public static final ItemEntry<LivingShovelItem> LIVING_SHOVEL = REGISTRATE.item("living_shovel", (p) -> new LivingShovelItem(RootsAPI.LIVING_TOOL_TIER, 1.5F, -3.0F, p))
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
      .save(p, new ResourceLocation(RootsAPI.MODID, "grove/living_shovel")))
    .register();

  public static final ItemEntry<LivingSwordItem> LIVING_SWORD = REGISTRATE.item("living_sword", (p) -> new LivingSwordItem(RootsAPI.LIVING_TOOL_TIER, 3, -2.4f, p))
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
      .save(p, new ResourceLocation(RootsAPI.MODID, "grove/living_sword")))
    .register();

  public static final ItemEntry<Item> PESTLE = REGISTRATE.item("pestle", Item::new)
    .model(subfolder("tools"))
    .tag(RootsTags.Items.MORTAR_ACTIVATION)
    .recipe((ctx, p) -> {
      ShapedRecipeBuilder.shaped(ctx.getEntry())
        .pattern("  S")
        .pattern("SS ")
        .pattern("SS ")
        .define('S', Ingredient.of(RootsTags.Items.STONELIKE))
        .unlockedBy("has_stone", p.has(RootsTags.Items.STONELIKE))
        .save(p, new ResourceLocation(RootsAPI.MODID, "pestle"));
    })
    .register();

  public static final ItemEntry<Item> RUNED_AXE = REGISTRATE.item("runed_axe", Item::new)
    .model(subfolder("tools"))
    .register();

  public static final ItemEntry<Item> RUNED_DAGGER = REGISTRATE.item("runed_dagger", Item::new)
    .model(subfolder("tools"))
    .register();

  public static final ItemEntry<Item> RUNED_HOE = REGISTRATE.item("runed_hoe", Item::new)
    .model(subfolder("tools"))
    .register();

  public static final ItemEntry<Item> RUNED_SHOVEL = REGISTRATE.item("runed_shovel", Item::new)
    .model(subfolder("tools"))
    .register();

  public static final ItemEntry<Item> RUNED_SWORD = REGISTRATE.item("runed_sword", Item::new)
    .model(subfolder("tools"))
    .register();

  public static final ItemEntry<RunicShearsItem> RUNIC_SHEARS = REGISTRATE.item("runic_shears", RunicShearsItem::new)
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
        .save(p, new ResourceLocation(RootsAPI.MODID, "grove/runic_shears"));
    })
    .tag(RootsTags.Items.RUNIC_SHEARS)
    .register();

  public static final ItemEntry<CastingItem> STAFF = REGISTRATE.item("staff", CastingItem::new)
    // TODO: CUSTOM MODEL
    .model(subfolder("tools"))
    .tag(RootsTags.Items.CASTING_TOOLS)
    .register();

  public static final ItemEntry<Item> WILDWOOD_BOW = REGISTRATE.item("wildwood_bow", Item::new)
    // TODO: MODEL, ETC
    .model(subfolder("tools"))
    .register();

  public static final ItemEntry<Item> WILDWOOD_QUIVER = REGISTRATE.item("wildwood_quiver", Item::new)
    .model(subfolder("tools"))
    .register();

  public static final ItemEntry<ShearsItem> WOODEN_SHEARS = REGISTRATE.item("wooden_shears", ShearsItem::new)
    .properties(o -> o.durability(120))
    .model(subfolder("tools"))
    .recipe((ctx, p) -> {
      ShapedRecipeBuilder.shaped(ctx.getEntry())
        .pattern(" LL")
        .pattern("L  ")
        .pattern(" LL")
        .define('L', Ingredient.of(ItemTags.LOGS))
        .unlockedBy("has_log", p.has(ItemTags.LOGS))
        .save(p, new ResourceLocation(RootsAPI.MODID, "wooden_shears"));
    })
    .register();

  public static ItemEntry<KnifeItem> WOODEN_KNIFE = REGISTRATE.item("wood_knife", (p) -> new KnifeItem(Tiers.WOOD, 0f, -1.5f, p))
    .tag(RootsTags.Items.KNIVES)
    .model((ctx, p) -> p.handheld(ModItems.WOODEN_KNIFE))
    .recipe((ctx, p) -> RECIPES.knife(ItemTags.PLANKS, ModItems.WOODEN_KNIFE, null, p)).register();
  public static ItemEntry<KnifeItem> STONE_KNIFE = REGISTRATE.item("stone_knife", (p) -> new KnifeItem(Tiers.STONE, 0f, -1.0f, p))
    .tag(RootsTags.Items.KNIVES)
    .model((ctx, p) -> p.handheld(ModItems.STONE_KNIFE))
    .recipe((ctx, p) -> {
      RECIPES.knife(Tags.Items.STONE, ModItems.STONE_KNIFE, null, p);
      RECIPES.knife(Tags.Items.COBBLESTONE, ModItems.STONE_KNIFE, null, p);
    }).register();
  public static RegistryEntry<KnifeItem> COPPER_KNIFE = REGISTRATE.item("copper_knife", (p) -> new KnifeItem(RootsAPI.COPPER_TIER, 0f, -1.5f, p))
    .tag(RootsTags.Items.COPPER_ITEMS, RootsTags.Items.KNIVES)
    .model((ctx, p) -> p.handheld(ModItems.COPPER_KNIFE))
    .recipe((ctx, p) -> RECIPES.knife(Tags.Items.STORAGE_BLOCKS_COPPER, ModItems.COPPER_KNIFE, null, p)).register();
  public static ItemEntry<KnifeItem> IRON_KNIFE = REGISTRATE.item("iron_knife", (p) -> new KnifeItem(Tiers.IRON, 0, -1.5f, p))
    .tag(RootsTags.Items.KNIVES)
    .model((ctx, p) -> p.handheld(ModItems.IRON_KNIFE))
    .recipe((ctx, p) -> RECIPES.knife(Tags.Items.INGOTS_IRON, ModItems.IRON_KNIFE, null, p)).register();
  public static ItemEntry<KnifeItem> GOLD_KNIFE = REGISTRATE.item("gold_knife", (p) -> new KnifeItem(Tiers.GOLD, 0f, -1.0f, p))
    .tag(RootsTags.Items.KNIVES)
    .model((ctx, p) -> p.handheld(ModItems.GOLD_KNIFE))
    .recipe((ctx, p) -> RECIPES.knife(Tags.Items.INGOTS_GOLD, ModItems.GOLD_KNIFE, null, p))
    .tag(ItemTags.PIGLIN_LOVED)
    .register();
  public static ItemEntry<KnifeItem> SILVER_KNIFE = REGISTRATE.item("silver_knife", (p) -> new KnifeItem(Tiers.GOLD, 0f, -1.0f, p))
    .tag(RootsTags.Items.KNIVES, RootsTags.Items.SILVER_ITEMS)
    .model((ctx, p) -> p.handheld(ctx::getEntry))
    .recipe((ctx, p) -> RECIPES.knife(RootsTags.Items.SILVER_INGOT, ctx::getEntry, null, p))
    .register();
  public static ItemEntry<KnifeItem> DIAMOND_KNIFE = REGISTRATE.item("diamond_knife", (p) -> new KnifeItem(Tiers.DIAMOND, 0.5f, -1.2f, p))
    .tag(RootsTags.Items.KNIVES)
    .model((ctx, p) -> p.handheld(ModItems.DIAMOND_KNIFE))
    .recipe((ctx, p) -> RECIPES.knife(Tags.Items.GEMS_DIAMOND, ModItems.DIAMOND_KNIFE, null, p)).register();
  public static ItemEntry<KnifeItem> NETHERITE_KNIFE = REGISTRATE.item("netherite_knife", (p) -> new KnifeItem(Tiers.NETHERITE, 0.5f, -1.2f, p))
    .tag(RootsTags.Items.KNIVES)
    .model((ctx, p) -> p.handheld(ModItems.NETHERITE_KNIFE))
    .recipe((ctx, p) -> RECIPES.knife(Tags.Items.INGOTS_NETHERITE, ModItems.NETHERITE_KNIFE, null, p)).register();

  public static final ItemEntry<Item> RELIQUARY = REGISTRATE.item("reliquary", Item::new)
    .model(subfolder("containers"))
    .register();

  public static final ItemEntry<Item> SPIRIT_BAG = REGISTRATE.item("spirit_bag", Item::new)
    .model(subfolder("containers"))
    .register();

  public static final ItemEntry<Item> FEY_LEATHER = REGISTRATE.item("fey_leather", Item::new)
    .model(subfolder("resources"))
    .recipe((ctx, p) -> {
      RunicEntityRecipe.builder(ctx.getEntry())
        .setCooldown(2 * 60 * 20)
        .setTest(new EntityTagTest(RootsTags.Entities.FEY_LEATHER))
        .unlockedBy("has_shears", p.has(RootsTags.Items.RUNIC_SHEARS))
        .save(p, new ResourceLocation(RootsAPI.MODID, "runic/entity/fey_leather"));
    })
    .register();

  public static final ItemEntry<Item> GLASS_EYE = REGISTRATE.item("glass_eye", Item::new)
    .model(subfolder("resources"))
    .register();

  public static final ItemEntry<Item> LIFE_ESSENCE = REGISTRATE.item("life_essence", Item::new)
    .model(subfolder("resources"))
    .register();

  public static final ItemEntry<Item> MYSTIC_FEATHER = REGISTRATE.item("mystic_feather", Item::new)
    .model(subfolder("resources"))
    .register();

  public static final ItemEntry<Item> PETALS = REGISTRATE.item("petals", Item::new)
    .model(subfolder("resources"))
    .tag(RootsTags.Items.PETALS)
    .recipe((ctx, p) -> {
      MortarRecipe.multiBuilder(ctx.getEntry(), 1)
        .addIngredient(ItemTags.SMALL_FLOWERS)
        .unlockedBy("has_flower", p.has(ItemTags.SMALL_FLOWERS))
        .save(p, new ResourceLocation(RootsAPI.MODID, "petals_from_small_flowers"));
      MortarRecipe.multiBuilder(new ItemStack(ctx.getEntry(), 2), 2)
        .addIngredient(ItemTags.TALL_FLOWERS)
        .unlockedBy("has_flower", p.has(ItemTags.TALL_FLOWERS))
        .save(p, new ResourceLocation(RootsAPI.MODID, "petals_from_tall_flowers"));
    })
    .register();

  public static final ItemEntry<Item> RUNIC_DUST = REGISTRATE.item("runic_dust", Item::new)
    .model(subfolder("resources"))
    .recipe((ctx, p) -> MortarRecipe.multiBuilder(ctx.getEntry(), 1)
      .addIngredient(RootsTags.Items.RUNESTONE)
      .unlockedBy("has_runestone", p.has(RootsTags.Items.RUNESTONE))
      .save(p, new ResourceLocation(RootsAPI.MODID, "runic_dust")))
    .tag(RootsTags.Items.RUNIC_DUST)
    .register();

  public static final ItemEntry<Item> STRANGE_OOZE = REGISTRATE.item("strange_ooze", Item::new)
    .model(subfolder("resources"))
    .register();

  public static RegistryEntry<AntlerHatItem> ANTLER_HAT = REGISTRATE.item("antler_hat", AntlerHatItem::new)
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
    .register();

  public static RegistryEntry<BeetleArmorItem> BEETLE_HELMET = REGISTRATE.item("beetle_helmet", (b) -> new BeetleArmorItem(b, EquipmentSlot.HEAD))
    .properties(o -> o.rarity(Rarity.RARE))
    .recipe((o, p) -> ShapedRecipeBuilder.shaped(o.getEntry(), 1)
      .pattern("CCC")
      .pattern("C C")
      .define('C', RootsTags.Items.CARAPACE)
      .unlockedBy("has_carapace", RegistrateRecipeProvider.has(RootsTags.Items.CARAPACE))
      .save(p))
    .register();

  public static RegistryEntry<BeetleArmorItem> BEETLE_CHESTPLATE = REGISTRATE.item("beetle_chestplate", (b) -> new BeetleArmorItem(b, EquipmentSlot.CHEST))
    .properties(o -> o.rarity(Rarity.RARE))
    .recipe((ctx, p) -> ShapedRecipeBuilder.shaped(ctx.getEntry(), 1)
      .pattern("C C")
      .pattern("CCC")
      .pattern("CCC")
      .define('C', RootsTags.Items.CARAPACE)
      .unlockedBy("has_carapace", RegistrateRecipeProvider.has(RootsTags.Items.CARAPACE))
      .save(p))
    .register();

  public static RegistryEntry<BeetleArmorItem> BEETLE_LEGGINGS = REGISTRATE.item("beetle_leggings", (b) -> new BeetleArmorItem(b, EquipmentSlot.LEGS))
    .recipe((ctx, p) -> ShapedRecipeBuilder.shaped(ctx.getEntry(), 1)
      .pattern("CCC")
      .pattern("C C")
      .pattern("C C")
      .define('C', RootsTags.Items.CARAPACE)
      .unlockedBy("has_carapace", RegistrateRecipeProvider.has(RootsTags.Items.CARAPACE))
      .save(p))
    .register();

  public static RegistryEntry<BeetleArmorItem> BEETLE_BOOTS = REGISTRATE.item("beetle_boots", (b) -> new BeetleArmorItem(b, EquipmentSlot.FEET))
    .recipe((ctx, p) -> ShapedRecipeBuilder.shaped(ctx.getEntry(), 1)
      .pattern("C C")
      .pattern("C C")
      .define('C', RootsTags.Items.CARAPACE)
      .unlockedBy("has_carapace", RegistrateRecipeProvider.has(RootsTags.Items.CARAPACE))
      .save(p))
    .register();

  public static RegistryEntry<Item> RAW_SILVER = REGISTRATE.item("raw_silver", Item::new)
    .tag(RootsTags.Items.RAW_SILVER)
    .recipe((ctx, p) -> {
      SimpleCookingRecipeBuilder.smelting(Ingredient.of(RootsTags.Items.RAW_SILVER), ModItems.SILVER_INGOT.get(), 0.7f, 200)
        .unlockedBy("has_raw_silver", RegistrateRecipeProvider.has(RootsTags.Items.RAW_SILVER))
        .save(p, new ResourceLocation(RootsAPI.MODID, "silver_ingot_from_raw_silver_smelting"));
      SimpleCookingRecipeBuilder.blasting(Ingredient.of(RootsTags.Items.RAW_SILVER), ModItems.SILVER_INGOT.get(), 0.7f, 100)
        .unlockedBy("has_raw_silver", RegistrateRecipeProvider.has(RootsTags.Items.RAW_SILVER))
        .save(p, new ResourceLocation(RootsAPI.MODID, "silver_ingot_from_raw_silver_blasting"));
      ShapelessRecipeBuilder.shapeless(ctx.getEntry(), 9)
        .requires(RootsTags.Items.RAW_SILVER_STORAGE)
        .unlockedBy("has_raw_silver_storage", RegistrateRecipeProvider.has(RootsTags.Items.RAW_SILVER_STORAGE))
        .save(p);
    })
    .register();
  public static RegistryEntry<Item> SILVER_INGOT = REGISTRATE.item("silver_ingot", Item::new)
    .tag(RootsTags.Items.SILVER_INGOT, ItemTags.BEACON_PAYMENT_ITEMS)
    .recipe(RECIPES.storage(() -> ModBlocks.SILVER_BLOCK, () -> ModItems.SILVER_INGOT, RootsTags.Items.SILVER_STORAGE, RootsTags.Items.SILVER_INGOT, RootsTags.Items.SILVER_ORE, () -> ModItems.SILVER_NUGGET, RootsTags.Items.SILVER_NUGGET, null))
    .register();

  public static RegistryEntry<Item> SILVER_NUGGET = REGISTRATE.item("silver_nugget'", Item::new)
    .tag(RootsTags.Items.SILVER_NUGGET)
    .recipe((ctx, p) -> {
      RECIPES.recycle(RootsTags.Items.SILVER_ITEMS, ModItems.SILVER_NUGGET, 0.15f, p);
    })
    .register();

  public static RegistryEntry<Item> COPPER_NUGGET = REGISTRATE.item("copper_nugget", Item::new)
    .tag(RootsTags.Items.COPPER_NUGGET)
    .recipe((ctx, p) -> {
      RECIPES.recycle(RootsTags.Items.COPPER_ITEMS, () -> Items.COPPER_INGOT, 0.15f, p);
      RECIPES.recycle(ModItems.GOLD_KNIFE, () -> Items.GOLD_NUGGET, 0.15f, RootsAPI.MODID, p);
      RECIPES.recycle(ModItems.IRON_KNIFE, () -> Items.IRON_NUGGET, 0.15f, RootsAPI.MODID, p);
      // TODO: Nugget change?
      ShapelessRecipeBuilder.shapeless(ctx.getEntry(), 9)
        .requires(Tags.Items.INGOTS_COPPER)
        .unlockedBy("has_copper_ingot", RegistrateRecipeProvider.has(Tags.Items.INGOTS_COPPER))
        .save(p, new ResourceLocation(RootsAPI.MODID, "copper_nugget_from_ingot"));
      ShapedRecipeBuilder.shaped(Items.COPPER_INGOT)
        .pattern("***")
        .pattern("*X*")
        .pattern("***")
        .define('*', RootsTags.Items.COPPER_NUGGET)
        .define('X', ctx.getEntry())
        .unlockedBy("has_copper_nugget", p.has(RootsTags.Items.COPPER_NUGGET))
        .save(p, new ResourceLocation(RootsAPI.MODID, "copper_ingot_from_nuggets"));
    })
    .register();



  // TODO: Check damage values
  public static RegistryEntry<AxeItem> COPPER_AXE = REGISTRATE.item("copper_axe", (p) -> new AxeItem(RootsAPI.COPPER_TIER, 5.0f, -3.1f, p))
    .tag(RootsTags.Items.COPPER_ITEMS)
    .model((ctx, p) -> p.handheld(ModItems.COPPER_AXE))
    .recipe((ctx, p) -> RECIPES.axe(Tags.Items.STORAGE_BLOCKS_COPPER, ModItems.COPPER_AXE, null, p)).register();
  public static RegistryEntry<HoeItem> COPPER_HOE = REGISTRATE.item("copper_hoe", (p) -> new HoeItem(RootsAPI.COPPER_TIER, 1, -1f, p))
    .tag(RootsTags.Items.COPPER_ITEMS)
    .model((ctx, p) -> p.handheld(ModItems.COPPER_HOE))
    .recipe((ctx, p) -> RECIPES.hoe(Tags.Items.STORAGE_BLOCKS_COPPER, ModItems.COPPER_HOE, null, p)).register();
  public static RegistryEntry<PickaxeItem> COPPER_PICKAXE = REGISTRATE.item("copper_pickaxe", (p) -> new PickaxeItem(RootsAPI.COPPER_TIER, 1, -1f, p))
    .tag(RootsTags.Items.COPPER_ITEMS)
    .model((ctx, p) -> p.handheld(ModItems.COPPER_PICKAXE))
    .recipe((ctx, p) -> RECIPES.pickaxe(Tags.Items.STORAGE_BLOCKS_COPPER, ModItems.COPPER_PICKAXE, null, p)).register();
  public static RegistryEntry<ShovelItem> COPPER_SHOVEL = REGISTRATE.item("copper_shovel", (p) -> new ShovelItem(RootsAPI.COPPER_TIER, 1, -1f, p))
    .tag(RootsTags.Items.COPPER_ITEMS)
    .model((ctx, p) -> p.handheld(ModItems.COPPER_SHOVEL))
    .recipe((ctx, p) -> RECIPES.shovel(Tags.Items.STORAGE_BLOCKS_COPPER, ModItems.COPPER_SHOVEL, null, p)).register();
  public static RegistryEntry<SwordItem> COPPER_SWORD = REGISTRATE.item("copper_sword", (p) -> new SwordItem(RootsAPI.COPPER_TIER, 1, -1f, p))
    .tag(RootsTags.Items.COPPER_ITEMS)
    .model((ctx, p) -> p.handheld(ModItems.COPPER_SWORD))
    .recipe((ctx, p) -> RECIPES.sword(Tags.Items.STORAGE_BLOCKS_COPPER, ModItems.COPPER_SWORD, null, p)).register();

  public static RegistryEntry<CopperArmorItem> COPPER_HELMET = REGISTRATE.item("copper_helmet", (p) -> new CopperArmorItem(Roots.COPPER_MATERIAL, EquipmentSlot.HEAD, p))
    .recipe((ctx, p) -> RECIPES.helmet(Tags.Items.STORAGE_BLOCKS_COPPER, ModItems.COPPER_HELMET, null, p))
    .tag(RootsTags.Items.COPPER_ITEMS)
    .register();
  public static RegistryEntry<CopperArmorItem> COPPER_CHESTPLATE = REGISTRATE.item("copper_chestplate", (p) -> new CopperArmorItem(Roots.COPPER_MATERIAL, EquipmentSlot.CHEST, p))
    .recipe((ctx, p) -> RECIPES.chest(Tags.Items.STORAGE_BLOCKS_COPPER, ModItems.COPPER_CHESTPLATE, null, p))
    .tag(RootsTags.Items.COPPER_ITEMS)
    .register();
  public static RegistryEntry<CopperArmorItem> COPPER_LEGGINGS = REGISTRATE.item("copper_leggings", (p) -> new CopperArmorItem(Roots.COPPER_MATERIAL, EquipmentSlot.LEGS, p))
    .recipe((ctx, p) -> RECIPES.legs(Tags.Items.STORAGE_BLOCKS_COPPER, ModItems.COPPER_LEGGINGS, null, p))
    .tag(RootsTags.Items.COPPER_ITEMS)
    .register();
  public static RegistryEntry<CopperArmorItem> COPPER_BOOTS = REGISTRATE.item("copper_boots", (p) -> new CopperArmorItem(Roots.COPPER_MATERIAL, EquipmentSlot.FEET, p))
    .recipe((ctx, p) -> RECIPES.boots(Tags.Items.STORAGE_BLOCKS_COPPER, ModItems.COPPER_BOOTS, null, p))
    .tag(RootsTags.Items.COPPER_ITEMS)
    .register();

/*  public static RegistryEntry<SilverAxeItem> SILVER_AXE = REGISTRATE.item(ModMaterials.SILVER.getInternalName() + "_axe", ItemGenerator.axe(SilverAxeItem::new, ModMaterials.SILVER))
    .tag(RootsTags.Items.SILVER_ITEMS)
    .model((ctx, p) -> p.handheld(ModItems.SILVER_AXE))
    .recipe((ctx, p) -> RECIPES.axe(RootsTags.Items.SILVER_INGOT, ModItems.SILVER_AXE, null, p)).register();
  public static RegistryEntry<SilverHoeItem> SILVER_HOE = REGISTRATE.item(ModMaterials.SILVER.getInternalName() + "_hoe", ItemGenerator.hoe(SilverHoeItem::new, ModMaterials.SILVER))
    .tag(RootsTags.Items.SILVER_ITEMS)
    .model((ctx, p) -> p.handheld(ModItems.SILVER_HOE))
    .recipe((ctx, p) -> RECIPES.hoe(RootsTags.Items.SILVER_INGOT, ModItems.SILVER_HOE, null, p)).register();
  public static RegistryEntry<SilverKnifeItem> SILVER_KNIFE = REGISTRATE.item(ModMaterials.SILVER.getInternalName() + "_knife", ItemGenerator.knife(SilverKnifeItem::new, ModMaterials.SILVER))
    .tag(RootsTags.Items.SILVER_ITEMS, RootsTags.Items.KNIVES)
    .model((ctx, p) -> p.handheld(ModItems.SILVER_KNIFE))
    .recipe((ctx, p) -> RECIPES.knife(RootsTags.Items.SILVER_INGOT, ModItems.SILVER_KNIFE, null, p)).register();
  public static RegistryEntry<SilverPickaxeItem> SILVER_PICKAXE = REGISTRATE.item(ModMaterials.SILVER.getInternalName() + "_pickaxe", ItemGenerator.pickaxe(SilverPickaxeItem::new, ModMaterials.SILVER))
    .tag(RootsTags.Items.SILVER_ITEMS)
    .model((ctx, p) -> p.handheld(ModItems.SILVER_PICKAXE))
    .recipe((ctx, p) -> RECIPES.pickaxe(RootsTags.Items.SILVER_INGOT, ModItems.SILVER_PICKAXE, null, p)).register();
  public static RegistryEntry<SilverShovelItem> SILVER_SHOVEL = REGISTRATE.item(ModMaterials.SILVER.getInternalName() + "_shovel", ItemGenerator.shovel(SilverShovelItem::new, ModMaterials.SILVER))
    .tag(RootsTags.Items.SILVER_ITEMS)
    .model((ctx, p) -> p.handheld(ModItems.SILVER_SHOVEL))
    .recipe((ctx, p) -> RECIPES.shovel(RootsTags.Items.SILVER_INGOT, ModItems.SILVER_SHOVEL, null, p)).register();
  public static RegistryEntry<SilverSwordItem> SILVER_SWORD = REGISTRATE.item(ModMaterials.SILVER.getInternalName() + "_sword", ItemGenerator.sword(SilverSwordItem::new, ModMaterials.SILVER))
    .tag(RootsTags.Items.SILVER_ITEMS)
    .model((ctx, p) -> p.handheld(ModItems.SILVER_SWORD))
    .recipe((ctx, p) -> RECIPES.sword(RootsTags.Items.SILVER_INGOT, ModItems.SILVER_SWORD, null, p)).register(); */

/*  public static RegistryEntry<SilverArmorItem> SILVER_HELMET = MysticalWorld.REGISTRATE.item(ModMaterials.SILVER.getInternalName() + "_helmet", ItemGenerator.armor(SilverArmorItem::new, ModMaterials.SILVER, EquipmentSlot.HEAD))
    .recipe((ctx, p) -> MysticalWorld.RECIPES.helmet(MWTags.Items.SILVER_INGOT, ModItems.SILVER_HELMET, null, p))
    .tag(MWTags.Items.SILVER_ITEMS)
    .register();
  public static RegistryEntry<SilverArmorItem> SILVER_CHESTPLATE = MysticalWorld.REGISTRATE.item(ModMaterials.SILVER.getInternalName() + "_chestplate", ItemGenerator.armor(SilverArmorItem::new, ModMaterials.SILVER, EquipmentSlot.CHEST))
    .recipe((ctx, p) -> MysticalWorld.RECIPES.chest(MWTags.Items.SILVER_INGOT, ModItems.SILVER_CHESTPLATE, null, p))
    .tag(MWTags.Items.SILVER_ITEMS)
    .register();
  public static RegistryEntry<SilverArmorItem> SILVER_LEGGINGS = MysticalWorld.REGISTRATE.item(ModMaterials.SILVER.getInternalName() + "_leggings", ItemGenerator.armor(SilverArmorItem::new, ModMaterials.SILVER, EquipmentSlot.LEGS))
    .recipe((ctx, p) -> MysticalWorld.RECIPES.legs(MWTags.Items.SILVER_INGOT, ModItems.SILVER_LEGGINGS, null, p))
    .tag(MWTags.Items.SILVER_ITEMS)
    .register();
  public static RegistryEntry<SilverArmorItem> SILVER_BOOTS = MysticalWorld.REGISTRATE.item(ModMaterials.SILVER.getInternalName() + "_boots", ItemGenerator.armor(SilverArmorItem::new, ModMaterials.SILVER, EquipmentSlot.FEET))
    .recipe((ctx, p) -> MysticalWorld.RECIPES.boots(MWTags.Items.SILVER_INGOT, ModItems.SILVER_BOOTS, null, p))
    .tag(MWTags.Items.SILVER_ITEMS)
    .register();*/

  private static <T extends Item> ItemModelBuilder spawnEggModel(DataGenContext<Item, T> ctx, RegistrateItemModelProvider p) {
    return p.withExistingParent(ctx.getName(), new ResourceLocation("item/template_spawn_egg"));
  }

  public static RegistryEntry<ForgeSpawnEggItem> BEETLE_SPAWN_EGG = REGISTRATE.item("beetle_spawn_egg", (p) -> new ForgeSpawnEggItem(ModEntities.BEETLE, 0x418594, 0x211D15, p))
    .properties(o -> o.tab(CreativeModeTab.TAB_MISC))
    .model(ModItems::spawnEggModel)
    .register();

  public static RegistryEntry<ForgeSpawnEggItem> DEER_SPAWN_EGG = REGISTRATE.item("deer_spawn_egg", (p) -> new ForgeSpawnEggItem(ModEntities.DEER, 0xa18458, 0x5e4d33, p))
    .properties(o -> o.tab(CreativeModeTab.TAB_MISC))
    .model(ModItems::spawnEggModel)
    .register();

  public static RegistryEntry<ForgeSpawnEggItem> FENNEC_SPAWN_EGG = REGISTRATE.item("fennec_spawn_egg", (p) -> new ForgeSpawnEggItem(ModEntities.FENNEC, 0xe9dcc2, 0xb1855c, p))
    .properties(o -> o.tab(CreativeModeTab.TAB_MISC))
    .model(ModItems::spawnEggModel)
    .register();

  public static RegistryEntry<ForgeSpawnEggItem> GREEN_SPROUT_SPAWN_EGG = REGISTRATE.item("green_sprout_spawn_egg", (p) -> new ForgeSpawnEggItem(ModEntities.GREEN_SPROUT, 0x9adb58, 0x2c9425, p))
    .properties(o -> o.tab(CreativeModeTab.TAB_MISC))
    .model(ModItems::spawnEggModel)
    .register();

  public static RegistryEntry<ForgeSpawnEggItem> TAN_SPROUT_SPAWN_EGG = REGISTRATE.item("tan_sprout_spawn_egg", (p) -> new ForgeSpawnEggItem(ModEntities.TAN_SPROUT, 0xeeca5f, 0xbb6c20, p))
    .properties(o -> o.tab(CreativeModeTab.TAB_MISC))
    .model(ModItems::spawnEggModel)
    .register();

  public static RegistryEntry<ForgeSpawnEggItem> RED_SPROUT_SPAWN_EGG = REGISTRATE.item("red_sprout_spawn_egg", (p) -> new ForgeSpawnEggItem(ModEntities.RED_SPROUT, 0xe6754c, 0xbd2637, p))
    .properties(o -> o.tab(CreativeModeTab.TAB_MISC))
    .model(ModItems::spawnEggModel)
    .register();

  public static RegistryEntry<ForgeSpawnEggItem> PURPLE_SPROUT_SPAWN_EGG = REGISTRATE.item("purple_sprout_spawn_egg", (p) -> new ForgeSpawnEggItem(ModEntities.PURPLE_SPROUT, 0xdd45e6, 0x6825ba, p))
    .properties(o -> o.tab(CreativeModeTab.TAB_MISC))
    .model(ModItems::spawnEggModel)
    .register();

  public static RegistryEntry<ForgeSpawnEggItem> OWL_SPAWN_EGG = REGISTRATE.item("owl_spawn_egg", (p) -> new ForgeSpawnEggItem(ModEntities.OWL, 0x8c654a, 0xdec9ba, p))
    .properties(o -> o.tab(CreativeModeTab.TAB_MISC))
    .model(ModItems::spawnEggModel)
    .register();

  public static RegistryEntry<ForgeSpawnEggItem> DUCK_SPAWN_EGG = REGISTRATE.item("duck_spawn_egg", (p) -> new ForgeSpawnEggItem(ModEntities.DUCK, 0xe4d6a5, 0xe9ad36, p))
    .properties(o -> o.tab(CreativeModeTab.TAB_MISC))
    .model(ModItems::spawnEggModel)
    .register();

  public static final ItemEntry<TokenItem> TOKEN = REGISTRATE.item("token", TokenItem::new)
    .model((ctx, p) -> {
      ModelFile generated = new ModelFile.UncheckedModelFile("item/generated");
      for (ResourceLocation ritual : Registries.RITUAL_REGISTRY.get().getKeys()) {
        p.getBuilder("ritual_" + ritual.getPath()).parent(generated).texture("layer0", p.modLoc("item/rituals/" + ritual.getPath()));
      }
      for (ResourceLocation spell : Registries.SPELL_REGISTRY.get().getKeys()) {
        p.getBuilder("spell_" + spell.getPath()).parent(generated).texture("layer0", p.modLoc("item/spells/" + spell.getPath()));
      }
    })
    .register();


  public static void load() {
  }
}
