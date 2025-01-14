package mysticmods.roots.api.spell;

import net.minecraft.core.Holder;

import java.util.Set;

// If this exists then it's unlocked
public record SpellData(Holder<Spell> spell, Set<Holder<SpellModifier>> unlockedModifiers) {
}
