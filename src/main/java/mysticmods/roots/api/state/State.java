package mysticmods.roots.api.state;

import net.minecraft.util.StringRepresentable;

public interface State<V extends Enum<V> & State<V>> extends StringRepresentable {
  StateCategory<V> category();
}
