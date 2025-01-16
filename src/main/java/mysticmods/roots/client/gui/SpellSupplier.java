package mysticmods.roots.client.gui;

import mysticmods.roots.api.SpellLike;
import mysticmods.roots.api.registry.IStyled;
import mysticmods.roots.api.spell.Spell;
import mysticmods.roots.client.CachedSpellLike;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.util.function.Supplier;

@FunctionalInterface
public interface SpellSupplier<T extends SpellLike> extends Supplier<T>, CachedSpellLike {
  @Override
  default Spell asSpell() {
    T result = get();
    if (result == null) {
      return null;
    }

    return result.asSpell();
  }

  default MutableComponent getStyledName() {
    if (get() instanceof IStyled styled) {
      return styled.getStyledName();
    }

    return Component.empty();
  }
}
