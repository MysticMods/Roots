package mysticmods.roots.api.reference;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.registry.RootsRegistries;
import mysticmods.roots.api.ritual.Ritual;
import net.minecraft.resources.ResourceKey;

public interface Rituals {
  ResourceKey<Ritual> EMPTY = key("empty");
  ResourceKey<Ritual> CRAFTING = key("crafting");
  ResourceKey<Ritual> ANIMAL_HARVEST = key("animal_harvest");
  ResourceKey<Ritual> AUGMENTATION = key("augmentation");
  ResourceKey<Ritual> BLOOMING = key("blooming");
  ResourceKey<Ritual> FIRE_STORM = key("fire_storm");
  ResourceKey<Ritual> FROST_LANDS = key("frost_lands");
  ResourceKey<Ritual> GATHERING = key("gathering");
  ResourceKey<Ritual> GERMINATION = key("germination");
  ResourceKey<Ritual> HEALING_AURA = key("healing_aura");
  ResourceKey<Ritual> HEAVY_STORMS = key("heavy_storms");
  ResourceKey<Ritual> OVERGROWTH = key("overgrowth");
  ResourceKey<Ritual> PROTECTION = key("protection");
  ResourceKey<Ritual> PURITY = key("purity");
  ResourceKey<Ritual> SPREADING_FOREST = key("spreading_forest");
  ResourceKey<Ritual> SUMMON_CREATURES = key("summon_creatures");
  ResourceKey<Ritual> WARDING = key("warding");
  ResourceKey<Ritual> WILDROOT_GROWTH = key("wildroot_growth");
  ResourceKey<Ritual> WINDWALL = key("windwall");
  ResourceKey<Ritual> CONTAINMENT = key("containment");
  ResourceKey<Ritual> GROVE_SUPPLICATION = key("grove_supplication");

  static ResourceKey<Ritual> key(String name) {
    return ResourceKey.create(RootsRegistries.Keys.RITUALS, RootsAPI.rl(name));
  }
}
