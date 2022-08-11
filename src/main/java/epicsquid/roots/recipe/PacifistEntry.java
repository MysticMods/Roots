package epicsquid.roots.recipe;

import epicsquid.roots.Roots;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public class PacifistEntry {
	private Class<? extends Entity> entityClass;
	private boolean checkTarget = false;
	private ResourceLocation name;
	
	public PacifistEntry(Entity entity) {
		this.entityClass = entity.getClass();
		this.name = new ResourceLocation(Roots.MODID, entity.getName());
	}
	
	public PacifistEntry(Class<? extends Entity> entity, String name) {
		this.entityClass = entity;
		this.name = new ResourceLocation(Roots.MODID, name);
	}
	
	public ResourceLocation getRegistryName() {
		return name;
	}
	
	public boolean getCheckTarget() {
		return checkTarget;
	}
	
	public void setCheckTarget(boolean checkTarget) {
		this.checkTarget = checkTarget;
	}
	
	public Class<? extends Entity> getEntityClass() {
		return entityClass;
	}
	
	public boolean matches(Entity entity) {
		return this.entityClass.equals(entity.getClass());
	}
	
	public boolean matches(Entity entity, EntityPlayer player) {
		if (!matches(entity)) return false;
		
		if (!getCheckTarget()) return true;
		
		if (entity instanceof EntityLiving) {
			EntityLiving en = (EntityLiving) entity;
			return en.getAttackTarget() == null || !en.getAttackTarget().equals(player);
		}
		
		return true;
	}
}
