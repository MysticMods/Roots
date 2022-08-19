package mysticmods.roots.api.ritual;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.blockentity.BoundedBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class RitualConditionTypes {
  @FunctionalInterface
  public interface LevelConditionType {
    Set<BlockPos> test (BlockPos pos, Level level, @Nullable Player player, Ritual ritual, BoundedBlockEntity pyre);
  }

  @FunctionalInterface
  public interface PlayerConditionType {
    boolean test (Level level, @Nullable Player player, Ritual ritual, BoundedBlockEntity pyre);
  }

  public static class PillarCondition implements LevelConditionType {
    private final TagKey<Block> capstone;
    private final TagKey<Block> pillar;
    private final int heightExcluding;

    public PillarCondition(TagKey<Block> capstone, TagKey<Block> pillar, int height) {
      this.capstone = capstone;
      this.pillar = pillar;
      // A 4-high pillar has 3 pillar blocks and 1 capstone
      this.heightExcluding = height - 1;
    }

    @Override
    public Set<BlockPos> test(BlockPos pos, Level level, @Nullable Player player, Ritual ritual, BoundedBlockEntity pyre) {
      BlockState initial = level.getBlockState(pos);
      // If the initial position isn't the capstone, we don't care
      if (!initial.is(capstone)) {
        return Collections.emptySet();
      }

      // Keep a note of which blockpositions are part of this pillar
      Set<BlockPos> result = new HashSet<>();
      BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
      mutableBlockPos.set(pos.getX(), pos.getY() - 1, pos.getZ());

      // Move downward for each of the height (excluding the capstone)
      for (int i = 0; i < heightExcluding; i++) {
        if (!level.getBlockState(mutableBlockPos).is(pillar)) {
          // If it isn't a pillar type, just return empty as this isn't valid OR it's a shorter pillar
          return Collections.emptySet();
        }
        result.add(mutableBlockPos.immutable());
        mutableBlockPos.move(Direction.DOWN);
      }

      // Check the final underneath block, if it's a pillar or a capstone it's too tall OR it isn't valid
      initial = level.getBlockState(mutableBlockPos);
      if (initial.is(capstone) || initial.is(pillar)) {
        return Collections.emptySet();
      }

      return result;
    }
  }

  public static PillarCondition runePillar (int height) {
    return new PillarCondition(RootsAPI.Tags.Blocks.RUNE_CAPSTONES, RootsAPI.Tags.Blocks.RUNE_PILLARS, height);
  }

  public static PillarCondition logPillar (int height) {
    return new PillarCondition(RootsAPI.Tags.Blocks.LOG_CAPSTONES, RootsAPI.Tags.Blocks.LOG_PILLARS, height);
  }
}
