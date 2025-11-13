package mysticmods.roots.api.modifier;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;

import java.util.Map;
import java.util.Set;

public class ModifierTree<V, C extends Modifier<V, C>> {
  private static final Set<ResourceKey<?>> DISABLED_MODIFIERS = new ReferenceOpenHashSet<>();

  private final Holder<V> object;
  private final Map<ResourceKey<C>, C> modifiers = new Object2ObjectOpenHashMap<>();
  private final Map<ResourceKey<C>, ModifierNode<V, C>> nodes = new Object2ObjectOpenHashMap<>();
  private final Set<ModifierNode<V, C>> rootNodes = new ReferenceOpenHashSet<>();
  private final Set<ModifierNode<V, C>> allNodes = new ReferenceOpenHashSet<>();

  public ModifierTree(Holder<V> object) {
    this.object = object;
  }

  public ModifierTree<V, C>.Instance validator (ModifierSet<V, C, ?> modifierSet) {
    return new Instance(modifierSet);
  }

  public boolean validate(ModifierSet<V, C, ?> modifierSet) {
    if (modifierSet.isEmpty()) {
      return true;
    }
    C firstElement = modifierSet.firstElement();
    if (firstElement == null) {
      return true; // Shouldn't happen?
    }
    if (!firstElement.getApplicable().equals(object.getKey())) {
      return false;
    }

    if (!modifierSet.validate()) {
      return false;
    }

    // If an element is enabled here but its children aren't enabled, that's invalid
    Instance instance = new Instance(modifierSet);


    return true;
  }

  public boolean addModifier(C modifier) {
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
    modifiers.put(modifier.getKey(), mod);
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

  public ModifierNode<V, C> getNode(ResourceKey<C> key) {
    ModifierNode<V, C> node = nodes.get(key);
    if (node == null) {
      throw new NullPointerException("No node for key " + key);
    }
    return node;
  }

  public Iterable<ModifierNode<V, C>> roots() {
    return rootNodes;
  }

  public static boolean isDisabled(ModifierNode<?, ?> node) {
    return DISABLED_MODIFIERS.contains(node.modifier());
  }

  public static boolean isDisabled(ResourceKey<?> modifier) {
    return DISABLED_MODIFIERS.contains(modifier);
  }

  public static boolean isDisabled(Modifier<?, ?> modifier) {
    return DISABLED_MODIFIERS.contains(modifier.builtInRegistryHolder().getKey());
  }

  public static boolean isDisabled(Holder<Modifier<?, ?>> modifier) {
    return DISABLED_MODIFIERS.contains(modifier.getKey());
  }

  public static void disableModifier(ResourceKey<?> modifier) {
    DISABLED_MODIFIERS.add(modifier);
  }

  public static void disableModifier(Modifier<?, ?> modifier) {
    DISABLED_MODIFIERS.add(modifier.builtInRegistryHolder().getKey());
  }

  public static void disableModifier(Holder<Modifier<?, ?>> modifier) {
    DISABLED_MODIFIERS.add(modifier.getKey());
  }

  public static void disableModifier(ModifierNode<?, ?> node) {
    DISABLED_MODIFIERS.add(node.modifier());
  }

  public class Instance {
    private final Set<ResourceKey<C>> enabledModifiers = new ReferenceOpenHashSet<>();

    public Instance(Set<C> modifierSet) {
      for (C modifier : modifierSet) {
        enable(modifier.builtInRegistryHolder().getKey());
      }
    }

    public Instance enable(ModifierNode<V, C> node) {
      if (isDisabled(node)) {
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
