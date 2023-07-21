package mysticmods.roots.ritual;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.property.RitualProperty;
import mysticmods.roots.api.ritual.Ritual;
import mysticmods.roots.block.crop.ThreeStageCropBlock;
import mysticmods.roots.blockentity.PyreBlockEntity;
import mysticmods.roots.init.ModBlocks;
import mysticmods.roots.init.ModRituals;
import mysticmods.roots.worldgen.trees.WildwoodTreeGrower;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.block.state.BlockState;

public class WildrootGrowthRitual extends Ritual {
  private final AbstractTreeGrower treeGrower = new WildwoodTreeGrower();

  @Override
  protected void functionalTick(PyreBlockEntity blockEntity, int duration) {
    if (duration == 0 || interval == 0) {
      RootsAPI.LOG.error("ugh");
    }
    if (duration % interval == 0) {
      ServerLevel level = (ServerLevel) blockEntity.getLevel();
      BlockPos.betweenClosedStream(getAABB().move(blockEntity.getBlockPos())).filter(o -> {
        BlockState state = level.getBlockState(o);
        if (state.is(RootsTags.Blocks.WILDROOT_CROP)) {
          if (state.hasProperty(ThreeStageCropBlock.AGE) && state.getValue(ThreeStageCropBlock.AGE) == ModBlocks.WILDROOT_CROP.get().getMaxAge()) {
            return true;
          }
        }
        return false;
      }).findFirst().ifPresent(pos -> {
        // It shouldn't be null
        // Remove the crop
        BlockPos treePos = pos.immutable();
        BlockState currentState = level.getBlockState(treePos);
        level.setBlock(treePos, Blocks.AIR.defaultBlockState(), 4);
        BlockPos below = treePos.below();
        // If it wasn't on a full height block, replace it with dirt
        BlockState belowState = level.getBlockState(below);
        if (!belowState.isFaceSturdy(level, below, Direction.UP)) {
          level.setBlock(below, Blocks.DIRT.defaultBlockState(), 4);
        }
        if (!treeGrower.growTree(level, level.getChunkSource().getGenerator(), pos, Blocks.AIR.defaultBlockState(), level.getRandom())) {
          // If we fail, set it back to how it was
          level.setBlock(below, belowState, 4);
          level.setBlock(treePos, currentState, 4);
        }
      });
    }
  }

  @Override
  protected void animationTick(PyreBlockEntity blockEntity, int duration) {

  }

  @Override
  protected void initialize() {

  }

  @Override
  protected RitualProperty<Integer> getDurationProperty() {
    return ModRituals.WILDROOT_GROWTH_DURATION.get();
  }

  @Override
  protected RitualProperty<Integer> getRadiusXZProperty() {
    return ModRituals.WILDROOT_GROWTH_RADIUS_XZ.get();
  }

  @Override
  protected RitualProperty<Integer> getRadiusYProperty() {
    return ModRituals.WILDROOT_GROWTH_RADIUS_Y.get();
  }

  @Override
  protected RitualProperty<Integer> getIntervalProperty() {
    return ModRituals.WILDROOT_GROWTH_INTERVAL.get();
  }
}
