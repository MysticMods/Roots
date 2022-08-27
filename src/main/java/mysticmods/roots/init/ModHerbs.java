package mysticmods.roots.init;

import com.tterrag.registrate.util.entry.RegistryEntry;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.herbs.Herb;
import net.minecraft.ChatFormatting;

import static mysticmods.roots.Roots.REGISTRATE;

public class ModHerbs {
  public static final RegistryEntry<Herb> GROVE_MOSS = REGISTRATE.simple("grove_moss", Herb.class, () -> new Herb(ModItems.Herbs.GROVE_MOSS, RootsAPI.Tags.Items.Herbs.GROVE_MOSS, ChatFormatting.GREEN));
  public static final RegistryEntry<Herb> WILDROOT = REGISTRATE.simple("wildroot", Herb.class, () -> new Herb(ModItems.Herbs.WILDROOT, RootsAPI.Tags.Items.Herbs.WILDROOT, ChatFormatting.YELLOW));
  public static final RegistryEntry<Herb> CLOUD_BERRY = REGISTRATE.simple("cloud_berry", Herb.class, () -> new Herb(ModItems.Herbs.CLOUD_BERRY, RootsAPI.Tags.Items.Herbs.CLOUD_BERRY, ChatFormatting.AQUA));

  public static final RegistryEntry<Herb> DEWGONIA = REGISTRATE.simple("dewgonia", Herb.class, () -> new Herb(ModItems.Herbs.DEWGONIA, RootsAPI.Tags.Items.Herbs.DEWGONIA, ChatFormatting.BLUE));
  public static final RegistryEntry<Herb> INFERNO_BULB = REGISTRATE.simple("inferno_bulb", Herb.class, () -> new Herb(ModItems.Herbs.INFERNO_BULB, RootsAPI.Tags.Items.Herbs.INFERNO_BULB, ChatFormatting.RED));
  public static final RegistryEntry<Herb> STALICRIPE = REGISTRATE.simple("stalicripe", Herb.class, () -> new Herb(ModItems.Herbs.STALICRIPE, RootsAPI.Tags.Items.Herbs.STALICRIPE, ChatFormatting.DARK_RED));
  public static final RegistryEntry<Herb> MOONGLOW = REGISTRATE.simple("moonglow", Herb.class, () -> new Herb(ModItems.Herbs.MOONGLOW, RootsAPI.Tags.Items.Herbs.MOONGLOW, ChatFormatting.DARK_PURPLE));
  public static final RegistryEntry<Herb> PERESKIA = REGISTRATE.simple("pereskia", Herb.class, () -> new Herb(ModItems.Herbs.PERESKIA, RootsAPI.Tags.Items.Herbs.PERESKIA, ChatFormatting.LIGHT_PURPLE));
  public static final RegistryEntry<Herb> SPIRITLEAF = REGISTRATE.simple("spiritleaf", Herb.class, () -> new Herb(ModItems.Herbs.SPIRITLEAF, RootsAPI.Tags.Items.Herbs.SPIRITLEAF, ChatFormatting.DARK_AQUA));

  public static final RegistryEntry<Herb> WILDEWHEET = REGISTRATE.simple("wildewheet", Herb.class, () -> new Herb(ModItems.Herbs.WILDEWHEET, RootsAPI.Tags.Items.Herbs.WILDEWHEET, ChatFormatting.GOLD));
  public static final RegistryEntry<Herb> BAFFLECAP = REGISTRATE.simple("bafflecap", Herb.class, () -> new Herb(ModItems.Herbs.BAFFLECAP, RootsAPI.Tags.Items.Herbs.BAFFLECAP, ChatFormatting.DARK_GREEN));

  public static void load() {
  }
}
