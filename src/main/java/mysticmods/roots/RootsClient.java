package mysticmods.roots;

import mysticmods.roots.api.RootsAPI;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(value = RootsAPI.MODID, dist = Dist.CLIENT)
public class RootsClient {
  public RootsClient(ModContainer container, IEventBus bus) {
    container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
  }
}
