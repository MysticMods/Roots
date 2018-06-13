package teamroots.roots.effect;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EffectFireResist extends Effect {
	public EffectFireResist(String name, boolean hasIcon) {
		super(name, hasIcon);
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	/**
	 * ARGS:
	 * 
	 *  - 0: "level" (Integer) ~ Represents effect strength.
	 */
	public static NBTTagCompound createData(Object... args){
		NBTTagCompound tag = new NBTTagCompound();
		tag.setInteger("level", (int)args[0]);
		return tag;
	}
	
	@SubscribeEvent
	public void onLivingAttack(LivingAttackEvent event){
		if (event.getSource().isFireDamage() || event.getSource().isExplosion() 
				|| event.getSource().getDamageType().compareTo(DamageSource.LIGHTNING_BOLT.getDamageType()) == 0){
				if (EffectManager.hasEffect(event.getEntityLiving(), name)){
					NBTTagCompound tag = EffectManager.getEffectData(event.getEntityLiving(), name);
					int level = tag.getInteger("level");
					if (level == 5){
						event.setCanceled(true);
					}
				}
			}
	}
	
	@SubscribeEvent
	public void onLivingDamage(LivingHurtEvent event){
		if (event.getSource().isFireDamage() || event.getSource().isExplosion() 
			|| event.getSource().getDamageType().compareTo(DamageSource.LIGHTNING_BOLT.getDamageType()) == 0){
			if (EffectManager.hasEffect(event.getEntityLiving(), name)){
				NBTTagCompound tag = EffectManager.getEffectData(event.getEntityLiving(), name);
				int level = tag.getInteger("level");
				event.setAmount(event.getAmount()*(1.0f-(float)level*0.2f));
			}
		}
	}
}
