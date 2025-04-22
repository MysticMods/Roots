package mysticmods.roots.api.blockentity;

import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.phys.AABB;

import javax.annotation.Nullable;

public interface Bounded {
  default boolean isBounded() {
    return getRadiusX() != 0 || getRadiusY() != 0 || getRadiusZ() != 0;
  }

  default int getRadiusX() {
    return 0;
  }

  default int getRadiusY() {
    return 0;
  }

  default int getRadiusZ() {
    return 0;
  }

  @Nullable
  default BoundingBox getBoundingBox() {
    return null;
  }

  @Nullable
  default AABB getAABB () {
    return null;
  }
}
