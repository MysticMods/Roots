package epicsquid.roots.item;

import epicsquid.mysticallib.util.Util;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface ILivingRepair {
  default void update(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
    update(stack, worldIn, entityIn, itemSlot, isSelected, 40);
  }

  default void update(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected, int bound) {
    if (entityIn instanceof PlayerEntity) {
      if (stack.equals(((PlayerEntity) entityIn).getActiveItemStack())) return;
    }
    if (Util.rand.nextInt(Math.max(1, bound)) == 0) {
      stack.setDamage(stack.getDamage() - 1);
    }
  }
}
