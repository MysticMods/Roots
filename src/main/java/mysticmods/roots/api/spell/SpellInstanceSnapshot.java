package mysticmods.roots.api.spell;

import mysticmods.roots.api.modifier.SpellModifierSet;
import net.minecraft.core.component.DataComponentHolder;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.PatchedDataComponentMap;

import java.util.UUID;

public record SpellInstanceSnapshot(UUID id, int slot, Spell spell, SpellModifierSet enabledModifiers,
                                    PatchedDataComponentMap data) implements ISpellInstance, DataComponentHolder {
  @Override
  public Spell asSpell() {
    return spell();
  }

  @Override
  public SpellModifierSet getEnabledModifiers() {
    return enabledModifiers();
  }

  @Override
  public DataComponentMap getComponents() {
    return data();
  }
}
