package mysticmods.roots.item.accessory;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

public class AlertnessRing extends RingItem implements ICurioItem {
  private final AABB bounds = new AABB(-15, -15, -15, 15, 15, 15);

  public AlertnessRing(Properties properties) {
    super(properties);
  }

  @Override
  public void curioTick(SlotContext slotContext, ItemStack stack) {

  }
}
