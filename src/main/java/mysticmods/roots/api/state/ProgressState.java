package mysticmods.roots.api.state;

import java.util.Locale;

public enum ProgressState implements State<ProgressState> {
  NONE,
  CRAFTING,
  CONSUMING_POWER;

  private final String serializedName = name().toLowerCase(Locale.ROOT);

  @Override
  public StateCategory<ProgressState> category() {
    return StateCategories.PROGRESS;
  }

  @Override
  public String getSerializedName() {
    return serializedName;
  }
}
