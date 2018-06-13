package teamroots.roots.effect;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.init.PotionTypes;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.client.event.GuiScreenEvent.PotionShiftEvent;
import teamroots.roots.Constants;
import teamroots.roots.particle.ParticleUtil;
import teamroots.roots.util.Misc;

public class EffectAllure extends Effect {
	public EffectAllure(String name, boolean hasIcon) {
		super(name, hasIcon);
	}
	
	public void onTick(EntityLivingBase entity, int timeRemaining){
		if (entity.ticksExisted % 20 == 0){
			List<EntityLivingBase> entities = entity.world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(entity.posX-48,entity.posY-48,entity.posZ-48,entity.posX+48,entity.posY+48,entity.posZ+48));
			for (EntityLivingBase e : entities){
				if (e.getUniqueID().compareTo(entity.getUniqueID()) != 0){
					if (e instanceof EntityLiving){
						((EntityLiving)e).setAttackTarget(entity);
					}
				}	
			}
		}
	}
}
