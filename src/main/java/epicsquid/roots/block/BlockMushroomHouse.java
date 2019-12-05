package epicsquid.roots.block;

import epicsquid.mysticallib.block.BlockBase;
import epicsquid.mysticallib.block.BlockTEBase;
import epicsquid.roots.Roots;
import epicsquid.roots.gui.GuiHandler;
import epicsquid.roots.item.ItemDruidKnife;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class BlockMushroomHouse extends BlockBase {

  public BlockMushroomHouse(@Nonnull Material mat, @Nonnull SoundType type, float hardness, @Nonnull String name) {
    super(mat, type, hardness, name);
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

  @Override
  public boolean onBlockActivated(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nonnull PlayerEntity player, @Nonnull Hand hand, @Nonnull Direction face, float hitX, float hitY, float hitZ) {
    if (player.getHeldItem(hand).getItem() instanceof ItemDruidKnife) {
      if (super.onBlockActivated(world, pos, state, player, hand, face, hitX, hitY, hitZ)) return true;
    }

    if (!world.isRemote) {
      //player.openGui(Roots.instance, GuiHandler.CRAFTER_ID, world, pos.getX(), pos.getY(), pos.getZ());
    }

    return true;
  }

  @Nonnull
  @Override
  public AxisAlignedBB getBoundingBox(@Nonnull BlockState state, @Nonnull IBlockAccess world, @Nonnull BlockPos pos) {
    return new AxisAlignedBB(0, 0, 0, 1, 1.5, 1);
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
}
