package mysticmods.roots.api.access;

public interface IShiftAccessor {
  default boolean isShiftKeyDown () {
    return false;
  }
}
