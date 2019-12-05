package epicsquid.roots.block;

import epicsquid.mysticallib.LibRegistry;
import epicsquid.mysticallib.block.BlockTEBase;
import epicsquid.roots.block.blockitem.UnendingBowlBlockItem;
import epicsquid.roots.integration.botania.PetalApothecaryFiller;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.Random;

public class UnendingBowlBlock extends BlockTEBase {

  public UnendingBowlBlock(@Nonnull Material mat, @Nonnull SoundType type, float hardness, @Nonnull String name, @Nonnull Class<? extends TileEntity> teClass) {
    super(mat, type, hardness, name, teClass);
    this.setItemBlock(new UnendingBowlBlockItem(this)).setRegistryName(LibRegistry.getActiveModid(), name);
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
    return new AxisAlignedBB(0.125, 0, 0.125, 0.875, 0.3125, 0.875);
  }

  @Nonnull
  @Override
  public BlockRenderLayer getRenderLayer() {
    return BlockRenderLayer.TRANSLUCENT;
  }

  @Override
  @Nonnull
  @SuppressWarnings("deprecation")
  public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, BlockState state, BlockPos pos, Direction face) {
    return BlockFaceShape.BOWL;
  }

  @Override
  public void onBlockAdded(World worldIn, BlockPos pos, BlockState state) {
    if (PetalApothecaryFiller.hasBotania()) {
      PetalApothecaryFiller.getAdjacentApothecary(worldIn, pos);
    }
    worldIn.scheduleUpdate(pos, this, tickRate(worldIn));
  }

  @Override
  public void updateTick(World worldIn, BlockPos pos, BlockState state, Random rand) {
    worldIn.scheduleUpdate(pos, this, tickRate(worldIn));
    if (PetalApothecaryFiller.hasBotania()) {
      PetalApothecaryFiller.getAdjacentApothecary(worldIn, pos);
    }
  }

  @Override
  public int tickRate(World worldIn) {
    return 40;
  }
}
