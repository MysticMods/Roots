package mysticmods.roots.gen;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import mysticmods.roots.api.RootsAPI;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public abstract class BaseProvider implements DataProvider {
  protected static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();

  protected static void saveRecipe(HashCache pCache, JsonObject pRecipeJson, Path pPath) {
    try {
      String s = GSON.toJson(pRecipeJson);
      String s1 = DataProvider.SHA1.hashUnencodedChars(s).toString();
      if (!Objects.equals(pCache.getHash(pPath), s1) || !Files.exists(pPath)) {
        Files.createDirectories(pPath.getParent());
        BufferedWriter bufferedwriter = Files.newBufferedWriter(pPath);

        try {
          bufferedwriter.write(s);
        } catch (Throwable throwable1) {
          try {
            bufferedwriter.close();
          } catch (Throwable throwable) {
            throwable1.addSuppressed(throwable);
          }

          throw throwable1;
        }

        bufferedwriter.close();
      }

      pCache.putNew(pPath, s1);
    } catch (IOException ioexception) {
      RootsAPI.LOG.error("Couldn't save recipe {}", pPath, ioexception);
    }
  }
}
