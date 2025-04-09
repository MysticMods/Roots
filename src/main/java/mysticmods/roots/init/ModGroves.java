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

  public static final DeferredHolder<Grove, Grove> PRIMAL = REGISTER.register("primal", () -> new Grove(ChatFormatting.GOLD));
  public static final DeferredHolder<Grove, Grove> FAIRY = REGISTER.register("fairy", () -> new Grove(ChatFormatting.LIGHT_PURPLE));
  public static final DeferredHolder<Grove, Grove> TWILIGHT = REGISTER.register("twilight", () -> new Grove(ChatFormatting.DARK_PURPLE));
  public static final DeferredHolder<Grove, Grove> FUNGAL = REGISTER.register("fungal", () -> new Grove(ChatFormatting.DARK_AQUA));
  public static final DeferredHolder<Grove, Grove> SPROUT = REGISTER.register("sprout", () -> new Grove(ChatFormatting.GREEN));
  public static final DeferredHolder<Grove, Grove> ELEMENTAL = REGISTER.register("elemental", () -> new Grove(ChatFormatting.DARK_RED));
  public static final DeferredHolder<Grove, Grove> WILD = REGISTER.register("wild", () -> new Grove(ChatFormatting.YELLOW));
  public static final DeferredHolder<Grove, Grove> HOLLOW = REGISTER.register("hollow", () -> new Grove(ChatFormatting.DARK_GRAY));

  public static void register(IEventBus bus) {
    REGISTER.register(bus);
  }

}
