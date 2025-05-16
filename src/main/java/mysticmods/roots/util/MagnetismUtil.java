package mysticmods.roots.util;

import mysticmods.roots.blockentity.template.BaseBlockEntity;
import mysticmods.roots.config.ConfigManager;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MagnetismUtil {
  public enum MagnetismContext {
    SPELL,
    RITUAL
  }

  public static List<Entity> collect(Level pLevel, BlockPos startPosition, int radiusX, int radiusY, int radiusZ, MagnetismContext context) {
    AABB radius = new AABB(-radiusX, -radiusY, -radiusZ, radiusX, radiusY, radiusZ).move(startPosition);
    return pLevel.getEntities((Entity) null, radius, o -> !skipPull(o, context));
  }

  public static int pull(Level pLevel, BlockPos startPosition, int radiusX, int raduisY, int radiusZ) {
    return pull(pLevel, startPosition, radiusX, raduisY, radiusZ, new ArrayList<>()).size();
  }

  public static List<Vec3> pull(Level pLevel, BlockPos startPosition, int radiusX, int radiusY, int radiusZ, List<Vec3> positions) {
    if (pLevel.isClientSide()) {
      return positions;
    }
    List<Entity> entities = collect(pLevel, startPosition, radiusX, radiusY, radiusZ, MagnetismContext.SPELL);
    int pulled = 0;
    for (Entity entity : entities) {
      if (entity instanceof ItemEntity item) {
        item.setPickUpDelay(0);
      }
      positions.add(entity.position());
      entity.teleportTo(startPosition.getX(), startPosition.getY(), startPosition.getZ());
    }

    return positions;
  }

  public static List<Vec3> store(Level pLevel, BlockPos startPosition, BaseBlockEntity pyre, int radiusX, int radiusY, int radiusZ) {
    if (pLevel.isClientSide()) {
      return Collections.emptyList();
    }

    List<Entity> entities = collect(pLevel, startPosition, radiusX, radiusY, radiusZ, MagnetismContext.RITUAL);
    List<Vec3> positions = new ArrayList<>();
    for (Entity entity : entities) {
      if (entity instanceof ItemEntity entityItem) {
        ItemStack item = entityItem.getItem();
        ItemStack result = pyre.outputAdjacent(item);
        positions.add(entityItem.position());
        if (result.isEmpty()) {
          entityItem.discard();
        } else {
          entityItem.setItem(result);
        }
      }
    }
    return positions;
  }

  public static boolean skipPull(Entity entity, MagnetismContext context) {
    if (entity.getPersistentData().contains("PreventRemoteMovement")) {
      return true;
    }

    if (entity instanceof ItemEntity item) {
      return item.getItem().isEmpty();
    } else if (entity instanceof ExperienceOrb orb) {
      if (context == MagnetismContext.RITUAL) {
        return true;
      }

      return !ConfigManager.EXPERIENCE_ORBS.get();
    }

    return true;
  }
}
