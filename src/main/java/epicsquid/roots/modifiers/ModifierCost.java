package epicsquid.roots.modifiers;

import epicsquid.roots.api.Herb;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class ModifierCost implements IModifierCost {
  private final CostType cost;
  private final double value;
  private final IModifierCore herb;

  public ModifierCost(CostType cost, double value, IModifierCore herb) {
    this.cost = cost;
    this.value = value;
    this.herb = herb;
    if (cost == CostType.ADDITIONAL_COST && herb == null) {
      throw new IllegalStateException("Modifier cannot be additional cost without a herb specified.");
    }
  }

  public ModifierCost(CostType cost, double value) {
    this(cost, value, null);
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

  public static List<IModifierCost> of (CostType cost, IModifierCore herb, double value) {
    return Collections.singletonList(new ModifierCost(cost, value, herb));
  }

  public static List<IModifierCost> of (CostType cost, double value) {
    return of(cost, null, value);
  }
}
