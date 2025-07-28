package mysticmods.roots.init;

import mysticmods.roots.api.RootsAPI;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModAttributes {
  private static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(BuiltInRegistries.ATTRIBUTE, RootsAPI.MODID);

  // 1 = 100% reduction.
  public static final DeferredHolder<Attribute, Attribute> COOLDOWN_REDUCTION = ATTRIBUTES.register("cooldown_reduction", () -> new RangedAttribute("roots.cooldown_reduction", 0.0D, 0.0D, 1.0D).setSyncable(true));

  public static final DeferredHolder<Attribute, Attribute> COST_REDUCTION = ATTRIBUTES.register("cost_reduction", () -> new RangedAttribute("roots.cost_reduction", 0.0D, 0.0D, 1.0D).setSyncable(true));

  public static final DeferredHolder<Attribute, Attribute> FORAGING = ATTRIBUTES.register("foraging", () -> new RangedAttribute("roots.foraging", 0.0D, 0.0D, Double.MAX_VALUE).setSyncable(true));

  public static void register(IEventBus bus) {
    ATTRIBUTES.register(bus);
  }
}
