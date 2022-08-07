package mysticmods.roots.spell;

import mysticmods.roots.api.herbs.Cost;
import mysticmods.roots.api.spells.Spell;

import java.util.List;

public class GrowthInfusionSpell extends Spell {
  public GrowthInfusionSpell(List<Cost> costs) {
    super(Type.CONTINUOUS, costs);
  }
}
