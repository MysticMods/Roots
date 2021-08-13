package epicsquid.roots.init;

import epicsquid.mysticallib.event.RegisterContentEvent;
import epicsquid.roots.Roots;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

import javax.annotation.Nonnull;

public class ModSounds {
  public static SoundEvent WHIRLWIND;

  public static class Spells {
    public static SoundEvent AUGMENT;
    public static SoundEvent ACID_CLOUD;
    public static SoundEvent ACID_CLOUD_ALT;
    public static SoundEvent NATURES_SCYTHE;
    public static SoundEvent CHRYSOPOEIA;
    public static SoundEvent DANDELION_WINDS;
    public static SoundEvent DESATURATE;
    public static SoundEvent DISARM;
    public static SoundEvent EXTENSION;
    public static SoundEvent FEY_LIGHT;
    public static SoundEvent GEAS;
    public static SoundEvent GEAS_EFFECT_END;
    public static SoundEvent GROWTH_INFUSION;
    public static SoundEvent HARVEST;
    public static SoundEvent AQUA_BUBBLE; // Iced Touch
    public static SoundEvent AQUA_BUBBLE_ALT;
    public static SoundEvent AQUA_BUBBLE_ALT_EFFECT_END;
    public static SoundEvent LIFE_DRAIN;
    public static SoundEvent LIGHT_DRIFTER;
    public static SoundEvent LIGHT_DRIFTER_EFFECT_END;
    public static SoundEvent MAGNETISM;
    public static SoundEvent PETAL_SHELL;
    public static SoundEvent PETAL_SHELL_EFFECT_BREAK;
    public static SoundEvent PETAL_SHELL_EFFECT_END;
    public static SoundEvent RADIANCE;
    public static SoundEvent REACH_EFFECT;
    public static SoundEvent REACH_EFFECT_END;
    public static SoundEvent ROSE_THORNS;
    public static SoundEvent SANCTUARY;
    public static SoundEvent SATURATE;
    public static SoundEvent SECOND_WIND;
    public static SoundEvent SENSE_ANIMALS;
    public static SoundEvent SENSE_ANIMALS_EFFECT_END;
    public static SoundEvent SENSE_DANGER;
    public static SoundEvent SENSE_DANGER_EFFECT_END;
    public static SoundEvent SHATTER;
    public static SoundEvent SKY_SOARER;
    public static SoundEvent STORM_CLOUD;
    public static SoundEvent STORM_CLOUD_END;
    public static SoundEvent TIME_STOP;
    public static SoundEvent TIME_STOP_END;
    public static SoundEvent WILDFIRE;
  }

  public static class Events {
    public static SoundEvent IMBUER_ADD_ITEM;
    public static SoundEvent IMBUER_REMOVE_ITEM;
    public static SoundEvent IMBUER_USE;
    public static SoundEvent IMBUER_FINISHED;
    public static SoundEvent MORTAR_ADD_ITEM;
    public static SoundEvent MORTAR_REMOVE_ITEM;
    public static SoundEvent MORTAR_USE;
    public static SoundEvent PYRE_ADD_ITEM;
    public static SoundEvent PYRE_REMOVE_ITEM;
  }

  public static void initSounds(@Nonnull RegisterContentEvent event) {
    event.addSound(WHIRLWIND = createSoundEvent(new ResourceLocation(Roots.MODID, "whirlwind")));

    // Spells
    event.addSound(Spells.AUGMENT = createSoundEvent("spell.augment"));
    event.addSound(Spells.ACID_CLOUD = createSoundEvent("spell.acid_cloud"));
    event.addSound(Spells.ACID_CLOUD_ALT = createSoundEvent("spell.acid_cloud_alt"));
    event.addSound(Spells.CHRYSOPOEIA = createSoundEvent("spell.chrysopoeia"));
    event.addSound(Spells.DANDELION_WINDS = createSoundEvent("spell.dandelion_winds"));
    event.addSound(Spells.DESATURATE = createSoundEvent("spell.desaturate"));
    event.addSound(Spells.DISARM = createSoundEvent("spell.disarm"));
    event.addSound(Spells.EXTENSION = createSoundEvent("spell.extension"));
    event.addSound(Spells.FEY_LIGHT = createSoundEvent("spell.fey_light"));
    event.addSound(Spells.GEAS = createSoundEvent("spell.geas"));
    event.addSound(Spells.GEAS_EFFECT_END = createSoundEvent("spell.geas_end"));
    event.addSound(Spells.GROWTH_INFUSION = createSoundEvent("spell.growth_infusion"));
    event.addSound(Spells.HARVEST = createSoundEvent("spell.harvest"));
    event.addSound(Spells.AQUA_BUBBLE = createSoundEvent("spell.aqua_bubble"));
    event.addSound(Spells.AQUA_BUBBLE_ALT = createSoundEvent("spell.aqua_bubble_alt"));
    event.addSound(Spells.AQUA_BUBBLE_ALT_EFFECT_END = createSoundEvent("spell.aqua_bubble_alt_end"));
    event.addSound(Spells.LIFE_DRAIN = createSoundEvent("spell.life_drain"));
    event.addSound(Spells.LIGHT_DRIFTER = createSoundEvent("spell.light_drifter"));
    event.addSound(Spells.LIGHT_DRIFTER_EFFECT_END = createSoundEvent("spell.light_drifter_end"));
    event.addSound(Spells.MAGNETISM = createSoundEvent("spell.magnetism"));
    event.addSound(Spells.NATURES_SCYTHE = createSoundEvent("spell.nature_scythe"));
    event.addSound(Spells.PETAL_SHELL = createSoundEvent("spell.petal_shell"));
    event.addSound(Spells.PETAL_SHELL_EFFECT_BREAK = createSoundEvent("spell.petal_shell_break"));
    event.addSound(Spells.PETAL_SHELL_EFFECT_END = createSoundEvent("spell.petal_shell_end"));
    event.addSound(Spells.RADIANCE = createSoundEvent("spell.radiance"));
    event.addSound(Spells.REACH_EFFECT = createSoundEvent("spell.reach"));
    event.addSound(Spells.REACH_EFFECT_END = createSoundEvent("spell.reach_end"));
    event.addSound(Spells.ROSE_THORNS = createSoundEvent("spell.rose_thorns"));
    event.addSound(Spells.SANCTUARY = createSoundEvent("spell.sanctuary"));
    event.addSound(Spells.SATURATE = createSoundEvent("spell.saturate"));
    event.addSound(Spells.SECOND_WIND = createSoundEvent("spell.second_wind"));
    event.addSound(Spells.SENSE_ANIMALS = createSoundEvent("spell.sense_animals"));
    event.addSound(Spells.SENSE_ANIMALS_EFFECT_END = createSoundEvent("spell.sense_animals_end"));
    event.addSound(Spells.SENSE_DANGER = createSoundEvent("spell.sense_danger"));
    event.addSound(Spells.SENSE_DANGER_EFFECT_END = createSoundEvent("spell.sense_danger_end"));
    event.addSound(Spells.SHATTER = createSoundEvent("spell.shatter"));
    event.addSound(Spells.SKY_SOARER = createSoundEvent("spell.sky_soarer"));
    event.addSound(Spells.STORM_CLOUD = createSoundEvent("spell.storm_cloud"));
    event.addSound(Spells.STORM_CLOUD_END = createSoundEvent("spell.storm_cloud_end"));
    event.addSound(Spells.TIME_STOP = createSoundEvent("spell.time_stop"));
    event.addSound(Spells.TIME_STOP_END = createSoundEvent("spell.time_stop_end"));
    event.addSound(Spells.WILDFIRE = createSoundEvent("spell.wildfire"));
    event.addSound(Events.IMBUER_ADD_ITEM = createSoundEvent("event.imbuer.add_item"));
    event.addSound(Events.IMBUER_REMOVE_ITEM = createSoundEvent("event.imbuer.remove_item"));
    event.addSound(Events.IMBUER_USE = createSoundEvent("event.imbuer.use"));
    event.addSound(Events.IMBUER_FINISHED = createSoundEvent("event.imbuer.finished"));
    event.addSound(Events.MORTAR_ADD_ITEM = createSoundEvent("event.mortar.add_item"));
    event.addSound(Events.MORTAR_REMOVE_ITEM = createSoundEvent("event.mortar.remove_item"));
    event.addSound(Events.MORTAR_USE = createSoundEvent("event.mortar.use"));
    event.addSound(Events.PYRE_ADD_ITEM = createSoundEvent("event.pyre.add_item"));
    event.addSound(Events.PYRE_REMOVE_ITEM = createSoundEvent("event.pyre.remove_item"));
  }

  public static SoundEvent createSoundEvent (String name) {
    return createSoundEvent(new ResourceLocation(Roots.MODID, name));
  }

  public static SoundEvent createSoundEvent(ResourceLocation name) {
    SoundEvent result = new SoundEvent(name);
    result.setRegistryName(name);
    return result;
  }
}
