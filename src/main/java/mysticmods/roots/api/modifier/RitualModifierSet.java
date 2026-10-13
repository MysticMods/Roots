package mysticmods.roots.api.modifier;

import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.Codec;
import mysticmods.roots.api.registry.RootsRegistries;
import mysticmods.roots.api.ritual.Ritual;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.tags.TagKey;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class RitualModifierSet extends ModifierSet<Ritual, RitualModifier, RitualModifierSet> {
  public static final RitualModifierSet EMPTY = new RitualModifierSet();

  public static final Codec<RitualModifierSet> CODEC = RootsRegistries.RITUAL_MODIFIERS.byNameCodec()
      .listOf().xmap(o -> new RitualModifierSet(o).validated(), set -> set.internal.asList());
  public static final StreamCodec<RegistryFriendlyByteBuf, RitualModifierSet> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.collection(HashSet::new, ByteBufCodecs.registry(RootsRegistries.Keys.RITUAL_MODIFIERS)), o -> o.internal, o -> new RitualModifierSet(o).validated());

  public RitualModifierSet(RitualModifier... elements) {
    super(elements);
  }

  public RitualModifierSet(Collection<RitualModifier> elements) {
    super(elements);
  }

  public RitualModifierSet(ImmutableSet<RitualModifier> build) {
    super(build);
  }

  @Override
  public boolean hasTag(TagKey<RitualModifier> tag) {
    for (RitualModifier modifier : this.internal) {
      if (modifier.is(tag)) {
        return true;
      }
    }

    return false;
  }

  @Override
  public int count(TagKey<RitualModifier> tag) {
    int count = 0;
    for (RitualModifier modifier : this.internal) {
      if (modifier.is(tag)) {
        count++;
      }
    }
    return count;
  }

  public RitualModifierSet validated() {
    if (this.isEmpty()) {
      return EMPTY;
    }

    if (this.firstElement == null) {
      return EMPTY;
    }

    if (!this.validate()) {
      return EMPTY; // TODO: This means that there is a modifier in the set whose applicable does not match the others
    }

    ModifierTree<Ritual, RitualModifier> tree = ModifierTrees.getRitual(this.firstElement.applicable);
    if (tree == null) {
      return EMPTY;
    }

    ModifierTree<Ritual, RitualModifier>.Instance instance = tree.instance(this, Collections.emptySet());
    Set<RitualModifier> validModifiers = instance.modifiersSet();
    if (validModifiers.size() == this.size() && this.containsAll(validModifiers)) {
      return this;
    } else if (validModifiers.isEmpty()) {
      return EMPTY;
    } else {
      return new RitualModifierSet(validModifiers);
    }
  }

  public RitualModifierSet copy() {
    if (this == EMPTY) {
      return this;
    }

    return new RitualModifierSet(ImmutableSet.copyOf(internal));
  }

  @Nullable
  public RitualModifier getFirstModifier(TagKey<RitualModifier> modifier) {
    for (RitualModifier mod : this) {
      if (mod.is(modifier)) {
        return mod;
      }
    }

    return null;
  }

  @Nullable
  public ModifierTree<Ritual, RitualModifier> getTree() {
    RitualModifier first = firstElement();
    if (first == null) {
      return null;
    }

    return ModifierTrees.getRitual(first.getApplicableHolder());
  }
}