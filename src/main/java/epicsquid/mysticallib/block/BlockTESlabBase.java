package epicsquid.mysticallib.block;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import epicsquid.mysticallib.tile.ITile;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class BlockTESlabBase extends BlockSlabBase implements ITileEntityProvider {
  private Class<? extends TileEntity> teClass;

  public BlockTESlabBase(@Nonnull Material mat, @Nonnull SoundType type, float hardness, @Nonnull String name, @Nonnull IBlockState parent, boolean isDouble,
      @Nullable Block slab, @Nonnull Class<? extends TileEntity> teClass) {
    super(mat, type, hardness, name, parent, isDouble, slab);
    this.teClass = teClass;
    BlockTEBase.attemptRegistry(teClass);
  }

  @Override
  public boolean onBlockActivated(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nonnull EntityPlayer player, @Nonnull EnumHand hand,
      @Nonnull EnumFacing face, float hitX, float hitY, float hitZ) {
    TileEntity t = world.getTileEntity(pos);
    if (t instanceof ITile) {
      return ((ITile) t).activate(world, pos, state, player, hand, face, hitX, hitY, hitZ);
    }
    return false;
  }

  @Override
  public void onBlockHarvested(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nonnull EntityPlayer player) {
    TileEntity t = world.getTileEntity(pos);
    if (t instanceof ITile) {
      ((ITile) t).breakBlock(world, pos, state, player);
    }
  }

  @Override
  public void onBlockExploded(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull Explosion e) {
    TileEntity t = world.getTileEntity(pos);
    if (t instanceof ITile) {
      ((ITile) t).breakBlock(world, pos, world.getBlockState(pos), null);
    }
    super.onBlockExploded(world, pos, e);
  }

  @Override
  @Nullable
  public TileEntity createNewTileEntity(@Nonnull World worldIn, int meta) {
    try {
      return teClass.newInstance();
    } catch (InstantiationException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }
    return null;
  }

}
