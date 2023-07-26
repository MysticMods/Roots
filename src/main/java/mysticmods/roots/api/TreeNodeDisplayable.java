package mysticmods.roots.api;

import java.util.Collections;
import java.util.List;

public interface TreeNodeDisplayable<T extends TreeNodeDisplayable<T>> {
  default List<T> getChildren () {
    return Collections.emptyList();
  }

  default boolean isVisible() {
    return true;
  }

  default void setLocation (float pX, float pY) {
  }
}
