package thaumcraft.api.internal;

import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandom;

import java.util.ArrayList;


public class WeightedRandomLoot extends WeightedRandom.Item {
	
	/**
	 * The Item/Block ID to generate in the bag.
	 */
	public ItemStack item;
	
	public WeightedRandomLoot(ItemStack stack, int weight) {
		super(weight);
		this.item = stack;
	}
	
	public static ArrayList<WeightedRandomLoot> lootBagCommon = new ArrayList<WeightedRandomLoot>();
	public static ArrayList<WeightedRandomLoot> lootBagUncommon = new ArrayList<WeightedRandomLoot>();
	public static ArrayList<WeightedRandomLoot> lootBagRare = new ArrayList<WeightedRandomLoot>();
	
}
