package mysticmods.roots.api.state;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.fml.loading.FMLEnvironment;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

public final class StateHolder<B extends BlockEntity> {
  private final StateSchema<B> schema;
  private final Map<StateCategory<?>, Object> values = new IdentityHashMap<>();

  StateHolder(StateSchema<B> schema) {
    this.schema = schema;
    schema.resolvers().keySet().forEach(category -> values.put(category, category.defaultValue()));
  }

  public boolean recompute(B be) {
    boolean changed = false;
    for (Map.Entry<StateCategory<?>, Function<B, ?>> entry : schema.resolvers().entrySet()) {
      Object next = entry.getValue().apply(be);
      if (next == null) {
        next = entry.getKey().defaultValue();
      }
      if (values.put(entry.getKey(), next) != next) {
        changed = true;
      }
    }
    return changed;
  }

  @SuppressWarnings("unchecked")
  public <V extends Enum<V> & State<V>> V get(StateCategory<V> category) {
    Object value = values.get(category);
    if (value == null) {
      if (!FMLEnvironment.production) {
        throw new IllegalArgumentException("Category " + category.id() + " not in schema");
      }
      return category.defaultValue();
    }
    return (V) value;
  }

  public boolean is(State<?> state) {
    return values.get(state.category()) == state;
  }

  public boolean anyOf(State<?>... states) {
    for (State<?> state : states) {
      if (is(state)) {
        return true;
      }
    }
    return false;
  }

  public boolean allOf(State<?>... states) {
    for (State<?> state : states) {
      if (!is(state)) {
        return false;
      }
    }
    return true;
  }

  public boolean has(StateCategory<?> category) {
    return schema.has(category);
  }

  public Map<StateCategory<?>, Object> asMap() {
    return Collections.unmodifiableMap(values);
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder("StateHolder{");
    boolean first = true;
    for (Map.Entry<StateCategory<?>, Object> entry : values.entrySet()) {
      if (!first) {
        builder.append(", ");
      }
      first = false;
      builder.append(entry.getKey().id().getPath()).append('=')
          .append(((State<?>) entry.getValue()).getSerializedName());
    }
    return builder.append('}').toString();
  }
}
