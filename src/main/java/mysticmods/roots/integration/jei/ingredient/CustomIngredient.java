package mysticmods.roots.integration.jei.ingredient;

import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.ingredients.IIngredientRenderer;
import mezz.jei.api.ingredients.IIngredientType;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public record CustomIngredient<I> (IIngredientType<I> type, List<I> ingredients, @Nullable IIngredientRenderer<I> renderer) {
  public CustomIngredient (IIngredientType<I> type, List<I> ingredient) {
    this(type, ingredient, null);
  }

  public CustomIngredient (IIngredientType<I> type, I ingredient) {
    this(type, ingredient, null);
  }

  public CustomIngredient (IIngredientType<I> type, I ingredient, @Nullable IIngredientRenderer<I> renderer) {
    this(type, List.of(ingredient), renderer);
  }

  public void fill (IRecipeSlotBuilder slotBuilder) {
    var x = slotBuilder.addIngredients(type, ingredients);
    if (renderer != null) {
      x.setCustomRenderer(type, renderer);
    }
  }
}
