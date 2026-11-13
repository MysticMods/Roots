package mysticmods.roots.api.registry;

import mysticmods.roots.api.spell.ISpellInstance;
import mysticmods.roots.api.spell.Spell;

public interface SpellLike extends Like<Spell> {
  Spell asSpell();

  default ISpellInstance simple () {
    if (this instanceof ISpellInstance instance) {
      return instance;
    }

    return ISpellInstance.of(this.asSpell());
  }

  @Override
  default Spell asLike () {
    return asSpell();
  }
}
