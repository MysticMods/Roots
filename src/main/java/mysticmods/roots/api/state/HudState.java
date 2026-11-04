package mysticmods.roots.api.state;

import java.util.Locale;

public enum HudState implements State<HudState> {
  NONE;

  private final String serializedName = name().toLowerCase(Locale.ROOT);

  @Override
  public StateCategory<HudState> category() {
    return StateCategories.HUD;
  }

  @Override
  public String getSerializedName() {
    return serializedName;
  }
}
