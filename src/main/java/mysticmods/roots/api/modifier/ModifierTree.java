package mysticmods.roots.api.modifier;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;

import java.util.Map;
import java.util.Set;

public class ModifierTree<T> {
  private final Holder<T> object;
  private final Map<ResourceKey<Modifier>, Modifier> modifiers = new Object2ObjectOpenHashMap<>();
  private final Set<ModifierNode> rootNodes = new ObjectOpenHashSet<>();
  private final Set<ModifierNode> allNodes = new ObjectOpenHashSet<>();

  public ModifierTree(Holder<T> object) {
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
