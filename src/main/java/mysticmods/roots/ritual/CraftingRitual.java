package mysticmods.roots.ritual;

import mysticmods.roots.api.ritual.Ritual;
import mysticmods.roots.blockentity.PyreBlockEntity;
import mysticmods.roots.init.ModRituals;
import net.minecraft.world.item.ItemStack;
import noobanidus.libs.noobutil.util.ItemUtil;

import java.util.List;

public class CraftingRitual extends Ritual {
  @Override
  public void functionalTick(PyreBlockEntity blockEntity, int dur) {
    if (dur == getInterval()) {
      List<ItemStack> output = blockEntity.popStoredItems();
      for (ItemStack stack : output) {
        ItemUtil.Spawn.spawnItem(blockEntity.getLevel(), blockEntity.getBlockPos().above(), stack);
      }
    }
  }

  @Override
  public void animationTick(PyreBlockEntity blockEntity, int dur) {

  }

  @Override
  public void initialize() {
    this.duration = ModRituals.CRAFTING_DURATION.get().getValue();
    this.interval = ModRituals.CRAFTING_INTERVAL.get().getValue();
  }
}
