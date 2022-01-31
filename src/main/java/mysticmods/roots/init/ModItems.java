package mysticmods.roots.init;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateItemModelProvider;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import mysticmods.roots.RootsTags;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.item.FireStarterItem;
import net.minecraft.item.BlockNamedItem;
import net.minecraft.item.Item;
import net.minecraft.item.ShearsItem;
import net.minecraft.util.ResourceLocation;

import static mysticmods.roots.Roots.REGISTRATE;

public class ModItems {
  private static <T extends Item> NonNullBiConsumer<DataGenContext<Item, T>, RegistrateItemModelProvider> subfolder(String subfolder) {
    return (ctx, p) -> {
      p.generated(ctx::getEntry, new ResourceLocation(RootsAPI.MODID, "item/" + subfolder + "/" + ctx.getName()));
    };
  }

  public static class Herbs {
    public static ItemEntry<BlockNamedItem> WILDROOT = REGISTRATE.item("wildroot", (p) -> new BlockNamedItem(ModBlocks.Crops.WILDROOT_CROP.get(), p))
        .model(subfolder("herbs"))
        .tag(RootsTags.Items.WILDROOT_SEEDS, RootsTags.Items.WILDROOT_CROP)
        .register();
    public static ItemEntry<BlockNamedItem> CLOUD_BERRY = REGISTRATE.item("cloud_berry", (p) -> new BlockNamedItem(ModBlocks.Crops.CLOUD_BERRY_CROP.get(), p))
        .model(subfolder("herbs"))
        .tag(RootsTags.Items.CLOUD_BERRY_SEEDS, RootsTags.Items.CLOUD_BERRY_CROP)
        .register();
    public static ItemEntry<BlockNamedItem> DEWGONIA = REGISTRATE.item("dewgonia", (p) -> new BlockNamedItem(ModBlocks.Crops.DEWGONIA_CROP.get(), p))
        .model(subfolder("herbs"))
        .tag(RootsTags.Items.DEWGONIA_SEEDS, RootsTags.Items.DEWGONIA_CROP)
        .register();
    public static ItemEntry<BlockNamedItem> INFERNAL_BULB = REGISTRATE.item("infernal_bulb", (p) -> new BlockNamedItem(ModBlocks.Crops.INFERNAL_BULB_CROP.get(), p))
        .model(subfolder("herbs"))
        .tag(RootsTags.Items.INFERNAL_BULB_SEEDS, RootsTags.Items.INFERNAL_BULB_CROP)
        .register();
    public static ItemEntry<BlockNamedItem> STALICRIPE = REGISTRATE.item("stalicripe", (p) -> new BlockNamedItem(ModBlocks.Crops.STALICRIPE_CROP.get(), p))
        .model(subfolder("herbs"))
        .tag(RootsTags.Items.STALICRIPE_SEEDS, RootsTags.Items.STALICRIPE_CROP)
        .register();
    public static ItemEntry<Item> MOONGLOW_LEAF = REGISTRATE.item("moonglow_leaf", Item::new)
        .model(subfolder("herbs"))
        .tag(RootsTags.Items.MOONGLOW_LEAF_CROP)
        .register();
    public static ItemEntry<Item> PERESKIA = REGISTRATE.item("pereskia", Item::new)
        .model(subfolder("herbs"))
        .tag(RootsTags.Items.PERESKIA_CROP)
        .register();
    public static ItemEntry<Item> SPIRIT_HERB = REGISTRATE.item("spirit_herb", Item::new)
        .model(subfolder("herbs"))
        .tag(RootsTags.Items.SPIRIT_HERB_CROP)
        .register();
    public static ItemEntry<Item> WILDEWHEET = REGISTRATE.item("wildewheet", Item::new)
        .model(subfolder("herbs"))
        .tag(RootsTags.Items.WILDEWHEET_CROP)
        .register();

    public static void load() {

    }
  }

  public static class Seeds {

    public static ItemEntry<BlockNamedItem> MOONGLOW_LEAF_SEEDS = REGISTRATE.item("moonglow_leaf_seeds", (p) -> new BlockNamedItem(ModBlocks.Crops.MOONGLOW_LEAF_CROP.get(), p))
        .model(subfolder("herbs"))
        .tag(RootsTags.Items.MOONGLOW_LEAF_SEEDS)
        .register();
    public static ItemEntry<BlockNamedItem> PERESKIA_BULB = REGISTRATE.item("pereskia_bulb", (p) -> new BlockNamedItem(ModBlocks.Crops.PERESKIA_CROP.get(), p))
        .model(subfolder("herbs"))
        .tag(RootsTags.Items.PERESKIA_SEEDS)
        .register();
    public static ItemEntry<BlockNamedItem> SPIRIT_HERB_SEEDS = REGISTRATE.item("spirit_herb_seeds", (p) -> new BlockNamedItem(ModBlocks.Crops.SPIRIT_HERB_CROP.get(), p))
        .model(subfolder("herbs"))
        .tag(RootsTags.Items.SPIRIT_HERB_SEEDS)
        .register();
    public static ItemEntry<BlockNamedItem> WILDEWHEET_SEEDS = REGISTRATE.item("wildewheet_seeds", (p) -> new BlockNamedItem(ModBlocks.Crops.WILDEWHEET_CROP.get(), p))
        .model(subfolder("herbs"))
        .tag(RootsTags.Items.WILDEWHEET_SEEDS)
        .register();

    public static void load() {

    }
  }

  public static class Bark {
    public static ItemEntry<Item> ACACIA_BARK = REGISTRATE.item("acacia_bark", Item::new)
        .tag(RootsTags.Items.ACACIA_BARK)
        .model(subfolder("bark"))
        .register();

    public static ItemEntry<Item> BIRCH_BARK = REGISTRATE.item("birch_bark", Item::new)
        .tag(RootsTags.Items.BIRCH_BARK)
        .model(subfolder("bark"))
        .register();

    public static ItemEntry<Item> DARK_OAK_BARK = REGISTRATE.item("dark_oak_bark", Item::new)
        .tag(RootsTags.Items.DARK_OAK_BARK)
        .model(subfolder("bark"))
        .register();

    public static ItemEntry<Item> JUNGLE_BARK = REGISTRATE.item("jungle_bark", Item::new)
        .tag(RootsTags.Items.JUNGLE_BARK)
        .model(subfolder("bark"))
        .register();

    public static ItemEntry<Item> OAK_BARK = REGISTRATE.item("oak_bark", Item::new)
        .tag(RootsTags.Items.OAK_BARK)
        .model(subfolder("bark"))
        .register();

    public static ItemEntry<Item> SPRUCE_BARK = REGISTRATE.item("spruce_bark", Item::new)
        .tag(RootsTags.Items.SPRUCE_BARK)
        .model(subfolder("bark"))
        .register();

    public static ItemEntry<Item> WILDWOOD_BARK = REGISTRATE.item("wildwood_bark", Item::new)
        .tag(RootsTags.Items.WILDWOOD_BARK)
        .model(subfolder("bark"))
        .register();

    public static ItemEntry<Item> CRIMSON_BARK = REGISTRATE.item("crimson_bark", Item::new)
        .tag(RootsTags.Items.CRIMSON_BARK)
        .model(subfolder("bark"))
        .register();

    public static ItemEntry<Item> WARPED_BARK = REGISTRATE.item("warped_bark", Item::new)
        .tag(RootsTags.Items.WARPED_BARK)
        .model(subfolder("bark"))
        .register();

    public static ItemEntry<Item> MIXED_BARK = REGISTRATE.item("mixed_bark", Item::new)
        .tag(RootsTags.Items.MIXED_BARK)
        .model(subfolder("bark"))
        .register();

    public static void load() {

    }
  }

  public static class Pouches {
    public static ItemEntry<Item> APOTHECARY_POUCH = REGISTRATE.item("apothecary_pouch", Item::new)
        .model(subfolder("pouches"))
        .register();

    public static ItemEntry<Item> COMPONENT_POUCH = REGISTRATE.item("component_pouch", Item::new)
        .model(subfolder("pouches"))
        .register();

    public static ItemEntry<Item> CREATIVE_POUCH = REGISTRATE.item("creative_pouch", Item::new)
        .model(subfolder("pouches"))
        .register();

    public static ItemEntry<Item> FEY_POUCH = REGISTRATE.item("fey_pouch", Item::new)
        .model(subfolder("pouches"))
        .register();

    public static ItemEntry<Item> HERB_POUCH = REGISTRATE.item("herb_pouch", Item::new)
        .model(subfolder("pouches"))
        .register();

    public static void load() {
    }
  }

  public static ItemEntry<Item> COOKED_PERESKIA = REGISTRATE.item("cooked_pereskia", Item::new)
      .model(subfolder("food"))
      .register();

  public static ItemEntry<Item> FLOUR = REGISTRATE.item("flour", Item::new)
      .model(subfolder("food"))
      .register();

  public static ItemEntry<Item> WILDEWHEET_BREAD = REGISTRATE.item("wildewheet_bread", Item::new)
      .model(subfolder("food"))
      .register();

  public static ItemEntry<Item> WILDROOT_STEW = REGISTRATE.item("wildroot_stew", Item::new)
      .model(subfolder("food"))
      .register();

  public static ItemEntry<Item> SYLVAN_BOOTS = REGISTRATE.item("sylvan_boots", Item::new)
      .model(subfolder("armor"))
      .register();

  public static ItemEntry<Item> SYLVAN_CHESTPLATE = REGISTRATE.item("sylvan_chestplate", Item::new)
      .model(subfolder("armor"))
      .register();

  public static ItemEntry<Item> SYLVAN_HELMET = REGISTRATE.item("sylvan_helmet", Item::new)
      .model(subfolder("armor"))
      .register();

  public static ItemEntry<Item> SYLVAN_LEGGINGS = REGISTRATE.item("sylvan_leggings", Item::new)
      .model(subfolder("armor"))
      .register();

  public static ItemEntry<Item> WILDWOOD_BOOTS = REGISTRATE.item("wildwood_boots", Item::new)
      .model(subfolder("armor"))
      .register();

  public static ItemEntry<Item> WILDWOOD_CHESTPLATE = REGISTRATE.item("wildwood_chestplate", Item::new)
      .model(subfolder("armor"))
      .register();

  public static ItemEntry<Item> WILDWOOD_HELMET = REGISTRATE.item("wildwood_helmet", Item::new)
      .model(subfolder("armor"))
      .register();

  public static ItemEntry<Item> WILDWOOD_LEGGINGS = REGISTRATE.item("wildwood_leggings", Item::new)
      .model(subfolder("armor"))
      .register();

  public static ItemEntry<FireStarterItem> FIRE_STARTER = REGISTRATE.item("fire_starter", FireStarterItem::new)
      .properties(o -> o.stacksTo(1))
      .model(subfolder("tools"))
      .register();

  public static ItemEntry<Item> GRAMARY = REGISTRATE.item("gramary", Item::new)
      .model(subfolder("tools"))
      .register();

  public static ItemEntry<Item> LIVING_ARROW = REGISTRATE.item("living_arrow", Item::new)
      .model(subfolder("tools"))
      .register();

  public static ItemEntry<Item> LIVING_AXE = REGISTRATE.item("living_axe", Item::new)
      .model(subfolder("tools"))
      .register();

  public static ItemEntry<Item> LIVING_HOE = REGISTRATE.item("living_hoe", Item::new)
      .model(subfolder("tools"))
      .register();

  public static ItemEntry<Item> LIVING_PICKAXE = REGISTRATE.item("living_pickaxe", Item::new)
      .model(subfolder("tools"))
      .register();

  public static ItemEntry<Item> LIVING_SHOVEL = REGISTRATE.item("living_shovel", Item::new)
      .model(subfolder("tools"))
      .register();

  public static ItemEntry<Item> LIViNG_SWORD = REGISTRATE.item("living_sword", Item::new)
      .model(subfolder("tools"))
      .register();

  public static ItemEntry<Item> PESTLE = REGISTRATE.item("pestle", Item::new)
      .model(subfolder("tools"))
      .register();

  public static ItemEntry<Item> RUNED_AXE = REGISTRATE.item("runed_axe", Item::new)
      .model(subfolder("tools"))
      .register();

  public static ItemEntry<Item> RUNED_DAGGER = REGISTRATE.item("runed_dagger", Item::new)
      .model(subfolder("tools"))
      .register();

  public static ItemEntry<Item> RUNED_HOE = REGISTRATE.item("runed_hoe", Item::new)
      .model(subfolder("tools"))
      .register();

  public static ItemEntry<Item> RUNED_SHOVEL = REGISTRATE.item("runed_shovel", Item::new)
      .model(subfolder("tools"))
      .register();

  public static ItemEntry<Item> RUNED_SWORD = REGISTRATE.item("runed_sword", Item::new)
      .model(subfolder("tools"))
      .register();

  public static ItemEntry<Item> RUNIC_SHEARS = REGISTRATE.item("runic_shears", Item::new)
      .model(subfolder("tools"))
      .register();

  public static ItemEntry<Item> STAFF = REGISTRATE.item("staff", Item::new)
      // TODO: CUSTOM MODEL
      .model(subfolder("tools"))
      .register();

  public static ItemEntry<Item> TERRASTONE_AXE = REGISTRATE.item("terrastone_axe", Item::new)
      .model(subfolder("tools"))
      .register();

  public static ItemEntry<Item> TERRASTONE_HOE = REGISTRATE.item("terrastone_hoe", Item::new)
      .model(subfolder("tools"))
      .register();

  public static ItemEntry<Item> TERRASTONE_PICKAXE = REGISTRATE.item("terrastone_pickaxe", Item::new)
      .model(subfolder("tools"))
      .register();

  public static ItemEntry<Item> TERRASTONE_SHOVEL = REGISTRATE.item("terrastone_shovel", Item::new)
      .model(subfolder("tools"))
      .register();

  public static ItemEntry<Item> TERRASTONE_SWORD = REGISTRATE.item("terrastone_sword", Item::new)
      .model(subfolder("tools"))
      .register();

  public static ItemEntry<Item> WILDWOOD_BOW = REGISTRATE.item("wildwood_bow", Item::new)
      // TODO: MODEL, ETC
      .model(subfolder("tools"))
      .register();

  public static ItemEntry<Item> WILDWOOD_QUIVER = REGISTRATE.item("wildwood_quiver", Item::new)
      .model(subfolder("tools"))
      .register();

  public static ItemEntry<ShearsItem> WOODEN_SHEARS = REGISTRATE.item("wooden_shears", ShearsItem::new)
      .properties(o -> o.durability(120))
      .model(subfolder("tools"))
      .register();

  public static ItemEntry<Item> RELIQUARY = REGISTRATE.item("reliquary", Item::new)
      .model(subfolder("containers"))
      .register();

  public static ItemEntry<Item> SPIRIT_BAG = REGISTRATE.item("spirit_bag", Item::new)
      .model(subfolder("containers"))
      .register();

  public static ItemEntry<Item> FEY_LEATHER = REGISTRATE.item("fey_leather", Item::new)
      .model(subfolder("resources"))
      .register();

  public static ItemEntry<Item> GLASS_EYE = REGISTRATE.item("glass_eye", Item::new)
      .model(subfolder("resources"))
      .register();

  public static ItemEntry<Item> LIFE_ESSENCE = REGISTRATE.item("life_essence", Item::new)
      .model(subfolder("resources"))
      .register();

  public static ItemEntry<Item> MYSTIC_FEATHER = REGISTRATE.item("mystic_feather", Item::new)
      .model(subfolder("resources"))
      .register();

  public static ItemEntry<Item> PETALS = REGISTRATE.item("petals", Item::new)
      .model(subfolder("resources"))
      .register();

  public static ItemEntry<Item> RUNIC_DUST = REGISTRATE.item("runic_dust", Item::new)
      .model(subfolder("resources"))
      .register();

  public static ItemEntry<Item> STRANGE_OOZE = REGISTRATE.item("strange_ooze", Item::new)
      .model(subfolder("resources"))
      .register();

  public static void load() {
    Herbs.load();
    Seeds.load();
    Bark.load();
    Pouches.load();
  }
}
