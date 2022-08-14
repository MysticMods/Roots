package mysticmods.roots.init;

import com.tterrag.registrate.util.entry.RegistryEntry;
import mysticmods.roots.RootsTags;
import mysticmods.roots.api.herbs.Herb;

import static mysticmods.roots.Roots.REGISTRATE;

public class ModHerbs {
  public static final RegistryEntry<Herb> SACRED_MOSS = REGISTRATE.simple("sacred_moss", Herb.class, () -> new Herb(ModItems.Herbs.SACRED_MOSS, RootsTags.Herbs.SACRED_MOSS));
  public static final RegistryEntry<Herb> WILDROOT = REGISTRATE.simple("wildroot", Herb.class, () -> new Herb(ModItems.Herbs.WILDROOT, RootsTags.Herbs.WILDROOT));
  public static final RegistryEntry<Herb> CLOUD_BERRY = REGISTRATE.simple("cloud_berry", Herb.class, () -> new Herb(ModItems.Herbs.CLOUD_BERRY, RootsTags.Herbs.CLOUD_BERRY));
  public static final RegistryEntry<Herb> DEWGONIA = REGISTRATE.simple("dewgonia", Herb.class, () -> new Herb(ModItems.Herbs.DEWGONIA, RootsTags.Herbs.DEWGONIA));
  public static final RegistryEntry<Herb> INFERNAL_BULB = REGISTRATE.simple("infernal_bulb", Herb.class, () -> new Herb(ModItems.Herbs.INFERNAL_BULB, RootsTags.Herbs.INFERNAL_BULB));
  public static final RegistryEntry<Herb> STALICRIPE = REGISTRATE.simple("stalicripe", Herb.class, () -> new Herb(ModItems.Herbs.STALICRIPE, RootsTags.Herbs.STALICRIPE));
  public static final RegistryEntry<Herb> MOONGLOW = REGISTRATE.simple("moonglow", Herb.class, () -> new Herb(ModItems.Herbs.MOONGLOW, RootsTags.Herbs.MOONGLOW));
  public static final RegistryEntry<Herb> PERESKIA = REGISTRATE.simple("pereskia", Herb.class, () -> new Herb(ModItems.Herbs.PERESKIA, RootsTags.Herbs.PERESKIA));
  public static final RegistryEntry<Herb> SPROUTNIP = REGISTRATE.simple("sproutnip", Herb.class, () -> new Herb(ModItems.Herbs.SPROUTNIP, RootsTags.Herbs.SPROUTNIP));
  public static final RegistryEntry<Herb> WILDEWHEET = REGISTRATE.simple("wildewheet", Herb.class, () -> new Herb(ModItems.Herbs.WILDEWHEET, RootsTags.Herbs.WILDEWHEET));

  public static void load() {
  }
}
