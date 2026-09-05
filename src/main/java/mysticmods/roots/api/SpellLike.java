package mysticmods.roots.api;

import mysticmods.roots.api.spell.ISpellInstance;
import mysticmods.roots.api.spell.Spell;

public interface SpellLike {
  Spell asSpell();

  default ISpellInstance simple () {
    if (this instanceof ISpellInstance instance) {
      return instance;
    }

    return ISpellInstance.of(this.asSpell());
  }
}
