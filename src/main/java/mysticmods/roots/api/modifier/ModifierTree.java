package mysticmods.roots.api.modifier;

import com.google.common.collect.ImmutableSet;
import com.sun.jna.platform.unix.Resource;
import it.unimi.dsi.fastutil.objects.*;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.util.SetUtils;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class ModifierTree<V, C extends Modifier<V, C>> {
  private static final Object2IntMap<ResourceKey<?>> KEY_TO_DEPTH_CACHE = new Object2IntOpenHashMap<>();

  public static int getDepth (ResourceKey<?> key) {
    return KEY_TO_DEPTH_CACHE.getOrDefault(key, 0);
  }

  private final Holder<V> object;
  private final Map<ResourceKey<C>, Holder<C>> modifiers = new Object2ObjectOpenHashMap<>();
  private final Map<ResourceKey<C>, IModifierNode<V, C>> nodes = new Object2ObjectOpenHashMap<>();
  private final List<IModifierNode<V, C>> rootNodes = new ArrayList<>();
  private final Set<IModifierNode<V, C>> allNodes = new ReferenceOpenHashSet<>();

  private final Map<ResourceKey<C>, Set<ResourceKey<C>>> conflicts = new Object2ObjectOpenHashMap<>();

  private final Map<ResourceKey<C>, Set<ResourceKey<C>>> ancestors = new Object2ObjectOpenHashMap<>();

  private final Set<ResourceKey<C>> missing = new ObjectOpenHashSet<>();

  private final RootModifierNode<V, C> root;

  private final Set<ResourceKey<C>> resetThisBuild = new ObjectOpenHashSet<>();

  public ModifierTree(Holder<V> object, ResourceKey<? extends Registry<C>> registry) {
    this.object = object;
    this.root = RootModifierNode.create(this, object, registry);
  }

  private IModifierNode<V, C> getOrResetNode(ResourceKey<C> key) {
    IModifierNode<V, C> node = ModifierNode.create(key);
    if (resetThisBuild.add(key)) {
      node.reset();
    }
    return node;
  }

  public Holder<V> getObject() {
    return object;
  }

  public RootModifierNode<V, C> root() {
    return root;
  }

  // This not being empty is a sign of invalid modifiers
  public Set<ResourceKey<C>> validateParents() {
    missing.removeIf(modifiers::containsKey);
    return missing;
  }

  public C getModifier(ResourceKey<C> key) {
    Holder<C> modifier = modifiers.get(key);
    if (modifier == null) {
      throw new NullPointerException("No modifier for key " + key);
    }
    return modifier.value();
  }

  public C getModifier(ModifierNode<V, C> node) {
    return getModifier(node.key());
  }

  // TODO: Server-side handling of 'requires unlock' modifiers
  public ModifierTree<V, C>.Instance instance(Set<C> modifiers, Set<C> grantedModifiers) {
    return new Instance(modifiers, grantedModifiers);
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

    IModifierNode<V, C> node = getOrResetNode(modifier.getKey());
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
      mod.setDepth(0);
      node.setDepth(0);
      KEY_TO_DEPTH_CACHE.put(modifier.getKey(), 0);
      rootNodes.add(node);
    } else {
      IModifierNode<V, C> parentNode = getOrResetNode(mod.getParent());
      parentNode.addChild(node);
      parents.add(mod.getParent());
      node.setParent(parentNode);
      int myDepth = parentNode.depth() + 1;
      mod.setDepth(myDepth);
      node.setDepth(myDepth);
      KEY_TO_DEPTH_CACHE.put(modifier.getKey(), myDepth);
      if (modifiers.get(mod.getParent()) == null) {
        missing.add(mod.getParent());
      }
    }

    return true;
  }

  public Set<IModifierNode<V, C>> all() {
    return allNodes;
  }

  private Set<C> allModifiers = null;

  public Set<C> allModifiers() {
    if (allModifiers == null) {
      allModifiers = modifiers.values().stream().map(Holder::value).collect(ImmutableSet.toImmutableSet());
    }
    return allModifiers;
  }

  public List<IModifierNode<V, C>> rootNodes() {
    return rootNodes;
  }

  public void position() {
    ModifierNodePosition.run(this);
  }

  // TODO: Handle this better because it's only in the instance
  protected static <V, T extends Modifier<V, T>> boolean isRestricted(ModifierTree<V, T> tree, IModifierNode<V, T> node) {
    Holder<T> modifier = tree.modifiers.get(node.key());
    if (modifier == null && node != tree.root()) {
      RootsAPI.LOG.error("Modifier {} is missing from the modifier tree, but has a node.", node.key());
      return false;
    }
    var tag = RootsAPI.getInstance().getRestrictedTagFor(modifier.getKey());
    if (tag == null) {
      RootsAPI.LOG.error("Modifier {} does not have an associated restriction tag!", node.key());
      return false;
    }
    return modifier.is(tag);
  }

  @NotNull
  public static <V, C extends Modifier<V, C>> ItemStack getIcon(ModifierTree<V, C> tree, IModifierNode<V, C> node) {
    Holder<C> modifier = tree.modifiers.get(node.key());
    if (modifier == null && node != tree.root()) {
      RootsAPI.LOG.error("Modifier {} is missing from the modifier tree, but has a node.", node.key());
      return ItemStack.EMPTY;
    }
    return modifier.value().getIcon();
  }

  protected static <V, C extends Modifier<V, C>> IModifierNode<V, C> getNode(ModifierTree<V, C> tree, ResourceKey<C> key) {
    IModifierNode<V, C> node = tree.nodes.get(key);
    if (node == null) {
      throw new NullPointerException("No node for key " + key);
    }
    return node;
  }

  public static <V, C extends Modifier<V, C>> Holder<C> getHolder(ModifierTree<V, C> tree, ResourceKey<C> key) {
    var result = tree.modifiers.get(key);
    if (result == null) {
      throw new NullPointerException("No holder for key " + key);
    }
    return result;
  }

  public static final StreamCodec<RegistryFriendlyByteBuf, Map<String, ModifierInfo>> MODIFIER_INFO_STREAM_CODEC = ByteBufCodecs.map(Object2ObjectOpenHashMap::new, ByteBufCodecs.STRING_UTF8, ModifierInfo.STREAM_CODEC);

  public class Instance {
    private final Set<ResourceKey<C>> enabledModifiers = new ReferenceOpenHashSet<>();
    private final Set<C> grantedModifiers;
    private Map<ResourceKey<C>, ModifierInfo> modifierInfoCache = null;

    public Instance(Set<C> modifierSet, Set<C> grantedModifiers) {
      for (C modifier : modifierSet) {
        if (!enable(modifier.getSelf())) {
          // TODO: Conflicting modifiers in the initial set
          // TODO: This should probably throw a catchable error
        }
      }
      this.grantedModifiers = grantedModifiers;
    }

    public ModifierTree<V, C> tree() {
      return ModifierTree.this;
    }

    public boolean enable(IModifierNode<V, C> node) {
      if (isRestricted(ModifierTree.this, node)) {
        return disable(node);
      }

      Set<ResourceKey<C>> conflicts = ModifierTree.this.conflicts.get(node.key());
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
      if (parent != null && !enabledModifiers.contains(parent.key())) {
        // TODO: Handle parental conflicts with children
        if (!enable(parent)) {
          return false;
        }
      }

      enabledModifiers.add(node.key());
      return true;
    }

    public boolean enable(C value) {
      return enable(value.getSelf());
    }

    public boolean enable(ResourceKey<C> key) {
      IModifierNode<V, C> node = getNode(ModifierTree.this, key);
      return enable(node);
    }

    public boolean disable(C value) {
      return disable(value.getSelf());
    }

    public boolean disable(IModifierNode<V, C> node) {
      for (IModifierNode<V, C> child : node.children()) {
        if (enabledModifiers.contains(child.key())) {
          if (!disable(child)) {
            return false;
          }
        }
      }
      enabledModifiers.remove(node.key());
      return true;
    }

    public boolean disable(ResourceKey<C> key) {
      return disable(getNode(ModifierTree.this, key));
    }

    public Instance copy() {
      Instance copy = new Instance(new ObjectOpenHashSet<>(), new HashSet<>(grantedModifiers));
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

    public boolean enabled(ResourceKey<C> key) {
      return enabledModifiers.contains(key);
    }

    public boolean enabled(IModifierNode<V, C> node) {
      return enabled(node.key());
    }

    public Map<ResourceKey<C>, ModifierInfo> getModifierInfoCache() {
      if (modifierInfoCache == null) {
        Map<ResourceKey<C>, ModifierInfo> infoMap = new Object2ObjectOpenHashMap<>();
        for (ResourceKey<C> key : modifiers.keySet()) {
          Set<ResourceKey<C>> cons = ModifierTree.this.conflicts.get(key);

          IModifierNode<V, C> node = getNode(ModifierTree.this, key);

          boolean canEnable = !SetUtils.containsAny(enabledModifiers, cons);
          if (canEnable) {
            IModifierNode<V, C> parent = node.parent();
            while (parent != null) {
              canEnable = !SetUtils.containsAny(enabledModifiers, ModifierTree.this.conflicts.get(parent.key()));
              if (canEnable) {
                parent = parent.parent();
              } else {
                break;
              }
            }
          }
          boolean isEnabled = enabledModifiers.contains(key);
          boolean isUnlocked;
          if (this.grantedModifiers == null) {
            isUnlocked = true;
          } else {
            isUnlocked = this.grantedModifiers.contains(modifiers.get(key).value());
          }
          boolean isRestricted = isRestricted(ModifierTree.this, node);
          infoMap.put(key, new ModifierInfo(canEnable, isEnabled, isUnlocked, isRestricted));
        }
        this.modifierInfoCache = infoMap;
      }
      return this.modifierInfoCache;
    }

    public ModifierInfo getModifierInfo(ResourceKey<C> key) {
      return getModifierInfoCache().get(key);
    }

    public ModifierInfo getModifierInfo(IModifierNode<V, C> node) {
      return getModifierInfo(node.key());
    }
  }
}
