package teamroots.roots.util;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import teamroots.roots.RegistryManager;

public class OfferingUtil {
	public static Map<Item, Integer> values = new HashMap<Item, Integer>();
	
	public static void init(){
		values.put(RegistryManager.moonglow_leaf, 2);
		values.put(RegistryManager.terra_moss_ball, 2);
		values.put(RegistryManager.wildroot_item, 2);
		values.put(RegistryManager.aubergine_item, 2);
		values.put(RegistryManager.pereskia_blossom, 2);
		values.put(RegistryManager.pereskia_bulb, 2);
		values.put(RegistryManager.aubergine_seeds, 2);
		
		values.put(Items.WHEAT, 1);
		values.put(Items.POTATO, 1);
		values.put(Items.CARROT, 1);
		values.put(Items.BEETROOT, 1);
		
		values.put(Items.BLAZE_POWDER, 16);
		values.put(Items.ENDER_PEARL, 16);
		values.put(Items.SPIDER_EYE, 16);
		
		values.put(Items.APPLE, 4);
	}
	
	public static int getValue(ItemStack heldItem) {
		if (!heldItem.isEmpty()){
			if (values.containsKey(heldItem.getItem())){
				return values.get(heldItem.getItem())*heldItem.getCount();
			}
		}
		return 0;
	}

}
