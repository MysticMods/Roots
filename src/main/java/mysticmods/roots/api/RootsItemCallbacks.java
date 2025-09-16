package mysticmods.roots.api;

import mysticmods.roots.api.grove.Grove;
import mysticmods.roots.api.ritual.Ritual;
import mysticmods.roots.api.spell.Spell;
import mysticmods.roots.api.spell.SpellModifier;
import mysticmods.roots.item.TokenItem;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Unit;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.registries.callback.AddCallback;
import net.neoforged.neoforge.registries.callback.ClearCallback;

import java.util.HashMap;
import java.util.Map;

public class RootsItemCallbacks implements AddCallback<Item>, ClearCallback<Item> {
  private static final RootsItemCallbacks INSTANCE = new RootsItemCallbacks();
  private static final Map<Spell, Item> SPELL_TO_ITEM_MAP = new HashMap<>();
  private static final Map<Spell, ItemStack> SPELL_TO_ITEMSTACK_MAP = new HashMap<>();
  private static final Map<Spell, ItemStack> SPELL_TO_LIBRARY_ITEMSTACK_MAP = new HashMap<>();
  private static final Map<SpellModifier, Item> SPELL_MODIFIER_ITEM_MAP = new HashMap<>();
  private static final Map<Ritual, Item> RITUAL_TO_ITEM_MAP = new HashMap<>();
  private static final Map<Ritual, ItemStack> RITUAL_TO_ITEMSTACK_MAP = new HashMap<>();
  private static final Map<Grove, Item> GROVE_TO_ITEM_MAP = new HashMap<>();
  private static final Map<Grove, ItemStack> GROVE_TO_ITEMSTACK_MAP = new HashMap<>();

  public static RootsItemCallbacks getInstance() {
    return INSTANCE;
  }

  @Override
  public void onAdd(Registry<Item> registry, int id, ResourceKey<Item> key, Item value) {
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

  @Override
  public void onClear(Registry<Item> registry, boolean full) {
    if (full) {
      SPELL_TO_ITEM_MAP.clear();
      SPELL_MODIFIER_ITEM_MAP.clear();
      RITUAL_TO_ITEM_MAP.clear();
      GROVE_TO_ITEM_MAP.clear();
      GROVE_TO_ITEMSTACK_MAP.clear();
      SPELL_TO_ITEMSTACK_MAP.clear();
      SPELL_TO_LIBRARY_ITEMSTACK_MAP.clear();
      RITUAL_TO_ITEMSTACK_MAP.clear();
    }
  }

  public static Item getItem(Spell spell) {
    return SPELL_TO_ITEM_MAP.getOrDefault(spell, Items.AIR);
  }

  public static ItemStack getItemStack(Spell spell) {
    return SPELL_TO_ITEMSTACK_MAP.getOrDefault(spell, ItemStack.EMPTY);
  }

  public static ItemStack getItemStack(Ritual ritual) {
    return RITUAL_TO_ITEMSTACK_MAP.getOrDefault(ritual, ItemStack.EMPTY);
  }

  public static ItemStack getLibraryItemStack(Spell spell) {
    return SPELL_TO_LIBRARY_ITEMSTACK_MAP.getOrDefault(spell, ItemStack.EMPTY);
  }

  public static Item getItem(SpellModifier modifier) {
    return SPELL_MODIFIER_ITEM_MAP.getOrDefault(modifier, Items.AIR);
  }

  public static Item getItem(Ritual ritual) {
    return RITUAL_TO_ITEM_MAP.getOrDefault(ritual, Items.AIR);
  }

  public static Item getItem (Grove grove) {
    return GROVE_TO_ITEM_MAP.getOrDefault(grove, Items.AIR);
  }

  public static ItemStack getItemStack (Grove grove) {
    return GROVE_TO_ITEMSTACK_MAP.getOrDefault(grove, ItemStack.EMPTY);
  }
}
