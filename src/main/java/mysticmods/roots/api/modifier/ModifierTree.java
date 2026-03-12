package mysticmods.roots.api.modifier;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import mysticmods.roots.api.RootsAPI;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class ModifierTree<V, C extends Modifier<V, C>> {
  private final Holder<V> object;
  private final Map<ResourceKey<C>, Holder<C>> modifiers = new Object2ObjectOpenHashMap<>();
  private final Map<ResourceKey<C>, IModifierNode<V, C>> nodes = new Object2ObjectOpenHashMap<>();
  private final List<IModifierNode<V, C>> rootNodes = new ArrayList<>();
  private final Set<IModifierNode<V, C>> allNodes = new ReferenceOpenHashSet<>();

  private final Map<ResourceKey<C>, Set<ResourceKey<C>>> conflicts = new Object2ObjectOpenHashMap<>();

  private final Map<ResourceKey<C>, Set<ResourceKey<C>>> ancestors = new Object2ObjectOpenHashMap<>();
  // TODO: Can positions just be stored in the nodes? Can they be guaranteed to be transmitted?
  private final Map<ResourceKey<C>, ModifierPositionInfo> positions = new Object2ObjectOpenHashMap<>();
  private final Map<ResourceKey<C>, Item> icons = new Object2ObjectOpenHashMap<>();

  private final Set<ResourceKey<C>> missing = new ObjectOpenHashSet<>();

  private final RootModifierNode<V, C> root;

  public ModifierTree(Holder<V> object, ResourceKey<? extends Registry<C>> registry) {
    this.object = object;
    this.root = RootModifierNode.create(object, registry);
  }

  public RootModifierNode<V, C> root() {
    return root;
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
      node.setParent(root);
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

  public void position () {
    ModifierNodePosition.run(this);
    for (IModifierNode<V, C> node : allNodes) {
      positions.putIfAbsent(node.key(), new ModifierPositionInfo(node.x(), node.y()));
    }
  }

  // TODO: Handle this better because it's only in the instance
  protected static <V, T extends Modifier<V, T>> boolean isRestricted(ModifierTree<V, T> tree, IModifierNode<V, T> node) {
    Holder<T> modifier = tree.modifiers.get(node.key());
    if (modifier == null) {
      RootsAPI.LOG.error("Modifier {} is missing from the modifier tree, but has a node.", node.key());
      return false;
    }
    return modifier.value().isRestricted();
  }

  @Nullable
  protected static <V, C extends Modifier<V, C>> Item getIcon (ModifierTree<V, C> tree, ResourceKey<C> key) {
    Item icon = tree.icons.get(key);
    if (icon == null) {
      Holder<C> modifier = tree.modifiers.get(key);
      if (modifier == null) {
        RootsAPI.LOG.error("Modifier {} is missing from the modifier tree, but has a node.", key);
        return null;
      }
      tree.icons.put(key, modifier.value().getIcon());
    }
    if (icon == null) {
      throw new NullPointerException("No icon for key " + key);
    }
    return icon;
  }

  protected static <V, C extends Modifier<V, C>> IModifierNode<V, C> getNode(ModifierTree<V, C> tree, ResourceKey<C> key) {
    IModifierNode<V, C> node = tree.nodes.get(key);
    if (node == null) {
      throw new NullPointerException("No node for key " + key);
    }
    return node;
  }

  public static final StreamCodec<RegistryFriendlyByteBuf, Map<String, ModifierInfo>> MODIFIER_INFO_STREAM_CODEC = ByteBufCodecs.map(Object2ObjectOpenHashMap::new, ByteBufCodecs.STRING_UTF8, ModifierInfo.STREAM_CODEC);

  public class Instance {
    private final Set<ResourceKey<C>> enabledModifiers = new ReferenceOpenHashSet<>();

    public Instance(Set<C> modifierSet) {
      for (C modifier : modifierSet) {
        if (!enable(modifier.builtInRegistryHolder().getKey())) {
          // TODO: Conflicting modifiers in the initial set
        }
      }
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

    public boolean enable(ResourceKey<C> key) {
      IModifierNode<V, C> node = getNode(ModifierTree.this, key);
      return enable(node);
    }

    public boolean disable(IModifierNode<V, C> node) {
      for (IModifierNode<V, C> child : node.children()) {
        if (enabledModifiers.contains(child.key())) {
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

  public record ModifierInfo (ModifierPositionInfo position, Item icon, boolean canEnable, boolean isEnabled, boolean isUnlocked, boolean isRestricted) {
    public static final Codec<ModifierInfo> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        ModifierPositionInfo.CODEC.forGetter(ModifierInfo::position),
        BuiltInRegistries.ITEM.byNameCodec().fieldOf("icon").forGetter(ModifierInfo::icon),
        Codec.BOOL.fieldOf("canEnable").forGetter(ModifierInfo::canEnable),
        Codec.BOOL.fieldOf("isEnabled").forGetter(ModifierInfo::isEnabled),
        Codec.BOOL.fieldOf("isUnlocked").forGetter(ModifierInfo::isUnlocked),
        Codec.BOOL.fieldOf("isRestricted").forGetter(ModifierInfo::isRestricted)
    ).apply(instance, ModifierInfo::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, ModifierInfo> STREAM_CODEC = StreamCodec.composite(
        ModifierPositionInfo.STREAM_CODEC, ModifierInfo::position, ByteBufCodecs.registry(Registries.ITEM), ModifierInfo::icon, ByteBufCodecs.BOOL, ModifierInfo::canEnable, ByteBufCodecs.BOOL, ModifierInfo::isEnabled, ByteBufCodecs.BOOL, ModifierInfo::isUnlocked, ByteBufCodecs.BOOL, ModifierInfo::isRestricted, ModifierInfo::new
    );
  }
}
