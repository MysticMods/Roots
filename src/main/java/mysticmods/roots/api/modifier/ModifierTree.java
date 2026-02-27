package mysticmods.roots.api.modifier;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class ModifierTree<V, C extends Modifier<V, C>> {
  private static final Set<ResourceKey<?>> RESTRICTED_MODIFIERS = new ReferenceOpenHashSet<>();

  private final Holder<V> object;
  private final Map<ResourceKey<C>, C> modifiers = new Object2ObjectOpenHashMap<>();
  private final Map<ResourceKey<C>, ModifierNode<V, C>> nodes = new Object2ObjectOpenHashMap<>();
  private final Set<ModifierNode<V, C>> rootNodes = new ReferenceOpenHashSet<>();
  private final Set<ModifierNode<V, C>> allNodes = new ReferenceOpenHashSet<>();

  private final Set<ResourceKey<C>> parents = new ObjectOpenHashSet<>();

  public ModifierTree(Holder<V> object) {
    this.object = object;
  }

  public Set<ResourceKey<C>> validateParents () {
    Set<ResourceKey<C>> missing = new HashSet<>();
    for (ResourceKey<C> parent : parents) {
      if (!modifiers.containsKey(parent)) {
        missing.add(parent);
      }
    }
    return missing;
  }

  public ModifierTree<V, C>.Instance validator (ModifierSet<V, C, ?> modifiers) {
    return new Instance(modifiers);
  }

  public boolean addModifier(C modifier) {
    return addModifier(modifier.builtInRegistryHolder());
  }

  public boolean addModifier(Holder<C> modifier) {
    C mod = modifier.value();
    if (!mod.getApplicable().equals(this.object.getKey())) {
      return false;
    }

    if (modifiers.containsKey(modifier.getKey())) {
      return true; // this was previously false but it's already added so it doesn't matter
    }

    ModifierNode<V, C> node = ModifierNode.create(modifier.getKey());
    allNodes.add(node);
    modifiers.put(modifier.getKey(), mod);
    nodes.put(modifier.getKey(), node);

    if (mod.getParent() == null) {
      rootNodes.add(node);
    } else {
      ModifierNode<V, C> parentNode = ModifierNode.create(mod.getParent());
      parentNode.addChild(node);
      parents.add(mod.getParent());
    }

    return true;
  }

  private ModifierNode<V, C> getNode(ResourceKey<C> key) {
    ModifierNode<V, C> node = nodes.get(key);
    if (node == null) {
      throw new NullPointerException("No node for key " + key);
    }
    return node;
  }

  public Iterable<ModifierNode<V, C>> roots() {
    return rootNodes;
  }

  public Iterable<ModifierNode<V, C>> all() {
    return allNodes;
  }

  public static boolean isRestricted(ModifierNode<?, ?> node) {
    return RESTRICTED_MODIFIERS.contains(node.modifier());
  }

  public static boolean isRestricted(ResourceKey<?> modifier) {
    return RESTRICTED_MODIFIERS.contains(modifier);
  }

  public static boolean isRestricted(Modifier<?, ?> modifier) {
    return RESTRICTED_MODIFIERS.contains(modifier.builtInRegistryHolder().getKey());
  }

  public static boolean isRestricted(Holder<Modifier<?, ?>> modifier) {
    return RESTRICTED_MODIFIERS.contains(modifier.getKey());
  }

  public static void restrictModifier(ResourceKey<?> modifier) {
    RESTRICTED_MODIFIERS.add(modifier);
  }

  public static void restrictModifier(Modifier<?, ?> modifier) {
    RESTRICTED_MODIFIERS.add(modifier.builtInRegistryHolder().getKey());
  }

  public static void restrictModifier(Holder<Modifier<?, ?>> modifier) {
    RESTRICTED_MODIFIERS.add(modifier.getKey());
  }

  public static void restrictModifier(ModifierNode<?, ?> node) {
    RESTRICTED_MODIFIERS.add(node.modifier());
  }

  public class Instance {
    private final Set<ResourceKey<C>> enabledModifiers = new ReferenceOpenHashSet<>();

    public Instance(Set<C> modifierSet) {
      for (C modifier : modifierSet) {
        enable(modifier.builtInRegistryHolder().getKey());
      }
    }

    public Instance enable(ModifierNode<V, C> node) {
      if (isRestricted(node)) {
        return disable(node);
      }

      enabledModifiers.add(node.modifier());
      ModifierNode<V, C> parent = node.parent();
      if (parent != null && !enabledModifiers.contains(parent.modifier())) {
        enable(parent);
      }
      return this;
    }

    public Instance enable(ResourceKey<C> key) {
      ModifierNode<V, C> node = getNode(key);
      return enable(node);
    }

    public Instance disable(ModifierNode<V, C> node) {
      for (ModifierNode<V, C> child : node.children()) {
        if (enabledModifiers.contains(child.modifier())) {
          disable(child);
        }
      }
      return this;
    }

    public Instance disable(ResourceKey<C> key) {
      return disable(getNode(key));
    }

    public Instance copy () {
      Instance copy = new Instance(new ObjectOpenHashSet<>());
      copy.enabledModifiers.addAll(this.enabledModifiers);
      return copy;
    }

    public Set<C> modifiersSet () {
      Set<C> mods = new ObjectOpenHashSet<>();
      for (ResourceKey<C> key : enabledModifiers) {
        mods.add(modifiers.get(key));
      }
      return mods;
    }
  }
}
