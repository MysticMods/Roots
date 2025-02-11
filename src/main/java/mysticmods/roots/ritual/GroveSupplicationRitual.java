package mysticmods.roots.ritual;

import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.StateProperties;
import mysticmods.roots.api.property.Property;
import mysticmods.roots.api.property.PropertyHolder;
import mysticmods.roots.api.ritual.Ritual;
import mysticmods.roots.blockentity.PyreBlockEntity;
import mysticmods.roots.init.ModRituals;
import mysticmods.roots.util.RitualPositionCache;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;

import java.util.List;
import java.util.function.BiPredicate;

public class GroveSupplicationRitual extends Ritual {
  private static final BiPredicate<Level, BlockPos> GROVE_STONE_PREDICATE = (level, pos) -> {
    BlockState state = level.getBlockState(pos);
    return state.is(RootsTags.Blocks.GROVE_STONE_PRIMAL) && state.hasProperty(StateProperties.GroveStone.PART) && state.hasProperty(StateProperties.GroveStone.ACTIVE) && !state.getValue(StateProperties.GroveStone.ACTIVE);
  };

  private static final List<BiPredicate<Level, BlockPos>> PREDICATES = List.of(GROVE_STONE_PREDICATE);

  @Override
  public List<BiPredicate<Level, BlockPos>> getPredicates() {
    return PREDICATES;
  }

  @Override
  public void functionalTick(Level pLevel, BlockPos pPos, BlockState pState, RitualPositionCache pCache, PyreBlockEntity blockEntity, int duration, RandomSource randomSource) {
    if (duration % getInterval() == 0) {
      // TODO: This currently activates every grove stone; maybe it should only activate 1?
      if (blockEntity.getBoundingBox() != null) {
        for (BlockPos pos : pCache.iterate(GROVE_STONE_PREDICATE, randomSource)) {
          BlockState state = blockEntity.getLevel().getBlockState(pos);
          if (state.is(RootsTags.Blocks.GROVE_STONE_PRIMAL)) {
            if (state.hasProperty(StateProperties.GroveStone.PART) && state.hasProperty(StateProperties.GroveStone.ACTIVE)) {
              if (!state.getValue(StateProperties.GroveStone.ACTIVE)) {
                blockEntity.getLevel().setBlockAndUpdate(pos, state.setValue(StateProperties.GroveStone.ACTIVE, true));
              }
            }
          }
        }
      }
    }
  }

  @Override
  public void animationTick(Level pLevel, BlockPos pPos, BlockState pState, BoundingBox pBoundingBox, PyreBlockEntity blockEntity, int duration, RandomSource randomSource) {

  }

  @Override
  public void initialize(Holder<Ritual> holder) {
  }

  @Override
  protected PropertyHolder<Property.IntegerProperty> getDurationProperty() {
    return ModRituals.GROVE_SUPPLICATION_DURATION;
  }

  @Override
  protected PropertyHolder<Property.IntegerProperty> getRadiusXZProperty() {
    return ModRituals.GROVE_SUPPLICATION_RADIUS_XZ;
  }

  @Override
  protected PropertyHolder<Property.IntegerProperty> getRadiusYProperty() {
    return ModRituals.GROVE_SUPPLICATION_RADIUS_Y;
  }

  @Override
  protected PropertyHolder<Property.IntegerProperty> getIntervalProperty() {
    return ModRituals.GROVE_SUPPLICATION_INTERVAL;
  }
}
