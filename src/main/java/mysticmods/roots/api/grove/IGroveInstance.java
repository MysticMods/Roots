package mysticmods.roots.api.grove;

import mysticmods.roots.api.blockentity.Bounded;

public interface IGroveInstance extends Bounded {
  Grove asGrove ();

  int getRank();

  GrovePower getPower();
}
