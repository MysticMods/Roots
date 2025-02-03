package mysticmods.roots.api.attachment;

public interface ICleanable {
  boolean isEmpty ();
  void setDirty (boolean dirty);
  boolean isDirty ();
}
