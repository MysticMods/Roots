package epicsquid.roots.block;

import epicsquid.mysticallib.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.block.BlockRenderType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.OnlyIn;

import javax.annotation.Nonnull;
import java.util.Random;

@SuppressWarnings("deprecation")
public abstract class BlockFeyLight extends Block {
  public BlockFeyLight(@Nonnull Material mat, @Nonnull SoundType type, float hardness, @Nonnull String name) {
    super(mat, type, hardness, name);
    this.setLightLevel(1.0f);
    this.setLightOpacity(0);

    // This prevents this from being registered as an itemblock
    this.setItemBlock(null);
  }

  @Override
  public BlockFaceShape getBlockFaceShape(final IBlockAccess worldIn, final BlockState state, final BlockPos pos, final Direction face) {
    return BlockFaceShape.UNDEFINED;
  }

  @Override
  public BlockRenderType getRenderType(final BlockState state) {
    return BlockRenderType.INVISIBLE;
  }

  @Override
  public AxisAlignedBB getBoundingBox(final BlockState state, final IBlockAccess source, final BlockPos pos) {
    return new AxisAlignedBB(0.33, 0.33, 0.33, 0.66, 0.66, 0.66);
  }

  @Override
  public AxisAlignedBB getCollisionBoundingBox(final BlockState state, final IBlockAccess worldIn, final BlockPos pos) {
    return null;
  }

  @Override
  public boolean isFullCube(final BlockState state) {
    return false;
  }

  @Override
  public boolean isOpaqueCube(final BlockState state) {
    return false;
  }

  @OnlyIn(Dist.CLIENT)
  @Override
  public abstract void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random);

  @Override
  public Item getItemDropped(BlockState state, Random rand, int fortune) {
    return Items.AIR;
  }
}

