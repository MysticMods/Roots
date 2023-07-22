package mysticmods.roots.client;

import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import mysticmods.roots.api.modifier.Modifier;
import mysticmods.roots.api.ritual.Ritual;
import mysticmods.roots.api.spell.Spell;
import mysticmods.roots.item.TokenItem;
import net.minecraft.world.item.ItemStack;

import java.util.Map;

// TODO: Reset when reloading
public class ItemCache {
  private static final Map<Spell, ItemStack> SPELL_CACHE = new Object2ObjectLinkedOpenHashMap<>();
  private static final Map<Modifier, ItemStack> MODIFIER_CACHE = new Object2ObjectLinkedOpenHashMap<>();
  private static final Map<Ritual, ItemStack> RITUAL_CACHE = new Object2ObjectLinkedOpenHashMap<>();

  public static ItemStack getCachedSpell (Spell spell) {
    return SPELL_CACHE.computeIfAbsent(spell, TokenItem::getSpellToken);
  }

  public static ItemStack getCachedRitual (Ritual ritual) {
    return RITUAL_CACHE.computeIfAbsent(ritual, TokenItem::getRitualToken);
  }

  public static ItemStack getCachedModifier (Modifier modifier) {
    return MODIFIER_CACHE.computeIfAbsent(modifier, TokenItem::getModifierToken);
  }
}
