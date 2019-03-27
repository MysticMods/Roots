package epicsquid.roots.entity.ritual;

import epicsquid.mysticallib.particle.particles.ParticleGlitter;
import epicsquid.mysticallib.proxy.ClientProxy;
import epicsquid.mysticallib.util.Util;
import epicsquid.roots.ritual.RitualRegistry;
import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

public class EntityRitualTransmutation extends EntityRitualBase {

  protected static Random random = new Random();
  protected static final DataParameter<Integer> lifetime = EntityDataManager.createKey(EntityRitualTransmutation.class, DataSerializers.VARINT);

  public EntityRitualTransmutation(World worldIn) {
    super(worldIn);
    this.getDataManager().register(lifetime, RitualRegistry.ritual_transmutation.getDuration() + 20);
  }

  @Override
  public void onUpdate() {
    int curLifetime = getDataManager().get(lifetime);
    getDataManager().set(lifetime, curLifetime - 1);
    getDataManager().setDirty(lifetime);
    if (getDataManager().get(lifetime) < 0) {
      setDead();
    }
    if (this.ticksExisted % 100 == 0) {
      List<BlockPos> eligiblePositions = Util.getBlocksWithinRadius(world, getPosition(), 16, 16, 8, (state) -> {
        Block block = state.getBlock();
        if (block == Blocks.DEADBUSH || block == Blocks.PUMPKIN) return true;

        if (block == Blocks.LOG) {
          return state.getValue(BlockOldLog.VARIANT) == BlockPlanks.EnumType.BIRCH;
        }

        return false;
      });
      if (eligiblePositions.isEmpty()) return;
      int index = random.nextInt(eligiblePositions.size());
      BlockPos pos = eligiblePositions.get(index);
      transmuteBlock(world, pos);
      eligiblePositions.remove(index);
    }
  }

  @Override
  public DataParameter<Integer> getLifetime() {
    return lifetime;
  }

  private void transmuteBlock(World world, BlockPos pos) {
    boolean didTransmute = false;
    IBlockState state = world.getBlockState(pos);
    Block block = state.getBlock();

    if (block == Blocks.PUMPKIN) {
      didTransmute = true;
      Block down = world.getBlockState(pos.down()).getBlock();
      if (down == Blocks.WATER || down == Blocks.FLOWING_WATER) {
        world.setBlockState(pos, Blocks.MELON_BLOCK.getDefaultState());
      } else if (world.getBlockState(pos.down()).getBlock() == Blocks.SAND){
        world.setBlockState(pos, Blocks.CACTUS.getDefaultState());
      } else {
        didTransmute = false;
      }
    } else if (block == Blocks.LOG) {
      world.setBlockState(pos, state.withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.JUNGLE));
      didTransmute = true;
    } else if (block == Blocks.DEADBUSH) {
      world.setBlockState(pos, Blocks.AIR.getDefaultState());
      ItemStack cocoaBeans = new ItemStack(Items.DYE, 1 + random.nextInt(2), 3);
      EntityItem entityBeans = new EntityItem(world, pos.getX(), pos.getY() + 0.5, pos.getZ(), cocoaBeans);
      world.spawnEntity(entityBeans);
      didTransmute = true;
    }

    if (didTransmute && world.isRemote) {
      for (int i = 0; i < 50; i++) {
        ClientProxy.particleRenderer
            .spawnParticle(world, Util.getLowercaseClassName(ParticleGlitter.class), pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                random.nextDouble() * 0.1 * (random.nextDouble() > 0.5 ? -1 : 1), random.nextDouble() * 0.1 * (random.nextDouble() > 0.5 ? -1 : 1),
                random.nextDouble() * 0.1 * (random.nextDouble() > 0.5 ? -1 : 1), 120, 0.607, 0.698 + random.nextDouble() * 0.05, 0.306, 1,
                random.nextDouble() + 0.5, random.nextDouble() * 2);
      }
    }
  }
}