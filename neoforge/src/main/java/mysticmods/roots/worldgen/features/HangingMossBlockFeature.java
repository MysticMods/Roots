package mysticmods.roots.worldgen.features;

import com.mojang.serialization.Codec;
import mysticmods.roots.api.RootsTags;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;

public class HangingMossBlockFeature extends Feature<SimpleBlockConfiguration> {
  public HangingMossBlockFeature(Codec<SimpleBlockConfiguration> pCodec) {
    super(pCodec);
  }

  @Override
  public boolean place(FeaturePlaceContext<SimpleBlockConfiguration> context) {
    WorldGenLevel level = context.level();
    BlockPos rootPos = context.origin();
    BlockState rootState = context.config().toPlace().getState(context.random(), rootPos);
    BlockState worldState = level.getBlockState(rootPos);

    if (worldState.isAir() || worldState.is(BlockTags.REPLACEABLE_PLANTS)) {
      BlockPos mPos = rootPos.above();
      BlockState mState = level.getBlockState(mPos);
      if (mState.is(RootsTags.Blocks.SUPPORTS_HANGING_MOSS) && rootState.canSurvive(level, rootPos)) {
        level.setBlock(rootPos, rootState, 3);
        return true;
      }
    }
    return false;
  }
}
