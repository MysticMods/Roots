package mysticmods.roots.blockentity.template;

import mysticmods.roots.api.state.State;
import mysticmods.roots.api.state.StateCategory;
import mysticmods.roots.api.state.StateHolder;

public interface StatefulBlockEntity<T extends BaseBlockEntity> {
  StateHolder<T> getStateHolder();

  default boolean is(State<?> state) {
    return getStateHolder().is(state);
  }

  default boolean anyOf(State<?>... states) {
    return getStateHolder().anyOf(states);
  }

  default boolean allOf(State<?>... states) {
    return getStateHolder().allOf(states);
  }

  default boolean has(StateCategory<?> category) {
    return getStateHolder().has(category);
  }
}
