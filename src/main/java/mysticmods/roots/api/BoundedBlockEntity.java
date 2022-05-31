package mysticmods.roots.api;

import net.minecraft.world.level.levelgen.structure.BoundingBox;

import javax.annotation.Nullable;

public interface BoundedBlockEntity {
  default boolean isBounded() {
    return getRadiusX() != 0 || getRadiusY() != 0 || getRadiusZ() != 0;
  }

  default int getRadiusX() { return 0; }

  default int getRadiusY() { return 0; }

  default int getRadiusZ() { return 0; }

  @Nullable
  default BoundingBox getBoundingBox () { return null; }
}
