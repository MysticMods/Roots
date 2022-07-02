package mysticmods.roots.ritual;

import mysticmods.roots.api.ritual.Ritual;
import mysticmods.roots.blockentity.PyreBlockEntity;
import mysticmods.roots.init.ModRituals;
import noobanidus.libs.noobutil.util.ItemUtil;

public class CraftingRitual extends Ritual {
  @Override
  public void ritualTick(PyreBlockEntity blockEntity) {
    int dur = getDuration() - blockEntity.getLifetime();
    if (dur == getInterval()) {
      // do execution
      ItemUtil.Spawn.spawnItem(blockEntity.getLevel(), blockEntity.getBlockPos().above(), blockEntity.popStoredItem());
    }
  }

  @Override
  public void animateTick(PyreBlockEntity blockEntity) {

  }

  @Override
  public void initialize() {
    this.duration = ModRituals.CRAFTING_DURATION.get().getValue();
    this.interval = ModRituals.CRAFTING_INTERVAL.get().getValue();
  }
}
