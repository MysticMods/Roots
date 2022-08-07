package mysticmods.roots.gen;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import mysticmods.roots.api.property.ModifierProperty;
import mysticmods.roots.api.registry.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.Map;


public class ModifierPropertyReloadListener extends SimpleJsonResourceReloadListener {
  private static final Gson GSON = (new GsonBuilder()).create();
  private static final ModifierPropertyReloadListener INSTANCE = new ModifierPropertyReloadListener();

  public ModifierPropertyReloadListener() {
    super(GSON, "properties/modifier");
  }

  @Override
  protected void apply(Map<ResourceLocation, JsonElement> pObject, ResourceManager pResourceManager, ProfilerFiller pProfiler) {
    pObject.forEach((id, element) -> {
      ModifierProperty<?> prop = Registries.MODIFIER_PROPERTY_REGISTRY.get().getValue(id);
      if (prop == null) {
        // do something
      } else if (element.isJsonObject()) {
        prop.updateFromJson(element.getAsJsonObject());
      }
    });
  }

  public static ModifierPropertyReloadListener getInstance() {
    return INSTANCE;
  }
}
