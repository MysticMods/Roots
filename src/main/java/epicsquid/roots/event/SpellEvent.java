package epicsquid.roots.event;

import epicsquid.roots.spell.SpellBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.Event;

public class SpellEvent extends Event {
  private EntityPlayer player;
  private SpellBase spell;
  private int cooldown;

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