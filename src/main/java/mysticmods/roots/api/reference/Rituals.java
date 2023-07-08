package mysticmods.roots.api.reference;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.ritual.Ritual;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public interface Rituals {
  // PLACEHOLDER
  ResourceKey<Ritual> CRAFTING = ritual("crafting");

  // ACTUAL RITUALS, NOT ALL CONFIRMED
  ResourceKey<Ritual> ANIMAL_HARVEST = ritual("animal_harvest");
  ResourceKey<Ritual> PROTECTION = ritual("protection");
  ResourceKey<Ritual> FIRE_STORM = ritual("fire_storm");
  ResourceKey<Ritual> BLOOMING = ritual("blooming");
  ResourceKey<Ritual> FROST_LANDS = ritual("frost_lands");
  ResourceKey<Ritual> GATHERING = ritual("gathering");
  ResourceKey<Ritual> GERMINATION = ritual("germination");
  ResourceKey<Ritual> HEALING_AURA = ritual("healing_aura");
  ResourceKey<Ritual> HEAVY_STORMS = ritual("heavy_storms");
  ResourceKey<Ritual> OVERGROWTH = ritual("overgrowth");
  ResourceKey<Ritual> PURITY = ritual("purity");
  ResourceKey<Ritual> SPREADING_FOREST = ritual("spreading_forest");
  ResourceKey<Ritual> SUMMON_CREATURES = ritual("summon_creatures");
  ResourceKey<Ritual> TRANSMUTATION = ritual("transmutation");
  ResourceKey<Ritual> WARDING = ritual("warding");
  ResourceKey<Ritual> WILDROOT_GROWTH = ritual("wildroot_growth");
  ResourceKey<Ritual> WINDWALL = ritual("windwall");
  ResourceKey<Ritual> GROVE_SUPPLICATION = ritual("grove_supplication");

  static ResourceKey<Ritual> ritual(String name) {
    return ResourceKey.create(RootsAPI.RITUAL_REGISTRY, new ResourceLocation(RootsAPI.MODID, name));
  }
}
