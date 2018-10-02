package epicsquid.roots.init;

public class ModEntities {

  /**
   * Registers mobs in the game
   * 
   * Egg colours are defined as Background colour then Foreground (spots) colour
   *
   * 
   * Format for registering a mob:
   * 
   * LibRegistry.registerEntity(Entity.class, BackgroundColour, ForegroundColour);
   * if (Mod.proxy instanceof ClientProxy)
   *   LibRegistry.registerEntityRenderer(Entity.class, new RenderEntity.Factory());
   */
  public static void registerMobs() {

  }

  /**
   * Registers the spawns of a mob in the world
   */
  public static void registerMobSpawn() {
    
  }
}
