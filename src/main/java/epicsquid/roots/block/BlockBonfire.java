package epicsquid.roots.block;

import javax.annotation.Nonnull;

import epicsquid.mysticallib.block.BlockTEBase;
import epicsquid.roots.tileentity.TileEntityBonfire;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockBonfire extends BlockTEBase {

  public static PropertyBool BURNING = PropertyBool.create("burning");

  public BlockBonfire(@Nonnull Material mat, @Nonnull SoundType type, float hardness, @Nonnull String name, @Nonnull Class<? extends TileEntity> teClass) {
    super(mat, type, hardness, name, teClass);

    setDefaultState(blockState.getBaseState().withProperty(BURNING, false));
  }

  @Override
  public boolean isFullCube(@Nonnull IBlockState state) {
    return false;
  }

  @Override
  public boolean isOpaqueCube(@Nonnull IBlockState state) {
    return false;
  }

  @Override
  public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos)
  {
    if (state.getValue(BURNING))
      return 12;
    else
      return 0;
  }

  @Nonnull
  @Override
  public AxisAlignedBB getBoundingBox(@Nonnull IBlockState state, @Nonnull IBlockAccess world, @Nonnull BlockPos pos) {
    return new AxisAlignedBB(-0.125, 0, -0.125, 1.125, 0.25, 1.125);
  }

  //Concerning the blockstate --------------------
  @Nonnull
  @Override
  protected BlockStateContainer createBlockState()
  {
    return new BlockStateContainer(this, BURNING);
  }

  @Override
  public int getMetaFromState(IBlockState state)
  {
    return state.getValue(BURNING) ? 1 : 0;
  }
}
