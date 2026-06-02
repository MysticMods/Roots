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

  public static final DeferredHolder<SpellModifier, SpellModifier> SKY_SOARER_FRIENDLY_EARTH = REGISTER.register("sky_soarer/friendly_earth", () -> new SpellModifier(CostInstance.add(ModHerbs.STALICRIPE, SpellCosts.BASE_0125), ModSpells.SKY_SOARER.getKey()));

  public static final DeferredHolder<SpellModifier, SpellModifier> SKY_SOARER_AMPLIFIED_1 = REGISTER.register("sky_soarer/amplified_1", () -> new SpellModifier(CostInstance.add(ModHerbs.CLOUD_BERRY, SpellCosts.BASE_0125), ModSpells.SKY_SOARER.getKey()));

  public static final DeferredHolder<SpellModifier, SpellModifier> SKY_SOARER_AMPLIFIED_2 = REGISTER.register("sky_soarer/amplified_2", () -> new SpellModifier(CostInstance.mult(ModHerbs.CLOUD_BERRY, SpellCosts.MULT_105), SKY_SOARER_AMPLIFIED_1.getKey(), ModSpells.SKY_SOARER.getKey()));

  public static final DeferredHolder<SpellModifier, SpellModifier> SKY_SOARER_SPEEDY_1 = REGISTER.register("sky_soarer/speedy_1", () -> new SpellModifier(CostInstance.add(ModHerbs.CLOUD_BERRY, SpellCosts.BASE_0125), ModSpells.SKY_SOARER.getKey()));

  public static final DeferredHolder<SpellModifier, SpellModifier> SKY_SOARER_SPEEDY_2 = REGISTER.register("sky_soarer/speedy_2", () -> new SpellModifier(CostInstance.add(ModHerbs.CLOUD_BERRY, SpellCosts.BASE_0125), SKY_SOARER_SPEEDY_1.getKey(), ModSpells.SKY_SOARER.getKey()));

  public static void register(IEventBus bus) {
    REGISTER.register(bus);
  }
}
