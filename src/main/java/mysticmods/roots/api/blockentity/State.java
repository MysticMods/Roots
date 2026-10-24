package mysticmods.roots.api.blockentity;

import mysticmods.roots.api.RootsAPI;
import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

// Pyre:
// Inventory, Progress, Recipe, Animation
// Inventory: EMPTY, PARTIAL, FULL
// Progress: NONE, CRAFTING
// Recipe: NONE, MATCHING
// Animation: NONE, CRAFTING

// Grove Crafter:
// Progress, Recipe, Animation, Lock
// Lock: NONE, LOCKED
// Progress: NONE, CRAFTING
// Recipe: NONE, MATCHING
// Animation: NONE, CRAFTING

// Mortar:
// Inventory, Recipe

// Fungal transmuter
// Inventory, Progress, Recipe, Animation, Lock
// Full inventory -> consuming power -> valid recipe -> consuming power -> locked


public record State(StateCategory category, ResourceLocation name) {
  public record StateCategory(ResourceLocation name) {
  }

  interface Categories {
    StateCategory INVENTORY = new StateCategory(RootsAPI.rl("inventory"));
    StateCategory LOCK = new StateCategory(RootsAPI.rl("lock"));
    StateCategory PROGRESS = new StateCategory(RootsAPI.rl("progress"));
    StateCategory RECIPE = new StateCategory(RootsAPI.rl("recipe"));
    StateCategory ANIMATION = new StateCategory(RootsAPI.rl("animation"));
  }

  interface Lock {
    State NONE = new State(Categories.LOCK, RootsAPI.rl("none"));
    State LOCKED = new State(Categories.LOCK, RootsAPI.rl("locked"));
  }

  interface Inventory {
    State EMPTY = new State(Categories.INVENTORY, RootsAPI.rl("empty"));
    State PARTIAL = new State(Categories.INVENTORY, RootsAPI.rl("partial"));
    State FULL = new State(Categories.INVENTORY, RootsAPI.rl("full"));
  }

  interface Progress {
    State NONE = new State(Categories.PROGRESS, RootsAPI.rl("none"));
    State CRAFTING = new State(Categories.PROGRESS, RootsAPI.rl("crafting"));
    State CONSUMING_POWER = new State(Categories.PROGRESS, RootsAPI.rl("consuming_power"));
  }

  interface Recipe {
    State MATCHING = new State(Categories.RECIPE, RootsAPI.rl("matching"));
    State NONE = new State(Categories.RECIPE, RootsAPI.rl("none"));
  }

  interface Animation {
    State NONE = new State(Categories.ANIMATION, RootsAPI.rl("none"));
    State CRAFTING = new State(Categories.ANIMATION, RootsAPI.rl("crafting"));
    State CONSUMING_POWER = new State(Categories.ANIMATION, RootsAPI.rl("consuming_power"));
  }

  public static class StateHolder {
    private final Set<State> eligibleStates;
    private final Set<StateCategory> eligibleCategories;

    public StateHolder(List<State> eligibleStates) {
      this.eligibleStates = Set.copyOf(eligibleStates);
      this.eligibleCategories = eligibleStates.stream().map(State::category).collect(Collectors.toSet());
      ;
    }

    // Can't have multiple states for the same category in the output
    // Can't have states for categories that aren't included
  }
}
