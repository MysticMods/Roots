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

    ResourceLocation rl = entity.getType().getRegistryName();

    if (forcedFriendly.contains(rl)) {
      return false;
    }

    if (forcedHostile.contains(rl)) {
      return true;
    }

    if (entity instanceof IMob) {
      return true;
    }

    if (entity.getType().getClassification() == EntityClassification.MONSTER) {
      return true;
    }

    if (entity instanceof IProjectile) {
      return true;
    }

    return false;
  }

  public static boolean isFriendly (Entity entity) {
    if (entity instanceof PlayerEntity) return false;

    ResourceLocation rl = entity.getType().getRegistryName();

    if (forcedFriendly.contains(rl)) {
      return true;
    }

    if (forcedHostile.contains(rl)) {
      return false;
    }

    if (entity instanceof AnimalEntity) {
      return true;
    }

    EntityClassification classification = entity.getType().getClassification();
    if (classification == EntityClassification.AMBIENT || classification == EntityClassification.WATER_CREATURE || classification == EntityClassification.CREATURE) {
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
