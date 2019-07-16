package epicsquid.roots.entity.ritual;

import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.mysticallib.util.Util;
import epicsquid.roots.network.fx.MessageOvergrowthEffectFX;
import epicsquid.roots.ritual.RitualRegistry;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class EntityRitualOvergrowth extends EntityRitualBase {

  protected static Random random = new Random();
  protected static final DataParameter<Integer> lifetime = EntityDataManager.createKey(EntityRitualOvergrowth.class, DataSerializers.VARINT);

  public EntityRitualOvergrowth(World worldIn) {
    super(worldIn);
    this.getDataManager().register(lifetime, RitualRegistry.ritual_overgrowth.getDuration() + 20);
  }

  @Override
  public void onUpdate() {
    super.onUpdate();
    int curLifetime = getDataManager().get(lifetime);
    getDataManager().set(lifetime, curLifetime - 1);
    getDataManager().setDirty(lifetime);
    if (getDataManager().get(lifetime) < 0) {
      setDead();
    }
    if (!world.isRemote) {
      if (this.ticksExisted % 150 == 0) {
        List<BlockPos> eligiblePositions = Util.getBlocksWithinRadius(world, getPosition(), 10, 20, 10, Blocks.COBBLESTONE).stream().filter((pos) -> isAdjacentToWater(world, pos)).collect(Collectors.toList());
        if (eligiblePositions.isEmpty()) return;

        BlockPos pos = eligiblePositions.get(random.nextInt(eligiblePositions.size()));
        world.setBlockState(pos, Blocks.MOSSY_COBBLESTONE.getDefaultState());
        PacketHandler.sendToAllTracking(new MessageOvergrowthEffectFX(pos.getX(), pos.getY(), pos.getZ()), this);
      }
    }
  }

  @Override
  public DataParameter<Integer> getLifetime() {
    return lifetime;
  }

  /**
   * Checks if the given block has water adjacent to it
   *
   * @return True if at least one side is touching a water source block
   */
  private boolean isAdjacentToWater(World world, BlockPos pos) {
    for (EnumFacing facing : EnumFacing.HORIZONTALS) {
      Block block = world.getBlockState(pos.offset(facing)).getBlock();
      if (block == Blocks.WATER || block == Blocks.FLOWING_WATER) {
        return true;
      }
    }
    return false;
  }

}