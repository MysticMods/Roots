package mysticmods.roots.api.datacomponent;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mysticmods.roots.api.RootsAPI;
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

public record SpellInstance(UUID spellId, int slot, Spell spell, SpellModifierSet enabledModifiers,
                            PatchedDataComponentMap data) implements ISpellInstance.Identified, DataComponentHolder {
  public static MapCodec<SpellInstance> MAP_CODEC = RecordCodecBuilder.<SpellInstance>mapCodec(instance -> instance.group(
      UUIDUtil.CODEC.fieldOf("spellId").forGetter(SpellInstance::spellId),
      Codec.INT.fieldOf("slot").forGetter(SpellInstance::slot),
      RootsRegistries.SPELLS.byNameCodec().fieldOf("spell").forGetter(SpellInstance::spell),
      SpellModifierSet.CODEC.fieldOf("enabledModifiers").forGetter(SpellInstance::enabledModifiers),
      DataComponentPatch.CODEC.fieldOf("data")
          .forGetter(o -> o.data == null ? DataComponentPatch.EMPTY : o.data.asPatch())
  ).apply(instance, SpellInstance::new)).validate(result -> {
    // TODO: Improve migration system
    if (result.is(Spells.RAMPANT_GROWTH)) {
      // Migrate to growth infusion with modifier
      var newSpell = RootsRegistries.SPELLS.get(Spells.GROWTH_INFUSION);
      var rampantGrowth = RootsRegistries.SPELL_MODIFIERS.get(RootsAPI.rl("growth_infusion/rampant_growth"));
      return DataResult.success(new SpellInstance(result.spellId, result.slot, newSpell, new SpellModifierSet(rampantGrowth), DataComponentPatch.EMPTY));
    }
    // TODO: Validation: remove restricted modifiers
    return DataResult.success(result);
  });
  public static StreamCodec<RegistryFriendlyByteBuf, SpellInstance> STREAM_CODEC = StreamCodec.composite(
      UUIDUtil.STREAM_CODEC, SpellInstance::spellId,
      ByteBufCodecs.VAR_INT, SpellInstance::slot,
      ByteBufCodecs.registry(RootsRegistries.Keys.SPELLS), SpellInstance::spell,
      SpellModifierSet.STREAM_CODEC, SpellInstance::enabledModifiers,
      DataComponentPatch.STREAM_CODEC, o -> o.data == null ? DataComponentPatch.EMPTY : o.data.asPatch(),
      SpellInstance::new
  );
  public static Codec<SpellInstance> CODEC = MAP_CODEC.codec();

  public SpellInstance(UUID spellId, int slot, Spell spell, SpellModifierSet enabledModifiers) {
    this(spellId, slot, spell, enabledModifiers, PatchedDataComponentMap.fromPatch(spell.getComponents(), DataComponentPatch.EMPTY));
  }

  public SpellInstance(UUID uuid, int integer, Spell spell, SpellModifierSet spellModifiers, DataComponentPatch dataComponentPatch) {
    this(uuid, integer, spell, spellModifiers, PatchedDataComponentMap.fromPatch(spell.getComponents(), dataComponentPatch));
  }

  @Override
  public PatchedDataComponentMap getSpellDataPatch() {
    return data();
  }

  @Override
  public int getSlot() {
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

  public <T> SpellInstance withData(DataComponentType<? super T> component, @Nullable T value) {
    var newData = this.data.copy();
    newData.set(component, value);
    return new SpellInstance(spellId, slot, spell, enabledModifiers.copy(), newData);
  }

  public SpellInstance withoutModifier(SpellModifier modifier) {
    if (!has(modifier)) {
      return copy();
    }

    return new SpellInstance(spellId, slot, spell, ModifierTrees.without(spell, enabledModifiers, modifier), data);
  }

  public SpellInstance withModifier(SpellModifier modifier) {
    if (has(modifier)) {
      return copy();
    }

    return new SpellInstance(spellId, slot, spell, ModifierTrees.with(spell, enabledModifiers, modifier), data);
  }

  public SpellInstance withSlot(int slot) {
    return new SpellInstance(spellId, slot, spell, enabledModifiers.copy(), data);
  }

  public SpellInstance copy() {
    return new SpellInstance(spellId, slot, spell, enabledModifiers.copy(), data);
  }

  @Override
  public DataComponentMap getComponents() {
    return data;
  }
}
