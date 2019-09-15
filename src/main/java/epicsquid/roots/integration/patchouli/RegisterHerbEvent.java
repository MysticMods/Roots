package epicsquid.roots.integration.patchouli;

import javax.annotation.Nonnull;

import epicsquid.roots.Roots;
import epicsquid.roots.api.Herb;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.eventhandler.GenericEvent;
import net.minecraftforge.fml.common.eventhandler.IContextSetter;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.function.Supplier;

/**
 * Subscribe to this event if you want to add your own herbs
 */
public class RegisterHerbEvent extends GenericEvent<Herb> implements IContextSetter {

  public final IForgeRegistry<Herb> registry;
  public final ResourceLocation name;

  public RegisterHerbEvent(@Nonnull ResourceLocation name, @Nonnull IForgeRegistry<Herb> registry) {
    super(registry.getRegistrySuperType());
    this.name = name;
    this.registry = registry;
  }

  @Nonnull
  public IForgeRegistry<Herb> getRegistry() {
    return registry;
  }

  @Nonnull
  public ResourceLocation getName() {
    return name;
  }

  /**
   * Register a pre-wrapped herb
   */
  public void register(@Nonnull Herb herb) {
    registry.register(herb);
  }

  /**
   * Register and item as a herb. Will be automatically wrapped for registering.
   */
  public void register(@Nonnull Supplier<Item> item, String registryName) {
    registry.register(new Herb(item, new ResourceLocation(Roots.MODID, registryName)));
  }
}
