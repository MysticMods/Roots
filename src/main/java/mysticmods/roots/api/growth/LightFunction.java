package mysticmods.roots.api.growth;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public abstract class LightFunction {
  public abstract boolean test(Level level, BlockPos blockPos, BlockState blockState);

  public static class LightLessThanFunction extends LightFunction {
    private final int lightValue;

    public LightLessThanFunction(int lightValue) {
      this.lightValue = lightValue;
    }

    public boolean test(Level level, BlockPos blockPos, BlockState blockState) {
      return level.getRawBrightness(blockPos, 0) < lightValue;
    }
  }

  public static class LightGreaterThanFunction extends LightFunction {
    private final int lightValue;

    public LightGreaterThanFunction(int lightValue) {
      this.lightValue = lightValue;
    }

    public boolean test(Level level, BlockPos blockPos, BlockState blockState) {
      return level.getRawBrightness(blockPos, 0) > lightValue;
    }
  }

  public static class AnyLightFunction extends LightFunction {
    public boolean test(Level level, BlockPos blockPos, BlockState blockState) {
      return true;
    }
  }
}
