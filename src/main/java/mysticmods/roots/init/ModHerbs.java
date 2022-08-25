package mysticmods.roots.init;

import com.tterrag.registrate.util.entry.RegistryEntry;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.herbs.Herb;

import static mysticmods.roots.Roots.REGISTRATE;

public class ModHerbs {
  public static final RegistryEntry<Herb> GROVE_MOSS = REGISTRATE.simple("grove_moss", Herb.class, () -> new Herb(ModItems.Herbs.GROVE_MOSS, RootsAPI.Tags.Items.Herbs.GROVE_MOSS));
  public static final RegistryEntry<Herb> WILDROOT = REGISTRATE.simple("wildroot", Herb.class, () -> new Herb(ModItems.Herbs.WILDROOT, RootsAPI.Tags.Items.Herbs.WILDROOT));
  public static final RegistryEntry<Herb> CLOUD_BERRY = REGISTRATE.simple("cloud_berry", Herb.class, () -> new Herb(ModItems.Herbs.CLOUD_BERRY, RootsAPI.Tags.Items.Herbs.CLOUD_BERRY));
  public static final RegistryEntry<Herb> DEWGONIA = REGISTRATE.simple("dewgonia", Herb.class, () -> new Herb(ModItems.Herbs.DEWGONIA, RootsAPI.Tags.Items.Herbs.DEWGONIA));
  public static final RegistryEntry<Herb> INFERNO_BULB = REGISTRATE.simple("inferno_bulb", Herb.class, () -> new Herb(ModItems.Herbs.INFERNO_BULB, RootsAPI.Tags.Items.Herbs.INFERNO_BULB));
  public static final RegistryEntry<Herb> STALICRIPE = REGISTRATE.simple("stalicripe", Herb.class, () -> new Herb(ModItems.Herbs.STALICRIPE, RootsAPI.Tags.Items.Herbs.STALICRIPE));
  public static final RegistryEntry<Herb> MOONGLOW = REGISTRATE.simple("moonglow", Herb.class, () -> new Herb(ModItems.Herbs.MOONGLOW, RootsAPI.Tags.Items.Herbs.MOONGLOW));
  public static final RegistryEntry<Herb> PERESKIA = REGISTRATE.simple("pereskia", Herb.class, () -> new Herb(ModItems.Herbs.PERESKIA, RootsAPI.Tags.Items.Herbs.PERESKIA));
  public static final RegistryEntry<Herb> SPIRITLEAF = REGISTRATE.simple("spiritleaf", Herb.class, () -> new Herb(ModItems.Herbs.SPIRITLEAF, RootsAPI.Tags.Items.Herbs.SPIRITLEAF));
  public static final RegistryEntry<Herb> WILDEWHEET = REGISTRATE.simple("wildewheet", Herb.class, () -> new Herb(ModItems.Herbs.WILDEWHEET, RootsAPI.Tags.Items.Herbs.WILDEWHEET));
  public static final RegistryEntry<Herb> BAFFLECAP = REGISTRATE.simple("bafflecap", Herb.class, () -> new Herb(ModItems.Herbs.BAFFLECAP, RootsAPI.Tags.Items.Herbs.BAFFLECAP));

  public static void load() {
  }
}
