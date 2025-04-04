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
  public static final DeferredHolder<GroveAction, BredAnimalAction> BRED_ANIMAL = ACTIONS.register("bred_animal", BredAnimalAction::new);
  public static final DeferredHolder<GroveAction, ShatterBlockAction> SHATTER_BLOCK = ACTIONS.register("shatter_block", ShatterBlockAction::new);
  public static final DeferredHolder<GroveAction, KillEntityAction> KILL_ENTITY = ACTIONS.register("kill_entity", KillEntityAction::new);
  public static final DeferredHolder<GroveAction, TameAnimalAction> TAME_ANIMAL = ACTIONS.register("tame_animal", TameAnimalAction::new);
  public static final DeferredHolder<GroveAction, TradeVillagerAction> TRADE_VILLAGER = ACTIONS.register("trade_villager", TradeVillagerAction::new);
  public static final DeferredHolder<GroveAction, CureVillagerAction> CURE_VILLAGER = ACTIONS.register("cure_villager", CureVillagerAction::new);
  public static final DeferredHolder<GroveAction, TradePiglinAction> TRADE_PIGLIN = ACTIONS.register("trade_piglin", TradePiglinAction::new);
  public static final DeferredHolder<GroveAction, EatItemAction> EAT_ITEM = ACTIONS.register("eat_item", EatItemAction::new);

  public static void register (IEventBus bus) {
    ACTIONS.register(bus);
  }
}
