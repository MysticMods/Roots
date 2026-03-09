package mysticmods.roots.api.modifier;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.objects.ReferenceArrayList;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class ModifierTree<V, C extends Modifier<V, C>> implements IModifierNode<V, C> {
  private static final Set<ResourceKey<?>> RESTRICTED_MODIFIERS = new ReferenceOpenHashSet<>();

  private final Holder<V> object;
  private final Map<ResourceKey<C>, Holder<C>> modifiers = new Object2ObjectOpenHashMap<>();
  private final Map<ResourceKey<C>, IModifierNode<V, C>> nodes = new Object2ObjectOpenHashMap<>();
  private final List<IModifierNode<V, C>> rootNodes = new ArrayList<>();
  private final Set<IModifierNode<V, C>> allNodes = new ReferenceOpenHashSet<>();

  private final Map<ResourceKey<C>, Set<ResourceKey<C>>> conflicts = new Object2ObjectOpenHashMap<>();

  private final Map<ResourceKey<C>, Set<ResourceKey<C>>> ancestors = new Object2ObjectOpenHashMap<>();
  private final Map<ResourceKey<C>, ModifierPositionInfo> positions = new Object2ObjectOpenHashMap<>();

  private final Set<ResourceKey<C>> missing = new ObjectOpenHashSet<>();

  public ModifierTree(Holder<V> object) {
    this.object = object;
  }

  // This not being empty is a sign of invalid modifiers
  public Set<ResourceKey<C>> validateParents() {
    missing.removeIf(modifiers::containsKey);
    return missing;
  }

  // TODO: Conflict validation: conflicting modifiers cannot be parents of the modifier. Basically, ensure there are no cycles in the graph.

  // This being called "validator" makes little sense
  public ModifierTree<V, C>.Instance validator(ModifierSet<V, C, ?> modifiers) {
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

    missing.remove(modifier.getKey());

    IModifierNode<V, C> node = ModifierNode.create(modifier.getKey());
    allNodes.add(node);
    modifiers.put(modifier.getKey(), modifier);
    nodes.put(modifier.getKey(), node);

    conflicts.computeIfAbsent(modifier.getKey(), k -> new HashSet<>())
        .addAll(mod.getConflicts());

    // Ensure back-references to all conflicts are resolved
    for (ResourceKey<C> conflict : mod.getConflicts()) {
      conflicts.computeIfAbsent(conflict, k -> new HashSet<>())
          .add(modifier.getKey());
    }

    var parents = ancestors.computeIfAbsent(modifier.getKey(), k -> new HashSet<>());
    if (mod.getParent() == null) {
      node.setParent(this);
      rootNodes.add(node);
    } else {
      IModifierNode<V, C> parentNode = ModifierNode.create(mod.getParent());
      parentNode.addChild(node);
      parents.add(mod.getParent());
      if (modifiers.get(mod.getParent()) == null) {
        missing.add(mod.getParent());
      }
    }

    return true;
  }

  public Set<IModifierNode<V, C>> all() {
    return allNodes;
  }

  public static boolean isRestricted(IModifierNode<?, ?> node) {
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

  public static void restrictModifier(IModifierNode<?, ?> node) {
    RESTRICTED_MODIFIERS.add(node.modifier());
  }

  @Override
  public @Nullable ResourceKey<C> modifier() {
    return null;
  }

  @Override
  public @Nullable IModifierNode<V, C> parent() {
    return null;
  }

  @Override
  public IModifierNode<V, C> setParent(IModifierNode<V, C> parent) {
    return this;
  }

  @Override
  public List<IModifierNode<V, C>> children() {
    return rootNodes;
  }

  @Override
  public void addChild(IModifierNode<V, C> child) {
    // NO-OP
  }

  public void position () {
    ModifierNodePosition.run(this);
  }

  protected static <V, C extends Modifier<V, C>> IModifierNode<V, C> getNode(ModifierTree<V, C> tree, ResourceKey<C> key) {
    IModifierNode<V, C> node = tree.nodes.get(key);
    if (node == null) {
      throw new NullPointerException("No node for key " + key);
    }
    return node;
  }


  public class Instance {
    private final Set<ResourceKey<C>> enabledModifiers = new ReferenceOpenHashSet<>();

    private boolean invalid = false;

    public Instance(Set<C> modifierSet) {
      for (C modifier : modifierSet) {
        if (!enable(modifier.builtInRegistryHolder().getKey())) {
          // TODO: Conflicting modifiers in the initial set
          invalid = true;
        }
      }
    }

    public boolean isValid() {
      return !invalid;
    }

    public boolean enable(IModifierNode<V, C> node) {
      if (isRestricted(node)) {
        return disable(node);
      }

      Set<ResourceKey<C>> conflicts = ModifierTree.this.conflicts.get(node.modifier());
      if (conflicts != null) {
        for (ResourceKey<C> conflict : conflicts) {
          if (enabledModifiers.contains(conflict)) {
            // If we're unable to disable conflicts, we just fail
            if (!disable(getNode(ModifierTree.this, conflict))) {
              return false;
            }
          }
        }
      }

      IModifierNode<V, C> parent = node.parent();
      if (parent != null && !enabledModifiers.contains(parent.modifier())) {
        // TODO: Handle parental conflicts with children
        if (!enable(parent)) {
          return false;
        }
      }

      enabledModifiers.add(node.modifier());
      return true;
    }

    public boolean enable(ResourceKey<C> key) {
      IModifierNode<V, C> node = getNode(ModifierTree.this, key);
      return enable(node);
    }

    public boolean disable(IModifierNode<V, C> node) {
      for (IModifierNode<V, C> child : node.children()) {
        if (enabledModifiers.contains(child.modifier())) {
          if (!disable(child)) {
            return false;
          }
        }
      }
      return true;
    }

    public boolean disable(ResourceKey<C> key) {
      return disable(getNode(ModifierTree.this, key));
    }

    public Instance copy() {
      Instance copy = new Instance(new ObjectOpenHashSet<>());
      copy.enabledModifiers.addAll(this.enabledModifiers);
      return copy;
    }

    public Set<C> modifiersSet() {
      Set<C> mods = new ObjectOpenHashSet<>();
      for (ResourceKey<C> key : enabledModifiers) {
        mods.add(modifiers.get(key).value());
      }
      return mods;
    }
  }
}
