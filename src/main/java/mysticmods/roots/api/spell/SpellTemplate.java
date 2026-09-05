package mysticmods.roots.api.spell;

import mysticmods.roots.api.modifier.SpellModifierSet;
import mysticmods.roots.api.registry.RootsRegistries;
import net.minecraft.core.component.DataComponentHolder;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.PatchedDataComponentMap;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record SpellTemplate(Spell spell, SpellModifierSet enabledModifiers,
                            PatchedDataComponentMap data) implements ISpellInstance, DataComponentHolder {
  public static StreamCodec<RegistryFriendlyByteBuf, SpellTemplate> STREAM_CODEC = StreamCodec.composite(
      ByteBufCodecs.registry(RootsRegistries.Keys.SPELLS), SpellTemplate::spell,
      SpellModifierSet.STREAM_CODEC, SpellTemplate::enabledModifiers,
      DataComponentPatch.STREAM_CODEC, o -> o.data == null ? DataComponentPatch.EMPTY : o.data.asPatch(), SpellTemplate::new
  );

  SpellTemplate(Spell spell, SpellModifierSet enabledModifiers, DataComponentPatch patch) {
    this(spell, enabledModifiers, PatchedDataComponentMap.fromPatch(spell.getComponents(), patch));
  }

  @Override
  public PatchedDataComponentMap getSpellDataPatch() {
    return data();
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
