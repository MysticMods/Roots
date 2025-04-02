package mysticmods.roots.util;

import mysticmods.roots.api.RootsTags;
import mysticmods.roots.config.ConfigManager;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.phys.AABB;

public class PacifistUtil {
  public static AABB AABB = new AABB(-0.75, -0.75, -0.75, 0.75, 0.75, 0.75);

  public static boolean test(ServerPlayer serverPlayer, Entity entity) {
    if (ConfigManager.PACIFIST_DISABLED.get()) {
      return false;
    }

    EntityType<?> type = entity.getType();
    if (!type.is(RootsTags.Entities.PACIFIST)) {
      return false;
    }

    // Chicken jockeys
    if (entity.getControllingPassenger() != null) {
      return false;
    }

    // TODO: Document this somewhere
    if (entity.getTags().contains("Roots_NoPacifist")) {
      return false;
    }

    if (entity.level()
        .getEntities(entity, AABB.move(entity.getX(), entity.getY(), entity.getZ()), e -> EntityUtils.isHostileTo(serverPlayer)
            .test(e)).isEmpty()) {
      return true;
    }

    return false;
  }
}
