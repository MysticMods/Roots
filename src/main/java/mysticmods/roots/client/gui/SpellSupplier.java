package mysticmods.roots.client.gui;

import mysticmods.roots.api.SpellLike;
import mysticmods.roots.api.registry.IStyledRegistryEntry;
import mysticmods.roots.api.spell.Spell;
import mysticmods.roots.client.CachedSpellLike;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.util.function.Supplier;

@FunctionalInterface
public interface SpellSupplier<T extends SpellLike> extends Supplier<T>, CachedSpellLike {
  @Override
  default Spell getAsSpell() {
    T result = get();
    if (result == null) {
      return null;
    }

    return result.getAsSpell();
  }

  default MutableComponent getStyledName () {
    if (get() instanceof IStyledRegistryEntry styled) {
      return styled.getStyledName();
    }

    return Component.empty();
  }
}
