package mysticmods.roots.integration;

import mysticmods.roots.integration.jei.RootsJEIPlugin;
import net.neoforged.fml.ModList;

public class ClientIntegrationUtil {
  public static void showRecipesFor(Class<?> clazz) {
    if (ModList.get().isLoaded("jei")) {
      RootsJEIPlugin.showRecipes(clazz);
    }
  }
}
