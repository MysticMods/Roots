// @HEADER@
package mysticmods.roots.api.reference;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.modifier.SpellModifier;
import mysticmods.roots.api.registry.RootsRegistries;
import net.minecraft.resources.ResourceKey;

public interface SpellModifiers {
  // @KEYS@

  static ResourceKey<SpellModifier> key(String id) {
    return ResourceKey.create(RootsRegistries.Keys.SPELL_MODIFIERS, RootsAPI.rl(id));
  }

  static void noop() {
  }
}
