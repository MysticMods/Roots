package mysticmods.roots.api.state;

import java.util.Locale;

public enum AnimationState implements State<AnimationState> {
  NONE,
  CRAFTING,
  CONSUMING_POWER;

  private final String serializedName = name().toLowerCase(Locale.ROOT);

  @Override
  public StateCategory<AnimationState> category() {
    return StateCategories.ANIMATION;
  }

  @Override
  public String getSerializedName() {
    return serializedName;
  }
}
