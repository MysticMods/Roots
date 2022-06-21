package mysticmods.roots.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.property.Property;
import mysticmods.roots.init.ModRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Map;

@Mod.EventBusSubscriber(modid= RootsAPI.MODID)
public class PropertyReloadListener extends SimpleJsonResourceReloadListener {
  private static final Gson GSON = (new GsonBuilder()).create();
  private static final PropertyReloadListener INSTANCE = new PropertyReloadListener();

  public PropertyReloadListener() {
    super(GSON, "properties/ritual");
  }

  @Override
  protected void apply(Map<ResourceLocation, JsonElement> pObject, ResourceManager pResourceManager, ProfilerFiller pProfiler) {
    pObject.forEach((id, element) -> {
      Property.RitualProperty<?> prop = ModRegistries.RITUAL_PROPERTY_REGISTRY.get().getValue(id);
      if (prop == null) {
        // do something
      } else if (element.isJsonObject()) {
        prop.updateFromJson(element.getAsJsonObject());
      }
    });
  }

  @SubscribeEvent
  public static void onReloadListeners (AddReloadListenerEvent event) {
    event.addListener(INSTANCE);
  }
}
