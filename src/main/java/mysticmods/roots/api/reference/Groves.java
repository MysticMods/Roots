package mysticmods.roots.api.reference;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.grove.Grove;
import mysticmods.roots.api.registry.RootsRegistries;
import net.minecraft.resources.ResourceKey;

public interface Groves {
  ResourceKey<Grove> WILD = grove("wild");
  ResourceKey<Grove> TWILIGHT = grove("twilight");
  ResourceKey<Grove> UNITY = grove("unity");
  ResourceKey<Grove> CULTIVATION = grove("cultivation");
  ResourceKey<Grove> FUNGAL = grove("fungal");
  ResourceKey<Grove> ELEMENTAL = grove("elemental");
  ResourceKey<Grove> ANIMATE = grove("animate");
  ResourceKey<Grove> HOLLOW = grove("hollow");

  static ResourceKey<Grove> grove(String name) {
    return ResourceKey.create(RootsRegistries.Keys.GROVES, RootsAPI.rl(name));
  }
}
