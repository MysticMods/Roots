package mysticmods.roots.api.modifier;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;

import java.util.Map;
import java.util.Set;

public class ModifierTree<V, T extends Modifier<V, T>> {
  private final Holder<V> object;
  private final Map<ResourceKey<T>, T> modifiers = new Object2ObjectOpenHashMap<>();
  private final Set<ModifierNode<V, T>> rootNodes = new ObjectOpenHashSet<>();
  private final Set<ModifierNode<V, T>> allNodes = new ObjectOpenHashSet<>();

  public ModifierTree(Holder<V> object) {
    this.object = object;
  }

  public boolean addModifier(Holder<Modifier> modifier) {
    if (modifiers.containsKey(modifier.getKey())) {
      return false; // ???
    }

    Modifier mod = modifier.value();
    if (!mod.getParents().isEmpty()) {
      //
    }

    if (modifier.value().canApply(object)) {
      ModifierNode node = new ModifierNode(modifier.getKey());

    }
  }


}
