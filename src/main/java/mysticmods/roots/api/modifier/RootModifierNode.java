package mysticmods.roots.api.modifier;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class RootModifierNode<V, T extends Modifier<V, T>> extends ModifierNode<V, T> {
  private ModifierTree<V, T> tree;

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

  public static <V, T extends Modifier<V, T>> RootModifierNode<V, T> create(ModifierTree<V, T> tree, Holder<V> object, ResourceKey<? extends Registry<T>> registry) {
    ResourceKey<T> key = ResourceKey.create(registry, object.getKey().location().withSuffix("_root_node"));
    return new RootModifierNode<>(key, tree);
  }
}
