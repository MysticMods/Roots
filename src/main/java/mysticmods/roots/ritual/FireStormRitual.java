package mysticmods.roots.ritual;

import mysticmods.roots.api.property.RitualProperty;
import mysticmods.roots.api.ritual.Ritual;
import mysticmods.roots.blockentity.PyreBlockEntity;
import mysticmods.roots.init.ModRituals;

public class FireStormRitual extends Ritual {
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
    return ModRituals.FIRE_STORM_DURATION.get();
  }

  @Override
  protected RitualProperty<Integer> getRadiusXZProperty() {
    return null;
  }

  @Override
  protected RitualProperty<Integer> getRadiusYProperty() {
    return null;
  }

  @Override
  protected RitualProperty<Integer> getIntervalProperty() {
    return ModRituals.FIRE_STORM_INTERVAL.get();
  }
}
