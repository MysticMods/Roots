package mysticmods.roots.api.herbs;

import mysticmods.roots.api.herbs.Herb;

public class Cost {
  private final CostType type;
  private final Herb herb;
  private final double value;

  public Cost(CostType type, Herb herb, double value) {
    this.type = type;
    this.herb = herb;
    this.value = value;
  }

  public CostType getType() {
    return type;
  }

  public Herb getHerb() {
    return herb;
  }

  public double getValue() {
    return value;
  }


  // TODO: tojson, tonetwork, fromjson, fromnetwork

  public enum CostType {
    ADDITIVE,
    MULTIPLICATIVE
  }
}
