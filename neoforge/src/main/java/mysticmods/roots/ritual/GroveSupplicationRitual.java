package mysticmods.roots.ritual;

import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.StateProperties;
import mysticmods.roots.api.property.RitualProperty;
import mysticmods.roots.api.ritual.Ritual;
import mysticmods.roots.blockentity.PyreBlockEntity;
import mysticmods.roots.init.ModRituals;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;

public class GroveSupplicationRitual extends Ritual {
  @Override
  public void functionalTick(Level pLevel, BlockPos pPos, BlockState pState, BoundingBox pBoundingBox, PyreBlockEntity blockEntity, int duration) {
    if (duration % getInterval() == 0) {
      if (blockEntity.getBoundingBox() != null) {
        BlockPos.betweenClosedStream(blockEntity.getBoundingBox()).forEach(pos -> {
          BlockState state = blockEntity.getLevel().getBlockState(pos);
          if (state.is(RootsTags.Blocks.GROVE_STONE_PRIMAL)) {
            if (state.hasProperty(StateProperties.GroveStone.PART) && state.hasProperty(StateProperties.GroveStone.VALID)) {
              if (!state.getValue(StateProperties.GroveStone.VALID)) {
                blockEntity.getLevel().setBlockAndUpdate(pos, state.setValue(StateProperties.GroveStone.VALID, true));
              }
            }
          }
        });
      }
    }
  }

  @Override
  public void animationTick(Level pLevel, BlockPos pPos, BlockState pState, BoundingBox pBoundingBox, PyreBlockEntity blockEntity, int duration) {

  }

  @Override
  public void initialize() {
  }

  @Override
  protected RitualProperty<Integer> getDurationProperty() {
    return ModRituals.GROVE_SUPPLICATION_DURATION.get();
  }

  @Override
  protected RitualProperty<Integer> getRadiusXZProperty() {
    return ModRituals.GROVE_SUPPLICATION_RADIUS_XZ.get();
  }

  @Override
  protected RitualProperty<Integer> getRadiusYProperty() {
    return ModRituals.GROVE_SUPPLICATION_RADIUS_Y.get();
  }

  @Override
  protected RitualProperty<Integer> getIntervalProperty() {
    return ModRituals.GROVE_SUPPLICATION_INTERVAL.get();
  }
}
