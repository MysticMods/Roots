package epicsquid.roots.spell;

import java.util.HashMap;
import java.util.Map;

public class SpellRegistry {
  public static Map<String, SpellBase> spellRegistry = new HashMap<>();

  public static SpellBase getSpell(String s){
    return spellRegistry.getOrDefault(s, null);
  }

  public static void init() {
    spellRegistry.put(SpellWildfire.spellName, SpellWildfire.instance);
    spellRegistry.put(SpellSanctuary.spellName, SpellSanctuary.instance);
    spellRegistry.put(SpellDandelionWinds.spellName, SpellDandelionWinds.instance);
    spellRegistry.put(SpellRoseThorns.spellName, SpellRoseThorns.instance);
    spellRegistry.put(SpellShatter.spellName, SpellShatter.instance);
    spellRegistry.put(SpellPetalShell.spellName, SpellPetalShell.instance);
    spellRegistry.put(SpellTimeStop.spellName, SpellTimeStop.instance);
    spellRegistry.put(SpellSkySoarer.spellName, SpellSkySoarer.instance);
    spellRegistry.put(SpellLifeDrain.spellName, SpellLifeDrain.instance);
    spellRegistry.put(SpellAcidCloud.spellName, SpellAcidCloud.instance);
    spellRegistry.put(SpellGrowthInfusion.spellName, SpellGrowthInfusion.instance);
    spellRegistry.put(SpellMindWard.spellName, SpellMindWard.instance);
    spellRegistry.put(SpellRadiance.spellName, SpellRadiance.instance);
    spellRegistry.put(SpellLightDrifter.spellName, SpellLightDrifter.instance);
    spellRegistry.put(SpellSenseAnimals.spellName, SpellSenseAnimals.instance);
    spellRegistry.put(SpellSecondWind.spellName, SpellSecondWind.instance);
    spellRegistry.put(SpellMagnetism.spellName, SpellMagnetism.instance);
    spellRegistry.put(SpellSenseDanger.spellName, SpellSenseDanger.instance);
    spellRegistry.put(SpellHarvest.spellName, SpellHarvest.instance);
    spellRegistry.put(SpellZephyrSlice.spellName, SpellZephyrSlice.instance);
  }
}