package mysticmods.roots.gen;

import com.google.gson.JsonObject;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;

import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

public abstract class BaseProvider implements DataProvider {
  protected static CompletableFuture<?> saveRecipe(CachedOutput pOutput, JsonObject pRecipeJson, Path pPath) {
    return DataProvider.saveStable(pOutput, pRecipeJson, pPath);
  }
}
