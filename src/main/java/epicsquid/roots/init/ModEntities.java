package epicsquid.roots.init;

import epicsquid.mysticallib.LibRegistry;
import epicsquid.mysticallib.entity.RenderNull;
import epicsquid.roots.Roots;
import epicsquid.roots.entity.grove.EntityWildlandsGrove;
import epicsquid.roots.entity.ritual.EntityFlare;
import epicsquid.roots.entity.ritual.EntityRitualFireStorm;
import epicsquid.roots.entity.ritual.EntityRitualLife;
import epicsquid.roots.entity.ritual.EntityRitualLight;
import epicsquid.roots.entity.ritual.EntityRitualRegrowth;
import epicsquid.roots.entity.ritual.EntityRitualStorm;
import epicsquid.roots.entity.ritual.EntityRitualWarden;
import epicsquid.roots.entity.ritual.EntityRitualWindwall;
import epicsquid.roots.entity.spell.EntityBoost;
import epicsquid.roots.entity.spell.EntityFireJet;
import epicsquid.roots.entity.spell.EntityPetalShell;
import epicsquid.roots.entity.spell.EntityThornTrap;
import epicsquid.roots.entity.spell.EntityTimeStop;
import epicsquid.roots.proxy.ClientProxy;

public class ModEntities {

  /**
   * Registers mobs in the game
   * <p>
   * Egg colours are defined as Background colour then Foreground (spots) colour
   * <p>
   * <p>
   * Format for registering a mob:
   * <p>
   * LibRegistry.registerEntity(Entity.class, BackgroundColour, ForegroundColour);
   * if (Mod.proxy instanceof ClientProxy)
   * LibRegistry.registerEntityRenderer(Entity.class, new RenderEntity.Factory());
   */
  public static void registerMobs() {
    //Spell & Ritual Entities
    LibRegistry.registerEntity(EntityFireJet.class);
    LibRegistry.registerEntity(EntityThornTrap.class);
    LibRegistry.registerEntity(EntityPetalShell.class);
    LibRegistry.registerEntity(EntityTimeStop.class);
    LibRegistry.registerEntity(EntityBoost.class);

    LibRegistry.registerEntity(EntityRitualLife.class);
    LibRegistry.registerEntity(EntityRitualStorm.class);
    LibRegistry.registerEntity(EntityRitualLight.class);
    LibRegistry.registerEntity(EntityRitualFireStorm.class);
    LibRegistry.registerEntity(EntityFlare.class);
    LibRegistry.registerEntity(EntityRitualRegrowth.class);
    LibRegistry.registerEntity(EntityRitualWindwall.class);
    LibRegistry.registerEntity(EntityRitualWarden.class);

    LibRegistry.registerEntity(EntityWildlandsGrove.class);

    if (Roots.proxy instanceof ClientProxy) {
      LibRegistry.registerEntityRenderer(EntityFireJet.class, new RenderNull.Factory());
      LibRegistry.registerEntityRenderer(EntityThornTrap.class, new RenderNull.Factory());
      LibRegistry.registerEntityRenderer(EntityPetalShell.class, new RenderNull.Factory());
      LibRegistry.registerEntityRenderer(EntityTimeStop.class, new RenderNull.Factory());
      LibRegistry.registerEntityRenderer(EntityBoost.class, new RenderNull.Factory());

      LibRegistry.registerEntityRenderer(EntityRitualLife.class, new RenderNull.Factory());
      LibRegistry.registerEntityRenderer(EntityRitualStorm.class, new RenderNull.Factory());
      LibRegistry.registerEntityRenderer(EntityRitualLight.class, new RenderNull.Factory());
      LibRegistry.registerEntityRenderer(EntityRitualFireStorm.class, new RenderNull.Factory());
      LibRegistry.registerEntityRenderer(EntityFlare.class, new RenderNull.Factory());
      LibRegistry.registerEntityRenderer(EntityRitualRegrowth.class, new RenderNull.Factory());
      LibRegistry.registerEntityRenderer(EntityRitualWindwall.class, new RenderNull.Factory());
      LibRegistry.registerEntityRenderer(EntityRitualWarden.class, new RenderNull.Factory());

      LibRegistry.registerEntityRenderer(EntityWildlandsGrove.class, new RenderNull.Factory());
    }
  }

  /**
   * Registers the spawns of a mob in the world
   */
  public static void registerMobSpawn() {

  }
}
