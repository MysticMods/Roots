package mysticmods.roots.api;

import mysticmods.roots.api.ritual.Ritual;

public interface RitualLike extends Like<Ritual> {
  Ritual asRitual();

  @Override
  default Ritual asLike () {
    return asRitual();
  }
}
