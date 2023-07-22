package mysticmods.roots.client.gui;

import mysticmods.roots.api.SpellLike;
import mysticmods.roots.api.spell.Spell;
import mysticmods.roots.client.CachedSpellLike;

import java.util.function.Supplier;

@FunctionalInterface
public interface SpellSupplier<T extends SpellLike> extends Supplier<T>, CachedSpellLike {
  @Override
  default Spell getAsSpell() {
    return get().getAsSpell();
  }
}
