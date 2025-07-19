package mysticmods.roots.api.datacomponent;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.registry.ICosted;
import mysticmods.roots.api.registry.RootsRegistries;
import mysticmods.roots.api.spell.ISpellInstance;
import mysticmods.roots.api.spell.Spell;
import mysticmods.roots.api.spell.SpellInstanceData;
import mysticmods.roots.api.spell.SpellModifier;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

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

  // This function is only used internally by the command
  public SpellStorage setSpell(int slot, Spell spell, Set<SpellModifier> modifiers) {
    if (!validateSlot(slot)) {
      return this;
    }

    List<SpellSlot> newSlots = new ArrayList<>(slots);
    newSlots.set(slot, new SpellSlot(UUID.randomUUID(), slot, spell, modifiers));
    return new SpellStorage(currentSlot, maxSlot, newSlots);
  }

  // This function is what's used by the network packet to set the spell
  public SpellStorage setSpell(int slot, Spell spell) {
    if (!validateSlot(slot)) {
      return this;
    }

    List<SpellSlot> newSlots = new ArrayList<>(slots);
    newSlots.set(slot, new SpellSlot(UUID.randomUUID(), slot, spell, Set.of()));
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

  @Deprecated
  public void setCooldown(Entity entity, int slot, int cooldown) {
    if (!validateSlot(slot)) {
      return;
    }

    SpellSlot slotData = slots.get(slot);

    if (slotData == null) {
      return;
    }

    return;
/*    List<SpellSlot> newSlots = new ArrayList<>(slots);
    newSlots.set(slot, slotData.withCooldown(cooldown, cooldown));
    return new SpellStorage(currentSlot, maxSlot, newSlots);*/
  }

  public SpellStorage setCurrentSlot(int slot) {
    if (slot < 0 || slot >= maxSlot || slot == currentSlot) {
      return this;
    }
    return new SpellStorage(slot, maxSlot, slots);
  }

  @Deprecated
  public SpellStorage setData(int slot, IntArrayList data) {
    if (slot < 0 || slot >= maxSlot) {
      return this;
    }

    SpellSlot slotData = slots.get(slot);
    if (slotData == null) {
      return this;
    }

    SpellInstanceData currentData = slotData.getSpellData();
    if (currentData != null && currentData.data().equals(data)) {
      return this;
    }

    List<SpellSlot> newSlots = new ArrayList<>(slots);
    newSlots.set(slot, slotData.withData(new SpellInstanceData(data)));
    return new SpellStorage(currentSlot, maxSlot, newSlots);
  }

  public SpellStorage setData(int slot, int index, int value) {
    if (slot < 0 || slot >= maxSlot) {
      return this;
    }


    SpellSlot slotData = slots.get(slot);
    if (slotData == null) {
      return this;
    }

    SpellInstanceData currentData = slotData.getSpellData();
    if (currentData != null && slotData.data().has(index) && slotData.data().get(index) == value) {
      return this;
    }

    List<SpellSlot> newSlots = new ArrayList<>(slots);
    newSlots.set(slot, slotData.withData(index, value));
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

  public record SpellSlot(UUID spellId, int slot, Spell spell, Set<SpellModifier> enabledModifiers,
                          SpellInstanceData data) implements ISpellInstance {
    public static MapCodec<SpellSlot> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        UUIDUtil.CODEC.fieldOf("spellId").forGetter(SpellSlot::spellId),
        Codec.INT.fieldOf("slot").forGetter(SpellSlot::slot),
        RootsRegistries.SPELLS.byNameCodec().fieldOf("spell").forGetter(SpellSlot::spell),
        RootsRegistries.SPELL_MODIFIERS.byNameCodec().listOf().xmap(HashSet::new, ArrayList::new)
            .fieldOf("enabledModifiers").forGetter(o -> new HashSet<>(o.enabledModifiers)),
        SpellInstanceData.CODEC.fieldOf("data").forGetter(o -> o.data)
    ).apply(instance, SpellSlot::new));
    public static StreamCodec<RegistryFriendlyByteBuf, SpellSlot> STREAM_CODEC = StreamCodec.composite(
        UUIDUtil.STREAM_CODEC, SpellSlot::spellId,
        ByteBufCodecs.VAR_INT, SpellSlot::slot,
        ByteBufCodecs.registry(RootsRegistries.Keys.SPELLS), SpellSlot::spell,
        ByteBufCodecs.collection(HashSet::new, ByteBufCodecs.registry(RootsRegistries.Keys.SPELL_MODIFIERS)), SpellSlot::enabledModifiers,
        SpellInstanceData.STREAM_CODEC, SpellSlot::data,
        SpellSlot::new
    );
    public static Codec<SpellSlot> CODEC = MAP_CODEC.codec();

    public SpellSlot(UUID spellId, int slot, Spell spell, Set<SpellModifier> enabledModifiers) {
      this(spellId, slot, spell, enabledModifiers, new SpellInstanceData(spell.getDataSlots() + 1));
    }

    @Override
    public @Nullable SpellInstanceData getSpellData() {
      return data();
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
    public Set<ICosted> getChildren() {
      return getEnabledModifiers().stream().map(o -> (ICosted) o).collect(Collectors.toSet());
    }

    public boolean hasModifier(SpellModifier modifier) {
      return enabledModifiers.contains(modifier);
    }

    public SpellSlot withData(int index, int value) {
      IntArrayList newData = new IntArrayList(data.data());

      newData.ensureCapacity(spell.getDataSlots() + 1);
      newData.set(index, value);
      return new SpellSlot(spellId, slot, spell, enabledModifiers, new SpellInstanceData(newData));
    }

    public SpellSlot withData(SpellInstanceData data) {
      if (data.equals(this.data)) {
        return this;
      }
      return new SpellSlot(spellId, slot, spell, enabledModifiers, data);
    }

    public SpellSlot withoutModifier(SpellModifier modifier) {
      if (!hasModifier(modifier)) {
        return copy();
      }
      Set<SpellModifier> modifiers = new HashSet<>(enabledModifiers);
      modifiers.remove(modifier);
      return new SpellSlot(spellId, slot, spell, modifiers, data);
    }

    public SpellSlot withModifier(SpellModifier modifier) {
      if (hasModifier(modifier)) {
        return copy();
      }
      Set<SpellModifier> modifiers = new HashSet<>(enabledModifiers);
      modifiers.add(modifier);
      return new SpellSlot(spellId, slot, spell, modifiers, data);
    }

    public SpellSlot withSlot(int slot) {
      return new SpellSlot(spellId, slot, spell, enabledModifiers, data);
    }

    public SpellSlot copy() {
      return new SpellSlot(spellId, slot, spell, enabledModifiers, data);
    }
  }
}
