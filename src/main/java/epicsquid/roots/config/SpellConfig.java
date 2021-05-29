package epicsquid.roots.config;

import epicsquid.roots.Roots;
import epicsquid.roots.spell.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Config.LangKey("config.roots.category.spells")
@Config(modid = Roots.MODID, name = "roots/spells", category = "spells")
public class SpellConfig {
  @SubscribeEvent(priority = EventPriority.HIGH)
  public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
    if (event.getModID().equals(Roots.MODID)) {
      ConfigManager.sync(Roots.MODID, Config.Type.INSTANCE);
      SpellWildfire.instance.setSound(SpellConfig.spellSoundsCategory.soundWildFire);
      SpellSanctuary.instance.setSound(SpellConfig.spellSoundsCategory.soundSanctuary);
      SpellDandelionWinds.instance.setSound(SpellConfig.spellSoundsCategory.soundDandelionWinds);
      SpellRoseThorns.instance.setSound(SpellConfig.spellSoundsCategory.soundRoseThorns);
      SpellShatter.instance.setSound(SpellConfig.spellSoundsCategory.soundShatter);
      SpellPetalShell.instance.setSound(SpellConfig.spellSoundsCategory.soundPetalShell);
      SpellTimeStop.instance.setSound(SpellConfig.spellSoundsCategory.soundTimeStop);
      SpellSkySoarer.instance.setSound(SpellConfig.spellSoundsCategory.soundSkySoarer);
      SpellLifeDrain.instance.setSound(SpellConfig.spellSoundsCategory.soundLifeDrain);
      SpellAcidCloud.instance.setSound(SpellConfig.spellSoundsCategory.soundAcidCloud);
      SpellGrowthInfusion.instance.setSound(SpellConfig.spellSoundsCategory.soundGrowthInfusion);
      SpellGeas.instance.setSound(SpellConfig.spellSoundsCategory.soundGeas);
      SpellRadiance.instance.setSound(SpellConfig.spellSoundsCategory.soundRadiance);
      SpellHarvest.instance.setSound(SpellConfig.spellSoundsCategory.soundHarvest);
      SpellFeyLight.instance.setSound(SpellConfig.spellSoundsCategory.soundFeyLight);
      SpellStormCloud.instance.setSound(SpellConfig.spellSoundsCategory.soundStormCloud);
      SpellSaturate.instance.setSound(SpellConfig.spellSoundsCategory.soundSaturate);
      SpellDesaturate.instance.setSound(SpellConfig.spellSoundsCategory.soundDesaturate);
      SpellChrysopoeia.instance.setSound(SpellConfig.spellSoundsCategory.soundChrysopoeia);
      SpellDisarm.instance.setSound(SpellConfig.spellSoundsCategory.soundDisarm);
      SpellNaturesScythe.instance.setSound(SpellConfig.spellSoundsCategory.soundNaturesScythe);
      SpellAquaBubble.instance.setSound(SpellConfig.spellSoundsCategory.soundAquaBubble);
      SpellAugment.instance.setSound(SpellConfig.spellSoundsCategory.soundAugment);
      SpellExtension.instance.setSound(SpellConfig.spellSoundsCategory.soundExtension);
    }
  }

  @Config.LangKey("config.roots.subcategory.disable_spells")
  public static DisableSpellsCategory disableSpellsCategory = new DisableSpellsCategory();
  @Config.LangKey("config.roots.subcategory.spell_features")
  public static SpellFeaturesCategory spellFeaturesCategory = new SpellFeaturesCategory();
  @Config.LangKey("config.roots.subcategory.spell_sounds")
  public static SpellSoundsCategory spellSoundsCategory = new SpellSoundsCategory();

  public static class DisableSpellsCategory {
    @Config.Name("Disable Acid Cloud Spell")
    @Config.Comment("Set to true to disable Acid Cloud Spell")
    public boolean disableAcidCloud = false;
    @Config.Name("Disable Aqueous Bubble Spell")
    @Config.Comment("Set to true to disable the Aqueous Bubble Spell")
    public boolean disableAquaBubble = false;
    @Config.Name("Disable Augment Spell")
    @Config.Comment("Set to true to disable the Augment Spell")
    public boolean disableAugment = false;
    @Config.Name("Disable Chrysopoeia Spell")
    @Config.Comment("Set to true to disable the Chrysopoeia Spell")
    public boolean disableChrysopoeia = false;
    @Config.Name("Disable Dandelion Winds Spell")
    @Config.Comment("Set to true to disable Dandelion Winds Spell")
    public boolean disableDandelionWinds = false;
    @Config.Name("Disable Desaturate Spell")
    @Config.Comment("Set to true to disable the Desaturate Spell")
    public boolean disableDesaturate = false;
    @Config.Name("Disable Disarm Spell")
    @Config.Comment("Set to true to disable Disarm Spell")
    public boolean disableDisarm = false;
    @Config.Name("Disable Extension Spell")
    @Config.Comment("Set to true to disable the Extension Spell")
    public boolean disableExtension = false;
    @Config.Name("Disable Fey Light Spell")
    @Config.Comment("Set to true to disable the Fey Light Spell")
    public boolean disableFeyLight = false;
    @Config.Name("Disable Geas Spell")
    @Config.Comment("Set to true to disable Geas Spell")
    public boolean disableGeas = false;
    @Config.Name("Disable Growth Infusion Spell")
    @Config.Comment("Set to true to disable Growth Infusion Spell")
    public boolean disableGrowthInfusion = false;
    @Config.Name("Disable Harvest Spell")
    @Config.Comment("Set to true to disable Harvest Spell")
    public boolean disableHarvest = false;
    @Config.Name("Disable Life Drain Spell")
    @Config.Comment("Set to true to disable Life Drain Spell")
    public boolean disableLifeDrain = false;
    @Config.Name("Disable Nature's Scythe")
    @Config.Comment("Set to true to disable Nature's Scythe Spell")
    public boolean disableNaturesScythe = false;
    @Config.Name("Disable Petal Shell Spell")
    @Config.Comment("Set to true to disable Petal Shell Spell")
    public boolean disablePetalShell = false;
    @Config.Name("Disable Radiance Spell")
    @Config.Comment("Set to true to disable Radiance Spell")
    public boolean disableRadiance = false;
    @Config.Name("Disable Rose Thorns Spell")
    @Config.Comment("Set to true to disable Rose Thorns Spell")
    public boolean disableRoseThorns = false;
    @Config.Name("Disable Sanctuary Spell")
    @Config.Comment("Set to true to disable Sanctuary Spell")
    public boolean disableSanctuary = false;
    @Config.Name("Disable Saturate Spell")
    @Config.Comment("Set to true to disable the Saturate Spell")
    public boolean disableSaturate = false;
    @Config.Name("Disable Shatter Spell")
    @Config.Comment("Set to true to disable Shatter Spell")
    public boolean disableShatter = false;
    @Config.Name("Disable Sky Soarer Spell")
    @Config.Comment("Set to true to disable Sky Soarer Spell")
    public boolean disableSkySoarer = false;
    @Config.Name("Disable Storm Cloud Spell")
    @Config.Comment("Set to true to disable the Storm Cloud Spell")
    public boolean disbleStormCloud = false;
    @Config.Name("Disable Time Stop Spell")
    @Config.Comment("Set to true to disable Time Stop Spell")
    public boolean disableTimeStop = false;
    @Config.Name("Disable Wild Fire Spell")
    @Config.Comment("Set to true to disable Wild Fire Spell")
    public boolean disableWildFire = false;
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
    @Config.Comment("Entities in this list will be excluded by Sanctuary Spell when repelling thaumcraft.entities (formatted as mod:mobname)")
    public String[] sanctuaryEntitiesBlacklist = {};

    @Config.Ignore
    public Set<ResourceLocation> resources = null;

    public Set<ResourceLocation> getSanctuaryBlacklist() {
      if (resources == null) {
        resources = Stream.of(sanctuaryEntitiesBlacklist).map(ResourceLocation::new).collect(Collectors.toSet());
      }
      return resources;
    }
  }

  public static class SpellSoundsCategory {
    @Config.Name("Acid Cloud Spell")
    @Config.Comment("Sounds settings for Acid Cloud Spell")
    public SpellSound soundAcidCloud = new SpellSound();
    @Config.Name("Aqueous Bubble Spell")
    @Config.Comment("Sounds settings for the Aqueous Bubble Spell")
    public SpellSound soundAquaBubble = new SpellSound();
    @Config.Name("Augment Spell")
    @Config.Comment("Sounds settings for the Augment Spell")
    public SpellSound soundAugment = new SpellSound();
    @Config.Name("Chrysopoeia Spell")
    @Config.Comment("Sounds settings for the Chrysopoeia Spell")
    public SpellSound soundChrysopoeia = new SpellSound();
    @Config.Name("Dandelion Winds Spell")
    @Config.Comment("Sounds settings for Dandelion Winds Spell")
    public SpellSound soundDandelionWinds = new SpellSound();
    @Config.Name("Desaturate Spell")
    @Config.Comment("Sounds settings for the Desaturate Spell")
    public SpellSound soundDesaturate = new SpellSound();
    @Config.Name("Disarm Spell")
    @Config.Comment("Sounds settings for Disarm Spell")
    public SpellSound soundDisarm = new SpellSound();
    @Config.Name("Extension Spell")
    @Config.Comment("Sounds settings for the Extension Spell")
    public SpellSound soundExtension = new SpellSound();
    @Config.Name("Fey Light Spell")
    @Config.Comment("Sounds settings for the Fey Light Spell")
    public SpellSound soundFeyLight = new SpellSound();
    @Config.Name("Geas Spell")
    @Config.Comment("Sounds settings for Geas Spell")
    public SpellSound soundGeas = new SpellSound();
    @Config.Name("Growth Infusion Spell")
    @Config.Comment("Sounds settings for Growth Infusion Spell")
    public SpellSound soundGrowthInfusion = new SpellSound();
    @Config.Name("Harvest Spell")
    @Config.Comment("Sounds settings for Harvest Spell")
    public SpellSound soundHarvest = new SpellSound();
    @Config.Name("Life Drain Spell")
    @Config.Comment("Sounds settings for Life Drain Spell")
    public SpellSound soundLifeDrain = new SpellSound();
    @Config.Name("Nature's Scythe")
    @Config.Comment("Sounds settings for Nature's Scythe Spell")
    public SpellSound soundNaturesScythe = new SpellSound();
    @Config.Name("Petal Shell Spell")
    @Config.Comment("Sounds settings for Petal Shell Spell")
    public SpellSound soundPetalShell = new SpellSound();
    @Config.Name("Radiance Spell")
    @Config.Comment("Sounds settings for Radiance Spell")
    public SpellSound soundRadiance = new SpellSound();
    @Config.Name("Rose Thorns Spell")
    @Config.Comment("Sounds settings for Rose Thorns Spell")
    public SpellSound soundRoseThorns = new SpellSound();
    @Config.Name("Sanctuary Spell")
    @Config.Comment("Sounds settings for Sanctuary Spell")
    public SpellSound soundSanctuary = new SpellSound();
    @Config.Name("Saturate Spell")
    @Config.Comment("Sounds settings for the Saturate Spell")
    public SpellSound soundSaturate = new SpellSound();
    @Config.Name("Shatter Spell")
    @Config.Comment("Sounds settings for Shatter Spell")
    public SpellSound soundShatter = new SpellSound();
    @Config.Name("Sky Soarer Spell")
    @Config.Comment("Sounds settings for Sky Soarer Spell")
    public SpellSound soundSkySoarer = new SpellSound();
    @Config.Name("Storm Cloud Spell")
    @Config.Comment("Sounds settings for the Storm Cloud Spell")
    public SpellSound soundStormCloud = new SpellSound();
    @Config.Name("Time Stop Spell")
    @Config.Comment("Sounds settings for Time Stop Spell")
    public SpellSound soundTimeStop = new SpellSound();
    @Config.Name("Wild Fire Spell")
    @Config.Comment("Sounds settings for Wild Fire Spell")
    public SpellSound soundWildFire = new SpellSound();

    public static class SpellSound {
      @Config.Name("Enable sound")
      @Config.Comment("Set to false to prevent the sounds for this spell from being played")
      public boolean enabled = true;
      @Config.Name("Volume")
      @Config.Comment("Set the volume of the sounds for this spell")
      @Config.RangeDouble(min = 0, max = 1)
      public double volume = 1;
    }
  }

}
