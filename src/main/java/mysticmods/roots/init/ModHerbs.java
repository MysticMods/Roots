package mysticmods.roots.init;

import com.tterrag.registrate.util.entry.RegistryEntry;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.herb.Herb;
import net.minecraft.ChatFormatting;

import static mysticmods.roots.Roots.REGISTRATE;

public class ModHerbs {
  public static final RegistryEntry<Herb> GROVE_MOSS = REGISTRATE.simple("grove_moss", RootsAPI.HERB_REGISTRY, () -> new Herb(ModItems.GROVE_MOSS, RootsAPI.Tags.Items.Herbs.GROVE_MOSS, ChatFormatting.GREEN));
  public static final RegistryEntry<Herb> WILDROOT = REGISTRATE.simple("wildroot", RootsAPI.HERB_REGISTRY, () -> new Herb(ModItems.WILDROOT, RootsAPI.Tags.Items.Herbs.WILDROOT, ChatFormatting.YELLOW));
  public static final RegistryEntry<Herb> CLOUD_BERRY = REGISTRATE.simple("cloud_berry", RootsAPI.HERB_REGISTRY, () -> new Herb(ModItems.CLOUD_BERRY, RootsAPI.Tags.Items.Herbs.CLOUD_BERRY, ChatFormatting.AQUA));

  public static final RegistryEntry<Herb> DEWGONIA = REGISTRATE.simple("dewgonia", RootsAPI.HERB_REGISTRY, () -> new Herb(ModItems.DEWGONIA, RootsAPI.Tags.Items.Herbs.DEWGONIA, ChatFormatting.BLUE));
  public static final RegistryEntry<Herb> INFERNO_BULB = REGISTRATE.simple("inferno_bulb", RootsAPI.HERB_REGISTRY, () -> new Herb(ModItems.INFERNO_BULB, RootsAPI.Tags.Items.Herbs.INFERNO_BULB, ChatFormatting.RED));
  public static final RegistryEntry<Herb> STALICRIPE = REGISTRATE.simple("stalicripe", RootsAPI.HERB_REGISTRY, () -> new Herb(ModItems.STALICRIPE, RootsAPI.Tags.Items.Herbs.STALICRIPE, ChatFormatting.DARK_RED));
  public static final RegistryEntry<Herb> MOONGLOW = REGISTRATE.simple("moonglow", RootsAPI.HERB_REGISTRY, () -> new Herb(ModItems.MOONGLOW, RootsAPI.Tags.Items.Herbs.MOONGLOW, ChatFormatting.DARK_PURPLE));
  public static final RegistryEntry<Herb> PERESKIA = REGISTRATE.simple("pereskia", RootsAPI.HERB_REGISTRY, () -> new Herb(ModItems.PERESKIA, RootsAPI.Tags.Items.Herbs.PERESKIA, ChatFormatting.LIGHT_PURPLE));
  public static final RegistryEntry<Herb> SPIRITLEAF = REGISTRATE.simple("spiritleaf", RootsAPI.HERB_REGISTRY, () -> new Herb(ModItems.SPIRITLEAF, RootsAPI.Tags.Items.Herbs.SPIRITLEAF, ChatFormatting.DARK_AQUA));
  public static final RegistryEntry<Herb> WILDEWHEET = REGISTRATE.simple("wildewheet", RootsAPI.HERB_REGISTRY, () -> new Herb(ModItems.WILDEWHEET, RootsAPI.Tags.Items.Herbs.WILDEWHEET, ChatFormatting.GOLD));
  public static final RegistryEntry<Herb> BAFFLECAP = REGISTRATE.simple("bafflecap", RootsAPI.HERB_REGISTRY, () -> new Herb(ModItems.BAFFLECAP, RootsAPI.Tags.Items.Herbs.BAFFLECAP, ChatFormatting.DARK_GREEN));

  public static void load() {
  }
}
