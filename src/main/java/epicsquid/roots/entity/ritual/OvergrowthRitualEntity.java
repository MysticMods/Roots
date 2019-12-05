package epicsquid.roots.entity.ritual;

import epicsquid.mysticallib.util.Util;
import epicsquid.roots.ritual.RitualOvergrowth;
import epicsquid.roots.ritual.RitualRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class OvergrowthRitualEntity extends BaseRitualEntity {
  private RitualOvergrowth ritual;

  public OvergrowthRitualEntity(EntityType<?> entityTypeIn, World worldIn) {
    super(entityTypeIn, worldIn);
    ritual = (RitualOvergrowth) RitualRegistry.ritual_overgrowth;
  }

/*  public OvergrowthRitualEntity(World worldIn) {
    super(worldIn);
  }*/

  @Override
  protected void registerData() {
    this.getDataManager().register(lifetime, RitualRegistry.ritual_overgrowth.getDuration() + 20);
  }

  @Override
  public void tick() {
    super.tick();

    if (!world.isRemote) {
      if (this.ticksExisted % ritual.interval == 0) {
        List<BlockPos> eligiblePositions = Util.getBlocksWithinRadius(world, getPosition(), ritual.radius_x, ritual.radius_y, ritual.radius_z, pos -> {
          if (world.isAirBlock(pos)) return false;
          BlockState state = world.getBlockState(pos);
          BlockState mossified = Blocks.MOSSY_COBBLESTONE.getDefaultState(); // MossConfig.mossConversion(state);
          return mossified != null && isAdjacentToWater(world, pos);
        });
        if (eligiblePositions.isEmpty()) return;

        BlockPos pos = eligiblePositions.get(Util.rand.nextInt(eligiblePositions.size()));
        world.setBlockState(pos, Blocks.MOSSY_COBBLESTONE.getDefaultState()); // Objects.requireNonNull(MossConfig.mossConversion(world.getBlockState(pos))));
        //PacketHandler.sendToAllTracking(new MessageOvergrowthEffectFX(pos.getX(), pos.getY(), pos.getZ()), this);
      }
    }
  }

  // TODO: MOVE THIS TO A UTILITY CLASS

  /**
   * Checks if the given block has water adjacent to it
   *
   * @return True if at least one side is touching a water source block
   */
  private boolean isAdjacentToWater(World world, BlockPos pos) {
    for (Direction facing : Direction.values()) {
      Block block = world.getBlockState(pos.offset(facing)).getBlock();
      if (block == Blocks.WATER) {
        return true;
      }
    }
    return false;
  }

}