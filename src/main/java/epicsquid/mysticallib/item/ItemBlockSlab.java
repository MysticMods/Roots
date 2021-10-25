package epicsquid.mysticallib.item;

import javax.annotation.Nonnull;

import epicsquid.mysticallib.model.IModeledObject;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SlabBlock;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SuppressWarnings("deprecation")
public class ItemBlockSlab extends BlockItem implements IModeledObject {

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

  public void decrementHeldStack(@Nonnull PlayerEntity player, @Nonnull ItemStack stack, @Nonnull Hand hand) {
    if (!player.capabilities.isCreativeMode) {
      stack.shrink(1);
      if (stack.getCount() == 0) {
        player.setItemStackToSlot(hand == Hand.MAIN_HAND ? EquipmentSlotType.MAINHAND : EquipmentSlotType.OFFHAND, ItemStack.EMPTY);
      }
    }
  }

  @Override
  public int getMetadata(int damage) {
    return damage;
  }

  @Override
  @Nonnull
  public ActionResultType onItemUse(@Nonnull PlayerEntity playerIn, @Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull Hand hand,
                                    @Nonnull Direction side, float hitX, float hitY, float hitZ) {
    ItemStack stack = playerIn.getHeldItem(hand);
    if (stack.getCount() == 0) {
      return ActionResultType.FAIL;
    } else if (!playerIn.canPlayerEdit(pos.offset(side), side, stack)) {
      return ActionResultType.FAIL;
    } else {
      BlockState iblockstate = worldIn.getBlockState(pos);

      if (iblockstate.getBlock() == getBlock()) {
        SlabBlock.EnumBlockHalf enumblockhalf = iblockstate.getValue(SlabBlock.HALF);

        if ((side == Direction.UP && enumblockhalf == SlabBlock.EnumBlockHalf.BOTTOM
            || side == Direction.DOWN && enumblockhalf == SlabBlock.EnumBlockHalf.TOP)) {
          BlockState iblockstate1 = this.doubleSlab.getDefaultState();

          if (worldIn.checkNoEntityCollision(this.doubleSlab.getBoundingBox(iblockstate1, worldIn, pos)) && worldIn.setBlockState(pos, iblockstate1, 3)) {
            worldIn.playSound(pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F, this.doubleSlab.getSoundType().getPlaceSound(), SoundCategory.BLOCKS,
                (this.doubleSlab.getSoundType().getVolume() + 1.0F) / 2.0F, this.doubleSlab.getSoundType().getPitch() * 0.8F, true);
            stack.shrink(1);
          }

          return ActionResultType.SUCCESS;
        }
      }

      return (this.userItem(stack, worldIn, pos.offset(side)) || (super.onItemUse(playerIn, worldIn, pos, hand, side, hitX, hitY, hitZ)
          == ActionResultType.SUCCESS)) ? ActionResultType.SUCCESS : ActionResultType.FAIL;
    }
  }

  @Override
  @SideOnly(Side.CLIENT)
  public boolean canPlaceBlockOnSide(@Nonnull World worldIn, @Nonnull BlockPos posIn, @Nonnull Direction faceIn, @Nonnull PlayerEntity playerIn,
                                     @Nonnull ItemStack stackIn) {
    BlockPos blockpos1 = posIn;
    BlockState iblockstate = worldIn.getBlockState(posIn);

    if (iblockstate.getBlock() == getBlock()) {
      boolean flag = iblockstate.getValue(SlabBlock.HALF) == SlabBlock.EnumBlockHalf.TOP;

      if ((faceIn == Direction.UP && !flag || faceIn == Direction.DOWN && flag)) {
        return true;
      }
    }

    posIn = posIn.offset(faceIn);
    BlockState iblockstate1 = worldIn.getBlockState(posIn);
    return iblockstate1.getBlock() == getBlock() || super.canPlaceBlockOnSide(worldIn, blockpos1, faceIn, playerIn, stackIn);
  }

  private boolean userItem(@Nonnull ItemStack stack, @Nonnull World worldIn, @Nonnull BlockPos pos) {
    BlockState iblockstate = worldIn.getBlockState(pos);

    if (iblockstate.getBlock() == getBlock()) {
      BlockState iblockstate1 = this.doubleSlab.getDefaultState();

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
