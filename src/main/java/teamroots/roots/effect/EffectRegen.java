package teamroots.roots.effect;

import java.util.Collection;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EffectRegen extends Effect {
	public EffectRegen(String name, boolean hasIcon) {
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
	
	@Override
	public void onTick(EntityLivingBase entity, int remainingDuration, NBTTagCompound data){
		int level = data.getInteger("level");
		if (remainingDuration % 12*(6-level) == 0){
			entity.heal(1.0f);
		}
	}
}
