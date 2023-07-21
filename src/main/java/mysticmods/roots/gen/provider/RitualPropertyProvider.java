package mysticmods.roots.gen.provider;

import com.google.common.collect.Sets;
import com.google.gson.JsonObject;
import mysticmods.roots.api.property.RitualProperty;
import mysticmods.roots.api.registry.Registries;
import mysticmods.roots.gen.BaseProvider;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;

import java.nio.file.Path;
import java.util.Set;

public class RitualPropertyProvider extends BaseProvider {
  protected final DataGenerator generator;

  public RitualPropertyProvider(DataGenerator generator) {
    this.generator = generator;
  }

  @Override
  public void run(CachedOutput pCache) {
    Path path = this.generator.getOutputFolder();
    Set<ResourceLocation> set = Sets.newHashSet();
    for (RitualProperty<?> prop : Registries.RITUAL_PROPERTY_REGISTRY.get().getValues()) {
      ResourceLocation id = Registries.RITUAL_PROPERTY_REGISTRY.get().getKey(prop);
      if (!set.add(id)) {
        throw new IllegalStateException("Duplicate recipe " + id);
      } else {
        JsonObject recipe = new JsonObject();
        recipe.addProperty("ritual", prop.getRitual().getKey().toString());
        recipe.add("value", prop.serializeValueJson());
        recipe.add("default_value", prop.serializeDefaultValueJson());
        recipe.addProperty("_comment", prop.getComment());
        saveRecipe(pCache, recipe, path.resolve("data/" + id.getNamespace() + "/properties/ritual/" + id.getPath() + ".json"));
      }
    }
  }

  @Override
  public String getName() {
    return "Ritual properties";
  }
}
