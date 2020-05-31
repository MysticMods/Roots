package epicsquid.roots.spell.info;

import epicsquid.roots.modifiers.IModifierList;
import epicsquid.roots.spell.SpellBase;

import javax.annotation.Nullable;

public abstract class AbstractSpellModifiers<T extends IModifierList<?, ?>> extends AbstractSpellInfo {
  protected T modifiers;

  public AbstractSpellModifiers() {
  }

  public AbstractSpellModifiers(SpellBase spell) {
    super(spell);
  }

  public abstract T getModifiers ();
}
