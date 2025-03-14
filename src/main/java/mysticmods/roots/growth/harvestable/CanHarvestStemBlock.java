package mysticmods.roots.growth.harvestable;

import mysticmods.roots.api.datamap.DataMaps;
import mysticmods.roots.api.growth.CanHarvestFunction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AttachedStemBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public record CanHarvestStemBlock() implements CanHarvestFunction {
  private static final List<Direction> HORIZONTALS = new ArrayList<>();

  static {
    HORIZONTALS.add(Direction.NORTH);
    HORIZONTALS.add(Direction.EAST);
    HORIZONTALS.add(Direction.SOUTH);
    HORIZONTALS.add(Direction.WEST);
  }


  @Override
  public boolean test(Level level, BlockPos blockPos, BlockState blockState, @Nullable IntegerProperty ageProperty, int maximumAge) {
    Block stemBlock = blockState.getBlockHolder().getData(DataMaps.STEM_BLOCKS);
    if (stemBlock == null) {
      return false;
    }

    for (Direction dir : HORIZONTALS) {
      BlockState possible = level.getBlockState(blockPos.relative(dir));
      if (possible.is(stemBlock)) {
        if (possible.hasProperty(AttachedStemBlock.FACING) && possible.getValue(AttachedStemBlock.FACING) == dir.getOpposite()) {
          return true;
        }
      }
    }

    return false;
  }
}
