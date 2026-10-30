package mysticmods.roots.api.state;

import java.util.Locale;

public enum LockState implements State<LockState> {
  NONE,
  LOCKED;

  private final String serializedName = name().toLowerCase(Locale.ROOT);

  public static LockState of(boolean locked) {
    return locked ? LOCKED : NONE;
  }

  @Override
  public StateCategory<LockState> category() {
    return StateCategories.LOCK;
  }

  @Override
  public String getSerializedName() {
    return serializedName;
  }
}
