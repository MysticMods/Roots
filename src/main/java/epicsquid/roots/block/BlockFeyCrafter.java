package epicsquid.roots.block;

import epicsquid.mysticallib.block.BlockTEBase;
import epicsquid.roots.Roots;
import epicsquid.roots.gui.GuiHandler;
import epicsquid.roots.item.ItemDruidKnife;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
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

public class BlockFeyCrafter extends BlockTEBase {

  public BlockFeyCrafter(@Nonnull Material mat, @Nonnull SoundType type, float hardness, @Nonnull String name, @Nonnull Class<? extends TileEntity> teClass) {
    super(mat, type, hardness, name, teClass);
    setLightOpacity(0);
    //No Facing edition :(
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
  public AxisAlignedBB getBoundingBox(@Nonnull IBlockState state, @Nonnull IBlockAccess world, @Nonnull BlockPos pos) {
    return new AxisAlignedBB(0, 0, 0, 1, 1.1, 1);
  }

  @Nonnull
  @Override
  public BlockRenderLayer getRenderLayer() {
    return BlockRenderLayer.CUTOUT;
  }
}
