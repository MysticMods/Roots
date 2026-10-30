package mysticmods.roots.api.state;

import mysticmods.roots.api.RootsAPI;

public final class StateCategories {
  public static final StateCategory<InventoryState> INVENTORY =
      new StateCategory<>(RootsAPI.rl("inventory"), InventoryState.EMPTY);
  public static final StateCategory<ProgressState> PROGRESS =
      new StateCategory<>(RootsAPI.rl("progress"), ProgressState.NONE);
  public static final StateCategory<RecipeState> RECIPE =
      new StateCategory<>(RootsAPI.rl("recipe"), RecipeState.NONE);
  public static final StateCategory<AnimationState> ANIMATION =
      new StateCategory<>(RootsAPI.rl("animation"), AnimationState.NONE);
  public static final StateCategory<LockState> LOCK =
      new StateCategory<>(RootsAPI.rl("lock"), LockState.NONE);

  private StateCategories() {
  }
}
