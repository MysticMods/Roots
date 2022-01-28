package mysticmods.roots.init;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateItemModelProvider;
import com.tterrag.registrate.util.entry.ItemEntry;
import mysticmods.roots.RootsTags;
import mysticmods.roots.api.RootsAPI;
import net.minecraft.item.BlockNamedItem;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ItemModelBuilder;

import static mysticmods.roots.Roots.REGISTRATE;

public class ModItems {
  private static <T extends Item> ItemModelBuilder cropModel(DataGenContext<Item, T> ctx, RegistrateItemModelProvider p) {
    return p.generated(ctx::getEntry, new ResourceLocation(RootsAPI.MODID, "item/herbs/" + ctx.getName()));
  }

  public static class Herbs {
    public static ItemEntry<BlockNamedItem> WILDROOT = REGISTRATE.item("wildroot", (p) -> new BlockNamedItem(ModBlocks.Crops.WILDROOT_CROP.get(), p))
        .model(ModItems::cropModel)
        .tag(RootsTags.Items.WILDROOT_SEEDS, RootsTags.Items.WILDROOT_CROP)
        .register();
    public static ItemEntry<BlockNamedItem> CLOUD_BERRY = REGISTRATE.item("cloud_berry", (p) -> new BlockNamedItem(ModBlocks.Crops.CLOUD_BERRY_CROP.get(), p))
        .model(ModItems::cropModel)
        .tag(RootsTags.Items.CLOUD_BERRY_SEEDS, RootsTags.Items.CLOUD_BERRY_CROP)
        .register();
    public static ItemEntry<BlockNamedItem> DEWGONIA = REGISTRATE.item("dewgonia", (p) -> new BlockNamedItem(ModBlocks.Crops.DEWGONIA_CROP.get(), p))
        .model(ModItems::cropModel)
        .tag(RootsTags.Items.DEWGONIA_SEEDS, RootsTags.Items.DEWGONIA_CROP)
        .register();
    public static ItemEntry<BlockNamedItem> INFERNAL_BULB = REGISTRATE.item("infernal_bulb", (p) -> new BlockNamedItem(ModBlocks.Crops.INFERNAL_BULB_CROP.get(), p))
        .model(ModItems::cropModel)
        .tag(RootsTags.Items.INFERNAL_BULB_SEEDS, RootsTags.Items.INFERNAL_BULB_CROP)
        .register();
    public static ItemEntry<BlockNamedItem> STALICRIPE = REGISTRATE.item("stalicripe", (p) -> new BlockNamedItem(ModBlocks.Crops.STALICRIPE_CROP.get(), p))
        .model(ModItems::cropModel)
        .tag(RootsTags.Items.STALICRIPE_SEEDS, RootsTags.Items.STALICRIPE_CROP)
        .register();
    public static ItemEntry<Item> MOONGLOW_LEAF = REGISTRATE.item("moonglow_leaf", Item::new)
        .model(ModItems::cropModel)
        .tag(RootsTags.Items.MOONGLOW_LEAF_CROP)
        .register();
    public static ItemEntry<Item> PERESKIA = REGISTRATE.item("pereskia", Item::new)
        .model(ModItems::cropModel)
        .tag(RootsTags.Items.PERESKIA_CROP)
        .register();
    public static ItemEntry<Item> SPIRIT_HERB = REGISTRATE.item("spirit_herb", Item::new)
        .model(ModItems::cropModel)
        .tag(RootsTags.Items.SPIRIT_HERB_CROP)
        .register();
    public static ItemEntry<Item> WILDEWHEET = REGISTRATE.item("wildewheet", Item::new)
        .model(ModItems::cropModel)
        .tag(RootsTags.Items.WILDEWHEET_CROP)
        .register();

    public static void load() {

    }
  }

  public static class Seeds {

    public static ItemEntry<BlockNamedItem> MOONGLOW_LEAF_SEEDS = REGISTRATE.item("moonglow_leaf_seeds", (p) -> new BlockNamedItem(ModBlocks.Crops.MOONGLOW_LEAF_CROP.get(), p))
        .model(ModItems::cropModel)
        .tag(RootsTags.Items.MOONGLOW_LEAF_SEEDS)
        .register();
    public static ItemEntry<BlockNamedItem> PERESKIA_BULB = REGISTRATE.item("pereskia_bulb", (p) -> new BlockNamedItem(ModBlocks.Crops.PERESKIA_CROP.get(), p))
        .model(ModItems::cropModel)
        .tag(RootsTags.Items.PERESKIA_SEEDS)
        .register();
    public static ItemEntry<BlockNamedItem> SPIRIT_HERB_SEEDS = REGISTRATE.item("spirit_herb_seeds", (p) -> new BlockNamedItem(ModBlocks.Crops.SPIRIT_HERB_CROP.get(), p))
        .model(ModItems::cropModel)
        .tag(RootsTags.Items.SPIRIT_HERB_SEEDS)
        .register();
    public static ItemEntry<BlockNamedItem> WILDEWHEET_SEEDS = REGISTRATE.item("wildewheet_seeds", (p) -> new BlockNamedItem(ModBlocks.Crops.WILDEWHEET_CROP.get(), p))
        .model(ModItems::cropModel)
        .tag(RootsTags.Items.WILDEWHEET_SEEDS)
        .register();

    public static void load() {

    }
  }

  public static void load() {
    Herbs.load();
    Seeds.load();
  }
}
