package epicsquid.roots.util.types;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class RandomIterable<T> implements Iterable<T> {
  private List<T> wrap;
  private List<Integer> list;

  public RandomIterable(List<T> wrap) {
    this.wrap = wrap;
    this.list = IntStream.rangeClosed(0, wrap.size()-1).boxed().collect(Collectors.toList());
    Collections.shuffle(this.list);
  }

  @Override
  public Iterator<T> iterator() {
    return new RandomIterator(this.list.iterator());
  }

  public class RandomIterator implements Iterator<T> {
    private Iterator<Integer> intIterator;

    public RandomIterator(Iterator<Integer> intIterator) {
      this.intIterator = intIterator;
    }

    @Override
    public boolean hasNext() {
      return this.intIterator.hasNext();
    }

    @Override
    public T next() {
      int next = this.intIterator.next();
      return wrap.get(next);
    }
  }
}
