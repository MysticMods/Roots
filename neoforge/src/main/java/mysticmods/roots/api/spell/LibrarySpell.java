package mysticmods.roots.api.spell;

import net.minecraft.core.Holder;

public record LibrarySpell(Holder<Spell> spell, boolean granted) {
}
