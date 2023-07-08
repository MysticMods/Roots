package mysticmods.roots.item;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public interface ILivingRepair {
  default void livingRepair(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected, int bounds) {
    if (!pLevel.isClientSide() && pEntity instanceof Player player && pStack.isDamaged() && pLevel.getRandom().nextInt(Math.max(1, bounds)) == 0) {
      pStack.setDamageValue(pStack.getDamageValue() - 1);
    }
  }
}
