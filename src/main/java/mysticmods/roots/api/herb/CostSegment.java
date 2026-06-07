package mysticmods.roots.api.herb;

public record CostSegment(double amount, CostType type, CostSource source) {
  public static CostSegment spell(double amount) {
    return new CostSegment(amount, CostType.ADDITIVE, CostSource.SPELL);
  }

  public static CostSegment discount(double amount) {
    return new CostSegment(amount, CostType.MULTIPLICATIVE_TOTAL, CostSource.DISCOUNT);
  }

  public static CostSegment modifier(double amount, CostType type) {
    return new CostSegment(amount, type, CostSource.MODIFIER);
  }
}
