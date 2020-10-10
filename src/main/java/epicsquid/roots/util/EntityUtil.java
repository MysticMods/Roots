package epicsquid.roots.util;

import epicsquid.roots.config.GeneralConfig;
import net.minecraft.entity.*;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityWitherSkull;
import net.minecraft.util.ResourceLocation;

import java.util.HashSet;
import java.util.Set;

public class EntityUtil {
  private static Set<ResourceLocation> forcedFriendly = new HashSet<>();
  private static Set<ResourceLocation> forcedHostile = new HashSet<>();

  public static boolean isFamiliar(Entity entity) {
    if (entity instanceof EntityPlayer) return false;

    if (!isFriendly(entity)) {
      return false;
    }

    if (entity instanceof AbstractHorse) {
      AbstractHorse owned = (AbstractHorse) entity;
      return owned.getOwnerUniqueId() != null;
    } else if (entity instanceof IEntityOwnable) {
      IEntityOwnable owned = (IEntityOwnable) entity;
      return owned.getOwnerId() != null;
    }

    return false;
  }

  public static boolean isFamiliar(EntityPlayer player, Entity entity) {
    if (entity instanceof EntityPlayer) return false;

    if (!isFriendly(entity)) {
      return false;
    }

    if (entity instanceof AbstractHorse) {
      AbstractHorse owned = (AbstractHorse) entity;
      return owned.getOwnerUniqueId() != null && owned.getOwnerUniqueId().equals(player.getUniqueID());
    } else if (entity instanceof IEntityOwnable) {
      IEntityOwnable owned = (IEntityOwnable) entity;
      return owned.getOwnerId() != null && owned.getOwnerId().equals(player.getUniqueID());
    }

    return false;
  }

  public static boolean isHostile(Entity entity) {
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

    return entity instanceof IProjectile || entity instanceof EntityWitherSkull;

  }

  public static boolean isFriendly(Entity entity) {
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

  public static boolean isHostileTo(Entity entity, EntityPlayer player) {
    if (isHostile(entity)) return true;

    if (entity instanceof EntityLiving) {
      EntityLiving living = (EntityLiving) entity;
      return living.getAttackTarget() == player;
    }

    return false;
  }

  public static boolean isAquatic(Entity entity) {
    if (entity instanceof EntityWaterMob) {
      return true;
    }

    if (EntitySpawnPlacementRegistry.getPlacementForEntity(entity.getClass()) == EntityLiving.SpawnPlacementType.IN_WATER) {
      return true;
    }

    return false;
  }

  public static boolean isFriendlyTo(Entity entity, EntityPlayer player) {
    if (!isFriendly(entity)) {
      return false;
    }

    if (entity instanceof EntityLiving) {
      return ((EntityLiving) entity).getAttackTarget() != player;
    }

    return true;
  }

  public static boolean canSummonPassive(EntityLivingBase entity) {
    if (!isFriendly(entity)) {
      return false;
    }

    if (entity.hasCustomName()) {
      return false;
    }

    if (entity instanceof EntityLiving) {
      EntityLiving living = (EntityLiving) entity;
      if (living.getLeashed()) {
        return false;
      }
    }

    return true;
  }

  public static boolean canSummonHostile(EntityLivingBase entity) {
    if (!isHostile(entity)) {
      return false;
    }

    if (entity.hasCustomName()) {
      return false;
    }

    if (isBoss(entity)) {
      return false;
    }

    return true;
  }

  public static boolean isBoss(EntityLivingBase entity) {
    if (!entity.isNonBoss()) {
      return true;
    }

    if (entity.getMaxHealth() > GeneralConfig.BossEntityHealth) {
      return true;
    }

    return false;
  }
}
