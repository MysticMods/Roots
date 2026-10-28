package mysticmods.roots.api.blockentity;

import java.util.EnumMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StateHolder {
  private final Set<State> eligibleStates;
  private final Set<Category> eligibleCategories;
  private final Map<Category, State> stateMap = new EnumMap<>(Category.class);

  public static final State NONE = new State(Category.ALL, "none");

  protected StateHolder(State... eligibleStates) {
    this.eligibleStates = Set.of(eligibleStates);
    this.eligibleCategories = Stream.of(eligibleStates).map(State::category).collect(Collectors.toUnmodifiableSet());
    this.eligibleCategories.forEach(o -> stateMap.put(o, NONE));
  }

  public State get(Category category) {
    if (!eligibleCategories.contains(category)) {
      throw new IllegalStateException(category + " is not a valid category for this holder!");
    }

    return stateMap.getOrDefault(category, NONE);
  }

  public void set(State state) {
    if (!eligibleStates.contains(state)) {
      throw new IllegalStateException(state + " is not a valid state for this holder!");
    }

    stateMap.put(state.category(), state);
  }

  public static StateHolder forStates(State... states) {
    return new StateHolder(states);
  }

  public record State(Category category, String name) {
    public boolean isEmpty() {
      return this == NONE;
    }
  }

  State INVENTORY_LOCKED = new State(Category.INVENTORY_LOCK, "locked");
  State INVENTORY_PARTIAL = new State(Category.INVENTORY, "partial");
  State INVENTORY_FULL = new State(Category.INVENTORY, "full");
  State PROGRESS_CRAFTING = new State(Category.PROGRESS, "crafting");
  State PROGRESS_CONSUMING_POWER = new State(Category.PROGRESS, "consuming_power");
  State RECIPE_MATCHING = new State(Category.RECIPE, "matching");
  State ANIMATION_CRAFTING = new State(Category.ANIMATION, "crafting");
  State ANIMATION_CONSUMING_POWER = new State(Category.ANIMATION, "consuming_power");

  public enum Category {
    INVENTORY,
    INVENTORY_LOCK,
    PROGRESS,
    RECIPE,
    ANIMATION,
    ALL;
  }
}
