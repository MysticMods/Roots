package epicsquid.roots.config;

import epicsquid.mysticallib.util.ItemUtil;
import epicsquid.roots.Roots;
import epicsquid.roots.util.StateUtil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Config;

@Config.LangKey("config.roots.category.runed_wood")
@Config(modid = Roots.MODID, name = "roots/runed_wood", category = "runed_wood")
public class RunedWoodConfig {
  @Config.Comment(("Configurations for the replacement wood for the Acacia runed pillar (note that the same configuration CAN be used for multiple runed pillars"))
  public static RunedPillarConfig ACACIA = new RunedPillarConfig("roots:runed_acacia", "minecraft:log2[variant=acacia]", "minecraft:log2:0");

  @Config.Comment(("Configurations for the replacement wood for the Oak runed pillar (note that the same configuration CAN be used for multiple runed pillars"))
  public static RunedPillarConfig OAK = new RunedPillarConfig("roots:runed_oak", "minecraft:log[variant=oak]", "minecraft:log:0");

  @Config.Comment(("Configurations for the replacement wood for the Dark Oak runed pillar (note that the same configuration CAN be used for multiple runed pillars"))
  public static RunedPillarConfig DARK_OAK = new RunedPillarConfig("roots:runed_dark_oak", "minecraft:log2[variant=dark_oak]", "minecraft:log2:1");

  @Config.Comment(("Configurations for the replacement wood for the Birch runed pillar (note that the same configuration CAN be used for multiple runed pillars"))
  public static RunedPillarConfig BIRCH = new RunedPillarConfig("roots:runed_birch", "minecraft:log[variant=birch]", "minecraft:log:2");

  @Config.Comment(("Configurations for the replacement wood for the Jungle runed pillar (note that the same configuration CAN be used for multiple runed pillars"))
  public static RunedPillarConfig JUNGLE = new RunedPillarConfig("roots:runed_jungle", "minecraft:log[variant=jungle]", "minecraft:log:3");

  @Config.Comment(("Configurations for the replacement wood for the Spruce runed pillar (note that the same configuration CAN be used for multiple runed pillars"))
  public static RunedPillarConfig SPRUCE = new RunedPillarConfig("roots:runed_spruce", "minecraft:log[variant=spruce]", "minecraft:log:1");

  @Config.Comment(("Configurations for the replacement wood for the Wildwood runed pillar (note that the same configuration CAN be used for multiple runed pillars"))
  public static RunedPillarConfig WILDWOOD = new RunedPillarConfig("roots:runed_wildwood", "roots:wildwood_log", "roots:wildwood_log:0");

  public static class RunedPillarConfig {
    @Config.Comment(("The blockstate that defines the capstone of the runed pillar (i.e, 'roots:runed_acacia'"))
    public String capstone;

    @Config.Ignore
    public StateUtil.StateMatcher capstoneMatcher = null;

    @Config.Comment(("The blockstate that defines the pillar of the runed pillar (i.e., 'minecraft:log[variant=birch]'"))
    public String pillar;

    @Config.Ignore
    public StateUtil.StateMatcher pillarMatcher = null;

    @Config.Comment(("The itemstack that defines the pillar of the runed pillar (i.e., 'minecraft:log:2'"))
    public String item;

    @Config.Ignore
    public ItemStack itemStack = null;

    public RunedPillarConfig(String capstone, String pillar, String item) {
      this.capstone = capstone;
      this.pillar = pillar;
      this.item = item;
    }

    public StateUtil.StateMatcher getCapstoneMatcher() {
      if (capstoneMatcher == null) {
        capstoneMatcher = new StateUtil.StateMatcher(capstone);
      }
      return capstoneMatcher;
    }

    public IBlockState getCapstoneState () {
      return getCapstoneMatcher().getState();
    }

    public IBlockState getPillarState () {
      return getPillarMatcher().getState();
    }

    public StateUtil.StateMatcher getPillarMatcher() {
      if (pillarMatcher == null) {
        pillarMatcher = new StateUtil.StateMatcher(pillar);
      }
      return pillarMatcher;
    }

    public ItemStack getItemStack() {
      if (itemStack == null) {
        itemStack = ItemUtil.stackFromString(item.split(":"));
      }
      return itemStack;
    }
  }
}
