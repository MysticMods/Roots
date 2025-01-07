package mysticmods.roots.api.spell;

import mysticmods.roots.api.modifier.SpellModifier;
import net.minecraft.core.Holder;

public record LibraryModifier(Holder<SpellModifier> modifier, boolean enabled) {
}
