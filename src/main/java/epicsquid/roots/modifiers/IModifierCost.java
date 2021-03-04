package epicsquid.roots.modifiers;

import epicsquid.roots.api.Herb;

import javax.annotation.Nullable;
import java.util.Locale;

public interface IModifierCost {
  CostType getCost();

  double getValue();

  Herb getHerb();

  default String asPropertyName () {
    Herb herb = getHerb();
    return herb.getName() + "_" + getCost().toString().toLowerCase(Locale.ROOT);
  }
}
