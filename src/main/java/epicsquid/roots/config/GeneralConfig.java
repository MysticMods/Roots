package epicsquid.roots.config;

import epicsquid.mysticallib.util.ConfigUtil;
import epicsquid.roots.Roots;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Set;

@Mod.EventBusSubscriber(modid=Roots.MODID)
@Config.LangKey("config.roots.category.general")
@Config(modid = Roots.MODID, name = "roots/general", category="main")
@SuppressWarnings("unused")
public class GeneralConfig {
  @SubscribeEvent(priority = EventPriority.HIGH)
  public static void onConfigChanged (ConfigChangedEvent.OnConfigChangedEvent event) {
    if (event.getModID().equals(Roots.MODID)) {
      ConfigManager.sync(Roots.MODID, Config.Type.INSTANCE);
    }
  }

  @Config.Comment(("Set to false to disable repairing items in an Imbuer"))
  public static boolean AllowImbuerRepair = true;

  @Config.Comment(("Set to false to prevent runic dust from removing enchants with an Imbuer"))
  public static boolean AllowImbuerDisenchant = true;

  @Config.Comment(("Divisor of max damage of an item to calculte how much is repaired in an Imbuer (formula: max damage / X)"))
  public static int MaxDamageDivisor = 4;

  @Config.Comment(("Client side only: disable elemental soil particles"))
  public static boolean DisableParticles = false;

  @Config.Comment(("Set to true to give players the Roots Guide Book upon joining the server"))
  public static boolean GiveBook = false;

  @Config.Comment(("Inject some items from Roots into dungeon & other loot chests"))
  public static boolean InjectLoot = true;

  @Config.Comment(("Minimum number of pulls for injected loot"))
  public static int InjectMinimum = 1;

  @Config.Comment(("Maximum nubmer of pulls for injected loot"))
  public static int InjectMaximum = 1;

  @Config.Comment(("1 in X chance per random tick for the Grove Stone to do anything"))
  public static int GroveStoneChance = 2;

  @Config.Comment(("Set to false to disable Grove Stone environmental effects"))
  public static boolean EnableGroveStoneEnvironment = true;

  @Config.Comment(("Set to true to automatically equip component and apothecary pouches when right-clicking"))
  public static boolean AutoEquipPouches = false;

  @Config.Comment(("Set to true to automatically refill your component and apothecary pouches when picking up herbs"))
  public static boolean AutoRefillPouches = false;

  @Config.Comment(("The aoe-radius for using runic shears to aoe-shear things"))
  public static int RunicShearsRadius = 15;

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

  @Config.Comment(("List of mod:item:meta (meta optional) of saplings that should be blacklisted from the Spreading Forest ritual"))
  public static String[] SaplingBlacklist = new String[]{"roots:wildwood_sapling"};

  @Config.Ignore
  private static Set<ItemStack> saplingBlacklist = null;

  public static Set<ItemStack> getSaplingBlacklist () {
    if (saplingBlacklist == null) {
      saplingBlacklist = ConfigUtil.parseItemStacksSet(SaplingBlacklist);
    }

    return saplingBlacklist;
  }

  @Config.Comment(("Name of the liquid as per the Forge registry to provide from the Unending Bowl"))
  public static String FluidName = "water";
}


