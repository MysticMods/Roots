package epicsquid.roots.spell;

import epicsquid.roots.config.SpellConfig;

import java.util.HashMap;
import java.util.Map;

public class SpellRegistry {
  public static Map<String, SpellBase> spellRegistry = new HashMap<>();

  public static SpellBase getSpell(String s) {
    return spellRegistry.getOrDefault(s, null);
  }

  public static void init() {

    if (!SpellConfig.disableSpellsCategory.disableWildFire)
      spellRegistry.put(SpellWildfire.spellName, SpellWildfire.instance);
    if (!SpellConfig.disableSpellsCategory.disableSanctuary)
      spellRegistry.put(SpellSanctuary.spellName, SpellSanctuary.instance);
    if (!SpellConfig.disableSpellsCategory.disableDandelionWinds)
      spellRegistry.put(SpellDandelionWinds.spellName, SpellDandelionWinds.instance);
    if (!SpellConfig.disableSpellsCategory.disableRoseThorns)
      spellRegistry.put(SpellRoseThorns.spellName, SpellRoseThorns.instance);
    if (!SpellConfig.disableSpellsCategory.disableShatter)
      spellRegistry.put(SpellShatter.spellName, SpellShatter.instance);
    if (!SpellConfig.disableSpellsCategory.disablePetalShell)
      spellRegistry.put(SpellPetalShell.spellName, SpellPetalShell.instance);
    if (!SpellConfig.disableSpellsCategory.disableTimeStop)
      spellRegistry.put(SpellTimeStop.spellName, SpellTimeStop.instance);
    if (!SpellConfig.disableSpellsCategory.disableSkySoarer)
      spellRegistry.put(SpellSkySoarer.spellName, SpellSkySoarer.instance);
    if (!SpellConfig.disableSpellsCategory.disableLifeDrain)
      spellRegistry.put(SpellLifeDrain.spellName, SpellLifeDrain.instance);
    if (!SpellConfig.disableSpellsCategory.disableAcidCloud)
      spellRegistry.put(SpellAcidCloud.spellName, SpellAcidCloud.instance);
    if (!SpellConfig.disableSpellsCategory.disableGrowthInfusion)
      spellRegistry.put(SpellGrowthInfusion.spellName, SpellGrowthInfusion.instance);
    if (!SpellConfig.disableSpellsCategory.disableGeas)
      spellRegistry.put(SpellGeas.spellName, SpellGeas.instance);
    if (!SpellConfig.disableSpellsCategory.disableRadiance)
      spellRegistry.put(SpellRadiance.spellName, SpellRadiance.instance);
    if (!SpellConfig.disableSpellsCategory.disableLightDrifter)
      spellRegistry.put(SpellLightDrifter.spellName, SpellLightDrifter.instance);
    if (!SpellConfig.disableSpellsCategory.disableSenseAnimals)
      spellRegistry.put(SpellSenseAnimals.spellName, SpellSenseAnimals.instance);
    if (!SpellConfig.disableSpellsCategory.disableSecondWind)
      spellRegistry.put(SpellSecondWind.spellName, SpellSecondWind.instance);
    if (!SpellConfig.disableSpellsCategory.disableMagnetism)
      spellRegistry.put(SpellMagnetism.spellName, SpellMagnetism.instance);
    if (!SpellConfig.disableSpellsCategory.disableSenseDanger)
      spellRegistry.put(SpellSenseDanger.spellName, SpellSenseDanger.instance);
    if (!SpellConfig.disableSpellsCategory.disableHarvest)
      spellRegistry.put(SpellHarvest.spellName, SpellHarvest.instance);
    if (!SpellConfig.disableSpellsCategory.disableRampantGrowth)
      spellRegistry.put(SpellRampantGrowth.spellName, SpellRampantGrowth.instance);
    if (!SpellConfig.disableSpellsCategory.disableFeyLight)
      spellRegistry.put(SpellFeyLight.spellName, SpellFeyLight.instance);
    if (!SpellConfig.disableSpellsCategory.disableIcedTouch)
      spellRegistry.put(SpellIcedTouch.spellName, SpellIcedTouch.instance);

    // Cannot disable Grove-related spells
    spellRegistry.put(SpellGroveSupplication.spellName, SpellGroveSupplication.instance);

  }
}