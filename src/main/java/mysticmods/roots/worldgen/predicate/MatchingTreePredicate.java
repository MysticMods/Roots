package mysticmods.roots.worldgen.predicate;

import com.mojang.serialization.MapCodec;
import mysticmods.roots.init.ModFeatures;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicateType;

public class MatchingTreePredicate implements BlockPredicate {
  private static final MatchingTreePredicate INSTANCE = new MatchingTreePredicate();

  public static final MapCodec<MatchingTreePredicate> CODEC = MapCodec.unit(INSTANCE);

  public static MatchingTreePredicate create() {
    return INSTANCE;
  }

  @Override
  public BlockPredicateType<?> type() {
    return ModFeatures.MATCHING_TREE_PREDICATE.get();
  }

  @Override
  public boolean test(WorldGenLevel worldGenLevel, BlockPos blockPos) {
    BlockState stateAt = worldGenLevel.getBlockState(blockPos);
    if (!stateAt.is(BlockTags.LOGS_THAT_BURN)) {
      return false;
    }

    BlockPos.MutableBlockPos mutableBlockPos = blockPos.mutable();
    for (int i = 0; i < 16; i++) {
      mutableBlockPos.move(0, 1, 0);
      stateAt = worldGenLevel.getBlockState(mutableBlockPos);
      if (!stateAt.is(BlockTags.LOGS_THAT_BURN)) {
        return stateAt.is(BlockTags.LEAVES);
      }
    }

    return false;
  }
}
