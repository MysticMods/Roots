package mysticmods.roots.gen;

import com.google.gson.JsonObject;
import mysticmods.roots.api.RootsAPI;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;

import java.io.IOException;
import java.nio.file.Path;

public abstract class BaseProvider implements DataProvider {
  protected static void saveRecipe(CachedOutput pOutput, JsonObject pRecipeJson, Path pPath) {
    try {
      DataProvider.saveStable(pOutput, pRecipeJson, pPath);
    } catch (IOException ioexception) {
      RootsAPI.LOG.error("Couldn't save recipe {}", pPath, ioexception);
    }
  }
}
