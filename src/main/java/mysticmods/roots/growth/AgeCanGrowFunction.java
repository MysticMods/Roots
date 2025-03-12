package mysticmods.roots.growth;

import mysticmods.roots.api.growth.CanGrowFunction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jetbrains.annotations.Nullable;

public record AgeCanGrowFunction () implements CanGrowFunction {

  @Override
  public boolean test(Level level, BlockPos blockPos, BlockState blockState, @Nullable IntegerProperty ageProperty, int maxmimumAge) {
    if (ageProperty == null) {
      return false;
    }
    if (!blockState.hasProperty(ageProperty)) {
      return false;
    }
    int age = blockState.getValue(ageProperty);
    return age < maxmimumAge;
  }
}
