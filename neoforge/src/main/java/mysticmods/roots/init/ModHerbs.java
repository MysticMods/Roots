package mysticmods.roots.init;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.herb.Herb;
import mysticmods.roots.api.registry.RootsRegistries;
import net.minecraft.ChatFormatting;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModHerbs {
  private static final DeferredRegister<Herb> REGISTER = DeferredRegister.create(RootsRegistries.Keys.HERBS, RootsAPI.MODID);

  public static final DeferredHolder<Herb, Herb> GROVE_MOSS = REGISTER.register("grove_moss", () -> new Herb(ModItems.GROVE_MOSS, RootsTags.Items.GROVE_MOSS_HERB, ChatFormatting.GREEN));
  public static final DeferredHolder<Herb, Herb> WILDROOT = REGISTER.register("wildroot", () -> new Herb(ModItems.WILDROOT, RootsTags.Items.WILDROOT_HERB, ChatFormatting.YELLOW));
  public static final DeferredHolder<Herb, Herb> CLOUD_BERRY = REGISTER.register("cloud_berry", () -> new Herb(ModItems.CLOUD_BERRY, RootsTags.Items.CLOUD_BERRY_HERB, ChatFormatting.AQUA));

  public static final DeferredHolder<Herb, Herb> DEWGONIA = REGISTER.register("dewgonia", () -> new Herb(ModItems.DEWGONIA, RootsTags.Items.DEWGONIA_HERB, ChatFormatting.BLUE));
  public static final DeferredHolder<Herb, Herb> INFERNO_BULB = REGISTER.register("inferno_bulb", () -> new Herb(ModItems.INFERNO_BULB, RootsTags.Items.INFERNO_BULB_HERB, ChatFormatting.RED));
  public static final DeferredHolder<Herb, Herb> STALICRIPE = REGISTER.register("stalicripe", () -> new Herb(ModItems.STALICRIPE, RootsTags.Items.STALICRIPE_HERB, ChatFormatting.DARK_RED));
  public static final DeferredHolder<Herb, Herb> MOONGLOW = REGISTER.register("moonglow", () -> new Herb(ModItems.MOONGLOW, RootsTags.Items.MOONGLOW_HERB, ChatFormatting.DARK_PURPLE));
  public static final DeferredHolder<Herb, Herb> PERESKIA = REGISTER.register("pereskia", () -> new Herb(ModItems.PERESKIA, RootsTags.Items.PERESKIA_HERB, ChatFormatting.LIGHT_PURPLE));
  public static final DeferredHolder<Herb, Herb> SPIRITLEAF = REGISTER.register("spiritleaf", () -> new Herb(ModItems.SPIRITLEAF, RootsTags.Items.SPIRITLEAF_HERB, ChatFormatting.DARK_AQUA));
  public static final DeferredHolder<Herb, Herb> WILDEWHEET = REGISTER.register("wildewheet", () -> new Herb(ModItems.WILDEWHEET, RootsTags.Items.WILDEWHEET_HERB, ChatFormatting.GOLD));
  public static final DeferredHolder<Herb, Herb> BAFFLECAP = REGISTER.register("bafflecap", () -> new Herb(ModItems.BAFFLECAP, RootsTags.Items.BAFFLECAP_HERB, ChatFormatting.DARK_GREEN));

  public static void load() {
  }
}
