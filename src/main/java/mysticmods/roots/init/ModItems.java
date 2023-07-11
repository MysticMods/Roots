package mysticmods.roots.init;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateItemModelProvider;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.RootsTags;
import mysticmods.roots.item.CastingItem;
import mysticmods.roots.item.FireStarterItem;
import mysticmods.roots.item.GroveSporesItem;
import mysticmods.roots.item.TokenItem;
import mysticmods.roots.item.living.*;
import mysticmods.roots.recipe.grove.GroveRecipe;
import mysticmods.roots.recipe.mortar.MortarRecipe;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;

import java.util.function.Supplier;

import static mysticmods.roots.Roots.REGISTRATE;

public class ModItems {
  private static <T extends Item> NonNullBiConsumer<DataGenContext<Item, T>, RegistrateItemModelProvider> subfolder(String subfolder) {
    return (ctx, p) -> p.generated(ctx::getEntry, new ResourceLocation(RootsAPI.MODID, "item/" + subfolder + "/" + ctx.getName()));
  }

  // GATHERED CROPS
  public static final ItemEntry<ItemNameBlockItem> WILDROOT = REGISTRATE.item("wildroot", (p) -> new ItemNameBlockItem(ModBlocks.WILDROOT_CROP.get(), p))
    .model(subfolder("herbs"))
    .tag(RootsTags.Items.WILDROOT_SEEDS, RootsTags.Items.SEEDS, RootsTags.Items.WILDROOT_CROP)
    .defaultLang()
    .register();
  public static final ItemEntry<Item> GROVE_MOSS = REGISTRATE.item("grove_moss", Item::new)
    .model(subfolder("herbs"))
    .tag(RootsTags.Items.GROVE_MOSS_CROP)
    .register();

  // PYRE-CRAFTED CROPS
  public static final ItemEntry<ItemNameBlockItem> CLOUD_BERRY = REGISTRATE.item("cloud_berry", (p) -> new ItemNameBlockItem(ModBlocks.CLOUD_BERRY_CROP.get(), p))
    .model(subfolder("herbs"))
    .tag(RootsTags.Items.CLOUD_BERRY_SEEDS, RootsTags.Items.SEEDS, RootsTags.Items.CLOUD_BERRY_CROP)
    .register();
  public static final ItemEntry<ItemNameBlockItem> DEWGONIA = REGISTRATE.item("dewgonia", (p) -> new ItemNameBlockItem(ModBlocks.DEWGONIA_CROP.get(), p))
    .model(subfolder("herbs"))
    .tag(RootsTags.Items.DEWGONIA_SEEDS, RootsTags.Items.SEEDS, RootsTags.Items.DEWGONIA_CROP)
    .register();
  public static final ItemEntry<ItemNameBlockItem> INFERNO_BULB = REGISTRATE.item("inferno_bulb", (p) -> new ItemNameBlockItem(ModBlocks.INFERNO_BULB_CROP.get(), p))
    .model(subfolder("herbs"))
    .tag(RootsTags.Items.INFERNO_BULB_SEEDS, RootsTags.Items.SEEDS, RootsTags.Items.INFERNO_BULB_CROP)
    .register();
  public static final ItemEntry<ItemNameBlockItem> STALICRIPE = REGISTRATE.item("stalicripe", (p) -> new ItemNameBlockItem(ModBlocks.STALICRIPE_CROP.get(), p))
    .model(subfolder("herbs"))
    .tag(RootsTags.Items.STALICRIPE_SEEDS, RootsTags.Items.SEEDS, RootsTags.Items.STALICRIPE_CROP)
    .register();
  // OTHER SOURCE CROPS

  // RUNIC SHEARS -> MUSHROOM
  public static final ItemEntry<ItemNameBlockItem> BAFFLECAP = REGISTRATE.item("bafflecap", (p) -> new ItemNameBlockItem(ModBlocks.BAFFLECAP.get(), p))
    .model(subfolder("herbs"))
    .tag(RootsTags.Items.BAFFLECAP_CROP)
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
    .tag(RootsTags.Items.MOONGLOW_SEEDS, RootsTags.Items.SEEDS)
    .register();
  public static final ItemEntry<ItemNameBlockItem> PERESKIA_BULB = REGISTRATE.item("pereskia_bulb", (p) -> new ItemNameBlockItem(ModBlocks.PERESKIA_CROP.get(), p))
    .model(subfolder("herbs"))
    .tag(RootsTags.Items.PERESKIA_SEEDS, RootsTags.Items.SEEDS)
    .register();
  public static final ItemEntry<ItemNameBlockItem> SPIRITLEAF_SEEDS = REGISTRATE.item("spiritleaf_seeds", (p) -> new ItemNameBlockItem(ModBlocks.SPIRITLEAF_CROP.get(), p))
    .model(subfolder("herbs"))
    .tag(RootsTags.Items.SPIRITLEAF_SEEDS, RootsTags.Items.SEEDS)
    .register();
  public static final ItemEntry<ItemNameBlockItem> WILDEWHEET_SEEDS = REGISTRATE.item("wildewheet_seeds", (p) -> new ItemNameBlockItem(ModBlocks.WILDEWHEET_CROP.get(), p))
    .model(subfolder("herbs"))
    .tag(RootsTags.Items.WILDEWHEET_SEEDS, RootsTags.Items.SEEDS)
    .register();

  public static final ItemEntry<GroveSporesItem> GROVE_SPORES = REGISTRATE.item("grove_spores", GroveSporesItem::new)
    .model(subfolder("herbs"))
    .tag(RootsTags.Items.SEEDS)
    .register();

  public static RegistryEntry<Item> AUBERGINE = REGISTRATE.item("aubergine", Item::new)
    .properties(o -> o.food(ModFoods.AUBERGINE))
/*      .tag(MWTags.Items.AUBERGINE, MWTags.Items.VEGETABLES)*/
    .register();

  public static <T extends Block> NonNullFunction<Item.Properties, ItemNameBlockItem> blockNamedItem(Supplier<Supplier<T>> block) {
    return (b) -> new ItemNameBlockItem(block.get().get(), b);
  }

  public static final ItemEntry<ItemNameBlockItem> AUBERGINE_SEEDS = REGISTRATE.item("aubergine_seeds", blockNamedItem(() -> ModBlocks.AUBERGINE_CROP))
/*      .recipe((ctx, p) -> MysticalWorld.RECIPES.singleItem(ModItems.AUBERGINE, ModItems.AUBERGINE_SEEDS, 1, 1, p))*/
/*      .tag(MWTags.Items.SEEDS)*/
    .register();

  public static final ItemEntry<Item> ACACIA_BARK = REGISTRATE.item("acacia_bark", Item::new)
    .tag(RootsTags.Items.ACACIA_BARK)
    .model(subfolder("bark"))
    .register();

  public static final ItemEntry<Item> BIRCH_BARK = REGISTRATE.item("birch_bark", Item::new)
    .tag(RootsTags.Items.BIRCH_BARK)
    .model(subfolder("bark"))
    .register();

  public static final ItemEntry<Item> DARK_OAK_BARK = REGISTRATE.item("dark_oak_bark", Item::new)
    .tag(RootsTags.Items.DARK_OAK_BARK)
    .model(subfolder("bark"))
    .register();

  public static final ItemEntry<Item> JUNGLE_BARK = REGISTRATE.item("jungle_bark", Item::new)
    .tag(RootsTags.Items.JUNGLE_BARK)
    .model(subfolder("bark"))
    .register();

  public static final ItemEntry<Item> OAK_BARK = REGISTRATE.item("oak_bark", Item::new)
    .tag(RootsTags.Items.OAK_BARK)
    .model(subfolder("bark"))
    .register();

  public static final ItemEntry<Item> SPRUCE_BARK = REGISTRATE.item("spruce_bark", Item::new)
    .tag(RootsTags.Items.SPRUCE_BARK)
    .model(subfolder("bark"))
    .register();

  public static final ItemEntry<Item> WILDWOOD_BARK = REGISTRATE.item("wildwood_bark", Item::new)
    .tag(RootsTags.Items.WILDWOOD_BARK)
    .model(subfolder("bark"))
    .register();

  public static final ItemEntry<Item> CRIMSON_BARK = REGISTRATE.item("crimson_bark", Item::new)
    .tag(RootsTags.Items.CRIMSON_BARK)
    .model(subfolder("bark"))
    .register();

  public static final ItemEntry<Item> WARPED_BARK = REGISTRATE.item("warped_bark", Item::new)
    .tag(RootsTags.Items.WARPED_BARK)
    .model(subfolder("bark"))
    .register();

  public static final ItemEntry<Item> MIXED_BARK = REGISTRATE.item("mixed_bark", Item::new)
    .tag(RootsTags.Items.MIXED_BARK)
    .model(subfolder("bark"))
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
    .register();

  public static final ItemEntry<FireStarterItem> FIRE_STARTER = REGISTRATE.item("fire_starter", FireStarterItem::new)
    .properties(o -> o.stacksTo(1))
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

  public static final ItemEntry<Item> RUNIC_SHEARS = REGISTRATE.item("runic_shears", Item::new)
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

  public static final ItemEntry<Item> RELIQUARY = REGISTRATE.item("reliquary", Item::new)
    .model(subfolder("containers"))
    .register();

  public static final ItemEntry<Item> SPIRIT_BAG = REGISTRATE.item("spirit_bag", Item::new)
    .model(subfolder("containers"))
    .register();

  public static final ItemEntry<Item> FEY_LEATHER = REGISTRATE.item("fey_leather", Item::new)
    .model(subfolder("resources"))
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

  public static final ItemEntry<TokenItem> TOKEN = REGISTRATE.item("token", TokenItem::new)
    .model((ctx, p) -> {
    })
    .register();

  public static void load() {
  }
}
