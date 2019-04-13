package epicsquid.roots.entity.ritual;

import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.mysticallib.util.Util;
import epicsquid.roots.Roots;
import epicsquid.roots.init.ModBlocks;
import epicsquid.roots.network.fx.MessageTreeCompleteFX;
import epicsquid.roots.ritual.RitualRegistry;
import epicsquid.roots.world.tree.WorldGenBigWildwoodTree;
import epicsquid.roots.world.tree.WorldGenWildwoodTree;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

import java.util.List;
import java.util.Random;

public class EntityRitualWildGrowth extends EntityRitualBase {

  protected static Random random = new Random();
  protected static final DataParameter<Integer> lifetime = EntityDataManager.createKey(EntityRitualWildGrowth.class, DataSerializers.VARINT);

  public EntityRitualWildGrowth(World worldIn) {
    super(worldIn);
    this.getDataManager().register(lifetime, RitualRegistry.ritual_overgrowth.getDuration() + 20);
  }

  @Override
  public void onUpdate() {
    getDataManager().set(lifetime, getDataManager().get(lifetime) - 1);
    getDataManager().setDirty(lifetime);
    if (getDataManager().get(lifetime) < 0) {
      setDead();
    }
    if (!world.isRemote) {
      if (this.ticksExisted % 400 == 0) {
        List<BlockPos> eligiblePositions = Util.getBlocksWithinRadius(world, getPosition(), 10, 20, 10, ModBlocks.wildroot);
        if (eligiblePositions.isEmpty()) return;

        BlockPos pos = eligiblePositions.get(random.nextInt(eligiblePositions.size()));
        generateTree(world, pos, world.getBlockState(pos), random);
      }
    }
  }

  @Override
  public DataParameter<Integer> getLifetime() {
    return lifetime;
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