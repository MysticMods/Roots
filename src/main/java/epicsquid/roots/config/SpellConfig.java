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

  public static class CategoryAcidCloud {

    @Config.Name("Acid Cloud Spell Cooldown")
    @Config.Comment("Sets the cooldown time in ticks for Acid Cloud")
    @Config.RangeInt(min = 0)
    public int cooldown = 24;

    @Config.Name("Acid Cloud Spell Damage")
    @Config.Comment("Sets how much damage per hit is dealt by Acid Cloud")
    @Config.RangeDouble(min = 0)
    public float damage = 1F;

    @Config.Name("Acid Cloud Poison Effect Duration")
    @Config.Comment("Sets how long in ticks poisoning from acid cloud should affect the player")
    @Config.RangeInt(min = 0)
    public int poisoningDuration = 80;

    @Config.Name("Acid Cloud fire effect duration")
    @Config.Comment("Sets how long in seconds the player should be on fire (Only in case the extra fire module is loaded)")
    @Config.RangeInt(min = 0)
    public int fireDuration = 5;

  }

  public static class CategoryDandelionWinds {

    @Config.Name("Dandelion Winds Spell Cooldown")
    @Config.Comment("Sets the cooldown time in ticks for Dandelion Winds")
    @Config.RangeInt(min = 0)
    public int cooldown = 20;

    @Config.Name("Dandelion Winds Intensity Multiplier")
    @Config.Comment("Sets how strong is the push of Dandelion Winds")
    @Config.RangeDouble(min = 0)
    public float intensityMultiplier = 1F;

  }

  public static class CategoryGrowthInfusion {

    @Config.Name("Growth Infusion Spell Cooldown")
    @Config.Comment("Sets the cooldown time in ticks for Growth Infusion")
    @Config.RangeInt(min = 0)
    public int cooldown = 16;

  }

  public static class CategoryHarvest {

    @Config.Name("Harvest Spell Cooldown")
    @Config.Comment("Sets the cooldown time in ticks for Harvest")
    @Config.RangeInt(min = 0)
    public int cooldown = 25;

    @Config.Name("Harvest Spell X-Z Radius")
    @Config.Comment("The radius in which the spell can work")
    @Config.RangeInt(min = 1)
    public int radius = 6;

    @Config.Name("Harvest Spell Y Radius")
    @Config.Comment("The highest and lowest y level the spell can work at")
    @Config.RangeInt(min = 0)
    public int Yradius = 5;

  }

public static class CategoryLifeDrain {

  @Config.Name("Life Drain Spell Cooldown")
  @Config.Comment("Sets the cooldown time in ticks for Life Drain")
  @Config.RangeInt(min = 0)
  public int cooldown = 28;

  @Config.Name("Life Drain Spell Damage")
  @Config.Comment("Sets how much damage per hit is dealt by Life Drain")
  @Config.RangeDouble(min = 0)
  public float damage = 1F;

  @Config.Name("Life Drain Spell Healing")
  @Config.Comment("Sets how much the player is healed per hit (by default is half the damage)")
  @Config.RangeDouble(min = 0)
  public float healing = 0.5F;

}

public static class CategoryLightDrifter {

  @Config.Name("Light Drifter Spell Cooldown")
  @Config.Comment("Sets the cooldown time in ticks for Light Drifter")
  @Config.RangeInt(min = 0)
  public int cooldown = 200;

  @Config.Name("Light Drift Effect Duration")
  @Config.Comment("Controls how much time in ticks the player can stay in light drift mode")
  @Config.RangeInt(min = 20)
  public int driftingDuration = 100;

}

}
