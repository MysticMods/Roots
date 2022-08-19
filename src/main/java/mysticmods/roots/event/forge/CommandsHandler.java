package mysticmods.roots.event.forge;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.command.RootsCommand;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = RootsAPI.MODID)
public class CommandsHandler {
  @SubscribeEvent
  public static void onCommands(RegisterCommandsEvent event) {
    RootsCommand.register(event.getDispatcher());
  }
}
