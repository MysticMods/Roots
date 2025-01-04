package mysticmods.roots.client;

import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.capability.Grant;
import mysticmods.roots.api.modifier.SpellModifier;
import mysticmods.roots.api.registry.RootsRegistries;
import mysticmods.roots.api.ritual.Ritual;
import mysticmods.roots.api.spell.Spell;
import net.minecraft.world.item.ItemStack;

import java.util.Map;

// TODO: Reset when reloading
public class ItemCache {
  private static final Map<Spell, ItemStack> SPELL_CACHE = new Object2ObjectLinkedOpenHashMap<>();
  private static final Map<SpellModifier, ItemStack> MODIFIER_CACHE = new Object2ObjectLinkedOpenHashMap<>();
  private static final Map<Ritual, ItemStack> RITUAL_CACHE = new Object2ObjectLinkedOpenHashMap<>();

  public static ItemStack getCachedSpell(Spell spell) {
    return ItemStack.EMPTY;
/*    return SPELL_CACHE.computeIfAbsent(spell, TokenItem::getSpellToken);*/
  }

  public static ItemStack getCachedRitual(Ritual ritual) {
    return ItemStack.EMPTY;
/*    return RITUAL_CACHE.computeIfAbsent(ritual, TokenItem::getRitualToken);*/
  }

  public static ItemStack getCachedModifier(SpellModifier modifier) {
    return ItemStack.EMPTY;
/*    return MODIFIER_CACHE.computeIfAbsent(modifier, TokenItem::getModifierToken);*/
  }

  public static ItemStack getGrantStack(Grant grant) {
    if (grant.type() == Grant.Type.SPELL) {
      Spell spell = RootsRegistries.SPELLS.get(grant.id());
      if (spell == null) {
        RootsAPI.LOG.error("Grant {} references non-existent spell {}", grant, grant.id());
        return ItemStack.EMPTY;
      }
      return getCachedSpell(spell);
    } else if (grant.type() == Grant.Type.MODIFIER) {
      SpellModifier modifier = RootsRegistries.SPELL_MODIFIERS.get(grant.id());
      if (modifier == null) {
        RootsAPI.LOG.error("Grant {} references non-existent modifier {}", grant, grant.id());
        return ItemStack.EMPTY;
      }
      return getCachedModifier(modifier);
    } else {
      RootsAPI.LOG.error("Grant {} references unknown type {}", grant, grant.type());
      return ItemStack.EMPTY;
    }
  }
}
