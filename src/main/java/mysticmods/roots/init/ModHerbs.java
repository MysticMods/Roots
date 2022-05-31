package mysticmods.roots.init;

import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import mysticmods.roots.RootsTags;
import mysticmods.roots.api.herbs.Herb;
import mysticmods.roots.api.herbs.IHerb;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;

import static mysticmods.roots.Roots.REGISTRATE;

public class ModHerbs {
  public static final RegistryEntry<Herb> WILDROOT = REGISTRATE.simple("wildroot", IHerb.class, () -> new Herb(ModItems.Herbs.WILDROOT));
  public static final RegistryEntry<Herb> CLOUD_BERRY = REGISTRATE.simple("cloud_berry", IHerb.class, () -> new Herb(ModItems.Herbs.CLOUD_BERRY));
  public static final RegistryEntry<Herb> DEWGONIA = REGISTRATE.simple("dewgonia", IHerb.class, () -> new Herb(ModItems.Herbs.DEWGONIA));
  public static final RegistryEntry<Herb> INFERNAL_BULB = REGISTRATE.simple("infernal_bulb", IHerb.class, () -> new Herb(ModItems.Herbs.INFERNAL_BULB));
  public static final RegistryEntry<Herb> STALICRIPE = REGISTRATE.simple("stalicripe", IHerb.class, () -> new Herb(ModItems.Herbs.STALICRIPE));
  public static final RegistryEntry<Herb> MOONGLOW_LEAF = REGISTRATE.simple("moonglow_leaf", IHerb.class, () -> new Herb(ModItems.Herbs.MOONGLOW_LEAF));
  public static final RegistryEntry<Herb> PERESKIA = REGISTRATE.simple("pereskia", IHerb.class, () -> new Herb(ModItems.Herbs.PERESKIA));
  public static final RegistryEntry<Herb> SPIRIT_HERB = REGISTRATE.simple("spirit_herb", IHerb.class, () -> new Herb(ModItems.Herbs.SPIRIT_HERB));
  public static final RegistryEntry<Herb> WILDEWHEET = REGISTRATE.simple("wildewheet", IHerb.class, () -> new Herb(ModItems.Herbs.WILDEWHEET));

  public static void load() {
  }
}
