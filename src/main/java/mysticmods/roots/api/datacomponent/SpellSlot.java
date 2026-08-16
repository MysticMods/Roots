package mysticmods.roots.api.datacomponent;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.modifier.ModifierTrees;
import mysticmods.roots.api.modifier.SpellModifier;
import mysticmods.roots.api.modifier.SpellModifierSet;
import mysticmods.roots.api.reference.Spells;
import mysticmods.roots.api.registry.ICostedChild;
import mysticmods.roots.api.registry.RootsRegistries;
import mysticmods.roots.api.spell.ISpellInstance;
import mysticmods.roots.api.spell.Spell;
import net.minecraft.core.UUIDUtil;
import net.minecraft.core.component.*;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import org.jetbrains.annotations.Nullable;

import java.util.Set;
import java.util.UUID;

public record SpellSlot(UUID spellId, int slot, Spell spell, SpellModifierSet enabledModifiers,
                        PatchedDataComponentMap data) implements ISpellInstance, DataComponentHolder {
  public static MapCodec<SpellSlot> MAP_CODEC = RecordCodecBuilder.<SpellSlot>mapCodec(instance -> instance.group(
      UUIDUtil.CODEC.fieldOf("spellId").forGetter(SpellSlot::spellId),
      Codec.INT.fieldOf("slot").forGetter(SpellSlot::slot),
      RootsRegistries.SPELLS.byNameCodec().validate(result -> {
        return DataResult.success(result);
      }).fieldOf("spell").forGetter(SpellSlot::spell),
      SpellModifierSet.CODEC.fieldOf("enabledModifiers").forGetter(SpellSlot::enabledModifiers),
      DataComponentPatch.CODEC.fieldOf("data")
          .forGetter(o -> o.data == null ? DataComponentPatch.EMPTY : o.data.asPatch())
  ).apply(instance, SpellSlot::new)).validate(result -> {
    // TODO: Improve migration system
    if (result.is(Spells.RAMPANT_GROWTH)) {
      // Migrate to growth infusion with modifier
      var newSpell = RootsRegistries.SPELLS.get(Spells.GROWTH_INFUSION);
      var rampantGrowth = RootsRegistries.SPELL_MODIFIERS.get(RootsAPI.rl("growth_infusion/rampant_growth"));
      return DataResult.success(new SpellSlot(result.spellId, result.slot, newSpell, new SpellModifierSet(rampantGrowth), DataComponentPatch.EMPTY));
    }
    // TODO: Validation: remove restricted modifiers
    return DataResult.success(result);
  });
  public static StreamCodec<RegistryFriendlyByteBuf, SpellSlot> STREAM_CODEC = StreamCodec.composite(
      UUIDUtil.STREAM_CODEC, SpellSlot::spellId,
      ByteBufCodecs.VAR_INT, SpellSlot::slot,
      ByteBufCodecs.registry(RootsRegistries.Keys.SPELLS), SpellSlot::spell,
      SpellModifierSet.STREAM_CODEC, SpellSlot::enabledModifiers,
      DataComponentPatch.STREAM_CODEC, o -> o.data == null ? DataComponentPatch.EMPTY : o.data.asPatch(),
      SpellSlot::new
  );
  public static Codec<SpellSlot> CODEC = MAP_CODEC.codec();

  public SpellSlot(UUID spellId, int slot, Spell spell, SpellModifierSet enabledModifiers) {
    this(spellId, slot, spell, enabledModifiers, PatchedDataComponentMap.fromPatch(spell.getComponents(), DataComponentPatch.EMPTY));
  }

  public SpellSlot(UUID uuid, int integer, Spell spell, SpellModifierSet spellModifiers, DataComponentPatch dataComponentPatch) {
    this(uuid, integer, spell, spellModifiers, PatchedDataComponentMap.fromPatch(spell.getComponents(), dataComponentPatch));
  }

  @Override
  public PatchedDataComponentMap getSpellDataPatch() {
    return data();
  }

  @Override
  public int getSlot () {
    return slot();
  }

  @Override
  public DataComponentMap getSpellData() {
    return data();
  }

  @Override
  public UUID getId() {
    return spellId();
  }

  @Override
  public Spell asSpell() {
    return spell();
  }

  @Override
  public SpellModifierSet getEnabledModifiers() {
    return enabledModifiers();
  }

  @Override
  public Set<? extends ICostedChild> getChildren() {
    return getEnabledModifiers();
  }

  @Override
  public boolean has(SpellModifier modifier) {
    return enabledModifiers.contains(modifier);
  }

  public <T> SpellSlot withData(DataComponentType<? super T> component, @Nullable T value) {
    var newData = this.data.copy();
    newData.set(component, value);
    return new SpellSlot(spellId, slot, spell, enabledModifiers.copy(), newData);
  }

  public SpellSlot withoutModifier(SpellModifier modifier) {
    if (!has(modifier)) {
      return copy();
    }

    return new SpellSlot(spellId, slot, spell, ModifierTrees.without(spell, enabledModifiers, modifier), data);
  }

  public SpellSlot withModifier(SpellModifier modifier) {
    if (has(modifier)) {
      return copy();
    }

    return new SpellSlot(spellId, slot, spell, ModifierTrees.with(spell, enabledModifiers, modifier), data);
  }

  public SpellSlot withSlot(int slot) {
    return new SpellSlot(spellId, slot, spell, enabledModifiers.copy(), data);
  }

  public SpellSlot copy() {
    return new SpellSlot(spellId, slot, spell, enabledModifiers.copy(), data);
  }

  @Override
  public DataComponentMap getComponents() {
    return data;
  }
}
