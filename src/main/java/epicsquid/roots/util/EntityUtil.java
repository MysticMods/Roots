package epicsquid.roots.util;

import net.minecraft.entity.*;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityWitherSkull;
import net.minecraft.util.ResourceLocation;

import java.util.HashSet;
import java.util.Set;

public class EntityUtil {
  private static Set<ResourceLocation> forcedFriendly = new HashSet<>();
  private static Set<ResourceLocation> forcedHostile = new HashSet<>();

  public static boolean isHostile (Entity entity) {
    if (entity instanceof EntityPlayer) return false;

    ResourceLocation rl = EntityList.getKey(entity);

    if (forcedFriendly.contains(rl)) {
      return false;
    }

    if (forcedHostile.contains(rl)) {
      return true;
    }

    if (entity instanceof IMob) {
      return true;
    }

    if (entity.isCreatureType(EnumCreatureType.MONSTER, false)) {
      return true;
    }

    if (entity instanceof IProjectile || entity instanceof EntityWitherSkull) {
      return true;
    }

    return false;
  }

  public static boolean isFriendly (Entity entity) {
    if (entity instanceof EntityPlayer) return false;

    ResourceLocation rl = EntityList.getKey(entity);

    if (forcedFriendly.contains(rl)) {
      return true;
    }

    if (forcedHostile.contains(rl)) {
      return false;
    }

    if (entity instanceof EntityAnimal) {
      return true;
    }

    if (entity.isCreatureType(EnumCreatureType.AMBIENT, false) || entity.isCreatureType(EnumCreatureType.WATER_CREATURE, false) || entity.isCreatureType(EnumCreatureType.CREATURE, false)) {
      return true;
    }

    return !isHostile(entity);
  }

  public static boolean isHostileTo (Entity entity, EntityPlayer player) {
    if (isHostile(entity)) return true;

    if (entity instanceof EntityLiving) {
      EntityLiving living = (EntityLiving) entity;
      if (living.getAttackTarget() == player) {
        return true;
      }
    }

    return false;
  }
}
