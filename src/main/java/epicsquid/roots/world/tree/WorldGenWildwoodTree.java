package epicsquid.roots.world.tree;

import java.util.Random;

import epicsquid.roots.init.ModBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.AbstractTreeFeature;

public class WorldGenWildwoodTree extends AbstractTreeFeature {
  private static final BlockState LOG = ModBlocks.wildwood_log.getDefaultState();
  private final boolean useExtraRandomHeight;

  public WorldGenWildwoodTree(boolean notify, boolean useExtraRandomHeightIn) {
    super(notify);
    this.useExtraRandomHeight = useExtraRandomHeightIn;
  }

  @Override
  public boolean generate(World worldIn, Random rand, BlockPos position) {
    for (int i = 0; i < 6; i++) {
      worldIn.setBlockState(position.add(0, i, 0), LOG);
    }

    return true;
  }
}