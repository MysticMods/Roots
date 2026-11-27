package mysticmods.roots.integration.jei.categories.defaulted;

import mysticmods.roots.integration.jei.ingredient.CustomIngredient;

import java.util.Collections;
import java.util.List;

public interface IPositioned {
  default List<CustomIngredient<?>> gatherInputs () {
    return Collections.emptyList();
  }

  default List<CustomIngredient<?>> gatherOutputs () {
    return Collections.emptyList();
  }

  default int getIngredientX (Pos pos) {
    return getIngredientXPadding() + pos.column * getIngredientWidth();
  }

  default int getIngredientY (Pos pos) {
    return getIngredientYPadding() + pos.row * getIngredientHeight();
  }

  int getIngredientWidth();

  int getIngredientXPadding();

  int getIngredientYPadding();

  int getIngredientHeight();

  int getIngredientColumnCount();

  int getOutputWidth();

  int getOutputHeight();

  int getOutputYPadding();

  int getOutputXPadding();

  int getOutputColumnCount();
}
