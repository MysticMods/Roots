package mysticmods.roots.api.attachment;

import com.google.common.collect.Sets;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet;
import mysticmods.roots.action.LearnSpellAction;
import mysticmods.roots.action.LearnSpellModifierAction;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.modifier.Modifier;
import mysticmods.roots.api.registry.IDescribed;
import mysticmods.roots.api.registry.RootsRegistries;
import mysticmods.roots.api.spell.*;
import mysticmods.roots.init.ModActions;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;

import java.util.*;

public class GrantStorage implements ICleanable {
  public static final MapCodec<GrantStorage> MAP_CODEC = RecordCodecBuilder.mapCodec(
      instance -> instance.group(
          RootsRegistries.SPELLS.byNameCodec().listOf().fieldOf("grantedSpells")
              .forGetter(o -> new ArrayList<>(o.grantedSpells)),
          RootsRegistries.SPELL_MODIFIERS.byNameCodec().listOf().fieldOf("grantedModifiers")
              .forGetter(o -> new ArrayList<>(o.grantedModifiers))).apply(instance, GrantStorage::new));
  public static final Codec<GrantStorage> CODEC = MAP_CODEC.codec();
  public static final StreamCodec<RegistryFriendlyByteBuf, GrantStorage> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.registry(RootsRegistries.Keys.SPELLS)
      .apply(ByteBufCodecs.list()), o -> List.copyOf(o.grantedSpells), ByteBufCodecs.registry(RootsRegistries.Keys.SPELL_MODIFIERS)
      .apply(ByteBufCodecs.list()), o -> List.copyOf(o.grantedModifiers), GrantStorage::new);

  private boolean dirty = true;
  private final Set<Spell> grantedSpells;
  private final Set<Modifier> grantedModifiers;

  private List<LibrarySpell> librarySpells = null;
  private Map<Spell, List<LibraryModifier>> libraryModifiers = null;

  public GrantStorage() {
    grantedSpells = new ObjectLinkedOpenHashSet<>();
    grantedModifiers = new ObjectLinkedOpenHashSet<>();
  }

  public GrantStorage(Set<Spell> grantedSpells, Set<Modifier> grantedModifiers) {
    this.grantedSpells = new ObjectLinkedOpenHashSet<>(grantedSpells);
    this.grantedModifiers = new ObjectLinkedOpenHashSet<>(grantedModifiers);
  }

  public GrantStorage(List<Spell> spells, List<Modifier> spellModifiers) {
    this.grantedSpells = new ObjectLinkedOpenHashSet<>(spells);
    this.grantedModifiers = new ObjectLinkedOpenHashSet<>(spellModifiers);
  }

  public boolean hasSpell(Spell spell) {
    return grantedSpells.contains(spell);
  }

  public boolean hasModifier(Modifier modifier) {
    return grantedModifiers.contains(modifier);
  }

  public boolean canUnlock(Unlock<?> unlock) {
    if (unlock instanceof Unlock.SpellUnlock(Holder<Spell> value)) {
      return !grantedSpells.contains(value.value());
    } else if (unlock instanceof Unlock.ModifierUnlock(Holder<Modifier> value)) {
      return !grantedModifiers.contains(value.value());
    }

    return false;
  }

  public boolean clearSpells(ServerPlayer player) {
    if (grantedSpells.isEmpty()) {
      return false;
    }

    grantedSpells.clear();
    setDirty(true);
    return true;
  }

  public boolean clearModifiers(ServerPlayer player) {
    if (grantedModifiers.isEmpty()) {
      return false;
    }

    grantedModifiers.clear();
    setDirty(true);
    return true;
  }

  public boolean unlock(ServerPlayer player, Unlock<?> unlock) {
    if (unlock instanceof Unlock.SpellUnlock(Holder<Spell> value)) {
      Spell spell = value.value();
      unlockSpell(player, spell);
    } else if (unlock instanceof Unlock.ModifierUnlock(Holder<Modifier> value)) {
      Modifier modifier = value.value();
      unlockModifier(player, modifier);
    }

    return false;
  }

  private void unlockSpell(ServerPlayer player, Spell spell) {
    if (grantedSpells.add(spell)) {
      setDirty(true);
      player.displayClientMessage(Component.translatable("roots.message.spell.learned", spell.getStyledName()), true);
      LearnSpellAction.Context context = new LearnSpellAction.Context(player.serverLevel(), player, ISpellInstance.of(spell));
      ModActions.LEARN_SPELL.get().accept(context);
    }
  }

  private void unlockModifier(ServerPlayer player, Modifier modifier) {
    if (grantedModifiers.add(modifier)) {
      setDirty(true);
      player.displayClientMessage(Component.translatable("roots.message.modifier.learned", modifier.getName()), true);
      LearnSpellModifierAction.Context context = new LearnSpellModifierAction.Context(player.serverLevel(), player, modifier);
      ModActions.LEARN_SPELL_MODIFIER.get().accept(context);
    }
  }

  public void removeSpell(ServerPlayer player, Spell spell) {
    if (grantedSpells.remove(spell)) {
      setDirty(true);
    }
  }

  public void removeModifier(SpellModifier modifier) {
    if (grantedModifiers.remove(modifier)) {
      setDirty(true);
    }
  }

  public Set<Spell> getSpells() {
    return grantedSpells;
  }

  public Set<SpellModifier> getModifiers() {
    return grantedModifiers;
  }

  public List<LibrarySpell> getLibrarySpells() {
    if (librarySpells == null) {
      librarySpells = new ArrayList<>();
      grantedSpells.stream().sorted(Comparator.comparing(IDescribed::getDescriptionId))
          .forEach(o -> librarySpells.add(new LibrarySpell(o.builtInRegistryHolder(), true)));
      RootsRegistries.SPELLS.stream().filter(o -> !grantedSpells.contains(o))
          .sorted(Comparator.comparing(IDescribed::getDescriptionId))
          .forEach(o -> librarySpells.add(new LibrarySpell(o.builtInRegistryHolder(), false)));
    }
    return librarySpells;
  }

  public List<LibraryModifier> getLibraryModifiers(Spell checkSpell) {
    if (libraryModifiers == null) {
      libraryModifiers = new Object2ObjectLinkedOpenHashMap<>();
    }
    return libraryModifiers.computeIfAbsent(checkSpell, spell -> {
      List<LibraryModifier> result = new ArrayList<>();
      for (SpellModifier mod : spell.getModifiers()) {
        result.add(new LibraryModifier(mod.builtInRegistryHolder(), grantedModifiers.contains(mod)));
      }
      result.sort(Comparator.comparing(LibraryModifier::enabled));
      return result;
    });
  }

  @Override
  public boolean isEmpty() {
    return grantedSpells.isEmpty() && grantedModifiers.isEmpty();
  }

  @Override
  public void setDirty(boolean dirty) {
    this.dirty = dirty;
  }

  @Override
  public boolean isDirty() {
    return this.dirty;
  }

  @Override
  public final boolean equals(Object o) {
    if (!(o instanceof GrantStorage that)) return false;

    return dirty == that.dirty && grantedSpells.equals(that.grantedSpells) && grantedModifiers.equals(that.grantedModifiers);
  }

  @Override
  public int hashCode() {
    int result = Boolean.hashCode(dirty);
    result = 31 * result + grantedSpells.hashCode();
    result = 31 * result + grantedModifiers.hashCode();
    return result;
  }

  public void difference(GrantStorage otherStorage) {
    Set<Spell> newOther = Sets.difference(otherStorage.grantedSpells, grantedSpells);
    if (!newOther.isEmpty()) {
      RootsAPI.LOG.error("The following spells are contained in `otherStorage` but not this and will be added:");
      for (Spell spell : newOther) {
        RootsAPI.LOG.error("- {}", spell);
      }
    }
    Set<Spell> removing = Sets.difference(grantedSpells, otherStorage.grantedSpells);
    if (!removing.isEmpty()) {
      RootsAPI.LOG.error("The following spells are contained in this but not `otherStorage` and will be removed:");
      for (Spell spell : removing) {
        RootsAPI.LOG.error("- {}", spell);
      }
    }
  }
}
