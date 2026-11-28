package mysticmods.roots.integration.jei.categories.defaulted;

import mysticmods.roots.util.Pos;

public interface IPositioned {
  default int getIngredientX(Pos pos) {
    return getIngredientXPadding() + pos.column * getIngredientWidth();
  }

  default int getIngredientY(Pos pos) {
    return getIngredientYPadding() + pos.row * getIngredientHeight();
  }

  default int getOutputX (Pos pos) {
    return getOutputXPadding() + pos.column * getOutputWidth();
  }

  default int getOutputY (Pos pos) {
    return getOutputYPadding() + pos.row * getOutputHeight();
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
