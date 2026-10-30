package mysticmods.roots.api.state;

import java.util.Locale;

public enum RecipeState implements State<RecipeState> {
  NONE,
  MATCHING;

  private final String serializedName = name().toLowerCase(Locale.ROOT);

  public static RecipeState of(boolean matching) {
    return matching ? MATCHING : NONE;
  }

  @Override
  public StateCategory<RecipeState> category() {
    return StateCategories.RECIPE;
  }

  @Override
  public String getSerializedName() {
    return serializedName;
  }
}
