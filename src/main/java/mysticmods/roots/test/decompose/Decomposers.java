/*
package mysticmods.roots.test.decompose;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

public class Decomposers {
  public interface Decomposer<FROM, INTO> {
    Collection<INTO> applySingle(FROM from);

    default Collection<INTO> applyMulti(Collection<FROM> from) {
      List<INTO> result = new ArrayList<>();
      for (FROM f : from) {
        result.addAll(applySingle(f));
      }
      return result;
    }

    @NotNull
    default <V> Function<V, Collection<INTO>> compose(@NotNull Function<? super V, ? extends FROM> before) {
      return composeSingle(before);
    }

    @NotNull
    default <V> Function<V, Collection<INTO>> composeSingle(@NotNull Function<? super V, ? extends FROM> before) {
      return Function.super.compose(before);
    }

    @NotNull
    default <V> Function<Collection<V>, Collection<INTO>> composeMulti(@NotNull Function<? super V, ? extends FROM> before) {
      return v -> {
        List<INTO> result = new ArrayList<>();
        for (V f : v) {
          result.addAll(composeSingle(before).apply(f));
        }
        return result;
      };
    }

    @Override
    @NotNull
    default <V> Function<FROM, V> andThen(@NotNull Function<? super Collection<INTO>, ? extends V> after) {
      return andThenSingle(after);
    }

    @NotNull
    default <V> Function<FROM, V> andThenSingle(@NotNull Function<? super Collection<INTO>, ? extends V> after) {
      return Function.super.andThen(after);
    }

    @NotNull
    default <V> Function<List<FROM>, V> andThenMulti(@NotNull Function<? super Collection<INTO>, ? extends V> after) {
      return from -> {
        List<INTO> result = new ArrayList<>();
        for (FROM f : from) {
          result.addAll(applySingle(f));
        }
        return after.apply(result);
      };
    }
  }
}
*/
