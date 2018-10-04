package epicsquid.roots.entity.ritual;

import java.util.List;

import epicsquid.roots.particle.ParticleUtil;
import epicsquid.roots.ritual.RitualRegistry;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

public class EntityRitualLight extends EntityRitualBase {

  protected static final DataParameter<Integer> lifetime = EntityDataManager.createKey(EntityRitualLight.class, DataSerializers.VARINT);

  public EntityRitualLight(World worldIn) {
    super(worldIn);
    getDataManager().register(lifetime, RitualRegistry.ritual_light.getDuration() + 20);
  }

  @Override
  public void onUpdate() {
    ticksExisted++;
    float alpha = (float) Math.min(40, (RitualRegistry.ritual_light.getDuration() + 20) - getDataManager().get(lifetime)) / 40.0f;
    getDataManager().set(lifetime, getDataManager().get(lifetime) - 1);
    getDataManager().setDirty(lifetime);
    if (getDataManager().get(lifetime) < 0) {
      setDead();
    }
    if (world.isRemote && getDataManager().get(lifetime) > 0) {
      ParticleUtil.spawnParticleStar(world, (float) posX, (float) posY, (float) posZ, 0, 0, 0, 255, 255, 75, 0.5f * alpha, 20.0f, 40);
      if (rand.nextInt(5) == 0) {
        ParticleUtil.spawnParticleSpark(world, (float) posX, (float) posY, (float) posZ, 0.125f * (rand.nextFloat() - 0.5f), 0.0625f * (rand.nextFloat()),
            0.125f * (rand.nextFloat() - 0.5f), 255, 255, 75, 1.0f * alpha, 1.0f + rand.nextFloat(), 160);
      }
      if (rand.nextInt(2) == 0) {
        float pitch = rand.nextFloat() * 360.0f;
        float yaw = rand.nextFloat() * 360.0f;
        for (float i = 0; i < 3.5; i += 0.2f) {
          float tx = (float) posX + i * (float) Math.sin(Math.toRadians(yaw)) * (float) Math.sin(Math.toRadians(pitch));
          float ty = (float) posY + i * (float) Math.cos(Math.toRadians(pitch));
          float tz = (float) posZ + i * (float) Math.cos(Math.toRadians(yaw)) * (float) Math.sin(Math.toRadians(pitch));
          float coeff = i / 3.5f;
          ParticleUtil.spawnParticleGlow(world, tx, ty, tz, 0, 0, 0, 255, 255, 75, 0.5f * alpha * (1.0f - coeff), 18.0f * (1.0f - coeff), 20);
        }
      }
    }
    if (this.ticksExisted % 20 == 0) {
      if (world.getWorldInfo().isRaining()) {
        world.getWorldInfo().setRaining(false);
      }
      if (world.getWorldTime() % 24000 >= 0 && world.getWorldTime() % 24000 < 12000) {
        if (rand.nextInt(3) == 0) {
          world.setWorldTime(world.getWorldTime() - 1);
        }
      } else {
        world.setWorldTime(world.getWorldTime() + 1);
      }
      List<EntityLivingBase> entities = world
          .getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(posX - 15.5, posY - 15.5, posZ - 15.5, posX + 15.5, posY + 15.5, posZ + 15.5));
      for (EntityLivingBase e : entities) {
        if (e.isEntityUndead()) {
          e.attackEntityFrom(DamageSource.IN_FIRE, 2.0f);
          e.setFire(2);
          if (world.isRemote) {
            for (float i = 0; i < 16; i++) {
              ParticleUtil
                  .spawnParticleFiery(world, (float) e.posX + 0.5f * (rand.nextFloat() - 0.5f), (float) e.posY + e.height / 2.5f + (rand.nextFloat() - 0.5f),
                      (float) e.posZ + 0.5f * (rand.nextFloat() - 0.5f), 0.0625f * (rand.nextFloat() - 0.5f), 0.09375f * (rand.nextFloat()),
                      0.0625f * (rand.nextFloat() - 0.5f), 255.0f, 96.0f, 32.0f, 1.0f * alpha, 4.0f + 12.0f * rand.nextFloat(), 40);
            }
          }
        }
      }
    }
  }

  @Override
  public DataParameter<Integer> getLifetime() {
    return lifetime;
  }

}