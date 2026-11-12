package mysticmods.roots.api.property;

public interface WithLayeredProperties<PROPERTY extends LayeredProperty<BASE>, BASE, INSTANCE> {
  PROPERTY getLayer(INSTANCE instance);
}
