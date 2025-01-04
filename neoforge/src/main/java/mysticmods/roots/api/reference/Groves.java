package mysticmods.roots.api.reference;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.grove.Grove;
import mysticmods.roots.api.registry.RootsRegistries;
import net.minecraft.resources.ResourceKey;

public interface Groves {
  ResourceKey<Grove> PRIMAL = grove("primal");
  ResourceKey<Grove> FAIRY = grove("fairy");
  ResourceKey<Grove> TWILIGHT = grove("twilight");
  ResourceKey<Grove> FUNGAL = grove("fungal");
  ResourceKey<Grove> SPROUT = grove("sprout");
  ResourceKey<Grove> ELEMENTAL = grove("elemental");
  ResourceKey<Grove> WILD = grove("wild");

  static ResourceKey<Grove> grove(String name) {
    return ResourceKey.create(RootsRegistries.Keys.GROVES, RootsAPI.rl(name));
  }
}
