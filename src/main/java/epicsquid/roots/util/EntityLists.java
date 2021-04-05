package epicsquid.roots.util;

import epicsquid.roots.config.EntityConfig;
import epicsquid.roots.ritual.RitualBase;
import epicsquid.roots.ritual.RitualRegistry;
import epicsquid.roots.spell.*;
import net.minecraft.util.ResourceLocation;

import java.util.Collections;
import java.util.Set;

public class EntityLists {
  public static Set<ResourceLocation> getSpell(SpellBase spell, Type type) {
    if (spell == SpellDandelionWinds.instance) {
      switch (type) {
        case ALLOW:
          return EntityConfig.getDandelionWhitelist();
        case BLOCK:
          return EntityConfig.getDandelionBlacklist();
      }
    } else if (spell == SpellWildfire.instance) {
      switch (type) {
        case ALLOW:
          return EntityConfig.getWildfireWhitelist();
        case BLOCK:
          return EntityConfig.getWildfireBlacklist();
      }
    } else if (spell == SpellRoseThorns.instance) {
      switch (type) {
        case ALLOW:
          return EntityConfig.getRoseThornsWhitelist();
        case BLOCK:
          return EntityConfig.getRoseThornsBlacklist();
      }
    } else if (spell == SpellTimeStop.instance) {
      switch (type) {
        case ALLOW:
          return EntityConfig.getTimeStopWhitelist();
        case BLOCK:
          return EntityConfig.getTimeStopBlacklist();
      }
    } else if (spell == SpellExtension.instance) {
      switch (type) {
        case ALLOW:
          return EntityConfig.getExtensionWhitelist();
        case BLOCK:
          return EntityConfig.getExtensionBlacklist();
      }
    } else if (spell == SpellStormCloud.instance) {
      switch (type) {
        case ALLOW:
          return EntityConfig.getStormCloudWhitelist();
        case BLOCK:
          return EntityConfig.getStormCloudBlacklist();
      }
    } else if (spell == SpellAcidCloud.instance) {
      switch (type) {
        case ALLOW:
          return EntityConfig.getAcidCloudWhitelist();
        case BLOCK:
          return EntityConfig.getAcidCloudBlacklist();
      }
    } else if (spell == SpellDesaturate.instance) {
      switch (type) {
        case ALLOW:
          return EntityConfig.getDesaturateWhitelist();
        case BLOCK:
          return EntityConfig.getDesaturateBlacklist();
      }
    } else if (spell == SpellDisarm.instance) {
      switch (type) {
        case ALLOW:
          return EntityConfig.getDisarmWhitelist();
        case BLOCK:
          return EntityConfig.getDisarmBlacklist();
      }
    } else if (spell == SpellLifeDrain.instance) {
      switch (type) {
        case ALLOW:
          return EntityConfig.getLifeDrainWhitelist();
        case BLOCK:
          return EntityConfig.getLifeDrainBlacklist();
      }
    } else if (spell == SpellRadiance.instance) {
      switch (type) {
        case ALLOW:
          return EntityConfig.getRadianceWhitelist();
        case BLOCK:
          return EntityConfig.getRadianceBlacklist();
      }
    } else if (spell == SpellSanctuary.instance) {
      switch (type) {
        case ALLOW:
          return EntityConfig.getSanctuaryWhitelist();
        case BLOCK:
          return EntityConfig.getSanctuaryBlacklist();
      }
    } else if (spell == SpellGeas.instance) {
      switch (type) {
        case ALLOW:
          return EntityConfig.getGeasWhitelist();
        case BLOCK:
          return EntityConfig.getGeasBlacklist();
      }
    }

    return Collections.emptySet();
  }

  public static Set<ResourceLocation> getRitual(RitualBase ritual, Type type) {
    if (ritual == RitualRegistry.ritual_fire_storm) {
      switch (type) {
        case ALLOW:
          return EntityConfig.getFireStormWhitelist();
        case BLOCK:
          return EntityConfig.getFireStormBlacklist();
      }
    } else if (ritual == RitualRegistry.ritual_healing_aura) {
      switch (type) {
        case ALLOW:
          return EntityConfig.getHealingAuraWhitelist();
        case BLOCK:
          return EntityConfig.getHealingAuraBlacklist();
      }
    } else if (ritual == RitualRegistry.ritual_purity) {
      switch (type) {
        case ALLOW:
          return EntityConfig.getPurityWhitelist();
        case BLOCK:
          return EntityConfig.getPurityBlacklist();
      }
    } else if (ritual == RitualRegistry.ritual_windwall) {
      switch (type) {
        case ALLOW:
          return EntityConfig.getWindWallWhitelist();
        case BLOCK:
          return EntityConfig.getWindWallBlacklist();
      }
    }
    return Collections.emptySet();
  }

  public static Set<ResourceLocation> getGeneral(Type type) {
    switch (type) {
      case ALLOW:
        return EntityConfig.getEntityWhitelist();
      case BLOCK:
        return EntityConfig.getEntityBlacklist();
    }

    return Collections.emptySet();
  }


  public enum Type {
    ALLOW, BLOCK
  }
}
