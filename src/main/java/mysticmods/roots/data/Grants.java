package mysticmods.roots.data;

import com.google.common.collect.ImmutableSet;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.modifier.Modifier;
import mysticmods.roots.api.spells.Spell;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class Grants extends DirectorySavedData {
  private final Map<UUID, Set<ResourceLocation>> GRANTED_SPELLS = new Object2ObjectLinkedOpenHashMap<>();
  private final Map<UUID, Set<ResourceLocation>> GRANTED_MODIFIERS = new Object2ObjectLinkedOpenHashMap<>();

  public boolean hasSpell (Player player, ResourceKey<Spell> spell) {
    return hasSpell(player, spell.location());
  }

  public boolean hasSpell(Player player, ResourceLocation spell) {
    Set<ResourceLocation> spells = GRANTED_SPELLS.get(player.getUUID());
    return spells != null && spells.contains(spell);
  }

  public void addSpell(Player player, ResourceKey<Spell> spell) {
    addSpell(player, spell.location());
  }

  public void addSpell(Player player, ResourceLocation spell) {
    getOrCreateSpells(player).add(spell);
  }

  @Nullable
  public Set<ResourceLocation> getSpells (Player player) {
    Set<ResourceLocation> result = GRANTED_SPELLS.get(player.getUUID());
    if (result == null) {
      return null;
    }
    return ImmutableSet.copyOf(result);
  }

  protected Set<ResourceLocation> getOrCreateSpells(Player player) {
    setDirty();
    return GRANTED_SPELLS.computeIfAbsent(player.getUUID(), (k) -> new ObjectLinkedOpenHashSet<>());
  }

  public boolean hasModifier (Player player, ResourceKey<Modifier> modifier) {
    return hasModifier(player, modifier.location());
  }

  public boolean hasModifier(Player player, ResourceLocation modifier) {
    Set<ResourceLocation> modifiers = GRANTED_MODIFIERS.get(player.getUUID());
    return modifiers != null && modifiers.contains(modifier);
  }

  public void addModifier (Player player, ResourceKey<Modifier> modifier) {
    addModifier(player, modifier.location());
  }

  public void addModifier(Player player, ResourceLocation modifier) {
    getOrCreateModifiers(player).add(modifier);
  }

  @Nullable
  public Set<ResourceLocation> getModifiers (Player player) {
    Set<ResourceLocation> result = GRANTED_MODIFIERS.get(player.getUUID());
    if (result == null) {
      return null;
    }
    return ImmutableSet.copyOf(result);
  }

  protected Set<ResourceLocation> getOrCreateModifiers(Player player) {
    setDirty();
    return GRANTED_MODIFIERS.computeIfAbsent(player.getUUID(), (k) -> new ObjectLinkedOpenHashSet<>());
  }

  public static Grants load(CompoundTag pCompoundTag) {
    Grants result = new Grants();
    ListTag spells = pCompoundTag.getList("spells", Tag.TAG_COMPOUND);
    for (int i = 0; i < spells.size(); i++) {
      CompoundTag thisEntry = spells.getCompound(i);
      Set<ResourceLocation> grantedSpells = result.GRANTED_SPELLS.computeIfAbsent(thisEntry.getUUID("player"), (k) -> new ObjectLinkedOpenHashSet<>());
      ListTag incomingSpells = thisEntry.getList("spells", Tag.TAG_STRING);
      for (int j = 0; j < incomingSpells.size(); j++) {
        grantedSpells.add(new ResourceLocation(incomingSpells.getString(j)));
      }
    }

    ListTag modifiers = pCompoundTag.getList("modifiers", Tag.TAG_COMPOUND);
    for (int i = 0; i < modifiers.size(); i++) {
      CompoundTag thisEntry = modifiers.getCompound(i);
      Set<ResourceLocation> grantedModifiers = result.GRANTED_MODIFIERS.computeIfAbsent(thisEntry.getUUID("player"), (k) -> new ObjectLinkedOpenHashSet<>());
      ListTag incomingModifiers = thisEntry.getList("modifiers", Tag.TAG_STRING);
      for (int j = 0; j < incomingModifiers.size(); j++) {
        grantedModifiers.add(new ResourceLocation(incomingModifiers.getString(j)));
      }
    }

    return result;
  }

  @Override
  public CompoundTag save(CompoundTag pCompoundTag) {
    ListTag spells = new ListTag();
    for (Map.Entry<UUID, Set<ResourceLocation>> entry : GRANTED_SPELLS.entrySet()) {
      CompoundTag thisEntry = new CompoundTag();
      thisEntry.putUUID("player", entry.getKey());
      ListTag thisSpells = new ListTag();
      for (ResourceLocation spell : entry.getValue()) {
        thisSpells.add(StringTag.valueOf(spell.toString()));
      }
      if (!thisSpells.isEmpty()) {
        thisEntry.put("spells", thisSpells);
        spells.add(thisEntry);
      }
    }
    ListTag modifiers = new ListTag();
    for (Map.Entry<UUID, Set<ResourceLocation>> entry : GRANTED_MODIFIERS.entrySet()) {
      CompoundTag thisEntry = new CompoundTag();
      thisEntry.putUUID("player", entry.getKey());
      ListTag thisModifiers = new ListTag();
      for (ResourceLocation modifier : entry.getValue()) {
        thisModifiers.add(StringTag.valueOf(modifier.toString()));
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
}
