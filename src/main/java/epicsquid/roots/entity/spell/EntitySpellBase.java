package epicsquid.roots.entity.spell;

import epicsquid.roots.entity.EntityLifetimeBase;
import epicsquid.roots.spell.SpellBase;
import net.minecraft.world.World;

public abstract class EntitySpellBase<T extends SpellBase> extends EntityLifetimeBase implements ISpellEntity {
  protected T instance;

  public EntitySpellBase(World worldIn) {
    super(worldIn);
  }
}
