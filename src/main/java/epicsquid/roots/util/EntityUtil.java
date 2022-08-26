package epicsquid.roots.util;

import epicsquid.roots.modifiers.IModifier;
import net.minecraft.entity.*;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityWitherSkull;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;

public class EntityUtil {
  private static Set<ResourceLocation> forcedFriendly = new HashSet<>();
  private static Set<ResourceLocation> forcedHostile = new HashSet<>();

  public static boolean isFamiliar (EntityPlayer player, Entity entity) {
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

  public static boolean isFriendlyTo (Entity entity, EntityPlayer player) {
    if (!isFriendly(entity)) {
      return false;
    }

    if (entity instanceof EntityLiving) {
      return ((EntityLiving) entity).getAttackTarget() == player;
    }

    return true;
  }

  @Nullable
  public static NBTTagCompound getModifierData (Entity entity, IModifier modifier) {
    NBTTagCompound data = entity.getEntityData();

    if (data.hasKey(modifier.getIdentifier(), Constants.NBT.TAG_COMPOUND)) {
      return data.getCompoundTag(modifier.getIdentifier());
    }

    return null;
  }
}
