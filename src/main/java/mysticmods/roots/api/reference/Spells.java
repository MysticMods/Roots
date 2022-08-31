package mysticmods.roots.api.reference;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.spell.Spell;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public interface Spells {
  ResourceKey<Spell> ACID_CLOUD = spell("acid_cloud");
  ResourceKey<Spell> AQUA_BUBBLE = spell("aqua_bubble");
  ResourceKey<Spell> AUGMENT = spell("augment");
  ResourceKey<Spell> LIGHT_DRIFTER = spell("light_drifter");
  ResourceKey<Spell> MAGNETISM = spell("magnetism");
  ResourceKey<Spell> DANDELION_WINDS = spell("dandelion_winds");
  ResourceKey<Spell> DESATURATE = spell("desaturate");
  ResourceKey<Spell> DISARM = spell("disarm");
  ResourceKey<Spell> EXTENSION = spell("extension");
  ResourceKey<Spell> NONDETECTION = spell("nondetection");
  ResourceKey<Spell> FEY_LIGHT = spell("fey_light");
  ResourceKey<Spell> GEAS = spell("geas");
  ResourceKey<Spell> CONTROL_UNDEAD = spell("control_undead");
  ResourceKey<Spell> GROWTH_INFUSION = spell("growth_infusion");
  ResourceKey<Spell> RAMPANT_GROWTH = spell("rampant_growth");
  ResourceKey<Spell> HARVEST = spell("harvest");
  ResourceKey<Spell> LIFE_DRAIN = spell("life_drain");
  ResourceKey<Spell> PETAL_SHELL = spell("petal_shell");
  ResourceKey<Spell> RADIANCE = spell("radiance");
  ResourceKey<Spell> ROSE_THORNS = spell("rose_thorns");
  ResourceKey<Spell> SANCTUARY = spell("sanctuary");
  ResourceKey<Spell> SHATTER = spell("shatter");
  ResourceKey<Spell> SKY_SOARER = spell("sky_soarer");
  ResourceKey<Spell> JAUNT = spell("jaunt");
  ResourceKey<Spell> STORM_CLOUD = spell("storm_cloud");
  ResourceKey<Spell> TIME_STOP = spell("time_stop");
  ResourceKey<Spell> WILDFIRE = spell("wildfire");

  static ResourceKey<Spell> spell(String name) {
    return ResourceKey.create(RootsAPI.SPELL_REGISTRY, new ResourceLocation(RootsAPI.MODID, name));
  }
}
