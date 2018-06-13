package teamroots.roots.effect;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import teamroots.roots.event.SpellEvent;

public class EffectArcanism extends Effect {
	public EffectArcanism(String name, boolean hasIcon) {
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
	public void onCastSpell(SpellEvent event){
		if (EffectManager.hasEffect(event.getPlayer(), name)){
			NBTTagCompound tag = EffectManager.getEffectData(event.getPlayer(), name);
			int level = tag.getInteger("level");
			event.setCooldown(Math.round((float)event.getCooldown()/(1.0f+0.3f*(float)level)));
		}
	}
}
