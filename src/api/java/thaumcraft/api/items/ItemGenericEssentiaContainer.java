package thaumcraft.api.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.IEssentiaContainerItem;

public class ItemGenericEssentiaContainer extends Item implements IEssentiaContainerItem
{
	
	public ItemGenericEssentiaContainer(int base)
    {
        super();
        this.base = base;
        this.setMaxStackSize(64);
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
        
    }	
	
	protected int base = 1;
	
    @Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
    	for (Aspect tag:Aspect.aspects.values()) {
    		ItemStack i = new ItemStack(this);
    		this.setAspects(i, new AspectList().add(tag, base));
    		items.add(i);
		}
	}
    
	@Override
	public AspectList getAspects(ItemStack itemstack) {
		if (itemstack.hasTagCompound()) {
			AspectList aspects = new AspectList();
			aspects.readFromNBT(itemstack.getTagCompound());
			return aspects.size()>0?aspects:null;
		}
		return null;
	}

	@Override
	public void setAspects(ItemStack itemstack, AspectList aspects) {
		if (!itemstack.hasTagCompound()) 
			itemstack.setTagCompound(new NBTTagCompound());
		aspects.writeToNBT(itemstack.getTagCompound());
	}
	
	@Override
	public boolean ignoreContainedAspects() {return false;}
	
	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int par4, boolean par5) {
		if (!world.isRemote && !stack.hasTagCompound()) {
			Aspect[] displayAspects = Aspect.aspects.values().toArray(new Aspect[]{});
			this.setAspects(stack, new AspectList().add(displayAspects[world.rand.nextInt(displayAspects.length)], base));
		}
		super.onUpdate(stack, world, entity, par4, par5);
	}

	@Override
	public void onCreated(ItemStack stack, World world, EntityPlayer player) {
		if (!world.isRemote && !stack.hasTagCompound()) {
			Aspect[] displayAspects = Aspect.aspects.values().toArray(new Aspect[]{});
			this.setAspects(stack, new AspectList().add(displayAspects[world.rand.nextInt(displayAspects.length)], base));
		}
	}
}
