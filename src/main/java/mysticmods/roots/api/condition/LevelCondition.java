package mysticmods.roots.api.condition;

import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.StateProperties;
import mysticmods.roots.api.faction.GroveType;
import mysticmods.roots.api.registry.DescribedRegistryEntry;
import mysticmods.roots.api.registry.Registries;
import mysticmods.roots.test.block.BlockPropertyMatchTest;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
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
    return Registries.LEVEL_CONDITION_REGISTRY.get().getKey(this);
  }

  public Set<BlockPos> test(Level level, @Nullable Player player, BoundingBox bounds, BlockPos pos, Set<BlockPos> exclusions) {
    BoundingBox newBounds = bounds.moved(pos.getX(), pos.getY(), pos.getZ());
    for (int x = newBounds.minX(); x < newBounds.maxX(); x++) {
      for (int y = newBounds.minY(); y < newBounds.maxY(); y++) {
        for (int z = newBounds.minZ(); z < newBounds.maxZ(); z++) {
          pos = new BlockPos(x, y, z);
          if (exclusions.contains(pos)) {
            continue;
          }
          Set<BlockPos> result = condition.test(pos, level, player);
          if (!result.isEmpty()) {
            return result;
          }
        }
      }
    }

    return Collections.emptySet();
  }

  @FunctionalInterface
  public interface Type {
    Set<BlockPos> test(BlockPos pos, Level level, @javax.annotation.Nullable Player player);
  }

  public static class BlockStatePropertyCondition implements Type {
    private final BlockPropertyMatchTest test;

    public BlockStatePropertyCondition(BlockPropertyMatchTest test) {
      this.test = test;
    }

    @Override
    public Set<BlockPos> test(BlockPos pos, Level level, @javax.annotation.Nullable Player player) {
      if (test.test(level.getBlockState(pos), level.getRandom())) {
        return Collections.singleton(pos.immutable());
      }

      return Collections.emptySet();
    }
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
    public Set<BlockPos> test(BlockPos pos, Level level, @javax.annotation.Nullable Player player) {
      BlockState initial = level.getBlockState(pos);
      // If the initial position isn't the capstone, we don't care
      if (!initial.is(capstone)) {
        return Collections.emptySet();
      }

      // Keep a note of which blockpositions are part of this pillar
      Set<BlockPos> result = new HashSet<>();
      result.add(pos.immutable());
      BlockPos pPos = pos.below();

      // Move downward for each of the height (excluding the capstone)
      for (int i = 0; i < heightExcluding; i++) {
        if (!level.getBlockState(pPos).is(pillar)) {
          // If it isn't a pillar type, just return empty as this isn't valid OR it's a shorter pillar
          return Collections.emptySet();
        }
        result.add(pPos);
        pPos = pPos.below();
      }

      // Check the final underneath block, if it's a pillar or a capstone it's too tall OR it isn't valid
      initial = level.getBlockState(pPos);
      if (initial.is(capstone) || initial.is(pillar)) {
        return Collections.emptySet();
      }

      return result;
    }
  }

  public static class GroveStoneCondition implements Type {
    private final TagKey<Block> groveType;
    private final boolean requireValid;

    public GroveStoneCondition(TagKey<Block> groveType, boolean requireValid) {
      this.groveType = groveType;
      this.requireValid = requireValid;
    }

    @Nullable
    protected StateProperties.Part getPart (BlockState state) {
      if (!state.hasProperty(StateProperties.GroveStone.PART)) {
        return null;
      } else {
        return state.getValue(StateProperties.GroveStone.PART);
      }
    }

    protected boolean getValid (BlockState state) {
      if (!state.hasProperty(StateProperties.GroveStone.VALID)) {
        return false;
      } else {
        return state.getValue(StateProperties.GroveStone.VALID);
      }
    }

    @Override
    public Set<BlockPos> test(BlockPos pos, Level level, @javax.annotation.Nullable Player player) {
      BlockState initial = level.getBlockState(pos);
      // If the initial position isn't the capstone, we don't care
      if (!initial.is(groveType) && getPart(initial) != StateProperties.Part.TOP) {
        return Collections.emptySet();
      }

      int validCount = 0;
      if (getValid(initial)) {
        validCount++;
      }

      // Keep a note of which blockpositions are part of this pillar
      Set<BlockPos> result = new HashSet<>();
      result.add(pos.immutable());
      BlockPos pPos = pos.below();

      initial = level.getBlockState(pPos);
      if (!initial.is(groveType) && getPart(initial) != StateProperties.Part.MIDDLE) {
        return Collections.emptySet();
      }

      result.add(pPos.immutable());

      if (getValid(initial)) {
        validCount++;
      }

      pPos = pPos.below();
      initial = level.getBlockState(pPos);
      if (!initial.is(groveType) && getPart(initial) != StateProperties.Part.BOTTOM) {
        return Collections.emptySet();
      }

      result.add(pPos.immutable());

      if (getValid(initial)) {
        validCount++;
      }

      if (requireValid && validCount != 3) {
        return Collections.emptySet();
      }

      return result;
    }
  }

  public static LevelCondition.PillarCondition runePillar(int height) {
    return new LevelCondition.PillarCondition(RootsTags.Blocks.RUNE_CAPSTONES, RootsTags.Blocks.RUNE_PILLARS, height);
  }

  public static LevelCondition.PillarCondition logPillar(PillarType type, int height) {
    return new PillarCondition(type.getCapstoneTag(), type.getPillarTag(), height);
  }

  public static LevelCondition.GroveStoneCondition groveStone (GroveType grove, boolean requireValid) {
    return new GroveStoneCondition(grove.getTag(), requireValid);
  }

  public static LevelCondition.GroveStoneCondition anyGroveStone (boolean requireValid) {
    return new GroveStoneCondition(RootsTags.Blocks.GROVE_STONES, requireValid);
  }

  public enum PillarType {
    ACACIA(RootsTags.Blocks.ACACIA_PILLARS, RootsTags.Blocks.ACACIA_CAPSTONES),
    BIRCH(RootsTags.Blocks.BIRCH_PILLARS, RootsTags.Blocks.BIRCH_CAPSTONES),
    DARK_OAK(RootsTags.Blocks.DARK_OAK_PILLARS, RootsTags.Blocks.DARK_OAK_CAPSTONES),
    JUNGLE(RootsTags.Blocks.JUNGLE_PILLARS, RootsTags.Blocks.JUNGLE_CAPSTONES),
    OAK(RootsTags.Blocks.OAK_PILLARS, RootsTags.Blocks.OAK_CAPSTONES),
    SPRUCE(RootsTags.Blocks.SPRUCE_PILLARS, RootsTags.Blocks.SPRUCE_CAPSTONES),
    CRIMSON(RootsTags.Blocks.CRIMSON_PILLARS, RootsTags.Blocks.CRIMSON_CAPSTONES),
    WARPED(RootsTags.Blocks.WARPED_PILLARS, RootsTags.Blocks.WARPED_CAPSTONES),
    WILDWOOD(RootsTags.Blocks.WILDWOOD_PILLARS, RootsTags.Blocks.WILDWOOD_CAPSTONES),
    MANGROVE(RootsTags.Blocks.MANGROVE_PILLARS, RootsTags.Blocks.MANGROVE_CAPSTONES);

    private final TagKey<Block> pillarTag;
    private final TagKey<Block> capstoneTag;

    PillarType(TagKey<Block> pillarTag, TagKey<Block> capstoneTag) {
      this.pillarTag = pillarTag;
      this.capstoneTag = capstoneTag;
    }

    public TagKey<Block> getPillarTag() {
      return pillarTag;
    }

    public TagKey<Block> getCapstoneTag() {
      return capstoneTag;
    }
  }
}
