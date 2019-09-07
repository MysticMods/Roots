package epicsquid.roots.config;

import epicsquid.mysticallib.util.ConfigUtil;
import epicsquid.roots.Roots;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
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
  private static Set<Block> growthBlacklist = null;

  @SuppressWarnings("ConstantConditions")
  public static Set<Block> getGrowthBlacklist() {
    if (growthBlacklist == null) {
      growthBlacklist = ConfigUtil.parseBlocksSet(GrowthBlacklist);
    }
    return growthBlacklist;
  }

  @Config.Comment(("List of mod:item:meta (meta optional) of saplings that should be planted in 2x2 by the Spreading Forest ritual"))
  public static String[] TwoByTwoSaplings = new String[]{"thaumcraft:sapling_greatwood"};

  @Config.Ignore
  private static Set<ItemStack> twoByTwoSaplings = null;

  public static Set<ItemStack> getTwoByTwoSaplings () {
    if (twoByTwoSaplings == null) {
      twoByTwoSaplings = ConfigUtil.parseItemStacksSet(TwoByTwoSaplings);
    }

    return twoByTwoSaplings;
  }

  @Config.Comment(("List of mod:item:meta (meta option) of saplings that should be blacklisted from the Spreading Forest ritual"))
  public static String[] SaplingBlacklist = new String[]{"roots:wildwood_sapling"};

  @Config.Ignore
  private static Set<ItemStack> saplingBlacklist = null;

  public static Set<ItemStack> getSaplingBlacklist () {
    if (saplingBlacklist == null) {
      saplingBlacklist = ConfigUtil.parseItemStacksSet(SaplingBlacklist);
    }

    return saplingBlacklist;
  }
}


