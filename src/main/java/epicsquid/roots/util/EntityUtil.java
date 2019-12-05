package epicsquid.roots.util;

import net.minecraft.entity.*;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;

import java.util.HashSet;
import java.util.Set;

public class EntityUtil {
  private static Set<ResourceLocation> forcedFriendly = new HashSet<>();
  private static Set<ResourceLocation> forcedHostile = new HashSet<>();

  public static boolean isHostile (Entity entity) {
    if (entity instanceof PlayerEntity) return false;

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

    if (entity.isCreatureType(EntityClassification.MONSTER, false)) {
      return true;
    }

    if (entity instanceof IProjectile) {
      return true;
    }

    return false;
  }

  public static boolean isFriendly (Entity entity) {
    if (entity instanceof PlayerEntity) return false;

    ResourceLocation rl = EntityList.getKey(entity);

    if (forcedFriendly.contains(rl)) {
      return true;
    }

    if (forcedHostile.contains(rl)) {
      return false;
    }

    if (entity instanceof AnimalEntity) {
      return true;
    }

    if (entity.isCreatureType(EntityClassification.AMBIENT, false) || entity.isCreatureType(EntityClassification.WATER_CREATURE, false) || entity.isCreatureType(EntityClassification.CREATURE, false)) {
      return true;
    }

    return !isHostile(entity);
  }

  public static boolean isHostile (Entity entity, PlayerEntity player) {
    if (isHostile(entity)) return true;

    if (entity instanceof MobEntity) {
      MobEntity living = (MobEntity) entity;
      if (living.getAttackTarget() == player) {
        return true;
      }
    }

    return false;
  }
}
