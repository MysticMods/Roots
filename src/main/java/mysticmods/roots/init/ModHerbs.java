package mysticmods.roots.init;

import com.tterrag.registrate.util.entry.RegistryEntry;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.herb.Herb;
import net.minecraft.ChatFormatting;

import static mysticmods.roots.Roots.REGISTRATE;

public class ModHerbs {
  public static final RegistryEntry<Herb> GROVE_MOSS = REGISTRATE.simple("grove_moss", RootsAPI.HERB_REGISTRY, () -> new Herb(ModItems.GROVE_MOSS, RootsTags.Items.GROVE_MOSS, ChatFormatting.GREEN));
  public static final RegistryEntry<Herb> WILDROOT = REGISTRATE.simple("wildroot", RootsAPI.HERB_REGISTRY, () -> new Herb(ModItems.WILDROOT, RootsTags.Items.WILDROOT, ChatFormatting.YELLOW));
  public static final RegistryEntry<Herb> CLOUD_BERRY = REGISTRATE.simple("cloud_berry", RootsAPI.HERB_REGISTRY, () -> new Herb(ModItems.CLOUD_BERRY, RootsTags.Items.CLOUD_BERRY, ChatFormatting.AQUA));

  public static final RegistryEntry<Herb> DEWGONIA = REGISTRATE.simple("dewgonia", RootsAPI.HERB_REGISTRY, () -> new Herb(ModItems.DEWGONIA, RootsTags.Items.DEWGONIA, ChatFormatting.BLUE));
  public static final RegistryEntry<Herb> INFERNO_BULB = REGISTRATE.simple("inferno_bulb", RootsAPI.HERB_REGISTRY, () -> new Herb(ModItems.INFERNO_BULB, RootsTags.Items.INFERNO_BULB, ChatFormatting.RED));
  public static final RegistryEntry<Herb> STALICRIPE = REGISTRATE.simple("stalicripe", RootsAPI.HERB_REGISTRY, () -> new Herb(ModItems.STALICRIPE, RootsTags.Items.STALICRIPE, ChatFormatting.DARK_RED));
  public static final RegistryEntry<Herb> MOONGLOW = REGISTRATE.simple("moonglow", RootsAPI.HERB_REGISTRY, () -> new Herb(ModItems.MOONGLOW, RootsTags.Items.MOONGLOW, ChatFormatting.DARK_PURPLE));
  public static final RegistryEntry<Herb> PERESKIA = REGISTRATE.simple("pereskia", RootsAPI.HERB_REGISTRY, () -> new Herb(ModItems.PERESKIA, RootsTags.Items.PERESKIA, ChatFormatting.LIGHT_PURPLE));
  public static final RegistryEntry<Herb> SPIRITLEAF = REGISTRATE.simple("spiritleaf", RootsAPI.HERB_REGISTRY, () -> new Herb(ModItems.SPIRITLEAF, RootsTags.Items.SPIRITLEAF, ChatFormatting.DARK_AQUA));
  public static final RegistryEntry<Herb> WILDEWHEET = REGISTRATE.simple("wildewheet", RootsAPI.HERB_REGISTRY, () -> new Herb(ModItems.WILDEWHEET, RootsTags.Items.WILDEWHEET, ChatFormatting.GOLD));
  public static final RegistryEntry<Herb> BAFFLECAP = REGISTRATE.simple("bafflecap", RootsAPI.HERB_REGISTRY, () -> new Herb(ModItems.BAFFLECAP, RootsTags.Items.BAFFLECAP, ChatFormatting.DARK_GREEN));

  public static void load() {
  }
}
