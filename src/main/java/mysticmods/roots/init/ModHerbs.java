package mysticmods.roots.init;

import com.tterrag.registrate.util.entry.RegistryEntry;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.herbs.Herb;

import static mysticmods.roots.Roots.REGISTRATE;

public class ModHerbs {
  public static final RegistryEntry<Herb> SACRED_MOSS = REGISTRATE.simple("sacred_moss", Herb.class, () -> new Herb(ModItems.Herbs.SACRED_MOSS, RootsAPI.Tags.Items.Herbs.SACRED_MOSS));
  public static final RegistryEntry<Herb> WILDROOT = REGISTRATE.simple("wildroot", Herb.class, () -> new Herb(ModItems.Herbs.WILDROOT, RootsAPI.Tags.Items.Herbs.WILDROOT));
  public static final RegistryEntry<Herb> CLOUD_BERRY = REGISTRATE.simple("cloud_berry", Herb.class, () -> new Herb(ModItems.Herbs.CLOUD_BERRY, RootsAPI.Tags.Items.Herbs.CLOUD_BERRY));
  public static final RegistryEntry<Herb> DEWGONIA = REGISTRATE.simple("dewgonia", Herb.class, () -> new Herb(ModItems.Herbs.DEWGONIA, RootsAPI.Tags.Items.Herbs.DEWGONIA));
  public static final RegistryEntry<Herb> INFERNAL_BULB = REGISTRATE.simple("infernal_bulb", Herb.class, () -> new Herb(ModItems.Herbs.INFERNAL_BULB, RootsAPI.Tags.Items.Herbs.INFERNAL_BULB));
  public static final RegistryEntry<Herb> STALICRIPE = REGISTRATE.simple("stalicripe", Herb.class, () -> new Herb(ModItems.Herbs.STALICRIPE, RootsAPI.Tags.Items.Herbs.STALICRIPE));
  public static final RegistryEntry<Herb> MOONGLOW = REGISTRATE.simple("moonglow", Herb.class, () -> new Herb(ModItems.Herbs.MOONGLOW, RootsAPI.Tags.Items.Herbs.MOONGLOW));
  public static final RegistryEntry<Herb> PERESKIA = REGISTRATE.simple("pereskia", Herb.class, () -> new Herb(ModItems.Herbs.PERESKIA, RootsAPI.Tags.Items.Herbs.PERESKIA));
  public static final RegistryEntry<Herb> SPIRIT_LEAF = REGISTRATE.simple("spirit_leaf", Herb.class, () -> new Herb(ModItems.Herbs.SPIRIT_LEAF, RootsAPI.Tags.Items.Herbs.SPIRIT_LEAF));
  public static final RegistryEntry<Herb> WILDEWHEET = REGISTRATE.simple("wildewheet", Herb.class, () -> new Herb(ModItems.Herbs.WILDEWHEET, RootsAPI.Tags.Items.Herbs.WILDEWHEET));

  public static void load() {
  }
}
