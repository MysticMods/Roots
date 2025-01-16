package mysticmods.roots.api.datacomponent;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.registry.RootsRegistries;
import mysticmods.roots.api.spell.ISpellInstance;
import mysticmods.roots.api.spell.Spell;
import mysticmods.roots.api.spell.SpellModifier;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public record SpellStorage(ImmutableList<SpellSlot> slots) implements TooltipComponent {
  public static final SpellStorage EMPTY = new SpellStorage(ImmutableList.of());

  public static MapCodec<SpellStorage> MAP_CODEC = RecordCodecBuilder.mapCodec(
      instance -> instance.group(
          SpellSlot.CODEC.listOf().xmap(ImmutableList::copyOf, ArrayList::new).fieldOf("slots").forGetter(SpellStorage::slots)
      ).apply(instance, SpellStorage::new)
  );
  public static Codec<SpellStorage> CODEC = MAP_CODEC.codec();
  public static StreamCodec<RegistryFriendlyByteBuf, SpellStorage> STREAM_CODEC = StreamCodec.composite(
      SpellSlot.STREAM_CODEC.apply(ByteBufCodecs.list()).map(ImmutableList::copyOf, ArrayList::new), SpellStorage::slots,
      SpellStorage::new
  );

  public SpellStorage(int maxSlots, List<SpellSlot> slots) {
    this(ImmutableList.copyOf(pad(maxSlots, slots)));
  }

  public SpellStorage (int maxSlots) {
    this(maxSlots, new ArrayList<>());
  }

  private static List<SpellSlot> pad (int maxSlots, List<SpellSlot> slots) {
    if (slots.size() == maxSlots) {
      return slots;
    }
    if (slots.size() > maxSlots) {
      throw new IllegalStateException("Too many slots!");
    }
    List<SpellSlot> padded;
    if (slots instanceof ImmutableCollection) {
      padded = new ArrayList<>(slots);
    } else {
      padded = slots;
    }
    while (padded.size() < maxSlots) {
      padded.add(null);
    }
    return padded;
  }

  private boolean validateSlot (int slot) {
    if (slot < 0 || slot >= slots.size()) {
      RootsAPI.LOG.error("Invalid slot: {}", slot);
      return false;
    }
    return true;
  }

  @Nullable
  public SpellSlot getSpell (int slot) {
    if (!validateSlot(slot)) {
      return null;
    }

    return slots.get(slot);
  }

  public boolean isEmpty () {
    if (this == EMPTY) {
      return true;
    }
    for (SpellSlot slot : slots) {
      if (slot != null) {
        return false;
      }
    }
    return true;
  }

  public int size () {
    return slots.size();
  }

  public SpellStorage setSpell (int slot, Spell spell) {
    if (!validateSlot(slot)) {
      // TODO:
      return this;
    }

    List<SpellSlot> newSlots = new ArrayList<>(slots);
    newSlots.set(slot, new SpellSlot(slot, spell, ImmutableSet.of()));
    return new SpellStorage(ImmutableList.copyOf(newSlots));
  }

  public SpellStorage clearSpell (int slot) {
    if (!validateSlot(slot)) {
      return this;
    }

    List<SpellSlot> newSlots = new ArrayList<>(slots);
    newSlots.set(slot, null);
    return new SpellStorage(ImmutableList.copyOf(newSlots));
  }

  public SpellStorage swapSlots (int slot1, int slot2) {
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
    return new SpellStorage(ImmutableList.copyOf(newSlots));
  }

  public boolean setCooldown (int slot, long cooldown) {
    if (!validateSlot(slot)) {
      return false;
    }

    List<SpellSlot> newSlots = new ArrayList<>(slots);
    SpellSlot slotData = newSlots.get(slot);
    if (slotData == null) {
      return false;
    }
    newSlots.set(slot, slotData.withCooldown(cooldown));
    return true;
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;

    SpellStorage that = (SpellStorage) o;
    return slots.equals(that.slots);
  }

  @Override
  public int hashCode() {
    return slots.hashCode();
  }

  public record SpellSlot(int slot, Spell spell, ImmutableSet<SpellModifier> enabledModifiers,
                          long cooldown) implements ISpellInstance {
    public static MapCodec<SpellSlot> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        Codec.INT.fieldOf("slot").forGetter(SpellSlot::slot),
        RootsRegistries.SPELLS.byNameCodec().fieldOf("spell").forGetter(SpellSlot::spell),
        RootsRegistries.SPELL_MODIFIERS.byNameCodec().listOf().xmap(ImmutableSet::copyOf, ArrayList::new).fieldOf("enabledModifiers").forGetter(SpellSlot::enabledModifiers),
        Codec.LONG.fieldOf("cooldown").forGetter(SpellSlot::cooldown)
    ).apply(instance, SpellSlot::new));
    public static StreamCodec<RegistryFriendlyByteBuf, SpellSlot> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.VAR_INT, SpellSlot::slot,
        ByteBufCodecs.registry(RootsRegistries.Keys.SPELLS), SpellSlot::spell,
        ByteBufCodecs.registry(RootsRegistries.Keys.SPELL_MODIFIERS).apply(ByteBufCodecs.list()).map(ImmutableSet::copyOf, ArrayList::new), SpellSlot::enabledModifiers,
        ByteBufCodecs.VAR_LONG, SpellSlot::cooldown,
        SpellSlot::new
    );
    public static Codec<SpellSlot> CODEC = MAP_CODEC.codec();

    public SpellSlot(int slot, Spell spell, ImmutableSet<SpellModifier> enabledModifiers) {
      this(slot, spell, enabledModifiers, 0);
    }

    @Override
    public Spell getSpell() {
      return spell();
    }

    @Override
    public Set<SpellModifier> getEnabledModifiers() {
      return enabledModifiers();
    }

    @Override
    public long getCooldown() {
      return cooldown();
    }

    public boolean hasModifier(SpellModifier modifier) {
      return enabledModifiers.contains(modifier);
    }

    public SpellSlot withoutModifier(SpellModifier modifier) {
      if (!hasModifier(modifier)) {
        return copy();
      }
      Set<SpellModifier> modifiers = new HashSet<>(enabledModifiers);
      modifiers.remove(modifier);
      return new SpellSlot(slot, spell, ImmutableSet.copyOf(modifiers), cooldown);
    }

    public SpellSlot withModifier(SpellModifier modifier) {
      if (hasModifier(modifier)) {
        return copy();
      }
      Set<SpellModifier> modifiers = new HashSet<>(enabledModifiers);
      modifiers.add(modifier);
      return new SpellSlot(slot, spell, ImmutableSet.copyOf(modifiers), cooldown);
    }

    public SpellSlot withCooldown(long cooldown) {
      return new SpellSlot(slot, spell, ImmutableSet.copyOf(enabledModifiers), cooldown);
    }

    public SpellSlot withSlot (int slot) {
      return new SpellSlot(slot, spell, ImmutableSet.copyOf(enabledModifiers), cooldown);
    }

    public SpellSlot copy() {
      return new SpellSlot(slot, spell, ImmutableSet.copyOf(enabledModifiers), cooldown);
    }

    @Override
    public boolean equals(Object o) {
      if (o == null || getClass() != o.getClass()) return false;

      SpellSlot spellSlot = (SpellSlot) o;
      return slot == spellSlot.slot && cooldown == spellSlot.cooldown && Objects.equals(spell, spellSlot.spell) && Objects.equals(enabledModifiers, spellSlot.enabledModifiers);
    }

    @Override
    public int hashCode() {
      int result = slot;
      result = 31 * result + Objects.hashCode(spell);
      result = 31 * result + Objects.hashCode(enabledModifiers);
      result = 31 * result + Long.hashCode(cooldown);
      return result;
    }
  }
}
