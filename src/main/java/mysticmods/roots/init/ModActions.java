package mysticmods.roots.init;

import mysticmods.roots.action.*;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.action.GroveAction;
import mysticmods.roots.api.registry.RootsRegistries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModActions {
  private static final DeferredRegister<GroveAction> ACTIONS = DeferredRegister.create(RootsRegistries.Keys.GROVE_ACTIONS, RootsAPI.MODID);

  public static final DeferredHolder<GroveAction, CropGrowthAction> CROP_GROWTH = ACTIONS.register("crop_growth", CropGrowthAction::new);
  public static final DeferredHolder<GroveAction, SpellCastAction> SPELL_CAST = ACTIONS.register("spell_cast", SpellCastAction::new);
  public static final DeferredHolder<GroveAction, StartRitualAction> START_RITUAL = ACTIONS.register("start_ritual", StartRitualAction::new);
  public static final DeferredHolder<GroveAction, CraftRecipeAction> CRAFT_RECIPE = ACTIONS.register("craft_recipe", CraftRecipeAction::new);
  public static final DeferredHolder<GroveAction, CraftItemAction> CRAFT_ITEM = ACTIONS.register("craft_item", CraftItemAction::new);

  public static void register (IEventBus bus) {
    ACTIONS.register(bus);
  }
}
