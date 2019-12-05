package epicsquid.roots.entity.ritual;

import java.util.List;

import epicsquid.roots.particle.ParticleUtil;
import epicsquid.roots.ritual.RitualHeavyStorms;
import epicsquid.roots.ritual.RitualRegistry;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

public class HeavyStormsRitualEntity extends BaseRitualEntity {
  private RitualHeavyStorms ritual;

  public HeavyStormsRitualEntity(World worldIn) {
    super(worldIn);
    getDataManager().register(lifetime, RitualRegistry.ritual_heavy_storms.getDuration() + 20);
    ritual = (RitualHeavyStorms) RitualRegistry.ritual_heavy_storms;
  }

  @Override
  public void onUpdate() {
    super.onUpdate();
    float alpha = (float) Math.min(40, (RitualRegistry.ritual_heavy_storms.getDuration() + 20) - getDataManager().get(lifetime)) / 40.0f;

    if (world.isRemote && getDataManager().get(lifetime) > 0) {
      ParticleUtil.spawnParticleStar(world, (float) posX, (float) posY, (float) posZ, 0, 0, 0, 50, 50, 255, 0.5f * alpha, 20.0f, 40);
      if (rand.nextInt(5) == 0) {
        ParticleUtil.spawnParticleSpark(world, (float) posX, (float) posY, (float) posZ, 0.125f * (rand.nextFloat() - 0.5f), 0.0625f * (rand.nextFloat()),
            0.125f * (rand.nextFloat() - 0.5f), 50, 50, 255, 1.0f * alpha, 1.0f + rand.nextFloat(), 160);
      }
      for (float i = 0; i < 360; i += rand.nextFloat() * 90.0f) {
        float tx = (float) posX + 2.5f * (float) Math.sin(Math.toRadians(i));
        float ty = (float) posY;
        float tz = (float) posZ + 2.5f * (float) Math.cos(Math.toRadians(i));
        ParticleUtil.spawnParticleSmoke(world, tx, ty, tz, 0, 0, 0, 70, 70, 70, 0.25f * alpha, 14.0f, 80, false);
      }
      for (float i = 0; i < 360; i += rand.nextFloat() * 90.0f) {
        float tx = (float) posX + 3.75f * (float) Math.sin(Math.toRadians(i));
        float ty = (float) posY;
        float tz = (float) posZ + 3.75f * (float) Math.cos(Math.toRadians(i));
        ParticleUtil.spawnParticleSmoke(world, tx, ty, tz, 0, 0, 0, 70, 70, 70, 0.125f * alpha, 7.0f, 80, false);
      }
      for (float i = 0; i < 360; i += rand.nextFloat() * 90.0f) {
        float vx = 0.25f * (float) Math.sin(Math.toRadians(i));
        float vz = 0.25f * (float) Math.cos(Math.toRadians(i));
        float tx = (float) posX + 3.75f * (float) Math.sin(Math.toRadians(i));
        float ty = (float) posY;
        float tz = (float) posZ + 3.75f * (float) Math.cos(Math.toRadians(i));
        ParticleUtil.spawnParticleSmoke(world, tx, ty, tz, vx, 0, vz, 70, 70, 70, 0.125f * alpha, 7.0f, 80, false);
      }
    }
    if (this.ticksExisted % 20 == 0) {
      if (!world.getWorldInfo().isRaining()) {
        world.getWorldInfo().setRaining(true);
      }
      List<LivingEntity> entities = world
          .getEntitiesWithinAABB(LivingEntity.class, new AxisAlignedBB(posX - ritual.radius_x, posY - ritual.radius_y, posZ - ritual.radius_z, posX + ritual.radius_x, posY + ritual.radius_y, posZ + ritual.radius_z));
      for (LivingEntity e : entities) {
        if (e.isBurning()) {
          e.extinguish();
          if (world.isRemote) {
            for (float i = 0; i < 24; i++) {
              ParticleUtil
                  .spawnParticleGlow(world, (float) e.posX + 0.5f * (rand.nextFloat() - 0.5f), (float) e.posY + e.height / 2.5f + (rand.nextFloat() - 0.5f),
                      (float) e.posZ + 0.5f * (rand.nextFloat() - 0.5f), 0.0625f * (rand.nextFloat() - 0.5f), 0.009375f * (rand.nextFloat()),
                      0.0625f * (rand.nextFloat() - 0.5f), 50, 50, 255, 0.25f * alpha, 2.0f + 4.0f * rand.nextFloat(), 80);
            }
          }
        }
      }
    }
  }
}