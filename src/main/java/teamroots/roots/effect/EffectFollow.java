package teamroots.roots.effect;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIFollowGolem;
import net.minecraft.entity.ai.EntityAIFollowOwner;
import net.minecraft.entity.ai.EntityAITasks.EntityAITaskEntry;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.init.PotionTypes;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.client.event.GuiScreenEvent.PotionShiftEvent;
import teamroots.roots.Constants;
import teamroots.roots.entity.ai.EntityAIFollow;
import teamroots.roots.particle.ParticleUtil;
import teamroots.roots.util.Misc;

public class EffectFollow extends Effect {
	public EffectFollow(String name, boolean hasIcon) {
		super(name, hasIcon);
	}
	
	public void onApplied(EntityLivingBase entity){
		if (entity instanceof EntityLiving){
			Entity target = entity.world.getEntityByID(entity.getEntityData().getInteger(Constants.FOLLOW_EFFECT_TARGET));
			if (target instanceof EntityLivingBase){
				System.out.println(target.getName());
				((EntityLiving)entity).tasks.addTask(8, new EntityAIFollow((EntityLiving)entity,(EntityLivingBase)target,entity.getAIMoveSpeed(),0,64));
			}
		}
	}
	
	public void onEnd(EntityLivingBase entity){
		if (entity instanceof EntityLiving){
			List<EntityAIBase> aiToDelete = new ArrayList<EntityAIBase>();
			for (EntityAITaskEntry ai : ((EntityLiving)entity).tasks.taskEntries){
				if (ai.action instanceof EntityAIFollow){
					aiToDelete.add(ai.action);
				}
			}
			for (int i = 0; i < aiToDelete.size(); i ++){
				((EntityLiving)entity).tasks.removeTask(aiToDelete.get(i));
			}
			entity.getEntityData().removeTag(Constants.FOLLOW_EFFECT_TARGET);
		}
	}
}
