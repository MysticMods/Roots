package mysticmods.roots.gen;

import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.property.ModifierProperty;
import mysticmods.roots.api.registry.Registries;
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

public class ModifierPropertyProvider extends BaseProvider {
  protected final DataGenerator generator;

  public ModifierPropertyProvider(DataGenerator generator) {
    this.generator = generator;
  }

  @Override
  public void run(HashCache pCache) {
    Path path = this.generator.getOutputFolder();
    Set<ResourceLocation> set = Sets.newHashSet();
    for (ModifierProperty<?> prop : Registries.MODIFIER_PROPERTY_REGISTRY.get().getValues()) {
      ResourceLocation id = Registries.MODIFIER_PROPERTY_REGISTRY.get().getKey(prop);
      if (!set.add(id)) {
        throw new IllegalStateException("Duplicate recipe " + id);
      } else {
        JsonObject recipe = new JsonObject();
        recipe.addProperty("spell", prop.getSpell().getKey().toString());
        recipe.add("value", prop.serializeValueJson());
        recipe.add("default_value", prop.serializeDefaultValueJson());
        recipe.addProperty("_comment", prop.getComment());
        saveRecipe(pCache, recipe, path.resolve("data/" + id.getNamespace() + "/properties/modifier/" + id.getPath() + ".json"));
      }
    }
  }

  @Override
  public String getName() {
    return "Modifier properties";
  }
}
