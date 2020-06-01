package epicsquid.roots.entity.spell;

import epicsquid.roots.entity.EntityLifetimeBase;
import epicsquid.roots.spell.SpellBase;
import net.minecraft.world.World;

public abstract class EntitySpellBase<T extends SpellBase> extends EntityLifetimeBase implements ISpellEntity {
  protected T instance;
  protected double amplifier;
  protected double speedy;

  public EntitySpellBase(World worldIn) {
    super(worldIn);
  }

  public double getAmplifier() {
    return amplifier;
  }

  public void setAmplifier(double amplifier) {
    this.amplifier = amplifier;
  }

  public double getSpeedy() {
    return speedy;
  }

  public void setSpeedy(double speedy) {
    this.speedy = speedy;
  }
}
