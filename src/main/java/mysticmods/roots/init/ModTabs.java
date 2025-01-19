package mysticmods.roots.init;

import mysticmods.roots.api.RootsAPI;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModTabs {
  private static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, RootsAPI.MODID);

  public static final DeferredHolder<CreativeModeTab, CreativeModeTab> ROOTS_TAB = CREATIVE_MODE_TABS.register("roots", () -> CreativeModeTab.builder()
      .title(Component.translatable("itemGroup.roots"))
      .icon(() -> new ItemStack(ModItems.WILDROOT.get()))
      .build());

  public static final DeferredHolder<CreativeModeTab, CreativeModeTab> SPELLS_TAB = CREATIVE_MODE_TABS.register("roots_spells", () -> CreativeModeTab.builder()
      .title(Component.translatable("itemGroup.roots_spells"))
      .icon(() -> new ItemStack(ModItems.STAFF.get()))
      .build());

  public static final DeferredHolder<CreativeModeTab, CreativeModeTab> RITUALS_TAB = CREATIVE_MODE_TABS.register("roots_rituals", () -> CreativeModeTab.builder()
      .title(Component.translatable("itemGroup.roots_rituals"))
      .icon(() -> new ItemStack(ModItems.PYRE.get()))
      .build());

  public static void register(IEventBus bus) {
    CREATIVE_MODE_TABS.register(bus);
  }
}
