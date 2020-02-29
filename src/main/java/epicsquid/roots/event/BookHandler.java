package epicsquid.roots.event;

import epicsquid.mysticallib.world.books.BookRegistry;
import epicsquid.roots.Roots;
import epicsquid.roots.config.GeneralConfig;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import vazkii.patchouli.api.PatchouliAPI;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = Roots.MODID)
public class BookHandler {
  public static final String BOOK_IDENTIFIER = "roots";

  @SubscribeEvent
  public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
    if (GeneralConfig.GiveBook) {
      if (event.player.isServerWorld()) {
        BookRegistry registry = BookRegistry.getBookRegistry(BOOK_IDENTIFIER, event.player);
        if (!registry.hasBook) {
          ItemStack stack = PatchouliAPI.instance.getBookStack("roots:roots_guide");
          event.player.addItemStackToInventory(stack);
          registry.hasBook = true;
          registry.markDirty();
          event.player.world.getMapStorage().saveAllData();
        }
      }
    }
  }
}
