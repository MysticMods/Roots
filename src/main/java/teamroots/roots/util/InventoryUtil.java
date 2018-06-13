package teamroots.roots.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import teamroots.roots.item.ItemPouch;

public class InventoryUtil {
	public static double getPowderCapacityTotal(EntityPlayer player, String plantName){
		double amount = 0;
		for (int i = 0; i < 36; i ++){
			if (player.inventory.getStackInSlot(i) != null){
				if (player.inventory.getStackInSlot(i).getItem() instanceof ItemPouch){
					if (player.inventory.getStackInSlot(i).hasTagCompound()){
						if (player.inventory.getStackInSlot(i).getTagCompound().hasKey("plant")){
							if (player.inventory.getStackInSlot(i).getTagCompound().getString("plant").compareTo(plantName) == 0){
								amount += ItemPouch.capacity;
							}
						}
					}
				}
			}
		}
		return amount;
	}
	
	public static double getPowderTotal(EntityPlayer player, String plantName){
		double amount = 0;
		for (int i = 0; i < 36; i ++){
			if (player.inventory.getStackInSlot(i) != null){
				if (player.inventory.getStackInSlot(i).getItem() instanceof ItemPouch){
					if (player.inventory.getStackInSlot(i).hasTagCompound()){
						if (player.inventory.getStackInSlot(i).getTagCompound().hasKey("plant")){
							if (player.inventory.getStackInSlot(i).getTagCompound().getString("plant").compareTo(plantName) == 0){
								amount += ItemPouch.getQuantity(player.inventory.getStackInSlot(i), plantName);
							}
						}
					}
				}
			}
		}
		return amount;
	}
	
	public static void removePowder(EntityPlayer player, String plantName, double amount){
		double temp = amount;
		for (int i = 0; i < 36; i ++){
			if (player.inventory.getStackInSlot(i) != null){
				if (player.inventory.getStackInSlot(i).getItem() instanceof ItemPouch){
					if (player.inventory.getStackInSlot(i).hasTagCompound()){
						if (player.inventory.getStackInSlot(i).getTagCompound().hasKey("plant")){
							if (player.inventory.getStackInSlot(i).getTagCompound().getString("plant").compareTo(plantName) == 0){
								double removeAmount = Math.min(128.0, Math.min(ItemPouch.getQuantity(player.inventory.getStackInSlot(i), plantName),temp));
								ItemPouch.setQuantity(player.inventory.getStackInSlot(i), plantName, ItemPouch.getQuantity(player.inventory.getStackInSlot(i), plantName)-removeAmount);
								temp -= removeAmount;
							}
						}
					}
				}
			}
		}
	}
}
