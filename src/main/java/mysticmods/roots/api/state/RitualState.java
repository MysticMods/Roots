package mysticmods.roots.api.state;

import java.util.Locale;

public enum RitualState implements State<RitualState> {
  NONE,
  RITUAL,
  CRAFTING;

  private final String serializedName = name().toLowerCase(Locale.ROOT);

  @Override
  public StateCategory<RitualState> category() {
    return StateCategories.RITUAL;
  }

  @Override
  public String getSerializedName() {
    return serializedName;
  }
}
