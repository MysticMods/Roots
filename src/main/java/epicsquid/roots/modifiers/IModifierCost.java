package epicsquid.roots.modifiers;

import epicsquid.roots.api.Herb;

import javax.annotation.Nullable;

public interface IModifierCost {
  CostType getCost();

  double getValue();

  @Nullable
  Herb getHerb();
}
