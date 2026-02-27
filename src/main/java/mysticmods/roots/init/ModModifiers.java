package mysticmods.roots.init;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.herb.Cost;
import mysticmods.roots.api.herb.CostInstance;
import mysticmods.roots.api.modifier.SpellModifier;
import mysticmods.roots.api.reference.SpellCosts;
import mysticmods.roots.api.registry.RootsRegistries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModModifiers {
  private static final DeferredRegister<SpellModifier> REGISTER = DeferredRegister.create(RootsRegistries.Keys.SPELL_MODIFIERS, RootsAPI.MODID);

  public static final DeferredHolder<SpellModifier, SpellModifier> SKY_SOARER_FRIENDLY_EARTH = REGISTER.register("sky_soarer/friendly_earth", () -> new SpellModifier(CostInstance.of(CostInstance.ChargeType.CAST, Cost.add(ModHerbs.STALICRIPE, SpellCosts.BASE_0125)), ModSpells.SKY_SOARER.getKey()));

  public static final DeferredHolder<SpellModifier, SpellModifier> SKY_SOARER_AMPLIFIED = REGISTER.register("sky_soarer/amplified", () -> new SpellModifier(CostInstance.of(CostInstance.ChargeType.CAST, Cost.add(ModHerbs.CLOUD_BERRY, SpellCosts.BASE_0125)), ModSpells.SKY_SOARER.getKey()));

  public static final DeferredHolder<SpellModifier, SpellModifier> SKY_SOARER_AMPLIFIED_2 = REGISTER.register("sky_soarer/amplified_2", () -> new SpellModifier(CostInstance.of(CostInstance.ChargeType.CAST, Cost.add(ModHerbs.CLOUD_BERRY, SpellCosts.BASE_0125)), SKY_SOARER_AMPLIFIED.getKey(), ModSpells.SKY_SOARER.getKey()));

  public static void register(IEventBus bus) {
    REGISTER.register(bus);
  }
}
