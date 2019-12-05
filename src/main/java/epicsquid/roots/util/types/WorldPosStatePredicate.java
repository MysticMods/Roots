package epicsquid.roots.util.types;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Objects;

@FunctionalInterface
public interface WorldPosStatePredicate {
  boolean test(World t, BlockPos u, BlockState v);

  default WorldPosStatePredicate and(WorldPosStatePredicate other) {
    Objects.requireNonNull(other);
    return (World t, BlockPos u, BlockState v) -> test(t, u, v) && other.test(t, u, v);
  }

  default WorldPosStatePredicate negate() {
    return (World t, BlockPos u, BlockState v) -> !test(t, u, v);
  }

  default WorldPosStatePredicate or(WorldPosStatePredicate other) {
    Objects.requireNonNull(other);
    return (World t, BlockPos u, BlockState v) -> test(t, u, v) || other.test(t, u, v);
  }
}
