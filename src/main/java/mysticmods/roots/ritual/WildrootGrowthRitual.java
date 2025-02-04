package mysticmods.roots.ritual;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.property.Property;
import mysticmods.roots.api.property.PropertyHolder;
import mysticmods.roots.api.ritual.Ritual;
import mysticmods.roots.block.crop.ThreeStageCropBlock;
import mysticmods.roots.blockentity.PyreBlockEntity;
import mysticmods.roots.init.ModBlocks;
import mysticmods.roots.init.ModRituals;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;

public class WildrootGrowthRitual extends Ritual {
  /*  private final AbstractTreeGrower treeGrower = new RootsTreeGrowers();*/

  @Override
  protected void functionalTick(Level pLevel, BlockPos pPos, BlockState pState, BoundingBox pBoundingBox, PyreBlockEntity blockEntity, int duration, RandomSource randomSource) {
    if (duration == 0 || interval == 0) {
      RootsAPI.LOG.error("ugh");
    }
    if (duration % interval == 0) {
      ServerLevel level = (ServerLevel) blockEntity.getLevel();
      BlockPos.betweenClosedStream(getAABB().move(blockEntity.getBlockPos())).filter(o -> {
        BlockState state = level.getBlockState(o);
        if (state.is(RootsTags.Blocks.WILDROOT_CROP)) {
          return state.hasProperty(ThreeStageCropBlock.AGE) && state.getValue(ThreeStageCropBlock.AGE) == ModBlocks.WILDROOT_CROP.value().getMaxAge();
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
        // TODO:
/*        if (!treeGrower.growTree(level, level.getChunkSource().getGenerator(), pos, Blocks.AIR.defaultBlockState(), level.getRandom())) {
          // If we fail, set it back to how it was
          level.setBlock(below, belowState, 4);
          level.setBlock(treePos, currentState, 4);
        }*/
      });
    }
  }

  @Override
  protected void animationTick(Level pLevel, BlockPos pPos, BlockState pState, BoundingBox pBoundingBox, PyreBlockEntity blockEntity, int duration, RandomSource randomSource) {

  }

  @Override
  protected void initialize(Holder<Ritual> holder) {

  }

  @Override
  protected PropertyHolder<Property.IntegerProperty> getDurationProperty() {
    return ModRituals.WILDROOT_GROWTH_DURATION;
  }

  @Override
  protected PropertyHolder<Property.IntegerProperty> getRadiusXZProperty() {
    return ModRituals.WILDROOT_GROWTH_RADIUS_XZ;
  }

  @Override
  protected PropertyHolder<Property.IntegerProperty> getRadiusYProperty() {
    return ModRituals.WILDROOT_GROWTH_RADIUS_Y;
  }

  @Override
  protected PropertyHolder<Property.IntegerProperty> getIntervalProperty() {
    return ModRituals.WILDROOT_GROWTH_INTERVAL;
  }
}
