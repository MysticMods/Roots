package mysticmods.roots.init;

import mysticmods.roots.action.CropGrowthAction;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.action.GroveAction;
import mysticmods.roots.api.registry.RootsRegistries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModActions {
  private static final DeferredRegister<GroveAction> ACTIONS = DeferredRegister.create(RootsRegistries.Keys.GROVE_ACTIONS, RootsAPI.MODID);

  public static final DeferredHolder<GroveAction, CropGrowthAction> CROP_GROWTH = ACTIONS.register("crop_growth", CropGrowthAction::new);

  public static void register (IEventBus bus) {
    ACTIONS.register(bus);
  }
}
