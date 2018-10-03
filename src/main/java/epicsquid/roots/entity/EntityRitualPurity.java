package epicsquid.roots.entity;

import java.util.List;

import epicsquid.roots.ritual.RitualRegistry;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

public class EntityRitualPurity extends EntityRitualBase {

  public EntityRitualPurity(World worldIn) {
    super(worldIn);
    getDataManager().register(lifetime, RitualRegistry.ritual_purity.duration + 20);
  }

  @Override
  public void onUpdate() {
    ticksExisted++;
    this.posX = x;
    this.posY = y;
    this.posZ = z;
    float alpha = (float) Math.min(40, (RitualRegistry.ritual_life.duration + 20) - getDataManager().get(lifetime)) / 40.0f;
    getDataManager().set(lifetime, getDataManager().get(lifetime) - 1);
    getDataManager().setDirty(lifetime);
    if (getDataManager().get(lifetime) < 0) {
      setDead();
    }
    if (world.isRemote && getDataManager().get(lifetime) > 0) {
      //todo: fix particle when available | ParticleUtil.spawnParticleStar(world, (float)posX, (float)posY, (float)posZ, 0, 0, 0, 100, 255, 100, 0.5f*alpha, 20.0f, 40);
      if (rand.nextInt(5) == 0) {
        //todo: fix particle when available | ParticleUtil.spawnParticleSpark(world, (float)posX, (float)posY, (float)posZ, 0.125f*(rand.nextFloat()-0.5f), 0.0625f*(rand.nextFloat()), 0.125f*(rand.nextFloat()-0.5f), 100, 255, 100, 1.0f*alpha, 1.0f+rand.nextFloat(), 160);
      }
      for (float i = 0; i < 360; i += 72.0f) {
        double ang = ticksExisted % 360;
        float tx = (float) posX + 1.0f * (float) Math.sin(Math.toRadians(i + ang));
        float ty = (float) posY;
        float tz = (float) posZ + 1.0f * (float) Math.cos(Math.toRadians(i + ang));
        //todo: fix particle when available | ParticleUtil.spawnParticleGlow(world, tx, ty, tz, 0, 0, 0, 100, 255, 100, 0.5f*alpha, 8.0f, 40);
      }
    }
    if (this.ticksExisted % 20 == 0) {
      List<EntityLivingBase> entities = world
          .getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(posX - 15.5, posY - 15.5, posZ - 15.5, posX + 15.5, posY + 15.5, posZ + 15.5));
      for (EntityLivingBase e : entities) {
        for (PotionEffect potionEffect : e.getActivePotionEffects()) {
          if (potionEffect.getPotion().isBadEffect()) {
            e.removePotionEffect(potionEffect.getPotion());
          }
        }
        if (world.isRemote) {
          for (float i = 0; i < 8; i++) {
            //todo: fix particle when available | ParticleUtil.spawnParticleStar(world, (float)e.posX+0.5f*(rand.nextFloat()-0.5f), (float)e.posY+e.height/2.5f+(rand.nextFloat()-0.5f), (float)e.posZ+0.5f*(rand.nextFloat()-0.5f), 0.125f*(rand.nextFloat()-0.5f), 0.01875f*(rand.nextFloat()), 0.125f*(rand.nextFloat()-0.5f), 100, 255, 100, 1.0f*alpha, 1.0f+2.0f*rand.nextFloat(), 40);
          }
        }
      }
    }
  }
}
