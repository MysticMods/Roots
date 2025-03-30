package mysticmods.roots.api.action;

import com.google.common.collect.Sets;
import mysticmods.roots.api.action.parameter.GroveParameter;
import mysticmods.roots.api.action.parameter.GroveParameterSet;
import net.minecraft.server.level.ServerLevel;

import javax.annotation.Nullable;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;

public class GroveContextParameters {
  private final ServerLevel level;
  private final Map<GroveParameter<?>, Object> params;

  public GroveContextParameters(ServerLevel level, Map<GroveParameter<?>, Object> params) {
    this.level = level;
    this.params = params;
  }

  public ServerLevel getLevel() {
    return this.level;
  }

  public boolean hasParameter(GroveParameter<?> param) {
    return this.params.containsKey(param);
  }

  public <T> T getParameter(GroveParameter<T> param, T defaultValue) {
    T t = (T) this.params.get(param);
    if (t == null) {
      return defaultValue;
    } else {
      return t;
    }
  }

  @Nullable
  public <T> T getParameter(GroveParameter<T> param) {
    return (T) this.params.get(param);
  }

  public static class Builder {
    private final ServerLevel level;
    private final Map<GroveParameter<?>, Object> params = new IdentityHashMap<>();

    public Builder(ServerLevel level) {
      this.level = level;
    }

    public ServerLevel getLevel() {
      return this.level;
    }

    public <T> Builder withParameter(GroveParameter<T> parameter, T value) {
      this.params.put(parameter, value);
      return this;
    }

    public <T> Builder withOptionalParameter(GroveParameter<T> parameter, @Nullable T value) {
      if (value == null) {
        this.params.remove(parameter);
      } else {
        this.params.put(parameter, value);
      }

      return this;
    }

/*    public <T> T getParameter(GroveParameter<T> parameter) {
      T t = (T) this.params.get(parameter);
      if (t == null) {
        throw new NoSuchElementException(parameter.getName().toString());
      } else {
        return t;
      }
    }*/

    public GroveContextParameters create(GroveParameterSet params) {
      Set<GroveParameter<?>> set = Sets.difference(this.params.keySet(), params.getAllowed());
      if (!set.isEmpty()) {
        throw new IllegalArgumentException("Parameters not allowed in this parameter set: " + set);
      } else {
        Set<GroveParameter<?>> set1 = Sets.difference(params.getRequired(), this.params.keySet());
        if (!set1.isEmpty()) {
          throw new IllegalArgumentException("Missing required parameters: " + set1);
        } else {
          return new GroveContextParameters(this.level, this.params);
        }
      }
    }
  }
}
