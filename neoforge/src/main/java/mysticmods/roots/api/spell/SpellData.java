package mysticmods.roots.api.spell;

import mysticmods.roots.api.modifier.SpellModifier;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;

import java.util.Set;

// If this exists then it's unlocked
public record SpellData(Holder<Spell> spell, Set<Holder<SpellModifier>> unlockedModifiers) {
}
