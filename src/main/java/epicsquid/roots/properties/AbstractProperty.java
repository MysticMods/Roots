package epicsquid.roots.properties;

import java.util.function.Predicate;

public abstract class AbstractProperty<T, V extends AbstractProperty<T, ?>> {
  protected Class<?> type;
  protected String name;
  protected T defaultValue;
  protected Predicate<T> validator = null;
  protected String description = "unknown";
  protected String bounds = "unknown";

  public String getDescription() {
    return description;
  }

  public abstract V setDescription(String description);

  public abstract V setValidation(Predicate<T> validator, String bounds);

  public Class<?> getType() {
    return type;
  }

  public String getName() {
    return name;
  }

  public T getDefaultValue() {
    return defaultValue;
  }

  public boolean validate(T value) {
    if (validator == null) {
      return true;
    }

    return validator.test(value);
  }

  public String getValidationBounds() {
    return bounds;
  }

  public boolean hasDefaultValue() {
    return true;
  }

  @SuppressWarnings("unchecked")
  public T cast(Object val) {
    return (T) val;
  }
}
