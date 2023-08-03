package mysticmods.roots.ritual;

import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.property.RitualProperty;
import mysticmods.roots.api.ritual.Ritual;
import mysticmods.roots.blockentity.PyreBlockEntity;
import mysticmods.roots.init.ModBlocks;
import mysticmods.roots.init.ModRituals;
import mysticmods.roots.item.GroveSporesItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;

import java.util.List;

public class OvergrowthRitual extends Ritual {
  private static final Direction[] HORIZONTALS = new Direction[]{Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST};
  private BlockPos lastChanged;


  @Override
  protected void functionalTick(Level pLevel, BlockPos pPos, BlockState pState, BoundingBox pBoundingBox, PyreBlockEntity blockEntity, int duration) {
    if (duration % interval == 0) {
      boolean placed = false;
      if (lastChanged != null && !pBoundingBox.isInside(lastChanged)) {
        lastChanged = null;
      }
      if (lastChanged != null) {
        BlockState lastState = pLevel.getBlockState(lastChanged);
        if (lastState.is(RootsTags.Blocks.GROVE_MOSS)) {
          for (Direction dir : HORIZONTALS) {
            BlockPos offset = lastChanged.relative(dir);
            if (GroveSporesItem.canPlace(pLevel, offset, Direction.UP)) {
              pLevel.setBlock(offset, ModBlocks.CREEPING_GROVE_MOSS.get().defaultBlockState(), 3);
              lastChanged = offset;
              placed = true;
              break;
            }
          }
        }
      }
      if (!placed) {
        lastChanged = null;
        List<BlockPos> positions = BlockPos.betweenClosedStream(pBoundingBox).map(BlockPos::immutable).toList();
        outer: for (BlockPos pos : positions) {
          if (pLevel.getFluidState(pos).is(FluidTags.WATER)) {
            for (Direction dir : HORIZONTALS) {
              BlockPos offset = pos.above().relative(dir);
              if (GroveSporesItem.canPlace(pLevel, offset, Direction.UP)) {
                pLevel.setBlock(offset, ModBlocks.CREEPING_GROVE_MOSS.get().defaultBlockState(), 2);
                lastChanged = offset;
                break outer;
              }
            }
          } else if (pLevel.getBlockState(pos).is(RootsTags.Blocks.GROVE_MOSS)) {
            for (Direction dir : HORIZONTALS) {
              BlockPos offset = pos.relative(dir);
              if (GroveSporesItem.canPlace(pLevel, offset, Direction.UP)) {
                pLevel.setBlock(offset, ModBlocks.CREEPING_GROVE_MOSS.get().defaultBlockState(), 2);
                lastChanged = offset;
                break outer;
              }
            }
          }
        }
      }
    }
  }

  @Override
  protected void animationTick(Level pLevel, BlockPos pPos, BlockState pState, BoundingBox pBoundingBox, PyreBlockEntity blockEntity, int duration) {

  }

  @Override
  protected void initialize() {

  }

  @Override
  protected RitualProperty<Integer> getDurationProperty() {
    return ModRituals.OVERGROWTH_DURATION.get();
  }

  @Override
  protected RitualProperty<Integer> getRadiusXZProperty() {
    return ModRituals.OVERGROWTH_RADIUS_XZ.get();
  }

  @Override
  protected RitualProperty<Integer> getRadiusYProperty() {
    return ModRituals.OVERGROWTH_RADIUS_Y.get();
  }

  @Override
  protected RitualProperty<Integer> getIntervalProperty() {
    return ModRituals.OVERGROWTH_INTERVAL.get();
  }
}
