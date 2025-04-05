package mysticmods.roots.init;

import mysticmods.roots.advancements.PacifistTrigger;
import mysticmods.roots.api.RootsAPI;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModAdvancements {
  private static final DeferredRegister<CriterionTrigger<?>> REGISTER = DeferredRegister.create(BuiltInRegistries.TRIGGER_TYPES, RootsAPI.MODID);

  public static final DeferredHolder<CriterionTrigger<?>, PacifistTrigger> PACIFIST = REGISTER.register("pacifist", PacifistTrigger::new);

  public static void register(IEventBus bus) {
    REGISTER.register(bus);
  }
}
