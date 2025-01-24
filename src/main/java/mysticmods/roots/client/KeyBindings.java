package mysticmods.roots.client;

import com.mojang.blaze3d.platform.InputConstants;
import cpw.mods.util.Lazy;
import mysticmods.roots.api.RootsAPI;
import net.minecraft.client.KeyMapping;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

@EventBusSubscriber(modid = RootsAPI.MODID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class KeyBindings {
  public static final String CATEGORY = "key.category.roots.general";

  public static final KeyMapping OPEN_SPELL_LIBRARY = new KeyMapping("key.roots.open_spell_library", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_K, CATEGORY);

  public static final List<KeyMapping> MAPPINGS = new ArrayList<>();

  @SubscribeEvent
  public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
    MAPPINGS.add(OPEN_SPELL_LIBRARY);
    event.register(OPEN_SPELL_LIBRARY);
    OPEN_SPELL_LIBRARY.setKeyConflictContext(KeyConflictContext.IN_GAME);
  }
}
