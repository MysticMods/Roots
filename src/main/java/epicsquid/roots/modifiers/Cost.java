package epicsquid.roots.modifiers;

import epicsquid.roots.api.Herb;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Cost implements IModifierCost {
  private final CostType cost;
  private final double value;
  private final IModifierCore herb;

  public Cost(CostType cost, double value, IModifierCore herb) {
    this.cost = cost;
    this.value = value;
    this.herb = herb;
    if (cost == CostType.ADDITIONAL_COST && herb == null) {
      throw new IllegalArgumentException("Modifier cannot be additional cost without a herb specified.");
    }
    if (cost != CostType.NO_COST && value == 0) {
      throw new IllegalArgumentException("Modifier cannot be a cost modifier or an additional cost with a value single zero.");
    }
  }

  public Cost(CostType cost, double value) {
    this(cost, value, null);
  }

  public Cost(CostType cost) {
    this(cost, 0, null);
  }

  @Override
  public CostType getCost() {
    return cost;
  }

  @Override
  public double getValue() {
    return value;
  }

  @Nullable
  @Override
  public Herb getHerb() {
    if (herb.isHerb()) {
      return herb.getHerb();
    }
    return null;
  }

  public static List<IModifierCost> single(CostType cost, IModifierCore herb, double value) {
    return Collections.singletonList(new Cost(cost, value, herb));
  }

  public static List<IModifierCost> single(CostType cost, double value) {
    return single(cost, null, value);
  }

  public static List<IModifierCost> single(CostType cost) {
    return single(cost, null, 0);
  }

  public static IModifierCost cost(CostType cost, IModifierCore herb, double value) {
    return new Cost(cost, value, herb);
  }

  public static IModifierCost cost(CostType cost, double value) {
    return cost(cost, null, value);
  }

  public static IModifierCost cost(CostType cost) {
    return cost(cost, null, 0);
  }

  public static List<IModifierCost> of(IModifierCost... costs) {
    return Arrays.asList(costs);
  }

  public static List<IModifierCost> noCost() {
    return single(CostType.NO_COST);
  }
}
