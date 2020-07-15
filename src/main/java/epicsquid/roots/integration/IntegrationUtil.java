package epicsquid.roots.integration;

import epicsquid.roots.integration.jei.JEIRootsPlugin;
import mezz.jei.api.IRecipeRegistry;
import mezz.jei.api.IRecipesGui;
import mezz.jei.api.recipe.IFocus;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.patchouli.api.PatchouliAPI;
import vazkii.patchouli.client.book.BookCategory;
import vazkii.patchouli.client.book.BookEntry;
import vazkii.patchouli.client.book.gui.GuiBookCategory;
import vazkii.patchouli.client.book.gui.GuiBookEntry;
import vazkii.patchouli.common.book.Book;
import vazkii.patchouli.common.book.BookRegistry;

public class IntegrationUtil {

  /**
   * Opens a patchouli book GUI with a specific book entry
   * Written by codetaylor & adapted by Davoleo
   *
   * @param world            the world you want to open the page in
   * @param player           the player that is opening the page
   * @param resourceLocation patchouli book resourceLocation (the path of this RL is the book ID)
   * @param entryLocation    patchouli entry resourceLocation
   * @param page             the page number
   */
  @SideOnly(Side.CLIENT)
  public static void openBook(World world, EntityPlayer player, ResourceLocation resourceLocation, ResourceLocation entryLocation, int page) {
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
    }
  }

  @SideOnly(Side.CLIENT)
  public static void openCategory(ResourceLocation book, ResourceLocation category) {
    Minecraft mc = Minecraft.getMinecraft();
    openCategory(mc.world, mc.player, book, category);
  }

  @SideOnly(Side.CLIENT)
  public static void openCategory(World world, EntityPlayer player, ResourceLocation resourceLocation, ResourceLocation categoryLocation) {
    if (world.isRemote) {
      PatchouliAPI.instance.openBookGUI(resourceLocation);
      SoundEvent sfx = SoundEvent.REGISTRY.getObject(new ResourceLocation("patchouli", "book_open"));

      if (sfx != null) {
        world.playSound(null, player.posX, player.posY, player.posZ, sfx, SoundCategory.PLAYERS, 1F, (float) (0.7 + Math.random() * 0.4));
      }

      Book book = BookRegistry.INSTANCE.books.get(resourceLocation);
      BookCategory entry = null;

      if (book != null) {
        for (ResourceLocation location : book.contents.categories.keySet()) {
          if (categoryLocation.equals(location)) {
            entry = book.contents.categories.get(location);
            break;
          }
        }

        if (entry != null && !entry.isLocked()) {
          GuiBookCategory pageToOpen = new GuiBookCategory(book, entry);
          book.contents.openLexiconGui(pageToOpen, true);
        }
      }
    }
  }

  @SideOnly(Side.CLIENT)
  public static void showUses(ItemStack stack) {
    if (JEIRootsPlugin.runtime == null) {
      return;
    }
    IRecipeRegistry registry = JEIRootsPlugin.runtime.getRecipeRegistry();
    IRecipesGui gui = JEIRootsPlugin.runtime.getRecipesGui();
    gui.show(registry.createFocus(IFocus.Mode.INPUT, stack));
  }
}
