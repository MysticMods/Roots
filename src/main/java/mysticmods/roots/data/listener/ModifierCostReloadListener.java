package mysticmods.roots.data.listener;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import mysticmods.roots.api.herb.Cost;
import mysticmods.roots.api.modifier.Modifier;
import mysticmods.roots.api.registry.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.Map;


public class ModifierCostReloadListener extends SimpleJsonResourceReloadListener {
  private static final Gson GSON = (new GsonBuilder()).create();
  private static final ModifierCostReloadListener INSTANCE = new ModifierCostReloadListener();

  public ModifierCostReloadListener() {
    super(GSON, "costs/modifier");
  }

  @Override
  protected void apply(Map<ResourceLocation, JsonElement> pObject, ResourceManager pResourceManager, ProfilerFiller pProfiler) {
    pObject.forEach((id, element) -> {
      Modifier prop = Registries.MODIFIER_REGISTRY.get().getValue(id);
      if (prop == null) {
        // do something
      } else if (element.isJsonObject()) {
        prop.setCosts(Cost.fromJsonArray(element));
      }
    });
  }

  public static ModifierCostReloadListener getInstance() {
    return INSTANCE;
  }
}
