package mysticmods.roots.api.spell;

import mysticmods.roots.api.modifier.SpellModifierSet;
import mysticmods.roots.api.registry.RootsRegistries;
import net.minecraft.core.UUIDUtil;
import net.minecraft.core.component.DataComponentHolder;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.PatchedDataComponentMap;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.UUID;

public record SpellInstanceSnapshot(UUID id, int slot, Spell spell, SpellModifierSet enabledModifiers,
                                    PatchedDataComponentMap data) implements ISpellInstance, DataComponentHolder {
  public static StreamCodec<RegistryFriendlyByteBuf, SpellInstanceSnapshot> STREAM_CODEC = StreamCodec.composite(
      UUIDUtil.STREAM_CODEC, SpellInstanceSnapshot::id,
      ByteBufCodecs.VAR_INT, SpellInstanceSnapshot::slot,
      ByteBufCodecs.registry(RootsRegistries.Keys.SPELLS), SpellInstanceSnapshot::spell,
      SpellModifierSet.STREAM_CODEC, SpellInstanceSnapshot::enabledModifiers,
      DataComponentPatch.STREAM_CODEC, o -> o.data == null ? DataComponentPatch.EMPTY : o.data.asPatch(), SpellInstanceSnapshot::new
  );

  SpellInstanceSnapshot(UUID id, int slot, Spell spell, SpellModifierSet enabledModifiers, DataComponentPatch patch) {
    this(id, slot, spell, enabledModifiers, PatchedDataComponentMap.fromPatch(spell.getComponents(), patch));
  }

  @Override
  public PatchedDataComponentMap getSpellDataPatch() {
    return data();
  }

  @Override
  public UUID getId() {
    return id();
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
  public DataComponentMap getComponents() {
    return data();
  }
}
