package mysticmods.roots.client;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.recipe.AnimalHarvestRecipe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ClientRecipes {
  public static List<AnimalHarvestRecipe> ANIMAL_HARVEST_RECIPES = new ArrayList<>();

  public static void clear() {
    RootsAPI.LOG.error("Animal Harvest recipes cleared");
    ANIMAL_HARVEST_RECIPES = new ArrayList<>();
  }
}
