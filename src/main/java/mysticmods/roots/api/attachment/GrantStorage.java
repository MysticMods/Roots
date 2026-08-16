package mysticmods.roots.api.attachment;

import com.google.common.collect.Sets;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import mysticmods.roots.action.LearnSpellAction;
import mysticmods.roots.action.LearnSpellModifierAction;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.modifier.SpellModifier;
import mysticmods.roots.api.registry.IDescribed;
import mysticmods.roots.api.registry.RootsRegistries;
import mysticmods.roots.api.spell.ISpellInstance;
import mysticmods.roots.api.spell.LibrarySpell;
import mysticmods.roots.api.spell.Spell;
import mysticmods.roots.init.ModActions;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class GrantStorage implements ICleanable {
  public static final MapCodec<GrantStorage> MAP_CODEC = RecordCodecBuilder.mapCodec(
      instance -> instance.group(
          RootsRegistries.SPELLS.byNameCodec().listOf().fieldOf("grantedSpells")
              .forGetter(o -> new ArrayList<>(o.grantedSpells)),
          RootsRegistries.SPELL_MODIFIERS.byNameCodec().listOf().fieldOf("grantedModifiers")
              .forGetter(o -> new ArrayList<>(o.grantedSpellModifiers))).apply(instance, GrantStorage::new));
  public static final Codec<GrantStorage> CODEC = MAP_CODEC.codec();
  public static final StreamCodec<RegistryFriendlyByteBuf, GrantStorage> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.registry(RootsRegistries.Keys.SPELLS)
      .apply(ByteBufCodecs.list()), o -> List.copyOf(o.grantedSpells), ByteBufCodecs.registry(RootsRegistries.Keys.SPELL_MODIFIERS)
      .apply(ByteBufCodecs.list()), o -> List.copyOf(o.grantedSpellModifiers), GrantStorage::new);

  private boolean dirty = true;
  private final Set<Spell> grantedSpells;
  private final Set<SpellModifier> grantedSpellModifiers;

  private List<LibrarySpell> librarySpells = null;

  public GrantStorage() {
    grantedSpells = new ObjectOpenHashSet<>();
    grantedSpellModifiers = new ObjectOpenHashSet<>();
  }

  public GrantStorage(Set<Spell> grantedSpells, Set<SpellModifier> grantedSpellModifiers) {
    this.grantedSpells = new ObjectOpenHashSet<>(grantedSpells);
    this.grantedSpellModifiers = new ObjectOpenHashSet<>(grantedSpellModifiers);
  }

  public GrantStorage(List<Spell> spells, List<SpellModifier> spellModifiers) {
    this.grantedSpells = new ObjectOpenHashSet<>(spells);
    this.grantedSpellModifiers = new ObjectOpenHashSet<>(spellModifiers);
  }

  public boolean hasSpell(Spell spell) {
    return grantedSpells.contains(spell);
  }

  public boolean hasSpellModifier(SpellModifier modifier) {
    return !(modifier.is(RootsTags.SpellModifiers.REQUIRES_UNLOCK) && grantedSpellModifiers.contains(modifier));
  }

  public boolean canUnlock(Unlock<?> unlock) {
    if (unlock instanceof Unlock.SpellUnlock(Holder<Spell> value)) {
      if (value.is(RootsTags.Spells.INVALID)) {
        return false;
      }
      return !hasSpell(value.value());
    } else if (unlock instanceof Unlock.SpellModifierUnlock(Holder<SpellModifier> value)) {
      return !hasSpellModifier(value.value());
    }

    return false;
  }

  public boolean clearSpells(ServerPlayer player) {
    if (grantedSpells.isEmpty()) {
      return false;
    }

    librarySpells = null;
    grantedSpells.clear();
    setDirty(true);
    return true;
  }

  public boolean clearSpellModifiers(ServerPlayer player) {
    if (grantedSpellModifiers.isEmpty()) {
      return false;
    }

    grantedSpellModifiers.clear();
    setDirty(true);
    return true;
  }

  public void unlock(ServerPlayer player, Unlock<?> unlock) {
    if (unlock instanceof Unlock.SpellUnlock(Holder<Spell> value)) {
      if (value.is(RootsTags.Spells.INVALID)) {
        return;
      }
      Spell spell = value.value();
      unlockSpell(player, spell);
    } else if (unlock instanceof Unlock.SpellModifierUnlock(Holder<SpellModifier> value)) {
      SpellModifier modifier = value.value();
      unlockSpellModifier(player, modifier);
    }
  }

  private void unlockSpell(ServerPlayer player, Spell spell) {
    if (spell.is(RootsTags.Spells.INVALID)) {
      return;
    }
    if (grantedSpells.add(spell)) {
      librarySpells = null;
      setDirty(true);
      player.displayClientMessage(Component.translatable("roots.message.spell.learned", spell.getStyledName()), true);
      LearnSpellAction.Context context = new LearnSpellAction.Context(player.serverLevel(), player, ISpellInstance.of(spell));
      ModActions.LEARN_SPELL.get().accept(context);
    }
  }

  private void unlockSpellModifier(ServerPlayer player, SpellModifier modifier) {
    if (!modifier.is(RootsTags.SpellModifiers.REQUIRES_UNLOCK)) {
      RootsAPI.LOG.error("Attempted to unlock modifier {} for {}, despite modifier not being tagged `requires_unlock`", modifier.builtInRegistryHolder().getKey(), player);
      return;
    }
    if (grantedSpellModifiers.add(modifier)) {
      setDirty(true);
      player.displayClientMessage(Component.translatable("roots.message.modifier.learned", modifier.getName()), true);
      LearnSpellModifierAction.Context context = new LearnSpellModifierAction.Context(player.serverLevel(), player, modifier);
      ModActions.LEARN_SPELL_MODIFIER.get().accept(context);
    }
  }

  public void removeSpell(ServerPlayer player, Spell spell) {
    if (grantedSpells.remove(spell)) {
      librarySpells = null;
      setDirty(true);
    }
  }

  public void removeSpellModifier(SpellModifier modifier) {
    if (grantedSpellModifiers.remove(modifier)) {
      setDirty(true);
    }
  }

  public Set<Spell> getSpells() {
    return grantedSpells;
  }

  public Set<SpellModifier> getSpellModifiers() {
    return grantedSpellModifiers;
  }

  public List<LibrarySpell> getLibrarySpells() {
    if (librarySpells == null || librarySpells.isEmpty()) {
      librarySpells = RootsRegistries.SPELLS.stream().filter(o -> !o.is(RootsTags.Spells.INVALID)).sorted(Comparator.comparing(IDescribed::getDescriptionId))
          .map(o -> new LibrarySpell(o.builtInRegistryHolder(), grantedSpells.contains(o)))
          .sorted(Comparator.comparing(LibrarySpell::granted).reversed()).toList();
    }
    return librarySpells;
  }

  @Override
  public boolean isEmpty() {
    return grantedSpells.isEmpty() && grantedSpellModifiers.isEmpty();
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

    return dirty == that.dirty && grantedSpells.equals(that.grantedSpells) && grantedSpellModifiers.equals(that.grantedSpellModifiers);
  }

  @Override
  public int hashCode() {
    int result = Boolean.hashCode(dirty);
    result = 31 * result + grantedSpells.hashCode();
    result = 31 * result + grantedSpellModifiers.hashCode();
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
