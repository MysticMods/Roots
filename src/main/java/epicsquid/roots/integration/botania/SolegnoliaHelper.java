package epicsquid.roots.integration.botania;

import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.Loader;
import vazkii.botania.api.BotaniaAPI;

public class SolegnoliaHelper {
  private static boolean hasBotania = false;
  private static boolean flag = false;

  public static boolean hasBotania () {
    if (flag) return hasBotania;

    flag = true;
    hasBotania = Loader.isModLoaded("botania");

    return hasBotania;
  }

  public static boolean hasSolegnoliaAround (Entity entity) {
    return BotaniaAPI.internalHandler.hasSolegnoliaAround(entity);
  }
}
