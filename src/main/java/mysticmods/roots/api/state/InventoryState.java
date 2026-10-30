package mysticmods.roots.api.state;

import mysticmods.roots.api.recipe.inventory.RecipeInventory;
import net.neoforged.neoforge.items.IItemHandler;

import java.util.Locale;

public enum InventoryState implements State<InventoryState> {
  EMPTY,
  PARTIAL,
  FULL;

  private final String serializedName = name().toLowerCase(Locale.ROOT);

  public static InventoryState of(IItemHandler handler) {
    int slots = handler.getSlots();
    if (slots == 0) {
      return EMPTY;
    }

    if (handler instanceof RecipeInventory recipeInventory) {
      if (recipeInventory.isEmpty()) {
        return EMPTY;
      }
      return recipeInventory.isFull() ? FULL : PARTIAL;
    }

    int filled = 0;
    for (int i = 0; i < slots; i++) {
      if (!handler.getStackInSlot(i).isEmpty()) {
        filled++;
      }
    }

    if (filled == 0) {
      return EMPTY;
    }

    return filled == slots ? FULL : PARTIAL;
  }

  @Override
  public StateCategory<InventoryState> category() {
    return StateCategories.INVENTORY;
  }

  @Override
  public String getSerializedName() {
    return serializedName;
  }
}
