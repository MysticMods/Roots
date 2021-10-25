package epicsquid.mysticallib.item;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemBlockMultiblock extends ItemBlock {

  private int offset;

  public ItemBlockMultiblock(@Nonnull Block block) {
    this(block, 1);
  }

  public ItemBlockMultiblock(@Nonnull Block block, int offset) {
    super(block);
    this.offset = offset;
  }

  @Override
  @Nonnull
  public EnumActionResult onItemUse(@Nonnull EntityPlayer player, @Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull EnumHand hand,
      @Nonnull EnumFacing facing, float hitX, float hitY, float hitZ) {
    IBlockState iblockstate = worldIn.getBlockState(pos);
    Block block = iblockstate.getBlock();

    if (!block.isReplaceable(worldIn, pos)) {
      pos = pos.offset(facing, offset);
    }

    ItemStack itemstack = player.getHeldItem(hand);

    if (!itemstack.isEmpty() && player.canPlayerEdit(pos, facing, itemstack) && worldIn.mayPlace(this.block, pos, false, facing, null)) {
      int i = this.getMetadata(itemstack.getMetadata());
      IBlockState iblockstate1 = this.block.getStateForPlacement(worldIn, pos, facing, hitX, hitY, hitZ, i, player, hand);

      if (placeBlockAt(itemstack, player, worldIn, pos, facing, hitX, hitY, hitZ, iblockstate1)) {
        iblockstate1 = worldIn.getBlockState(pos);
        SoundType soundtype = iblockstate1.getBlock().getSoundType(iblockstate1, worldIn, pos, player);
        worldIn.playSound(player, pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
        itemstack.shrink(1);
      }

      return EnumActionResult.SUCCESS;
    } else {
      return EnumActionResult.FAIL;
    }
  }
}
