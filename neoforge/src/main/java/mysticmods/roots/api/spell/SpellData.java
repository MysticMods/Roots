package mysticmods.roots.api.spell;

import mysticmods.roots.api.modifier.SpellModifier;

import java.util.Set;

// If this exists then it's unlocked
public class SpellData {
  private final Spell spell;
  private final Set<SpellModifier> unlockedModifiers;

  public SpellData(Spell spell, Set<SpellModifier> unlockedModifiers) {
    this.spell = spell;
    this.unlockedModifiers = unlockedModifiers;
  }
}
