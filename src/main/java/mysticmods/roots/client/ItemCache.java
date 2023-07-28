package mysticmods.roots.client;

import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.capability.Grant;
import mysticmods.roots.api.modifier.Modifier;
import mysticmods.roots.api.registry.Registries;
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

  public static ItemStack getGrantStack (Grant grant) {
    if (grant.getType() == Grant.Type.SPELL) {
      Spell spell = Registries.SPELL_REGISTRY.get().getValue(grant.getId());
      if (spell == null) {
        RootsAPI.LOG.error("Grant {} references non-existent spell {}", grant, grant.getId());
        return ItemStack.EMPTY;
      }
      return getCachedSpell(spell);
    } else if (grant.getType() == Grant.Type.MODIFIER) {
      Modifier modifier = Registries.MODIFIER_REGISTRY.get().getValue(grant.getId());
      if (modifier == null) {
        RootsAPI.LOG.error("Grant {} references non-existent modifier {}", grant, grant.getId());
        return ItemStack.EMPTY;
      }
      return getCachedModifier(modifier);
    } else {
      RootsAPI.LOG.error("Grant {} references unknown type {}", grant, grant.getType());
      return ItemStack.EMPTY;
    }
  }
}
