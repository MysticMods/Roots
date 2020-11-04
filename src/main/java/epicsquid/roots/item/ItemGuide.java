package epicsquid.roots.item;

import epicsquid.mysticallib.item.ItemBase;
import epicsquid.roots.Roots;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.patchouli.common.base.PatchouliSounds;
import vazkii.patchouli.common.book.Book;
import vazkii.patchouli.common.book.BookRegistry;
import vazkii.patchouli.common.network.NetworkHandler;
import vazkii.patchouli.common.network.message.MessageOpenBookGui;

import javax.annotation.Nonnull;
import java.util.List;

public class ItemGuide extends ItemBase {
  public static ResourceLocation BOOK_LOCATION = new ResourceLocation(Roots.MODID, "roots_guide");

  public ItemGuide(@Nonnull String name) {
    super(name);
  }

  public static Book getBook(ItemStack stack) {
    return BookRegistry.INSTANCE.books.get(BOOK_LOCATION);
  }

  @Override
  @SideOnly(Side.CLIENT)
  public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
    super.addInformation(stack, worldIn, tooltip, flagIn);

    Book book = getBook(stack);
    if (book != null && book.contents != null)
      tooltip.add(book.contents.getSubtitle());
  }

  @Override
  public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
    ItemStack stack = playerIn.getHeldItem(handIn);
    Book book = getBook(stack);
    if (book == null)
      return new ActionResult<>(EnumActionResult.FAIL, stack);

    if (playerIn instanceof EntityPlayerMP) {
      NetworkHandler.INSTANCE.sendTo(new MessageOpenBookGui(book.resourceLoc.toString()), (EntityPlayerMP) playerIn);
      SoundEvent sfx = PatchouliSounds.getSound(book.openSound, PatchouliSounds.book_open);
      worldIn.playSound(null, playerIn.posX, playerIn.posY, playerIn.posZ, sfx, SoundCategory.PLAYERS, 1F, (float) (0.7 + Math.random() * 0.4));
    }

    return new ActionResult<>(EnumActionResult.SUCCESS, stack);
  }
}
