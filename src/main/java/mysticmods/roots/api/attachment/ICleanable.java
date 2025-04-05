package mysticmods.roots.api.attachment;

public interface ICleanable {
  boolean isEmpty();

  boolean isDirty();

  void setDirty(boolean dirty);
}
