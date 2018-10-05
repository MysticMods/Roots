package epicsquid.roots.spell;

import java.util.HashMap;
import java.util.Map;

import epicsquid.mysticalworld.init.ModItems;

public class SpellRegistry {
  public static Map<String, SpellBase> spellRegistry = new HashMap<>();

  public static SpellBase spell_wild_fire, spell_sanctuary, spell_dandelion_winds, spell_rose_thorns, spell_shatter, spell_petal_shell, spell_time_stop, spell_gravity_boost, spell_life_drain, spell_acid_cloud, spell_growth_infusion, spell_mind_ward, spell_radiance, spell_light_drifter;

  public static void init() {
    spellRegistry.put("spell_wild_fire", spell_wild_fire = new SpellWildfire("spell_wild_fire").addCost(ModItems.wildroot, 0.125f));
    spellRegistry.put("spell_sanctuary",
        spell_sanctuary = new SpellSanctuary("spell_sanctuary").addCost(ModItems.moonglow_leaf, 0.125f).addCost(ModItems.aubergine_seed, 0.125f));
    spellRegistry.put("spell_dandelion_winds", spell_dandelion_winds = new SpellDandelionWinds("spell_dandelion_winds").addCost(ModItems.moonglow_leaf, 0.125f));
    spellRegistry.put("spell_rose_thorns", spell_rose_thorns = new SpellRoseThorns("spell_rose_thorns").addCost(ModItems.terra_moss, 0.25f));
    spellRegistry.put("spell_shatter", spell_shatter = new SpellShatter("spell_shatter").addCost(ModItems.wildroot, 0.0625f));
    spellRegistry.put("spell_petal_shell", spell_petal_shell = new SpellPetalShell("spell_petal_shell").addCost(ModItems.aubergine_seed, 0.5f).addCost(ModItems.moonglow_leaf, 0.25f));
    spellRegistry.put("spell_time_stop",
        spell_time_stop = new SpellTimeStop("spell_time_stop").addCost(ModItems.pereskia, 0.5f).addCost(ModItems.moonglow_leaf, 0.25f)
            .addCost(ModItems.pereskia_bulb, 0.25f));
    spellRegistry.put("spell_gravity_boost", spell_gravity_boost = new SpellGravityBoost("spell_gravity_boost").addCost(ModItems.pereskia, 0.125f));
    spellRegistry.put("spell_life_drain", spell_life_drain = new SpellLifeDrain("spell_life_drain").addCost(ModItems.pereskia_bulb, 0.125f));
    spellRegistry.put("spell_acid_cloud", spell_acid_cloud = new SpellAcidCloud("spell_acid_cloud").addCost(ModItems.terra_moss, 0.0625f));
    spellRegistry.put("spell_growth_infusion", spell_growth_infusion = new SpellGrowthInfusion("spell_growth_infusion").addCost(ModItems.terra_moss, 0.125f));
    spellRegistry.put("spell_mind_ward", spell_mind_ward = new SpellMindWard("spell_mind_ward").addCost(ModItems.aubergine_seed, 0.25f).addCost(ModItems.terra_moss, 0.25f));
    spellRegistry.put("spell_radiance",
        spell_radiance = new SpellRadiance("spell_radiance").addCost(ModItems.moonglow_leaf, 0.25f).addCost(ModItems.wildroot, 0.125f)
            .addCost(ModItems.aubergine_seed, 0.25f));
    spellRegistry.put("spell_light_drifter",
        spell_light_drifter = new LightDrifter("spell_light_drifter").addCost(ModItems.pereskia, 0.5f).addCost(ModItems.wildroot, 0.25f));
  }
}