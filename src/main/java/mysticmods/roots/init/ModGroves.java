package mysticmods.roots.init;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.condition.GroveType;
import mysticmods.roots.api.grove.Grove;
import mysticmods.roots.api.reference.Colors;
import mysticmods.roots.api.reference.Groves;
import mysticmods.roots.api.registry.RootsRegistries;
import net.minecraft.ChatFormatting;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModGroves {
  private static final DeferredRegister<Grove> REGISTER = DeferredRegister.create(RootsRegistries.Keys.GROVES, RootsAPI.MODID);

  public static final DeferredHolder<Grove, Grove> WILD = REGISTER.register("wild", () -> new Grove(new Grove.Properties(Groves.WILD)
      .type(GroveType.WILD)
      .color(Colors.WILD, 0x82d5ac, 0x17b86d)));

  public static final DeferredHolder<Grove, Grove> UNITY = REGISTER.register("unity", () -> new Grove(new Grove.Properties(Groves.UNITY)
      .type(GroveType.UNITY)
      .color(Colors.UNITY, 0xe38192, 0xdab7ca)));

  public static final DeferredHolder<Grove, Grove> TWILIGHT = REGISTER.register("twilight", () -> new Grove(new Grove.Properties(Groves.TWILIGHT)
      .type(GroveType.TWILIGHT)
      .color(Colors.TWILIGHT, 0x99e5ff, 0x6d62ff)));

  public static final DeferredHolder<Grove, Grove> FUNGAL = REGISTER.register("fungal", () -> new Grove(new Grove.Properties(Groves.FUNGAL)
      .type(GroveType.FUNGAL)
      .color(Colors.FUNGAL, 0xcca3a7, 0x8b7173)));

  public static final DeferredHolder<Grove, Grove> CULTIVATION = REGISTER.register("cultivation", () -> new Grove(new Grove.Properties(Groves.CULTIVATION)
      .type(GroveType.CULTIVATION)
      .color(Colors.CULTIVATION, 0xdbac39, 0xd4d887)));

  public static final DeferredHolder<Grove, Grove> ELEMENTAL = REGISTER.register("elemental", () -> new Grove(new Grove.Properties(Groves.ELEMENTAL)
      .type(GroveType.ELEMENTAL)
      .color(Colors.ELEMENTAL, 0xffe98e, 0x80f7ff)));

  public static final DeferredHolder<Grove, Grove> ANIMATE = REGISTER.register("animate", () -> new Grove(new Grove.Properties(Groves.ANIMATE)
      .type(GroveType.ANIMATE)
      .color(Colors.ANIMATE, 0x71d443, 0xb6cf89)));

  public static final DeferredHolder<Grove, Grove> HOLLOW = REGISTER.register("hollow", () -> new Grove(new Grove.Properties(Groves.HOLLOW)
      .type(GroveType.HOLLOW)
      .color(ChatFormatting.DARK_GRAY.getColor(), 0x000000, 0x000000)));

  static {
    REGISTER.addAlias(RootsAPI.rl("primal"), RootsAPI.rl("animate"));
    REGISTER.addAlias(RootsAPI.rl("sprout"), RootsAPI.rl("cultivation"));
    REGISTER.addAlias(RootsAPI.rl("sprouting"), RootsAPI.rl("cultivation"));
    REGISTER.addAlias(RootsAPI.rl("fairy"), RootsAPI.rl("unity"));
  }

  public static void register(IEventBus bus) {
    REGISTER.register(bus);
  }

}
