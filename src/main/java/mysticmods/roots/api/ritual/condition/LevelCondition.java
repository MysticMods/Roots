package mysticmods.roots.api.ritual.condition;

import mysticmods.roots.api.DescribedRegistryEntry;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.registry.Registries;
import mysticmods.roots.api.ritual.Ritual;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class LevelCondition extends DescribedRegistryEntry<LevelCondition> {
  private final Type condition;

  public LevelCondition(Type condition) {
    this.condition = condition;
  }

  @Override
  protected String getDescriptor() {
    return "level_condition";
  }

  @Override
  public ResourceLocation getKey() {
    return Registries.RITUAL_LEVEL_CONDITION.get().getKey(this);
  }

  // TODO: Mutation of completedPositions but also return value
  public Set<BlockPos> test(Level level, @Nullable Player player, Ritual ritual, BlockEntity pyre, BoundingBox bounds) {
    Set<BlockPos> newCompletedPositions = new HashSet<>();
    bounds = bounds.move(pyre.getBlockPos());
    BlockPos.betweenClosedStream(bounds).forEach(mPos -> {
      BlockPos pos = mPos.immutable();
      if (newCompletedPositions.contains(pos)) {
        return;
      }

      newCompletedPositions.addAll(condition.test(pos, level, player, ritual, pyre));
      newCompletedPositions.add(pos);
    });
    return newCompletedPositions;
  }

  @FunctionalInterface
  public interface Type {
    Set<BlockPos> test(BlockPos pos, Level level, @javax.annotation.Nullable Player player, Ritual ritual, BlockEntity pyre);
  }

  public static class PillarCondition implements Type {
    private final TagKey<Block> capstone;
    private final TagKey<Block> pillar;
    private final int heightExcluding;

    public PillarCondition(TagKey<Block> capstone, TagKey<Block> pillar, int height) {
      this.capstone = capstone;
      this.pillar = pillar;
      this.heightExcluding = height;
    }

    @Override
    public Set<BlockPos> test(BlockPos pos, Level level, @javax.annotation.Nullable Player player, Ritual ritual, BlockEntity pyre) {
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

  public static LevelCondition.PillarCondition runePillar(int height) {
    return new LevelCondition.PillarCondition(RootsAPI.Tags.Blocks.RUNE_CAPSTONES, RootsAPI.Tags.Blocks.RUNE_PILLARS, height);
  }

  // TODO: better implementation of this
  public static LevelCondition.PillarCondition logPillar(int height) {
    return new LevelCondition.PillarCondition(RootsAPI.Tags.Blocks.LOG_CAPSTONES, RootsAPI.Tags.Blocks.LOG_PILLARS, height);
  }
}
