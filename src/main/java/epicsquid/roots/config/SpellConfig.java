package epicsquid.roots.config;

import epicsquid.roots.Roots;
import net.minecraftforge.common.config.Config;

@Config(modid = Roots.MODID, name = "roots/spells")
public class SpellConfig {

  public static CategoryAcidCloud categoryAcidCloud = new CategoryAcidCloud();
  public static CategoryDandelionWinds categoryDandelionWinds = new CategoryDandelionWinds();

  public static class CategoryAcidCloud {

    @Config.Name("Acid Cloud Spell Cooldown")
    @Config.Comment("Sets the cooldown time for Acid Cloud")
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

    public int cooldown = 20;

  }

}
