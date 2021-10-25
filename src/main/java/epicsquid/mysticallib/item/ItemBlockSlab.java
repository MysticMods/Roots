package epicsquid.mysticallib.item;

import javax.annotation.Nonnull;

import epicsquid.mysticallib.model.IModeledObject;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SuppressWarnings("deprecation")
public class ItemBlockSlab extends ItemBlock implements IModeledObject {

  private Block doubleSlab;

  public ItemBlockSlab(@Nonnull Block block, @Nonnull Block doubleSlabBlock) {
    super(block);
    doubleSlab = doubleSlabBlock;
  }

  @Override
  @SideOnly(Side.CLIENT)
  public void initModel() {
    ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName().toString()));
  }

  public void decrementHeldStack(@Nonnull EntityPlayer player, @Nonnull ItemStack stack, @Nonnull EnumHand hand) {
    if (!player.capabilities.isCreativeMode) {
      stack.shrink(1);
      if (stack.getCount() == 0) {
        player.setItemStackToSlot(hand == EnumHand.MAIN_HAND ? EntityEquipmentSlot.MAINHAND : EntityEquipmentSlot.OFFHAND, ItemStack.EMPTY);
      }
    }
  }

  @Override
  public int getMetadata(int damage) {
    return damage;
  }

  @Override
  @Nonnull
  public EnumActionResult onItemUse(@Nonnull EntityPlayer playerIn, @Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull EnumHand hand,
      @Nonnull EnumFacing side, float hitX, float hitY, float hitZ) {
    ItemStack stack = playerIn.getHeldItem(hand);
    if (stack.getCount() == 0) {
      return EnumActionResult.FAIL;
    } else if (!playerIn.canPlayerEdit(pos.offset(side), side, stack)) {
      return EnumActionResult.FAIL;
    } else {
      IBlockState iblockstate = worldIn.getBlockState(pos);

      if (iblockstate.getBlock() == getBlock()) {
        BlockSlab.EnumBlockHalf enumblockhalf = iblockstate.getValue(BlockSlab.HALF);

        if ((side == EnumFacing.UP && enumblockhalf == BlockSlab.EnumBlockHalf.BOTTOM
            || side == EnumFacing.DOWN && enumblockhalf == BlockSlab.EnumBlockHalf.TOP)) {
          IBlockState iblockstate1 = this.doubleSlab.getDefaultState();

          if (worldIn.checkNoEntityCollision(this.doubleSlab.getBoundingBox(iblockstate1, worldIn, pos)) && worldIn.setBlockState(pos, iblockstate1, 3)) {
            worldIn.playSound(pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F, this.doubleSlab.getSoundType().getPlaceSound(), SoundCategory.BLOCKS,
                (this.doubleSlab.getSoundType().getVolume() + 1.0F) / 2.0F, this.doubleSlab.getSoundType().getPitch() * 0.8F, true);
            stack.shrink(1);
          }

          return EnumActionResult.SUCCESS;
        }
      }

      return (this.userItem(stack, worldIn, pos.offset(side)) || (super.onItemUse(playerIn, worldIn, pos, hand, side, hitX, hitY, hitZ)
          == EnumActionResult.SUCCESS)) ? EnumActionResult.SUCCESS : EnumActionResult.FAIL;
    }
  }

  @Override
  @SideOnly(Side.CLIENT)
  public boolean canPlaceBlockOnSide(@Nonnull World worldIn, @Nonnull BlockPos posIn, @Nonnull EnumFacing faceIn, @Nonnull EntityPlayer playerIn,
      @Nonnull ItemStack stackIn) {
    BlockPos blockpos1 = posIn;
    IBlockState iblockstate = worldIn.getBlockState(posIn);

    if (iblockstate.getBlock() == getBlock()) {
      boolean flag = iblockstate.getValue(BlockSlab.HALF) == BlockSlab.EnumBlockHalf.TOP;

      if ((faceIn == EnumFacing.UP && !flag || faceIn == EnumFacing.DOWN && flag)) {
        return true;
      }
    }

    posIn = posIn.offset(faceIn);
    IBlockState iblockstate1 = worldIn.getBlockState(posIn);
    return iblockstate1.getBlock() == getBlock() || super.canPlaceBlockOnSide(worldIn, blockpos1, faceIn, playerIn, stackIn);
  }

  private boolean userItem(@Nonnull ItemStack stack, @Nonnull World worldIn, @Nonnull BlockPos pos) {
    IBlockState iblockstate = worldIn.getBlockState(pos);

    if (iblockstate.getBlock() == getBlock()) {
      IBlockState iblockstate1 = this.doubleSlab.getDefaultState();

      if (worldIn.checkNoEntityCollision(this.doubleSlab.getBoundingBox(iblockstate1, worldIn, pos)) && worldIn.setBlockState(pos, iblockstate1, 3)) {
        worldIn.playSound(pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F, this.doubleSlab.getSoundType().getPlaceSound(), SoundCategory.BLOCKS,
            (this.doubleSlab.getSoundType().getVolume() + 1.0F) / 2.0F, this.doubleSlab.getSoundType().getPitch() * 0.8F, true);
        stack.shrink(1);
      }

      return true;
    }

    return false;
  }
}
