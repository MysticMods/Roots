package mysticmods.roots.client;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.init.ResolvedRecipes;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RecipesUpdatedEvent;

@EventBusSubscriber(modid = RootsAPI.MODID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public class ClientEvents {
  @SubscribeEvent
  public static void onRecipeUpdate(RecipesUpdatedEvent event) {
    ResolvedRecipes.reset();
  }
}
