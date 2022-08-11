package epicsquid.roots.item;

import epicsquid.mysticallib.util.Util;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface ILivingRepair {
	default void update(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected, int bound) {
		if (!worldIn.isRemote && entityIn instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) entityIn;
			if (player.isHandActive() && isSelected) {
				return;
			}
			if (stack.getItemDamage() > 0) {
				if (Util.rand.nextInt(Math.max(1, bound)) == 0) {
					stack.setItemDamage(stack.getItemDamage() - 1);
				}
			}
		}
	}
}
