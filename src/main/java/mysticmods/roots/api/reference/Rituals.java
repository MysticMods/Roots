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
  ResourceKey<Ritual> DIVINE_PROTECTION = ritual("divine_protection");
  ResourceKey<Ritual> FIRE_STORM = ritual("fire_storm");
  ResourceKey<Ritual> FLOWER_GROWTH = ritual("flower_growth");
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
  ResourceKey<Ritual> WARDING_PROTECTION = ritual("warding_protection");
  ResourceKey<Ritual> WILDROOT_GROWTH = ritual("wildroot_growth");
  ResourceKey<Ritual> WINDWALL = ritual("windwall");

  static ResourceKey<Ritual> ritual(String name) {
    return ResourceKey.create(RootsAPI.RITUAL_REGISTRY, new ResourceLocation(RootsAPI.MODID, name));
  }
}
