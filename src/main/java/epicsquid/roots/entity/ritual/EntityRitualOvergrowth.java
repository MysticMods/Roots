package epicsquid.roots.entity.ritual;

import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.mysticallib.util.Util;
import epicsquid.roots.config.MossConfig;
import epicsquid.roots.network.fx.MessageOvergrowthEffectFX;
import epicsquid.roots.ritual.RitualOvergrowth;
import epicsquid.roots.ritual.RitualRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityRitualOvergrowth extends EntityRitualBase {
  private RitualOvergrowth ritual;

  public EntityRitualOvergrowth(World worldIn) {
    super(worldIn);
    this.getDataManager().register(lifetime, RitualRegistry.ritual_overgrowth.getDuration() + 20);
    ritual = (RitualOvergrowth) RitualRegistry.ritual_overgrowth;
  }

  @Override
  public void onUpdate() {
    super.onUpdate();

    if (!world.isRemote) {
      if (this.ticksExisted % ritual.interval == 0) {
        List<BlockPos> eligiblePositions = Util.getBlocksWithinRadius(world, getPosition(), ritual.radius_x, ritual.radius_y, ritual.radius_z, pos -> {
          if (world.isAirBlock(pos)) return false;
          BlockState state = world.getBlockState(pos);
          BlockState mossified = MossConfig.mossConversion(state);
          return mossified != null && isAdjacentToWater(world, pos);
        });
        if (eligiblePositions.isEmpty()) return;

        BlockPos pos = eligiblePositions.get(Util.rand.nextInt(eligiblePositions.size()));
        world.setBlockState(pos, Objects.requireNonNull(MossConfig.mossConversion(world.getBlockState(pos))));
        PacketHandler.sendToAllTracking(new MessageOvergrowthEffectFX(pos.getX(), pos.getY(), pos.getZ()), this);
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
    for (Direction facing : Direction.HORIZONTALS) {
      Block block = world.getBlockState(pos.offset(facing)).getBlock();
      if (block == Blocks.WATER || block == Blocks.FLOWING_WATER) {
        return true;
      }
    }
    return false;
  }

}