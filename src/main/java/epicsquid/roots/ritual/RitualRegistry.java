package epicsquid.roots.ritual;

import epicsquid.roots.config.RitualConfig;
import epicsquid.roots.ritual.natural.RitualFlowerGrowth;
import epicsquid.roots.ritual.natural.RitualWildGrowth;
import epicsquid.roots.ritual.wild.RitualSummonCreatures;
import epicsquid.roots.tileentity.TileEntityBonfire;
import net.minecraft.entity.player.EntityPlayer;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class RitualRegistry {

  public static Map<String, RitualBase> ritualRegistry = new HashMap<>();

  public static RitualBase ritual_life, ritual_storm, ritual_light, ritual_fire_storm, ritual_regrowth, ritual_windwall,
      ritual_warden, ritual_natural_aura, ritual_purity, ritual_frost, ritual_animal_harvest, ritual_summoning,
      ritual_wild_growth, ritual_overgrowth, ritual_flower_growth, ritual_transmutation, ritual_gathering;

  public static RitualBase getRitual(TileEntityBonfire tileEntity, @Nullable EntityPlayer player) {
    for (int i = 0; i < ritualRegistry.size(); i++) {
      RitualBase ritual = ritualRegistry.values().toArray(new RitualBase[0])[i];
      if (ritual.isRitualRecipe(tileEntity, player)) {
        return ritual;
      }
    }
    return null;
  }

  public static RitualBase getRitual(String ritualName) {
    if (ritualName == null) {
      return null;
    }
    for (RitualBase ritual : ritualRegistry.values()) {
      if (ritual.getName().equalsIgnoreCase(ritualName)) {
        return ritual;
      }
    }
    return null;
  }

  public static void init() {
    addRitual(ritual_life = new RitualHealingAura("ritual_healing_aura", 800, RitualConfig.disableRitualCategory.disableHealingAura));
    addRitual(ritual_storm = new RitualHeavyStorms("ritual_heavy_storms", 2400, RitualConfig.disableRitualCategory.disableHeavyStorms));
    addRitual(ritual_light = new RitualDivineProtection("ritual_divine_protection", 1200, RitualConfig.disableRitualCategory.disableDivineProtection));
    addRitual(ritual_fire_storm = new RitualFireStorm("ritual_fire_storm", 1200, RitualConfig.disableRitualCategory.disableFireStorm));
    addRitual(ritual_regrowth = new RitualSpreadingForest("ritual_spreading_forest", 2400, RitualConfig.disableRitualCategory.disableNaturalGrowth));
    addRitual(ritual_windwall = new RitualWindwall("ritual_windwall", 3000, RitualConfig.disableRitualCategory.disableWindwall));
    addRitual(ritual_warden = new RitualWardingProtection("ritual_warding_protection", 1200, RitualConfig.disableRitualCategory.disableWardingProtection));
    addRitual(ritual_natural_aura = new RitualGermination("ritual_germination", 6400, RitualConfig.disableRitualCategory.disableNaturalAura));
    addRitual(ritual_purity = new RitualPurity("ritual_purity", 1200, RitualConfig.disableRitualCategory.disablePurity));
    addRitual(ritual_frost = new RitualFrostLands("ritual_frost_lands", 6400, RitualConfig.disableRitualCategory.disableFrostLands));
    addRitual(ritual_animal_harvest = new RitualAnimalHarvest("ritual_animal_harvest", 3200, RitualConfig.disableRitualCategory.disableAnimalHarvest));
    addRitual(ritual_summoning = new RitualSummonCreatures("ritual_summon_creatures", 0, RitualConfig.disableRitualCategory.disableSummonCreatures));
    addRitual(ritual_wild_growth = new RitualWildGrowth("ritual_wild_growth", 300, RitualConfig.disableRitualCategory.disableWildGrowth));
    addRitual(ritual_overgrowth = new RitualOvergrowth("ritual_overgrowth", 4500, RitualConfig.disableRitualCategory.disableOvergrowth));
    addRitual(ritual_flower_growth = new RitualFlowerGrowth("ritual_flower_growth", 3200, RitualConfig.disableRitualCategory.disableFlowerGrowth));
    addRitual(ritual_transmutation = new RitualTransmutation("ritual_transmutation", 2400, RitualConfig.disableRitualCategory.disableTransmutation));
    addRitual(ritual_gathering = new RitualGathering("ritual_gathering", 6000, RitualConfig.disableRitualCategory.disableGathering));
  }

  public static void addRitual(RitualBase ritual) {
    ritualRegistry.put(ritual.getName(), ritual);
  }

}