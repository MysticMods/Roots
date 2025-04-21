package mysticmods.roots.init;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.grove.Grove;
import mysticmods.roots.api.registry.RootsRegistries;
import net.minecraft.ChatFormatting;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModGroves {
  private static final DeferredRegister<Grove> REGISTER = DeferredRegister.create(RootsRegistries.Keys.GROVES, RootsAPI.MODID);

  public static final DeferredHolder<Grove, Grove> PRIMAL = REGISTER.register("primal", () -> new Grove(ChatFormatting.GOLD, 0x82d5ac, 0x17b86d));
  public static final DeferredHolder<Grove, Grove> FAIRY = REGISTER.register("fairy", () -> new Grove(ChatFormatting.LIGHT_PURPLE, 0xe38192, 0xdab7ca));
  public static final DeferredHolder<Grove, Grove> TWILIGHT = REGISTER.register("twilight", () -> new Grove(ChatFormatting.DARK_PURPLE, 0x99e5ff, 0x6d62ff));
  public static final DeferredHolder<Grove, Grove> FUNGAL = REGISTER.register("fungal", () -> new Grove(ChatFormatting.DARK_AQUA, 0xcca3a7, 0x8b7173));
  public static final DeferredHolder<Grove, Grove> SPROUTING = REGISTER.register("sprouting", () -> new Grove(ChatFormatting.GREEN, 0xdbac39, 0xd4d887));
  static {
    REGISTER.addAlias(RootsAPI.rl("sprout"), RootsAPI.rl("sprouting"));
  }
  public static final DeferredHolder<Grove, Grove> ELEMENTAL = REGISTER.register("elemental", () -> new Grove(ChatFormatting.DARK_RED, 0xffe98e, 0x80f7ff));
  public static final DeferredHolder<Grove, Grove> WILD = REGISTER.register("wild", () -> new Grove(ChatFormatting.YELLOW, 0x71d443, 0xb6cf89));
  public static final DeferredHolder<Grove, Grove> HOLLOW = REGISTER.register("hollow", () -> new Grove(ChatFormatting.DARK_GRAY, 0x000000, 0x000000));

  public static void register(IEventBus bus) {
    REGISTER.register(bus);
  }

}
