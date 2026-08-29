package mysticmods.roots.event.neoforge;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.command.RootsCommand;
import mysticmods.roots.test.decompose.DumpDataCommand;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;


@EventBusSubscriber(modid = RootsAPI.MODID)
public class CommandsHandler {
  @SubscribeEvent
  public static void onCommands(RegisterCommandsEvent event) {
    RootsCommand.register(event.getDispatcher(), event.getBuildContext());
    DumpDataCommand.register(event.getDispatcher());
  }
}
