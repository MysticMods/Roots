package mysticmods.roots.api.recipe.processors;

import mysticmods.roots.api.recipe.IRootsCrafting;

public abstract class RootsProcessor<T extends IRootsCrafting<?>> implements IRootsProcessor<T> {
  public RootsProcessor() {
  }
}
