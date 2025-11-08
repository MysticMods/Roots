package mysticmods.roots.api.modifier;

import it.unimi.dsi.fastutil.objects.*;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;

import java.util.Map;
import java.util.Set;

public class ModifierTree<V, C extends Modifier<V, C>, T extends Modifier.ModifierRecord<V, C>> {
  private final Holder<V> object;
  private final Map<ResourceKey<C>, T> modifiers = new Object2ObjectOpenHashMap<>();
  private final Map<ResourceKey<C>, ModifierNode<V, C>> nodes = new Object2ObjectOpenHashMap<>();
  private final Set<ModifierNode<V, C>> rootNodes = new ReferenceOpenHashSet<>();
  private final Set<ModifierNode<V, C>> allNodes = new ReferenceOpenHashSet<>();

  public ModifierTree(Holder<V> object) {
    this.object = object;
  }

  public boolean addModifier (C modifier) {
    return addModifier(modifier.builtInRegistryHolder());
  }

  public boolean addModifier(Holder<C> modifier) {
    if (modifiers.containsKey(modifier.getKey())) {
      return false; // ???
    }

    C mod = modifier.value();
    if (!mod.getApplicable().equals(this.object.getKey())) {
      return false;
    }

    ModifierNode<V, C> node = ModifierNode.create(modifier.getKey());
    allNodes.add(node);
    //noinspection unchecked
    modifiers.put(modifier.getKey(), (T) mod.record());
    nodes.put(modifier.getKey(), node);

    if (mod.getParent() == null) {
      rootNodes.add(node);
      // do something if the parent isn't contained in this already
    } else {
      ModifierNode<V, C> parentNode = ModifierNode.create(mod.getParent());
      parentNode.addChild(node);
    }

    return true;
  }

  public ModifierNode<V, C> getNode (ResourceKey<C> key) {
    ModifierNode<V, C> node = nodes.get(key);
    if (node == null) {
      throw new NullPointerException("No node for key " + key);
    }
    return node;
  }

  public Iterable<ModifierNode<V, C>> roots () {
    return rootNodes;
  }

  public void resolveChildren () {
    for (ModifierNode<V, C> node : allNodes) {
      C record = modifiers.get(node.modifier()).modifier().value();
      ModifierNode<V, C> parentNode = getNode(record.getParent());
      parentNode.addChild(node);
    }
  }
}
