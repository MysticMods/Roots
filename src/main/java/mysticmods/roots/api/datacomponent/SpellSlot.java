package mysticmods.roots.api.datacomponent;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import mysticmods.roots.api.modifier.SpellModifier;
import mysticmods.roots.api.modifier.SpellModifierSet;
import mysticmods.roots.api.registry.ICosted;
import mysticmods.roots.api.registry.RootsRegistries;
import mysticmods.roots.api.spell.ISpellInstance;
import mysticmods.roots.api.spell.Spell;
import mysticmods.roots.api.spell.SpellInstanceData;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

// TODO: `enabledModifiers` should be immutable
public record SpellSlot(UUID spellId, int slot, Spell spell, SpellModifierSet enabledModifiers,
                        SpellInstanceData data) implements ISpellInstance {
  public static MapCodec<SpellSlot> MAP_CODEC = RecordCodecBuilder.<SpellSlot>mapCodec(instance -> instance.group(
      UUIDUtil.CODEC.fieldOf("spellId").forGetter(SpellSlot::spellId),
      Codec.INT.fieldOf("slot").forGetter(SpellSlot::slot),
      RootsRegistries.SPELLS.byNameCodec().fieldOf("spell").forGetter(SpellSlot::spell),
      SpellModifierSet.CODEC.fieldOf("enabledModifiers").forGetter(SpellSlot::enabledModifiers),
      SpellInstanceData.CODEC.fieldOf("data").forGetter(SpellSlot::data)
  ).apply(instance, SpellSlot::new)).validate(result -> {
    // TODO: What validation needed to take place here?
    return DataResult.success(result);
  });
  public static StreamCodec<RegistryFriendlyByteBuf, SpellSlot> STREAM_CODEC = StreamCodec.composite(
      UUIDUtil.STREAM_CODEC, SpellSlot::spellId,
      ByteBufCodecs.VAR_INT, SpellSlot::slot,
      ByteBufCodecs.registry(RootsRegistries.Keys.SPELLS), SpellSlot::spell,
      SpellModifierSet.STREAM_CODEC, SpellSlot::enabledModifiers,
      SpellInstanceData.STREAM_CODEC, SpellSlot::data,
      SpellSlot::new
  );
  public static Codec<SpellSlot> CODEC = MAP_CODEC.codec();

  public SpellSlot(UUID spellId, int slot, Spell spell, SpellModifierSet enabledModifiers) {
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
  public SpellModifierSet getEnabledModifiers() {
    return enabledModifiers();
  }

  @Override
  public Set<ICosted> getChildren() {
    return getEnabledModifiers().stream().map(o -> (ICosted) o).collect(Collectors.toSet());
  }

  @Override
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
    return new SpellSlot(spellId, slot, spell, enabledModifiers.without(modifier), data);
  }

  public SpellSlot withModifier(SpellModifier modifier) {
    if (hasModifier(modifier)) {
      return copy();
    }
    return new SpellSlot(spellId, slot, spell, enabledModifiers.with(modifier), data);
  }

  public SpellSlot withSlot(int slot) {
    return new SpellSlot(spellId, slot, spell, enabledModifiers, data);
  }

  public SpellSlot copy() {
    return new SpellSlot(spellId, slot, spell, enabledModifiers, data);
  }
}
