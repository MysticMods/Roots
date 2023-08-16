package mysticmods.roots.client;

import mysticmods.roots.api.RootsAPI;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

import java.util.List;

@Mod.EventBusSubscriber(modid= RootsAPI.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class KeyBindings {
  public static final String CATEGORY = "key.category.roots.general";

  public static final KeyMapping OPEN_SPELL_LIBRARY = new KeyMapping("key.roots.open_spell_library", GLFW.GLFW_KEY_K, CATEGORY);

  public static final List<KeyMapping> MAPPINGS = List.of(OPEN_SPELL_LIBRARY);

  @SubscribeEvent
  public static void registerKeyMappings (RegisterKeyMappingsEvent event) {
    event.register(OPEN_SPELL_LIBRARY);
    OPEN_SPELL_LIBRARY.setKeyConflictContext(KeyConflictContext.IN_GAME);
  }
}
