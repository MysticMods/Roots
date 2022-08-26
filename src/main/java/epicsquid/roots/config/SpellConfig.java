package epicsquid.roots.config;

import epicsquid.roots.Roots;
import net.minecraftforge.common.config.Config;

@Config.LangKey("config.roots.category.spells")
@Config(modid = Roots.MODID, name = "roots/spells", category = "spells")
public class SpellConfig {


  @Config.LangKey("config.roots.subcategory.disable_spells")
  public static DisableSpellsCategory disableSpellsCategory = new DisableSpellsCategory();
  @Config.LangKey("config.roots.subcategory.spell_features")
  public static SpellFeaturesCategory spellFeaturesCategory = new SpellFeaturesCategory();

  public static class DisableSpellsCategory {

    @Config.Name("Disable Acid Cloud Spell")
    @Config.Comment("Set to true to disable Acid Cloud Spell")
    public boolean disableAcidCloud = false;
    @Config.Name("Disable Nature's Scythe")
    @Config.Comment("Set to true to disable Nature's Scythe Spell")
    public boolean disableNaturesScythe = false;
    @Config.Name("Disable Dandelion Winds Spell")
    @Config.Comment("Set to true to disable Dandelion Winds Spell")
    public boolean disableDandelionWinds = false;
    @Config.Name("Disable Disarm Spell")
    @Config.Comment("Set to true to disable Disarm Spell")
    public boolean disableDisarm = false;
    @Config.Name("Disable Autumn's Fall Spell")
    @Config.Comment("Set to true to disable Autumn's Fall Spell")
    public boolean disableAutumnsFall = false;
    @Config.Name("Disable Growth Infusion Spell")
    @Config.Comment("Set to true to disable Growth Infusion Spell")
    public boolean disableGrowthInfusion = false;
    @Config.Name("Disable Harvest Spell")
    @Config.Comment("Set to true to disable Harvest Spell")
    public boolean disableHarvest = false;
    @Config.Name("Disable Life Drain Spell")
    @Config.Comment("Set to true to disable Life Drain Spell")
    public boolean disableLifeDrain = false;
    @Config.Name("Disable Light Drifter Spell")
    @Config.Comment("Set to true to disable Light Drifter Spell")
    public boolean disableLightDrifter = false;
    @Config.Name("Disable Magnetism Spell")
    @Config.Comment("Set to true to disable Magnetism Spell")
    public boolean disableMagnetism = false;
    @Config.Name("Disable Geas Spell")
    @Config.Comment("Set to true to disable Geas Spell")
    public boolean disableGeas = false;
    @Config.Name("Disable Petal Shell Spell")
    @Config.Comment("Set to true to disable Petal Shell Spell")
    public boolean disablePetalShell = false;
    @Config.Name("Disable Radiance Spell")
    @Config.Comment("Set to true to disable Radiance Spell")
    public boolean disableRadiance = false;
    @Config.Name("Disable Rampant Growth Spell")
    @Config.Comment("Set to true to disable Rampant Growth Spell")
    public boolean disableRampantGrowth = false;
    @Config.Name("Disable Rose Thorns Spell")
    @Config.Comment("Set to true to disable Rose Thorns Spell")
    public boolean disableRoseThorns = false;
    @Config.Name("Disable Sanctuary Spell")
    @Config.Comment("Set to true to disable Sanctuary Spell")
    public boolean disableSanctuary = false;
    @Config.Name("Disable Second Wind Spell")
    @Config.Comment("Set to true to disable Second Wind Spell")
    public boolean disableSecondWind = false;
    @Config.Name("Disable Sense Animals Spell")
    @Config.Comment("Set to true to disable Sense Animals Spell")
    public boolean disableSenseAnimals = false;
    @Config.Name("Disable Sense Danger Spell")
    @Config.Comment("Set to true to disable Sense Danger Spell")
    public boolean disableSenseDanger = false;
    @Config.Name("Disable Shatter Spell")
    @Config.Comment("Set to true to disable Shatter Spell")
    public boolean disableShatter = false;
    @Config.Name("Disable Sky Soarer Spell")
    @Config.Comment("Set to true to disable Sky Soarer Spell")
    public boolean disableSkySoarer = false;
    @Config.Name("Disable Time Stop Spell")
    @Config.Comment("Set to true to disable Time Stop Spell")
    public boolean disableTimeStop = false;
    @Config.Name("Disable Wild Fire Spell")
    @Config.Comment("Set to true to disable Wild Fire Spell")
    public boolean disableWildFire = false;
    @Config.Name("Disable Fey Light Spell")
    @Config.Comment("Set to true to disable Fey Light Spell")
    public boolean disableFeyLight = false;
    @Config.Name("Disable Winter Cloud Spell")
    @Config.Comment("Set to true to disable the Winter Cloud Spell")
    public boolean disableWinterCloud = false;
    @Config.Name("Disable Spring Storm Spell")
    @Config.Comment("Set to true to disable the Spring Storm Spell")
    public boolean disableSpringStorm = false;
    @Config.Name("Disable Reach Spell")
    @Config.Comment("Set to true to disable the Reach Spell")
    public boolean disableReach = false;
    @Config.Name("Disable Saturate Spell")
    @Config.Comment("Set to true to disable the Saturate Spell")
    public boolean disableSaturate = false;
    @Config.Name("Disable Desaturate Spell")
    @Config.Comment("Set to true to disable the Desaturate Spell")
    public boolean disableDesaturate = false;
    @Config.Name("Disable Chrysopoeia Spell")
    @Config.Comment("Set to true to disable the Chrysopoeia Spell")
    public boolean disableChrysopoeia = false;
    @Config.Name("Disable Aqueous Bubble Spell")
    @Config.Comment("Set to true to disable the Aqueous Bubble Spell")
    public boolean disableAquaBubble = false;
  }

  public static class SpellFeaturesCategory {

    @Config.Name("Should Acid Cloud Poison Enemies")
    @Config.Comment("Set to false to disable Acid Cloud's poisoning effect on enemies")
    public boolean acidCloudPoisoningEffect = true;

    @Config.Name("Should Magnetism Attract Experience Orbs")
    @Config.Comment("Set to true to allow the spell to attract xp orbs as well")
    public boolean shouldMagnetismAttractXP = true;

    @Config.RequiresMcRestart
    @Config.Name("Sanctuary Spell Entities Blacklist")
    @Config.Comment("Entities in this list will be excluded by Sanctuary Spell when repelling entities")
    public String[] sanctuaryEntitiesBlacklist = {};
  }

}
