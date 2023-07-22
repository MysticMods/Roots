package mysticmods.roots.client;

import mysticmods.roots.api.SpellLike;
import net.minecraft.world.item.ItemStack;

public interface CachedSpellLike extends SpellLike {
  default ItemStack getAsItemStack () {
    return ItemCache.getCachedSpell(getAsSpell());
  }
}
