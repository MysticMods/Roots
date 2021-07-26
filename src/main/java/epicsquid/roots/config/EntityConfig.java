package epicsquid.roots.config;

import epicsquid.mysticallib.util.ConfigUtil;
import epicsquid.roots.Roots;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Set;

@Mod.EventBusSubscriber(modid = Roots.MODID)
@Config.LangKey("config.roots.category.entity")
@Config(modid = Roots.MODID, name = "roots/entity", category = "main")
@SuppressWarnings({"unused", "WeakerAccess"})
public class EntityConfig {
  @SubscribeEvent(priority = EventPriority.HIGH)
  public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
    if (event.getModID().equals(Roots.MODID)) {
      ConfigManager.sync(Roots.MODID, Config.Type.INSTANCE);
    }
  }

  // ******************
  //      GENERAL
  // ******************

  @Config.Comment(("List of entities that should be considered specifically hostile for the purposes of spells and rituals (modid:entityname)"))
  public static String[] EntityBlacklist = new String[]{
      "minecraft:villager"
  };

  @Config.Ignore
  private static Set<ResourceLocation> entityBlacklist = null;

  public static Set<ResourceLocation> getEntityBlacklist() {
    if (entityBlacklist == null) {
      entityBlacklist = ConfigUtil.parseLocationsSet(EntityBlacklist);
    }

    return entityBlacklist;
  }

  @Config.Comment(("List of entities that should be considered specifically friendly for the purposes of spells and rituals (modid:entityname)"))
  public static String[] EntityWhitelist = new String[]{
      "minecraft:enderman"
  };

  @Config.Ignore
  private static Set<ResourceLocation> entityWhitelist = null;

  public static Set<ResourceLocation> getEntityWhitelist() {
    if (entityWhitelist == null) {
      entityWhitelist = ConfigUtil.parseLocationsSet(EntityWhitelist);
    }

    return entityWhitelist;
  }

  // ******************
  //     DANDELION
  // ******************

  @Config.Comment(("List of entities that should be considered specifically hostile for the purposes of Dandelion Winds (modid:dandelionname)"))
  public static String[] DandelionBlacklist = new String[]{
      "minecraft:villager"
  };

  @Config.Ignore
  private static Set<ResourceLocation> dandelionBlacklist = null;

  public static Set<ResourceLocation> getDandelionBlacklist() {
    if (dandelionBlacklist == null) {
      dandelionBlacklist = ConfigUtil.parseLocationsSet(DandelionBlacklist);
    }

    return dandelionBlacklist;
  }

  @Config.Comment(("List of entities that should be considered specifically prevented from being knocked back by Dandelion Winds (modid:entity)"))
  public static String[] DandelionWhitelist = new String[]{
      "minecraft:enderman"
  };

  @Config.Ignore
  private static Set<ResourceLocation> dandelionWhitelist = null;

  public static Set<ResourceLocation> getDandelionWhitelist() {
    if (dandelionWhitelist == null) {
      dandelionWhitelist = ConfigUtil.parseLocationsSet(DandelionWhitelist);
    }

    return dandelionWhitelist;
  }

  // ******************
  //     Wildfire
  // ******************

  @Config.Comment(("List of entities that should be considered specifically hostile for the purposes of Wildfire (modid:wildfirename)"))
  public static String[] WildfireBlacklist = new String[]{
      "minecraft:villager"
  };

  @Config.Ignore
  private static Set<ResourceLocation> wildfireBlacklist = null;

  public static Set<ResourceLocation> getWildfireBlacklist() {
    if (wildfireBlacklist == null) {
      wildfireBlacklist = ConfigUtil.parseLocationsSet(WildfireBlacklist);
    }

    return wildfireBlacklist;
  }

  @Config.Comment(("List of entities that should be considered considered friendly for the purposes of Wildfire (modid:entity)"))
  public static String[] WildfireWhitelist = new String[]{
      "minecraft:enderman"
  };

  @Config.Ignore
  private static Set<ResourceLocation> wildfireWhitelist = null;

  public static Set<ResourceLocation> getWildfireWhitelist() {
    if (wildfireWhitelist == null) {
      wildfireWhitelist = ConfigUtil.parseLocationsSet(WildfireWhitelist);
    }

    return wildfireWhitelist;
  }

  // ******************
  //     FireStorm
  // ******************

  @Config.Comment(("List of entities that should be considered specifically hostile for the purposes of FireStorm (modid:fireStormname)"))
  public static String[] FireStormBlacklist = new String[]{
      "minecraft:villager"
  };

  @Config.Ignore
  private static Set<ResourceLocation> fireStormBlacklist = null;

  public static Set<ResourceLocation> getFireStormBlacklist() {
    if (fireStormBlacklist == null) {
      fireStormBlacklist = ConfigUtil.parseLocationsSet(FireStormBlacklist);
    }

    return fireStormBlacklist;
  }

  @Config.Comment(("List of entities that should be considered considered friendly for the purposes of Fire Storm (modid:entity)"))
  public static String[] FireStormWhitelist = new String[]{
      "minecraft:enderman"
  };

  @Config.Ignore
  private static Set<ResourceLocation> fireStormWhitelist = null;

  public static Set<ResourceLocation> getFireStormWhitelist() {
    if (fireStormWhitelist == null) {
      fireStormWhitelist = ConfigUtil.parseLocationsSet(FireStormWhitelist);
    }

    return fireStormWhitelist;
  }

  // ******************
  //     HealingAura
  // ******************

  @Config.Comment(("List of entities that should be considered specifically hostile for the purposes of HealingAura (modid:healingAuraname)"))
  public static String[] HealingAuraBlacklist = new String[]{
      "minecraft:villager"
  };

  @Config.Ignore
  private static Set<ResourceLocation> healingAuraBlacklist = null;

  public static Set<ResourceLocation> getHealingAuraBlacklist() {
    if (healingAuraBlacklist == null) {
      healingAuraBlacklist = ConfigUtil.parseLocationsSet(HealingAuraBlacklist);
    }

    return healingAuraBlacklist;
  }

  @Config.Comment(("List of entities that should be considered considered friendly for the purposes of Fire Storm (modid:entity)"))
  public static String[] HealingAuraWhitelist = new String[]{
      "minecraft:enderman"
  };

  @Config.Ignore
  private static Set<ResourceLocation> healingAuraWhitelist = null;

  public static Set<ResourceLocation> getHealingAuraWhitelist() {
    if (healingAuraWhitelist == null) {
      healingAuraWhitelist = ConfigUtil.parseLocationsSet(HealingAuraWhitelist);
    }

    return healingAuraWhitelist;
  }

  // ******************
  //     Purity
  // ******************

  @Config.Comment(("List of entities that should be considered specifically hostile for the purposes of Purity (modid:purityname)"))
  public static String[] PurityBlacklist = new String[]{
      "minecraft:villager"
  };

  @Config.Ignore
  private static Set<ResourceLocation> purityBlacklist = null;

  public static Set<ResourceLocation> getPurityBlacklist() {
    if (purityBlacklist == null) {
      purityBlacklist = ConfigUtil.parseLocationsSet(PurityBlacklist);
    }

    return purityBlacklist;
  }

  @Config.Comment(("List of entities that should be considered considered friendly for the purposes of Fire Storm (modid:entity)"))
  public static String[] PurityWhitelist = new String[]{
      "minecraft:enderman"
  };

  @Config.Ignore
  private static Set<ResourceLocation> purityWhitelist = null;

  public static Set<ResourceLocation> getPurityWhitelist() {
    if (purityWhitelist == null) {
      purityWhitelist = ConfigUtil.parseLocationsSet(PurityWhitelist);
    }

    return purityWhitelist;
  }

  // ******************
  //     WindWall
  // ******************

  @Config.Comment(("List of entities that should be considered specifically hostile for the purposes of WindWall (modid:windWallname)"))
  public static String[] WindWallBlacklist = new String[]{
      "minecraft:villager"
  };

  @Config.Ignore
  private static Set<ResourceLocation> windWallBlacklist = null;

  public static Set<ResourceLocation> getWindWallBlacklist() {
    if (windWallBlacklist == null) {
      windWallBlacklist = ConfigUtil.parseLocationsSet(WindWallBlacklist);
    }

    return windWallBlacklist;
  }

  @Config.Comment(("List of entities that should be considered considered friendly for the purposes of Fire Storm (modid:entity)"))
  public static String[] WindWallWhitelist = new String[]{
      "minecraft:enderman"
  };

  @Config.Ignore
  private static Set<ResourceLocation> windWallWhitelist = null;

  public static Set<ResourceLocation> getWindWallWhitelist() {
    if (windWallWhitelist == null) {
      windWallWhitelist = ConfigUtil.parseLocationsSet(WindWallWhitelist);
    }

    return windWallWhitelist;
  }

  // ******************
  //     RoseThorns
  // ******************

  @Config.Comment(("List of entities that should be considered specifically hostile for the purposes of RoseThorns (modid:roseThornsname)"))
  public static String[] RoseThornsBlacklist = new String[]{
      "minecraft:villager"
  };

  @Config.Ignore
  private static Set<ResourceLocation> roseThornsBlacklist = null;

  public static Set<ResourceLocation> getRoseThornsBlacklist() {
    if (roseThornsBlacklist == null) {
      roseThornsBlacklist = ConfigUtil.parseLocationsSet(RoseThornsBlacklist);
    }

    return roseThornsBlacklist;
  }

  @Config.Comment(("List of entities that should be considered considered friendly for the purposes of Fire Storm (modid:entity)"))
  public static String[] RoseThornsWhitelist = new String[]{
      "minecraft:enderman"
  };

  @Config.Ignore
  private static Set<ResourceLocation> roseThornsWhitelist = null;

  public static Set<ResourceLocation> getRoseThornsWhitelist() {
    if (roseThornsWhitelist == null) {
      roseThornsWhitelist = ConfigUtil.parseLocationsSet(RoseThornsWhitelist);
    }

    return roseThornsWhitelist;
  }

  // ******************
  //     TimeStop
  // ******************

  @Config.Comment(("List of entities that should be considered specifically hostile for the purposes of TimeStop (modid:timeStopname)"))
  public static String[] TimeStopBlacklist = new String[]{
      "minecraft:villager"
  };

  @Config.Ignore
  private static Set<ResourceLocation> timeStopBlacklist = null;

  public static Set<ResourceLocation> getTimeStopBlacklist() {
    if (timeStopBlacklist == null) {
      timeStopBlacklist = ConfigUtil.parseLocationsSet(TimeStopBlacklist);
    }

    return timeStopBlacklist;
  }

  @Config.Comment(("List of entities that should be considered considered friendly for the purposes of Fire Storm (modid:entity)"))
  public static String[] TimeStopWhitelist = new String[]{
      "minecraft:enderman"
  };

  @Config.Ignore
  private static Set<ResourceLocation> timeStopWhitelist = null;

  public static Set<ResourceLocation> getTimeStopWhitelist() {
    if (timeStopWhitelist == null) {
      timeStopWhitelist = ConfigUtil.parseLocationsSet(TimeStopWhitelist);
    }

    return timeStopWhitelist;
  }

  // ******************
  //     Extension
  // ******************

  @Config.Comment(("List of entities that should be considered specifically hostile for the purposes of Extension (modid:extensionname)"))
  public static String[] ExtensionBlacklist = new String[]{
      "minecraft:villager"
  };

  @Config.Ignore
  private static Set<ResourceLocation> extensionBlacklist = null;

  public static Set<ResourceLocation> getExtensionBlacklist() {
    if (extensionBlacklist == null) {
      extensionBlacklist = ConfigUtil.parseLocationsSet(ExtensionBlacklist);
    }

    return extensionBlacklist;
  }

  @Config.Comment(("List of entities that should be considered considered friendly for the purposes of Fire Storm (modid:entity)"))
  public static String[] ExtensionWhitelist = new String[]{
      "minecraft:enderman"
  };

  @Config.Ignore
  private static Set<ResourceLocation> extensionWhitelist = null;

  public static Set<ResourceLocation> getExtensionWhitelist() {
    if (extensionWhitelist == null) {
      extensionWhitelist = ConfigUtil.parseLocationsSet(ExtensionWhitelist);
    }

    return extensionWhitelist;
  }

  // ******************
  //     StormCloud
  // ******************

  @Config.Comment(("List of entities that should be considered specifically hostile for the purposes of StormCloud (modid:stormCloudname)"))
  public static String[] StormCloudBlacklist = new String[]{
      "minecraft:villager"
  };

  @Config.Ignore
  private static Set<ResourceLocation> stormCloudBlacklist = null;

  public static Set<ResourceLocation> getStormCloudBlacklist() {
    if (stormCloudBlacklist == null) {
      stormCloudBlacklist = ConfigUtil.parseLocationsSet(StormCloudBlacklist);
    }

    return stormCloudBlacklist;
  }

  @Config.Comment(("List of entities that should be considered considered friendly for the purposes of Fire Storm (modid:entity)"))
  public static String[] StormCloudWhitelist = new String[]{
      "minecraft:enderman"
  };

  @Config.Ignore
  private static Set<ResourceLocation> stormCloudWhitelist = null;

  public static Set<ResourceLocation> getStormCloudWhitelist() {
    if (stormCloudWhitelist == null) {
      stormCloudWhitelist = ConfigUtil.parseLocationsSet(StormCloudWhitelist);
    }

    return stormCloudWhitelist;
  }

  // ******************
  //     AcidCloud
  // ******************

  @Config.Comment(("List of entities that should be considered specifically hostile for the purposes of AcidCloud (modid:acidCloudname)"))
  public static String[] AcidCloudBlacklist = new String[]{
      "minecraft:villager"
  };

  @Config.Ignore
  private static Set<ResourceLocation> acidCloudBlacklist = null;

  public static Set<ResourceLocation> getAcidCloudBlacklist() {
    if (acidCloudBlacklist == null) {
      acidCloudBlacklist = ConfigUtil.parseLocationsSet(AcidCloudBlacklist);
    }

    return acidCloudBlacklist;
  }

  @Config.Comment(("List of entities that should be considered considered friendly for the purposes of Fire Storm (modid:entity)"))
  public static String[] AcidCloudWhitelist = new String[]{
      "minecraft:enderman"
  };

  @Config.Ignore
  private static Set<ResourceLocation> acidCloudWhitelist = null;

  public static Set<ResourceLocation> getAcidCloudWhitelist() {
    if (acidCloudWhitelist == null) {
      acidCloudWhitelist = ConfigUtil.parseLocationsSet(AcidCloudWhitelist);
    }

    return acidCloudWhitelist;
  }

  // ******************
  //     Desaturate
  // ******************

  @Config.Comment(("List of entities that should be considered specifically hostile for the purposes of Desaturate (modid:desaturatename)"))
  public static String[] DesaturateBlacklist = new String[]{
      "minecraft:villager"
  };

  @Config.Ignore
  private static Set<ResourceLocation> desaturateBlacklist = null;

  public static Set<ResourceLocation> getDesaturateBlacklist() {
    if (desaturateBlacklist == null) {
      desaturateBlacklist = ConfigUtil.parseLocationsSet(DesaturateBlacklist);
    }

    return desaturateBlacklist;
  }

  @Config.Comment(("List of entities that should be considered considered friendly for the purposes of Fire Storm (modid:entity)"))
  public static String[] DesaturateWhitelist = new String[]{
      "minecraft:enderman"
  };

  @Config.Ignore
  private static Set<ResourceLocation> desaturateWhitelist = null;

  public static Set<ResourceLocation> getDesaturateWhitelist() {
    if (desaturateWhitelist == null) {
      desaturateWhitelist = ConfigUtil.parseLocationsSet(DesaturateWhitelist);
    }

    return desaturateWhitelist;
  }

  // ******************
  //     Disarm
  // ******************

  @Config.Comment(("List of entities that should be considered specifically hostile for the purposes of Disarm (modid:disarmname)"))
  public static String[] DisarmBlacklist = new String[]{
      "minecraft:villager"
  };

  @Config.Ignore
  private static Set<ResourceLocation> disarmBlacklist = null;

  public static Set<ResourceLocation> getDisarmBlacklist() {
    if (disarmBlacklist == null) {
      disarmBlacklist = ConfigUtil.parseLocationsSet(DisarmBlacklist);
    }

    return disarmBlacklist;
  }

  @Config.Comment(("List of entities that should be considered considered friendly for the purposes of Fire Storm (modid:entity)"))
  public static String[] DisarmWhitelist = new String[]{
      "minecraft:enderman"
  };

  @Config.Ignore
  private static Set<ResourceLocation> disarmWhitelist = null;

  public static Set<ResourceLocation> getDisarmWhitelist() {
    if (disarmWhitelist == null) {
      disarmWhitelist = ConfigUtil.parseLocationsSet(DisarmWhitelist);
    }

    return disarmWhitelist;
  }

  // ******************
  //     LifeDrain
  // ******************

  @Config.Comment(("List of entities that should be considered specifically hostile for the purposes of LifeDrain (modid:lifeDrainname)"))
  public static String[] LifeDrainBlacklist = new String[]{
      "minecraft:villager"
  };

  @Config.Ignore
  private static Set<ResourceLocation> lifeDrainBlacklist = null;

  public static Set<ResourceLocation> getLifeDrainBlacklist() {
    if (lifeDrainBlacklist == null) {
      lifeDrainBlacklist = ConfigUtil.parseLocationsSet(LifeDrainBlacklist);
    }

    return lifeDrainBlacklist;
  }

  @Config.Comment(("List of entities that should be considered considered friendly for the purposes of Fire Storm (modid:entity)"))
  public static String[] LifeDrainWhitelist = new String[]{
      "minecraft:enderman"
  };

  @Config.Ignore
  private static Set<ResourceLocation> lifeDrainWhitelist = null;

  public static Set<ResourceLocation> getLifeDrainWhitelist() {
    if (lifeDrainWhitelist == null) {
      lifeDrainWhitelist = ConfigUtil.parseLocationsSet(LifeDrainWhitelist);
    }

    return lifeDrainWhitelist;
  }

  // ******************
  //     Radiance
  // ******************

  @Config.Comment(("List of entities that should be considered specifically hostile for the purposes of Radiance (modid:radiancename)"))
  public static String[] RadianceBlacklist = new String[]{
      "minecraft:villager"
  };

  @Config.Ignore
  private static Set<ResourceLocation> radianceBlacklist = null;

  public static Set<ResourceLocation> getRadianceBlacklist() {
    if (radianceBlacklist == null) {
      radianceBlacklist = ConfigUtil.parseLocationsSet(RadianceBlacklist);
    }

    return radianceBlacklist;
  }

  @Config.Comment(("List of entities that should be considered considered friendly for the purposes of Fire Storm (modid:entity)"))
  public static String[] RadianceWhitelist = new String[]{
      "minecraft:enderman"
  };

  @Config.Ignore
  private static Set<ResourceLocation> radianceWhitelist = null;

  public static Set<ResourceLocation> getRadianceWhitelist() {
    if (radianceWhitelist == null) {
      radianceWhitelist = ConfigUtil.parseLocationsSet(RadianceWhitelist);
    }

    return radianceWhitelist;
  }

  // ******************
  //     Sanctuary
  // ******************

  @Config.Comment(("List of entities that should be considered specifically hostile for the purposes of Sanctuary (modid:sanctuaryname)"))
  public static String[] SanctuaryBlacklist = new String[]{
      "minecraft:villager"
  };

  @Config.Ignore
  private static Set<ResourceLocation> sanctuaryBlacklist = null;

  public static Set<ResourceLocation> getSanctuaryBlacklist() {
    if (sanctuaryBlacklist == null) {
      sanctuaryBlacklist = ConfigUtil.parseLocationsSet(SanctuaryBlacklist);
    }

    return sanctuaryBlacklist;
  }

  @Config.Comment(("List of entities that should be considered considered friendly for the purposes of Fire Storm (modid:entity)"))
  public static String[] SanctuaryWhitelist = new String[]{
      "minecraft:enderman"
  };

  @Config.Ignore
  private static Set<ResourceLocation> sanctuaryWhitelist = null;

  public static Set<ResourceLocation> getSanctuaryWhitelist() {
    if (sanctuaryWhitelist == null) {
      sanctuaryWhitelist = ConfigUtil.parseLocationsSet(SanctuaryWhitelist);
    }

    return sanctuaryWhitelist;
  }

  // ******************
  //     Geas
  // ******************

  @Config.Comment(("List of entities that should be considered specifically hostile for the purposes of Geas (modid:geasname)"))
  public static String[] GeasBlacklist = new String[]{
      "minecraft:villager"
  };

  @Config.Ignore
  private static Set<ResourceLocation> geasBlacklist = null;

  public static Set<ResourceLocation> getGeasBlacklist() {
    if (geasBlacklist == null) {
      geasBlacklist = ConfigUtil.parseLocationsSet(GeasBlacklist);
    }

    return geasBlacklist;
  }

  @Config.Comment(("List of entities that should be considered considered friendly for the purposes of Fire Storm (modid:entity)"))
  public static String[] GeasWhitelist = new String[]{
      "minecraft:enderman"
  };

  @Config.Ignore
  private static Set<ResourceLocation> geasWhitelist = null;

  public static Set<ResourceLocation> getGeasWhitelist() {
    if (geasWhitelist == null) {
      geasWhitelist = ConfigUtil.parseLocationsSet(GeasWhitelist);
    }

    return geasWhitelist;
  }
}


