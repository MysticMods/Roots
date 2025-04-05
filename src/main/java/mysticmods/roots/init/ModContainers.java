package mysticmods.roots.init;

import mysticmods.roots.inventory.HerbPouchContainer;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModContainers {
  private static final DeferredRegister<MenuType<?>> REGISTER = DeferredRegister.create(Registries.MENU, "roots");

  public static void register(IEventBus bus) {
    REGISTER.register(bus);
  }  public static final DeferredHolder<MenuType<?>, MenuType<HerbPouchContainer>> HERB_POUCH = REGISTER.register("herb_pouch", () -> new MenuType<>(HerbPouchContainer::new, FeatureFlags.REGISTRY.allFlags()));


}
