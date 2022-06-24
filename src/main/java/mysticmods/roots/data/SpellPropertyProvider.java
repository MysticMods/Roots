package mysticmods.roots.data;

import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.property.Property;
import mysticmods.roots.init.ModRegistries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;
import net.minecraft.resources.ResourceLocation;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Set;

public class SpellPropertyProvider implements DataProvider {
  private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();

  protected final DataGenerator generator;

  public SpellPropertyProvider(DataGenerator generator) {
    this.generator = generator;
  }

  @Override
  public void run(HashCache pCache) throws IOException {
    Path path = this.generator.getOutputFolder();
    Set<ResourceLocation> set = Sets.newHashSet();
    for (Property.SpellProperty<?> prop : ModRegistries.SPELL_PROPERTY_REGISTRY.get().getValues()) {
      ResourceLocation id = ModRegistries.SPELL_PROPERTY_REGISTRY.get().getKey(prop);
      if (!set.add(id)) {
        throw new IllegalStateException("Duplicate recipe " + id);
      } else {
        JsonObject recipe = new JsonObject();
        recipe.addProperty("spell", prop.getSpell().location().toString());
        recipe.add("value", prop.serializeValueJson());
        recipe.add("default_value", prop.serializeDefaultValueJson());
        saveRecipe(pCache, recipe, path.resolve("data/" + id.getNamespace() + "/properties/spell/" + id.getPath() + ".json"));
      }
    }
  }

  // TODO: abstract this up a level
  private static void saveRecipe(HashCache pCache, JsonObject pRecipeJson, Path pPath) {
    try {
      String s = GSON.toJson((JsonElement) pRecipeJson);
      String s1 = SHA1.hashUnencodedChars(s).toString();
      if (!Objects.equals(pCache.getHash(pPath), s1) || !Files.exists(pPath)) {
        Files.createDirectories(pPath.getParent());
        BufferedWriter bufferedwriter = Files.newBufferedWriter(pPath);

        try {
          bufferedwriter.write(s);
        } catch (Throwable throwable1) {
          if (bufferedwriter != null) {
            try {
              bufferedwriter.close();
            } catch (Throwable throwable) {
              throwable1.addSuppressed(throwable);
            }
          }

          throw throwable1;
        }

        if (bufferedwriter != null) {
          bufferedwriter.close();
        }
      }

      pCache.putNew(pPath, s1);
    } catch (IOException ioexception) {
      RootsAPI.LOG.error("Couldn't save recipe {}", pPath, ioexception);
    }
  }

  @Override
  public String getName() {
    return "Spell properties";
  }
}
