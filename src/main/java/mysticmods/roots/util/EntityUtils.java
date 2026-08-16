package mysticmods.roots.util;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.RootsTags;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;

import java.util.function.Predicate;

// TODO: Move sense danger checks in here
public class EntityUtils {
  public static Predicate<Entity> isProjectile = entity -> entity instanceof Projectile;

  public static Predicate<Entity> isProjectileOrHostile(LivingEntity pPlayer) {
    return entity -> isProjectile.test(entity) || isHostileTo(pPlayer).test(entity);
  }

  public static Predicate<Entity> isHostileTo(LivingEntity pPlayer) {
    return isHostileTo(pPlayer, true);
  }

  public static Predicate<Entity> allEntities(LivingEntity pPlayer, boolean skipSelf) {
    return inc -> {
      if (!(inc instanceof LivingEntity entity)) {
        return false;
      }

      if ((entity == pPlayer && skipSelf) || entity.isDeadOrDying() || entity.hurtTime > 0) {
        return false;
      }

      return true;
    };
  }


  public static Predicate<Entity> isHostileTo(LivingEntity pPlayer, boolean skipSelf) {
    return inc -> {
      if (!(inc instanceof LivingEntity entity)) {
        return false;
      }

      Level pLevel = pPlayer.level();

      if ((entity == pPlayer && skipSelf) || entity.isDeadOrDying() || entity.hurtTime > 0) {
        return false;
      }

      EntityType<?> type = entity.getType();

      if (type.is(RootsTags.Entities.FORCE_HOSTILE)) {
        return true;
      } else if (type.is(RootsTags.Entities.FORCE_FRIENDLY)) {
        return false;
      }

      if (entity instanceof Player otherPlayer && pLevel.getServer() != null && pLevel.getServer()
          .isPvpAllowed() && (pPlayer.getTeam() == null || pPlayer.getTeam().isAlliedTo(otherPlayer.getTeam()))) {
        return true;
      }

      if (entity instanceof NeutralMob neutral) {
        if (neutral.isAngryAt(pPlayer)) {
          return true;
        }
      }

      return entity instanceof Enemy;
    };
  }

  private static final Predicate<Entity> isDeflectableByDandelionWinds = (entity) -> {
    if (!(entity instanceof Projectile projectile)) {
      return false;
    }

    if (EntityUtils.state == DeflectState.WHITELIST) {
      return entity.getType().is(RootsTags.Entities.DANDELION_WINDS_DEFLECTABLES);
    } else if (EntityUtils.state == DeflectState.BLACKLIST) {
      return !entity.getType().is(RootsTags.Entities.DANDELION_WINDS_UNDEFLECTABLES);
    }

    // TODO: What if the state is unknown or invalid?
    return false;
  };

  public static boolean isDeflectableByDandelionWinds(Entity entity) {
    return isDeflectableByDandelionWinds.test(entity);
  }

  // TODO: This actually feels quite gross
  private static DeflectState state = DeflectState.UNKNOWN;

  private enum DeflectState {
    UNKNOWN,
    WHITELIST,
    BLACKLIST;
  }

  public static void retestDeflectState(RegistryAccess server) {
    server.registry(Registries.ENTITY_TYPE)
        .ifPresentOrElse(registry -> {
              var whitelist = registry.getTag(RootsTags.Entities.DANDELION_WINDS_DEFLECTABLES);
              if (whitelist.isEmpty()) {
                EntityUtils.state = DeflectState.BLACKLIST;
              } else {
                EntityUtils.state = DeflectState.WHITELIST;
              }
            }, () -> RootsAPI.LOG.error("Somehow, Palpatine has returned: Server has no entity type registry!")
        );
  }

  private static final Predicate<Entity> isHostile =
    inc -> {
    if (!(inc instanceof LivingEntity entity)) {
      return false;
    }

    Level pLevel = inc.level();

    if (entity.isDeadOrDying() || entity.hurtTime > 0) {
      return false;
    }

    EntityType<?> type = entity.getType();

    if (type.is(RootsTags.Entities.FORCE_HOSTILE)) {
      return true;
    } else if (type.is(RootsTags.Entities.FORCE_FRIENDLY)) {
      return false;
    }

    if (entity instanceof NeutralMob neutral) {
      if (neutral.isAngryAtAllPlayers(pLevel)) {
        return true;
      }
    }

    return entity instanceof Enemy;
  };

  public static boolean isHostile(Entity entity) {
    return isHostile.test(entity);
  }

  public static Predicate<Entity> isHostile () {
    return isHostile;
  }
}
