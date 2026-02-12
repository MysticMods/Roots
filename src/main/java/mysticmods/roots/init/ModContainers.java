package mysticmods.roots.init;

import mysticmods.roots.inventory.fake.mortar.MortarContainer;
import mysticmods.roots.inventory.pouch.apothecary.ApothecaryPouchContainer;
import mysticmods.roots.inventory.pouch.component.ComponentPouchContainer;
import mysticmods.roots.inventory.pouch.herb.HerbPouchContainer;
import mysticmods.roots.inventory.pouch.sylvan.SylvanPouchContainer;
import mysticmods.roots.inventory.quiver.QuiverContainer;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModContainers {
  private static final DeferredRegister<MenuType<?>> REGISTER = DeferredRegister.create(Registries.MENU, "roots");

  public static final DeferredHolder<MenuType<?>, MenuType<HerbPouchContainer>> HERB_POUCH = REGISTER.register("herb_pouch", () -> new MenuType<>(HerbPouchContainer::new, FeatureFlags.REGISTRY.allFlags()));
  public static final DeferredHolder<MenuType<?>, MenuType<ApothecaryPouchContainer>> APOTHECARY_POUCH = REGISTER.register("apothecary_pouch", () -> new MenuType<>(ApothecaryPouchContainer::new, FeatureFlags.REGISTRY.allFlags()));
  public static final DeferredHolder<MenuType<?>, MenuType<ComponentPouchContainer>> COMPONENT_POUCH = REGISTER.register("component_pouch", () -> new MenuType<>(ComponentPouchContainer::new, FeatureFlags.REGISTRY.allFlags()));
  public static final DeferredHolder<MenuType<?>, MenuType<SylvanPouchContainer>> SYLVAN_POUCH = REGISTER.register("sylvan_pouch", () -> new MenuType<>(SylvanPouchContainer::new, FeatureFlags.REGISTRY.allFlags()));
  public static final DeferredHolder<MenuType<?>, MenuType<QuiverContainer>> QUIVER = REGISTER.register("quiver", () -> new MenuType<>(QuiverContainer::new, FeatureFlags.REGISTRY.allFlags()));
  public static final DeferredHolder<MenuType<?>, MenuType<MortarContainer>> MORTAR = REGISTER.register("mortar", () -> IMenuTypeExtension.create(MortarContainer::new));

  public static void register(IEventBus bus) {
    REGISTER.register(bus);
  }
}
