package epicsquid.roots.entity.ritual;

import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.roots.mechanics.Growth;
import epicsquid.roots.network.fx.MessageRampantLifeInfusionFX;
import epicsquid.roots.particle.ParticleUtil;
import epicsquid.roots.ritual.RitualRegistry;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

@SuppressWarnings("deprecation")
public class EntityRitualGermination extends EntityRitualBase {
  public EntityRitualGermination(World worldIn) {
    super(worldIn);
    getDataManager().register(lifetime, RitualRegistry.ritual_germination.getDuration() + 20);
  }

  @Override
  public void onUpdate() {
    super.onUpdate();
    float alpha = (float) Math.min(40, (RitualRegistry.ritual_life.getDuration() + 20) - getDataManager().get(lifetime)) / 40.0f;

    if (world.isRemote && getDataManager().get(lifetime) > 0) {
      ParticleUtil.spawnParticleStar(world, (float) posX, (float) posY, (float) posZ, 0, 0, 0, 100, 255, 100, 0.5f * alpha, 20.0f, 40);
      if (rand.nextInt(5) == 0) {
        ParticleUtil.spawnParticleSpark(world, (float) posX, (float) posY, (float) posZ, 0.125f * (rand.nextFloat() - 0.5f), 0.0625f * (rand.nextFloat()),
            0.125f * (rand.nextFloat() - 0.5f), 100, 255, 100, 1.0f * alpha, 1.0f + rand.nextFloat(), 160);
      }
      for (float i = 0; i < 360; i += 72.0f) {
        double ang = ticksExisted % 360;
        float tx = (float) posX + 1.0f * (float) Math.sin(Math.toRadians(i + ang));
        float ty = (float) posY;
        float tz = (float) posZ + 1.0f * (float) Math.cos(Math.toRadians(i + ang));
        ParticleUtil.spawnParticleGlow(world, tx, ty, tz, 0, 0, 0, 100, 255, 100, 0.5f * alpha, 8.0f, 40);
      }
    }
    if (this.ticksExisted % 80 == 0) {
      List<BlockPos> positions = Growth.collect(world, getPosition(), 19, 19, 19);
      if (positions.isEmpty()) return;
      if (!world.isRemote) {
        for (int i = 0; i < 5; i++) {
          BlockPos pos = positions.get(world.rand.nextInt(positions.size()));
          IBlockState state = world.getBlockState(pos);
          int x = 0;
          if (state.getBlock() == Blocks.REEDS || state.getBlock() == Blocks.CACTUS) {
            x += 15;
          }
          for (int j = 0; j < 3 + x; j++) {
            state.getBlock().randomTick(world, pos, state, world.rand);
          }
          PacketHandler.sendToAllTracking(new MessageRampantLifeInfusionFX(pos.getX(), pos.getY(), pos.getZ()), this);
        }
      }
    }
  }
}
