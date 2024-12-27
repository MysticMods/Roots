package mysticmods.roots.client;

import mysticmods.roots.api.SpellLike;
import mysticmods.roots.api.spell.Spell;
import net.minecraft.world.item.ItemStack;

public interface CachedSpellLike extends SpellLike {
  default ItemStack getAsItemStack () {
    Spell result = getAsSpell();
    if (result == null) {
      return ItemStack.EMPTY;
    }
    return ItemCache.getCachedSpell(result);
  }
}
