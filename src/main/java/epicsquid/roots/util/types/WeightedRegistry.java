package epicsquid.roots.util.types;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

public class WeightedRegistry<T extends WeightedRegistry.WeightedRegistryItem> extends ArrayList<T> {
  public WeightedRegistry(int initialCapacity) {
    super(initialCapacity);
  }

  public WeightedRegistry() {
  }

  public WeightedRegistry(Collection<? extends T> c) {
    super(c);
  }

  public int getTotalWeight() {
    return this.stream().mapToInt(WeightedRegistryItem::getWeight).sum();
  }

  @Nullable
  public T getRandomItem(Random random, int totalWeight) {
    if (totalWeight <= 0) {
      throw new IllegalArgumentException();
    }

    return getRandomItem(random.nextInt(totalWeight));
  }

  @Nullable
  public T getRandomItem(int weight) {
    for (T t : this) {
      weight -= t.getWeight();

      if (weight < 0) {
        return t;
      }
    }

    return null;
  }

  @Nullable
  public T getRandomItem(Random random) {
    return getRandomItem(random, getTotalWeight());
  }

  public static abstract class WeightedRegistryItem extends RegistryItem {
    private int weight = 0;

    public int getWeight() {
      return weight;
    }

    public void setWeight(int weight) {
      this.weight = weight;
    }
  }
}
