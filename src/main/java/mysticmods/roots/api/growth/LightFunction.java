package mysticmods.roots.api.growth;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

@FunctionalInterface
public interface LightFunction {
  boolean test(Level level, BlockPos blockPos, BlockState blockState);

  class LightLessThanFunction implements LightFunction {
    private final int lightValue;

    public LightLessThanFunction(int lightValue) {
      this.lightValue = lightValue;
    }

    public boolean test(Level level, BlockPos blockPos, BlockState blockState) {
      return level.getRawBrightness(blockPos, 0) < lightValue;
    }
  }

  class LightGreaterThanFunction implements LightFunction {
    private final int lightValue;

    public LightGreaterThanFunction(int lightValue) {
      this.lightValue = lightValue;
    }

    public boolean test(Level level, BlockPos blockPos, BlockState blockState) {
      return level.getRawBrightness(blockPos, 0) > lightValue;
    }
  }

  class AnyLightFunction implements LightFunction {
    public boolean test(Level level, BlockPos blockPos, BlockState blockState) {
      return true;
    }
  }
}
