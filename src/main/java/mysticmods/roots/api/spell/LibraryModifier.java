package mysticmods.roots.api.spell;

import mysticmods.roots.api.modifier.Modifier;
import net.minecraft.core.Holder;

public record LibraryModifier(Holder<Modifier> modifier, boolean enabled) {
}
