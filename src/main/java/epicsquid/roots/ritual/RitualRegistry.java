package epicsquid.roots.ritual;

import epicsquid.roots.config.RitualConfig;
import epicsquid.roots.tileentity.TileEntityBonfire;
import net.minecraft.entity.player.EntityPlayer;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class RitualRegistry {

  public static Map<String, RitualBase> ritualRegistry = new HashMap<>();

  public static RitualBase ritual_healing_aura, ritual_heavy_storms, ritual_divine_protection, ritual_fire_storm, ritual_spreading_forest, ritual_windwall,
      ritual_warding_protection, ritual_germination, ritual_purity, ritual_frost_lands, ritual_animal_harvest, ritual_summon_creatures,
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

  public static void preInit() {
    // TODO: Move all of these ritual name declarations as static members
    addRitual(ritual_animal_harvest = new RitualAnimalHarvest("ritual_animal_harvest", RitualConfig.disableRitualCategory.disableAnimalHarvest));
    addRitual(ritual_divine_protection = new RitualDivineProtection("ritual_divine_protection", RitualConfig.disableRitualCategory.disableDivineProtection));
    addRitual(ritual_fire_storm = new RitualFireStorm("ritual_fire_storm", RitualConfig.disableRitualCategory.disableFireStorm));
    addRitual(ritual_flower_growth = new RitualFlowerGrowth("ritual_flower_growth", RitualConfig.disableRitualCategory.disableFlowerGrowth));
    addRitual(ritual_frost_lands = new RitualFrostLands("ritual_frost_lands", RitualConfig.disableRitualCategory.disableFrostLands));
    addRitual(ritual_gathering = new RitualGathering("ritual_gathering", RitualConfig.disableRitualCategory.disableGathering));
    addRitual(ritual_germination = new RitualGermination("ritual_germination", RitualConfig.disableRitualCategory.disableNaturalAura));
    addRitual(ritual_healing_aura = new RitualHealingAura("ritual_healing_aura", RitualConfig.disableRitualCategory.disableHealingAura));
    addRitual(ritual_heavy_storms = new RitualHeavyStorms("ritual_heavy_storms", RitualConfig.disableRitualCategory.disableHeavyStorms));
    addRitual(ritual_overgrowth = new RitualOvergrowth("ritual_overgrowth", RitualConfig.disableRitualCategory.disableOvergrowth));
    addRitual(ritual_purity = new RitualPurity("ritual_purity", RitualConfig.disableRitualCategory.disablePurity));
    addRitual(ritual_spreading_forest = new RitualSpreadingForest("ritual_spreading_forest", RitualConfig.disableRitualCategory.disableNaturalGrowth));
    // TODO:
    addRitual(ritual_summon_creatures = new RitualSummonCreatures("ritual_summon_creatures", 0, RitualConfig.disableRitualCategory.disableSummonCreatures));
    addRitual(ritual_transmutation = new RitualTransmutation("ritual_transmutation", RitualConfig.disableRitualCategory.disableTransmutation));
    addRitual(ritual_warding_protection = new RitualWardingProtection("ritual_warding_protection", RitualConfig.disableRitualCategory.disableWardingProtection));
    addRitual(ritual_wild_growth = new RitualWildGrowth("ritual_wild_growth", RitualConfig.disableRitualCategory.disableWildGrowth));
    addRitual(ritual_windwall = new RitualWindwall("ritual_windwall", 3000, RitualConfig.disableRitualCategory.disableWindwall));
  }

  public static void init () {
    ritualRegistry.values().forEach(RitualBase::init);
  }

  public static void addRitual(RitualBase ritual) {
    ritualRegistry.put(ritual.getName(), ritual);
  }

  public static void finalise () {
    ritualRegistry.values().forEach(RitualBase::finalise);
  }
}