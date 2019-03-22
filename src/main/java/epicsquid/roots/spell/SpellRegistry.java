package epicsquid.roots.spell;

import java.util.HashMap;
import java.util.Map;

public class SpellRegistry {
  public static Map<String, SpellBase> spellRegistry = new HashMap<>();

  public static SpellBase spell_wild_fire, spell_sanctuary, spell_dandelion_winds, spell_rose_thorns, spell_shatter, spell_petal_shell, spell_time_stop, spell_sky_soarer, spell_life_drain, spell_acid_cloud, spell_growth_infusion, spell_mind_ward, spell_radiance, spell_light_drifter,
      spell_sense_animals, spell_second_wind, spell_magnetism;

  public static SpellBase getSpell(String s){
    return spellRegistry.getOrDefault(s, null);
  }

  public static void init() {
    spellRegistry.put("spell_wild_fire", spell_wild_fire = new SpellWildfire("spell_wild_fire"));
    spellRegistry.put("spell_sanctuary", spell_sanctuary = new SpellSanctuary("spell_sanctuary"));
    spellRegistry.put("spell_dandelion_winds", spell_dandelion_winds = new SpellDandelionWinds("spell_dandelion_winds"));
    spellRegistry.put("spell_rose_thorns", spell_rose_thorns = new SpellRoseThorns("spell_rose_thorns"));
    spellRegistry.put("spell_shatter", spell_shatter = new SpellShatter("spell_shatter"));
    spellRegistry.put("spell_petal_shell", spell_petal_shell = new SpellPetalShell("spell_petal_shell"));
    spellRegistry.put("spell_time_stop", spell_time_stop = new SpellTimeStop("spell_time_stop"));
    spellRegistry.put("spell_sky_soarer", spell_sky_soarer = new SpellSkySoarer("spell_sky_soarer"));
    spellRegistry.put("spell_life_drain", spell_life_drain = new SpellLifeDrain("spell_life_drain"));
    spellRegistry.put("spell_acid_cloud", spell_acid_cloud = new SpellAcidCloud("spell_acid_cloud"));
    spellRegistry.put("spell_growth_infusion", spell_growth_infusion = new SpellGrowthInfusion("spell_growth_infusion"));
    spellRegistry.put("spell_mind_ward", spell_mind_ward = new SpellMindWard("spell_mind_ward"));
    spellRegistry.put("spell_radiance", spell_radiance = new SpellRadiance("spell_radiance"));
    spellRegistry.put("spell_light_drifter", spell_light_drifter = new SpellLightDrifter("spell_light_drifter"));
    spellRegistry.put("spell_sense_animals", spell_sense_animals = new SpellSenseAnimals("spell_sense_animals"));
    spellRegistry.put("spell_second_wind", spell_second_wind = new SpellSecondWind("spell_second_wind"));
    spellRegistry.put("spell_magnetism", spell_magnetism = new SpellMagnetism("spell_magnetism"));
  }

}