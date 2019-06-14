package epicsquid.roots.block;

import epicsquid.mysticallib.block.BlockTEBase;
import epicsquid.roots.Roots;
import epicsquid.roots.gui.GuiHandler;
import epicsquid.roots.item.ItemDruidKnife;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class BlockGroveCrafter extends BlockTEBase {

  private static final PropertyDirection FACING = BlockHorizontal.FACING;

  public BlockGroveCrafter(@Nonnull Material mat, @Nonnull SoundType type, float hardness, @Nonnull String name, @Nonnull Class<? extends TileEntity> teClass) {
    super(mat, type, hardness, name, teClass);
    setLightOpacity(0);
    this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
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
  public boolean onBlockActivated(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nonnull EntityPlayer player, @Nonnull EnumHand hand, @Nonnull EnumFacing face, float hitX, float hitY, float hitZ) {
    if (player.getHeldItem(hand).getItem() instanceof ItemDruidKnife) {
      if (super.onBlockActivated(world, pos, state, player, hand, face, hitX, hitY, hitZ)) return true;
    }

    if (!world.isRemote) {
      player.openGui(Roots.instance, GuiHandler.CRAFTER_ID, world, pos.getX(), pos.getY(), pos.getZ());
    }

    return true;
  }

  @Nonnull
  @Override
  public IBlockState getStateForPlacement(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
    return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite().rotateY());
  }

  @Override
  public int getMetaFromState(IBlockState state) {
    int i = 0;

    switch (state.getValue(FACING))
    {
      case EAST:
        i = i | 1;
        break;
      case WEST:
        i = i | 2;
        break;
      case SOUTH:
        i = i | 3;
        break;
      case NORTH:
      default:
        i = i | 4;
    }

    return i;
  }

  @SuppressWarnings("deprecation")
  @Nonnull
  @Override
  public IBlockState getStateFromMeta(int meta) {
      IBlockState state = this.getDefaultState();
      switch (meta)
      {
        case 1:
          state = state.withProperty(FACING, EnumFacing.EAST);
          break;
        case 2:
          state = state.withProperty(FACING, EnumFacing.WEST);
          break;
        case 3:
          state = state.withProperty(FACING, EnumFacing.SOUTH);
          break;
        case 4:
        default:
          state = state.withProperty(FACING, EnumFacing.NORTH);
      }

      return state;
  }

  @Nonnull
  @Override
  public AxisAlignedBB getBoundingBox(@Nonnull IBlockState state, @Nonnull IBlockAccess world, @Nonnull BlockPos pos) {
    return new AxisAlignedBB(0, 0, 0, 1, 1.1, 1);
  }

  @Nonnull
  @Override
  public BlockRenderLayer getRenderLayer() {
    return BlockRenderLayer.CUTOUT;
  }
}
