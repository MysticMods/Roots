package thaumcraft.api.research;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import thaumcraft.api.ThaumcraftApiHelper;

public class ScanItem implements IScanThing {
	
	String research;	
	ItemStack stack;

	public ScanItem(String research, ItemStack stack) {
		this.research = research;
		this.stack = stack;
	}

	@Override
	public boolean checkThing(EntityPlayer player, Object obj) {	
		if (obj == null) return false;
		
		ItemStack is = null;
		
		if (obj instanceof ItemStack) 
			is = (ItemStack) obj;
		if (obj instanceof EntityItem && ((EntityItem)obj).getItem()!=null) 
			is = ((EntityItem)obj).getItem();
		
		return is!=null && !is.isEmpty() && ThaumcraftApiHelper.areItemStacksEqualForCrafting(is, stack);
	}

	@Override
	public String getResearchKey(EntityPlayer player, Object object) {
		return research;
	}
	
	
	
}
