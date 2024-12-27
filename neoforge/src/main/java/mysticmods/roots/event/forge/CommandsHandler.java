package mysticmods.roots.event.forge;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.command.RootsCommand;




@EventBusSubscriber(modid = RootsAPI.MODID)
public class CommandsHandler {
  @SubscribeEvent
  public static void onCommands(RegisterCommandsEvent event) {
    RootsCommand.register(event.getDispatcher());
  }
}
