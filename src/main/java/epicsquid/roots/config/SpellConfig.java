package epicsquid.roots.config;

import epicsquid.roots.Roots;
import net.minecraftforge.common.config.Config;

@Config(modid = Roots.MODID, name = "roots/spells")
public class SpellConfig {

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
