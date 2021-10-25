package epicsquid.mysticallib.util;

import javax.annotation.Nullable;

@FunctionalInterface
public interface NullableSupplier<T> {
  @SuppressWarnings("unused")
  static <T> NullableSupplier<T> nullable(Class<? extends T> clazz) {
    return new NullableSupplier<T>() {
      @Nullable
      @Override
      public T get() {
        return null;
      }

      @Override
      public boolean isNull() {
        return true;
      }
    };
  }

  @Nullable
  T get();

  default boolean isNull () {
    return false;
  }
}
