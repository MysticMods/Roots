package mysticmods.roots.api.datacomponent;

import com.google.common.collect.Sets;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.modifier.SpellModifier;
import mysticmods.roots.api.modifier.SpellModifierSet;
import mysticmods.roots.api.spell.Cycling;
import mysticmods.roots.api.spell.Spell;
import mysticmods.roots.api.spell.SpellInstanceData;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Supplier;

public record SpellStorage(int currentSlot, int maxSlot, List<SpellSlot> slots) {
  private static final List<SpellSlot> EMPTY_SLOTS = new ArrayList<>(Arrays.asList(new SpellSlot[]{null, null, null, null, null}));
  public static final Supplier<SpellStorage> EMPTY = () -> new SpellStorage(0, 5, new ArrayList<>(EMPTY_SLOTS));

  public static MapCodec<SpellStorage> MAP_CODEC = RecordCodecBuilder.mapCodec(
      instance -> instance.group(
          Codec.INT.fieldOf("currentSlot").forGetter(SpellStorage::currentSlot),
          Codec.INT.fieldOf("maxSlots").forGetter(SpellStorage::maxSlot),
          SpellSlot.CODEC.listOf().fieldOf("slots").forGetter(o -> o.slots.stream().filter(Objects::nonNull).toList())
      ).apply(instance, SpellStorage::new)
  );
  public static Codec<SpellStorage> CODEC = MAP_CODEC.codec();
  public static StreamCodec<RegistryFriendlyByteBuf, SpellStorage> STREAM_CODEC = StreamCodec.composite(
      ByteBufCodecs.VAR_INT, SpellStorage::currentSlot,
      ByteBufCodecs.VAR_INT, SpellStorage::maxSlot,
      SpellSlot.STREAM_CODEC.apply(ByteBufCodecs.list()), o -> o.slots.stream().filter(Objects::nonNull).toList(),
      SpellStorage::new
  );

  public SpellStorage(int currentSlot, int maxSlot, List<SpellSlot> slots) {
    this.currentSlot = currentSlot;
    this.maxSlot = maxSlot;
    List<SpellSlot> result;
    if (slots.size() == maxSlot) {
      result = slots;
    } else {
      if (slots.size() > maxSlot) {
        throw new IllegalStateException("Too many slots!");
      }
      List<SpellSlot> newSlots = new ArrayList<>(EMPTY_SLOTS);
      for (SpellSlot slot : slots) {
        if (slot != null) {
          newSlots.set(slot.slot(), slot);
        }
      }
      result = newSlots;
    }
    this.slots = result;
  }

  private boolean validateSlot(int slot) {
    if (slot < 0 || slot >= slots.size()) {
      RootsAPI.LOG.error("Invalid slot: {}", slot);
      return false;
    }
    return true;
  }

  @Nullable
  public SpellSlot getCurrentSpell() {
    return slots.get(currentSlot);
  }

  @Nullable
  public SpellSlot getSpell(int slot) {
    if (!validateSlot(slot)) {
      return null;
    }

    return slots.get(slot);
  }

  public boolean isEmpty() {
    for (SpellSlot slot : slots) {
      if (slot != null) {
        return false;
      }
    }
    return true;
  }

  public int size() {
    return slots.size();
  }

  // Will only be used by command, in theory?
  public SpellStorage setSpell(int slot, Spell spell, Set<SpellModifier> modifiers) {
    if (!validateSlot(slot)) {
      return this;
    }

    List<SpellSlot> newSlots = new ArrayList<>(slots);
    if (newSlots.get(slot) != null) {
      var existing = newSlots.get(slot);
      if (existing.spell() == spell && Sets.symmetricDifference(existing.enabledModifiers(), modifiers).isEmpty()) {
        return this;
      }
    }
    newSlots.set(slot, new SpellSlot(UUID.randomUUID(), slot, spell, new SpellModifierSet(modifiers)));
    return new SpellStorage(currentSlot, maxSlot, newSlots);
  }

  public SpellStorage setSpell(int slot, SpellSlot spell) {
    if (!validateSlot(slot)) {
      return this;
    }

    List<SpellSlot> newSlots = new ArrayList<>(slots);
    if (spell.equals(newSlots.get(slot))) {
      return this;
    }
    newSlots.set(slot, spell);
    return new SpellStorage(currentSlot, maxSlot, newSlots);
  }

  // This function is what's used by the network packet to set the spell
  public SpellStorage setSpell(int slot, Spell spell) {
    if (!validateSlot(slot)) {
      return this;
    }

    List<SpellSlot> newSlots = new ArrayList<>(slots);
    if (newSlots.get(slot) != null) {
      if (newSlots.get(slot).spell() == spell) {
        return this;
      }
    }

    newSlots.set(slot, new SpellSlot(UUID.randomUUID(), slot, spell, SpellModifierSet.EMPTY));
    return new SpellStorage(currentSlot, maxSlot, newSlots);
  }

  public SpellStorage clearSpell(int slot) {
    if (!validateSlot(slot)) {
      return this;
    }

    List<SpellSlot> newSlots = new ArrayList<>(slots);
    newSlots.set(slot, null);
    return new SpellStorage(currentSlot, maxSlot, newSlots);
  }

  public SpellStorage swapSlots(int slot1, int slot2) {
    if (!validateSlot(slot1) || !validateSlot(slot2)) {
      return this;
    }

    List<SpellSlot> newSlots = new ArrayList<>(slots);
    SpellSlot temp1 = newSlots.get(slot1);
    if (temp1 != null) {
      temp1 = temp1.withSlot(slot2);
    }
    SpellSlot temp2 = newSlots.get(slot2);
    if (temp2 != null) {
      temp2 = temp2.withSlot(slot1);
    }
    newSlots.set(slot1, temp2);
    newSlots.set(slot2, temp1);
    return new SpellStorage(currentSlot, maxSlot, newSlots);
  }

  public SpellStorage setCurrentSlot(int slot) {
    if (slot < 0 || slot >= maxSlot || slot == currentSlot) {
      return this;
    }
    return new SpellStorage(slot, maxSlot, slots);
  }

  public <T> SpellStorage setData(int slot, DataComponentType<T> component, @Nullable T value) {
    if (slot < 0 || slot >= maxSlot) {
      return this;
    }


    SpellSlot slotData = slots.get(slot);
    if (slotData == null) {
      return this;
    }

    List<SpellSlot> newSlots = new ArrayList<>(slots);
    newSlots.set(slot, slotData.withData(component, value));
    return new SpellStorage(currentSlot, maxSlot, newSlots);
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;

    SpellStorage that = (SpellStorage) o;
    return maxSlot == that.maxSlot && currentSlot == that.currentSlot && slots.equals(that.slots);
  }

  @Override
  public int hashCode() {
    int result = currentSlot;
    result = 31 * result + maxSlot;
    result = 31 * result + slots.hashCode();
    return result;
  }

  public List<SpellSlot> getSpells() {
    return slots;
  }

}
