package epicsquid.roots.ritual;

import epicsquid.roots.config.RitualConfig;
import epicsquid.roots.ritual.natural.RitualFlowerGrowth;
import epicsquid.roots.ritual.natural.RitualWildGrowth;
import epicsquid.roots.ritual.wild.RitualSummonCreatures;

import java.lang.reflect.InvocationTargetException;

/*************************************************
 * Author: Davoleo
 * Date / Hour: 12/07/2019 / 17:10
 * Enum: EnumRitual
 * Project: Mystic Mods
 * Copyright - © - Davoleo - 2019
 **************************************************/

public enum EnumRitual {

  HEALING_AURA("ritual_healing_aura", 800, !RitualConfig.disableRitualCategory.disableHealingAura, RitualHealingAura.class),
  HEAVY_STORMS("ritual_heavy_storms", 2400, !RitualConfig.disableRitualCategory.disableHeavyStorms, RitualHeavyStorms.class),
  DIVINE_PROTECTION("ritual_divine_protection", 1200, !RitualConfig.disableRitualCategory.disableDivineProtection, RitualDivineProtection.class),
  FIRE_STORM("ritual_fire_storm", 1200, !RitualConfig.disableRitualCategory.disableFireStorm, RitualFireStorm.class),
  SPREADING_FOREST("ritual_spreading_forest", 2400, !RitualConfig.disableRitualCategory.disableSpreadingForest, RitualSpreadingForest.class),
  WINDWALL("ritual_windwall", 3000, !RitualConfig.disableRitualCategory.disableWindwall, RitualWindwall.class),
  WARDING_PROTECTION("ritual_warding_protection", 1200, !RitualConfig.disableRitualCategory.disableWardingProtection, RitualWardingProtection.class),
  GERMINATION("ritual_germination", 6400, !RitualConfig.disableRitualCategory.disableGermination, RitualGermination.class),
  PURITY("ritual_purity", 1200, !RitualConfig.disableRitualCategory.disablePurity, RitualPurity.class),
  FROST_LANDS("ritual_frost_lands", 6400, !RitualConfig.disableRitualCategory.disableFrostLands, RitualFrostLands.class),
  ANIMAL_HARVEST("ritual_animal_harvest", 3200, !RitualConfig.disableRitualCategory.disableAnimalHarvest, RitualAnimalHarvest.class),
  SUMMON_CREATURES("ritual_summon_creatures", 0, !RitualConfig.disableRitualCategory.disableSummonCreatures, RitualSummonCreatures.class),
  WILD_GROWTH("ritual_wild_growth", 300, !RitualConfig.disableRitualCategory.disableWildGrowth, RitualWildGrowth.class),
  OVERGROWTH("ritual_overgrowth", 4500, !RitualConfig.disableRitualCategory.disableOvergrowth, RitualOvergrowth.class),
  FLOWER_GROWTH("ritual_flower_growth", 3200, !RitualConfig.disableRitualCategory.disableFlowerGrowth, RitualFlowerGrowth.class),
  TRANSMUTATION("ritual_transmutation", 2400, !RitualConfig.disableRitualCategory.disableTransmutation, RitualTransmutation.class);

  private String name;
  private int duration;
  private boolean enabled;
  private Class<? extends RitualBase> classRef;

  EnumRitual(String name, int duration, boolean enabled, Class<? extends RitualBase> classRef) {
    this.name = name;
    this.duration = duration;
    this.enabled = enabled;
    this.classRef = classRef;
  }

  public String getName() {
    return name;
  }

  public int getDuration() {
    return duration;
  }

  public boolean isEnabled() {
    return enabled;
  }

  public RitualBase createRitual() {
    try {
      return classRef.getDeclaredConstructor(String.class, int.class).newInstance(name, duration);
    } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
      e.printStackTrace();
    }
    return null;
  }
}