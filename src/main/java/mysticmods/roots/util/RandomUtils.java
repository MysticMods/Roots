package mysticmods.roots.util;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class RandomUtils {
  // From Stack Overflow: https://stackoverflow.com/posts/51618656/revisions
  public static <T> Iterable<T> shuffle(final List<T> input) {
    return () -> new Iterator<T>() {
      final Random randomizer = new Random();
      int i = 0;
      final int n = input.size();
      final Int2ObjectOpenHashMap<T> shuffled = new Int2ObjectOpenHashMap<>();

      @Override
      public boolean hasNext() {
        return i < n;
      }

      @Override
      public T next() {
        int j = i + randomizer.nextInt(n - i);
        T a = get(i), b = get(j);
        shuffled.put(j, a);
        shuffled.remove(i);
        ++i;
        return b;
      }

      T get(int i) {
        return shuffled.containsKey(i) ? shuffled.get(i) : input.get(i);
      }
    };
  }
}
