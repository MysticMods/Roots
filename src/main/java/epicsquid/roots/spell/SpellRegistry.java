package epicsquid.roots.spell;

import epicsquid.roots.config.SpellConfig;

import java.util.HashMap;
import java.util.Map;

public class SpellRegistry {
  public static Map<String, SpellBase> spellRegistry = new HashMap<>();

  public static SpellBase getSpell(String s){
    return spellRegistry.getOrDefault(s, null);
  }

  public static void init() {

    if (!SpellConfig.disableWildFire)
      spellRegistry.put(SpellWildfire.spellName, SpellWildfire.instance);
    if (!SpellConfig.disableSanctuary)
      spellRegistry.put(SpellSanctuary.spellName, SpellSanctuary.instance);
    if (!SpellConfig.disableDandelionWinds)
      spellRegistry.put(SpellDandelionWinds.spellName, SpellDandelionWinds.instance);
    if (!SpellConfig.disableRoseThorns)
      spellRegistry.put(SpellRoseThorns.spellName, SpellRoseThorns.instance);
    if (!SpellConfig.disableShatter)
      spellRegistry.put(SpellShatter.spellName, SpellShatter.instance);
    if (!SpellConfig.disablePetalShell)
      spellRegistry.put(SpellPetalShell.spellName, SpellPetalShell.instance);
    if (!SpellConfig.disableTimeStop)
      spellRegistry.put(SpellTimeStop.spellName, SpellTimeStop.instance);
    if (!SpellConfig.disableSkySoarer)
      spellRegistry.put(SpellSkySoarer.spellName, SpellSkySoarer.instance);
    if (!SpellConfig.disableLifeDrain)
      spellRegistry.put(SpellLifeDrain.spellName, SpellLifeDrain.instance);
    if (!SpellConfig.disableAcidCloud)
      spellRegistry.put(SpellAcidCloud.spellName, SpellAcidCloud.instance);
    if (!SpellConfig.disableGrowthInfusion)
      spellRegistry.put(SpellGrowthInfusion.spellName, SpellGrowthInfusion.instance);
    if (!SpellConfig.disableMindWard)
      spellRegistry.put(SpellMindWard.spellName, SpellMindWard.instance);
    if (!SpellConfig.disableRadiance)
      spellRegistry.put(SpellRadiance.spellName, SpellRadiance.instance);
    if (!SpellConfig.disableLightDrifter)
      spellRegistry.put(SpellLightDrifter.spellName, SpellLightDrifter.instance);
    if (!SpellConfig.disableSenseAnimals)
      spellRegistry.put(SpellSenseAnimals.spellName, SpellSenseAnimals.instance);
    if (!SpellConfig.disableSecondWind)
      spellRegistry.put(SpellSecondWind.spellName, SpellSecondWind.instance);
    if (!SpellConfig.disableMagnetism)
      spellRegistry.put(SpellMagnetism.spellName, SpellMagnetism.instance);
    if (!SpellConfig.disableSenseDanger)
      spellRegistry.put(SpellSenseDanger.spellName, SpellSenseDanger.instance);
    if (!SpellConfig.disableHarvest)
      spellRegistry.put(SpellHarvest.spellName, SpellHarvest.instance);
    if (!SpellConfig.disableRampantGrowth)
      spellRegistry.put(SpellRampantGrowth.spellName, SpellRampantGrowth.instance);

  }
}