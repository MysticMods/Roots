package mysticmods.roots.api.registry;

import mysticmods.roots.api.spell.SpellType;
import org.jetbrains.annotations.Nullable;

public interface ICostedChild extends ICosted {
  SpellType.Condition getChargeCondition();

  @Nullable
  default SpellType.Charge getChargeType () {
    return null;
  }
}
