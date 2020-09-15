package epicsquid.roots.init;

import epicsquid.mysticallib.LibRegistry;
import epicsquid.mysticallib.entity.RenderNull;
import epicsquid.roots.Roots;
import epicsquid.roots.entity.mob.EntityHuskSlave;
import epicsquid.roots.entity.mob.EntityZombieSlave;
import epicsquid.roots.entity.projectile.EntityFlare;
import epicsquid.roots.entity.ritual.*;
import epicsquid.roots.entity.spell.EntityBoost;
import epicsquid.roots.entity.spell.EntityFireJet;
import epicsquid.roots.entity.spell.EntityThornTrap;
import epicsquid.roots.entity.spell.EntityTimeStop;
import epicsquid.roots.proxy.ClientProxy;
import net.minecraft.client.renderer.entity.RenderHusk;
import net.minecraft.client.renderer.entity.RenderZombie;
import net.minecraft.entity.Entity;

import java.util.Arrays;
import java.util.List;

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
    LibRegistry.setActiveMod(Roots.MODID, Roots.CONTAINER);

    // Helper entities
    LibRegistry.registerEntity(EntityFireJet.class);
    LibRegistry.registerEntity(EntityThornTrap.class);
    LibRegistry.registerEntity(EntityTimeStop.class);
    LibRegistry.registerEntity(EntityBoost.class);
    LibRegistry.registerEntity(EntityFlare.class);

    // Ritual entities
    List<Class<? extends Entity>> ritualClasses = Arrays.asList(
        EntityRitualAnimalHarvest.class,
        EntityRitualDivineProtection.class,
        EntityRitualFireStorm.class,
        EntityRitualFlowerGrowth.class,
        EntityRitualFrostLands.class,
        EntityRitualGathering.class,
        EntityRitualGermination.class,
        EntityRitualHealingAura.class,
        EntityRitualHeavyStorms.class,
        EntityRitualOvergrowth.class,
        EntityRitualPurity.class,
        EntityRitualSpreadingForest.class,
        EntityRitualTransmutation.class,
        EntityRitualWardingProtection.class,
        EntityRitualWildrootGrowth.class,
        EntityRitualWindwall.class,
        EntityRitualSummonCreatures.class
    );

    ritualClasses.forEach(LibRegistry::registerEntity);

    // Slaves
    LibRegistry.registerEntity(EntityHuskSlave.class);
    LibRegistry.registerEntity(EntityZombieSlave.class);

    if (Roots.proxy instanceof ClientProxy) {
      LibRegistry.registerEntityRenderer(EntityFireJet.class, new RenderNull.Factory());
      LibRegistry.registerEntityRenderer(EntityThornTrap.class, new RenderNull.Factory());
      LibRegistry.registerEntityRenderer(EntityTimeStop.class, new RenderNull.Factory());
      LibRegistry.registerEntityRenderer(EntityBoost.class, new RenderNull.Factory());
      LibRegistry.registerEntityRenderer(EntityFlare.class, new RenderNull.Factory());

      ritualClasses.forEach(c -> LibRegistry.registerEntityRenderer(c, new RenderNull.Factory()));

      LibRegistry.registerEntityRenderer(EntityHuskSlave.class, RenderHusk::new);
      LibRegistry.registerEntityRenderer(EntityZombieSlave.class, RenderZombie::new);
    }
  }

  public static void registerLootTables() {
  }
}
