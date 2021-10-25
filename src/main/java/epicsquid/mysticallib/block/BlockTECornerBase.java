package epicsquid.mysticallib.block;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import epicsquid.mysticallib.tile.ITile;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class BlockTECornerBase extends BlockCornerBase implements ITileEntityProvider {
  private Class<? extends TileEntity> teClass;

  public BlockTECornerBase(@Nonnull IBlockState parent, @Nonnull SoundType type, float hardness, @Nonnull String name, boolean inner,
      @Nonnull Class<? extends TileEntity> teClass) {
    super(parent, type, hardness, name, inner);
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
