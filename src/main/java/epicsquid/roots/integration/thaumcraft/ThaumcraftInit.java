package epicsquid.roots.integration.thaumcraft;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;

public class ThaumcraftInit {
  public static void init() {
    if (Loader.isModLoaded("thaumcraft")) {
      MinecraftForge.EVENT_BUS.register(AspectRegistry.class);
    }
  }
}
