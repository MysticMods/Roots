package teamroots.roots.effect;

import java.lang.reflect.Field;
import java.util.Collection;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import teamroots.roots.event.SpellEvent;

public class EffectNaturesCure extends Effect {
	//Field f;
	public EffectNaturesCure(String name, boolean hasIcon) {
		super(name, hasIcon);
		MinecraftForge.EVENT_BUS.register(this);
		/*f = ReflectionHelper.findField(PotionEffect.class, "duration");
		f.setAccessible(true);*/
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
	
	@Override
	public void onTick(EntityLivingBase entity, int remainingDuration, NBTTagCompound data){
		/*int level = data.getInteger("level");
		Collection<PotionEffect> effects = entity.getActivePotionEffects();
		for (PotionEffect e : effects){
			if (e.getPotion().isBadEffect()){
				try {
					f.set(e, Math.max(0, e.getDuration()-level*20));
				} catch (IllegalArgumentException e1) {
					e1.printStackTrace();
				} catch (IllegalAccessException e1) {
					e1.printStackTrace();
				}
			}
		}*/
	}
}
