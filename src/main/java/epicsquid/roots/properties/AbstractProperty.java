package epicsquid.roots.properties;

public abstract class AbstractProperty<T, V extends AbstractProperty<T, ?>> {
  protected Class<?> type;
  protected String name;
  protected T defaultValue;
  protected String description = "unknown";

  public String getDescription() {
    return description;
  }

  public abstract V setDescription(String description);

  public Class<?> getType() {
    return type;
  }

  public String getName() {
    return name;
  }

  public T getDefaultValue() {
    return defaultValue;
  }

  public abstract boolean validate(T value);

  public boolean hasDefaultValue() {
    return true;
  }

  @SuppressWarnings("unchecked")
  public T cast(Object val) {
    return (T) val;
  }
}
