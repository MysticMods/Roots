package epicsquid.roots.util;

import epicsquid.roots.Roots;
import epicsquid.roots.item.ItemQuiver;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Roots.MODID)
public class QuiverInventoryUtil {
	public static ItemStack getQuiver(EntityPlayer player) {
		for (int i = 0; i < 36; i++) {
			if (player.inventory.getStackInSlot(i).getItem() instanceof ItemQuiver) {
				return player.inventory.getStackInSlot(i);
			}
		}
		
		return ItemStack.EMPTY;
	}
}