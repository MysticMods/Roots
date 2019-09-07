package epicsquid.roots.event;

import epicsquid.roots.spell.SpellBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.Event;

// TODO: Actually do something interesting with this. At the minute it's split between onItemUse/onItemStoppedUsing -- there's nothing to differentiate between those two events, however. Additionally, if we are going to have an event, it would make more sense for the event to be "on start, on tick, on stop".
@Deprecated
public class SpellEvent extends Event {
  private EntityPlayer player;
  private SpellBase spell;
  private int cooldown;

  public SpellEvent(EntityPlayer player, SpellBase spell) {
    super();
    this.player = player;
    this.spell = spell;
    this.cooldown = spell.getCooldown();
  }

  public EntityPlayer getPlayer() {
    return player;
  }

  public SpellBase getSpell() {
    return spell;
  }

  public int getCooldown() {
    return cooldown;
  }

  public void setSpell(SpellBase spell) {
    this.spell = spell;
  }

  public void setCooldown(int cooldown) {
    this.cooldown = cooldown;
  }

  @Override
  public boolean isCancelable() {
    return true;
  }
}