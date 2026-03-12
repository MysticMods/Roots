package mysticmods.roots.api.modifier;

import com.google.common.collect.MapMaker;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.ConcurrentMap;

public class RootModifierNode<V, T extends Modifier<V, T>> extends ModifierNode<V, T> {
  private static final ConcurrentMap<ResourceKey<?>, RootModifierNode<?, ?>> VALUES = new MapMaker().weakValues().makeMap();

  protected RootModifierNode(ResourceKey<T> key) {
    super(key);
  }

  @Override
  public @Nullable IModifierNode<V, T> parent() {
    return null;
  }

  @Override
  public IModifierNode<V, T> setParent(IModifierNode<V, T> parent) {
    return null;
  }

  @SuppressWarnings("unchecked")
  public static <V, T extends Modifier<V, T>> RootModifierNode<V, T> create(Holder<V> type, ResourceKey<? extends Registry<T>> registry) {
    ResourceKey<T> key = ResourceKey.create(registry, type.getKey().location().withSuffix("_root_node"));

    return (RootModifierNode<V, T>) VALUES.computeIfAbsent(key, (k) -> new RootModifierNode<>((ResourceKey<T>) k));
  }
}
