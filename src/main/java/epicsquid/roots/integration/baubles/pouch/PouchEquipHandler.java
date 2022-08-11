package epicsquid.roots.integration.baubles.pouch;

import baubles.api.BaubleType;
import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;

public class PouchEquipHandler {
	
	public static boolean tryEquipPouch(EntityPlayer player, ItemStack stack) {
		IBaublesItemHandler baubles = BaublesApi.getBaublesHandler(player);
		ItemStack copy = stack.copy();
		
		for (int i : BaubleType.BELT.getValidSlots()) {
			if (baubles.insertItem(i, copy, false).isEmpty()) {
				stack.shrink(1);
				player.world.playSound(null, player.getPosition(), SoundEvents.ITEM_ARMOR_EQUIP_CHAIN, SoundCategory.PLAYERS, 0.5F, 1.0F);
				return true;
			}
		}
		
		return false;
	}
}


