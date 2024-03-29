package mysticmods.roots.api.condition;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.registry.DescribedRegistryEntry;
import mysticmods.roots.api.registry.Registries;
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
      BlockPos pPos = new BlockPos(pos.getX(), pos.getY() - 1, pos.getZ());

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

  public static LevelCondition.PillarCondition runePillar(int height) {
    return new LevelCondition.PillarCondition(RootsAPI.Tags.Blocks.RUNE_CAPSTONES, RootsAPI.Tags.Blocks.RUNE_PILLARS, height);
  }

  public static LevelCondition.PillarCondition logPillar(PillarType type, int height) {
    return new PillarCondition(type.getCapstoneTag(), type.getPillarTag(), height);
  }

  public enum PillarType {
    ACACIA(RootsAPI.Tags.Blocks.ACACIA_PILLARS, RootsAPI.Tags.Blocks.ACACIA_CAPSTONES),
    BIRCH(RootsAPI.Tags.Blocks.BIRCH_PILLARS, RootsAPI.Tags.Blocks.BIRCH_CAPSTONES),
    DARK_OAK(RootsAPI.Tags.Blocks.DARK_OAK_PILLARS, RootsAPI.Tags.Blocks.DARK_OAK_CAPSTONES),
    JUNGLE(RootsAPI.Tags.Blocks.JUNGLE_PILLARS, RootsAPI.Tags.Blocks.JUNGLE_CAPSTONES),
    OAK(RootsAPI.Tags.Blocks.OAK_PILLARS, RootsAPI.Tags.Blocks.OAK_CAPSTONES),
    SPRUCE(RootsAPI.Tags.Blocks.SPRUCE_PILLARS, RootsAPI.Tags.Blocks.SPRUCE_CAPSTONES),
    CRIMSON(RootsAPI.Tags.Blocks.CRIMSON_PILLARS, RootsAPI.Tags.Blocks.CRIMSON_CAPSTONES),
    WARPED(RootsAPI.Tags.Blocks.WARPED_PILLARS, RootsAPI.Tags.Blocks.WARPED_CAPSTONES),
    WILDWOOD(RootsAPI.Tags.Blocks.WILDWOOD_PILLARS, RootsAPI.Tags.Blocks.WILDWOOD_CAPSTONES);

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
