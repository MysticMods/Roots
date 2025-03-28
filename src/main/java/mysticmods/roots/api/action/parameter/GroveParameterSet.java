package mysticmods.roots.api.action.parameter;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import java.util.Set;

public class GroveParameterSet {
  private final Set<GroveParameter<?>> required;
  private final Set<GroveParameter<?>> all;

  GroveParameterSet(Set<GroveParameter<?>> required, Set<GroveParameter<?>> optional) {
    this.required = ImmutableSet.copyOf(required);
    this.all = ImmutableSet.copyOf(Sets.union(required, optional));
  }

  public boolean isAllowed(GroveParameter<?> param) {
    return this.all.contains(param);
  }

  public Set<GroveParameter<?>> getRequired() {
    return this.required;
  }

  public Set<GroveParameter<?>> getAllowed() {
    return this.all;
  }

  @Override
  public String toString() {
    return "["
        + Joiner.on(", ").join(this.all.stream().map(p_339580_ -> (this.required.contains(p_339580_) ? "!" : "") + p_339580_.getName()).iterator())
        + "]";
  }

  public static GroveParameterSet.Builder builder() {
    return new GroveParameterSet.Builder();
  }

  public static class Builder {
    private final Set<GroveParameter<?>> required = Sets.newIdentityHashSet();
    private final Set<GroveParameter<?>> optional = Sets.newIdentityHashSet();

    public GroveParameterSet.Builder required(GroveParameter<?> parameter) {
      if (this.optional.contains(parameter)) {
        throw new IllegalArgumentException("Parameter " + parameter.getName() + " is already optional");
      } else {
        this.required.add(parameter);
        return this;
      }
    }

    public GroveParameterSet.Builder optional(GroveParameter<?> parameter) {
      if (this.required.contains(parameter)) {
        throw new IllegalArgumentException("Parameter " + parameter.getName() + " is already required");
      } else {
        this.optional.add(parameter);
        return this;
      }
    }

    public GroveParameterSet build() {
      return new GroveParameterSet(this.required, this.optional);
    }
  }
}
