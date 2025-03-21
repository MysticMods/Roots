package mysticmods.roots.ritual;

import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.property.Property;
import mysticmods.roots.api.property.PropertyHolder;
import mysticmods.roots.api.ritual.Ritual;
import mysticmods.roots.block.CreepingGroveMossBlock;
import mysticmods.roots.blockentity.PyreBlockEntity;
import mysticmods.roots.init.ModBlocks;
import mysticmods.roots.init.ModRituals;
import mysticmods.roots.item.GroveSporesItem;
import mysticmods.roots.util.RitualPositionCache;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.BiPredicate;

public class OvergrowthRitual extends Ritual {
  private static List<Direction> HORIZONTALS;
  // TODO: This *must* be on the block entity
  private BlockPos lastChanged;

  private static List<Direction> horizontals() {
    if (HORIZONTALS == null) {
      HORIZONTALS = new ArrayList<>(Arrays.stream(Direction.values()).filter(dir -> dir.getAxis().isHorizontal())
          .toList());
    }
    Collections.shuffle(HORIZONTALS);
    return HORIZONTALS;
  }

  private static final BiPredicate<Level, BlockPos> GROVE_MOSS_PREDICATE = (level, pos) -> level.getBlockState(pos)
      .is(RootsTags.Blocks.GROVE_MOSS);

  private static final BiPredicate<Level, BlockPos> WATER_PREDICATE = (level, pos) -> level.getFluidState(pos)
      .is(FluidTags.WATER);

  private static final List<BiPredicate<Level, BlockPos>> PREDICATES = Arrays.asList(GROVE_MOSS_PREDICATE, WATER_PREDICATE);

  @Override
  public List<BiPredicate<Level, BlockPos>> getPredicates() {
    return PREDICATES;
  }

  @Override
  protected void functionalTick(Level pLevel, BlockPos pPos, BlockState pState, RitualPositionCache pCache, PyreBlockEntity blockEntity, int duration, RandomSource randomSource) {
    if (duration % interval == 0) {
      boolean placed = false;
      if (lastChanged != null && !pCache.isInside(lastChanged)) {
        lastChanged = null;
      }
      if (lastChanged != null && randomSource.nextInt(4) == 0) {
        lastChanged = null;
      }
      if (lastChanged != null) {
        BlockState lastState = pLevel.getBlockState(lastChanged);
        if (lastState.is(RootsTags.Blocks.GROVE_MOSS)) {
          for (Direction dir : horizontals()) {
            BlockPos offset = lastChanged.relative(dir);
            if (pLevel.getFluidState(offset).isEmpty() && GroveSporesItem.canPlace(pLevel, offset, Direction.UP)) {
              pLevel.setBlock(offset, ModBlocks.CREEPING_GROVE_MOSS.get().defaultBlockState()
                  .setValue(CreepingGroveMossBlock.RITUAL_PLACED, true), 3);
              lastChanged = offset;
              placed = true;
              break;
            }
          }
        }
      }
      if (!placed) {
        lastChanged = null;
        for (BlockPos pos : pCache.iterate(GROVE_MOSS_PREDICATE, randomSource)) {
          for (Direction dir : horizontals()) {
            BlockPos offset = pos.relative(dir);
            if (pLevel.getFluidState(offset).isEmpty() && GroveSporesItem.canPlace(pLevel, offset, Direction.UP)) {
              pLevel.setBlock(offset, ModBlocks.CREEPING_GROVE_MOSS.get().defaultBlockState()
                  .setValue(CreepingGroveMossBlock.RITUAL_PLACED, true), 3);
              lastChanged = offset;
              return;
            }
          }
        }
        for (BlockPos pos : pCache.iterate(WATER_PREDICATE, randomSource)) {
          for (Direction dir : horizontals()) {
            BlockPos offset = pos.above().relative(dir);
            if (pLevel.getFluidState(offset).isEmpty() && GroveSporesItem.canPlace(pLevel, offset, Direction.UP)) {
              pLevel.setBlock(offset, ModBlocks.CREEPING_GROVE_MOSS.get().defaultBlockState()
                  .setValue(CreepingGroveMossBlock.RITUAL_PLACED, true), 3);
              lastChanged = offset;
              return;
            }
          }
        }
      }
    }
  }

  @Override
  protected void animationTick(Level pLevel, BlockPos pPos, BlockState pState, BoundingBox
      pBoundingBox, PyreBlockEntity blockEntity, int duration, RandomSource randomSource) {

  }

  @Override
  protected void initialize(Holder<Ritual> holder) {

  }

  @Override
  protected PropertyHolder<Property.IntegerProperty> getDurationProperty() {
    return ModRituals.OVERGROWTH_DURATION;
  }

  @Override
  protected PropertyHolder<Property.IntegerProperty> getRadiusXZProperty() {
    return ModRituals.OVERGROWTH_RADIUS_XZ;
  }

  @Override
  protected PropertyHolder<Property.IntegerProperty> getRadiusYProperty() {
    return ModRituals.OVERGROWTH_RADIUS_Y;
  }

  @Override
  protected PropertyHolder<Property.IntegerProperty> getIntervalProperty() {
    return ModRituals.OVERGROWTH_INTERVAL;
  }
}
