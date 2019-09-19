package epicsquid.roots.spell;

import epicsquid.roots.config.SpellConfig;

import java.util.HashMap;
import java.util.Map;

public class SpellRegistry {
  public static Map<String, SpellBase> spellRegistry = new HashMap<>();

  public static SpellBase getSpell(String s) {
    SpellBase spell = spellRegistry.get(s);
    if (spell == null) {
      return null;
    }

    return spell;
  }

  public static void preInit() {
    spellRegistry.put(SpellWildfire.spellName, SpellWildfire.instance);
    SpellWildfire.instance.setDisabled(SpellConfig.disableSpellsCategory.disableWildFire);
    spellRegistry.put(SpellSanctuary.spellName, SpellSanctuary.instance);
    SpellSanctuary.instance.setDisabled(SpellConfig.disableSpellsCategory.disableSanctuary);
    spellRegistry.put(SpellDandelionWinds.spellName, SpellDandelionWinds.instance);
    SpellDandelionWinds.instance.setDisabled(SpellConfig.disableSpellsCategory.disableDandelionWinds);
    spellRegistry.put(SpellRoseThorns.spellName, SpellRoseThorns.instance);
    SpellRoseThorns.instance.setDisabled(SpellConfig.disableSpellsCategory.disableRoseThorns);
    spellRegistry.put(SpellShatter.spellName, SpellShatter.instance);
    SpellShatter.instance.setDisabled(SpellConfig.disableSpellsCategory.disableShatter);
    spellRegistry.put(SpellPetalShell.spellName, SpellPetalShell.instance);
    SpellPetalShell.instance.setDisabled(SpellConfig.disableSpellsCategory.disablePetalShell);
    spellRegistry.put(SpellTimeStop.spellName, SpellTimeStop.instance);
    SpellTimeStop.instance.setDisabled(SpellConfig.disableSpellsCategory.disableTimeStop);
    spellRegistry.put(SpellSkySoarer.spellName, SpellSkySoarer.instance);
    SpellSkySoarer.instance.setDisabled(SpellConfig.disableSpellsCategory.disableSkySoarer);
    spellRegistry.put(SpellLifeDrain.spellName, SpellLifeDrain.instance);
    SpellLifeDrain.instance.setDisabled(SpellConfig.disableSpellsCategory.disableLifeDrain);
    spellRegistry.put(SpellAcidCloud.spellName, SpellAcidCloud.instance);
    SpellAcidCloud.instance.setDisabled(SpellConfig.disableSpellsCategory.disableAcidCloud);
    spellRegistry.put(SpellGrowthInfusion.spellName, SpellGrowthInfusion.instance);
    SpellGrowthInfusion.instance.setDisabled(SpellConfig.disableSpellsCategory.disableGrowthInfusion);
    spellRegistry.put(SpellGeas.spellName, SpellGeas.instance);
    SpellGeas.instance.setDisabled(SpellConfig.disableSpellsCategory.disableGeas);
    spellRegistry.put(SpellRadiance.spellName, SpellRadiance.instance);
    SpellRadiance.instance.setDisabled(SpellConfig.disableSpellsCategory.disableRadiance);
    spellRegistry.put(SpellLightDrifter.spellName, SpellLightDrifter.instance);
    SpellLightDrifter.instance.setDisabled(SpellConfig.disableSpellsCategory.disableLightDrifter);
    spellRegistry.put(SpellSenseAnimals.spellName, SpellSenseAnimals.instance);
    SpellSenseAnimals.instance.setDisabled(SpellConfig.disableSpellsCategory.disableSenseAnimals);
    spellRegistry.put(SpellSecondWind.spellName, SpellSecondWind.instance);
    SpellSecondWind.instance.setDisabled(SpellConfig.disableSpellsCategory.disableSecondWind);
    spellRegistry.put(SpellMagnetism.spellName, SpellMagnetism.instance);
    SpellMagnetism.instance.setDisabled(SpellConfig.disableSpellsCategory.disableMagnetism);
    spellRegistry.put(SpellSenseDanger.spellName, SpellSenseDanger.instance);
    SpellSenseDanger.instance.setDisabled(SpellConfig.disableSpellsCategory.disableSenseDanger);
    spellRegistry.put(SpellHarvest.spellName, SpellHarvest.instance);
    SpellHarvest.instance.setDisabled(SpellConfig.disableSpellsCategory.disableHarvest);
    spellRegistry.put(SpellRampantGrowth.spellName, SpellRampantGrowth.instance);
    SpellRampantGrowth.instance.setDisabled(SpellConfig.disableSpellsCategory.disableRampantGrowth);
    spellRegistry.put(SpellFeyLight.spellName, SpellFeyLight.instance);
    SpellFeyLight.instance.setDisabled(SpellConfig.disableSpellsCategory.disableFeyLight);
    spellRegistry.put(SpellIcedTouch.spellName, SpellIcedTouch.instance);
    SpellIcedTouch.instance.setDisabled(SpellConfig.disableSpellsCategory.disableIcedTouch);
    spellRegistry.put(SpellDisarm.spellName, SpellDisarm.instance);
    SpellDisarm.instance.setDisabled(SpellConfig.disableSpellsCategory.disableDisarm);
    spellRegistry.put(SpellFall.spellName, SpellFall.instance);
    SpellFall.instance.setDisabled(SpellConfig.disableSpellsCategory.disableFall);
    spellRegistry.put(SpellScatter.spellName, SpellScatter.instance);
    SpellScatter.instance.setDisabled(SpellConfig.disableSpellsCategory.disableScatter);
    spellRegistry.put(SpellSoftTouch.spellName, SpellSoftTouch.instance);
    SpellSoftTouch.instance.setDisabled(SpellConfig.disableSpellsCategory.disableSoftTouch);
    spellRegistry.put(SpellThaw.spellName, SpellThaw.instance);
    SpellThaw.instance.setDisabled(SpellConfig.disableSpellsCategory.disableThaw);
    spellRegistry.put(SpellMark.spellName, SpellMark.instance);
    SpellMark.instance.setDisabled(SpellConfig.disableSpellsCategory.disableMark);


    // Cannot disable Grove-related spells
    spellRegistry.put(SpellGroveSupplication.spellName, SpellGroveSupplication.instance);
    SpellGroveSupplication.instance.setDisabled(false);
    spellRegistry.put(SpellFairySupplication.spellName, SpellFairySupplication.instance);
    SpellFairySupplication.instance.setDisabled(false);
  }

  public static void init () {
    spellRegistry.values().forEach(SpellBase::init);
  }

  public static void finalise () {
    spellRegistry.values().forEach(SpellBase::finalise);
    spellRegistry.values().forEach(SpellBase::finaliseCosts);
  }
}