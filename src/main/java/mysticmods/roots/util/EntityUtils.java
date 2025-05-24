package mysticmods.roots.util;

import mysticmods.roots.api.RootsTags;
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
}
