package epicsquid.roots.block;

import epicsquid.mysticallib.block.Block;
import epicsquid.roots.recipe.FeyCraftingRecipe;
import epicsquid.roots.tileentity.TileEntityFeyCrafter;
import epicsquid.roots.tileentity.TileEntityRunicCrafter;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

@SuppressWarnings("deprecation")
public class BlockFeyCrafter extends Block {

  public BlockFeyCrafter(@Nonnull Material mat, @Nonnull SoundType type, float hardness, @Nonnull String name, @Nonnull Class<? extends TileEntity> teClass) {
    super(mat, type, hardness, name, teClass);
    setLightOpacity(0);
    //No Facing edition :(
  }

  @Override
  public boolean isFullCube(@Nonnull BlockState state) {
    return false;
  }

  @Override
  public boolean isOpaqueCube(@Nonnull BlockState state) {
    return false;
  }

  @Nonnull
  @Override
  public AxisAlignedBB getBoundingBox(@Nonnull BlockState state, @Nonnull IBlockAccess world, @Nonnull BlockPos pos) {
    return new AxisAlignedBB(0, 0, 0, 1, 1.1, 1);
  }

  @Nonnull
  @Override
  public BlockRenderLayer getRenderLayer() {
    return BlockRenderLayer.CUTOUT;
  }

  @Override
  @Nonnull
  @SuppressWarnings("deprecation")
  public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, BlockState state, BlockPos pos, Direction face) {
    if (face == Direction.UP) {
      return BlockFaceShape.UNDEFINED;
    }
    return super.getBlockFaceShape(worldIn, state, pos, face);
  }

  @Override
  public boolean hasComparatorInputOverride(BlockState state) {
    return true;
  }

  @Override
  public int getComparatorInputOverride(BlockState blockState, World worldIn, BlockPos pos) {
    TileEntity te = worldIn.getTileEntity(pos);
    if (te instanceof TileEntityRunicCrafter) {
      TileEntityRunicCrafter tile = (TileEntityRunicCrafter) te;
      FeyCraftingRecipe recipe = tile.getRecipe();
      if (recipe == null) {
        return 0;
      }
      if (recipe.matches(tile.getContents())) {
        return 15;
      }
    }
    if (te instanceof TileEntityFeyCrafter) {
      TileEntityFeyCrafter tile = (TileEntityFeyCrafter) te;
      if (tile.getRecipe() == null) {
        return 0;
      } else {
        return 15;
      }
    }

    return 0;
  }
}
