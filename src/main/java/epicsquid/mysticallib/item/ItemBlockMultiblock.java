package epicsquid.mysticallib.item;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemBlockMultiblock extends BlockItem {

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
  public ActionResultType onItemUse(@Nonnull PlayerEntity player, @Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull Hand hand,
                                    @Nonnull Direction facing, float hitX, float hitY, float hitZ) {
    BlockState iblockstate = worldIn.getBlockState(pos);
    Block block = iblockstate.getBlock();

    if (!block.isReplaceable(worldIn, pos)) {
      pos = pos.offset(facing, offset);
    }

    ItemStack itemstack = player.getHeldItem(hand);

    if (!itemstack.isEmpty() && player.canPlayerEdit(pos, facing, itemstack) && worldIn.mayPlace(this.block, pos, false, facing, null)) {
      int i = this.getMetadata(itemstack.getMetadata());
      BlockState iblockstate1 = this.block.getStateForPlacement(worldIn, pos, facing, hitX, hitY, hitZ, i, player, hand);

      if (placeBlockAt(itemstack, player, worldIn, pos, facing, hitX, hitY, hitZ, iblockstate1)) {
        iblockstate1 = worldIn.getBlockState(pos);
        SoundType soundtype = iblockstate1.getBlock().getSoundType(iblockstate1, worldIn, pos, player);
        worldIn.playSound(player, pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
        itemstack.shrink(1);
      }

      return ActionResultType.SUCCESS;
    } else {
      return ActionResultType.FAIL;
    }
  }
}
