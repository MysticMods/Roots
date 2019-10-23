package epicsquid.roots.util;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import vazkii.patchouli.api.PatchouliAPI;
import vazkii.patchouli.client.book.BookEntry;
import vazkii.patchouli.client.book.gui.GuiBookEntry;
import vazkii.patchouli.common.book.Book;
import vazkii.patchouli.common.book.BookRegistry;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class RitualUtil {

  private static Random rand = new Random();

  public static BlockPos getRandomPosRadialXZ(BlockPos centerPos, int xRadius, int zRadius)
  {
      BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(centerPos.getX() -xRadius, centerPos.getY(), centerPos.getZ() -zRadius);

      return pos.add(rand.nextInt(xRadius * 2), 0, rand.nextInt(zRadius * 2));
  }

  public static BlockPos getRandomPosRadialXYZ(BlockPos centerPos, int xRadius, int yRadius, int zRadius)
  {
      BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(centerPos.getX() -xRadius, centerPos.getY() -yRadius, centerPos.getZ() -zRadius);

      BlockPos pos2 = pos.add(rand.nextInt(xRadius * 2), rand.nextInt(yRadius * 2), rand.nextInt(zRadius * 2));

      //Debug Print
      //System.out.println("Pos: " +  pos.getX() +  " | " + pos.getY() + " | " + pos.getZ());
      return pos2;
  }

  public static BlockPos getRandomPosRadialXYZ(World world, BlockPos centerPos, int xRadius, int yRadius, int zRadius, Block... whitelistedBlocks)
  {
    BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(centerPos.getX() -xRadius, centerPos.getY() -yRadius, centerPos.getZ() -zRadius);

    BlockPos pos2 = pos.add(rand.nextInt(xRadius * 2), rand.nextInt(yRadius * 2), rand.nextInt(zRadius * 2));
    //System.out.println("Pos: " +  pos.getX() +  " | " + pos.getY() + " | " + pos.getZ());
    List<Block> blocks = Arrays.asList(whitelistedBlocks);

    if (blocks.contains(world.getBlockState(pos)))
      return pos2;

    return null;
  }

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
  //@SideOnly(Side.CLIENT)
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
      else {
        System.out.println("THE BOOK ENTRY IS NULL");
      }
    }
    else {
      System.out.println("THE BOOK IS NULL");
    }
  }

}
