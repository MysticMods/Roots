package epicsquid.roots.block;

import epicsquid.mysticallib.block.BlockBase;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.Random;

@SuppressWarnings("deprecation")
public abstract class BlockFeyLight extends BlockBase {
  public BlockFeyLight(@Nonnull Material mat, @Nonnull SoundType type, float hardness, @Nonnull String name) {
    super(mat, type, hardness, name);
    this.setLightLevel(1.0f);
    this.setLightOpacity(0);

    // This prevents this from being registered as an itemblock
    this.setItemBlock(null);
  }

  @Override
  public BlockFaceShape getBlockFaceShape(final IBlockAccess worldIn, final IBlockState state, final BlockPos pos, final EnumFacing face) {
    return BlockFaceShape.UNDEFINED;
  }

  @Override
  public EnumBlockRenderType getRenderType(final IBlockState state) {
    return EnumBlockRenderType.INVISIBLE;
  }

  @Override
  public AxisAlignedBB getBoundingBox(final IBlockState state, final IBlockAccess source, final BlockPos pos) {
    return new AxisAlignedBB(0.33, 0.33, 0.33, 0.66, 0.66, 0.66);
  }

  @Override
  public AxisAlignedBB getCollisionBoundingBox(final IBlockState state, final IBlockAccess worldIn, final BlockPos pos) {
    return null;
  }

  @Override
  public boolean isFullCube(final IBlockState state) {
    return false;
  }

  @Override
  public boolean isOpaqueCube(final IBlockState state) {
    return false;
  }

  @SideOnly(Side.CLIENT)
  @Override
  public abstract void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random random);

  @Override
  public Item getItemDropped(IBlockState state, Random rand, int fortune) {
    return Items.AIR;
  }
}

