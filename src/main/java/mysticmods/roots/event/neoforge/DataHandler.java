package mysticmods.roots.event.neoforge;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.registry.IDataMapInitialize;
import mysticmods.roots.init.ModAttachments;
import mysticmods.roots.recipe.AnimalHarvestRecipe;
import mysticmods.roots.network.client.ClientboundAnimalHarvestSyncPacket;
import net.minecraft.core.Holder;
import net.minecraft.server.MinecraftServer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import net.neoforged.neoforge.registries.datamaps.DataMapsUpdatedEvent;
import net.neoforged.neoforge.server.ServerLifecycleHooks;


@EventBusSubscriber(modid = RootsAPI.MODID)
public class DataHandler {
  private static void callInit (Holder<?> holder) {
    if (holder.value() instanceof IDataMapInitialize<?> init) {
      init.performInit(holder);
    }
  }

  @SubscribeEvent
  public static void onDataReloaded(DataMapsUpdatedEvent event) {
    if (event.getCause() == DataMapsUpdatedEvent.UpdateCause.SERVER_RELOAD) {
      MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
      if (server != null) {
        server.overworld()
            .setData(ModAttachments.ANIMAL_HARVEST_RECIPE_CACHE.get(), AnimalHarvestRecipe.getServerRecipes(server.reloadableRegistries()
                .lookup()));
      }
    }

    var reference = event.getRegistry().getAny().orElse(null);
    if (reference == null) {
      return;
    }

    if (reference.value() instanceof IDataMapInitialize<?>) {
      event.getRegistry().holders().forEach(DataHandler::callInit);
    }
  }

  @SubscribeEvent
  public static void onDataPackSync (OnDatapackSyncEvent event) {
    MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
    var cache = server.overworld().getData(ModAttachments.ANIMAL_HARVEST_RECIPE_CACHE.get());
    if (cache.recipes().isEmpty()) {
      return;
    }

    ClientboundAnimalHarvestSyncPacket packet = new ClientboundAnimalHarvestSyncPacket(cache.recipes());

    event.getRelevantPlayers().forEach(o -> {
      o.connection.send(packet);
    });
  }
}
