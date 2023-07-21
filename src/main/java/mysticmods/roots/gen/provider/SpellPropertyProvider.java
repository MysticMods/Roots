package mysticmods.roots.gen.provider;

import com.google.common.collect.Sets;
import com.google.gson.JsonObject;
import mysticmods.roots.api.property.SpellProperty;
import mysticmods.roots.api.registry.Registries;
import mysticmods.roots.gen.BaseProvider;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;

import java.nio.file.Path;
import java.util.Set;

public class SpellPropertyProvider extends BaseProvider {
  protected final DataGenerator generator;

  public SpellPropertyProvider(DataGenerator generator) {
    this.generator = generator;
  }

  @Override
  public void run(CachedOutput pCache) {
    Path path = this.generator.getOutputFolder();
    Set<ResourceLocation> set = Sets.newHashSet();
    for (SpellProperty<?> prop : Registries.SPELL_PROPERTY_REGISTRY.get().getValues()) {
      ResourceLocation id = Registries.SPELL_PROPERTY_REGISTRY.get().getKey(prop);
      if (!set.add(id)) {
        throw new IllegalStateException("Duplicate recipe " + id);
      } else {
        JsonObject recipe = new JsonObject();
        recipe.addProperty("spell", prop.getSpell().getKey().toString());
        recipe.add("value", prop.serializeValueJson());
        recipe.add("default_value", prop.serializeDefaultValueJson());
        recipe.addProperty("_comment", prop.getComment());
        saveRecipe(pCache, recipe, path.resolve("data/" + id.getNamespace() + "/properties/spell/" + id.getPath() + ".json"));
      }
    }
  }

  @Override
  public String getName() {
    return "Spell properties";
  }
}
