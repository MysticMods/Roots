package epicsquid.roots.modifiers;

public enum CostType {
  NO_COST, ADDITIONAL_COST, ALL_COST_MULTIPLIER;

  public String getTranslationKey() {
    return "roots.modifiers.types." + name().toLowerCase();
  }
}
