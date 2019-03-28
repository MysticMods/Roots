package epicsquid.roots.entity.ritual;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import epicsquid.mysticallib.particle.particles.ParticleGlitter;
import epicsquid.mysticallib.proxy.ClientProxy;
import epicsquid.mysticallib.util.Util;
import epicsquid.roots.ritual.RitualRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

public class EntityRitualOvergrowth extends EntityRitualBase {

  protected static Random random = new Random();
  protected static final DataParameter<Integer> lifetime = EntityDataManager.createKey(EntityRitualOvergrowth.class, DataSerializers.VARINT);

  public EntityRitualOvergrowth(World worldIn) {
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
    if (this.ticksExisted % 400 == 0) {
      List<BlockPos> eligiblePositions = Util.getBlocksWithinRadius(world, getPosition(), 10, 20, 10, Blocks.COBBLESTONE).stream().filter((pos) -> isAdjacentToWater(world, pos)).collect(Collectors.toList());
      if (eligiblePositions.isEmpty()) return;

      BlockPos pos = eligiblePositions.get(random.nextInt(eligiblePositions.size()));
      overgrowBlock(world, pos, world.getBlockState(pos));
    }
  }

  @Override
  public DataParameter<Integer> getLifetime() {
    return lifetime;
  }

  /**
   * Has all valid possibilities for changing the block from a normal block to an overgrown one.
   * TODO add a way for crafttweaker to do this
   * @return True if a block is overgrown
   */
  private boolean overgrowBlock(World world, BlockPos pos, IBlockState state) {
    boolean didOvergrow = false;
    if (state.getBlock() == Blocks.COBBLESTONE && isAdjacentToWater(world, pos)) {
      world.setBlockState(pos, Blocks.MOSSY_COBBLESTONE.getDefaultState());
      didOvergrow = true;
    }

    if (didOvergrow && world.isRemote) {
      for (int i = 0; i < 50; i++) {
        ClientProxy.particleRenderer
            .spawnParticle(world, Util.getLowercaseClassName(ParticleGlitter.class), pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                random.nextDouble() * 0.1 * (random.nextDouble() > 0.5 ? -1 : 1), random.nextDouble() * 0.1 * (random.nextDouble() > 0.5 ? -1 : 1),
                random.nextDouble() * 0.1 * (random.nextDouble() > 0.5 ? -1 : 1), 120, 0.607, 0.698 + random.nextDouble() * 0.05, 0.306, 1,
                random.nextDouble() + 0.5, random.nextDouble() * 2);
      }
    }

    return didOvergrow;
  }

  /**
   * Checks if the given block has water adjacent to it
   *
   * @return True if at least one side is touching a water source block
   */
  private boolean isAdjacentToWater(World world, BlockPos pos) {
    return world.getBlockState(pos.offset(EnumFacing.EAST)).getBlock() == Blocks.WATER
        || world.getBlockState(pos.offset(EnumFacing.NORTH)).getBlock() == Blocks.WATER
        || world.getBlockState(pos.offset(EnumFacing.WEST)).getBlock() == Blocks.WATER
        || world.getBlockState(pos.offset(EnumFacing.SOUTH)).getBlock() == Blocks.WATER;
  }

}