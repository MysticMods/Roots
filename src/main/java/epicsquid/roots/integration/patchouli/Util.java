package epicsquid.roots.integration.patchouli;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.patchouli.api.PatchouliAPI;
import vazkii.patchouli.client.book.BookEntry;
import vazkii.patchouli.client.book.gui.GuiBookEntry;
import vazkii.patchouli.common.book.Book;
import vazkii.patchouli.common.book.BookRegistry;

public class Util {

  /**
     * Opens a patchouli book GUI with a specific book entry
     * Written by codetaylor & adapted by Davoleo
     *
     * @param world the world you want to open the page in
     * @param player the player that is opening the page
     * @param resourceLocation patchouli book resourceLocation (the path of this RL is the book ID)
     * @param entryLocation patchouli entry resourceLocation
     * @param page the page number
     */
    @SideOnly(Side.CLIENT)
    public static void openBook(World world, PlayerEntity player, ResourceLocation resourceLocation, ResourceLocation entryLocation, int page) {
      if (world.isRemote) {
        PatchouliAPI.instance.openBookGUI(resourceLocation);
        SoundEvent sfx = SoundEvent.REGISTRY.getObject(new ResourceLocation("patchouli", "book_open"));

        if (sfx != null) {
          world.playSound(null, player.posX, player.posY, player.posZ, sfx, SoundCategory.PLAYERS, 1F, (float) (0.7 + Math.random() * 0.4));
        }

        //ClientBookRegistry doesn't have a "books" field :thonk:
        Book book = BookRegistry.INSTANCE.books.get(resourceLocation);
        BookEntry entry = null;

        if (book != null) {
          for (ResourceLocation location : book.contents.entries.keySet()) {
            if (entryLocation.equals(location)) {
              entry = book.contents.entries.get(location);
              break;
            }
          }

          if (entry != null && !entry.isLocked()) {
            GuiBookEntry pageToOpen = new GuiBookEntry(book, entry, page);
            book.contents.openLexiconGui(pageToOpen, true);
          }
        }
        else {
          System.out.println("THE BOOK ENTRY IS NULL");
        }
      }
      else {
        System.out.println("THE BOOK IS NULL");
      }
    }
}
