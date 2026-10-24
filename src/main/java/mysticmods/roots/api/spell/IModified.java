package mysticmods.roots.api.spell;

import mysticmods.roots.api.modifier.Modifier;
import mysticmods.roots.api.modifier.ModifierSet;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;

import javax.annotation.Nullable;
import java.util.Set;

public interface IModified<T, M extends Modifier<T, M>, S extends ModifierSet<T, M, S>> {
  S getEnabledModifiers();

  default Set<ResourceKey<M>> getEnabledModifierKeys() {
    return getEnabledModifiers().getKeys();
  }

  default Set<ResourceKey<M>> getTransformingKeys() {
    return getEnabledModifiers().getTransformingKeys();
  }

  default boolean has(M modifier) {
    return getEnabledModifiers().contains(modifier);
  }

  default boolean has(Holder<M> modifier) {
    return getEnabledModifiers().contains(modifier.value());
  }

  @Nullable
  default M getFirstModifier(TagKey<M> modifier) {
    return getEnabledModifiers().getFirstModifier(modifier);
  }

  default boolean has(TagKey<M> modifier) {
    return getEnabledModifiers().hasTag(modifier);
  }

  default boolean has(ResourceKey<M> modifier) {
    return getEnabledModifierKeys().contains(modifier);
  }

  default int getTransformingHash() {
    return getEnabledModifiers().transformingHash();
  }
}
