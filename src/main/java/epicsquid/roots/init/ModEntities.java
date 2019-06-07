package epicsquid.roots.init;

import epicsquid.mysticallib.LibRegistry;
import epicsquid.mysticallib.entity.RenderNull;
import epicsquid.roots.Roots;
import epicsquid.roots.entity.grove.EntityNaturalGrove;
import epicsquid.roots.entity.grove.EntityWildGrove;
import epicsquid.roots.entity.render.RenderPetalShell;
import epicsquid.roots.entity.ritual.*;
import epicsquid.roots.entity.spell.*;
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

    LibRegistry.registerEntity(EntityRitualHealingAura.class);
    LibRegistry.registerEntity(EntityRitualHeavyStorms.class);
    LibRegistry.registerEntity(EntityRitualDivineProtection.class);
    LibRegistry.registerEntity(EntityRitualFireStorm.class);
    LibRegistry.registerEntity(EntityFlare.class);
    LibRegistry.registerEntity(EntityRitualNaturalGrowth.class);
    LibRegistry.registerEntity(EntityRitualWindwall.class);
    LibRegistry.registerEntity(EntityRitualWardingProtection.class);
    LibRegistry.registerEntity(EntitySpawnItem.class);
    LibRegistry.registerEntity(EntityRitualOvergrowth.class);
    LibRegistry.registerEntity(EntityRitualFrostLands.class);
    LibRegistry.registerEntity(EntityRitualFlowerGrowth.class);

    LibRegistry.registerEntity(EntityWildGrove.class);
    LibRegistry.registerEntity(EntityNaturalGrove.class);

    if (Roots.proxy instanceof ClientProxy) {
      LibRegistry.registerEntityRenderer(EntityFireJet.class, new RenderNull.Factory());
      LibRegistry.registerEntityRenderer(EntityThornTrap.class, new RenderNull.Factory());
      LibRegistry.registerEntityRenderer(EntityPetalShell.class, new RenderPetalShell.Factory());
      LibRegistry.registerEntityRenderer(EntityTimeStop.class, new RenderNull.Factory());
      LibRegistry.registerEntityRenderer(EntityBoost.class, new RenderNull.Factory());

      LibRegistry.registerEntityRenderer(EntityRitualHealingAura.class, new RenderNull.Factory());
      LibRegistry.registerEntityRenderer(EntityRitualHeavyStorms.class, new RenderNull.Factory());
      LibRegistry.registerEntityRenderer(EntityRitualDivineProtection.class, new RenderNull.Factory());
      LibRegistry.registerEntityRenderer(EntityRitualFireStorm.class, new RenderNull.Factory());
      LibRegistry.registerEntityRenderer(EntityFlare.class, new RenderNull.Factory());
      LibRegistry.registerEntityRenderer(EntityRitualNaturalGrowth.class, new RenderNull.Factory());
      LibRegistry.registerEntityRenderer(EntityRitualWindwall.class, new RenderNull.Factory());
      LibRegistry.registerEntityRenderer(EntityRitualWardingProtection.class, new RenderNull.Factory());
      LibRegistry.registerEntityRenderer(EntitySpawnItem.class, new RenderNull.Factory());
      LibRegistry.registerEntityRenderer(EntityRitualOvergrowth.class, new RenderNull.Factory());
      LibRegistry.registerEntityRenderer(EntityRitualFrostLands.class, new RenderNull.Factory());
      LibRegistry.registerEntityRenderer(EntityRitualFlowerGrowth.class, new RenderNull.Factory());

      LibRegistry.registerEntityRenderer(EntityWildGrove.class, new RenderNull.Factory());
      LibRegistry.registerEntityRenderer(EntityNaturalGrove.class, new RenderNull.Factory());
    }
  }

  /**
   * Registers the spawns of a mob in the world
   */
  public static void registerMobSpawn() {

  }
}
