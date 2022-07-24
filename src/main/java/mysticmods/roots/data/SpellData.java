package mysticmods.roots.data;

import mysticmods.roots.api.modifier.Modifier;
import mysticmods.roots.api.spells.Spell;

import java.util.Set;

// If this exists then it's unlocked
public class SpellData {
  private final Spell spell;
  private final Set<Modifier> unlockedModifiers;

  public SpellData(Spell spell, Set<Modifier> unlockedModifiers) {
    this.spell = spell;
    this.unlockedModifiers = unlockedModifiers;
  }
}
