package mysticmods.roots.init;

import mysticmods.roots.api.RootsAPI;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.effects.EnchantmentValueEffect;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModEnchantment {
  public static final ResourceKey<Enchantment> FORAGING = ResourceKey.create(Registries.ENCHANTMENT, RootsAPI.rl("foraging"));

  private static final DeferredRegister<DataComponentType<?>> REGISTER = DeferredRegister.create(Registries.ENCHANTMENT_EFFECT_COMPONENT_TYPE, RootsAPI.MODID);

  public static final DeferredHolder<DataComponentType<?>, DataComponentType<EnchantmentValueEffect>> FORAGING_EFFECT = REGISTER.register("foraging", () -> new DataComponentType.Builder<EnchantmentValueEffect>().persistent(EnchantmentValueEffect.CODEC).build());

  public static void register (IEventBus bus) {
    REGISTER.register(bus);
  }
}
