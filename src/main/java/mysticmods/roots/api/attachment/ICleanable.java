package mysticmods.roots.api.attachment;

public interface ICleanable<T extends ICleanable<T>> {
  boolean isEmpty();

  void setDirty(boolean dirty);

  boolean isDirty();

  T copy ();
}
