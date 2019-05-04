package epicsquid.roots.item;

import epicsquid.mysticallib.util.Util;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface ILivingRepair {
  default void update(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
    if (isSelected && entityIn instanceof EntityPlayer) {
      if (stack.equals(((EntityPlayer) entityIn).getActiveItemStack())) return;
    }
    int chance = Util.rand.nextInt(60);
    if(chance == 0){
      stack.setItemDamage(stack.getItemDamage()-1);
    }
  }
}
