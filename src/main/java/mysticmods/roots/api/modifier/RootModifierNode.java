package mysticmods.roots.api.modifier;

import com.google.common.collect.MapMaker;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.ConcurrentMap;

public class RootModifierNode<V, T extends Modifier<V, T>> extends ModifierNode<V, T> {
  private static final ConcurrentMap<ResourceKey<?>, RootModifierNode<?, ?>> VALUES = new MapMaker().weakValues().makeMap();

  private final ModifierTree<V, T> tree;

  protected RootModifierNode(ResourceKey<T> key, ModifierTree<V, T> tree) {
    super(key);
    this.tree = tree;
  }

  @Override
  public @Nullable IModifierNode<V, T> parent() {
    return null;
  }

  @Override
  public IModifierNode<V, T> setParent(IModifierNode<V, T> parent) {
    return null;
  }

  @Override
  public List<IModifierNode<V, T>> children() {
    return tree.rootNodes();
  }

  @SuppressWarnings("unchecked")
  public static <V, T extends Modifier<V, T>> RootModifierNode<V, T> create(ModifierTree<V, T> tree, Holder<V> object, ResourceKey<? extends Registry<T>> registry) {
    ResourceKey<T> key = ResourceKey.create(registry, object.getKey().location().withSuffix("_root_node"));

    return (RootModifierNode<V, T>) VALUES.computeIfAbsent(key, (k) -> new RootModifierNode<>((ResourceKey<T>) k, tree));
  }
}
