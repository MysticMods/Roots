package mysticmods.roots.api.grove.power.consumer;

import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import mysticmods.roots.api.grove.IGroveInstance;
import mysticmods.roots.api.recipe.IRootsRecipe;

public class PowerAccumulationRecord {
  private final int hashCode;
  private final Int2IntMap accumulated = new Int2IntOpenHashMap();

  protected PowerAccumulationRecord(int hashCode) {
    this.hashCode = hashCode;
  }

  public PowerAccumulationRecord(IRootsRecipe<?> recipe) {
    this(recipe.getPowerRequirements());
  }

  public PowerAccumulationRecord(PowerCollection collection) {
    this.hashCode = collection.hashCode();
    for (PowerAccumulationRequest request : collection) {
      accumulated.put(request.hashCode(), 0);
    }
  }

  public boolean isFulfilled(PowerCollection collection) {
    for (PowerAccumulationRequest request : collection) {
      if (!accumulated.containsKey(request.hashCode())) {
        return false;
      }
      int acquired = accumulated.get(request.hashCode());
      if (acquired < request.totalRequired()) {
        return false;
      }
    }

    return true;
  }

  public boolean isFor(PowerCollection collection) {
    return this.hashCode == collection.hash();
  }

  public void supply(IGroveInstance grove, PowerAccumulationRequest request, int amountSupplied) {
    if (!accumulated.containsKey(request.hashCode())) {
      throw new IllegalStateException("Power request '" + request + "' is not valid for this Power Accumulation Record!");
    }

    accumulated.mergeInt(request.hashCode(), amountSupplied, Integer::sum);
  }
}
