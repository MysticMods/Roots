package mysticmods.roots.api.modifier;

import net.minecraft.resources.ResourceKey;

import javax.annotation.Nullable;
import java.util.List;

public interface IModifierNode<V, T extends IModifier<V, T>> {
  @Nullable
  ResourceKey<T> modifier();

  @Nullable
  IModifierNode<V, T> parent();

  IModifierNode<V, T> setParent(IModifierNode<V, T> parent);

  List<IModifierNode<V, T>> children();

  default void addChild(IModifierNode<V, T> child) {
    // NO-OP
  }

  default void setLocation(float x, float y) {
    // NO-OP
  }

  default float x () {
    return 0;
  }

  default float y () {
    return 0;
  }
}
