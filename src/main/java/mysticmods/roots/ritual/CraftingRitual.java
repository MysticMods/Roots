package mysticmods.roots.ritual;

import mysticmods.roots.api.ritual.Ritual;
import mysticmods.roots.blockentity.PyreBlockEntity;
import mysticmods.roots.init.ModRituals;

public class CraftingRitual extends Ritual {
  @Override
  public void ritualTick(PyreBlockEntity blockEntity) {
    if (blockEntity.getLifetime() == 10) {

    }
  }

  @Override
  public void animateTick(PyreBlockEntity blockEntity) {

  }

  @Override
  public void initialize() {
    this.duration = ModRituals.CRAFTING_DURATION.get().getValue();
  }
}
