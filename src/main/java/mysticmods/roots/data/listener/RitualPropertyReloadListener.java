package mysticmods.roots.data.listener;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import mysticmods.roots.api.property.RitualProperty;
import mysticmods.roots.api.registry.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.Map;


public class RitualPropertyReloadListener extends SimpleJsonResourceReloadListener {
  private static final Gson GSON = (new GsonBuilder()).create();
  private static final RitualPropertyReloadListener INSTANCE = new RitualPropertyReloadListener();

  public RitualPropertyReloadListener() {
    super(GSON, "properties/ritual");
  }

  @Override
  protected void apply(Map<ResourceLocation, JsonElement> pObject, ResourceManager pResourceManager, ProfilerFiller pProfiler) {
    pObject.forEach((id, element) -> {
      RitualProperty<?> prop = Registries.RITUAL_PROPERTY_REGISTRY.get().getValue(id);
      if (prop == null) {
        // do something
      } else if (element.isJsonObject()) {
        prop.updateFromJson(element.getAsJsonObject());
      }
    });
  }

  public static RitualPropertyReloadListener getInstance() {
    return INSTANCE;
  }
}
