package mysticmods.roots.init;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.condition.GroveType;
import mysticmods.roots.api.grove.Grove;
import mysticmods.roots.api.registry.RootsRegistries;
import net.minecraft.ChatFormatting;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModGroves {
  private static final DeferredRegister<Grove> REGISTER = DeferredRegister.create(RootsRegistries.Keys.GROVES, RootsAPI.MODID);

  // W, F, T, F, C, E, P, H

  // A, B, D, G, I, J, K, L, M, N, O, P, Q, R, S, V, X, Y, Z

  public static final DeferredHolder<Grove, Grove> WILD = REGISTER.register("wild", () -> new Grove(GroveType.WILD, ChatFormatting.GOLD, 0x82d5ac, 0x17b86d));

  // TODO: Rename
  public static final DeferredHolder<Grove, Grove> FAIRY = REGISTER.register("fairy", () -> new Grove(GroveType.FAIRY, ChatFormatting.LIGHT_PURPLE, 0xe38192, 0xdab7ca));
  public static final DeferredHolder<Grove, Grove> TWILIGHT = REGISTER.register("twilight", () -> new Grove(GroveType.TWILIGHT, ChatFormatting.DARK_PURPLE, 0x99e5ff, 0x6d62ff));
  public static final DeferredHolder<Grove, Grove> FUNGAL = REGISTER.register("fungal", () -> new Grove(GroveType.FUNGAL, ChatFormatting.DARK_AQUA, 0xcca3a7, 0x8b7173));
  public static final DeferredHolder<Grove, Grove> CULTIVATION = REGISTER.register("cultivation", () -> new Grove(GroveType.CULTIVATION, ChatFormatting.GREEN, 0xdbac39, 0xd4d887));

  static {
    REGISTER.addAlias(RootsAPI.rl("sprout"), RootsAPI.rl("cultivation"));
    REGISTER.addAlias(RootsAPI.rl("sprouting"), RootsAPI.rl("cultivation"));
  }

  public static final DeferredHolder<Grove, Grove> ELEMENTAL = REGISTER.register("elemental", () -> new Grove(GroveType.ELEMENTAL, ChatFormatting.DARK_RED, 0xffe98e, 0x80f7ff));

  // TODO: Rename
  public static final DeferredHolder<Grove, Grove> PASTORAL = REGISTER.register("pastoral", () -> new Grove(GroveType.PASTORAL, ChatFormatting.YELLOW, 0x71d443, 0xb6cf89));

  static {
    REGISTER.addAlias(RootsAPI.rl("primal"), RootsAPI.rl("pastoral"));
  }

  public static final DeferredHolder<Grove, Grove> HOLLOW = REGISTER.register("hollow", () -> new Grove(GroveType.HOLLOW, ChatFormatting.DARK_GRAY, 0x000000, 0x000000));

  public static void register(IEventBus bus) {
    REGISTER.register(bus);
  }

}
