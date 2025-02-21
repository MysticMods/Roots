package mysticmods.roots.worldgen.features;

import com.mojang.serialization.Codec;
import mysticmods.roots.api.RootsTags;
import mysticmods.roots.block.HangingGroveMossBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class HangingMossBlockFeature extends Feature<SimpleBlockConfiguration> {
  public HangingMossBlockFeature(Codec<SimpleBlockConfiguration> pCodec) {
    super(pCodec);
  }

  private List<Direction> directions;

  @Override
  public boolean place(FeaturePlaceContext<SimpleBlockConfiguration> context) {
    if (directions == null) {
      directions = Arrays.asList(Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST);
    }

    WorldGenLevel level = context.level();
    BlockPos rootPos = context.origin();
    BlockState rootState = context.config().toPlace().getState(context.random(), rootPos);
    BlockState worldState = level.getBlockState(rootPos);

    // TODO: Compare replaceable, replaceable_by_trees
    if (worldState.isAir() || worldState.is(BlockTags.REPLACEABLE_BY_TREES)) {
      BlockPos.MutableBlockPos target = rootPos.mutable();
      for (Direction direction : directions) {
        target.move(direction.getOpposite());

        rootState = rootState.setValue(HangingGroveMossBlock.FACING, direction);
        worldState = level.getBlockState(target);

        if (!worldState.is(RootsTags.Blocks.SUPPORTS_HANGING_MOSS)) {
          continue;
        }

        if (rootState.canSurvive(level, rootPos)) {
          level.setBlock(rootPos, rootState, 3);
          return true;
        }
      }
    }
    return false;
  }
}
