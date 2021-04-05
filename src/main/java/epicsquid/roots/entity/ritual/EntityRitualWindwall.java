package epicsquid.roots.entity.ritual;

import epicsquid.roots.particle.ParticleUtil;
import epicsquid.roots.ritual.RitualRegistry;
import epicsquid.roots.ritual.RitualWindwall;
import epicsquid.roots.util.EntityUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.List;

public class EntityRitualWindwall extends EntityRitualBase {
  private RitualWindwall ritual;

  public EntityRitualWindwall(World worldIn) {
    super(worldIn);
    getDataManager().register(lifetime, RitualRegistry.ritual_windwall.getDuration() + 20);
    ritual = (RitualWindwall) RitualRegistry.ritual_windwall;
  }

  private void knockBack(Entity entityIn, float strength, double xRatio, double zRatio) {
    entityIn.isAirBorne = true;
    float f = MathHelper.sqrt(xRatio * xRatio + zRatio * zRatio);
    entityIn.motionX /= 2.0D;
    entityIn.motionZ /= 2.0D;
    entityIn.motionX -= xRatio / (double) f * (double) strength;
    entityIn.motionZ -= zRatio / (double) f * (double) strength;

    if (entityIn.onGround) {
      entityIn.motionY /= 2.0D;
      entityIn.motionY += (double) strength;

      if (entityIn.motionY > 0.4D) {
        entityIn.motionY = 0.4D;
      }
    }
  }

  @Override
  public void onUpdate() {
    super.onUpdate();

    float alpha = (float) Math.min(40, (RitualRegistry.ritual_windwall.getDuration() + 20) - getDataManager().get(lifetime)) / 40.0f;
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
        ParticleUtil.spawnParticleSpark(world, (float) posX, (float) posY, (float) posZ, 0.125f * (rand.nextFloat() - 0.5f), 0.0625f * (rand.nextFloat()), 0.125f * (rand.nextFloat() - 0.5f), 70, 70, 70, 1.0f * alpha, 1.0f + rand.nextFloat(), 160);
      }
    }
    if (this.ticksExisted % ritual.interval == 0) {
      List<Entity> entities = world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(posX - ritual.radius_x, posY - ritual.radius_y, posZ - ritual.radius_z, posX + ritual.radius_x, posY + ritual.radius_y, posZ + ritual.radius_z));
      for (Entity e : entities) {
        if (EntityUtil.isHostile(e, RitualRegistry.ritual_windwall) && (Math.pow((posX - e.posX), 2) + Math.pow((posY - e.posY), 2) + Math.pow((posZ - e.posZ), 2)) < ritual.distance) {
          if (!world.isRemote) {
            if (e instanceof EntityLivingBase) {
              ((EntityLivingBase) e).knockBack(this, ritual.knockback, posX - e.posX, posZ - e.posZ);
            } else {
              knockBack(e, ritual.knockback, posX - e.posX, posZ - e.posZ);
            }
            e.motionY = 0.4 + (e.height * 0.1);
          }
          if (world.isRemote) {
            for (int i = 0; i < 10; i++) {
              ParticleUtil.spawnParticleSmoke(world, (float) e.posX, (float) e.posY, (float) e.posZ, (float) e.motionX * rand.nextFloat() * 0.5f, (float) e.motionY * rand.nextFloat() * 0.5f, (float) e.motionZ * rand.nextFloat() * 0.5f, 0.65f, 0.65f, 0.65f, 0.15f, 12.0f + 24.0f * rand.nextFloat(), 80, false);
            }
          }
        }
      }
    }
  }
}