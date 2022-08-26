package epicsquid.roots.entity.ritual;

import epicsquid.mysticallib.block.BlockCropBase;
import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.mysticallib.util.Util;
import epicsquid.roots.init.ModBlocks;
import epicsquid.roots.network.fx.MessageTreeCompleteFX;
import epicsquid.roots.ritual.RitualRegistry;
import epicsquid.roots.ritual.RitualWildGrowth;
import epicsquid.roots.world.tree.WorldGenBigWildwoodTree;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

public class EntityRitualWildGrowth extends EntityRitualBase {
  private RitualWildGrowth ritual;

  public EntityRitualWildGrowth(World worldIn) {
    super(worldIn);
    this.getDataManager().register(lifetime, RitualRegistry.ritual_wild_growth.getDuration() + 20);
    ritual = (RitualWildGrowth) RitualRegistry.ritual_wild_growth;
  }

  @Override
  public void onUpdate() {
    super.onUpdate();

    if (!world.isRemote) {
      if (this.ticksExisted % ritual.interval == 0) {
        List<BlockPos> eligiblePositions = Util.getBlocksWithinRadius(world, getPosition(), ritual.radius_x, ritual.radius_y, ritual.radius_z, (pos) -> {
          IBlockState state = world.getBlockState(pos);
          return state.getBlock() == ModBlocks.wildroot && state.getValue(BlockCropBase.AGE) == 7;
        });
        if (eligiblePositions.isEmpty()) return;

        BlockPos pos = eligiblePositions.get(Util.rand.nextInt(eligiblePositions.size()));
        generateTree(world, pos, world.getBlockState(pos), Util.rand);
      }
    }
  }

  private void generateTree(World worldIn, BlockPos pos, IBlockState state, Random rand) {
    WorldGenBigWildwoodTree worldgenerator = new WorldGenBigWildwoodTree(true);
    IBlockState iblockstate2 = Blocks.AIR.getDefaultState();
    worldIn.setBlockState(pos, iblockstate2, 4);
    if (!worldgenerator.generate(worldIn, rand, pos)) {
      worldIn.setBlockState(pos, state, 4);
    } else {
      MessageTreeCompleteFX message = new MessageTreeCompleteFX(worldgenerator.affectedBlocks);
      PacketHandler.sendToAllTracking(message, this);
    }
  }
}