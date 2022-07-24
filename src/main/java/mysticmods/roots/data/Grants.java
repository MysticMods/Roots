package mysticmods.roots.data;

import com.google.common.collect.ImmutableSet;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet;
import mysticmods.roots.api.modifier.Modifier;
import mysticmods.roots.api.spells.Spell;
import mysticmods.roots.init.ModRegistries;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.server.ServerLifecycleHooks;

import javax.annotation.Nullable;
import java.util.*;

// TODO: Rename this?
public class Grants extends DirectorySavedData {
  private final Map<UUID, Set<Spell>> GRANTED_SPELLS = new Object2ObjectLinkedOpenHashMap<>();
  private final Map<UUID, Set<Modifier>> GRANTED_MODIFIERS = new Object2ObjectLinkedOpenHashMap<>();

  private static final Map<Modifier, Spell> MODIFIER_TO_SPELL_MAP = new Object2ObjectLinkedOpenHashMap<>();
  private static final Map<Spell, Set<Modifier>> SPELL_TO_MODIFIERS_MAP = new Object2ObjectLinkedOpenHashMap<>();

  @Nullable
  public static <T extends IForgeRegistryEntry<T>> T getRegistryEntry(ResourceKey<T> key) {
    Registry<T> registry = ServerLifecycleHooks.getCurrentServer().registryAccess().registryOrThrow(ResourceKey.createRegistryKey(key.location()));
    return registry.get(key);
  }

  public static void registerModifier(Spell spell, Modifier... modifiers) {
    SPELL_TO_MODIFIERS_MAP.computeIfAbsent(spell, (k) -> new ObjectLinkedOpenHashSet<>()).addAll(Arrays.asList(modifiers));
    for (Modifier modifier : modifiers) {
      MODIFIER_TO_SPELL_MAP.put(modifier, spell);
    }
  }

  @Nullable
  public static Spell spellForModifier(Modifier modifier) {
    return MODIFIER_TO_SPELL_MAP.get(modifier);
  }

  @Nullable
  public static Set<Modifier> modifiersForSpell(Spell spell) {
    return SPELL_TO_MODIFIERS_MAP.get(spell);
  }

  // TODO: does this make any sense?
  public static Map<Spell, SpellData> spellDataFromGrants(Player player) {
    Map<Spell, SpellData> result = new HashMap<>();
    Collection<Spell> spells = ModRegistries.SPELL_REGISTRY.get().getValues();
    Grants grants = getGrants();
    for (Spell spell : spells) {
      if (grants.hasSpell(player, spell)) {
        Set<Modifier> unlockedModifiers = new HashSet<>();
        Set<Modifier> modifiers = modifiersForSpell(spell);
        if (modifiers != null) {
          for (Modifier modId : modifiers) {
            if (grants.hasModifier(player, modId)) {
              unlockedModifiers.add(modId);
            }
          }
        }
        result.put(spell, new SpellData(spell, unlockedModifiers));
      }
    }
    return result;
  }

  public boolean hasSpell(Player player, Spell spell) {
    Set<Spell> spells = GRANTED_SPELLS.get(player.getUUID());
    return spells != null && spells.contains(spell);
  }

  public void addSpell(Player player, Spell spell) {
    getOrCreateSpells(player).add(spell);
  }

  @Nullable
  public Set<Spell> getSpells(Player player) {
    Set<Spell> result = GRANTED_SPELLS.get(player.getUUID());
    if (result == null) {
      return null;
    }
    return ImmutableSet.copyOf(result);
  }

  protected Set<Spell> getOrCreateSpells(Player player) {
    setDirty();
    return GRANTED_SPELLS.computeIfAbsent(player.getUUID(), (k) -> new ObjectLinkedOpenHashSet<>());
  }

  public boolean hasModifier(Player player, Modifier modifier) {
    Set<Modifier> modifiers = GRANTED_MODIFIERS.get(player.getUUID());
    return modifiers != null && modifiers.contains(modifier);
  }

  public void addModifier(Player player, Modifier modifier) {
    getOrCreateModifiers(player).add(modifier);
  }

  @Nullable
  public Set<Modifier> getModifiers(Player player) {
    Set<Modifier> result = GRANTED_MODIFIERS.get(player.getUUID());
    if (result == null) {
      return null;
    }
    return ImmutableSet.copyOf(result);
  }

  protected Set<Modifier> getOrCreateModifiers(Player player) {
    setDirty();
    return GRANTED_MODIFIERS.computeIfAbsent(player.getUUID(), (k) -> new ObjectLinkedOpenHashSet<>());
  }

  public static Grants load(CompoundTag pCompoundTag) {
    Grants result = new Grants();
    ListTag spells = pCompoundTag.getList("spells", Tag.TAG_COMPOUND);
    for (int i = 0; i < spells.size(); i++) {
      CompoundTag thisEntry = spells.getCompound(i);
      Set<Spell> grantedSpells = result.GRANTED_SPELLS.computeIfAbsent(thisEntry.getUUID("player"), (k) -> new ObjectLinkedOpenHashSet<>());
      ListTag incomingSpells = thisEntry.getList("spells", Tag.TAG_STRING);
      for (int j = 0; j < incomingSpells.size(); j++) {
        grantedSpells.add(ModRegistries.SPELL_REGISTRY.get().getValue(new ResourceLocation(incomingSpells.getString(j))));
      }
    }

    ListTag modifiers = pCompoundTag.getList("modifiers", Tag.TAG_COMPOUND);
    for (int i = 0; i < modifiers.size(); i++) {
      CompoundTag thisEntry = modifiers.getCompound(i);
      Set<Modifier> grantedModifiers = result.GRANTED_MODIFIERS.computeIfAbsent(thisEntry.getUUID("player"), (k) -> new ObjectLinkedOpenHashSet<>());
      ListTag incomingModifiers = thisEntry.getList("modifiers", Tag.TAG_STRING);
      for (int j = 0; j < incomingModifiers.size(); j++) {
        grantedModifiers.add(ModRegistries.MODIFIER_REGISTRY.get().getValue(new ResourceLocation(incomingModifiers.getString(j))));
      }
    }

    return result;
  }

  @Override
  public CompoundTag save(CompoundTag pCompoundTag) {
    ListTag spells = new ListTag();
    for (Map.Entry<UUID, Set<Spell>> entry : GRANTED_SPELLS.entrySet()) {
      CompoundTag thisEntry = new CompoundTag();
      thisEntry.putUUID("player", entry.getKey());
      ListTag thisSpells = new ListTag();
      for (Spell spell : entry.getValue()) {
        thisSpells.add(StringTag.valueOf(ModRegistries.SPELL_REGISTRY.get().getKey(spell).toString()));
      }
      if (!thisSpells.isEmpty()) {
        thisEntry.put("spells", thisSpells);
        spells.add(thisEntry);
      }
    }
    ListTag modifiers = new ListTag();
    for (Map.Entry<UUID, Set<Modifier>> entry : GRANTED_MODIFIERS.entrySet()) {
      CompoundTag thisEntry = new CompoundTag();
      thisEntry.putUUID("player", entry.getKey());
      ListTag thisModifiers = new ListTag();
      for (Modifier modifier : entry.getValue()) {
        thisModifiers.add(StringTag.valueOf(ModRegistries.MODIFIER_REGISTRY.get().getKey(modifier).toString()));
      }
      if (!thisModifiers.isEmpty()) {
        thisEntry.put("modifiers", thisModifiers);
        modifiers.add(thisEntry);
      }
    }
    pCompoundTag.put("granted_spells", spells);
    pCompoundTag.put("granted_modifiers", modifiers);
    return pCompoundTag;
  }

  public static Grants getGrants() {
    //noinspection ConstantConditions
    return ServerLifecycleHooks.getCurrentServer().getLevel(Level.OVERWORLD).getDataStorage().computeIfAbsent(Grants::load, Grants::new, "roots/Grants");
  }
}
