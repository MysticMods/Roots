package mysticmods.roots.api.blockentity;

public interface ClearableBlockEntity {
  void clearContents();

  boolean canClear();
}
