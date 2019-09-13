package epicsquid.roots.util;

import net.minecraft.entity.*;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

import java.util.HashSet;
import java.util.Set;

public class EntityUtil {
  private static Set<ResourceLocation> forcedFriendly = new HashSet<>();
  private static Set<ResourceLocation> forcedHostile = new HashSet<>();

  public static boolean isHostile (Entity entity) {
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

    if (entity instanceof IProjectile) {
      return true;
    }

    return false;
  }

  public static boolean isHostile (Entity entity, EntityPlayer player) {
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
