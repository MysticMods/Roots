package epicsquid.roots.config;

import epicsquid.roots.Roots;
import net.minecraftforge.common.config.Config;

import java.util.Arrays;
import java.util.List;

@Config.LangKey("config.roots.category.rituals")
@Config(modid = Roots.MODID, name = "roots/rituals", category = "rituals")
public class RitualConfig {

  @Config.Name("Fluids to extinguish rituals")
  @Config.Comment("List of fluid names that can be used to extinguish ritual fires if their default temperature doesn't work")
  public static String[] ritualExtinguishFluids = new String[]{};

  @Config.Ignore
  private static List<String> extinguishFluids = null;

  public static List<String> getExtinguishFluids() {
    if (extinguishFluids == null) {
      extinguishFluids = Arrays.asList(ritualExtinguishFluids);
    }
    return extinguishFluids;
  }

  @Config.Name("Animal Harvest produces fish")
  @Config.Comment("Whether or not the Animal Harvest recipe should produce fish in water source thaumcraft.blocks")
  public static boolean animalHarvestDoFish = true;

  @Config.LangKey("config.roots.subcategory.disable_rituals")
  public static DisableRitualCategory disableRitualCategory = new DisableRitualCategory();

  public static class DisableRitualCategory {

    @Config.Name("Disable Animal Harvest Ritual")
    @Config.Comment("Set to true to disable Animal Harvest Ritual")
    public boolean disableAnimalHarvest = false;

    @Config.Name("Disable Fire Storm Ritual")
    @Config.Comment("Set to true to disable Fire Storm Ritual")
    public boolean disableFireStorm = false;

    @Config.Name("Disable Frost Ritual")
    @Config.Comment("Set to true to disable Frost Ritual")
    public boolean disableFrostLands = false;

    @Config.Name("Disable Life Ritual")
    @Config.Comment("Set to true to disable Life Ritual")
    public boolean disableHealingAura = false;

    @Config.Name("Disable Light Ritual")
    @Config.Comment("Set to true to disable Light Ritual")
    public boolean disableDivineProtection = false;

    @Config.Name("Disable Natural Aura Ritual")
    @Config.Comment("Set to true to disable Natural Aura Ritual")
    public boolean disableNaturalAura = false;

    @Config.Name("Disable Overgrowth Ritual")
    @Config.Comment("Set to true to disable Overgrowth Ritual")
    public boolean disableOvergrowth = false;

    @Config.Name("Disable Purity Ritual")
    @Config.Comment("Set to true to disable Purity Ritual")
    public boolean disablePurity = false;

    @Config.Name("Disable Regrowth Ritual")
    @Config.Comment("Set to true to disable Regrowth Ritual")
    public boolean disableNaturalGrowth = false;

    @Config.Name("Disable Storm Ritual")
    @Config.Comment("Set to true to disable Storm Ritual")
    public boolean disableHeavyStorms = false;

    @Config.Name("Disable Transmutation Ritual")
    @Config.Comment("Set to true to disable Transmutation Ritual")
    public boolean disableTransmutation = false;

    @Config.Name("Disable Warden Ritual")
    @Config.Comment("Set to true to disable Warden Ritual")
    public boolean disableWardingProtection = false;

    @Config.Name("Disable Windwall Ritual")
    @Config.Comment("Set to true to disable Windwall Ritual")
    public boolean disableWindwall = false;

    @Config.Name("Disable Flower Growth Ritual")
    @Config.Comment("Set to true to disable Flower Growth Ritual")
    public boolean disableFlowerGrowth = false;

    @Config.Name("Disable WildGrowth Ritual")
    @Config.Comment("Set to true to disable WildGrowth Ritual")
    public boolean disableWildGrowth = false;

    @Config.Name("Disable Summoning Ritual")
    @Config.Comment("Set to true to disable Summoning Ritual")
    public boolean disableSummonCreatures = false;

    @Config.Name("Disable Gathering Ritual")
    @Config.Comment("Set to true to disable Gathering Ritual")
    public boolean disableGathering = false;

  }


}
