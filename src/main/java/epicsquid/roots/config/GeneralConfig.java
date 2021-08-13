package epicsquid.roots.config;

import epicsquid.mysticallib.util.ConfigUtil;
import epicsquid.roots.Roots;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;

@Mod.EventBusSubscriber(modid = Roots.MODID)
@Config.LangKey("config.roots.category.general")
@Config(modid = Roots.MODID, name = "roots/general", category = "main")
@SuppressWarnings("unused")
public class GeneralConfig {
  @SubscribeEvent(priority = EventPriority.HIGH)
  public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
    if (event.getModID().equals(Roots.MODID)) {
      ConfigManager.sync(Roots.MODID, Config.Type.INSTANCE);
    }
  }

  @Config.Comment(("Set to false to disable repairing items in an Imbuer"))
  public static boolean AllowImbuerRepair = true;

  @Config.Comment(("Set to false to prevent runic dust from removing enchants with an Imbuer"))
  public static boolean AllowImbuerDisenchant = true;

  @Config.Comment(("Set to false to prevent Imbuer disenchantment operations from reducing the repair cost of an item"))
  public static boolean AllowImbuerDisenchantReduceCost = true;

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

  @Config.Comment(("The health threshold at which entities are considered boss-type creatures (in half hearts)"))
  public static float BossEntityHealth = 40;

  @Config.Comment(("List of mod:item:meta (meta optional) of saplings that should be planted in 2x2 by the Spreading Forest ritual"))
  public static String[] TwoByTwoSaplings = new String[]{"thaumcraft:sapling_greatwood"};

  @Config.Ignore
  private static Set<ItemStack> twoByTwoSaplings = null;

  public static Set<ItemStack> getTwoByTwoSaplings() {
    if (twoByTwoSaplings == null) {
      twoByTwoSaplings = ConfigUtil.parseItemStacksSet(TwoByTwoSaplings);
    }

    return twoByTwoSaplings;
  }

  @Config.Comment(("List of mod:item:meta (meta optional) of saplings that should be blacklisted from the Spreading Forest ritual"))
  public static String[] SaplingBlacklist = new String[]{
      "roots:wildwood_sapling",
      "corvus:frankinsence_sapling",
      "thebetweenlands:sapling_spirit_tree"
  };

  @Config.Ignore
  private static Set<ItemStack> saplingBlacklist = null;

  public static Set<ItemStack> getSaplingBlacklist() {
    if (saplingBlacklist == null) {
      saplingBlacklist = ConfigUtil.parseItemStacksSet(SaplingBlacklist);
    }

    return saplingBlacklist;
  }

  @Config.Comment(("Name of the liquid as per the Forge registry to provide from the Unending Bowl"))
  public static String FluidName = "water";

  @Nullable
  public static Fluid getFluid() {
    return FluidRegistry.getFluid(FluidName);
  }

  @Config.Comment(("How long it takes a Fire Starter to start a fire (in ticks)"))
  public static int FireStarterTicks = 20 * 3;

  @Config.Comment(("Whether or not the Untrue Pacifist advancement should be tracked"))
  public static boolean UntruePacifist = true;

  @Config.Comment(("Whether or not the Wild Mage villager career should be populated (note: this may break pre-existing worlds if changed)"))
  public static boolean WildMageVillager = true;

  @Config.Comment(("List of blocks that Fey Crafters and Runic Crafters should not output to"))
  public static String[] crafterOutputBlackist = new String[]{
      "minecraft:dispenser"
  };

  @Config.Ignore
  private static Set<Block> crafterOutputIgnore = null;

  public static Set<Block> getCrafterOutputIgnore() {
    if (crafterOutputIgnore == null) {
      crafterOutputIgnore = new HashSet<>();
      for (String rl : crafterOutputBlackist) {
        Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(rl));
        if (block == null) {
          Roots.logger.error("Invalid Configuration Value for: crafterOutputBlacklist.\n  - " + rl + " is not a valid block.");
        } else {
          crafterOutputIgnore.add(block);
        }
      }
    }
    return crafterOutputIgnore;
  }

  @Config.Comment(("List of blocks that Overgrowth and Terra Moss should consider water for adjacency purposes"))
  public static String[] waterBlocks = new String[]{
      "minecraft:water",
      "minecraft:flowing_water"
  };

  @Config.Comment(("The block that is considered the cardinal definition of water"))
  public static String waterBlock = "minecraft:water";

  @Config.Comment(("Whether or not the firestarter should be injected into JEI"))
  public static boolean injectFirestarter = true;

  @Config.Ignore
  private static Block actualWaterBlock = null;

  @Config.Ignore
  private static Set<Block> actualWaterBlocks = null;

  public static Set<Block> getWaterBlocks() {
    if (actualWaterBlocks == null) {
      actualWaterBlocks = new HashSet<>();
      for (String rl : waterBlocks) {
        Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(rl));
        if (block == null) {
          Roots.logger.error("Invalid Configuration Value for: waterBlocks.\n  - " + rl + " is not a valid block.");
        } else {
          actualWaterBlocks.add(block);
        }
      }
    }
    return actualWaterBlocks;
  }

  public static Block getWaterBlock() {
    if (actualWaterBlock == null) {
      Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(waterBlock));
      if (block == null) {
        Roots.logger.error("Invalid Configuration Value for: waterBlock.\n  - " + waterBlock + " is not a valid block.");
      } else {
        actualWaterBlock = block;
      }
    }
    return actualWaterBlock;
  }
}


