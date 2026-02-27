package mysticmods.roots.api.modifier;

import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.Codec;
import mysticmods.roots.api.registry.RootsRegistries;
import mysticmods.roots.api.spell.Spell;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class SpellModifierSet extends ModifierSet<Spell, SpellModifier, SpellModifierSet> {
  public static final SpellModifierSet EMPTY = new SpellModifierSet();

  public static final Codec<SpellModifierSet> CODEC = RootsRegistries.SPELL_MODIFIERS.byNameCodec()
      .listOf().xmap(SpellModifierSet::new, set -> set.internal.asList());
  public static final StreamCodec<RegistryFriendlyByteBuf, SpellModifierSet> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.collection(HashSet::new, ByteBufCodecs.registry(RootsRegistries.Keys.SPELL_MODIFIERS)), o -> o.internal, SpellModifierSet::new);

  public SpellModifierSet(SpellModifier... elements) {
    super(elements);
  }

  public SpellModifierSet(Collection<SpellModifier> elements) {
    super(elements);
  }

  public SpellModifierSet(ImmutableSet<SpellModifier> build) {
    super(build);
  }

  @Override
  public SpellModifierSet without(SpellModifier element) {
    if (this.isEmpty()) {
      return EMPTY;
    }

    if (!this.contains(element)) {
      return this;
    }

    if (this.size() == 1 && this.contains(element)) {
      return EMPTY;
    }

    ImmutableSet.Builder<SpellModifier> builder = ImmutableSet.builder();
    for (SpellModifier modifier : this.internal) {
      if (!modifier.equals(element)) {
        builder.add(modifier);
      }
    }
    return new SpellModifierSet(builder.build()).validated();
  }

  @Override
  public SpellModifierSet without(Collection<SpellModifier> elements) {
    if (this.isEmpty()) {
      return EMPTY;
    }

    if (!this.containsAll(elements)) {
      return this;
    }

    if (this.size() == elements.size() && this.containsAll(elements)) {
      return EMPTY;
    }

    ImmutableSet.Builder<SpellModifier> builder = ImmutableSet.builder();
    for (SpellModifier modifier : this.internal) {
      if (!elements.contains(modifier)) {
        builder.add(modifier);
      }
    }
    return new SpellModifierSet(builder.build()).validated();
  }

  @Override
  public SpellModifierSet with(SpellModifier element) {
    if (this.contains(element)) {
      return this;
    }

    ImmutableSet.Builder<SpellModifier> builder = ImmutableSet.builder();
    builder.addAll(this);
    builder.add(element);
    return new SpellModifierSet(builder.build()).validated();
  }

  @Override
  public SpellModifierSet with(Collection<SpellModifier> elements) {
    if (this.containsAll(elements)) {
      return this;
    }

    ImmutableSet.Builder<SpellModifier> builder = ImmutableSet.builder();
    builder.addAll(this);
    builder.addAll(elements);
    return new SpellModifierSet(builder.build()).validated();
  }

  public SpellModifierSet validated () {
    if (this.isEmpty()) {
      return EMPTY;
    }

    if (this.firstElement == null) {
      return EMPTY; // TODO: issue
    }

    ModifierTree<Spell, SpellModifier> tree = ModifierTrees.getSpell(this.firstElement.applicable);
    if (tree == null) {
      return EMPTY; // TODO: issue
    }

    ModifierTree<Spell, SpellModifier>.Instance instance = tree.validator(this);
    Set<SpellModifier> validModifiers = instance.modifiersSet();
    if (validModifiers.size() == this.size() && this.containsAll(validModifiers)) {
      return this;
    } else {
      return new SpellModifierSet(validModifiers);
    }
  }
}
