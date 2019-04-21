package epicsquid.roots.config;

import epicsquid.roots.Roots;
import net.minecraftforge.common.config.Config;

@Config(modid = Roots.MODID, name = "roots/spells")
public class SpellConfig {

  @Config.Name("Disable Acid Cloud Spell")
  @Config.Comment("Set to true to disable Acid Cloud Spell")
  public static boolean disableAcidCloud = false;
  @Config.Name("Disable Dandelion Winds Spell")
  @Config.Comment("Set to true to disable Dandelion Winds Spell")
  public static boolean disableDandelionWinds = false;
  @Config.Name("Disable Growth Infusion Spell")
  @Config.Comment("Set to true to disable Growth Infusion Spell")
  public static boolean disableGrowthInfusion = false;
  @Config.Name("Disable Harvest Spell")
  @Config.Comment("Set to true to disable Harvest Spell")
  public static boolean disableHarvest = false;
  @Config.Name("Disable Life Drain Spell")
  @Config.Comment("Set to true to disable Life Drain Spell")
  public static boolean disableLifeDrain = false;
  @Config.Name("Disable Light Drifter Spell")
  @Config.Comment("Set to true to disable Light Drifter Spell")
  public static boolean disableLightDrifter = false;
  @Config.Name("Disable Magnetism Spell")
  @Config.Comment("Set to true to disable Magnetism Spell")
  public static boolean disableMagnetism = false;
  @Config.Name("Disable Mind Ward Spell")
  @Config.Comment("Set to true to disable Mind Ward Spell")
  public static boolean disableMindWard = false;
  @Config.Name("Disable Petal Shell Spell")
  @Config.Comment("Set to true to disable Petal Shell Spell")
  public static boolean disablePetalShell = false;
  @Config.Name("Disable Radiance Spell")
  @Config.Comment("Set to true to disable Radiance Spell")
  public static boolean disableRadiance = false;
  @Config.Name("Disable Rampant Growth Spell")
  @Config.Comment("Set to true to disable Rampant Growth Spell")
  public static boolean disableRampantGrowth = false;
  @Config.Name("Disable Rose Thorns Spell")
  @Config.Comment("Set to true to disable Rose Thorns Spell")
  public static boolean disableRoseThorns = false;
  @Config.Name("Disable Sanctuary Spell")
  @Config.Comment("Set to true to disable Sanctuary Spell")
  public static boolean disableSanctuary = false;
  @Config.Name("Disable Second Wind Spell")
  @Config.Comment("Set to true to disable Second Wind Spell")
  public static boolean disableSecondWind = false;
  @Config.Name("Disable Sense Animals Spell")
  @Config.Comment("Set to true to disable Sense Animals Spell")
  public static boolean disableSenseAnimals = false;
  @Config.Name("Disable Sense Danger Spell")
  @Config.Comment("Set to true to disable Sense Danger Spell")
  public static boolean disableSenseDanger = false;
  @Config.Name("Disable Shatter Spell")
  @Config.Comment("Set to true to disable Shatter Spell")
  public static boolean disableShatter= false;
  @Config.Name("Disable Sky Soarer Spell")
  @Config.Comment("Set to true to disable Sky Soarer Spell")
  public static boolean disableSkySoarer = false;
  @Config.Name("Disable Time Stop Spell")
  @Config.Comment("Set to true to disable Time Stop Spell")
  public static boolean disableTimeStop = false;
  @Config.Name("Disable Wild Fire Spell")
  @Config.Comment("Set to true to disable Wild Fire Spell")
  public static boolean disableWildFire = false;



  public static CategoryAcidCloud categoryAcidCloud = new CategoryAcidCloud();
  public static CategoryDandelionWinds categoryDandelionWinds = new CategoryDandelionWinds();
  public static CategoryGrowthInfusion categoryGrowthInfusion = new CategoryGrowthInfusion();
  public static CategoryHarvest categoryHarvest = new CategoryHarvest();
  public static CategoryLifeDrain categoryLifeDrain = new CategoryLifeDrain();
  public static CategoryLightDrifter categoryLightDrifter = new CategoryLightDrifter();
  public static CategoryMagnetism categoryMagnetism = new CategoryMagnetism();
  public static CategoryMindWard categoryMindWard = new CategoryMindWard();
  public static CategoryPetalShell categoryPetalShell = new CategoryPetalShell();
  public static CategoryRadiance categoryRadiance = new CategoryRadiance();
  public static CategoryRampantGrowth categoryRampantGrowth = new CategoryRampantGrowth();

  public static class CategoryAcidCloud {



  }

  public static class CategoryDandelionWinds {

  }

  public static class CategoryGrowthInfusion {

  }

  public static class CategoryHarvest {

  }

  public static class CategoryLifeDrain {

  }

  public static class CategoryLightDrifter {

  }

  public static class CategoryMagnetism {

    @Config.Name("Should Attract Experience Orbs")
    @Config.Comment("When set to true allows the spell to attract xp orbs as well")
    public boolean shouldAttractXP = true;

  }

  public static class CategoryMindWard {

  }

  public static class CategoryPetalShell {

  }

  public static class CategoryRadiance {

  }

  public static class CategoryRampantGrowth {

  }


}
