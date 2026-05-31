package mysticmods.roots.api;

import mysticmods.roots.api.grove.Grove;
import mysticmods.roots.api.modifier.SpellModifier;
import mysticmods.roots.api.registry.RootsRegistries;
import mysticmods.roots.api.ritual.Ritual;
import mysticmods.roots.api.spell.Spell;
import mysticmods.roots.item.TokenItem;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Unit;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.HashMap;
import java.util.Map;

public class RootsItemCallbacks {
  private static final Map<Spell, Item> SPELL_TO_ITEM_MAP = new HashMap<>();
  private static final Map<Spell, ItemStack> SPELL_TO_ITEMSTACK_MAP = new HashMap<>();
  private static final Map<Spell, ItemStack> SPELL_TO_LIBRARY_ITEMSTACK_MAP = new HashMap<>();
  private static final Map<SpellModifier, Item> SPELL_MODIFIER_ITEM_MAP = new HashMap<>();
  private static final Map<Ritual, Item> RITUAL_TO_ITEM_MAP = new HashMap<>();
  private static final Map<Ritual, ItemStack> RITUAL_TO_ITEMSTACK_MAP = new HashMap<>();
  private static final Map<Grove, Item> GROVE_TO_ITEM_MAP = new HashMap<>();
  private static final Map<Grove, ItemStack> GROVE_TO_ITEMSTACK_MAP = new HashMap<>();

  public static void fill() {
    for (Item value : BuiltInRegistries.ITEM) {
      if (value instanceof TokenItem.SpellTokenItem spellToken) {
        var key = spellToken.getSpell();
        SPELL_TO_ITEM_MAP.put(key, spellToken);
        SPELL_TO_LIBRARY_ITEMSTACK_MAP.put(key, new ItemStack(spellToken));
        ItemStack library = new ItemStack(spellToken);
        library.set(RootsAPI.getInstance().getDeletableType(), Unit.INSTANCE);
        library.set(RootsAPI.getInstance().getModifiableType(), Unit.INSTANCE);
        SPELL_TO_ITEMSTACK_MAP.put(key, library);
      } else if (value instanceof TokenItem.RitualTokenItem ritualToken) {
        var key = ritualToken.getRitual();
        RITUAL_TO_ITEM_MAP.put(key, ritualToken);
        RITUAL_TO_ITEMSTACK_MAP.put(key, new ItemStack(ritualToken));
      } else if (value instanceof TokenItem.GroveTokenItem groveToken) {
        var key = groveToken.getGrove();
        GROVE_TO_ITEM_MAP.put(key, groveToken);
        GROVE_TO_ITEMSTACK_MAP.put(key, new ItemStack(groveToken));
      }
    }
  }

  @SuppressWarnings("unchecked")
  public static Item getItemGeneric(ResourceKey<?> key) {
    if (key == RootsRegistries.Keys.SPELLS) {
      return getItem(RootsRegistries.SPELLS.get((ResourceKey<Spell>) key));
    } else if (key == RootsRegistries.Keys.RITUALS) {
      return getItem(RootsRegistries.RITUALS.get((ResourceKey<Ritual>) key));
    } else if (key == RootsRegistries.Keys.GROVES) {
      return getItem(RootsRegistries.GROVES.get((ResourceKey<Grove>) key));
    } else {
      return Items.AIR;
    }
  }

  public static ItemStack getItemStackGeneric(ResourceKey<?> key) {
    if (key.isFor(RootsRegistries.Keys.SPELLS)) {
      return getItemStack(RootsRegistries.SPELLS.get((ResourceKey<Spell>) key));
    } else if (key.isFor(RootsRegistries.Keys.RITUALS)) {
      return getItemStack(RootsRegistries.RITUALS.get((ResourceKey<Ritual>) key));
    } else if (key.isFor(RootsRegistries.Keys.GROVES)) {
      return getItemStack(RootsRegistries.GROVES.get((ResourceKey<Grove>) key));
    } else {
      return ItemStack.EMPTY;
    }
  }

  public static Item getItem(Spell spell) {
    if (SPELL_TO_ITEM_MAP.isEmpty()) {
      fill();
    }
    return SPELL_TO_ITEM_MAP.getOrDefault(spell, Items.AIR);
  }

  public static ItemStack getItemStack(Spell spell) {
    if (SPELL_TO_ITEMSTACK_MAP.isEmpty()) {
      fill();
    }
    return SPELL_TO_ITEMSTACK_MAP.getOrDefault(spell, ItemStack.EMPTY);
  }

  public static ItemStack getItemStack(Ritual ritual) {
    if (RITUAL_TO_ITEMSTACK_MAP.isEmpty()) {
      fill();
    }
    return RITUAL_TO_ITEMSTACK_MAP.getOrDefault(ritual, ItemStack.EMPTY);
  }

  public static ItemStack getLibraryItemStack(Spell spell) {
    if (SPELL_TO_LIBRARY_ITEMSTACK_MAP.isEmpty()) {
      fill();
    }
    return SPELL_TO_LIBRARY_ITEMSTACK_MAP.getOrDefault(spell, ItemStack.EMPTY);
  }

  public static Item getItem(SpellModifier modifier) {
    return Items.AIR;
    //return SPELL_MODIFIER_ITEM_MAP.getOrDefault(modifier, Items.AIR);
  }

  public static Item getItem(Ritual ritual) {
    if (RITUAL_TO_ITEM_MAP.isEmpty()) {
      fill();
    }
    return RITUAL_TO_ITEM_MAP.getOrDefault(ritual, Items.AIR);
  }

  public static Item getItem(Grove grove) {
    if (GROVE_TO_ITEM_MAP.isEmpty()) {
      fill();
    }
    return GROVE_TO_ITEM_MAP.getOrDefault(grove, Items.AIR);
  }

  public static ItemStack getItemStack(Grove grove) {
    if (GROVE_TO_ITEMSTACK_MAP.isEmpty()) {
      fill();
    }
    return GROVE_TO_ITEMSTACK_MAP.getOrDefault(grove, ItemStack.EMPTY);
  }
}
