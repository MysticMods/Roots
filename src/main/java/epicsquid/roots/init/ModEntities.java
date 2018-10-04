package epicsquid.roots.init;

import epicsquid.mysticallib.LibRegistry;
import epicsquid.mysticallib.entity.RenderNull;
import epicsquid.roots.proxy.ClientProxy;
import epicsquid.roots.Roots;
import epicsquid.roots.entity.ritual.EntityFlare;

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
    LibRegistry.registerEntity(EntityFlare.class, 0xD46724, 0xF5E0D3);
    if (Roots.proxy instanceof ClientProxy)
      LibRegistry.registerEntityRenderer(EntityFlare.class, new RenderNull.Factory());
  }

  /**
   * Registers the spawns of a mob in the world
   */
  public static void registerMobSpawn() {

  }
}
