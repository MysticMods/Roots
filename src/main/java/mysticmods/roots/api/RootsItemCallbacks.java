package mysticmods.roots.api;

import mysticmods.roots.api.grove.Grove;
import mysticmods.roots.api.ritual.Ritual;
import mysticmods.roots.api.spell.Spell;
import mysticmods.roots.api.spell.SpellModifier;
import mysticmods.roots.item.TokenItem;
import net.minecraft.core.registries.BuiltInRegistries;
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
        SPELL_TO_ITEM_MAP.put(spellToken.getSpell(), spellToken);
        SPELL_TO_ITEMSTACK_MAP.put(spellToken.getSpell(), new ItemStack(spellToken));
        ItemStack library = new ItemStack(spellToken);
        library.set(RootsAPI.getInstance().getDeletableType(), Unit.INSTANCE);
        SPELL_TO_LIBRARY_ITEMSTACK_MAP.put(spellToken.getSpell(), library);
      } else if (value instanceof TokenItem.RitualTokenItem ritualToken) {
        RITUAL_TO_ITEM_MAP.put(ritualToken.getRitual(), ritualToken);
        RITUAL_TO_ITEMSTACK_MAP.put(ritualToken.getRitual(), new ItemStack(ritualToken));
      } else if (value instanceof TokenItem.GroveTokenItem groveToken) {
        GROVE_TO_ITEM_MAP.put(groveToken.getGrove(), groveToken);
        GROVE_TO_ITEMSTACK_MAP.put(groveToken.getGrove(), new ItemStack(groveToken));
      }
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
