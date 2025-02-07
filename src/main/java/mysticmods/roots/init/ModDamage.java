package mysticmods.roots.init;

import mysticmods.roots.api.RootsAPI;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.damagesource.DamageType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModDamage {
  private static final DeferredRegister<DamageType> DAMAGE_TYPES = DeferredRegister.create(Registries.DAMAGE_TYPE, RootsAPI.MODID);

  public static void register(IEventBus bus) {
    DAMAGE_TYPES.register(bus);
  }
}
