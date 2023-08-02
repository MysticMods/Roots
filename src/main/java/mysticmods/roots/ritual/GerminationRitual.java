package mysticmods.roots.ritual;

import mysticmods.roots.api.property.RitualProperty;
import mysticmods.roots.api.ritual.Ritual;
import mysticmods.roots.blockentity.PyreBlockEntity;
import mysticmods.roots.init.ModRituals;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;

public class GerminationRitual extends Ritual {
  @Override
  protected void functionalTick(Level pLevel, BlockPos pPos, BlockState pState, BoundingBox pBoundingBox, PyreBlockEntity blockEntity, int duration) {

  }

  @Override
  protected void animationTick(Level pLevel, BlockPos pPos, BlockState pState, BoundingBox pBoundingBox, PyreBlockEntity blockEntity, int duration) {

  }

  @Override
  protected void initialize() {

  }

  @Override
  protected RitualProperty<Integer> getDurationProperty() {
    return ModRituals.GERMINATION_DURATION.get();
  }

  @Override
  protected RitualProperty<Integer> getRadiusXZProperty() {
    return ModRituals.GERMINATION_RADIUS_XZ.get();
  }

  @Override
  protected RitualProperty<Integer> getRadiusYProperty() {
    return ModRituals.GERMINATION_RADIUS_Y.get();
  }

  @Override
  protected RitualProperty<Integer> getIntervalProperty() {
    return ModRituals.GERMINATION_INTERVAL.get();
  }
}
