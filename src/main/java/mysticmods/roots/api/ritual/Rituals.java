package mysticmods.roots.api.ritual;

import mysticmods.roots.api.RootsAPI;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public interface Rituals {
  ResourceKey<Ritual> TRANSMUTATION = ritual("transmutation");
  ResourceKey<Ritual> ANIMAL_HARVEST = ritual("animal_harvest");

  static ResourceKey<Ritual> ritual(String name) {
    return ResourceKey.create(RootsAPI.RITUAL_REGISTRY, new ResourceLocation(RootsAPI.MODID, name));
  }
}
