package epicsquid.roots.entity.ritual;

import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.mysticallib.util.Util;
import epicsquid.roots.advancements.Advancements;
import epicsquid.roots.block.groves.BlockGroveStone;
import epicsquid.roots.init.ModBlocks;
import epicsquid.roots.network.fx.MessageGroveCompleteFX;
import epicsquid.roots.particle.ParticleUtil;
import epicsquid.roots.ritual.RitualGroveSupplication;
import epicsquid.roots.ritual.RitualRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class EntityRitualGroveSupplication extends EntityRitualBase {
  private RitualGroveSupplication ritual;

  public EntityRitualGroveSupplication(World worldIn) {
    super(worldIn);
    getDataManager().register(lifetime, RitualRegistry.ritual_grove_supplication.getDuration() + 20);
    ritual = (RitualGroveSupplication) RitualRegistry.ritual_grove_supplication;
  }

  @Override
  public void onUpdate() {
    super.onUpdate();

    float alpha = (float) Math.min(40, (RitualRegistry.ritual_grove_supplication.getDuration() + 20) - getDataManager().get(lifetime)) / 40.0f;
    if (world.isRemote && getDataManager().get(lifetime) > 0) {
      ParticleUtil.spawnParticleStar(world, (float) posX, (float) posY, (float) posZ, 0, 0, 0, 70, 70, 70, 0.5f * alpha, 20.0f, 40);
      for (float i = 0; i < 360; i += 120) {
        float ang = (float) (ticksExisted % 360);
        float tx = (float) posX + 2.5f * (float) Math.sin(Math.toRadians(2.0f * (i + ang)));
        float ty = (float) posY + 0.5f * (float) Math.sin(Math.toRadians(4.0f * (i + ang)));
        float tz = (float) posZ + 2.5f * (float) Math.cos(Math.toRadians(2.0f * (i + ang)));
        ParticleUtil.spawnParticleStar(world, tx, ty, tz, 0, 0, 0, 70, 70, 70, 0.5f * alpha, 10.0f, 40);
      }
      if (rand.nextInt(5) == 0) {
        ParticleUtil.spawnParticleSpark(world, (float) posX, (float) posY, (float) posZ, 0.125f * (rand.nextFloat() - 0.5f), 0.0625f * (rand.nextFloat()),
            0.125f * (rand.nextFloat() - 0.5f), 70, 70, 70, 1.0f * alpha, 1.0f + rand.nextFloat(), 160);
      }
    }
    if (this.ticksExisted % ritual.interval == 0) {
      List<BlockPos> positions = Util.getBlocksWithinRadius(world, getPosition(), ritual.radius_x, ritual.radius_y, ritual.radius_z, ModBlocks.grove_stone);
      if (positions.isEmpty()) return;

      boolean didStuff = false;

      List<BlockPos> changed = new ArrayList<>();

      for (BlockPos pos : positions) {
        BlockState state = world.getBlockState(pos);
        if (state.getBlock() != ModBlocks.grove_stone) {
          continue;
        }

        if (state.get(BlockGroveStone.VALID)) {
          continue;
        }

        didStuff = true;
        changed.add(pos);

        if (!world.isRemote) {
          world.setBlockState(pos, state.with(BlockGroveStone.VALID, true));
          // -> How to handle this
          ServerPlayerEntity player = (ServerPlayerEntity) getPlayerEntity();
          if (player != null) {
            Advancements.GROVE_TRIGGER.trigger(player, null);
          }
        }
      }

      if (didStuff && !changed.isEmpty() && !world.isRemote) {
        MessageGroveCompleteFX message = new MessageGroveCompleteFX(changed);
        PacketHandler.sendToAllTracking(message, this);
      }
    }
  }
}