package teamroots.roots.effect;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class Effect {
	public String name = "";
	public boolean hasIcon = false;
	public ResourceLocation icon = null;
	public Effect(String name, boolean hasIcon){
		this.name = name;
		this.hasIcon = hasIcon;
	}
	
	public Effect setIcon(ResourceLocation icon){
		this.icon = icon;
		this.hasIcon = true;
		return this;
	}
	
	public void onTick(EntityLivingBase entity, int remainingDuration, NBTTagCompound data){
		
	}
	
	public void onApplied(EntityLivingBase entity, NBTTagCompound data){
		
	}
	
	public void onEnd(EntityLivingBase entity, NBTTagCompound data){
		
	}
}
