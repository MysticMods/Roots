package mysticmods.roots.init;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateItemModelProvider;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.item.CastingItem;
import mysticmods.roots.item.FireStarterItem;
import mysticmods.roots.item.TokenItem;
import mysticmods.roots.recipe.grove.GroveRecipe;
import mysticmods.roots.recipe.mortar.MortarRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;

import static mysticmods.roots.Roots.REGISTRATE;

public class ModItems {
  private static <T extends Item> NonNullBiConsumer<DataGenContext<Item, T>, RegistrateItemModelProvider> subfolder(String subfolder) {
    return (ctx, p) -> p.generated(ctx::getEntry, new ResourceLocation(RootsAPI.MODID, "item/" + subfolder + "/" + ctx.getName()));
  }

  public static class Herbs {
    public static final ItemEntry<ItemNameBlockItem> WILDROOT = REGISTRATE.item("wildroot", (p) -> new ItemNameBlockItem(ModBlocks.Crops.WILDROOT_CROP.get(), p))
        .model(subfolder("herbs"))
        .tag(RootsAPI.Tags.Items.WILDROOT_SEEDS, RootsAPI.Tags.Items.WILDROOT_CROP)
        .defaultLang()
        .register();
    public static final ItemEntry<Item> GROVE_MOSS = REGISTRATE.item("grove_moss", Item::new)
        .model(subfolder("herbs"))
        .tag(RootsAPI.Tags.Items.GROVE_MOSS_CROP)
        .register();
    public static final ItemEntry<ItemNameBlockItem> CLOUD_BERRY = REGISTRATE.item("cloud_berry", (p) -> new ItemNameBlockItem(ModBlocks.Crops.CLOUD_BERRY_CROP.get(), p))
        .model(subfolder("herbs"))
        .tag(RootsAPI.Tags.Items.CLOUD_BERRY_SEEDS, RootsAPI.Tags.Items.CLOUD_BERRY_CROP)
        .register();
    public static final ItemEntry<ItemNameBlockItem> DEWGONIA = REGISTRATE.item("dewgonia", (p) -> new ItemNameBlockItem(ModBlocks.Crops.DEWGONIA_CROP.get(), p))
        .model(subfolder("herbs"))
        .tag(RootsAPI.Tags.Items.DEWGONIA_SEEDS, RootsAPI.Tags.Items.DEWGONIA_CROP)
        .register();
    public static final ItemEntry<ItemNameBlockItem> INFERNO_BULB = REGISTRATE.item("inferno_bulb", (p) -> new ItemNameBlockItem(ModBlocks.Crops.INFERNO_BULB_CROP.get(), p))
        .model(subfolder("herbs"))
        .tag(RootsAPI.Tags.Items.INFERNO_BULB_SEEDS, RootsAPI.Tags.Items.INFERNO_BULB_CROP)
        .register();
    public static final ItemEntry<ItemNameBlockItem> STALICRIPE = REGISTRATE.item("stalicripe", (p) -> new ItemNameBlockItem(ModBlocks.Crops.STALICRIPE_CROP.get(), p))
        .model(subfolder("herbs"))
        .tag(RootsAPI.Tags.Items.STALICRIPE_SEEDS, RootsAPI.Tags.Items.STALICRIPE_CROP)
        .register();
    public static final ItemEntry<Item> MOONGLOW = REGISTRATE.item("moonglow", Item::new)
        .model(subfolder("herbs"))
        .tag(RootsAPI.Tags.Items.MOONGLOW_CROP)
        .register();
    public static final ItemEntry<Item> PERESKIA = REGISTRATE.item("pereskia", Item::new)
        .model(subfolder("herbs"))
        .tag(RootsAPI.Tags.Items.PERESKIA_CROP)
        .register();
    public static final ItemEntry<Item> SPIRITLEAF = REGISTRATE.item("spiritleaf", Item::new)
        .model(subfolder("herbs"))
        .tag(RootsAPI.Tags.Items.SPIRITLEAF_CROP)
        .register();
    public static final ItemEntry<Item> WILDEWHEET = REGISTRATE.item("wildewheet", Item::new)
        .model(subfolder("herbs"))
        .tag(RootsAPI.Tags.Items.WILDEWHEET_CROP)
        .register();

    public static void load() {

    }
  }

  public static class Seeds {

    public static final ItemEntry<ItemNameBlockItem> MOONGLOW_SEEDS = REGISTRATE.item("moonglow_seeds", (p) -> new ItemNameBlockItem(ModBlocks.Crops.MOONGLOW_CROP.get(), p))
        .model(subfolder("herbs"))
        .tag(RootsAPI.Tags.Items.MOONGLOW_SEEDS)
        .register();
    public static final ItemEntry<ItemNameBlockItem> PERESKIA_BULB = REGISTRATE.item("pereskia_bulb", (p) -> new ItemNameBlockItem(ModBlocks.Crops.PERESKIA_CROP.get(), p))
        .model(subfolder("herbs"))
        .tag(RootsAPI.Tags.Items.PERESKIA_SEEDS)
        .register();
    public static final ItemEntry<ItemNameBlockItem> SPIRITLEAF_SEEDS = REGISTRATE.item("spiritleaf_seeds", (p) -> new ItemNameBlockItem(ModBlocks.Crops.SPIRITLEAF_CROP.get(), p))
        .model(subfolder("herbs"))
        .tag(RootsAPI.Tags.Items.SPIRITLEAF_SEEDS)
        .register();
    public static final ItemEntry<ItemNameBlockItem> WILDEWHEET_SEEDS = REGISTRATE.item("wildewheet_seeds", (p) -> new ItemNameBlockItem(ModBlocks.Crops.WILDEWHEET_CROP.get(), p))
        .model(subfolder("herbs"))
        .tag(RootsAPI.Tags.Items.WILDEWHEET_SEEDS)
        .register();

    public static void load() {

    }
  }

  public static class Bark {
    public static final ItemEntry<Item> ACACIA_BARK = REGISTRATE.item("acacia_bark", Item::new)
        .tag(RootsAPI.Tags.Items.ACACIA_BARK)
        .model(subfolder("bark"))
        .register();

    public static final ItemEntry<Item> BIRCH_BARK = REGISTRATE.item("birch_bark", Item::new)
        .tag(RootsAPI.Tags.Items.BIRCH_BARK)
        .model(subfolder("bark"))
        .register();

    public static final ItemEntry<Item> DARK_OAK_BARK = REGISTRATE.item("dark_oak_bark", Item::new)
        .tag(RootsAPI.Tags.Items.DARK_OAK_BARK)
        .model(subfolder("bark"))
        .register();

    public static final ItemEntry<Item> JUNGLE_BARK = REGISTRATE.item("jungle_bark", Item::new)
        .tag(RootsAPI.Tags.Items.JUNGLE_BARK)
        .model(subfolder("bark"))
        .register();

    public static final ItemEntry<Item> OAK_BARK = REGISTRATE.item("oak_bark", Item::new)
        .tag(RootsAPI.Tags.Items.OAK_BARK)
        .model(subfolder("bark"))
        .register();

    public static final ItemEntry<Item> SPRUCE_BARK = REGISTRATE.item("spruce_bark", Item::new)
        .tag(RootsAPI.Tags.Items.SPRUCE_BARK)
        .model(subfolder("bark"))
        .register();

    public static final ItemEntry<Item> WILDWOOD_BARK = REGISTRATE.item("wildwood_bark", Item::new)
        .tag(RootsAPI.Tags.Items.WILDWOOD_BARK)
        .model(subfolder("bark"))
        .register();

    public static final ItemEntry<Item> CRIMSON_BARK = REGISTRATE.item("crimson_bark", Item::new)
        .tag(RootsAPI.Tags.Items.CRIMSON_BARK)
        .model(subfolder("bark"))
        .register();

    public static final ItemEntry<Item> WARPED_BARK = REGISTRATE.item("warped_bark", Item::new)
        .tag(RootsAPI.Tags.Items.WARPED_BARK)
        .model(subfolder("bark"))
        .register();

    public static final ItemEntry<Item> MIXED_BARK = REGISTRATE.item("mixed_bark", Item::new)
        .tag(RootsAPI.Tags.Items.MIXED_BARK)
        .model(subfolder("bark"))
        .register();

    public static void load() {

    }
  }

  public static class Pouches {
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

    public static void load() {
    }
  }

  public static final ItemEntry<Item> COOKED_PERESKIA = REGISTRATE.item("cooked_pereskia", Item::new)
      .model(subfolder("food"))
      .register();

  public static final ItemEntry<Item> FLOUR = REGISTRATE.item("flour", Item::new)
      .model(subfolder("food"))
      .recipe((ctx, p) -> {
        MortarRecipe.builder(ctx.getEntry(), 5, 4)
            .addIngredient(Items.WHEAT)
            .addIngredient(Items.WHEAT)
            .addIngredient(Items.WHEAT)
            .addIngredient(Items.WHEAT)
            .addIngredient(Items.WHEAT)
            .build(p, new ResourceLocation(RootsAPI.MODID, "mortar/flour"));
      })
      .register();

  public static final ItemEntry<Item> WILDEWHEET_BREAD = REGISTRATE.item("wildewheet_bread", Item::new)
      .model(subfolder("food"))
      .register();

  public static final ItemEntry<Item> WILDROOT_STEW = REGISTRATE.item("wildroot_stew", Item::new)
      .model(subfolder("food"))
      .register();

  public static final ItemEntry<Item> SYLVAN_BOOTS = REGISTRATE.item("sylvan_boots", Item::new)
      .model(subfolder("armor"))
      .register();

  public static final ItemEntry<Item> SYLVAN_CHESTPLATE = REGISTRATE.item("sylvan_chestplate", Item::new)
      .model(subfolder("armor"))
      .register();

  public static final ItemEntry<Item> SYLVAN_HELMET = REGISTRATE.item("sylvan_helmet", Item::new)
      .model(subfolder("armor"))
      .register();

  public static final ItemEntry<Item> SYLVAN_LEGGINGS = REGISTRATE.item("sylvan_leggings", Item::new)
      .model(subfolder("armor"))
      .register();

  public static final ItemEntry<Item> WILDWOOD_BOOTS = REGISTRATE.item("wildwood_boots", Item::new)
      .model(subfolder("armor"))
      .register();

  public static final ItemEntry<Item> WILDWOOD_CHESTPLATE = REGISTRATE.item("wildwood_chestplate", Item::new)
      .model(subfolder("armor"))
      .register();

  public static final ItemEntry<Item> WILDWOOD_HELMET = REGISTRATE.item("wildwood_helmet", Item::new)
      .model(subfolder("armor"))
      .register();

  public static final ItemEntry<Item> WILDWOOD_LEGGINGS = REGISTRATE.item("wildwood_leggings", Item::new)
      .model(subfolder("armor"))
      .register();

  public static final ItemEntry<FireStarterItem> FIRE_STARTER = REGISTRATE.item("fire_starter", FireStarterItem::new)
      .properties(o -> o.stacksTo(1))
      .model(subfolder("tools"))
      .register();

  public static final ItemEntry<Item> GRAMARY = REGISTRATE.item("gramary", Item::new)
      .model(subfolder("tools"))
      .register();

  public static final ItemEntry<Item> LIVING_ARROW = REGISTRATE.item("living_arrow", Item::new)
      .model(subfolder("tools"))
      .register();

  public static final ItemEntry<Item> LIVING_AXE = REGISTRATE.item("living_axe", Item::new)
      .model(subfolder("tools"))
      .register();

  public static final ItemEntry<Item> LIVING_HOE = REGISTRATE.item("living_hoe", Item::new)
      .model(subfolder("tools"))
      .register();

  public static final ItemEntry<Item> LIVING_PICKAXE = REGISTRATE.item("living_pickaxe", Item::new)
      .model(subfolder("tools"))
      .recipe((ctx, p) -> GroveRecipe.builder(new ItemStack(ctx.getEntry()))
          .addIngredient(Items.WOODEN_PICKAXE)
          .addIngredient(net.minecraftforge.common.Tags.Items.INGOTS_GOLD)
          .addIngredient(RootsAPI.Tags.Items.WILDROOT_CROP)
          .build(p, new ResourceLocation(RootsAPI.MODID, "living_pickaxe")))

      .register();

  public static final ItemEntry<Item> LIVING_SHOVEL = REGISTRATE.item("living_shovel", Item::new)
      .model(subfolder("tools"))
      .register();

  public static final ItemEntry<Item> LIVING_SWORD = REGISTRATE.item("living_sword", Item::new)
      .model(subfolder("tools"))
      .register();

  public static final ItemEntry<Item> PESTLE = REGISTRATE.item("pestle", Item::new)
      .model(subfolder("tools"))
      .tag(RootsAPI.Tags.Items.MORTAR_ACTIVATION)
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
      .register();

  public static final ItemEntry<CastingItem> STAFF = REGISTRATE.item("staff", CastingItem::new)
      // TODO: CUSTOM MODEL
      .model(subfolder("tools"))
      .tag(RootsAPI.Tags.Items.CASTING_TOOLS)
      .register();

  public static final ItemEntry<Item> TERRASTONE_AXE = REGISTRATE.item("terrastone_axe", Item::new)
      .model(subfolder("tools"))
      .register();

  public static final ItemEntry<Item> TERRASTONE_HOE = REGISTRATE.item("terrastone_hoe", Item::new)
      .model(subfolder("tools"))
      .register();

  public static final ItemEntry<Item> TERRASTONE_PICKAXE = REGISTRATE.item("terrastone_pickaxe", Item::new)
      .model(subfolder("tools"))
      .register();

  public static final ItemEntry<Item> TERRASTONE_SHOVEL = REGISTRATE.item("terrastone_shovel", Item::new)
      .model(subfolder("tools"))
      .register();

  public static final ItemEntry<Item> TERRASTONE_SWORD = REGISTRATE.item("terrastone_sword", Item::new)
      .model(subfolder("tools"))
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
      .register();

  public static final ItemEntry<Item> RUNIC_DUST = REGISTRATE.item("runic_dust", Item::new)
      .model(subfolder("resources"))
      .recipe((ctx, p) -> MortarRecipe.builder(ctx.getEntry(), 1, 1)
          .addIngredient(RootsAPI.Tags.Items.Blocks.RUNESTONE)
          .build(p, new ResourceLocation(RootsAPI.MODID, "runic_dust")))
      .register();

  public static final ItemEntry<Item> STRANGE_OOZE = REGISTRATE.item("strange_ooze", Item::new)
      .model(subfolder("resources"))
      .register();

  public static final ItemEntry<TokenItem> TOKEN = REGISTRATE.item("token", TokenItem::new)
      .model((ctx, p) -> {
      })
      .register();

  public static void load() {
    Herbs.load();
    Seeds.load();
    Bark.load();
    Pouches.load();
  }
}
