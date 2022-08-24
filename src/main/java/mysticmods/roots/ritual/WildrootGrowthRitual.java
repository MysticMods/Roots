package mysticmods.roots.ritual;

import mysticmods.roots.api.property.RitualProperty;
import mysticmods.roots.api.ritual.Ritual;
import mysticmods.roots.blockentity.PyreBlockEntity;
import mysticmods.roots.init.ModRituals;

public class WildrootGrowthRitual extends Ritual {
  @Override
  protected void functionalTick(PyreBlockEntity blockEntity, int duration) {

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
