package mysticmods.roots.api.state;

import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

public final class StateSchema<B extends BlockEntity> {
  private final Map<StateCategory<?>, Function<B, ?>> resolvers;

  private StateSchema(Map<StateCategory<?>, Function<B, ?>> resolvers) {
    this.resolvers = Collections.unmodifiableMap(new IdentityHashMap<>(resolvers));
  }

  public Map<StateCategory<?>, Function<B, ?>> resolvers() {
    return resolvers;
  }

  public boolean has(StateCategory<?> category) {
    return resolvers.containsKey(category);
  }

  public StateHolder<B> createHolder() {
    return new StateHolder<>(this);
  }

  public static <B extends BlockEntity> Builder<B> builder() {
    return new Builder<>();
  }

  public static final class Builder<B extends BlockEntity> {
    private final Map<StateCategory<?>, Function<B, ?>> resolvers = new IdentityHashMap<>();

    private Builder() {
    }

    public <V extends Enum<V> & State<V>> Builder<B> add(StateCategory<V> category, Function<B, V> resolver) {
      if (resolvers.putIfAbsent(category, resolver) != null) {
        throw new IllegalStateException("Duplicate state category " + category.id());
      }
      return this;
    }

    public StateSchema<B> build() {
      return new StateSchema<>(resolvers);
    }
  }
}
