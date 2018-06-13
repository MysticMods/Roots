package teamroots.roots.effect;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.PotionTypes;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.client.event.GuiScreenEvent.PotionShiftEvent;
import teamroots.roots.particle.ParticleUtil;
import teamroots.roots.util.Misc;

public class EffectAstralGlow extends Effect {
	public EffectAstralGlow(String name, boolean hasIcon) {
		super(name, hasIcon);
	}
	
	public void onTick(EntityLivingBase entity, int remainingDuration){
		if (entity.world.isRemote){
			if (entity.getUniqueID().compareTo(Minecraft.getMinecraft().player.getUniqueID()) == 0){
				List<EntityLivingBase> entities = entity.world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(entity.posX-32,entity.posY-32,entity.posZ-32,entity.posX+32,entity.posY+32,entity.posZ+32));
				for (EntityLivingBase e : entities){
					if (Misc.random.nextInt(10) == 0){
						if (e.getUniqueID().compareTo(entity.getUniqueID()) != 0){
							ParticleUtil.spawnParticleGlowThroughBlocks(entity.world, (float)e.posX, (float)e.posY+e.height/2.0f, (float)e.posZ, 0, Misc.random.nextFloat()*0.025f, 0, 122, 144, 255, 0.25f, 6.0f+Misc.random.nextFloat()*4.0f, 120);
						}
					}	
				}
			}
		}
	}
}
