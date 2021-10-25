package epicsquid.mysticallib.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockHugeMushroomBase extends BlockBase {
  private Block smallBlock;

  public BlockHugeMushroomBase(Material materialIn, SoundType type, float hardness, String name) {
    super(materialIn, type, hardness, name);
  }

  public void setSmallBlock(Block smallBlockIn) {
    this.smallBlock = smallBlockIn;
  }

  @Override
  public int quantityDropped(Random random) {
    return Math.max(0, random.nextInt(10) - 8);
  }

  @Override
  public Item getItemDropped(IBlockState state, Random rand, int fortune) {
    return Item.getItemFromBlock(smallBlock);
  }

  @Override
  @SuppressWarnings("deprecation")
  public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
    return new ItemStack(smallBlock);
  }
}
