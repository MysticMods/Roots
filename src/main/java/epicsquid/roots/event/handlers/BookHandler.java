package epicsquid.roots.event.handlers;

import epicsquid.roots.Roots;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = Roots.MODID)
public class BookHandler {
  public static final String BOOK_IDENTIFIER = "roots";

  @SubscribeEvent
  public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
    if (true) {
      if (event.getPlayer().isServerWorld()) {
        // TODO: Port this from MysticalLib 1.12
        //BookRegistry registry = BookRegistry.getBookRegistry(BOOK_IDENTIFIER, event.player);
       /* if (!registry.hasBook) {
          ItemStack stack = new ItemStack(Items.BOOK); // PatchouliAPI.instance.getBookStack("roots:roots_guide");
          event.getPlayer().addItemStackToInventory(stack);
          registry.hasBook = true;
          registry.markDirty();
          event.player.world.getMapStorage().saveAllData();
        }*/
      }
    }
  }
}
