package epicsquid.roots.config;

import com.google.common.collect.Sets;
import epicsquid.mysticallib.util.ConfigUtil;
import epicsquid.roots.Roots;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Config;

import java.util.Set;

@Config.LangKey("config.roots.category.crops")
@Config(modid = Roots.MODID, name = "roots/crops", category = "crops")
public class CropConfig {
  @Config.Comment(("List of mod:thaumcraft.blocks to ignore when growing crops, etc; use /roots growables for a complete list (for individual thaumcraft.blocks)"))
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

  @Config.Comment(("List of modids that should be ignored when growing crops, etc (for complete mods)"))
  public static String[] GrowthModBlacklist = new String[]{};

  @Config.Ignore
  private static Set<String> growthModBlacklist = null;

  public static Set<String> getGrowthModBlacklist() {
    if (growthModBlacklist == null) {
      growthModBlacklist = Sets.newHashSet(GrowthModBlacklist);
    }

    return growthModBlacklist;
  }

  @Config.Comment(("List of modids that should be excluded from Harvesting (for complete mods)"))
  public static String[] HarvestModBlacklist = new String[]{"rustic", "simplecorn", "teastory"};

  @Config.Ignore
  public static Set<String> harvestModBlacklist = null;

  public static Set<String> getHarvestModBlacklist() {
    if (harvestModBlacklist == null) {
      harvestModBlacklist = Sets.newHashSet(HarvestModBlacklist);
    }

    return harvestModBlacklist;
  }

  @Config.Comment(("List modid:name of thaumcraft.blocks/resource locations that should be excluded from Harvesting (for individual crops)"))
  public static String[] HarvestBlacklist = new String[]{};

  @Config.Ignore
  public static Set<ResourceLocation> harvestBlacklist = null;

  public static Set<ResourceLocation> getHarvestBlacklist() {
    if (harvestBlacklist == null) {
      harvestBlacklist = ConfigUtil.parseLocationsSet(HarvestBlacklist);
    }

    return harvestBlacklist;
  }
}
