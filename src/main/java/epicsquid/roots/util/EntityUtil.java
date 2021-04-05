package epicsquid.roots.util;

import epicsquid.roots.config.GeneralConfig;
import epicsquid.roots.ritual.RitualBase;
import epicsquid.roots.spell.SpellBase;
import net.minecraft.entity.*;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityWitherSkull;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;

public class EntityUtil {
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

  public static boolean isHostile(Entity entity, SpellBase spell) {
    return isHostile(entity, spell, null);
  }

  public static boolean isHostile(Entity entity, RitualBase ritual) {
    return isHostile(entity, null, ritual);
  }

  public static boolean isHostile(Entity entity) {
    return isHostile(entity, null, null);
  }

  public static boolean isHostile(Entity entity, @Nullable SpellBase spell, @Nullable RitualBase ritual) {

    if (entity instanceof EntityPlayer) return false;

    ResourceLocation rl = EntityList.getKey(entity);

    if (spell != null) {
      if (EntityLists.getSpell(spell, EntityLists.Type.BLOCK).contains(rl)) {
        return true;
      } else if (EntityLists.getSpell(spell, EntityLists.Type.ALLOW).contains(rl)) {
        return false;
      }
    }

    if (ritual != null) {
      if (EntityLists.getRitual(ritual, EntityLists.Type.BLOCK).contains(rl)) {
        return true;
      } else if (EntityLists.getRitual(ritual, EntityLists.Type.ALLOW).contains(rl)) {
        return false;
      }
    }

    if (EntityLists.getGeneral(EntityLists.Type.BLOCK).contains(rl)) {
      return true;
    } else if (EntityLists.getGeneral(EntityLists.Type.ALLOW).contains(rl)) {
      return false;
    }

    if (entity instanceof IMob) {
      return true;
    }

    if (entity.isCreatureType(EnumCreatureType.MONSTER, false)) {
      return true;
    }

    return entity instanceof IProjectile || entity instanceof EntityWitherSkull;

  }

  public static boolean isFriendly(Entity entity, SpellBase spell) {
    return isFriendly(entity, spell, null);
  }

  public static boolean isFriendly(Entity entity, RitualBase ritual) {
    return isFriendly(entity, null, ritual);
  }

  public static boolean isFriendly(Entity entity) {
    return isFriendly(entity, null, null);
  }

  public static boolean isFriendly(Entity entity, @Nullable SpellBase spell, @Nullable RitualBase ritual) {
    if (entity instanceof EntityPlayer) return false;

    ResourceLocation rl = EntityList.getKey(entity);

    if (spell != null) {
      if (EntityLists.getSpell(spell, EntityLists.Type.BLOCK).contains(rl)) {
        return false;
      } else if (EntityLists.getSpell(spell, EntityLists.Type.ALLOW).contains(rl)) {
        return true;
      }
    }

    if (ritual != null) {
      if (EntityLists.getRitual(ritual, EntityLists.Type.BLOCK).contains(rl)) {
        return false;
      } else if (EntityLists.getRitual(ritual, EntityLists.Type.ALLOW).contains(rl)) {
        return true;
      }
    }

    if (EntityLists.getGeneral(EntityLists.Type.BLOCK).contains(rl)) {
      return false;
    } else if (EntityLists.getGeneral(EntityLists.Type.ALLOW).contains(rl)) {
      return true;
    }

    if (entity instanceof EntityAnimal) {
      return true;
    }

    if (entity.isCreatureType(EnumCreatureType.AMBIENT, false) || entity.isCreatureType(EnumCreatureType.WATER_CREATURE, false) || entity.isCreatureType(EnumCreatureType.CREATURE, false)) {
      return true;
    }

    return !isHostile(entity);
  }

  public static boolean isHostileTo (Entity entity, EntityPlayer player, SpellBase spell) {
    return isHostileTo(entity, player, spell, null);
  }

  public static boolean isHostileTo (Entity entity, EntityPlayer player, RitualBase ritual) {
    return isHostileTo(entity, player, null, ritual);
  }

  public static boolean isHostileTo(Entity entity, EntityPlayer player) {
    return isHostileTo(entity, player, null, null);
  }

  public static boolean isHostileTo(Entity entity, EntityPlayer player, @Nullable SpellBase spell, @Nullable RitualBase ritual) {
    if (isHostile(entity, spell, ritual)) return true;

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

    return EntitySpawnPlacementRegistry.getPlacementForEntity(entity.getClass()) == EntityLiving.SpawnPlacementType.IN_WATER;

  }

  public static boolean isFriendlyTo (Entity entity, EntityPlayer player, SpellBase spell) {
    return isFriendlyTo(entity, player, spell, null);
  }

  public static boolean isFriendlyTo (Entity entity, EntityPlayer player, RitualBase ritual) {
    return isFriendlyTo(entity, player, null, ritual);
  }

  public static boolean isFriendlyTo(Entity entity, EntityPlayer player) {
    return isFriendlyTo(entity, player, null, null);
  }

  public static boolean isFriendlyTo(Entity entity, EntityPlayer player, @Nullable SpellBase spell, @Nullable RitualBase ritual) {
    if (!isFriendly(entity, spell, ritual)) {
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
      return !living.getLeashed();
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

    return !isBoss(entity);
  }

  public static boolean isBoss(EntityLivingBase entity) {
    if (!entity.isNonBoss()) {
      return true;
    }

    return entity.getMaxHealth() > GeneralConfig.BossEntityHealth;

  }
}
