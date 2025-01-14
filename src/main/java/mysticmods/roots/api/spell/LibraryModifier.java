package mysticmods.roots.api.spell;

import net.minecraft.core.Holder;

public record LibraryModifier(Holder<SpellModifier> modifier, boolean enabled) {
}
