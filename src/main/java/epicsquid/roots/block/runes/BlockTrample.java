package epicsquid.roots.block.runes;

import epicsquid.mysticallib.block.BlockBase;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

import javax.annotation.Nonnull;

public class BlockTrample extends BlockBase {
  public static int SAFE_RANGE_X = 30;
  public static int SAFE_RANGE_Y = 5;
  public static int SAFE_RANGE_Z = 30;

  public BlockTrample(@Nonnull Material mat, @Nonnull SoundType type, float hardness, @Nonnull String name) {
    super(mat, type, hardness, name);
    setDefaultState(this.getDefaultState().withProperty(BlockLiquid.LEVEL, 15));
  }

  @Override
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, BlockLiquid.LEVEL);
  }

  @Override
  public IBlockState getStateFromMeta(int meta) {
    return this.getDefaultState().withProperty(BlockLiquid.LEVEL, meta);
  }

  @Override
  public int getMetaFromState(IBlockState state) {
    return state.getValue(BlockLiquid.LEVEL);
  }

  @Override
  public boolean isReplaceable(IBlockAccess worldIn, BlockPos pos) {
    return false;
  }
}
