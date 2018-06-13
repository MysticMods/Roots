package teamroots.roots.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.Event;
import teamroots.roots.spell.SpellBase;

public class SpellEvent extends Event{
	EntityPlayer player = null;
	SpellBase spell = null;
	int cooldown = 0;
	public SpellEvent(EntityPlayer player, SpellBase spell){
		super();
		this.player = player;
		this.spell = spell;
		this.cooldown = spell.cooldown;
	}
	
	public EntityPlayer getPlayer(){
		return player;
	}
	
	public SpellBase getSpell(){
		return spell;
	}
	
	public int getCooldown(){
		return cooldown;
	}
	
	public void setSpell(SpellBase spell){
		this.spell = spell;
	}
	
	public void setCooldown(int cooldown){
		this.cooldown = cooldown;
	}
	
	@Override
	public boolean isCancelable(){
		return true;
	}
}
