package epicsquid.roots.block;

import net.minecraft.block.BlockState;
import net.minecraft.state.BooleanProperty;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Random;

@SuppressWarnings("deprecation")
public class BlockDecorativePyre extends BlockPyre {

  public static BooleanProperty BURNING = BooleanProperty.create("burning");

  public BlockDecorativePyre(Properties props) {
    super(props);
  }

/*  @Override
  @Nonnull
  public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, BlockState state, BlockPos pos, Direction face) {
    return BlockFaceShape.BOWL;
  }*/

/*  @Nonnull
  @Override
  public AxisAlignedBB getBoundingBox(@Nonnull BlockState state, @Nonnull IBlockAccess world, @Nonnull BlockPos pos) {
    return new AxisAlignedBB(-0.125, 0, -0.125, 1.125, 0.25, 1.125);
  }*/

  @Override
  @OnlyIn(Dist.CLIENT)
  public void animateTick(BlockState stateIn, World world, BlockPos pos, Random rand) {
    if (stateIn.get(BURNING)) {
      world.playSound((double) pos.getX() + 0.5D, (double) pos.getY(), (double) pos.getZ() + 0.5D, SoundEvents.BLOCK_FIRE_AMBIENT, SoundCategory.BLOCKS, 0.5F, 1.0F, false);
    }
  }
}
