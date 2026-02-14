package mysticmods.roots.recipe.transmutation;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.recipe.inventory.RecipeInventory;

public class TransmutationInventory extends RecipeInventory {
  public TransmutationInventory() {
    super(RootsAPI.MAX_TRANSMUTATION_INGREDIENTS);
  }
}
