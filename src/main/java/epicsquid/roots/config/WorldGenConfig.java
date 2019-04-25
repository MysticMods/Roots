package epicsquid.roots.config;

import epicsquid.roots.Roots;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.fml.common.Mod;

@Config.LangKey("config.roots.category.worldgen")
@Config(modid = Roots.MODID, name = "roots/world_gen")
@Mod.EventBusSubscriber(modid = Roots.MODID)
public class WorldGenConfig {

  @Config.Comment("Worldgen settings")
  public static ConfigWorldGen worldGen = new ConfigWorldGen();

  public static class ConfigWorldGen {
    @Config.Comment("Minimum amount = 0, Default = 200, Configures the generation chance of the Barrow structure. Higher numbers mean less structures.")
    public int barrowChance = 200;
    @Config.Comment("Minimum amount = 0, Default = 180, Configures the generation chance of the Hut structure. Higher numbers mean less structures.")
    public int hutChance = 180;
  }
}