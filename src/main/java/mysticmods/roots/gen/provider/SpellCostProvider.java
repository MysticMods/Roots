package mysticmods.roots.gen.provider;

import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import mysticmods.roots.api.herbs.Cost;
import mysticmods.roots.api.registry.Registries;
import mysticmods.roots.api.spell.Spell;
import mysticmods.roots.gen.BaseProvider;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.HashCache;
import net.minecraft.resources.ResourceLocation;

import java.nio.file.Path;
import java.util.Set;

public class SpellCostProvider extends BaseProvider implements DataProvider {
  protected final DataGenerator generator;

  public SpellCostProvider(DataGenerator generator) {
    this.generator = generator;
  }

  @Override
  public void run(CachedOutput pCache) {
    Path path = this.generator.getOutputFolder();
    Set<ResourceLocation> set = Sets.newHashSet();
    for (Spell spell : Registries.SPELL_REGISTRY.get().getValues()) {
      ResourceLocation id = Registries.SPELL_REGISTRY.get().getKey(spell);
      if (!set.add(id)) {
        throw new IllegalStateException("Duplicate recipe " + id);
      } else {
        JsonArray result = new JsonArray();
        for (Cost cost : spell.getCosts()) {
          result.add(cost.toJson());
        }
        JsonObject recipe = new JsonObject();
        recipe.add("costs", result);
        saveRecipe(pCache, recipe, path.resolve("data/" + id.getNamespace() + "/costs/spell/" + id.getPath() + ".json"));
      }
    }
  }

  @Override
  public String getName() {
    return "Spell costs";
  }
}
