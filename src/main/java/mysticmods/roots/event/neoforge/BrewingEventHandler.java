package mysticmods.roots.event.neoforge;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.init.ModItems;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent;

@EventBusSubscriber(modid = RootsAPI.MODID)
public class BrewingEventHandler {
  @SubscribeEvent
  public static void onBrewingRecipe(RegisterBrewingRecipesEvent event) {
    event.getBuilder().addContainerRecipe(Items.SPLASH_POTION, ModItems.BAFFLECAP.get(), Items.LINGERING_POTION);
  }
}
