package mysticmods.roots.api.capability;

import com.google.common.collect.ImmutableSet;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet;
import mysticmods.roots.api.modifier.SpellModifier;
import mysticmods.roots.api.registry.IDescribedRegistryEntry;
import mysticmods.roots.api.registry.RootsRegistries;
import mysticmods.roots.api.spell.LibraryModifier;
import mysticmods.roots.api.spell.LibrarySpell;
import mysticmods.roots.api.spell.Spell;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.*;

public class GrantCapability {
  private boolean dirty = true;
  private final Set<Spell> GRANTED_SPELLS = new ObjectLinkedOpenHashSet<>();
  private final Set<SpellModifier> GRANTED_MODIFIERS = new ObjectLinkedOpenHashSet<>();
  private ImmutableSet<Spell> IMMUTABLE_GRANTED_SPELLS = null;
  private ImmutableSet<SpellModifier> IMMUTABLE_GRANTED_MODIFIERS = null;

  private List<LibrarySpell> LIBRARY_SPELLS = null;

  private Map<Spell, List<LibraryModifier>> LIBRARY_MODIFIERS = null;

  private void reset() {
    NeoForgeRegistries

        IMMUTABLE_GRANTED_MODIFIERS = null;
    IMMUTABLE_GRANTED_SPELLS = null;
    LIBRARY_SPELLS = null;
    LIBRARY_MODIFIERS = null;
  }

  public GrantCapability() {
  }

  public boolean hasSpell(Spell spell) {
    return GRANTED_SPELLS.contains(spell);
  }

  public boolean hasModifier(SpellModifier modifier) {
    return GRANTED_MODIFIERS.contains(modifier);
  }

  public boolean canGrant(Unlock<?> grant) {
/*
    if (grant.type() == Grant.Type.SPELL) {
      Spell spell = RootsRegistries.SPELLS.get(grant.id());
      if (spell == null) {
        throw new NullPointerException("Spell " + grant.id() + " does not exist!");
      }

      return grant.repeatable() || !hasSpell(spell);
    } else if (grant.type() == Grant.Type.MODIFIER) {
      SpellModifier modifier = RootsRegistries.SPELL_MODIFIERS.get(grant.id());
      if (modifier == null) {
        throw new NullPointerException("Modifier " + grant.id() + " does not exist!");
      }

      return grant.repeatable() || !hasModifier(modifier);
    }
*/

    return false;
  }

  public boolean unlock(ServerPlayer player, Unlock<?> grant) {
/*
    if (grant.type() == Grant.Type.SPELL) {
      Spell spell = RootsRegistries.SPELLS.get(grant.id());
      if (spell == null) {
        throw new NullPointerException("Spell " + grant.id() + " does not exist!");
      }

      if (grant.repeatable() || !hasSpell(spell)) {
        player.displayClientMessage(Component.translatable("roots.message.spell.learned", spell.getStyledName()), true);
        grantSpell(spell);
        return true;
      }
    } else if (grant.type() == Grant.Type.MODIFIER) {
      SpellModifier modifier = RootsRegistries.SPELL_MODIFIERS.get(grant.id());
      if (modifier == null) {
        throw new NullPointerException("Modifier " + grant.id() + " does not exist!");
      }

      if (grant.repeatable() || !hasModifier(modifier)) {
        player.displayClientMessage(Component.translatable("roots.message.modifier.learned", modifier.getName()), true);
        grantModifier(modifier);
        return true;
      }
    }
*/

    return false;
  }

  private void grantSpell(Spell spell) {
    if (GRANTED_SPELLS.add(spell)) {
      reset();
      setDirty(true);
      // TODO: Handle reputation gains from learning new spells
    }
  }

  private void grantModifier(SpellModifier modifier) {
    if (GRANTED_MODIFIERS.add(modifier)) {
      reset();
      setDirty(true);
    }
  }

  public void removeSpell(Spell spell) {
    if (GRANTED_SPELLS.remove(spell)) {
      reset();
      setDirty(true);
    }
  }

  public void removeModifier(SpellModifier modifier) {
    if (GRANTED_MODIFIERS.remove(modifier)) {
      reset();
      setDirty(true);
    }
  }

  public Set<Spell> getSpells() {
    if (IMMUTABLE_GRANTED_SPELLS == null) {
      IMMUTABLE_GRANTED_SPELLS = ImmutableSet.copyOf(GRANTED_SPELLS);
    }
    return IMMUTABLE_GRANTED_SPELLS;
  }

  public Set<SpellModifier> getModifiers() {
    if (IMMUTABLE_GRANTED_MODIFIERS == null) {
      IMMUTABLE_GRANTED_MODIFIERS = ImmutableSet.copyOf(GRANTED_MODIFIERS);
    }
    return IMMUTABLE_GRANTED_MODIFIERS;
  }

  public List<LibrarySpell> getLibrarySpells() {
    if (LIBRARY_SPELLS == null) {
      LIBRARY_SPELLS = new ArrayList<>();
      GRANTED_SPELLS.stream().sorted(Comparator.comparing(IDescribedRegistryEntry::getDescriptionId)).forEach(o -> LIBRARY_SPELLS.add(new LibrarySpell(o.builtInRegistryHolder(), true)));
      RootsRegistries.SPELLS.stream().filter(o -> !GRANTED_SPELLS.contains(o)).sorted(Comparator.comparing(IDescribedRegistryEntry::getDescriptionId)).forEach(o -> LIBRARY_SPELLS.add(new LibrarySpell(o.builtInRegistryHolder(), false)));
    }
    return LIBRARY_SPELLS;
  }

  public List<LibraryModifier> getLibraryModifiers(Spell checkSpell) {
    if (LIBRARY_MODIFIERS == null) {
      LIBRARY_MODIFIERS = new Object2ObjectLinkedOpenHashMap<>();
    }
    return LIBRARY_MODIFIERS.computeIfAbsent(checkSpell, spell -> {
      List<LibraryModifier> result = new ArrayList<>();
      for (SpellModifier mod : spell.getModifiers()) {
        result.add(new LibraryModifier(mod.builtInRegistryHolder(), GRANTED_MODIFIERS.contains(mod)));
      }
      result.sort(Comparator.comparing(LibraryModifier::enabled));
      return result;
    });
  }

  public void fromRecord(SerializedGrantRecord record) {
    this.GRANTED_MODIFIERS.clear();
    this.GRANTED_SPELLS.clear();
    this.GRANTED_MODIFIERS.addAll(record.getGrantedModifiers());
    this.GRANTED_SPELLS.addAll(record.getGrantedSpells());
    reset();
    setDirty(true);
  }

  public SerializedGrantRecord toRecord() {
    return new SerializedGrantRecord(GRANTED_SPELLS, GRANTED_MODIFIERS);
  }

  public void setDirty(boolean dirty) {
    this.dirty = dirty;
  }

  public boolean isDirty() {
    return dirty;
  }

  public CompoundTag serializeNBT() {
    CompoundTag result = new CompoundTag();
    ListTag spells = new ListTag();
    GRANTED_SPELLS.forEach(o ->
        spells.add(StringTag.valueOf(o.builtInRegistryHolder().getKey().location().toString()))
    );
    result.put("spells", spells);
    ListTag modifiers = new ListTag();
    GRANTED_MODIFIERS.forEach(o ->
        modifiers.add(StringTag.valueOf(o.builtInRegistryHolder().getKey().location().toString()))
    );
    result.put("modifiers", modifiers);
    return result;
  }

  public void deserializeNBT(CompoundTag nbt) {
    GRANTED_SPELLS.clear();
    GRANTED_MODIFIERS.clear();
    ListTag spells = nbt.getList("spells", Tag.TAG_STRING);
    ListTag modifiers = nbt.getList("modifiers", Tag.TAG_STRING);
    for (int i = 0; i < spells.size(); i++) {
      ResourceLocation key = ResourceLocation.tryParse(spells.getString(i));
      Spell spell = RootsRegistries.SPELLS.get(key);
      if (spell != null) {
        GRANTED_SPELLS.add(spell);
      }
    }
    for (int i = 0; i < modifiers.size(); i++) {
      ResourceLocation key = ResourceLocation.tryParse(modifiers.getString(i));
      SpellModifier modifier = RootsRegistries.SPELL_MODIFIERS.get(key);
      if (modifier != null) {
        GRANTED_MODIFIERS.add(modifier);
      }
    }
    setDirty(true);
  }

  public static SerializedGrantRecord fromNetwork(FriendlyByteBuf buf) {
    SerializedGrantRecord result = new SerializedGrantRecord();
    result.fromNetwork(buf);
    return result;
  }

  public static class SerializedGrantRecord {
    private final Set<Spell> GRANTED_SPELLS = new ObjectLinkedOpenHashSet<>();
    private final Set<SpellModifier> GRANTED_MODIFIERS = new ObjectLinkedOpenHashSet<>();

    public SerializedGrantRecord() {
    }

    public SerializedGrantRecord(Set<Spell> spells, Set<SpellModifier> modifiers) {
      this.GRANTED_SPELLS.addAll(spells);
      this.GRANTED_MODIFIERS.addAll(modifiers);
    }

    public void fromNetwork(FriendlyByteBuf buf) {
      GRANTED_SPELLS.clear();
      GRANTED_MODIFIERS.clear();
      int spellCount = buf.readVarInt();
      for (int i = 0; i < spellCount; i++) {
        GRANTED_SPELLS.add(RootsRegistries.SPELLS.byIdOrThrow(buf.readVarInt()));
      }
      int modifierCount = buf.readVarInt();
      for (int i = 0; i < modifierCount; i++) {
        GRANTED_MODIFIERS.add(RootsRegistries.SPELL_MODIFIERS.byIdOrThrow(buf.readVarInt()));
      }
    }

    public void toNetwork(FriendlyByteBuf buf) {
      buf.writeVarInt(GRANTED_SPELLS.size());
      for (Spell spell : GRANTED_SPELLS) {
        buf.writeVarInt(RootsRegistries.SPELLS.getId(spell));
      }
      buf.writeVarInt(GRANTED_MODIFIERS.size());
      for (SpellModifier modifier : GRANTED_MODIFIERS) {
        buf.writeVarInt(RootsRegistries.SPELL_MODIFIERS.getId(modifier));
      }
    }

    public Set<Spell> getGrantedSpells() {
      return GRANTED_SPELLS;
    }

    public Set<SpellModifier> getGrantedModifiers() {
      return GRANTED_MODIFIERS;
    }
  }
}
