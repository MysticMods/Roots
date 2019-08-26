package epicsquid.roots.handler;

import epicsquid.roots.Roots;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ConfigHandler {

  @SubscribeEvent
  public static void syncConfig(ConfigChangedEvent.OnConfigChangedEvent event) {
    if (event.getModID().equals(Roots.MODID))
      ConfigManager.sync(Roots.MODID, Config.Type.INSTANCE);
  }

}
