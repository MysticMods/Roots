package epicsquid.mysticallib.event;

import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.registries.IForgeRegistry;

public class RegisterModRecipesEvent extends Event {
  IForgeRegistry<IRecipe> registry;

  public RegisterModRecipesEvent(IForgeRegistry<IRecipe> registry) {
    this.registry = registry;
  }

  public IForgeRegistry<IRecipe> getRegistry() {
    return registry;
  }
}
