package epicsquid.roots.util;

import epicsquid.roots.config.GeneralConfig;
import epicsquid.roots.ritual.RitualBase;
import epicsquid.roots.spell.SpellBase;
import net.minecraft.entity.*;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.horse.AbstractHorseEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.WaterMobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.WitherSkullEntity;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class EntityUtil {
  public static boolean isFamiliar(Entity entity) {
    if (entity instanceof PlayerEntity) return false;

    if (!isFriendly(entity)) {
      return false;
    }

    if (entity instanceof AbstractHorseEntity) {
      AbstractHorseEntity owned = (AbstractHorseEntity) entity;
      return owned.getOwnerUniqueId() != null;
    } else if (entity instanceof IEntityOwnable) {
      IEntityOwnable owned = (IEntityOwnable) entity;
      return owned.getOwnerId() != null;
    }

    return false;
  }

  public static boolean isFamiliar(PlayerEntity player, Entity entity) {
    if (entity instanceof PlayerEntity) return false;

    if (!isFriendly(entity)) {
      return false;
    }

    if (entity instanceof AbstractHorseEntity) {
      AbstractHorseEntity owned = (AbstractHorseEntity) entity;
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

    if (entity instanceof PlayerEntity) return false;

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

    if (entity.isCreatureType(EntityClassification.MONSTER, false)) {
      return true;
    }

    return entity instanceof IProjectile || entity instanceof WitherSkullEntity;

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
    if (entity instanceof PlayerEntity) return false;

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

    if (entity instanceof AnimalEntity) {
      return true;
    }

    if (entity.isCreatureType(EntityClassification.AMBIENT, false) || entity.isCreatureType(EntityClassification.WATER_CREATURE, false) || entity.isCreatureType(EntityClassification.CREATURE, false)) {
      return true;
    }

    return !isHostile(entity);
  }

  public static boolean isHostileTo (Entity entity, PlayerEntity player, SpellBase spell) {
    return isHostileTo(entity, player, spell, null);
  }

  public static boolean isHostileTo (Entity entity, PlayerEntity player, RitualBase ritual) {
    return isHostileTo(entity, player, null, ritual);
  }

  public static boolean isHostileTo(Entity entity, PlayerEntity player) {
    return isHostileTo(entity, player, null, null);
  }

  public static boolean isHostileTo(Entity entity, PlayerEntity player, @Nullable SpellBase spell, @Nullable RitualBase ritual) {
    if (isHostile(entity, spell, ritual)) return true;

    if (entity instanceof MobEntity) {
      MobEntity living = (MobEntity) entity;
      return living.getAttackTarget() == player;
    }

    return false;
  }

  public static boolean isAquatic(Entity entity) {
    if (entity instanceof WaterMobEntity) {
      return true;
    }

    return EntitySpawnPlacementRegistry.getPlacementForEntity(entity.getClass()) == MobEntity.SpawnPlacementType.IN_WATER;

  }

  public static boolean isFriendlyTo (Entity entity, PlayerEntity player, SpellBase spell) {
    return isFriendlyTo(entity, player, spell, null);
  }

  public static boolean isFriendlyTo (Entity entity, PlayerEntity player, RitualBase ritual) {
    return isFriendlyTo(entity, player, null, ritual);
  }

  public static boolean isFriendlyTo(Entity entity, PlayerEntity player) {
    return isFriendlyTo(entity, player, null, null);
  }

  public static boolean isFriendlyTo(Entity entity, PlayerEntity player, @Nullable SpellBase spell, @Nullable RitualBase ritual) {
    if (!isFriendly(entity, spell, ritual)) {
      return false;
    }

    if (entity instanceof MobEntity) {
      return ((MobEntity) entity).getAttackTarget() != player;
    }

    return true;
  }

  public static boolean canSummonPassive(LivingEntity entity) {
    if (!isFriendly(entity)) {
      return false;
    }

    if (entity.hasCustomName()) {
      return false;
    }

    if (entity instanceof MobEntity) {
      MobEntity living = (MobEntity) entity;
      return !living.getLeashed();
    }

    return true;
  }

  public static boolean canSummonHostile(LivingEntity entity) {
    if (!isHostile(entity)) {
      return false;
    }

    if (entity.hasCustomName()) {
      return false;
    }

    return !isBoss(entity);
  }

  public static boolean isBoss(LivingEntity entity) {
    if (!entity.isNonBoss()) {
      return true;
    }

    return entity.getMaxHealth() > GeneralConfig.BossEntityHealth;

  }
}
