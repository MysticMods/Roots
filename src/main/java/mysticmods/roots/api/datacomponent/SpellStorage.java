package mysticmods.roots.api.datacomponent;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mysticmods.roots.api.registry.RootsRegistries;
import mysticmods.roots.api.spell.ISpellInstance;
import mysticmods.roots.api.spell.Spell;
import mysticmods.roots.api.spell.SpellModifier;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public record SpellStorage(int selectedSlot, int maxSlots, ImmutableList<SpellSlot> slots) {
  public static MapCodec<SpellStorage> MAP_CODEC = RecordCodecBuilder.mapCodec(
      instance -> instance.group(
          Codec.INT.fieldOf("selectedSlot").forGetter(SpellStorage::selectedSlot),
          Codec.INT.fieldOf("maxSlots").forGetter(SpellStorage::maxSlots),
          SpellSlot.CODEC.listOf().xmap(ImmutableList::copyOf, ArrayList::new).fieldOf("slots").forGetter(SpellStorage::slots)
      ).apply(instance, SpellStorage::new)
  );
  public static Codec<SpellStorage> CODEC = MAP_CODEC.codec();
  public static StreamCodec<RegistryFriendlyByteBuf, SpellStorage> STREAM_CODEC = StreamCodec.composite(
      ByteBufCodecs.VAR_INT, SpellStorage::selectedSlot,
      ByteBufCodecs.VAR_INT, SpellStorage::maxSlots,
      SpellSlot.STREAM_CODEC.apply(ByteBufCodecs.list()).map(ImmutableList::copyOf, ArrayList::new), SpellStorage::slots,
      SpellStorage::new
  );

  public SpellStorage(int selectedSlot, int maxSlots, List<SpellSlot> slots) {
    this(selectedSlot, maxSlots, ImmutableList.copyOf(pad(maxSlots, slots)));
  }

  public SpellStorage (int maxSlots) {
    this(0, maxSlots, pad(maxSlots));
  }

  private static List<SpellSlot> pad (int maxSlots, List<SpellSlot> slots) {
    if (slots.size() >= maxSlots) {
      return slots;
    }
    List<SpellSlot> padded = new ArrayList<>(slots);
    while (padded.size() < maxSlots) {
      padded.add(null);
    }
    return padded;
  }

  private static List<SpellSlot> pad (int maxSlots) {
    List<SpellSlot> padded = new ArrayList<>();
    for (int i = 0; i < maxSlots; i++) {
      padded.add(null);
    }
    return padded;
  }

  // advance slot
  // advance slot backwards
  // get spell in slot
  // add spell to slot
  // add modifier to spell
  // get cooldown of spell
  // set cooldown of spell

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

    public SpellSlot withCooldown(int cooldown) {
      return new SpellSlot(slot, spell, ImmutableSet.copyOf(enabledModifiers), cooldown);
    }

    public SpellSlot copy() {
      return new SpellSlot(slot, spell, ImmutableSet.copyOf(enabledModifiers), cooldown);
    }
  }
}
