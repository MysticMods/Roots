package epicsquid.roots.config;

import epicsquid.roots.Roots;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Config;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Config.LangKey("config.roots.category.general")
@Config(modid = Roots.MODID, name = "roots/general")
@SuppressWarnings("unused")
public class GeneralConfig {
  @Config.Comment(("Inject some items from Roots into dungeon & other loot chests"))
  public static boolean InjectLoot = true;

  @Config.Comment(("Minimum number of pulls for injected loot"))
  public static int InjectMinimum = 1;

  @Config.Comment(("Maximum nubmer of pulls for injected loot"))
  public static int InjectMaximum = 1;

  @Config.Comment(("Set to true to automatically equip component and apothecary pouches when right-clicking"))
  public static boolean AutoEquipPouches = false;

  @Config.Comment(("Set to true to automatically refill your component and apothecary pouches when picking up herbs"))
  public static boolean AutoRefillPouches = true;

  @Config.Comment(("The aoe-radius for using runic shears to aoe-shear things"))
  public static int RunicShearsRadius = 15;

  @Config.Comment(("List of mod:blocks to ignore when growing crops, etc; use /roots growables for a complete list"))
  public static String[] GrowthBlacklist = new String[]{"minecraft:tallgrass"};

  @Config.Ignore
  private static boolean growthParsed = false;

  @Config.Ignore
  private static Set<Block> growthBlacklist = null;

  public static Set<Block> getGrowthBlacklist() {
    if (growthBlacklist == null) {
      growthParsed = true;
      growthBlacklist = new HashSet<>();
      for (String ref : GrowthBlacklist) {
        ResourceLocation res = new ResourceLocation(ref);
        Block block = Block.REGISTRY.getObject(res);
        if (block != null) {
          growthBlacklist.add(block);
        }
      }
    }
    return growthBlacklist;
  }
}


