package mysticmods.roots.gen.listener;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import mysticmods.roots.api.herb.Cost;
import mysticmods.roots.api.registry.Registries;
import mysticmods.roots.api.spell.Spell;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.Map;


public class SpellCostReloadListener extends SimpleJsonResourceReloadListener {
  private static final Gson GSON = (new GsonBuilder()).create();
  private static final SpellCostReloadListener INSTANCE = new SpellCostReloadListener();

  public SpellCostReloadListener() {
    super(GSON, "costs/spell");
  }

  @Override
  protected void apply(Map<ResourceLocation, JsonElement> pObject, ResourceManager pResourceManager, ProfilerFiller pProfiler) {
    pObject.forEach((id, element) -> {
      Spell prop = Registries.SPELL_REGISTRY.get().getValue(id);
      if (prop == null) {
        // do something
      } else if (element.isJsonObject()) {
        prop.setCosts(Cost.fromJsonArray(element));
      }
    });
  }

  public static SpellCostReloadListener getInstance() {
    return INSTANCE;
  }
}
