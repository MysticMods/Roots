package mysticmods.roots.api.util;

import com.google.common.collect.AbstractIterator;
import com.google.common.collect.UnmodifiableIterator;

import javax.annotation.CheckForNull;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.stream.Stream;

public class SetView<E> extends AbstractSet<E> {
  private final Set<E> delegate;

  private SetView(Set<E> delegate) {
    this.delegate = delegate;
  }

  public final boolean add(E e) {
    throw new UnsupportedOperationException();
  }

  public final boolean remove(@CheckForNull Object object) {
    throw new UnsupportedOperationException();
  }

  public final boolean addAll(Collection<? extends E> newElements) {
    throw new UnsupportedOperationException();
  }

  public final boolean removeAll(Collection<?> oldElements) {
    throw new UnsupportedOperationException();
  }

  public final boolean removeIf(java.util.function.Predicate<? super E> filter) {
    throw new UnsupportedOperationException();
  }

  public final boolean retainAll(Collection<?> elementsToKeep) {
    throw new UnsupportedOperationException();
  }

  public final void clear() {
    throw new UnsupportedOperationException();
  }

  @Override
  public int size() {
    return delegate.size();
  }

  @Override
  public boolean isEmpty() {
    return delegate.isEmpty();
  }

  @Override
  public UnmodifiableIterator<E> iterator() {
    return new AbstractIterator<E>() {
      final Iterator<? extends E> itr1 = delegate.iterator();

      @Override
      @CheckForNull
      protected E computeNext() {
        if (itr1.hasNext()) {
          return itr1.next();
        }
        return endOfData();
      }
    };
  }

  @Override
  public Stream<E> stream() {
    return delegate.stream();
  }

  @Override
  public Stream<E> parallelStream() {
    return stream().parallel();
  }

  @Override
  public boolean contains(@CheckForNull Object object) {
    return delegate.contains(object);
  }

  public static <E> SetView<E> of(Set<E> set) {
    return new SetView<>(set);
  }
}
