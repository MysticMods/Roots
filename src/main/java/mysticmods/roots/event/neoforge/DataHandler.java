package mysticmods.roots.event.neoforge;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.registry.IDataMapInitialize;
import net.minecraft.core.Holder;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.datamaps.DataMapsUpdatedEvent;


@EventBusSubscriber(modid = RootsAPI.MODID)
public class DataHandler {
  private static void callInit (Holder<?> holder) {
    if (holder.value() instanceof IDataMapInitialize<?> init) {
      init.performInit(holder);
    }
  }

  @SubscribeEvent
  public static void onDataReloaded(DataMapsUpdatedEvent event) {
    var reference = event.getRegistry().getAny().orElse(null);
    if (reference == null) {
      return;
    }

    if (reference.value() instanceof IDataMapInitialize<?>) {
      event.getRegistry().holders().forEach(DataHandler::callInit);
    }
  }
}
