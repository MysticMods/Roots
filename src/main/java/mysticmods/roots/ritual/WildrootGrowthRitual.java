package mysticmods.roots.ritual;

import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.property.Property;
import mysticmods.roots.api.property.PropertyHolder;
import mysticmods.roots.api.ritual.Ritual;
import mysticmods.roots.block.crop.ThreeStageCropBlock;
import mysticmods.roots.blockentity.PyreBlockEntity;
import mysticmods.roots.init.ModBlocks;
import mysticmods.roots.init.ModRituals;
import mysticmods.roots.mixin.accessor.AccessorMixinSaplingBlock;
import mysticmods.roots.util.RitualPositionCache;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;

import java.util.List;
import java.util.function.BiPredicate;

public class WildrootGrowthRitual extends Ritual {
  private static final BiPredicate<Level, BlockPos> MATURE_WILDROOT_CROP = (level, pos) -> {
    BlockState state = level.getBlockState(pos);
    if (state.is(RootsTags.Blocks.WILDROOT_CROP)) {
      return state.hasProperty(ThreeStageCropBlock.AGE) && state.getValue(ThreeStageCropBlock.AGE) == ModBlocks.WILDROOT_CROP.value()
          .getMaxAge();
    }
    return false;
  };

  private static final List<BiPredicate<Level, BlockPos>> PREDICATES = List.of(MATURE_WILDROOT_CROP);

  @Override
  public List<BiPredicate<Level, BlockPos>> getPredicates() {
    return PREDICATES;
  }

  @Override
  protected void functionalTick(Level pLevel, BlockPos pPos, BlockState pState, RitualPositionCache pCache, PyreBlockEntity blockEntity, int duration, RandomSource randomSource) {
    if (duration % interval == 0) {
      ServerLevel level = (ServerLevel) pLevel;

      BlockPos treePos = pCache.random(MATURE_WILDROOT_CROP, randomSource);
      if (treePos == null) {
        return; // TODO: Something here?
      }
      BlockState currentState = level.getBlockState(treePos);
      level.setBlock(treePos, Blocks.AIR.defaultBlockState(), 4);
      BlockPos below = treePos.below();
      // If it wasn't on a full height block, replace it with dirt
      BlockState belowState = level.getBlockState(below);
      if (!belowState.isFaceSturdy(level, below, Direction.UP) || !belowState.isCollisionShapeFullBlock(level, below)) {
        level.setBlock(below, Blocks.DIRT.defaultBlockState(), 3);
      }
      if (!((AccessorMixinSaplingBlock) ModBlocks.WILDWOOD_SAPLING.get()).rootsGetTreeGrower()
          .growTree(level, level.getChunkSource()
              .getGenerator(), treePos, Blocks.AIR.defaultBlockState(), level.getRandom())) {
        level.setBlock(below, belowState, 3);
        level.setBlock(treePos, currentState, 3);
      }
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
