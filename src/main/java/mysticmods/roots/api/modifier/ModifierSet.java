package mysticmods.roots.api.modifier;

import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.Codec;
import mysticmods.roots.api.registry.RootsRegistries;
import mysticmods.roots.api.spell.Spell;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.stream.Stream;

public abstract class ModifierSet<V, T extends Modifier<V, T>, C extends ModifierSet<V, T, C>> implements Set<T> {
  protected final ImmutableSet<T> internal;

  @SafeVarargs
  public ModifierSet(T... elements) {
    this.internal = ImmutableSet.copyOf(elements);
  }

  public ModifierSet(Collection<T> elements) {
    this.internal = ImmutableSet.copyOf(elements);
  }

  public ModifierSet(ImmutableSet<T> elements) {
    this.internal = elements;
  }

  public abstract ModifierSet<V, T, C> without(T element);

  public abstract ModifierSet<V, T, C> with(T element);

  public boolean has(T element) {
    return contains(element);
  }

  @Override
  public int size() {
    return internal.size();
  }

  @Override
  public boolean isEmpty() {
    return internal.isEmpty();
  }

  @Override
  public boolean contains(Object o) {
    return internal.contains(o);
  }

  @Override
  public @NotNull Iterator<T> iterator() {
    return internal.iterator();
  }

  @Override
  public @NotNull Object[] toArray() {
    return internal.toArray();
  }

  @Override
  public @NotNull <T1> T1[] toArray(@NotNull T1[] a) {
    return internal.toArray(a);
  }

  @Override
  public boolean add(T t) {
    return false;
  }

  @Override
  public boolean remove(Object o) {
    return internal.remove(o);
  }

  @Override
  public boolean containsAll(@NotNull Collection<?> c) {
    return internal.containsAll(c);
  }

  @Override
  public boolean addAll(@NotNull Collection<? extends T> c) {
    return false;
  }

  @Override
  public boolean removeAll(@NotNull Collection<?> c) {
    return internal.removeAll(c);
  }

  @Override
  public boolean retainAll(@NotNull Collection<?> c) {
    return internal.retainAll(c);
  }

  @Override
  public void clear() {
    internal.clear();
  }

  @Override
  public String toString() {
    return internal.toString();
  }

  @Override
  public <T1> T1[] toArray(@NotNull IntFunction<T1[]> generator) {
    return internal.toArray(generator);
  }

  @Override
  public @NotNull Stream<T> stream() {
    return internal.stream();
  }

  @Override
  public @NotNull Stream<T> parallelStream() {
    return internal.parallelStream();
  }

  @Override
  public void forEach(Consumer<? super T> action) {
    internal.forEach(action);
  }
}
